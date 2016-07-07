/**
 * 
 */
package com.euphor.paperpad.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.euphor.paperpad.R;
import com.euphor.paperpad.Beans.Album;
import com.euphor.paperpad.Beans.Illustration;
import com.euphor.paperpad.Beans.Photo;
import com.euphor.paperpad.utils.Colors;
import com.euphor.paperpad.widgets.AutoResizeTextView;


import java.io.File;
import java.util.Iterator;
import java.util.List;

import javax.microedition.khronos.opengles.GL;

/**
 * @author euphordev02
 *
 */
public class AlbumsAdapter extends BaseAdapter {

	private List<Album> albums;
	private Context context;
	private Album album;
	private Colors colors;
	
	private int txtSize;

	/**
	 * @param colors 
	 * 
	 */
	public AlbumsAdapter(List<Album> albums, Context context, Colors colors) {
		this.albums = albums;
		this.context = context;
		this.colors = colors;
		
		DisplayMetrics metrics = new DisplayMetrics(); 
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics); 
		
		if(metrics.densityDpi >= 213 ){
			
			   if (((Activity)context).getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
				   this.txtSize = 26;
	    } else if (((Activity)context).getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
	    	this.txtSize = 20;
	    	}

			
		}
		else{
			if (((Activity)context).getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
				   this.txtSize = 30;
	    } else if (((Activity)context).getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
	    	this.txtSize = 26;
	    	}
		}
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return albums.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Album getItem(int position) {
		// TODO Auto-generated method stub
		return albums.get(position);
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
		
		if (convertView==null) {
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView  = inflater.inflate(R.layout.album_grid_item, parent, false);
			LinearLayout albumItem = (LinearLayout)convertView.findViewById(R.id.album_item);
			albumItem.setBackgroundColor(colors.getColor(colors.getForeground_color(), "80"));
			LinearLayout innerAlbumItem = (LinearLayout)convertView.findViewById(R.id.inner_album_item);
//			innerAlbumItem.setBackgroundColor(colors.getColor(colors.getBackground_color()));
			StateListDrawable drawable = new StateListDrawable();
			drawable.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(colors.getColor(colors.getForeground_color())));
			drawable.addState(new int[]{android.R.attr.state_focused}, new ColorDrawable(colors.getColor(colors.getForeground_color())));
			drawable.addState(new int[]{}, new ColorDrawable(colors.getColor(colors.getBackground_color())));
			innerAlbumItem.setBackgroundDrawable(drawable);
		}
		ColorStateList colorSelector = new ColorStateList(new int[][] { new int[] { android.R.attr.state_pressed }, new int[] {} }, new int[] {
						colors.getColor(colors.getBackground_color(), "AA"),
						colors.getColor(colors.getTitle_color()) });
		ColorStateList colorSelectorDesc = new ColorStateList(new int[][] { new int[] { android.R.attr.state_pressed }, new int[] {} }, new int[] {
				colors.getColor(colors.getBackground_color(), "AA"),
				colors.getColor(colors.getBody_color(), "DD") });
		ViewHolder holder = new ViewHolder();
		holder.Title = (AutoResizeTextView)convertView.findViewById(R.id.AlbumTitle);
		holder.albumImgCount = (AutoResizeTextView)convertView.findViewById(R.id.AlbumImgCount);
		holder.imgTL = (ImageView)convertView.findViewById(R.id.topImgLeft);
		holder.imgTR = (ImageView)convertView.findViewById(R.id.topImgRight);
		holder.imgBL = (ImageView)convertView.findViewById(R.id.bottomImgLeft);
		holder.imgBR = (ImageView)convertView.findViewById(R.id.bottomImgRight);
		holder.position = position;
		album = getItem(position);  
		holder.Title.setText(album.getTitle());
		holder.Title.setTextColor(colorSelector);
		holder.Title.setTextSize(txtSize);
		int size = album.getPhotos().size();
		String PhotosCount = size + context.getResources().getString(R.string.photos);
		holder.albumImgCount.setText(PhotosCount);
		holder.albumImgCount.setTextColor(colorSelectorDesc);
		holder.albumImgCount.setTextSize(txtSize - 10);
		if (size>0) {
			boolean index = true;
			boolean first = true;
			boolean second = false;
			boolean third = false;
			boolean fourth = false;
			for (Iterator<Photo> iterator = album.getPhotos().iterator(); iterator.hasNext();) {
				Photo photo = (Photo) iterator.next();
				Illustration illustration = photo.getIllustration();
				
				if (illustration.getOriginalHeight()>0) {
				
					if (first && index) {
						
//						String uri = !illustration.getPath().isEmpty()?"file:///"+illustration.getPath():illustration.getLink();
//						imageLoader.displayImage(uri , holder.imgTL);
						if (!illustration.getPath().isEmpty()) {
							Glide.with(context).load(new File(illustration.getPath())).into(holder.imgTL);
						}else {
							Glide.with(context).load(illustration.getLink()).into(holder.imgTL);
						}
						first = false;
						second = true;
						index = false;
					}
					if (second && index) {
						
//						String uri = !illustration.getPath().isEmpty()?"file:///"+illustration.getPath():illustration.getLink();
//						imageLoader.displayImage(uri , holder.imgTR);
						if (!illustration.getPath().isEmpty()) {
							Glide.with(context).load(new File(illustration.getPath())).into(holder.imgTR);
						}else {
							Glide.with(context).load(illustration.getLink()).into(holder.imgTR);
						}
						second = false;
						third = true;
						index = false;
					}
	
					if (third && index) {
//						String uri = !illustration.getPath().isEmpty()?"file:///"+illustration.getPath():illustration.getLink();
//						imageLoader.displayImage(uri , holder.imgBL);
						if (!illustration.getPath().isEmpty()) {
							Glide.with(context).load(new File(illustration.getPath())).into(holder.imgBL);
						}else {
							Glide.with(context).load(illustration.getLink()).into(holder.imgBL);
						}
						third = false; 
						fourth = true;
						index = false;
					}
	
					if (fourth && index) {
//						String uri = !illustration.getPath().isEmpty()?"file:///"+illustration.getPath():illustration.getLink();
//						imageLoader.displayImage(uri , holder.imgBR);
						if (!illustration.getPath().isEmpty()) {
							Glide.with(context).load(new File(illustration.getPath())).into(holder.imgBR);
						}else {
							Glide.with(context).load(illustration.getLink()).into(holder.imgBR);
						}
						fourth = false;
						break;
					}
					
					index = true;
				}
			}
			if (first && !second) {
				for (Iterator<Photo> iterator = album.getPhotos().iterator(); iterator.hasNext();) {
					Photo photo = (Photo) iterator.next();
					Illustration illustration = photo.getIllustration();
					
					
						if (first && index) {
							
//							String uri = illustration.getLink();
////							imageLoader.displayImage(uri , holder.imgTL);
//							try {
//								Glide.with(context).load(uri).into(holder.imgTL);
//							} catch (Exception e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
							try {
								if (!illustration.getPath().isEmpty()) {
									Glide.with(context).load(new File(illustration.getPath())).into(holder.imgTL);
								}else {
									Glide.with(context).load(illustration.getLink()).into(holder.imgTL);
								}
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							first = false;
							second = true;
							index = false;
						}
						if (second && index) {
							
							try {
								if (!illustration.getPath().isEmpty()) {
									Glide.with(context).load(new File(illustration.getPath())).into(holder.imgTR);
								}else {
									Glide.with(context).load(illustration.getLink()).into(holder.imgTR);
								}
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							second = false;
							third = true;
							index = false;
						}
		
						if (third && index) {
							try {
								if (!illustration.getPath().isEmpty()) {
									Glide.with(context).load(new File(illustration.getPath())).into(holder.imgBL);
								}else {
									Glide.with(context).load(illustration.getLink()).into(holder.imgBL);
								}
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							third = false; 
							fourth = true;
							index = false;
						}
		
						if (fourth && index) {
							try {
								if (!illustration.getPath().isEmpty()) {
									Glide.with(context).load(new File(illustration.getPath())).into(holder.imgBR);
								}else {
									Glide.with(context).load(illustration.getLink()).into(holder.imgBR);
								}
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							fourth = false;
							break;
						}
						
						index = true;
				}
			}
			
		}
		return convertView;
	}
	
	static class ViewHolder {
		AutoResizeTextView Title;
		AutoResizeTextView albumImgCount; 
		  ImageView imgTL; // T : TOP , B : BOTTOM , L : LEFT , R : RIGHT
		  ImageView imgTR;
		  ImageView imgBL;
		  ImageView imgBR;
		  int position;
		}

}
