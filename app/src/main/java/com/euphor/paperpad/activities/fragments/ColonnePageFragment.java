/**
 * 
 */
package com.euphor.paperpad.activities.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.euphor.paperpad.Beans.MyInteger;
import com.euphor.paperpad.Beans.MyString;
import com.euphor.paperpad.InterfaceRealm.CommunElements1;
import com.euphor.paperpad.R;
import com.euphor.paperpad.R.drawable;
import com.euphor.paperpad.activities.main.MainActivity;
import com.euphor.paperpad.activities.main.MyApplication;
import com.euphor.paperpad.activities.main.YoutubePlayerActivity;
import com.euphor.paperpad.adapters.FormulePagesAdapter;

import com.euphor.paperpad.Beans.CartItem;

import com.euphor.paperpad.Beans.Category;
import com.euphor.paperpad.Beans.Child_pages;
import com.euphor.paperpad.Beans.Contact;
import com.euphor.paperpad.Beans.Formule;
import com.euphor.paperpad.Beans.FormuleElement;
import com.euphor.paperpad.Beans.Illustration;

import com.euphor.paperpad.Beans.Link;
import com.euphor.paperpad.Beans.Linked;
import com.euphor.paperpad.Beans.Parameters;
import com.euphor.paperpad.Beans.Price;
import com.euphor.paperpad.Beans.Related;

import com.euphor.paperpad.utils.Colors;
import com.euphor.paperpad.utils.RelatedItem;
import com.euphor.paperpad.utils.RelatedItem1;
import com.euphor.paperpad.utils.RelatedTitle;
import com.euphor.paperpad.utils.RelatedTitle1;
import com.euphor.paperpad.utils.Utils;
import com.euphor.paperpad.utils.jsonUtils.AppHit;
import com.euphor.paperpad.widgets.ArrowImageView;
import com.euphor.paperpad.widgets.AutoResizeTextView;
import com.euphor.paperpad.widgets.PopupBackround;
//import com.facebook.internal.Utility;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import com.euphor.paperpad.adapters.FormulePagesAdapter.CallbackRelatedLinks;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * @author euphordev02
 *
 */
public class ColonnePageFragment extends Fragment implements CallbackRelatedLinks {


	private static final int FIRST = 0;
	private static final int PREVIOUS = -1;
	private static final int NEXT = 1;
	private static int WIDTH_SWIPE_PICS = 600;
	private static String ALPHA = "CC";
	private String design;

	private int page_id;
	private Child_pages page;
	private Colors colors;
	private RealmList<Child_pages> pages;
	private int indexCurrent;
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
	private LinearLayout choiceHolder;
    public Realm realm;
	public ColonnePageFragment() {
		// TODO Auto-generated constructor stub
	}


	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}


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
		//isTablet = getResources().getBoolean(R.bool.isTablet);
		super.onAttach(activity);
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		setRetainInstance(true);
		super.onCreate(savedInstanceState);

		page_id = getArguments().getInt("page_id");
		((MainActivity) getActivity()).extras = new Bundle();
		((MainActivity) getActivity()).extras.putInt("page_id", page_id);
		((MainActivity) getActivity()).bodyFragment = "ColonnePageFragment";
		time = System.currentTimeMillis();
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.colonne_page_fragment, container, false);
        view.setBackgroundColor(colors.getColor(colors.getBackground_color()));

        realm.beginTransaction();

		choiceHolder = (LinearLayout)view.findViewById(R.id.choicesHolder);
        page = realm.where(Child_pages.class).equalTo("id_cp", page_id).findFirst();
        //page = appController.getChildPageDao().queryForId(page_id);
        LinearLayout brokenTitleHolder = (LinearLayout) view.findViewById(R.id.brokenTitleHolder);
			/*
		 * COMMUN TASKS
		 */
        //arrows
        Paint paint2 = new Paint();
        paint2.setColor(colors.getColor(colors.getTitle_color()));
        ArrowImageView showArrowImg = (ArrowImageView)view.findViewById(R.id.showArrowImg);
        ArrowImageView hideArrowImg = (ArrowImageView)view.findViewById(R.id.hideArrowImg);
        showArrowImg.setPaint(paint2);
        hideArrowImg.setPaint(paint2);

        ((TextView)view.findViewById(R.id.showMoreInfos)).setTextColor(colors.getColor(colors.getTitle_color()));
        ((TextView)view.findViewById(R.id.exitMoreInfos)).setTextColor(colors.getColor(colors.getTitle_color()));

        //fill navigation bar
        Category cate = realm.where(Category.class).equalTo("id", page.getCategory_id()).findFirst();
        RealmList<Child_pages> pagesTmp1 = cate.getChildren_pages();
        pages = new RealmList<>();
        pages.addAll(pagesTmp1);

        if (pages != null && pages.size()>0) {
            for (int i = 0; i < pages.size(); i++) {
                pages.get(i).setSelected(false);
            }
        }
        //page.setSelected(true);
        Category cat = realm.where(Category.class).equalTo("id", page.getCategory_id()).findFirst();
        RealmList<Child_pages> children = getChildrenPages(cat);
        for (Iterator<Child_pages> iterator = children.iterator(); iterator.hasNext();) {
            Child_pages pPage = (Child_pages) iterator.next();
            if (pPage.getId_cp() == page.getId_cp()) {
                pPage.setSelected(true);
            }
            fillNavigationBar(pPage);
        }
        page.setSelected(true);
//		fillNavigationBar(page);

        //Title product
        LayoutParams paramsTxt = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        paramsTxt.setMargins(0, 5, 0, 5);
        if (brokenTitleHolder  != null) {
            if (page.getExtra_fields()==null) {
                TextView titleTV = new TextView(getActivity());
                titleTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 32);
                titleTV.setText(page.getTitle());
                titleTV.setTypeface(MainActivity.FONT_TITLE);
                titleTV.setTextColor(colors.getColor(colors.getTitle_color()));
                titleTV.setGravity(Gravity.CENTER);
                titleTV.setBackgroundColor(colors.getColor(colors.getBackground_color()));
                titleTV.setPadding(5, 5, 5, 5);
                brokenTitleHolder.addView(titleTV, paramsTxt);
            }else {
                String multiline = page.getExtra_fields().getMultiline_title();
                if (multiline != null && multiline.contains("[break]")) {
                    String[] bodies = multiline.split("(\\[)break(\\])");
                    for (int i = 0; i < bodies.length; i++) {
                        String str = bodies[i].replace("(/[break])", "");
                        TextView txt = new TextView(getActivity());
                        txt.setTypeface(MainActivity.FONT_BODY);
                        txt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 32);
                        txt.setText(str);
                        txt.setTextColor(colors.getColor(colors.getTitle_color()));
                        txt.setBackgroundColor(colors.getColor(colors.getBackground_color()));
                        txt.setPadding(5, 5, 5, 5);
                        brokenTitleHolder.addView(txt, paramsTxt);
                    }
                }else {
                    TextView titleTV = new TextView(getActivity());
                    titleTV.setTypeface(MainActivity.FONT_TITLE);
                    titleTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 32);
                    titleTV.setText(page.getTitle());
                    titleTV.setTextColor(colors.getColor(colors.getTitle_color()));
                    titleTV.setGravity(Gravity.CENTER);
                    titleTV.setBackgroundColor(colors.getColor(colors.getBackground_color()));
                    titleTV.setPadding(5, 5, 5, 5);
                    brokenTitleHolder.addView(titleTV, paramsTxt);
                }
            }

        }

//		if (design.equals("formule")) {
//			titleTV.setGravity(Gravity.CENTER);
//		}
//		titleTV.setTextSize(34);
        view.findViewById(R.id.titleProductHolder).setBackgroundColor(colors.getColor(colors.getBackground_color(),ALPHA));
        view.findViewById(R.id.titleProductHolder).setVisibility(View.GONE);
        //background
        WebView introWV = (WebView)view.findViewById(R.id.introWV);
        view.findViewById(R.id.introHolder).setBackgroundColor(colors.getColor(colors.getBackground_color(),ALPHA));
        view.findViewById(R.id.verticalPage).setBackgroundColor(colors.getColor(colors.getBackground_color()));//,ALPHA));

        //view.findViewById(R.id.Category).setVisibility(View.GONE);
        view.findViewById(R.id.navButtons).setVisibility(View.GONE);

        final LinearLayout toogle = (LinearLayout)view.findViewById(R.id.toogle);
        toogle.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
//				view.findViewById(R.id.introWV).setVisibility(View.GONE);
//				view.findViewById(R.id.viewHelper).setVisibility(View.GONE);
                view.findViewById(R.id.introHolder).setVisibility(View.GONE);
                view.findViewById(R.id.viewHelper).setVisibility(View.GONE);
                view.findViewById(R.id.verticalPage).setVisibility(View.VISIBLE);
                v.setVisibility(View.GONE);
            }
        });

        LinearLayout toogleHide = (LinearLayout)view.findViewById(R.id.toogleHide);
        toogleHide.setBackgroundColor(colors.getColor(colors.getForeground_color(),ALPHA));

        toogleHide.setOnClickListener(new OnClickListener() {

        @Override
        public void onClick(View v) {
//				view.findViewById(R.id.introWV).setVisibility(View.VISIBLE);
//				view.findViewById(R.id.viewHelper).setVisibility(View.VISIBLE);
            view.findViewById(R.id.introHolder).setVisibility(View.VISIBLE);
            view.findViewById(R.id.viewHelper).setVisibility(View.VISIBLE);
            view.findViewById(R.id.verticalPage).setVisibility(View.GONE);
            toogle.setVisibility(View.VISIBLE);
        }
    });


        //Image Product
        final RealmList<Illustration> images=new RealmList<>();
        images.add(page.getIllustration());
        if (images.size()>0 && (ImageView)view.findViewById(R.id.ProductIMG)!= null) {
            Illustration imagePage = images.iterator().next();
            illust = page.getIllustration();
//			path = !illust.getPath().isEmpty()?"file:///"+illust.getPath():illust.getLink();
            ImageView productImage = (ImageView)view.findViewById(R.id.ProductIMG);
            productImage.setScaleType(ScaleType.CENTER_CROP);
            if (!illust.getPath().isEmpty()) {
                path = illust.getPath();
                Glide.with(getActivity()).load(new File(path)).into(productImage);
            }else {
                path = illust.getLink();
                Glide.with(getActivity()).load(path).into(productImage);
            }
//			imageLoader.displayImage(path, productImage);
        }

        // Arrows next and previous coloring and actions !
        ArrowImageView previousIMG = (ArrowImageView)view.findViewById(R.id.PreviousIMG);
        Paint paint = getNewPaint();
        previousIMG.setPaint(paint );
        ArrowImageView nextIMG = (ArrowImageView)view.findViewById(R.id.NextIMG);
        nextIMG.setPaint(paint );
        Category category = realm.where(Category.class).equalTo("id", page.getCategory_id()).findFirst();
        RealmList<Child_pages> pagesTmp = category.getChildren_pages();

        LinearLayout navButtons = (LinearLayout)view.findViewById(R.id.navButtons);
        LinearLayout backPrev = (LinearLayout)view.findViewById(R.id.backPrevious);
        LinearLayout backNext = (LinearLayout)view.findViewById(R.id.backNext);
        if (pagesTmp.size()>1) {

            pages = new RealmList<>();
            pages.addAll(pagesTmp);
            indexCurrent = indexOfPage();
            backPrev.setOnClickListener(getNavigationBtnListener(PREVIOUS));
            backNext.setOnClickListener(getNavigationBtnListener(NEXT));
        }else {

            navButtons.setVisibility(View.GONE);
        }

        //category parent
/*		RelativeLayout navigationHolder = (RelativeLayout)view.findViewById(R.id.navigationHolder);
		TextView categoryTv = (TextView)view.findViewById(R.id.categorieTV);
		categoryTv.setTypeface(MainActivity.FONT_TITLE);
		categoryTv.setText(category.getTitle());
		categoryTv.setTextColor(colors.getColor(colors.getForeground_color()));
		LinearLayout categoryHolder = (LinearLayout)view.findViewById(R.id.Category);
		categoryHolder.setBackgroundColor(colors.getColor(colors.getBackground_color(), "66"));
		categoryId = category.getId();
		categoryHolder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Category category = appController.getCategoryById(categoryId);

				((MainActivity)getActivity()).openCategory(category);


			}
		});*/

        view.findViewById(R.id.Category).setVisibility(View.GONE);

        //share button
        LinearLayout shareHolder = (LinearLayout)view.findViewById(R.id.shareHolder);
        if(category.getShare_button().equals("1")) {
            shareHolder.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
//				String path = "/mnt/sdcard/dir1/sample_1.jpg";
                    Intent share = new Intent(Intent.ACTION_SEND);
                    MimeTypeMap map = MimeTypeMap.getSingleton(); //mapping from extension to mimetype
                    String ext = path.substring(path.lastIndexOf('.') + 1);
                    String mime = map.getMimeTypeFromExtension(ext);
                    share.setType(mime); // might be text, sound, whatever
                    Uri uri;
                    if (path.contains("Paperpad/")) {
                        uri = Uri.fromFile(new File(path));
                    } else {
                        uri = Uri.parse(path);
                    }
                    share.putExtra(Intent.EXTRA_SUBJECT, page.getTitle());
                    share.putExtra(Intent.EXTRA_TEXT, page.getIntro());
                    share.putExtra(Intent.EXTRA_STREAM, uri);//using a string here didnt work for me
                    Log.d("", "share " + uri + " ext:" + ext + " mime:" + mime);
                    startActivity(Intent.createChooser(share, "share"));

                }
            });
        }else{
            view.findViewById(R.id.navigationHolderForStroke).setVisibility(View.GONE);
            view.findViewById(R.id.navigationHolder).setVisibility(View.GONE);
            shareHolder.setVisibility(View.GONE);
        }

        // Long description under the product image
        WebView longdescWV = (WebView)view.findViewById(R.id.LongDescWV);

        longdescWV.setBackgroundColor(Color.TRANSPARENT);
        introWV.setBackgroundColor(Color.TRANSPARENT);
        if (view.findViewById(R.id.tempLL) != null) {
            view.findViewById(R.id.tempLL).setBackgroundColor(colors.getColor(colors.getBackground_color(), ALPHA));
        }else {
            longdescWV.setBackgroundColor(colors.getColor(colors.getBackground_color(), ALPHA));
        }
//		StringBuilder htmlString = populateWebview("14sp");
        WebSettings webSettings = longdescWV.getSettings();

        webSettings.setTextSize(WebSettings.TextSize.NORMAL);
        WebSettings webSettings2 = introWV.getSettings();
        webSettings2.setGeolocationEnabled(true);
        webSettings2.setTextSize(WebSettings.TextSize.NORMAL);
        longdescWV.loadDataWithBaseURL(null, populateWebview("16", false).toString(), "text/html", "UTF-8", null);
        introWV.loadDataWithBaseURL(null, populateWebview("22", true).toString(), "text/html", "UTF-8", null);

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
        Drawable cadreDrawable = getResources().getDrawable(drawable.cadre);
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

        //prices related to this product
        parameters = null;
        parameters = realm.where(Parameters.class).findFirst();//appController.getParametersDao().queryForId(1);

        RealmList<Price> prices = page.getPrices();
        LinearLayout pricesContainer = (LinearLayout)view.findViewById(R.id.PricesHolder);
        if (prices.size()>0) {

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


                                if (parameters != null) {
                                    if (parameters.isShow_cart()) {

                                        Category category = null;
                                        if (page.getCategory() != null && page.getCategory().isNeeds_stripe_payment()) {
                                            category = page.getCategory();
                                        }else {
                                            //AppController controller = new AppController(mainActivity);
                                            category =  realm.where(Category.class).equalTo("id", page.getCategory_id()).findFirst();//controller.getCategoryById(page.getCategory_id());
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
                pricesContainer.addView(priceElement);
            }

        }else {
                pricesContainer.setVisibility(View.GONE);
        }

        //Utilities
        ShapeDrawable shape = getNewShape();
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
        //navigationHolder.setBackgroundColor(colors.getColor(colors.getBackground_color(),ALPHA));

        navButtons.setBackgroundColor(colors.getColor(colors.getBackground_color(),"66"));
        TextView shareLabelTV = (TextView)view.findViewById(R.id.shareLabelTV);
        shareLabelTV.setTextColor(colors.getColor(colors.getForeground_color()));
        ImageView shareIMG = (ImageView)view.findViewById(R.id.shareIMG);
        Drawable drawableShare = getResources().getDrawable(drawable.feed_icon);
        drawableShare.setColorFilter(new PorterDuffColorFilter(colors.getColor(colors.getForeground_color()) , Mode.MULTIPLY));
        shareIMG.setBackgroundDrawable(drawableShare);


        view.findViewById(R.id.PricesHolderAbove).setBackgroundColor(colors.getColor(colors.getBackground_color(), ALPHA));
        llRelated.setBackgroundColor(colors.getColor(colors.getBackground_color(), ALPHA));

        realm.commitTransaction();
        /*** END Coloring ***/

		/*
		 * END COMMUN TASKS
		 */


        //		for (int i = 0; i < 5; i++) {
//			addItemToCart(null);
//		}
		return view;
	}

	
	/** a method to fill the upper bar where we choose the {@link Child_pages}
	 * @param child_page
	 */
	private void fillNavigationBar( Child_pages child_page) {
		TextView child_pageTxt = new TextView(getActivity());
		child_pageTxt.setTypeface(MainActivity.FONT_TITLE);
		child_pageTxt.setPadding(10, 5, 10, 5);
		if (child_page.isSelected() ) {
			LinearLayout.LayoutParams txtParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
			txtParams.gravity = Gravity.CENTER;
			child_pageTxt.setGravity(Gravity.CENTER);
//			txtParams.setMargins(10, 5, 10, 5);
			child_pageTxt.setText(child_page.getTitle().toUpperCase());
			child_pageTxt.setTextColor(colors.getColor(colors.getBackground_color()));
//			Drawable selectDrawable = getResources().getDrawable(R.drawable.back_choices_final);
//			selectDrawable.setColorFilter(colors.getColor(colors.getTitle_color()), Mode.MULTIPLY);
			child_pageTxt.setBackgroundColor(colors.getColor(colors.getTitle_color()));
			child_pageTxt.setTag(child_page);
			choiceHolder.addView(child_pageTxt, txtParams);
			
		}else {
			
			LinearLayout.LayoutParams txtParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
//			txtParams.setMargins(10, 5, 10, 5);
			
			child_pageTxt.setGravity(Gravity.CENTER);
			txtParams.gravity = Gravity.CENTER;
			child_pageTxt.setText((child_page.getTitle()).toUpperCase());
			child_pageTxt.setTextColor(colors.getColor(colors.getTitle_color()));
//			Drawable selectDrawable = getResources().getDrawable(R.drawable.back_empty);
//			selectDrawable.setColorFilter(colors.getColor(colors.getForeground_color()), Mode.MULTIPLY);
//			child_pageTxt.setBackgroundDrawable(selectDrawable);
			child_pageTxt.setTag(child_page);
			choiceHolder.addView(child_pageTxt, txtParams);
		}
		
		child_pageTxt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
                realm.beginTransaction();
				Child_pages aPage = (Child_pages)v.getTag();
				Category cat_ = realm.where(Category.class).equalTo("id",aPage.getCategory_id()).findFirst();
                List<Child_pages> pages = getChildrenPages(cat_);
				if (pages != null && pages.size()>0) {
					for (int i = 0; i < pages.size(); i++) {
						pages.get(i).setSelected(false);
					}
					aPage.setSelected(true);
					choiceHolder.removeAllViews();
					for (int i = 0; i < pages.size(); i++) {

						fillNavigationBar(pages.get(i));
					}
					((MainActivity) getActivity()).extras = new Bundle();
					((MainActivity) getActivity()).extras.putInt("page_id",
							aPage.getId_cp());
					ColonnePageFragment pagesFragment = new ColonnePageFragment();
					((MainActivity) getActivity()).bodyFragment = "ColonnePageFragment";
					pagesFragment
					.setArguments(((MainActivity) getActivity()).extras);
					getActivity().getSupportFragmentManager()
					.beginTransaction().replace(R.id.fragment_container, pagesFragment)
					.addToBackStack(null).commit();
					//				hsvLayout.removeAllViews();
				}
				
				
				realm.commitTransaction();
			}
		});
	}
	
	
	public RealmList<Child_pages> getChildrenPages(Category category) {
		//category = realm.where(Category.class).equalTo("id_c",category.getId_c()).findFirst();//appController.getCategoryByIdDB(category.getId_category());
		RealmList<Child_pages> result = new RealmList<Child_pages>();
        if(category == null) return result;
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

	/**
	 * @param taille 
	 * @return
	 */
	public StringBuilder populateWebview(String taille, boolean justify) {
		StringBuilder htmlString = new StringBuilder();
		int[] colorText = Colors.hex2Rgb(colors.getTitle_color());
//		int sizeText = getResources().getDimensionPixelSize(R.dimen.priceTV);
//		int sizeText = (int) Utils.spToPixels(getActivity(), (float) 14);
		String justifyStmt = "style=\"text-align:center\"";
		if (!justify) {
			justifyStmt = "";
            colorText = Colors.hex2Rgb(colors.getBody_color());
            htmlString.append(Utils.paramBodyHTMLConf(colorText, "no-justify", taille));
            String body = page.getBody();
            htmlString.append(body);
            view.findViewById(R.id.truncate_holder).setVisibility(View.GONE);
        }else {
            htmlString.append(Utils.paramBodyHTMLConf(colorText, "center", taille));
//			String body = page.getBody().replace("[break]", "");
            String body = page.getBody();
            if (body.contains("[break]")) {
                String[] bodies = body.split("(\\[)break(\\])");
                body = bodies[0].replace("(/[break])", "");
/*                ImageView truncateIcon = (ImageView) view.findViewById(R.id.truncate_icon);
                if (truncateIcon != null) {
                    view.findViewById(R.id.truncate_holder).setVisibility(View.VISIBLE);
                    truncateIcon.setVisibility(View.VISIBLE);
                    truncateIcon.getDrawable().setColorFilter(new PorterDuffColorFilter(colors.getColor(colors.getTitle_color(), "AA"), PorterDuff.Mode.MULTIPLY));
                    truncateIcon.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            int[] colorText = Colors.hex2Rgb(colors.getTitle_color());
                            AlertDialog.Builder builder;

                            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            View layout = inflater.inflate(R.layout.dialog_truncate_body, null, false);
                            layout.setBackgroundColor(colors.getColor(colors.getBackground_color()));

                            ImageView image = (ImageView) layout.findViewById(R.id.truncate_body_icon);
                            image.getDrawable().setColorFilter(new PorterDuffColorFilter(colors.getColor(colors.getTitle_color(), "AA"), PorterDuff.Mode.MULTIPLY));

                            WebView completeBodyWB = (WebView) layout.findViewById(R.id.LongDescWV_truncate);
                            completeBodyWB.setBackgroundColor(Color.TRANSPARENT);
                            StringBuilder htmlStringTruncate = new StringBuilder();
                            htmlStringTruncate.append(Utils.paramBodyHTML(colorText));
                            String bodyComplet = page.getBody().replace("[break]", "");
                            htmlStringTruncate.append(bodyComplet);
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
                }*/
            }
            htmlString.append(body);
        }
			htmlString.append("</div></body></html>");
		return htmlString;
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

	/*
	* fill the cart with the {@link CartItem} taken from the database
	*/
	private void fillCart(){
		RealmResults<CartItem> cartItems ;// = new ArrayList<CartItem>();
        cartItems =realm.where(CartItem.class).findAll();
        //cartItems = appController.getCartItemDao().queryForAll();
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
		SlidingMenu menu = mainActivity.getMenu();
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
				try {
					if (!formule.getIllustration().getPath().isEmpty()) {
						Glide.with(getActivity()).load(new File(formule.getIllustration().getPath())).into(imgFormule);
					}else {
						Glide.with(getActivity()).load(formule.getIllustration().getLink()).into(imgFormule);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
//				String path = !formule.getIllustration().getPath().isEmpty() ? "file:///"
//						+ formule.getIllustration().getPath()
//						: formule.getIllustration().getLink();
//						imageLoader.displayImage(path, imgFormule, options);
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
				subSum.setText(cartItem.getName());
				subSum.setTypeface(MainActivity.FONT_BODY);
				LinearLayout subDelete = (LinearLayout)subItem.findViewById(R.id.deleteContainer);
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
		RealmList<FormuleElement> elements = new RealmList<FormuleElement>();
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
		boolean result = true;
		for (int i = 0; i < objects.size(); i++) {
			if (objects.get(i) instanceof Category) {
				result = false;
				return result;
			}
		}


		return result;
	}

	private ArrayList<Object> getAllRelatedElements(Related related, Linked linked){

        ArrayList<Object> objects = new ArrayList<Object>();
        RealmList<MyInteger> relatedCats = related.getList();
        RealmList<MyString> relatedPages = related.getList_pages();
        RealmList<Link> links = linked.getLinks();


		if (!related.getTitle().isEmpty()) {
            RelatedTitle1 relatedTitle = (RelatedTitle1)related;
            if (relatedPages.size() > 0) {

                objects.add(relatedTitle);
            }

		}
		if (relatedPages.size() > 0) {
			for (Iterator<MyString> iterator = relatedPages.iterator(); iterator.hasNext();) {

                MyString relatedPageId = (MyString) iterator.next();
				RealmResults<Child_pages> child_pages ;// = new ArrayList<Child_pages>();
                //child_pages = appController.getChildPageDao().queryForEq("id",relatedPageId.getLinked_id());
                child_pages = realm.where(Child_pages.class).equalTo("id", Integer.parseInt(relatedPageId.getMyString())).findAll();
                if (child_pages.size() > 0) {
					RelatedItem1 item = (RelatedItem1)child_pages.get(0);
					objects.add(item);
				}
			}
		}
		if (!related.getTitle_categories().isEmpty()) {
            /***************/
            {
                RelatedTitle1 relatedTitle = (RelatedTitle1)related;

                if (relatedCats.size() > 0) {

                    objects.add(relatedTitle);
                }

            }


		}
		if (relatedCats.size() > 0) {
			for (Iterator<MyInteger> iterator = relatedCats.iterator(); iterator
					.hasNext();) {
                MyInteger relatedCatIds = (MyInteger) iterator
						.next();
				final int id_related_cat = relatedCatIds.getMyInt();
				RealmResults<Category>  categories = realm.where(Category.class).equalTo("id", id_related_cat).findAll();
                // appController.getCategoryDao().queryForEq("id", relatedCatIds.getLinked_id());
                if (categories.size() > 0) {
					RelatedItem1 item = (RelatedItem1)categories.get(0);
					objects.add(item);
				}
			}
		}

		if (related.getContact_form()!=null) {
			List<Contact> list = new ArrayList<Contact>();
            list = realm.where(Contact.class).equalTo("id", related.getContact_form().getId()).findAll();
            //appController.getContactDao().queryForEq("id", related.getContact_form().getId());
            if (list.size()>0) {
				RelatedItem1 item = (RelatedItem1)list.get(0);
				objects.add(item);
			}

		}
		if (!linked.getTitle().isEmpty()) {

            RelatedTitle1 relatedTitle = (RelatedTitle1)linked;
			if (links.size() > 0) {

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

			if (objects.get(i) instanceof RelatedTitle) {

				RelatedTitle1 item = (RelatedTitle1)objects.get(i);
				if (toggle) {
					//item.setPage(false);

				}
				View relatedTitleView = inflater.inflate(R.layout.related_title, null, false);
				TextView relatedTitleTV = (TextView) relatedTitleView.findViewById(R.id.title_related);
				relatedTitleTV.setTypeface(MainActivity.FONT_BODY);
				if (item.getRelatedTitle() != null) {
					relatedTitleTV.setText(item.getRelatedTitle());
				}else {
					relatedTitleView.setVisibility(View.GONE);
				}
				relatedTitleTV.setTextColor(colors.getColor(colors.getBackground_color()));
				LinearLayout titleRelatedLL = (LinearLayout) relatedTitleView
						.findViewById(R.id.title_related_LL);
				titleRelatedLL.setBackgroundDrawable(getNewShape());
				toggle = true;
				llRelated.addView(relatedTitleView);
			}
			if (objects.get(i) instanceof RelatedItem) {
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
        arrowImg.setLayoutParams(new LayoutParams(26, 26));
		ImageView imgPage = (ImageView) relatedView.findViewById(R.id.imgCategory);
			relatedDesc.setVisibility(View.GONE);
			titleTv.setText(item.getItemTitle1());

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
			relatedDesc.setTextAppearance(getActivity(), android.R.style.TextAppearance_Small);
            relatedDesc.setTypeface(MainActivity.FONT_BODY);
			relatedDesc.setTextColor(colors.getColor(colors.getBody_color()));
			relatedDesc.setVisibility(View.VISIBLE);
			//relatedDesc.setTypeface(relatedDesc.getTypeface(), Typeface.NORMAL);
		}

		if (!noImageUse) {
			Object illustration = item.getItemIllustration1();
			if (illustration != null) {

				if (illustration instanceof Illustration) {
					Illustration image = (Illustration) illustration;
					String path = "";
					if (!illust.getPath().isEmpty()) {
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

						if (!category.getDisplay_type().equals("multi_select")) {
							((MainActivity)getActivity()).openCategory(category);

						}else {
							MultiSelectPagesFragment categoryFragment = new MultiSelectPagesFragment();
							((MainActivity) getActivity()).bodyFragment = "CategoryFragment";
							// In case this activity was started with special instructions from an Intent,
							// pass the Intent's extras to the fragment as arguments
							((MainActivity) getActivity()).extras = new Bundle();
							((MainActivity) getActivity()).extras.putInt(
									"Category_id", category.getId());
							categoryFragment
							.setArguments(((MainActivity) getActivity()).extras);
							// Add the fragment to the 'fragment_container' FrameLayout
							getActivity()
							.getSupportFragmentManager()
							.beginTransaction()
							.replace(R.id.fragment_container,
									categoryFragment)
									.setTransition(
											FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
											.addToBackStack(null).commit();
						}
				}else if (item instanceof Child_pages) {
					Child_pages page  =(Child_pages)item;
						((MainActivity)getActivity()).openPage(page);
				}else if (item instanceof Contact) {
					int sid = 0;
					if (page.getCategory()!=null && page.getCategory().getSection()!=null) {
						sid = page.getCategory().getSection().getId_s();
					}
					FormContactFragment forFrag = FormContactFragment.newInstance();
					((MainActivity) getActivity()).bodyFragment = "CategoryFragment";
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

	public void showPopup(int id_cat, View relatedView, LayoutInflater inflater) {

        RealmResults<Category> categories=  realm.where(Category.class).equalTo("id",id_cat).findAll();
        //List<Category> categories = appController.getCategoryDao().queryForEq("id", id_cat);
        if (categories.size()>0) {
            Category category  = categories.get(0);
            List<CommunElements1> pages = new ArrayList<CommunElements1>();
            pages.addAll(category.getChildren_pages());
            View formulelist = inflater.inflate(R.layout.formule_list, null, false);
            ListView listView = (ListView)formulelist.findViewById(R.id.formule_list_element);
            listView.setBackgroundColor(Color.parseColor("#80ffffff"));//colors.getColor(colors.getTitle_color(), "CC"));
        	FormulePagesAdapter adapter = new FormulePagesAdapter(getActivity(), pages, colors, ColonnePageFragment.this);
            listView.setAdapter(adapter);
            pw = new PopupWindow(formulelist, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
            // display the popup in the center
            pw.setOutsideTouchable(true);
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

            //				Rect locationPopo = locateView(formulelist);
            //
            //				Rect locationList = locateView(formulelist);
            pw.showAtLocation(view, Gravity.TOP|Gravity.RIGHT, location.left, (location.bottom+location.top)/2);
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
					if (indexCurrent>0) {
						pageTo = pages.get(indexCurrent+PREVIOUS);
					}else {
						pageTo = pages.get(pages.size()-1);
					}
					pageTo = getPreviousPage(indexCurrent);

					if (pageTo!=null) {
						((MainActivity) getActivity()).extras = new Bundle();
						((MainActivity) getActivity()).extras.putInt("page_id",
								pageTo.getId_cp());
						ColonnePageFragment pagesFragment = new ColonnePageFragment();
						((MainActivity) getActivity()).bodyFragment = "ColonnePageFragment";
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
						ColonnePageFragment pagesFragment = new ColonnePageFragment();
						((MainActivity) getActivity()).bodyFragment = "ColonnePageFragment";
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


	public void onChildPageClick(Child_pages child_pages) {
		Log.i("child_pages", ""+child_pages.toString());
		pw.dismiss();
		objects.remove(indexClicked);
		objects.add(indexClicked, child_pages);
		llRelated.removeAllViews();
		drawRelatedElements(objects, layoutInflater);
		//		clickedView.refreshDrawableState();
	}

	@Override
	public void onStop() {
		AppHit hit = new AppHit(System.currentTimeMillis()/1000, time/1000, "sales_page", page.getId());
		((MyApplication)getActivity().getApplication()).hits.add(hit);
		super.onStop();
	}

}
