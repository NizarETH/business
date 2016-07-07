package com.euphor.paperpad.activities.fragments;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.euphor.paperpad.Beans.Parameters;
import com.euphor.paperpad.InterfaceRealm.CommunElements1;
import com.euphor.paperpad.R;
import com.euphor.paperpad.activities.main.MainActivity;
import com.euphor.paperpad.Beans.Category;
import com.euphor.paperpad.Beans.Child_pages;
import com.euphor.paperpad.Beans.Contact;

import com.euphor.paperpad.utils.Colors;

import android.app.Activity;
import android.graphics.PorterDuff.Mode;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.euphor.paperpad.widgets.ArrowImageView;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * @author euphordev02
 *
 */
public class SplitFilteredCategoriesFragment extends Fragment{

	
	private static int IS_HEADER_ADDED = 0;
	private Collection<Child_pages> elements;
	private Colors colors;

	private String titleInStrip = null;
	private List<Category> categories;
	protected int id_cat = 0;
	protected LinearLayout choiceHolder;
	protected Category cat;
	private MainActivity mainActivity;
	public static ListView list;
    public Realm realm;
	public static int mActivatedPosition = 1;
//	View view;
	
	int index;
	/**
	 * 
	 */
	public SplitFilteredCategoriesFragment() {
		// TODO Auto-generated constructor stub
	}

	

	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view;
		//this.inflater = inflater;
		view = inflater.inflate(R.layout.split_filtred_categories, container, false);
		view.setBackgroundColor(colors.getColor(colors.getBackground_color()));


//		SplitListCategoryFragment.list = list;

//		productInfo = (FrameLayout)view.findViewById(R.id.productInfo);
//		FrameLayout frameProductInfo = (FrameLayout)view.findViewById(R.id.frameProductInfo);
//		frameProductInfo.setBackgroundColor(colors.getColor(colors.getForeground_color()));
		view.findViewById(R.id.backChoices).setBackgroundColor(colors.getBackMixColor(colors.getForeground_color(), 0.10f));
		//view.findViewById(R.id.backChoices).setBackgroundColor(colors.getColor(colors.getTitle_color(), "10"));;
		try {
			if (categories != null && categories.size()>0) {
				
				
				choiceHolder = (LinearLayout)view.findViewById(R.id.choicesHolder);
				
				cat = categories.get(index);
                realm.beginTransaction();
				cat.setSelected(true);
                realm.commitTransaction();
				for (Iterator<Category> iterator = categories.iterator(); iterator.hasNext();) {
					Category element = (Category) iterator.next();
					fillNavigationBar(element);

				}
				if (cat!=null && getChildrenPages(cat).size()>0) {
					fillProductScroller(getChildrenPages(cat),getChildrenPages(cat).iterator().next());

				}
				
				SplitListCategoryFragment groupedCategoriesFragment = new SplitListCategoryFragment();
//			((MainActivity) getActivity()).bodyFragment = "splitListCategoryFragment";
				if(((MainActivity) getActivity()).extras == null)
					((MainActivity)getActivity()).extras = new Bundle();
//			((MainActivity) getActivity()).extras.putInt("Section_id",cat.getSection().getId_section());
				((MainActivity)getActivity()).extras.putInt("Category_id_", category_id);
				((MainActivity)getActivity()).extras.putBoolean("isSplitFiltered", true);
				((MainActivity) getActivity()).extras.putInt("Category_id",categories.get(index).getId());
				((MainActivity) getActivity()).extras.putString("split_list", cat.getDisplay_type());

				
				groupedCategoriesFragment
				.setArguments(((MainActivity) getActivity()).extras);
				// Add the fragment to the 'fragment_container' FrameLayout
				mainActivity.getSupportFragmentManager().beginTransaction().replace(R.id.split_fragment_container,groupedCategoriesFragment)
				.commit();
				
				if(list != null) list = null;
				titleInStrip = categories.get(index).getTitle();

				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ListView listView = (ListView)view.findViewById(R.id.listOfSplit);
		//Add Header view to show the category title!	
		if (titleInStrip!=null && !titleInStrip.isEmpty()) {
			View viewTitle = inflater.inflate(R.layout.title_strip, null, false);
			viewTitle.findViewById(R.id.TitleHolder).setBackgroundDrawable(colors.getForePD());
			TextView titleContactsTV = (TextView)viewTitle.findViewById(R.id.TitleTV);
			titleContactsTV.setText(titleInStrip);
			titleContactsTV.setTextSize(22);
			titleContactsTV.setTextColor(colors.getColor(colors.getBackground_color()));
			viewTitle.setClickable(false);
			(listView).addHeaderView(viewTitle, null, false);
			IS_HEADER_ADDED = 1;
		}else {
			IS_HEADER_ADDED = 0;
		}

		listView.setDivider(new ColorDrawable(colors.getColor(colors.getForeground_color(), "80")));
		listView.setDividerHeight(1);
		list = listView;

		
		return view;
		
	}
	

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		
		if(SplitListCategoryFragment.list != null) {
			SplitListCategoryFragment.list = list;
		}
		
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long arg3) {
				
				/** Uness Modif **/

				/** End **/

				int contact_id = 445;
				String url = null; // "http://backoffice.paperpad.fr/pdf/41/CONNECTION_WIFI.pdf";
				boolean isContact = false;
				
				CommunElements1 element = SplitListCategoryFragment.adapter.getItem(position-IS_HEADER_ADDED);  //getElements().get(position-IS_HEADER_ADDED); // because of the listView header we subtract 1

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

                    RealmResults<Contact> contact = realm.where(Contact.class).equalTo("id", contact_id).findAll();
                    //appController.getContactDao().queryForEq("id", contact_id);
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
//					Log.e(" SplitListCategoryFragment <=== passage normal "," : passage normal");

					if (element instanceof Category) {
						Category category = (Category)element;

						((MainActivity) getActivity()).openCategory(category);

					}else if (element instanceof Child_pages) {

						Child_pages page = (Child_pages)element;
						((MainActivity) getActivity()).openChildPage(page,false);

					}
				}
				
//				Toast.makeText(getActivity(), " List View Element "+position, 500).show();
			     
				if(mActivatedPosition != position){
					if(list.getChildAt(mActivatedPosition - list.getFirstVisiblePosition()) != null)
					{
						if(position >1 && position < 4) {
							list.getChildAt(mActivatedPosition - list.getFirstVisiblePosition()).setBackgroundColor(colors.getColor(colors.getBackground_color()));
							((TextView)list.getChildAt(mActivatedPosition - list.getFirstVisiblePosition()).findViewById(R.id.TVTitleCategory)).setTextColor(colors.getColor(colors.getTitle_color()));

						}
//						if(((TextView)list.getChildAt(mActivatedPosition - list.getFirstVisiblePosition()).findViewById(R.id.TVTitleCategory)) != null) {
						list.getChildAt(mActivatedPosition - list.getFirstVisiblePosition()).setBackgroundColor(colors.getColor(colors.getBackground_color()));

						((TextView)list.getChildAt(mActivatedPosition - list.getFirstVisiblePosition()).findViewById(R.id.TVTitleCategory)).setTextColor(colors.getColor(colors.getTitle_color()));
						((ArrowImageView)list.getChildAt(mActivatedPosition - list.getFirstVisiblePosition()).findViewById(R.id.imgArrow)).setBackgroundColor(colors.getColor(colors.getBackground_color()));

//						Paint paint = new Paint();
//						paint.setColor(colors.getColor(colors.getTitle_color()));
//						((ArrowImageView)list.getChildAt(position - list.getFirstVisiblePosition()).findViewById(R.id.imgArrow)).setPaint(paint);
//						Log.e("  SpltFilredCategories <<!normal>> Color Changed To :  ", ""+colors.getTitle_color());
//						}
					}
					
					mActivatedPosition = position;

					list.getChildAt(mActivatedPosition - list.getFirstVisiblePosition()).setBackgroundDrawable(colors.makeGradientToColor(colors.getForeground_color()));//Color(colors.getColor(colors.getTitle_color()));
					((TextView)list.getChildAt(mActivatedPosition - list.getFirstVisiblePosition()).findViewById(R.id.TVTitleCategory)).setTextColor(colors.getColor(colors.getBackground_color()));
					((ArrowImageView)list.getChildAt(mActivatedPosition - list.getFirstVisiblePosition()).findViewById(R.id.imgArrow)).setBackgroundColor(colors.getColor(colors.getForeground_color()));

				}
				
				
				
//				CategoryFragment.list.smoothScrollToPosition(mActivatedPosition);
				SplitListFragment.currentDetailPosition = mActivatedPosition;
				SplitListCategoryFragment.mActivatedPosition = mActivatedPosition;
				SplitListCategoryFragment.list = list;

				
			}
		});
		super.onActivityCreated(savedInstanceState);
	}
	int category_id;

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
		//appController = ((MyApplication)getActivity().getApplication()).getAppController();//new AppController(getActivity());
				realm = Realm.getInstance(getActivity());
		colors = ((MainActivity)activity).colors;
        Parameters ParamColor = realm.where(Parameters.class).findFirst();
        if (colors==null) {
            colors = new Colors(ParamColor);
        }
		int section_id = -1;
		if (getArguments().getInt("Section_id")!=0) {
			section_id = getArguments().getInt("Section_id");
		}else {
		}
		
		index = getArguments().getInt("index", 0);
		Log.e("Index is ", " : "+index);
		mainActivity = (MainActivity)activity;
		category_id = getArguments().getInt("Category_id_");
		((MainActivity)getActivity()).bodyFragment = "SplitFilteredCategoriesFragment";
		if(((MainActivity) getActivity()).extras == null)
			((MainActivity)getActivity()).extras = new Bundle();
			((MainActivity)getActivity()).extras.putInt("Category_id_", category_id);
			((MainActivity)getActivity()).extras.putBoolean("isSplitFiltered", true);
		super.onAttach(activity);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);

		int category_id = getArguments().getInt("Category_id_");
		Category category = realm.where(Category.class).equalTo("id",category_id).findFirst(); //appController.getCategoryById(category_id);
		if (category != null) {
			categories = getChildCategories( category);
		}
		
		
		int section_id = getArguments().getInt("Section_id");
		
		/*try {
			Section section = appController.getSectionsDao().queryForId(section_id);
			
			if (section!=null) {
				titleInStrip = section.getTitle();
				if (section.getCategories1().size() > 0) {
					categories = new ArrayList<Category>();
					for (Iterator<Category> iterator = section.getCategories1()
							.iterator(); iterator.hasNext();) {
						Category communElement = (Category) iterator.next();
						categories.add(communElement);
					}
				} 
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
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
	private void fillNavigationBar( Category category) {
		TextView categoryTxt = new TextView(getActivity());
		categoryTxt.setTypeface(MainActivity.FONT_BODY, Typeface.BOLD);
		if (category.isSelected() ) {
			LinearLayout.LayoutParams txtParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
			txtParams.gravity = Gravity.CENTER;
			categoryTxt.setGravity(Gravity.CENTER);
			categoryTxt.setText(category.getTitle().toUpperCase());
			categoryTxt.setTextColor(colors.getColor(colors.getBackground_color()));
			Drawable selectDrawable = getResources().getDrawable(R.drawable.back_choices_final);
			selectDrawable.setColorFilter(colors.getColor(colors.getTitle_color()), Mode.MULTIPLY);
			categoryTxt.setBackgroundDrawable(selectDrawable);
			categoryTxt.setTextSize(16);
			categoryTxt.setTag(category);
			choiceHolder.addView(categoryTxt, txtParams);
			
		}else {
			
			LinearLayout.LayoutParams txtParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
			txtParams.setMargins(10, 0, 10, 5);
			categoryTxt.setGravity(Gravity.CENTER);
			txtParams.gravity = Gravity.CENTER;
			categoryTxt.setText((category.getTitle()).toUpperCase());
			categoryTxt.setTextSize(16);
			categoryTxt.setTextColor(colors.getColor(colors.getTitle_color()));
//			Drawable selectDrawable = getResources().getDrawable(R.drawable.back_empty);
//			selectDrawable.setColorFilter(colors.getColor(colors.getForeground_color()), Mode.MULTIPLY);
//			categoryTxt.setBackgroundDrawable(selectDrawable);
			categoryTxt.setTag(category);
			choiceHolder.addView(categoryTxt, txtParams);
		}
		
		categoryTxt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				cat = (Category)v.getTag();
                realm.beginTransaction();
				for (int i = 0; i < categories.size(); i++) {
					categories.get(i).setSelected(false);
				}
				cat.setSelected(true);
				choiceHolder.removeAllViews();
				for (int i = 0; i < categories.size(); i++) {
					
					fillNavigationBar(categories.get(i));
					if(categories.get(i).isSelected()) {
						mActivatedPosition = 1;
						index = i;
						((MainActivity) getActivity()).extras.putInt("index", i);
					}
				}
//				hsvLayout.removeAllViews();
				if (cat!=null && getChildrenPages(cat).size()>0) {
					fillProductScroller(getChildrenPages(cat), getChildrenPages(cat).iterator().next());
					SplitFilteredCategoriesFragment.mActivatedPosition = 1;
//					SplitListFragment.currentDetailPosition = 1;
//					fillProductBox(getChildrenPages(cat).iterator().next());
				}


//				for(int i= 0; i< categories.size(); i++) {
//					if(categories.get(i).isSelected()) {
//						mActivatedPosition = 1;
//						index = i;
//						((MainActivity) getActivity()).extras.putInt("index", i);
//					}
//				}
				
			if(((MainActivity) getActivity()).extras == null)
				((MainActivity) getActivity()).extras = new Bundle();
                Category category1 =realm.where(Category.class).equalTo("id",cat.getId()).findFirst();
              if(cat.getParentCategory()!=null)
			((MainActivity)getActivity()).extras.putInt("Category_id_",cat.getParentCategory().getId() /*category1.getId()*//* à vérifier juste dépannage*/ /*cat.getParentCategory().getId()*/);
			((MainActivity)getActivity()).extras.putBoolean("isSplitFiltered", true);
			((MainActivity) getActivity()).extras.putInt("Category_id",categories.get(index).getId());
			((MainActivity) getActivity()).extras.putString("split_list", cat.getDisplay_type());

				
				SplitFilteredCategoriesFragment groupedCategoriesFragment = new SplitFilteredCategoriesFragment();
				((MainActivity) getActivity()).bodyFragment = "SplitFilteredCategoriesFragment";
									
				groupedCategoriesFragment
				.setArguments(((MainActivity) getActivity()).extras);
				// Add the fragment to the 'fragment_container' FrameLayout
				getFragmentManager().beginTransaction().replace(R.id.fragment_container, groupedCategoriesFragment)
				/*.addToBackStack(null)*/.commit();

				

realm.commitTransaction();
				
				
			}
		});
	}

	View selectedBefore;
//	private int indexCurrent;
	protected ArrayList<Child_pages> pages;
//	private Parameters parameters;
//	private Child_pages page;
	/**
	 * @param collection 
	 * 
	 */
	public void fillProductScroller(Collection<Child_pages> collection, Child_pages selected) {
//		hsvLayout.removeAllViews();
		//list = list;
		List<CommunElements1> elementsTmp = new ArrayList<CommunElements1>();
		elementsTmp.addAll(getChildrenPages(cat));


	}


	
	@Override
	public void onDestroyView() {
		if(SplitListCategoryFragment.list != null)
			SplitListCategoryFragment.list.removeAllViewsInLayout();
		((MainActivity)getActivity()).bodyFragment = "SplitFilteredCategoriesFragment";
		super.onDestroyView();
	}
	
}
 