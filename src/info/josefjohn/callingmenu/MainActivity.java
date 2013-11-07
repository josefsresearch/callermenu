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
	protected static boolean calling = false;
	Button done;
	protected static HashMap<String, CompanyMenu> numberToMenu;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		done = (Button) findViewById(R.id.done);
		sp = getSharedPreferences("COMPANY_MENU", MODE_PRIVATE);
		
		if (!sp.getBoolean("SET_UP_COMPLETE", false)) {
			runSetUp();
		} else {
			Log.i("Set up", "already set up");
		}
		Log.i("CALLER MENU", "STARTED");

		done.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//call("18004337300");AA
				finish();
			}
		});
	}
	
	//wont have it when real version
	private void runSetUp() {
		numberToMenu = new HashMap<String, CompanyMenu>();
		CompanyMenu aa = new CompanyMenu("American Airlines", "18004337300", Constants.americanAirlinesNums, Constants.americanAirlinesOptions);
		numberToMenu.put("8004337300", aa);
		numberToMenu.put("18004337300", aa);
		numberToMenu.put("+18004337300", aa);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
