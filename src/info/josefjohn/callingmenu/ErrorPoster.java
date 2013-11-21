package info.josefjohn.callingmenu;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;

import android.os.AsyncTask;
import android.util.Log;

public class ErrorPoster  extends AsyncTask<Object, Integer, String> {
    static File f;
    static FileWriter fw;
    static PrintWriter w;
    static File sdDir;
    static int updateT =0;
    
    public ErrorPoster() {
            // TODO Auto-generated constructor stub
            super();
    }
    
    static void post() {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(Constants.POST_ERRORS_URI);
            //MultipartEntity multipartEntity = new MultipartEntity();
            BasicHttpResponse httpResponse = null;
            MainActivity.sErrorFileLock.lock();
            try {
                    Log.i("SENDING ERROR_FILE", HelperMethods.readFile(MainActivity.errorFilepath));
            } catch (Exception e) {
                    e.printStackTrace();                
                    MainActivity.sErrorLog.println("EXCEPTION:"+e.getClass()+","+e.getMessage());
            }
            try {
                    File fi = new File(MainActivity.errorFilepath);
                    //multipartEntity.addPart("name", new StringBody("test.txt"));
                    //multipartEntity.addPart("type", new StringBody("text"));
                    //multipartEntity.addPart("error", new FileBody(fi));
                    HttpEntity multipartEntity = null; //get rid of this
                    httpPost.setEntity(multipartEntity);
                    Log.i("in posterror", "executing httpPost");
            //} catch (UnsupportedEncodingException e1) {
              //      e1.printStackTrace();
                //    MainActivity.sErrorLog.println("PE1 EXCEPTION @"+new Date().toString()+":"+e1.getClass()+","+e1.getMessage());
            } catch (Exception e) {
                    e.printStackTrace();
                    MainActivity.sErrorLog.println("PE1.1 EXCEPTION @"+new Date().toString()+":"+e.getClass()+","+e.getMessage());
            }  finally {
                    MainActivity.sErrorFileLock.unlock();
            }
            try {
                    httpResponse = (BasicHttpResponse) httpClient.execute(httpPost);
            } catch (ClientProtocolException e) {
                    e.printStackTrace();
                    MainActivity.sErrorLog.println("PE2 EXCEPTION @"+new Date().toString()+":"+e.getClass()+","+e.getMessage());
            } catch (Exception e) {
                    e.printStackTrace();
                    MainActivity.sErrorLog.println("PE2.1 EXCEPTION @"+new Date().toString()+":"+e.getClass()+","+e.getMessage());
            }
            String totalResponse = null;
            try {
                    Log.i("postError received", httpResponse.getStatusLine().toString()+", "+
                                    httpResponse.getProtocolVersion().toString());
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                            //TODO SUCCESS
                    } else {
                            //TODO FAIL
                    }
                    totalResponse = HelperMethods.readInputStream(httpResponse.getEntity().getContent());
                    Log.i("postError results", totalResponse.toString());
            } catch (IllegalStateException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                    MainActivity.sErrorLog.println("PE3 EXCEPTION @"+new Date().toString()+":"+e1.getClass()+","+e1.getMessage());
            } catch (IOException e2) {
                    // TODO Auto-generated catch block
                    e2.printStackTrace();
                    MainActivity.sErrorLog.println("PE3.1 EXCEPTION @"+new Date().toString()+":"+e2.getClass()+","+e2.getMessage());
            } catch (Exception e) {
                    e.printStackTrace();
                    MainActivity.sErrorLog.println("PE3.2 EXCEPTION @"+new Date().toString()+":"+e.getClass()+","+e.getMessage());
            }
            MainActivity.sErrorFileLock.lock();
            try {
                    //TODO ? only if success post
                    new File(MainActivity.errorFilepath).delete();
                    MainActivity.sHasErrorsToSend = false;
            } catch (Exception e) {
                    e.printStackTrace();
                    MainActivity.sErrorLog.println("PE4 EXCEPTION @"+new Date().toString()+":"+e.getClass()+","+e.getMessage());
            } finally {
                    MainActivity.sErrorFileLock.unlock();
            }
    }


    @Override
    protected String doInBackground(Object... arg0) {
            // TODO Auto-generated method stub
            Log.i("called", "post");
            //post();
            return Constants.SUCCESS;
    }
}
