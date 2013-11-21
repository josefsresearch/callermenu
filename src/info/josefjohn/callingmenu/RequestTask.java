package info.josefjohn.callingmenu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class RequestTask extends AsyncTask<String, String, String>{
	private ProgressDialog loadingDialog;
	private Context context;
	
	public RequestTask() {
		//this.context = context;
		//loadingDialog = new ProgressDialog(context);
	}
	
	@Override
    protected void onPostExecute(String result) {   
		//loadingDialog.cancel();
    }

    @Override
    protected void onPreExecute() {
    	super.onPreExecute();
    	//loadingDialog.setTitle("Please wait");
        //loadingDialog.show();
    }


	
	@Override
	protected String doInBackground(String... arg0) {
		try {
			if (MainActivity.phoneNumber == null) {
				Log.e("ERROR request task", "got null phone number");
			}
			URL url = new URL("http://copymysite.heroku.com/menus/menu/"+MainActivity.phoneNumber+"/");
			HttpURLConnection con = (HttpURLConnection) url
					.openConnection();
			MainActivity.cm = readStream(con.getInputStream());
			MainActivity.gotMenu = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private CompanyMenu readStream(InputStream in) {
		BufferedReader reader = null;
		StringBuilder response  = new StringBuilder();
		try {
			reader = new BufferedReader(new InputStreamReader(in));
			String line = "";
			while ((line = reader.readLine()) != null) {
				response.append(line);
				Log.i("line", line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		JSONObject json = null;
		try {
			json = new JSONObject(response.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("json is", "null");
		}
	
		return new CompanyMenu(json);
	} 
}