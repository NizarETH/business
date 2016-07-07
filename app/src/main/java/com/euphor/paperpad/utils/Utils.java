/**
 * 
 */
package com.euphor.paperpad.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.util.Log;

import com.nostra13.universalimageloader.core.download.ImageDownloader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author euphordev02
 *
 */
public class Utils {
	private static final int timeout = 20000;
	public static String LANG = "LANG";
    public static String fontPath = "fonts/montserrat/Montserrat-Light.otf";//"fonts/montserrat/Montserrat-Hairline.otf";//
	
	
	/**this method is used to change in the course of the application the {@link Locale} we do this because we have two places 
     * where we get the I18N resources ... from the BO and from android I18N strings
     * @param string this string is the locale name for example "it" for Italic "fr" for French ...
	 * @param context 
     */
    public static void changeLocale(String string, Context context) {
		Configuration config = new Configuration();
		Locale locale = new Locale(string);
		Locale.setDefault(locale);

		config.locale = locale;
		context.getResources()
				.updateConfiguration(
						config, null);
		
	}
    
    
	public static String retrieveJson(String url) {
		HttpGet getRequest = new HttpGet(url);
	
	    try {
	    	DefaultHttpClient httpClient = new DefaultHttpClient();
	    	HttpParams httpParams = new BasicHttpParams();
	        HttpConnectionParams.setConnectionTimeout(httpParams, timeout);
	    	httpClient.setParams(httpParams);
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
	        	
	        	String s = EntityUtils.toString(getResponseEntity, HTTP.UTF_8 );;
	        	
	            return s;//EntityUtils.toString(getResponseEntity, HTTP.UTF_8 );
	        }
	
	    } catch (ConnectTimeoutException e) {
	    	Log.w("Timeout exception "+timeout,   url, e);
		}catch (IOException e) {
	        getRequest.abort();
	        Log.w("Error in URL ",   url, e);
	    }
	
	    return null;
		
	}
	
	

	public static String encode(String input) {
		StringBuilder resultStr = new StringBuilder();
		for (char ch : input.toCharArray()) {
			if (isUnsafe(ch)) {
				resultStr.append('%');
				resultStr.append(toHex(ch / 16));
				resultStr.append(toHex(ch % 16));
			} else {
				resultStr.append(ch);
			}
		}
		return resultStr.toString();
	}

	private static char toHex(int ch) {
		return (char) (ch < 10 ? '0' + ch : 'A' + ch - 10);
	}

    public static int[] intToRgb(int rgb) {

        return new int[]{
                (rgb & 0xFF0000) >>> 16,
                (rgb & 0xFF00) >>> 8,
                rgb & 0xFF };
    }

	private static boolean isUnsafe(char ch) {
		if (ch > 128 || ch < 0)
			return true;
		return " %$&+,/:;=?@<>#%".indexOf(ch) >= 0;
	}
	
	public static String encodeUrl(String url) {
		String[] parts = url.split("/");
		URI uri;
		String pathTmp = "";
		if (parts.length>3) {
			for (int i = 3; i < parts.length; i++) {
				pathTmp = pathTmp+"/"+ parts[i];
			}
		}
		try {
			uri = new URI(
			        "http",
			        null, // this is for userInfo
			        parts[2],
			        null, // port number as int
			        "pathTmp");
			
			String request = uri.toASCIIString();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return url;
		
	}
	
	/**Check if the supplied string is a valide email (checks only the format )
	 * @param email
	 * @return
	 */
	public static boolean isEmailValid(String email) {
		String regExpn = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
				+ "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
				+ "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
				+ "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
				+ "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
				+ "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

		CharSequence inputStr = email;

		Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputStr);

		if (matcher.matches())
			return true;
		else
			return false;
	}
	
	public static Date ConvertToDate(String dateString, String format){
		if (format.isEmpty()) {
			format = "dd/MM/yyyy hh:mm";
		}
	    SimpleDateFormat dateFormat = new SimpleDateFormat(format);
	    Date convertedDate = new Date();
	    try {
	        convertedDate = dateFormat.parse(dateString);
	        
	    } catch (ParseException e) {
	        e.printStackTrace();
	        convertedDate = null;
	    }
	    return convertedDate;
	}
	
	
	public static float spToPixels(Context context, Float sp) {
	    float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
	    return sp*scaledDensity;
	}

    public static float dpToPixels(Context context, Float dp) {
        float dpDensity = context.getResources().getDisplayMetrics().density;//.scaledDensity;
/*        if(context.getResources().getDisplayMetrics().densityDpi == 213)
            dpDensity = 2f;
        else if(context.getResources().getDisplayMetrics().densityDpi > 160)
            dpDensity = 1.5f;*/
        return dp*dpDensity;
    }

	public static int hsvInterpolate ( float mix ) {
	    float[] hsv0 = new float[3];
	    float[] hsv1 = new float[3];

	    float alt = 1.0f - mix;
	    Color.RGBToHSV( 255, 0,0 , hsv0 );
	    Color.RGBToHSV( 0, 255, 0, hsv1 );

	    float h = mix * hsv0 [ 0 ] +  alt * hsv1 [ 0 ];
	    float s = mix * hsv0 [ 1 ] +  alt * hsv1 [ 1 ];
	    float v = mix * hsv0 [ 2 ] +  alt * hsv1 [ 2 ];
	    float[] hsv = new float[]{h,s,v};

	    return Color.HSVToColor(hsv);
	}
	
	public static String progressiveColor(int value, int all){
		
		int red = 255 - (int)((float)(value*255)/(float)all);
		int green = (int)((float)(value*255)/(float)all);
		return String.format("#%06X", (0xFFFFFF & Color.argb(255, red, green, 0)));
		
	}
	
	private static String makeFragmentName(int viewId, int index)
	{
	     return "android:switcher:" + viewId + ":" + index;
	}
	
	public static String removeHtmlTags(String html){
		html = html.replaceAll("<(.*?)\\>"," ");//Removes all items in brackets
		html = html.replaceAll("<(.*?)\\\n"," ");//Must be undeneath
		html = html.replaceFirst("(.*?)\\>", " ");//Removes any connected item to the last bracket
		html = html.replaceAll("&nbsp;"," ");
		html = html.replaceAll("&amp;"," ");
		return html;
	}
	
	public static String removeUnderScore(String chaine){
		chaine = chaine.replaceAll("\n\n","");//Removes all items in brackets
//		chaine = chaine.replaceAll("<(.*?)\\\n"," ");//Must be undeneath
//		chaine = chaine.replaceFirst("(.*?)\\>", " ");//Removes any connected item to the last bracket
//		chaine = chaine.replaceAll("&nbsp;"," ");
//		chaine = chaine.replaceAll("&amp;"," ");
		return chaine;
	}
	
	
	public static String paramBodyHTML(int[] colorText){
		String body = "<html lang='fr'><head>" +
                "<meta charset='utf-8'>" +
                " <title>paperpad</title>  "
                + "<style type=\"text/css\" media=\"screen\">" +
                "@font-face {font-family: MyFont;src: url(\"file:///android_asset/"+fontPath+"\")}body {font-size: 16; font-size: medium; text-align: no-justify;"
                + "background-color: transparent; color: rgba("+colorText[0]+", "+colorText[1]+", "+colorText[2]+", 1);"
                + "text-decoration : none;"
                + "font-family:MyFont; "
				/*+ "font-family: Gill Sans, Gill Sans MT, sans-serif;"*/
                + "} a { color: rgba("+colorText[0]+", "+colorText[1]+", "+colorText[2]+", 1); font-weight: normal; } "
                + "p:first-child, div:first-child { margin-top: 0; }    </style></head><body id=\"body_element\"><div id=\"content\">    ";
		return body;
		
	}

    public static String paramBodyHTMLConf(int[] colorText, String text_align, String taille){
        String body = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"+
                "<html><head>"+
                "<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" />"+
                " <title>paperpad</title>  "
                + "<style type=\"text/css\" media=\"screen\">" +
                "@font-face {font-family: MyFont;src: url(\"file:///android_asset/"+fontPath+"\")}body {font-size: "+taille+"; font-size: medium; text-align: "+text_align+";"
                + "background-color: transparent; color: rgba("+colorText[0]+", "+colorText[1]+", "+colorText[2]+", 0.8);"
                + "text-decoration : none;"
                + "font-family:MyFont; "
				/*+ "font-family: Gill Sans, Gill Sans MT, sans-serif;"*/
                + "} a { color: rgba("+colorText[0]+", "+colorText[1]+", "+colorText[2]+", 1); font-weight: normal; } "
                + "p:first-child, div:first-child { margin-top: 0; }    </style></head><body id=\"body_element\"><div id=\"content\">    ";
        return body;

    }

    public static boolean isTablet(Context context) {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }

	/**
	 * Check if an asset exists. This will fail if the asset has a size < 1 byte.
	 * @param mgr
	 * @param path
	 * @return TRUE if the asset exists and FALSE otherwise
	 */
	public static boolean assetExists(AssetManager mgr, String path) {
		boolean bAssetOk = false;
		try {
			//String ASSET_BASE_PATH = "assets://";//file:///android_asset/";
			InputStream stream = mgr.open(path);
			stream.close();
			/*File f=new File("file:///android_asset/" + path);
			if(f.isDirectory())*/
				bAssetOk = true;
		} catch (FileNotFoundException e) {
			Log.w("Utils", "assetExists failed: "+e.toString());
		} catch (IOException e) {
			Log.w("Utils", "assetExists failed: "+e.toString());
		}/*finally {
			Log.e("Utils", "assetExists : "+bAssetOk);
		}*/
		return bAssetOk;
	}

}
