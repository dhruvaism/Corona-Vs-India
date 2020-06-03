package com.sarkar.dhruv.coronacases.models;

import java.util.ArrayList;

public class StateModel {
   String active,confirmed,deaths,deltaconfirmed,deltadeaths,deltarecovered,lastupdatedtime,recovered,state,statecode,statenodes,time;
   ArrayList<DistrictModel> districtModels;
    public boolean withDistrict = false;
    public StateModel(String active, String confirmed, String deaths, String deltaconfirmed, String deltadeaths, String deltarecovered, String lastupdatedtime, String recovered, String state, String statecode, String statenodes,String time) {
        this.active = active;
        this.confirmed = confirmed;
        this.deaths = deaths;
        this.deltaconfirmed = deltaconfirmed;
        this.deltadeaths = deltadeaths;
        this.deltarecovered = deltarecovered;
        this.lastupdatedtime = lastupdatedtime;
        this.recovered = recovered;
        this.state = state;
        this.statecode = statecode;
        this.statenodes = statenodes;
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public boolean isWithDistrict() {
        return withDistrict;
    }

    public void setWithDistrict(boolean withDistrict) {
        this.withDistrict = withDistrict;
    }

    public String getActive() {
        return active;
    }

    public String getConfirmed() {
        return confirmed;
    }

    public String getDeaths() {
        return deaths;
    }

    public String getDeltaconfirmed() {
        return deltaconfirmed;
    }

    public String getDeltadeaths() {
        return deltadeaths;
    }

    public String getDeltarecovered() {
        return deltarecovered;
    }

    public String getLastupdatedtime() {
        return lastupdatedtime;
    }

    public String getRecovered() {
        return recovered;
    }

    public String getState() {
        return state;
    }

    public String getStatecode() {
        return statecode;
    }

    public String getStatenodes() {
        return statenodes;
    }

    public ArrayList<DistrictModel> getDistrictModels() {
        return districtModels;
    }
    public void setDistrictModels(ArrayList<DistrictModel> districtModels) {
        this.districtModels = districtModels;
    }
}
