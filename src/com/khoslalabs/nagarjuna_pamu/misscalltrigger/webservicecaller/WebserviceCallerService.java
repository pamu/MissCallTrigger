package com.khoslalabs.nagarjuna_pamu.misscalltrigger.webservicecaller;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.khoslalabs.nagarjuna_pamu.misscalltrigger.MainActivity;
import com.khoslalabs.nagarjuna_pamu.misscalltriigger.R;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketConnectionHandler;
import de.tavendo.autobahn.WebSocketException;

public class WebserviceCallerService extends Service {
	public final static String TAG = "com.khoslalabs.nagarjuna_pamu.misscalltrigger.WebserviceCallerService";
	/**
	 * websocket connection
	 */
	final WebSocketConnection mConnection = new WebSocketConnection();
	public WebserviceCallerService() {
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
		start();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		stopForeground(true);
		if(mConnection != null) {
			mConnection.disconnect();
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		int flag = intent.getFlags();
		if(flag == 1) {
			Bundle args = intent.getExtras();
			String simNo = args.getString("id");
			String phno = args.getString("phno");
			JSONObject json = new JSONObject();
			try {
				json.put("simNumber", simNo);
				json.put("phno", phno);
				String msg = json.toString();
				sendEnsuringConnection(msg);
				Toast.makeText(getApplicationContext(), "msg to be sent: "+msg, Toast.LENGTH_SHORT).show();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return super.onStartCommand(intent, flags, startId);
	}
	
	public void start() {
		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
	    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|
	                 Intent.FLAG_ACTIVITY_SINGLE_TOP);
		Notification notification = new Notification(R.drawable.ic_launcher, "Service running ...",
                System.currentTimeMillis());
		PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
		notification.setLatestEventInfo(getApplicationContext(), "Miss Call Trigger", "Listening ...", pi);
		notification.flags|=Notification.FLAG_NO_CLEAR;
		startForeground(1234, notification);
	}
	
	private void connectSendingMessage(final String msg) {
		if(!mConnection.isConnected()) {
			toast("trying to connect");
			try { 
				SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
				toast("WS URL -> "+prefs.getString("ws", "ws://192.168.2.19:9000/"));
				mConnection.connect(prefs.getString("ws", "ws://192.168.2.19:9000/")+"foo", new WebSocketConnectionHandler() {

					@Override
					public void onClose(int code, String reason) {
						// TODO Auto-generated method stub
						super.onClose(code, reason);
						
						Log.d(TAG, "code: "+code+" reason: "+reason);
					}

					@Override
					public void onOpen() {
						// TODO Auto-generated method stub
						super.onOpen();
						
						mConnection.sendTextMessage(msg);
					}

					@Override
					public void onTextMessage(String payload) {
						// TODO Auto-generated method stub
						super.onTextMessage(payload);
						
					}
					
				});
			} catch (WebSocketException e) {
				// TODO Auto-generated catch block
				Log.d(TAG, e.getMessage());
				toast("failed to send");
				e.printStackTrace();
			}
		}
	}
	
	public void sendEnsuringConnection(String msg) {
		if(!mConnection.isConnected()) {
			connectSendingMessage(msg);
		}else {
			mConnection.sendTextMessage(msg);
		}
	}
	
	public void toast(String msg) {
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
	}
	
}
