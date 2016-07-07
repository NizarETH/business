package com.euphor.paperpad.activities.fragments;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import android.R.style;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.euphor.paperpad.Beans.Parameters;
import com.euphor.paperpad.InterfaceRealm.CommunElements1;
import com.euphor.paperpad.R;
import com.euphor.paperpad.activities.main.MainActivity;
import com.euphor.paperpad.activities.main.MyApplication;
import com.euphor.paperpad.adapters.MultiSelectPagesAdapter;
import com.euphor.paperpad.Beans.CartItem;
import com.euphor.paperpad.Beans.Category;
import com.euphor.paperpad.Beans.Child_pages;
import com.euphor.paperpad.Beans.Formule;
import com.euphor.paperpad.Beans.FormuleElement;
import com.euphor.paperpad.Beans.Price;
import com.euphor.paperpad.Beans.Section;

import com.euphor.paperpad.utils.CategoryTo;
import com.euphor.paperpad.utils.Colors;
import com.euphor.paperpad.utils.Utils;
import com.euphor.paperpad.utils.jsonUtils.AppHit;
import com.euphor.paperpad.utils.quickAction.ActionItem;
import com.euphor.paperpad.utils.quickAction.QuickAction;
import com.euphor.paperpad.utils.quickAction.QuickAction.OnActionItemClickListener;
import com.euphor.paperpad.widgets.AutoResizeTextView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;


import io.realm.Realm;
import io.realm.RealmList;

public class MultiSelectPagesFragment extends ListFragment{

	private static int IS_HEADER_ADDED = 1;
	private Collection<Child_pages> elements;
	private MultiSelectPagesAdapter adapter;
	private Colors colors;

	private String titleInStrip = null;
	private String checkBoxes_color;
	private Category category;
	private MainActivity mainActivity;
	private LayoutInflater layoutInflater;
	private long time;
	private int id;
    public Realm realm;

	/* (non-Javadoc)
	 * @see android.support.v4.app.ListFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		layoutInflater = inflater;
		View view = inflater.inflate(R.layout.multi_select_pages_list, container, false);
		ListView listView = (ListView)view.findViewById(android.R.id.list);
		view.setBackgroundColor(colors.getColor(colors.getBackground_color()));
		
		DisplayMetrics metrics = new DisplayMetrics(); 
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics); 
		
		if (titleInStrip!=null && !titleInStrip.isEmpty()) {
			LinearLayout viewTitle = (LinearLayout)view.findViewById(R.id.TitleHolder);
//			viewTitle.setBackgroundColor(colors.getColor(colors.getForeground_color()));
			viewTitle.setBackgroundDrawable(colors.getForePD());
			TextView titleContactsTV = (TextView)view.findViewById(R.id.TitleTV);
			titleContactsTV.setTextAppearance(getActivity(), style.TextAppearance_Large);
			titleContactsTV.setTypeface(MainActivity.FONT_TITLE, Typeface.BOLD);
			titleContactsTV.setText(titleInStrip);
			//titleContactsTV.setTextSize(titleContactsTV.getTextSize() - 9);

			if( Utils.isTablet(getActivity())){
				if(metrics.densityDpi >= 213 )
					titleContactsTV.setTextSize(titleContactsTV.getTextSize() - 4);
				else
					titleContactsTV.setTextSize(titleContactsTV.getTextSize() + 2);
			}
			else{
				if(metrics.densityDpi <= 240 )
					titleContactsTV.setTextSize(titleContactsTV.getTextSize() - 16);
				else
					titleContactsTV.setTextSize(titleContactsTV.getTextSize() - 22);
				//titleContactsTV.setTextSize(titleContactsTV.getTextSize() - 22);
			}
			titleContactsTV.setTextColor(colors.getColor(colors.getBackground_color()));
		}else {
		}
		//Add Header view to show the category title!
			View viewTitle = inflater.inflate(R.layout.title_strip, null, false);
			
			viewTitle.findViewById(R.id.TitleHolder).setBackgroundColor(colors.getColor(category.getCheckboxes_color(), "80"));//setBackgroundDrawable(colors.getForePD());
			TextView titleContactsTV = (TextView)viewTitle.findViewById(R.id.TitleTV);
			titleContactsTV.setTextAppearance(getActivity(), style.TextAppearance_Large);
			titleContactsTV.setTypeface(MainActivity.FONT_TITLE, Typeface.BOLD);
			
			//titleContactsTV.setTextSize(titleContactsTV.getTextSize() - 10);
			if(Utils.isTablet(getActivity())){
				if(metrics.densityDpi >= 213 )
					titleContactsTV.setTextSize(titleContactsTV.getTextSize() - 5);
				else
					titleContactsTV.setTextSize(titleContactsTV.getTextSize() + 1);
			}
			else{
				if(metrics.densityDpi <= 240 )
					titleContactsTV.setTextSize(titleContactsTV.getTextSize() - 22);
				else
					titleContactsTV.setTextSize(titleContactsTV.getTextSize() - 30);
				//titleContactsTV.setTextSize(titleContactsTV.getTextSize() - 26);
			}
			titleContactsTV.setText(getResources().getString(R.string.multiple_choices));
			titleContactsTV.setTextColor(colors.getColor(colors.getTitle_color()));
			viewTitle.setClickable(false);
			listView.addHeaderView(viewTitle, null, false);
		
		
			listView.setDivider(new ColorDrawable(colors.getColor(colors.getTitle_color(), "80")));
		listView.setDividerHeight(1);
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
        Parameters ParamColor = realm.where(Parameters.class).findFirst();
        if (colors==null) {
            colors = new Colors(ParamColor);
        }
		
		((MainActivity)getActivity()).bodyFragment = "MultiSelectPagesFragment";
		if(((MainActivity)getActivity()).extras == null)
		((MainActivity)getActivity()).extras = new Bundle();
		int section_id = -1;
		if (getArguments().getInt("Section_id")!=0) {
			section_id = getArguments().getInt("Section_id");
			((MainActivity)getActivity()).extras.putInt("Section_id", section_id);
			id = section_id;
		}else {
			int category_id = getArguments().getInt("Category_id");
			((MainActivity)getActivity()).extras.putInt("Category_id", category_id);
			id = category_id;
		}
		
		time = System.currentTimeMillis();
		mainActivity = (MainActivity)activity;
		super.onAttach(activity);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);

		List<CommunElements1> communElements = new ArrayList<CommunElements1>();

		int category_id = getArguments().getInt("Category_id");
		category = realm.where(Category.class).equalTo("id",category_id).findFirst();
		//appController.getCategoryById(category_id);
		if (category != null) {
			titleInStrip = category.getTitle();
			checkBoxes_color = category.getCheckboxes_color();
			
			if (category.getChildren_categories().size()>1) {
				for (Iterator<Category> iterator = category.getChildren_categories().iterator(); iterator
						.hasNext();) {
					CommunElements1 communElement = (CommunElements1) iterator
							.next();
					communElements.add(communElement);
				}
			}else if (category.getChildren_categories().size()==1) {
				Category childCategory = category.getChildren_categories().iterator().next();
				titleInStrip = childCategory.getTitle();
				checkBoxes_color = childCategory.getCheckboxes_color();
				if (childCategory.getChildren_categories().size()>1) {
					for (Iterator<Category> iterator = childCategory.getChildren_categories().iterator(); iterator
							.hasNext();) {
						CommunElements1 communElement = (CommunElements1) iterator
								.next();
						communElements.add(communElement);
					}
				}else if (childCategory.getChildren_categories().size()==1) {
					
				}else {
					elements = childCategory.getChildren_pages();

					int size = elements.size();
					if (size > 1) {
						for (Iterator<Child_pages> iterator = elements
								.iterator(); iterator.hasNext();) {
							CommunElements1 communElement = (Child_pages) iterator
									.next();
							communElements.add(communElement);
						}
					} else if (size == 1) {
						Child_pages page = elements.iterator().next();
						if (page.getDesign().equals("panoramic")) {
							FragmentTransaction fragmentTransaction =
							         getActivity().getSupportFragmentManager().beginTransaction();
							 Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("panorama");
							    if (prev != null) {
							    	fragmentTransaction.remove(prev);
							    }
							    Fragment panoFragment = new PanoramaFragment();
							    ((MainActivity) getActivity()).extras.putInt("page_id", page.getId_cp());
							    ((MainActivity) getActivity()).bodyFragment = "PanoramaFragment";
							    panoFragment.setArguments(((MainActivity) getActivity()).extras);
							    fragmentTransaction.addToBackStack(null);
							 fragmentTransaction.replace(R.id.fragment_container, panoFragment, "panorama");
							 fragmentTransaction.commit();
						}else {
							((MainActivity)getActivity()).extras = new Bundle();
							((MainActivity)getActivity()).extras.putInt("page_id", page.getId_cp());
							PagesFragment pagesFragment = new PagesFragment();
							((MainActivity)getActivity()).bodyFragment = "PagesFragment";
							pagesFragment.setArguments(((MainActivity)getActivity()).extras);
							getActivity()
									.getSupportFragmentManager()
									.beginTransaction()
									.replace(R.id.fragment_container,
											pagesFragment).addToBackStack(null)
									.commit();
						}
						
					}
				}
			}else {
			
				elements = category.getChildren_pages();

				int size = elements.size();
				if (size > 1) {
					for (Iterator<Child_pages> iterator = elements
							.iterator(); iterator.hasNext();) {
						CommunElements1 communElement = (Child_pages) iterator
								.next();
						communElements.add(communElement);
					}
				} else if (size == 1) {
					Child_pages page = elements.iterator().next();
					((MainActivity)getActivity()).extras = new Bundle();
					((MainActivity)getActivity()).extras.putInt("page_id", page.getId_cp());
					PagesFragment pagesFragment = new PagesFragment();
					((MainActivity)getActivity()).bodyFragment = "PagesFragment";
					pagesFragment.setArguments(((MainActivity)getActivity()).extras);
					getActivity()
							.getSupportFragmentManager()
							.beginTransaction()
							.replace(R.id.fragment_container,
									pagesFragment).addToBackStack(null)
							.commit();
				}
			
			}
		}
		
		int section_id = getArguments().getInt("Section_id");

        Section section = realm.where(Section.class).equalTo("id",section_id).findFirst(); //appController.getSectionsDao().queryForId(section_id);

        if (section!=null) {
            titleInStrip = section.getTitle();
            if (section.getCategories().size() > 1) {
                for (Iterator<Category> iterator = section.getCategories()
                        .iterator(); iterator.hasNext();) {
                    CommunElements1 communElement = (CommunElements1) iterator
                            .next();
                    communElements.add(communElement);
                }
            } else if (section.getCategories().size() == 1) {

                Category childCategory = section.getCategories()
                        .iterator().next();
                titleInStrip = childCategory.getTitle();
                checkBoxes_color = childCategory.getCheckboxes_color();
                if (childCategory.getCategories().size() > 1) {
                    for (Iterator<Category> iterator = childCategory
                            .getCategories().iterator(); iterator
                            .hasNext();) {
                        CommunElements1 communElement = (CommunElements1) iterator
                                .next();
                        communElements.add(communElement);
                    }
                } else if (childCategory.getCategories().size() == 1) {

                } else {
                    elements = childCategory.getChildren_pages();

                    int size = elements.size();
                    if (size > 1) {
                        for (Iterator<Child_pages> iterator = elements
                                .iterator(); iterator.hasNext();) {
                            CommunElements1 communElement = (Child_pages) iterator
                                    .next();
                            communElements.add(communElement);
                        }
                    } else if (size == 1) {
                        Child_pages page = elements.iterator().next();
                        Bundle extra = new Bundle();
                        extra.putInt("page_id", page.getId_cp());
                        ((MainActivity)getActivity()).bodyFragment = "PagesFragment";
                        PagesFragment pagesFragment = new PagesFragment();
                        pagesFragment.setArguments(extra);
                        getActivity()
                                .getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container,
                                        pagesFragment).addToBackStack(null)
                                .commit();
                    }
                }
            }
        }


        adapter = new MultiSelectPagesAdapter(getActivity(), communElements, colors, checkBoxes_color);
		setListAdapter(adapter);
		
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
		AppHit hit = new AppHit(System.currentTimeMillis(), time, "sales_category", id);
		((MyApplication)getActivity().getApplication()).hits.add(hit);
		super.onDestroy();
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.ListFragment#getSelectedItemId()
	 */
	@Override
	public long getSelectedItemId() {
		// TODO Auto-generated method stub
		return super.getSelectedItemId();
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.ListFragment#getSelectedItemPosition()
	 */
	@Override
	public int getSelectedItemPosition() {
		// TODO Auto-generated method stub
		return super.getSelectedItemPosition();
	}
	
	private int actualPos = 1;
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.ListFragment#onListItemClick(android.widget.ListView, android.view.View, int, long)
	 */
	@Override
	public void onListItemClick(ListView list, View v, int position, long id) {
		CommunElements1 element = adapter.getElements().get(position-IS_HEADER_ADDED); // because of the listView header we subtract 1
		
		if(actualPos != position){
			if(list.getChildAt(actualPos - list.getFirstVisiblePosition()) != null)
			{
				list.getChildAt(actualPos - list.getFirstVisiblePosition()).setBackgroundColor(Color.TRANSPARENT);
//				getGridView().setItemChecked(MainActivity.lyricsPosition - getGridView().getFirstVisiblePosition(), false);
			}

		}
		actualPos = position;
		
		if (list.getChildAt(position - list.getFirstVisiblePosition()) != null) {
			list.getChildAt(position - list.getFirstVisiblePosition()).setBackgroundColor(colors.getColor(colors.getForeground_color()));
		}

		
		if (element instanceof Category) {
			Category category = (Category)element;
			
			MultiSelectPagesFragment categoryFragment = new MultiSelectPagesFragment();
			((MainActivity)getActivity()).bodyFragment = "CategoryFragment";
			// In case this activity was started with special instructions from an Intent,
			// pass the Intent's extras to the fragment as arguments
			((MainActivity)getActivity()).extras = new Bundle();
			((MainActivity)getActivity()).extras.putInt("Category_id", category.getId());
			categoryFragment.setArguments(((MainActivity)getActivity()).extras);
			// Add the fragment to the 'fragment_container' FrameLayout
			getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, categoryFragment).addToBackStack(null).commit();

		}else if (element instanceof Child_pages) {
			
			final Child_pages page = (Child_pages)element;
			if (CategoryTo.getParent(page,getActivity()) != 0/*page.getCategory().getCart_parent_category() != 0*/) {
				List<CartItem> itemsParent = checkCart();
				if (itemsParent.size() > 0) {
					final QuickAction quickAction = new QuickAction(getActivity(), QuickAction.VERTICAL, colors);
					quickAction.addActionItem(new ActionItem(getResources().getString(R.string.ingredient_popup_title), 0, true));
					for (int i = 0; i < itemsParent.size(); i++) {
						ActionItem actionItem = new ActionItem(itemsParent.get(i).getName(), i+1, itemsParent.get(i));
						quickAction.addActionItem(actionItem);
					}
					quickAction.setOnActionItemClickListener(new OnActionItemClickListener() {

								@Override
								public void onItemClick(QuickAction source,
										int pos, int actionId) {
									ActionItem actionItem = quickAction.getActionItem(pos);
									CartItem itemParent = actionItem.getCartItem();
									String price = "";
									RealmList<Price> prices = page.getPrices();
									Category category = null;
									if (page.getCategory() != null && page.getCategory().isNeeds_stripe_payment()) {
										category = page.getCategory();
									}else {

										category = realm.where(Category.class).equalTo("id",page.getCategory_id()).findFirst();
										//controller.getCategoryById(page.getCategory_id());
									}
									if(category != null && (MainActivity.stripe_or_not == -1 || (category.isNeeds_stripe_payment() && MainActivity.stripe_or_not == 1) ||
											(!category.isNeeds_stripe_payment() && MainActivity.stripe_or_not == 0))){
										if (category.isNeeds_stripe_payment()) {
											MainActivity.stripe_or_not = 1;
										}else {
											MainActivity.stripe_or_not = 0;
										}
										if (prices.size() > 0) {
											price = prices.iterator().next().getAmount();
										}
                                        mainActivity.id_Cart++;

                                       	CartItem tobeAdded = new CartItem(mainActivity.id_Cart,page, itemParent, 0, 1, null, price, page.getTitle(), new RealmList<CartItem>());
										itemParent.getCartItems().add(tobeAdded);
										/*	appController.getCartItemDao().createOrUpdate(tobeAdded);*/
                                        realm.beginTransaction();
                                        realm.copyToRealmOrUpdate(itemParent);
                                        //appController.getCartItemDao().createOrUpdate(itemParent);
                                        realm.commitTransaction();
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
							});
					
					quickAction.show(v);
				} else {
					final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
					DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int which) {
							dialog.dismiss();
							
						}
					};
					alertDialog.setTitle(getResources().getString(R.string.error_title_add_sup_without_base));
					final Category categoryParent = realm.where(Category.class).equalTo("id", category.getCart_parent_category()).findFirst();
					 //appController.getCategoryById(category.getCart_parent_category());
					if(categoryParent != null){
						String baseElment = categoryParent.getTitle();
						alertDialog.setMessage(getResources().getString(R.string.error_msg_add_sup_without_base, baseElment));
						
						alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel_cart), listener);
						alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, Html.fromHtml("<b><i>" +getString(R.string.error_btn_add_sup_without_base)+ "</i><b>"), new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
								//((MainActivity) getActivity()).openCategory(categoryParent);
								MultiSelectPagesFragment categoryFragment = new MultiSelectPagesFragment();
								((MainActivity) getActivity()).bodyFragment = "MultiSelectPagesFragment";
								if(((MainActivity) getActivity()).extras == null)
								((MainActivity) getActivity()).extras = new Bundle();
								((MainActivity) getActivity()).extras.putInt(
										"Category_id", categoryParent.getId());
								categoryFragment
								.setArguments(((MainActivity) getActivity()).extras);
								getActivity()
								.getSupportFragmentManager()
								.beginTransaction()
								.replace(R.id.fragment_container,
										categoryFragment)
										.setTransition(
												FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
												.addToBackStack(null).commit();
							}
						});
						
					}
					else{
						
						alertDialog.setMessage("Il faut d\'abord choisir la base ");
						alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok", listener );
					}

					alertDialog.show();
				}
			}else {
				String price = "";
				Collection<Price> prices = page
						.getPrices();
				if (prices.size() > 0) {
					price = prices.iterator().next()
							.getAmount();
				}
                mainActivity.id_Cart++;
				CartItem cartItem = new CartItem(  mainActivity.id_Cart,page, null, 0, 1, null, price, page.getTitle(), new RealmList<CartItem>());
				mainActivity.addItemToDB(1, page, null, 0, 1, null, price, page.getTitle(), "", new RealmList<CartItem>());
//					appController.getCartItemDao().createOrUpdate(cartItem);
				mainActivity.fillCart();
//					Handler handler = new Handler();
//			        long delay = 1300;
				mainActivity.getMenu().showMenu();
//			        handler.postDelayed(new Runnable() {
//			            @Override
//			            public void run() {
//			            	mainActivity.getMenu().toggle();
//			            }
//			        }, delay);
			}
			
			Bundle extra = new Bundle();
			extra.putInt("page_id", page.getId_cp());
			
		}
	}

	private List<CartItem> checkCart() {
		List<CartItem> results = new ArrayList<CartItem>();
		List<CartItem> cartItems = new ArrayList<CartItem>();
        cartItems = realm.where(CartItem.class).findAll();
        //appController.getCartItemDao().queryForAll();
        if (cartItems.size()>0) {
			for (int i = 0; i < cartItems.size(); i++) {
				if (cartItems.get(i).getChild_page() != null) {
					int idCat = cartItems.get(i).getChild_page().getCategory().getId();
					if (idCat == category.getCart_parent_category()) {
						results.add(cartItems.get(i));
					}
				}
			}
		}
		return results;
		
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.ListFragment#setListAdapter(android.widget.ListAdapter)
	 */
	@Override
	public void setListAdapter(ListAdapter adapter) {
		// TODO Auto-generated method stub
		super.setListAdapter(adapter);
	}

	/**
	 * 
	 */
	public MultiSelectPagesFragment() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * fill the cart with the {@link CartItem} taken from the database
	 */
	private void fillCart(){
		List<CartItem> cartItems = new ArrayList<CartItem>();
        cartItems = realm.where(CartItem.class).findAll();
        //appController.getCartItemDao().queryForAll();
        LinearLayout menuView = mainActivity.cartTagContainer;
		if (cartItems.size()>0) {
			menuView.removeAllViews();
			menuView.addView(getNewDivider());
			total = 0.0;
			for (int i = 0; i < cartItems.size(); i++) {
				mainActivity.addItemToCart(cartItems.get(i));
			}
		}else {
			menuView.removeAllViews();
		}
		mainActivity.totalSum.setText(total+"");
	}


	/**add a {@link CartItem} to the cart
	 * @param item
	 */
	protected void addItemToCart(final CartItem item) {
		SlidingMenu menu = mainActivity.getMenu();
		LinearLayout menuView = mainActivity.cartTagContainer;
		View cartItemView = layoutInflater.inflate(R.layout.cart_tag, null, false);
		//define common graphic elements
		TextView titleProduct = (AutoResizeTextView)cartItemView.findViewById(R.id.TitleProduit);
		titleProduct.setTypeface(MainActivity.FONT_BODY);
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
//				imageLoader.displayImage(path, imgFormule, options);
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
			RealmList<CartItem> items = item.getCartItems();
			for (Iterator<CartItem> iterator = items.iterator(); iterator.hasNext();) {
				CartItem cartItem = (CartItem) iterator.next();
				View subItem = layoutInflater.inflate(R.layout.cart_tag_sub_item, null, false);
				TextView tvElement = (AutoResizeTextView)subItem.findViewById(R.id.TitleProduit);
				tvElement.setTypeface(MainActivity.FONT_BODY);
				tvElement.setText(cartItem.getName());
				TextView subSum = (AutoResizeTextView)subItem.findViewById(R.id.relativeSum);
				subSum.setTypeface(MainActivity.FONT_BODY);
				subSum.setText(cartItem.getName());
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
                CartItem itemTmp =realm.where(CartItem.class).findFirst();
                realm.where(CartItem.class).findAll().remove(itemTmp);
                realm.commitTransaction();
				/*appController.getCartItemDao().delete(itemTmp);*/
                done  = true;
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
				/*	appController.getCartItemDao().update(arg0);*/
                    done  = true;
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
					/*appController.getCartItemDao().update(arg0);*/
                done  = true;
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
	
	private View getNewDivider() {
		View divider = new View(getActivity());
		divider.setLayoutParams(new LayoutParams(
				LayoutParams.MATCH_PARENT, 1));
		divider.setBackgroundColor(colors.getColor(colors
				.getForeground_color()));
		return divider;
	}
	
	@Override
	public void onStop() {
		AppHit hit = new AppHit(System.currentTimeMillis(), time, "sales_category", id);
		((MyApplication)getActivity().getApplication()).hits.add(hit);
		super.onStop();
	}
	

	
}
