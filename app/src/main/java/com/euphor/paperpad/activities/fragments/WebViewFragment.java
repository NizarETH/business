/**
 * 
 */
package com.euphor.paperpad.activities.fragments;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.euphor.paperpad.Beans.Parameters;
import com.euphor.paperpad.R;
import com.euphor.paperpad.activities.MyWebViewClient;
import com.euphor.paperpad.activities.main.MainActivity;
import com.euphor.paperpad.activities.main.MyApplication;

import com.euphor.paperpad.utils.Colors;
import com.euphor.paperpad.utils.jsonUtils.AppHit;

import io.realm.Realm;

/**
 * @author euphordev02
 * 
 */
public class WebViewFragment extends Fragment {

	/**
	 * You'll need this in your class to cache the helper in the class.
	 */


	private Colors colors;
	private String link;
	private boolean isRss;
	private long time;
	private SharedPreferences wmbPreference;
    public Realm realm;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle arg0) {

		super.onCreate(arg0);
		setRetainInstance(true);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onDestroy()
	 */
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {


				realm = Realm.getInstance(getActivity());
		colors = ((MainActivity) activity).colors;
        Parameters ParamColor = realm.where(Parameters.class).findFirst();
        if (colors==null) {
            colors = new Colors(ParamColor);
        }
		link = getArguments().getString("link");
		isRss = getArguments().getBoolean("isRss", false);
		
		((MainActivity) getActivity()).extras = new Bundle();
		((MainActivity) getActivity()).extras.putString("link", link);
		((MainActivity) getActivity()).bodyFragment = "WebViewFragment";
		super.onAttach(activity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
	 * android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.webview_fragment, container,
				false);
		WebView webView = (WebView) view.findViewById(R.id.webview);
		//WebViewClient webView_ = new WebViewClient();

		final ProgressBar progess = (ProgressBar) view
				.findViewById(R.id.ProgressBar);
		webView.setWebViewClient(new MyWebViewClient(progess));
		webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		WebSettings settings = webView.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setSupportZoom(true);
		settings.setBuiltInZoomControls(true);
		webView.setScrollY(0);
		webView.setWebChromeClient(new WebChromeClient()); 
		webView.getSettings().setPluginState(PluginState.ON);
		webView.getSettings().setUseWideViewPort(true); 
		webView.getSettings().setLoadWithOverviewMode(true);
//		webView.getSettings().setDefaultZoom(ZoomDensity.FAR);
		webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);
		webView.getSettings().setSupportMultipleWindows(true);
		webView.getSettings().setSupportZoom(true);
		webView.setVerticalScrollBarEnabled(true);
		webView.setHorizontalScrollBarEnabled(true);
//		webView.onScrollChanged(webView.getScrollX(), webView.getScrollY(), webView.getScrollX(), webView.getScrollY());
		
		if(isRss)
		{
			settings.setUserAgentString("Mozilla/5.0 (Windows; U; MSIE 9.0; Windows NT 9.0; en-US)");
		}
		
		webView.invokeZoomPicker();
		
		wmbPreference = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		
		/*Map<String, String> headers = new HashMap<String, String>();
		String fr = "fr_FR";
		String us = "en-US";
		String lang = wmbPreference.getString(Utils.LANG, "fr");
		if (lang.equalsIgnoreCase("fr")) {
//			accepte_lang = fr;
			headers.put("Accept-Language", fr );
		}else {
//			accepte_lang = us;
			headers.put("Accept-Language", us );
		}*/
		
//		headers.put("Accept-Language", accepte_lang );
		webView.loadUrl(link);

		return view;
	}
	
	@Override
	public void onStop() {
		AppHit hit = new AppHit(System.currentTimeMillis()/1000, time/1000, "sales_web_section", 0);
		((MyApplication)getActivity().getApplication()).hits.add(hit);
		super.onStop();
	}

}
