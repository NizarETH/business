/**
 * 
 */
package com.euphor.paperpad.activities.fragments;

import android.R.style;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.webkit.MimeTypeMap;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.euphor.paperpad.R;
import com.euphor.paperpad.activities.main.MainActivity;
import com.euphor.paperpad.Beans.CartItem;
import com.euphor.paperpad.Beans.CategoriesMyBox;
import com.euphor.paperpad.Beans.Category;
import com.euphor.paperpad.Beans.Child_pages;
import com.euphor.paperpad.Beans.Illustration;
import com.euphor.paperpad.Beans.Parameters;
import com.euphor.paperpad.Beans.Price;
import com.euphor.paperpad.Beans.Section;

import com.euphor.paperpad.utils.Colors;
import com.euphor.paperpad.utils.Utils;
import com.euphor.paperpad.widgets.AViewFlipper;
import com.euphor.paperpad.widgets.ArrowImageView;


import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;

/**
 * @author euphordev02
 *
 */
public class SliderCategoryFragment_ extends Fragment{

	

	private Colors colors;

	private List<Category> categories;
	protected int id_cat = 0;
	protected LinearLayout choiceHolder;
	protected Category cat;
	private LinearLayout hsvLayout;
	private MainActivity mainActivity;
	private int category_id;
	protected PopupWindow pwShare;
	Illustration illust;
	String path = null;


    public Realm realm;

	private int current;
	private AViewFlipper viewFlipper;
	private int downX,upX;
	


	private ProductBoxPagerAdapter mAdapter;
	private ViewPager mPager;
	private Runnable runPager;
	private Handler handler;
	private boolean mCreated = false;
	/**
	 * 
	 */
	public SliderCategoryFragment_() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.ListFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view;
		view = inflater.inflate(R.layout.slider_categories, container, false);
		view.setBackgroundColor(colors.getColor(colors.getBackground_color()));
		hsvLayout = (LinearLayout)view.findViewById(R.id.choicesProduct);
		//productInfo = (FrameLayout)view.findViewById(R.id.productInfo);
		FrameLayout frameProductInfo = (FrameLayout)view.findViewById(R.id.frameProductInfo);
		frameProductInfo.setBackgroundColor(colors.getColor(colors.getForeground_color()));
		view.findViewById(R.id.backChoices).setBackgroundColor(colors.getBackMixColor(colors.getForeground_color(), 0.10f));//colors.getColor(colors.getForeground_color(), "20"));
//		view.findViewById(R.id.backChoices).setBackgroundDrawable(colors.getForePD());;
		if (categories != null && categories.size()>0) {
			choiceHolder = (LinearLayout)view.findViewById(R.id.choicesHolder);
			cat = categories.get(0);
            realm.beginTransaction();
			cat.setSelected(true);
            realm.commitTransaction();
			for (Iterator<Category> iterator = categories.iterator(); iterator.hasNext();) {
				Category element = (Category) iterator.next();
				fillNavigationBar(element);
			}
			
			if (cat!=null && getChildrenPages(cat).size()>0) {
				fillProductScroller(getChildrenPages(cat), getChildrenPages(cat).iterator().next());
				//fillProductBox(getChildrenPages(cat).iterator().next());
				pages = (ArrayList<Child_pages>) getChildrenPages(cat);
				//mPager = new ViewPager(getActivity());// 
				mPager = (ViewPager) view.findViewById(R.id.pager);
				//productInfo.addView(mPager, android.widget.FrameLayout.LayoutParams.MATCH_PARENT, android.widget.FrameLayout.LayoutParams.MATCH_PARENT);
				mPager.setOffscreenPageLimit(1);
				mPager.setAdapter(new ProductBoxPagerAdapter(getChildFragmentManager()));
				
				mPager.setOnPageChangeListener(new OnPageChangeListener() {
					
					@Override
					public void onPageSelected(int position) {

								if (position >= 0) {
									//int i = position;// - 1;
									if (hsvLayout.getChildAt(position) != null)
									{
										((HorizontalScrollView) ((View) hsvLayout.getParent())).smoothScrollTo(hsvLayout.getChildAt(position).getLeft() + hsvLayout.getChildAt(position).getPaddingLeft(), hsvLayout.getChildAt(position).getTop());
										hsvLayout.getChildAt(position).setAlpha(1);
										if ((position + 1) <= pages.size() - 1) {
											if (hsvLayout.getChildAt(position + 1) != null)
												hsvLayout.getChildAt(position + 1).setAlpha(0.5f);
										}
									}
								}
								


						
								if (position <= pages.size() - 1) {
									//int i = position;// + 1;
									//mPager.setCurrentItem(i);
									if (hsvLayout.getChildAt(position) != null)
									{

										((HorizontalScrollView) ((View) hsvLayout.getParent())).smoothScrollTo(hsvLayout.getChildAt(position).getLeft() + hsvLayout.getChildAt(position).getPaddingLeft(), hsvLayout.getChildAt(position).getTop());
										hsvLayout.getChildAt(position).setAlpha(1);
										if ((position - 1) >= 0)
											hsvLayout.getChildAt(position - 1).setAlpha(0.5f);
									}
								}
					}
					
					@Override
					public void onPageScrolled(int arg0, float arg1, int arg2) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onPageScrollStateChanged(int arg0) {
						// TODO Auto-generated method stub
						
					}
				});

			}
			
			
		}
		

		return view;
		
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
		int section_id = -1;
		if (getArguments().getInt("Section_id")!=0) {
			section_id = getArguments().getInt("Section_id");
		}else {
		}
		
		mainActivity = (MainActivity)activity;
		int category_id = getArguments().getInt("Category_id");
		((MainActivity)getActivity()).bodyFragment = "SliderCategoryFragment";
		((MainActivity)getActivity()).extras = new Bundle();
		((MainActivity)getActivity()).extras.putInt("Category_id", category_id);
		super.onAttach(activity);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);



		category_id = getArguments().getInt("Category_id");
		Category category = realm.where(Category.class).equalTo("id",category_id).findFirst();
		//appController.getCategoryById(category_id);
		if (category != null) {
			categories = getChildCategories( category);
		}
		
		
		int section_id = getArguments().getInt("Section_id");

        Section section = realm.where(Section.class).equalTo("id",section_id).findFirst();
        //appController.getSectionsDao().queryForId(section_id);

        if (section!=null) {
            //titleInStrip = section.getTitle();
            if (section.getCategories().size() > 0) {
                categories = new ArrayList<Category>();
                for (Iterator<Category> iterator = section.getCategories()
                        .iterator(); iterator.hasNext();) {
                    Category communElement = (Category) iterator.next();
                    categories.add(communElement);
                }
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
	
	protected void setAdapter(ProductBoxPagerAdapter mAdapter2)
	{
		mAdapter = mAdapter2;
		runPager = new Runnable() {


			@Override
			public void run()
			{
				mPager.setAdapter(mAdapter);
			}
		};
		if (mCreated)
		{
			handler.post(runPager);
		}
	}

	
	public class ProductBoxPagerAdapter extends FragmentStatePagerAdapter {
		

		public ProductBoxPagerAdapter(FragmentManager fm) {
			super(fm);
			//calendarFragments = new ArrayList<CalendarMonthFragment>();
		}


		@Override
		public Fragment getItem(int position) {
			ProductBoxFragment productBoxFragment = new ProductBoxFragment();
			productBoxFragment.create(pages.get(position));
			//calendarFragment.setCustomTag(""+position);
			return productBoxFragment;
		}

		@Override
		public int getCount() {
			return pages.size();
		}
		
//		@Override
//		public Object instantiateItem(ViewGroup arg0, int arg1) {
//			//mPager.setCurrentItem(indexPage);
//			return super.instantiateItem(arg0, arg1);
//		}



	}

	public List<Category> getChildCategories(Category category) {
		List<Category> result = new ArrayList<Category>();
//		Log.e(" getChildrenCategorie.size for category : "+category.getTitle(),"  : "+category.getCategories1().size());

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
	
	
	@SuppressLint("ValidFragment")
    public class ProductBoxFragment extends Fragment{
		private Child_pages page;
		public ProductBoxFragment(){
			
		}
		
		public ProductBoxFragment create(Child_pages page){
			//ProductBoxFragment fragment = new ProductBoxFragment();
			this.setPage(page);
			return this;
			
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			LinearLayout view = (LinearLayout)inflater.inflate(R.layout.slider_inner_page, null, false);//sliderInnerPage
			view.findViewById(R.id.slideInnerPageDivider).setBackgroundColor(colors.getColor(colors.getForeground_color()));
			/*
			 * COMMUN TASKS
			 */
			//Title product
			TextView titleTV = (TextView)view.findViewById(R.id.titleProductTV);
			titleTV.setTextAppearance(getActivity(), style.TextAppearance_Large);
			titleTV.setTypeface(MainActivity.FONT_TITLE, Typeface.BOLD);
			titleTV.setText(page.getTitle());
			titleTV.setTextColor(colors.getColor(colors.getTitle_color()));

			DisplayMetrics metrics = new DisplayMetrics(); 
			getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);


			if(Utils.isTablet(getActivity())){
				if(metrics.densityDpi >= 213 )
					titleTV.setTextSize(titleTV.getTextSize() + 4);
				else
					titleTV.setTextSize(titleTV.getTextSize() + 8);

				//titleTV.setTextSize(titleTV.getTextSize() + 8);
			}else{
				if(metrics.densityDpi <= 240 )
					titleTV.setTextSize(titleTV.getTextSize() - 10);
				else
					titleTV.setTextSize(titleTV.getTextSize() - 16);
			}

			view.findViewById(R.id.titleProductHolder).setBackgroundColor(colors.getColor(colors.getBackground_color(),"FF"));
			
			//Image Product
			
			final RealmList<Illustration> images = new RealmList<>();
			if(page.getIllustration() != null)
               images.add(page.getIllustration());
			if (images.size()>0 && (ImageView)view.findViewById(R.id.ProductIMG)!= null) {
//				ImagePage imagePage = images.iterator().next();
//				Illustration illust = imagePage.getIllustration();
//				ImageView productImage = (ImageView)view.findViewById(R.id.ProductIMG);
//				productImage.setScaleType(ScaleType.CENTER_CROP);
////				imageLoader.displayImage(path, productImage);
//				if (!illust.getPath().isEmpty()) {
//					Glide.with(getActivity()).load(new File(illust.getPath())).into(productImage);
//				}else {
//					Glide.with(getActivity()).load(illust.getLink()).into(productImage);
//				}
				/**  Uness Modif **/
				///ImageSwitcher  
				viewFlipper = (AViewFlipper) view.findViewById(R.id.viewFlipper);

				Log.e("  image Switcher ","  ==> "+viewFlipper);

				current = 0;
				String path = null;
				Illustration illust = null;

				String[] paths = new String[images.size()];
				//int i = 0;
				for(Iterator<Illustration> iterator = images.iterator(); iterator.hasNext();){

					//			ImagePage imagePage = images.iterator().next();
					Illustration imagePage = iterator.next();
					illust = imagePage;

					ImageView imageView = new ImageView(getActivity().getApplicationContext());
					imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
					imageView.setLayoutParams(new ImageSwitcher.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));

					viewFlipper.addView(imageView);

					if (!illust.getPath().isEmpty()) {
						path = illust.getPath();
						//				paths[i] = illust.getPath();
						Glide.with(getActivity()).load(new File(path)).into(imageView);

						//				Glide.with(getActivity()).load(new File(path)).into(productImage);
					}else {
						path = illust.getLink();
						//				paths[i] = illust.getLink();
						Glide.with(getActivity()).load(path).into(imageView);
						//				Glide.with(getActivity()).load(path).into(productImage);
					}

					current++;
					//			imageLoader.displayImage(path, productImage);
				}	


				/** Uness Modif **/
				if(current > 1)
					viewFlipper.setOnTouchListener(new View.OnTouchListener() {
						@Override
						public boolean onTouch(View v, MotionEvent event) {


							if (event.getAction() == MotionEvent.ACTION_DOWN) {
								downX = (int) event.getX(); 
								return true;
							} 

							else if (event.getAction() == MotionEvent.ACTION_UP) {
								upX = (int) event.getX(); 

								if (upX - downX > 100) {

									current--;
									if (current < 0) {
										current = images.size() - 1;
									}

									viewFlipper.setInAnimation(AnimationUtils.loadAnimation(getActivity().getApplicationContext(),R.anim.slide_in_left));
									viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(getActivity().getApplicationContext(),R.anim.slide_out_right));

									viewFlipper.showPrevious(); //.setImageResource(imgs[current]);
								} 

								else if (downX - upX > - 100) {

									current++;
									if (current > images.size() - 1) {
										current = 0;
									}


									viewFlipper.setInAnimation(AnimationUtils.loadAnimation(getActivity().getApplicationContext(),R.anim.slide_in_right));
									viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(getActivity().getApplicationContext(),R.anim.slide_out_left));

									viewFlipper.showNext(); //.setImageResource(imgs[current]);
								}
								return true;
							}

							return false;

						}

					});

			}

			
			
			// Arrows next and previous coloring and actions !
			ArrowImageView previousIMG = (ArrowImageView)view.findViewById(R.id.PreviousIMG);
			Paint paint = getNewPaint();
			previousIMG.setPaint(paint );
			ArrowImageView nextIMG = (ArrowImageView)view.findViewById(R.id.NextIMG);
			nextIMG.setPaint(paint );
            Category category=null;
            if(page.getCategory()!=null)
            {category = page.getCategory();}
            category =realm.where(Category.class).equalTo("id",page.getCategory_id()).findFirst();

//			try {
//				category = appController.getCategoryDao().queryForId(category.getId_category());
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			Collection<Child_pages> pagesTmp = getChildrenPages(category);
			LinearLayout navButtons = (LinearLayout)view.findViewById(R.id.navButtons);
			LinearLayout backPrev = (LinearLayout)view.findViewById(R.id.backPrevious);
			LinearLayout backNext = (LinearLayout)view.findViewById(R.id.backNext);
			if (pagesTmp.size()>1) {

				pages = new ArrayList<Child_pages>();
				pages.addAll(pagesTmp);
				//indexCurrent = indexOfPage(page);
				backPrev.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						if (mPager.getCurrentItem() > 0) {
							int i = mPager.getCurrentItem() - 1;
							mPager.setCurrentItem(i);
							

//							((HorizontalScrollView)((View)hsvLayout.getParent())).smoothScrollTo(hsvLayout.getChildAt(i).getLeft() + hsvLayout.getChildAt(i).getPaddingLeft(), hsvLayout.getChildAt(i).getTop());
//							hsvLayout.getChildAt(i).setAlpha(1);
//							hsvLayout.getChildAt(mPager.getCurrentItem() + 1).setAlpha(0.5f);
						}
						


						
					}
				});; 
				backNext.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						if (mPager.getCurrentItem() < pages.size() - 1) {
							int i = mPager.getCurrentItem() + 1;
							mPager.setCurrentItem(i);
							

//							((HorizontalScrollView)((View)hsvLayout.getParent())).smoothScrollTo(hsvLayout.getChildAt(i).getLeft() + hsvLayout.getChildAt(i).getPaddingLeft(), hsvLayout.getChildAt(i).getTop());
//							hsvLayout.getChildAt(i).setAlpha(1);
//							hsvLayout.getChildAt(i - 1).setAlpha(0.5f);
						}

					}
				});
//				backPrev.setOnClickListener(getNavigationBtnListener(PREVIOUS));
//				backNext.setOnClickListener(getNavigationBtnListener(NEXT));
			}else {

				navButtons.setVisibility(View.GONE);
			}
			
			//category parent
			TextView categoryTv = (TextView)view.findViewById(R.id.categorieTV);
			categoryTv.setTypeface(MainActivity.FONT_BODY, Typeface.BOLD);
			categoryTv.setText(category.getTitle());
			categoryTv.setTextColor(colors.getColor(colors.getForeground_color()));
			LinearLayout categoryHolder = (LinearLayout)view.findViewById(R.id.Category);
			categoryHolder.setBackgroundColor(colors.getColor(colors.getBackground_color(), "FF"));
			final int categoryId = category.getId();
			categoryHolder.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Category category = realm.where(Category.class).equalTo("id", categoryId).findFirst();
                            //appController.getCategoryById(categoryId);

					((MainActivity)getActivity()).openCategory(category);


				}
			});
			/** Uness to Modify **/
			
			if (view.findViewById(R.id.shareHolder) != null) {

				//view.findViewById(R.id.shareHolder).setVisibility(View.GONE);
//				view.findViewById(R.id.divider4).setVisibility(View.GONE);
				final View layout = inflater.inflate(R.layout.popup_share, null, false);
				LinearLayout shareHolder = (LinearLayout)view.findViewById(R.id.shareHolder);
				
				navButtons.setBackgroundColor(colors.getColor(colors.getBackground_color(),"66"));
				TextView shareLabelTV = (TextView)view.findViewById(R.id.shareLabelTV);
				shareLabelTV.setTextColor(colors.getColor(colors.getForeground_color()));
				shareLabelTV.setTypeface(MainActivity.FONT_BODY, Typeface.BOLD);
				ImageView shareIMG = (ImageView)view.findViewById(R.id.shareIMG);
				Drawable drawableShare = getResources().getDrawable(R.drawable.feed_icon);
				drawableShare.setColorFilter(new PorterDuffColorFilter(colors.getColor(colors.getForeground_color()) ,PorterDuff.Mode.MULTIPLY));
				shareIMG.setBackgroundDrawable(drawableShare);
				
				if(!cat.getShare_button().equalsIgnoreCase("1")){
					shareIMG.setVisibility(View.GONE);
					shareLabelTV.setVisibility(View.GONE);
					view.findViewById(R.id.divider4).setVisibility(View.GONE);
				}else
				shareHolder.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						pwShare = new PopupWindow(layout ,
								LinearLayout.LayoutParams.WRAP_CONTENT,
								LinearLayout.LayoutParams.WRAP_CONTENT, true);
						// display the popup in the center
						pwShare.setOutsideTouchable(true);
						pwShare.setBackgroundDrawable(new ColorDrawable(
								Color.TRANSPARENT));
						pwShare.setFocusable(true);
						pwShare.setTouchInterceptor(new OnTouchListener() {
							@Override
							public boolean onTouch(View v, MotionEvent event) {
								if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
									pwShare.dismiss();
									return true;
								}
								return false;
							}
						});
						pwShare.showAtLocation(layout, Gravity.CENTER, 0, 0);
						/*new Handler().postDelayed(new Runnable(){

					    public void run() {

					    }

					}, 100L);*/

						ImageView fb = (ImageView)layout.findViewById(R.id.fb);
						ImageView others = (ImageView)layout.findViewById(R.id.others);
						fb.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								pwShare.dismiss();
								mainActivity.fbShare(illust.getLink(), page);

							}
						});
						others.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								pwShare.dismiss();
								Intent share = new Intent(Intent.ACTION_SEND);
								MimeTypeMap map = MimeTypeMap.getSingleton(); //mapping from extension to mimetype
								String ext = path.substring(path.lastIndexOf('.') + 1);
								String mime = map.getMimeTypeFromExtension(ext);
								share.setType(mime); // might be text, sound, whatever
								Uri uri;
								//					    if (path.contains("Paperpad/")) {
								//							uri = Uri.fromFile(new File(path));
								//						}else {
								uri =  Uri.parse(illust.getLink());
								//						}
								share.putExtra(Intent.EXTRA_SUBJECT,page.getTitle());
								share.putExtra(Intent.EXTRA_TEXT,page.getIntro());
								share.putExtra(Intent.EXTRA_STREAM,uri);//using a string here didnt work for me
								Log.d("", "share " + uri + " ext:" + ext + " mime:" + mime);
								startActivity(Intent.createChooser(share, "share"));

							}
						});
						//				String path = "/mnt/sdcard/dir1/sample_1.jpg";


					}
				});
			}
			
			/**  **/
			 if(view.findViewById(R.id.navigationHolder)!=null){
			    	view.findViewById(R.id.navigationHolder).setBackgroundColor(colors.getColor(colors.getBackground_color()));
//			    	view.findViewById(R.id.shareHolder).setBackgroundColor(colors.getColor(colors.getBackground_color()));
//			    	view.findViewById(R.id.navButtons).setBackgroundColor(colors.getColor(colors.getBackground_color()));
			 }
			
			
			// Long description under the product image
			WebView longdescWV = (WebView)view.findViewById(R.id.LongDescWV);
			StringBuilder htmlString = new StringBuilder();
			int[] colorText = Colors.hex2Rgb(colors.getTitle_color());
			longdescWV.setBackgroundColor(Color.TRANSPARENT);
			if (view.findViewById(R.id.tempLL) != null) {
				view.findViewById(R.id.tempLL).setBackgroundColor(colors.getColor(colors.getBackground_color(), "FF"));
			}else {
				longdescWV.setBackgroundColor(colors.getColor(colors.getBackground_color(), "FF"));
			}
			htmlString.append(Utils.paramBodyHTML(colorText));
			htmlString.append(page.getBody());
			
			if(view.findViewById(R.id.divider)!=null)
				view.findViewById(R.id.divider).setBackgroundColor(colors.getColor(colors.getForeground_color()));
			if(view.findViewById(R.id.divider2)!=null)
				view.findViewById(R.id.divider2).setBackgroundColor(colors.getColor(colors.getForeground_color()));
		    if(view.findViewById(R.id.divider3)!=null)
		    	view.findViewById(R.id.divider3).setBackgroundColor(colors.getColor(colors.getForeground_color()));
		    if(view.findViewById(R.id.divider4)!=null)
		    	view.findViewById(R.id.divider4).setBackgroundColor(colors.getColor(colors.getForeground_color()));
		    if(view.findViewById(R.id.divider5)!=null)
		    	view.findViewById(R.id.divider5).setBackgroundColor(colors.getColor(colors.getForeground_color()));
		    if(view.findViewById(R.id.divider6)!=null)
		    	view.findViewById(R.id.divider6).setBackgroundColor(colors.getColor(colors.getForeground_color()));
		    Drawable cadreDrawable = getResources().getDrawable(R.drawable.cadre);
		    cadreDrawable.setColorFilter(new PorterDuffColorFilter(colors.getColor(colors.getBackground_color()) ,PorterDuff.Mode.MULTIPLY));
		    view.findViewById(R.id.navigationHolderForStroke).setBackgroundDrawable(cadreDrawable);
		    
		    view.findViewById(R.id.shareHolder).setBackgroundColor(colors.getColor(colors.getBackground_color(), "FF"));
		    LinearLayout portNavigationHolderForStroke = (LinearLayout)view.findViewById(R.id.navigationHolderForStrokePort);
		    if (portNavigationHolderForStroke != null) {
		    	portNavigationHolderForStroke.setBackgroundDrawable(cadreDrawable);
		    	view.findViewById(R.id.dividerExtra).setBackgroundColor(colors.getColor(colors.getForeground_color()));
		    	view.findViewById(R.id.dividerExtra1).setBackgroundColor(colors.getColor(colors.getForeground_color()));
		    	view.findViewById(R.id.navigationHolderPort).setBackgroundColor(colors.getColor(colors.getBackground_color(),"FF"));
			}
		    longdescWV.getSettings().setDefaultFontSize(18);
		    longdescWV.loadDataWithBaseURL(null, htmlString.toString(), "text/html", "UTF-8", null); 
		    
		    //prices related to this product
		    Collection<Price> prices = page.getPrices();
		    LinearLayout pricesContainer = (LinearLayout)view.findViewById(R.id.PricesHolder);
		    pricesContainer.setBackgroundColor(colors.getColor(colors.getBackground_color(), "FF"));
		    view.findViewById(R.id.hsvPrices).setBackgroundColor(colors.getColor(colors.getBackground_color(), "FF"));
		    if (prices.size()>0) {

		    	for (Iterator<Price> iterator = prices.iterator(); iterator.hasNext();) {
		    		Price price = (Price) iterator.next();
		    		View priceElement = inflater.inflate(R.layout.price, null, false);
		    		LinearLayout btnPrice = (LinearLayout)priceElement.findViewById(R.id.btnPrice);
		    		
		    	
		    		TextView btnPlus  = (TextView) priceElement.findViewById(R.id.txtPlus);
		    		btnPlus.setVisibility(View.VISIBLE);
		    		btnPlus.setTextColor(colors.getColor(colors.getBody_color(),"80"));
		    		priceElement.findViewById(R.id.imgPlus).setVisibility(View.GONE);
		    		
//		    		color_txt = new ColorStateList(new int[][]{{android.R.attr.state_pressed},{}}, 
//					new int[]{colors.getColor(colors.getBackground_color()), 
//					colors.getColor(colors.getTabs_selected_foreground_color())});

		    		//Drawable drawable = btnPrice.getBackground();
		    		//drawable.setColorFilter(colors.getColor(colors.getForeground_color(), "88"), Mode.MULTIPLY);
		    		//btnPrice.setBackgroundDrawable(drawable);
//		    		btnPrice.setBackgroundColor(colors.getColor(colors.getNavigation_background_color()));

		    		btnPrice.setBackgroundColor(colors.getColor(colors.getForeground_color(), "25") /*colors.getColor("D1CCB6")*/);
//		    		PorterDuffColorFilter greyFilter = new PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
//		    		btnPrice.getBackground().setColorFilter(greyFilter);

		    		TextView label = (TextView)priceElement.findViewById(R.id.priceLabelTV);
		    		label.setTypeface(MainActivity.FONT_BODY);
		    		if (price!=null && price.getLabel()!=null && !price.getLabel().isEmpty()) {
		    			label.setText(price.getLabel());
		    			label.setTextSize(15);
		    			label.setTextColor(colors.getColor(colors.getBody_color()));
		    		}else {
		    			label.setVisibility(View.GONE);
		    		}
		    		TextView value = (TextView)priceElement.findViewById(R.id.priceValueTV);
		    		value.setTypeface(MainActivity.FONT_BODY);
		    		value.setText((price!=null && price.getAmount()!=null)?price.getAmount()+" "+price.getCurrency():"");
		    		value.setTextColor(colors.getColor(colors.getTitle_color()));
		    		value.setTextSize(23);
		    		priceElement.setTag(price);
		    		parameters = null;
                    parameters = realm.where(Parameters.class).findFirst();
                    //appController.getParametersDao().queryForId(1);

                    if (parameters != null) {
		    			if (!parameters.isShow_cart()) {
		    				priceElement.findViewById(R.id.imgPlus).setVisibility(View.GONE);
		    			}
		    		}
		    		//	  				priceElement.setTag(page);
		    		priceElement.setOnClickListener(new OnClickListener() {

		    			@Override
		    			public void onClick(View v) {
		    				//	  						Child_pages page = (Child_pages)v.getTag();


		    				if (parameters != null) {
		    					if (parameters.isShow_cart()) {

									Category category = null;
									if (page.getCategory() != null && page.getCategory().isNeeds_stripe_payment()) {
										category = page.getCategory();
									}else {
										//AppController controller = new AppController(mainActivity);
										category = realm.where(Category.class).equalTo("id",page.getCategory_id()).findFirst();
										// controller.getCategoryById(page.getCategory_id());
									}
									if(category != null && (MainActivity.stripe_or_not == -1 || (category.isNeeds_stripe_payment() && MainActivity.stripe_or_not == 1) ||
											(!category.isNeeds_stripe_payment() && MainActivity.stripe_or_not == 0))){
										if (category.isNeeds_stripe_payment()) {
											MainActivity.stripe_or_not = 1;
										}else {
											MainActivity.stripe_or_not = 0;
										}
										Price price = (Price) v.getTag();
                                        mainActivity.id_Cart++;
										CartItem cartItem = new CartItem( mainActivity.id_Cart,price.getId_price(), page, null, 0, 1, null, price.getAmount(), page.getTitle(), price.getLabel(), new RealmList<CartItem>());
										//mainActivity.addItemToDB(cartItem);
										mainActivity.addItemToDB(price.getId_price(), page, null, 0, 1, null, price.getAmount(), page.getTitle(), price.getLabel(), new RealmList<CartItem>());
										mainActivity.fillCart();
										mainActivity.getMenu().showMenu();
									}else {
//										Toast.makeText(getActivity(), "not allowed to add because mode :"+MainActivity.stripe_or_not, Toast.LENGTH_LONG).show();
										if (category!= null) {
											if (!category.isNeeds_stripe_payment() && MainActivity.stripe_or_not == 1) {
												AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
												builder.setTitle(getString(R.string.payment_stripe_title)).setMessage(getString(R.string.payment_stripe_msg))
												.setPositiveButton(getString(R.string.close_dialog),new DialogInterface.OnClickListener() {
													public void onClick(DialogInterface dialog, int id) {
														dialog.cancel();
													}
												});

												builder.create().show();
											}else if (category.isNeeds_stripe_payment() && MainActivity.stripe_or_not == 0) {
												AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
												builder.setTitle(getString(R.string.payment_stripe_title)).setMessage(getString(R.string.no_payment_stripe_msg))
												.setPositiveButton(getString(R.string.close_dialog),new DialogInterface.OnClickListener() {
													public void onClick(DialogInterface dialog, int id) {
														dialog.cancel();
													}
												});

												builder.create().show();
											}
										}
										
									}
								}
		    				}

		    			}
		    		});

		    		// espacement
		    		View espace = new View(getActivity());
		    		android.widget.LinearLayout.LayoutParams params = new android.widget.LinearLayout.LayoutParams(20 /*5*/, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
		    		pricesContainer.addView(espace, params);
		    		pricesContainer.addView(priceElement);
		    	}

		    }else {
		    	pricesContainer.setVisibility(View.GONE);
		    }
			view.findViewById(R.id.LLInfoProduct).setBackgroundColor(colors.getColor(colors.getBackground_color(), "FF"));
			view.findViewById(R.id.LLRelatedItems).setBackgroundColor(colors.getColor(colors.getBackground_color(), "FF"));
			view.findViewById(R.id.innerPage).setBackgroundColor(colors.getColor(colors.getBackground_color(), "FF"));
			//productInfo.addView(view);
		
			return view;
		}

		public Child_pages getPage() {
			return page;
		}
		public void setPage(Child_pages page) {
			this.page = page;
		}
	}
	
	public List<Child_pages> getChildrenPages(Category category) {
		List<Child_pages> result = new ArrayList<Child_pages>();
//		Log.e(" getChildrenPages.size for category : "+category.getTitle(),"  : "+category.getCategories1().size());
        RealmList<Child_pages> elements=new RealmList<>();
      if(category!=null){
      if(category.getChildren_pages() !=null)
      {elements = category.getChildren_pages();}}

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
		categoryTxt.setTextAppearance(getActivity(), style.TextAppearance_Medium);
		categoryTxt.setTypeface(MainActivity.FONT_BODY, Typeface.BOLD);
		if (category.isSelected() ) {
			realm.beginTransaction();
			for (int i = 0; i < categories.size(); i++) {
				categories.get(i).setSelected(false);
			}
			realm.commitTransaction();
			LinearLayout.LayoutParams txtParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
			//txtParams.setMargins(10, 0, 10, 5);
			txtParams.gravity = Gravity.CENTER;
			categoryTxt.setGravity(Gravity.CENTER);
			categoryTxt.setText(category.getTitle().toUpperCase());
			categoryTxt.setTextColor(colors.getColor(colors.getBackground_color()));
			
//			Drawable dr = getResources().getDrawable(R.drawable.bottom_bubble);		
//			Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
//			Drawable d = new BitmapDrawable(getResources(),Bitmap.createScaledBitmap(bitmap, categoryTxt.getText().length() * 11, 50, true));
//			d.setColorFilter(colors.getColor(colors.getTitle_color()), Mode.MULTIPLY);
//			//categoryTxt.setCompoundDrawablesWithIntrinsicBounds(d, null,null,null);
//			categoryTxt.setBackgroundDrawable(d);
			Drawable selectDrawable = getResources().getDrawable(R.drawable.back_choices_final);
			selectDrawable.setColorFilter(colors.getColor(colors.getTitle_color()), Mode.MULTIPLY);
			categoryTxt.setBackgroundDrawable(selectDrawable);
			categoryTxt.setTag(category);
			choiceHolder.addView(categoryTxt);//, txtParams);
			
		}else {
			
			LinearLayout.LayoutParams txtParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
			txtParams.setMargins(10, 0, 10, 5);
			categoryTxt.setGravity(Gravity.CENTER);
			txtParams.gravity = Gravity.CENTER;
			categoryTxt.setText((category.getTitle()).toUpperCase());
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
                realm.beginTransaction();
				cat = (Category)v.getTag();
				for (int i = 0; i < categories.size(); i++) {
					categories.get(i).setSelected(false);
				}
				cat.setSelected(true);
				realm.commitTransaction();

				choiceHolder.removeAllViews();
				for (int i = 0; i < categories.size(); i++) {
					
					fillNavigationBar(categories.get(i));
				}
				hsvLayout.removeAllViews();
				if (cat!=null && getChildrenPages(cat).size()>0) {
					fillProductScroller(getChildrenPages(cat), getChildrenPages(cat).iterator().next());
					//fillProductBox(getChildrenPages(cat).iterator().next());
					pages = (ArrayList<Child_pages>) getChildrenPages(cat);
					mPager.setAdapter(new ProductBoxPagerAdapter(getChildFragmentManager()));
				}

			}
		});
	}

	View selectedBefore;
	//private int indexCurrent;
	protected ArrayList<Child_pages> pages;
	private Parameters parameters;
	private Child_pages page;
	/**
	 * @param collection 
	 * 
	 */
	public void fillProductScroller(Collection<Child_pages> collection, Child_pages selected) {
		hsvLayout.removeAllViews();
		for (Iterator<Child_pages> iterator = getChildrenPages(cat).iterator(); iterator.hasNext();) {
			page = (Child_pages) iterator.next();
			LinearLayout layoutProduct = new LinearLayout(getActivity());

			layoutProduct.setTag(page);
			layoutProduct.setPadding(10, 5, 10, 5);
			
			ImageView imgProd = new ImageView(getActivity());
			//Thumb Product
			RealmList<Illustration> images = new RealmList<>();
			if(page.getIllustration() != null)
            images.add(page.getIllustration());
			if (images.size()>0) {
                Illustration imagePage = images.iterator().next();
				Illustration illust = imagePage;
//				String path = !illust.getPath().isEmpty()?"file:///"+illust.getPath():illust.getLink();
//				imageLoader.displayImage(path, imgProd);
				if(illust != null) {
					
					
					imgProd.setScaleType(ScaleType.CENTER_CROP);

					float ratioWidth = ((illust.getOriginalWidth() / illust.getOriginalHeight() * 10) == 0) ? 10 : (illust.getOriginalWidth() / illust.getOriginalHeight() * 12);

					imgProd.setLayoutParams(new LinearLayout.LayoutParams((int) (ratioWidth * 12), 140));
					
					layoutProduct.addView(imgProd);
					hsvLayout.addView(layoutProduct, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
					
				if (!illust.getPath().isEmpty()) {
					Glide.with(getActivity()).load(new File(illust.getPath())).into(imgProd);
				}else {
					Glide.with(getActivity()).load(illust.getLink()).into(imgProd);
				}
				}
			}
			

			
			if (page.equals(selected)) {
				layoutProduct.setAlpha(1);
				selectedBefore = layoutProduct;
			
			}else {
				layoutProduct.setAlpha(0.5f);
			}
			

			layoutProduct.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
//					Toast.makeText(getActivity(), "id_page : "+((Child_pages)v.getTag()).getId_cpages(), Toast.LENGTH_SHORT).show();
					v.setAlpha(1);
					if (selectedBefore!= null && !(((Child_pages)selectedBefore.getTag()).getId() == ((Child_pages)v.getTag()).getId())) {
						selectedBefore.setAlpha(0.5f);
					}
					selectedBefore = v;
					//fillProductBox(((Child_pages)v.getTag()));
					
					mPager.setCurrentItem(indexOfPage(((Child_pages)v.getTag())), true);
					
				}
			});
		}
		
		
//		for (int i = 0 ; i <  hsvLayout.getChildCount(); i++){
//
//			LinearLayout layoutProduct = (LinearLayout) hsvLayout.getChildAt(i);
//			if (layoutProduct.getAlpha() == 1.0f) {		
//				((HorizontalScrollView)((View)hsvLayout.getParent())).smoothScrollTo(layoutProduct.getLeft() + layoutProduct.getPaddingLeft(), layoutProduct.getTop());
//				
//			}
//			
//		}
	}


	private Paint getNewPaint() {
		Paint paint = new Paint();
		paint.setColor(colors.getColor(colors.getForeground_color(),"FF"));
		return paint;
	}
	
	private int indexOfPage(Child_pages page) {
		int index = -1;
		for (int i = 0; i < pages.size(); i++) {
			if (page.getId_cp()==pages.get(i).getId_cp()) {
				index = i;
			}
		}
		return index;
	}

		

}
