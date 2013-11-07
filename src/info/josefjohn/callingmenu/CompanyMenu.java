package info.josefjohn.callingmenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import android.util.SparseArray;

public class CompanyMenu {
	String companyName;
	String phone;
	SparseArray<String> menu;
	
//	CompanyMenu(JSONObject j) {
//		menu = new SparseArray<String>();
//		try {
//			companyName = j.getString("company");
//			phone = j.getString("phone");
//			JSONArray a = j.getJSONArray("menu");
//			JSONObject temp;
//			int num;
//			String name;
//			for (int i=0;i<a.length();i++) {
//				temp = (JSONObject) a.get(i);
//				num = temp.getInt("num");
//				name = temp.getString("option");
//				menu.put(num, name);
//			}
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
	CompanyMenu(String company, String phone, int[] nums, String[] options) {
		this.companyName = company;
		this.phone = phone;
		menu = new SparseArray<String>();
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
	String getOption(int i) {
		if (menu.get(i) == null) {
			return ",,,";
		}
		return menu.get(i);
	}
	
	int getNumChildren(int i) {
		int base = 10*i;
		boolean done = false;
		int size = 0;
		while (!done) {
			if (menu.get(base+size+1) != null) {
				size++;
			} else {
				done = true;
			}
		}
		return size;
	}
}
