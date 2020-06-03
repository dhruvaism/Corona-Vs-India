package com.sarkar.dhruv.coronacases.models;

public class GlobalModel {

    public static GlobalModel globalModel;
    public String country,newCnf,totalCnf,newDeaths,totalDeaths,newRecov,totalRecov;
    public static GlobalModel getInstance() {
        if(globalModel==null){
            globalModel = new GlobalModel();
        }
        return globalModel;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setNewCnf(String newCnf) {
        this.newCnf = newCnf;
    }

    public void setTotalCnf(String totalCnf) {
        this.totalCnf = totalCnf;
    }

    public void setNewDeaths(String newDeaths) {
        this.newDeaths = newDeaths;
    }

    public void setTotalDeaths(String totalDeaths) {
        this.totalDeaths = totalDeaths;
    }

    public void setNewRecov(String newRecov) {
        this.newRecov = newRecov;
    }

    public void setTotalRecov(String totalRecov) {
        this.totalRecov = totalRecov;
    }
}
