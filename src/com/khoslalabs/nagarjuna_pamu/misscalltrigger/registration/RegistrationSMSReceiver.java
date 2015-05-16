package com.khoslalabs.nagarjuna_pamu.misscalltrigger.registration;

import com.khoslalabs.nagarjuna_pamu.misscalltrigger.IntentFlags;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class RegistrationSMSReceiver extends BroadcastReceiver {
	public RegistrationSMSReceiver() {
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO: This method is called when the BroadcastReceiver is receiving
		// an Intent broadcast.
		//throw new UnsupportedOperationException("Not yet implemented");
		//retrieve the bundle form intent
		final Bundle bundle = intent.getExtras();
		 
		try {
		     
		    if (bundle != null) {
		         
		        final Object[] pdusObj = (Object[]) bundle.get("pdus");
		         
		        for (int i = 0; i < pdusObj.length; i++) {
		             
		            SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
		            String phoneNumber = currentMessage.getDisplayOriginatingAddress();
		             
		            String senderNum = phoneNumber;
		            String message = currentMessage.getDisplayMessageBody();
		            Log.d(RegistrationSMSReceiver.class.toString(), "senderNum: "+ senderNum + "; message: " + message);
		            //show toast
		            int duration = Toast.LENGTH_SHORT;
		            Toast toast = Toast.makeText(context, "senderNum: "+ senderNum + ", message: " + message, duration);
		            toast.show();
		            //send the message to the webservice for registration
		            if(message.contains("register")) {
		            	String[] strings = message.split("\\s+");
		            	String number = strings[0];
		            	String smsMsg = strings[1];
		            	Bundle smsBundle = new Bundle();
		            	smsBundle.putString("phno", number);
		            	smsBundle.putString("smsText",smsMsg);
		            	Intent registerIntent = new Intent(context, RegistrationClientService.class);
		            	registerIntent.setFlags(IntentFlags.REGISTER_INTENT);
		            	context.startActivity(registerIntent);
		            }
		        }
		      } 
		 
		} catch (Exception e) {
		    Log.e("SmsReceiver", "Exception smsReceiver" +e);
		     
		}
	}
}
