/**
 * 
 */
package com.euphor.paperpad.activities.fragments;

import android.R.style;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.euphor.paperpad.InterfaceRealm.CommunElements1;
import com.euphor.paperpad.R;
import com.euphor.paperpad.activities.main.MainActivity;
import com.euphor.paperpad.adapters.CategoriesAdapter;
import com.euphor.paperpad.Beans.CartItem;
import com.euphor.paperpad.Beans.CategoriesMyBox;
import com.euphor.paperpad.Beans.Category;
import com.euphor.paperpad.Beans.Child_pages;
import com.euphor.paperpad.Beans.Illustration;

import com.euphor.paperpad.Beans.Parameters;
import com.euphor.paperpad.Beans.Price;

import com.euphor.paperpad.utils.Colors;
import com.euphor.paperpad.utils.Utils;
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
public class FilteredCategoriesFragment extends Fragment{


	private static final int PREVIOUS = -1;
	private static final int NEXT = 1;
	private Colors colors;

	private String titleInStrip = null;
	private List<Category> categories;
	protected int id_cat = 0;
	protected LinearLayout choiceHolder;
	protected Category cat;
	private FrameLayout productInfo;
	private LayoutInflater inflater;
	private MainActivity mainActivity;
	private ListView listView;
	private View view;
	private int index;
    public Realm realm;




	/**
	 * 
	 */
	public FilteredCategoriesFragment() {}

	/* (non-Javadoc)
	 * @see android.support.v4.app.ListFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//View view;
		this.inflater = inflater;
		view = inflater.inflate(R.layout.filtred_categories, container, false);
		view.setBackgroundColor(colors.getColor(colors.getBackground_color()));
		listView = (ListView)view.findViewById(android.R.id.list);

		view.findViewById(R.id.backChoices).setBackgroundColor(colors.getBackMixColor(colors.getForeground_color(), 0.10f));
		//.setBackgroundColor(colors.getColor(colors.getTitle_color(), "10"));;
		if (categories.size()>0) {
			choiceHolder = (LinearLayout)view.findViewById(R.id.choicesHolder);
			//choiceHolder.setBackgroundColor(colors.getColor("E6DCE2"));
			cat = categories.get(index);
			//((MainActivity) getActivity()).extras.putInt("Category_id",cat.getId());

                realm.beginTransaction();
			cat.setSelected(true);
            realm.commitTransaction();
			for (Iterator<Category> iterator = categories.iterator(); iterator.hasNext();) {
				Category element = (Category) iterator.next();
				fillNavigationBar(element);

			}
			if (cat!=null && getChildrenPages(cat).size()>0) {
				titleInStrip = cat.getTitle();
				if (titleInStrip!=null && !titleInStrip.isEmpty()) {
					View viewTitle = inflater.inflate(R.layout.title_strip, null, false);
					viewTitle.findViewById(R.id.TitleHolder).setBackgroundDrawable(colors.getForePD());//.setBackgroundColor(colors.getColor(colors.getForeground_color(), "88"));
					TextView titleContactsTV = (TextView)viewTitle.findViewById(R.id.TitleTV);
					//	titleContactsTV.setTypeface(MainActivity.FONT_TITLE);
					//	titleContactsTV.setText(titleInStrip);			
					titleContactsTV.setTextAppearance(getActivity(), style.TextAppearance_Large);
					titleContactsTV.setTypeface(MainActivity.FONT_TITLE, Typeface.BOLD);
					titleContactsTV.setText(titleInStrip);
					DisplayMetrics metrics = new DisplayMetrics(); 
					getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics); 

					if(Utils.isTablet(getActivity())){
						if(metrics.densityDpi >= 213 )
							titleContactsTV.setTextSize(titleContactsTV.getTextSize() - 3);
						else
							titleContactsTV.setTextSize(titleContactsTV.getTextSize());
					}
					else{
						if(metrics.densityDpi <= 240 )
							titleContactsTV.setTextSize(titleContactsTV.getTextSize() - 16);
						else
							titleContactsTV.setTextSize(titleContactsTV.getTextSize() - 22);

					}

					titleContactsTV.setTextColor(colors.getColor(colors.getBackground_color()));
					viewTitle.setClickable(false);
					((LinearLayout)view.findViewById(R.id.filteredContent)).addView(viewTitle, 0);
				}

				fillProductScroller(getChildrenPages(cat),getChildrenPages(cat).iterator().next());



				listView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View v, int position,
							long arg3) {

						CommunElements1 element = getChildrenPages(cat).get(position);

						if (element instanceof Category) {
							Category category = (Category)element;
							((MainActivity) getActivity()).openCategory(category);
						}else if (element instanceof Child_pages) {
							Child_pages page = (Child_pages)element;
							((MainActivity) getActivity()).openChildPage(page,false);
						}
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
				realm = Realm.getInstance(getActivity());
		//new AppController(getActivity());
		colors = ((MainActivity)activity).colors;
        Parameters ParamColor = realm.where(Parameters.class).findFirst();
        if (colors==null) {
            colors = new Colors(ParamColor);
        }


        index = getArguments().getInt("index", 0);
		
		int section_id = -1;
		if (getArguments().getInt("Section_id")!=0) {
			section_id = getArguments().getInt("Section_id");
		}else {
		}

		mainActivity = (MainActivity)activity;
		int category_id = getArguments().getInt("Category_id");
		((MainActivity)getActivity()).bodyFragment = "FilteredCategoriesFragment";
		if(((MainActivity)getActivity()).extras == null)
			((MainActivity)getActivity()).extras = new Bundle();
		((MainActivity)getActivity()).extras.putInt("Category_id", category_id);
		((MainActivity)getActivity()).extras.putInt("index", index);

		super.onAttach(activity);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);

		int category_id = getArguments().getInt("Category_id");
		int section_id = getArguments().getInt("Section_id");
		Category category = realm.where(Category.class).equalTo("id",category_id).findFirst();//appController.getCategoryById(category_id);

		if (category != null) {
			categories = getChildCategories(category);
			titleInStrip = category.getTitle();
		}	


		//		try {
		//			Section section = appController.getSectionsDao().queryForId(section_id);
		//			
		//			if (section!=null) {
		//				titleInStrip = section.getTitle();
		//				if (section.getCategories1().size() > 0) {
		//					categories = new ArrayList<Category>();
		//					for (Iterator<Category> iterator = section.getCategories1()
		//							.iterator(); iterator.hasNext();) {
		//						Category communElement = (Category) iterator.next();
		//						categories.add(communElement);
		//					}
		//				} 
		//			}
		//			
		//		} catch (SQLException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		} 

	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	public List<Category> getChildCategories(Category category) {
		List<Category> result = new ArrayList<Category>();
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

	/** a method to fill the upper bar where we choose the {@link CategoriesMyBox}
	 * @param category
	 */
	private void fillNavigationBar(Category category) {
		TextView categoryTxt = new TextView(getActivity());
		categoryTxt.setTypeface(MainActivity.FONT_BODY, Typeface.BOLD);
		if (category.isSelected() ) {
			LinearLayout.LayoutParams txtParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
			//			txtParams.gravity = Gravity.CENTER;
			categoryTxt.setGravity(Gravity.CENTER);
			categoryTxt.setText(category.getTitle().toUpperCase());
			categoryTxt.setTextColor(colors.getColor(colors.getBackground_color()));
			categoryTxt.setTextSize(16);
			Drawable selectDrawable = getResources().getDrawable(R.drawable.back_choices_final);
			selectDrawable.setColorFilter(colors.getColor(colors.getTitle_color()), Mode.MULTIPLY);
			categoryTxt.setBackgroundDrawable(selectDrawable);
			categoryTxt.setTag(category);
			choiceHolder.addView(categoryTxt, txtParams);

		}else {

			LinearLayout.LayoutParams txtParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
			txtParams.setMargins(10, 0, 10, 5);
			categoryTxt.setGravity(Gravity.CENTER);
			//			txtParams.gravity = Gravity.CENTER;
			categoryTxt.setText((category.getTitle()).toUpperCase());
			categoryTxt.setTextColor(colors.getColor(colors.getTitle_color()));
			categoryTxt.setTextSize(16);
			categoryTxt.setTag(category);
			choiceHolder.addView(categoryTxt, txtParams);
		}

		categoryTxt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				cat = (Category)v.getTag();
                realm.beginTransaction();
				for (int i = 0; i < categories.size(); i++) {

					categories.get(i).setSelected(false);
				}

			cat.setSelected(true);
                realm.commitTransaction();
				choiceHolder.removeAllViews();
				for (int i = 0; i < categories.size(); i++) {

					fillNavigationBar(categories.get(i));
					if(categories.get(i).isSelected()) {
						index = i;
						((MainActivity) getActivity()).extras.putInt("index", i);
					}
				}
				//				hsvLayout.removeAllViews();
				if (cat!=null && getChildrenPages(cat).size()>0) {
					titleInStrip = cat.getTitle();
					if (titleInStrip!=null && !titleInStrip.isEmpty()) {
						View viewTitle = inflater.inflate(R.layout.title_strip, null, false);
						viewTitle.findViewById(R.id.TitleHolder).setBackgroundDrawable(colors.getForePD());//.setBackgroundColor(colors.getColor(colors.getForeground_color(), "88"));
						TextView titleContactsTV = (TextView)viewTitle.findViewById(R.id.TitleTV);
						//					titleContactsTV.setTypeface(MainActivity.FONT_TITLE);
						//					titleContactsTV.setText(titleInStrip);			
						titleContactsTV.setTextAppearance(getActivity(), style.TextAppearance_Large);
						titleContactsTV.setTypeface(MainActivity.FONT_TITLE, Typeface.BOLD);
						titleContactsTV.setText(titleInStrip);
						DisplayMetrics metrics = new DisplayMetrics(); 
						getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics); 

						if( Utils.isTablet(getActivity())){
							if(metrics.densityDpi >= 213 )
								titleContactsTV.setTextSize(titleContactsTV.getTextSize() - 3);
							else
								titleContactsTV.setTextSize(titleContactsTV.getTextSize());
						}
						else{
							if(metrics.densityDpi <= 240 )
								titleContactsTV.setTextSize(titleContactsTV.getTextSize() - 16);
							else
								titleContactsTV.setTextSize(titleContactsTV.getTextSize() - 22);

						}

						titleContactsTV.setTextColor(colors.getColor(colors.getBackground_color()));
						viewTitle.setClickable(false);
						//(listView).addHeaderView(viewTitle, null, false);
						if (((LinearLayout)view.findViewById(R.id.filteredContent)).getChildCount() ==2) 
							((LinearLayout)view.findViewById(R.id.filteredContent)).removeViewAt(0);
						
						((LinearLayout)view.findViewById(R.id.filteredContent)).addView(viewTitle, 0);
						//IS_HEADER_ADDED = 1;
					}

					fillProductScroller(getChildrenPages(cat), getChildrenPages(cat).iterator().next());
					//					fillProductBox(getChildrenPages(cat).iterator().next());
				}




			}
		});
	}

	View selectedBefore;
	private int indexCurrent;
	protected ArrayList<Child_pages> pages;
	private Parameters parameters;
	private Child_pages page;
	/**
	 * @param collection 
	 * 
	 */
	public void fillProductScroller(Collection<Child_pages> collection, Child_pages selected) {
		//		hsvLayout.removeAllViews();

		List<CommunElements1> elementsTmp = new ArrayList<CommunElements1>();
		elementsTmp.addAll(getChildrenPages(cat));
		CategoriesAdapter adapter = new CategoriesAdapter(mainActivity, elementsTmp, colors, R.layout.categories_list_item);
		listView.setAdapter(adapter);
	}




	protected void fillProductBox(final Child_pages page) {
		productInfo.removeAllViews();
		LinearLayout view = (LinearLayout)inflater.inflate(R.layout.slider_inner_page, null, false);//sliderInnerPage
		/*
		 * COMMUN TASKS
		 */
		//Title product
		TextView titleTV = (TextView)view.findViewById(R.id.titleProductTV);
		titleTV.setTypeface(MainActivity.FONT_TITLE);
		titleTV.setText(page.getTitle());
		titleTV.setTextColor(colors.getColor(colors.getTitle_color()));
		titleTV.setTextSize(34);
		view.findViewById(R.id.titleProductHolder).setBackgroundColor(colors.getColor(colors.getBackground_color(),"FF"));

		//Image Product

        final RealmList<Illustration> images = new RealmList<>();

        images.add(page.getIllustration());
		if (images.size()>0 && (ImageView)view.findViewById(R.id.ProductIMG)!= null) {

			Illustration illust =  page.getIllustration();
			ImageView productImage = (ImageView)view.findViewById(R.id.ProductIMG);
			productImage.setScaleType(ScaleType.CENTER_CROP);
			//			imageLoader.displayImage(path, productImage);
			if (!illust.getPath().isEmpty()) {
				Glide.with(getActivity()).load(new File(illust.getPath())).into(productImage);
			}else {
				Glide.with(getActivity()).load(illust.getLink()).into(productImage);
			}
		}

		// Arrows next and previous coloring and actions !
		ArrowImageView previousIMG = (ArrowImageView)view.findViewById(R.id.PreviousIMG);
		Paint paint = getNewPaint();
		previousIMG.setPaint(paint );
		ArrowImageView nextIMG = (ArrowImageView)view.findViewById(R.id.NextIMG);
		nextIMG.setPaint(paint );
		Category category = page.getCategory();
		Collection<Child_pages> pagesTmp = getChildrenPages(category);
		LinearLayout navButtons = (LinearLayout)view.findViewById(R.id.navButtons);
		LinearLayout backPrev = (LinearLayout)view.findViewById(R.id.backPrevious);
		LinearLayout backNext = (LinearLayout)view.findViewById(R.id.backNext);
		if (pagesTmp.size()>1) {

			pages = new ArrayList<Child_pages>();
			pages.addAll(pagesTmp);
			indexCurrent = indexOfPage(page);
			backPrev.setOnClickListener(getNavigationBtnListener(PREVIOUS));
			backNext.setOnClickListener(getNavigationBtnListener(NEXT));
		}else {

			navButtons.setVisibility(View.GONE);
		}

		//category parent
		TextView categoryTv = (TextView)view.findViewById(R.id.categorieTV);
		categoryTv.setTypeface(MainActivity.FONT_BODY);
		categoryTv.setText(category.getTitle());
		categoryTv.setTextColor(colors.getColor(colors.getForeground_color()));
		LinearLayout categoryHolder = (LinearLayout)view.findViewById(R.id.Category);
		categoryHolder.setBackgroundColor(colors.getColor(colors.getBackground_color(), "FF"));
		final int categoryId = category.getId();
		categoryHolder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Category category = realm.where(Category.class).equalTo("id",categoryId).findFirst();//appController.getCategoryById(categoryId);

				((MainActivity)getActivity()).openCategory(category);


			}
		});
		if (view.findViewById(R.id.shareHolder) != null) {
			view.findViewById(R.id.shareHolder).setVisibility(View.GONE);
		}
		if(view.findViewById(R.id.navigationHolder)!=null)
			view.findViewById(R.id.navigationHolder).setBackgroundColor(colors.getColor(colors.getBackground_color()));


		// Long description under the product image
		WebView longdescWV = (WebView)view.findViewById(R.id.LongDescWV);
		StringBuilder htmlString = new StringBuilder();
		int[] colorText = Colors.hex2Rgb(colors.getBody_color());
		longdescWV.setBackgroundColor(Color.TRANSPARENT);
		if (view.findViewById(R.id.tempLL) != null) {
			view.findViewById(R.id.tempLL).setBackgroundColor(colors.getColor(colors.getBackground_color(), "FF"));
		}else {
			longdescWV.setBackgroundColor(colors.getColor(colors.getBackground_color(), "FF"));
		}
		htmlString.append(Utils.paramBodyHTML(colorText));
		htmlString.append(page.getBody());
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

		longdescWV.loadDataWithBaseURL(null, htmlString.toString(), "text/html", "UTF-8", null); 

		//prices related to this product
		RealmList<Price> prices = page.getPrices();
		LinearLayout pricesContainer = (LinearLayout)view.findViewById(R.id.PricesHolder);
		pricesContainer.setBackgroundColor(colors.getColor(colors.getBackground_color(), "FF"));
		view.findViewById(R.id.hsvPrices).setBackgroundColor(colors.getColor(colors.getBackground_color(), "FF"));
		if (prices.size()>0) {

			for (Iterator<Price> iterator = prices.iterator(); iterator.hasNext();) {
				Price price = (Price) iterator.next();
				View priceElement = inflater.inflate(R.layout.price, null, false);
				LinearLayout btnPrice = (LinearLayout)priceElement.findViewById(R.id.btnPrice);
				Drawable drawable = btnPrice.getBackground();
				drawable.setColorFilter(colors.getColor(colors.getForeground_color(), "88"), Mode.MULTIPLY);
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
				parameters = null;
                parameters = realm.where(Parameters.class).findFirst();
                // appController.getParametersDao().queryForId(1);

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

									category = realm.where(Category.class).equalTo("id",page.getId()).findFirst();//controller.getCategoryById(page.getCategory_id());
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
									//									Toast.makeText(getActivity(), "not allowed to add because mode :"+MainActivity.stripe_or_not, Toast.LENGTH_LONG).show();
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
				android.widget.LinearLayout.LayoutParams params = new android.widget.LinearLayout.LayoutParams(5, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
				pricesContainer.addView(espace, params);
				pricesContainer.addView(priceElement);
			}

		}else {
			pricesContainer.setVisibility(View.GONE);
		}
		view.findViewById(R.id.LLInfoProduct).setBackgroundColor(colors.getColor(colors.getBackground_color(), "FF"));
		view.findViewById(R.id.LLRelatedItems).setBackgroundColor(colors.getColor(colors.getBackground_color(), "FF"));
		view.findViewById(R.id.innerPage).setBackgroundColor(colors.getColor(colors.getBackground_color(), "FF"));
		productInfo.addView(view);
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


	private OnClickListener getNavigationBtnListener(final int which) {
		OnClickListener listener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				Child_pages pageTo = null ;
				//				ArrayList<Child_pages> collCats = new ArrayList<Child_pages>();
				//				for (Iterator<Child_pages> iterator = cat.getChildren_pages1().iterator(); iterator
				//						.hasNext();) {
				//					Child_pages child_page = (Child_pages) iterator
				//							.next();
				//					collCats.add(child_page);
				//				}
				if (which == PREVIOUS) {
					if (indexCurrent>0) {
						pageTo = pages.get(indexCurrent+PREVIOUS);

					}else {
						pageTo = pages.get(pages.size()-1);
					}

					if (pageTo!=null) {
						fillProductBox(pageTo);
					}
					fillProductScroller(pages, pageTo);
					//					fillProductScroller(pages, pageTo);
				}else if (which == NEXT) {
					if (indexCurrent == pages.size()-1) {
						pageTo = pages.get(0);
					}else {
						pageTo = pages.get(indexCurrent+NEXT);
					}

					if (pageTo!=null) {
						fillProductBox(pageTo);
					}
					fillProductScroller(pages, pageTo);
					//					fillProductScroller(pages, pageTo);
				} 


			}
		};
		return listener;
	}


}
