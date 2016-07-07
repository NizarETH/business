/**
 * 
 */
package com.euphor.paperpad.activities.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import com.euphor.paperpad.R;
import com.euphor.paperpad.activities.fragments.TabsSupportFragment.ActionCallBack;
import com.euphor.paperpad.activities.main.MainActivity;
import com.euphor.paperpad.activities.main.PriceCallBack;
import com.euphor.paperpad.activities.main.SplashActivity;
import com.euphor.paperpad.activities.main.YoutubePlayerActivity;
import com.euphor.paperpad.Beans.Application;
import com.euphor.paperpad.Beans.Cart;
import com.euphor.paperpad.Beans.CartItem;
import com.euphor.paperpad.Beans.Language;
import com.euphor.paperpad.Beans.Parameters;
import com.euphor.paperpad.Beans.Tab;

import com.euphor.paperpad.utils.Colors;
import com.euphor.paperpad.utils.Constants;
import com.euphor.paperpad.utils.Utils;
import com.euphor.paperpad.utils.jsonUtilities.AppJsonWriter;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.realm.Realm;

/**
 * @author euphordev02
 *
 */
public class TabsFragment extends Fragment implements PriceCallBack{


	private ActionCallBack mCallBack = new ActionCallBack() {
		
		@Override
		public void onTabClick(Tab tab, int index) {
			Log.i(TAG, "Dumb implementation");
			
		}

		@Override
		public void onLanguageChanged() {
			// TODO Auto-generated method stub
			
		}
	};
	
	private boolean isTablet;
	public Tab seletedTab;
	private static final String TAG = "TabsFragment";
	private Activity activity;
	public LinearLayout tabsContainer;
	public List<Tab> tabs;
	private YoutubePlayerActivity mainActivity;
	private BitmapDrawable mDrawable;
	private ImageView tabImg;
	private Bitmap bm;
	private View scrollView;
	public int indexSeletedTab;





	private Colors colors;

	public LayoutInflater inflater;

	private boolean bottomNav = false;

	protected PopupWindow pw;
	
	private boolean showFrench = false;
	private boolean showEnglish = false;
    public Realm realm;
	private ImageView flag;

	/**
	 * 
	 */
	public TabsFragment() {
		// TODO Auto-generated constructor stub
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setRetainInstance(true);



    }
	

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	SharedPreferences wmbPreference;

	private String lang;

	private TextView totalPriceTV;

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
		this.activity = activity;
		mCallBack = (ActionCallBack)activity;
		mainActivity = (YoutubePlayerActivity)activity;
//		((MainActivity)activity).initImageLoader();


				realm = Realm.getInstance(getActivity());
		colors = ((YoutubePlayerActivity)activity).colors;
        com.euphor.paperpad.Beans.Parameters ParamColor = realm.where(com.euphor.paperpad.Beans.Parameters.class).findFirst();
        if (colors==null) {
            colors = new Colors(ParamColor);
        }


        Parameters parameters = null;
        parameters = realm.where(Parameters.class).findFirst();
        //appController.getParametersDao().queryForId(1);
        if (parameters != null) {
			if (parameters.getNavigation_type()!= null) {
				if (parameters.getNavigation_type().contains("bottom")) {
					bottomNav = true;
				}else {
					bottomNav = false;
				}
			}else {
				bottomNav = true;
			}
		}
		wmbPreference = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		lang = wmbPreference.getString(Utils.LANG, "fr");
        isTablet = Utils.isTablet(activity);
		super.onAttach(activity);
	}



	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view;
		Parameters params = null;

        params = realm.where(Parameters.class).findFirst();
        //appController.getParametersDao().queryForId(1);

        if (params != null) {
			if (params.getNavigation_type().contains("bottom")) {
				bottomNav = true;
			}else {
				bottomNav = false;
			}
		}

		if (isTablet) {
			if (bottomNav) {
				view = inflater.inflate(R.layout.horizontal_tabs_fragment, container, false);
			}else {
				view = inflater.inflate(R.layout.tabs_fragment, container, false);
			}
		}else {
			view = inflater.inflate(R.layout.horizontal_tabs_fragment, container, false);
		}
		LinearLayout svContainer = (LinearLayout)view.findViewById(R.id.svContainer);
		LinearLayout svContainerParent = (LinearLayout)view.findViewById(R.id.svContainerParent);
		svContainerParent.setBackgroundColor(mainActivity.colors.getColor(mainActivity.colors.getTabs_background_color()));
		final LayoutInflater tmpInflater = inflater;
		
		flag = (ImageView)view.findViewById(R.id.FlagLanguage);
		if (lang.equals("fr")) {
			flag.setImageDrawable(getResources().getDrawable(
					R.drawable.french_r));
			// change the locale to use other languages
			Utils.changeLocale("fr", getActivity());
		} else if (lang.equals("en")) {
			flag.setImageDrawable(getResources().getDrawable(
					R.drawable.english_r));

			Utils.changeLocale("en",getActivity());
		}
		LinearLayout languageContainer = (LinearLayout)view.findViewById(R.id.LanguageContainer);
		if ( !isTablet) {
			languageContainer.setVisibility(View.GONE);
		}
		List<Language> languages = new ArrayList<Language>();
        languages = realm.where(Language.class).findAll();
        //appController.getLanguageDao().queryForAll();

        if (languages.size()>1) {
			for (Iterator<Language> iterator = languages.iterator(); iterator
					.hasNext();) {
				Language language = (Language) iterator.next();
				if (language.getString().equals("fr")) {
					showFrench  = true;
				}else if (language.getString().equals("en")) {
					showEnglish = true;
				}
				
			}
			
			languageContainer.setOnClickListener(new OnClickListener() {
				
				

				

				@Override
				public void onClick(View v) {
//					mCallBack.onLanguageChanged();
					View layout = tmpInflater.inflate(
							R.layout.language_dialog, null);
					LinearLayout frenchContainer = (LinearLayout) layout
							.findViewById(R.id.frenchContainer);
					if (!showFrench) {
						frenchContainer.setVisibility(View.GONE);
					}
					LinearLayout englishContainer = (LinearLayout) layout
							.findViewById(R.id.englishContainer);

					if (showFrench) {
						frenchContainer.setVisibility(View.VISIBLE);

						frenchContainer
								.setOnClickListener(new OnClickListener() {


									@Override
									public void onClick(View v) {
										flag.setImageDrawable(getResources()
												.getDrawable(
														R.drawable.french_r));
										//langTxt.setTypeface(FONT_REGULAR);
										SharedPreferences.Editor editor = wmbPreference
												.edit();
										editor.putString(Utils.LANG, "fr");
										editor.commit();
										String lang = wmbPreference
												.getString(Utils.LANG,
														"fr");
//										LANGUAGE_ID = Integer.parseInt(lang);
										Intent intent = new Intent(getActivity(),SplashActivity.class);
										Bundle b = new Bundle();
										b.putString(Utils.LANG, "fr");
										int id = 0;
                                        List<Parameters> parameters = realm.where(Parameters.class).findAll();
                                        //appController.getParametersDao().queryForAll();
                                        if (parameters.size()>0) {
                                            Parameters params = parameters.get(0);
                                            id = params.getId();
                                        }
                                        b.putInt("ID_MENU", id);
										intent.putExtras(b);
										startActivity(intent);
										pw.dismiss();
									}

									
								});
					} else {
						frenchContainer.setVisibility(View.GONE);
					}

					if (showEnglish) {
						englishContainer.setVisibility(View.VISIBLE);

						englishContainer
								.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										flag.setImageDrawable(getResources()
												.getDrawable(
														R.drawable.english_r));
										SharedPreferences.Editor editor = wmbPreference.edit();
										editor.putString(Utils.LANG, "en");
										editor.commit();
							
										String lang = wmbPreference
												.getString(Utils.LANG,
														"fr");
//										LANGUAGE_ID = Integer
//												.parseInt(lang);
//										Intent intent = new Intent();
										Intent intent = new Intent(getActivity(),SplashActivity.class);
										Bundle b = new Bundle();
										b.putString(Utils.LANG, "en");
										int id = 0;
                                        List<Parameters> parameters = realm.where(Parameters.class).findAll();
                                        //appController.getParametersDao().queryForAll();
                                        if (parameters.size()>0) {
                                            Parameters params = parameters.get(0);
                                            id = params.getId();
                                        }
                                        b.putInt("ID_MENU", id);
										intent.putExtras(b);
										startActivity(intent);
										pw.dismiss();
									}
								});
					} else {
						englishContainer.setVisibility(View.GONE);
					}

					
					pw = new PopupWindow(layout,
							LinearLayout.LayoutParams.WRAP_CONTENT,
							LinearLayout.LayoutParams.WRAP_CONTENT, true);
					// display the popup in the center
					pw.setOutsideTouchable(true);
					pw.setBackgroundDrawable(new ColorDrawable(
							android.R.color.transparent));
					pw.setFocusable(true);
					pw.setTouchInterceptor(new OnTouchListener() {
						@Override
						public boolean onTouch(View v, MotionEvent event) {
							if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
								pw.dismiss();
								return true;
							}
							return false;
						}
					});
					if (bottomNav || !isTablet) {
						layout.setBackgroundDrawable(getResources().getDrawable(R.drawable.bonuspack_bubble_white_mod_horizontal));
						pw.showAtLocation(v, Gravity.LEFT|Gravity.BOTTOM, 0, 100);
					}else {
						layout.setBackgroundDrawable(getResources().getDrawable(R.drawable.bonuspack_bubble_white_mod2));
						pw.showAsDropDown(v);
					}
				}
			});
		}else {
			languageContainer.setVisibility(View.GONE);
		}
		
		tabsContainer = new LinearLayout(activity);
		LayoutParams lParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		if (Utils.isTablet(activity)) {
			if (bottomNav) {
				scrollView = (HorizontalScrollView)view.findViewById(R.id.scrollViewTabs);
				tabsContainer.setOrientation(LinearLayout.HORIZONTAL);
			}else {
				scrollView = (ScrollView)view.findViewById(R.id.scrollViewTabs);
				tabsContainer.setPadding(0, 40, 0, 80);
				tabsContainer.setOrientation(LinearLayout.VERTICAL);
				scrollView.setVerticalScrollBarEnabled(false);
				scrollView.setVerticalFadingEdgeEnabled(false);
				scrollView.setHorizontalFadingEdgeEnabled(false);
			}
		}else {
			scrollView = (HorizontalScrollView)view.findViewById(R.id.scrollViewTabs);
			tabsContainer.setOrientation(LinearLayout.HORIZONTAL);
		}
		
		tabsContainer.setLayoutParams(lParams);
		tabsContainer.setGravity(Gravity.CENTER);
		tabs = new ArrayList<Tab>();
        if(tabs.size()==0)
        {tabs = realm.where(Tab.class).findAll();}//appController.getTabDao().queryForAll();
        tabsContainer.setTag(tabs);
		Bundle bundle = getArguments();
		this.inflater = inflater;
		if(bundle != null) {
			int index_selected = getArguments().getInt("highlighted_tab");
			indexSeletedTab = index_selected;
			boolean orientation = getArguments().getBoolean("orientation");
			if (orientation) {
				redrawIcons(inflater, index_selected);
			} else {
				redrawIcons(inflater, Constants.DEFAULT_TAB_VALUE);
			}
		}else {
			redrawIcons(inflater, Constants.DEFAULT_TAB_VALUE);
		}
		if ( Utils.isTablet(activity)) {
			if (bottomNav) {
				((HorizontalScrollView)scrollView).addView(tabsContainer);
			}else {
				((ScrollView)scrollView).addView(tabsContainer);
			}
		}else {
			((HorizontalScrollView)scrollView).addView(tabsContainer);
			
		}
		
		

		
		LinearLayout command = (LinearLayout)view.findViewById(R.id.command);
		
		NumberFormat nf = NumberFormat.getInstance(); // get instance
		nf.setMaximumFractionDigits(2); // set decimal places
		nf.setMinimumFractionDigits(2);
		String s = nf.format(/*mainActivity.total*/0.00);
		totalPriceTV = (TextView)view.findViewById(R.id.cartPrice);
		if (totalPriceTV != null) {
			totalPriceTV.setText(s);
			totalPriceTV.setTypeface(MainActivity.FONT_BODY, Typeface.BOLD);
			totalPriceTV.setTextColor(Color.parseColor("#"+mainActivity.colors.getSide_tabs_foreground_color()));
		}
		
		Application application = null;
		Cart cart = null;
        application = realm.where(Application.class).findFirst();
        //appController.getApplicationDataDao().queryForId(1);
        if(isTablet){
			if (application != null && application.getParameters() != null && !application.getParameters().isShow_cart()) {
				command.setVisibility(View.GONE);
			}else {

                cart = realm.where(Cart.class).findFirst();// appController.getCartDao().queryForId(1);
                if (cart == null) {
					command.setVisibility(View.GONE);
				}
			}
		}else {
			if (application != null && application.getParameters() != null && !application.getParameters().isShow_cart_smartphone()) {
				command.setVisibility(View.GONE);
			}else {

                cart = realm.where(Cart.class).findFirst();
                //appController.getCartDao().queryForId(1);
                if (cart == null) {
					command.setVisibility(View.GONE);
				}
			}
		}
		
		if (cart != null) {
			TextView cartName = (TextView)view.findViewById(R.id.cartName);
			cartName.setText(cart.getTab_title());
			cartName.setTextColor(Color.parseColor("#"+mainActivity.colors.getSide_tabs_foreground_color()));
			ImageView cartImg = (ImageView) view.findViewById(R.id.cartImage);
			bm = null;
			try {
				bm = BitmapFactory.decodeStream(activity.getAssets().open(
						"icon/"+cart.getTab_icon()));
			} catch (IOException e) {
				Log.e("TabFragment", e.getMessage());
				e.printStackTrace();
			}
			mDrawable = new BitmapDrawable(bm);
			mDrawable.setColorFilter(new PorterDuffColorFilter(Color
					.parseColor("#80"
							+ mainActivity.colors
									.getSide_tabs_foreground_color()),
					PorterDuff.Mode.MULTIPLY));
			cartImg.setImageDrawable(mDrawable);
		}
		command.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mainActivity.menu.showMenu();
				AppJsonWriter appJsonWriter = new AppJsonWriter(realm);
				List<CartItem> items = new ArrayList<CartItem>();
                items = realm.where(CartItem.class).findAll();//appController.getCartItemDao().queryForAll();
                appJsonWriter.writeListCartItems(items);
				
			}
		});
		return view;
	}
	
	
	public void redrawIcons(final LayoutInflater inflater, int highlighted) {
		tabsContainer.removeAllViews();
		if (bm != null) {
			bm.recycle();
			bm = null;
			}
		Runtime.getRuntime().gc();
		
		for (int i = 0; i < tabs.size(); i++) {
			final Tab tab = tabs.get(i);
			LinearLayout tabLayout = (LinearLayout)inflater.inflate(R.layout.tab, null, false);
			if (Utils.isTablet(activity)) {
				if (bottomNav) {
					tabLayout  = (LinearLayout)inflater.inflate(R.layout.horizontal_tab_horizontal, null, false);
				}else {
					tabLayout  = (LinearLayout)inflater.inflate(R.layout.tab, null, false);
				}
			}else {
				tabLayout = (LinearLayout)inflater.inflate(R.layout.tab_horizontal, null, false);
			}
			
			tabLayout.setTag(tab);
			tabImg = (ImageView)tabLayout.findViewById(R.id.actionImage);
			bm = null;
			try {
				bm = BitmapFactory.decodeStream(activity.getAssets().open(tab.getIcon()));
			} catch (IOException e) {
				Log.e("TabFragment", e.getMessage());
				e.printStackTrace();
			}
			/*
			 * color the white illumination back image with some color 
			 */
			TextView actionTV = (TextView)tabLayout.findViewById(R.id.actionName);
			actionTV.setText(tab.getTitle());
			actionTV.setMaxLines(3);
			mDrawable = new BitmapDrawable(bm);
			ImageView illumination = (ImageView)tabLayout.findViewById(R.id.illumination);
			if (i==highlighted) { 
				mDrawable.setColorFilter(new PorterDuffColorFilter(Color
						.parseColor("#" + mainActivity.colors.getSide_tabs_foreground_color()), PorterDuff.Mode.MULTIPLY));
				actionTV.setTextColor(Color.parseColor("#"+mainActivity.colors.getSide_tabs_foreground_color()));
				
				Drawable drawableIllumination = activity.getResources().getDrawable(R.drawable.illumin_back);
				drawableIllumination.setColorFilter(new PorterDuffColorFilter(Color.parseColor("#ff"+mainActivity.colors.getSide_tabs_foreground_color()),PorterDuff.Mode.MULTIPLY));
				illumination.setBackgroundDrawable(drawableIllumination);

			}else {
				mDrawable.setColorFilter(new PorterDuffColorFilter(Color
						.parseColor("#80" + mainActivity.colors.getSide_tabs_foreground_color()), PorterDuff.Mode.MULTIPLY));
				actionTV.setTextColor(Color.parseColor("#80"+mainActivity.colors.getSide_tabs_foreground_color()));
				illumination.setBackgroundColor(Color.TRANSPARENT);
			}
			tabImg.setImageDrawable(mDrawable);
			
			
			tabsContainer.addView(tabLayout);
//			if (i<(tabs.size()-1)) {
//				View v = new View(activity);
//				v.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 1));
//				v.setBackgroundColor(Color.parseColor("#80"+mainActivity.colors.getSide_tabs_separators_color()));
//				tabsContainer.addView(v);
//			}
//			
			scrollView.setFocusable(false);
			tabsContainer.setFocusable(false);
			tabLayout.setClickable(true);
			tabLayout.setFocusable(true);
			final int j = i;
			tabLayout.setOnClickListener(new OnClickListener() {
				

				@Override
				public void onClick(View v) {
					seletedTab = tabs.get(j);
					indexSeletedTab = j;
				//	Collections.sort(tabs, new TabComparator());
					tabs.get(0).setIsHomeGrid(true);
					mCallBack.onTabClick(tab, indexSeletedTab);
					for (int j = 0; j < tabsContainer.getChildCount(); j++) {
						tabsContainer.getChildAt(j).setSelected(false);
					}
					v.setSelected(true);
					tabImg = (ImageView)v.findViewById(R.id.actionImage);
					try {
						bm = null;
						bm = BitmapFactory.decodeStream(activity.getAssets().open(tab.getIcon()));
					} catch (IOException e) {
						Log.e("TabFragment", e.getMessage());
						e.printStackTrace();
					}
					redrawIcons(inflater, j);
					
				}
			});
		}
	}


	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
		
		super.onDestroy();
	}


	@Override
	public void notify(String s) {
		if (totalPriceTV != null) {
			totalPriceTV.setText(s);
//			totalPriceTV.setTextColor(Color.parseColor("#"+mainActivity.colors.getSide_tabs_foreground_color()));
		}
		
	}


	@Override
	public void notifyNumber(int nmbr) {
		// TODO Auto-generated method stub
		
	}
	
	

}
