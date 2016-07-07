/**
 * 
 */
package com.euphor.paperpad.activities.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils.TruncateAt;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.euphor.paperpad.R;
import com.euphor.paperpad.activities.main.MainActivity;
import com.euphor.paperpad.activities.main.MyApplication;
import com.euphor.paperpad.Beans.Category;
import com.euphor.paperpad.Beans.Illustration;
import com.euphor.paperpad.Beans.Parameters;
import com.euphor.paperpad.Beans.Parameters_;
import com.euphor.paperpad.Beans.Parameters__;
import com.euphor.paperpad.Beans.Tile;
import com.euphor.paperpad.Beans.Tile_;

import com.euphor.paperpad.utils.Colors;
import com.euphor.paperpad.utils.ResizeAnimation;
import com.euphor.paperpad.utils.Utils;
import com.euphor.paperpad.utils.jsonUtils.AppHit;


import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import io.realm.Realm;
import io.realm.processor.RealmVersionChecker;
import uk.co.chrisjenx.paralloid.views.ParallaxHorizontalScrollView;

/**
 * @author euphordev02
 *
 */
public class HomeGridFragment extends Fragment{

	/**
	 * You'll need this in your class to cache the helper in the class.
	 */


	private Colors colors;
	private boolean bottomNav = false;
	private long time;
	private int id;
	private Timer timer;
    public Realm realm;
	boolean b = true;
    private boolean isTablet;

    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
     */
	@Override
	public void onCreate(Bundle arg0) {

		super.onCreate(arg0);
		setRetainInstance(true);

		listOfAnimateImages = new ArrayList<ImageView>();

		WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();


		width = display.getWidth();  
		height = display.getHeight(); 
		
		if(!Utils.isTablet(getActivity()))
			width = display.getWidth() *2;
		//		PopupBackround backround = new PopupBackround();	
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onDestroy()
	 */
	@Override
	public void onDestroy() {
		Runtime.getRuntime().gc();
		super.onDestroy();
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

        if (((MainActivity) getActivity()).bodyFragment == null) {
			((MainActivity) getActivity()).bodyFragment = "HomeGridFragment";
		}

		Parameters parameters = null;

        parameters = realm.where(Parameters.class).findFirst(); //appController.getParametersDao().queryForId(1);

        if (parameters != null) {
			if (parameters.getNavigation_type()!= null) {
                if (parameters.getNavigation_type().contains("top_wide") || parameters.getNavigation_type().contains("bottom") || parameters.getNavigation_type().contains("top_narrow")) {
					bottomNav = true;
				}else {
					bottomNav = false;
				}
			}else {
				bottomNav = true;
			}
		}

		time = System.currentTimeMillis();
		id = 0;
        isTablet = Utils.isTablet(getActivity());//getResources().getBoolean(R.bool.isTablet);
        super.onAttach(activity);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) { 

		View view = inflater.inflate(R.layout.home_grid, container, false);
		view.setBackgroundColor(colors.getColor(colors.getBackground_color()));
		RelativeLayout rlContainer = new RelativeLayout(getActivity());
		if (isTablet) {
			List<Tile> tiles = new ArrayList<Tile>();
			List<Parameters_> params = new ArrayList<Parameters_>();
			//List<Tablet_home_grid> tabHomeGrid = new ArrayList<Tablet_home_grid>();
            tiles = realm.where(Tile.class).findAll();//appController.getTileDao().queryForAll();
            params = realm.where(Parameters_.class).findAll();//appController.getPrametersTabletDao().queryForAll();


            try {
				Drawable drawable_ = BitmapDrawable.createFromPath(params.get(0).getIllustration().getPath());
				ParallaxHorizontalScrollView scrollView = (ParallaxHorizontalScrollView) view.findViewById(R.id.parallax_scroll_view);
				scrollView.parallaxViewBackgroundBy(scrollView, drawable_, .3f);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			DisplayMetrics dm = new DisplayMetrics();
			getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
			final int deviceHeight;
			final int deviceWidth;
			//			bottomNav = true;
			if (bottomNav) {
				deviceHeight = dm.heightPixels - (int)getResources().getDimension(R.dimen.height_tab_fragment_bottom);
				deviceWidth = dm.widthPixels;
			}else {
				deviceHeight = dm.heightPixels;
				deviceWidth = dm.widthPixels - (int)getResources().getDimension(R.dimen.width_tab_fragment);
			}


			RelativeLayout.LayoutParams pms;
			//			DisplayImageOptions opts = new DisplayImageOptions.Builder().cacheInMemory().cacheOnDisc().build();
			for (Iterator<Tile> iterator = tiles.iterator(); iterator.hasNext();) {
				final Tile tile = (Tile) iterator.next();

				int relWidth = tile.getWidth();
				int relHeight = tile.getHeight();

				pms = new LayoutParams((int)((float)relWidth*(((float)deviceWidth/(float)40))), (int)((float)relHeight*(((float)deviceHeight/(float)30))));
				pms.leftMargin= (int) (((float)tile.getX())*((float)deviceWidth/(float)40));
				pms.topMargin = (int) (((float)tile.getY())*(((float)deviceHeight/(float)30)));


				RelativeLayout imgContainerRL = new RelativeLayout(getActivity());
				imgContainerRL.setBackgroundColor(Color.TRANSPARENT);
				imgContainerRL.setLayoutParams(pms);
				RelativeLayout innerContainer = new RelativeLayout(getActivity());
				LayoutParams innerContainerPms = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				innerContainer.setPadding(params.get(0).getTiles_inner_padding(), params.get(0).getTiles_inner_padding(), params.get(0).getTiles_inner_padding(), params.get(0).getTiles_inner_padding());
				innerContainer.setLayoutParams(innerContainerPms);
				innerContainer.setBackgroundColor(Color.TRANSPARENT);

                Illustration illustration=tile.getIllustration();
				List<Illustration> illustrations = new ArrayList<Illustration>();
				if(illustration!=null)
                illustrations.add(illustration);
				RelativeLayout rel = new RelativeLayout(getActivity());
				ImageView[] imgs = new ImageView[/*tile.getList_images().size()*/illustrations.size()];
				for (int i = 0; i < illustrations.size(); i++) {
					imgs[i] = new ImageView(getActivity());
					LinearLayout linear = new LinearLayout(getActivity());
					if (tile.getType().equals("logo")) {
						imgs[i].setScaleType(ScaleType.FIT_CENTER);
					}else
						imgs[i].setScaleType(ScaleType.CENTER_CROP);


                    Illustration illust = illustrations.get(i);
					if (!illust.getPath().isEmpty()) {
						Glide.with(getActivity()).load(new File(illust.getPath())).into(imgs[i]);
					}else {
						Glide.with(getActivity()).load(illust.getLink()).into(imgs[i]);
					}
					//				Illustration illustration = appController.getIllustrationByLink(imageUrl);
					//					Illustration illustration = tile.getIllustration();
					//					if (illustration!=null) {
					//						if (!illustration.getPath().isEmpty()) {
					//							imageUrl = "file:///" + illustration.getPath();
					//							Glide.with(getActivity()).load(new File(illustration.getPath())).into(imgs[i]);
					//						}
					//					}

					//listOfAnimateImages.add(imgs[i]);

					if(tile.isZoom_animation_effect()) {
						linear.addView(imgs[i], LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                        rel.addView(linear);
                        listOfAnimateImages.add(imgs[i]);
					}else{
						linear.addView(imgs[i], LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
						rel.addView(linear, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
					}
				}



				if(tile.getList_images().size() == 0) {
					ImageView img = new ImageView(getActivity());
					String imageUrl = tile.getImage();
					/*Illustration */illustration = tile.getIllustration();
					if (illustration!=null) {
						if (!illustration.getPath().isEmpty()) {
							imageUrl = "file:///" + illustration.getPath();
							Glide.with(getActivity()).load(new File(illustration.getPath())).into(img);
						}else{
							Glide.with(getActivity()).load((illustration.getLink() == null || illustration.getLink().isEmpty()) ? "/" : illustration.getLink()).into(img);
						}
					}

					if (tile.getType().equals("logo")) {
						img.setScaleType(ScaleType.FIT_CENTER);
					}
					else {
						img.setScaleType(ScaleType.CENTER_CROP);
					}
					LayoutParams pmsImg = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
					img.setLayoutParams(pmsImg);
					innerContainer.addView(img);
				}else {
					innerContainer.addView(rel);
					//innerContainer.addView(kenView);
				}




				if (!tile.getTitle().isEmpty()) {
					//img.setScaleType(ScaleType.CENTER_CROP);
					//a transparent strip that contain the title  
					LinearLayout textStrip = new LinearLayout(getActivity());

					//textStrip.setBackgroundColor(colors.getColor(colors.getBackground_color(),"AA"));

					LayoutParams txtStripPms = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT /*getResources().getDimensionPixelSize(R.dimen.height_tv_home_grid)*/);
					txtStripPms.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
					textStrip.setGravity(Gravity.CENTER);
					textStrip.setLayoutParams(txtStripPms);

					//the title
					TextView titleTV = new TextView(getActivity());
                    titleTV.setTextAppearance(getActivity(), android.R.style.TextAppearance_DeviceDefault_Large);
					titleTV.setTypeface(MainActivity.FONT_TITLE, Typeface.BOLD);
					//					titleTV.setLines(3);
					titleTV.setGravity(Gravity.CENTER);
					titleTV.setText(tile.getTitle());
					titleTV.setPadding(5, 5, 5, 5);
					//titleTV.setTextColor(colors.getColor(colors.getTitle_color()));

					ColorStateList color_txt; 

					StateListDrawable drawable = new StateListDrawable();
					drawable.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(colors.getColor(tile.getTitle_color(), "AA")));
					drawable.addState(new int[]{android.R.attr.state_focused}, new ColorDrawable(colors.getColor(tile.getTitle_color(), "AA")));
					drawable.addState(new int[]{}, new ColorDrawable(colors.getColor(colors.getBackground_color(),"AA")));

					/** Uness Modif **/

					if(colors.isHexColor(tile.getTitle_color())){

						color_txt = new ColorStateList(new int[][]{{android.R.attr.state_pressed},{}}, 
								new int[]{colors.getColor(colors.getBackground_color()), 
								colors.getColor(tile.getTitle_color())});

					} else{
						color_txt = new ColorStateList(new int[][]{{android.R.attr.state_pressed},{}}, 
								new int[]{colors.getColor(colors.getBackground_color()), 
								colors.getColor(colors.getTitle_color())});

					}

					titleTV.setTextColor(color_txt);
					/** End Modif **/  

					//titleTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
					titleTV.setEllipsize(TruncateAt.END);
//					titleTV.setTextSize(getResources().getDimension(R.dimen.home_grid_title_size));
					//					titleTV.setTypeface(, Typeface.BOLD);
					LinearLayout.LayoutParams txtPms = new LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.MATCH_PARENT, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
					txtPms.gravity = Gravity.CENTER;


					textStrip.setBackgroundColor(colors.getColor(colors.getBackground_color(),"AA"));
					textStrip.addView(titleTV,txtPms);
					innerContainer.addView(textStrip);


					if (tile.getCategory_id()>0) {
						textStrip.setBackgroundDrawable(drawable);
						innerContainer.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								Category category = realm.where(Category.class).equalTo("id", tile.getCategory_id()).findFirst();//appController.getCategoryById(tile.getCategory_id());
								((MainActivity) getActivity()).openCategory(category);
							}
						});
					}

					if (tile.getType().equals("logo")) {
						titleTV.setVisibility(View.GONE);
					}

				}



				imgContainerRL.addView(innerContainer);

				rlContainer.addView(imgContainerRL);

			}

		}else {


			List<Tile_> tiles = new ArrayList<Tile_>();
			List<Parameters__> params = new ArrayList<Parameters__>();

            tiles = realm.where(Tile_.class).findAll();//appController.getTilePhoneDao().queryForAll();
            params =realm.where(Parameters__.class).findAll(); //appController.getPrametersPhoneDao().queryForAll();


			ImageView homeGridImg = (ImageView)view.findViewById(R.id.homeGridImg);
			Illustration illustr = params.get(0).getIllustration();
			if(illustr != null) {
				String pathImg = illustr.getPath();
				if (!pathImg.isEmpty()) {
					pathImg = illustr.getPath();
					Glide.with(getActivity()).load(new File(pathImg)).into(homeGridImg);

				} else {
					pathImg = illustr.getLink();
					Glide.with(getActivity()).load(pathImg).into(homeGridImg);
				}
			}

            DisplayMetrics dm = new DisplayMetrics();
			getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
			final int deviceHeight;
			final int deviceWidth;

			//			if (bottomNav) {
			deviceHeight = dm.heightPixels - (int)getResources().getDimension(R.dimen.height_tab_fragment_bottom);
			deviceWidth = dm.widthPixels;
			//			}else {
			//				deviceHeight = dm.heightPixels;
			//				deviceWidth = dm.widthPixels - (int)getResources().getDimension(R.dimen.width_tab_fragment);
			//			}


			RelativeLayout.LayoutParams pms;
			for (Iterator<Tile_> iterator = tiles.iterator(); iterator.hasNext();) {

				final Tile_ tile = (Tile_) iterator.next();

				int relWidth = tile.getWidth();
				int relHeight = tile.getHeight();

                pms = new LayoutParams((int)((float)relWidth*(((float)deviceWidth/(float)20))), (int)((float)relHeight*(((float)deviceHeight/(float)30))));
                pms.leftMargin= (int) (((float)tile.getX())*((float)deviceWidth/(float)20));
                pms.topMargin = (int) (((float)tile.getY())*(((float)deviceHeight/(float)30)));


				RelativeLayout imgContainerRL = new RelativeLayout(getActivity());
				imgContainerRL.setBackgroundColor(Color.TRANSPARENT);
				imgContainerRL.setLayoutParams(pms);
				RelativeLayout innerContainer = new RelativeLayout(getActivity());
				LayoutParams innerContainerPms = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				innerContainer.setPadding(params.get(0).getTiles_inner_padding(), params.get(0).getTiles_inner_padding(), params.get(0).getTiles_inner_padding(), params.get(0).getTiles_inner_padding());
				innerContainer.setLayoutParams(innerContainerPms);
				innerContainer.setBackgroundColor(Color.TRANSPARENT);

				List<Illustration> illustrations = new ArrayList<>() ;
                if(tile.getIllustration() !=null)
                illustrations.add(tile.getIllustration());

				RelativeLayout rel = new RelativeLayout(getActivity());
				ImageView[] imgs = new ImageView[/*tile.getIllustration()*/illustrations.size()];
				for (int i = 0; i < illustrations.size()/*tile.getList_images().size()*/; i++) {
				 /* modifier pr résoudre le blem de tile.getillustration() plusque tile.getList_images().size() */
					imgs[i] = new ImageView(getActivity());
					LinearLayout linear = new LinearLayout(getActivity());
					if (tile.getType().equals("logo")) {
						imgs[i].setScaleType(ScaleType.FIT_CENTER);
					}else
						imgs[i].setScaleType(ScaleType.CENTER_CROP);

					//					LayoutParams pmsImg = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
					//					imgs[i].setLayoutParams(pmsImg);
					//					linear.addView(imgs[i], LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					//					rel.addView(linear);
					//rel.addView(imgs[i]);

					Illustration illust = illustrations.get(i);
					if (illust != null ) {
						if ( !illust.getPath().isEmpty()) {
							Glide.with(getActivity()).load(new File(illust.getPath())).into(imgs[i]);
						}else {
							try {
								Glide.with(getActivity()).load((illust.getLink() == null || illust.getLink().isEmpty()) ? "/" : illust.getLink()).into(imgs[i]);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}

					//				Illustration illustration = appController.getIllustrationByLink(imageUrl);
					//					Illustration illustration = tile.getIllustration();
					//					if (illustration!=null) {
					//						if (!illustration.getPath().isEmpty()) {
					//							imageUrl = "file:///" + illustration.getPath();
					//							Glide.with(getActivity()).load(new File(illustration.getPath())).into(imgs[i]);
					//						}
					//					}

					if(tile.isZoom_animation_effect()) {
						linear.addView(imgs[i], LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
						rel.addView(linear);

						listOfAnimateImages.add(imgs[i]);

					}else{
						linear.addView(imgs[i], LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
						rel.addView(linear, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
					}
				}

				if(illustrations.size() == 0) {
					ImageView img = new ImageView(getActivity());
					String imageUrl = tile.getImage();
					Illustration illustration = tile.getIllustration();
					if (illustration!=null) {
						if (!illustration.getPath().isEmpty()) {
							imageUrl = "file:///" + illustration.getPath();
							Glide.with(getActivity()).load(new File(illustration.getPath())).into(img);
						}else{
							Glide.with(getActivity()).load((illustration.getLink() == null || illustration.getLink().isEmpty()) ? "/" : illustration.getLink()).into(img);
						}
					}

					if (tile.getType().equals("logo")) {
						img.setScaleType(ScaleType.FIT_CENTER);
					}
					else {
						img.setScaleType(ScaleType.CENTER_CROP);
					}
					LayoutParams pmsImg = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
					img.setLayoutParams(pmsImg);
					innerContainer.addView(img);
				}else {
					innerContainer.addView(rel);
					//innerContainer.addView(kenView);
				}

				if (!tile.getTitle().isEmpty()) {
					//img.setScaleType(ScaleType.CENTER_CROP);
					//a transparent strip that contain the title  
					LinearLayout textStrip = new LinearLayout(getActivity());

					//textStrip.setBackgroundColor(colors.getColor(colors.getBackground_color(),"AA"));

					LayoutParams txtStripPms = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT /*getResources().getDimensionPixelSize(R.dimen.height_tv_home_grid)*/);
					txtStripPms.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
					textStrip.setGravity(Gravity.CENTER);
					textStrip.setLayoutParams(txtStripPms);

					//the title
					TextView titleTV = new TextView(getActivity());
                    titleTV.setTextAppearance(getActivity(), android.R.style.TextAppearance_DeviceDefault_Large);
					titleTV.setTypeface(MainActivity.FONT_TITLE, Typeface.BOLD);
					//					titleTV.setLines(3);
					titleTV.setGravity(Gravity.CENTER);
					titleTV.setText(tile.getTitle());
					titleTV.setPadding(5, 5, 5, 5);
					//titleTV.setTextColor(colors.getColor(colors.getTitle_color()));

					ColorStateList color_txt; 

					StateListDrawable drawable = new StateListDrawable();
					drawable.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(colors.getColor(tile.getTitle_color(), "AA")));
					drawable.addState(new int[]{android.R.attr.state_focused}, new ColorDrawable(colors.getColor(tile.getTitle_color(), "AA")));
					drawable.addState(new int[]{}, new ColorDrawable(colors.getColor(colors.getBackground_color(),"AA")));

					/** Uness Modif **/

					if(colors.isHexColor(tile.getTitle_color())){

						color_txt = new ColorStateList(new int[][]{{android.R.attr.state_pressed},{}}, 
								new int[]{colors.getColor(colors.getBackground_color()), 
								colors.getColor(tile.getTitle_color())});

					} else{
						color_txt = new ColorStateList(new int[][]{{android.R.attr.state_pressed},{}}, 
								new int[]{colors.getColor(colors.getBackground_color()), 
								colors.getColor(colors.getTitle_color())});

					}

					titleTV.setTextColor(color_txt);
					/** End Modif **/  

					titleTV.setEllipsize(TruncateAt.MARQUEE);
					/** End Modif **/  

					//titleTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
					//					titleTV.setTypeface(, Typeface.BOLD);
					LinearLayout.LayoutParams txtPms = new LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.MATCH_PARENT, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
					txtPms.gravity = Gravity.CENTER;

					textStrip.setBackgroundColor(colors.getColor(colors.getBackground_color(),"AA"));
					textStrip.addView(titleTV,txtPms);
					innerContainer.addView(textStrip);


					if (tile.getCategory_id()>0) {
						textStrip.setBackgroundDrawable(drawable);
						innerContainer.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								Category category = realm.where(Category.class).equalTo("id",tile.getCategory_id()).findFirst();
								//appController.getCategoryById(tile.getCategory_id());
								((MainActivity) getActivity()).openCategory(category);
							}
						});
					}
					if (tile.getType().equals("logo")) {
						titleTV.setVisibility(View.GONE);
					}

				}



				imgContainerRL.addView(innerContainer);

				rlContainer.addView(imgContainerRL);
			}
		}


		//		rlContainer.setPadding(params.get(0).getTiles_outer_margin(), params.get(0).getTiles_outer_margin(), params.get(0).getTiles_outer_margin(), params.get(0).getTiles_outer_margin());
		LinearLayout tilesContainer = (LinearLayout)view.findViewById(R.id.tilesContainer);
		LinearLayout.LayoutParams paramsRLContainer = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		tilesContainer.addView(rlContainer, paramsRLContainer);
		//		setContentView(rlContainer);
		return view;
	}

	long animation_clock = 5000;

	float  MIN_RATIO = 0.7f;
	//	float  IMAGE_SIZE_RATIO = 1.2f;
	//float  ZOOM_RATIO = 1.4f;

	float width, height;

	List<ImageView> listOfAnimateImages;

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {

		//		if(listOfAnimateImages.size() > 0){
		//		
		//			if(timer == null)
		//		timer = new Timer();
		//
		//		timer.scheduleAtFixedRate(new TimerTask() {
		//			@Override
		//			public void run() {
		//				//extracted(image, animRes);
		//				getActivity().runOnUiThread(new Runnable() {
		//					
		//					@Override
		//					public void run() {
		//						Random r = new Random();
		//						float p = (float) (r.nextInt(3) + 7) / 10.0f;
		//						if(b){
		//							for (int i = 0; i < listOfAnimateImages.size() - 1; i += 2) {
		////								width = listOfAnimateImages.get(i).getWidth();
		////								height = listOfAnimateImages.get(i).getHeight();
		//					    		_crossfadeLeft(listOfAnimateImages.get(i), listOfAnimateImages.get(i + 1), p);
		// 
		//							}
		//							b = false;
		//						}else{
		//							for (int i = 0; i < listOfAnimateImages.size() - 1; i += 2) {
		////								width = listOfAnimateImages.get(i + 1).getWidth();
		////								height = listOfAnimateImages.get(i + 1).getHeight();
		//					    		_crossfadeRight(listOfAnimateImages.get(i + 1), listOfAnimateImages.get(i), p);
		//							}
		//							b = true;
		//						}		    		
		//						
		//						
		//					}
		//				});
		//				//crossfade(image1, image1));
		//    	    		//crossfade(imgs[i+1], imgs[i]);
		//
		//    		
		//			}
		//		}, 0 , animation_clock);
		//		}
		super.onViewCreated(view, savedInstanceState);
	}

	//	private void crossfade(ImageView image_1, ImageView image_2){
	//
	//		width = 1000; //image_1.getWidth();
	//		height = 800; //image_1.getHeight();
	//						
	//			if((image_1 != null && image_2 != null) && image_2.getVisibility() == View.VISIBLE){
	//	    		_crossfadeLeft(image_1, image_2);
	//	    	}
	//	    	else if((image_1 != null && image_2 != null) ){
	//	    		_crossfadeRight(image_2, image_1);
	//	    	}
	//
	//	}

	@SuppressLint("NewApi")
	private void _crossfadeLeft(View fadeIn, final View fadeOut, float p) {

		// Set the content view to 0% opacity but visible, so that it is visible
		// (but fully transparent) during the animation.
		//Log.i("FadeIn", "FadeOut alpha: " + String.valueOf(fadeOut.getAlpha()));
		fadeIn.setAlpha(0f);
		fadeIn.setVisibility(View.VISIBLE);

		// Animate the content view to 100% opacity, and clear any animation
		// listener set on the view.
		fadeIn.animate()
		.alpha(1f)
		.setDuration(1000)
		.setListener(null).start();


		ResizeAnimation anim = new ResizeAnimation(fadeIn, width * MIN_RATIO - 200, height * p , width  , height * MIN_RATIO);
		anim.setDuration(animation_clock - 50);
		//image_1.setAnimation(anim);
		fadeIn.startAnimation(anim);

		//Log.i("FadeOut", "FadeIn alpha: " + String.valueOf(fadeIn.getAlpha()));
		fadeOut.animate()
		.alpha(0f)
		.setDuration(1000)
		.setListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				fadeOut.setVisibility(View.GONE);
				fadeOut.setScaleX((float)1.20);
				fadeOut.setScaleY((float)1);
			}
		}).start();
	}

	@SuppressLint("NewApi")
	private void _crossfadeRight(View fadeIn, final View fadeOut, float p) {

		// Set the content view to 0% opacity but visible, so that it is visible
		// (but fully transparent) during the animation.

		//Log.i("FadeIn", "FadeOut alpha: " + String.valueOf(fadeOut.getAlpha()));
		fadeIn.setAlpha(0f);
		fadeIn.setVisibility(View.VISIBLE);

		// Animate the content view to 100% opacity, and clear any animation
		// listener set on the view.
		fadeIn.animate()
		.alpha(1f)
		.setDuration(1000)
		.setListener(null).start();

		ResizeAnimation anim = new ResizeAnimation(fadeIn,width  , height * MIN_RATIO , width * MIN_RATIO - 200, height * p);
		anim.setDuration(animation_clock - 50);
		fadeIn.startAnimation(anim);


		//Log.i("FadeOut", "FadeIn alpha: " + String.valueOf(fadeIn.getAlpha()));
		fadeOut.animate()
		.alpha(0f)
		.setDuration(1000)
		.setListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				fadeOut.setVisibility(View.GONE);
				fadeOut.setScaleX((float)1);
				fadeOut.setScaleY((float)1.10);
			}
		}).start();
	}

	@SuppressLint("NewApi")
	private void _crossfadeTop(View fadeIn, final View fadeOut, float p) {

		// Set the content view to 0% opacity but visible, so that it is visible
		// (but fully transparent) during the animation.

		//Log.i("FadeIn", "FadeOut alpha: " + String.valueOf(fadeOut.getAlpha()));
		fadeIn.setAlpha(0f);
		fadeIn.setVisibility(View.VISIBLE);

		// Animate the content view to 100% opacity, and clear any animation
		// listener set on the view.
		fadeIn.animate()
		.alpha(1f)
		.setDuration(1000)
		.setListener(null).start();

		ResizeAnimation anim = new ResizeAnimation(fadeIn, width *  MIN_RATIO - 100, height , width * MIN_RATIO - 100, height * MIN_RATIO - 100);
		anim.setDuration(animation_clock - 50);
		fadeIn.startAnimation(anim);


		//Log.i("FadeOut", "FadeIn alpha: " + String.valueOf(fadeIn.getAlpha()));
		fadeOut.animate()
		.alpha(0f)
		.setDuration(1000)
		.setListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				fadeOut.setVisibility(View.GONE);
				fadeOut.setScaleX((float)0.90);
				fadeOut.setScaleY((float)1);
			}
		}).start();
	}

	@Override
	public void onResume() {
		if(listOfAnimateImages.size() > 0){
			//if(timer == null)
				timer = new Timer();

			timer.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {
					//extracted(image, animRes);
					getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {
							Random r = new Random();
							float p = (float) (r.nextInt(3) + 7) / 10.0f;
							if(b){
								for (int i = 0; i < listOfAnimateImages.size() - 1; i += 2) {
									_crossfadeLeft(listOfAnimateImages.get(i), listOfAnimateImages.get(i + 1), p);

								}
								b = false;
							}else{
								for (int i = 0; i < listOfAnimateImages.size() - 1; i += 2) {
									_crossfadeRight(listOfAnimateImages.get(i + 1), listOfAnimateImages.get(i), p);
								}
								b = true;
							}		    		


						}
					});
					//crossfade(image1, image1));
					//crossfade(imgs[i+1], imgs[i]);


				}
			}, 0 , animation_clock);
		}

		super.onResume();
	}

	@Override
	public void onPause() {
		if(timer != null)
			timer.cancel();
		super.onPause();
	}
	@Override
	public void onStop() {
		AppHit hit = new AppHit(System.currentTimeMillis()/1000, time/1000, "sales_home", id);
		((MyApplication)getActivity().getApplication()).hits.add(hit);

		super.onStop();
	}

	private Handler handler = new Handler();


	public void extracted(final ImageView img, final int animRes){
		//	Random rd = new Random();
		//	int i = rd.nextInt(illustrations.size());
		//	final ImagePage illust = illustrations.get(i);

		//		Handler handler = new Handler();
		handler.post(new Runnable() {	
			@Override
			public void run() {
				//
				Animation grow;
				//				 Random rand = new Random();
				//				 	int animResRand = 2;
				//					if(animRes > 0)
				//						animResRand = rand.nextInt(3);
				if(getActivity() != null) {
					if(animRes == 0) {
						grow = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_home_grid_from_top);
						//grow.setDuration(4000);
					}

					else if( animRes == 1) {
						grow = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_home_grid_from_bottomright_to_topleft);
						//grow.setDuration(2000);
					}
					else if( animRes == 2) {
						grow = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_home_grid_from_bottomleft_to_topright);
						//grow.setDuration(2000);
					}
					else {
						grow = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_home_grid_from_topleft_to_bottomright);
						//grow.setDuration(7000);
					}
					grow.setDuration(4000);
					img.startAnimation(grow);
				}
			}

		});

	}


}
