package com.euphor.paperpad.activities.fragments;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.euphor.paperpad.Beans.MyString;
import com.euphor.paperpad.Beans.Parameters;
import com.euphor.paperpad.R;
import com.euphor.paperpad.activities.main.MainActivity;
import com.euphor.paperpad.activities.main.MyApplication;
import com.euphor.paperpad.adapters.MultiSelectPagesAdapter;
import com.euphor.paperpad.Beans.CartItem;
import com.euphor.paperpad.Beans.Category;
import com.euphor.paperpad.Beans.Formule;
import com.euphor.paperpad.Beans.FormuleElement;
import com.euphor.paperpad.Beans.Illustration;
import com.euphor.paperpad.Beans.MyBox;

import com.euphor.paperpad.utils.Colors;
import com.euphor.paperpad.utils.Utils;
import com.euphor.paperpad.utils.jsonUtils.AppHit;
import com.euphor.paperpad.widgets.ArrowImageView;
import com.euphor.paperpad.widgets.AutoResizeTextView;


import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;

public class MyBoxPageFragment extends Fragment{

    private static int IS_HEADER_ADDED = 1;
    private MultiSelectPagesAdapter adapter;
    private Colors colors;

    private Category category;
    private MainActivity mainActivity;
    private LayoutInflater layoutInflater;
    MyBox box;
    private Realm realm;

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
        if (getArguments() != null) {
            int box_id = getArguments().getInt("box_id");

            if (box_id != 0) {
                box =  realm.where(MyBox.class).equalTo("id_mb",box_id).findFirst();//appController.getMyBoxDao().queryForId(box_id);
                if (((MainActivity) getActivity()).extras == null)
                    ((MainActivity)getActivity()).extras = new Bundle();
                ((MainActivity)getActivity()).extras.putInt("box_id", box.getId_mb());
                ((MainActivity)getActivity()).bodyFragment = "MyBoxPageFragment";
            }
        }
        time = System.currentTimeMillis();
        super.onAttach(activity);
    }

    /* (non-Javadoc)
     * @see android.support.v4.app.ListFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutInflater = inflater;
        View view = inflater.inflate(R.layout.mybox_page_fragment, container, false);
//		if (box != null) {
        view.setBackgroundColor(colors.getColor(colors.getBackground_color()));
        drawValidity(view);
        colorDays(view);
        TextView myboxInfoTV = (TextView)view.findViewById(R.id.myboxInfoTV);
        myboxInfoTV.setTextSize(34);
        myboxInfoTV.setTextAppearance(getActivity(), android.R.style.TextAppearance_DeviceDefault_Large);
        myboxInfoTV.setTypeface(MainActivity.FONT_BOLD, Typeface.BOLD);
        myboxInfoTV.setText(box.getTitre_coffret());
        myboxInfoTV.setTextColor(colors.getColor(colors.getTitle_color()));



        WebView myboxInfoWV = (WebView)view.findViewById(R.id.myboxInfoWV);
        StringBuilder htmlString = new StringBuilder();
        int[] colorText = Colors.hex2Rgb(colors.getTitle_color());
        myboxInfoWV.setBackgroundColor(Color.TRANSPARENT);
        htmlString.append(Utils.paramBodyHTML(colorText));
        htmlString.append(box.getDescription());
        htmlString.append("</div></body></html>");
        myboxInfoWV.loadDataWithBaseURL(null, htmlString.toString(), "text/html", "UTF-8", null);
        TextView personne = (TextView)view.findViewById(R.id.personne);
        personne.setTypeface(MainActivity.FONT_BODY);
        Resources res = getResources();
        personne.setTextColor(colors.getColor(colors.getBody_color()));
        String text = String.format(res.getString(R.string.people), box.getNombre_personnes());
        personne.setText(text);
        TextView boxPrice = (TextView)view.findViewById(R.id.boxPrice);
        boxPrice.setTypeface(MainActivity.FONT_BODY);
        boxPrice.setText(box.getPrix()+ ",00 "+getString(R.string.euro));
        boxPrice.setTextColor(colors.getColor(colors.getBody_color()));

        RelativeLayout itemOrder = (RelativeLayout)view.findViewById(R.id.itemOrder);
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(colors.getColor(colors.getBackground_color())));
        drawable.addState(new int[]{android.R.attr.state_focused}, new ColorDrawable(colors.getColor(colors.getBackground_color())));
        drawable.addState(new int[]{}, new ColorDrawable(colors.getColor(colors.getBody_color())));
        itemOrder.setBackgroundDrawable(drawable);
        itemOrder.setPadding(10, 0, 10, 0);

        itemOrder.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                WebViewFragment webViewFragment = new WebViewFragment();
                ((MainActivity)getActivity()).bodyFragment = "WebViewFragment";
                // In case this activity was started with special instructions from an Intent,
                // pass the Intent's extras to the fragment as arguments
                ((MainActivity)getActivity()).extras = new Bundle();
                String link = box.getLien_web();
                if (!link.contains("http://") || !link.contains("https://")) {
                    link = "http://" + link;
                }
                ((MainActivity)getActivity()).extras.putString("link", link);
                webViewFragment.setArguments(((MainActivity)getActivity()).extras);
                // Add the fragment to the 'fragment_container' FrameLayout
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, webViewFragment).addToBackStack("WebViewFragment").commit();
            }
        });

        ArrowImageView arrowImg = (ArrowImageView)view.findViewById(R.id.imgArrow);
        Paint paint = new Paint();
        paint.setColor(colors.getColor(colors.getBackground_color(), "AA"));
        arrowImg.setPaint(paint);

        TextView orderOnline = (TextView)view.findViewById(R.id.orderOnline);

        ImageView imageBox = (ImageView)view.findViewById(R.id.imageBox);

        ColorStateList colorStateList = new ColorStateList(
                new int[][] {new int[] { android.R.attr.state_pressed }, new int[] {} },
                new int[] {colors.getColor(colors.getBody_color()), colors.getColor(colors.getBackground_color())});

        orderOnline.setTextColor(colorStateList);
//			orderOnline.setTextColor(colors.getColor(colors.getBackground_color()));
        RealmList<Illustration> illustrations=new RealmList<>();
        illustrations.add(box.getIllustration());
        if (illustrations.size()>0) {
            Illustration illust = illustrations.iterator().next();
            imageBox.setScaleType(ScaleType.CENTER_CROP);
            try {
                if (!illust.getPath().isEmpty()) {
                    Glide.with(getActivity()).load(new File(illust.getPath())).into(imageBox);
                }else {
                    Glide.with(getActivity()).load(illust.getLink()).into(imageBox);
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

//				String path = !illust.getPath().isEmpty()?"file:///"+illust.getPath():illust.getLink();
//
//				imageLoader.displayImage(path, imageBox, options);
        }


        // coloring
        ((TextView) view.findViewById(R.id.jours_de_disponibilite)).setTextColor(colors.getColor(colors.getTitle_color()));
        ((TextView) view.findViewById(R.id.offrez)).setTextColor(colors.getColor(colors.getTitle_color()));//ofrrez

        return view;
//		}else {
//			return new View(getActivity());
//		}


    }

    private void colorDays(View view) {
        ((TextView)view.findViewById(R.id.sundayTxt)).setTextColor(colors.getColor(colors.getBody_color()));
        ((TextView)view.findViewById(R.id.saturdayTxt)).setTextColor(colors.getColor(colors.getBody_color()));
        ((TextView)view.findViewById(R.id.fridayTxt)).setTextColor(colors.getColor(colors.getBody_color()));
        ((TextView)view.findViewById(R.id.thursdayTxt)).setTextColor(colors.getColor(colors.getBody_color()));
        ((TextView)view.findViewById(R.id.wednesdayTxt)).setTextColor(colors.getColor(colors.getBody_color()));
        ((TextView)view.findViewById(R.id.tuesdayTxt)).setTextColor(colors.getColor(colors.getBody_color()));
        ((TextView)view.findViewById(R.id.mondayTxt)).setTextColor(colors.getColor(colors.getBody_color()));

    }

    private void drawValidity(View view) {
        RealmList<MyString> validity = box.getList_validitee(); //box.getValiditee1();//box.getList()

        for (Iterator iterator = validity.iterator(); iterator.hasNext();) {
            MyString stringValidityBox = (MyString) iterator
                    .next();
            if (stringValidityBox.getMyString().equals("monday")) {
                ImageView imgMonday = (ImageView)view.findViewById(R.id.imgMonday);
                imgMonday.setImageDrawable(getResources().getDrawable(R.drawable.available));
            }else if (stringValidityBox.getMyString().equals("tuesday")) {
                ImageView imgTuesday = (ImageView)view.findViewById(R.id.imgTuesday);
                imgTuesday.setImageDrawable(getResources().getDrawable(R.drawable.available));
            }else if (stringValidityBox.getMyString().equals("wednesday")) {
                ImageView imgWednesday = (ImageView)view.findViewById(R.id.imgWednesday);
                imgWednesday.setImageDrawable(getResources().getDrawable(R.drawable.available));
            }else if (stringValidityBox.getMyString().equals("thursday")) {
                ImageView imgThursday = (ImageView)view.findViewById(R.id.imgThursday);
                imgThursday.setImageDrawable(getResources().getDrawable(R.drawable.available));
            }else if (stringValidityBox.getMyString().equals("friday")) {
                ImageView imgFriday = (ImageView)view.findViewById(R.id.imgFriday);
                imgFriday.setImageDrawable(getResources().getDrawable(R.drawable.available));
            }else if (stringValidityBox.getMyString().equals("saturday")) {
                ImageView imgSaturday = (ImageView)view.findViewById(R.id.imgSaturday);
                imgSaturday.setImageDrawable(getResources().getDrawable(R.drawable.available));
            }else if (stringValidityBox.getMyString().equals("sunday")) {
                ImageView imgSunday = (ImageView)view.findViewById(R.id.imgSunday);
                imgSunday.setImageDrawable(getResources().getDrawable(R.drawable.available));
            }
        }

        ((TextView)view.findViewById(R.id.mondayTxt)).setText(getResources().getString(R.string.monday_abbr));
        ((TextView)view.findViewById(R.id.tuesdayTxt)).setText(getResources().getString(R.string.tuesday_abbr));
        ((TextView)view.findViewById(R.id.wednesdayTxt)).setText(getResources().getString(R.string.wednesday_abbr));
        ((TextView)view.findViewById(R.id.thursdayTxt)).setText(getResources().getString(R.string.thursday_abbr));
        ((TextView)view.findViewById(R.id.fridayTxt)).setText(getResources().getString(R.string.friday_abbr));
        ((TextView)view.findViewById(R.id.saturdayTxt)).setText(getResources().getString(R.string.saturday_abbr));
        ((TextView)view.findViewById(R.id.sundayTxt)).setText(getResources().getString(R.string.sunday_abbr));

    }

    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);



    }

    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onDestroy()
     */
    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }


    private List<CartItem> checkCart() {
        List<CartItem> results = new ArrayList<CartItem>();
        List<CartItem> cartItems = new ArrayList<CartItem>();
        cartItems = realm.where(CartItem.class).findAll();//appController.getCartItemDao().queryForAll();
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

    /**
     *
     */
    public MyBoxPageFragment() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * fill the cart with the {@link CartItem} taken from the database
     */
    private void fillCart(){
        List<CartItem> cartItems = new ArrayList<CartItem>();
        cartItems = realm.where(CartItem.class).findAll();//appController.getCartItemDao().queryForAll();
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
        mainActivity.getMenu();
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
                subItem.findViewById(R.id.deleteContainer);
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
                realm.where(CartItem.class).findAll().remove(itemTmp);
                //appController.getCartItemDao().delete(itemTmp);
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
                    realm.copyToRealmOrUpdate(arg0);
                    //appController.getCartItemDao().update(arg0);
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
                realm.copyToRealmOrUpdate(arg0);
                //appController.getCartItemDao().update(arg0);
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
    private long time;

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
        AppHit hit = new AppHit(System.currentTimeMillis()/1000, time/1000, "sales_mybox_section", box.getId_mb());
        ((MyApplication)getActivity().getApplication()).hits.add(hit);
        super.onStop();
    }




}
