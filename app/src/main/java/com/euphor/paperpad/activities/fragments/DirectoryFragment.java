package com.euphor.paperpad.activities.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.euphor.paperpad.Beans.MyInteger;
import com.euphor.paperpad.Beans.MyString;
import com.euphor.paperpad.Beans.Parameters;
import com.euphor.paperpad.R;
import com.euphor.paperpad.activities.main.MainActivity;
import com.euphor.paperpad.activities.main.MyApplication;
import com.euphor.paperpad.activities.main.YoutubePlayerActivity;
import com.euphor.paperpad.adapters.FormulePageHAdapter;
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

import com.euphor.paperpad.utils.Colors;
import com.euphor.paperpad.utils.RelatedItem;
import com.euphor.paperpad.utils.RelatedItem1;
import com.euphor.paperpad.utils.RelatedTitle;
import com.euphor.paperpad.utils.RelatedTitle1;
import com.euphor.paperpad.utils.Utils;
import com.euphor.paperpad.utils.jsonUtils.AppHit;
import com.euphor.paperpad.widgets.ArrowImageView;


import java.io.File;
import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class DirectoryFragment extends DialogFragment {

	private View view;
	private List<Child_pages> pages;

	private List<String> childPagesKeys, dayKeys;
	HashMap<String, Integer> postionPagesKeys;
	HashMap<String, List<Child_pages>> map;



	protected RelativeLayout choiceHolder;


	private Colors colors;

	private long time;

	private String title;

	private int  HIGHT, widthItem, heightItem; 

	private final short PADDING = 10, DIFF_PADDING = 50;

	private ArrayList<Child_pages> listPage;

    private boolean isTablet;
    public Realm realm;



	private OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			String TAG = (String) v.getTag();
			if(postionPagesKeys.containsKey(TAG)) {
				View tagView = view.findViewById(postionPagesKeys.get(TAG));
				if(tagView != null) {
					((HorizontalScrollView)view.findViewById(R.id.SVchoicesHolder)).smoothScrollTo((int) tagView.getX(), 0);
				}
			}
			//Toast.makeText(getActivity(), TAG , 500).show();
		}
	};
    private DisplayMetrics metrics;


    //private MainActivity mainActivity;

	@Override
	public Dialog onCreateDialog(final Bundle savedInstanceState) {
		final ProgressDialog dialog = new ProgressDialog(getActivity());
		//


		dialog.setIndeterminate(true);
		dialog.setCancelable(true);
		// etc...
		return dialog;
	}

	@Override
	public void onAttach(Activity activity) {

				realm = Realm.getInstance(getActivity());
            //new AppController(getActivity());

		int category_id = getArguments().getInt("Category_id");
		
		if(category_id == 0) {
			
			category_id = getArguments().getBundle("EXTRAS").getInt("Category_id");
		}

			Category category = realm.where(Category.class).equalTo("id_c",category_id).findFirst();//appController.getCategoryById(category_id);
			if (category != null) {
				title = category.getTitle();
				pages = getChildCategories( category);
			}

			colors = ((MainActivity)activity).colors;
        Parameters ParamColor = realm.where(Parameters.class).findFirst();
        if (colors==null) {
            colors = new Colors(ParamColor);
        }
			((MainActivity)activity).bodyFragment = "DirectoryFragment";
			if(((MainActivity)activity).extras == null)
				((MainActivity)activity).extras = new Bundle();
			((MainActivity)activity).extras.putInt("Category_id", category_id);
			//setAdapter(new CalendarPagerAdapter(getChildFragmentManager()));
          //  isTablet = getResources().getBoolean(R.bool.isTablet);
        isTablet = Utils.isTablet(activity);
			time = System.currentTimeMillis();
		super.onAttach(activity);
	}

	@Override
	public void onSaveInstanceState(Bundle extras) {
		super.onSaveInstanceState(extras);
	}
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);


	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        map = getMapPages(pages);
		view = inflater.inflate(R.layout.directory_design, container, false); 
		view.setBackgroundColor(colors.getBackMixColor(colors.getForeground_color(), 0.30f));;
		choiceHolder = (RelativeLayout)view.findViewById(R.id.choicesHolder);
		HorizontalScrollView hSV = (HorizontalScrollView)view.findViewById(R.id.SVchoicesHolder);
		hSV.setFillViewport(true);
		hSV.setOverScrollMode(HorizontalScrollView.OVER_SCROLL_IF_CONTENT_SCROLLS);
		hSV.computeScroll();
		//		choiceHolder.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
		//			
		//			@Override
		//			public void onGlobalLayout() {
		////				width = getView().getWidth();
		////				height =  getView().getHeight();
		//				WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
		//				Display display = wm.getDefaultDisplay();
		//				HIGHT = display.getHeight() - (getView().findViewById(R.id.titleDirectory).getHeight() + getView().findViewById(R.id.abc).getHeight());
		//				fillPageDirectory(map);
		//				//getView().requestLayout();
		//			}
		//		});

		TextView titleEvents = (TextView)view.findViewById(R.id.titleDirectory);
		//titleEvents.setBackgroundColor(colors.getColor(colors.getForeground_color()));
		titleEvents.setTextColor(colors.getColor(colors.getTitle_color()));
		titleEvents.setText(title);
		titleEvents.setTypeface(MainActivity.FONT_TITLE);
		String[] alphab = new String[] {
				"ABC", "DEF", "GHI", "JKL", "MNO", "PQR", "STU", "VWX", "YZ"
		};

		int childCount = ((LinearLayout)view.findViewById(R.id.alphacontainer)).getChildCount();

		for(int i = 0; i < childCount; i ++) {
			TextView alphabetics  = (TextView)((LinearLayout)view.findViewById(R.id.alphacontainer)).getChildAt(i);//view.findViewById(R.id.abc);
			StateListDrawable drawable = new StateListDrawable();
			drawable.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(colors.getColor(colors.getForeground_color())));
			drawable.addState(new int[]{android.R.attr.state_focused}, new ColorDrawable(colors.getColor(colors.getForeground_color())));
			drawable.addState(new int[]{}, new ColorDrawable(colors.getColor(colors.getTitle_color(),"33")));

			alphabetics.setBackgroundDrawable(drawable);
			//abc.setBackgroundColor(colors.getColor(colors.getTitle_color(), "33"));
			alphabetics.setTextColor(new ColorStateList(new int[][]{{android.R.attr.state_pressed}, {android.R.attr.state_pressed},{}}, 
					new int[]{colors.getColor(colors.getBackground_color(), "80"), colors.getColor(colors.getBackground_color(), "80"),
					colors.getColor(colors.getForeground_color())}));
			alphabetics.setTag(alphab[i]);
			
			WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);

			Display display = wm.getDefaultDisplay();
			DisplayMetrics metrics = new DisplayMetrics(); 
			getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics); 

			if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT && (metrics.densityDpi >= 213 && metrics.densityDpi <= 219))
				alphabetics.setTextSize(13);
			alphabetics.setOnClickListener(clickListener);
		}



		return view;
	}



	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		HIGHT = display.getHeight() - (view.findViewById(R.id.titleDirectory).getHeight() + view.findViewById(R.id.abc).getHeight());
		fillPageDirectory(map);

		super.onViewCreated(view, savedInstanceState);
	}

	public List<Child_pages> getChildCategories(Category category) {
		List<Child_pages> result = new ArrayList<Child_pages>();
		if (category.getChildren_pages().size()>0) {
			for (Iterator<Child_pages> iterator = category.getChildren_pages().iterator(); iterator
					.hasNext();) {
				result.add((Child_pages) iterator
						.next());
			}
		}
		return result;
	}

	private void fillPageDirectory(HashMap<String, List<Child_pages>> map) {

		//float width, height, sectionRight;
		int topOffset, leftOffset, sectionRight; 
		sectionRight = 0;
		topOffset = leftOffset = 0;

		WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);

		Display display = wm.getDefaultDisplay();
		DisplayMetrics metrics = new DisplayMetrics(); 
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics); 
		if(metrics.densityDpi >= 213 && metrics.densityDpi <= 219) {
			widthItem = 550;
		}else {
			widthItem = 450;
		}

		postionPagesKeys = new HashMap<String, Integer>();
		//		dimens = new ArrayList<Point>();
		int I = 0;
		for(int i = 0; i < childPagesKeys.size(); i ++) {
			List<Child_pages> pages = map.get(childPagesKeys.get(i));	
			postionPagesKeys.put(childPagesKeys.get(i), I);
			for(int j = 0; j < pages.size(); j++){
				View directoryViewItem = fillDirectoryItem(pages.get(j));
				directoryViewItem.setId(I);

				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(widthItem, heightItem);
				//params.addRule(RelativeLayout.ALIGN_BASELINE);
				int currentTop = topOffset + PADDING;


				topOffset += heightItem + PADDING;
				if ((topOffset + heightItem - (3 * PADDING)) > HIGHT) {// - self.collectionView.contentInset.bottom - self.collectionView.contentInset.top) {  

					sectionRight = widthItem + PADDING;
					leftOffset += (sectionRight + PADDING);
					topOffset = 0;
					//					if(j == 0)
					//						this.leftOffset += widthItem;
					params.topMargin = topOffset + PADDING;
					topOffset += heightItem + PADDING;

					//										if(I > 0)
					//											params.addRule(RelativeLayout.RIGHT_OF, R);
					//										R = I;
				}else {

					params.topMargin = currentTop;
					//					if(I > 0)
					//						params.addRule(RelativeLayout.BELOW, I - 1);
				}



				params.leftMargin = leftOffset + PADDING;;
				params.bottomMargin = PADDING;



				choiceHolder.addView(directoryViewItem, params);
				I++;

			}	
			topOffset = 0;
			leftOffset += (widthItem + DIFF_PADDING);	
		}	

	}

	private View fillDirectoryItem(Child_pages page) {

		LayoutInflater mInflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View container = mInflater.inflate(R.layout.item_directory, null, false);
		heightItem = 100;
		container.setBackgroundColor(colors.getColor(colors.getBackground_color()));
		container.setTag(page);

		ImageView img = (ImageView)container.findViewById(R.id.imageDirectoryItem);

		TextView titleDirectoryItem = (TextView)container.findViewById(R.id.titleDirectoryItem);
		TextView descriptDirectoryItem = (TextView)container.findViewById(R.id.descriptDirectoryItem);


		//		LinearLayout.LayoutParams txtParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
		//		txtParams.gravity = Gravity.CENTER;
		//		txtParams.setMargins(5, 15, 5, 15);


		titleDirectoryItem.setText(page.getTitle());
		titleDirectoryItem.setTypeface(MainActivity.FONT_TITLE);
		titleDirectoryItem.setTextColor(new ColorStateList(new int[][]{{android.R.attr.state_pressed},{}}, 
				new int[]{colors.getColor(colors.getBackground_color()), 
				colors.getColor(colors.getTitle_color())}));
		//titleDirectoryItem.setTextColor(colors.getColor(colors.getTitle_color()));

		descriptDirectoryItem.setText(Utils.removeHtmlTags(page.getBody()).replace("\n", "").replace("[br]", "\n"));
		descriptDirectoryItem.setTextColor(new ColorStateList(new int[][]{{android.R.attr.state_pressed},{}}, 
				new int[]{colors.getColor(colors.getBackground_color()), 
				colors.getColor(colors.getBody_color())}));
		descriptDirectoryItem.setTypeface(MainActivity.FONT_BODY);
		WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);

		Display display = wm.getDefaultDisplay();
		metrics = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics); 
		if(metrics.densityDpi >= 213 && metrics.densityDpi <= 219) {
			heightItem += (descriptDirectoryItem.getText().toString().length() / (1.5));
		}else {
			heightItem += (descriptDirectoryItem.getText().toString().length() / (3.7));
		}



		Illustration illust = page.getIllustration();

		if(illust!= null) {
			if(!illust.getPath().isEmpty()) {
				Glide.with(getActivity()).load(new File(illust.getPath())).into(img);
			}else {
				Glide.with(getActivity()).load(illust.getLink()).into(img);
			}
		}

        heightItem += 10;

		LinearLayout relatedItemContainer = (LinearLayout)container.findViewById(R.id.relativeItemContainer);
		Linked linked =  page.getLinked();
        Related related = page.getRelated();
/*		if(linked != null && linked.getLinks1() != null) {
			heightItem += 10;
			for (Iterator<Link> iterator = linked.getLinks1().iterator(); iterator.hasNext();) {
				Link link = (Link) iterator.next();

                heightItem += 50;
				if(metrics.densityDpi >= 213 && metrics.densityDpi <= 219) {
					relatedItemContainer.addView(createRelatedView(mInflater, page,  link, 0), widthItem - 225, 50);
				}else {
					relatedItemContainer.addView(createRelatedView(mInflater, page, link, 0), widthItem - 160, 50);
				}

			}
		}else*/

            //LinearLayout
            llRelated = relatedItemContainer;//(LinearLayout)view.findViewById(R.id.LLRelatedItems);
            objects = getAllRelatedElements(page, related, linked);
            drawRelatedElements(page, objects, getActivity().getLayoutInflater());


		StateListDrawable drawable = new StateListDrawable();
		drawable.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(colors.getColor(colors.getForeground_color())));
		drawable.addState(new int[]{android.R.attr.state_focused}, new ColorDrawable(colors.getColor(colors.getForeground_color())));
		drawable.addState(new int[]{}, new ColorDrawable(colors.getColor(colors.getBackground_color())));

		container.setBackgroundDrawable(drawable);

		container.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Child_pages page = (Child_pages)v.getTag();
				//				choiceHolder.removeAllViews();
				//				for (int i = 0; i < pages.size(); i++) {
				//					if(pages.get(i).getId() == page.getId())
				//						pages.get(i).setSelected(pages.get(i).isSelected()?false:true);
				//						fillNavigationBar(pages.get(i));
				//				}		
			}
		});

		container.setLayoutParams(new LayoutParams(widthItem, LayoutParams.WRAP_CONTENT));

		return container;

	}

	public String getAlphabeticPage(String s) {

		String[] alphab = new String[] {
				"ABC", "DEF", "GHI", "JKL", "MNO", "PQR", "STU", "VWX", "YZ"
		};

		for(int i = 0; i < alphab.length; i++) {
			if(alphab[i].contains(s))return alphab[i];
		}

		return null;
	}

	public HashMap<String, List<Child_pages>> getMapPages(List<Child_pages> pages){
		HashMap<String,  List<Child_pages>> map = new HashMap<String, List<Child_pages>>();


		String tmp = "ABC", comparTag = "", titleTag = "";
		int j,som = 0;

		Child_pages page = null;

		j = pages.size() - 1;

		if(j > 0) {
			//fastTri(events, j - 1, j);
			quicksort(pages, 0, j);
		}



		List<String> list = new ArrayList<String>();

		//		pages = new ArrayList<Child_pages>();
		dayKeys = new ArrayList<String>();
		childPagesKeys= new ArrayList<String>();


		boolean end = false;
		boolean anotherDate = false;

		j = 0;
		while(!end) {
			listPage = new ArrayList<Child_pages>();

			for(int i = j ; i<pages.size(); i++) {
				page = pages.get(i);

				titleTag = getAlphabeticPage(page.getTitle().substring(0, 1));

				if(!titleTag.isEmpty() && !isAlreadyChecked(list, titleTag)) {
					if(anotherDate) break;
					//					if(isLooperFinished) {
					list.add(titleTag);
					listPage.add(page);
					comparTag = titleTag;
					//					}
					anotherDate = true;
					tmp = titleTag;
					//isLooperFinished = false;
					j++;
				}else if(comparTag.compareTo(titleTag) == 0){

					listPage.add(page);
					j++;
				}




			}
			anotherDate = false;



			if(!map.keySet().contains(tmp)) {
				map.put(tmp, listPage);
				childPagesKeys.add(tmp);
			}


			som += listPage.size();

			if(som >= pages.size()) {
				end = true;
			}

		}

		return map;

	}

	public boolean isAlreadyChecked(List<String> list, String value) {
		if(list.size() == 0)return false;

		for(int i = 0; i< list.size(); i++) {
			if(list.get(i).compareTo(value) == 0)
				return true;
		}

		return false;
	}

	public void fastTri(List<Child_pages> pages, int pivotIndex, int lastIndex){

		int j = 0;

		if(pivotIndex < lastIndex) 
		{
			j = partition(pages, pivotIndex, lastIndex);
			fastTri(pages, pivotIndex, j);
			fastTri(pages, j+1 , lastIndex);
		}	

	}	

	public static void quicksort(List<Child_pages> pages, int debut, int fin) {
		if (debut < fin) {
			int indicePivot = partition(pages, debut, fin);
			quicksort(pages, debut, indicePivot-1);
			quicksort(pages, indicePivot+1, fin);
		}
	}

	public static int partition(List<Child_pages> pages, int debut, int fin) {
		Child_pages pivotPages = pages.get(debut);
		String valeurPivot = formatStringNormalizer(pivotPages.getTitle()); //t[d�but];
		int d = debut+1;
		int f = fin;

		while (d < f) {

			while(d < f && formatStringNormalizer(pages.get(f).getTitle()).compareToIgnoreCase(valeurPivot) >= 0) f--;
			while(d < f && formatStringNormalizer(pages.get(d).getTitle()).compareToIgnoreCase(valeurPivot) <= 0) d++;

			Child_pages tmp = pages.get(d);
			pages.set(d, pages.get(f));
			pages.set(f, tmp);

		}

		if (formatStringNormalizer(pages.get(d).getTitle()).compareToIgnoreCase(valeurPivot) >= 0) d--;
		pages.set(debut, pages.get(d));
		pages.set(d, pivotPages);
		return d;
	}

	public static String formatStringNormalizer(String s) {
		String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
		return temp.replaceAll("[^\\p{ASCII}]", "").toLowerCase();
	}

	@Override
	public void onStop() {
		AppHit hit = new AppHit(System.currentTimeMillis()/1000, time/1000, "events_section", 0);
		((MyApplication)getActivity().getApplication()).hits.add(hit);
		super.onStop();
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
                //relatedTitle.setPage(true);
                objects.add(relatedTitle);
            }

        }
        if (relatedPages.size() > 0) {
            for (Iterator<MyString> iterator = relatedPages.iterator(); iterator.hasNext(); ) {


                MyString relatedPageId = iterator.next();
                RealmResults<Child_pages> child_pages;
                //child_pages = appController.getChildPageDao().queryForEq("id",relatedPageId.getLinked_id());

                child_pages = realm.where(Child_pages.class).equalTo("id",Integer.parseInt(relatedPageId.getMyString())).findAll();

                if (child_pages.size() > 0) {
                    RelatedItem1 item = (RelatedItem1)child_pages.get(0);
                    objects.add(item);
                }
            }
            for (Iterator<MyString> iterator = relatedPages.iterator(); iterator.hasNext();) {

                MyString relatedPageId = (MyString) iterator.next();
                List<Child_pages> child_pages = new ArrayList<Child_pages>();
                child_pages=realm.where(Child_pages.class).equalTo("id",Integer.parseInt(relatedPageId.getMyString())).findAll();


                if (child_pages.size() > 0) {
                    RelatedItem1 item = (RelatedItem1)child_pages.get(0);
                    objects.add(item);
                }
            }
        }
        if (related != null && !related.getTitle_categories().isEmpty()) {
            RelatedTitle1 relatedTitle = (RelatedTitle1)related;
            if (relatedCats.size() > 0) {
               // relatedTitle.setCategory(true);
                objects.add(relatedTitle);
            }

        }
        if (relatedCats.size() > 0) {
            for (Iterator<MyInteger> iterator = relatedCats.iterator(); iterator
                    .hasNext(); ) {
                MyInteger relatedCatIds = (MyInteger) iterator
                        .next();
                final int id_related_cat = relatedCatIds.getMyInt();
                RealmResults<Category> categories;
                //categories = appController.getCategoryDao().queryForEq("id", relatedCatIds.getLinked_id());
                categories = realm.where(Category.class).equalTo("id", id_related_cat).findAll();
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

                objects.add(relatedTitle);
            }


        }
        if (links != null && links.size() > 0) {
            for (Iterator<Link> iterator = links.iterator(); iterator
                    .hasNext();) {
                Link link = (Link) iterator.next();
                RelatedItem item = (RelatedItem)link;
                objects.add(item);
            }
        }
        return objects;
    }


    private LinearLayout llRelated;
    List<Object> objects;
    private View clickedView;
    protected int indexClicked;
    //private long time;
    private FormulePageHAdapter mHAdapter;


    public void drawRelatedElements(Child_pages page, List<Object> objects, LayoutInflater inflater){

        //		objects = getAllRelatedElements(related, linked);
        boolean toggle = false;
        boolean add_or_not = true;
        for (int i = 0; i < objects.size(); i++) {

            if (objects.get(i) instanceof RelatedTitle) {

                RelatedTitle1 item = (RelatedTitle1)objects.get(i);
                if (toggle) {
                   // item.setPage(false);

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
                    relatedTitleView.setBackgroundDrawable(colors.getForePD());//colors.makeGradientToColor(colors.getForeground_color()));
                    //relatedTitleView.setBackgroundColor(colors.getColor(colors.getForeground_color()));



                //				StateListDrawable stateListDrawable;
                //				stateListDrawable = new StateListDrawable();
                //				stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, colors.makeGradientToColor(colors.getTabs_background_color()));//new ColorDrawable(colors.getColor(colors.getTitle_color())));
                //				stateListDrawable.addState(new int[]{android.R.attr.state_selected}, colors.makeGradientToColor(colors.getTabs_background_color()));//new ColorDrawable(colors.getColor(colors.getTitle_color())));
                //
                //				titleRelatedLL.setBackgroundDrawable(stateListDrawable);

                toggle = true;
                llRelated.addView(relatedTitleView);
            }

            if (objects.get(i) instanceof RelatedItem1) {
/*                if (add_or_not) {
                    llRelated.addView(getNewDivider());
                }*/
                add_or_not = false;

                View relatedView = createRelatedView(inflater, page,  objects.get(i), i);
                heightItem += 40;
                if(metrics.densityDpi >= 213 && metrics.densityDpi <= 219) {
                    llRelated.addView(relatedView, widthItem - 225, 50);
                }else{
                    llRelated.addView(relatedView, widthItem - 160, 50);
                }
                //llRelated.addView(getNewDivider());
                //				if (i==objects.size()-1) {
                //					llRelated.addView(getNewDivider());
                //				}
            }
        }
    }

	/*private View createRelatedView(LayoutInflater inflater, Link link, int index) {
		final RelatedItem item = (RelatedItem)link;
		//		final Child_pages pageRelated = child_pages.get(0);
		boolean noImageUse = false;
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
								.getBackground_color()),
								colors.getColor(colors.getTitle_color(), "AA") });
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
		stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(colors.getColor(colors.getTabs_background_color())));//colors.makeGradientToColor(colors.getTabs_background_color()));//
		stateListDrawable.addState(new int[]{android.R.attr.state_selected}, new ColorDrawable(colors.getColor(colors.getTabs_background_color()))); //colors.makeGradientToColor(colors.getTabs_background_color()));//new ColorDrawable(colors.getColor(colors.getTitle_color())));

		//		relatedView.setBackgroundDrawable(stateListDrawable);

		relatedView.setBackgroundDrawable(stateListDrawable);
		TextView titleTv = (TextView) relatedView.findViewById(R.id.TVTitleCategory);
		titleTv.setTypeface(MainActivity.FONT_TITLE);
		ArrowImageView arrowImg = (ArrowImageView) relatedView.findViewById(R.id.imgArrow);
		ImageView imgPage = (ImageView) relatedView.findViewById(R.id.imgCategory);
		
		//arrowImg.setLayoutParams(new LayoutParams(22, 22));

		//		//arrowImg.setVisibility(View.GONE);
		//		LinearLayout imgArrowContainer = (LinearLayout)relatedView.findViewById(R.id.imgArrowContainer);
		//		ImageView imgFormule = new ImageView(getActivity());
		//		imgFormule.setLayoutParams(new LinearLayout.LayoutParams(32, 32));
		//		imgFormule.setScaleType(ScaleType.CENTER_CROP);
		//		Drawable stylo = getResources().getDrawable(R.drawable.icon_0_15);
		//		stylo.setColorFilter(new PorterDuffColorFilter(colors.getColor(colors.getForeground_color()),PorterDuff.Mode.MULTIPLY));
		//		imgFormule.setImageDrawable(stylo );
		//		imgArrowContainer.addView(imgFormule);



		//		titleTv.setTextColor(colors.getColor(colors
		//				.getTitle_color()));
		//		ColorStateList colorSelector2 = new ColorStateList(
		//				new int[][] {
		//						new int[] { android.R.attr.state_pressed },
		//						new int[] {} },
		//						new int[] {
		//						colors.getColor(colors
		//								.getBackground_color()),
		//								colors.getColor(colors.getTitle_color()) });
		titleTv.setTextColor(colorSelector);

		if (item instanceof Link) {
			titleTv.setText(item.getItemTitle());
			//			relatedDesc.setText(item.getItemIntro());
			//			relatedDesc.setTextAppearance(getActivity(), android.R.style.TextAppearance_Small);
			//			relatedDesc.setTextColor(colorSelector);//colors.getColor(colors.getBody_color()));
			//			relatedDesc.setTypeface(relatedDesc.getTypeface(), Typeface.NORMAL);
		}else if(item instanceof RelatedContactForm) {
			try {
				List<Contact> contacts = appController.getContactDao().queryForEq("id", ((RelatedContactForm)item).getId());
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
							contactIcon.setColorFilter(new PorterDuffColorFilter(colors.getColor(colors.getTitle_color(), "88"),PorterDuff.Mode.MULTIPLY));
							LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(26, 26);
							imgPage.setLayoutParams(params);					
							imgPage.setBackgroundDrawable(contactIcon);
						}
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


		}


		if (!noImageUse) {

			Object illustration = item.getItemIllustration();
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
					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(26, 26);
					imgPage.setLayoutParams(params);					

					try {


						BitmapDrawable drawable = new BitmapDrawable(BitmapFactory.decodeStream(getActivity().getAssets().open(icon)));

						//						stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, drawable1);
						StateListDrawable stateListDrawable_ = new  StateListDrawable();

						drawable.setColorFilter(new PorterDuffColorFilter(
								colors.getColor(colors.getTitle_color(), "AA"),
								PorterDuff.Mode.MULTIPLY));

						stateListDrawable_.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(colors.getColor(colors.getBackground_color())));//colors.makeGradientToColor(colors.getTabs_background_color()));//
						stateListDrawable_.addState(new int[]{android.R.attr.state_selected}, new ColorDrawable(colors.getColor(colors.getBackground_color()))); //colors.makeGradientToColor(colors.getTabs_background_color()));//new ColorDrawable(colors.getColor(colors.getTitle_color())));
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
				if(relatedView.findViewById(R.id.imgCategoryContainer) != null)
					relatedView.findViewById(R.id.imgCategoryContainer).setVisibility(View.GONE);
				else
					imgPage.setVisibility(View.GONE);
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


		relatedView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				long time = System.currentTimeMillis();
				//clickedView = v;
				//indexClicked = indexTmp; 	
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
							*//** Uness Modif **//*
							//							Intent target = new Intent(Intent.ACTION_VIEW);
							//							Uri uri = Uri.parse(url);
							//							target.setData(uri);
							//							//							target.setDataAndType(uri ,"application/pdf");
							//							target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
							//
							//							Intent intent = Intent.createChooser(target, "Open File");
							//							try {
							//								startActivity(intent);
							//							} catch (ActivityNotFoundException e) {
							//								// Instruct the user to install a PDF reader here, or something
							//							}

							Log.e(" PageFragment <=== url "," : "+url);

							((MainActivity) getActivity()).extras = new Bundle();
							//String url = new ArrayList<Child_pages>(elements).get(position - IS_HEADER_ADDED).getAuto_open_url();
							((MainActivity) getActivity()).extras.putString("link", *//** URL to GoogleDoc => **//*"http://docs.google.com/gview?embedded=true&url=" +  *//** URL to PDF => **//*url);
							//((MainActivity) getActivity()).extras.putString("link", *//** URL to GoogleDoc => **//*"http://docs.google.com/gview?embedded=true&url=" +  *//** URL to PDF => **//* "http://backoffice.paperpad.fr/pdf/41/CONNECTION_WIFI.pdf");
							WebViewFragment webFragment = new WebViewFragment();
							webFragment.setArguments(((MainActivity) getActivity()).extras);
							getActivity().getSupportFragmentManager().beginTransaction()
							.replace(R.id.fragment_container, webFragment).addToBackStack(null).commit();


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

				}else if (item instanceof Child_pages) {}else if (item instanceof Contact) {}
				else if (item instanceof RelatedContactForm) {}				
				else if (item instanceof RelatedLocation) {}

				time = time - System.currentTimeMillis();
				Log.i("PageFrag"+" time", time+"");
			}
		});

		return relatedView;
	}

*/

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
                                .getBackground_color()),
                        colors.getColor(colors.getTitle_color(), "AA") });
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
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(colors.getColor(colors.getTabs_background_color())));//colors.makeGradientToColor(colors.getTabs_background_color()));//
        stateListDrawable.addState(new int[]{android.R.attr.state_selected}, new ColorDrawable(colors.getColor(colors.getTabs_background_color()))); //colors.makeGradientToColor(colors.getTabs_background_color()));//new ColorDrawable(colors.getColor(colors.getTitle_color())));

        //		relatedView.setBackgroundDrawable(stateListDrawable);

        relatedView.setBackgroundDrawable(stateListDrawable);
        TextView titleTv = (TextView) relatedView.findViewById(R.id.TVTitleCategory);
        titleTv.setTypeface(MainActivity.FONT_TITLE);
        ArrowImageView arrowImg = (ArrowImageView) relatedView.findViewById(R.id.imgArrow);

        ImageView imgPage = (ImageView) relatedView.findViewById(R.id.imgCategory);

        imgPage.setPadding(5, 5, 5, 5);



        titleTv.setText(item.getItemTitle1());
        relatedView.setPadding(10, 0, 10, 0);


        titleTv.setTextColor(colorSelector);

        if (item instanceof Link) {
            titleTv.setText(item.getItemTitle1());
        }else if(item instanceof RelatedContactForm) {
            List<Contact> contacts = realm.where(Contact.class).equalTo("id", ((RelatedContactForm)item).getId()).findAll();
            //appController.getContactDao().queryForEq("id", ((RelatedContactForm)item).getId());
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
                        contactIcon.setColorFilter(new PorterDuffColorFilter(colors.getColor(colors.getForeground_color()),PorterDuff.Mode.MULTIPLY));
                        LayoutParams params = new LayoutParams(32, 32);
                        imgPage.setLayoutParams(params);
                        imgPage.setBackgroundDrawable(contactIcon);
                    }
                }
            }


        }
        else if(item instanceof RelatedLocation) {
            try {
                List<Location> locations = realm.where(Location.class).equalTo("id", ((RelatedLocation)item).getId()).findAll();
                 //appController.getLocationDao().queryForEq("id", ((RelatedLocation)item).getId());
                if(locations != null && locations.size() > 0) {
                    titleTv.setText(locations.get(0).getTitle());

                    List<Locations_group> locationGrps = realm.where(Locations_group.class).findAll();
                    //appController.getLocationGroupDao().queryForAll();
                    if(locationGrps != null && locationGrps.size() > 0) {

                        BitmapDrawable contactIcon = null;
                        for(int i = 0; i < locationGrps.size(); i++) {
                            List<Location> locations_ = new ArrayList<Location>();
                            if(locations_ != null && locations_.size() > 0) {
                                if(locations_.get(0).getId() == locations.get(0).getId()) {
                                    noImageUse = true;
                                    relatedView.findViewById(R.id.imgCategoryContainer).setVisibility(View.VISIBLE);
                                    imgPage.setVisibility(View.VISIBLE);
                                    contactIcon = new BitmapDrawable(BitmapFactory.decodeStream(getActivity().getAssets().open(locationGrps.get(i).getPin_icon())));
                                    contactIcon.setColorFilter(new PorterDuffColorFilter(colors.getColor(colors.getTitle_color(), "88"),PorterDuff.Mode.MULTIPLY));
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
                            ((LayoutParams)imgPage.getLayoutParams()).width = 60;
                            ((LayoutParams)imgPage.getLayoutParams()).height = 40;
                            ((LayoutParams)((View) imgPage.getParent()).getLayoutParams()).height = 60;
                        }
                        else{
                            ((LayoutParams)imgPage.getLayoutParams()).width = 70;
                            ((LayoutParams)imgPage.getLayoutParams()).height = 60;
                            ((LayoutParams)((View) imgPage.getParent()).getLayoutParams()).height = 80;
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

                        stateListDrawable_.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(colors.getColor(colors.getBackground_color())));//colors.makeGradientToColor(colors.getTabs_background_color()));//
                        stateListDrawable_.addState(new int[]{android.R.attr.state_selected}, new ColorDrawable(colors.getColor(colors.getBackground_color()))); //colors.makeGradientToColor(colors.getTabs_background_color()));//new ColorDrawable(colors.getColor(colors.getTitle_color())));
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
                    titleTv.setTextColor(colors.getColor(colors.getTitle_color()));
                    relatedView.setBackgroundColor(colors.getColor(colors.getBackground_color()));
                    arrowImg.setVisibility(View.GONE);
                }
            }
        }

        final View viewFinal = relatedView;
        LayoutInflater layoutInflater = inflater;
        final int indexTmp = index;
        relatedView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                long time = System.currentTimeMillis();
                clickedView = v;
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
                    page.setVisible(true);

                    ((MainActivity) getActivity()).openPage(page);
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
                    List<Contact> list = null;
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
                    List<Locations_group> locationGrps = null;
                    locationGrps = realm.where(Locations_group.class).findAll(); //appController.getLocationGroupDao().queryForAll();
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
		paint.setColor(colors.getColor(colors.getTitle_color(),ALPHA));
		return paint;
	}

    private View getNewDivider() {
        View divider = new View(getActivity());
        divider.setLayoutParams(new LayoutParams(
                LayoutParams.MATCH_PARENT, 1));
        divider.setBackgroundColor(colors.getColor(colors
                .getForeground_color()));
        return divider;
    }


}

