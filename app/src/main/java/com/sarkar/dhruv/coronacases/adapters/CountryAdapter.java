package com.sarkar.dhruv.coronacases.adapters;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sarkar.dhruv.coronacases.MainActivity;
import com.sarkar.dhruv.coronacases.R;
import com.sarkar.dhruv.coronacases.fragments.India;
import com.sarkar.dhruv.coronacases.models.CountryModel;
import com.sarkar.dhruv.coronacases.models.StateModel;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.sarkar.dhruv.coronacases.MainActivity.formatter;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.CountryVH> {

    private ArrayList<CountryModel> countryModels;
    private Context context;
    public CountryAdapter(Context context, ArrayList<CountryModel> countryModels) {
        this.context = context;
        this.countryModels = countryModels;
    }



    @NonNull
    @Override
    public CountryVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.country_wise_cases_item,parent,false);
        return new CountryVH(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull final CountryVH holder, final int position) {
        holder.countryName.setText(countryModels.get(position).getCountry());
        holder.confNo.setText(formatter(countryModels.get(position).getTotalCnf()));
        holder.confNewNo.setText(formatter(countryModels.get(position).getNewConfirmed()));
        holder.recNo.setText(formatter(countryModels.get(position).getTotalRecov()));
        holder.recNewNo.setText(formatter(countryModels.get(position).getTotalRecov()));
        holder.deathsNo.setText(formatter(countryModels.get(position).getTotalDths()));
        holder.deathsNewNo.setText(formatter(countryModels.get(position).getNewDeaths()));

        DisplayMetrics displayMetrics = new DisplayMetrics();
        MainActivity.activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float size = displayMetrics.widthPixels;
        ViewGroup.LayoutParams lp_state_name = holder.state.getLayoutParams();
        ViewGroup.LayoutParams lp_conf_case = holder.conf.getLayoutParams();
        ViewGroup.LayoutParams lp_recov_case = holder.recov.getLayoutParams();
        ViewGroup.LayoutParams lp_death_case = holder.death.getLayoutParams();

        lp_state_name.width=(int)size*2/5;
        lp_conf_case.width = (int)size*1/5;
        lp_recov_case.width = (int)size*1/5;
        lp_death_case.width = (int)size*1/5;


        if(position%2!=0) {
           // holder.itemView.setBackgroundTintList(ColorStateList.valueOf(context.getColor(R.color.alternate_color)));
            holder.itemView.setBackgroundColor(context.getColor(R.color.alternate_color));
        }


    }


    @Override
    public int getItemCount() {
        return countryModels==null?0:countryModels.size();
    }


    public static class CountryVH extends RecyclerView.ViewHolder{

        public TextView countryName,confNo,confNewNo,recNo,recNewNo,deathsNo,deathsNewNo;
        public LinearLayoutCompat state,conf,active,recov,death;
        public CountryVH(@NonNull View itemView) {
            super(itemView);
            countryName = itemView.findViewById(R.id.country_name);
            confNo = itemView.findViewById(R.id.confirmed_no);
            confNewNo = itemView.findViewById(R.id.confirmed_new_no);
            recNo = itemView.findViewById(R.id.recovered_no);
            recNewNo = itemView.findViewById(R.id.recovered_new_no);
            deathsNo = itemView.findViewById(R.id.deaths_no);
            deathsNewNo = itemView.findViewById(R.id.deaths_new_no);
            state = itemView.findViewById(R.id.state_name_ll);
            conf = itemView.findViewById(R.id.cc_ll);
            recov = itemView.findViewById(R.id.rc_ll);
            death = itemView.findViewById(R.id.dc_ll);

        }

    }




}
