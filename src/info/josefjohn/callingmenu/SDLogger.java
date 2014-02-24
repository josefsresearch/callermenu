/*
 * Copyright (C) February 2014
 * @author Josef John, josefjohn88@gmail.com
 */

package info.josefjohn.callingmenu;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import android.os.Environment;
import android.util.Log;

/*
 * This class just provides a layer of abstraction for writing to SD Card
 * Used for error logging
 */
public class SDLogger {
        PrintWriter logWriter;
        String fileName;
        boolean dirExists = false;
        String dirName;
        String filepath;
        
        public SDLogger(String dir, String filename) {
                try {
                        File sdDir = Environment.getExternalStorageDirectory();
                        if (sdDir.canWrite()) {
                                File logFile;
                                if (dir == "") {
                                        logFile = new File(sdDir, filename);
                                        FileWriter logFileWriter = new FileWriter(logFile, true);
                                        logWriter = new PrintWriter(logFileWriter);
                                        filepath = sdDir+"/"+filename;
                                }
                                else {
                                        dirExists = true;
                                        dirName = dir;
                                        File appDir = new File(sdDir+"/"+dir);
                                        appDir.mkdir();
                                        logFile = new File(appDir, filename);
                                        FileWriter logFileWriter = new FileWriter(logFile, true);
                                        logWriter = new PrintWriter(logFileWriter);
                                        filepath = appDir+"/"+filename;
                                }
                        }
                        this.fileName = filename;
                } catch (IOException e) {
                        Log.e("SD LOGGER EXCEPTION", "failed to initialize PrintWriter", e);
                        String error = "SD1 EXCEPTION @"+new Date().toString()+":"+e.getClass()+","+e.getMessage();
                        HelperMethods.saveError(error);
                } 
        }
        
        public String getPath() {
                return filepath;
        }
        
        public void write(String s) {
                if (logWriter != null) {
                        logWriter.write(s);
                        //logWriter.flush();
                } else {
                        Log.e("SD LOGGER ERROR", "failed to initialize PrintWriter");
                        String error = "SD2 ERROR @"+new Date().toString()+":failed to initialize PrintWriter";
                        HelperMethods.saveError(error);
                }
        }
        
        public void println(String s) {
                if (logWriter != null) {
                        logWriter.print(s+"\r\n");
                        Log.i("SDLogger", "logging into file"+s);
                        logWriter.flush();
                } else {
                        Log.e("SD LOGGER ERROR", "failed to initialize PrintWriter");
                        String error = "SD3 ERROR @"+new Date().toString()+":failed to initialize PrintWriter";
                        HelperMethods.saveError(error);
                }
        }
        
        public void print(String s) {
                if (logWriter != null) {
                        logWriter.print(s);
                        //logWriter.flush();
                } else {
                        Log.e("SD LOGGER ERROR", "failed to initialize PrintWriter");
                        String error = "SD4 ERROR @"+new Date().toString()+":failed to initialize PrintWriter";
                        HelperMethods.saveError(error);
                }
        }

        //this copies from buffer to file, must be called at the end or won't write to file
        public void flush() {
                logWriter.flush();
        }

        public void println(Object printStackTrace) {
                // TODO Auto-generated method stub
                this.println(String.valueOf(printStackTrace));
        }
}