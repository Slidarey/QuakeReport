package com.example.android.quakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Zahir on 20/09/2017.
 */

public class EarthquakeAdapter extends ArrayAdapter<EarthQuake> {

    private static final String LOCATION_OPERATOR = "of";

    private String formatMagnitude(Double magnitude) {
        DecimalFormat magnitudeFormat = new DecimalFormat("0.0");
        return magnitudeFormat.format(magnitude);
    }

    private int getMagnitudeColor(double magnitude) {

        int color;
        switch ((int) Math.floor(magnitude)) {
            case 0:
            case 1:
                color = R.color.magnitude1;
                break;
            case 2:
                color = R.color.magnitude2;
                break;
            case 3:
                color = R.color.magnitude3;
                break;
            case 4:
                color = R.color.magnitude4;
                break;
            case 5:
                color = R.color.magnitude5;
                break;
            case 6:
                color = R.color.magnitude6;
                break;
            case 7:
                color = R.color.magnitude7;
                break;
            case 8:
                color = R.color.magnitude8;
                break;
            case 9:
                color = R.color.magnitude9;
                break;
            default:
                color = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(), color);
    }

    private String formatTime(Date time) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        return timeFormat.format(time);
    }

    private String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        return dateFormat.format(date);
    }

    public EarthquakeAdapter(Context context, ArrayList<EarthQuake> earthquake) {
        super(context, 0, earthquake);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_view, parent, false);
        }

        EarthQuake currentEarthquake = getItem(position);

        TextView magnitudeText = (TextView) listItemView.findViewById(R.id.magnitude_text_view);

        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) magnitudeText.getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(currentEarthquake.getMagnitude());

        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);

        magnitudeText.setText(formatMagnitude(currentEarthquake.getMagnitude()));

        String mOffset;
        String mPrimaryLocation;
        String location = currentEarthquake.getPlace();

        if (location.contains(LOCATION_OPERATOR)) {
            String[] location_parts = location.split("of");
            mOffset = location_parts[0] + LOCATION_OPERATOR;
            mPrimaryLocation = location_parts[1];
        } else {
            mOffset = getContext().getString(R.string.near_the);
            mPrimaryLocation = location;
        }

        TextView offsetText = (TextView) listItemView.findViewById(R.id.offset);
        offsetText.setText(mOffset);

        TextView primaryLocationText = (TextView) listItemView.findViewById(R.id.primary_location);
        primaryLocationText.setText(mPrimaryLocation);

        Date dateObject = new Date(currentEarthquake.getDate());

        TextView dateText = (TextView) listItemView.findViewById(R.id.date_text_view);
        dateText.setText(formatDate(dateObject));

        TextView timeText = (TextView) listItemView.findViewById(R.id.time_text_view);
        timeText.setText(formatTime(dateObject));

        return listItemView;
    }
}