package com.euphor.paperpad.activities.fragments;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.euphor.paperpad.Beans.Parameters;
import com.euphor.paperpad.InterfaceRealm.CommunElements1;
import com.euphor.paperpad.R;
import com.euphor.paperpad.activities.main.MainActivity;
import com.euphor.paperpad.activities.main.MyApplication;
import com.euphor.paperpad.adapters.IndexHeaderAdapter;
import com.euphor.paperpad.Beans.Category;
import com.euphor.paperpad.Beans.Child_pages;

import com.euphor.paperpad.utils.Colors;
import com.euphor.paperpad.utils.jsonUtils.AppHit;
import com.euphor.paperpad.utils.StringMatcher;
import com.euphor.paperpad.widgets.IndexableListView;

import io.realm.Realm;

public class CategoryIndexFragment extends Fragment{

//	private Collection<Child_pages> elements;
	private ContentAdapter adapter;
	private IndexableListView list;
	

	private Colors colors;


//	private boolean isBottomSlider = false;
//	private List<CommunElements> communElements;
	private long time;
	private int id;
	

	private List<Child_pages> pages;

	private List<String> childPagesKeys;
	HashMap<String, Integer> postionPagesKeys;
	HashMap<String, List<Child_pages>> map;

	private String title;
	private ArrayList<Child_pages> listPage;
    public Realm realm;
	


	/* (non-Javadoc)
	 * @see android.support.v4.app.ListFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		/** to Modify ==> Make a new Layout of divided list screen instead of categories_list **/
		View view = inflater.inflate(R.layout.index_list_view, container, false); 
		view.setBackgroundColor(colors.getColor(colors.getBackground_color()));

		/** list to modify **/
		IndexableListView listView = (IndexableListView)view.findViewById(R.id.indexListView);
			
			TextView titleContactsTV = (TextView)view.findViewById(R.id.TitleTV);
			view.findViewById(R.id.titleTVContainer).setBackgroundColor(colors.getColor(colors.getForeground_color()));
			//view.findViewById(R.id.titleTVContainer).setVisibility(View.GONE);
			titleContactsTV.setTypeface(MainActivity.FONT_TITLE);
			titleContactsTV.setText(title);
			titleContactsTV.setTextColor(colors.getColor(colors.getBackground_color()));


		(listView).setDivider(new ColorDrawable(colors.getColor(colors.getForeground_color(), "80")));
		(listView).setDividerHeight(1);
		//		}
		

		
		list = listView;
		
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position,
					long arg3) {
				CommunElements1 element = (CommunElements1) view.getTag(); // because of the listView header we subtract 1
				{ 
					if (element instanceof Category) {
						Category category = (Category)element;
						//Log.e(" OpenCategorie  "," <<< === >>> ");

						((MainActivity)getActivity()).openCategory(category);

					}else if (element instanceof Child_pages) {

						Child_pages page = (Child_pages)element;
						//Log.e(" OpenChild_pages  "," <<< === >>> ");

						((MainActivity)getActivity()).openChildPage(page,false);

					}
				}
				
			}
		});
		
		return view;

	}



    private class ContentAdapter extends IndexHeaderAdapter implements SectionIndexer {
    	
    	private String mSections = "ABCDEFGHIJKLMNOPQRSTUVWXYZ#";
    	
//		public ContentAdapter(MainActivity activity, List<CommunElements> elements, Colors colors, int layout_item) {
//			super(activity, elements, colors, layout_item);
//		}
		
		public ContentAdapter(MainActivity activity, HashMap<String, List<Child_pages>> map, List<String> childPagesKeys, Colors colors, int layout_item) {

			super((MainActivity) getActivity(), map,childPagesKeys, colors, layout_item);
			mSections = "";
			char tmp = '#';
			for(int i = 0 ; i < pages.size(); i++) {
				if(pages.get(i).getTitle().charAt(0) != tmp) {
					tmp = pages.get(i).getTitle().charAt(0);
					mSections += tmp;
				}
				
			}
		}
		
//		@Override
//		public int getPositionForSection(int section) {
//			// If there is no item for current section, previous section will be selected
//			for (int i = section; i >= 0; i--) {
//				for (int j = 0; j < getCount(); j++) {
//					if (i == 0) {
//						// For numeric section
//						for (int k = 0; k <= 9; k++) {
//							if (StringMatcher.match(String.valueOf(getItem(j).getCommunTitle().charAt(0)), String.valueOf(k)))
//								return j;
//						}
//					} else {
//						if (StringMatcher.match(String.valueOf(getItem(j).getCommunTitle().charAt(0)), String.valueOf(mSections.charAt(i))))
//							return j;
//					}
//				}
//			}
//			return 0;
//		}

		@Override
		public int getPositionForSection(int section) {
			// If there is no item for current section, previous section will be selected
			for (int i = section; i >= 0; i--) {
				for (int j = 0; j < getCount(); j++) {
					if (i == 0) {
						// For numeric section
						for (int k = 0; k <= 9; k++) {
							if (StringMatcher.match(String.valueOf(getCommunElementsItem(j).getCommunTitle1().charAt(0)), String.valueOf(k)))
								return j;
						}
					} else {
						if (StringMatcher.match(String.valueOf(getCommunElementsItem(j).getCommunTitle1().charAt(0)), String.valueOf(mSections.charAt(i))))
							return j;
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

	

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
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

		((MainActivity)getActivity()).bodyFragment = "CategoryIndexFragment";
		((MainActivity)getActivity()).extras = new Bundle();
		int section_id = -1;
		if (getArguments().getInt("Section_id") != 0) {
			section_id = getArguments().getInt("Section_id");
			((MainActivity)getActivity()).extras.putInt("Section_id", section_id);
			id = section_id; 
		}else {
			int category_id = getArguments().getInt("Category_id");
			Category category = realm.where(Category.class).equalTo("id",category_id).findFirst();//appController.getCategoryById(category_id);
			if (category != null) {
				title = category.getTitle();
				pages = getChildCategories( category);
			}
			((MainActivity)getActivity()).extras.putInt("Category_id", category_id);
			id = category_id;
		}
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


//		communElements = new ArrayList<CommunElements>();
//		
//
//		quicksort(pages, 0, pages.size() - 1);
//
//
//		for (Iterator<Child_pages> iterator = pages
//				.iterator(); iterator.hasNext();) {
//			CommunElements communElement = (Child_pages) iterator.next();
//			if (((Child_pages)communElement).getVisible()) {
//				communElements.add(communElement);
//			}
//		}

		map = getMapPages(pages);
		
		adapter = new ContentAdapter((MainActivity) getActivity(), map,childPagesKeys, colors, R.layout.item_index_listview);
			//adapter = new ContentAdapter((MainActivity) getActivity(), communElements, colors, R.layout.categories_list_item);

		
		

	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		list.setIndexScrollerColor(colors.getColor(colors.getTitle_color()));
		list.setAdapter(adapter);
		list.setFastScrollEnabled(true);
		super.onViewCreated(view, savedInstanceState);
	}
	
	

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
		AppHit hit = new AppHit(System.currentTimeMillis(), time, "directory", id);
		((MyApplication)getActivity().getApplication()).hits.add(hit);
		super.onDestroy();
	}


	public List<Child_pages> getChildCategories(Category category) {
		List<Child_pages> result = new ArrayList<Child_pages>();
		if (category.getChildren_pages().size()>0) {
			for (Iterator<Child_pages> iterator = category.getChildren_pages().iterator(); iterator
					.hasNext();) {
				result.add((Child_pages) iterator
						.next());
			}
		}
		return result;
	}

	/**
	 * 
	 */
	public CategoryIndexFragment() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onStop() {
		AppHit hit = new AppHit(System.currentTimeMillis()/1000, time/1000, "sales_category", id);
		((MyApplication)getActivity().getApplication()).hits.add(hit);
		super.onStop();
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

	public HashMap<String, List<Child_pages>> getMapPages(List<Child_pages> pages){
		HashMap<String,  List<Child_pages>> map = new HashMap<String, List<Child_pages>>();


		String tmp = "A", comparTag = "", titleTag = "";
		int j,som = 0;

		Child_pages page = null;

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
			listPage = new ArrayList<Child_pages>();

			for(int i = j ; i<pages.size(); i++) {
				page = pages.get(i);

				titleTag = getAlphabeticPage(page.getTitle().substring(0, 1));

				if(!titleTag.isEmpty() && !isAlreadyChecked(list, titleTag)) {
					if(anotherDate) break;
					//					if(isLooperFinished) {
					list.add(titleTag);
					listPage.add(page);
					comparTag = titleTag;
					//					}
					anotherDate = true;
					tmp = titleTag;
					//isLooperFinished = false;
					j++;
				}else if(comparTag.compareTo(titleTag) == 0){

					listPage.add(page);
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

	
	public void fastTri(List<Child_pages> pages, int pivotIndex, int lastIndex){

		int j = 0;

		if(pivotIndex < lastIndex) 
		{
			j = partition(pages, pivotIndex, lastIndex);
			fastTri(pages, pivotIndex, j);
			fastTri(pages, j+1 , lastIndex);
		}	

	}	

	public static void quicksort(List<Child_pages> pages, int debut, int fin) {
		if (debut < fin) {
			int indicePivot = partition(pages, debut, fin);
			quicksort(pages, debut, indicePivot-1);
			quicksort(pages, indicePivot+1, fin);
		}
	}

	public static int partition(List<Child_pages> pages, int debut, int fin) {
		Child_pages pivotPages = pages.get(debut);
		String valeurPivot = formatStringNormalizer(pivotPages.getTitle()); //t[d�but];
		int d = debut+1;
		int f = fin;

		while (d < f) {

			while(d < f && formatStringNormalizer(pages.get(f).getTitle()).compareToIgnoreCase(valeurPivot) >= 0) f--;
			while(d < f && formatStringNormalizer(pages.get(d).getTitle()).compareToIgnoreCase(valeurPivot) <= 0) d++;

			Child_pages tmp = pages.get(d);
			pages.set(d, pages.get(f));
			pages.set(f, tmp);

		}

		if (formatStringNormalizer(pages.get(d).getTitle()).compareToIgnoreCase(valeurPivot) >= 0) d--;
		pages.set(debut, pages.get(d));
		pages.set(d, pivotPages);
		return d;
	}

	public static String formatStringNormalizer(String s) {
		String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
		return temp.replaceAll("[^\\p{ASCII}]", "").toLowerCase();
	}




//	@Override
//	public void onResume() {
//		setActivateOnItemClick(true);
//		//list.setItemChecked(1, true);
//		super.onResume();
//	}
}
