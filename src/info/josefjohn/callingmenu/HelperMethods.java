package info.josefjohn.callingmenu;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Date;

import android.os.Environment;
import android.util.Log;


public class HelperMethods {
	private static FileWriter fw;
	private static PrintWriter w;

	
	public static String readInputStream(InputStream in) {
        return read(new InputStreamReader(in));
}

public static String readFile(String file) {
        FileInputStream in = null;
        try {
                in = new FileInputStream(file);
        } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                String error = "HM1 EXCEPTION @"+new Date().toString()+":"+e.getClass()+","+e.getMessage();
                saveError(error);
                MainActivity.sErrorLog.println(error);
                return null;//Constants.EMPTY
        }
        String ret = read(new InputStreamReader(in));
        try {
                in.close();
        } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                MainActivity.sErrorLog.println("HM2 EXCEPTION @"+new Date().toString()+":"+e.getClass()+","+e.getMessage());
        }
        return ret;
}

public static String read(InputStreamReader input) {
        StringBuilder sb = new StringBuilder();
        try {
                InputStreamReader inputStreamReader = input;
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                        sb.append(line);
                }
                bufferedReader.close();
                inputStreamReader.close();
        } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                MainActivity.sErrorLog.println("HM3 EXCEPTION @"+new Date().toString()+":"+e.getClass()+","+e.getMessage());
                return "null";
        } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                MainActivity.sErrorLog.println("HM4 EXCEPTION @"+new Date().toString()+":"+e.getClass()+","+e.getMessage());
                return "null";
        }
        return sb.toString();
}
	
	public static void saveError(String error) {
		try {
			MainActivity.sErrorFileLock.lock();
			try {
				fw = new FileWriter(new File(Environment.getExternalStorageDirectory(), Constants.ERROR_FILE), true);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				MainActivity.sErrorLog.println("HM5 EXCEPTION @"+new Date().toString()+":"+e.getClass()+","+e.getMessage());
			}
			w = new PrintWriter(fw);
			w.print(error);
			w.flush();
			w.close();
			try {
				fw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			MainActivity.sHasErrorsToSend = true;
		} catch (Exception e) {
			MainActivity.sErrorLog.println("HM6 EXCEPTION @"+new Date().toString()+":"+e.getClass()+","+e.getMessage());
		} finally {
			MainActivity.sErrorFileLock.unlock();
		}
	}

	public static boolean hasNumber(String phoneNumber) {
		for (String num:Constants.allCurNumbers) {
			if (phoneNumber.equals(num)) {
				return true;
			}
		}
		return false;
	}
}
