package com.customcalendar;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.customcalendar.animations.ActivitySwitcher;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ACTDay extends ListActivity implements OnItemClickListener {

	String[] day_hours = new String[24];
	TextView dateHeader ;
	String temp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.daylist);

		ListView lv = (ListView) findViewById(android.R.id.list);
		dateHeader = (TextView)findViewById(R.id.TVDayLabel);
		MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(this, day_hours);
		setListAdapter(adapter);
		retrieveExtrasNSetDate();
		lv.setOnItemClickListener(this);

	}


	private void retrieveExtrasNSetDate() {
		String tempDate = null;
		String tempMonth = null;
		String tempYear = null;
		
		Bundle extras = getIntent().getExtras(); 
		if (extras != null) {
			temp = extras.getString("DATE");
			tempDate = temp.substring(0,2);
			tempMonth = temp.substring(2,4);
			tempYear = temp.substring(4);
		}
		
         dateHeader.setText(tempDate+"-"+getMonth(Integer.parseInt(tempMonth)));
	}
	
	public String getMonth(int month) {
	    return new DateFormatSymbols().getMonths()[month-1];
	}

	public class MySimpleArrayAdapter extends ArrayAdapter<String> {
		private final Context context;
		private final String[] values;

		public MySimpleArrayAdapter(Context context, String[] values) {
			super(context, R.layout.row_list_dayhours, values);
			this.context = context;
			this.values = values;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			LayoutInflater inflator=((Activity) context).getLayoutInflater();
			View row=inflator.inflate(R.layout.row_list_dayhours, null);

			TextView _UITVTime = (TextView)row.findViewById(R.id.TVHourDisp);
			_UITVTime.setText(""+position);

			return(row);
		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		animatedStartActivity();
	}
	
	private void animatedStartActivity() {
		final Intent i = new Intent(this,ACTEventDetails.class);
		i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		i.putExtra("DATE",temp);
		ActivitySwitcher.animationOut(findViewById(R.id.LLDayListContainer), getWindowManager(), new ActivitySwitcher.AnimationFinishedListener() {
			@Override
			public void onAnimationFinished() {
				int KEY = 1101;
				startActivity(i);
			}
		});
	}
	@Override
	protected void onResume() {
		// animateIn this activity

		ActivitySwitcher.animationIn(findViewById(R.id.LLDayListContainer), getWindowManager());
		super.onResume();
	}


}
