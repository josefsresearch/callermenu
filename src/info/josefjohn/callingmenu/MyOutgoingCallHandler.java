/*
 * Copyright (C) February 2014
 * Project for Tal Lavian, tlavian@gmail.com
 * @author Josef John, josefjohn88@gmail.com
 */

package info.josefjohn.callingmenu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;
public class MyOutgoingCallHandler extends BroadcastReceiver {
	private ConnectivityManager connectivityManager;

	@Override
	public void onReceive(Context context, Intent intent) {
		if (!MainActivity.calling) {
			connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			String phoneNumber = getResultData();
			if (phoneNumber == null) {
				// No reformatted number, use the original
				phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
			}
			Log.i("PHONE NUMBER IS ", phoneNumber);
			MainActivity.cm = null;
			MainActivity.phoneNumber = phoneNumber;
			//if not connected b4 call, our app doesn't run
			if (connectedToInternet() && phoneNumber != null && HelperMethods.hasNumber(phoneNumber)) {
				//PRECHENE!, +1 formating? add to all? or bool split 3
				//do on server side
				//TODO
				Log.i("App Running", "We hav internet & phoneNumber");
				getCallerMenu(phoneNumber);
				if (MainActivity.cm == null) {
					Log.e("company menu", "returned null, no num"+phoneNumber);
				} else {
					// My app will bring up the call, so cancel the call broadcast
					setResultData(null);
					//if we have logged errors, upload error file
					if (MainActivity.sHasErrorsToSend) {
                        new ErrorPoster().execute();
					}
					Intent i = new Intent(context, CallerActivity.class);
					i.putExtra("PHONE_NUMBER", phoneNumber);
					i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(i);
					//bug cuz activity staying around when call 2nd time
					//i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
					//i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);//?
				}
			} else {
				Log.i("no internet or no number", "continue normal call");
			}
		} else {
			Log.i("Our app", "Making our call");
		}
	}

	private void getCallerMenu(String phoneNumber) {
		new RequestTask().execute();
		int i = 0;
		while (!MainActivity.gotMenu) {
			i++;
			if (i>10000000) {
				i = 0;
				Log.i("still waiting", "retrieving...");
			}
		}
		MainActivity.gotMenu = false;
		return;
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