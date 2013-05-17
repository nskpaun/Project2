package nkspaun.project190.vyne;

import com.google.android.gcm.GCMBaseIntentService;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class GCMIntentService extends GCMBaseIntentService{
	
	@Override
	protected void onMessage(Context arg0, Intent arg1) {
	Log.d("BAM", "RECIEVED A MESSAGE");
	// Get the data from intent and send to notificaion bar
		//generateNotification(arg0, arg1.getStringExtra("message"));
	 }

	@Override
	protected void onError(Context arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onRegistered(Context arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onUnregistered(Context arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

}
