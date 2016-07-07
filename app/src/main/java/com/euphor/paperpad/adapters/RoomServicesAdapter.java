/**
 * 
 */
package com.euphor.paperpad.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.euphor.paperpad.R;
import com.euphor.paperpad.Beans.roomServices.Request;
import com.euphor.paperpad.utils.Colors;

import java.util.List;

/**
 * @author euphordev02
 *
 */
public class RoomServicesAdapter extends BaseAdapter {

	private List<Request> requests;
//	private Colors colors;
	private Context context;

	/**
	 * 
	 */
	public RoomServicesAdapter(List<Request> requests, Colors colors, Context context) {
		this.requests = requests;
		//this.colors = colors;
		this.context = context;
	}

	@Override
	public int getCount() {
		if(requests == null) return 1;
		return requests.size();
	}

	@Override
	public Request getItem(int position) {
		// TODO Auto-generated method stub
		if(requests == null) return null;
		return requests.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.room_services_list_item, null, false);
		}
		
		ViewHolder holder = new ViewHolder();

		holder.position = position;
		holder.title = (TextView) convertView.findViewById(R.id.titleService);
		holder.title.setTextAppearance(context, android.R.style.TextAppearance_DeviceDefault_Medium);

		holder.detail = (TextView)convertView.findViewById(R.id.detailService);
		holder.detail.setTextAppearance(context, android.R.style.TextAppearance_DeviceDefault_Small);

		holder.totalAmount = (TextView)convertView.findViewById(R.id.totalAmount);
		holder.totalAmount.setTextAppearance(context, android.R.style.TextAppearance_DeviceDefault_Medium);
		holder.status = (TextView)convertView.findViewById(R.id.statusService);
		holder.statusLayout = (LinearLayout)convertView.findViewById(R.id.statusServiceLayout);
		
		if(getItem(position) == null) {
			
			holder.detail.setText("Vous n'avez fait aucune demande.");
			holder.title.setVisibility(View.GONE);
			holder.totalAmount.setVisibility(View.GONE);
			holder.status.setVisibility(View.GONE);
			holder.statusLayout.setVisibility(View.GONE);
			convertView.setMinimumHeight(120);
			
			
		}else {	
			
		Request request = requests.get(position);
		
//		Drawable shape_rounded = context.getResources().getDrawable(R.drawable.shape_rounded_corners);
//		
//		getStatusBackColor( request.getStatus());
		holder.statusLayout.setBackgroundDrawable(getStatusBackColor(request.getStatus()));
		holder.status.setText(getRessourceText(request.getStatus()).toUpperCase());
		holder.title.setText(request.getTitle());
		holder.detail.setText(getTimeWhenDone(request.getDate()));
		holder.totalAmount.setText(request.getTotal_amount() +" € • ");
		}
		return convertView;
	}
	
	private String getRessourceText(String status) {
		String result = "";
		if (status.equalsIgnoreCase("new")) {
			result = context.getResources().getString(R.string.status_new); 
		}else if (status.equalsIgnoreCase("pending")) {
			result = context.getResources().getString(R.string.status_pending);
		}else if (status.equalsIgnoreCase("treated")) {
			result = context.getResources().getString(R.string.status_treated);
		}else if (status.equalsIgnoreCase("denied")) {
			result = context.getResources().getString(R.string.status_denied);
		}
		
		return result;
	}

	private Drawable getStatusBackColor(String string) {
		Drawable shape_rounded = context.getResources().getDrawable(R.drawable.shape_rounded_corners);

		if (string.equalsIgnoreCase("new")) {
			//shape_rounded.setColorFilter(Color.parseColor("#34ABFA"), Mode.MULTIPLY);
			shape_rounded = context.getResources().getDrawable(R.drawable.new_shape_rounded_corners);
		}else if (string.equalsIgnoreCase("pending")) {
			//shape_rounded.setColorFilter(Color.parseColor("#F5FF2E"), Mode.MULTIPLY);
			shape_rounded = context.getResources().getDrawable(R.drawable.pending_shape_rounded_corners);
		}else if (string.equalsIgnoreCase("treated")) {
			//shape_rounded.setColorFilter(Color.parseColor("#2EFF77"), Mode.MULTIPLY);
			shape_rounded = context.getResources().getDrawable(R.drawable.treated_shape_rounded_corners);

		}else if (string.equalsIgnoreCase("denied")) {
			//shape_rounded.setColorFilter(Color.parseColor("#FC2D2D"), Mode.MULTIPLY);
			shape_rounded = context.getResources().getDrawable(R.drawable.denied_shape_rounded_corners);

		}
		
		return shape_rounded;
		
	}

	private String getTimeWhenDone(int date) {
		long nowTime = System.currentTimeMillis() / 1000L;
//		Calendar now = Calendar.getInstance();
//		Calendar serverTime = Calendar.getInstance();
//		serverTime.setTimeInMillis(date);
//		int diff = now.compareTo(serverTime);
		String result = "";
//		if (diff>=0) {
//			result = getTimeDifference(now, serverTime);
//		}
		int difference = (int) (nowTime-date);
		result = getTimeDifference(difference);
		return result ;
	}

	private String getTimeDifference(int difference) {
		String result = String.format(context.getResources().getString(R.string.time_when_done_now), "");
		if (difference <= 60) {
			
			result = String.format(context.getResources().getString(R.string.time_when_done_now), "");
		}else if (difference > 60 && difference <= 60*60) {
			
			int minutes = (int)((float)difference/(float)60);
			result = String.format(context.getResources().getString(R.string.time_when_done_minutes), ""+minutes);
		}else if (difference > 60*60 && difference <= 24*60*60) {
			
			int heures = (int)((float)difference/((float)60*60));
			result = String.format(context.getResources().getString(R.string.time_when_done_hours), ""+heures);
		}else if (difference > 2*24*60*60 ) {
			
			int days = (int)((float)difference/((float)24*60*60));
			result = String.format(context.getResources().getString(R.string.time_when_done_days), ""+days);
		}else if (difference > 24*60*60 && difference <= 2*24*60*60) {
			
			result = String.format(context.getResources().getString(R.string.time_when_done_yesterday), "");
		}else {
			
			result = "--";
		}
		return result;
	}

	static class ViewHolder{
		TextView title; //title Service
		TextView detail; // detail Service
		TextView totalAmount;
		TextView status; // status service
		LinearLayout statusLayout;
		int position;
	}

}
