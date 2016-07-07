package com.euphor.paperpad.activities.fragments;

import android.R.style;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.euphor.paperpad.Beans.Parameters;
import com.euphor.paperpad.InterfaceRealm.CommunElements1;
import com.euphor.paperpad.R;
import com.euphor.paperpad.activities.main.MainActivity;
import com.euphor.paperpad.activities.main.MyApplication;
import com.euphor.paperpad.adapters.CategoriesAdapter;
import com.euphor.paperpad.Beans.Category;
import com.euphor.paperpad.Beans.Child_pages;
import com.euphor.paperpad.Beans.Illustration;
import com.euphor.paperpad.Beans.Section;

import com.euphor.paperpad.utils.Colors;
import com.euphor.paperpad.utils.Utils;
import com.euphor.paperpad.utils.jsonUtils.AppHit;


import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import io.realm.Realm;

public class CategoryFragment_ extends ListFragment{

	private static int IS_HEADER_ADDED = 0;
	private Collection<Child_pages> elements;
	private CategoriesAdapter adapter;
	
	//private MySplitAdapter adapter_;
	
//	private DisplayImageOptions options;
	private Colors colors;

//	private ImageLoader imageLoader;
	private String titleInStrip = null;
//	private boolean isBottomSlider = false;
	private List<CommunElements1> communElements;
	private long time;
	private int id;
	private boolean isTablet;
    public Realm realm;
	/** Uness Modif **/
	public static ListView list;
	//public static boolean isNewDesign = false;
	public static int mActivatedPosition = ListView.INVALID_POSITION;
	


	/* (non-Javadoc)
	 * @see android.support.v4.app.ListFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		/** to Modify ==> Make a new Layout of divided list screen instead of categories_list **/
		View view = inflater.inflate(R.layout.categories_list, container, false); 
		if (isTablet) {
			Object obj = null;
			String displayType;
			if (communElements.size()>0) {
				if (communElements.get(0) instanceof Child_pages) {
					Child_pages dispPage = (Child_pages) communElements.get(0);
					if (dispPage.getCategory() != null) {
						if (dispPage.getCategory().getDisplay_type()!=null) {
							displayType = dispPage.getCategory().getDisplay_type();
							obj = dispPage.getCategory();
						}else { 
							displayType = "";
						}

					}else {
						displayType = "";
					}
					if (displayType.equalsIgnoreCase("illustration_list")) {
						view = inflater.inflate(R.layout.categories_list_illustration, container, false);
						addImageForList(view, obj);
//					}
//						else if(displayType.equalsIgnoreCase("split_list")) {
//						view = inflater.inflate(R.layout.split_list, container, false);

					}else {
						view = inflater.inflate(R.layout.categories_list, container, false);

					}

				}else if (communElements.get(0) instanceof Category) {
					Category dispCat = (Category) communElements.get(0);
					if (dispCat.getParentCategory()!=null) {
						if (dispCat.getParentCategory().getDisplay_type()!=null) {
							displayType = dispCat.getParentCategory().getDisplay_type();
							obj = dispCat.getParentCategory();
						}else {
							displayType = "";
						}
						//						displayType = dispCat.getParentCategory().getDisplay_type()!=null ? dispCat.getParentCategory().getDisplay_type() :"";
					}else if (dispCat.getSection() != null) {
						if (dispCat.getSection().getDisplay_type()!=null) {
							displayType = dispCat.getSection().getDisplay_type();
							obj = dispCat.getSection();
						}else {
							displayType = "";
						}

					}else {
						displayType = "";
					}
					if (displayType.equalsIgnoreCase("illustration_list")) {
						view = inflater.inflate(R.layout.categories_list_illustration, container, false);
						addImageForList(view, obj);
//					}else if(displayType.equalsIgnoreCase("split_list")) {
//						view = inflater.inflate(R.layout.split_list, container, false);

					}
					else{
						view = inflater.inflate(R.layout.categories_list, container, false);
					}

				}
			}
		}else {
			/** to modify here "inject layout split_list here**/
			view = inflater.inflate(R.layout.categories_list, container, false);

		}


		view.setBackgroundColor(colors.getColor(colors.getBackground_color()));

		/** list to modify **/
		ListView listView = (ListView)view.findViewById(android.R.id.list);
		//Add Header view to show the category title!	
		if (titleInStrip!=null && !titleInStrip.isEmpty()) {
			View viewTitle = inflater.inflate(R.layout.title_strip, null, false);
			viewTitle.findViewById(R.id.TitleHolder).setBackgroundDrawable(colors.getForePD());//.setBackgroundColor(colors.getColor(colors.getForeground_color()));
			TextView titleContactsTV = (TextView)viewTitle.findViewById(R.id.TitleTV);
//			titleContactsTV.setTypeface(MainActivity.FONT_TITLE);
//			titleContactsTV.setText(titleInStrip);
			
			titleContactsTV.setTextAppearance(getActivity(), style.TextAppearance_Large);
			titleContactsTV.setTypeface(MainActivity.FONT_TITLE);
			titleContactsTV.setText(titleInStrip);

			DisplayMetrics metrics = new DisplayMetrics(); 
			getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics); 
			
				if(Utils.isTablet(getActivity())){
					if(metrics.densityDpi >= 213 )
						titleContactsTV.setTextSize(titleContactsTV.getTextSize() - 3);
					else
						titleContactsTV.setTextSize(titleContactsTV.getTextSize() + 2);
				}
				else{
					if(metrics.densityDpi <= 240 )
						titleContactsTV.setTextSize(titleContactsTV.getTextSize() - 16);
					else
						titleContactsTV.setTextSize(titleContactsTV.getTextSize() - 22);
				}
				
//			titleContactsTV.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/gill-sans-light.ttf"));
//			titleContactsTV.setTextSize(24);
			titleContactsTV.setTextColor(colors.getColor(colors.getBackground_color()));
			viewTitle.setClickable(false);
			(listView).addHeaderView(viewTitle, null, false);
			IS_HEADER_ADDED = 1;
		}else {
			IS_HEADER_ADDED = 0;
		}

		(listView).setDivider(new ColorDrawable(colors.getColor(colors.getForeground_color(), "80")));
		(listView).setDividerHeight(1);
		//		}
		

//		if(getArguments().containsKey("split_list")){
//			(listView).setMinimumHeight(70);
//		}
		
		list = listView;

		
		return view;

	}




	
	private void addImageForList(View view, Object obj) {
		ImageView imageCat = (ImageView) view.findViewById(R.id.image_category);
		Illustration illustration = null;
		if (obj instanceof Category) {
			illustration = ((Category)obj).getIllustration();
		}else if (obj instanceof Section){
			illustration = ((Section)obj).getIllustrationObj();
		}
		if (illustration != null) {
			if (!illustration.getPath().isEmpty()) {
				Glide.with(getActivity()).load(new File(illustration.getPath())).into(imageCat);
			}else {
				Glide.with(getActivity()).load(illustration.getLink()).into(imageCat);
			} 
		}else {
			imageCat.setBackgroundColor(colors.getColor(colors.getForeground_color(), "AA"));
		}

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
		realm = Realm.getInstance(getActivity());
      //new AppController(getActivity());
		colors = ((MainActivity)activity).colors;
        Parameters ParamColor = realm.where(Parameters.class).findFirst();
        if (colors==null) {
            colors = new Colors(ParamColor);
        }

		((MainActivity)getActivity()).bodyFragment = "CategoryFragment";
		if(((MainActivity)getActivity()).extras == null)
		((MainActivity)getActivity()).extras = new Bundle();
		int section_id = -1;
		if (getArguments().getInt("Section_id")!=0) {
			section_id = getArguments().getInt("Section_id");
			((MainActivity)getActivity()).extras.putInt("Section_id", section_id);
			id = section_id; 
		}else {
			int category_id = getArguments().getInt("Category_id");
			((MainActivity)getActivity()).extras.putInt("Category_id", category_id);
			id = category_id;
		}
        isTablet = Utils.isTablet(activity); //getResources().getBoolean(R.bool.isTablet);
		time = System.currentTimeMillis();

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

		int category_id = getArguments().getInt("Category_id");
		Category category = realm.where(Category.class).equalTo("id",category_id).findFirst();//appController.getCategoryById(category_id);
		if (category != null) {
			fillListCategories(communElements, category);
		}


		int section_id = getArguments().getInt("Section_id");

        Section section = realm.where(Section.class).equalTo("id",section_id).findFirst();
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


        adapter = new CategoriesAdapter((MainActivity) getActivity(), communElements, colors, R.layout.categories_list_item);
			setListAdapter(adapter);

		
		

	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
		AppHit hit = new AppHit(System.currentTimeMillis(), time, "sales_category", id);
		((MyApplication)getActivity().getApplication()).hits.add(hit);
		super.onDestroy();
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.ListFragment#getSelectedItemId()
	 */
	@Override
	public long getSelectedItemId() {
		// TODO Auto-generated method stub
		return super.getSelectedItemId();
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.ListFragment#getSelectedItemPosition()
	 */
	@Override
	public int getSelectedItemPosition() {
		// TODO Auto-generated method stub
		return super.getSelectedItemPosition();
	} 

	/* (non-Javadoc)
	 * @see android.support.v4.app.ListFragment#onListItemClick(android.widget.ListView, android.view.View, int, long)
	 */
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {

		CommunElements1 element = adapter.getElements().get(position-IS_HEADER_ADDED); // because of the listView header we subtract 1
		{ 
			if (element instanceof Category) {
				Category category = (Category)element;

				((MainActivity) getActivity()).openCategory(category);

			}else if (element instanceof Child_pages) {

				Child_pages page = (Child_pages)element;
				if(!page.isHide_product_detail())
					((MainActivity) getActivity()).openChildPage(page,false);

			}
		}

	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.ListFragment#setListAdapter(android.widget.ListAdapter)
	 */
	@Override
	public void setListAdapter(ListAdapter adapter) {
		// TODO Auto-generated method stub
		super.setListAdapter(adapter);
	}

	/**
	 * 
	 */
	public CategoryFragment_() {
		super();
		// TODO Auto-generated constructor stub
	}


	public void fillListCategories(List<CommunElements1> communElements, Category category) {
		titleInStrip = category.getTitle();

		if (category.getCategories().size()>1) {
			for (Iterator<Category> iterator = category.getCategories().iterator(); iterator
					.hasNext();) {
				CommunElements1 communElement = (CommunElements1) iterator
						.next();
				communElements.add(communElement);
			}
		}else if (category.getCategories().size()==1) {
			Category childCategory = category.getCategories().iterator().next();
			fillListCategories(communElements, childCategory);

		}else {

			elements = category.getChildren_pages();

			int size = elements.size();
			if (size > 0) {
				for (Iterator<Child_pages> iterator = elements
						.iterator(); iterator.hasNext();) {
					CommunElements1 communElement = (Child_pages) iterator.next();
					if (((Child_pages)communElement).isVisible()) {
						communElements.add(communElement);
					}  

				}
			} 
			
		}


	}

	@Override
	public void onStop() {
		AppHit hit = new AppHit(System.currentTimeMillis()/1000, time/1000, "sales_category", id);
		((MyApplication)getActivity().getApplication()).hits.add(hit);
		super.onStop();
	}

}
