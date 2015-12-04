package com.customcalendar.adapter;

import java.text.DateFormatSymbols;

import com.customcalendar.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

public class DayofWeekAdapter extends ArrayAdapter{
	Context context;

	public DayofWeekAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
		this.context = context;
	}

	public int getCount() {
		return 7;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView = vi.inflate(R.layout.day_of_week, null);
		Button dayView = (Button)convertView.findViewById(R.id.Days);
		dayView.setText(new DateFormatSymbols().getShortWeekdays()[position+1]);
		return convertView;
	}


}
