/**
 * 
 */
package com.euphor.paperpad.utils.rssParser;

import android.os.StrictMode;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author euphordev02
 *
 */
public abstract class BaseFeedParser implements FeedParser {

    // names of the XML tags
    static final String PUB_DATE = "pubDate";
    static final  String DESCRIPTION = "description";
    static final  String LINK = "link";
    static final  String TITLE = "title";
    static final  String ITEM = "item";
    static final  String CHANNEL = "channel";
    
    final URL feedUrl;

    protected BaseFeedParser(String feedUrl){
        try {
            this.feedUrl = new URL(feedUrl);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    protected InputStream getInputStream() {
    	StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
		.permitAll().build();
    	StrictMode.setThreadPolicy(policy);
    	HttpGet getRequest = new HttpGet("http://www.floconsdesel.com//offres-speciales.rss");
		InputStream result = null;
	    try {
	    	DefaultHttpClient httpClient = new DefaultHttpClient();
	        HttpResponse getResponse = null;
			try {
				getResponse = httpClient.execute(getRequest);
			} catch (ClientProtocolException e) {
				Log.w("get Response error", ""+e.getMessage());
				e.printStackTrace();
			}
	        final int statusCode = getResponse.getStatusLine().getStatusCode();
	
	        if (statusCode != HttpStatus.SC_OK) {
	            return null;
	        }
	
	        HttpEntity getResponseEntity = getResponse.getEntity();
	
	        if (getResponseEntity != null) {
	        	
	        	result = getResponseEntity.getContent();
	        	/*String logtxt = result.toString();
	        	Log.i(TITLE, ""+logtxt);
	        	result = getResponseEntity.getContent();*/
	        }
	
	    } 
	    catch (IOException e) {
	        getRequest.abort();
	        Log.w(/*getClass().getSimpleName()*/"Error in URL ",    e);
	    }
		return result;
    	
       /* try {
        	StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
			.permitAll().build();
        	StrictMode.setThreadPolicy(policy);
            return feedUrl.openConnection().getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/
    }
}
