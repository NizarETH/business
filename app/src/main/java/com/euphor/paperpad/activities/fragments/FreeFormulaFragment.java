/**
 *
 */
package com.euphor.paperpad.activities.fragments;

import android.R.style;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.webkit.MimeTypeMap;
import android.webkit.WebView;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.applidium.headerlistview.HeaderListView;
import com.bumptech.glide.Glide;
import com.euphor.paperpad.Beans.Contact;
import com.euphor.paperpad.Beans.MyInteger;
import com.euphor.paperpad.Beans.MyString;
import com.euphor.paperpad.R;
import com.euphor.paperpad.activities.main.MainActivity;
import com.euphor.paperpad.activities.main.MyApplication;
import com.euphor.paperpad.adapters.FreeFormulePageAdapter;
import com.euphor.paperpad.adapters.FreeFormulePageAdapter.CallbackRelatedLinks_;
import com.euphor.paperpad.Beans.CartItem;
import com.euphor.paperpad.Beans.Category;
import com.euphor.paperpad.Beans.Child_pages;
import com.euphor.paperpad.Beans.Formule;
import com.euphor.paperpad.Beans.FormuleElement;
import com.euphor.paperpad.Beans.Illustration;

import com.euphor.paperpad.Beans.Link;
import com.euphor.paperpad.Beans.Linked;
import com.euphor.paperpad.Beans.Parameters;
import com.euphor.paperpad.Beans.Price;
import com.euphor.paperpad.Beans.Related;
import com.euphor.paperpad.Beans.RelatedCatIds;


import com.euphor.paperpad.utils.Colors;

import com.euphor.paperpad.utils.RelatedItem1;
/*import com.euphor.paperpad.utils.RelatedTitle;*/
import com.euphor.paperpad.utils.RelatedTitle1;
import com.euphor.paperpad.utils.Utils;
import com.euphor.paperpad.utils.jsonUtils.AppHit;
import com.euphor.paperpad.widgets.ArrowImageView;
import com.euphor.paperpad.widgets.AutoResizeTextView;
import com.euphor.paperpad.widgets.PopupBackround;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

//import com.euphor.paperpad.adapters.FreeFormulaPagesAdapter.CallbackRelatedLinks;

/**
 * @author euphordev02
 *
 */
public class FreeFormulaFragment extends Fragment implements CallbackRelatedLinks_ {

    //	private static final int FIRST = 0;
    private static final int PREVIOUS = -1;
    private static final int NEXT = 1;
    //	private static int WIDTH_SWIPE_PICS = 600;
    private static String ALPHA = "CC";
    private String design;

    private int page_id;
    private Child_pages page;
    //	private ImageLoader imageLoader;
    private Colors colors;
    private List<Child_pages> pages;
    private int indexCurrent;
    //	private DisplayImageOptions options;
    private Integer categoryId;
    private StateListDrawable stateListDrawable;
    protected LayoutInflater layoutInflater;
    private PopupWindow pw;
    private View view;
    private LinearLayout llRelated;
    private MainActivity mainActivity;
    private String path;
    private Illustration illust;
    private Parameters parameters;
    private boolean isTablet;
    protected AlertDialog alertDialog;
    protected PopupWindow pwShare;
    private ViewFlipper viewFlipper;
    private int current;
    private int downX,upX;
    public Realm realm;

    /*******/
    boolean isPage =false;
    boolean isCategory =false;
    boolean isLink =false;

    /**
     *
     */
    public FreeFormulaFragment() {
        // TODO Auto-generated constructor stub
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
        mainActivity = (MainActivity)activity;
        isTablet = Utils.isTablet(activity);
        super.onAttach(activity);
    }

    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setRetainInstance(true);
        super.onCreate(savedInstanceState);

        page_id = getArguments().getInt("page_id");
        if(((MainActivity)getActivity()).extras == null)
            ((MainActivity) getActivity()).extras = new Bundle();
        ((MainActivity) getActivity()).extras.putInt("page_id", page_id);
        ((MainActivity) getActivity()).bodyFragment = "FreeFormulaFragment";
        time = System.currentTimeMillis();
    }

    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = new View(getActivity());
        design = "horizontal"; // for testing remove when production
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
//		final int deviceWidth = dm.widthPixels - (int)getResources().getDimension(R.dimen.width_tab_fragment);
//		WIDTH_SWIPE_PICS = (int) ((float)deviceWidth*(70f/100f));
        //get the child page by its id
        page = realm.where(Child_pages.class).equalTo("id_cp",page_id).findFirst();
        //appController.getChildPageDao().queryForId(page_id);

        if (page != null) {
            if (isTablet) {
                design = page.getDesign();
            }else {
                design = page.getDesign_smartphone();
            }


        }else {
            design = "horizontal";
        }


        if (design.equals("free_formule")) {
            ALPHA = "FF";
            view = inflater.inflate(R.layout.formule_page, container, false);
            view.setBackgroundColor(colors.getColor(colors.getBackground_color()));
            view.findViewById(R.id.separator).setVisibility(View.GONE);
            //view.findViewById(R.id.separator).setBackgroundColor(colors.getColor(colors.getForeground_color(),ALPHA));

        }else{
            ALPHA = "FF";
            view = inflater.inflate(R.layout.horizontal_for_split, container, false);
            view.setBackgroundColor(colors.getColor(colors.getBackground_color()));
        }

        /*
		 * COMMUN TASKS
		 */
        //Title product
        TextView titleTV = (TextView)view.findViewById(R.id.titleProductTV);

        titleTV.setTextAppearance(getActivity(), style.TextAppearance_Large);
        titleTV.setTypeface(MainActivity.FONT_TITLE, Typeface.BOLD);
        titleTV.setText(page.getTitle());

		/*
		 * choose which template to use to show the child_page
		 */
//			if (isTablet) {
//				if (design.equals("free_formule")) {
//					ALPHA = "FF";
//					view = inflater.inflate(R.layout.formule_page, container, false);
//					view.setBackgroundColor(colors.getColor(colors.getBackground_color()));
//					view.findViewById(R.id.separator).setBackgroundColor(colors.getColor(colors.getForeground_color(),ALPHA));
//				}
//			} else {
//				if (design.equals("free_formule")) {
//					ALPHA = "FF";
//					view = inflater.inflate(R.layout.formule_page, container, false);
//					view.setBackgroundColor(colors.getColor(colors.getBackground_color()));
//
//					view.findViewById(R.id.separator).setBackgroundColor(colors.getColor(colors.getForeground_color(),ALPHA));
//				}
//			}




//		titleTV.setTextColor(colors.getColor(colors.getTitle_color()));
//		titleTV.setGravity(Gravity.CENTER);
//		if (design.equals("formule")) {
//			titleTV.setGravity(Gravity.CENTER);
//		}

//		DisplayMetrics metrics = new DisplayMetrics();
//		getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
//
//		if(!isTablet){
//			titleTV.setTextSize(26);
//		}else
//		if(metrics.densityDpi >= 213 ){
//
//			if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
//				titleTV.setTextSize(26);
//			} else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
//				titleTV.setTextSize(24);
//			}
//
//
//		}
//		else{
//			if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
//				titleTV.setTextSize(34);
//			} else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
//				titleTV.setTextSize(32);
//			}
//		}

        if(isTablet){
            titleTV.setTextSize(titleTV.getTextSize() + 8);
        }else{
            titleTV.setTextSize(titleTV.getTextSize() - 10);
        }
        titleTV.setTextColor(colors.getColor(colors.getTitle_color()));
        //titleTV.setGravity(Gravity.CENTER);
        view.findViewById(R.id.titleProductHolder).setBackgroundColor(colors.getColor(colors.getBackground_color(),ALPHA));

        //Image Product

        final RealmList<Illustration> images = new RealmList<>();
        if(page.getIllustration()!=null)
            images.add(page.getIllustration());

        if (images.size()>0 && (ImageView)view.findViewById(R.id.ProductIMG)!= null) {

            /**  Uness Modif **/
            ///ImageSwitcher
            viewFlipper = (ViewFlipper) view.findViewById(R.id.viewFlipper);

            //Log.e("  image Switcher ","  ==> "+viewFlipper);
            //final int[] imgs= new int[]{R.id.switched_img1, R.id.switched_img2, R.id.switched_img3, R.id.switched_img3, R.id.switched_img4, R.id.switched_img5};


            //imageSwitcher.setFactory(getActivity().getApplicationContext());
            //            viewFlipper.setInAnimation(AnimationUtils.loadAnimation(getActivity().getApplicationContext(),android.R.anim.fade_in));
            //            viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(getActivity().getApplicationContext(),android.R.anim.fade_out));

            current = 0;

            //            viewFlipper.setImageResource(imgs[current]);


            //String[] paths = new String[images.size()];
            //int i = 0;
            for(Iterator<Illustration> iterator = images.iterator(); iterator.hasNext();){

                //			ImagePage imagePage = images.iterator().next();
                Illustration imagePage = iterator.next();
                illust = page.getIllustration();//imagePage.getIllustration();
//		path = !illust.getPath().isEmpty()?"file:///"+illust.getPath():illust.getLink();
                //			ImageView productImage = (ImageView)view.findViewById(R.id.ProductIMG);
                //			productImage.setScaleType(ScaleType.CENTER_CROP);

                ImageView imageView = new ImageView(getActivity().getApplicationContext());
                imageView.setScaleType(ScaleType.CENTER_CROP);
                imageView.setLayoutParams(new ImageSwitcher.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));

                viewFlipper.addView(imageView, LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);

                if (!illust.getPath().isEmpty()) {
                    path = illust.getPath();
                    //				paths[i] = illust.getPath();
                    Glide.with(getActivity()).load(new File(path)).into(imageView);

                }else {
                    path = illust.getLink();
                    //				paths[i] = illust.getLink();
                    Glide.with(getActivity()).load(new File(path)).into(imageView);
                    //				Glide.with(getActivity()).load(path).into(productImage);
                }

                current++;
//		imageLoader.displayImage(path, productImage);
            }


            if(current > 1)
                viewFlipper.setOnTouchListener(new OnTouchListener() {


                    @Override
                    public boolean onTouch(View v, MotionEvent event) {




                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            downX = (int) event.getX();
                            //Log.i("event.getX()", " downX " + downX);
                            return true;
                        }

                        else if (event.getAction() == MotionEvent.ACTION_UP) {
                            upX = (int) event.getX();
                            //Log.i("event.getX()", " upX " + downX);
                            if (upX - downX > 100) {

                                current--;
                                if (current < 0) {
                                    current = images.size() - 1;
                                }

                                viewFlipper.setInAnimation(AnimationUtils.loadAnimation(getActivity().getApplicationContext(),R.anim.slide_in_left));
                                viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(getActivity().getApplicationContext(),R.anim.slide_out_right));

                                viewFlipper.showPrevious(); //.setImageResource(imgs[current]);
                            }

                            else if (downX - upX > -100) {

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
		/*
		if (images.size()>0 && (ImageView)view.findViewById(R.id.ProductIMG)!= null) {
			ImagePage imagePage = images.iterator().next();
			illust = imagePage.getIllustration();
//			path = !illust.getPath().isEmpty()?"file:///"+illust.getPath():illust.getLink();
			ImageView productImage = (ImageView)view.findViewById(R.id.ProductIMG);
			productImage.setScaleType(ScaleType.CENTER_CROP);
			if (!illust.getPath().isEmpty()) {
				path =  illust.getPath();
				Glide.with(getActivity()).load(new File(path)).into(productImage);
			}else {
				path = illust.getLink();
				Glide.with(getActivity()).load(path).into(productImage);
			}
//			imageLoader.displayImage(path, productImage);
		}	*/

        // Arrows next and previous coloring and actions !
        ArrowImageView previousIMG = (ArrowImageView)view.findViewById(R.id.PreviousIMG);
        Paint paint = getNewPaint();
        previousIMG.setPaint(paint );
        ArrowImageView nextIMG = (ArrowImageView)view.findViewById(R.id.NextIMG);
        nextIMG.setPaint(paint );
        int id_cat = page.getCategory_id();
        Category category = realm.where(Category.class).equalTo("id",id_cat).findFirst();//appController.getCategoryDao().queryForId(category.getId_category());
        RealmList<Child_pages> pagesTmp = category.getChildren_pages();
        LinearLayout navButtons = (LinearLayout)view.findViewById(R.id.navButtons);
        LinearLayout backPrev = (LinearLayout)view.findViewById(R.id.backPrevious);
        LinearLayout backNext = (LinearLayout)view.findViewById(R.id.backNext);
        if (pagesTmp.size()>1) {

            pages = new ArrayList<Child_pages>();
            pages.addAll(pagesTmp);
            indexCurrent = indexOfPage();
            backPrev.setOnClickListener(getNavigationBtnListener(PREVIOUS));
            backNext.setOnClickListener(getNavigationBtnListener(NEXT));
        }else {

            navButtons.setVisibility(View.GONE);
        }

        //category parent
        RelativeLayout navigationHolder = (RelativeLayout)view.findViewById(R.id.navigationHolder);
        TextView categoryTv = (TextView)view.findViewById(R.id.categorieTV);
        categoryTv.setTypeface(MainActivity.FONT_BODY, Typeface.BOLD);
        if (design.equals("free_formule")) {
            navigationHolder.setVisibility(View.GONE);
        }else {
            categoryTv.setText(category.getTitle());
            categoryTv.setTextColor(colors.getColor(colors.getForeground_color()));
            LinearLayout categoryHolder = (LinearLayout)view.findViewById(R.id.Category);
            categoryHolder.setBackgroundColor(colors.getColor(colors.getBackground_color(), "66"));
            categoryId = category.getId();
            categoryHolder.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    //Category category = appController.getCategoryById(categoryId);
                    Category category = realm.where(Category.class).equalTo("id",categoryId).findFirst();

                    ((MainActivity)getActivity()).openCategory(category);


                }
            });
        }


        //button zoom
        ImageView iconZoom = (ImageView)view.findViewById(R.id.zoomIMG);
        Drawable drawZoom = getResources().getDrawable(R.drawable.icon_0_26);
        drawZoom.setColorFilter(new PorterDuffColorFilter(colors.getColor(colors.getForeground_color()) , Mode.MULTIPLY));
        iconZoom.setImageDrawable(drawZoom);
        TextView textZoom = (TextView)view.findViewById(R.id.tvGalery);
        textZoom.setTextColor(colors.getColor(colors.getForeground_color()));
        LinearLayout galeryHolder = (LinearLayout)view.findViewById(R.id.galeryHolder);
        galeryHolder.setBackgroundColor(colors.getColor(colors.getBackground_color(), ALPHA));
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
        final View layout = inflater.inflate(R.layout.popup_share, null, false);
        LinearLayout shareHolder = (LinearLayout)view.findViewById(R.id.shareHolder);
        shareHolder.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                pwShare = new PopupWindow(layout ,
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT, true);
                // display the popup in the center
                pwShare.setOutsideTouchable(true);
                pwShare.setBackgroundDrawable(new ColorDrawable(00000000));
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

        // Long description under the product image
        WebView longdescWV = (WebView)view.findViewById(R.id.LongDescWV);
        StringBuilder htmlString = new StringBuilder();
        int[] colorText = Colors.hex2Rgb(colors.getBody_color());
        longdescWV.setBackgroundColor(Color.TRANSPARENT);
        if (view.findViewById(R.id.tempLL) != null) {
            view.findViewById(R.id.tempLL).setBackgroundColor(colors.getColor(colors.getBackground_color(), ALPHA));
        }else {
            longdescWV.setBackgroundColor(colors.getColor(colors.getBackground_color(), ALPHA));
        }
//		int sizeText = getResources().getDimensionPixelSize(R.dimen.priceTV);
//		int sizeText = (int) Utils.spToPixels(getActivity(), (float) 14);
        if (!design.equals("free_formule")) {

            htmlString.append(Utils.paramBodyHTML(colorText));
//			String body = page.getBody().replace("[break]", "");
            String body = (page.getBody().isEmpty()) ? page.getIntro() : page.getBody();
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
            htmlString.append("</div></body></html>");
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
            cadreDrawable.setColorFilter(new PorterDuffColorFilter(colors.getColor(colors.getForeground_color()) , Mode.MULTIPLY));
            view.findViewById(R.id.navigationHolderForStroke).setBackgroundDrawable(cadreDrawable);

            shareHolder.setBackgroundColor(colors.getColor(colors.getBackground_color(), "66"));
            LinearLayout portNavigationHolderForStroke = (LinearLayout)view.findViewById(R.id.navigationHolderForStrokePort);
            if (portNavigationHolderForStroke != null) {
                portNavigationHolderForStroke.setBackgroundDrawable(cadreDrawable);
                view.findViewById(R.id.dividerExtra).setBackgroundColor(colors.getColor(colors.getForeground_color()));
                view.findViewById(R.id.dividerExtra1).setBackgroundColor(colors.getColor(colors.getForeground_color()));
                view.findViewById(R.id.navigationHolderPort).setBackgroundColor(colors.getColor(colors.getBackground_color(),ALPHA));
            }
        }else {
//			sizeText = (int) Utils.spToPixels(getActivity(), (float) 16);
            htmlString.append(Utils.paramBodyHTML(colorText));
            htmlString.append((page.getBody().isEmpty()) ? page.getIntro() : page.getBody());
            if(view.findViewById(R.id.divider) != null)
                view.findViewById(R.id.divider).setVisibility(View.GONE);
            if(view.findViewById(R.id.divider2) != null)
                view.findViewById(R.id.divider2).setVisibility(View.GONE);
            if(view.findViewById(R.id.divider3) != null)
                view.findViewById(R.id.divider3).setVisibility(View.GONE);
            if(view.findViewById(R.id.divider4) != null)
                view.findViewById(R.id.divider4).setVisibility(View.GONE);
            if(view.findViewById(R.id.divider5) != null)
                view.findViewById(R.id.divider5).setVisibility(View.GONE);
            if(view.findViewById(R.id.divider6) != null)
                view.findViewById(R.id.divider6).setVisibility(View.GONE);
            if(view.findViewById(R.id.navigationHolderForStroke) != null)
                view.findViewById(R.id.navigationHolderForStroke).setVisibility(View.GONE);
            LinearLayout portNavigationHolderForStroke = (LinearLayout)view.findViewById(R.id.navigationHolderForStrokePort);
            if (portNavigationHolderForStroke != null) {
                portNavigationHolderForStroke.setVisibility(View.GONE);
            }
        }
        htmlString.append("</div></body></html>");
        longdescWV.loadDataWithBaseURL(null, htmlString.toString(), "text/html", "UTF-8", null);

        //prices related to this product
        parameters = null;
        parameters = realm.where(Parameters.class).findFirst();//appController.getParametersDao().queryForId(1);

        RealmList<Price> prices = page.getPrices();
        LinearLayout pricesContainer = (LinearLayout)view.findViewById(R.id.PricesHolder);
        if (prices.size()>0 && !design.equals("free_formule")) {

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

                priceElement.findViewById(R.id.priceLabel).setBackgroundColor(colors.getColor(colors.getTitle_color()));
                priceElement.findViewById(R.id.priceValue).setBackgroundColor(colors.getColor(colors.getTitle_color(), "AA"));
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


                                if (parameters != null) {
                                    if (parameters.isShow_cart()) {

                                        Category category = null;
                                        if (page.getCategory() != null && page.getCategory().isNeeds_stripe_payment()) {
                                            category = page.getCategory();
                                        }else {
                                            //AppController controller = new AppController(mainActivity);
                                            category = realm.where(Category.class).equalTo("id_c",page.getCategory_id()).findFirst(); //controller.getCategoryById(page.getCategory_id());
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
//											Toast.makeText(getActivity(), "not allowed to add because mode :"+MainActivity.stripe_or_not, Toast.LENGTH_LONG).show();
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
                        }

                    }
                });

                // espacement
                View espace = new View(getActivity());
                LayoutParams params = new LayoutParams(5, LayoutParams.WRAP_CONTENT);
                pricesContainer.addView(espace, params);
                ((android.widget.FrameLayout.LayoutParams)pricesContainer.getLayoutParams()).leftMargin = 20;
                ((android.widget.FrameLayout.LayoutParams)pricesContainer.getLayoutParams()).bottomMargin = 20;
                pricesContainer.addView(priceElement);
                //pricesContainer.addView(priceElement);
            }

        }else {
            pricesContainer.setVisibility(View.GONE);
        }
        if (design.equals("free_formule")) {

            if ((parameters != null && parameters.isShow_cart()) && prices.size()>0){
                Price price = prices.iterator().next();
                View priceElement = inflater.inflate(R.layout.formule_price, container, false);
                TextView label = (TextView)priceElement.findViewById(R.id.priceLabelTV);
                label.setTypeface(MainActivity.FONT_BODY);
//				label.setText((price!=null && price.getLabel()!=null)?price.getLabel():"");
                label.setTextColor(colors.getColor(colors.getBackground_color()));
                TextView value = (TextView)priceElement.findViewById(R.id.priceValueTV);
                value.setTypeface(MainActivity.FONT_BODY);
                value.setText((price!=null && price.getAmount()!=null)?price.getAmount()+price.getCurrency():"");
                value.setTextColor(colors.getColor(colors.getBackground_color()));
                priceElement.findViewById(R.id.priceLabel).setBackgroundColor(colors.getColor(colors.getTitle_color()));
                priceElement.findViewById(R.id.priceValue).setBackgroundColor(colors.getColor(colors.getTitle_color(), "AA"));
                ((android.widget.FrameLayout.LayoutParams)pricesContainer.getLayoutParams()).leftMargin = 20;
                ((android.widget.FrameLayout.LayoutParams)pricesContainer.getLayoutParams()).bottomMargin = 20;
                pricesContainer.addView(priceElement);
                pricesContainer.setVisibility(View.VISIBLE);
                final String price_id = String.valueOf(price.getId_price());
                priceElement.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Category category = null;
                        if (page.getCategory() != null && page.getCategory().isNeeds_stripe_payment()) {
                            category = page.getCategory();
                        }else {
                            //AppController controller = new AppController(mainActivity);
                            category =  realm.where(Category.class).equalTo("id",page.getCategory_id()).findFirst();//controller.getCategoryById(page.getCategory_id());
                        }
                        if(category != null && (MainActivity.stripe_or_not == -1 || (category.isNeeds_stripe_payment() && MainActivity.stripe_or_not == 1) ||
                                (!category.isNeeds_stripe_payment() && MainActivity.stripe_or_not == 0))){
                            if (category.isNeeds_stripe_payment()) {
                                MainActivity.stripe_or_not = 1;
                            }else {
                                MainActivity.stripe_or_not = 0;
                            }

                            if (checkFilledFormula()) {
                                Formule formule = FillFormula();
                                mainActivity.id_Cart++;

                                CartItem cartItem = new CartItem(mainActivity.id_Cart,Integer.valueOf(price_id), null, null, 0, 1, formule, formule.getPrice(), page.getTitle(),"", new RealmList<CartItem>());
                                //							addItemToCart(cartItem);
                                addItemToDB(cartItem);
                                mainActivity.fillCart();
                                Handler handler = new Handler();
                                long delay = 3000;
                                mainActivity.getMenu().showMenu();
                                if(page.getExtra_fields() != null && page.getExtra_fields().getFormule_supplements_category_id() != null) {
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            mainActivity.getMenu().toggle();
                                            //if(page.getExtra_fields() != null && page.getExtra_fields().getFormule_supplements_category_id() != null) {
                                            //List<Category> cat = appController.getCategoryDao().queryForEq("id", page.getExtra_fields().getFormule_supplements_category_id());
                                            RealmResults<Category> cat=realm.where(Category.class).equalTo("id", Integer.parseInt(page.getExtra_fields().getFormule_supplements_category_id())).findAll();
                                            if(cat!= null && cat.size() > 0) {
                                                mainActivity.openCategory(cat.get(0));
                                            }
                                            //}
                                        }
                                    }, delay);
                                }


                            }else {

                                Toast.makeText(getActivity(), getResources().getString(R.string.choix_incomplet), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        }

        //Utilities
//		ShapeDrawable shape = getNewShape();
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
        objects = getAllRelatedElements(related, linked);
        drawRelatedElements(objects, inflater);

        /*** Coloring ***/
        navigationHolder.setBackgroundColor(colors.getColor(colors.getBackground_color(),ALPHA));
        navButtons.setBackgroundColor(colors.getColor(colors.getBackground_color(),"66"));
        TextView shareLabelTV = (TextView)view.findViewById(R.id.shareLabelTV);
        shareLabelTV.setTextColor(colors.getColor(colors.getForeground_color()));
        shareLabelTV.setTypeface(MainActivity.FONT_BODY, Typeface.BOLD);
        ImageView shareIMG = (ImageView)view.findViewById(R.id.shareIMG);
        Drawable drawableShare = getResources().getDrawable(R.drawable.feed_icon);
        drawableShare.setColorFilter(new PorterDuffColorFilter(colors.getColor(colors.getForeground_color()) , Mode.MULTIPLY));
        shareIMG.setBackgroundDrawable(drawableShare);


        view.findViewById(R.id.PricesHolderAbove).setBackgroundColor(colors.getColor(colors.getBackground_color(),ALPHA));
        llRelated.setBackgroundColor(colors.getColor(colors.getBackground_color(),ALPHA));
        /*** END Coloring ***/

		/*
		 * END COMMUN TASKS
		 */


        //		for (int i = 0; i < 5; i++) {
//			addItemToCart(null);
//		}
        return view;
    }

    protected void addItemToDB(CartItem cartItem) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(cartItem);
        realm.commitTransaction();
		/*try {
			appController.getCartItemDao().createOrUpdate(cartItem);
		} catch (SQLException e) {
			e.printStackTrace();
		}*/

    }

    /**
     * fill the cart with the {@link CartItem} taken from the database
     */
    private void fillCart(){
        RealmResults<CartItem>  cartItems = realm.where(CartItem.class).findAll();//appController.getCartItemDao().queryForAll();
        LinearLayout menuView = mainActivity.cartTagContainer;
        if (cartItems.size()>0) {
            menuView.removeAllViews();
            menuView.addView(getNewDivider());
            total = 0.0;
            for (int i = 0; i < cartItems.size(); i++) {
                addItemToCart(cartItems.get(i));
            }
        }else {
            menuView.removeAllViews();
        }
        mainActivity.totalSum.setText(total+"");
    }


    /**add a {@link CartItem} to the cart
     * @param item
     */
    protected void addItemToCart(CartItem item) {
        //SlidingMenu menu = mainActivity.getMenu();
        LinearLayout menuView = mainActivity.cartTagContainer;
        View cartItemView = layoutInflater.inflate(R.layout.cart_tag, null, false);
        //define common graphic elements
        TextView titleProduct = (AutoResizeTextView)cartItemView.findViewById(R.id.TitleProduit);
        titleProduct.setTypeface(MainActivity.FONT_TITLE);
        TextView quantity = (AutoResizeTextView)cartItemView.findViewById(R.id.quantity);
        quantity.setTypeface(MainActivity.FONT_BODY);
        AutoResizeTextView relativeSum = (AutoResizeTextView)cartItemView.findViewById(R.id.relativeSum);
        relativeSum.setTypeface(MainActivity.FONT_BODY);
        LinearLayout deleteContainer = (LinearLayout)cartItemView.findViewById(R.id.deleteContainer);
        ImageView imgFormule = (ImageView)cartItemView.findViewById(R.id.imgCartItem);


        String prix = item.getPrice();
        Double rSum = (item.getQuantity())*(Double.parseDouble(prix));
        relativeSum.setText(rSum+"");
        total = total + rSum;

        if (item.getFormule()!= null) {
            Formule formule = item.getFormule();
            titleProduct.setText(formule.getName());
            relativeSum.setText(formule.getPrice());

            if (formule.getIllustration() != null) {

                if(formule.getIllustration().getPath().isEmpty()) {
                    Glide.with(getActivity()).load(formule.getIllustration().getLink()).into(imgFormule);
                }else {
                    Glide.with(getActivity()).load(new File(formule.getIllustration().getPath())).into(imgFormule);
                }

//				String path = !formule.getIllustration().getPath().isEmpty() ? "file:///"
//						+ formule.getIllustration().getPath()
//						: formule.getIllustration().getLink();
//				imageLoader.displayImage(path, imgFormule, options);
//				

            }else {
                imgFormule.setVisibility(View.GONE);
            }
            menuView.addView(cartItemView);
            menuView.addView(getNewDivider());
            for (Iterator<FormuleElement> iterator = formule.getElements().iterator(); iterator.hasNext();) {
                View elementsView = layoutInflater.inflate(R.layout.cart_tag_sub_item, null, false);
                FormuleElement element = (FormuleElement) iterator.next();
                TextView tvElement = (AutoResizeTextView)elementsView.findViewById(R.id.TitleProduit);
                tvElement.setTypeface(MainActivity.FONT_BODY);
                tvElement.setText(element.getName());
                elementsView.findViewById(R.id.relativeSum).setVisibility(View.GONE);
                elementsView.findViewById(R.id.deleteContainer).setVisibility(View.GONE);
                menuView.addView(elementsView);
                menuView.addView(getNewDivider());
            }

        }else if (item.getCartItems().size()>0 ) {

            titleProduct.setText(item.getName());
            relativeSum.setText(item.getPrice());
            imgFormule.setVisibility(View.GONE);
            menuView.addView(cartItemView);
            menuView.addView(getNewDivider());
            Collection<CartItem> items = item.getCartItems();
            for (Iterator<CartItem> iterator = items.iterator(); iterator.hasNext();) {
                CartItem cartItem = (CartItem) iterator.next();
                View subItem = layoutInflater.inflate(R.layout.cart_tag_sub_item, null, false);
                TextView tvElement = (AutoResizeTextView)subItem.findViewById(R.id.TitleProduit);
                tvElement.setTypeface(MainActivity.FONT_BODY);
                tvElement.setText(cartItem.getName());
                TextView subSum = (AutoResizeTextView)subItem.findViewById(R.id.relativeSum);
                subSum.setTypeface(MainActivity.FONT_BODY);
                subSum.setText(cartItem.getName());
//				LinearLayout subDelete = (LinearLayout)subItem.findViewById(R.id.deleteContainer);
                menuView.addView(subItem);
                menuView.addView(getNewDivider());
            }

        } else if (item.getParentItem() == null) {
            titleProduct.setText(item.getName());
            relativeSum.setText(item.getPrice());
            imgFormule.setVisibility(View.GONE);
            menuView.addView(cartItemView);
            menuView.addView(getNewDivider());
        }else {

        }

        quantity.setText(item.getQuantity()+"");
        final CartItem itemTmp = item;
        deleteContainer.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                boolean done = false;
                realm.beginTransaction();
                realm.where(CartItem.class).findAll().removeLast();
                realm.commitTransaction();
				/*try {
					appController.getCartItemDao().delete(itemTmp);
					done  = true;
				} catch (SQLException e) {
					
					e.printStackTrace();
				}*/
                if (done) {
//					String prix = itemTmp.getPrice();
//					Double rSum = (itemTmp.getQuantity())*(Double.parseDouble(prix));
//					total = total - rSum;
                    mainActivity.fillCart();
                }
            }
        });
        ImageView quantityLess = (ImageView)cartItemView.findViewById(R.id.quantityLess);
        ImageView quantityMore = (ImageView)cartItemView.findViewById(R.id.quantityMore);
        quantityLess.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                int quant = itemTmp.getQuantity();
                CartItem arg0 = itemTmp;
                if (quant > 1) {
                    arg0.setQuantity(quant -1);

                    boolean done = false;
                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(arg0);
                    realm.commitTransaction();
				/*try {
					appController.getCartItemDao().update(arg0);
					done  = true;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
                    if (done) {
//					String prix = itemTmp.getPrice();
//					Double rSum = (itemTmp.getQuantity())*(Double.parseDouble(prix));
//					total = total - Double.parseDouble(prix);
                        mainActivity.fillCart();
                    }
                }

            }
        });

        quantityMore.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                int quant = itemTmp.getQuantity();
                CartItem arg0 = itemTmp;
                arg0.setQuantity(quant +1);
                boolean done = false;
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(arg0);
                realm.commitTransaction();
				/*try {
					appController.getCartItemDao().update(arg0);
					done  = true;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
                if (done) {
//					String prix = itemTmp.getPrice();
//					Double rSum = (itemTmp.getQuantity())*(Double.parseDouble(prix));
//					relativeSum.setText(rSum+"");
//					total = total + Double.parseDouble(prix);
                    mainActivity.fillCart();
                }

            }
        });

    }
    public Double total = 0.0;

    protected Formule FillFormula() {
        RealmList<FormuleElement> elements = new RealmList<>();
        Price a = page.getPrices().iterator().next();
        int id_f=0;
        String price = a.getAmount();
        Formule result = new Formule(page.getTitle(), elements, price);
        int size_f = realm.where(Formule.class).findAll().size();
        if (size_f > 0)
            id_f = realm.where(Formule.class).findAllSorted("id").last().getId() + 1;
        else id_f++;
        result = new Formule(id_f,elements, price, page.getTitle(), page.getIllustration());
        for (int i = 0; i < objects.size(); i++) {
            if (objects.get(i) instanceof Child_pages) {
                Child_pages page = (Child_pages)objects.get(i);
                FormuleElement element = new FormuleElement(price, result, page.getTitle(), page.getId());
                elements.add(element);
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(element);
                realm.commitTransaction();
				/*try {
					appController.getFormuleElementDao().createOrUpdate(element);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
            }
        }

        result.setElements(elements);
//		result.setIllustration(page.getIllustration());
        return result;
    }

    protected boolean checkFilledFormula() {
        boolean result = false;
//		if (objects.size()==1) {
//			if (objects.get(0) instanceof Category) {
//				result = false;
////				return result;
//			}
//		}
        for (int i = 0; i < objects.size(); i++) {
            if (objects.get(i) instanceof Category) {
//				result = false;
//				return result;
            }else if (objects.get(i) instanceof Child_pages) {
                result = true;
            }
        }


        return result;
    }

    private ArrayList<Object> getAllRelatedElements(Related related, Linked linked){
        ArrayList<Object> objects = new ArrayList<Object>();
        RealmList<MyInteger> relatedCats = related.getList();
        RealmList<MyString> relatedPages = related.getList_pages();
        RealmList<Link> links = linked.getLinks();



//		Collection<RelatedPageId> relatedPages = related.getPages1();
//		Collection<Link> links = linked.getLinks1();
        if (!related.getTitle().isEmpty()) {
            RelatedTitle1 relatedTitle = (RelatedTitle1)related;
            if (relatedPages.size() > 0) {
                //relatedTitle.setPage(true);
                isPage=true;
                objects.add(relatedTitle);
            }

        }
        if (relatedPages.size() > 0) {
            for (Iterator<MyString> iterator = relatedPages.iterator(); iterator.hasNext();) {

                MyString relatedPageId = (MyString) iterator.next();
                List<Child_pages> child_pages = new ArrayList<Child_pages>();
                child_pages = realm.where(Child_pages.class).equalTo("id",Integer.parseInt(relatedPageId.getMyString())).findAll();
				/*try {
					child_pages = appController.getChildPageDao().queryForEq("id",relatedPageId.getLinked_id());
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/

                if (child_pages.size() > 0) {
                    RelatedItem1 item = (RelatedItem1)child_pages.get(0);
                    objects.add(item);
                }
            }
        }
        if (!related.getTitle_categories().isEmpty()) {
            RelatedTitle1 relatedTitle = (RelatedTitle1)related;
            if (relatedCats.size() > 0) {
                //relatedTitle.setCategory(true);
                isCategory=true;
                objects.add(relatedTitle);
            }

        }
        if (relatedCats.size() > 0) {
//			for (Iterator<RelatedCatIds> iterator = relatedCats.iterator(); iterator
//					.hasNext();) {
            MyInteger relatedCatIds = (MyInteger) relatedCats.iterator().next();
            final int id_related_cat = relatedCatIds.getMyInt();

					/*categories = appController.getCategoryDao()
							.queryForEq("id", relatedCatIds.getLinked_id());*/
            RealmResults<Category> categories =  realm.where(Category.class).equalTo("id", relatedCatIds.getMyInt()).findAll();

            if (categories.size() > 0) {
                RelatedItem1 item = (RelatedItem1)categories.get(0);
                objects.add(item);
            }
//			}
        }

        if (related.getContact_form()!=null) {
            List<Contact> list = new ArrayList<Contact>();
            list=realm.where(Contact.class).equalTo("id",related.getContact_form().getId()).findAll();
			/*try {
				list = appController.getContactDao().queryForEq("id", related.getContact_form().getId());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
            if (list.size()>0) {
                RelatedItem1 item = (RelatedItem1)list.get(0);
                objects.add(item);
            }

        }
        if (!linked.getTitle().isEmpty()) {
            RelatedTitle1 relatedTitle = (RelatedTitle1)linked;
            if (links.size() > 0) {
                isLink=true;
                //relatedTitle.setLink(true);
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
    //	private View clickedView;
    protected int indexClicked;
    private long time;
    private FreeFormulePageAdapter mHAdapter;
//	private HeaderListView HLview;

    public void drawRelatedElements(List<Object> objects, LayoutInflater inflater){

//		objects = getAllRelatedElements(related, linked);
        boolean toggle = false;

        for (int i = 0; i < objects.size(); i++) {

            if (objects.get(i) instanceof RelatedTitle1) {

                RelatedTitle1 item = (RelatedTitle1)objects.get(i);
                if (toggle) {
                    	isPage=false;

                }

                View relatedTitleView = inflater.inflate(R.layout.related_title, null, false);
                TextView relatedTitleTV = (TextView) relatedTitleView.findViewById(R.id.title_related);

                relatedTitleTV.setTextAppearance(getActivity(), style.TextAppearance_Medium);
                relatedTitleTV.setTypeface(MainActivity.FONT_BODY, Typeface.BOLD);
                //relatedTitleTV.setTextSize(relatedTitleTV.getTextSize() + 4);


                if(!isTablet){
                    DisplayMetrics metrics = new DisplayMetrics();
                    getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

                    if(metrics.densityDpi <= 240 )
                        relatedTitleTV.setTextSize(relatedTitleTV.getTextSize() - 12);
                    else
                        relatedTitleTV.setTextSize(relatedTitleTV.getTextSize() - 20);
                }

                if (item.getRelatedTitle() != null && !item.getRelatedTitle().isEmpty())  {
                    relatedTitleTV.setText(item.getRelatedTitle());
                }else {
                    relatedTitleView.setVisibility(View.GONE);
                }
                relatedTitleTV.setTextColor(colors.getColor(colors.getTitle_color()));
                relatedTitleView.setBackgroundColor(colors.getColor(colors.getForeground_color(), "44"));//titleRelatedLL.setBackgroundDrawable(getNewShape());
                toggle = true;
                llRelated.addView(relatedTitleView);
            }
            else if (objects.get(i) instanceof RelatedItem1) {

                View relatedView = createRelatedView(inflater, objects.get(i), i);
                createRelatedViewEnhanced(inflater, objects.get(i), i);
                llRelated.addView(relatedView);
                llRelated.addView(getNewDivider());
//				if (i==objects.size()-1) {
//					llRelated.addView(getNewDivider());
//
//				}
            }
        }
    }

    private View createRelatedViewEnhanced(LayoutInflater inflater, Object object, int index) {
        final RelatedItem1 item = (RelatedItem1)object;
//		final Child_pages pageRelated = child_pages.get(0);
//		boolean noImageUse = false;
        View relatedView = inflater.inflate(
                R.layout.related_elements_list_item, null, false);
        if (item instanceof Category) {
            Category category  = (Category)item;
        }
        return relatedView;

    }



    private View createRelatedView(LayoutInflater inflater, Object object, final int index) {
        final RelatedItem1 item = (RelatedItem1)object;
//		final Child_pages pageRelated = child_pages.get(0);
        boolean noImageUse = false;
        View relatedView = inflater.inflate(R.layout.related_elements_list_item, null, false);

        relatedView.setClickable(true);
//		ColorStateList colorSelector = new ColorStateList(
//				new int[][] {
//						new int[] { android.R.attr.state_pressed },
//						new int[] {} },
//				new int[] {
//						colors.getColor(colors
//								.getBackground_color(), "AA"),
//						colors.getColor(colors.getBody_color(), "44") });
//		stateListDrawable = new StateListDrawable();
//		stateListDrawable.addState(
//				new int[] { android.R.attr.state_pressed },
//				new ColorDrawable(colors.getColor(colors
//						.getTitle_color())));
//		stateListDrawable.addState(
//				new int[] { android.R.attr.state_selected },
//				new ColorDrawable(colors.getColor(colors
//						.getTitle_color())));
//		relatedView.setBackgroundDrawable(stateListDrawable);
        TextView titleTv = (TextView) relatedView
                .findViewById(R.id.TVTitleCategory);
        titleTv.setTypeface(MainActivity.FONT_TITLE);
        TextView relatedDesc = (TextView)relatedView.findViewById(R.id.TVrelatedDesc);
        relatedDesc.setTypeface(MainActivity.FONT_BODY);
        ArrowImageView arrowImg = (ArrowImageView) relatedView.findViewById(R.id.imgArrow);
        ImageView imgPage = (ImageView) relatedView.findViewById(R.id.imgCategory);

//		((LayoutParams)imgPage.getLayoutParams()).leftMargin = 30;
//		((LayoutParams)imgPage.getLayoutParams()).rightMargin = 30;
        if (design.equals("free_formule") ) {
            relatedView.setPadding(30, 0, 30, 0);
            arrowImg.setVisibility(View.GONE);
            LinearLayout imgArrowContainer = (LinearLayout)relatedView.findViewById(R.id.imgArrowContainer);
            ImageView imgFormule = new ImageView(getActivity());
            imgFormule.setLayoutParams(new LinearLayout.LayoutParams(32, 32));
            imgFormule.setScaleType(ScaleType.CENTER_CROP);
            Drawable stylo = getResources().getDrawable(R.drawable.ic_exit);
            stylo.setColorFilter(new PorterDuffColorFilter(colors.getColor(colors.getForeground_color(), "AA"),PorterDuff.Mode.MULTIPLY));
            imgFormule.setImageDrawable(stylo);
            imgFormule.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    objects.remove(index);
                    llRelated.removeAllViews();
                    drawRelatedElements(objects, layoutInflater);
                }
            });
            //((LayoutParams)imgFormule.getLayoutParams()).rightMargin = 10;
            imgArrowContainer.addView(imgFormule);
            imgArrowContainer.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    objects.remove(index);
                    llRelated.removeAllViews();
                    drawRelatedElements(objects, layoutInflater);
                }
            });
            if (item instanceof Child_pages) {
                Child_pages pageItem = (Child_pages)item;
                Category categoryItem = pageItem.getCategory();
                titleTv.setVisibility(View.GONE);
//				titleTv.setText(categoryItem.getTitle());
//				titleTv.setTextAppearance(getActivity(), android.R.style.TextAppearance_Small);
//				titleTv.setTextSize(getResources().getDimension(R.dimen.subtitleListSize));
//				relatedDesc.setTypeface(null, Typeface.BOLD);
                relatedDesc.setVisibility(View.VISIBLE);
                ((LayoutParams)relatedDesc.getLayoutParams()).leftMargin = 30;
                relatedDesc.setText(pageItem.getTitle());
//				relatedDesc.setTextColor(Color.WHITE);
                relatedDesc.setTextAppearance(getActivity(), android.R.style.TextAppearance_Medium);
//				relatedDesc.setTextSize(getResources().getDimension(R.dimen.titleListSize));
                relatedDesc.setTypeface(MainActivity.FONT_TITLE, Typeface.BOLD);
                relatedDesc.setTextColor(colors.getColor(colors.getTitle_color()));
//				colorSelector = new ColorStateList(
//						new int[][] {
//								new int[] { android.R.attr.state_pressed },
//								new int[] {} },
//						new int[] {
//								colors.getColor(colors
//										.getBackground_color(), "AA"),
//								colors.getColor(colors.getTitle_color()) });
//				relatedDesc.setTextColor(colorSelector);
            }else {
                noImageUse = true;
                Drawable plus_formule = getResources().getDrawable(R.drawable.multi_select_d);
                plus_formule.setColorFilter(new PorterDuffColorFilter(colors.getColor(colors.getBody_color(), "55"),PorterDuff.Mode.MULTIPLY));

                imgPage.setBackgroundDrawable(plus_formule);
                //((LayoutParams)imgPage.getLayoutParams()).leftMargin = 30;
                ((LayoutParams)imgPage.getLayoutParams()).width = 42;
                ((LayoutParams)imgPage.getLayoutParams()).height = 42;
                imgFormule.setVisibility(View.GONE);
                //titleTv.setVisibility(View.GONE);
                ((LayoutParams)relatedDesc.getLayoutParams()).leftMargin = 30;

                Category cat;
                if (item instanceof Category) {
                    cat = (Category)item;
                    if(cat.getChildren_pages().size() > 0)
                        relatedDesc.setText(getString(R.string.product_choices));
                }
                else if(!item.getItemIntro1().isEmpty())
                    relatedDesc.setText(item.getItemIntro1());
                else
                    relatedDesc.setText(getString(R.string.product_choices));
                //relatedDesc.setText(getString(R.string.product_choices));//item.getItemIntro());

                relatedDesc.setTextAppearance(getActivity(), style.TextAppearance_Medium);
                relatedDesc.setTypeface(mainActivity.FONT_ITALIC, Typeface.ITALIC);
                relatedDesc.setTextColor(colors.getColor(colors.getBody_color(), "66"));
                //relatedDesc.setTextColor(colorSelector);
                //relatedDesc.setTextSize(relatedDesc.getTextSize() + 4);
                titleTv.setVisibility(View.GONE);

//					relatedDesc.setText(getString(R.string.product_choices));//item.getItemIntro());
//					relatedDesc.setTypeface(mainActivity.FONT_ITALIC, Typeface.BOLD_ITALIC);
//					titleTv.setTextColor(colorSelector);
            }

        }else {
            relatedDesc.setVisibility(View.GONE);
            titleTv.setText(item.getItemTitle1());
        }

        titleTv.setTextColor(colors.getColor(colors
                .getTitle_color()));
//		ColorStateList colorSelector2 = new ColorStateList(
//				new int[][] {
//						new int[] { android.R.attr.state_pressed },
//						new int[] {} },
//				new int[] {
//						colors.getColor(colors
//								.getBackground_color()),
//						colors.getColor(colors.getTitle_color()) });
//		titleTv.setTextColor(colorSelector2);
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
                    if (!image.getPath().isEmpty()) {
                        path = image.getPath();
                        Glide.with(getActivity()).load(new File(path)).into(imgPage);
                    }else {
                        path = image.getLink();
                        Glide.with(getActivity()).load(path).into(imgPage);
                    }
//					imageLoader.displayImage(path, imgPage, options);
                }else if (illustration instanceof String) {
                    String icon = (String) illustration;
//					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(32, 32);
//					params.leftMargin = 30;
//					imgPage.setLayoutParams(params);
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


        arrowImg.setPaint(getNewPaint());
        if (item instanceof Link) {
            if (((Link)item).getUrl().startsWith("tel://")) {
                arrowImg.setVisibility(View.GONE);
            }
        }

        final View viewFinal = relatedView;
        layoutInflater = inflater;
        final int indexTmp = index;
        relatedView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //clickedView = v;
                indexClicked = indexTmp;
//				if (item instanceof Category) {
//					Category category = (Category)item;
//					
//					List<RelatedCatIds> list = new ArrayList<RelatedCatIds>(page.getRelated().getRelatedCatIds());
//					showPopup(list, category.getId(), viewFinal, layoutInflater);
//				}
//				
//				else 
                if (item instanceof Category) {
                    Category category = (Category)item;

                    //showPopup(category.getId(), viewFinal, layoutInflater);
                    List<MyInteger> list = new RealmList<MyInteger>();//new ArrayList<RelatedCatIds>(page.getRelated().getRelatedCatIds());

                        /*Child_pages cp=realm.where(Child_pages.class).equalTo("id",category.getChildren_pages().get(0).getId()).findFirst();
                        if(cp!=null)*/
                    list=page.getRelated().getList();
                    showPopup(list, viewFinal, layoutInflater);

                }else if (item instanceof Child_pages) {
                    Child_pages page  =(Child_pages)item;

                    List<MyInteger> list = new RealmList<MyInteger>();
                    Category category = realm.where(Category.class).equalTo("id",page.getCategory_id()).findFirst();
                    //appController.getCategoryByIdDB(page.getCategory().getId_category());
                    showPopup(list, category.getId(), viewFinal, layoutInflater);
                    //						List<RelatedCatIds> list = new ArrayList<RelatedCatIds>(page.getRelated().getRelatedCatIds());
                    //						showPopup_(list, viewFinal, layoutInflater);

                }


            }
        });

        return relatedView;
    }

    public RealmList<Child_pages> fromCollectionToList(Collection<Child_pages> collectionPage){
        RealmList<Child_pages> pages = new RealmList<Child_pages>();
        Child_pages page = null;
        //		loc_grp.addAll(locationGroups);
        //		List<Location> loc = null;
        int i = 0;

        for (Iterator<Child_pages> iterator = collectionPage.iterator(); iterator.hasNext();) {
            page = (Child_pages) iterator.next();
            if(page.isVisible()) {
                pages.add(i,page);
                i++;
            }
        }
        return pages;
    }
    public void showPopup(List<MyInteger> ids_cat, View relatedView, LayoutInflater inflater) {

        List<Category> categories = new ArrayList<Category>();
        for(int i = 0; i< ids_cat.size(); i++) {
            //List<Category> cat = appController.getCategoryDao().queryForEq("id", ids_cat.get(i).getLinked_id());
            RealmResults<Category> cat=realm.where(Category.class).equalTo("id", ids_cat.get(i).getMyInt()).findAll();
            for(int j = 0; j < cat.size(); j++) {
                /*RealmList<Child_pages> pages = fromCollectionToList(cat.get(j).getChildren_pages());
                cat.get(j).setChildren_pages(pages);*/
                categories.add(cat.get(j));

            }
        }
        if (categories.size()>0) {

//				Category category  = categories.get(0);
//				List<CommunElements> pages = new ArrayList<CommunElements>();
//				pages.addAll(category.getChildren_pages1());
            View formulelist = inflater.inflate(R.layout.formule_list, null, false);
//				ListView listView = (ListView)formulelist.findViewById(R.id.formule_list_element);
//				FreeFormulaPagesAdapter adapter = new FreeFormulaPagesAdapter(getActivity(), pages, imageLoader, options, colors, FreeFormulaFragment.this);
//				listView.setAdapter(adapter);

            mHAdapter = new FreeFormulePageAdapter(getActivity(),categories , colors, FreeFormulaFragment.this);
            //mHAdapter.setCallBack((com.euphor.paperpad.adapters.FreeFormulePageAdapter.CallbackRelatedLinks) FreeFormulaFragment.this);
            HeaderListView HLview = (HeaderListView)formulelist.findViewById(R.id.formule_list_element);
            HLview.setBackgroundColor(Color.parseColor("#80ffffff"));//colors.getColor(colors.getTitle_color(), "CC"));
            HLview.setAdapter(mHAdapter);

            formulelist.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            pw = new PopupWindow(formulelist, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
            //pw.setWidth(350);
            // display the popup in the center
            pw.setOutsideTouchable(true);
            //pw.setBackgroundDrawable(null);
            pw.setBackgroundDrawable(new PopupBackround(colors));
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

            Rect location = locateView(relatedView);
            if(isTablet){
                pw.setWidth(350);
                //pw.setHeight(450);
                pw.showAtLocation(view, Gravity.NO_GRAVITY, location.left - 360, location.top - (location.top - location.bottom)/2);
            }
            else {

                pw.showAtLocation(view, Gravity.CENTER, 0, 0);
            }
            //pw.showAtLocation(view, Gravity.NO_GRAVITY, location.left - 360, location.top + (location.top - location.bottom)/2 - (location.top + pw.getHeight())/2 );
            //pw.showAtLocation(view, Gravity.NO_GRAVITY, location.left - 360, location.top - (location.top - location.bottom)/2);
        }

    }



    public void showPopup(List<MyInteger> ids_cat, int id_cat, View relatedView, LayoutInflater inflater) {

        List<Category> categories = new ArrayList<Category>();
        for(int i = 0; i< ids_cat.size(); i++) {
            RealmResults<Category> cat = realm.where(Category.class).equalTo("id", ids_cat.get(i).getMyInt()).findAll();//appController.getCategoryDao().queryForEq("id", ids_cat.get(i).getLinked_id());
            for(int j = 0; j < cat.size(); j++) {
                if(cat.get(j).getId() == id_cat){
               /* RealmList<Child_pages> pages = fromCollectionToList(cat.get(j).getChildren_pages());
                cat.get(j).setChildren_pages(pages);*/
                    categories.add(cat.get(j));

                }
            }
        }
        if (categories.size()>0) {

//				Category category  = categories.get(0);
//				List<CommunElements> pages = new ArrayList<CommunElements>();
//				pages.addAll(category.getChildren_pages1());
            View formulelist = inflater.inflate(R.layout.formule_list, null, false);
//				ListView listView = (ListView)formulelist.findViewById(R.id.formule_list_element);
//				FreeFormulaPagesAdapter adapter = new FreeFormulaPagesAdapter(getActivity(), pages, imageLoader, options, colors, FreeFormulaFragment.this);
//				listView.setAdapter(adapter);

            mHAdapter = new FreeFormulePageAdapter(getActivity(),categories , colors, FreeFormulaFragment.this);
            //mHAdapter.setCallBack((com.euphor.paperpad.adapters.FreeFormulePageAdapter.CallbackRelatedLinks) FreeFormulaFragment.this);
            HeaderListView HLview = (HeaderListView)formulelist.findViewById(R.id.formule_list_element);
            HLview.setBackgroundColor(Color.parseColor("#80ffffff"));//colors.getColor(colors.getTitle_color(), "CC"));
            HLview.setAdapter(mHAdapter);




            pw = new PopupWindow(formulelist, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);

            // display the popup in the center
            pw.setOutsideTouchable(true);
            //pw.setBackgroundDrawable(null);
            pw.setBackgroundDrawable(new PopupBackround(colors));
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

            Rect location = locateView(relatedView);
            if(isTablet){
                pw.setWidth(350);
                //pw.setHeight(450);
                pw.showAtLocation(view, Gravity.NO_GRAVITY, location.left - 360, location.top - (location.top - location.bottom)/2);
            }
            else {
                pw.showAtLocation(view, Gravity.CENTER, 0, 0);
            }
            //pw.showAtLocation(view, Gravity.NO_GRAVITY, location.left - 360, location.top - (location.bottom + location.top + pw.getHeight() + (location.top - location.bottom))/2 );

        }

    }




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
        paint.setColor(colors.getColor(colors.getForeground_color(),ALPHA));
        return paint;
    }

    private ShapeDrawable getNewShape() {
        ShapeDrawable shape = new ShapeDrawable(new RectShape());
        int color1 = colors.getColor(colors.getForeground_color(), "FF");
        int color2 = colors.getColor(colors.getForeground_color(), "AA");
        Shader shader1 = new LinearGradient(0, 0, 0, 50, new int[] {
                color2, color1  }, null, Shader.TileMode.CLAMP);
        shape.getPaint().setShader(shader1);
        shape.getPaint().setColor(Color.WHITE);
        shape.getPaint().setStyle(Paint.Style.FILL);
        return shape;
    }

    private int indexOfPage() {
        int index = -1;
        for (int i = 0; i < pages.size(); i++) {
            if (page.getId_cp()==pages.get(i).getId_cp()) {
                index = i;
            }
        }
        return index;
    }

    private OnClickListener getNavigationBtnListener(final int which) {
        OnClickListener listener = new OnClickListener() {

            @Override
            public void onClick(View v) {
                Child_pages pageTo = null ;
                if (which == PREVIOUS) {
                    pageTo = getPreviousPage(indexCurrent);

                    if (pageTo!=null) {
                        ((MainActivity) getActivity()).extras = new Bundle();
                        ((MainActivity) getActivity()).extras.putInt("page_id",
                                pageTo.getId_cp());
                        FreeFormulaFragment pagesFragment = new FreeFormulaFragment();
                        ((MainActivity) getActivity()).bodyFragment = "FreeFormulaFragment";
                        pagesFragment
                                .setArguments(((MainActivity) getActivity()).extras);
                        getActivity().getSupportFragmentManager()
                                .beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                                .replace(R.id.fragment_container, pagesFragment)
                                .addToBackStack(null).commit();
                    }

                }else if (which == NEXT) {
                    if (indexCurrent == pages.size()-1) {
                        pageTo = pages.get(0);
                    }else {
                        pageTo = pages.get(indexCurrent+NEXT);
                    }
                    pageTo = getNextPage(indexCurrent);

                    if (pageTo!=null) {
                        ((MainActivity) getActivity()).extras = new Bundle();
                        ((MainActivity) getActivity()).extras.putInt("page_id",
                                pageTo.getId_cp());
                        FreeFormulaFragment pagesFragment = new FreeFormulaFragment();
                        ((MainActivity) getActivity()).bodyFragment = "FreeFormulaFragment";
                        pagesFragment
                                .setArguments(((MainActivity) getActivity()).extras);
                        getActivity().getSupportFragmentManager()
                                .beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                                .replace(R.id.fragment_container, pagesFragment)
                                .addToBackStack(null).commit();
                    }

                }


            }
        };
        return listener;
    }

    protected Child_pages getNextPage(int pIndexCurrent) {
        Child_pages pageTo;
        if (pIndexCurrent >= pages.size()-1) {
            pageTo = pages.get(0);
        }else {
            pageTo = pages.get(pIndexCurrent+NEXT);
        }
        while (pageTo == null || (pageTo != null && !pageTo.isVisible())) {
            pageTo = getNextPage(pIndexCurrent+NEXT);

        }
        return pageTo;
    }

    protected Child_pages getPreviousPage(int pIndexCurrent) {
        Child_pages pageTo;
        if (pIndexCurrent>0) {
            pageTo = pages.get(pIndexCurrent+PREVIOUS);
        }else {
            pageTo = pages.get(pages.size()-1);
        }
        while (pageTo == null || (pageTo != null && !pageTo.isVisible())) {
            pageTo = getPreviousPage(pIndexCurrent+PREVIOUS);
        }
        return pageTo;
    }

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
        pw.dismiss();
        objects.remove(indexClicked);
        objects.add(indexClicked, child_pages);
        objects.add(child_pages.getCategory());
        llRelated.removeAllViews();
        drawRelatedElements(objects, layoutInflater);
    }
    @Override
    public void onStop() {
        AppHit hit = new AppHit(System.currentTimeMillis()/1000, time/1000, "sales_page", page.getId());
        ((MyApplication)getActivity().getApplication()).hits.add(hit);
        super.onStop();
    }



}
