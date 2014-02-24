/*
 * Copyright (C) February 2014
 * Project for Tal Lavian, tlavian@gmail.com
 * @author Josef John, josefjohn88@gmail.com
 */

package info.josefjohn.callingmenu;

import java.util.ArrayList;
import java.util.HashMap;

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
	String selection = "";
	String phoneNumber;
	String prevCur = "";
	String prevSelection = "";
	HashMap<String, String> responses;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_caller);
		final ListView listView = (ListView) findViewById(R.id.listview);
		responses = new HashMap<String, String>();
		for (String resp:MainActivity.cm.getResponses()) {
			responses.put(resp, "");
		}
		curOption = "";
		Intent intent = getIntent();
		phoneNumber = intent.getExtras().getString("PHONE_NUMBER");
		values = getValues(MainActivity.cm, curOption);
		if (values == null) {
			String error = "CA1 ERROR - getValues with "+phoneNumber+", and "+curOption+"got null";
			HelperMethods.saveError(error);
			call(phoneNumber);
		}
		final ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < values.length; ++i) {
			list.add(values[i]);
		}
		list.add("None");
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, 
				R.layout.list_row, R.id.textView1, list);
		listView.setAdapter(adapter);
		selection = MainActivity.cm.getOption("~");

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int index,
					long arg3) {
				if (index == values.length) {
					if (list.get(index) == "None") {
						call(phoneNumber);
					} else {
						Log.i("Back pressed", "Going back");
						if (curOption.length() <= 1) {
							curOption = "";
							selection = MainActivity.cm.getOption("~");
							updateList(curOption, false);
						} else {
							selection = prevSelection;
							curOption = prevCur;
							if (prevCur.length() > 1) {
								prevCur = curOption.substring(0, prevCur.length()-1);
								prevSelection = MainActivity.cm.getOption("~");
								String cur = "";
								for (int i=1;i<prevCur.length();i++) {
									cur = prevCur.substring(0, i);
									prevSelection += MainActivity.cm.getOption(cur);
									if (MainActivity.cm.getOption(cur+"~") != null) {
										prevSelection += MainActivity.cm.getOption(cur+"~");
									}
								}
							} else {
								prevCur = "";
								prevSelection = MainActivity.cm.getOption("~");
							}
							updateList(curOption, true);
						}
					}
				} else {
					String temp = curOption + Constants.allChars[index];
					//if there is a website to visit
					if (MainActivity.cm.has(temp+"_")) {
						//pop up confirmation, visit website
						//TODO
						Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(MainActivity.cm.getOption(temp+"_")));
						startActivity(myIntent);
					} else {
						//if there is a response to give
						if (MainActivity.cm.has(temp+"?")) {
							//pop up get response, update responses
							//TODO pop up with hashmap value, if ok add hashmapvalue else ?
							
						}
					prevSelection = selection;
					prevCur = curOption;
					MainActivity.cm.getOption(temp);
					selection += Constants.allChars[index]+MainActivity.cm.getOption(temp+"~");
					Log.i("cur option is", String.valueOf(curOption));
					if (MainActivity.cm.getNumChildren(temp) > 0) {
						curOption = temp;
						updateList(curOption, true);
					} else {
						Log.i("SELECTION is", selection);
						call(phoneNumber+","+selection);
					}
					}
				}
			}

			private void updateList(String curOption, boolean needBack) {
				values = getValues(MainActivity.cm, curOption);
				if (values == null) {
					String error = "CA2 ERROR - getValues with "+phoneNumber+", and "+curOption+"got null";
					HelperMethods.saveError(error);
					call(phoneNumber);
				} else {
					list.clear();
					for (int i = 0; i < values.length; ++i) {
						list.add(values[i]);
					}
					if (needBack) {
						list.add("Back");
					} else {
						list.add("None");
					}
					adapter.notifyDataSetChanged();
				}
			}
		});
	}

	//returns one level of the menu
	private String[] getValues(CompanyMenu cMenu, String s) {
		String[] ret;
		int numChildren = cMenu.getNumChildren(s);
		if (numChildren == 0) {
			return null;
		}
		ret = new String[numChildren];
		String[] allChars = Constants.allChars;
		for (int j=0;j<numChildren;j++) {
			ret[j] = cMenu.getOption(s+allChars[j]);
		}
		return ret;
	}

	private void call(String number) {
		MainActivity.calling = true;//do i need false in onPause?
		Log.i("CALLER_ACTIVITY", "CALLING "+number);
		PhoneCallListener phoneListener = new PhoneCallListener();
		TelephonyManager telephonyManager = (TelephonyManager) this
				.getSystemService(Context.TELEPHONY_SERVICE);
		telephonyManager.listen(phoneListener,
				PhoneStateListener.LISTEN_CALL_STATE);
		Intent callIntent = new Intent(Intent.ACTION_CALL);
		callIntent.setData(Uri.parse("tel:"+number));
		startActivity(callIntent);
	}

	private class PhoneCallListener extends PhoneStateListener {
		private boolean isPhoneCalling = false;
		String LOG_TAG = "CALL_LISTENER";

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			if (TelephonyManager.CALL_STATE_RINGING == state) {
				Log.i(LOG_TAG, "RINGING, number: " + incomingNumber);
			}

			if (TelephonyManager.CALL_STATE_OFFHOOK == state) {
				Log.i(LOG_TAG, "OFFHOOK");
				isPhoneCalling = true;
			}

			if (TelephonyManager.CALL_STATE_IDLE == state) {
				// run when class initial and phone call ended, need detect flag
				// from CALL_STATE_OFFHOOK
				Log.i(LOG_TAG, "IDLE");
				if (isPhoneCalling) {
					MainActivity.calling = false;
					MainActivity.cm = null;
					MainActivity.phoneNumber = null;
					isPhoneCalling = false;
					/////should i finish?
				}
			}
		}
	}

	protected void onPause() {
		super.onPause();
		Log.i("In Caller Activity", "onPause finishing");
		finish();//do i need this?
	}
}
