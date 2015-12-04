package com.customcalendar;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.customcalendar.animations.ActivitySwitcher;

public class ACTEventDetails extends Activity implements OnClickListener,OnCheckedChangeListener{

	private Button _UIBTSubmit, _UIBTFromDate, _UIBTFromTime, _UIBTToDate, _UIBTToTime, _UIBTCancel,_UIBTRepition, _UIBTGuest;
	private CheckBox _UICBAllDay;


	private int mYear;
	private int mMonth;
	private int mDay;

	private int mHour;
	private int mMinute;
	AlertDialog alert1;
	AlertDialog alert2;


	static final int FROM_TIME_DIALOG_ID = 3;
	static final int TO_TIME_DIALOG_ID = 4;
	static final int FROM_DATE_DIALOG_ID = 1;
	static final int TO_DATE_DIALOG_ID = 2;
	static final int REPITION_TRIGGER = 0;
	static final int GUESTS_INVITATION = 5;

	static final int START_HOUR_OF_DAY = 8;
	static final int START_MINUTE_OF_DAY = 00;
	static final int END_HOUR_OF_DAY = 23;
	static final int END_MINUTE_OF_DAY = 00;

	private CharSequence[] _options = { "One-time event", "Daily", "Every Weekday (Mon-Fri)", "Monthly", "Yearly" };
	private String[] _guestList = new  String[]{"a","b","c","d","e","f","g","h","i","j","k","l","m","n"};
	boolean[] guestsChecked = new boolean[_guestList.length];

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.event_details);

		getHandles();
		retrieveExtrasNSetDate();

		// display the current date (this method is below)
		updateFromDisplay( );
		updateToDisplay();
		updateFromTimeDisplay(mHour,mMinute);
		updateToTimeDisplay(mHour,mMinute);
	}


	private void retrieveExtrasNSetDate() {
		String tempDate = null;
		String tempMonth = null;
		String tempYear = null;

		String temp = null;
		Bundle extras = getIntent().getExtras(); 
		if (extras != null) {
			temp = extras.getString("DATE");
			tempDate = temp.substring(0,2);
			tempMonth = temp.substring(2,4);
			tempYear = temp.substring(4);
		}

		mYear = Integer.parseInt(tempYear);
		mMonth = Integer.parseInt(tempMonth)-1;
		mDay = Integer.parseInt(tempDate);
		final Calendar c = Calendar.getInstance();
		mHour = c.get(Calendar.HOUR_OF_DAY);
		mMinute = c.get(Calendar.MINUTE);
	}


	//-------------------------------------------------	DATE UPDATES-------------------------------------------------------------------//
	private void updateFromDisplay() {
		_UIBTFromDate.setText(new StringBuilder().append(mDay).append("-").append(mMonth + 1).append("-").append(mYear).append(" "));
	}


	private DatePickerDialog.OnDateSetListener mFromDateSetListener1 = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			updateFromDisplay();
		}
	};


	private void updateToDisplay() {
		_UIBTToDate.setText(new StringBuilder().append(mDay).append("-").append(mMonth + 1).append("-").append(mYear).append(" "));
	}


	private DatePickerDialog.OnDateSetListener mToDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			updateToDisplay();
		}
	};

	//---------------------------------------------------------------------------------------------------------------------------------//

	//-----------------------------------------------------------TIME UPDATES---------------------------------------------------------//
	private void updateFromTimeDisplay(int hour, int minute) {
		_UIBTFromTime.setText(new StringBuilder().append(pad(hour)).append(":").append(pad(minute)));
	}


	private TimePickerDialog.OnTimeSetListener mFromTimeSetListener =  new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			mHour = hourOfDay;
			mMinute = minute;
			updateFromTimeDisplay(mHour,mMinute);
		}
	};


	private void updateToTimeDisplay(int hour, int minute) {

		_UIBTToTime.setText(new StringBuilder().append(pad(hour)).append(":").append(pad(minute)));
	}


	private TimePickerDialog.OnTimeSetListener mToTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			mHour = hourOfDay;
			mMinute = minute;
			updateToTimeDisplay(mHour,mMinute);
		}
	};


	//---------------------------------------------------------------------------------------------------------------------------------//

	//-------------------------------------------------------------CREATING DIALOG FOR DATE AND TIME-----------------------------------//
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case FROM_DATE_DIALOG_ID:
			return new DatePickerDialog(this,mFromDateSetListener1,mYear, mMonth, mDay);

		case TO_DATE_DIALOG_ID:
			return new DatePickerDialog(this,mToDateSetListener,mYear, mMonth, mDay);

		case FROM_TIME_DIALOG_ID:
			return new TimePickerDialog(this, mFromTimeSetListener, mHour, mMinute, false);

		case TO_TIME_DIALOG_ID:
			return new TimePickerDialog(this, mToTimeSetListener, mHour, mMinute, false);

		case REPITION_TRIGGER:
			alert1 = new AlertDialog.Builder( this ).setTitle( "Repition" ).setSingleChoiceItems(_options, 0, new DialogSelectionClickHandler() ).create();
			return alert1;

		case GUESTS_INVITATION:

			return new AlertDialog.Builder(this).setTitle("Dialog with simple text").setPositiveButton("OK", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {

				}
			})

			.setMultiChoiceItems(_guestList, guestsChecked, new DialogInterface.OnMultiChoiceClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int position, boolean isChecked) {
					Toast.makeText(getBaseContext(), _guestList[position] + (isChecked ? "checked!" : "unchecked!"), 122).show();
				}
			})
			.create();
		}
		return null;
	}

	private static String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}


	//---------------------------------------------------------------------------------------------------------------------------------//
	private void getHandles() {
		_UIBTSubmit = (Button) findViewById(R.id.BTSubmitDetail);
		_UIBTFromDate = (Button) findViewById(R.id.BTFromDate);
		_UIBTFromTime = (Button) findViewById(R.id.BTFromTime); 
		_UIBTToDate = (Button) findViewById(R.id.BTToDate); 
		_UIBTToTime = (Button) findViewById(R.id.BTToTime);
		_UIBTCancel = (Button) findViewById(R.id.BTCancel);
		_UIBTRepition = (Button) findViewById(R.id.BTRepition);
		_UIBTGuest = (Button) findViewById(R.id.BTGuests);
		_UICBAllDay = (CheckBox)findViewById(R.id.checkBox1);


		_UIBTRepition.setOnClickListener(new ButtonClickHandler());
		_UIBTGuest.setOnClickListener(this);
		_UICBAllDay.setOnCheckedChangeListener(this);
		_UIBTSubmit.setOnClickListener(this);
		_UIBTFromDate.setOnClickListener(this);
		_UIBTFromTime.setOnClickListener(this);
		_UIBTToDate.setOnClickListener(this);
		_UIBTToTime.setOnClickListener(this);
		_UIBTCancel.setOnClickListener(this);
	}



	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.BTSubmitDetail:
			animatedStartActivity();
			break;
		case R.id.BTFromDate:
			showDialog(FROM_DATE_DIALOG_ID);
			break;

		case R.id.BTFromTime:
			showDialog(FROM_TIME_DIALOG_ID);
			break;

		case R.id.BTToDate:
			showDialog(TO_DATE_DIALOG_ID);
			break;

		case R.id.BTToTime:
			showDialog(TO_TIME_DIALOG_ID);
			break;

		case R.id.BTCancel:
			animatedStartActivity();
			break;

		case R.id.BTGuests:
			showDialog(GUESTS_INVITATION);
		default:
			break;
		}
	}


	@Override
	protected void onResume() {
		// animateIn this activity

		ActivitySwitcher.animationIn(findViewById(R.id.container), getWindowManager());
		super.onResume();
	}

	@Override
	protected void onStop() {
		finish();
		super.onStop();
	}

	private void animatedStartActivity() {
		final Intent intent = new Intent(getApplicationContext(),ACTCalendarActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		ActivitySwitcher.animationOut(findViewById(R.id.container), getWindowManager(), new ActivitySwitcher.AnimationFinishedListener() {

			@Override
			public void onAnimationFinished() {
				int KEY = 1101;
				startActivity(intent);
			}
		});
	}

	public class ButtonClickHandler implements View.OnClickListener {
		public void onClick( View view ) {
			showDialog( 0 );
		}
	}

	public class DialogSelectionClickHandler implements DialogInterface.OnClickListener
	{
		@Override
		public void onClick(DialogInterface dialog, int pos) {

			Toast.makeText(getBaseContext(),_options[pos]+"selected", Toast.LENGTH_SHORT).show();
			alert1.cancel();
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

		if(isChecked){


			updateFromTimeDisplay(START_HOUR_OF_DAY, START_MINUTE_OF_DAY);
			updateToTimeDisplay(END_HOUR_OF_DAY, END_MINUTE_OF_DAY);
		}else{
			updateFromTimeDisplay(mHour, mMinute);
			updateToTimeDisplay(mHour, mMinute);

		}

	}

}


//-----------------------retrieve all the guests selected--------------------------------------------------------// 

//for (int i = 0; i < _guestList.length; i++) {
//	if (guestsChecked[i]) {
//		Toast.makeText(getBaseContext(), _guestList[i] + " checked!", Toast.LENGTH_LONG).show();
//	}
//}
