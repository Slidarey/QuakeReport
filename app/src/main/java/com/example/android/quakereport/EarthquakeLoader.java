package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by Zahir on 10/10/2017.
 */

public class EarthquakeLoader extends AsyncTaskLoader<List<EarthQuake>> {

    public static final String LOG_TAG = EarthquakeLoader.class.getSimpleName();

    private String mUrl;

    public EarthquakeLoader(Context context, String url) {
        super(context);
        this.mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<EarthQuake> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        List<EarthQuake> earthquakes = QueryUtils.fetchEarthQuakeData(mUrl);
        return earthquakes;

    }
}
