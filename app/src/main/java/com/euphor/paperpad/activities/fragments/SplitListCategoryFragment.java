package com.euphor.paperpad.activities.fragments;


import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.euphor.paperpad.Beans.Parameters;
import com.euphor.paperpad.InterfaceRealm.CommunElements1;
import com.euphor.paperpad.R;
import com.euphor.paperpad.activities.main.MainActivity;
import com.euphor.paperpad.activities.main.MyApplication;
import com.euphor.paperpad.adapters.MySplitAdapter;
import com.euphor.paperpad.Beans.Category;
import com.euphor.paperpad.Beans.Child_pages;
import com.euphor.paperpad.Beans.Contact;

import com.euphor.paperpad.utils.Colors;
import com.euphor.paperpad.utils.jsonUtils.AppHit;
import com.euphor.paperpad.widgets.ArrowImageView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * @author euphordev04
 *
 */

public class SplitListCategoryFragment extends Fragment{

	private static int IS_HEADER_ADDED = 0;
	private Collection<Child_pages> elements;
	//private CategoriesAdapter adapter;
	public static MySplitAdapter adapter;
	//private MySplitAdapter adapter_;

	//	private DisplayImageOptions options;
	private Colors colors;

	//	private ImageLoader imageLoader;
	private String titleInStrip = null;
	//	private boolean isBottomSlider = false;
	private List<CommunElements1> communElements;
	private long time;
	private int id;
	//	private boolean isTablet;
	//	private FrameLayout frameLayout;
	private View view;

    public Realm realm;
	public static ListView list;
	//public static boolean isNewDesign = false;
	public static int mActivatedPosition = 1;

	//	View view;
	//	LayoutInflater inflater;




	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if(isVisibleToUser) {
			Activity a = getActivity();
			if(a != null) a.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.ListFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		/** to Modify ==> Make a new Layout of divided list screen instead of categories_list **/
		//	    LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		view = inflater.inflate(R.layout.split_list, container, false); 


		view.setBackgroundColor(colors.getColor(colors.getBackground_color()));

		//		this.inflater = inflater;

		//		if(list !=null) list = null;

		/** list to modify **/
		//ListView 
		list = (ListView)view.findViewById(R.id.listOfSplit);
		//Add Header view to show the category title!	
		if (titleInStrip!=null && !titleInStrip.isEmpty()) {
			View viewTitle = inflater.inflate(R.layout.title_strip, null, false);
			viewTitle.findViewById(R.id.TitleHolder).setBackgroundDrawable(colors.getForePD());
			TextView titleContactsTV = (TextView)viewTitle.findViewById(R.id.TitleTV);
			titleContactsTV.setTypeface(MainActivity.FONT_TITLE);
			titleContactsTV.setText(titleInStrip);
			//			titleContactsTV.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/gill-sans-light.ttf"));
			titleContactsTV.setTextSize(22);
			titleContactsTV.setTextColor(colors.getColor(colors.getBackground_color()));
			viewTitle.setClickable(false);
			list.addHeaderView(viewTitle, null, false);
			IS_HEADER_ADDED = 1;
		}else {
			IS_HEADER_ADDED = 0;
		}

		list.setDivider(new ColorDrawable(colors.getColor(colors.getForeground_color(), "80")));
		list.setDividerHeight(1);
		//		}


		//list = listView;

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> viewAdapter, View v, int position,
					long id) {



				/** Uness Modif **/


				/** End **/


				int contact_id = 445;
				String url = null; // "http://backoffice.paperpad.fr/pdf/41/CONNECTION_WIFI.pdf";
				boolean isContact = false;

				CommunElements1 element = adapter.getItem(position-IS_HEADER_ADDED);  //getElements().get(position-IS_HEADER_ADDED); // because of the listView header we subtract 1

				if(element instanceof Child_pages){
					//if(((Child_pages) element) != null){
					url = ((Child_pages) element).getAuto_open_url();
					if(((Child_pages) element).getExtra_fields() != null && ((Child_pages) element).getExtra_fields().getAuto_open_contact_form() != null
							&& !((Child_pages) element).getExtra_fields().getAuto_open_contact_form().isEmpty()){
						//contact_id = ((Child_pages) element).getRelated().getContact_form().getId();
						//Log.e("  Contact_id  ", "  autoOpenContactForm : "+((Child_pages) element).getExtra_fields().getAuto_open_contact_form());
						isContact = true;
					}

					//Log.e(" Url  "+ url, "  url : "+url );
					//}
				}

				if(isContact){

					contact_id = new ArrayList<Child_pages>(elements).get(position - IS_HEADER_ADDED).getRelated().getContact_form().getId();

                    RealmResults<Contact> contact = realm.where(Contact.class).equalTo("id", contact_id).findAll(); //appController.getContactDao().queryForEq("id", contact_id);
                    if(contact.size()>0)
                        contact_id = contact.get(0).getId_con();
                    //					Log.e(" SplitListCategoryFragment <=== contact_id "," : "+contact_id);

					((MainActivity) getActivity()).extras = new Bundle();
					//					int contact_id = new ArrayList<Child_pages>(elements).get(position - IS_HEADER_ADDED).getRelated().getContact_form().getId();
					((MainActivity) getActivity()).extras.putInt("Contact",contact_id );
					((MainActivity) getActivity()).extras.putBoolean("newDesign",true);
					//((MainActivity) getActivity()).extras.putInt("Contact", 445);
					FormContactFragment formFragment = new FormContactFragment();
					formFragment.setArguments(((MainActivity) getActivity()).extras);
					getActivity().getSupportFragmentManager().beginTransaction()
					.replace(R.id.navitem_detail_container, formFragment).addToBackStack(null).commit();


				}else if(url != null && !url.isEmpty()){

					//					Log.e(" SplitListCategoryFragment <=== url "," : "+url);

					((MainActivity) getActivity()).extras = new Bundle();
					//String url = new ArrayList<Child_pages>(elements).get(position - IS_HEADER_ADDED).getAuto_open_url();
					((MainActivity) getActivity()).extras.putString("link", /** URL to GoogleDoc => **/"http://docs.google.com/gview?embedded=true&url=" +  /** URL to PDF => **/url);
					//((MainActivity) getActivity()).extras.putString("link", /** URL to GoogleDoc => **/"http://docs.google.com/gview?embedded=true&url=" +  /** URL to PDF => **/ "http://backoffice.paperpad.fr/pdf/41/CONNECTION_WIFI.pdf");

					WebViewFragment webFragment = new WebViewFragment();
					webFragment.setArguments(((MainActivity) getActivity()).extras);
					getActivity().getSupportFragmentManager().beginTransaction()
					.replace(R.id.navitem_detail_container, webFragment).addToBackStack(null).commit();
				}
				else{ 
					//Log.e(" SplitListCategoryFragment <=== passage normal "," : passage normal");

					if (element instanceof Category) {
						Category category = (Category)element;

						((MainActivity) getActivity()).openCategory(category);

					}else if (element instanceof Child_pages) {

						Child_pages page = (Child_pages)element;
						((MainActivity) getActivity()).openChildPage(page,false);

					}
				}


				if(mActivatedPosition != position){
					if(list.getChildAt(mActivatedPosition - list.getFirstVisiblePosition()) != null)
					{

						//						if(((TextView)list.getChildAt(mActivatedPosition - list.getFirstVisiblePosition()).findViewById(R.id.TVTitleCategory)) != null) {
						list.getChildAt(mActivatedPosition - list.getFirstVisiblePosition()).setBackgroundColor(colors.getColor(colors.getBackground_color()));

						((TextView)list.getChildAt(mActivatedPosition - list.getFirstVisiblePosition()).findViewById(R.id.TVTitleCategory)).setTextColor(colors.getColor(colors.getTitle_color()));
						//						Paint paint = new Paint();
						//						paint.setColor(colors.getColor(colors.getTitle_color()));
						//						((ArrowImageView)list.getChildAt(position - list.getFirstVisiblePosition()).findViewById(R.id.imgArrow)).setPaint(paint);
						//((ArrowImageView)list.getChildAt(position - list.getFirstVisiblePosition()).findViewById(R.id.imgArrow)).setDiffOfColorCode(colors.getColor(colors.getForeground_color()), colors.getColor(colors.getBackground_color()));
						((ArrowImageView)list.getChildAt(mActivatedPosition - list.getFirstVisiblePosition()).findViewById(R.id.imgArrow)).setBackgroundColor(colors.getColor(colors.getBackground_color()));
						//						Log.e(" SpltListCategory <<normal>> Color Changed To :  "+mActivatedPosition, ""+colors.getTitle_color());
						//						}
					}

					mActivatedPosition = position;

					list.getChildAt(mActivatedPosition - list.getFirstVisiblePosition()).setBackgroundColor(colors.getColor(colors.getForeground_color()));//Color(colors.getColor(colors.getTitle_color()));

					((TextView)list.getChildAt(mActivatedPosition - list.getFirstVisiblePosition()).findViewById(R.id.TVTitleCategory)).setTextColor(colors.getColor(colors.getBackground_color()));
					((ArrowImageView)list.getChildAt(mActivatedPosition - list.getFirstVisiblePosition()).findViewById(R.id.imgArrow)).setBackgroundColor(colors.getColor(colors.getForeground_color()));

				}



				//				if(list.getChildAt(mActivatedPosition - list.getFirstVisiblePosition()) != null){
				//					list.getChildAt(mActivatedPosition - list.getFirstVisiblePosition()).setBackgroundDrawable(colors.makeGradientToColor(colors.getTitle_color()));//Color(colors.getColor(colors.getTitle_color()));
				//					((TextView)list.getChildAt(mActivatedPosition - list.getFirstVisiblePosition()).findViewById(R.id.TVTitleCategory)).setTextColor(colors.getColor(colors.getBackground_color()));
				//					Log.e(" SpltListCategory <<pressed>> Color Changed To :  ", ""+colors.getBackground_color());

				//				}


				//				CategoryFragment.list.smoothScrollToPosition(mActivatedPosition);
				SplitListFragment.currentDetailPosition = mActivatedPosition;
				//SplitFilteredCategoriesFragment.mActivatedPosition = mActivatedPosition;



			}
		});

		//setListAdapter(adapter);

		//		frameLayout = new FrameLayout(getActivity());
		//		frameLayout.addView(view);
		return view;

	}


	public void setActivateOnItemClick(boolean activateOnItemClick) {
		// When setting CHOICE_MODE_SINGLE, ListView will automatically
		// give items the 'activated' state when touched.

		Log.i(" SplitListCategoryFragment <==== setActivateOnItemClick " , ""+activateOnItemClick);

		list.setChoiceMode(
				activateOnItemClick ? ListView.CHOICE_MODE_SINGLE
						: ListView.CHOICE_MODE_NONE);

	}


	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		//		if(list != null) 
		list = (ListView)getActivity().findViewById(R.id.listOfSplit);
		////		else
		////			list = getListView();
		//		adapter = new MySplitAdapter((MainActivity) getActivity(), communElements, colors, R.layout.divided_screen_item, 18);
		if(list != null)
			list.setAdapter(adapter);

		super.onActivityCreated(savedInstanceState);
	}


	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
		//		appController = ((MainActivity)getActivity()).appController;
		//		if(appController == null)
//new AppController(getActivity());
				realm = Realm.getInstance(getActivity());
			colors = ((MainActivity)activity).colors;
        Parameters ParamColor = realm.where(Parameters.class).findFirst();
        if (colors==null) {
            colors = new Colors(ParamColor);
        }


		if(((MainActivity) getActivity()).extras != null && ((MainActivity)getActivity()).extras.getBoolean("isSplitFiltered", false))
			((MainActivity)getActivity()).bodyFragment = "SplitFilteredCategoriesFragment";
		else
			((MainActivity)getActivity()).bodyFragment = "SplitListCategoryFragment";


		if(((MainActivity) getActivity()).extras == null)
			((MainActivity)getActivity()).extras = new Bundle();

		int section_id = -1;
		if(getArguments() != null) {
			if (getArguments().getInt("Section_id")!=0) {
				section_id = getArguments().getInt("Section_id");
				((MainActivity)getActivity()).extras.putInt("Section_id", section_id);
				id = section_id; 
			}
			//		else if(getArguments().containsKey("split_fragment_container")){
			//			isFilteredList = true;
			//		}
			if (getArguments().containsKey("Category_id")) {	
				int category_id = getArguments().getInt("Category_id");
				((MainActivity)getActivity()).extras.putInt("Category_id", category_id);
				id = category_id;
			}

		}

		//		isTablet = getResources().getBoolean(R.bool.isTablet);
		time = System.currentTimeMillis();

		super.onAttach(activity);
	}



	Category category;

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(((MainActivity)getActivity()).bodyFragment.equals("SplitListCategoryFragment"))
			setRetainInstance(true);

				realm = Realm.getInstance(getActivity());
		communElements = new ArrayList<CommunElements1>();

		int category_id = getArguments().getInt("Category_id");
		//Category 
		category = realm.where(Category.class).equalTo("id",category_id).findFirst();
		//appController.getCategoryById(category_id);
		if (category != null) {
			fillListCategories(communElements, category);
		}


		//		int section_id = getArguments().getInt("Section_id");
		//
		//		try {
		//			Section section = appController.getSectionsDao().queryForId(section_id);
		//
		//			if (section!=null) {
		//				titleInStrip = section.getTitle();
		//				if (section.getCategories1().size() > 1) {
		//					for (Iterator<Category> iterator = section.getCategories1()
		//							.iterator(); iterator.hasNext();) {
		//						CommunElements communElement = (CommunElements) iterator
		//								.next();
		//						communElements.add(communElement);
		//					}
		//				} else if (section.getCategories1().size() == 1) {
		//
		//					Category childCategory = section.getCategories1()
		//							.iterator().next();
		//					fillListCategories(communElements, childCategory);
		//
		//				}
		//			}



		//
		//		} catch (SQLException e) {
		//			e.printStackTrace();
		//		} 

		adapter = new MySplitAdapter((MainActivity) getActivity(), communElements, colors, R.layout.divided_screen_item, 18);
		//list.setAdapter(adapter);

	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		//adapter = new MySplitAdapter((MainActivity) getActivity(), communElements, colors, R.layout.divided_screen_item, 18);
		list.setAdapter(adapter);
		//getListView().getChildAt(1).setBackgroundColor(colors.getColor(colors.getForeground_color()));
		if(list.getChildAt(mActivatedPosition - IS_HEADER_ADDED) != null)
			list.getChildAt(mActivatedPosition - IS_HEADER_ADDED).setBackgroundColor(colors.getColor(colors.getBackground_color()));
		SplitListFragment.currentDetailPosition = mActivatedPosition;
		CommunElements1 element = null;
		if(adapter.getCount() > 0)
			element = adapter.getItem(mActivatedPosition - IS_HEADER_ADDED); //.getElements().get(0); // because of the listView header we subtract 1


		if (element instanceof Category) {
			category = (Category)element;
			((MainActivity) getActivity()).openCategory(category);

		}else if (element instanceof Child_pages) {

			Child_pages page = (Child_pages)element;
			((MainActivity) getActivity()).openChildPage(page,false);

		}
		super.onViewCreated(view, savedInstanceState);
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


	/**
	 * 
	 */
	public SplitListCategoryFragment() {
		super();
		// TODO Auto-generated constructor stub
	}


	public void fillListCategories(List<CommunElements1> communElements, Category category) {
		titleInStrip = category.getTitle();

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

			} else 
				if (size == 1) {
					Child_pages page = elements.iterator().next();
					communElements.add(page);
					//adapter = new MySplitAdapter((MainActivity) getActivity(), communElements, colors, R.layout.categories_list_item, 18);
					//list.setAdapter(adapter);

				}
			SplitListCategoryFragment.mActivatedPosition = 1;
			SplitListFragment.currentDetailPosition = 1;



		}


	}

	@Override
	public void onStop() {
		AppHit hit = new AppHit(System.currentTimeMillis()/1000, time/1000, "sales_category", id);
		((MyApplication)getActivity().getApplication()).hits.add(hit);

		if(((MainActivity) getActivity()).extras == null)
			((MainActivity)getActivity()).extras = new Bundle();
		((MainActivity)getActivity()).extras.putInt("mActivatedPosition", mActivatedPosition);
		super.onStop();
	}


	//	@Override
	//	public void onResume() {
	//        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
	//
	//		setActivateOnItemClick(true);
	//		//list.setItemChecked(1, true);
	//		super.onResume();
	//	}
	//
	//	@Override
	//	public void onPause() {
	//        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // set the activity back to //whatever it needs to be when going back.
	//
	//		super.onPause();
	//	}


	//	@Override
	//	public void setListView(Category category) {
	//		adapter = new MySplitAdapter((MainActivity) getActivity(), communElements, colors, R.layout.divided_screen_item);
	//		getListView().setAdapter(adapter);
	//
	//		
	//	}





}


