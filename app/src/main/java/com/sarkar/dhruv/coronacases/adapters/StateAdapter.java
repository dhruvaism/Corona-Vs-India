package com.sarkar.dhruv.coronacases.adapters;


import android.content.Context;
import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sarkar.dhruv.coronacases.MainActivity;
import com.sarkar.dhruv.coronacases.R;
import com.sarkar.dhruv.coronacases.fragments.India;
import com.sarkar.dhruv.coronacases.models.StateModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.sarkar.dhruv.coronacases.MainActivity.formatter;

public class StateAdapter extends RecyclerView.Adapter<StateAdapter.StateVH> {

    private ArrayList<StateModel> stateList;
    private Context context;
    public StateAdapter(Context context,ArrayList<StateModel> stateList) {
        this.context = context;
        this.stateList = stateList;
    }



    @NonNull
    @Override
    public StateVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.state_wise_cases_item,parent,false);
        return new StateVH(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull final StateVH holder, final int position) {
        holder.statename.setText(stateList.get(position).getState());
        holder.confNo.setText(formatter(stateList.get(position).getConfirmed()));
        holder.confNewNo.setText(formatter(stateList.get(position).getDeltaconfirmed()));
        holder.recNo.setText(formatter(stateList.get(position).getRecovered()));
        holder.recNewNo.setText(formatter(stateList.get(position).getDeltarecovered()));
        holder.deathsNo.setText(formatter(stateList.get(position).getDeaths()));
        holder.deathsNewNo.setText(formatter(stateList.get(position).getDeltadeaths()));
        holder.activeNo.setText(formatter(stateList.get(position).getActive()));

        DisplayMetrics displayMetrics = new DisplayMetrics();
        MainActivity.activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float size = displayMetrics.widthPixels;
        ViewGroup.LayoutParams lp_state_name = holder.state.getLayoutParams();
        ViewGroup.LayoutParams lp_conf_case = holder.conf.getLayoutParams();
        ViewGroup.LayoutParams lp_active_case = holder.active.getLayoutParams();
        ViewGroup.LayoutParams lp_recov_case = holder.recov.getLayoutParams();
        ViewGroup.LayoutParams lp_death_case = holder.death.getLayoutParams();

        lp_state_name.width=(int)size*2/5;
        lp_conf_case.width = (int)size*3/20;
        lp_active_case.width = (int)size*3/20;
        lp_recov_case.width = (int)size*3/20;
        lp_death_case.width = (int)size*3/20;


        if(position%2!=0) {
           // holder.itemView.setBackgroundTintList(ColorStateList.valueOf(context.getColor(R.color.alternate_color)));
            holder.itemView.setBackgroundColor(context.getColor(R.color.alternate_color));
        }

        if(stateList.get(position).isWithDistrict()) {
            holder.districtLayout.setVisibility(View.VISIBLE);
            holder.right.setVisibility(View.GONE);
            holder.down.setVisibility(View.VISIBLE);
            new BackgroundTask(holder,position).execute();

         }else {
            holder.districtLayout.setVisibility(View.GONE);
            holder.right.setVisibility(View.VISIBLE);
            holder.down.setVisibility(View.GONE);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0;i<stateList.size();i++) {
                    if(position!=i) {
                        stateList.get(i).setWithDistrict(false);

                    }else {
                        if(!stateList.get(position).isWithDistrict()) {
                            stateList.get(position).setWithDistrict(true);
                        }else {
                            stateList.get(position).setWithDistrict(false);
                        }

                    }
                }
                India.stateAdapter.notifyDataSetChanged();

            }
        });

    }


    @Override
    public int getItemCount() {
        return stateList==null?0:stateList.size();
    }


    public static class StateVH extends RecyclerView.ViewHolder{

        public TextView statename,confNo,confNewNo,recNo,recNewNo,deathsNo,deathsNewNo,activeNo;
        RecyclerView districtRecyclerView;
        LinearLayout districtLayout;
        ProgressBar progress;
        AppCompatImageView right,down;
        public LinearLayoutCompat state,conf,active,recov,death;
        public StateVH(@NonNull View itemView) {
            super(itemView);
            statename = itemView.findViewById(R.id.state_name);
            confNo = itemView.findViewById(R.id.confirmed_no);
            confNewNo = itemView.findViewById(R.id.confirmed_new_no);
            recNo = itemView.findViewById(R.id.recovered_no);
            recNewNo = itemView.findViewById(R.id.recovered_new_no);
            deathsNo = itemView.findViewById(R.id.deaths_no);
            deathsNewNo = itemView.findViewById(R.id.deaths_new_no);
            activeNo = itemView.findViewById(R.id.active_no);
            districtRecyclerView = itemView.findViewById(R.id.district_recycler_view);
            state = itemView.findViewById(R.id.state_name_ll);
            conf = itemView.findViewById(R.id.cc_ll);
            active = itemView.findViewById(R.id.ac_ll);
            recov = itemView.findViewById(R.id.rc_ll);
            death = itemView.findViewById(R.id.dc_ll);
            districtLayout = itemView.findViewById(R.id.district_layout);
            progress = itemView.findViewById(R.id.district_progress);
            right = itemView.findViewById(R.id.right_);
            down = itemView.findViewById(R.id.down_);

        }

    }

    private class BackgroundTask extends AsyncTask<Void,Void,Void> {

        StateVH stateVH;
        int position;
        public BackgroundTask(StateVH stateVH,int position) {
            this.stateVH = stateVH;
            this.position = position;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            MainActivity.activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context,RecyclerView.VERTICAL,false);
                    DistrictAdapter districtAdapter = new DistrictAdapter(context,stateList.get(position).getDistrictModels());
                    stateVH.districtRecyclerView.setLayoutManager(linearLayoutManager);
                    stateVH.districtRecyclerView.setAdapter(districtAdapter);
                    stateVH.progress.setVisibility(View.GONE);
                }
            });
            return null;
        }

    }



}
