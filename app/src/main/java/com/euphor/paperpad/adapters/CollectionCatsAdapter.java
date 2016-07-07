/**
 * 
 */
package com.euphor.paperpad.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.euphor.paperpad.InterfaceRealm.CommunElements1;
import com.euphor.paperpad.R;
import com.euphor.paperpad.activities.main.MainActivity;
import com.euphor.paperpad.Beans.CartItem;
import com.euphor.paperpad.Beans.Category;
import com.euphor.paperpad.Beans.Child_pages;
import com.euphor.paperpad.Beans.Illustration;
import com.euphor.paperpad.Beans.Parameters;
import com.euphor.paperpad.Beans.Price;

import com.euphor.paperpad.utils.Colors;
import com.euphor.paperpad.utils.actionsPrices.ActionItem;
import com.euphor.paperpad.utils.actionsPrices.QuickAction;
import com.euphor.paperpad.utils.actionsPrices.QuickAction.OnActionItemClickListener;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;

/**
 * @author euphordev02
 *
 */
public class CollectionCatsAdapter extends BaseAdapter {

	private List<CommunElements1> items;
	private Context context;
	private CommunElements1 item;
	private Colors colors;
	private MainActivity mainActivity;
	private Parameters parameters;

    public Realm realm;


	/**
	 * @param cats
	 * @param context
	 * @param colors
	 * @param mainActivity
	 */
	public CollectionCatsAdapter(List<CommunElements1> cats, Context context, Colors colors, MainActivity mainActivity) {
		this.items = cats;
		this.context = context;
		this.colors = colors;
		this.mainActivity = mainActivity;

		
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return items.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public CommunElements1 getItem(int position) {
		// TODO Auto-generated method stub
		return items.get(position);
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView==null) {
			
			convertView  = inflater.inflate(R.layout.collection_list_item, parent, false);
//			LinearLayout albumItem = (LinearLayout)convertView.findViewById(R.id.holder);
//			albumItem.setBackgroundColor(colors.getColor(colors.getForeground_color(), "80"));
			LinearLayout innerAlbumItem = (LinearLayout)convertView.findViewById(R.id.holder);
//			innerAlbumItem.setBackgroundColor(colors.getColor(colors.getBackground_color()));
			StateListDrawable drawable = new StateListDrawable();
			drawable.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(colors.getColor(colors.getForeground_color())));
			drawable.addState(new int[]{android.R.attr.state_focused}, new ColorDrawable(colors.getColor(colors.getForeground_color())));
			drawable.addState(new int[]{}, new ColorDrawable(colors.getColor(colors.getBackground_color())));
			innerAlbumItem.setBackgroundDrawable(drawable);
		}
		final ViewHolder holder = new ViewHolder();
		holder.position = position;
		item = getItem(position); 
		holder.title = (TextView)convertView.findViewById(R.id.title);
		holder.detail = (TextView)convertView.findViewById(R.id.detail);
		holder.imageItem = (ImageView)convertView.findViewById(R.id.imageItem);
		
		/*Title element*/
		holder.title.setText(item.getCommunTitle1());
//		holder.title.setTextColor(colors.getColor(colors.getTitle_color()));
		
		/*Image element*/
		if (item.getIllustration1() != null) {
			
//			holder.imageItem.setScaleType(ScaleType.CENTER_CROP);
			try {
				Illustration illustration = item.getIllustration1();
				if (illustration != null) {
					if (!illustration.getPath().isEmpty()) {
						Glide.with(context).load(new File(illustration.getPath())).into(holder.imageItem);
					}else {
						Glide.with(context).load(illustration.getLink()).into(holder.imageItem);
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		
		/*Description item*/
//		holder.detail.setTextColor(colors.getColor(colors.getTitle_color(), "80"));
		if (item.getCommunDesc1().isEmpty()) {
			holder.detail.setVisibility(View.GONE);
		}else {
			holder.detail.setText(item.getCommunDesc1());
		}
		ViewTreeObserver vto = holder.detail.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
		    private int maxLines = -1;
		    @Override
		    public void onGlobalLayout() {
		        if (maxLines < 0 && holder.detail.getHeight() > 0 && holder.detail.getLineHeight() > 0) {
		            int height = holder.detail.getHeight();
		            int lineHeight = holder.detail.getLineHeight();
		            maxLines = height / lineHeight;
		            holder.detail.setMaxLines(maxLines);
		        }
		    }
		});
		
		ColorStateList colorStateList = new ColorStateList(
				new int[][] {
						new int[] { android.R.attr.state_pressed },
						new int[] {} },
						new int[] { colors.getColor(colors.getBackground_color(), "AA"),
								colors.getColor(colors.getTitle_color(), "AA") });
		holder.detail.setTextColor(colorStateList);
		holder.title.setTextColor(colorStateList);
		/*
		 * show the price if it's a list of child categories
		 */
		LinearLayout priceHolder = (LinearLayout)convertView.findViewById(R.id.priceContainer);
		if (item instanceof Category) {
			Category cat = (Category) item;
			priceHolder.setVisibility(View.GONE);
			
		}else if (item instanceof Child_pages) {
			priceHolder.setVisibility(View.VISIBLE);
			priceHolder.removeAllViews();
			final Child_pages page = (Child_pages) item;
			final ArrayList<Price> prices = new ArrayList<Price>();
			prices.addAll(page.getPrices());
			if (prices.size()>0) {


				parameters = null;
                parameters = mainActivity.realm.where(Parameters.class).findFirst(); //appController.getParametersDao().queryForId(1);

                View priceView = inflater.inflate(R.layout.price, null, false);

				LinearLayout btnPrice = (LinearLayout)priceView.findViewById(R.id.btnPrice);
				final Price price = prices.get(0);
				TextView label = (TextView)priceView.findViewById(R.id.priceLabelTV);
				if (price!=null && price.getLabel()!=null && !price.getLabel().isEmpty()) {
					label.setText(price.getLabel());
					label.setTextColor(colors.getColor(colors.getBody_color()));
				}else {
					label.setVisibility(View.GONE);
				}

				TextView value = (TextView)priceView.findViewById(R.id.priceValueTV);
				value.setText((price!=null && price.getAmount()!=null)?price.getAmount()+" "+price.getCurrency():"");
				value.setTextColor(colors.getColor(colors.getTitle_color()));
				Drawable drawable = context.getResources().getDrawable(R.drawable.white_back_rounded_corners);
				drawable.setColorFilter(colors.getColor(colors.getBody_color(), "55"), Mode.MULTIPLY);
				btnPrice.setBackgroundDrawable(drawable);
				btnPrice.setTag(price);



				btnPrice.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						Category category = null;
						if (page.getCategory() != null && page.getCategory().isNeeds_stripe_payment()) {
							category = page.getCategory();
						}else {
						//	AppController controller = new AppController(mainActivity);
                            category=  mainActivity.realm.where(Category.class).equalTo("id",page.getCategory_id()).findFirst();
						//	category = controller.getCategoryById(page.getCategory_id());
						}
						if(category != null && (MainActivity.stripe_or_not == -1 || (category.isNeeds_stripe_payment() && MainActivity.stripe_or_not == 1) ||
								(!category.isNeeds_stripe_payment() && MainActivity.stripe_or_not == 0))){
							if (category.isNeeds_stripe_payment()) {
								MainActivity.stripe_or_not = 1;
							}else {
								MainActivity.stripe_or_not = 0;
							}
							if ((page).getExtra_fields() != null &&
									page.getExtra_fields().getAlways_show_price_popover().equalsIgnoreCase("oui")) {

								if (prices.size()>0) {
									QuickAction quickAction = new QuickAction(context, QuickAction.VERTICAL, colors);
									ActionItem title = new ActionItem("Choisissez une option", 0, true);
									quickAction.addViewItem(title);
									for (int i = 0; i < prices.size(); i++) {
										String titleIem= prices.get(i).getLabel();
										if (titleIem != null && titleIem.isEmpty()) {
											titleIem = null;
										}
										boolean isShowIconCart = false;
										if (page.getExtra_fields() != null && page.getExtra_fields().getShow_carts_in_price_popover() != null && !page.getExtra_fields().getShow_carts_in_price_popover().isEmpty()) {
											isShowIconCart = true;
										}
										ActionItem item = new ActionItem(titleIem, i+1, prices.get(i), isShowIconCart);
										quickAction.addActionItem(item);
									}
									quickAction.setAnimStyle(QuickAction.ANIM_AUTO);
									quickAction.show(v);
									quickAction.setOnActionItemClickListener(new OnActionItemClickListener() {

										@Override
										public void onItemClick(QuickAction source, int pos, int actionId) {
											if (actionId>0) {
												ActionItem item = source.getActionItem(actionId);

									Price price = item.getPrice();


												Category category = null;
												if (page.getCategory() != null && page.getCategory().isNeeds_stripe_payment()) {
													category = page.getCategory();
												}else {
                                                    category =    mainActivity.realm.where(Category.class).equalTo("id",page.getCategory_id()).findFirst();
												//	category = appController.getCategoryById(page.getCategory_id());
												}
												if(category != null && (MainActivity.stripe_or_not == -1 || (category.isNeeds_stripe_payment() && MainActivity.stripe_or_not == 1) ||
														(!category.isNeeds_stripe_payment() && MainActivity.stripe_or_not == 0))){
													if (category.isNeeds_stripe_payment()) {
														MainActivity.stripe_or_not = 1;
													}else {
														MainActivity.stripe_or_not = 0;
													}
                                                    mainActivity.id_Cart++;
													CartItem cartItem = new CartItem(mainActivity.id_Cart,price.getId_price(), page, null, 0, item.getQuantity(), null, price.getAmount(), page.getTitle(), price.getLabel(), new RealmList<CartItem>());
													mainActivity.addItemToDB(price.getId_price(), page, null, 0, item.getQuantity(), null, price.getAmount(), page.getTitle(), price.getLabel(), new RealmList<CartItem>());
													mainActivity.fillCart();
													mainActivity.getMenu().showMenu();
												}else {
													//											Toast.makeText(getActivity(), "not allowed to add because mode :"+MainActivity.stripe_or_not, Toast.LENGTH_LONG).show();
													if (category!= null) {
														if (!category.isNeeds_stripe_payment() && MainActivity.stripe_or_not == 1) {
															AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
															builder.setTitle(mainActivity.getString(R.string.payment_stripe_title)).setMessage(mainActivity.getString(R.string.payment_stripe_msg))
															.setPositiveButton(mainActivity.getString(R.string.close_dialog),new DialogInterface.OnClickListener() {
																public void onClick(DialogInterface dialog, int id) {
																	dialog.cancel();
																}
															});

															builder.create().show();
														}else if (category.isNeeds_stripe_payment() && MainActivity.stripe_or_not == 0) {
															AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
															builder.setTitle(mainActivity.getString(R.string.payment_stripe_title)).setMessage(mainActivity.getString(R.string.no_payment_stripe_msg))
															.setPositiveButton(mainActivity.getString(R.string.close_dialog),new DialogInterface.OnClickListener() {
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
									});
								} 
							}else {
								
								mainActivity.addItemToDB(price.getId_price(), page, null, 0, 1, null, price.getAmount(), page.getTitle(), price.getLabel(), new RealmList<CartItem>());
								mainActivity.fillCart();
								mainActivity.getMenu().showMenu();
							}
						}else {
							//											Toast.makeText(getActivity(), "not allowed to add because mode :"+MainActivity.stripe_or_not, Toast.LENGTH_LONG).show();
							if (category!= null) {
								if (!category.isNeeds_stripe_payment() && MainActivity.stripe_or_not == 1) {
									AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
									builder.setTitle(mainActivity.getString(R.string.payment_stripe_title)).setMessage(mainActivity.getString(R.string.payment_stripe_msg))
									.setPositiveButton(mainActivity.getString(R.string.close_dialog),new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int id) {
											dialog.cancel();
										}
									});

									builder.create().show();
								}else if (category.isNeeds_stripe_payment() && MainActivity.stripe_or_not == 0) {
									AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
									builder.setTitle(mainActivity.getString(R.string.payment_stripe_title)).setMessage(mainActivity.getString(R.string.no_payment_stripe_msg))
									.setPositiveButton(mainActivity.getString(R.string.close_dialog),new DialogInterface.OnClickListener() {
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
				if (!parameters.isShow_cart()) {
					priceView.findViewById(R.id.imgPlus).setVisibility(View.GONE);
				}



				priceHolder.addView(priceView);
			}else {
				priceHolder.setVisibility(View.GONE);
			}
		}
		
		return convertView;
	}
	
	static class ViewHolder {
		TextView title;
		TextView detail; 
		  ImageView imageItem; // T : TOP , B : BOTTOM , L : LEFT , R : RIGHT
		  int position;
		}

}
