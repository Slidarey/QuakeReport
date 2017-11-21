/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<EarthQuake>> {

    public static final String LOG_TAG = EarthquakeActivity.class.getSimpleName();

    private static final int EARTHQUAKE_LOADER_ID = 1;

    private TextView mEmptyView;

    private ProgressBar mProgressBar;


    /**
     * Adapter for the list of earthquakes
     */
    private EarthquakeAdapter mAdapter;

    private static final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);
//        String str = "io am groove";
//        String[] a = str.split(" ");
//        Log.e("EarthQuakeActivity", "" + a[0]);

        // Create a fake list of earthquake locations.
        // Since the QueryUtils class will create an ArrayList<EarthQuake> named earthquakes
        // and the method QueryUtils.extractEarthquakes will return an ArrayList<EarthQuake> we will just need to assign the
        // static method call to the newly created arraylist in EarthquakeActivity.java

        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);
        earthquakeListView.setEmptyView(mEmptyView);

        // Create a new {@link ArrayAdapter} of earthquakes
        mAdapter = new EarthquakeAdapter(this, new ArrayList<EarthQuake>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(mAdapter);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EarthQuake earthquake = mAdapter.getItem(position);

                String uriString = earthquake.getUrl();
                if (uriString != null) {
                    Intent websiteIntent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(uriString));

                    // Checks to see if there is an Activity capable of handling the intent
                    if (websiteIntent.resolveActivity(getPackageManager()) != null) {
                        startActivity(websiteIntent);
                    }
                }
            }
        });

        Log.e(LOG_TAG, "" + this + ":" + getBaseContext() + ":" + EarthquakeActivity.this + ":" + getApplicationContext());
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (isConnected) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loadManager = getLoaderManager();
            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loadManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);

            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                Log.e(LOG_TAG, "It is Wifi");
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                Log.e(LOG_TAG, "It is Internet");
            } else {
                return;
            }
        } else {
            mEmptyView = (TextView) findViewById(R.id.empty_view);
            mProgressBar = (ProgressBar) findViewById(R.id.loading_spinner);
            mEmptyView.setText(R.string.no_internet_connection);
            mProgressBar.setVisibility(View.GONE);
        }
    }


    @Override
    public Loader<List<EarthQuake>> onCreateLoader(int id, Bundle args) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        Log.e(LOG_TAG, "" + this);
        String minMagnitude = sharedPrefs.getString(getString(R.string.settings_min_magnitude_key),
                getString(R.string.settings_min_magnitude_default));
        String orderBy = sharedPrefs.getString(getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default));
        String maxMagnitude = sharedPrefs.getString(getString(R.string.settings_max_magnitude_key),
                getString(R.string.settings_max_magnitude_default));
        String startTime = sharedPrefs.getString(getString(R.string.settings_start_time_key),
                getString(R.string.settings_start_time_default));
        String endTime = sharedPrefs.getString(getString(R.string.settings_end_time_key),
                getString(R.string.settings_end_time_default));

        Log.e(LOG_TAG, "" + minMagnitude);

        Uri baseUri = Uri.parse(USGS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("format", "geojson");

        uriBuilder.appendQueryParameter("minmagnitude", minMagnitude);
        uriBuilder.appendQueryParameter("orderby", orderBy);
        uriBuilder.appendQueryParameter("maxmagnitude", maxMagnitude);
        uriBuilder.appendQueryParameter("starttime", startTime);
        uriBuilder.appendQueryParameter("endtime", endTime);

        return new EarthquakeLoader(EarthquakeActivity.this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<EarthQuake>> loader, List<EarthQuake> data) {
        mAdapter.clear();
        mEmptyView = (TextView) findViewById(R.id.empty_view);
        mProgressBar = (ProgressBar) findViewById(R.id.loading_spinner);
        mProgressBar.setVisibility(View.GONE);

        if (data != null && !data.isEmpty()) {
            mAdapter.addAll(data);
        } else {
            mEmptyView.setText(R.string.no_earthquakes);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<EarthQuake>> loader) {
        mAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, Settings.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //you can declare a variable 'final' in the parameter section  of a method when declaring a method as shown below
    public void updateUI(final ArrayList<EarthQuake> earthquakes) {

    }
}