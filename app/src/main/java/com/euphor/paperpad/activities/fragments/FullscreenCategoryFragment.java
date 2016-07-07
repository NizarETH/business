/**
 * 
 */
package com.euphor.paperpad.activities.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.euphor.paperpad.InterfaceRealm.CommunElements1;
import com.euphor.paperpad.R;
import com.euphor.paperpad.activities.main.MainActivity;
import com.euphor.paperpad.Beans.CategoriesMyBox;
import com.euphor.paperpad.Beans.Category;
import com.euphor.paperpad.Beans.Child_pages;
import com.euphor.paperpad.Beans.Illustration;
import com.euphor.paperpad.Beans.Section;

import com.euphor.paperpad.utils.Colors;
import com.euphor.paperpad.utils.Utils;
import com.euphor.paperpad.widgets.ArrowImageView;


import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import io.realm.Realm;

/**
 * @author euphordev02
 *
 */
public class FullscreenCategoryFragment extends Fragment{

	
//	private static final int PREVIOUS = -1;
//	private static final int NEXT = 1;
//	private static int IS_HEADER_ADDED = 0;
	private Colors colors;

	private String titleInStrip = null;
//	private boolean isBottomSlider = false;
	private List<CommunElements1> communElements;
//	private List<Category> categories;
	protected int id_cat = 0;
	protected LinearLayout choiceHolder;
	protected Category cat;
//	private MainActivity mainActivity;
	private int section_id;
	private int category_id;
	private boolean isTablet;

    public Realm realm;


    /**
	 * 
	 */
	public FullscreenCategoryFragment() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.ListFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view;
		view = inflater.inflate(R.layout.fullscreen_category_fragment, container, false);
		view.setBackgroundColor(colors.getColor(colors.getBackground_color()));
		
		RelativeLayout categoryHolder1 = (RelativeLayout)view.findViewById(R.id.Category_holder1);
		RelativeLayout categoryHolder2 = (RelativeLayout)view.findViewById(R.id.Category_holder2);
		RelativeLayout categoryHolder3 = (RelativeLayout)view.findViewById(R.id.Category_holder3);
		
		if (communElements.size() > 0) {
			CommunElements1 pCommunElements = communElements.get(0);
			ImageView imageItem = (ImageView)view.findViewById(R.id.imgCategory1);
			if (pCommunElements.getIllustration1() != null) {
				
				imageItem.setScaleType(ScaleType.CENTER_CROP);
				Illustration illustration = pCommunElements.getIllustration1();
				if (!illustration.getPath().isEmpty()) {
					Glide.with(getActivity()).load(new File(illustration.getPath())).into(imageItem);
				}else {
					Glide.with(getActivity()).load(illustration.getLink()).into(imageItem);
				}
				
			}
			categoryHolder1.setTag(pCommunElements);
			LinearLayout categoryLayout1 = (LinearLayout)view.findViewById(R.id.Category1);
			buildCategoryView(view, inflater, pCommunElements, categoryLayout1, categoryHolder1);
		}else {
			categoryHolder1.setVisibility(View.GONE);
		}
		if (communElements.size() > 1) {
			
			CommunElements1 pCommunElements = communElements.get(1);
			categoryHolder2.setTag(pCommunElements);
			ImageView imageItem = (ImageView)view.findViewById(R.id.imgCategory2);
			if (pCommunElements.getIllustration1() != null) {
				
				imageItem.setScaleType(ScaleType.CENTER_CROP);
				Illustration illustration = pCommunElements.getIllustration1();
				if (!illustration.getPath().isEmpty()) {
					Glide.with(getActivity()).load(new File(illustration.getPath())).into(imageItem);
				}else {
					Glide.with(getActivity()).load(illustration.getLink()).into(imageItem);
				}
				
			}
			
			LinearLayout categoryLayout2 = (LinearLayout)view.findViewById(R.id.Category2);
			buildCategoryView(view, inflater, pCommunElements, categoryLayout2, categoryHolder2);
		}else {
			categoryHolder2.setVisibility(View.GONE);
		}
		
		if (communElements.size() > 2) {
			
			CommunElements1 pCommunElements = communElements.get(2);
			categoryHolder3.setTag(pCommunElements);
			ImageView imageItem = (ImageView)view.findViewById(R.id.imgCategory3);
			if (pCommunElements.getIllustration1() != null) {
				
				imageItem.setScaleType(ScaleType.CENTER_CROP);
				Illustration illustration = pCommunElements.getIllustration1();
				if (!illustration.getPath().isEmpty()) {
					Glide.with(getActivity()).load(new File(illustration.getPath())).into(imageItem);
				}else {
					Glide.with(getActivity()).load(illustration.getLink()).into(imageItem);
				}	
			}
			
			LinearLayout categoryLayout3 = (LinearLayout)view.findViewById(R.id.Category3);
			buildCategoryView(view, inflater, pCommunElements, categoryLayout3, categoryHolder3);
		}else {
			categoryHolder3.setVisibility(View.GONE);
		}
		OnClickListener clickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				CommunElements1 element = (CommunElements1) v.getTag();
				if (element instanceof Category) {
					Category category = (Category)element;		
					((MainActivity) getActivity()).openCategory(category);
				}else if (element instanceof Child_pages) {	
					Child_pages page = (Child_pages)element;
					((MainActivity) getActivity()).openChildPage(page,false);
				}			
			}
		};
		categoryHolder1.setOnClickListener(clickListener);
		categoryHolder2.setOnClickListener(clickListener);
		categoryHolder3.setOnClickListener(clickListener);
		return view;
		
	}

	private void buildCategoryView(View view, LayoutInflater inflater,
			CommunElements1 pCommunElements, LinearLayout categoryLayout, RelativeLayout categoryHolder) {
		View categoryItem = inflater.inflate(R.layout.category_item, null, false);
		categoryItem.setPadding(10, 0, 10, 0);
		categoryItem.setBackgroundColor(colors.getColor(colors.getForeground_color(), "D2"));
		TextView title = (TextView)categoryItem.findViewById(R.id.TVTitleCategory);
		title.setTypeface(MainActivity.FONT_TITLE, Typeface.BOLD);
		title.setText(pCommunElements.getCommunTitle1());
		/** Modif Uness **/
		//title.setTextColor(colors.getColor(colors.getTitle_color()));

		
		title.setTextColor(colors.getColor(colors.getBackground_color()));
		
		TextView desc = (TextView)categoryItem.findViewById(R.id.TVDescCategory);
		desc.setTypeface(MainActivity.FONT_BODY);
		desc.setTextColor(colors.getColor(colors.getBackground_color(), "AA"));
		
		if (pCommunElements.getCommunDesc1().isEmpty()) {
			desc.setVisibility(View.GONE);
		}else {
			desc.setText(pCommunElements.getCommunDesc1());
		}
		
		ArrowImageView arrowImg = (ArrowImageView)categoryItem.findViewById(R.id.imgArrow);
		
		WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);

		Display display = wm.getDefaultDisplay();
		DisplayMetrics metrics = new DisplayMetrics(); 
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics); 

	
		
		if(isTablet && (metrics.densityDpi < 213))
		title.setTextSize(30);
		else if(metrics.densityDpi >= 213 && metrics.densityDpi <= 219)
			title.setTextSize(28);
		else {
		title.setTextSize(19);
//		arrowImg.getLayoutParams().width = 28;
//		arrowImg.getLayoutParams().height = 28;
		}
		Paint paint = new Paint();
		paint.setColor(colors.getColor(colors.getBackground_color(),"AA"));
		arrowImg.setPaint(paint);
		

		
		/*if (parameters != null) {
			if (!parameters.getShow_cart()) {
				arrowImg.setVisibility(View.GONE);
			}
		}*/
		if (pCommunElements instanceof Child_pages) {
			if (((Child_pages)pCommunElements).isHide_product_detail()) {
				arrowImg.setVisibility(View.GONE);
			}
		}
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		categoryLayout.addView(categoryItem, params);
		
		
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
	//new AppController(getActivity());
				realm = Realm.getInstance(getActivity());
		colors = ((MainActivity)activity).colors;

        com.euphor.paperpad.Beans.Parameters ParamColor = realm.where(com.euphor.paperpad.Beans.Parameters.class).findFirst();
        if (colors==null) {
            colors = new Colors(ParamColor);
        }
		section_id = 0;
		if (getArguments().getInt("Section_id")!=0) {
			section_id = getArguments().getInt("Section_id");
		}else {
		}
        isTablet = Utils.isTablet(activity);
//		mainActivity = (MainActivity)activity;
		category_id = getArguments().getInt("Category_id");
		((MainActivity)getActivity()).bodyFragment = "FullscreenCategoryFragment";
		if(((MainActivity)getActivity()).extras == null)
			((MainActivity)getActivity()).extras = new Bundle();
		((MainActivity)getActivity()).extras.putInt("Category_id", category_id);
		((MainActivity)getActivity()).extras.putInt("Section_id", section_id);
		super.onAttach(activity);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);

		communElements = new ArrayList<CommunElements1>();

//		int category_id = getArguments().getInt("Category_id");
		Category category = realm.where(Category.class).equalTo("id",category_id).findFirst();
		// appController.getCategoryById(category_id);
		if (category != null) {
			fillListCategories(communElements, category);
		}
		
		
//		int section_id = getArguments().getInt("Section_id");

        Section section = realm.where(Section.class).equalTo("id_s",section_id).findFirst();
        //appController.getSectionsDao().queryForId(section_id);

        if (section!=null) {
            titleInStrip = section.getTitle();
            if (section.getCategories().size() > 1) {
                for (Iterator<Category> iterator = section.getCategories()
                        .iterator(); iterator.hasNext();) {
                    CommunElements1 communElement = (CommunElements1) iterator
                            .next();
                    communElements.add(communElement);
                }
            } else if (section.getCategories().size() == 1) {

                Category childCategory = section.getCategories()
                        .iterator().next();
                fillListCategories(communElements, childCategory);

            }
        }

    }

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	public List<Category> getChildCategories(Category category) {
		List<Category> result = new ArrayList<Category>();
		if (category.getChildren_categories().size()>0) {
			for (Iterator<Category> iterator = category.getChildren_categories().iterator(); iterator
					.hasNext();) {
				Category communElement = (Category) iterator
						.next();
				result.add(communElement);
			}
		}
		
		return result;
		
		
	}
	
	public List<Child_pages> getChildrenPages(Category category) {
		List<Child_pages> result = new ArrayList<Child_pages>();
		Collection<Child_pages> elements = category.getChildren_pages();

		int size = elements.size();
		if (size > 0) {
			for (Iterator<Child_pages> iterator = elements
					.iterator(); iterator.hasNext();) {
				Child_pages communElement = (Child_pages) iterator
						.next();
				if (communElement.isVisible()) {
					result.add(communElement);
				}
				
			}
		}
		return result;
		
	}
	
	/** a method to fill the upper bar where we choose the {@link CategoriesMyBox}
	 * @param category
	 */

	View selectedBefore;
//	private int indexCurrent;
	protected ArrayList<Child_pages> pages;
//	private Parameters parameters;


	
//	private Paint getNewPaint() {
//		Paint paint = new Paint();
//		paint.setColor(colors.getColor(colors.getForeground_color(),"FF"));
//		return paint;
//	}
	
	public void fillListCategories(List<CommunElements1> communElements, Category category) {
		titleInStrip = category.getTitle();
		Collection<Child_pages> elements;
		if (category.getChildren_categories().size()>1) {
			for (Iterator<Category> iterator = category.getChildren_categories().iterator(); iterator
					.hasNext();) {
				CommunElements1 communElement = (CommunElements1) iterator
						.next();
				communElements.add(communElement);
			}
		}else if (category.getChildren_categories().size()==1) {
			Category childCategory = category.getChildren_categories().iterator().next();
			fillListCategories(communElements, childCategory);
		
		}else {
		
			elements = category.getChildren_pages();

			int size = elements.size();
			if (size > 1) {
				for (Iterator<Child_pages> iterator = elements
						.iterator(); iterator.hasNext();) {
					CommunElements1 communElement = (Child_pages) iterator.next();
					if (((Child_pages)communElement).isVisible()) {
						communElements.add(communElement);
					}
					
				}
			} else if (size == 1) {
				Child_pages page = elements.iterator().next();
				communElements.add(page);
				if (page.getDesign().equals("panoramic")) {
					FragmentTransaction fragmentTransaction =
					         getActivity().getSupportFragmentManager().beginTransaction();
					 Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("panorama");
					    if (prev != null) {
					    	fragmentTransaction.remove(prev);
					    }
					    Fragment panoFragment = new PanoramaFragment();
					    ((MainActivity) getActivity()).extras.putInt("page_id", page.getId_cp());
					    ((MainActivity) getActivity()).bodyFragment = "PanoramaFragment";
					    panoFragment.setArguments(((MainActivity) getActivity()).extras);
					 fragmentTransaction.replace(R.id.fragment_container, panoFragment, "panorama");
					 fragmentTransaction.addToBackStack(null).commit();
				}else {
					((MainActivity)getActivity()).extras = new Bundle();
					((MainActivity)getActivity()).extras.putInt("page_id", page.getId_cp());
					PagesFragment pagesFragment = new PagesFragment();
					((MainActivity)getActivity()).bodyFragment = "PagesFragment";
					pagesFragment.setArguments(((MainActivity)getActivity()).extras);
					getActivity()
							.getSupportFragmentManager()
							.beginTransaction()
							.replace(R.id.fragment_container,
									pagesFragment).addToBackStack(null)
							.commit();
				}
				
			}
		
		}
		
		
	}
	
	

}
