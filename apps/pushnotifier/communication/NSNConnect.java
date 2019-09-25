package nsn.mobile.apps.nsnnotifier.communication;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import nsn.mobile.apps.nsnnotifier.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;


public class NSNConnect {
		
	public static String getContent(Context ctx, String url) throws NSNConnectException {
        
		StringBuilder returnString = new StringBuilder();
		
		ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
		
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		
		if (networkInfo != null && networkInfo.isConnected()) {
			
			HttpClient httpclient = new DefaultHttpClient();
	 
	        HttpGet httpget = new HttpGet(ctx.getString(R.string.server_ip)+"/nsn_mobile_apps/resources/android/"+url);
	 
	        HttpResponse response;
	        
	        try {
	 
	            response = httpclient.execute(httpget);
	 
	            if(response.getStatusLine().getStatusCode() == 200) {
	 
	                HttpEntity entity = response.getEntity();
	 
	                if (entity != null) {
	
	                	InputStream instream = entity.getContent();
	                                       
	                    BufferedReader reader = new BufferedReader(new InputStreamReader(instream));
	                    
	                    String line = null;
	                    
	                    while ((line = reader.readLine()) != null) {
	                    	returnString.append(line + "\n");
	                    }
	                    
	                    instream.close();
	                }
	            }
	            else {
	            	throw new NSNConnectException(response.getStatusLine().toString());
	            }
	        } catch (IOException  ex) {
	        	throw new NSNConnectException(ex.getMessage().toString());
	        }
		}
        return returnString.toString();
    }
	public static String postContent(Context ctx, String url, String data) throws NSNConnectException {
		String response=null;
		
		ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
		
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		
		if (networkInfo != null && networkInfo.isConnected()) {
			try {
				
				DefaultHttpClient hc=new DefaultHttpClient(); 
				
				HttpPost postMethod=new HttpPost(ctx.getString(R.string.server_ip)+"/nsn_mobile_apps/resources/android/"+url);
				
				ResponseHandler <String> res=new BasicResponseHandler();  
				
				StringEntity se = new StringEntity(data,HTTP.UTF_8);
				
				postMethod.setEntity(se);
				
				postMethod.setHeader("Accept", "application/json");
				
				postMethod.setHeader("Content-type", "application/json");
				
				response=hc.execute(postMethod,res);
				
			} catch(Throwable t) {
				throw new NSNConnectException(t.getMessage().toString());
			}
		}
		return response;
	}
}
