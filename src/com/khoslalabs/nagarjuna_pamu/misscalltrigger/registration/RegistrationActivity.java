package com.khoslalabs.nagarjuna_pamu.misscalltrigger.registration;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.khoslalabs.nagarjuna_pamu.misscalltriigger.R;

public class RegistrationActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.registration, menu);
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

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		public PlaceholderFragment() {
			
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_registration,
					container, false);
			
			final Button register = (Button)rootView.findViewById(R.id.register);
			
			register.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					SharedPreferences prefs = (SharedPreferences)PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
					if(register.getText().equals("Register")) {
						new RegistrationTask(getActivity(), register).execute(new String[]{prefs.getString("http", "http://192.168.2.25:9000/")+"register"});
					}else {
						new RegistrationTask(getActivity(), register).execute(new String[]{prefs.getString("http", "http://192.168.2.25:9000/")+"unregister"});
					}
					
				}
			});
			return rootView;
		}
	}
	public static class RegistrationTask extends AsyncTask<String, Void, Integer> {
		ProgressDialog dialog;
		Context context;
		Button button;
		public RegistrationTask(Context context, Button button) {
			this.context = context;
			dialog = new ProgressDialog(context);
			this.button = button;
		}
		@Override
		protected Integer doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			
			 DefaultHttpClient httpclient = new DefaultHttpClient();

			    //url with the post data
			    HttpPost httpost = new HttpPost(arg0[0]);
			    
			    JSONObject holder = new JSONObject();
				TelephonyManager tm = (TelephonyManager)context.getSystemService(context.TELEPHONY_SERVICE);
				try {
					holder.put("simId", tm.getSimSerialNumber());
				} catch (JSONException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				//String json = obj.toString();

			    //passes the results to a string builder/entity
			    StringEntity se = null;
				try {
					se = new StringEntity(holder.toString());
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			    //sets the post request as the resulting string
			    httpost.setEntity(se);
			    //sets a request header so the page receving the request
			    //will know what to do with it
			    httpost.setHeader("Accept", "application/json");
			    httpost.setHeader("Content-type", "application/json");

			    //Handles what is returned from the page 
			    //ResponseHandler responseHandler = new BasicResponseHandler();
			    int status = 404;
			    try {
			    	
					HttpResponse response = httpclient.execute(httpost/*, responseHandler*/);
					//int result = response.getStatusLine().getStatusCode();
					HttpEntity entity = response.getEntity();
					if(entity != null && response != null) {
						Scanner scan = new Scanner(entity.getContent());
						String result = scan.nextLine();
						JSONObject obj = new JSONObject(result);
						status = obj.getInt("status");
						scan.close();
					}
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			   
				 
				
			return status;
		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
			dialog.cancel();
		}

		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Toast.makeText(context, "status: "+result, Toast.LENGTH_SHORT).show();
			if(result == 0 || result == 1) {
				if(button.getText().equals("Register")) {
					button.setText("unregister");
				}else {
					button.setText("Register");
				}
			}
			dialog.cancel();
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog.show();
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}
		
	}
}
