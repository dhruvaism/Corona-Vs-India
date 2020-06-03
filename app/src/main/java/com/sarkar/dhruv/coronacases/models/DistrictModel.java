package com.sarkar.dhruv.coronacases.models;

public class DistrictModel implements Comparable {


    String districtName;
    String confirmed;
    String active;
    String recovered;
    String deaths;
    String zone;



    public DistrictModel(String districtName, String confirmed, String active, String recovered, String deaths, String zone) {
        this.districtName = districtName;
        this.confirmed = confirmed;
        this.active = active;
        this.recovered = recovered;
        this.deaths = deaths;
        this.zone = zone;
    }



    public String getZone() {
        return zone;
    }

    public String getDistrictName() {
        return districtName;
    }

    public String getConfirmed() {
        return confirmed;
    }

    public String getActive() {
        return active;
    }

    public String getRecovered() {
        return recovered;
    }

    public String getDeaths() {
        return deaths;
    }

    @Override
    public int compareTo(Object comparestu) {
        int compareage=Integer.parseInt(((DistrictModel)comparestu).getConfirmed());

        /* For Descending order do like this */
        return compareage-Integer.parseInt(this.confirmed);
    }
}
