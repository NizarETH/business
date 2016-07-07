/**
 * 
 */
package com.euphor.paperpad.activities.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.euphor.paperpad.Beans.Parameters;
import com.euphor.paperpad.InterfaceRealm.CommunElements1;
import com.euphor.paperpad.R;
import com.euphor.paperpad.activities.main.MainActivity;
import com.euphor.paperpad.activities.main.MyApplication;
import com.euphor.paperpad.adapters.CollectionCatsAdapter;
import com.euphor.paperpad.Beans.Album;
import com.euphor.paperpad.Beans.Category;
import com.euphor.paperpad.Beans.Child_pages;

import com.euphor.paperpad.utils.Colors;
import com.euphor.paperpad.utils.jsonUtils.AppHit;
import com.euphor.paperpad.widgets.GridFragment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import io.realm.Realm;

/**
 * @author euphordev02
 *
 */
public class CollectionGridFragment extends GridFragment {


	private Colors colors;
	private List<Album> albums;
	private long time;
	private int id;
	private Collection<Child_pages> elements;
	private String titleInStrip = null;
	private List<CommunElements1> communElements;
	private CollectionCatsAdapter adapter;
    public Realm realm;
	/**
	 * 
	 */
	public CollectionGridFragment() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.euphor.paperpad.widgets.GridFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.collection_grid_fragment, container, false);
		view.setBackgroundColor(colors.getColor(colors.getBackground_color()));
		

			TextView titleContactsTV = (TextView)view.findViewById(R.id.TitleTV);
			titleContactsTV.setTypeface(MainActivity.FONT_TITLE);
			view.findViewById(R.id.TitleHolder).setBackgroundColor(colors.getColor(colors.getTitle_color()));
			titleContactsTV.setTextColor(colors.getColor(colors.getBackground_color()));
			
			
//			Section section = appController.getSectionsDao().queryForId(section_id);
//			if (section != null) {
//
//				if (section.getTitle().isEmpty()) {
//					//view.findViewById(R.id.TitleHolder).setVisibility(View.GONE);
//					titleContactsTV.setText(titleInStrip);
//				}else {
//					titleContactsTV.setText(section.getTitle());
//				}
//				
//			}
			Category category = realm.where(Category.class).equalTo("id",id).findFirst();//appController.getCategoryById(id);
			if (category != null) {
				if(category.getTitle().isEmpty())
					titleContactsTV.setText(titleInStrip);
				else
					titleContactsTV.setText(category.getTitle());
			}
	
		
		return view;
	}

	/* (non-Javadoc)
	 * @see com.euphor.paperpad.widgets.GridFragment#setGridAdapter(android.widget.ListAdapter)
	 */
	@Override
	public void setGridAdapter(ListAdapter adapter) {
		// TODO Auto-generated method stub
		super.setGridAdapter(adapter);
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
		
		time = System.currentTimeMillis();
		
		int section_id = -1;
		if (getArguments().getInt("Section_id")!=0) {
			section_id = getArguments().getInt("Section_id");
			((MainActivity)getActivity()).extras.putInt("Section_id", section_id);
			id = section_id;
		}if(getArguments().getInt("Category_id")!=0) {
			int category_id = getArguments().getInt("Category_id");
			((MainActivity)getActivity()).extras.putInt("Category_id", category_id);
			id = category_id;
		}
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
		Category category = realm.where(Category.class).equalTo("id",category_id).findFirst(); //appController.getCategoryById(category_id);
		if (category != null) {
			titleInStrip = category.getTitle();
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

		adapter = new CollectionCatsAdapter(communElements, getActivity(), colors, (MainActivity)getActivity());
		setGridAdapter(adapter);
		
	}

	/* (non-Javadoc)
	 * @see com.euphor.paperpad.widgets.GridFragment#onGridItemClick(android.widget.GridView, android.view.View, int, long)
	 */
	@Override
	public void onGridItemClick(GridView g, View v, int position, long id) {

		CommunElements1 element = adapter.getItem(position); // because of the listView header we subtract 1
		if (element instanceof Category) {
			Category category = (Category)element;
			
			((MainActivity) getActivity()).openCategory(category);

		}else if (element instanceof Child_pages) {
			
			Child_pages page = (Child_pages)element;
			((MainActivity) getActivity()).openChildPage(page,false);
			

		}
	
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
		Runtime.getRuntime().gc();
		super.onDestroy();
	}
	
	
	public List<CommunElements1> fillListCategories(List<CommunElements1> communElements, Category category) {
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
//				adapter = new CategoriesAdapter((MainActivity) getActivity(), communElements, colors, R.layout.categories_list_item);
//				setListAdapter(adapter);
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
		
		return communElements;
		
	}
	
	@Override
	public void onStop() {
		AppHit hit = new AppHit(System.currentTimeMillis()/1000, time/1000, "sales_album", id);
		((MyApplication)getActivity().getApplication()).hits.add(hit);
		super.onStop();
	}

}
