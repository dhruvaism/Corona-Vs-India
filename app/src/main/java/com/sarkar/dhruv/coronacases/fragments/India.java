package com.sarkar.dhruv.coronacases.fragments;


import android.content.Context;
import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.card.MaterialCardView;
import com.richpath.RichPath;
import com.richpath.RichPathView;
import com.richpathanimator.RichPathAnimator;
import com.sarkar.dhruv.coronacases.MainActivity;
import com.sarkar.dhruv.coronacases.R;
import com.sarkar.dhruv.coronacases.adapters.StateAdapter;
import com.sarkar.dhruv.coronacases.handlers.HttpRequest;
import com.sarkar.dhruv.coronacases.models.DistrictModel;
import com.sarkar.dhruv.coronacases.models.StateModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.sarkar.dhruv.coronacases.MainActivity.activity;

import static com.sarkar.dhruv.coronacases.MainActivity.formatter;
import static com.sarkar.dhruv.coronacases.MainActivity.stateList;
import static com.sarkar.dhruv.coronacases.MainActivity.titleLayout;
import static com.sarkar.dhruv.coronacases.constants.Constant.Active;
import static com.sarkar.dhruv.coronacases.constants.Constant.Confirmed;
import static com.sarkar.dhruv.coronacases.constants.Constant.DISTRICT_DATA;
import static com.sarkar.dhruv.coronacases.constants.Constant.Deaths;
import static com.sarkar.dhruv.coronacases.constants.Constant.Deltaconfirmed;
import static com.sarkar.dhruv.coronacases.constants.Constant.Deltadeaths;
import static com.sarkar.dhruv.coronacases.constants.Constant.Deltarecovered;
import static com.sarkar.dhruv.coronacases.constants.Constant.Lastupdatedtime;
import static com.sarkar.dhruv.coronacases.constants.Constant.Recovered;
import static com.sarkar.dhruv.coronacases.constants.Constant.STATEWISE;
import static com.sarkar.dhruv.coronacases.constants.Constant.State;
import static com.sarkar.dhruv.coronacases.constants.Constant.Statecode;
import static com.sarkar.dhruv.coronacases.constants.Constant.Statenotes;


public class India extends Fragment {

    public static final String id ="india";
    RecyclerView stateRecyclerView;
    StateModel India,IndiaCopy;
    RichPathView map;
    private View context;
    public static StateAdapter stateAdapter;

    ShimmerFrameLayout shimmerFrameLayout;
    LinearLayoutCompat contentLayout;
    public India() {
        // Required empty public constructor
    }
    final Handler handler = new Handler();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.title.setText("Covid19 Vs India");

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_india, container, false);
        shimmerFrameLayout = view.findViewById(R.id.shimmer_view_container);
        contentLayout = view.findViewById(R.id.content_layout);
        context =  view;
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        contentLayout.setVisibility(View.GONE);
        shimmerFrameLayout.startShimmerAnimation();
        if(stateList==null) {
            new HttpHandler().execute();
        }else {
            India = IndiaCopy;
            handler.post(new Runnable() {
                @Override
                public void run() {
                    setAllCases();
                    setMap();
                    setStateRecyclerView();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    contentLayout.setVisibility(View.VISIBLE);
                    shimmerFrameLayout.stopShimmerAnimation();
                }
            });
        }
        return view;

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setMap() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float size = displayMetrics.widthPixels;
        map = context.findViewById(R.id.india_map);
//        ViewGroup.LayoutParams layoutParams = map.getLayoutParams();
//        layoutParams.height=(int)size;
        for(int i=0;i<32;i++) {
            final RichPath state = map.findRichPathByIndex(i);
            state.setStrokeColor(activity.getColor(R.color.fifty_k));
            state.setFillColor(activity.getColor(R.color.map_default_color));
            state.setOnPathClickListener(new RichPath.OnPathClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(RichPath richPath) {
                    setColor(state);
                    updateIndia(state);
                    setAllCases();
                    MainActivity.title.setText("Covid19 Vs "+state.getName());
                    RichPathAnimator.animate(richPath)
                            .interpolator(new DecelerateInterpolator())
                            .rotation(1, -1, 0, 0)
                            .startDelay(50)
                            .duration(1000)
                            .start();
                }
            });
        }
    }

    private void updateIndia(RichPath state) {
        for(StateModel stateModel:stateList) {
            if(stateModel.getState().equals(state.getName())) {
                India = stateModel;
                break;
            }
        }
    }

    private void resetColor(RichPath state) {
        for(int i=0;i<32;i++) {
            RichPath richPath = map.findRichPathByIndex(i);
            if(!richPath.getName().equals(state.getName())) {
                richPath.setFillColor(getResources().getColor(R.color.map_default_color));
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setColor(RichPath state) {
                int cnf_case = getConfirmedCases(state.getName());
                if(cnf_case>=0 && cnf_case<100) {
                    state.setFillColor(activity.getColor(R.color.less_hundred));
                } else if(cnf_case>=100 && cnf_case<500) {
                    state.setFillColor(activity.getColor(R.color.less_five_hundred));
                }else if(cnf_case>=500 && cnf_case<1000) {
                    state.setFillColor(activity.getColor(R.color.less_one_k));
                }else if(cnf_case>=1000 && cnf_case<2500) {
                    state.setFillColor(activity.getColor(R.color.less_two_k_fifty));
                } else if(cnf_case>=2500 && cnf_case<5000) {
                    state.setFillColor(activity.getColor(R.color.less_five_k));
                } else if(cnf_case>=5000 && cnf_case<10000) {
                    state.setFillColor(activity.getColor(R.color.less_ten_k));
                }else if(cnf_case>=10000 && cnf_case<20000) {
                    state.setFillColor(activity.getColor(R.color.ten_k));
                }else if(cnf_case>=20000 && cnf_case<30000) {
                    state.setFillColor(activity.getColor(R.color.tweenty_k));
                }else if(cnf_case>=30000 && cnf_case<40000) {
                    state.setFillColor(activity.getColor(R.color.thirty_k));
                }else if(cnf_case>=40000 && cnf_case<50000) {
                    state.setFillColor(activity.getColor(R.color.forty_k));
                }else {
                    state.setFillColor(activity.getColor(R.color.fifty_k));
                }

    }





    private int getConfirmedCases(String statename) {
        int cnf_case=0;
        for(int i=0;i<stateList.size();i++) {
            if(stateList.get(i).getState().equals(statename)) {
                cnf_case = Integer.parseInt(stateList.get(i).getConfirmed());
                break;
            }
        }
        return cnf_case;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setAllCases() {

        TextView time = context.findViewById(R.id.time);
        time.setText("Last updated at: "+India.getTime());
        time.setSelected(true);
        MaterialCardView confirmed,recovered,deaths,active;
        confirmed = context.findViewById(R.id.confirmed_case);
        recovered =  context.findViewById(R.id.recovered_case);
        deaths =  context.findViewById(R.id.deaths_case);
        active =  context.findViewById(R.id.active_case);
        confirmed.setBackgroundTintList(ColorStateList.valueOf(activity.getColor(R.color.confirmed_back)));
        recovered.setBackgroundTintList(ColorStateList.valueOf(activity.getColor(R.color.recovered_back)));
        deaths.setBackgroundTintList(ColorStateList.valueOf(activity.getColor(R.color.deaths_back)));
        active.setBackgroundTintList(ColorStateList.valueOf(activity.getColor(R.color.active_back)));


        TextView c_no_case,c_no_new_case,c_type_case;
        TextView r_no_case,r_no_new_case,r_type_case;
        TextView d_no_case,d_no_new_case,d_type_case;
        TextView a_no_case,a_no_new_case,a_type_case;

        c_no_case = confirmed.findViewById(R.id.no_case);
        c_no_new_case = confirmed.findViewById(R.id.no_new_case);
        c_type_case = confirmed.findViewById(R.id.case_type);

        r_no_case = recovered.findViewById(R.id.no_case);
        r_no_new_case = recovered.findViewById(R.id.no_new_case);
        r_type_case = recovered.findViewById(R.id.case_type);

        d_no_case = deaths.findViewById(R.id.no_case);
        d_no_new_case = deaths.findViewById(R.id.no_new_case);
        d_type_case = deaths.findViewById(R.id.case_type);

        a_no_case = active.findViewById(R.id.no_case);
        a_no_new_case = active.findViewById(R.id.no_new_case);
        a_type_case = active.findViewById(R.id.case_type);


        r_no_case.setTextColor(activity.getColor(R.color.recovered));
        r_type_case.setText(activity.getString(R.string.recovered_text));
        r_no_case.setText(formatter(India.getRecovered()));
        r_no_new_case.setText(formatter(India.getDeltarecovered()));

        d_no_case.setTextColor(activity.getColor(R.color.deaths));
        d_type_case.setText(activity.getString(R.string.deaths_text));
        d_no_case.setText(formatter(India.getDeaths()));
        d_no_new_case.setText(formatter(India.getDeltadeaths()));

        a_no_case.setTextColor(activity.getColor(R.color.active));
        a_type_case.setText(activity.getString(R.string.active_text));
        a_no_case.setText(formatter(India.getActive()));
        a_no_new_case.setText("---");

        c_no_case.setText(formatter(India.getConfirmed()));
        c_no_new_case.setText(formatter(India.getDeltaconfirmed()));

        ImageView new_case_icon_active = active.findViewById(R.id.new_case_icon);
        new_case_icon_active.setVisibility(View.GONE);

        ImageView a_ci,r_ci,d_ci;
        a_ci = active.findViewById(R.id.corona_icon);
        r_ci = recovered.findViewById(R.id.corona_icon);
        d_ci = deaths.findViewById(R.id.corona_icon);

        a_ci.setImageDrawable(activity.getDrawable(R.drawable.active_icon));
        r_ci.setImageDrawable(activity.getDrawable(R.drawable.recovered_icon));
        d_ci.setImageDrawable(activity.getDrawable(R.drawable.deaths_icon));



    }

    private void setHeader() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        MainActivity.activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float size = displayMetrics.widthPixels;
        TextView stateHeader = context.findViewById(R.id.state_header);
        TextView confHeader = context.findViewById(R.id.conf_header);
        TextView activeHeader = context.findViewById(R.id.active_header_);
        TextView recovHeader = context.findViewById(R.id.recov_header);
        TextView deathsHeader = context.findViewById(R.id.deaths_header);
        stateHeader.getLayoutParams().width = (int)size*2/5;
        confHeader.getLayoutParams().width = (int)size*3/20;
        activeHeader.getLayoutParams().width = (int)size*3/20;
        recovHeader.getLayoutParams().width = (int)size*3/20;
        deathsHeader.getLayoutParams().width = (int)size*3/20;

    }
    private void setStateRecyclerView() {
        setHeader();
        stateRecyclerView = context.findViewById(R.id.states_wise_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity ,RecyclerView.VERTICAL,false);
        stateAdapter = new StateAdapter(activity,stateList);
        stateRecyclerView.hasFixedSize();
        stateRecyclerView.setLayoutManager(linearLayoutManager);
        stateRecyclerView.setAdapter(stateAdapter);


    }


    private class HttpHandler extends AsyncTask<Void,Void,Void> {


        @Override
        protected Void doInBackground(Void... voids) {
            HttpRequest httpRequest = HttpRequest.getInstance();
            JSONObject stateWise = httpRequest.getStateWise();
            JSONObject districtWise = httpRequest.getDistrictWise();
            JSONObject zones = httpRequest.getZones();
            try {
                if(stateWise!=null && districtWise!=null && zones!=null) {
                    setStateList(stateWise,districtWise,zones);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(stateList!=null && context!=null) {

                setAllCases();
                setMap();
                setStateRecyclerView();
                shimmerFrameLayout.setVisibility(View.GONE);
                contentLayout.setVisibility(View.VISIBLE);
                shimmerFrameLayout.stopShimmerAnimation();

            }else {
                Toast.makeText(activity
                        , "Check Internet Connection and try again", Toast.LENGTH_SHORT).show();
            }
        }
    }



    private void setStateList(JSONObject stateWise,JSONObject districtWise,JSONObject zones) throws JSONException {
        stateList = new ArrayList<>();
        JSONArray stateWiseJSONArray = stateWise.getJSONArray(STATEWISE);
        for(int i=0;i<stateWiseJSONArray.length();i++) {
            JSONObject stateJsonObj = stateWiseJSONArray.getJSONObject(i);
            StateModel stateModel = new StateModel(stateJsonObj.getString(Active),
                    stateJsonObj.getString(Confirmed),
                    stateJsonObj.getString(Deaths),
                    stateJsonObj.getString(Deltaconfirmed),
                    stateJsonObj.getString(Deltadeaths),
                    stateJsonObj.getString(Deltarecovered),
                    stateJsonObj.getString(Lastupdatedtime),
                    stateJsonObj.getString(Recovered),
                    stateJsonObj.getString(State),
                    stateJsonObj.getString(Statecode),
                    stateJsonObj.getString(Statenotes),
                    stateJsonObj.getString("lastupdatedtime")
            );

            if(i==0) {
                India = stateModel;
                IndiaCopy = India;
            }else {
                ArrayList<DistrictModel> districtModels = findDistrictModelsByState(stateJsonObj.getString(State),districtWise,zones);
                Collections.sort(districtModels);
                stateModel.setDistrictModels(districtModels);
                stateList.add(stateModel);
            }


        }
    }

    private ArrayList<DistrictModel> findDistrictModelsByState(String state,JSONObject districtWise,JSONObject zones) throws JSONException {
        ArrayList<DistrictModel> districtModels = new ArrayList<>();
        JSONObject DistrictWise = districtWise.getJSONObject(state).getJSONObject(DISTRICT_DATA);
        Iterator x = DistrictWise.keys();
        JSONArray DistrictJA = new JSONArray();
        ArrayList<String> districtList = new ArrayList<>();
        while (x.hasNext()){
            String key = (String) x.next();
            DistrictJA.put(DistrictWise.get(key));
            districtList.add(key);
        }

        for(int i=0;i<DistrictJA.length();i++) {
            String zone = getZoneByDistrict(zones,districtList.get(i));
            DistrictModel districtModel = new DistrictModel(districtList.get(i),
                    DistrictJA.getJSONObject(i).getString(Confirmed),
                    DistrictJA.getJSONObject(i).getString(Active),
                    DistrictJA.getJSONObject(i).getString(Recovered),
                    DistrictJA.getJSONObject(i).getString("deceased"),
                    zone);

            districtModels.add(districtModel);
        }
        return districtModels;
    }

    private String getZoneByDistrict(JSONObject zones, String district) throws JSONException {
        String ans = "";
        JSONArray zoneArray = zones.getJSONArray("zones");
        for(int i=0;i<zoneArray.length();i++) {
            if(zoneArray.getJSONObject(i).get("district").equals(district)) {
                ans = zoneArray.getJSONObject(i).getString("zone");
                break;
            }
        }
        return ans;
    }





}
