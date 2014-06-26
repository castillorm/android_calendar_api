package com.culebracut.displayallnonrecurringevents;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.ListActivity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

public class MainActivity extends ListActivity {

	// protected String[] mEventTitles = { "Title 1", "Title 2", "Title 3" };
	protected ArrayList<String> mEventTitles = new ArrayList<String>();
	protected ArrayList<String> mCalendarIds = new ArrayList<String>();
	public static final String TAG = MainActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		
		// 1. Get all of the calendar ID's and populate the mCalendarIds
		// arraylist
		long[] calIds = obtainCalendarIds();
		
		// 2. Populate the mEventTitles arraylist
		
		if (calIds.length > 0) {
			getAllEvents();
		}

		// 3. Send the list of events to the user's screen
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, mEventTitles);
		setListAdapter(adapter);

	}

	public void getAllEvents() {

		String[] EVENT_PROJECTION = new String[] {
				CalendarContract.Events.CALENDAR_ID,
				CalendarContract.Events.TITLE, CalendarContract.Events.DTSTART, CalendarContract.Events.DTEND };

		ContentResolver resolver = getContentResolver();
		
		Calendar c_start = new GregorianCalendar();
		c_start.set(Calendar.YEAR, 2014);
		c_start.set(Calendar.MONTH, 6); // 6 = July
		c_start.set(Calendar.DAY_OF_MONTH, 1); // new years eve
		c_start.set(Calendar.HOUR_OF_DAY, 1);
		c_start.set(Calendar.MINUTE, 0);
		c_start.set(Calendar.SECOND,0);
		c_start.set(Calendar.MILLISECOND,0);		

		Calendar c_end = new GregorianCalendar();
		c_end.set(Calendar.YEAR, 2014);
		c_end.set(Calendar.MONTH, 6); // 6 = July
		c_end.set(Calendar.DAY_OF_MONTH, 5); // July 4th
		c_end.set(Calendar.HOUR_OF_DAY, 1);
		c_end.set(Calendar.MINUTE, 0);
		c_end.set(Calendar.SECOND,0);
		c_end.set(Calendar.MILLISECOND,0);
		
		final String selection = Events.DTSTART+">= ? AND "+Events.DTEND+"<= ?";
		String startDateString = c_start.getTimeInMillis()+"";
		String endDateString = c_end.getTimeInMillis()+"";
		String[] selectionArgs = new String[] { startDateString, endDateString };
		String sortOrder = null;
		sortOrder = Events.DTSTART+ " ASC";

		Cursor eventCursor = resolver.query(Uri.parse("content://com.android.calendar/events"),EVENT_PROJECTION, selection, selectionArgs, sortOrder);
		

		while (eventCursor.moveToNext()) {
			final String calID = eventCursor.getString(0);
			final String title = eventCursor.getString(1);
			final long dtStart = eventCursor.getLong(2);
			String formatString = "dd-MM-yyyy";
			formatString = "E yyyy.MM.dd 'at' hh:mm:ss a zzz";
			formatString = "EEE, MMM d, ''yy";

			SimpleDateFormat formatter = new SimpleDateFormat(formatString);
			String startDate = formatter.format(dtStart);
			
			mEventTitles.add("Calendar ID: " + calID + ", Title: " + title + ", Start Date: " + startDate);
		}

	}	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	long[] obtainCalendarIds() {
		String[] returnColumns = new String[] { CalendarContract.Calendars._ID, // 0
				CalendarContract.Calendars.ACCOUNT_NAME, // 1
				CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, // 2
				CalendarContract.Calendars.ACCOUNT_TYPE // 3
		};

		Cursor cursor = null;
		ContentResolver cr = getContentResolver();

		// Call query to get all rows from the Calendars table
		cursor = cr.query(CalendarContract.Calendars.CONTENT_URI,
				returnColumns, null, null, null);

		long calIds[] = new long[cursor.getCount()];
		int calIdIndex = 0;

		while (cursor.moveToNext()) {
			long calID = 0;
			String displayName = null;
			String accountName = null;
			String accountType = null;

			// Get the field values
			calID = cursor.getLong(0);
			displayName = cursor.getString(1);
			accountName = cursor.getString(2);
			accountType = cursor.getString(3);
			Log.i(TAG, String.format("ID=%d  Display=%s  Account=%s  Type=%s",
					calID, displayName, accountName, accountType));
			calIds[calIdIndex] = calID;

		}

		cursor.close();

		return calIds;
	}

}
