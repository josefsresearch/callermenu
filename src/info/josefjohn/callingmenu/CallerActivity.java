package info.josefjohn.callingmenu;

import java.util.ArrayList;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class CallerActivity extends Activity {
	String curOption;
	String[] values;
	static String selection = "";
	String phoneNumber;
	

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_caller);
		final ListView listView = (ListView) findViewById(R.id.listview);
		curOption = "";
		Intent intent = getIntent();
		phoneNumber = intent.getExtras().getString("PHONE_NUMBER");
		//cm = MainActivity.numberToMenu.get(phoneNumber);
		values = getValues(MainActivity.cm, curOption);
		final ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < values.length; ++i) {
			list.add(values[i]);
		}
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, 
				android.R.layout.simple_list_item_1, list);
		listView.setAdapter(adapter);
		//this should already give commas?
		selection = MainActivity.cm.getOption("~");
		
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int index,
					long arg3) {
				Log.i("Clicked", String.valueOf(index));
				Log.i("means clicked", Constants.allChars[index]);
				//map to number and click number
				String temp = curOption + Constants.allChars[index];//?
				Log.i("now temp is ", temp);
				MainActivity.cm.getOption(temp);
				Log.i("wait for"+temp, MainActivity.cm.getOption(temp+"~"));
				selection += Constants.allChars[index]+MainActivity.cm.getOption(temp+"~");
				Log.i("cur option is", String.valueOf(curOption));
				if (MainActivity.cm.getNumChildren(temp) > 0) {
					curOption = temp;
					Log.i(String.valueOf(curOption)+" has ", String.valueOf(MainActivity.cm.getNumChildren(curOption)));
					values = getValues(MainActivity.cm, curOption);
					list.clear();
					for (int i = 0; i < values.length; ++i) {
						list.add(values[i]);
					}
					adapter.notifyDataSetChanged();
					Log.i("SELECTION is", selection);
				} else {
					Log.i("SELECTION is", selection);
					MainActivity.calling = true;//do i need to false onPause?
					call(phoneNumber+selection);
					//Log.i("calling", "finish");
					//MainActivity.calling = false;
					//finish();
				}
			}
		});
		//Log.i("NUM GOT IS ", num);
		//call(num);
	}

	private String[] getValues(CompanyMenu cMenu, String s) {
		String[] ret;
		int numChildren = cMenu.getNumChildren(s);
		if (numChildren == 0) {
			Log.i("Got kids", "0");
			return null;
		}
		ret = new String[numChildren];
		String[] allChars = {"1","2","3","4","5","6","7","8","9","*","0","#"};
		for (int j=0;j<numChildren;j++) {
			ret[j] = cMenu.getOption(s+allChars[j]);
		}
		return ret;
	}

	private void call(String number) {
		Log.i("CALLER_ACTIVITY", "CALLING "+number);
		//**
		PhoneCallListener phoneListener = new PhoneCallListener();
        TelephonyManager telephonyManager = (TelephonyManager) this
                .getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(phoneListener,
                PhoneStateListener.LISTEN_CALL_STATE);
		//**
		Intent callIntent = new Intent(Intent.ACTION_CALL);
		callIntent.setData(Uri.parse("tel:"+number));
		startActivity(callIntent);
		//when done, MainActivity.calling = false;
	}
	
	//**
	private class PhoneCallListener extends PhoneStateListener {

        private boolean isPhoneCalling = false;

        String LOG_TAG = "LOGGING 123";

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            if (TelephonyManager.CALL_STATE_RINGING == state) {
                // phone ringing
                Log.i(LOG_TAG, "RINGING, number: " + incomingNumber);
            }

            if (TelephonyManager.CALL_STATE_OFFHOOK == state) {
                // active
                Log.i(LOG_TAG, "OFFHOOK");

                isPhoneCalling = true;
            }

            if (TelephonyManager.CALL_STATE_IDLE == state) {
                // run when class initial and phone call ended, need detect flag
                // from CALL_STATE_OFFHOOK
                Log.i(LOG_TAG, "IDLE");

                if (isPhoneCalling) {

                    Log.i(LOG_TAG, "restart app");

                    //** restart app
//                    Intent i = getBaseContext().getPackageManager()
//                            .getLaunchIntentForPackage(
//                                    getBaseContext().getPackageName());
//                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(i);
                    MainActivity.calling = false;
                    MainActivity.cm = null;
                    Log.i("main calling", String.valueOf(MainActivity.calling));
                    isPhoneCalling = false;
                    /////should i finish?
                }

            }
        }
	}
	//**
	
//	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
//        Log.i("REQUEST CODE IS", String.valueOf(requestCode));
//        if (resultCode == RESULT_CANCELED){
//        	Log.e("in onActivityResult", "some problem");
//        } else if (requestCode == 1) {
//        	MainActivity.calling = false;
//        	Log.i("MAIN ACTIVITY CALLING IS ", String.valueOf(MainActivity.calling));
//        	finish();
//        } else {
//        	Log.e("unknown errorCode",String.valueOf(requestCode));
//        }
//	}
	
	//?
	protected void onPause() {
		super.onPause();
		finish();
	}
}
