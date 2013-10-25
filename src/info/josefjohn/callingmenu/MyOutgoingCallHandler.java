package info.josefjohn.callingmenu;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;
//didnt declare as activity yet, do i need to? is it an activity
public class MyOutgoingCallHandler extends BroadcastReceiver {

	ConnectivityManager connectivityManager;
	CompanyMenu cm;

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i("CALLING", "set as"+String.valueOf(MainActivity.calling));
		if (!MainActivity.calling) {
			connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			// Extract phone number reformatted by previous receivers
			String phoneNumber = getResultData();
			if (phoneNumber == null) {
				// No reformatted number, use the original
				phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
			}
			Log.i("PHONE NUMBER IS ", phoneNumber);
			//here if already in cache? do it, else check internet
			//if not connected b4 call, our app doesn't run
			//if (connectedToInternet()) {
			//run get from db
			//PRECHENE!, +1 formating? add to all? or bool split 3
			if (MainActivity.numberToMenu.containsKey(phoneNumber)) {
				Log.i("we hav num", "running our app");
				//get json tree from database, show options
				cm = getCallerMenu(phoneNumber);
				if (cm == null) {
					Log.e("company menu", "returned null");
				} else {
					// My app will bring up the call, so cancel the broadcast
					setResultData(null);
					MainActivity.phoneNum = phoneNumber;
					Intent i = new Intent(context, CallerActivity.class);
					//MainActivity
					i.putExtra("PHONE_NUMBER", phoneNumber);
					//i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);//?
					i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(i);
					//					//bug cuz activity staying around when call 2nd time
					//					i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

				}
				// Start my app to bring up the calls
			} else {
				Log.i("no num", "continue normal call");

			}
			//} else {
			//	Log.i("no internet", "continue normal call");
			//}
		} else {
			Log.i("Calling", "SET TO TRUE!!!!");
		}
	}

	private CompanyMenu getCallerMenu(String phoneNumber) {
		//use phoneNumber, connect if none, return none, else return menu
		return MainActivity.numberToMenu.get(phoneNumber);
	}

	private boolean connectedToInternet() {
		if (connectivityManager.getActiveNetworkInfo() != null &&
				connectivityManager.getActiveNetworkInfo().isConnected()) {
			return true;
		} else {
			return false;
		}
	}

}