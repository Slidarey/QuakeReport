package com.example.android.quakereport;

/**
 * Created by Zahir on 20/09/2017.
 */

public class EarthQuake {

    private Double mMagnitude;
    private String mPlace;
    private long mTimeInMilliseconds;
    private String mUrl;


    public EarthQuake(Double magnitude, String place, long timeInMilliseconds, String url) {
        mMagnitude = magnitude;
        mPlace = place;
        mTimeInMilliseconds = timeInMilliseconds;
        mUrl = url;
    }

    public void setMagnitude(Double magnitude) {
        this.mMagnitude = magnitude;
    }

    public Double getMagnitude() {
        return mMagnitude;
    }

    public void setPlace(String place) {
        this.mPlace = place;
    }

    public String getPlace() {
        return mPlace;
    }

    public void setDate(long timeInMilliseconds) {
        this.mTimeInMilliseconds = timeInMilliseconds;
    }

    public long getDate() {
        return mTimeInMilliseconds;
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }

    public String getUrl() {
        return mUrl;
    }
}
