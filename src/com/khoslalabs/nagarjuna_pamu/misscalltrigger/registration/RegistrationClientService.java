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

import com.khoslalabs.nagarjuna_pamu.misscalltrigger.IntentFlags;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class RegistrationClientService extends Service {
	public RegistrationClientService() {
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO: Return the communication channel to the service.
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		int flag = intent.getFlags();
		if(flag == IntentFlags.REGISTER_INTENT) {
			
		}
		return super.onStartCommand(intent, flags, startId);
	}
	
	public static class RegistrationTask extends AsyncTask<String, Void, Integer> {
		private Context context;
		public RegistrationTask(Context context){
			this.context = context;
		}
		@Override
		protected Integer doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(arg0[0]);
			JSONObject jo = new JSONObject();
			TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
			String simSerialNumber = tm.getSimSerialNumber();
			int status = 404;
			try {
				jo.put("simId", simSerialNumber);
				StringEntity se = new StringEntity(jo.toString());
				httpPost.setEntity(se);
				httpPost.setHeader("Accept", "application/json");
			    httpPost.setHeader("Content-type", "application/json");
			    HttpResponse response = httpClient.execute(httpPost);
			    HttpEntity entity = response.getEntity();
			    if(entity != null) {
			    	Scanner scan = new Scanner(entity.getContent());
					String result = scan.nextLine();
					JSONObject obj = new JSONObject(result);
					status = obj.getInt("status");
					scan.close();
			    }
			} catch (JSONException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return status;
		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Toast.makeText(context, "status: "+result, Toast.LENGTH_SHORT).show();
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}
		
		
	}
	
	
}
