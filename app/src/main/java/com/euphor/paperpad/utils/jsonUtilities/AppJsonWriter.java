/**
 * 
 */
package com.euphor.paperpad.utils.jsonUtilities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;

import com.euphor.paperpad.Beans.CartItem;
import com.euphor.paperpad.Beans.Field;
import com.euphor.paperpad.Beans.FieldFormContact;
import com.euphor.paperpad.Beans.FieldSatisfaction;
import com.euphor.paperpad.Beans.FormuleElement;
import com.euphor.paperpad.activities.main.SplashActivity;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.realm.Realm;


/**
 * @author euphordev02
 *
 */
public class AppJsonWriter {
	

	 private Realm realm;
    SplashActivity SplashActivity;

	public String writeJson(Order order) {
		Map<String, JSONObject> map = new HashMap<String, JSONObject>();
		map.put("order", writeOrder(order));
		JSONObject jsonObject = new JSONObject(map);
		return jsonObject.toJSONString();
		
	}
	
	private JSONObject writeOrder(Order order) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("account_id", order.getAccount_id());
		map.put("application_id", order.getApplication_id());
		map.put("key", order.getKey());
		map.put("language", order.getLanguage());
		map.put("application_push_token", order.getApplication_push_token());
		map.put("platform", "android");
		map.put("number", order.getNumber());
		map.put("application_unique_identifier", order.getApplication_unique_identifier());
		
		
		if (order.getFields() != null) {
			map.put("fields", writeFields(order.getFields()));
		}
		map.put("products", writeProducts(order.getProducts()));
		
		JSONObject jsonObject = new JSONObject(map);
		return jsonObject;
	}

	private JSONArray writeProducts(List<OrderProduct> products) {
		JSONArray jsonArray = new JSONArray();

		for (OrderProduct product : products) {
			jsonArray.add(writeProduct(product));
		}
		return jsonArray;
	}

	private JSONObject writeProduct(OrderProduct product) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("price_id", product.getPrice_id());
		map.put("quantity", product.getQuantity());
		if (product.getChildren() != null) {
			map.put("children", writeChildren(product.getChildren()));
		}
	
		JSONObject jsonObject = new JSONObject(map);
		return jsonObject;
	}

	private JSONArray writeChildren(List<OrderChild> children) {
		JSONArray jsonArray = new JSONArray();

		for (OrderChild child : children) {
			jsonArray.add(writeChild(child));
		}
		return jsonArray;
	}

	private JSONObject writeChild(OrderChild child) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("price_id", child.getPrice_id());
		map.put("page_id", child.getPage_id());
		map.put("quantity", child.getQuantity());
		
		JSONObject jsonObject = new JSONObject(map);
		return jsonObject;
	}

	private JSONArray writeFields(List<OrderField> fields) {
		JSONArray jsonArray = new JSONArray();

		for (OrderField field : fields) {
			jsonArray.add(writeField(field));
		}
		return jsonArray;
	}
	private JSONArray writeFieldsForm(List<OrderField> fields) {
		JSONArray jsonArray = new JSONArray();

		for (OrderField field : fields) {
			jsonArray.add(writeFieldFormContact(field));
		}
		return jsonArray;
	}
	
	private JSONArray writeFieldsSatisfaction(List<OrderField> fields) {
		JSONArray jsonArray = new JSONArray();

		for (OrderField field : fields) {
			jsonArray.add(writeFieldSatisfaction(field));
		}
		return jsonArray;
	}

	private JSONObject writeFieldFormContact(OrderField field) {
		Map<String, Object> map = new HashMap<String, Object>();

        map.put("value_id", field.getValue_id());
		map.put("field_id", field.getField_id());
		if (field.getValue() != null) {
			FieldFormContact dbField = null;
            if (SplashActivity!=null)
            realm = Realm.getInstance(SplashActivity.getApplicationContext());
            List<FieldFormContact> fields =  realm.where(FieldFormContact.class).equalTo("id", field.getField_id()).findAll();
            //.appController.getFieldFormContactDao().queryForEq("id", field.getField_id());
            if (fields.size()>0) {
                dbField = fields.get(0);
            }
            //			if (dbField.getType().equals("select")) {
//				
//				try {
//					map.put("value_id", Integer.parseInt(field.getValue()));
//				} catch (NumberFormatException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}else {
				map.put("value", field.getValue());
//			}
			
		}
		if (field.getValue_period() != null) {
			map.put("value_period", writeValuePeriod(field.getValue_period()));
		}
		if (field.getValue_date() != null) {
			Date date = field.getValue_date();
			String value= (String) DateFormat.format("yyyy-MM-dd", date);
			map.put("value_date", value);
		}
		
		if (field.getValue_date_time() != null) {
			Date date = field.getValue_date_time();
			String value= (String) DateFormat.format("yyyy-MM-dd hh:mm", date);
			map.put("value_date", value);
		}
		
		JSONObject jsonObject = new JSONObject(map);
		return jsonObject;
	}
	
	private JSONObject writeFieldSatisfaction(OrderField field) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("field_id", field.getField_id());
		FieldSatisfaction dbField = null;
        if(SplashActivity!=null)
        realm=Realm.getInstance(SplashActivity.getApplication());
        List<FieldSatisfaction> fields = realm.where(FieldSatisfaction.class).equalTo("id", field.getField_id()).findAll();
        //appController.getFieldSatistfactionDao().queryForEq("id", field.getField_id());
        if (fields.size()>0) {
            dbField = fields.get(0);
        }
        if (dbField != null) {
			map.put("type", dbField.getType());
		}
		if (field.getValue() != null) {
			
			if (dbField.getType().equals("select")) {
				
				try {
					map.put("value_id", Integer.parseInt(field.getValue()));
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else {
				map.put("value", field.getValue());
			}
			
			
		}
		if (field.getValue_period() != null) {
			map.put("value_period", writeValuePeriod(field.getValue_period()));
		}
		if (field.getValue_date() != null) {
			Date date = field.getValue_date();
			String value= (String) DateFormat.format("yyyy-MM-dd", date);
			map.put("value_date", value);
		}
		
		if (field.getValue_date_time() != null) {
			Date date = field.getValue_date_time();
			String value= (String) DateFormat.format("yyyy-MM-dd hh:mm", date);
			map.put("value_date", value);
		}
		
		JSONObject jsonObject = new JSONObject(map);
		return jsonObject;
	}
	
	private JSONObject writeField(OrderField field) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("field_id", field.getField_id());
		if (field.getValue() != null) {
			Field dbField = null;
            if(SplashActivity!=null)
                realm=Realm.getInstance(SplashActivity.getApplication());
            List<Field> fields = realm.where(Field.class).equalTo("id", field.getField_id()).findAll();
            //appController.getFieldDao().queryForEq("id", field.getField_id());
            if (fields.size()>0) {
                dbField = fields.get(0);
            }
            if (dbField.getType().equals("select")) {
				
				try {
					map.put("value_id", Integer.parseInt(field.getValue()));
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else {
				map.put("value", field.getValue());
			}
			
		}
		if (field.getType() != null) {
			map.put("type", field.getType());
		}
		if (field.getValue_period() != null) {
			map.put("value_period", writeValuePeriod(field.getValue_period()));
		}
		if (field.getValue_date() != null) {
			Date date = field.getValue_date();
			String value= (String) DateFormat.format("yyyy-MM-dd", date);
			map.put("value_date", value);
		}
		
		if (field.getValue_date_time() != null) {
			Date date = field.getValue_date_time();
			String value= (String) DateFormat.format("yyyy-MM-dd hh:mm", date);
			map.put("value_date", value);
		}
		
		JSONObject jsonObject = new JSONObject(map);
		return jsonObject;
	}

	private JSONObject writeValuePeriod(ValuePeriod value_period) {
		Map<String, Object> map = new HashMap<String, Object>();
		Date date = value_period.getEnd();
		String valueEnd= (String) DateFormat.format("yyyy-MM-dd", date);
		map.put("end", valueEnd);
		Date dateStart = value_period.getStart();
		String valueStart= (String) DateFormat.format("yyyy-MM-dd", dateStart);
		map.put("start", valueStart);
		
		JSONObject jsonObject = new JSONObject(map);
		return jsonObject;
	}

	public interface PostCallBack {
		void onPosted(boolean posted);
		void getResult(HashMap<String, Object> result);
		
	}
	private PostCallBack dummyImpl = new PostCallBack() {
		
		@Override
		public void onPosted(boolean posted) {
			// this a dummy implementation
			
		}

		@Override
		public void getResult(HashMap<String, Object> result) {
			// TODO Auto-generated method stub
			
		}

	};
	public static HashMap<String,Object> map;
	
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
     * @param params request parameters.
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
            conn.setConnectTimeout(10000);
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
    
    public String writeListCartItems(List<CartItem> items) {
    	List<OrderProduct> products = new ArrayList<OrderProduct>();
		for (Iterator<CartItem> iterator = items.iterator(); iterator.hasNext();) {
			CartItem cartItem = (CartItem) iterator.next();
			if (cartItem.getFormule() != null) {
				List<OrderChild> children = new ArrayList<OrderChild>();
				for (Iterator<FormuleElement> iterator2 = cartItem.getFormule().getElements().iterator(); iterator2.hasNext();) {
					FormuleElement element = (FormuleElement) iterator2.next();
					OrderChild child = new OrderChild(element.getPage_id(), null, 1);
					children.add(child);
				}
				
				OrderProduct product = new OrderProduct(cartItem.getPrice_id(), cartItem.getQuantity(), children);
				products.add(product);
				
			}else if (cartItem.getCartItems().size()>0 ) {
				List<OrderChild> children = new ArrayList<OrderChild>();
				for (Iterator<CartItem> iterator2 = cartItem.getCartItems().iterator(); iterator2
						.hasNext();) {
					CartItem subItem = (CartItem) iterator2.next();
					OrderChild child = new OrderChild(-1, subItem.getChild_page().getPrices().iterator().next().getId_price(), 1);
					children.add(child);
				}
				OrderProduct product = new OrderProduct(cartItem.getChild_page().getPrices().iterator().next().getId_price(), cartItem.getQuantity(), children);
				products.add(product);
				
			}else if (cartItem.getParentItem() == null && cartItem.getCartItems().size()==0 && cartItem.getChild_page() !=null /**/  ) {
				OrderProduct product = new OrderProduct(cartItem.getChild_page().getPrices().iterator().next().getId_price(), cartItem.getQuantity(), null);
				products.add(product);
			}
		}
		Order order = new Order(0, 0, "cdsvhshsjhduvsi", "fr", null, products);
		String outStr = writeJson(order);
		Log.i("Json writing", outStr);
		return outStr;
	}

	public AppJsonWriter(Realm  realm) {
		super();
		this.realm = realm;
	}
	
	public List<OrderProduct> getProductsFromCartItems(List<CartItem> items) {
		List<OrderProduct> products = new ArrayList<OrderProduct>();
		for (Iterator<CartItem> iterator = items.iterator(); iterator.hasNext();) {
			CartItem cartItem = (CartItem) iterator.next();
			if (cartItem.getFormule() != null) {
				List<OrderChild> children = new ArrayList<OrderChild>();
				for (Iterator<FormuleElement> iterator2 = cartItem.getFormule().getElements().iterator(); iterator2.hasNext();) {
					FormuleElement element = (FormuleElement) iterator2.next();
					OrderChild child = new OrderChild(element.getPage_id(), null, 1);
					children.add(child);
				}
				
				OrderProduct product = new OrderProduct(cartItem.getPrice_id(), cartItem.getQuantity(), children);
				products.add(product);
				
			}else if (cartItem.getCartItems().size()>0 ) {
				List<OrderChild> children = new ArrayList<OrderChild>();
				for (Iterator<CartItem> iterator2 = cartItem.getCartItems().iterator(); iterator2
						.hasNext();) {
					CartItem subItem = (CartItem) iterator2.next();
					OrderChild child = new OrderChild(-1, subItem.getChild_page().getPrices().iterator().next().getId_price(), 1);
					children.add(child);
				}
				OrderProduct product = new OrderProduct(cartItem.getChild_page().getPrices().iterator().next().getId_price(), cartItem.getQuantity(), children);
				products.add(product);
				
			}else if (cartItem.getParentItem() == null && cartItem.getCartItems().size()==0 ) {
				OrderProduct product = new OrderProduct(cartItem.getChild_page().getPrices().iterator().next().getId_price(), cartItem.getQuantity(), null);
				products.add(product);
			}
		}
		return products;
	}

	public String writeFormJson(FormFields formFields) {
		Map<String, JSONObject> map = new HashMap<String, JSONObject>();
		map.put("contact_form", writeFormFields(formFields));
		JSONObject jsonObject = new JSONObject(map);
		return jsonObject.toJSONString();
	}
	
	public String writeSatisfactionJson(SatisfactionData formFields) {
		Map<String, JSONObject> map = new HashMap<String, JSONObject>();
		map.put("satisfaction", writeSatisfactionFields(formFields));
		JSONObject jsonObject = new JSONObject(map);
		return jsonObject.toJSONString();
	}

	private JSONObject writeFormFields(FormFields formFields) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("section_id", formFields.getSection_id());
		map.put("application_id", formFields.getApplication_id());
		map.put("key", formFields.getKey());
		map.put("language", formFields.getLanguage());
		map.put("application_push_token", formFields.getApplication_push_token());
		map.put("platform", "android");
		map.put("number", formFields.getNumber());
		
		if (formFields.getFields() != null) {
			map.put("fields", writeFieldsForm(formFields.getFields()));
		}
//		map.put("products", writeProducts(order.getProducts()));
		
		JSONObject jsonObject = new JSONObject(map);
		return jsonObject;
	}
	
	private JSONObject writeSatisfactionFields(SatisfactionData satisData) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("section_id", satisData.getSection_id());
		map.put("application_id", satisData.getApplication_id());
		map.put("key", satisData.getKey());
		map.put("language", satisData.getLanguage());
		map.put("survey_id", satisData.getSurvey_id());
		map.put("application_push_token", satisData.getApplication_push_token());
		map.put("platform", "android");
		map.put("number", satisData.getNumber());
		
		if (satisData.getFields() != null) {
			map.put("fields", writeFieldsSatisfaction(satisData.getFields()));
		}
		
		if (satisData.getResponses() != null) {
			map.put("questions", writeSatisResponses(satisData.getResponses()));
		}
//		map.put("products", writeProducts(order.getProducts()));
		
		JSONObject jsonObject = new JSONObject(map);
		return jsonObject;
	}

	private JSONArray writeSatisResponses(List<Response> responses) {
		JSONArray jsonArray = new JSONArray();

		for (Response response : responses) {
			jsonArray.add(writeResponse(response));
		}
		return jsonArray;
	}

	private JSONObject writeResponse(Response response) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("field_id", response.getField_id());
		map.put("type", response.getType());
		map.put("value", response.getValue());
		JSONObject jsonObject = new JSONObject(map);
		return jsonObject;
	}

}
