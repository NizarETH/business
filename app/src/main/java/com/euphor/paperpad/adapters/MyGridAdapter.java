package com.euphor.paperpad.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.euphor.paperpad.InterfaceRealm.CommunElements1;
import com.euphor.paperpad.R;
import com.euphor.paperpad.utils.Colors;
import com.euphor.paperpad.widgets.ArrowImageView;


import java.io.File;
import java.util.List;

/**
 * 
 * @author manish.s
 *
 */
public class MyGridAdapter extends ArrayAdapter<CommunElements1> {
	Context context;
	private Colors colors;
	int layoutResourceId;
	List<CommunElements1> data; // = new ArrayList<CommunElements>();
//	private String POLICE;
	private ColorStateList colorStateList, colorStateList_;
	private Paint paint;
	
	private int txtSize;
	
	public static Drawable getButtonDrawableByScreenCathegory(Context con, View buttonType) {

	    //int normalStateResID = (int) GXConstants.NOT_ID;
	    //int pressedStateResID = R.drawable.button_header_pressed;



	    Drawable state_normal = null;//con.getResources().getDrawable(normalStateResID).mutate();

	    Drawable state_pressed = null;//con.getResources().getDrawable(pressedStateResID).mutate();

	    StateListDrawable drawable = new StateListDrawable();

	    drawable.addState(new int[] { android.R.attr.state_pressed },
	            state_pressed);
	    drawable.addState(new int[] { android.R.attr.state_enabled },
	            state_normal);

	    return drawable;
	}

	public MyGridAdapter(Context context, int layoutResourceId, Colors colors, List<CommunElements1> data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
		
		this.txtSize = (int) context.getResources().getDimension(R.dimen.grid_text_size);
		Log.e("  grid_text_size : ", "  grid_text_size " +txtSize);
		
//		if(this.txtSize > 20) {
//			this.txtSize = 13;
//		}else {
//			this.txtSize = 25;
//		}
		DisplayMetrics metrics = new DisplayMetrics(); 
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics); 

		if(metrics.densityDpi >= 213 ){
			
			   if (((Activity)context).getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
				   this.txtSize = 24;
	    } else if (((Activity)context).getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
	    	this.txtSize = 15;
	    	}

			
		}
		else{
			if (((Activity)context).getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
				   this.txtSize = 26;
	    } else if (((Activity)context).getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
	    	this.txtSize = 22;
	    	}
		}
		
		this.colors = colors;
//		POLICE = "fonts/gill-sans-light.ttf";
		colorStateList = new ColorStateList(new int[][]{{android.R.attr.state_pressed},{}}, 
				new int[]{colors.getColor(colors.getBackground_color()), 
				colors.getColor(colors.getTitle_color())});
		
		colorStateList_ = new ColorStateList(new int[][]{{android.R.attr.state_pressed},{}}, 
				new int[]{colors.getColor(colors.getBackground_color()), 
				colors.getColor(colors.getBody_color())});
		
		paint = new Paint();
		paint.setColor(colors.getColor(colors.getForeground_color(), "AA"));
	}
 
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		RecordHolder holder = null;

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);

			row.findViewById(R.id.gridItemLayout).setBackgroundColor(colors.getColor(colors.getForeground_color(), "80"));

			StateListDrawable d = new StateListDrawable();
			d.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(colors.getColor(colors.getForeground_color(), "EE")));
			d.addState(new int[]{android.R.attr.state_focused}, new ColorDrawable(colors.getColor(colors.getForeground_color(), "EE")));
			d.addState(new int[]{android.R.attr.state_selected}, new ColorDrawable(colors.getColor(colors.getForeground_color(), "EE")));
			d.addState(new int[]{}, new ColorDrawable(colors.getColor(colors.getBackground_color())));

			//row.setBackgroundDrawable(d);
			LinearLayout lyt = (LinearLayout) row.findViewById(R.id.dividedScreenItemGridLayout);
			//lyt.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 300));
			lyt.setBackgroundDrawable(d);

			//lyt.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			holder = new RecordHolder();
			holder.txtTitle = (TextView) row.findViewById(R.id.TVTitleCategory);
			holder.txtTitle.setTextSize(txtSize);
			holder.txtDescript = (TextView) row.findViewById(R.id.TVDescriptCategory);
			holder.txtDescript.setTextSize(txtSize - 10);
			holder.imageItem = (ImageView) row.findViewById(R.id.imgCategory);

			holder.imageItem.setLayoutParams(new LinearLayout.LayoutParams(150, 180));

			holder.txtTitle.setTextColor(colorStateList);
			holder.txtDescript.setTextColor(colorStateList_);

			holder.arrowImg = (ArrowImageView) row.findViewById(R.id.imgArrow);
			holder.arrowImg.setLayoutParams(new LinearLayout.LayoutParams(txtSize, txtSize - 5));

			StateListDrawable d_ = new StateListDrawable();
			d_.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(colors.getColor(colors.getForeground_color(), "EE")));
			d_.addState(new int[]{android.R.attr.state_focused}, new ColorDrawable(colors.getColor(colors.getForeground_color(), "EE")));
			d_.addState(new int[]{android.R.attr.state_selected}, new ColorDrawable(colors.getColor(colors.getForeground_color(), "EE")));
			d_.addState(new int[]{}, new ColorDrawable(colors.getColor(colors.getBackground_color())));

			holder.arrowImg.setDiffOfColorCode(colors.getColor(colors.getForeground_color(), "EE"), colors.getColor(colors.getBackground_color()));
			holder.arrowImg.setBackgroundDrawable(d_);
			holder.arrowImg.setPaint(paint);

			row.setTag(holder);
		} else {
			holder = (RecordHolder) row.getTag();
		}
		CommunElements1 item = data.get(position);


		//Log.e(" GridAdapter Informations : ", "  Title : "+item.getCommunTitle()+"    Illustration Path : "+item.getIllustration().getPath());
		holder.txtTitle.setText(item.getCommunTitle1());
		if (item != null && item.getCommunDesc1().isEmpty())
			holder.txtDescript.setVisibility(View.GONE);
		else
			holder.txtDescript.setText(item.getCommunDesc1());
//		holder.txtTitle.setTypeface(Typeface.createFromAsset(context.getAssets(), POLICE));

		//holder.imageItem.setImageBitmap(item.getCommunImagePath());
		if (item.getIllustration1() != null) {
			if (!item.getIllustration1().getPath().isEmpty()) {
				Glide.with(context).load(new File(item.getIllustration1().getPath())).into(holder.imageItem);
			} else {
				Glide.with(context).load(item.getIllustration1().getLink()).into(holder.imageItem);

			}
		}

		return row;

	}

	static class RecordHolder {
		TextView txtTitle;
		TextView txtDescript;
		ImageView imageItem;
		ArrowImageView arrowImg;

	}
}