/**
 * 
 */
package com.euphor.paperpad.activities.fragments;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.euphor.paperpad.Beans.Parameters;
import com.euphor.paperpad.R;
import com.euphor.paperpad.activities.main.MainActivity;
import com.euphor.paperpad.Beans.Child_pages;
import com.euphor.paperpad.Beans.ElementSwipe;
import com.euphor.paperpad.Beans.Illustration;

import com.euphor.paperpad.utils.Colors;
import com.euphor.paperpad.widgets.ArrowImageView;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

/**
 * @author euphordev02
 *
 */
public class SwipePage extends Fragment {

	private String txt;

	private Colors colors;
	private MainActivity mainActivity;
	ElementSwipe elementSwipe;
    public Realm realm;

    @Override
	public void onAttach(Activity activity) {
		//new AppController(getActivity());
				realm = Realm.getInstance(getActivity());
		colors = ((MainActivity)activity).colors;
        Parameters ParamColor = realm.where(Parameters.class).findFirst();
        if (colors==null) {
            colors = new Colors(ParamColor);
        }

        mainActivity = (MainActivity)activity;
		int id_element = getArguments().getInt(EXTRA_MESSAGE);
        elementSwipe = realm.where(ElementSwipe.class).equalTo("id",id_element).findFirst();
        //appController.getElemantsDao().queryForId(id_element);
        super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.myfragment_layout, container, false);
		if (elementSwipe != null) {
			ImageView image_swipe = (ImageView)layout.findViewById(R.id.image_swipe);
			Illustration illust = elementSwipe.getIllustration();
			if (illust != null) {
				String path = !illust .getPath().isEmpty()?"file:///"+illust.getPath():illust.getLink();
//				imageLoader.displayImage(path, image_swipe);
				Glide.with(this).load(new File(illust.getPath())).into(image_swipe);
				
			}else {
				Glide.with(this).load(elementSwipe.getImage()).into(image_swipe);
//				imageLoader.displayImage(elementSwipe.getImage(), image_swipe);
			}
			
		}
		LinearLayout linkHolder = (LinearLayout)layout.findViewById(R.id.linkHolder);
		
		if (elementSwipe.getPage_id()!= 0) {
			linkHolder.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					List<Child_pages> pages = new ArrayList<Child_pages>();
                    pages = realm.where(Child_pages.class).equalTo("id", elementSwipe.getPage_id()).findAll();
                    // appController.getChildPageDao().queryForEq("id", elementSwipe.getPage_id());
                    if (pages.size()>0) {
						Child_pages page = pages.get(0);
						if (page.getDesign().equals("panoramic")) {
							FragmentTransaction fragmentTransaction =getActivity().getSupportFragmentManager().beginTransaction();
							Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("panorama");
							if (prev != null) {
								fragmentTransaction.remove(prev);
							}
							Fragment panoFragment = new PanoramaFragment();
							((MainActivity) getActivity()).extras.putInt("page_id", page.getId_cp());
							((MainActivity) getActivity()).bodyFragment = "PanoramaFragment";
							panoFragment.setArguments(((MainActivity) getActivity()).extras);
							fragmentTransaction.addToBackStack(null);
							fragmentTransaction.replace(R.id.fragment_container, panoFragment, "panorama");
							fragmentTransaction.commit();
						}else if (page.getDesign().equalsIgnoreCase("column")) {
                            ((MainActivity)getActivity()).extras = new Bundle();
                            ((MainActivity)getActivity()).extras.putInt("page_id", page.getId_cp());
                            ((MainActivity)getActivity()).bodyFragment = "ColonnePageFragment";
                            ColonnePageFragment colonnePageFragment = new ColonnePageFragment();
                            colonnePageFragment.setArguments(((MainActivity)getActivity()).extras);
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, colonnePageFragment).addToBackStack(((MainActivity)getActivity()).bodyFragment).commit();
                        }else {
							((MainActivity) getActivity()).extras = new Bundle();
							((MainActivity) getActivity()).extras.putInt("page_id", page.getId_cp());
							PagesFragment pagesFragment = new PagesFragment();
							pagesFragment.setArguments(((MainActivity) getActivity()).extras);
							getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, pagesFragment).addToBackStack(null).commit();
						}
					}
					
					mainActivity.timer.cancel();
					getActivity().findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
					getActivity().findViewById(R.id.swipe_container).setVisibility(View.GONE);
					
				}
			});
		}else {
			linkHolder.setVisibility(View.GONE);
		}
		StateListDrawable stateDrawable = new StateListDrawable();
		stateDrawable.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(colors.getColor(colors.getBackground_color(),"88")));
		stateDrawable.addState(new int[]{android.R.attr.state_focused}, new ColorDrawable(colors.getColor(colors.getBackground_color(),"88"))); 
		stateDrawable.addState(new int[]{}, colors.getBackTabsLG()); 
		linkHolder.setBackgroundDrawable(stateDrawable);
		
		TextView linkTitle = (TextView)layout.findViewById(R.id.linkTitle);
		linkTitle.setText(elementSwipe.getCaption());
		ColorStateList colorStateList = new ColorStateList(
				new int[][] {new int[] { android.R.attr.state_pressed }, new int[] {} },
				new int[] {colors.getColor(colors.getTitle_color()), colors.getColor(colors.getSide_tabs_foreground_color()) });
		linkTitle.setTextColor(colorStateList);
		ArrowImageView linkArrow = (ArrowImageView)layout.findViewById(R.id.linkArrow);
		Paint paint = new Paint();
		paint.setColor(colors.getColor(colors.getSide_tabs_foreground_color()));
		linkArrow.setPaint(paint );
		
		return layout;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	public SwipePage() {
		super();
		// TODO Auto-generated constructor stub
	}
	public static final String EXTRA_MESSAGE = "ID_ELEMENT";
	public static SwipePage newInstance(int id_element) {
		SwipePage f = new SwipePage();
		
		Bundle bdl = new Bundle(1);
	    bdl.putInt(EXTRA_MESSAGE, id_element);
	    f.setArguments(bdl);
	    return f;

	}

	public String getTxt() {
		return txt;
	}

	public void setTxt(String txt) {
		this.txt = txt;
	}
	
//	public static void newInstance(String txt) {
//		this.txt = txt;
//	}

}
