package com.euphor.paperpad.activities.fragments;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.bumptech.glide.Glide;
import com.euphor.paperpad.InterfaceRealm.CommunElements1;
import com.euphor.paperpad.R;
import com.euphor.paperpad.activities.main.MainActivity;
import com.euphor.paperpad.adapters.CommunElements;
import com.euphor.paperpad.Beans.Category;
import com.euphor.paperpad.Beans.Child_pages;
import com.euphor.paperpad.Beans.ExtraField;
import com.euphor.paperpad.Beans.Illustration;

import com.euphor.paperpad.utils.Colors;
import com.euphor.paperpad.utils.Utils;
import com.euphor.paperpad.widgets.ArrowImageView;


import android.R.style;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;


/**
 * @author euphordev02
 * opens a fragment that shows either the categories children of a category or the page children of a category as a horizontal grid or gallery
 */
public class ListOfPageFragment extends Fragment {

	private Colors colors;
	protected int id_cat = 0;
	protected LinearLayout choiceHolder;
	protected Category cat;
	protected ArrayList<Child_pages> pages;
	private LinearLayout listOfPagesContainer;
	public Realm realm;
	public static ListOfPageFragment newInstance(Category cat, Colors colors){
		ListOfPageFragment fragment = new ListOfPageFragment();
		fragment.setCat(cat);
		fragment.setColors(colors);
		return fragment;

	}



	@Override
	public void onAttach(Activity activity) {
				realm = Realm.getInstance(getActivity());
		((MainActivity) getActivity()).bodyFragment = "ListOfPageFragment";
		((MainActivity)activity).extras = new Bundle();
		if(cat!=null)
			((MainActivity)activity).extras.putInt("Category_id", cat.getId());
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		setRetainInstance(true);
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{

		View view = inflater.inflate(R.layout.list_of_pages, container, false);
		//view.setBackgroundColor(colors.getColor(colors.getBackground_color(), "AA"));
		view.setBackgroundColor(colors.getBackMixColor(colors.getForeground_color(), 0.10f));//colors.getColor(colors.getForeground_color(), "10"));
		//view.setBackgroundColor(colors.getColor(colors.getSide_tabs_foreground_color(),"88"));
		RealmList<Category> Cats_From_Category = new RealmList<>();
		if(cat.getCategories() !=null && cat.getCategories().size()>0)
			Cats_From_Category = cat.getCategories();
		else
			Cats_From_Category = cat.getChildren_categories();

		List<Category> cats = new ArrayList<Category>(Cats_From_Category);


		TextView title = (TextView)view.findViewById(R.id.title);
		title.setTypeface(MainActivity.FONT_TITLE);
		title.setText(cat.getTitle());
		title.setTextColor(colors.getColor(colors.getTitle_color()));

		TextView description = (TextView)view.findViewById(R.id.description);
		description.setTextAppearance(getActivity(), style.TextAppearance_DeviceDefault_Small);
		description.setTypeface(MainActivity.FONT_BODY);
		description.setText(cat.getIntro());
		description.setTextColor(colors.getColor(colors.getBody_color()
		));

		listOfPagesContainer = (LinearLayout)view.findViewById(R.id.listOfPagesContainer);


		boolean visible = true;
		RealmResults<Child_pages> les_pages = realm.where(Child_pages.class).equalTo("visible",visible).equalTo("category_id",cat.getId()).findAll();
		if (cats.size() > 1) {
			for (int i = 0; i < cats.size(); i++) {
				if(cats.get(i).getVisible().equals("1")) {
					fillListOfCats(cats.get(i));
				}
			}
		}else if (cats.size() == 1) {
			if(cats.get(0).getVisible().equals("1"))
				((MainActivity) getActivity()).openCategory(cats.get(0));

		}else if(les_pages.size() > 1) {
			for (Iterator iterator = les_pages.iterator(); iterator.hasNext();) {
				Child_pages page = (Child_pages) iterator.next();
				if (page.isVisible())
					fillListOfPagess(page);
			}
		}else if(les_pages.size() == 1) {
			if (les_pages.first().isVisible())
				((MainActivity) getActivity()).openChildPage(les_pages.first(),false);

		}

		return view;
	}



	private void fillListOfPagess(Child_pages page) {

		//productInfo.removeAllViews();
		//params.setMargins(20, 10, 20, 10);


		LinearLayout v = (LinearLayout)getActivity().getLayoutInflater().inflate(R.layout.detail_list_of_pages, null, false);//sliderInnerPage
           /*v.addView(v, screenWidth, screenHeight);*/
		/*v.weight = metrics_.widthPixels;
		v.height = metrics_.heightPixels/4;	*/

		StateListDrawable drawable = new StateListDrawable();
		drawable.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(colors.getColor(colors.getForeground_color())));
		drawable.addState(new int[]{android.R.attr.state_focused}, new ColorDrawable(colors.getColor(colors.getForeground_color())));
		drawable.addState(new int[]{}, new ColorDrawable(colors.getColor(colors.getBackground_color())));

		v.setBackgroundDrawable(drawable); //Color(colors.getColor(colors.getBackground_color()));
		v.findViewById(R.id.detailContainer).setBackgroundColor(colors.getColor(colors.getTitle_color(), "10"));


		/*
		 * COMMUN TASKS
		 */
		//Title product
		TextView titleTV = (TextView)v.findViewById(R.id.pageTitle);
		titleTV.setTextAppearance(getActivity(), style.TextAppearance_Large);
		titleTV.setTypeface(MainActivity.FONT_TITLE);
		titleTV.setText(page.getTitle());
		titleTV.setTextColor(colors.getColor(colors.getTitle_color()));
		//titleTV.setTextSize(20);
		v.setBackgroundColor(colors.getColor(colors.getBackground_color(),"FF"));
		String path = null;
		if(page.getIllustration() !=null) {
			path = page.getIllustration().getPath();
		}
		ImageView imageView = (ImageView) v.findViewById(R.id.imageDetail);
		imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		//imageView.setLayoutParams(new ImageSwitcher.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));


		if (path != null) {
			if (path.contains("http")) {
				Glide.with(getActivity()).load(path).into(imageView);
			}else {
				Glide.with(getActivity()).load(new File(path)).into(imageView);
			}
		}




/*        float ratio = 1f;
        int width = imageView.getMeasuredWidth();
        int height = imageView.getMeasuredHeight();
		Illustration illust = page.getIllustration();
		if(illust != null) {
			width = illust.getOriginalWidth();
			height = illust.getOriginalHeight();

			ratio = ((float)height / (float)width);
		}*/


//		android.view.ViewGroup.LayoutParams imageParams;

//		if(ratio > 0.8f){
//			//ratio = 1.2f;
//			imageParams = new LayoutParams((int)(fix_length * 0.9f), (int)(fix_length * 1.2f)); //new LayoutParams((int)(fix_length * (1 / ratio)), (int)(fix_length * ratio));
//		
//		}else if(ratio != 0){
//			//ratio = 1.2f;
//			imageParams = new LayoutParams((int)(fix_length * 1f), (int)(fix_length * 0.9f)); //new LayoutParams((int)(fix_length * ratio), (int)(fix_length * (1 / ratio)));
//		}else
//			imageParams = new LayoutParams((int)(fix_length), (int)(fix_length));

/*        float TITLE_SIZE = titleTV.getTextSize();


        int top = 10;
        float paddedWidth = fix_length - 10 * 2;

        Rect dimOfTitle = new Rect();

        titleTV.getPaint().getTextBounds(titleTV.getText().toString(), 0, titleTV.getText().toString().length(), dimOfTitle);
        float titleHeight = (dimOfTitle.height() + TITLE_SIZE + 8) * (dimOfTitle.width()/paddedWidth);
        top += titleHeight;

		imageParams = new LayoutParams((int)(fix_length), (int)((fix_length - top)* 1.1f)); //new LayoutParams((int)(fix_length * (1 / ratio)), (int)(fix_length * ratio));

		
		imageView.setLayoutParams(imageParams);

        top += fix_length;*/

		//LinearLayout.LayoutParams txtParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, top);
		//titleTV.setLayoutParams(txtParams);

		/*DisplayMetrics metrics_ = getResources().getDisplayMetrics();
		int screenHeight = metrics_.heightPixels/4;
		int screenWidth = metrics_.widthPixels;
*/
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		float density = metrics.density;
		int fix_length = (int) (300*density);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(fix_length, (int)((fix_length)* 1.2f));
		params.gravity = Gravity.CENTER_VERTICAL;
		v.setLayoutParams(params);


		WebView longdescWV = (WebView)v.findViewById(R.id.LongDescWV);
		StringBuilder htmlString = new StringBuilder();
		int[] colorText = Colors.hex2Rgb(colors.getBody_color());
		longdescWV.setBackgroundColor(Color.TRANSPARENT);
		if (v.findViewById(R.id.tempLL) != null) {
			v.findViewById(R.id.tempLL).setBackgroundColor(colors.getColor(colors.getBackground_color(), "FF"));
		}else {
			longdescWV.setBackgroundColor(colors.getColor(colors.getBackground_color(), "FF"));
		}



		htmlString.append(Utils.paramBodyHTML(colorText));
		htmlString.append(page.getIntro());//.getBody());
		longdescWV.getSettings().setDefaultFontSize(14);
		if(!page.getIntro().isEmpty())
			longdescWV.loadDataWithBaseURL(null, htmlString.toString(), "text/html", "UTF-8", null);
		else
			longdescWV.setVisibility(View.GONE);


		ArrowImageView arrowDetail = (ArrowImageView)v.findViewById(R.id.arrowDetail);
		Paint paint = new Paint();
		paint.setColor(colors.getColor(colors.getTitle_color()));
		arrowDetail.setPaint(paint);

		ExtraField extraFields = page.getExtra_fields();
		if(extraFields != null && extraFields.getDetails_button_title() != null && !extraFields.getDetails_button_title().isEmpty() ){
			v.findViewById(R.id.detailContainer).setBackgroundColor(colors.getColor(colors.getTitle_color(), "10"));
			TextView detailPageitem = (TextView)v.findViewById(R.id.detailPageitem);
			detailPageitem.setTypeface(MainActivity.FONT_BODY);
			//detailPageitem.setText(category.getExtra_fields().getDetails_button_title());
			detailPageitem.setTextColor(colors.getColor(colors.getTitle_color()));
			detailPageitem.setGravity(Gravity.CENTER);
			detailPageitem.setText(extraFields.getDetails_button_title());
		}
		else{
			v.findViewById(R.id.detailContainer).setVisibility(View.GONE);
		}
		//	}
		v.setTag(page);
		v.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				CommunElements1 element = (CommunElements1) v.getTag();

				if (element instanceof Category) {
					List<Category> cats = new ArrayList<Category>(((Category)element).getCategories());

					Category category = (Category)element;
					if(cats.size() > 0) {
						category = cats.get(0);
					}

					((MainActivity) getActivity()).openCategory(category);

				}else if (element instanceof Child_pages) {

					Child_pages page = (Child_pages)element;

					((MainActivity) getActivity()).openChildPage(page,false);

				}

			}
		});

		listOfPagesContainer.addView(v);//, params);
		View separator = new View(getActivity());
		separator.setLayoutParams(new LayoutParams(30, android.view.ViewGroup.LayoutParams.MATCH_PARENT));
		listOfPagesContainer.addView(separator);





	}



	protected void fillListOfCats(final Category category) {
		//productInfo.removeAllViews();
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER_VERTICAL;
		//params.setMargins(20, 10, 20, 10);

		LinearLayout v = (LinearLayout)getActivity().getLayoutInflater().inflate(R.layout.detail_list_of_pages, null, false); //sliderInnerPage


		StateListDrawable drawable = new StateListDrawable();
		drawable.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(colors.getColor(colors.getForeground_color())));
		drawable.addState(new int[]{android.R.attr.state_focused}, new ColorDrawable(colors.getColor(colors.getForeground_color())));
		drawable.addState(new int[]{}, new ColorDrawable(colors.getColor(colors.getBackground_color())));

		v.setBackgroundDrawable(drawable); //Color(colors.getColor(colors.getBackground_color()));
		v.findViewById(R.id.detailContainer).setBackgroundColor(colors.getColor(colors.getTitle_color(), "10"));


		/*
		 * COMMUN TASKS
		 */
		//Title product
		TextView titleTV = (TextView)v.findViewById(R.id.pageTitle);
		titleTV.setTypeface(MainActivity.FONT_TITLE);
		titleTV.setText(category.getTitle());
		titleTV.setTextColor(colors.getColor(colors.getTitle_color()));
		titleTV.setTextSize(20);
		v.setBackgroundColor(colors.getColor(colors.getBackground_color(),"FF"));


		String path = category.getCommunImagePath1();

		ImageView imageView = (ImageView) v.findViewById(R.id.imageDetail);
		imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		//imageView.setLayoutParams(new ImageSwitcher.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));




		if (path.contains("http")) {
			Glide.with(getActivity()).load(path).into(imageView);
		}else {
			Glide.with(getActivity()).load(new File(path)).into(imageView);
		}

		int width = imageView.getMeasuredWidth();
		int height = imageView.getMeasuredHeight();

		float ratio ;//= height / width;
		ratio = 1f;
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		float density = metrics.density;;
		int fix_length = (int) (300*density);
		//		Display d = getActivity().getWindowManager().getDefaultDisplay();
		//		if(d.getWidth() > d.getHeight())
		//			fix_length = 250;//d.getHeight() - 400;
		//		else
		//			fix_length = 300; //d.getWidth() - 400;

		Illustration illust = category.getIllustration();
		if(illust != null) {
			width = illust.getOriginalWidth();
			height = illust.getOriginalHeight();

			ratio = ((float)height / (float)width);
		}


		android.view.ViewGroup.LayoutParams imageParams;

//		if(ratio > 0.8f){
//			//ratio = 1.2f;
//			imageParams = new LayoutParams((int)(fix_length * 0.9f), (int)(fix_length * 1.2f)); //new LayoutParams((int)(fix_length * (1 / ratio)), (int)(fix_length * ratio));
//		
//		}else if(ratio != 0){
//			//ratio = 1.2f;
//			imageParams = new LayoutParams((int)(fix_length * 1f), (int)(fix_length * 0.9f)); //new LayoutParams((int)(fix_length * ratio), (int)(fix_length * (1 / ratio)));
//		}else
//			imageParams = new LayoutParams((int)(fix_length), (int)(fix_length));

		imageParams = new LayoutParams((int)(fix_length), (int)(fix_length * 1.1f)); //new LayoutParams((int)(fix_length * (1 / ratio)), (int)(fix_length * ratio));


		imageView.setLayoutParams(imageParams);

		WebView longdescWV = (WebView)v.findViewById(R.id.LongDescWV);
		StringBuilder htmlString = new StringBuilder();
		int[] colorText = Colors.hex2Rgb(colors.getBody_color());
		longdescWV.setBackgroundColor(Color.TRANSPARENT);
		if (v.findViewById(R.id.tempLL) != null) {
			v.findViewById(R.id.tempLL).setBackgroundColor(colors.getColor(colors.getBackground_color(), "FF"));
		}else {
			longdescWV.setBackgroundColor(colors.getColor(colors.getBackground_color(), "FF"));
		}



		htmlString.append(Utils.paramBodyHTML(colorText));
		htmlString.append(category.getIntro());//.getBody());
		longdescWV.getSettings().setDefaultFontSize(14);
		if(!category.getIntro().isEmpty())
			longdescWV.loadDataWithBaseURL(null, htmlString.toString(), "text/html", "UTF-8", null);
		else
			longdescWV.setVisibility(View.GONE);


		ArrowImageView arrowDetail = (ArrowImageView)v.findViewById(R.id.arrowDetail);
		Paint paint = new Paint();
		paint.setColor(colors.getColor(colors.getTitle_color()));
		arrowDetail.setPaint(paint);

//		ExtraField extraFields = page.getExtra_fields();
//		if(extraFields != null && extraFields.getDetails_button_title() != null && !extraFields.getDetails_button_title().isEmpty() ){
//			v.findViewById(R.id.detailContainer).setBackgroundColor(colors.getColor(colors.getTitle_color(), "10"));
//			TextView detailPageitem = (TextView)v.findViewById(R.id.detailPageitem);
//			detailPageitem.setTypeface(MainActivity.FONT_BODY);
//			//detailPageitem.setText(category.getExtra_fields().getDetails_button_title());
//			detailPageitem.setTextColor(colors.getColor(colors.getTitle_color()));
//			detailPageitem.setGravity(Gravity.CENTER);
//			detailPageitem.setText(extraFields.getDetails_button_title());
//		}
//		else{
		v.findViewById(R.id.detailContainer).setVisibility(View.GONE);
//		} 


		//	if(category.getExtra_fields() != null) {
		TextView detailPageitem = (TextView)v.findViewById(R.id.detailPageitem);
		detailPageitem.setTypeface(MainActivity.FONT_BODY);
		//detailPageitem.setText(category.getExtra_fields().getDetails_button_title());
		detailPageitem.setTextColor(colors.getColor(colors.getTitle_color()));
		detailPageitem.setGravity(Gravity.CENTER);
		//	}
		v.setTag(category);
		v.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				CommunElements1 element = (CommunElements1) v.getTag();

				if (element instanceof Category) {
					List<Category> cats = new ArrayList<Category>(((Category)element).getCategories());

					Category category = (Category)element;
					if(cats.size() > 0) {
						category = cats.get(0);
					}

					((MainActivity) getActivity()).openCategory(category);

				}else if (element instanceof Child_pages) {

					Child_pages page = (Child_pages)element;

					((MainActivity) getActivity()).openChildPage(page,false);

				}

			}
		});

		listOfPagesContainer.addView(v, params);
		View separator = new View(getActivity());
		separator.setLayoutParams(new LayoutParams(30, android.view.ViewGroup.LayoutParams.MATCH_PARENT));
		listOfPagesContainer.addView(separator);


	}

	public List<Child_pages> getChildrenPages(Category category) {
		List<Child_pages> result = new ArrayList<Child_pages>();
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

	public ListOfPageFragment() {
		super();
		// TODO Auto-generated constructor stub
	}


	/**
	 * @return the colors
	 */
	public Colors getColors() {
		return colors;
	}


	/**
	 * @param colors the colors to set
	 */
	public void setColors(Colors colors) {
		this.colors = colors;
	}


	/**
	 * @return the cat
	 */
	public Category getCat() {
		return cat;
	}


	/**
	 * @param cat the cat to set
	 */
	public void setCat(Category cat) {
		this.cat = cat;
	}


}