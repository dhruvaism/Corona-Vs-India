package com.sarkar.dhruv.coronacases.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sarkar.dhruv.coronacases.R;
import com.sarkar.dhruv.coronacases.models.StateModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

    @Override
    public void onBindViewHolder(@NonNull StateVH holder, int position) {
        holder.statename.setText(stateList.get(position).getState());
        holder.confNo.setText(stateList.get(position).getConfirmed());
        holder.confNewNo.setText(stateList.get(position).getDeltaconfirmed());
        holder.recNo.setText(stateList.get(position).getRecovered());
        holder.recNewNo.setText(stateList.get(position).getDeltarecovered());
        holder.deathsNo.setText(stateList.get(position).getDeaths());
        holder.deathsNewNo.setText(stateList.get(position).getDeltadeaths());

    }

    @Override
    public int getItemCount() {
        return stateList==null?0:stateList.size();
    }


    public static class StateVH extends RecyclerView.ViewHolder{

        public TextView statename,confNo,confNewNo,recNo,recNewNo,deathsNo,deathsNewNo;
        public StateVH(@NonNull View itemView) {
            super(itemView);
            statename = itemView.findViewById(R.id.state_name);
            confNo = itemView.findViewById(R.id.confirmed_no);
            confNewNo = itemView.findViewById(R.id.confirmed_new_no);
            recNo = itemView.findViewById(R.id.recovered_no);
            recNewNo = itemView.findViewById(R.id.recovered_new_no);
            deathsNo = itemView.findViewById(R.id.deaths_no);
            deathsNewNo = itemView.findViewById(R.id.deaths_new_no);

        }

    }

}
