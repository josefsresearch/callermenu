/*
 * Copyright (C) February 2014
 * Project for Tal Lavian, tlavian@gmail.com
 * @author Josef John, josefjohn88@gmail.com
 */

package info.josefjohn.callingmenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import android.util.SparseArray;

public class CompanyMenu {
	String companyName;
	String phone;
	String key;
	HashMap<String, String> menu;
	List<String> responses;

	CompanyMenu(JSONObject jsonObj) {
		menu = new HashMap<String, String>();
		responses = new ArrayList<String>();
		try {
			companyName = jsonObj.getString("name");
			phone = jsonObj.getString("number");
			jsonObj.remove("name");
			jsonObj.remove("number");
			Iterator<String> jsonKeys = jsonObj.keys();
			while (jsonKeys.hasNext()) {
				key = jsonKeys.next();
				//Log.i("in json got", key);
				if (key.endsWith("?")) {
					responses.add(key);
				}
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
	
	List<String> getResponses() {
		return responses;
	}

	int getCount() {
		int i=0;
		for (String s:menu.keySet()) {
			if (s.contains("~")) {
				//dont add
			} else {
				i++;
			}
		}
		return i;
	}

	String[] getAllOptions() {
		Stack<String> stack = new Stack<String>();
		List<String> list = new ArrayList<String>();
		for (String s:Constants.allCharsReversed) {
			if (menu.containsKey(s)) {
				stack.add(s);
			}
		}
		while (stack.peek() != null) {
			String cur = stack.pop();
			list.add(menu.get(cur));
			for (String s:Constants.allCharsReversed) {
				if (menu.containsKey(cur+s)) {
					stack.add(cur+s);
				}
			}
		}
		String[] ret = new String[list.size()];
		for (int i=0;i<list.size();i++) {
			ret[i] = list.get(i);
		}
		return ret;
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

	public boolean has(String string) {
		if (menu.get(string) == null) {
			return false;
		} else {
			return true;
		}
	}
}
