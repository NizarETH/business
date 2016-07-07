package com.euphor.paperpad.activities.fragments;

import android.R.style;
import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.euphor.paperpad.Beans.Parameters;
import com.euphor.paperpad.R;
import com.euphor.paperpad.activities.main.MainActivity;
import com.euphor.paperpad.Beans.Category;
import com.euphor.paperpad.Beans.Child_pages;
import com.euphor.paperpad.Beans.ElementSwipe;
import com.euphor.paperpad.Beans.Illustration;
import com.euphor.paperpad.Beans.Parameters_swipe;
import com.euphor.paperpad.Beans.Section;

import com.euphor.paperpad.utils.Colors;
import com.euphor.paperpad.utils.Utils;
import com.euphor.paperpad.widgets.ArrowImageView;
import com.euphor.paperpad.widgets.AutoResizeTextView;


import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.realm.Realm;

public class SwipperFragment extends Fragment {

	private View view;
	private MyPageAdapter pageAdapter;
	private ViewPager mViewPager;

	private Colors colors;

	private boolean isTablet;
	private List<ElementSwipe> elementSwipes;
	private Parameters_swipe parameters_swipe;
    public Realm realm;

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig); 

		//	   if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
		//	        Toast.makeText(this.getActivity(), "landscape", Toast.LENGTH_SHORT).show();
		//	    } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
		//	        Toast.makeText(this.getActivity(), "portrait", Toast.LENGTH_SHORT).show();
		//	    }

		//Your fragment animation layout changing code

	}


	@Override
	public void onAttach(Activity activity) {
		
	//new AppController(getActivity());
				realm = Realm.getInstance(getActivity());
		colors = ((MainActivity)activity).colors;
        Parameters ParamColor =realm.where(Parameters.class).findFirst();
        if (colors==null) {
            colors = new Colors(ParamColor);
        }

		((MainActivity)getActivity()).bodyFragment = "SwipperFragment";
		if(((MainActivity)getActivity()).extras == null)
			((MainActivity)getActivity()).extras = new Bundle();
		int section_id = -1;
		if (getArguments().getInt("Section_id")!=0) {
			section_id = getArguments().getInt("Section_id");
			((MainActivity)getActivity()).extras.putInt("Section_id", section_id);
			//id = section_id; 
		}
		else {	int category_id = getArguments().getInt("Category_id");
		((MainActivity)getActivity()).extras.putInt("Category_id", category_id);
		//id = category_id;
		}
        isTablet = Utils.isTablet(activity);
		//time = System.currentTimeMillis();

		super.onAttach(activity);
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		//Log.e(" onCreate est l� ", "  Elle est l� !");
		setRetainInstance(true);

		elementSwipes = new ArrayList<ElementSwipe>();
        elementSwipes = realm.where(ElementSwipe.class).findAll();//appController.getElemantsDao().queryForAll();
        //Parameters_swipe parameters_swipe = null;
        parameters_swipe = realm.where(Parameters_swipe.class).findFirst();//appController.getParameters_swipeDao().queryForId(1);
        List<Fragment> fragments2 = getFragments(elementSwipes);
		//		Collections.reverse(fragments2);
		pageAdapter = new MyPageAdapter(getChildFragmentManager()/*getChildFragmentManager()*/, fragments2);


		super.onCreate(savedInstanceState);
	}
	//	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		//buildSwipe();;
		//		List<Fragment> fragments2 = getFragments(elementSwipes);
		//		//		Collections.reverse(fragments2);
		//		pageAdapter = new MyPageAdapter(getFragmentManager()/*getChildFragmentManager()*/, fragments2);
		//						frameLayout.removeAllViews();
		//						frameLayout.addView(mViewPager,0);
		try {
			mViewPager.setAdapter(pageAdapter);

			mViewPager.setCurrentItem(0);

			if (elementSwipes.size()>1) {
				pageSwitcher(5, elementSwipes.size());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view =new View(getActivity());
		try {
			if (isTablet) {
				if (parameters_swipe.getLogo_position().equalsIgnoreCase("center_top")) {
					view = inflater.inflate(R.layout.swipper_center_top_layout, container, false);
				}else {
					view = inflater.inflate(R.layout.swipper_layout, container, false);
				}
			}else {
				view = inflater.inflate(R.layout.swipper_center_top_layout_smart, container, false);

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		buildSwipe();
		return view;
	}



	public void buildSwipe() {
		mViewPager = (ViewPager)view.findViewById(R.id.pager);

		if (parameters_swipe != null) {

			Illustration illustration = parameters_swipe.getIllustration();
			ImageView logo_swipe = (ImageView)view.findViewById(R.id.logo_swipe);
			if (illustration != null) {
				try {
					Glide.with(getActivity()).load(new File(illustration.getPath())).into(logo_swipe);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//				imageLoader.displayImage(path, logo_swipe);
			}else {
				try {
					Glide.with(getActivity()).load(parameters_swipe.getLogo()).into(logo_swipe);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//				imageLoader.displayImage(parameters_swipe.getLogo(), logo_swipe);
			}

			TextView title_swipe = (AutoResizeTextView)view.findViewById(R.id.swipe_title);
			/*ColorStateList colorStateList = new ColorStateList(
					new int[][] {new int[] { android.R.attr.state_pressed }, new int[] {} },
					new int[] {Color.GRAY, colors.getColor(parameters_swipe.getColor()) });*/
//			title_swipe.setTextColor(colorStateList);
			
			//title_swipe.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);

			title_swipe.setTextAppearance(getActivity(), style.TextAppearance_Large);
			title_swipe.setTypeface(MainActivity.FONT_TITLE, Typeface.BOLD);
            if(!parameters_swipe.getColor().isEmpty())
			title_swipe.setTextColor(colors.getColor(parameters_swipe.getColor()));
            else
                title_swipe.setTextColor(colors.getColor("FFFFFF"));
			title_swipe.setText(parameters_swipe.getButton_title());
			//			title_swipe.setTextSize(42); /** Uness Modif **/

			final ArrowImageView swipeAImg = (ArrowImageView)view.findViewById(R.id.title_arrow);
			Paint paint = new Paint();
//			paint.setColor(Color.WHITE);
            if(!parameters_swipe.getColor().isEmpty())
			paint.setColor(colors.getColor(parameters_swipe.getColor()));
            else
            paint.setColor(colors.getColor("FFFFFF"));
			swipeAImg.setLayoutParams(new LinearLayout.LayoutParams((int)title_swipe.getTextSize() - 6, (int)title_swipe.getTextSize()  - 6));
			swipeAImg.setPaint(paint);

			//title_swipe.setTextSize(38);
			//			RelativeLayout swipeBtn = (RelativeLayout) findViewById(R.id.swipe_btn);
			//			ImageView swipe_logo_holder = (ImageView)findViewById(R.id.logo_swipe);
			LinearLayout sub_swipe_title_holder = (LinearLayout)view.findViewById(R.id.sub_swipe_title_holder);
			//sub_swipe_title_holder.setPadding(0, 40, 0, 0);
			if (parameters_swipe.getSection_id()!= 0) {
				final int section_id = parameters_swipe.getSection_id();

				OnClickListener swipeClick = new OnClickListener() {

					@Override
					public void onClick(View v) {
						//						swipeAImg.getPaint().setColor(colors.getColor(colors.getTabs_background_color()));

						List<Section> sections = new ArrayList<Section>();
                        sections = realm.where(Section.class).equalTo("id", section_id).findAll();
                        //appController.getSectionsDao().queryForEq("id", section_id);
                        if (sections.size()>0) {
							Collection<Category> categories = sections.get(0).getCategories();
							if (categories!=null && categories.size()>0) {
								Category cat = categories.iterator().next();
								if (cat.getDisplay_type()!=null && cat.getDisplay_type().equals("bottom_slider") && isTablet) {

									SliderCategoryFragment fragment = new SliderCategoryFragment();
									((MainActivity)getActivity()).bodyFragment = "SliderCategoryFragment";
									// In case this activity was started with special instructions from an Intent,
									// pass the Intent's extras to the fragment as arguments
									((MainActivity)getActivity()).extras = new Bundle();
									((MainActivity)getActivity()).extras.putInt("Section_id", sections.get(0).getId_s());
									fragment.setArguments(((MainActivity)getActivity()).extras);
									// Add the fragment to the 'fragment_container' FrameLayout
									getFragmentManager()/*getChildFragmentManager()*/.beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();

								}else if (sections.get(0).getDisplay_type()!=null && sections.get(0).getDisplay_type().equals("fullscreen")) {

									FullscreenCategoryFragment categoryFragment = new FullscreenCategoryFragment();
									((MainActivity)getActivity()).bodyFragment = "FullscreenCategoryFragment";
									((MainActivity)getActivity()).extras = new Bundle();
									((MainActivity)getActivity()).extras.putInt("Section_id", sections.get(0).getId_s());
									categoryFragment.setArguments(((MainActivity)getActivity()).extras);
									getFragmentManager()/*getChildFragmentManager()*/.beginTransaction().replace(R.id.fragment_container, categoryFragment).addToBackStack(null).commit();

								}else if (sections.get(0).getDisplay_type()!=null && sections.get(0).getDisplay_type().equals("collection")) {

									CollectionGridFragment collectionGridFragment = new CollectionGridFragment();
									((MainActivity)getActivity()).bodyFragment = "CollectionGridFragment";
									((MainActivity)getActivity()).extras = new Bundle();
									((MainActivity)getActivity()).extras.putInt("Section_id", sections.get(0).getId_s());
									collectionGridFragment.setArguments(((MainActivity)getActivity()).extras);
									getFragmentManager()/*getChildFragmentManager()*/.beginTransaction().replace(R.id.fragment_container, collectionGridFragment).addToBackStack(null).commit();

								}else if (isTablet && sections.get(0).getDisplay_type()!=null && sections.get(0).getDisplay_type().equals("grid")) {

									CategorieGridFragment coategorieGridFragment = new CategorieGridFragment();
									((MainActivity)getActivity()).bodyFragment = "CategorieGridFragment";
									((MainActivity)getActivity()).extras = new Bundle();
									((MainActivity)getActivity()).extras.putInt("Section_id", sections.get(0).getId_s());
									coategorieGridFragment.setArguments(((MainActivity)getActivity()).extras);
									getFragmentManager().beginTransaction().replace(R.id.fragment_container, coategorieGridFragment).addToBackStack(null).commit();

								}
								else {

									CategoryFragment categoryFragment = new CategoryFragment();
									((MainActivity)getActivity()).bodyFragment = "CategoryFragment";
									// In case this activity was started with special instructions from an Intent,
									// pass the Intent's extras to the fragment as arguments
									((MainActivity)getActivity()).extras = new Bundle();
									((MainActivity)getActivity()).extras.putInt("Section_id", sections.get(0).getId_s());
									categoryFragment.setArguments(((MainActivity)getActivity()).extras);
									// Add the fragment to the 'fragment_container' FrameLayout
									getFragmentManager()
									/*getChildFragmentManager()*/.beginTransaction().replace(R.id.fragment_container, categoryFragment).addToBackStack(null).commit();
								}

							}else {

								CategoryFragment categoryFragment = new CategoryFragment();
								((MainActivity)getActivity()).bodyFragment = "CategoryFragment";
								// In case this activity was started with special instructions from an Intent,
								// pass the Intent's extras to the fragment as arguments
								((MainActivity)getActivity()).extras = new Bundle();
								((MainActivity)getActivity()).extras.putInt("Section_id", sections.get(0).getId_s());
								categoryFragment.setArguments(((MainActivity)getActivity()).extras);
								// Add the fragment to the 'fragment_container' FrameLayout
								getFragmentManager()/*getChildFragmentManager()*/.beginTransaction().replace(R.id.fragment_container, categoryFragment).addToBackStack(null).commit();
							}
						}


						if (timer != null) {
							Runtime.getRuntime().gc();
							timer.cancel();
						}

						//						view.findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
						//						view.findViewById(R.id.swipe_container).setVisibility(View.GONE);

					}
				};
				logo_swipe.setOnClickListener(swipeClick );
				//sub_swipe_title_holder.setLayoutParams(new LinearLayout.LayoutParams(300, 60));
				sub_swipe_title_holder.setOnClickListener(swipeClick );
				//				swipeBtn.setOnClickListener(swipeClick );
			}else if (parameters_swipe.getCategory_id()!= 0) {
				final int category_id = parameters_swipe.getCategory_id();
				OnClickListener swipeClick = new OnClickListener() {

					@Override
					public void onClick(View v) {
						//						swipeAImg.getPaint().setColor(colors.getColor(colors.getTabs_background_color()));
						/*CategoryFragment categoryFragment = new CategoryFragment();
						bodyFragment = "CategoryFragment";
						// In case this activity was started with special
						// instructions from an Intent,
						// pass the Intent's extras to the fragment as arguments
						if (category_id!= 0) {
							extras.putInt("Category_id", category_id);
							categoryFragment.setArguments(extras);
							// Add the fragment to the 'fragment_container' FrameLayout
							MainActivity.this
							.getSupportFragmentManager()
							.beginTransaction()
							.replace(R.id.fragment_container, categoryFragment)
							.addToBackStack(null).commit();
						}*/
						List<Category> categories = new ArrayList<Category>();
                        categories =  realm.where(Category.class).equalTo("id", category_id).findAll();
                        //appController.getCategoryDao().queryForEq("id", category_id);
                        if (categories.size()>0) {
							((MainActivity)getActivity()).openCategory(categories.get(0));
							if (timer != null) {timer.cancel();}
							//							view.findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
							//							view.findViewById(R.id.swipe_container).setVisibility(View.GONE);
						}



					}
				};
				logo_swipe.setOnClickListener(swipeClick );
				sub_swipe_title_holder.setOnClickListener(swipeClick );

			}else if (parameters_swipe.getPage_id()!= 0) {
				final int page_id = parameters_swipe.getPage_id();
				OnClickListener swipeClick = new OnClickListener() {

					@Override
					public void onClick(View v) {
						//						swipeAImg.getPaint().setColor(colors.getColor(colors.getTabs_background_color()));

						List<Child_pages> pages = new ArrayList<Child_pages>();
                        pages = realm.where(Child_pages.class).equalTo("id", page_id).findAll();
                        //appController.getChildPageDao().queryForEq("id", page_id);
                        if (pages.size()>0) {
							Child_pages page = pages.get(0);
							if (page.getDesign().equals("panoramic")) {
								FragmentTransaction fragmentTransaction =  getFragmentManager()/*getChildFragmentManager()*/.beginTransaction();
								Fragment prev = getChildFragmentManager().findFragmentByTag("panorama");
								if (prev != null) {
									fragmentTransaction.remove(prev);
								}
								Fragment panoFragment = new PanoramaFragment();
								((MainActivity)getActivity()).extras.putInt("page_id", page.getId_cp());
								((MainActivity)getActivity()).bodyFragment = "PanoramaFragment";
								panoFragment.setArguments(((MainActivity)getActivity()).extras);
								fragmentTransaction.addToBackStack(null);
								fragmentTransaction.replace(R.id.fragment_container, panoFragment, "panorama");
								fragmentTransaction.commit();
							}else if (page.getDesign().equalsIgnoreCase("column") && isTablet) {
                                ((MainActivity)getActivity()).extras = new Bundle();
                                ((MainActivity)getActivity()).extras.putInt("page_id", page.getId_cp());
                                ((MainActivity)getActivity()).bodyFragment = "ColonnePageFragment";
                                ColonnePageFragment colonnePageFragment = new ColonnePageFragment();
                                colonnePageFragment.setArguments(((MainActivity)getActivity()).extras);
                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, colonnePageFragment).addToBackStack(((MainActivity)getActivity()).bodyFragment).commit();
                            }else {
								((MainActivity)getActivity()).extras = new Bundle();
								((MainActivity)getActivity()).extras.putInt("page_id", page.getId_cp());
								PagesFragment pagesFragment = new PagesFragment();
								pagesFragment.setArguments(((MainActivity)getActivity()).extras);
								getFragmentManager()/*getChildFragmentManager()*/.beginTransaction().replace(R.id.fragment_container, pagesFragment).addToBackStack(null).commit();
							}
						}

						if (timer != null) {timer.cancel(); timer.purge();}
						//						view.findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
						//						view.findViewById(R.id.swipe_container).setVisibility(View.GONE);

					}
				};
				logo_swipe.setOnClickListener(swipeClick );
				sub_swipe_title_holder.setOnClickListener(swipeClick );

			}
			StateListDrawable stateDrawable = new StateListDrawable();
			stateDrawable.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(Color.parseColor("#88ffffff")));
			stateDrawable.addState(new int[]{android.R.attr.state_focused}, new ColorDrawable(Color.parseColor("#88ffffff"))); 
			if (isTablet) {
				stateDrawable.addState(new int[]{}, new ColorDrawable(Color.TRANSPARENT)); 
			}else {
				stateDrawable.addState(new int[]{}, new ColorDrawable(Color.parseColor("#AA000000"))); 
			}

			sub_swipe_title_holder.setBackgroundDrawable(stateDrawable);

			swipeAImg.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					ArrowImageView img = (ArrowImageView) v;
					Paint paint = new Paint();
					paint.setColor(Color.GRAY);
					img.setPaint(paint);
					return false;
				}
			});

		}

	}



	public Timer timer;
	int page = 0;

	public void pageSwitcher(int seconds, int max_page) {
		timer = new Timer(); // At this line a new Thread will be created
		timer.scheduleAtFixedRate(new RemindTask(max_page), 0, seconds * 1000); // delay
		// in
		// milliseconds
	}

	// this is an inner class...
	class RemindTask extends TimerTask {

		int maxPage;


		@Override
		public void run() {

			// As the TimerTask run on a separate thread from UI thread we have
			// to call runOnUiThread to do work on UI thread.
			if(((MainActivity)getActivity()) == null) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else 
			((MainActivity)getActivity()).runOnUiThread(new Runnable() {
				public void run() {
					//					if(mViewPager.getChildAt(page) == null) {
					//						List<Fragment> fragments2 = getFragments(elementSwipes);
					//						//		Collections.reverse(fragments2);
					//						pageAdapter = new MyPageAdapter(getFragmentManager()/*getChildFragmentManager()*/, fragments2);
					////						maxPage = pageAdapter.getCount();
					//						mViewPager.setAdapter(pageAdapter);
					////						Toast.makeText(getActivity(), " adapter Item  : "+pageAdapter.getItem(page).getTag()+" Time of swipping to page : "+page+"/"+maxPage+"  of Child : "+mViewPager.getChildAt(page), 500).show();
					//
					//					}
					if (page >= maxPage) { // In my case the number of pages are 5
						page = 0;
						mViewPager.setCurrentItem(page++);
						// Showing a toast for just testing purpose

					} else {
						mViewPager.setCurrentItem(page++);
					}

					//					Toast.makeText(getActivity(), " adapter Item  : "+pageAdapter.getItem(page - 1).getTag()+" Time of swipping to page : "+page+"/"+maxPage+"  of Child : "+mViewPager.getChildAt(page), 500).show();
				}
				//				}
			});

		}


		public RemindTask(int maxPage) {
			super();
			this.maxPage = maxPage;
		}
	}

	private List<Fragment> getFragments(List<ElementSwipe> elementSwipes){
		List<Fragment> fList = new ArrayList<Fragment>();
		for (int i = 0; i < elementSwipes.size(); i++) {
			fList.add(SwipePage.newInstance(elementSwipes.get(i).getId()));
		}



		return fList;
	}

	private class MyPageAdapter extends FragmentPagerAdapter {
		private List<Fragment> fragments;

		public MyPageAdapter(FragmentManager fm, List<Fragment> fragments) {
			super(fm);
			this.fragments = fragments;
		}
		@Override
		public Fragment getItem(int position) {
			return this.fragments.get(position);
		}

		@Override
		public int getCount() {
			return this.fragments.size();
		}
	}


	@Override
	public void onPause() {
		if (timer!=null) {
			timer.cancel();
			//timer.purge();
		}
		super.onPause();
	}

	@Override
	public void onStop() {
		if (timer!=null) {
			Runtime.getRuntime().gc();
			timer.cancel();
			//timer.purge();
		}
		super.onStart();
	}

	@Override
	public void onDestroy(){
		//		imageLoader.destroy();
		Runtime.getRuntime().gc();
		if (timer!=null) {
			//timer.purge();
			timer.cancel();
		}
		//		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
		//		.permitAll().build();
		//		StrictMode.setThreadPolicy(policy);
		//		sendRegistrationIdToBackend();


		super.onDestroy();
	}

	@Override
	public void onResume() {
		//		List<Fragment> fragments2 = getFragments(elementSwipes);
		//		//		Collections.reverse(fragments2);
		//		pageAdapter = new MyPageAdapter(getFragmentManager()/*getChildFragmentManager()*/, fragments2);
		////		maxPage = pageAdapter.getCount();
		//		mViewPager.setAdapter(pageAdapter);

		super.onResume();
	}

}
