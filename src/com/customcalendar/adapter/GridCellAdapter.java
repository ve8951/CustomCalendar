package com.customcalendar.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.customcalendar.ACTDay;
import com.customcalendar.R;
import com.customcalendar.animations.ActivitySwitcher;

public class GridCellAdapter extends BaseAdapter implements OnClickListener
{
	private static final String tag = "GridCellAdapter";
	private final Context _context;

	private final List<String> list1,list2,list3,list4;
	private static final int DAY_OFFSET = 1;
	private final String[] weekdays = new String[]{"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
	private final String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
	private final int[] daysOfMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
	private final int month, year;
	private int prevMonthDays;
	private int currentDayOfMonth;
	private int currentWeekDay;
	private Button gridcell;
	Calendar calendar;
	private TextView num_events_per_day;
	private final HashMap eventsPerMonthMap;
	private final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MMM-yyyy");
	Activity activity;
	String date_month_year;

	// Days in Current Month
	public GridCellAdapter(Activity activity, int textViewResourceId, int month, int year)
	{
		super();
		this.activity = activity;
		this._context = activity.getBaseContext();
		this.list1 = new ArrayList<String>();
		this.list2 = new ArrayList<String>();
		this.list3 = new ArrayList<String>();
		this.list4 = new ArrayList<String>();
		this.month = month;
		this.year = year;

		calendar = Calendar.getInstance();
		setCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
		setCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK));

		// Print Month
		printMonth(month, year);

		// Find Number of Events
		eventsPerMonthMap = findNumberOfEventsPerMonth(2012, 04);
	}
	private String getMonthAsString(int i)
	{
		return months[i];
	}

	private String getWeekDayAsString(int i)
	{
		return weekdays[i];
	}

	private int getNumberOfDaysOfMonth(int i)
	{
		return daysOfMonth[i];
	}

	public String getItem(int position)
	{
		return list1.get(position);
	}

	@Override
	public int getCount()
	{
		return list1.size();
	}

	private void printMonth(int mm, int yy)
	{

		// The number of days to leave blank at
		// the start of this month.
		int trailingSpaces = 0;
		int leadSpaces = 0;
		int daysInPrevMonth = 0;
		int prevMonth = 0;
		int prevYear = 0;
		int nextMonth = 0;
		int nextYear = 0;

		int currentMonth = mm - 1;
		String currentMonthName = getMonthAsString(currentMonth);
		int daysInMonth = getNumberOfDaysOfMonth(currentMonth);


		// Gregorian Calendar : MINUS 1, set to FIRST OF MONTH
		GregorianCalendar cal = new GregorianCalendar(yy, currentMonth, 1);


		if (currentMonth == 11)

		{
			prevMonth = currentMonth - 1;
			daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
			nextMonth = 0;
			prevYear = yy;
			nextYear = yy + 1;

		}
		else if (currentMonth == 0)
		{
			prevMonth = 11;
			prevYear = yy - 1;
			nextYear = yy;
			daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
			nextMonth = 1;

		}
		else
		{
			prevMonth = currentMonth - 1;
			nextMonth = currentMonth + 1;
			nextYear = yy;
			prevYear = yy;
			daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);

		}

		// Compute how much to leave before before the first day of the
		// month.
		// getDay() returns 0 for Sunday.
		int currentWeekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
		trailingSpaces = currentWeekDay;

		if (isLeapYear(cal.get(Calendar.YEAR)))
		{if(currentMonth == 1){

			++daysInMonth;
		}
		}

		if (prevMonth==1) {
			if(isLeapYear(cal.get(Calendar.YEAR))){	
				++daysInPrevMonth;
			}

		}


		// Trailing Month days
		for (int i = 0; i < trailingSpaces; i++)
		{	
			list1.add(String.valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET) + i));
			list2.add("GREY");
			list3.add(""+prevMonth);
			list4.add(""+prevYear);
		}

		// Current Month Days
		for (int i = 1; i <= daysInMonth; i++)
		{

			if (i == getCurrentDayOfMonth())
			{
				if(currentMonth == calendar.get(Calendar.MONTH)){

					list1.add(String.valueOf(i));
					list2.add("YELLOW");
					list3.add(""+currentMonth);
					list4.add(""+yy);
				}
				else {
					list1.add(String.valueOf(i));
					list2.add("BLACK");
					list3.add(""+currentMonth);
					list4.add(""+yy);
				}
			}

			else
			{
				list1.add(String.valueOf(i));
				list2.add("BLACK");
				list3.add(""+currentMonth);
				list4.add(""+yy);
			}


		}

		// Leading Month days
		for (int i = 0; i < list1.size() % 7; i++)
		{
			list1.add(String.valueOf(i + 1));
			list2.add("GREY");
			list3.add(""+nextMonth);
			list4.add(""+nextYear);
		}

	}


	// checking for leap year
	private boolean isLeapYear(int year) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		if(cal.getActualMaximum(Calendar.DAY_OF_YEAR) > 365){
			return true;
		}else return false;

	}
	/**
	 * NOTE: YOU NEED TO IMPLEMENT THIS PART Given the YEAR, MONTH, retrieve
	 * ALL entries from a SQLite database for that month. Iterate over the
	 * List of All entries, and get the dateCreated, which is converted into
	 * day.
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	private HashMap findNumberOfEventsPerMonth(int year, int month)
	{
		HashMap map = new HashMap<String, Integer>();
		//		DateFormat dateFormatter2 = new DateFormat();
		//
		//		String day = dateFormatter2.format("dd", 01).toString();
		//
		//		if (map.containsKey(day))
		//		{
		//			Integer val = (Integer) map.get(day) + 1;
		//			map.put(day, val);
		//		}
		//		else
		//		{
		//			map.put(day, 1);
		//		}
		//
		//		return map;
		return map;
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View row = convertView;
		if (row == null)
		{
			LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.calendar_day_gridcell, parent, false);
		}

		// Day gridcell
		gridcell = (Button) row.findViewById(R.id.calendar_day_gridcell);
		gridcell.setOnClickListener(this);
		num_events_per_day = (TextView) row.findViewById(R.id.num_events_per_day);

		final String color = list2.get(position);
		final String theday = list1.get(position);
		final String themonth = list3.get(position);
		final String theyear = list4.get(position);


		
		appendZeroToSingle(theday, themonth, theyear);






		if (color.equals("GREY"))
		{
			gridcell.setEnabled(false);
			num_events_per_day.setTextColor(Color.BLACK);
			gridcell.setBackgroundResource(R.drawable.donegrey);
		}
		if (color.equals("BLACK"))
		{
			num_events_per_day.setTextColor(Color.GRAY);

		}
		if (color.equals("YELLOW"))
		{
			num_events_per_day.setTextColor(Color.YELLOW);
			
		}


		String[] sundays = new String[]{"0","7","14","21","28","35"};
		for(int i=0;i<sundays.length;i++){
			if(position == Integer.parseInt(sundays[i])){
				gridcell.setBackgroundResource(R.drawable.donegrey);
				num_events_per_day.setTextColor(Color.RED);
				gridcell.setEnabled(false);

			}
		}
		return row;
	}


	private void appendZeroToSingle(String theday, String themonth, String theyear) {

		String temp;
		if(theday.length()==1){
			theday = "0"+theday;
		}else{
			theday = theday;
		}
		int calculateTempMonth = Integer.parseInt(themonth)+1;
		themonth = ""+calculateTempMonth;
		if(themonth.length()==1){
			themonth = "0"+themonth;
		}else{
			themonth = themonth;
		}
		// Set the Day GridCell
		num_events_per_day.setText(theday);
		gridcell.setTag(theday + "" + themonth + "" + theyear);

	}



	public void onClick(View view)
	{
		date_month_year = (String) view.getTag();

		Toast.makeText(_context, date_month_year, Toast.LENGTH_LONG).show();
		animatedStartActivity();
	}

	public int getCurrentDayOfMonth()
	{

		return currentDayOfMonth;
	}

	private void setCurrentDayOfMonth(int currentDayOfMonth)
	{
		this.currentDayOfMonth = currentDayOfMonth;
	}

	public void setCurrentWeekDay(int currentWeekDay)
	{
		this.currentWeekDay = currentWeekDay;
	}

	public int getCurrentWeekDay()
	{
		return currentWeekDay;
	}


	private void animatedStartActivity() {
		final Intent intent = new Intent(_context,ACTDay.class);
		intent.putExtra("DATE", date_month_year);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		ActivitySwitcher.animationOut(activity.findViewById(R.id.CalenderGrid),activity.getWindowManager(), new ActivitySwitcher.AnimationFinishedListener() {
			@Override
			public void onAnimationFinished() {
				activity.startActivity(intent);
			}
		});
	}

}

//
//if ((!eventsPerMonthMap.isEmpty()) && (eventsPerMonthMap != null))
//{
//	if (eventsPerMonthMap.containsKey(theday))
//	{
//		num_events_per_day = (TextView) row.findViewById(R.id.num_events_per_day);
//		Integer numEvents = (Integer) eventsPerMonthMap.get(theday);
//		num_events_per_day.setText(numEvents.toString()+"der it is");
//	}
//}
