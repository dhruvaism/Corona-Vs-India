package com.sarkar.dhruv.coronacases.models;

public class CountryModel implements Comparable{

    public String country,countryCode,newConfirmed,totalCnf,newDeaths,totalDths,newRecov,totalRecov,date;

    public CountryModel(String country, String countryCode, String newConfirmed, String totalCnf, String newDeaths, String totalDths, String newRecov, String totalRecov, String date) {
        this.country = country;
        this.countryCode = countryCode;
        this.newConfirmed = newConfirmed;
        this.totalCnf = totalCnf;
        this.newDeaths = newDeaths;
        this.totalDths = totalDths;
        this.newRecov = newRecov;
        this.totalRecov = totalRecov;
        this.date = date;
    }

    public String getCountry() {
        return country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getNewConfirmed() {
        return newConfirmed;
    }

    public String getTotalCnf() {
        return totalCnf;
    }

    public String getNewDeaths() {
        return newDeaths;
    }

    public String getTotalDths() {
        return totalDths;
    }

    public String getNewRecov() {
        return newRecov;
    }

    public String getTotalRecov() {
        return totalRecov;
    }

    public String getDate() {
        return date;
    }

    @Override
    public int compareTo(Object comparestu) {
        int compareage=Integer.parseInt(((CountryModel)comparestu).getTotalCnf());

        /* For Descending order do like this */
        return compareage-Integer.parseInt(this.totalCnf);
    }
}
