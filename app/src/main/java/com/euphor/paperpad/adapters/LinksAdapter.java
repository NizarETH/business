/**
 * 
 */
package com.euphor.paperpad.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.euphor.paperpad.R;

import java.util.List;

/**
 * @author euphordev02
 *
 */
public class LinksAdapter extends BaseAdapter {

	private Context context;
	private List<String> strings;

	/**
	 * 
	 */
	public LinksAdapter(Context context) {

		this.context = context;
	}
	
	/**
	 * 
	 */
	public LinksAdapter(Context context, List<String> strings) {

		this.context = context;
		this.strings = strings;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return strings.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public String getItem(int position) {
		// TODO Auto-generated method stub
		return strings.get(position);
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
			view = inflater.inflate(R.layout.related_elements_list_item, parent, false);
			TextView titleTv = (TextView)view.findViewById(R.id.TVTitleCategory);
			titleTv.setText(strings.get(position));
			
		}
		return view;
	}

}
