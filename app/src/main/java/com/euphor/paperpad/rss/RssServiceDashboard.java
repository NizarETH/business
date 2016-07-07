/**
 * 
 */
package com.euphor.paperpad.rss;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.euphor.paperpad.R;
import com.euphor.paperpad.activities.fragments.DashboardFragment;
import com.euphor.paperpad.adapters.RSSAdapter;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import nl.matshofman.saxrssreader.RssFeed;
import nl.matshofman.saxrssreader.RssReader;

/**
 * @author euphordev02
 *
 */	
public class RssServiceDashboard extends AsyncTask<String, Void, RssFeed> {
	
	private DashboardFragment rssFragment;
	private ProgressDialog progress;
	private Context context;
	protected ListView list;
	protected RSSAdapter adapter;
	private ProgressBar bar;

	/**
	 * @param list 
	 * @param adapter 
	 * @param bar 
	 * @param mainActivity
	 */
	public RssServiceDashboard(DashboardFragment rssFragment2, ListView list, RSSAdapter adapter, ProgressBar bar) {
		super();
		this.context = rssFragment2.getActivity();
		this.rssFragment = rssFragment2;
//		progress = new ProgressDialog(context);
//		progress.setMessage("Loading...");
		this.list = list;
		this.adapter = adapter;
		this.bar = bar;
	}


	/**
	 * 
	 */
	public RssServiceDashboard() {
		// TODO Auto-generated constructor stub
	}
	

	@Override
	protected RssFeed doInBackground(String... params) {
		/*
		 * test Rss
		 */
		java.net.URL urlRss = null;
		try {
			urlRss = new java.net.URL(params[0]); //"http://www.floconsdesel.com//offres-speciales.rss" //http://rss.lemonde.fr/c/205/f/3050/index.rss
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		RssFeed feed = null;
		try {
			
			InputStream in =urlRss.openStream();
			byte[] bytes = new byte[1000];
//			char[] character = new char[2048];
//
			StringBuilder x = new StringBuilder();
//
			int numRead = 0;

			String Charset = "UTF-8";
			
			boolean close = false;
			while ((numRead = in.read(bytes)) >= 0 && !close) {
				
			    x.append(new String(bytes, 0, numRead));
			    if(x.toString().contains("iso-8859-1")) {
			    	Charset = "ISO-8859-1";
			    	close = true;
			    }else if(x.toString().contains("utf-8")) {
			    	close = true;

			    }
			}
			in.close();

			
			feed = RssReader.read(urlRss);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		return feed;
	}

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(RssFeed result) {
		Log.e("ASYNC RssService Dashboard", "POST EXECUTE");
		final RssFeed feed = result;
		if (rssFragment != null && feed != null && rssFragment.getActivity() != null) {
			rssFragment.getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {

					adapter = new RSSAdapter(rssFragment
							.getActivity(), feed, rssFragment.colors, R.layout.widget_rss_list_item);
					list.setAdapter(adapter);
					adapter.notifyDataSetChanged();

				}
			});
		}
		bar.setVisibility(View.GONE);
//		progress.dismiss();
	}

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#onPreExecute()
	 */
	@Override
	protected void onPreExecute() {
		Log.e("ASYNC RssService Dashboard", "PRE EXECUTE");
//		progress.show();
//		super.onPreExecute();
	}

	

}
