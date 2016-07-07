package com.euphor.paperpad.activities.fragments;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

//import com.crashlytics.android.Crashlytics;
import com.euphor.paperpad.Beans.Parameters;
import com.euphor.paperpad.R;
import com.euphor.paperpad.activities.main.MainActivity;
import com.euphor.paperpad.Beans.Child_pages;
import com.euphor.paperpad.Beans.paymentStripe.Payment_info;
import com.euphor.paperpad.Beans.paymentStripe.ProduitStripe;

import com.euphor.paperpad.utils.CommandUtils;
import com.euphor.paperpad.utils.CommandUtils.PostSendAsyncTask;
import com.euphor.paperpad.utils.Colors;
import com.euphor.paperpad.utils.Constants;
import com.euphor.paperpad.utils.VerifiedCard;
import com.euphor.paperpad.utils.jsonUtilities.AppJsonWriter.PostCallBack;

import me.brendanweinstein.util.ToastUtils;
import me.brendanweinstein.util.ViewUtils;
import me.brendanweinstein.views.FieldHolder;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
//import com.stripe.exception.AuthenticationException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import io.realm.Realm;

public class StripePaymentFragment extends DialogFragment implements PostCallBack
{
    public void setCard() {
    }
	private final static String TAG = StripePaymentFragment.class.getSimpleName();

	public static final float INPUT_WIDTH = 0.99f; // defined in terms of screen
																									// width
	private Button mSaveBtn;
	private ImageView mAcceptedCardsImg;
	private FieldHolder mFieldHolder;
	 VerifiedCard card;
	Payment_info info;
    private Realm realm;;


	
	public StripePaymentFragment() {
		
	}

	 //(non-Javadoc)* @see android.support.v4.app.DialogFragment#onActivityCreated(android.os.Bundle)

	@Override
	public void onActivityCreated(Bundle arg0) {
		super.onActivityCreated(arg0);
		setupViews();
	}

	 //(non-Javadoc)	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setStyle(STYLE_NO_TITLE, getTheme());
		View viewRoot = inflater.inflate(R.layout.add_credit_card, container, false);
		viewRoot.setBackgroundColor(colors.getColor(colors.getForeground_color()));
		mSaveBtn = (Button) viewRoot.findViewById(R.id.save_btn);
		mAcceptedCardsImg = (ImageView) viewRoot.findViewById(R.id.accepted_cards);
		mFieldHolder = (FieldHolder) viewRoot.findViewById(R.id.field_holder);
		mSaveBtn.setOnClickListener(mSaveBtnListener);
		
		ColorStateList colorSelector = new ColorStateList(
				new int[][] { 
						new int[] { android.R.attr.state_pressed }, new int[] {} },
						new int[] { colors.getColor(colors.getBackground_color()), colors.getColor(colors.getTitle_color()) });
		
		StateListDrawable drawable = new StateListDrawable();
		drawable.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(colors.getColor(colors.getTitle_color())));
		drawable.addState(new int[]{}, new ColorDrawable(colors.getColor(colors.getBackground_color())));
		
		StateListDrawable drawable_ = new StateListDrawable();
		drawable_.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(colors.getColor(colors.getBody_color())));
		drawable_.addState(new int[]{}, new ColorDrawable(colors.getColor(colors.getBackground_color())));

		mSaveBtn.setBackgroundDrawable(drawable);
		mSaveBtn.setTextColor(colorSelector);
		
		TextView title = (TextView)viewRoot.findViewById(R.id.title);
		title.setTextColor(colors.getColor(colors.getBackground_color()));
		TableLayout tableLayout = (TableLayout)viewRoot.findViewById(R.id.table_recap);
		tableLayout.setBackgroundColor(colors.getColor(colors.getBackground_color()));
		List<ProduitStripe> produits = new ArrayList<ProduitStripe>();
        produits=realm.where(ProduitStripe.class).findAll();

        double total = 0;
		for (Iterator<ProduitStripe> iterator = produits.iterator(); iterator.hasNext();) {
			ProduitStripe produitStripe = (ProduitStripe) iterator.next();
			TableRow row = (TableRow) inflater.inflate(R.layout.stripe_recap_row, null, false);
            List<Child_pages> child_pages = realm.where(Child_pages.class).equalTo("id", produitStripe.getId()).findAll();
            //appController.getChildPageDao().queryForEq("id", produitStripe.getId());
            if (child_pages.size()>0) {
                TextView product_name = (TextView)row.findViewById(R.id.product_name);
                product_name.setTypeface(MainActivity.FONT_TITLE);
                product_name.setText(child_pages.get(0).getTitle());
                product_name.setTextColor(colors.getColor(colors.getTitle_color()));
                TextView product_desc = (TextView)row.findViewById(R.id.product_desc);
                product_desc.setTypeface(MainActivity.FONT_BODY);
                product_desc.setTextColor(colors.getColor(colors.getBody_color()));
                product_desc.setText(child_pages.get(0).getIntro());
                TextView price_product = (TextView)row.findViewById(R.id.price_product);
                price_product.setTypeface(MainActivity.FONT_BODY);
                price_product.setTextColor(colors.getColor(colors.getBody_color()));
                String price = produitStripe.getPrice();
                String[] str = price.split("-");
                if (str.length>1) {
                    price = str[1];
                    try {
                        total = total + Double.parseDouble(price);
                    } catch (NumberFormatException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    price_product.setText(price +" €");
                }
//					price_product.setText(child_pages.get(0).getPr);
                tableLayout.addView(row);
            }
        }
		try{
			TableRow row = (TableRow) inflater.inflate(R.layout.stripe_recap_row, null, false);
			row.findViewById(R.id.product_name).setVisibility(View.GONE);
			TextView product_desc = (TextView)row.findViewById(R.id.product_desc);
			product_desc.setTypeface(MainActivity.FONT_BODY);
			product_desc.setText(getActivity().getString(R.string.shipping));
			product_desc.setTextColor(colors.getColor(colors.getBody_color()));
			TextView price_product = (TextView)row.findViewById(R.id.price_product);
			price_product.setTypeface(MainActivity.FONT_BODY);
			price_product.setTextColor(colors.getColor(colors.getBody_color()));
			Double price = info.getShipping();
			price_product.setText(price +" €");
			//		price_product.setText(child_pages.get(0).getPr);
			tableLayout.addView(row);
		}catch(Exception e){

		}

		TableRow row1 = (TableRow) inflater.inflate(R.layout.stripe_recap_row, null, false);
		row1.findViewById(R.id.product_name).setVisibility(View.GONE);
		TextView product_desc1 = (TextView)row1.findViewById(R.id.product_desc);
		product_desc1.setTypeface(MainActivity.FONT_BODY);
		product_desc1.setTextColor(colors.getColor(colors.getBody_color()));
		product_desc1.setText(getActivity().getString(R.string.total));
		TextView price_product1 = (TextView)row1.findViewById(R.id.price_product);
		price_product1.setTypeface(MainActivity.FONT_BODY);
		price_product1.setTextColor(colors.getColor(colors.getBody_color()));
		price_product1.setText(total +" €");
		//		price_product.setText(child_pages.get(0).getPr);
		tableLayout.addView(row1);
		
		viewRoot.findViewById(R.id.p_holder).setBackgroundColor(colors.getColor(colors.getBackground_color()));
		return viewRoot;
	}
	
	private OnClickListener mSaveBtnListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			ViewUtils.hideSoftKeyboard(getActivity());
			if (mFieldHolder.isFieldsValid()) {
				ToastUtils.showToast(getActivity(), "Valid credit card entry!");

					if (mFieldHolder.isFieldsValid()) {
						ToastUtils.showToast(getActivity(), "Valid credit card entry!");
					} else {
						ToastUtils.showToast(getActivity(), getResources().getString(R.string.pk_error_invalid_card_no));
					}

			}
		}
	};

	private Colors colors;

	
	private void setupViews() {
		float marginLeft = 1.0f - INPUT_WIDTH;
		ViewUtils.setMarginLeft(mAcceptedCardsImg, (int) (marginLeft * ViewUtils.getScreenWidth(getActivity())));
		ViewUtils.setWidth(mFieldHolder, (int) (INPUT_WIDTH * ViewUtils.getScreenWidth(getActivity())));
		if(Build.VERSION.SDK_INT < 14) {
			//mFieldHolder.setInputStyle(InputStyle.GINGERBREAD);
		}
	}

	@Override
	public void onPosted(boolean posted) {
		// TODO Auto-generated method stub
		Log.i("Payment success ? ", posted+"");
	}

	@SuppressWarnings("unused")
	@Override
	public void getResult(HashMap<String, Object> result) {
		int status = (Integer)result.get("Status");
		String response = (String) result.get("Result");
		if (status == 200) {
			Toast.makeText(getActivity(), "Payment success", Toast.LENGTH_SHORT).show();
		}else {
			Toast.makeText(getActivity(), "Payment failed, retry later", Toast.LENGTH_SHORT).show();
		}
		getDialog().dismiss();
	}

	//** @return the card

	public VerifiedCard getCard() {
		return card;
	}

	//** @param card the card to set

	public void setCard(VerifiedCard card) {
		this.card = card;
	}

	 //(non-Javadoc)
	 //* @see android.support.v4.app.DialogFragment#onAttach(android.app.Activity)

	@Override
	public void onAttach(Activity activity) {
				realm = Realm.getInstance(getActivity());



		colors = ((MainActivity)activity).colors;
        Parameters ParamColor = realm.where(Parameters.class).findFirst();
        if (colors==null) {
            // colors = new Colors(appController.getParametersDao().queryForId(1));
            colors = new Colors(ParamColor);
        }
		try {
			info = realm.where(Payment_info.class).findFirst();//appController.getPaymentInfoDao().queryForAll().get(0);
		} catch (Exception e) {
			// TODO: handle exception
		}
		super.onAttach(activity);
	}

	 //(non-Javadoc)* @see android.support.v4.app.DialogFragment#onCreate(android.os.Bundle)

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setRetainInstance(true);
		super.onCreate(savedInstanceState);
	}
	
}
