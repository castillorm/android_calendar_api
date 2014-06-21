package com.culebracut.displayallcalendars;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

public class MainActivity extends ListActivity {
	
	protected String[] mBlogPostTitles = { "Title 1", "Title 2", "Title 3" };
	ArrayList<String> mCalendarNames = new ArrayList<String>();
	public static final String TAG = MainActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		listCalendars();
		
		//ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, mBlogPostTitles);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, mCalendarNames);
		setListAdapter(adapter);

		/*if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}*/
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
	
	void listCalendars(){
        String[] returnColumns = new String[] {
            CalendarContract.Calendars._ID,                     // 0
            CalendarContract.Calendars.ACCOUNT_NAME,            // 1
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,   // 2
            CalendarContract.Calendars.ACCOUNT_TYPE             // 3
        };

        Cursor cursor = null;
        ContentResolver cr = getContentResolver();

        // Call query to get all rows from the Calendars table
        cursor = cr.query(CalendarContract.Calendars.CONTENT_URI, returnColumns, null, null, null);

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
            mCalendarNames.add("Calendar ID = " + displayName+", " + "Display Name = " + accountName +", " + "Type = " + accountType);
        }

        cursor.close();
    }

	/**
	 * A placeholder fragment containing a simple view.
	 */
	/*public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}*/

}
