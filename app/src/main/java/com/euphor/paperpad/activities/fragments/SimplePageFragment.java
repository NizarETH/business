package com.euphor.paperpad.activities.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.euphor.paperpad.Beans.MyInteger;
import com.euphor.paperpad.Beans.MyString;
import com.euphor.paperpad.R;
import com.euphor.paperpad.activities.main.MainActivity;
import com.euphor.paperpad.activities.main.MyApplication;
import com.euphor.paperpad.activities.main.YoutubePlayerActivity;

import com.euphor.paperpad.Beans.CartItem;
import com.euphor.paperpad.Beans.Category;
import com.euphor.paperpad.Beans.Child_pages;
import com.euphor.paperpad.Beans.Contact;
import com.euphor.paperpad.Beans.Illustration;
import com.euphor.paperpad.adapters.FormulePagesAdapter;

import com.euphor.paperpad.Beans.Link;
import com.euphor.paperpad.Beans.Linked;
import com.euphor.paperpad.Beans.Parameters;
import com.euphor.paperpad.Beans.Price;
import com.euphor.paperpad.Beans.Related;

import com.euphor.paperpad.utils.Colors;
import com.euphor.paperpad.utils.RelatedItem1;
import com.euphor.paperpad.utils.RelatedTitle1;
import com.euphor.paperpad.utils.Utils;
import com.euphor.paperpad.utils.jsonUtils.AppHit;
import com.euphor.paperpad.widgets.AViewFlipper;
import com.euphor.paperpad.widgets.ArrowImageView;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;

public class SimplePageFragment extends Fragment implements FormulePagesAdapter.CallbackRelatedLinks {


	//	private static int WIDTH_SWIPE_PICS = 600;
	//	private static String ALPHA = "CC";
	private String design;

	private int page_id;
	private Child_pages page;
	//	private ImageLoader imageLoader;
	private Colors colors;



	private StateListDrawable stateListDrawable;
	protected LayoutInflater layoutInflater;
	//	private PopupWindow pw;
	private View view;
	private LinearLayout llRelated;
	private MainActivity mainActivity;
	private String path;
	private Illustration illust;
	private Parameters parameters;
	//	private boolean isTablet;
	protected AlertDialog alertDialog;
    public Realm realm;

	private int current;
	private AViewFlipper viewFlipper;
	private int downX,upX;


	/**
	 * 
	 */
	public SimplePageFragment() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onResume() {
		super.onResume();
		final View viewCreated = view; //getSupportFragmentManager().findFragmentById(R.id.map).getView();
		if (viewCreated.getViewTreeObserver().isAlive()) {
		    viewCreated.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
		        @Override
		        public void onGlobalLayout() {

		            viewCreated.getViewTreeObserver().removeGlobalOnLayoutListener(this);
		            
		            


		            Log.e("   Screen Point by Projection : ", " <<>>  ");
		            }
		        }
		    );
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
	//new AppController(getActivity());

        		realm = Realm.getInstance(getActivity());
		colors = ((MainActivity)activity).colors;
        Parameters ParamColor = realm.where(Parameters.class).findFirst();
        if (colors==null) {
            colors = new Colors(ParamColor);
        }

		mainActivity = (MainActivity)activity;
		//		isTablet = getResources().getBoolean(R.bool.isTablet);

		super.onAttach(activity);
	}

	//	@Override
	//	public void onDetach() {
	//		mCallbacks = mDetailCallbacks;
	//		Log.i(" NavItemDetailFragment <==== onDetach " , ""+mCallbacks);
	//
	//		super.onDetach();
	//	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		//setRetainInstance(true);
		super.onCreate(savedInstanceState);

		page_id = getArguments().getInt("page_id");
		if(((MainActivity) getActivity()).extras != null)
		((MainActivity) getActivity()).extras.putInt("page_id", page_id);
		//		((MainActivity) getActivity()).bodyFragment = "SplitListFragment";
		time = System.currentTimeMillis();
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		//view = new View(getActivity());		

        page = realm.where(Child_pages.class).equalTo("id_cp",page_id).findFirst();
        //appController.getChildPageDao().queryForId(page_id);


        design = "horizontal";


        view = inflater.inflate(R.layout.horizontal_for_split, container, false);
        view.setBackgroundColor(colors.getColor(colors.getBackground_color()));
        if(view.findViewById(R.id.navigationHolderForStrokePort) != null && (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT))
            view.findViewById(R.id.navigationHolderForStrokePort).setVisibility(View.GONE);
        else
            view.findViewById(R.id.Category).setVisibility(View.GONE);


        view.findViewById(R.id.navigationHolderForStroke).setVisibility(View.GONE);

			/*ALPHA = "FF";*/

        //SplitListCategoryFragment.list.setOnScrollListener(scrollListener);


			/*
			 * COMMUN TASKS
			 */
        //Title product
        TextView titleTV = (TextView)view.findViewById(R.id.titleProductTV);
        titleTV.setTypeface(MainActivity.FONT_TITLE);
        titleTV.setText(page.getTitle());

        //		titleTV.setTextAppearance(getActivity(), android.R.style.TextAppearance_Large);
        titleTV.setTextColor(colors.getColor(colors.getTitle_color()));
        titleTV.setGravity(Gravity.CENTER);
        //		if (design.equals("formule")) {
        //			titleTV.setGravity(Gravity.CENTER);
        //		}
        //		titleTV.setTextSize(34);
        view.findViewById(R.id.titleProductHolder).setBackgroundColor(colors.getColor(colors.getBackground_color(),"FF"/* ALPHA*/));

        //Image Product
        final RealmList<Illustration> images = new RealmList<>();

        images.add(page.getIllustration());


        if (page.getExtra_fields() == null || (page.getExtra_fields() != null && page.getExtra_fields().getCombine_images_animation() == null || (page.getExtra_fields().getCombine_images_animation() != null && page.getExtra_fields().getCombine_images_animation().isEmpty() )) && images.size()>0 && (ViewFlipper)view.findViewById(R.id.viewFlipper)!= null) {

            view.findViewById(R.id.ImageProductHolder).setVisibility(View.VISIBLE);
            viewFlipper = (AViewFlipper) view.findViewById(R.id.viewFlipper);

            //viewFlipper.setVisibility(View.VISIBLE);

            current = 0;


            for(Iterator<Illustration> iterator = images.iterator(); iterator.hasNext();){

                Illustration imagePage = iterator.next();
                illust = imagePage;//page.getIllustration();

                ImageView imageView = new ImageView(getActivity().getApplicationContext());
                imageView.setScaleType(ScaleType.CENTER_CROP);
                imageView.setLayoutParams(new ImageSwitcher.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));

                viewFlipper.addView(imageView);

                if (!illust.getPath().isEmpty()) {
                    path = illust.getPath();
                    Glide.with(getActivity()).load(new File(path)).into(imageView);

                }else {
                    path = illust.getLink();
                    Glide.with(getActivity()).load(path).into(imageView);
                }

                    current++;


            }


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

                        //				    	 if (gestureDetector.onTouchEvent(event))
                        //				             return true;
                        //				         else
                        //				             return false;

                    }



                });


        }
//			else{
//				view.findViewById(R.id.ImageProductHolder).setVisibility(View.GONE);
//			}
        else
            if (images.size()>0 && (ImageView)view.findViewById(R.id.ProductIMG) != null) {

                Animation fadeOutLeftAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_in_left);
                Animation fadeOutRightAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_in_right);
                Animation fadeOutTopAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out_top);


                view.findViewById(R.id.ImageProductHolder).setVisibility(View.VISIBLE);
                view.findViewById(R.id.ProductIMG).setVisibility(View.VISIBLE);
                view.findViewById(R.id.viewFlipper).setVisibility(View.GONE);
            /**  Uness Modif **/
                RelativeLayout relativeView = (RelativeLayout) view.findViewById(R.id.ImageProductHolder);
                relativeView.setVisibility(View.VISIBLE);

            //				Log.e("  image Switcher ","  ==> "+viewFlipper);



            ImageView imageView = (ImageView)view.findViewById(R.id.ProductIMG);
            imageView.setScaleType(ScaleType.CENTER_CROP);
            imageView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

            int i = 0;

            for(Iterator<Illustration> iterator = images.iterator(); iterator.hasNext();){


                Illustration imagePage = iterator.next();

                illust = imagePage;

                // new ImageView(getActivity().getApplicationContext());

                if(i == images.size() - 1) {
//						}
                    break;
                }

                else if(i == 0) {
                    imageView.startAnimation(fadeOutTopAnimation);
                }
                if(images.size() > 2 && i > 0) {


//						ImageView img = new ImageView(getActivity().getApplicationContext());
//						img.setScaleType(ImageView.ScaleType.CENTER_CROP);
//						img.setLayoutParams(new ImageSwitcher.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
//



                    ImageView[] arrayofImages = new ImageView[images.size()];
                    for (int j = 0; j < images.size(); j++) {

                        arrayofImages[j] = new ImageView(getActivity());



                        arrayofImages[j].setId(j);
                        if (j != 0) {
                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                            params.addRule(RelativeLayout.RIGHT_OF, arrayofImages[j - 1].getId());
                            // arrayofImages[i].setLayoutParams(params);
                            relativeView.addView(arrayofImages[j], params);
                        } else {
                            relativeView.addView(arrayofImages[j]);
                        }

                        if (illust != null && !illust.getPath().isEmpty()) {
                            path = illust.getPath();
                            Glide.with(getActivity()).load(new File(path)).into(arrayofImages[j]);
                        }else {
                            path = illust.getLink();
                            Glide.with(getActivity()).load(path).into(arrayofImages[j]);
                        }


                        if(j % 2 == 0) {
                            arrayofImages[j].startAnimation(fadeOutRightAnimation);
                        }
                        else if( j % 3 == 0) {
                            arrayofImages[j].startAnimation(fadeOutTopAnimation);
                        }
                        else if(j % 5 == 0 || j == 1) {
                            arrayofImages[j].startAnimation(fadeOutLeftAnimation);
                        }


                    }



//						int wid = img.getWidth();
//						int hgt = img.getHeight();

                    /**  Test 1 **/
                    //Resources r = getResources();
//						Drawable[] layers = new Drawable[2];
//						layers[0] = imageView.getDrawable(); //r.getDrawable(R.drawable.t);
//						layers[1] = img.getDrawable(); //r.getDrawable(R.drawable.tt);
//						LayerDrawable layerDrawable = new LayerDrawable(layers);
//						imageView.setImageDrawable(layerDrawable);

                    /** Test 3 **/
//						Canvas canvas = new Canvas();
//						Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
//						canvas.setBitmap(bitmap);
//						Bitmap bitmap2 = ((BitmapDrawable)img.getDrawable()).getBitmap();
//						canvas.drawBitmap(bitmap2, new Matrix(), null);

                    /** **/


                    /** Test 4 **/
//						Bitmap newImage = Bitmap.createBitmap
//								(wid, hgt, Bitmap.Config.ARGB_8888);
//
//						Canvas canvas = new Canvas(newImage);
//
//						canvas.drawBitmap(img.getDrawingCache(), 0f, 0f, null);
//
//						Drawable drawable = img.getDrawable(); //getResources().(R.drawable.vmlogo1);
//						drawable.setBounds(20, 20, 260, 160);
//						drawable.draw(canvas);
//						imageView.setBackground(drawable);

                }else if(images.size() > 0) {


                    //imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    //imageView.setLayoutParams(new ImageSwitcher.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));


                    if (!illust.getPath().isEmpty()) {
                        path = illust.getPath();
                        Glide.with(getActivity()).load(new File(path)).into(imageView);

                    }else {
                        path = illust.getLink();
                        Glide.with(getActivity()).load(path).into(imageView);
                    }








                }
                i++;

            }

        }
        else{
            view.findViewById(R.id.ImageProductHolder).setVisibility(View.GONE);
        }


        // Arrows next and previous coloring and actions !
        //			Category category = page.getCategory();
        //			category = appController.getCategoryDao().queryForId(category.getId_category());
        //			Collection<Child_pages> pagesTmp = category.getChildren_pages1();
        //
        //			//category parent
        //			RelativeLayout navigationHolder = (RelativeLayout)view.findViewById(R.id.navigationHolder);
        //			TextView categoryTv = (TextView)view.findViewById(R.id.categorieTV);
        //
        //
        //			categoryTv.setText(category.getTitle());
        //			categoryTv.setTextColor(colors.getColor(colors.getForeground_color()));
        //			LinearLayout categoryHolder = (LinearLayout)view.findViewById(R.id.Category);
        //			categoryHolder.setBackgroundColor(colors.getColor(colors.getBackground_color(), "66"));
        //			categoryId = category.getId();
        //			categoryHolder.setOnClickListener(new OnClickListener() {
        //
        //				@Override
        //				public void onClick(View v) {
        //					Category category = appController.getCategoryById(categoryId);
        //
        //					((MainActivity)getActivity()).openCategory(category);
        //
        //
        //				}
        //			});


        //button zoom
        ImageView iconZoom = (ImageView)view.findViewById(R.id.zoomIMG);
        Drawable drawZoom = getResources().getDrawable(R.drawable.icon_0_26);
        drawZoom.setColorFilter(new PorterDuffColorFilter(colors.getColor(colors.getForeground_color()) , Mode.MULTIPLY));
        iconZoom.setImageDrawable(drawZoom);
        TextView textZoom = (TextView)view.findViewById(R.id.tvGalery);
        textZoom.setTextColor(colors.getColor(colors.getForeground_color()));
        LinearLayout galeryHolder = (LinearLayout)view.findViewById(R.id.galeryHolder);
        galeryHolder.setBackgroundColor(colors.getColor(colors.getBackground_color()/*, *ALPHA*/));
        galeryHolder.setVisibility(View.GONE);
			/*galeryHolder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				((MainActivity) getActivity()).extras = new Bundle();
				((MainActivity) getActivity()).extras.putInt("whereToScroll", illust.getId() );
				((MainActivity) getActivity()).bodyFragment = "GaleryFragment";
				GaleryFragment galeryFragment = new GaleryFragment();
				galeryFragment.setArguments(((MainActivity) getActivity()).extras);
				getActivity().getSupportFragmentManager().beginTransaction()
				.replace(R.id.fragment_container, galeryFragment).addToBackStack(null).commit();

			}
		});*/

        //share button
        //			LinearLayout shareHolder = (LinearLayout)view.findViewById(R.id.shareHolder);
        //			shareHolder.setOnClickListener(new OnClickListener() {
        //
        //				@Override
        //				public void onClick(View v) {
        //					//				String path = "/mnt/sdcard/dir1/sample_1.jpg";
        //					Intent share = new Intent(Intent.ACTION_SEND);
        //					MimeTypeMap map = MimeTypeMap.getSingleton(); //mapping from extension to mimetype
        //					String ext = path.substring(path.lastIndexOf('.') + 1);
        //					String mime = map.getMimeTypeFromExtension(ext);
        //					share.setType(mime); // might be text, sound, whatever
        //					Uri uri;
        //					if (path.contains("Paperpad/")) {
        //						uri = Uri.fromFile(new File(path));
        //					}else {
        //						uri =  Uri.parse(path);
        //					}
        //					share.putExtra(Intent.EXTRA_SUBJECT,page.getTitle());
        //					share.putExtra(Intent.EXTRA_TEXT,page.getIntro());
        //					share.putExtra(Intent.EXTRA_STREAM,uri);//using a string here didnt work for me
        //					Log.d("", "share " + uri + " ext:" + ext + " mime:" + mime);
        //					startActivity(Intent.createChooser(share, "share"));
        //
        //				}
        //			});


        /**  New Modif  **/


        /** end **/
        // Long description under the product image
        WebView longdescWV = (WebView)view.findViewById(R.id.LongDescWV);
        StringBuilder htmlString = new StringBuilder();
        int[] colorText = Colors.hex2Rgb(colors.getTitle_color());
        longdescWV.setBackgroundColor(Color.TRANSPARENT);
        if (view.findViewById(R.id.tempLL) != null) {
            view.findViewById(R.id.tempLL).setBackgroundColor(colors.getColor(colors.getBackground_color()/*, ALPHA*/));
        }else {
            longdescWV.setBackgroundColor(colors.getColor(colors.getBackground_color()/*, ALPHA*/));
        }
        //		int sizeText = getResources().getDimensionPixelSize(R.dimen.priceTV);
        //		int sizeText = (int) Utils.spToPixels(getActivity(), (float) 14);

        //			sizeText = (int) Utils.spToPixels(getActivity(), (float) 16);


        htmlString.append(Utils.paramBodyHTML(colorText));
        //			String body = page.getBody().replace("[break]", "");
        String body = page.getBody();
        if (body.contains("[break]")) {
            String[] bodies = body.split("(\\[)break(\\])");
            body = bodies[0].replace("(/[break])", "");
            ImageView truncateIcon = (ImageView) view.findViewById(R.id.truncate_icon);
            if (truncateIcon != null) {
                view.findViewById(R.id.truncate_holder).setVisibility(View.VISIBLE);
                truncateIcon.setVisibility(View.VISIBLE);
                truncateIcon.getDrawable().setColorFilter(new PorterDuffColorFilter(colors.getColor(colors.getTitle_color(), "88"), Mode.MULTIPLY));
                truncateIcon.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        int[] colorText = Colors.hex2Rgb(colors.getBody_color());
                        AlertDialog.Builder builder;

                        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View layout = inflater.inflate(R.layout.dialog_truncate_body,null,false);
                        layout.setBackgroundColor(colors.getColor(colors.getBackground_color()));

                        ImageView image = (ImageView) layout.findViewById(R.id.truncate_body_icon);
                        image.getDrawable().setColorFilter(new PorterDuffColorFilter(colors.getColor(colors.getTitle_color(), "88"), Mode.MULTIPLY));

                        WebView completeBodyWB = (WebView)layout.findViewById(R.id.LongDescWV_truncate);
                        completeBodyWB.setBackgroundColor(Color.TRANSPARENT);
                        StringBuilder htmlStringTruncate = new StringBuilder();
                        htmlStringTruncate.append(Utils.paramBodyHTML(colorText));
                        String bodyComplet = page.getBody().replace("[break]", "");
                        htmlStringTruncate.append(bodyComplet );
                        htmlStringTruncate.append("</div></body></html>");
                        completeBodyWB.loadDataWithBaseURL(null, htmlStringTruncate.toString(), "text/html", "UTF-8", null);

                        builder = new AlertDialog.Builder(getActivity());
                        builder.setView(layout);
                        alertDialog = builder.create();
                        alertDialog.show();
                        image.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                alertDialog.dismiss();

                            }
                        });
                    }
                });
            }
        }
        htmlString.append(body);

        //			if(view.findViewById(R.id.divider)!=null)
        //				view.findViewById(R.id.divider).setBackgroundColor(colors.getColor(colors.getForeground_color()));
        //			if(view.findViewById(R.id.divider2)!=null)
        //				view.findViewById(R.id.divider2).setBackgroundColor(colors.getColor(colors.getForeground_color()));
        //			if(view.findViewById(R.id.divider3)!=null)
        //				view.findViewById(R.id.divider3).setBackgroundColor(colors.getColor(colors.getForeground_color()));
        //			if(view.findViewById(R.id.divider4)!=null)
        //				view.findViewById(R.id.divider4).setBackgroundColor(colors.getColor(colors.getForeground_color()));
        //			if(view.findViewById(R.id.divider5)!=null)
        //				view.findViewById(R.id.divider5).setBackgroundColor(colors.getColor(colors.getForeground_color()));
        //			if(view.findViewById(R.id.divider6)!=null)
        //				view.findViewById(R.id.divider6).setBackgroundColor(colors.getColor(colors.getForeground_color()));
        //			Drawable cadreDrawable = getResources().getDrawable(R.drawable.cadre);
        //			cadreDrawable.setColorFilter(new PorterDuffColorFilter(colors.getColor(colors.getForeground_color()) ,PorterDuff.Mode.MULTIPLY));
        //			view.findViewById(R.id.navigationHolderForStroke).setBackgroundDrawable(cadreDrawable);

        //			shareHolder.setBackgroundColor(colors.getColor(colors.getBackground_color(), "66"));
        //			LinearLayout portNavigationHolderForStroke = (LinearLayout)view.findViewById(R.id.navigationHolderForStrokePort);
        //			if (portNavigationHolderForStroke != null) {
        //				portNavigationHolderForStroke.setBackgroundDrawable(cadreDrawable);
        //				view.findViewById(R.id.dividerExtra).setBackgroundColor(colors.getColor(colors.getForeground_color()));
        //				view.findViewById(R.id.dividerExtra1).setBackgroundColor(colors.getColor(colors.getForeground_color()));
        //				view.findViewById(R.id.navigationHolderPort).setBackgroundColor(colors.getColor(colors.getBackground_color()/*,ALPHA*/));
        //			}

        htmlString.append("</div></body></html>");
        WebSettings webSettings = longdescWV.getSettings();
        webSettings.setTextSize(WebSettings.TextSize.NORMAL);

        longdescWV.loadDataWithBaseURL(null, htmlString.toString(), "text/html", "UTF-8", null);

        //prices related to this product
        parameters = null;
        parameters = realm.where(Parameters.class).findFirst();
        //appController.getParametersDao().queryForId(1);

        RealmList<Price> prices = page.getPrices();
        LinearLayout pricesContainer = (LinearLayout)view.findViewById(R.id.PricesHolder);
        if (prices.size()>0 /*&& !design.equals("formule")*/) {

            for (Iterator<Price> iterator = prices.iterator(); iterator.hasNext();) {
                Price price = (Price) iterator.next();
                View priceElement = inflater.inflate(R.layout.price, container, false);
                LinearLayout btnPrice = (LinearLayout)priceElement.findViewById(R.id.btnPrice);
                Drawable drawable = btnPrice.getBackground();
                drawable.setColorFilter(colors.getColor(colors.getBody_color(), "55"), Mode.MULTIPLY);
                btnPrice.setBackgroundDrawable(drawable);
                TextView label = (TextView)priceElement.findViewById(R.id.priceLabelTV);
                label.setTypeface(MainActivity.FONT_BODY);
                if (price!=null && price.getLabel()!=null && !price.getLabel().isEmpty()) {
                    label.setText(price.getLabel());
                    label.setTextColor(colors.getColor(colors.getBody_color()));
                }else {
                    label.setVisibility(View.GONE);
                }
                TextView value = (TextView)priceElement.findViewById(R.id.priceValueTV);
                value.setTypeface(MainActivity.FONT_BODY);
                value.setText((price!=null && price.getAmount()!=null)?price.getAmount()+price.getCurrency():"");
                value.setTextColor(colors.getColor(colors.getTitle_color()));
                priceElement.setTag(price);

                if (parameters != null) {
                    if (!parameters.isShow_cart()) {
                        priceElement.findViewById(R.id.imgPlus).setVisibility(View.GONE);
                    }
                }

                priceElement.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {


                        if (parameters != null) {
                            if (parameters.isShow_cart()) {

                                Category category = null;
                                if (page.getCategory() != null && page.getCategory().isNeeds_stripe_payment()) {
                                    category = page.getCategory();
                                }else {

                            //category = controller.getCategoryById(page.getCategory_id());
                            category=realm.where(Category.class).equalTo("id",page.getCategory_id()).findFirst();
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
                                	CartItem cartItem = new CartItem(mainActivity.id_Cart,price.getId_price(), page, null, 0, 1, null, price.getAmount(), page.getTitle(), price.getLabel(), new RealmList<CartItem>());
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
                LayoutParams params = new LayoutParams(5, LayoutParams.WRAP_CONTENT);
                pricesContainer.addView(espace, params);
                pricesContainer.addView(priceElement);
            }

        }

        //Utilities
        //			ShapeDrawable shape = getNewShape();
        //--state drawables
        stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(colors.getColor(colors.getTitle_color())));
        stateListDrawable.addState(new int[]{android.R.attr.state_selected}, new ColorDrawable(colors.getColor(colors.getTitle_color())));
        //		stateListDrawable.setColorFilter(new PorterDuffColorFilter(Color.parseColor("#ff"+mainActivity.colors.getSide_tabs_foreground_color()),PorterDuff.Mode.MULTIPLY));


        //Add Related products links
        Linked linked = page.getLinked();
        Related related = page.getRelated();
        llRelated = (LinearLayout)view.findViewById(R.id.LLRelatedItems);

        //		Collection<RelatedCatIds> relatedCats = related.getRelatedCatIds();
        //		Collection<RelatedPageId> relatedPages = related.getPages1();
        //		Collection<Link> links = linked.getLinks1();
        //		ArrayList<View> views = new ArrayList<View>();

        //		addRelatedElements(llRelated, related, linked, relatedCats, relatedPages, links, inflater);
        page = realm.where(Child_pages.class).equalTo("id_cp",page_id).findFirst();
        objects = getAllRelatedElements(related, linked);
        drawRelatedElements(objects, inflater);

        /*** Coloring ***/
        //			navigationHolder.setBackgroundColor(colors.getColor(colors.getBackground_color()/*,ALPHA*/));
        //			//navButtons.setBackgroundColor(colors.getColor(colors.getBackground_color(),"66"));
        //			TextView shareLabelTV = (TextView)view.findViewById(R.id.shareLabelTV);
        //			shareLabelTV.setTextColor(colors.getColor(colors.getForeground_color()));
        //			ImageView shareIMG = (ImageView)view.findViewById(R.id.shareIMG);
        //			Drawable drawableShare = getResources().getDrawable(R.drawable.feed_icon);
        //			drawableShare.setColorFilter(new PorterDuffColorFilter(colors.getColor(colors.getForeground_color()) ,PorterDuff.Mode.MULTIPLY));
        //			shareIMG.setBackgroundDrawable(drawableShare);
        //


        view.findViewById(R.id.PricesHolderAbove).setBackgroundColor(colors.getColor(colors.getBackground_color()/*,ALPHA*/));
        llRelated.setBackgroundColor(colors.getColor(colors.getBackground_color()/*,ALPHA*/));
        /*** END Coloring ***/

			/*
			 * END COMMUN TASKS
			 */


        //		for (int i = 0; i < 5; i++) {
		//			addItemToCart(null);
		//		}
		return view;
	}

	//	private void removeInvisiblePages(Collection<Child_pages> pagesTmp) {
	//		for (Iterator<Child_pages> iterator = pagesTmp.iterator(); iterator.hasNext();) {
	//			Child_pages child_pages = (Child_pages) iterator.next();
	//			if (child_pages.getVisible()) {
	//				pages.add(child_pages);
	//			}
	//		}
	//		
	//	}

	protected void addItemToDB(CartItem cartItem) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(cartItem);
        realm.commitTransaction();
        //appController.getCartItemDao().createOrUpdate(cartItem);

    }

	/**
	 * fill the cart with the {@link CartItem} taken from the database
	 */
	//	private void fillCart(){
	//		List<CartItem> cartItems = new ArrayList<CartItem>();
	//		try {
	//			cartItems = appController.getCartItemDao().queryForAll();
	//		} catch (SQLException e) {
	//			// TODO Auto-generated catch block
	//			e.printStackTrace();
	//		}
	//		LinearLayout menuView = mainActivity.cartTagContainer;
	//		if (cartItems.size()>0) {
	//			menuView.removeAllViews();
	//			menuView.addView(getNewDivider());
	//			total = 0.0;
	//			for (int i = 0; i < cartItems.size(); i++) {
	//				addItemToCart(cartItems.get(i));
	//			}
	//		}else {
	//			menuView.removeAllViews();
	//		}
	//		mainActivity.totalSum.setText(total+"");
	//	}


	/**add a {@link CartItem} to the cart
	 * @param item
	 */
	//	protected void addItemToCart(CartItem item) {
	//		SlidingMenu menu = mainActivity.getMenu();
	//		LinearLayout menuView = mainActivity.cartTagContainer;
	//		View cartItemView = layoutInflater.inflate(R.layout.cart_tag, null, false);
	//		//define common graphic elements
	//		TextView titleProduct = (AutoResizeTextView)cartItemView.findViewById(R.id.TitleProduit);
	//		TextView quantity = (AutoResizeTextView)cartItemView.findViewById(R.id.quantity);
	//		AutoResizeTextView relativeSum = (AutoResizeTextView)cartItemView.findViewById(R.id.relativeSum);
	//		LinearLayout deleteContainer = (LinearLayout)cartItemView.findViewById(R.id.deleteContainer);
	//		ImageView imgFormule = (ImageView)cartItemView.findViewById(R.id.imgCartItem);
	//
	//
	//		String prix = item.getPrice();
	//		Double rSum = (item.getQuantity())*(Double.parseDouble(prix));
	//		relativeSum.setText(rSum+"");
	//		total = total + rSum;
	//
	//		if (item.getFormule()!= null) {
	//			Formule formule = item.getFormule();
	//			titleProduct.setText(formule.getName());
	//			relativeSum.setText(formule.getPrice());
	//
	//			if (formule.getIllustration() != null) {
	//				String path = !formule.getIllustration().getPath().isEmpty() ? "file:///"
	//						+ formule.getIllustration().getPath()
	//						: formule.getIllustration().getLink();
	//						//imageLoader.displayImage(path, imgFormule, options);
	//			}else {
	//				imgFormule.setVisibility(View.GONE);
	//			}
	//			menuView.addView(cartItemView);
	//			menuView.addView(getNewDivider());
	//			for (Iterator<FormuleElement> iterator = formule.getElements().iterator(); iterator.hasNext();) {
	//				View elementsView = layoutInflater.inflate(R.layout.cart_tag_sub_item, null, false);
	//				FormuleElement element = (FormuleElement) iterator.next();
	//				TextView tvElement = (AutoResizeTextView)elementsView.findViewById(R.id.TitleProduit);
	//				tvElement.setText(element.getName());
	//				elementsView.findViewById(R.id.relativeSum).setVisibility(View.GONE);
	//				elementsView.findViewById(R.id.deleteContainer).setVisibility(View.GONE);
	//				menuView.addView(elementsView);
	//				menuView.addView(getNewDivider());
	//			}
	//
	//		}else if (item.getCartItems().size()>0 ) {
	//
	//			titleProduct.setText(item.getName());
	//			relativeSum.setText(item.getPrice());
	//			imgFormule.setVisibility(View.GONE);
	//			menuView.addView(cartItemView);
	//			menuView.addView(getNewDivider());
	//			Collection<CartItem> items = item.getCartItems();
	//			for (Iterator<CartItem> iterator = items.iterator(); iterator.hasNext();) {
	//				CartItem cartItem = (CartItem) iterator.next();
	//				View subItem = layoutInflater.inflate(R.layout.cart_tag_sub_item, null, false);
	//				TextView tvElement = (AutoResizeTextView)subItem.findViewById(R.id.TitleProduit);
	//				tvElement.setText(cartItem.getName());
	//				TextView subSum = (AutoResizeTextView)subItem.findViewById(R.id.relativeSum);
	//				subSum.setText(cartItem.getName());
	//				LinearLayout subDelete = (LinearLayout)subItem.findViewById(R.id.deleteContainer);
	//				menuView.addView(subItem);
	//				menuView.addView(getNewDivider());
	//			}
	//
	//		} else if (item.getParentItem() == null) {
	//			titleProduct.setText(item.getName());
	//			relativeSum.setText(item.getPrice());
	//			imgFormule.setVisibility(View.GONE);
	//			menuView.addView(cartItemView);
	//			menuView.addView(getNewDivider());
	//		}else {
	//
	//		}
	//
	//		quantity.setText(item.getQuantity()+"");
	//		final CartItem itemTmp = item;
	//		deleteContainer.setOnClickListener(new OnClickListener() {
	//
	//			@Override
	//			public void onClick(View v) {
	//				boolean done = false;
	//				try {
	//					appController.getCartItemDao().delete(itemTmp);
	//					done  = true;
	//				} catch (SQLException e) {
	//
	//					e.printStackTrace();
	//				}
	//				if (done) {
	//					//					String prix = itemTmp.getPrice();
	//					//					Double rSum = (itemTmp.getQuantity())*(Double.parseDouble(prix));
	//					//					total = total - rSum;
	//					fillCart();
	//				}
	//			}
	//		});
	//		ImageView quantityLess = (ImageView)cartItemView.findViewById(R.id.quantityLess);
	//		ImageView quantityMore = (ImageView)cartItemView.findViewById(R.id.quantityMore);
	//		quantityLess.setOnClickListener(new OnClickListener() {
	//
	//			@Override
	//			public void onClick(View v) {
	//				int quant = itemTmp.getQuantity();
	//				CartItem arg0 = itemTmp;
	//				if (quant > 1) {
	//					arg0.setQuantity(quant -1);
	//
	//					boolean done = false;
	//					try {
	//						appController.getCartItemDao().update(arg0);
	//						done  = true;
	//					} catch (SQLException e) {
	//						// TODO Auto-generated catch block
	//						e.printStackTrace();
	//					}
	//					if (done) {
	//						//					String prix = itemTmp.getPrice();
	//						//					Double rSum = (itemTmp.getQuantity())*(Double.parseDouble(prix));
	//						//					total = total - Double.parseDouble(prix);
	//						fillCart();
	//					}
	//				}
	//
	//			}
	//		});
	//
	//		quantityMore.setOnClickListener(new OnClickListener() {
	//
	//			@Override
	//			public void onClick(View v) {
	//				int quant = itemTmp.getQuantity();
	//				CartItem arg0 = itemTmp;
	//				arg0.setQuantity(quant +1);
	//				boolean done = false;
	//				try {
	//					appController.getCartItemDao().update(arg0);
	//					done  = true;
	//				} catch (SQLException e) {
	//					// TODO Auto-generated catch block
	//					e.printStackTrace();
	//				}
	//				if (done) {
	//					//					String prix = itemTmp.getPrice();
	//					//					Double rSum = (itemTmp.getQuantity())*(Double.parseDouble(prix));
	//					//					relativeSum.setText(rSum+"");
	//					//					total = total + Double.parseDouble(prix);
	//					fillCart();
	//				}
	//
	//			}
	//		});
	//
	//	}
	//	
	//	
	//	public Double total = 0.0;

	//	protected Formule FillFormula() {
	//		List<FormuleElement> elements = new ArrayList<FormuleElement>();
	//		Price a = page.getPrices1().iterator().next();
	//		String price = a.getAmount();
	//		Formule result = new Formule(page.getTitle(), elements, price);
	//		result = new Formule(elements, price, page.getTitle(), page.getIllustration());
	//		for (int i = 0; i < objects.size(); i++) {
	//			if (objects.get(i) instanceof Child_pages) {
	//				Child_pages page = (Child_pages)objects.get(i);
	//				FormuleElement element = new FormuleElement(price, result, page.getTitle(), page.getId());
	//				elements.add(element);
	//				try {
	//					appController.getFormuleElementDao().createOrUpdate(element);
	//				} catch (SQLException e) {
	//					// TODO Auto-generated catch block
	//					e.printStackTrace();
	//				}
	//			}
	//		}
	//
	//		result.setElements(elements);
	//		//		result.setIllustration(page.getIllustration());
	//		return result;
	//	}

	//	protected boolean checkFilledFormula() {
	//		boolean result = true;
	//		for (int i = 0; i < objects.size(); i++) {
	//			if (objects.get(i) instanceof Category) {
	//				result = false;
	//				return result;
	//			}
	//		}
	//
	//
	//		return result;
	//	}

	private ArrayList<Object> getAllRelatedElements(Related related, Linked linked){
        ArrayList<Object> objects = new ArrayList<Object>();
        RealmList<MyInteger> relatedCats = new RealmList<>();
        RealmList<MyString> relatedPages = new RealmList<>();
        if(related !=null) {
            relatedCats = related.getList();

          relatedPages = related.getList_pages();
        }
        RealmList<Link> links =new RealmList<>();
        if (linked !=null) {
            links   = linked.getLinks();
        }

        if (related!=null && related.getTitle() !=null && !related.getTitle().isEmpty()) {
			RelatedTitle1 relatedTitle = (RelatedTitle1)related;
			if (relatedPages.size() > 0) {
			//	relatedTitle.setPage(true);
				objects.add(relatedTitle);
			}

		}
		if (relatedPages.size() > 0) {
			for (Iterator<MyString> iterator = relatedPages.iterator(); iterator.hasNext();) {

				MyString relatedPageId = iterator.next();
				List<Child_pages> child_pages = new ArrayList<Child_pages>();
				/*	child_pages = appController.getChildPageDao().queryForEq("id",relatedPageId.getLinked_id());*/
                child_pages = realm.where(Child_pages.class).equalTo("id",Integer.parseInt(relatedPageId.getMyString())).findAll();

                if (child_pages.size() > 0) {
					RelatedItem1 item = (RelatedItem1)child_pages.get(0);
					objects.add(item);
				}
			}
		}
		if (related !=null && related.getTitle_categories() !=null && !related.getTitle_categories().isEmpty()) {
			RelatedTitle1 relatedTitle = (RelatedTitle1)related;
			if (relatedCats.size() > 0) {
			//	relatedTitle.setCategory(true);
				objects.add(relatedTitle);
			}

		}
		if (relatedCats.size() > 0) {
			for (Iterator<MyInteger> iterator = relatedCats.iterator(); iterator
					.hasNext();) {
				MyInteger relatedCatIds = iterator
						.next();
						final int id_related_cat = relatedCatIds.getMyInt();
				List<Category> categories = new ArrayList<Category>();
                categories = realm.where(Category.class).equalTo("id", id_related_cat).findAll();
					/*categories = appController.getCategoryDao()
							.queryForEq("id", relatedCatIds.getLinked_id());*/
                if (categories.size() > 0) {
					RelatedItem1 item = (RelatedItem1)categories.get(0);
					objects.add(item);
				}
			}
		}

		if (related !=null && related.getContact_form()!=null) {
			List<Contact> list = new ArrayList<Contact>();
            list = realm.where(Contact.class).equalTo("id",related.getContact_form().getId()).findAll();
            //appController.getContactDao().queryForEq("id", related.getContact_form().getId());
            if (list.size()>0) {
				RelatedItem1 item = (RelatedItem1)list.get(0);
				objects.add(item);
			}

		}
		if (linked !=null && linked.getTitle() !=null && !linked.getTitle().isEmpty()) {
			RelatedTitle1 relatedTitle = (RelatedTitle1)linked;
			if (links.size() > 0) {
			//	relatedTitle.setLink(true);
				objects.add(relatedTitle);
			}

		}
		if (links.size() > 0) {
			for (Iterator<Link> iterator = links.iterator(); iterator
					.hasNext();) {
				Link link = (Link) iterator.next();
				RelatedItem1 item = (RelatedItem1)link;
				objects.add(item);
			}
		}

		return objects;

	}

	List<Object> objects;
	private View clickedView;
	protected int indexClicked;
	private long time;

	public void drawRelatedElements(List<Object> objects, LayoutInflater inflater){

		//		objects = getAllRelatedElements(related, linked);
		boolean toggle = false;
		for (int i = 0; i < objects.size(); i++) {

			if (objects.get(i) instanceof RelatedTitle1) {

				RelatedTitle1 item = (RelatedTitle1)objects.get(i);
				if (toggle) {
					//item.setPage(false);

				}
				View relatedTitleView = inflater.inflate(R.layout.related_title, null, false);
				TextView relatedTitleTV = (TextView) relatedTitleView.findViewById(R.id.title_related);
				relatedTitleTV.setTypeface(MainActivity.FONT_TITLE);
				if (item.getRelatedTitle() != null) {
					relatedTitleTV.setText(item.getRelatedTitle());
				}else {
					relatedTitleView.setVisibility(View.GONE);
				}
				relatedTitleTV.setTextColor(colors.getColor(colors.getBackground_color()));
				LinearLayout titleRelatedLL = (LinearLayout) relatedTitleView
						.findViewById(R.id.title_related_LL);
				//				titleRelatedLL.setBackgroundDrawable(getNewShape());

				ColorFilter d = new ColorFilter();
				//titleRelatedLL.setBackgroundColor(colors.getColor(colors.getTitle_color() ,"ff"));
				titleRelatedLL.setBackgroundDrawable(colors.getForePD());
				//titleRelatedLL.setBackgroundDrawable(colors.makeGradientToColor(colors.getTitle_color()));
				toggle = true;
				llRelated.addView(relatedTitleView);
			}
			if (objects.get(i) instanceof RelatedItem1) {
				llRelated.addView(getNewDivider());
				View relatedView = createRelatedView(inflater, objects.get(i), i);
				llRelated.addView(relatedView);
				if (i==objects.size()-1) {
					llRelated.addView(getNewDivider());

				}
			}
		}
	}



	private View createRelatedView(LayoutInflater inflater, Object object, int index) {
		final RelatedItem1 item = (RelatedItem1)object;
		//		final Child_pages pageRelated = child_pages.get(0);
		boolean noImageUse = false;
		View relatedView = inflater.inflate(
				R.layout.related_elements_list_item, null,
				false);

		relatedView.setClickable(true);	
		ColorStateList colorSelector = new ColorStateList(
				new int[][] {
						new int[] { android.R.attr.state_pressed },
						new int[] {} },
						new int[] {
						colors.getColor(colors
								.getBackground_color(), "AA"),
								colors.getColor(colors.getTitle_color(), "AA") });
		stateListDrawable = new StateListDrawable();
		stateListDrawable.addState(
				new int[] { android.R.attr.state_pressed },
				new ColorDrawable(colors.getColor(colors
						.getTitle_color())));
		stateListDrawable.addState(
				new int[] { android.R.attr.state_selected },
				new ColorDrawable(colors.getColor(colors
						.getTitle_color())));
		relatedView.setBackgroundDrawable(stateListDrawable);
		TextView titleTv = (TextView) relatedView.findViewById(R.id.TVTitleCategory);
		titleTv.setTypeface(MainActivity.FONT_TITLE);
		TextView relatedDesc = (TextView)relatedView.findViewById(R.id.TVrelatedDesc);
		relatedDesc.setTypeface(MainActivity.FONT_BODY);
		ArrowImageView arrowImg = (ArrowImageView) relatedView.findViewById(R.id.imgArrow);
		ImageView imgPage = (ImageView) relatedView.findViewById(R.id.imgCategory);
		if (design.equals("formule") ) {
			arrowImg.setVisibility(View.GONE);
			LinearLayout imgArrowContainer = (LinearLayout)relatedView.findViewById(R.id.imgArrowContainer);
			ImageView imgFormule = new ImageView(getActivity());
			imgFormule.setLayoutParams(new LinearLayout.LayoutParams(32, 32));
			imgFormule.setScaleType(ScaleType.CENTER_CROP);
			Drawable stylo = getResources().getDrawable(R.drawable.icon_0_15);
			stylo.setColorFilter(new PorterDuffColorFilter(colors.getColor(colors.getForeground_color()),PorterDuff.Mode.MULTIPLY));
			imgFormule.setImageDrawable(stylo );
			imgArrowContainer.addView(imgFormule);

			if (item instanceof Child_pages) {
				Child_pages pageItem = (Child_pages)item;
				Category categoryItem = pageItem.getCategory();
				titleTv.setVisibility(View.VISIBLE);
				titleTv.setText(categoryItem.getTitle());
				titleTv.setTextAppearance(getActivity(), android.R.style.TextAppearance_Small);
				//				titleTv.setTextSize(getResources().getDimension(R.dimen.subtitleListSize));
				//relatedDesc.setTypeface(null, Typeface.BOLD);
				relatedDesc.setVisibility(View.VISIBLE);
				relatedDesc.setText(pageItem.getTitle());
				//				relatedDesc.setTextColor(Color.WHITE);
				relatedDesc.setTextAppearance(getActivity(), android.R.style.TextAppearance_Medium);
				//				relatedDesc.setTextSize(getResources().getDimension(R.dimen.titleListSize));
				relatedDesc.setTextColor(colorSelector);
			}else {
				noImageUse = true;
				Drawable plus_formule = getResources().getDrawable(R.drawable.multi_select_d);
				plus_formule.setColorFilter(new PorterDuffColorFilter(colors.getColor(colors.getTitle_color(), "88"),PorterDuff.Mode.MULTIPLY));
				imgPage.setBackgroundDrawable(plus_formule);
				imgFormule.setVisibility(View.GONE);
				titleTv.setVisibility(View.GONE);
				relatedDesc.setText(item.getItemIntro1());
				relatedDesc.setTypeface( mainActivity.FONT_ITALIC);
				//relatedDesc.setTextAppearance(getActivity(), android.R.style.TextAppearance_Small_Inverse);

				relatedDesc.setTextColor(colorSelector);
			}

		}else {
			relatedDesc.setVisibility(View.GONE);
			titleTv.setText(item.getItemTitle1());
		}

		titleTv.setTextColor(colors.getColor(colors
				.getTitle_color()));
		ColorStateList colorSelector2 = new ColorStateList(
				new int[][] {
						new int[] { android.R.attr.state_pressed },
						new int[] {} },
						new int[] {
						colors.getColor(colors
								.getBackground_color()),
								colors.getColor(colors.getTitle_color()) });
		titleTv.setTextColor(colorSelector2);
		if (item instanceof Link) {
			titleTv.setText(item.getItemTitle1());
			relatedDesc.setText(item.getItemIntro1());
			relatedDesc.setTextAppearance(getActivity(), android.R.style.TextAppearance_Medium);
			relatedDesc.setTextColor(colors.getColor(colors.getBody_color()));
			relatedDesc.setVisibility(View.VISIBLE);

			relatedDesc.setTypeface(relatedDesc.getTypeface(), Typeface.NORMAL);
		}

		if (!noImageUse) {
			Object illustration = item.getItemIllustration1();
			if (illustration != null) {

				if (illustration instanceof Illustration) {
					Illustration image = (Illustration) illustration;
					String path = "";
					if (illust != null && !illust.getPath().isEmpty()) {
						path = illust.getPath();
						Glide.with(getActivity()).load(new File(path)).into(imgPage);
					}else {
						path = illust.getLink();
						Glide.with(getActivity()).load(path).into(imgPage);
					}
					//					imageLoader.displayImage(path, imgPage, options);
				}else if (illustration instanceof String) {
					String icon = (String) illustration;
					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(32, 32);
					imgPage.setLayoutParams(params);
					try {
						BitmapDrawable drawable = new BitmapDrawable(BitmapFactory.decodeStream(getActivity().getAssets().open(icon)));
						drawable.setColorFilter(new PorterDuffColorFilter(
								colors.getColor(colors.getForeground_color()),
								PorterDuff.Mode.MULTIPLY));
						imgPage.setImageDrawable(drawable);
					} catch (IOException e) {
						e.printStackTrace();
					}

				} 

			}
		}

		PackageManager pm = getActivity()
				.getPackageManager();
		final boolean hasTelephony = pm
				.hasSystemFeature(PackageManager.FEATURE_TELEPHONY);
		


		arrowImg.setPaint(getNewPaint());
		if (item instanceof Link) {
			if (((Link)item).getUrl().startsWith("tel://")) {
				if (!hasTelephony) {
				arrowImg.setVisibility(View.GONE);
				}
			}
		}

		final View viewFinal = relatedView;
		layoutInflater = inflater;
		final int indexTmp = index;
		relatedView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				clickedView = v;
				indexClicked = indexTmp; 	
				if (item instanceof Link) {
					Link link = (Link)item;
					String url = link .getUrl();
					if (url.startsWith("http")) {
						if (url.contains("youtube")) {

							Intent intent = new Intent(getActivity(), YoutubePlayerActivity.class);
							intent.putExtra("InfoActivity", ((MainActivity) getActivity()).infoActivity);
							intent.putExtra("link", url);
							startActivity(intent);
						}else if (url.contains(".pdf")) {
							Intent target = new Intent(Intent.ACTION_VIEW);
							Uri uri = Uri.parse(url);
							target.setData(uri);
							//							target.setDataAndType(uri ,"application/pdf");
							target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

							Intent intent = Intent.createChooser(target, "Open File");
							try {
								startActivity(intent);
							} catch (ActivityNotFoundException e) {
								// Instruct the user to install a PDF reader here, or something
							}
						}else {
							WebViewFragment webViewFragment = new WebViewFragment();
							((MainActivity) getActivity()).bodyFragment = "WebViewFragment";
							// In case this activity was started with special instructions from an Intent,
							// pass the Intent's extras to the fragment as arguments
							((MainActivity) getActivity()).extras = new Bundle();
							((MainActivity) getActivity()).extras
							.putString("link", url);
							webViewFragment
							.setArguments(((MainActivity) getActivity()).extras);
							// Add the fragment to the 'fragment_container' FrameLayout
							getActivity()
							.getSupportFragmentManager()
							.beginTransaction()
							.replace(R.id.fragment_container,
									webViewFragment)
									.setTransition(
											FragmentTransaction.TRANSIT_FRAGMENT_FADE)
											.addToBackStack(null).commit();
						}


					} else if (url.startsWith("mailto:")) {
						Intent mailer = new Intent(Intent.ACTION_SEND);
						mailer.setType("message/rfc822");//text/plain
						String email = url.replaceAll("mailto:", "");

						mailer.putExtra(Intent.EXTRA_EMAIL,
								new String[] { email });
						startActivity(Intent.createChooser(mailer,
								"Send email..."));
					} else if (url.startsWith("tel:")) {

						if (hasTelephony) {
							Intent intent = new Intent(
									Intent.ACTION_CALL);
							intent.setData(Uri.parse(url));
							startActivity(intent);
						}
					}
				}else if (item instanceof Category) {
					Category category = (Category)item;
					if (!design.equals("formule")) {

						if (!category.getDisplay_type().equals("multi_select")) {
							((MainActivity)getActivity()).openCategory(category);

						}else {
							MultiSelectPagesFragment SplitListCategoryFragment = new MultiSelectPagesFragment();
							((MainActivity) getActivity()).bodyFragment = "SplitListCategoryFragment";
							// In case this activity was started with special instructions from an Intent,
							// pass the Intent's extras to the fragment as arguments
							((MainActivity) getActivity()).extras = new Bundle();
							((MainActivity) getActivity()).extras.putInt(
									"Category_id", category.getId());
							SplitListCategoryFragment
							.setArguments(((MainActivity) getActivity()).extras);
							// Add the fragment to the 'fragment_container' FrameLayout
							getActivity()
							.getSupportFragmentManager()
							.beginTransaction()
							.replace(R.id.fragment_container,
									SplitListCategoryFragment)
									.setTransition(
											FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
											.addToBackStack(null).commit();
						}
					}else {
						//showPopup(category.getId(), viewFinal, layoutInflater);
					}
				}else if (item instanceof Child_pages) {
					Child_pages page  =(Child_pages)item;
					if (!design.equals("formule")) {
						((MainActivity)getActivity()).openPage(page);

					}else {
						Category category = realm.where(Category.class).equalTo("id",page.getCategory().getId()).findFirst();
						//appController.getCategoryByIdDB(page.getCategory().getId_category());
						//showPopup(category.getId(), viewFinal, layoutInflater);
					}
				}else if (item instanceof Contact) {
					int sid = 0;
					if (page.getCategory()!=null && page.getCategory().getSection()!=null) {
						sid = page.getCategory().getSection().getId_s();
					}
					FormContactFragment forFrag = FormContactFragment.newInstance();
					((MainActivity) getActivity()).bodyFragment = "FormContactFragment";
					// In case this activity was started with special instructions from an Intent,
					// pass the Intent's extras to the fragment as arguments
					((MainActivity) getActivity()).extras = new Bundle();
					((MainActivity) getActivity()).extras.putInt("Section_id_form", sid);
					((MainActivity) getActivity()).extras.putInt("Contact", ((Contact)item).getId_con());
					forFrag.setArguments(((MainActivity) getActivity()).extras);
					// Add the fragment to the 'fragment_container' FrameLayout
					getActivity()
					.getSupportFragmentManager()
					.beginTransaction()
					.replace(R.id.fragment_container,
							forFrag)
							.setTransition(
									FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
									.addToBackStack(null).commit();
				}


			}
		});

		return relatedView;
	}

	//	public void showPopup(int id_cat, View relatedView, LayoutInflater inflater) {
	//
	//		try {
	//			List<Category> categories = appController.getCategoryDao().queryForEq("id", id_cat);
	//			if (categories.size()>0) {
	//				Category category  = categories.get(0);
	//				List<CommunElements> pages = new ArrayList<CommunElements>();
	//				pages.addAll(category.getChildren_pages1());
	//				View formulelist = inflater.inflate(R.layout.formule_list, null, false);
	//				ListView listView = (ListView)formulelist.findViewById(R.id.formule_list_element);
	//				FormulePagesAdapter adapter = new FormulePagesAdapter(getActivity(), pages, imageLoader, ((MyApplication)getActivity().getApplication()).options, colors, SplitListFragment.this);
	//				listView.setAdapter(adapter);
	//				pw = new PopupWindow(formulelist, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
	//				// display the popup in the center
	//				pw.setOutsideTouchable(true);
	//				pw.setBackgroundDrawable(new PopupBackround());
	//				pw.setFocusable(true);
	//				pw.setTouchInterceptor(new OnTouchListener() {
	//					@Override
	//					public boolean onTouch(View v, MotionEvent event) {
	//						if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
	//							pw.dismiss();
	//							return true;
	//						}
	//						return false;
	//					}
	//				});
	//				Rect location = locateView(relatedView);
	//
	//				//				Rect locationPopo = locateView(formulelist);
	//				//				
	//				//				Rect locationList = locateView(formulelist);
	//				pw.showAtLocation(view, Gravity.TOP|Gravity.RIGHT, location.left, (location.bottom+location.top)/2);
	//			}
	//		} catch (SQLException e) {
	//
	//			e.printStackTrace();
	//		}
	//
	//	}
	//
	private View getNewDivider() {
		View divider = new View(getActivity());
		divider.setLayoutParams(new LayoutParams(
				LayoutParams.MATCH_PARENT, 1));
		divider.setBackgroundColor(colors.getColor(colors
				.getForeground_color()));
		return divider;
	}

	private Paint getNewPaint() {
		Paint paint = new Paint();
		paint.setColor(colors.getColor(colors.getForeground_color()/*,ALPHA*/));
		return paint;
	}

	//	private ShapeDrawable getNewShape() {
	//		ShapeDrawable shape = new ShapeDrawable(new RectShape());
	//		int color1 = colors.getColor(colors.getForeground_color(), "FF");
	//		int color2 = colors.getColor(colors.getForeground_color(), "AA");
	//		Shader shader1 = new LinearGradient(0, 0, 0, 50, new int[] {
	//				color2, color1  }, null, Shader.TileMode.CLAMP);
	//		shape.getPaint().setShader(shader1);
	//		shape.getPaint().setColor(Color.WHITE);
	//		shape.getPaint().setStyle(Paint.Style.FILL);
	//		return shape;
	//	}
	//
	//	private int indexOfPage() {
	//		int index = -1;
	//		for (int i = 0; i < pages.size(); i++) {
	//			if (page.getId_cpages()==pages.get(i).getId_cpages()) {
	//				index = i;
	//			}
	//		}
	//		return index;
	//	}




	//	protected Child_pages getNextPage(int pIndexCurrent) {
	//		Child_pages pageTo;
	//		if (pIndexCurrent >= pages.size()-1) {
	//			pageTo = pages.get(0);
	//		}else {
	//			pageTo = pages.get(pIndexCurrent+NEXT);
	//		}
	//		while (pageTo == null || (pageTo != null && !pageTo.getVisible())) {
	//			pageTo = getNextPage(pIndexCurrent+NEXT);
	//
	//		}
	//		return pageTo;
	//	}
	//
	//	protected Child_pages getPreviousPage(int pIndexCurrent) {
	//		Child_pages pageTo;
	//		if (pIndexCurrent>0) {
	//			pageTo = pages.get(pIndexCurrent+PREVIOUS);
	//		}else {
	//			pageTo = pages.get(pages.size()-1);
	//		}
	//		while (pageTo == null || (pageTo != null && !pageTo.getVisible())) {
	//			pageTo = getPreviousPage(pIndexCurrent+PREVIOUS);
	//		}
	//		return pageTo;
	//	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
		Runtime.getRuntime().gc();
		//		imageLoader.clearMemoryCache();
		super.onDestroy();
	}

	public static Rect locateView(View v)
	{
		int[] loc_int = new int[2];
		if (v == null) return null;
		try
		{
			v.getLocationOnScreen(loc_int);
		} catch (NullPointerException npe)
		{
			//Happens when the view doesn't exist on screen anymore.
			return null;
		}
		Rect location = new Rect();
		location.left = loc_int[0];
		location.top = loc_int[1];
		location.right = location.left + v.getWidth();
		location.bottom = location.top + v.getHeight();
		return location;
	}

     @Override
	 public void onChildPageClick(Child_pages child_pages) {
		//Log.i("child_pages", ""+child_pages.toString());
		//pw.dismiss();
		objects.remove(indexClicked);
		objects.add(indexClicked, child_pages);
		llRelated.removeAllViews();
		drawRelatedElements(objects, layoutInflater);
		//		clickedView.refreshDrawableState();
	}

	@Override
	public void onStop() {

            page=realm.where(Child_pages.class).findFirst();
        if(page!=null) {
            AppHit hit = new AppHit(System.currentTimeMillis() / 1000, time / 1000, "sales_page", page.getId());

		((MyApplication)getActivity().getApplication()).hits.add(hit);
        }

		super.onStop();

	}



}
