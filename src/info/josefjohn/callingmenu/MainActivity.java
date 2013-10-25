package info.josefjohn.callingmenu;

import java.util.HashMap;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	public final Context context = this;
	protected static boolean calling = false;
	static ConnectivityManager connectivityManager;
	protected static String phoneNum;
	Button done;
	protected static HashMap<String, CompanyMenu> numberToMenu;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		sp = getSharedPreferences("COMPANY_MENU", MODE_PRIVATE);
		
		done = (Button) findViewById(R.id.done);
		if (!sp.getBoolean("SET_UP_COMPLETE", false)) {
			runSetUp();
		}
		Log.i("CALLER MENU", "STARTED");
//		Intent i = getIntent();
//		if (i == null) {
//			
//		}
		done.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//call("18004337300");
				finish();
			}
		});
	}





	
	@Override
	public void onResume() {
		super.onResume();
//		if (phoneNum != null && cm != null) {
//			Intent i = new Intent(context, CallerActivity.class);
//			i.putExtra("PHONE_NUMBER", phoneNum);
//			startActivityForResult(i, 0);
//		}
	}
	
	private void call(String phoneNumber, String selection) {
	    try {
	        Intent callIntent = new Intent(Intent.ACTION_CALL);
	        callIntent.setData(Uri.parse("tel:"+phoneNumber+selection));
	        startActivity(callIntent);
	    } catch (ActivityNotFoundException e) {
	        Log.e("sample call in android", "Call failed", e);
	    }
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		Log.i("REQUEST CODE IS", String.valueOf(requestCode));
		if (resultCode != RESULT_CANCELED){
			if (requestCode == 0) {
				call(intent.getStringExtra("PHONE_NUMBER"), intent.getStringExtra("SELECTION"));
				finish();
			}
		}
	}
	
	private void runSetUp() {
		numberToMenu = new HashMap<String, CompanyMenu>();
		CompanyMenu aa = new CompanyMenu("American Airlines", "18004337300", Constants.americanAirlinesNums, Constants.americanAirlinesOptions);
		numberToMenu.put("18004337300", aa);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
