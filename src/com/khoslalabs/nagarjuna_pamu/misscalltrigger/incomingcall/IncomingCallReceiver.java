package com.khoslalabs.nagarjuna_pamu.misscalltrigger.incomingcall;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.android.internal.telephony.ITelephony;
import com.khoslalabs.nagarjuna_pamu.misscalltrigger.webservicecaller.WebserviceCallerService;

public class IncomingCallReceiver extends BroadcastReceiver {
	public IncomingCallReceiver() {
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO: This method is called when the BroadcastReceiver is receiving
		// an Intent broadcast.
		TelephonyManager manager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		
		AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE); 
        //Turn ON the mute
        audioManager.setStreamMute(AudioManager.STREAM_RING, true);
		
		String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);  
		if(state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
			String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER); 
		    Toast.makeText(context, incomingNumber, Toast.LENGTH_SHORT).show();
		    
		    //end call
		    try {
				Class<?> clazz = Class.forName(manager.getClass().getName());
				Method method = clazz.getDeclaredMethod("getITelephony");
				method.setAccessible(true);
				ITelephony itelephonyService = (ITelephony) method.invoke(manager);
				//itelephonyService.silenceRinger();
				Toast.makeText(context, "Call from "+incomingNumber+" ended", Toast.LENGTH_SHORT).show();
				itelephonyService.endCall();
			} catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | RemoteException e) {
				// TODO Auto-generated catch block
				Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}
		    
		    //send intent to webservice caller service
		    Intent send = new Intent(context, WebserviceCallerService.class);
		    Bundle info = new Bundle();
		    info.putString("phno", incomingNumber);
		    TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		   // String androidId = Secure.getString(context.getContentResolver(),  Secure.ANDROID_ID); 
		    info.putString("id", tm.getSimSerialNumber());
		    send.putExtras(info);
		    send.setFlags(1);
		    context.startService(send);
		}
		//unmute the phone
		audioManager.setStreamMute(AudioManager.STREAM_RING, false);
	}
}
