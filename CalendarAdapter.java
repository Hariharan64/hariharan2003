package com.example.qrstaff;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Map;

public class CalendarAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> dates;
    private String selectedDate;
    private String highlightedDate;
    private Map<String, String> dateStatusMap;

    public CalendarAdapter(Context context, ArrayList<String> dates, Map<String, String> dateStatusMap) {
        this.context = context;
        this.dates = dates;
        this.dateStatusMap = dateStatusMap;
    }

    @Override
    public int getCount() {
        return dates.size();
    }

    @Override
    public Object getItem(int position) {
        return dates.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.calendar_item, parent, false);
        }

        TextView dateText = convertView.findViewById(R.id.dateText);
        String date = dates.get(position);
        dateText.setText(date.split("-")[2]); // Show only the day part (e.g., "01")

        // Check the status from the map and apply the appropriate color
        String status = dateStatusMap.get(date);
        if ("punch_in".equals(status)) {
            dateText.setBackgroundColor(ContextCompat.getColor(context, R.color.blue)); // Blue for punch-in
        } else if ("punch_out".equals(status)) {
            dateText.setBackgroundColor(ContextCompat.getColor(context, R.color.green)); // Green for punch-out
        } else {
            dateText.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent)); // Default transparent
        }

        return convertView;
    }

    public void setSelectedDate(String date) {
        this.selectedDate = date;
        notifyDataSetChanged();
    }

    public void setHighlightedDate(String date) {
        this.highlightedDate = date;
        notifyDataSetChanged();
    }
}
