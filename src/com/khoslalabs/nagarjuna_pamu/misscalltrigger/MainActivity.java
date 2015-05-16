package com.khoslalabs.nagarjuna_pamu.misscalltrigger;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.khoslalabs.nagarjuna_pamu.misscalltrigger.registration.RegistrationActivity;
import com.khoslalabs.nagarjuna_pamu.misscalltrigger.webservicecaller.WebserviceCallerService;
import com.khoslalabs.nagarjuna_pamu.misscalltriigger.R;

public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
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
			/**
			 * start Preference Activity
			 */
			startActivity(new Intent(getApplicationContext(), PreferenceActivity.class));
			return true;
		}
		if(id == R.id.register){
			/**
			 * start Registration Activity
			 */
			startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			/**
			 * Button for starting service
			 */
			Button button = (Button)rootView.findViewById(R.id.button1);
			/**
			 * click listener for start service button
			 */
			button.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					/**
					 * send intent to the service
					 */
					Intent intent = new Intent(getActivity(), WebserviceCallerService.class);
					getActivity().startService(intent);
				}
			});
			/**
			 * Button for stopping the service
			 */
			Button stop = (Button)rootView.findViewById(R.id.button2);
			stop.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					/**
					 * send intent to the service
					 */
					Intent intent = new Intent(getActivity(), WebserviceCallerService.class);
					getActivity().stopService(intent);	
				}
			});
			
			/**
			 * text view for showing phone number
			 */
			final TextView tv = (TextView) rootView.findViewById(R.id.textView1);
			tv.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					/**
					 * get telephony manager to get phone number
					 * notice this doesn't work as phone number is not available
					 * and can't be accessed programatically
					 */
					TelephonyManager tm = (TelephonyManager)getActivity().getSystemService(TELEPHONY_SERVICE); 
					String number = tm.getLine1Number();
					if(number == null) {
						tv.setText("null");
					}else {
						if(number.equals("")) {
							tv.setText("empty number");
						}else {
							tv.setText(number);
						}
						
					}
					
					Toast.makeText(getActivity(), "number "+number, Toast.LENGTH_SHORT).show();
				}
			});
			
			/**
			 * text view for showing serial number
			 */
			final TextView ssn = (TextView)rootView.findViewById(R.id.ssn);
			ssn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Context context = getActivity().getApplicationContext();
					TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
					ssn.setText("SSN: "+tm.getSimSerialNumber());
				}
			});
			return rootView;
		}
	}

}
