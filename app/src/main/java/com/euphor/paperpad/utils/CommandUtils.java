package com.euphor.paperpad.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.euphor.paperpad.utils.jsonUtilities.AppJsonWriter.PostCallBack;

import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class CommandUtils {

	public static HashMap<String, Object> map;


	public CommandUtils() {
		// TODO Auto-generated constructor stub
	}
	
	public String writeJson(VerifiedCard card) {
		Map<String, JSONObject> map = new HashMap<String, JSONObject>();
		map.put("payment", writeDetail(card));
		JSONObject jsonObject = new JSONObject(map);
		return jsonObject.toJSONString();
		
	}

	private JSONObject writeDetail(VerifiedCard card) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("card_token", card.getCard_token());
			map.put("application_id", card.getApplication_id());
			map.put("order_id", card.getOrder_id());
			map.put("email", card.getEmail());
			
			JSONObject jsonObject = new JSONObject(map);
			return jsonObject;
	}
	
	
	public static class PostSendAsyncTask extends AsyncTask<String, Integer, HashMap<String, Object>>{
		
		public int status = -1;
		private Context context;
		/** progress dialog to show user that the backup is processing. */
	    public ProgressDialog dialog;
	    PostCallBack callBack;
		
		@Override
		protected HashMap<String, Object> doInBackground(String... params) {
			String endpoint = params[0];
			String body = params[1];
			status = 0;
			try {
				map = post(endpoint, body);
				status = (Integer) map.get("Status");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return map;
		}

		@Override
		protected void onPostExecute(HashMap<String,Object> result) {
			dialog.dismiss();
			if (status != 200) {
				callBack.onPosted(false);
				
			}else {
				Log.i("File sent successfully", ""+status);
				callBack.onPosted(true);
				callBack.getResult(result);
				
			}
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
//			this.dialog.setMessage(context.getResources().getString(R.string.waiting));
			this.dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			this.dialog.setIndeterminate(false);
			this.dialog.setCancelable(false);
			this.dialog.show();
			super.onPreExecute();
		}

		public PostSendAsyncTask(Fragment dialogFragment) {
			super();
			this.context = dialogFragment.getActivity();
			dialog = new ProgressDialog(context);
			callBack = (PostCallBack)dialogFragment;
		}
		
	}
	
	
	/**
     * Issue a POST request to the server.
     *
     * @param endpoint POST address.
     * @param body request parameters.
     *
     * @throws IOException propagated from POST.
     */
    public static HashMap<String, Object> post(String endpoint, String body)
            throws IOException {    
    	
    	
        URL url;
/** FOR LOCAL DEV   HttpPost post = new HttpPost("http://192.168.0.186:3000/events"); //works with and without "/create" on the end */
        try {
            url = new URL(endpoint);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("invalid url: " + endpoint);
        }
       
        Log.v("AppJsonWriter", "Posting '" + body + "' to " + url);
        byte[] bytes = body.getBytes();
        HttpURLConnection conn = null;
        try {
            Log.e("URL", "> " + url);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setFixedLengthStreamingMode(bytes.length);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type",
                    "application/json;charset=UTF-8");
            
            // post the request
            OutputStream out = conn.getOutputStream();
            out.write(bytes);

            out.close();
            // handle the response
            int status = conn.getResponseCode();
            String msg = conn.getResponseMessage();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line+"\n");
            }
            br.close();
            msg = sb.toString();
            HashMap<String , Object> map = new HashMap<String, Object>();
            map.put("Status", status);
            map.put("Result", msg);
            if (status != 200) {
              throw new IOException("Post failed with error code " + status);
            }else {
				Log.i("File sent successfully", ""+status);
			}
            return map;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
      }

}
