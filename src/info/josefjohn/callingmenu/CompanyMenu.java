package info.josefjohn.callingmenu;

import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import android.util.SparseArray;

public class CompanyMenu {
	String companyName;
	String phone;
	String key;
	HashMap<String, String> menu;

	CompanyMenu(JSONObject jsonObj) {
		menu = new HashMap<String, String>();
		try {
			companyName = jsonObj.getString("name");
			phone = jsonObj.getString("number");
			jsonObj.remove("name");
			jsonObj.remove("number");
			Iterator<String> jsonKeys = jsonObj.keys();
			while (jsonKeys.hasNext()) {
				key = jsonKeys.next();
				//Log.i("in json got", key);
				menu.put(key, jsonObj.getString(key));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	CompanyMenu(String company, String phone, String[] nums, String[] options) {
		this.companyName = company;
		this.phone = phone;
		menu = new HashMap<String, String>();
		//initialization check, error if 2 arrays not matching
		if (nums.length != options.length) {
			//throw error
			Log.e("ERROR", "Diff lengths");
		}
		for (int i=0;i<nums.length;i++) {
			//Log.i("Adding"+String.valueOf(nums[i]),", "+options[i]);
			menu.put(nums[i], options[i]);
		}
	}

	String getCompanyName() {
		return companyName;
	}

	String getPhoneNum() {
		return phone;
	}

	//temp? or ,, or ,,,
	String getOption(String s) {
		//Log.i("Getting string", s);
		if (menu.get(s) == null) {
			Log.e("not sure", "some error");
			return "";
		}
		//Log.i("getting", menu.get(s));
		return menu.get(s);
	}

	int getNumChildren(String s) {
		String[] allChars = Constants.allChars;
		boolean done = false;
		int i = 0;
		while (i < allChars.length && !done) {
			if (menu.get(s+allChars[i]) != null) {
				i++;
			} else {
				done = true;
			}
		}
		return i;
	}
}
