package com.customcalendar;

import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.customcalendar.adapter.DayofWeekAdapter;
import com.customcalendar.adapter.GridCellAdapter;
import com.customcalendar.adapter.GridGesture;
import com.customcalendar.animations.ActivitySwitcher;

public class ACTCalendarActivity extends Activity implements OnItemClickListener,OnClickListener{
	static TextView currentMonth;
	 static GridView calendarGridView;
	GridView weekDayGrid;
	static GridCellAdapter adapter;
	static Calendar _calendar;
	static int month;
	static int year;
	final static DateFormat dateFormatter = new DateFormat();
	static final String dateTemplate = "MMMM yyyy";
	static Context context;
	GestureDetector gestureDetector;
	View.OnTouchListener gestureListener;
	LinearLayout _UILLCalendar;
	static Activity activity;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.simple_calendar_view);
		_calendar = Calendar.getInstance();
		month = _calendar.get(Calendar.MONTH) + 1;
		year = _calendar.get(Calendar.YEAR);

		context = getBaseContext();
		activity = ACTCalendarActivity.this;
		getHandle();
		adapter = new GridCellAdapter(activity, R.id.calendar_day_gridcell, month, year);
		adapter.notifyDataSetChanged();
		calendarGridView.setAdapter(adapter);
		weekDayGrid.setAdapter(new DayofWeekAdapter(getBaseContext(), Calendar.DAY_OF_WEEK));

		gestureDetector = new GestureDetector(new GridGesture(ACTCalendarActivity.this));
		gestureListener = new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {


				return gestureDetector.onTouchEvent(event);
			}
		};

		calendarGridView.setOnItemClickListener(this);
		calendarGridView.setOnTouchListener(gestureListener);
		weekDayGrid.setOnItemClickListener(this);
		weekDayGrid.setOnTouchListener(gestureListener);
		//		_UILLCalendar.setOnClickListener(this);
		_UILLCalendar.setOnTouchListener(gestureListener);
	}



	private void getHandle() {
		//		prevMonth = (Button) this.findViewById(R.id.prevMonth);
		//		prevMonth.setOnClickListener(this);

		currentMonth = (TextView) this.findViewById(R.id.currentMonth);
		currentMonth.setText(dateFormatter.format(dateTemplate, _calendar.getTime()));

		//		nextMonth = (Button) this.findViewById(R.id.nextMonth);
		//		nextMonth.setOnClickListener(this);

		_UILLCalendar = (LinearLayout)findViewById(R.id.CalenderGrid);

		calendarGridView = (GridView) this.findViewById(R.id.calendar);
		weekDayGrid = (GridView) this.findViewById(R.id.daysOfWeek);
	}


	public static void setGridCellAdapterToDate(int month, int year)
	{
		adapter = new GridCellAdapter(activity, R.id.calendar_day_gridcell, month, year);
		_calendar.set(year, month - 1, _calendar.get(Calendar.DAY_OF_MONTH));
		currentMonth.setText(dateFormatter.format(dateTemplate, _calendar.getTime()));
		adapter.notifyDataSetChanged();
		calendarGridView.setAdapter(adapter);

	}


	public static void goToNext() {
		calendarGridView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.push_left_in));
		playSound();

		if (month > 11)
		{
			month = 1;
			year++;
		}
		else
		{
			month++;
		}

		setGridCellAdapterToDate(month, year);
	}



	public static void goToPrev() {


		if (month <= 1)
		{
			month = 12;
			year--;
		}
		else
		{
			month--;
		}

		setGridCellAdapterToDate(month, year);
		calendarGridView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.push_right_in));
		playSound();
	}



	private static void playSound() {
		MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.pageturn);
		mediaPlayer.setLooping(false);
		mediaPlayer.start();
	}


	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

	}



	public boolean dispatchTouchEvent( MotionEvent ev ) {
		// TouchEvent dispatcher.
		if( gestureDetector != null ) {
			if( gestureDetector.onTouchEvent( ev ) )
				return true;
		}
		return super.dispatchTouchEvent( ev );
	}



	@Override
	public void onClick(View v) {

	}


	@Override
	protected void onResume() {
		// animateIn this activity
		ActivitySwitcher.animationIn(findViewById(R.id.CalenderGrid), getWindowManager());
		super.onResume();
	}
	@Override
	protected void onStop() {

		finish();
		super.onStop();
	}




}
