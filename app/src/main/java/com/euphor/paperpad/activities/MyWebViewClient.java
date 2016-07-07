/**
 * 
 */
package com.euphor.paperpad.activities;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

/**
 * @author euphordev02
 *
 */
public class MyWebViewClient extends WebViewClient {

	private ProgressBar progess;

	/**
	 * 
	 */
	public MyWebViewClient(ProgressBar progess) {
		this.progess = progess;
	}

	/* (non-Javadoc)
	 * @see android.webkit.WebViewClient#onPageFinished(android.webkit.WebView, java.lang.String)
	 */
	@Override
	public void onPageFinished(WebView view, String url) {
		progess.setVisibility(View.GONE);
		Log.i("MyWebViewClient", ""+url);
		super.onPageFinished(view, url);
	}

	/* (non-Javadoc)
	 * @see android.webkit.WebViewClient#onPageStarted(android.webkit.WebView, java.lang.String, android.graphics.Bitmap)
	 */
	@Override
	public void onPageStarted(WebView view, String url, Bitmap favicon) {
		progess.setVisibility(View.VISIBLE);
		Log.i("MyWebViewClient", ""+url);
		super.onPageStarted(view, url, favicon);
	}

	/* (non-Javadoc)
	 * @see android.webkit.WebViewClient#shouldOverrideUrlLoading(android.webkit.WebView, java.lang.String)
	 */
	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		Log.i("MyWebViewClient", ""+url);
		return super.shouldOverrideUrlLoading(view, url);
	}

}
