/**
 * 
 */
package com.euphor.paperpad.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.euphor.paperpad.R;
import com.euphor.paperpad.Beans.Illustration;
import com.euphor.paperpad.Beans.MyBox;
import com.euphor.paperpad.activities.main.MainActivity;
import com.euphor.paperpad.utils.Colors;
import com.euphor.paperpad.widgets.ArrowImageView;


import java.io.File;
import java.util.List;

import io.realm.RealmList;

/**
 * @author euphordev02
 *
 */
public class MyBoxCategoriesAdapter extends BaseAdapter {

	private List<MyBox> boxs;
	private Context context;
	private MyBox box;
//	private ImageLoader imageLoader;
	private Colors colors;

	/**
	 * @param boxs
	 * @param context
	 * @param colors 
	 * 
	 */
	public MyBoxCategoriesAdapter(List<MyBox> boxs, Context context, Colors colors) {
		this.boxs = boxs;
		this.context = context;
		this.colors = colors;
		
		
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return boxs.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public MyBox getItem(int position) {
		// TODO Auto-generated method stub
		return boxs.get(position);
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
		ViewHolder holder = new ViewHolder();
		if (convertView==null) {
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView  = inflater.inflate(R.layout.myboxs_grid_item, parent, false);
			LinearLayout albumItem = (LinearLayout)convertView.findViewById(R.id.album_item);
			albumItem.setBackgroundColor(colors.getColor(colors.getTitle_color(), "80"));
			LinearLayout innerAlbumItem = (LinearLayout)convertView.findViewById(R.id.inner_album_item);
//			innerAlbumItem.setBackgroundColor(colors.getColor(colors.getBackground_color()));
			StateListDrawable drawable = new StateListDrawable();
			drawable.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(colors.getColor(colors.getForeground_color())));
			drawable.addState(new int[]{android.R.attr.state_focused}, new ColorDrawable(colors.getColor(colors.getForeground_color())));
			drawable.addState(new int[]{}, new ColorDrawable(colors.getColor(colors.getBackground_color())));
			innerAlbumItem.setBackgroundDrawable(drawable);
			holder.title = (TextView)convertView.findViewById(R.id.BoxTitle);
			holder.boxDesc = (TextView)convertView.findViewById(R.id.BoxDesc);
			holder.imgBox = (ImageView)convertView.findViewById(R.id.ImgBox);

			holder.imgArrow = (ArrowImageView) convertView.findViewById(R.id.imgArrow);

			StateListDrawable d_ = new StateListDrawable();
			d_.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(colors.getColor(colors.getForeground_color())));// colors.makeGradientToColor(colors.getTitle_color())); //
			d_.addState(new int[]{}, new ColorDrawable(colors.getColor(colors.getBackground_color())));

			holder.imgArrow.setDiffOfColorCode(colors.getColor(colors.getForeground_color()), colors.getColor(colors.getBackground_color()));
			holder.imgArrow.setBackgroundDrawable(d_);

			holder.position = position;

			//holder.title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 26);
			holder.title.setTextAppearance(context, android.R.style.TextAppearance_DeviceDefault_Medium);
			holder.title.setTypeface(MainActivity.FONT_TITLE, Typeface.BOLD);
			//holder.title.setTextColor(colors.getColor(colors.getTitle_color()));
			holder.title.setTextColor(/*colors.getColor(colors.getTitle_color()));*/
					new ColorStateList(new int[][]{{android.R.attr.state_focused}, {android.R.attr.state_pressed}, {}},
							new int[]{	colors.getColor(colors.getBackground_color()),
										colors.getColor(colors.getBackground_color()),
										colors.getColor(colors.getTitle_color())}));

			//holder.boxDesc.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
			holder.boxDesc.setTextAppearance(context, android.R.style.TextAppearance_DeviceDefault_Small);
			holder.boxDesc.setTypeface(MainActivity.FONT_BODY);
			holder.boxDesc.setTextColor(/*colors.getColor(colors.getBody_color(), "DD")*/
					new ColorStateList(new int[][]{{android.R.attr.state_focused}, {android.R.attr.state_pressed}, {}},
							new int[]{	colors.getColor(colors.getBackground_color()),
									colors.getColor(colors.getBackground_color()),
									colors.getColor(colors.getBody_color(), "CC")}));

			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
			
		}


		

		box = getItem(position);

		holder.title.setText(box.getTitre_coffret());
		String desc = box.getDescription();
		if (desc.contains("<!--break-->")) {
					String[] bodies = desc.split("(\\<!--)break(\\-->)");
					desc = bodies[0].replace("(/<!--break-->)", ""); 
			
		}

		holder.boxDesc.setText(Html.fromHtml(desc));


        RealmList<Illustration> illustrations=new RealmList<>();
        if(box.getIllustration()!=null)
        illustrations.add(box.getIllustration());
		if (illustrations.size()>0) {
				
				holder.imgBox.setScaleType(ScaleType.CENTER_CROP);
				Illustration illustration =illustrations.iterator().next();
				String path;
				if (!illustration.getPath().isEmpty()) {
					path = illustration.getPath();
					Glide.with(context).load(new File(path)).into(holder.imgBox);
				}else {
					path = illustration.getLink();
					Glide.with(context).load(path).into(holder.imgBox);
				}
//				imageLoader.displayImage(path, holder.imgBox);
		}
		
		return convertView;
	}
	
	static class ViewHolder {
		TextView title;
		TextView boxDesc; 
		ImageView imgBox; // T : TOP , B : BOTTOM , L : LEFT , R : RIGHT
		ArrowImageView imgArrow;
		int position;
		}

}
