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
	int curOption;
	CompanyMenu cm;
	String[] values;
	static String selection = "";
	String phoneNumber;
	

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_caller);
		final ListView listView = (ListView) findViewById(R.id.listview);
		curOption = 0;
		Intent intent = getIntent();
		phoneNumber = intent.getExtras().getString("PHONE_NUMBER");
		cm = MainActivity.numberToMenu.get(phoneNumber);
		values = getValues(cm, curOption);
		final ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < values.length; ++i) {
			list.add(values[i]);
		}
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, 
				android.R.layout.simple_list_item_1, list);
		listView.setAdapter(adapter);
		//this should already give commas?
		selection = cm.getOption(curOption);
		
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int index,
					long arg3) {
				Log.i("Clicked", String.valueOf(index));
				//map to number and click number
				int temp = curOption*10+index+1;//?
				Log.i("wait for"+String.valueOf(temp*10), cm.getOption(temp*10));
				selection += String.valueOf(index+1)+cm.getOption(temp*10);
				Log.i("cur option is", String.valueOf(curOption));
				if (cm.getNumChildren(temp) > 0) {
					curOption = temp;
					Log.i(String.valueOf(curOption)+" has ", String.valueOf(cm.getNumChildren(curOption)));
					values = getValues(cm, curOption);
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

	private String[] getValues(CompanyMenu cm, int i) {
		String[] ret;
		int numChildren = cm.getNumChildren(i);
		if (numChildren == 0) {
			Log.i("Got kids", "0");
			return null;
		}
		ret = new String[numChildren];
		for (int j=0;j<numChildren;j++) {
			ret[j] = cm.getOption(i*10 + j+1);
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
