package com.sarkar.dhruv.coronacases.adapters;


import android.content.Context;
import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.os.Build;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import com.sarkar.dhruv.coronacases.MainActivity;
import com.sarkar.dhruv.coronacases.R;

import com.sarkar.dhruv.coronacases.models.DistrictModel;


import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import static com.sarkar.dhruv.coronacases.MainActivity.formatter;

public class DistrictAdapter extends RecyclerView.Adapter<DistrictAdapter.DistrictVH> {

    private ArrayList<DistrictModel> districtModels;
    private Context context;
    public DistrictAdapter(Context context,ArrayList<DistrictModel> stateList) {
        this.context = context;
        this.districtModels = stateList;
    }


    @NonNull
    @Override
    public DistrictVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.district_wise_cases_item,parent,false);
        return new DistrictVH(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull final DistrictVH holder, final int position) {
        holder.district.setText(districtModels.get(position).getDistrictName());
        holder.conf.setText(formatter(districtModels.get(position).getConfirmed()));
        holder.recov.setText(formatter(districtModels.get(position).getRecovered()));
        holder.death.setText(formatter(districtModels.get(position).getDeaths()));
        holder.active.setText(formatter(districtModels.get(position).getActive()));

        DisplayMetrics displayMetrics = new DisplayMetrics();
        MainActivity.activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float size = displayMetrics.widthPixels;

        ViewGroup.LayoutParams lp_state_name = holder.districtL.getLayoutParams();
        ViewGroup.LayoutParams lp_conf_case = holder.conf.getLayoutParams();
        ViewGroup.LayoutParams lp_active_case = holder.active.getLayoutParams();
        ViewGroup.LayoutParams lp_recov_case = holder.recov.getLayoutParams();
        ViewGroup.LayoutParams lp_death_case = holder.death.getLayoutParams();

        lp_state_name.width=(int)size*2/5;
        lp_conf_case.width = (int)size*3/20;
        lp_active_case.width = (int)size*3/20;
        lp_recov_case.width = (int)size*3/20;
        lp_death_case.width = (int)size*3/20;

        if(districtModels.get(position).getZone().equals("Green")) {
             holder.zoneIcon.setImageTintList(ColorStateList.valueOf(context.getColor(R.color.recovered)));
        }else if(districtModels.get(position).getZone().equals("Red")) {
            holder.zoneIcon.setImageTintList(ColorStateList.valueOf(context.getColor(R.color.deaths)));
        }else if(districtModels.get(position).getZone().equals("Orange")) {
            holder.zoneIcon.setImageTintList(ColorStateList.valueOf(context.getColor(R.color.confirmed)));
        }


    }


    @Override
    public int getItemCount() {
        return districtModels==null?0:districtModels.size();
    }


    public static class DistrictVH extends RecyclerView.ViewHolder{

        public TextView district,conf,active,recov,death;
        LinearLayoutCompat districtL;
        AppCompatImageView zoneIcon;
        public DistrictVH(@NonNull View itemView) {
            super(itemView);
            district = itemView.findViewById(R.id.district_name);
            conf = itemView.findViewById(R.id.confirmed_no);
            active = itemView.findViewById(R.id.active_no);
            recov = itemView.findViewById(R.id.recovered_no);
            death = itemView.findViewById(R.id.deaths_no);
            districtL = itemView.findViewById(R.id.disrict_ll);
            zoneIcon = itemView.findViewById(R.id.zone_icon);

        }

    }


}
