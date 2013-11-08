package info.josefjohn.callingmenu;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;
//didnt declare as activity yet, do i need to? is it an activity
public class MyOutgoingCallHandler extends BroadcastReceiver {

	ConnectivityManager connectivityManager;
	//CompanyMenu cm;

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
			MainActivity.cm = null;
			//wont have it when real version
			//				MainActivity.numberToMenu = new HashMap<String, CompanyMenu>();
			//				CompanyMenu aa = new CompanyMenu("American Airlines", "18004337300", Constants.americanAirlinesNums, Constants.americanAirlinesOptions);
			//				MainActivity.numberToMenu.put("8004337300", aa);
			//				MainActivity.numberToMenu.put("18004337300", aa);
			//				MainActivity.numberToMenu.put("+18004337300", aa); //not necessary later on
			//here if already in cache? do it, else check internet
			//if not connected b4 call, our app doesn't run
			if (connectedToInternet() && phoneNumber != null) {
				//run get from db
				//PRECHENE!, +1 formating? add to all? or bool split 3
				//if (phoneNumber != null && MainActivity.numberToMenu.containsKey(phoneNumber)) {
				Log.i("we hav num", "running our app");
				//get json tree from database, show options
				getCallerMenu(phoneNumber);//TODO context is null
				if (MainActivity.cm == null) {
					Log.e("company menu", "returned null");
				} else {
					// My app will bring up the call, so cancel the broadcast
					setResultData(null);
					//MainActivity.phoneNum = phoneNumber;
					Intent i = new Intent(context, CallerActivity.class);
					i.putExtra("PHONE_NUMBER", phoneNumber);

					//i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);//?
					i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(i);
					//					//bug cuz activity staying around when call 2nd time
					//					i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

				}
				// Start my app to bring up the calls
			} else {
				Log.i("no internet", "continue normal call");

			}
			//} else {
			//	Log.i("no internet", "continue normal call");
			//}
		} else {
			Log.i("Calling", "SET TO TRUE!!!!");
		}
	}

	private void getCallerMenu(String phoneNumber) {
		//use phoneNumber, connect if none, return none, else return menu
		//return MainActivity.numberToMenu.get(phoneNumber);
		//TODO get json, if !null convert to CM return
		//TODO
		
		//InputStreamReader reader = new InputStreamReader(in);
		new RequestTask().execute();
		// read the JSON data
		int i = 0;
		while (!MainActivity.gotMenu) {
			i++;
			if (i>10000000) {
				i = 0;
				Log.i("still doing it", "hit");
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