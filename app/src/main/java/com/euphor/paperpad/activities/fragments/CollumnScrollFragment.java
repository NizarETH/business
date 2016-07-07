package com.euphor.paperpad.activities.fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.euphor.paperpad.Beans.MyInteger;
import com.euphor.paperpad.Beans.MyString;
import com.euphor.paperpad.Beans.Parameters;
import com.euphor.paperpad.R;
import com.euphor.paperpad.activities.main.MainActivity;
import com.euphor.paperpad.activities.main.YoutubePlayerActivity;
import com.euphor.paperpad.Beans.Category;
import com.euphor.paperpad.Beans.Child_pages;
import com.euphor.paperpad.Beans.Contact;
import com.euphor.paperpad.Beans.Illustration;
import com.euphor.paperpad.Beans.Link;
import com.euphor.paperpad.Beans.Linked;
import com.euphor.paperpad.Beans.Location;
import com.euphor.paperpad.Beans.Locations_group;
import com.euphor.paperpad.Beans.Related;
import com.euphor.paperpad.Beans.RelatedContactForm;
import com.euphor.paperpad.Beans.RelatedLocation;
import com.euphor.paperpad.Beans.Section;

import com.euphor.paperpad.utils.Colors;
import com.euphor.paperpad.utils.RelatedItem1;
import com.euphor.paperpad.utils.RelatedTitle1;
import com.euphor.paperpad.utils.Utils;
import com.euphor.paperpad.widgets.ArrowImageView;
import com.euphor.paperpad.widgets.HorizontalScrollViewExt;
//import com.facebook.Session;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by Euphor on 06/03/15.
 */
public class CollumnScrollFragment extends Fragment {



    private Colors colors;
    //private Section section;
    private boolean isTablet;
    private LayoutInflater mInflater;
    private String title;
    private Collection<Category> categories;
    private int index = 0;
    private View.OnClickListener listener;
    private LinearLayout categoriesContainer;
    //private WebView longdescWV;
    private LinearLayout relatedWebViewContainer;
    //private Category cat;
    public Realm realm;

    public static CollumnScrollFragment create(String title, Collection<Category> categories){
        CollumnScrollFragment f = new CollumnScrollFragment();
        f.setTitle(title);
        f.setCategories(categories);
        return f;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        realm = Realm.getInstance(getActivity());
     //new AppController(getActivity());

        if(categories == null || categories.isEmpty()) {
            int section_id = getArguments().getInt("Section_id");

            if (section_id == 0) {
                section_id = getArguments().getBundle("EXTRAS").getInt("Section_id");
            }

            //List<Section> sections = appController.getSectionsDao().queryForEq("id", section_id);
            RealmResults<Section> sections = realm.where(Section.class).equalTo("id", section_id).findAll();
            if (sections != null && !sections.isEmpty()) {
                Section section = sections.get(0);
                title = section.getName();
                categories = section.getCategories();
            }

        }

        if(getArguments() != null)
        index = getArguments().getInt("index", 0);

        colors = ((MainActivity)activity).colors;

        Parameters ParamColor = realm.where(Parameters.class).findFirst();
        if (colors==null) {
            colors = new Colors(ParamColor);
        }
        ((MainActivity)activity).bodyFragment = "CollumnScrollFragment";
        if(((MainActivity)activity).extras == null)
            ((MainActivity)activity).extras = new Bundle();
        ((MainActivity)getActivity()).extras.putInt("index", index);


        isTablet = Utils.isTablet(getActivity());
        //time = System.currentTimeMillis();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mInflater = inflater;
        View view = inflater.inflate(R.layout.collumn_scroll_fragment, container, false);
        view.setBackgroundColor(colors.getColor(colors.getBackground_color()));

        GradientDrawable drawable_bar = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[] {colors.getColor(colors.getBackground_color()), colors.getColor(colors.getBackground_color(), "40")});
        drawable_bar.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        drawable_bar.setCornerRadius(0f);
        drawable_bar.setColorFilter(colors.getColor(colors.getBackground_color(), "40"), android.graphics.PorterDuff.Mode.OVERLAY);

        view.findViewById(R.id.headerMask).setBackgroundDrawable(drawable_bar);

        TextView titleCollumnScroll = (TextView)view.findViewById(R.id.titleCollumnScroll);
        titleCollumnScroll.setTextAppearance(getActivity(), android.R.style.TextAppearance_DeviceDefault_Large);
        titleCollumnScroll.setTypeface(MainActivity.FONT_BOLD, Typeface.BOLD);
        titleCollumnScroll.setText("" +title);
        titleCollumnScroll.setTextColor(colors.getColor(colors.getTitle_color()));

        LinearLayout choicesHolder = (LinearLayout)view.findViewById(R.id.choicesHolder);
        //List<Category> categories = new ArrayList<Category>();
        if(categories != null && categories.size() > 0){


            final com.euphor.paperpad.widgets.HorizontalScrollViewExt hSV = (com.euphor.paperpad.widgets.HorizontalScrollViewExt)view.findViewById(R.id.SVchoicesHolder);
            hSV.setFillViewport(true);
            hSV.setOverScrollMode(HorizontalScrollView.OVER_SCROLL_IF_CONTENT_SCROLLS);
            hSV.computeScroll();
            hSV.setScrollViewListener(new HorizontalScrollViewExt.ScrollViewListener() {
                @Override
                public void onScrollChanged(HorizontalScrollViewExt scrollView, int x, int y, int oldx, int oldy) {
                    realm.beginTransaction();
                    int offsetPos = x;//Math.abs(x - oldx);
                    int offsetItem = (int) Utils.dpToPixels(getActivity(), 600f);
                    offsetPos += 0.75f * offsetItem;
                    int indexPos = offsetPos / offsetItem;
                    if(indexPos != index){
                        index = indexPos;
                        int i = 0;
                        categoriesContainer.removeAllViews();

                        for (Iterator<Category> iterator = /*section.getCategories1()*/categories.iterator(); iterator
                                .hasNext();) {
                            Category category = iterator.next();
                            category.setSelected(false);
                            if(i == index){
                                category.setSelected(true);
                            }
                            fillNavigationBar(i, category);
                            i++;
                        }
                        ((MainActivity) getActivity()).extras.putInt("index", i);
                    }
                    realm.commitTransaction();
                }
            });

            categoriesContainer = (LinearLayout)view.findViewById(R.id.categoriesContainer);

            listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                   realm.beginTransaction();
                    index = (int) v.getTag();
                    float xCollumnPos = index * Utils.dpToPixels(getActivity(), 600f);
                    hSV.smoothScrollTo((int) xCollumnPos, 0);

                    int i = 0;
                    categoriesContainer.removeAllViews();

                    for (Iterator<Category> iterator = /*section.getCategories1()*/categories.iterator(); iterator
                            .hasNext();) {
                        Category category = iterator.next();
                        category.setSelected(false);
                        if(i == index){
                            category.setSelected(true);
                        }
                        fillNavigationBar(i, category);
                        i++;
                    }
                    ((MainActivity) getActivity()).extras.putInt("index", i);

                realm.commitTransaction();

                }
            };

            Category cat = categories.iterator().next();

            Section section = cat.getSection();

            if(((MainActivity)getActivity()).extras == null)
                ((MainActivity)getActivity()).extras = new Bundle();

            Illustration illust = null;

            if(section != null) {

                ((MainActivity) getActivity()).extras.putInt("Section_id", section.getId());
                illust = section.getIllustrationObj();
            }else{

                illust = cat.getIllustration();
            }



            if(illust != null) {
                ImageView bg_image = (ImageView)view.findViewById(R.id.bg_image);
                if(!illust.getPath().isEmpty()) {
                    Glide.with(getActivity()).load(new File(illust.getPath())).into(bg_image);
                }else if(!illust.getLink().isEmpty()){
                    Glide.with(getActivity()).load(illust.getLink()).into(bg_image);
                }
                bg_image.setAlpha(0.2f);

            }
            int i = 0;
             realm.beginTransaction();
            for (Iterator<Category> iterator = /*section.getCategories1()*/categories.iterator(); iterator
                    .hasNext();) {
                Category category = iterator.next();
                if(i == index){
                    category.setSelected(true);
                }
                fillNavigationBar(i, category);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.rightMargin = 50;

                View collumnContainerView = fillCategoryCollumn(category);
                choicesHolder.addView(collumnContainerView, params);

                i++;
            }
            realm.commitTransaction();
        }


        return view;
    }

    /** a method to fill the upper bar where we choose the {@link com.euphor.paperpad.beans.CategoriesMyBox}
     * @param category
     */
    private void fillNavigationBar(int i, Category category) {
        TextView catTxt = new TextView(getActivity());
        catTxt.setTextAppearance(getActivity(), android.R.style.TextAppearance_DeviceDefault_Small);
        catTxt.setTypeface(MainActivity.FONT_BOLD, Typeface.BOLD);
        catTxt.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams paramTxt = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        paramTxt.rightMargin = 5;
        paramTxt.gravity = Gravity.CENTER_VERTICAL;
        catTxt.setLayoutParams(paramTxt);
        catTxt.setPadding(10, 10, 10, 10);
        catTxt.setText(category.getTitle().toUpperCase());
        if(category.isSelected()){

            catTxt.setBackgroundColor(colors.getColor(colors.getForeground_color()));
            catTxt.setTextColor(colors.getColor(colors.getBackground_color()));
        }else{
            StateListDrawable stateListDrawable = new StateListDrawable();
            stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(colors.getColor(colors.getForeground_color())));//colors.makeGradientToColor(colors.getTabs_background_color()));//
            stateListDrawable.addState(new int[]{android.R.attr.state_selected}, new ColorDrawable(colors.getColor(colors.getForeground_color()))); //colors.makeGradientToColor(colors.getTabs_background_color()));//new ColorDrawable(colors.getColor(colors.getTitle_color())));
            stateListDrawable.addState(new int[]{}, new ColorDrawable(colors.getColor(colors.getTitle_color(), "30"))); //colors.makeGradientToColor(colors.getTabs_background_color()));//new ColorDrawable(colors.getColor(colors.getTitle_color())));
            catTxt.setBackgroundDrawable(stateListDrawable);

            //catTxt.setBackgroundColor(colors.getColor(colors.getTitle_color(), "30"));
            catTxt.setTextColor(new ColorStateList(
                    new int[][]{
                            new int[]{android.R.attr.state_selected},
                            new int[]{android.R.attr.state_pressed},
                            new int[]{}},
                    new int[]{
                            colors.getColor(colors.getBackground_color()),
                            colors.getColor(colors.getBackground_color()),
                            colors.getColor(colors.getTitle_color())}));
        }

/*        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.rightMargin = 50;*/

        catTxt.setTag(i/*collumnContainerView.getX()*/);
        catTxt.setOnClickListener(listener);
        categoriesContainer.addView(catTxt, paramTxt);
    }



    private View fillCategoryCollumn(Category category){
        View container = mInflater.inflate(R.layout.collumn_container, null, false);
        LinearLayout categoryCollumnContainer = (LinearLayout)container.findViewById(R.id.categoryCollumnContainer);

        if (category.getChildren_pages().size()>0) {
            for (Iterator<Child_pages> iterator = category.getChildren_pages().iterator(); iterator.hasNext();) {
                Child_pages page =  iterator.next();
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) Utils.dpToPixels(getActivity(), 550f), LinearLayout.LayoutParams.WRAP_CONTENT);
                params.bottomMargin = 20;
                categoryCollumnContainer.addView(fillCollumnItem(page), params);
            }
        }

        return container;
    }

    private View fillCollumnItem(final Child_pages page) {


        View container = mInflater.inflate(R.layout.collumn_scroll_item, null, false);
        container.setBackgroundColor(colors.getColor(colors.getBackground_color()));

        final ImageView hasDescriptionIcon = (ImageView)container.findViewById(R.id.hasDescriptionIcon);
        hasDescriptionIcon.setBackgroundColor(colors.getColor(colors.getForeground_color()));
        hasDescriptionIcon.setImageResource(R.drawable.ic_add);
        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(relatedWebViewContainer != null){
                    relatedWebViewContainer.removeViewAt(0);
                    relatedWebViewContainer.setVisibility(View.GONE);
                }
                LinearLayout webViewContainer = (LinearLayout)v.findViewById(R.id.webViewContainer);
                relatedWebViewContainer = (LinearLayout)v.findViewById(R.id.relatedWebViewContainer);
                if(webViewContainer.getVisibility() == View.GONE){
                    hasDescriptionIcon.setVisibility(View.GONE);
                    relatedWebViewContainer.setVisibility(View.VISIBLE);
                    webViewContainer.setVisibility(View.VISIBLE);

                    WebView longdescWV = new WebView(getActivity());//(WebView)container.findViewById(R.id.webview);
                    longdescWV.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    StringBuilder htmlString = new StringBuilder();
                    int[] colorText = Utils.intToRgb(colors.getColor(colors.getBody_color(), "50"));//Colors.hex2Rgb(colors.getBody_color());
                    longdescWV.setBackgroundColor(Color.TRANSPARENT);
                    htmlString.append(Utils.paramBodyHTMLConf(colorText, "no-justify", "12"));
                    String body = page.getBody()/*.replace("strong", "b")*/;
                    htmlString.append(body);
                    htmlString.append("</div></body></html>");
                    //longdescWV.getSettings().setMinimumFontSize(17);
                    //longdescWV.getSettings().setMinimumLogicalFontSize(17);
                    //longdescWV.getSettings().setDefaultFontSize(19);
                    //longdescWV.getSettings().setDefaultFixedFontSize(15);
                    //longdescWV.getSettings().setTextSize(WebSettings.TextSize.NORMAL);
                    //longdescWV.getSettings().setJavaScriptEnabled(true);
                    longdescWV.setWebViewClient(new WebViewClient() {
                        @Override
                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                            view.loadUrl(url);
                            return true;
                        }
                    });
                    longdescWV.loadData(htmlString.toString(), "text/html; charset=utf-8", "UTF-8");// "text/html", "UTF-8");
                    relatedWebViewContainer.addView(longdescWV, 0);
            }else{
                    if(!page.getBody().isEmpty()){
                        hasDescriptionIcon.setVisibility(View.VISIBLE);
                    }
                    webViewContainer.setVisibility(View.GONE);
                    if(relatedWebViewContainer.getChildCount() == 2){
                        relatedWebViewContainer.removeViewAt(0);
                    }
                    relatedWebViewContainer = null;
                }
            }
        });

        ImageView img = (ImageView)container.findViewById(R.id.image_item);
        //float ratio = 1f;
        //int image_height = 200;

        Illustration illust = page.getIllustration();

        if(illust != null) {

            img.setScaleType(ImageView.ScaleType.CENTER_CROP);

            if(!illust.getPath().isEmpty()) {
                Glide.with(getActivity()).load(new File(illust.getPath())).into(img);
            }else if(!illust.getLink().isEmpty()){
                Glide.with(getActivity()).load(illust.getLink()).into(img);
            }

        }else{
            img.setVisibility(View.GONE);
        }


        TextView title_item = (TextView)container.findViewById(R.id.title_item);
        title_item.setText(page.getTitle());
        title_item.setTypeface(MainActivity.FONT_TITLE);
        title_item.setTextColor(colors.getColor(colors.getTitle_color()));
/*        title_item.setTextColor(new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_selected},
                        new int[]{android.R.attr.state_pressed},
                        new int[]{}},
                new int[]{
                        colors.getColor(colors.getBackground_color()),
                        colors.getColor(colors.getBackground_color()),
                        colors.getColor(colors.getTitle_color())}));*/


        TextView description_item = (TextView)container.findViewById(R.id.description_item);
        if(!page.getIntro().isEmpty()) {
            hasDescriptionIcon.setVisibility(View.VISIBLE);
            description_item.setTextAppearance(getActivity(), android.R.style.TextAppearance_DeviceDefault_Medium);
            description_item.setTypeface(MainActivity.FONT_BODY);
            description_item.setTextColor(colors.getColor(colors.getBody_color()));

        /*description_item.setTextColor(new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_selected},
                        new int[]{android.R.attr.state_pressed},
                        new int[]{}},
                new int[]{
                        colors.getColor(colors.getBackground_color()),
                        colors.getColor(colors.getBackground_color()),
                        colors.getColor(colors.getBody_color())}));*/

            description_item.setText(Utils.removeHtmlTags(page.getIntro()).replace("\n", "").replace("[br]", "\n"));

            if (description_item.getText().toString().length() > 0 && description_item.getText().toString().substring(0, 1).equalsIgnoreCase(" "))
                description_item.setText(description_item.getText().toString().replaceFirst(" ", ""));

        }else{
            description_item.setVisibility(View.INVISIBLE);
        }

        if(page.getBody().isEmpty())
        {
            hasDescriptionIcon.setVisibility(View.GONE);

            Linked linked =  page.getLinked();
            Related related = page.getRelated();

            llRelated = (LinearLayout)container.findViewById(R.id.relativeItemContainer);
            objects = getAllRelatedElements(page, related, linked);
            drawRelatedElements(page, objects, getActivity().getLayoutInflater());
        }





        //Glide.with(getActivity()).load(R.drawable.right).transform(new ColorTransformation(colors.getColor(colors.getBackground_color()))).into((ImageView) container.findViewById(R.id.arrow));


 /*       String link =  (page.getLinked() == null) ? null : page.getLinked().getTitle();
        if(link == null || link.isEmpty()) {
            container.findViewById(R.id.linkContainer).setVisibility(View.GONE);
            link = "";
            container.setBackgroundColor(colors.getColor(colors.getBackground_color()));
        }else{

            StateListDrawable drawable = new StateListDrawable();
            drawable.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(colors.getColor(colors.getForeground_color())));
            drawable.addState(new int[]{android.R.attr.state_focused}, new ColorDrawable(colors.getColor(colors.getForeground_color())));
            drawable.addState(new int[]{}, new ColorDrawable(colors.getColor(colors.getBackground_color())));
            container.setBackgroundDrawable(drawable);

            StateListDrawable drawable_ = new StateListDrawable();
            drawable_.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(colors.getColor(colors.getBackground_color())));
            drawable_.addState(new int[]{android.R.attr.state_focused}, new ColorDrawable(colors.getColor(colors.getBackground_color())));
            drawable_.addState(new int[]{}, new ColorDrawable(colors.getColor(colors.getForeground_color(), "AA")));
            container.findViewById(R.id.linkContainer).setBackgroundDrawable(drawable_);
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });

            TextView more_info = (TextView)container.findViewById(R.id.more_info);
            more_info.setTextAppearance(getActivity(), android.R.style.TextAppearance_DeviceDefault_Medium);
            more_info.setTypeface(MainActivity.FONT_BOLD, Typeface.BOLD);
            more_info.setTextColor(colors.getColor(colors.getBackground_color()));
        }*/

        //heightItem = heightOfCellForWidth(widthItem, /*event_date,*/ event_title, event_description, (link != null) ? link : "");//120;

        container.setLayoutParams(new LinearLayout.LayoutParams((int) Utils.dpToPixels(getActivity(), 550f), LinearLayout.LayoutParams.WRAP_CONTENT));//heightItem));//

        return container;

    }


    private ArrayList<Object> getAllRelatedElements(Child_pages page,  Related related, Linked linked){
        ArrayList<Object> objects = new ArrayList<Object>();
        RealmList<MyInteger> relatedCats = related.getList();
        RealmList<MyString> relatedPages = related.getList_pages();
        RealmList<Link> links = linked.getLinks();

        if(related != null && related.getContact_form() != null && !related.getContact_form().getTitle().isEmpty()) {

            RelatedItem1 item = (RelatedItem1)page.getRelated().getContact_form(); //list.get(0);
            objects.add(item);

        }

        if(related != null && related.getRelatedLocation() != null && related.getRelatedLocation().getId() != 0) {


            RelatedItem1 item = (RelatedItem1)page.getRelated().getRelatedLocation(); //list.get(0);
            objects.add(item);
        }

        if (related != null && !related.getTitle().isEmpty()) {
            RelatedTitle1 relatedTitle = (RelatedTitle1)related;
            if (relatedPages.size() > 0) {
               // relatedTitle.setPage(true);
                objects.add(relatedTitle);
            }

        }
        if (relatedPages.size() > 0) {
            for (Iterator<MyString> iterator = relatedPages.iterator(); iterator.hasNext();) {

                MyString relatedPageId = iterator.next();
                RealmResults<Child_pages> child_pages = realm.where(Child_pages.class).equalTo("id",Integer.parseInt(relatedPageId.getMyString())).findAll();//appController.getChildPageDao().queryForEq("id",relatedPageId.getLinked_id());

                if (child_pages.size() > 0) {
                    RelatedItem1 item = (RelatedItem1)child_pages.get(0);
                    objects.add(item);
                }
            }
        }
        if (related != null && !related.getTitle_categories().isEmpty()) {
            RelatedTitle1 relatedTitle = (RelatedTitle1)related;
            if (relatedCats.size() > 0) {
             //   relatedTitle.setCategory(true);
                objects.add(relatedTitle);
            }

        }
        if (relatedCats.size() > 0) {
            for (Iterator<MyInteger> iterator = relatedCats.iterator(); iterator
                    .hasNext();) {
                MyInteger relatedCatIds = (MyInteger) iterator
                        .next();
                final int id_related_cat = relatedCatIds.getMyInt();
                RealmResults<Category> categories = realm.where(Category.class).equalTo("id",id_related_cat).findAll();

                   // categories = appController.getCategoryDao().queryForEq("id", relatedCatIds.getLinked_id());

                if (categories.size() > 0) {
                    RelatedItem1 item = (RelatedItem1)categories.get(0);
                    objects.add(item);
                }
            }
        }

        //		if (related.getContact_form()!=null) {
        //			List<Contact> list = new ArrayList<Contact>();
        //			try {
        //				list = appController.getContactDao().queryForEq("id", related.getContact_form().getId());
        //			} catch (SQLException e) {
        //				// TODO Auto-generated catch block
        //				e.printStackTrace();
        //			}
        //			if (list.size()>0) {
        //				RelatedItem item = (RelatedItem)list.get(0);
        //				objects.add(item);
        //			}
        //
        //		}

        if (linked != null && !linked.getTitle().isEmpty()) {
            RelatedTitle1 relatedTitle = (RelatedTitle1)linked;
            if (links.size() > 0) {
              //  relatedTitle.setLink(true);
                objects.add(relatedTitle);
            }

        }
        if (links != null && links.size() > 0) {
            for (Iterator<Link> iterator = links.iterator(); iterator
                    .hasNext();) {
                Link link = (Link) iterator.next();
                RelatedItem1 item = (RelatedItem1)link;
                objects.add(item);
            }
        }

        return objects;

    }
    private LinearLayout llRelated;
    List<Object> objects;
    protected int indexClicked;


    public void drawRelatedElements(Child_pages page, List<Object> objects, LayoutInflater inflater){

        //		objects = getAllRelatedElements(related, linked);
        boolean toggle = false;
        //boolean add_or_not = true;
        for (int i = 0; i < objects.size(); i++) {

            if (objects.get(i) instanceof RelatedTitle1) {

                RelatedTitle1 item = (RelatedTitle1)objects.get(i);
                if (toggle) {
                  //  item.setPage(false);

                }
                View relatedTitleView = inflater.inflate(R.layout.related_title, null, false);
                TextView relatedTitleTV = (TextView) relatedTitleView.findViewById(R.id.title_related);
                relatedTitleTV.setTextAppearance(getActivity(), android.R.style.TextAppearance_Medium);
                relatedTitleTV.setTypeface(MainActivity.FONT_BODY, Typeface.BOLD);

                if(!isTablet){
                    DisplayMetrics metrics = new DisplayMetrics();
                    getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

                    if(metrics.densityDpi <= 240 )
                        relatedTitleTV.setTextSize(relatedTitleTV.getTextSize() - 12);
                    else
                        relatedTitleTV.setTextSize(relatedTitleTV.getTextSize() - 20);
                }

                if (item.getRelatedTitle() != null) {
                    relatedTitleTV.setText(item.getRelatedTitle());
                }else {
                    relatedTitleView.setVisibility(View.GONE);
                }


                relatedTitleTV.setTextColor(colors.getColor(colors.getBackground_color()));
                relatedTitleView.setBackgroundDrawable(colors.getForePD());

                toggle = true;
                llRelated.addView(relatedTitleView);
            }

            if (objects.get(i) instanceof RelatedItem1) {
/*                if (add_or_not) {
                    llRelated.addView(getNewDivider());
                }*/
                //add_or_not = false;

                View relatedView = createRelatedView(inflater, page,  objects.get(i), i);
                //heightItem += 40;
                //if(metrics.densityDpi >= 213 && metrics.densityDpi <= 219) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 50);
                params.topMargin = 1;
                params.bottomMargin = 1;
                params.leftMargin = 8;
                llRelated.addView(relatedView, params);
                //}else{
                    //llRelated.addView(relatedView, LinearLayout.LayoutParams.WRAP_CONTENT, 50);
                //}
                //llRelated.addView(getNewDivider());
                //				if (i==objects.size()-1) {
                //					llRelated.addView(getNewDivider());
                //				}
            }
        }
    }


    @SuppressWarnings("deprecation")
    private View createRelatedView(LayoutInflater inflater, final Child_pages page, Object object, int index) {
        final RelatedItem1 item = (RelatedItem1)object;
        //		final Child_pages pageRelated = child_pages.get(0);
        boolean noImageUse = true;
        View relatedView = inflater.inflate(
                R.layout.item_related_directory, null,
                false);

        relatedView.setClickable(true);
        ColorStateList colorSelector = new ColorStateList(
                new int[][] {
                        new int[] { android.R.attr.state_pressed },
                        new int[] {} },
                new int[] {
                        colors.getColor(colors
                                .getTitle_color()),
                        colors.getColor(colors.getBackground_color()) });
        //		stateListDrawable = new StateListDrawable();
        //		stateListDrawable.addState(
        //				new int[] { android.R.attr.state_pressed },
        //				new ColorDrawable(colors.getColor(colors
        //						.getTitle_color())));
        //		stateListDrawable.addState(
        //				new int[] { android.R.attr.state_selected },
        //				new ColorDrawable(colors.getColor(colors
        //						.getTitle_color())));

        //		StateListDrawable stateListDrawable;
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(colors.getColor(colors.getBackground_color())));//colors.makeGradientToColor(colors.getTabs_background_color()));//
        stateListDrawable.addState(new int[]{android.R.attr.state_selected}, new ColorDrawable(colors.getColor(colors.getBackground_color()))); //colors.makeGradientToColor(colors.getTabs_background_color()));//new ColorDrawable(colors.getColor(colors.getTitle_color())));
        stateListDrawable.addState(new int[]{}, new ColorDrawable(colors.getColor(colors.getForeground_color()))); //colors.makeGradientToColor(colors.getTabs_background_color()));//new ColorDrawable(colors.getColor(colors.getTitle_color())));

        //		relatedView.setBackgroundDrawable(stateListDrawable);

        relatedView.setBackgroundDrawable(stateListDrawable);
        TextView titleTv = (TextView) relatedView.findViewById(R.id.TVTitleCategory);
        titleTv.setTypeface(MainActivity.FONT_TITLE);
        ArrowImageView arrowImg = (ArrowImageView) relatedView.findViewById(R.id.imgArrow);
        arrowImg.setLayoutParams(new LinearLayout.LayoutParams(16, 16));

        ImageView imgPage = (ImageView) relatedView.findViewById(R.id.imgCategory);

        imgPage.setPadding(5, 5, 5, 5);



        titleTv.setText(item.getItemTitle1());
        relatedView.setPadding(10, 0, 0, 0);


        titleTv.setTextColor(colorSelector);

        if (item instanceof Link) {
            titleTv.setText(item.getItemTitle1());
        }else if(item instanceof RelatedContactForm) {
            // List<Contact> contacts = appController.getContactDao().queryForEq("id", ((RelatedContactForm)item).getId());
            RealmResults<Contact> contacts= realm.where(Contact.class).equalTo("id", ((RelatedContactForm)item).getId()).findAll();
            if(contacts != null && contacts.size() > 0) {
                for(int i = 0; i < contacts.size(); i++) {
                    if(contacts.get(i).getId() == ((RelatedContactForm)item).getId()) {
                        noImageUse = true;
                        relatedView.findViewById(R.id.imgCategoryContainer).setVisibility(View.VISIBLE);
                        imgPage.setVisibility(View.VISIBLE);
                        BitmapDrawable contactIcon = null;
                        try {
                            contactIcon = new BitmapDrawable(BitmapFactory.decodeStream(getActivity().getAssets().open(contacts.get(i).getIcon())));
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        if (contactIcon != null) {
                            contactIcon.setColorFilter(new PorterDuffColorFilter(colors.getColor(colors.getBackground_color()), PorterDuff.Mode.MULTIPLY));
                        }
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(32, 32);
                        imgPage.setLayoutParams(params);
                        imgPage.setBackgroundDrawable(contactIcon);
                    }
                }
            }


        }
        else if(item instanceof RelatedLocation) {
            try {
               // List<Location> locations = appController.getLocationDao().queryForEq("id", ((RelatedLocation)item).getId());
                RealmResults<Location> locations=realm.where(Location.class).equalTo("id", ((RelatedLocation)item).getId()).findAll();
                if(locations != null && locations.size() > 0) {
                    titleTv.setText(locations.get(0).getTitle());

                    RealmResults<Locations_group> locationGrps = realm.where(Locations_group.class).findAll();//appController.getLocationGroupDao().queryForAll();
                    if(locationGrps != null && locationGrps.size() > 0) {

                        BitmapDrawable contactIcon = null;
                        for(int i = 0; i < locationGrps.size(); i++) {
                            List<Location> locations_ = new ArrayList<Location>(locationGrps.get(i).getLocations());
                            if(locations_ != null && locations_.size() > 0) {
                                if(locations_.get(0).getId() == locations.get(0).getId()) {
                                    noImageUse = true;
                                    relatedView.findViewById(R.id.imgCategoryContainer).setVisibility(View.VISIBLE);
                                    imgPage.setVisibility(View.VISIBLE);
                                    contactIcon = new BitmapDrawable(BitmapFactory.decodeStream(getActivity().getAssets().open(locationGrps.get(i).getPin_icon())));
                                    contactIcon.setColorFilter(new PorterDuffColorFilter(colors.getColor(colors.getBackground_color(), "88"),PorterDuff.Mode.MULTIPLY));
                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(32, 32);
                                    imgPage.setLayoutParams(params);
                                    imgPage.setBackgroundDrawable(contactIcon);
                                }
                            }
                        }

                    }
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        if (!noImageUse) {
            Object illustration = item.getItemIllustration1();
            if (illustration != null) {

                if (illustration instanceof Illustration) {
                    Illustration image = (Illustration) illustration;
                    String path = "";
                    if (!image.getPath().isEmpty()) {

                        if(isTablet){
                            ((LinearLayout.LayoutParams)imgPage.getLayoutParams()).width = 60;
                            ((LinearLayout.LayoutParams)imgPage.getLayoutParams()).height = 40;
                            ((LinearLayout.LayoutParams)((View) imgPage.getParent()).getLayoutParams()).height = 60;
                        }
                        else{
                            ((LinearLayout.LayoutParams)imgPage.getLayoutParams()).width = 70;
                            ((LinearLayout.LayoutParams)imgPage.getLayoutParams()).height = 60;
                            ((LinearLayout.LayoutParams)((View) imgPage.getParent()).getLayoutParams()).height = 80;
                        }
                        path = image.getPath();
                        Glide.with(getActivity()).load(new File(path)).into(imgPage);
                    }else {
                        path = image.getLink();
                        Glide.with(getActivity()).load((path.isEmpty()) ? "http://" : path).into(imgPage);
                    }
                    //					imageLoader.displayImage(path, imgPage, options);
                }else if (illustration instanceof String) {
                    String icon = (String) illustration;
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(32, 32);
                    imgPage.setLayoutParams(params);

                    try {

                        BitmapDrawable drawable = new BitmapDrawable(BitmapFactory.decodeStream(getActivity().getAssets().open(icon)));

                        //						stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, drawable1);
                        StateListDrawable stateListDrawable_ = new  StateListDrawable();

                        drawable.setColorFilter(new PorterDuffColorFilter(
                                colors.getColor(colors.getForeground_color()),
                                PorterDuff.Mode.MULTIPLY));

                        stateListDrawable_.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(colors.getColor(colors.getForeground_color())));//colors.makeGradientToColor(colors.getTabs_background_color()));//
                        stateListDrawable_.addState(new int[]{android.R.attr.state_selected}, new ColorDrawable(colors.getColor(colors.getForeground_color()))); //colors.makeGradientToColor(colors.getTabs_background_color()));//new ColorDrawable(colors.getColor(colors.getTitle_color())));
                        //						stateListDrawable.addState(new int[]{}, drawable2);
                        imgPage.setBackgroundDrawable(stateListDrawable_);
                        imgPage.setImageDrawable(drawable);
                        //						BitmapDrawable drawable = new BitmapDrawable(BitmapFactory.decodeStream(getActivity().getAssets().open(icon)));
                        //						drawable.setColorFilter(new PorterDuffColorFilter(
                        //								colors.getColor(colors.getForeground_color()),
                        //								PorterDuff.Mode.MULTIPLY));
                        //						imgPage.setImageDrawable(drawable);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }else {
                relatedView.findViewById(R.id.imgCategoryContainer).setVisibility(View.GONE);
            }
        }else{
            relatedView.findViewById(R.id.imgCategoryContainer).setVisibility(View.GONE);
        }

        PackageManager pm = getActivity()
                .getPackageManager();
        final boolean hasTelephony = pm
                .hasSystemFeature(PackageManager.FEATURE_TELEPHONY);



        arrowImg.setPaint(getNewPaint());
        if (item instanceof Link) {
            if (((Link)item).getUrl().startsWith("tel://")) {
                if (!hasTelephony) {
                    titleTv.setTextColor(colors.getColor(colors.getBackground_color()));
                    relatedView.setBackgroundColor(colors.getColor(colors.getForeground_color()));
                    arrowImg.setVisibility(View.GONE);
                }
            }
        }

        //final View viewFinal = relatedView;
        //LayoutInflater layoutInflater = inflater;
        final int indexTmp = index;
        relatedView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                long time = System.currentTimeMillis();
                //clickedView = v;
                indexClicked = indexTmp;
                if (item instanceof Link) {
                    Link link = (Link) item;
                    String url = link.getUrl();
                    if (url.startsWith("http")) {
                        if (url.contains("youtube")) {

                            Intent intent = new Intent(getActivity(), YoutubePlayerActivity.class);
                            intent.putExtra("InfoActivity", ((MainActivity) getActivity()).infoActivity);
                            intent.putExtra("link", url);
                            startActivity(intent);
                        } else if (url.contains(".pdf")) {
                            /** Uness Modif **/

                            Log.e(" PageFragment <=== url ", " : " + url);

                            ((MainActivity) getActivity()).extras = new Bundle();
                            //String url = new ArrayList<Child_pages>(elements).get(position - IS_HEADER_ADDED).getAuto_open_url();
                            ((MainActivity) getActivity()).extras.putString("link", /* URL to GoogleDoc => */"http://docs.google.com/gview?embedded=true&url=" +  /* URL to PDF => */url);
                            //((MainActivity) getActivity()).extras.putString("link", *//** URL to GoogleDoc => **//*"http://docs.google.com/gview?embedded=true&url=" +  *//** URL to PDF => **//* "http://backoffice.paperpad.fr/pdf/41/CONNECTION_WIFI.pdf");
                            WebViewFragment webFragment = new WebViewFragment();
                            webFragment.setArguments(((MainActivity) getActivity()).extras);
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container, webFragment).addToBackStack(null).commit();

                                /*QPDFViewerFragment qpdfViewerFragment = QPDFViewerFragment.create(url);
                                //webFragment.setArguments(((MainActivity) getActivity()).extras);
                                getActivity().getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.fragment_container, qpdfViewerFragment).addToBackStack(null).commit();*/


                        } else {

                            WebViewFragment webViewFragment = new WebViewFragment();
                            ((MainActivity) getActivity()).bodyFragment = "WebViewFragment";
                            // In case this activity was started with special instructions from an Intent,
                            // pass the Intent's extras to the fragment as arguments
                            ((MainActivity) getActivity()).extras = new Bundle();
                            ((MainActivity) getActivity()).extras.putString("link", url);

                            webViewFragment.setArguments(((MainActivity) getActivity()).extras);
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
                                new String[]{email});
                        startActivity(Intent.createChooser(mailer,
                                "Send email..."));
                    } else if (url.startsWith("tel:")) {
                        //						PackageManager pm = getActivity()
                        //								.getPackageManager();
                        //						boolean hasTelephony = pm
                        //								.hasSystemFeature(PackageManager.FEATURE_TELEPHONY);
                        if (hasTelephony) {
                            Intent intent = new Intent(
                                    Intent.ACTION_CALL);
                            intent.setData(Uri.parse(url));
                            startActivity(intent);
                        }
                    }
                } else if (item instanceof Category) {
                    Category category = (Category) item;


                    if (!category.getDisplay_type().equals("multi_select")) {
                        ((MainActivity) getActivity()).openCategory(category);

                    } else {
                        MultiSelectPagesFragment categoryFragment = new MultiSelectPagesFragment();
                        ((MainActivity) getActivity()).bodyFragment = "MultiSelectPagesFragment";
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

                } else if (item instanceof Child_pages) {
                    Child_pages page = (Child_pages) item;
                    realm.beginTransaction();
                    page.setVisible(true);
                    realm.commitTransaction();

                    ((MainActivity) getActivity()).openPage(page);  /*à modifier aprés*/
                } else if (item instanceof Contact) {
                    int sid = 0;
                    if (page.getCategory() != null && page.getCategory().getSection() != null) {
                        sid = page.getCategory().getSection().getId_s();
                    }
                    FormContactFragment forFrag = FormContactFragment.newInstance();
                    ((MainActivity) getActivity()).bodyFragment = "CategoryFragment";
                    // In case this activity was started with special instructions from an Intent,
                    // pass the Intent's extras to the fragment as arguments
                    ((MainActivity) getActivity()).extras = new Bundle();
                    ((MainActivity) getActivity()).extras.putInt("Section_id_form", sid);
                    ((MainActivity) getActivity()).extras.putInt("Contact", ((Contact) item).getId_con());
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
                } else if (item instanceof RelatedContactForm)

                {
                    //					long time2 = System.currentTimeMillis();
                    //					time = System.currentTimeMillis();

                    int sid = 0;
                    if (page.getCategory() != null && page.getCategory().getSection() != null) {
                        sid = page.getCategory().getSection().getId_s();
                    }
                    //					time = System.currentTimeMillis()- time;
                    //					Log.i("PageFrag"+" time sid", time+"");
                    //					time = System.currentTimeMillis();
                    FormContactFragment forFrag = FormContactFragment.newInstance();
                    ((MainActivity) getActivity()).bodyFragment = "FormFragment";//CategoryFragment";
                    // In case this activity was started with special instructions from an Intent,
                    // pass the Intent's extras to the fragment as arguments
                    RealmResults<Contact> list = null;
                    //					time = System.currentTimeMillis();
                    Log.i("PageFrag" + " time initialise frag", time + "");
                    list = realm.where(Contact.class).equalTo("id", ((RelatedContactForm) item).getId()).findAll();
                    //appController.getContactDao().queryForEq("id", ((RelatedContactForm) item).getId());

                    //					time = System.currentTimeMillis() - time ;
                    //					Log.i("PageFrag"+" time contact db", time+"");
                    ((MainActivity) getActivity()).extras = new Bundle();
                    ((MainActivity) getActivity()).extras.putInt("Section_id_form", sid);
                    ((MainActivity) getActivity()).extras.putInt("Contact", list.get(0).getId_con());
                    forFrag.setArguments(((MainActivity) getActivity()).extras);
                    // Add the fragment to the 'fragment_container' FrameLayout
                    getActivity()
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container,
                                    forFrag)
                            .setTransition(
                                    FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .addToBackStack(null).commit();
                    //Runtime.getRuntime().gc();
                    //					time2 = System.currentTimeMillis() - time2;
                    //					Log.i("PageFrag"+" time load formulaire", time2+"");
                } else if (item instanceof RelatedLocation) {


                    ((MainActivity) getActivity()).extras = new Bundle();
                    //((RelatedLocation)item).getId()
                    RealmResults<Locations_group> locationGrps = null;
                    locationGrps = realm.where(Locations_group.class).findAll(); /*appController.getLocationGroupDao().queryForAll();*/
                    //						if(locationGrps != null && locationGrps.size() > 0) {
                    //							List<Section> sections = appController.getSectionsDao().queryForEq("id_section", locationGrps.get(0).getSection().getId_section());
                    //							if(sections != null && sections.size() > 0) {
                    Fragment mMapFragment = new MapV2Fragment();
                    ((MainActivity) getActivity()).extras.putInt("Section_id", locationGrps.get(0).getSection().getId_s());
                    mMapFragment.setArguments(((MainActivity) getActivity()).extras);
                    ((MainActivity) getActivity()).bodyFragment = "MapV2Fragment";

                    getActivity()
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container,
                                    mMapFragment).addToBackStack(null).commit();
                    //							}
                    //
                    //						}
                }

            }

        });

        return relatedView;
    }



    private Paint getNewPaint() {
        String ALPHA = "CC";
        Paint paint = new Paint();
        paint.setColor(colors.getColor(colors.getBackground_color(),ALPHA));
        return paint;
    }

    private View getNewDivider(){
        View divider = new View(getActivity());
        divider.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 1));
        divider.setBackgroundColor(colors.getColor(colors
                .getForeground_color()));
        return divider;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Collection<Category> getCategories() {
        return categories;
    }

    public void setCategories(Collection<Category> categories) {
        this.categories = categories;
    }


}
