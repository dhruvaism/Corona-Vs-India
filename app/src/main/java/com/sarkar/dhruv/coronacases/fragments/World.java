package com.sarkar.dhruv.coronacases.fragments;


import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PointF;
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
import android.text.method.Touch;
import android.util.DisplayMetrics;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.card.MaterialCardView;
import com.richpath.RichPath;
import com.richpath.RichPathView;
import com.richpathanimator.RichPathAnimator;
import com.sarkar.dhruv.coronacases.CustomScrollview;
import com.sarkar.dhruv.coronacases.MainActivity;
import com.sarkar.dhruv.coronacases.R;
import com.sarkar.dhruv.coronacases.adapters.CountryAdapter;
import com.sarkar.dhruv.coronacases.adapters.StateAdapter;
import com.sarkar.dhruv.coronacases.handlers.HttpRequest;
import com.sarkar.dhruv.coronacases.models.CountryModel;
import com.sarkar.dhruv.coronacases.models.GlobalModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;

import static com.sarkar.dhruv.coronacases.MainActivity.activity;
import static com.sarkar.dhruv.coronacases.MainActivity.countryModels;
import static com.sarkar.dhruv.coronacases.MainActivity.formatter;


public class World extends Fragment implements RichPath.OnPathClickListener {
    public static final String id ="world";

    View view;
    private RichPathView world;
    ShimmerFrameLayout shimmerFrameLayout;
    LinearLayoutCompat contentLayout;
    GlobalModel globalModel = GlobalModel.getInstance();
    GlobalModel globalModelCopy;
    RecyclerView countryRV;

    final Handler handler = new Handler();
    public World() {}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.title.setText("Covid19 Vs World");

    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_world, container, false);
        this.view = view;
        shimmerFrameLayout = view.findViewById(R.id.shimmer_view_container);
        contentLayout = view.findViewById(R.id.content_layout);
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        contentLayout.setVisibility(View.GONE);
        shimmerFrameLayout.startShimmerAnimation();
        if(countryModels==null) {
            new HttpHandler().execute();
        }else {
            globalModel = globalModelCopy;
           handler.post(new Runnable() {
               @Override
               public void run() {
                   setAllCases();
                   setCountryRecyclerView();
                   setMap();
                   shimmerFrameLayout.setVisibility(View.GONE);
                   contentLayout.setVisibility(View.VISIBLE);
                   shimmerFrameLayout.stopShimmerAnimation();
               }
           });
        }
        return view;
    }


    private void setClickEvent() {
        int noOfCountry = world.findAllRichPaths().length;
        for(int i=0;i<noOfCountry;i++) {
            RichPath country = world.findRichPathByIndex(i);
            country.setStrokeWidth(1);
            country.setStrokeColor(activity.getResources().getColor(R.color.fifty_k));

        }
    }

    private void setMap() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        world = view.findViewById(R.id.world_map);
        float size = displayMetrics.widthPixels;
        ViewGroup.LayoutParams layoutParams = world.getLayoutParams();
        layoutParams.height=(int)size*2/3;
        world.setOnPathClickListener(this);
        setClickEvent();
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(RichPath richPath) {
            RichPathAnimator.animate(richPath)
                    .interpolator(new DecelerateInterpolator())
                    .rotation(1, -1, 0, 0)
                    .startDelay(50)
                    .duration(1000)
                    .start();

             if(updateGlobalModel(richPath.getName())) {
                 setColor(richPath);
                 MainActivity.title.setText("Covid19 Vs " +globalModel.country);
                 setAllCases();
             }

    }
    private void resetColor(RichPath country) {
        for(int i=0;i<32;i++) {
            RichPath richPath = world.findRichPathByIndex(i);
            if(!richPath.getName().equals(country.getName())) {
                richPath.setFillColor(getResources().getColor(R.color.map_default_color));
            }
        }
    }


    private boolean updateGlobalModel(String code) {
        boolean ans = false;
        for(int i=0;i<countryModels.size();i++) {
            if(countryModels.get(i).getCountryCode().equals(code)) {
                globalModel.setCountry(countryModels.get(i).getCountry());
                globalModel.setTotalCnf(countryModels.get(i).getTotalCnf());
                globalModel.setNewCnf(countryModels.get(i).getNewConfirmed());
                globalModel.setTotalRecov(countryModels.get(i).getTotalRecov());
                globalModel.setNewRecov(countryModels.get(i).getNewRecov());
                globalModel.setTotalDeaths(countryModels.get(i).getTotalDths());
                globalModel.setNewDeaths(countryModels.get(i).getNewDeaths());
                ans = true;
                break;
            }
        }
        return ans;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setAllCases() {
        TextView time = view.findViewById(R.id.time);
        time.setText("Last updated at: "+countryModels.get(0).getDate());
        time.setSelected(true);
        MaterialCardView confirmed,recovered,deaths,active;
        confirmed = view.findViewById(R.id.confirmed_case);
        recovered =  view.findViewById(R.id.recovered_case);
        deaths =  view.findViewById(R.id.deaths_case);
        confirmed.setBackgroundTintList(ColorStateList.valueOf(activity.getColor(R.color.confirmed_back)));
        recovered.setBackgroundTintList(ColorStateList.valueOf(activity.getColor(R.color.recovered_back)));
        deaths.setBackgroundTintList(ColorStateList.valueOf(activity.getColor(R.color.deaths_back)));


        TextView c_no_case,c_no_new_case,c_type_case;
        TextView r_no_case,r_no_new_case,r_type_case;
        TextView d_no_case,d_no_new_case,d_type_case;

        c_no_case = confirmed.findViewById(R.id.no_case);
        c_no_new_case = confirmed.findViewById(R.id.no_new_case);
        c_type_case = confirmed.findViewById(R.id.case_type);

        r_no_case = recovered.findViewById(R.id.no_case);
        r_no_new_case = recovered.findViewById(R.id.no_new_case);
        r_type_case = recovered.findViewById(R.id.case_type);

        d_no_case = deaths.findViewById(R.id.no_case);
        d_no_new_case = deaths.findViewById(R.id.no_new_case);
        d_type_case = deaths.findViewById(R.id.case_type);


        r_no_case.setTextColor(activity.getColor(R.color.recovered));
        r_type_case.setText(activity.getString(R.string.recovered_text));
        r_type_case.setTextSize(10);
        r_no_case.setText(formatter(globalModel.totalRecov));
        r_no_case.setSelected(true);
        r_no_case.setTextSize(11);
        r_no_new_case.setText(formatter(globalModel.newRecov));

        d_no_case.setTextColor(activity.getColor(R.color.deaths));
        d_type_case.setText(activity.getString(R.string.deaths_text));
        d_type_case.setTextSize(10);
        c_type_case.setTextSize(10);
        d_no_case.setText(formatter(globalModel.totalDeaths));
        d_no_case.setSelected(true);
        d_no_case.setTextSize(11);
        d_no_new_case.setText(formatter(globalModel.newDeaths));


        c_no_case.setText(formatter(globalModel.totalCnf));
        c_no_new_case.setText(formatter(globalModel.newCnf));
        c_no_case.setSelected(true);
        c_no_case.setTextSize(11);


        ImageView r_ci,d_ci;
        r_ci = recovered.findViewById(R.id.corona_icon);
        d_ci = deaths.findViewById(R.id.corona_icon);

        r_ci.setImageDrawable(activity.getDrawable(R.drawable.recovered_icon));
        d_ci.setImageDrawable(activity.getDrawable(R.drawable.deaths_icon));



    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setColor(RichPath country) {
                int cnf_case = getConfirmedCases(country.getName());
                if(cnf_case>=0 && cnf_case<100) {
                    country.setFillColor(activity.getColor(R.color.less_hundred));
                } else if(cnf_case>=100 && cnf_case<500) {
                    country.setFillColor(activity.getColor(R.color.less_five_hundred));
                }else if(cnf_case>=500 && cnf_case<1000) {
                    country.setFillColor(activity.getColor(R.color.less_one_k));
                }else if(cnf_case>=1000 && cnf_case<2500) {
                    country.setFillColor(activity.getColor(R.color.less_two_k_fifty));
                } else if(cnf_case>=2500 && cnf_case<5000) {
                    country.setFillColor(activity.getColor(R.color.less_five_k));
                } else if(cnf_case>=5000 && cnf_case<10000) {
                    country.setFillColor(activity.getColor(R.color.less_ten_k));
                }else if(cnf_case>=10000 && cnf_case<20000) {
                    country.setFillColor(activity.getColor(R.color.ten_k));
                }else if(cnf_case>=20000 && cnf_case<30000) {
                    country.setFillColor(activity.getColor(R.color.tweenty_k));
                }else if(cnf_case>=30000 && cnf_case<40000) {
                    country.setFillColor(activity.getColor(R.color.thirty_k));
                }else if(cnf_case>=50000 && cnf_case<100000) {
                    country.setFillColor(activity.getColor(R.color.forty_k));
                }else {
                    country.setFillColor(activity.getColor(R.color.fifty_k));
                }


    }


    private int getConfirmedCases(String countryCode) {
        int cnf_case=0;
        for(int i=0;i<countryModels.size();i++) {
            if(countryModels.get(i).getCountryCode().equals(countryCode)) {
                cnf_case = Integer.parseInt(countryModels.get(i).getTotalCnf());
                break;
            }
        }
        return cnf_case;
    }

    private void setHeader() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        MainActivity.activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float size = displayMetrics.widthPixels;
        TextView stateHeader = view.findViewById(R.id.state_header);
        TextView confHeader = view.findViewById(R.id.conf_header);
        TextView recovHeader = view.findViewById(R.id.recov_header);
        TextView deathsHeader = view.findViewById(R.id.deaths_header);
        stateHeader.getLayoutParams().width = (int)size*2/5;
        confHeader.getLayoutParams().width = (int)size*4/20;
        recovHeader.getLayoutParams().width = (int)size*4/20;
        deathsHeader.getLayoutParams().width = (int)size*4/20;

    }

    private void setCountryRecyclerView() {
        setHeader();
        countryRV = view.findViewById(R.id.country_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, RecyclerView.VERTICAL,false);
        CountryAdapter countryAdapter = new CountryAdapter(activity,countryModels);
        countryRV.hasFixedSize();
        countryRV.setLayoutManager(linearLayoutManager);
        countryRV.setAdapter(countryAdapter);

    }


    private class HttpHandler extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpRequest httpRequest = HttpRequest.getInstance();
            JSONObject countryWise = httpRequest.getCountryWise();

            if(countryWise!=null) {
                try {
                    setCountryList(countryWise);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(countryModels!=null) {
                setAllCases();
                setCountryRecyclerView();
                setMap();
                shimmerFrameLayout.setVisibility(View.GONE);
                contentLayout.setVisibility(View.VISIBLE);
                shimmerFrameLayout.stopShimmerAnimation();

            }else {
                Toast.makeText(activity, "Check Internet Connection and try again", Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void setCountryList(JSONObject countryWise) throws JSONException {
          JSONObject gm = countryWise.getJSONObject("Global");
          globalModel.setNewCnf(gm.getString("NewConfirmed"));
          globalModel.setTotalCnf(gm.getString("TotalConfirmed"));
          globalModel.setNewDeaths(gm.getString("NewDeaths"));
          globalModel.setTotalDeaths(gm.getString("TotalDeaths"));
          globalModel.setNewRecov(gm.getString("NewRecovered"));
          globalModel.setTotalRecov(gm.getString("TotalRecovered"));
          globalModelCopy = globalModel;
          JSONArray countryList = countryWise.getJSONArray("Countries");
          countryModels = new ArrayList<>();
          for(int i=0;i<countryList.length();i++) {
              JSONObject country = countryList.getJSONObject(i);
              countryModels.add(new CountryModel(country.getString("Country"),
                      country.getString("CountryCode"),
                      country.getString("NewConfirmed"),
                      country.getString("TotalConfirmed"),
                      country.getString("NewDeaths"),
                      country.getString("TotalDeaths"),
                      country.getString("NewRecovered"),
                      country.getString("TotalRecovered"),
                      country.getString("Date")));
          }
        Collections.sort(countryModels);

    }

}
