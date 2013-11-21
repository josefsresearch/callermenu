package info.josefjohn.callingmenu;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	protected static boolean calling = false;
	protected static boolean gotMenu = false;
	Button done;
	protected static CompanyMenu cm = null;
	protected static Context mainContext;
	protected static String phoneNumber = null;
	
	protected static SDLogger sErrorLog;
	protected static ReentrantLock sErrorFileLock;
    protected static String errorFilepath;
    protected static boolean sHasErrorsToSend;
    protected static File sErrorFile;
    
    static {
    	sErrorFileLock = new ReentrantLock();
        sHasErrorsToSend = false;
        sErrorFile = new File(Environment.getExternalStorageDirectory(), Constants.ERROR_FILE);
        errorFilepath = sErrorFile.getPath();
        sErrorLog = new SDLogger("", "CMLog.txt");
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mainContext = this;
		done = (Button) findViewById(R.id.done);
		Log.i("CALLER MENU", "STARTED");
		done.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
