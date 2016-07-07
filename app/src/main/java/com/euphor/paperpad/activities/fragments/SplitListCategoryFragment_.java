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
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.euphor.paperpad.Beans.Parameters;
import com.euphor.paperpad.InterfaceRealm.CommunElements1;
import com.euphor.paperpad.R;
import com.euphor.paperpad.activities.main.MainActivity;
import com.euphor.paperpad.activities.main.MyApplication;
import com.euphor.paperpad.adapters.CommunElements;
import com.euphor.paperpad.adapters.IndexHeaderSplitAdapter;
import com.euphor.paperpad.Beans.Category;
import com.euphor.paperpad.Beans.Child_pages;
import com.euphor.paperpad.Beans.Contact;

import com.euphor.paperpad.utils.Colors;
import com.euphor.paperpad.utils.StringMatcher;
import com.euphor.paperpad.utils.jsonUtils.AppHit;
import com.euphor.paperpad.widgets.ArrowImageView;
import com.euphor.paperpad.widgets.IndexableListView;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import io.realm.Realm;

/**
 * @author euphordev04
 *
 */

public class SplitListCategoryFragment_ extends Fragment{

	private static int IS_HEADER_ADDED = 0;
	private Collection<Child_pages> elements;
	//private CategoriesAdapter adapter;
	//public static MySplitAdapter adapter;
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
	//public static ListView list;
	//public static boolean isNewDesign = false;
	public static int mActivatedPosition = 1;



	private List<String> childPagesKeys;
	HashMap<String, Integer> postionPagesKeys;
	HashMap<String, List<CommunElements1>> map;

	public static ContentAdapter adapter;
	public static IndexableListView list;
	private ArrayList<CommunElements1> listPage;
	public static  boolean isIndexed = false;


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

		view = inflater.inflate(R.layout.split_index_list, container, false); 


		view.setBackgroundColor(colors.getColor(colors.getBackground_color()));

		//		this.inflater = inflater;

		//		if(list !=null) list = null;

		/** list to modify **/
		IndexableListView listView = (IndexableListView)view.findViewById(R.id.indexListView);
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
			(listView).addHeaderView(viewTitle, null, false);
			IS_HEADER_ADDED = 1;
		}else {
			IS_HEADER_ADDED = 0;
		}

		(listView).setDivider(new ColorDrawable(colors.getColor(colors.getForeground_color(), "80")));
		(listView).setDividerHeight(1);
		//		}


		list = listView;
		list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position,
					long arg3) {


				int contact_id = 445;
				String url = null; // "http://backoffice.paperpad.fr/pdf/41/CONNECTION_WIFI.pdf";
				boolean isContact = false;

				CommunElements1 element = (CommunElements1) view.getTag();  //getElements().get(position-IS_HEADER_ADDED); // because of the listView header we subtract 1

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

                    List<Contact> contact = realm.where(Contact.class).equalTo("id",contact_id).findAll();
                    //appController.getContactDao().queryForEq("id", contact_id);
                    if(contact.size()>0)
                        contact_id = contact.get(0).getId_con();
                    //			Log.e(" SplitListCategoryFragment <=== contact_id "," : "+contact_id);

					((MainActivity) getActivity()).extras = new Bundle();
					//			int contact_id = new ArrayList<Child_pages>(elements).get(position - IS_HEADER_ADDED).getRelated().getContact_form().getId();
					((MainActivity) getActivity()).extras.putInt("Contact",contact_id );
					((MainActivity) getActivity()).extras.putBoolean("newDesign",true);
					//((MainActivity) getActivity()).extras.putInt("Contact", 445);
					FormContactFragment formFragment = new FormContactFragment();
					formFragment.setArguments(((MainActivity) getActivity()).extras);
					getActivity().getSupportFragmentManager().beginTransaction()
					.replace(R.id.navitem_detail_container, formFragment).addToBackStack(null).commit();


				}
				else if(url != null){ 

					//			Log.e(" SplitListCategoryFragment <=== url "," : "+url);

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
					Log.e(" SplitListCategoryFragment <=== passage normal "," : passage normal");

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
						//				if(((TextView)list.getChildAt(mActivatedPosition - list.getFirstVisiblePosition()).findViewById(R.id.TVTitleCategory)) != null) {
						if(((TextView)list.getChildAt(mActivatedPosition - list.getFirstVisiblePosition()).findViewById(R.id.TVTitleCategory))!= null){
							list.getChildAt(mActivatedPosition - list.getFirstVisiblePosition()).setBackgroundColor(colors.getColor(colors.getBackground_color()));
							//if(((TextView)list.getChildAt(mActivatedPosition - list.getFirstVisiblePosition()).findViewById(R.id.TVTitleCategory)) != null){
							((TextView)list.getChildAt(mActivatedPosition - list.getFirstVisiblePosition()).findViewById(R.id.TVTitleCategory)).setTextColor(colors.getColor(colors.getTitle_color()));
							((ArrowImageView)list.getChildAt(mActivatedPosition - list.getFirstVisiblePosition()).findViewById(R.id.imgArrow)).setBackgroundColor(colors.getColor(colors.getBackground_color()));

							//							Paint paint = new Paint();
//							paint.setColor(colors.getColor(colors.getTitle_color()));
//							((ArrowImageView)list.getChildAt(position - list.getFirstVisiblePosition()).findViewById(R.id.imgArrow)).setPaint(paint);
							
						}
						//}
						//				Log.e(" SpltListCategory <<normal>> Color Changed To :  "+mActivatedPosition, ""+colors.getTitle_color());
						//				}
					}

					mActivatedPosition = position;


				}
				if(((TextView)list.getChildAt(mActivatedPosition - list.getFirstVisiblePosition()).findViewById(R.id.TVTitleCategory))!= null){

					list.getChildAt(mActivatedPosition - list.getFirstVisiblePosition()).setBackgroundColor(colors.getColor(colors.getForeground_color()));//Color(colors.getColor(colors.getTitle_color()));
					//if(((TextView)list.getChildAt(mActivatedPosition - list.getFirstVisiblePosition()).findViewById(R.id.TVTitleCategory)) != null)
					((TextView)list.getChildAt(mActivatedPosition - list.getFirstVisiblePosition()).findViewById(R.id.TVTitleCategory)).setTextColor(colors.getColor(colors.getBackground_color()));
					((ArrowImageView)list.getChildAt(mActivatedPosition - list.getFirstVisiblePosition()).findViewById(R.id.imgArrow)).setBackgroundColor(colors.getColor(colors.getForeground_color()));

				}

				//		if(list.getChildAt(mActivatedPosition - list.getFirstVisiblePosition()) != null){
				//			list.getChildAt(mActivatedPosition - list.getFirstVisiblePosition()).setBackgroundDrawable(colors.makeGradientToColor(colors.getTitle_color()));//Color(colors.getColor(colors.getTitle_color()));
				//			((TextView)list.getChildAt(mActivatedPosition - list.getFirstVisiblePosition()).findViewById(R.id.TVTitleCategory)).setTextColor(colors.getColor(colors.getBackground_color()));
				//			Log.e(" SpltListCategory <<pressed>> Color Changed To :  ", ""+colors.getBackground_color());

				//		}


				//		CategoryFragment.list.smoothScrollToPosition(mActivatedPosition);
				SplitListFragment.currentDetailPosition = mActivatedPosition;
				//SplitFilteredCategoriesFragment.mActivatedPosition = mActivatedPosition;


			}
		});

		//setListAdapter(adapter);

		//		frameLayout = new FrameLayout(getActivity());
		//		frameLayout.addView(view);
		return view;

	}



	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		if(list == null){
			list = (IndexableListView)getActivity().findViewById(R.id.indexListView);

			list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			map = getMapPages(communElements);
			adapter = new ContentAdapter((MainActivity) getActivity(), map,childPagesKeys, colors, R.layout.item_index_listview);
			list.setAdapter(adapter);
			
		}
		isIndexed = true;
		

		
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

        Parameters ParamColor = realm.where(Parameters.class).findFirst();
        if (colors==null) {
            colors = new Colors(ParamColor);
        }


//		if(((MainActivity) getActivity()).extras != null && ((MainActivity)getActivity()).extras.getBoolean("isSplitFiltered", false))
//			((MainActivity)getActivity()).bodyFragment = "SplitFilteredCategoriesFragment";
//		else
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

			if (getArguments().containsKey("Category_id")) {	
				int category_id = getArguments().getInt("Category_id");
				((MainActivity)getActivity()).extras.putInt("Category_id", category_id);
				id = category_id;
			}
			
		}
		
		((MainActivity) getActivity()).extras.putBoolean("isSorted", true);
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

		communElements = new ArrayList<CommunElements1>();

		int category_id = getArguments().getInt("Category_id");
		//Category 
		category =realm.where(Category.class).equalTo("id",category_id).findFirst();
		// appController.getCategoryById(category_id);
		if (category != null) {
			fillListCategories(communElements, category);
		}


//				int section_id = getArguments().getInt("Section_id");
//		
//				try {
//					Section section = appController.getSectionsDao().queryForId(section_id);
//		
//					if (section!=null) {
//						titleInStrip = section.getTitle();
//						if (section.getCategories1().size() > 1) {
//							
//							for (Iterator<Category> iterator = section.getCategories1()
//									.iterator(); iterator.hasNext();) {
//								CommunElements communElement = (CommunElements) iterator
//										.next();
//								communElements.add(communElement);
//							}
//							
//						} else if (section.getCategories1().size() == 1) {
//		
//							Category childCategory = section.getCategories1().iterator().next();
//							fillListCategories(communElements, childCategory);
//		
//						}
//					}
//		
//		
//		
//		
//				} catch (SQLException e) {
//					e.printStackTrace();
//				} 


	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		//adapter = new MySplitAdapter((MainActivity) getActivity(), communElements, colors, R.layout.divided_screen_item, 18);
		if(list != null){

			list.setIndexScrollerColor(colors.getColor(colors.getTitle_color()));
			list.setFastScrollEnabled(true);
			//list.setAdapter(adapter);
			map = getMapPages(communElements);
			adapter = new ContentAdapter((MainActivity) getActivity(), map,childPagesKeys, colors, R.layout.item_index_listview);
			list.setAdapter(adapter);

		}
		//getListView().getChildAt(1).setBackgroundColor(colors.getColor(colors.getForeground_color()));

		//mActivatedPosition += IS_HEADER_ADDED;
		if(mActivatedPosition != 1)mActivatedPosition = 1;
		SplitListFragment.currentDetailPosition = mActivatedPosition;// - IS_HEADER_ADDED;
		CommunElements1 element = null;
		if(adapter.getCount() > 0)
			element = (CommunElements1) adapter.getItem(mActivatedPosition); //.getElements().get(0); // because of the listView header we subtract 1
		
		if (element instanceof Category) {
			
			category = (Category)element;
			((MainActivity) getActivity()).openCategory(category);

		}else if (element instanceof Child_pages) {

			Child_pages page = (Child_pages)element;
			((MainActivity) getActivity()).openChildPage(page,false);

		}
		
		mActivatedPosition += IS_HEADER_ADDED;	
		
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
	public SplitListCategoryFragment_() {
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
					//adapter = new ContentAdapter((MainActivity) getActivity(), map,childPagesKeys, colors, R.layout.item_index_listview);
					//adapter = new MySplitAdapter((MainActivity) getActivity(), communElements, colors, R.layout.categories_list_item, 18);
					//list.setAdapter(adapter);



					//				}

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

	class ContentAdapter extends IndexHeaderSplitAdapter implements SectionIndexer {

		private String mSections = "ABCDEFGHIJKLMNOPQRSTUVWXYZ#";

		//			public ContentAdapter(MainActivity activity, List<CommunElements> elements, Colors colors, int layout_item) {
		//				super(activity, elements, colors, layout_item);
		//			}

		public ContentAdapter(MainActivity activity, HashMap<String, List<CommunElements1>> map, List<String> childPagesKeys, Colors colors, int layout_item) {

			super((MainActivity) getActivity(), map,childPagesKeys, colors, layout_item);
			mSections = "";
			char tmp = '#';
			for(int i = 0 ; i < communElements.size(); i++) {
				if(communElements.get(i).getCommunTitle1().charAt(0) != tmp) {
					tmp = communElements.get(i).getCommunTitle1().charAt(0);
					mSections += tmp;
				}

			}
		}

		//			@Override
		//			public int getPositionForSection(int section) {
		//				// If there is no item for current section, previous section will be selected
		//				for (int i = section; i >= 0; i--) {
		//					for (int j = 0; j < getCount(); j++) {
		//						if (i == 0) {
		//							// For numeric section
		//							for (int k = 0; k <= 9; k++) {
		//								if (StringMatcher.match(String.valueOf(getItem(j).getCommunTitle().charAt(0)), String.valueOf(k)))
		//									return j;
		//							}
		//						} else {
		//							if (StringMatcher.match(String.valueOf(getItem(j).getCommunTitle().charAt(0)), String.valueOf(mSections.charAt(i))))
		//								return j;
		//						}
		//					}
		//				}
		//				return 0;
		//			}

		@Override
		public int getPositionForSection(int section) {
			// If there is no item for current section, previous section will be selected
			for (int i = section; i >= 0; i--) {
				for (int j = 0; j < getCount(); j++) {
					if (i == 0) {
						// For numeric section
						for (int k = 0; k <= 9; k++) {
							if (StringMatcher.match(String.valueOf(getCommunElementsItem(j).getCommunTitle1().charAt(0)), String.valueOf(k)))
								return j + IS_HEADER_ADDED;
						}
					} else {
						if (StringMatcher.match(String.valueOf(getCommunElementsItem(j).getCommunTitle1().charAt(0)), String.valueOf(mSections.charAt(i))))
							return j + IS_HEADER_ADDED;
					}
				}
			}
			return 0;
		}

		@Override
		public int getSectionForPosition(int position) {
			return 0;
		}

		@Override
		public Object[] getSections() {
			String[] sections = new String[mSections.length()];
			for (int i = 0; i < mSections.length(); i++)
				sections[i] = String.valueOf(mSections.charAt(i));
			return sections;
		}
	}


	public String getAlphabeticPage(String s) {

		String[] alphab = new String[] {
				"A", "B", "C", "D", "E", "F", "G" ,"H", "I", "J", "K", "L", "M", "N" , "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"
		};

		for(int i = 0; i < alphab.length; i++) {
			if(alphab[i].contains(s))return alphab[i];
		}

		return s;
	}

	public HashMap<String, List<CommunElements1>> getMapPages(List<CommunElements1> pages){
		HashMap<String,  List<CommunElements1>> map = new HashMap<String, List<CommunElements1>>();


		String tmp = "A", comparTag = "", titleTag = "";
		int j,som = 0;

		CommunElements1 element = null;

		j = pages.size() - 1;

		if(j > 0) {
			//fastTri(events, j - 1, j);
			quicksort(pages, 0, j);
		}



		List<String> list = new ArrayList<String>();


		childPagesKeys= new ArrayList<String>();


		boolean end = false;
		boolean anotherDate = false;

		j = 0;
		while(!end) {
			listPage = new ArrayList<CommunElements1>();

			for(int i = j ; i<pages.size(); i++) {
				element = pages.get(i);

				titleTag = getAlphabeticPage(element.getCommunTitle1().substring(0, 1));

				if(titleTag != null && !titleTag.isEmpty() && !isAlreadyChecked(list, titleTag)) {
					if(anotherDate) break;
					//					if(isLooperFinished) {
					list.add(titleTag);
					listPage.add(element);
					comparTag = titleTag;
					//					}
					anotherDate = true;
					tmp = titleTag;
					//isLooperFinished = false;
					j++;
				}else if(comparTag.compareTo(titleTag) == 0){

					listPage.add(element);
					j++;
				}




			}
			anotherDate = false;



			if(!map.keySet().contains(tmp)) {
				map.put(tmp, listPage);
				childPagesKeys.add(tmp);
			}


			som += listPage.size();

			if(som >= pages.size()) {
				end = true;
			}

		}

		return map;

	}

	public boolean isAlreadyChecked(List<String> list, String value) {
		if(list.size() == 0)return false;

		for(int i = 0; i< list.size(); i++) {
			if(list.get(i).compareTo(value) == 0)
				return true;
		}

		return false;
	}


	public void fastTri(List<CommunElements1> elements, int pivotIndex, int lastIndex){

		int j = 0;

		if(pivotIndex < lastIndex) 
		{
			j = partition(elements, pivotIndex, lastIndex);
			fastTri(elements, pivotIndex, j);
			fastTri(elements, j+1 , lastIndex);
		}	

	}	

	public static void quicksort(List<CommunElements1> elements, int debut, int fin) {
		if (debut < fin) {
			int indicePivot = partition(elements, debut, fin);
			quicksort(elements, debut, indicePivot-1);
			quicksort(elements, indicePivot+1, fin);
		}
	}

	public static int partition(List<CommunElements1> elements, int debut, int fin) {
		CommunElements1 pivotPages = elements.get(debut);
		String valeurPivot = formatStringNormalizer(pivotPages.getCommunTitle1()); //t[d�but];
		int d = debut+1;
		int f = fin;

		while (d < f) {

			while(d < f && formatStringNormalizer(elements.get(f).getCommunTitle1()).compareToIgnoreCase(valeurPivot) >= 0) f--;
			while(d < f && formatStringNormalizer(elements.get(d).getCommunTitle1()).compareToIgnoreCase(valeurPivot) <= 0) d++;

			CommunElements1 tmp = elements.get(d);
			elements.set(d, elements.get(f));
			elements.set(f, tmp);

		}

		if (formatStringNormalizer(elements.get(d).getCommunTitle1()).compareToIgnoreCase(valeurPivot) >= 0) d--;
		elements.set(debut, elements.get(d));
		elements.set(d, pivotPages);
		return d;
	}

	public static String formatStringNormalizer(String s) {
		String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
		return temp.replaceAll("[^\\p{ASCII}]", "").toLowerCase();
	}



	@Override
	public void onDestroyView() {
		isIndexed = false;
		super.onDestroyView();
	}

}


