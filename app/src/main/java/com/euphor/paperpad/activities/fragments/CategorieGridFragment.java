/**
 * 
 */
package com.euphor.paperpad.activities.fragments;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
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
import com.euphor.paperpad.adapters.MyGridAdapter;
import com.euphor.paperpad.Beans.Category;
import com.euphor.paperpad.Beans.Child_pages;
import com.euphor.paperpad.Beans.Section;

import com.euphor.paperpad.utils.Colors;
import com.euphor.paperpad.utils.Utils;
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
public class CategorieGridFragment extends GridFragment {



	private Colors colors;
//	private DisplayImageOptions options;
//	private List<Album> albums;
	private long time;
	private int id;
	private Collection<Child_pages> elements;
	private String titleInStrip = null;
	private List<CommunElements1> communElements;
	private MyGridAdapter adapter;
	private Section section;
	private Category category;
    public Realm realm;
	/**
	 * 
	 */
	public CategorieGridFragment() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.euphor.paperpad.widgets.GridFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.gridview_layout, container, false);
		view.setBackgroundColor(colors.getColor(colors.getBackground_color()));

		//int section_id = getArguments().getInt("Section_id");

			//Section section = appController.getSectionsDao().queryForId(section_id);
		TextView titleContactsTV = (TextView)view.findViewById(R.id.TitleTV);
		titleContactsTV.setTypeface(MainActivity.FONT_TITLE, Typeface.BOLD);
		titleContactsTV.setText(titleInStrip);
		DisplayMetrics metrics = new DisplayMetrics(); 
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics); 
		
			if( Utils.isTablet(getActivity())){
				if(metrics.densityDpi >= 213 ){
					if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
						titleContactsTV.setTextSize(titleContactsTV.getTextSize() - 6);
					} else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
						titleContactsTV.setTextSize(titleContactsTV.getTextSize());
					}
					titleContactsTV.setTextSize(titleContactsTV.getTextSize() - 4);
				}
				else
					titleContactsTV.setTextSize(titleContactsTV.getTextSize() + 2);
			}
			else{
				if(metrics.densityDpi <= 240 )
					titleContactsTV.setTextSize(titleContactsTV.getTextSize() - 16);
				else
					titleContactsTV.setTextSize(titleContactsTV.getTextSize() - 22);
			}

		//titleContactsTV.setTextSize(titleContactsTV.getTextSize() - 10);
		view.findViewById(R.id.TitleHolder).setBackgroundDrawable(colors.getForePD());
		titleContactsTV.setTextColor(colors.getColor(colors.getBackground_color()));
		
			if (section != null) {
				
				if (section.getTitle().isEmpty()) {
					view.findViewById(R.id.TitleHolder).setVisibility(View.GONE);
					//view.findViewById(R.id.TitleHolder).setBackgroundColor(colors.getColor(colors.getForeground_color()));
					
					titleContactsTV.setText(getActivity().getResources().getString(R.string.galeryPhotos));
					//titleContactsTV.setTextColor(colors.getColor(colors.getBackground_color()));
				}else {
					view.findViewById(R.id.TitleHolder).setBackgroundColor(colors.getColor(colors.getForeground_color()));
					titleContactsTV.setText(section.getTitle());
					//titleContactsTV.setTextColor(colors.getColor(colors.getBackground_color()));
				}
			}else if(category != null){
				//view.findViewById(R.id.TitleHolder).setBackgroundColor(colors.getColor(colors.getForeground_color()));
				titleContactsTV.setText(category.getTitle());
				//titleContactsTV.setTextColor(colors.getColor(colors.getBackground_color()));
				

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
	//new AppController(getActivity());
		realm = Realm.getInstance(getActivity());
		colors = ((MainActivity)activity).colors;
        Parameters ParamColor = realm.where(Parameters.class).findFirst();
        if (colors==null) {
            colors = new Colors(ParamColor);
        }

		time = System.currentTimeMillis();
		
		int section_id = -1;
		if (getArguments() != null && getArguments().getInt("Section_id")!=0) {
			section_id = getArguments().getInt("Section_id");
			if(((MainActivity)getActivity()).extras != null)
			((MainActivity)getActivity()).extras.putInt("Section_id", section_id);
			id = section_id;
		}else {
			int category_id = getArguments().getInt("Category_id");
			if(((MainActivity)getActivity()).extras != null)
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


        int section_id = getArguments().getInt("Section_id");
        section = realm.where(Section.class).equalTo("id_s",section_id).findFirst();//appController.getSectionsDao().queryForId(section_id);

        int category_id = getArguments().getInt("Category_id");
        category = realm.where(Category.class).equalTo("id",category_id).findFirst();//appController.getCategoryById(category_id);

        if (category != null) {
            titleInStrip = category.getTitle();
            fillListCategories(communElements, category);
            section = null;
        }else if (section != null) {
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

        //adapter = new CollectionCatsAdapter(communElements, getActivity(), colors, (MainActivity)getActivity());
		adapter = new MyGridAdapter(getActivity(), R.layout.grid_item, colors,  communElements);
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
		

		if (category.getChildren_categories().size()>1) {
			for (Iterator<Category> iterator = category.getChildren_categories().iterator(); iterator.hasNext();) {
				CommunElements1 communElement = (CommunElements1) iterator.next();
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
