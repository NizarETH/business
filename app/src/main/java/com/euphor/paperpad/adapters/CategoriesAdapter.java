/**
 * 
 */
package com.euphor.paperpad.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
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
import com.euphor.paperpad.utils.Utils;
import com.euphor.paperpad.utils.actionsPrices.ActionItem;
import com.euphor.paperpad.utils.actionsPrices.QuickAction;
import com.euphor.paperpad.utils.actionsPrices.QuickAction.OnActionItemClickListener;
import com.euphor.paperpad.widgets.ArrowImageView;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;

/**
 * @author euphordev02
 *
 */
public class CategoriesAdapter extends BaseAdapter {

	private Context context;
	private List<CommunElements1> elements;
	//	private String POLICE;
	//	private ImageLoader imageLoader;
	//	DisplayImageOptions opts ;
	//	private DisplayImageOptions options;
	private Colors colors;
	private MainActivity mainActivity;
	private Parameters parameters;
	private int layout_item;
    private Realm realm;
	private ColorStateList color_txt;
    private boolean isTablet;
    //private boolean isNewDesign;
	//private int mItemSelected = 1;

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return elements.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public CommunElements1 getItem(int position) {
		// TODO Auto-generated method stub
		return elements.get(position);
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {

		return 0;
	}

	//	public void setItemSelected(int position){
	//	mItemSelected=position;
	//	Log.e(" setItemSelected : ",""+mItemSelected);
	//	}
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView; 
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(layout_item, parent,false);

		/** Uness Modif **/

		StateListDrawable d = new StateListDrawable();
		d.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(colors.getColor(colors.getForeground_color())));// colors.makeGradientToColor(colors.getTitle_color())); //
		d.addState(new int[]{}, new ColorDrawable(colors.getColor(colors.getBackground_color())));

		view.setBackgroundDrawable(d);


		final CommunElements1 element = getItem(position);
		ViewHolder holder = new ViewHolder();

		holder.position = position;

		holder.title = (TextView)view.findViewById(R.id.TVTitleCategory);
        if(isTablet) {
            holder.title.setTextAppearance(mainActivity, android.R.style.TextAppearance_DeviceDefault_Large);
        }else{
            holder.title.setTextAppearance(mainActivity, android.R.style.TextAppearance_DeviceDefault_Medium);
            holder.title.setSingleLine(false);
        }
        holder.title.setTypeface(MainActivity.FONT_TITLE);
		holder.title.setTextColor(color_txt);

/*		DisplayMetrics metrics = new DisplayMetrics();
		mainActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics); 

		if(mainActivity.getResources().getBoolean(R.bool.isTablet)){
			if(metrics.densityDpi >= 213 )
				holder.title.setTextSize(holder.title.getTextSize() - 3);
			else
				holder.title.setTextSize(holder.title.getTextSize());
		}
		else{
			if(metrics.densityDpi <= 240 )
				holder.title.setTextSize(holder.title.getTextSize() - 6);
			else
				holder.title.setTextSize(holder.title.getTextSize() - 10);

		}*/
		//holder.title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 26);

		holder.title.setText(element.getCommunTitle1());

		holder.imageItem = (ImageView)view.findViewById(R.id.imgCategory);


		if (element.getIllustration1() != null) {
			holder.imageItem.setVisibility(View.VISIBLE);
			holder.imageItem.setScaleType(ScaleType.CENTER_CROP);
			Illustration illustration = element.getIllustration1();
			if (!illustration.getPath().isEmpty()) {
				Glide.with(context).load(new File(illustration.getPath())).into(holder.imageItem);
			}else {
				try {
					Glide.with(context).load(illustration.getLink()).into(holder.imageItem);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		else {
			holder.imageItem.setVisibility(View.GONE);
		}


		{


			holder.desc = (TextView)view.findViewById(R.id.TVDescCategory);
            holder.desc.setTextAppearance(mainActivity, android.R.style.TextAppearance_DeviceDefault_Small);
            holder.desc.setTypeface(MainActivity.FONT_BODY);
			holder.desc.setTextColor(colors.getColor(colors.getBody_color(), "AA"));
			if (element.getCommunDesc1().isEmpty()) {
				holder.desc.setVisibility(View.GONE);
			}else {
				holder.desc.setText(element.getCommunDesc1());
			}

			/*
			 * show the price if it's a list of child categories
			 */
			realm = Realm.getInstance(mainActivity);

			parameters = null;
            parameters = realm.where(Parameters.class).findFirst();
            //mainActivity.appController.getParametersDao().queryForId(1);

            holder.btnPrice = (LinearLayout)view.findViewById(R.id.btnPrice);
			holder.btnPrice2 = (LinearLayout)view.findViewById(R.id.btnPriceTwo);
			holder.btnPrice3 = (LinearLayout)view.findViewById(R.id.btnPriceThree);
			try {
				if(holder.btnPrice2 != null)
					holder.btnPrice2.setVisibility(View.GONE);
				if(holder.btnPrice3 != null)
					holder.btnPrice3.setVisibility(View.GONE);
			} catch (NullPointerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (element instanceof Child_pages && isTablet) {
				final Child_pages child_page= (Child_pages)element;
				if (!child_page.getDesign().equals("formule")) { /** Uness Modif delete negation condition **/


					final ArrayList<Price> prices = new ArrayList<Price>();
					prices.addAll(child_page.getPrices());

					if (holder.btnPrice != null) {
						if (prices.size()>0) {
							Price price = prices.get(0);
							TextView label = (TextView)view.findViewById(R.id.priceLabelTV);
							if (price!=null && price.getLabel()!=null && !price.getLabel().isEmpty()) {
								label.setText(price.getLabel());
								label.setTextColor(colors.getColor(colors.getBody_color()));
							}else {

									label.setVisibility(View.GONE);
							}

							TextView value = (TextView)view.findViewById(R.id.priceValueTV);
							value.setText((price!=null && price.getAmount()!=null)?price.getAmount()+" "+price.getCurrency():"");
							value.setTextColor(colors.getColor(colors.getTitle_color()));
							Drawable drawable = context.getResources().getDrawable(R.drawable.white_back_rounded_corners);
							drawable.setColorFilter(colors.getBackMixColor(colors.getForeground_color(), 0.10f), Mode.MULTIPLY);
							holder.btnPrice.setBackgroundDrawable(drawable);
							holder.btnPrice.setTag(price);



							holder.btnPrice.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									if (parameters != null && parameters.isShow_cart()) {

										Category category = null;
										if (child_page.getCategory() != null && child_page.getCategory().isNeeds_stripe_payment()) {
											category = child_page.getCategory();
										}else {

											category = realm.where(Category.class).equalTo("id",child_page.getCategory_id()).findFirst();
											 //controller.getCategoryById(child_page.getCategory_id());
										}
										if(category != null && (MainActivity.stripe_or_not == -1 || (category.isNeeds_stripe_payment() && MainActivity.stripe_or_not == 1) ||
												(!category.isNeeds_stripe_payment() && MainActivity.stripe_or_not == 0))){
											if (category.isNeeds_stripe_payment()) {
												MainActivity.stripe_or_not = 1;
											}else {
												MainActivity.stripe_or_not = 0;
											}
											if (prices.size()>0) {
												if (((Child_pages)element).getExtra_fields() != null &&
														((Child_pages)element).getExtra_fields().getAlways_show_price_popover().equalsIgnoreCase("oui")) {


													QuickAction quickAction = new QuickAction(context, QuickAction.VERTICAL, colors);
													ActionItem title = new ActionItem("Choisissez une option", 0, true);
													quickAction.addViewItem(title);
													for (int i = 0; i < prices.size(); i++) {
														String titleIem= prices.get(i).getLabel();
														if (titleIem != null && titleIem.isEmpty()) {
															titleIem = null;
														}
														boolean isShowIconCart = false;
														if (child_page.getExtra_fields() != null && child_page.getExtra_fields().getShow_carts_in_price_popover() != null && !child_page.getExtra_fields().getShow_carts_in_price_popover().isEmpty()) {
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

																mainActivity.addItemToDB(price.getId_price(), child_page, null, 0, item.getQuantity(), null, price.getAmount(), child_page.getTitle(), price.getLabel(), new RealmList<CartItem>());
																mainActivity.fillCart();
																mainActivity.getMenu().showMenu();
															}
														}
													});

												}else {
													Price price = prices.get(0);
													//																 CartItem cartItem = new CartItem(price.getId_price(), child_page, null, 0, item.getQuantity(), null, price.getAmount(), child_page.getTitle(), price.getLabel(), new ArrayList<CartItem>());
													mainActivity.addItemToDB(price.getId_price(), child_page, null, 0, 1, null, price.getAmount(), child_page.getTitle(), price.getLabel(), new RealmList<CartItem>());
													mainActivity.fillCart();
													mainActivity.getMenu().showMenu();
												}
											}
										}else {
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

							if (!parameters.isShow_cart()) {
								view.findViewById(R.id.imgPlus).setVisibility(View.GONE);
							}else{
								view.findViewById(R.id.imgArrowContainer).setVisibility(View.VISIBLE);
								view.findViewById(R.id.txtPlus).setVisibility(View.VISIBLE);
								((TextView)view.findViewById(R.id.txtPlus)).setTextColor(colors.getColor(colors.getForeground_color()));
							}

						}else {

							holder.btnPrice.setVisibility(View.GONE);
						}
					}

					TextView label;
					TextView value = null;
					Price price;
				}else if(child_page.getDesign().equals("formule")){
					/** Uness Modif delete negation condition **/


					final ArrayList<Price> prices = new ArrayList<Price>();
					prices.addAll(child_page.getPrices());

					if (holder.btnPrice != null) {
						if (prices.size()>0) {
							view.findViewById(R.id.priceTVContainer).setBackgroundColor(Color.TRANSPARENT);
							view.findViewById(R.id.priceBtnHolder).setBackgroundColor(Color.TRANSPARENT);
							view.findViewById(R.id.btnPrice).setBackgroundColor(Color.TRANSPARENT);
							Price price = prices.get(0);
							TextView label = (TextView)view.findViewById(R.id.priceLabelTV);
							if (price!=null && price.getLabel()!=null && !price.getLabel().isEmpty()) {
								label.setText(price.getLabel());
								label.setTextColor(colors.getColor(colors.getBody_color()));
							}else {
								label.setVisibility(View.GONE);
							}
							TextView value = (TextView)view.findViewById(R.id.priceValueTV);
							value.setText((price!=null && price.getAmount()!=null)?price.getAmount()+" "+price.getCurrency():"");
							value.setTextColor(colors.getColor(colors.getTitle_color()));
							//							 Drawable drawable = context.getResources().getDrawable(R.drawable.white_back_rounded_corners);
							//							 drawable.setColorFilter(colors.getColor(colors.getBody_color(), "55"), Mode.MULTIPLY);
							//							 holder.btnPrice.setBackgroundDrawable(drawable);
							holder.btnPrice.setTag(price);
							view.findViewById(R.id.imgPlus).setVisibility(View.GONE);


						}else {

							holder.btnPrice.setVisibility(View.GONE);
						}
					}

				}else {	 
					holder.btnPrice.setVisibility(View.GONE);
					view.findViewById(R.id.priceBtnHolder).setVisibility(View.GONE);
				}
			}else if (element instanceof Child_pages && !isTablet) {

				final Child_pages child_page= (Child_pages)element;
				if (!child_page.getDesign().equals("formule")) {
					//					view.findViewById(R.id.btnPriceTwo).setVisibility(View.GONE);
					//					view.findViewById(R.id.btnPriceThree).setVisibility(View.GONE);
					final ArrayList<Price> prices = new ArrayList<Price>();
					prices.addAll(child_page.getPrices());


					if (prices.size()>0) {
						Price price = prices.get(0);
						TextView label = (TextView)view.findViewById(R.id.priceLabelTV);
						if (price!=null && price.getLabel()!=null && !price.getLabel().isEmpty()) {
							label.setText(price.getLabel());
							label.setTextColor(colors.getColor(colors.getBody_color()));
						}else {
							label.setVisibility(View.GONE);
						}
						TextView value = (TextView)view.findViewById(R.id.priceValueTV);
						value.setText((price!=null && price.getAmount()!=null)?price.getAmount()+price.getCurrency():"");
						value.setTextColor(colors.getColor(colors.getTitle_color()));
						Drawable drawable = context.getResources().getDrawable(R.drawable.white_back_rounded_corners);
						drawable.setColorFilter(colors.getBackMixColor(colors.getForeground_color(), 0.10f), Mode.MULTIPLY);
						holder.btnPrice.setBackgroundDrawable(drawable);
						holder.btnPrice.setTag(price);



						holder.btnPrice.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								if (parameters != null && parameters.isShow_cart_smartphone()) {
									Category category = null;
									if (child_page.getCategory() != null && child_page.getCategory().isNeeds_stripe_payment()) {
										category = child_page.getCategory();
									}else {

										category = realm.where(Category.class).equalTo("id",child_page.getCategory_id()).findFirst();
										//controller.getCategoryById(child_page.getCategory_id());
									}
									if(category != null && (MainActivity.stripe_or_not == -1 || (category.isNeeds_stripe_payment() && MainActivity.stripe_or_not == 1) ||
											(!category.isNeeds_stripe_payment() && MainActivity.stripe_or_not == 0))){
										if (category.isNeeds_stripe_payment()) {
											MainActivity.stripe_or_not = 1;
										}else {
											MainActivity.stripe_or_not = 0;
										}
										if (prices.size()>0) {
											if (((Child_pages)element).getExtra_fields() != null &&
													((Child_pages)element).getExtra_fields().getAlways_show_price_popover().equalsIgnoreCase("oui")) {


												QuickAction quickAction = new QuickAction(context, QuickAction.VERTICAL, colors);
												ActionItem title = new ActionItem("Choisissez une option", 0, true);
												quickAction.addViewItem(title);
												for (int i = 0; i < prices.size(); i++) {
													String titleIem= prices.get(i).getLabel();
													if (titleIem != null && titleIem.isEmpty()) {
														titleIem = null;
													}
													boolean isShowIconCart = false;
													if (child_page.getExtra_fields() != null && child_page.getExtra_fields().getShow_carts_in_price_popover() != null && !child_page.getExtra_fields().getShow_carts_in_price_popover().isEmpty()) {
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
															mainActivity.addItemToDB(price.getId_price(), child_page, null, 0, item.getQuantity(), null, price.getAmount(), child_page.getTitle(), price.getLabel(), new RealmList<CartItem>());
															mainActivity.fillCart();
															mainActivity.getMenu().showMenu();
														}
													}
												});
											}else {

												Price price = prices.get(0);
												//																 CartItem cartItem = new CartItem(price.getId_price(), child_page, null, 0, item.getQuantity(), null, price.getAmount(), child_page.getTitle(), price.getLabel(), new ArrayList<CartItem>());
												mainActivity.addItemToDB(price.getId_price(), child_page, null, 0, 1, null, price.getAmount(), child_page.getTitle(), price.getLabel(), new RealmList<CartItem>());
												mainActivity.fillCart();
												mainActivity.getMenu().showMenu();

											}
										} 
									}else {
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

						if (!parameters.isShow_cart()) {
							view.findViewById(R.id.imgPlus).setVisibility(View.GONE);
						}else{
							view.findViewById(R.id.imgArrowContainer).setVisibility(View.VISIBLE);
							view.findViewById(R.id.txtPlus).setVisibility(View.VISIBLE);
							((TextView)view.findViewById(R.id.txtPlus)).setTextColor(colors.getColor(colors.getForeground_color()));
						}


					}else {

						holder.btnPrice.setVisibility(View.GONE);
					}
				}else if(child_page.getDesign().equals("formule")){
					/** Uness Modif delete negation condition **/


					final ArrayList<Price> prices = new ArrayList<Price>();
					prices.addAll(child_page.getPrices());

					if (holder.btnPrice != null) {
						if (prices.size()>0) {
							Price price = prices.get(0);
							view.findViewById(R.id.priceTVContainer).setBackgroundColor(Color.TRANSPARENT);
							view.findViewById(R.id.priceBtnHolder).setBackgroundColor(Color.TRANSPARENT);
							view.findViewById(R.id.btnPrice).setBackgroundColor(Color.TRANSPARENT);

							TextView label = (TextView)view.findViewById(R.id.priceLabelTV);
							if (price!=null && price.getLabel()!=null && !price.getLabel().isEmpty()) {
								label.setText(price.getLabel());
								label.setTextColor(colors.getColor(colors.getBody_color()));
							}else {
								label.setVisibility(View.GONE);
							}
							TextView value = (TextView)view.findViewById(R.id.priceValueTV);
							value.setText((price!=null && price.getAmount()!=null)?price.getAmount()+" "+price.getCurrency():"");
							value.setTextColor(colors.getColor(colors.getTitle_color()));
							//							 Drawable drawable = context.getResources().getDrawable(R.drawable.white_back_rounded_corners);
							//							 drawable.setColorFilter(colors.getColor(colors.getBody_color(), "55"), Mode.MULTIPLY);
							//							 holder.btnPrice.setBackgroundDrawable(drawable);
							holder.btnPrice.setTag(price);
							view.findViewById(R.id.imgPlus).setVisibility(View.GONE);


						}else {

							holder.btnPrice.setVisibility(View.GONE);
						}
					}

				}
			}else {
				holder.btnPrice.setVisibility(View.GONE);
				view.findViewById(R.id.priceBtnHolder).setVisibility(View.GONE);
			}
			ArrowImageView arrowImg = (ArrowImageView)view.findViewById(R.id.imgArrow);
			//			LinearLayout.LayoutParams layoutParams = (LayoutParams) arrowImg.getLayoutParams();
			//			layoutParams.height = 22;
			//			layoutParams.width = 22;
			arrowImg.setLayoutParams(new LinearLayout.LayoutParams(20, 20));
			//			arrowImg.setLayoutParams(layoutParams);
			//			Paint paint = new Paint();
			//			paint.setColor(colors.getColor(colors.getForeground_color(),"AA"));
			//			arrowImg.setPaint(paint);

			StateListDrawable d_ = new StateListDrawable();
			d_.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(colors.getColor(colors.getForeground_color())));// colors.makeGradientToColor(colors.getTitle_color())); //
			d_.addState(new int[]{}, new ColorDrawable(colors.getColor(colors.getBackground_color())));


			arrowImg.setDiffOfColorCode(colors.getColor(colors.getForeground_color()), colors.getColor(colors.getBackground_color()));
			arrowImg.setBackgroundDrawable(d_);

			//			if (element instanceof Child_pages) {
			//				if (((Child_pages)element).isHide_product_detail()) {
			//					arrowImg.setVisibility(View.GONE);
			//					view.findViewById(R.id.imgArrowContainer).setVisibility(View.GONE);
			//				}
			//			}

			if (element instanceof Child_pages) {
				if (((Child_pages)element).isHide_product_detail()) {
					arrowImg.setVisibility(View.GONE);
					view.findViewById(R.id.imgPlus).setVisibility(View.GONE);
					//view.findViewById(R.id.imgArrowContainer).setVisibility(View.GONE);
				}
			}
		}
		return view;
	}

	/**
	 * @param activity
	 * @param elements
     * @param colors
     * @param layout_item
     *
	 */
	public CategoriesAdapter(MainActivity activity, List<CommunElements1> elements, Colors colors, int layout_item) {
		//this.options = options;
		this.mainActivity = activity;
		this.context = activity.getApplicationContext();
		this.elements = elements;
		this.colors = colors;
		this.layout_item = layout_item;
        this.isTablet = Utils.isTablet(activity.getApplicationContext());
        //this.isNewDesign = isNewDesign;
		//			POLICE = "fonts/gill-sans-light.ttf";

		color_txt = new ColorStateList(new int[][]{{android.R.attr.state_pressed},{}}, 
				new int[]{colors.getColor(colors.getBackground_color()),
				colors.getColor(colors.getTitle_color())});
	}

	/**
	 * @return the elements
	 */
	public List<CommunElements1> getElements() {
		return elements;
	}

	/**
	 * @param elements the elements to set
	 */
	public void setElements(List<CommunElements1> elements) {
		this.elements = elements;
	}

	static class ViewHolder{
		TextView title;
		TextView desc; 
		ImageView imageItem;
		LinearLayout btnPrice;
		LinearLayout btnPrice2;
		LinearLayout btnPrice3;
		int position;
	}

}
