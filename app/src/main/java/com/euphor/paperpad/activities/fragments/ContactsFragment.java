/**
 * 
 */
package com.euphor.paperpad.activities.fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.euphor.paperpad.Beans.Parameters;
import com.euphor.paperpad.R;
import com.euphor.paperpad.activities.main.MainActivity;
import com.euphor.paperpad.activities.main.MyApplication;
import com.euphor.paperpad.adapters.ContactsAdapter;
import com.euphor.paperpad.Beans.Contact;
import com.euphor.paperpad.Beans.Illustration;
import com.euphor.paperpad.Beans.Section;

import com.euphor.paperpad.utils.Colors;
import com.euphor.paperpad.utils.Utils;
import com.euphor.paperpad.utils.jsonUtils.AppHit;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

/**
 * @author euphordev02
 *
 */
public class ContactsFragment extends Fragment {


    private Bitmap bm;
    private Colors colors;
    private long time;
    private int id;
    private boolean isTablet;
    private int section_id;
    private int id_contact;
    private Section contactSection;
    private Realm realm;
    /**
     *
     */
    public ContactsFragment() {
        // TODO Auto-generated constructor stub
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
        // TODO Auto-generated method stub
        super.onAttach(activity);
        realm = Realm.getInstance(getActivity());
   //new AppController(getActivity());
        colors = ((MainActivity)activity).colors;
        Parameters ParamColor = realm.where(Parameters.class).findFirst();
        if (colors==null) {
            colors = new Colors(ParamColor);
        }
        isTablet = Utils.isTablet(activity);
      //  isTablet = getResources().getBoolean(R.bool.isTablet);

        time = System.currentTimeMillis();
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
     * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contacts, container, false);
        view.setBackgroundColor(colors.getColor(colors.getBackground_color()));
        view.findViewById(R.id.contactsPrincipal).setBackgroundColor(colors.getColor(colors.getBackground_color(), "DD"));

        id_contact = getArguments().getInt("Section_id");

        // get Contact section from the database! and add the title of the section above the scrollView and the image in the right side
        contactSection = realm.where(Section.class).equalTo("id_s",id_contact).findFirst();
        // appController.getSectionsDao().queryForId(id_contact);

//			List<Section> sections = appController.getSectionsDao().queryForEq("type", "contact");
        if (contactSection != null) {
            section_id = contactSection.getId();
            view.findViewById(R.id.TitleHolder).setBackgroundDrawable(colors.getForePD());//.setBackgroundColor(colors.getColor(colors.getForeground_color()));
            TextView titleContactsTV = (TextView)view.findViewById(R.id.TitleTV);
            titleContactsTV.setTypeface(MainActivity.FONT_TITLE);
            titleContactsTV.setTextColor(colors.getColor(colors.getBackground_color()));
            if (contactSection.getTitle().isEmpty()) {
                titleContactsTV.setText(getString(R.string.contact_us));
            }else {

                titleContactsTV.setText(contactSection.getTitle());

            }

            ImageView imageContacts = (ImageView)view.findViewById(R.id.IVContacts);
            Illustration illustration = contactSection.getIllustrationObj();
//				String path;
//				if (illustration != null) {
//					path = !illustration.getPath().isEmpty()?"file:///"+illustration.getPath():illustration.getLink();
//				}else {
//					path = contactSection.getIllustration();
//				}
//				imageLoader.displayImage(path, imageContacts, options);
            try {
                if (!illustration.getPath().isEmpty()) {
                    Glide.with(getActivity()).load(new File(illustration.getPath())).into(imageContacts);
                }else {
                    Glide.with(getActivity()).load(illustration.getLink()).into(imageContacts);
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }


		/*
		 *  Task : adding contact boxes
		 *  what we do here is we include a already done view from the layouts this view contain two
		 *  contact boxes vertically if mode portrait horizontally when landscape!
		 */
        List<Contact> contacts = new ArrayList<Contact>();
        if (section_id == 0) {
            contacts = realm.where(Contact.class).findAll();//appController.getContactDao().queryForAll();
        }else {
            contacts.addAll(contactSection.getContacts());
        }

        if (isTablet) {
            LinearLayout contactsContainer = (LinearLayout)view.findViewById(R.id.ContactsContainer);
            View doubleContacts =  inflater.inflate(R.layout.contacts_row, null, false);



            int size = contacts.size();
            if (contacts.size()>0) {
                int k = size%2;
                for (int i = 0; i < contacts.size(); i +=2) {
                    doubleContacts =  inflater.inflate(R.layout.contacts_row, null, false);
                    LinearLayout contactLeft = (LinearLayout)doubleContacts.findViewById(R.id.ContactLeft);
                    contactLeft.setBackgroundColor(colors.getColor(colors.getForeground_color(), "80"));
                    contactLeft.setOnClickListener(getContactListener(contacts.get(i)));

                    doubleContacts.findViewById(R.id.ContactLeftInner).setBackgroundColor(colors.getColor(colors.getBackground_color()));
                    TextView leftContactTV = (TextView)doubleContacts.findViewById(R.id.TVContactLeft);
                    leftContactTV.setText(contacts.get(i).getTitle());
                    leftContactTV.setTypeface(MainActivity.FONT_TITLE, Typeface.BOLD);
                    leftContactTV.setTextColor(colors.getColor(colors.getTitle_color(), "FF"));
                    TextView leftDescTv = (TextView)doubleContacts.findViewById(R.id.TVDescContactLeft);
                    leftDescTv.setText(contacts.get(i).getDetails());
                    leftDescTv.setTypeface(MainActivity.FONT_BODY);
                    leftDescTv.setEllipsize(TruncateAt.END);
                    leftDescTv.setTextColor(colors.getColor(colors.getBody_color()));
                    ImageView leftImg = (ImageView)doubleContacts.findViewById(R.id.IVContactLeft);
                    //					leftImg.setImageBitmap(getSpecifiedTypeBitmap(contacts.get(i).getType()));
                    if (contacts.get(i).getIcon() != null && !contacts.get(i).getIcon().isEmpty()) {
                        bm = null;
                        try {
                            bm = BitmapFactory.decodeStream(getActivity().getAssets().open(contacts.get(i).getIcon()));
                        } catch (IOException e) {
                            Log.e("Contact Fragment", e.getMessage());
                            e.printStackTrace();
                        }
                    }else {
                        bm = getSpecifiedTypeBitmap(contacts.get(i).getType());
                    }
                    BitmapDrawable mDrawableLeft = new BitmapDrawable(bm);
                    mDrawableLeft.setColorFilter(new PorterDuffColorFilter(colors.getColor(colors.getForeground_color()),PorterDuff.Mode.MULTIPLY));
                    leftImg.setImageDrawable(mDrawableLeft);
                    if(i+1<size){
                        LinearLayout contactRight = (LinearLayout)doubleContacts.findViewById(R.id.ContactRight);
                        contactRight.setBackgroundColor(colors.getColor(colors.getForeground_color(), "80"));

                        contactRight.setOnClickListener(getContactListener(contacts.get(i+1)));
						/*StateListDrawable backClick = new StateListDrawable();
						backClick.addState(StateSet.NOTHING, new ColorDrawable(colors.getColor(colors.getBackground_color())));
						backClick.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(colors.getColor(colors.getForeground_color())));
						backClick.addState(new int[]{android.R.attr.state_focused}, new ColorDrawable(colors.getColor(colors.getForeground_color())));*/
                        doubleContacts.findViewById(R.id.ContactRightInner).setBackgroundColor(colors.getColor(colors.getBackground_color()));
                        //						doubleContacts.findViewById(R.id.ContactRightInner).setBackgroundDrawable(backClick);
                        TextView righContactTV = (TextView)doubleContacts.findViewById(R.id.TVContactRight);
                        righContactTV.setText(contacts.get(i+1).getTitle());
                        righContactTV.setTypeface(MainActivity.FONT_TITLE, Typeface.BOLD);
                        righContactTV.setTextColor(colors.getColor(colors.getTitle_color(), "FF"));
                        TextView rightDescTv = (TextView)doubleContacts.findViewById(R.id.TVDescContactRight);
                        rightDescTv.setText(contacts.get(i+1).getDetails());
                        rightDescTv.setTypeface(MainActivity.FONT_BODY);
                        rightDescTv.setEllipsize(TruncateAt.END);
                        rightDescTv.setTextColor(colors.getColor(colors.getBody_color()));
                        ImageView rightImg = (ImageView)doubleContacts.findViewById(R.id.IVContactRight);
                        if (contacts.get(i+1).getIcon() != null && !contacts.get(i+1).getIcon().isEmpty()) {
                            bm = null;
                            try {
                                bm = BitmapFactory.decodeStream(getActivity().getAssets().open(contacts.get(i+1).getIcon()));
                            } catch (IOException e) {
                                Log.e("Contact Fragment", e.getMessage());
                                e.printStackTrace();
                            }
                        }else {
                            bm = getSpecifiedTypeBitmap(contacts.get(i+1).getType());
                        }
                        BitmapDrawable mDrawableRight = new BitmapDrawable(bm);
                        //						BitmapDrawable mDrawableRight = new BitmapDrawable(getSpecifiedTypeBitmap(contacts.get(i+1).getType()));
                        mDrawableRight.setColorFilter(new PorterDuffColorFilter(colors.getColor(colors.getForeground_color()),PorterDuff.Mode.MULTIPLY));
                        rightImg.setImageDrawable(mDrawableRight);
                    }else {
                        LinearLayout rightContactSide = (LinearLayout)doubleContacts.findViewById(R.id.ContactRight);
                        rightContactSide.setVisibility(View.INVISIBLE);
                    }

                    contactsContainer.addView(doubleContacts);
                }

            }

        }else {
            ListView lv = (ListView)view.findViewById(R.id.lv);
            lv.setBackgroundColor(colors.getColor(colors.getBackground_color(), "AA"));
            ContactsAdapter adapter = new ContactsAdapter((MainActivity)getActivity(), contacts, colors);
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int arg2, long arg3) {
//					Toast.makeText(getActivity(), "hey", Toast.LENGTH_SHORT).show();
                    Contact contact = (Contact) arg0.getItemAtPosition(arg2);
                    getContactAction(contact );
                }


            });
        }

        return view;
    }


    private OnClickListener getContactListener(Contact contact) {
        TypeContact contactType = TypeContact.valueOf(contact.getType());
        OnClickListener l = new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

            }
        };
        final String link = contact.getLink();
        final int id_contact = contact.getId_con();

        switch (contactType.id) {
            case 1: // email icon/group_12/icon_0_12.png
                l= new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent mailer = new Intent(Intent.ACTION_SEND);
                        mailer.setType("message/rfc822");//text/plain
                        String email = link.replaceAll("mailto:", "");

                        mailer.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
                        startActivity(Intent.createChooser(mailer, "Send email..."));

                    }
                };

                break;

            case 2: // phone
                // http://backoffice.paperpad.fr/icon/group_0/icon_0_0.png
                l = new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        PackageManager pm = getActivity().getPackageManager();
                        boolean hasTelephony = pm.hasSystemFeature(PackageManager.FEATURE_TELEPHONY);
                        if (hasTelephony) {
                            Intent intent = new Intent(Intent.ACTION_CALL);
                            intent.setData(Uri.parse(link));
                            startActivity(intent);
                        }


                    }
                };
                break;
            case 3: // location
                // http://backoffice.paperpad.fr/icon/group_8/icon_0_8.png
                l = new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if(!link.isEmpty())
                        {
                            WebViewFragment webViewFragment = new WebViewFragment();
                            ((MainActivity)getActivity()).bodyFragment = "WebViewFragment";
                            // In case this activity was started with special instructions from an Intent,
                            // pass the Intent's extras to the fragment as arguments
                            ((MainActivity)getActivity()).extras = new Bundle();
                            ((MainActivity)getActivity()).extras.putString("link", link);
                            webViewFragment.setArguments(((MainActivity)getActivity()).extras);
                            // Add the fragment to the 'fragment_container' FrameLayout
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, webViewFragment).addToBackStack(null).commit();
                        }
                    }
                };
                break;
            case 4: // reservation
                // http://backoffice.paperpad.fr/icon/group_1/icon_0_1.png
                l = new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if(!link.isEmpty())
                        {
                            WebViewFragment webViewFragment = new WebViewFragment();
                            ((MainActivity)getActivity()).bodyFragment = "WebViewFragment";
                            // In case this activity was started with special instructions from an Intent,
                            // pass the Intent's extras to the fragment as arguments
                            ((MainActivity)getActivity()).extras = new Bundle();
                            ((MainActivity)getActivity()).extras.putString("link", link);
                            webViewFragment.setArguments(((MainActivity)getActivity()).extras);
                            // Add the fragment to the 'fragment_container' FrameLayout
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, webViewFragment).addToBackStack(null).commit();
                        }
                    }
                };
                break;
            case 5: // facebook
                // http://backoffice.paperpad.fr/icon/group_22/icon_0_22.png
                l = new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if(!link.isEmpty())
                        {
                            WebViewFragment webViewFragment = new WebViewFragment();
                            ((MainActivity)getActivity()).bodyFragment = "WebViewFragment";
                            // In case this activity was started with special instructions from an Intent,
                            // pass the Intent's extras to the fragment as arguments
                            ((MainActivity)getActivity()).extras = new Bundle();
                            ((MainActivity)getActivity()).extras.putString("link", link);
                            webViewFragment.setArguments(((MainActivity)getActivity()).extras);
                            // Add the fragment to the 'fragment_container' FrameLayout
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, webViewFragment).addToBackStack(null).commit();
                        }
                    }
                };
                break;
            case 6: // boutique icon/group_38/icon_0_38.png
                break;
            case 7: // gift
                // http://backoffice.paperpad.fr/icon/group_23/icon_0_23.png
                l = new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if(!link.isEmpty())
                        {
                            WebViewFragment webViewFragment = new WebViewFragment();
                            ((MainActivity)getActivity()).bodyFragment = "WebViewFragment";
                            // In case this activity was started with special instructions from an Intent,
                            // pass the Intent's extras to the fragment as arguments
                            ((MainActivity)getActivity()).extras = new Bundle();
                            ((MainActivity)getActivity()).extras.putString("link", link);
                            webViewFragment.setArguments(((MainActivity)getActivity()).extras);
                            // Add the fragment to the 'fragment_container' FrameLayout
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, webViewFragment).addToBackStack(null).commit();
                        }
                    }
                };
                break;
            case 8: // michelin icon/group_38/icon_0_38.png
                l = new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if(!link.isEmpty())
                        {
                            WebViewFragment webViewFragment = new WebViewFragment();
                            ((MainActivity)getActivity()).bodyFragment = "WebViewFragment";
                            // In case this activity was started with special instructions from an Intent,
                            // pass the Intent's extras to the fragment as arguments
                            ((MainActivity)getActivity()).extras = new Bundle();
                            ((MainActivity)getActivity()).extras.putString("link", link);
                            webViewFragment.setArguments(((MainActivity)getActivity()).extras);
                            // Add the fragment to the 'fragment_container' FrameLayout
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, webViewFragment).addToBackStack(null).commit();
                        }
                    }
                };
                break;
            case 9: // trip_advisor
                // http://backoffice.paperpad.fr/icon/group_22/icon_2_22.png
                l = new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if(!link.isEmpty())
                        {
                            WebViewFragment webViewFragment = new WebViewFragment();
                            ((MainActivity)getActivity()).bodyFragment = "WebViewFragment";
                            // In case this activity was started with special instructions from an Intent,
                            // pass the Intent's extras to the fragment as arguments
                            ((MainActivity)getActivity()).extras = new Bundle();
                            ((MainActivity)getActivity()).extras.putString("link", link);
                            webViewFragment.setArguments(((MainActivity)getActivity()).extras);
                            // Add the fragment to the 'fragment_container' FrameLayout
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, webViewFragment).addToBackStack(null).commit();
                        }
                    }
                };
                break;
            case 10: // twitter
                // http://backoffice.paperpad.fr/icon/group_22/icon_1_22.png
                l = new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if(!link.isEmpty())
                        {
                            WebViewFragment webViewFragment = new WebViewFragment();
                            ((MainActivity)getActivity()).bodyFragment = "WebViewFragment";
                            // In case this activity was started with special instructions from an Intent,
                            // pass the Intent's extras to the fragment as arguments
                            ((MainActivity)getActivity()).extras = new Bundle();
                            ((MainActivity)getActivity()).extras.putString("link", link);
                            webViewFragment.setArguments(((MainActivity)getActivity()).extras);
                            // Add the fragment to the 'fragment_container' FrameLayout
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, webViewFragment).addToBackStack(null).commit();
                        }
                    }
                };
                break;

            case 11: // twitter
                // http://backoffice.paperpad.fr/icon/group_22/icon_1_22.png
                l = new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        FormContactFragment formFragment = FormContactFragment.newInstance();
                        ((MainActivity)getActivity()).bodyFragment = "FormFragment";
                        // In case this activity was started with special instructions from an Intent,
                        // pass the Intent's extras to the fragment as arguments
                        ((MainActivity)getActivity()).extras = new Bundle();
                        ((MainActivity)getActivity()).extras.putInt("Section_id_form", section_id);
                        ((MainActivity)getActivity()).extras.putInt("Contact", id_contact);
                        formFragment.setArguments(((MainActivity)getActivity()).extras);
                        // Add the fragment to the 'fragment_container' FrameLayout
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, formFragment).addToBackStack(null).commit();

                    }
                };
                break;

            default:// http://backoffice.paperpad.fr/icon/group_38/icon_0_38.png
                l = new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if(!link.isEmpty())
                        {
                            WebViewFragment webViewFragment = new WebViewFragment();
                            ((MainActivity)getActivity()).bodyFragment = "WebViewFragment";
                            // In case this activity was started with special instructions from an Intent,
                            // pass the Intent's extras to the fragment as arguments
                            ((MainActivity)getActivity()).extras = new Bundle();
                            ((MainActivity)getActivity()).extras.putString("link", link);
                            webViewFragment.setArguments(((MainActivity)getActivity()).extras);
                            // Add the fragment to the 'fragment_container' FrameLayout
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, webViewFragment).addToBackStack(null).commit();
                        }
                    }
                };
                break;
        }
        return l;
    }

    private void getContactAction(Contact contact) {
        TypeContact contactType = TypeContact.valueOf(contact.getType());

        final String link = contact.getLink();
        final int id_contact = contact.getId_con();
        WebViewFragment webViewFragment = new WebViewFragment();

        switch (contactType.id) {
            case 1: // email icon/group_12/icon_0_12.png

                Intent mailer = new Intent(Intent.ACTION_SEND);
                mailer.setType("message/rfc822");//text/plain
                String email = link.replaceAll("mailto:", "");

                mailer.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
                startActivity(Intent.createChooser(mailer, "Send email..."));


                break;

            case 2: // phone
                // http://backoffice.paperpad.fr/icon/group_0/icon_0_0.png
                PackageManager pm = getActivity().getPackageManager();
                boolean hasTelephony = pm.hasSystemFeature(PackageManager.FEATURE_TELEPHONY);
                if (hasTelephony) {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse(link));
                    startActivity(intent);
                }


                break;
            case 3: // location
                // http://backoffice.paperpad.fr/icon/group_8/icon_0_8.png
                ((MainActivity)getActivity()).bodyFragment = "WebViewFragment";
                // In case this activity was started with special instructions from an Intent,
                // pass the Intent's extras to the fragment as arguments
                ((MainActivity)getActivity()).extras = new Bundle();
                ((MainActivity)getActivity()).extras.putString("link", link);
                webViewFragment.setArguments(((MainActivity)getActivity()).extras);
                // Add the fragment to the 'fragment_container' FrameLayout
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, webViewFragment).addToBackStack(null).commit();

                break;
            case 4: // reservation
                // http://backoffice.paperpad.fr/icon/group_1/icon_0_1.png

                ((MainActivity)getActivity()).bodyFragment = "WebViewFragment";
                // In case this activity was started with special instructions from an Intent,
                // pass the Intent's extras to the fragment as arguments
                ((MainActivity)getActivity()).extras = new Bundle();
                ((MainActivity)getActivity()).extras.putString("link", link);
                webViewFragment.setArguments(((MainActivity)getActivity()).extras);
                // Add the fragment to the 'fragment_container' FrameLayout
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, webViewFragment).addToBackStack(null).commit();

                break;
            case 5: // facebook
                // http://backoffice.paperpad.fr/icon/group_22/icon_0_22.png
                ((MainActivity)getActivity()).bodyFragment = "WebViewFragment";
                // In case this activity was started with special instructions from an Intent,
                // pass the Intent's extras to the fragment as arguments
                ((MainActivity)getActivity()).extras = new Bundle();
                ((MainActivity)getActivity()).extras.putString("link", link);
                webViewFragment.setArguments(((MainActivity)getActivity()).extras);
                // Add the fragment to the 'fragment_container' FrameLayout
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, webViewFragment).addToBackStack(null).commit();

                break;
            case 6: // boutique icon/group_38/icon_0_38.png
                break;
            case 7: // gift
                // http://backoffice.paperpad.fr/icon/group_23/icon_0_23.png
                ((MainActivity)getActivity()).bodyFragment = "WebViewFragment";
                // In case this activity was started with special instructions from an Intent,
                // pass the Intent's extras to the fragment as arguments
                ((MainActivity)getActivity()).extras = new Bundle();
                ((MainActivity)getActivity()).extras.putString("link", link);
                webViewFragment.setArguments(((MainActivity)getActivity()).extras);
                // Add the fragment to the 'fragment_container' FrameLayout
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, webViewFragment).addToBackStack(null).commit();

                break;
            case 8: // michelin icon/group_38/icon_0_38.png
                break;
            case 9: // trip_advisor
                // http://backoffice.paperpad.fr/icon/group_22/icon_2_22.png
                ((MainActivity)getActivity()).bodyFragment = "WebViewFragment";
                // In case this activity was started with special instructions from an Intent,
                // pass the Intent's extras to the fragment as arguments
                ((MainActivity)getActivity()).extras = new Bundle();
                ((MainActivity)getActivity()).extras.putString("link", link);
                webViewFragment.setArguments(((MainActivity)getActivity()).extras);
                // Add the fragment to the 'fragment_container' FrameLayout
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, webViewFragment).addToBackStack(null).commit();

                break;
            case 10: // twitter
                // http://backoffice.paperpad.fr/icon/group_22/icon_1_22.png
                ((MainActivity)getActivity()).bodyFragment = "WebViewFragment";
                // In case this activity was started with special instructions from an Intent,
                // pass the Intent's extras to the fragment as arguments
                ((MainActivity)getActivity()).extras = new Bundle();
                ((MainActivity)getActivity()).extras.putString("link", link);
                webViewFragment.setArguments(((MainActivity)getActivity()).extras);
                // Add the fragment to the 'fragment_container' FrameLayout
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, webViewFragment).addToBackStack(null).commit();

                break;

            case 11: // twitter
                // http://backoffice.paperpad.fr/icon/group_22/icon_1_22.png
                FormContactFragment formFragment = FormContactFragment.newInstance();
                ((MainActivity)getActivity()).bodyFragment = "FormFragment";
                // In case this activity was started with special instructions from an Intent,
                // pass the Intent's extras to the fragment as arguments
                ((MainActivity)getActivity()).extras = new Bundle();
                ((MainActivity)getActivity()).extras.putInt("Section_id_form", section_id);
                ((MainActivity)getActivity()).extras.putInt("Contact", id_contact);
                formFragment.setArguments(((MainActivity)getActivity()).extras);
                // Add the fragment to the 'fragment_container' FrameLayout
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, formFragment).addToBackStack(null).commit();

                break;

            default:// http://backoffice.paperpad.fr/icon/group_38/icon_0_38.png
                break;
        }
    }

    public Bitmap getSpecifiedTypeBitmap(String type) {
        TypeContact contact = null;
        try {
            contact = TypeContact.valueOf(type);
        } catch (Exception e1) {
            Log.e("Verify or add this type :", type);
            e1.printStackTrace();
        }
        try {
            switch (contact.id) {
                case 1: // email icon/group_12/icon_0_12.png

                    bm = BitmapFactory.decodeStream(getActivity().getAssets().open("icon/group_12/icon_0_12.png"));

                    break;

                case 2: // phone
                    // http://backoffice.paperpad.fr/icon/group_0/icon_0_0.png
                    bm = BitmapFactory.decodeStream(getActivity().getAssets().open("icon/group_0/icon_0_0.png"));
                    break;
                case 3: // location
                    // http://backoffice.paperpad.fr/icon/group_8/icon_0_8.png
                    bm = BitmapFactory.decodeStream(getActivity().getAssets().open("icon/group_8/icon_0_8.png"));
                    break;
                case 4: // reservation
                    // http://backoffice.paperpad.fr/icon/group_1/icon_0_1.png
                    bm = BitmapFactory.decodeStream(getActivity().getAssets().open("icon/group_1/icon_0_1.png"));
                    break;
                case 5: // facebook
                    // http://backoffice.paperpad.fr/icon/group_22/icon_0_22.png
                    bm = BitmapFactory.decodeStream(getActivity().getAssets().open("icon/group_22/icon_0_22.png"));
                    break;
                case 6: // boutique icon/group_38/icon_0_38.png
                    bm = BitmapFactory.decodeStream(getActivity().getAssets().open("icon/group_38/icon_0_38.png"));
                    break;
                case 7: // gift
                    // http://backoffice.paperpad.fr/icon/group_23/icon_0_23.png
                    bm = BitmapFactory.decodeStream(getActivity().getAssets().open("icon/group_23/icon_0_23.png"));
                    break;
                case 8: // michelin icon/group_38/icon_0_38.png
                    bm = BitmapFactory.decodeStream(getActivity().getAssets().open("icon/group_38/icon_0_38.png"));
                    break;
                case 9: // trip_advisor
                    // http://backoffice.paperpad.fr/icon/group_22/icon_2_22.png
                    bm = BitmapFactory.decodeStream(getActivity().getAssets().open("icon/group_22/icon_2_22.png"));
                    break;
                case 10: // twitter
                    // http://backoffice.paperpad.fr/icon/group_22/icon_1_22.png
                    bm = BitmapFactory.decodeStream(getActivity().getAssets().open("icon/group_22/icon_1_22.png"));
                    break;

                default:// http://backoffice.paperpad.fr/icon/group_38/icon_0_38.png
                    bm = BitmapFactory.decodeStream(getActivity().getAssets().open("icon/group_38/icon_0_38.png"));
                    break;
            }
        } catch (IOException e) {
            bm = null;
            e.printStackTrace();
        }
        return bm;

    }

    public enum TypeContact{

        email("email",1),
        phone("phone",2),
        location("location",3),
        reservation("reservation",4),
        facebook("facebook",5),
        boutique("boutique",6),
        gift("gift",7),
        michelin("michelin",8),
        trip_advisor("trip_advisor",9),
        twitter("twitter",10),
        form("form",11);


        private String name;
        private int id;
        /**
         * @return the name
         */
        public String getName() {
            return name;
        }
        /**
         * @param name the name to set
         */
        public void setName(String name) {
            this.name = name;
        }
        /**
         * @return the id
         */
        public int getId() {
            return id;
        }
        /**
         * @param id the id to set
         */
        public void setId(int id) {
            this.id = id;
        }

        private TypeContact(){

        }
        /**
         * @param name
         * @param ordinal
         */
        private TypeContact(String name, int ordinal) {
            this.name = name;
            this.id = ordinal;
        }


    }

    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onDestroy()
     */
    @Override
    public void onDestroy() {
        Runtime.getRuntime().gc();
        super.onDestroy();
    }

    @Override
    public void onStop() {
        AppHit hit = new AppHit(System.currentTimeMillis()/1000, time/1000, "sales_contacts_section", id);
        ((MyApplication)getActivity().getApplication()).hits.add(hit);
        super.onStop();
    }


}
