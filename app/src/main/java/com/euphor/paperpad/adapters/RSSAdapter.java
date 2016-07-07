/**
 * 
 */
package com.euphor.paperpad.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.euphor.paperpad.R;
import com.euphor.paperpad.activities.fragments.WebViewFragment;
import com.euphor.paperpad.activities.main.MainActivity;
import com.euphor.paperpad.utils.Colors;
import com.euphor.paperpad.utils.Utils;
import com.euphor.paperpad.widgets.ArrowImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;


import java.util.ArrayList;

import nl.matshofman.saxrssreader.RssFeed;
import nl.matshofman.saxrssreader.RssItem;

/**
 * @author euphordev02
 *
 */
public class RSSAdapter extends BaseAdapter {

	private Context context;
	private RssFeed feed;
	DisplayImageOptions opts ;
	private Colors colors;
	ArrayList<RssItem> items;
	private FragmentActivity activity;
	private int item;
	private boolean isDahshboardPhone;
	
	

	public boolean isDahshboardPhone() {
		return isDahshboardPhone;
	}

	public void setDahshboardPhone(boolean isDahshboard) {
		this.isDahshboardPhone = isDahshboard;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return items.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public RssItem getItem(int position) {
		// TODO Auto-generated method stub
		return items.get(position);
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view==null) {
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//			item = R.layout.rss_list_item;
			view = inflater.inflate(item, parent,false);
		}
		RssItem element = getItem(position);
		final int pos = position;
		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				WebViewFragment webViewFragment = new WebViewFragment();
				((MainActivity)activity).bodyFragment = "WebViewFragment";
				// In case this activity was started with special instructions from an Intent,
				// pass the Intent's extras to the fragment as arguments
				((MainActivity)activity).extras = new Bundle();
				((MainActivity)activity).extras.putString("link", getItem(pos).getLink());
				((MainActivity)activity).extras.putBoolean("isRss", true);
				webViewFragment.setArguments(((MainActivity)activity).extras);
				// Add the fragment to the 'fragment_container' FrameLayout
				activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, webViewFragment).addToBackStack(null).commit();

			}
		});
			ViewHolder holder = new ViewHolder();
			holder.position = position;
			
			holder.title = (TextView)view.findViewById(R.id.TVTitleCategory);
			holder.title.setText(element.getTitle());
			holder.title.setTextColor(colors.getColor(colors.getTitle_color()));
			holder.title.setLines(2);
			holder.title.setEllipsize(TruncateAt.END);
//			holder.title.setTextSize(context.getResources().getDimension(R.dimen.subtitleSize));

			holder.imageItem = (ImageView)view.findViewById(R.id.imgCategory);
			if (element.getLink()!= null && !element.getLink().isEmpty()) {
				
				holder.imageItem.setScaleType(ScaleType.CENTER_CROP);
				String path = element.getLink();
				Glide.with(activity).load(path).into(holder.imageItem);
//				imageLoader.displayImage(path, holder.imageItem, options);
			}
			else {
				holder.imageItem.setVisibility(View.GONE);
			}
			
			if(!isDahshboardPhone) {
				
			holder.desc = (TextView)view.findViewById(R.id.TVDescCategory);
			if (element.getDescription().isEmpty()) {
				//holder.desc.setVisibility(View.GONE);
				view.findViewById(R.id.TVDescCategoryContainer).setVisibility(View.GONE);
			}else {

				holder.desc.setTextColor(colors.getColor(colors.getBody_color(),"AA"));
				String desc = element.getDescription();
				//holder.desc.setText(Utils.removeUnderScore(TextUtils.htmlEncode(Utils.removeHtmlTags(Html.fromHtml(desc).toString()))));
                holder.desc.setText(Utils.removeUnderScore(Utils.removeHtmlTags(Html.fromHtml(desc).toString())));
				holder.desc.setClickable(false);
				holder.desc.setLines(2);
//				holder.desc.setTextSize(15);
				holder.desc.setEllipsize(TruncateAt.END);
			}
			

			}else {
				view.findViewById(R.id.TVDescCategoryContainer).setVisibility(View.GONE);
			}
			
			ArrowImageView arrowImg = (ArrowImageView)view.findViewById(R.id.imgArrow);
			Paint paint = new Paint();
			paint.setColor(colors.getColor(colors.getForeground_color(),"AA"));
			arrowImg.setPaint(paint);
			
		return view;
	}

	/**
	 * @param activity
	 * @param feed
     * @param colors
     * @param item
     */
	public RSSAdapter(FragmentActivity activity, RssFeed feed, Colors colors, int item) {
		this.activity = activity;
		this.context = activity.getApplicationContext();
		this.feed = feed;
		 opts = new DisplayImageOptions.Builder().cacheInMemory().cacheOnDisc().build();
		 this.colors = colors;
		 this.feed = feed;
		 items = feed.getRssItems();
		 this.item = item;
		 
	}


	
	static class ViewHolder{
		TextView title;
		TextView desc; 
		ImageView imageItem;
		int position;
	}



	/**
	 * @return the feed
	 */
	public RssFeed getFeed() {
		return feed;
	}

	/**
	 * @param feed the feed to set
	 */
	public void setFeed(RssFeed feed) {
		this.feed = feed;
	}

	/**
	 * @return the items
	 */
	public ArrayList<RssItem> getItems() {
		return items;
	}

	/**
	 * @param items the items to set
	 */
	public void setItems(ArrayList<RssItem> items) {
		this.items = items;
	}

}
