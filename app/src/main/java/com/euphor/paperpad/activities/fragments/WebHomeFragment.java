package com.euphor.paperpad.activities.fragments;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.euphor.paperpad.Beans.Contact;
import com.euphor.paperpad.R;
import com.euphor.paperpad.activities.main.MainActivity;
import com.euphor.paperpad.Beans.Category;
import com.euphor.paperpad.Beans.Child_pages;
import com.euphor.paperpad.Beans.Section;
import com.euphor.paperpad.Beans.Tab;

import com.euphor.paperpad.utils.Utils;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by euphordev02 on 26/09/2014.
 */
public class WebHomeFragment extends Fragment {

    /**
     * You'll need this in your class to cache the helper in the class.
     */


    private String testLink;
    //private Colors colors;
    //private boolean isRss;
    //private long time;
    //private SharedPreferences wmbPreference;
    public Realm realm;

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle arg0) {

        super.onCreate(arg0);
        setRetainInstance(true);


    }

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
    }

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
     */
    @Override
    public void onAttach(Activity activity) {

        ((MainActivity) getActivity()).bodyFragment = "WebHomeFragment";
        testLink = getArguments().getString("link");
        		realm = Realm.getInstance(getActivity());
        super.onAttach(activity);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
     * android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.webview_fragment, container, false);

        WebView webView = (WebView) view.findViewById(R.id.webview);

        //WebViewClient webView_ = new WebViewClient();

        final ProgressBar progess = (ProgressBar) view
                .findViewById(R.id.ProgressBar);
        webView.setWebViewClient(new WebHomeClient(progess));
        //webView.clearCache(true);
        WebSettings settings = webView.getSettings();

        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);


        settings.setPluginState(WebSettings.PluginState.ON);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
//		settings.setDefaultZoom(ZoomDensity.FAR);
        settings.setJavaScriptCanOpenWindowsAutomatically(false);
//		webView.onScrollChanged(webView.getScrollX(), webView.getScrollY(), webView.getScrollX(), webView.getScrollY());

        settings.setUserAgentString("Mozilla/5.0 (Windows; U; MSIE 9.0; Windows NT 9.0; en-US)");

        webView.invokeZoomPicker();

		/*Map<String, String> headers = new HashMap<String, String>();
		String fr = "fr_FR";
		String us = "en-US";
		String lang = wmbPreference.getString(Utils.LANG, "fr");
		if (lang.equalsIgnoreCase("fr")) {
//			accepte_lang = fr;
			headers.put("Accept-Language", fr );
		}else {
//			accepte_lang = us;
			headers.put("Accept-Language", us );
		}*/

//		headers.put("Accept-Language", accepte_lang );

        /*if(getString(R.string.application_id).equals("1904")*//* || getString(R.string.application_id).equals("1872") || getString(R.string.application_id).equals("699")*//*){

            settings.setSupportMultipleWindows(true);
            webView.setWebChromeClient(new WebChromeClient(){
                @Override
                public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
                    WebView newWebView = new WebView(view.getContext());
                    newWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
                    newWebView.getSettings().setBuiltInZoomControls(false);
                    newWebView.getSettings().setSupportZoom(false);
                    newWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
                    newWebView.getSettings().setAllowFileAccess(true);
                    newWebView.getSettings().setDomStorageEnabled(true);

                    WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
                    transport.setWebView(newWebView);
                    resultMsg.sendToTarget();
                    return true;
                }
            });
            SharedPreferences wmbPreference = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String lang = wmbPreference.getString(Utils.LANG, "fr");

            if (testLink != null && !testLink.isEmpty()) {
                if (lang.contains("en"))
                    testLink = testLink.replace(".fr.html", ".en.html");
                webView.loadUrl(testLink);
            } else if (lang.contains("fr")) {
                webView.loadUrl("file:///android_asset/webHome/home.fr.html");
            } else {
                webView.loadUrl("file:///android_asset/webHome/home.en.html");
            }
        }else {*/

            webView.setWebChromeClient(new WebChromeClient());

            SharedPreferences wmbPreference = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String lang = wmbPreference.getString(Utils.LANG, "fr");

        if(Utils.assetExists(getActivity().getResources().getAssets(), "webHome/home.fr.html")){
            if (lang.contains("fr")) {
                webView.loadUrl("file:///android_asset/webHome/home.fr.html");
            } else {
                webView.loadUrl("file:///android_asset/webHome/home.en.html");
            }
        }else if (testLink != null && !testLink.isEmpty()) {
            if (lang.contains("en"))
                testLink = testLink.replace(".fr.html", ".en.html");
            webView.loadUrl(testLink);
        }else{
            if (lang.contains("fr")) {
                webView.loadUrl("file:///android_asset/webHome/home.fr.html");
            } else {
                webView.loadUrl("file:///android_asset/webHome/home.en.html");
            }
        }

            /*if (testLink != null && !testLink.isEmpty()) {
                if (lang.contains("en"))
                    testLink = testLink.replace(".fr.html", ".en.html");
                webView.loadUrl(testLink);
            } else if (lang.contains("fr")) {
                webView.loadUrl("file:///android_asset/webHome/home.fr.html");
            } else {
                webView.loadUrl("file:///android_asset/webHome/home.en.html");
            }*/
        //}

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ((WebView) view.findViewById(R.id.webview)).reload();
        super.onViewCreated(view, savedInstanceState);
    }

    class WebHomeClient extends WebViewClient {

        private ProgressBar progess;

        /**
         *
         */
        public WebHomeClient(ProgressBar progess) {
            this.progess = progess;
        }

        /* (non-Javadoc)
         * @see android.webkit.WebViewClient#onPageFinished(android.webkit.WebView, java.lang.String)
         */
        @Override
        public void onPageFinished(WebView view, String url) {
            progess.setVisibility(View.GONE);
            Log.i("MyWebViewClient", "" + url);
            if(url.contains("section(")){
                String s = url.split("\\(")[1];
                s = s.replaceAll("\\D+","");//s.split("\\)")[0];

                Section sections =realm.where(Section.class).equalTo("id",Integer.parseInt(s)).findFirst();
                 /*appController.getSectionsDao().queryForEq("id", s);//.queryForId(Integer.parseInt(s));*/

                Tab tabs = realm.where(Tab.class).equalTo("section_id",Integer.parseInt(s)).findFirst();
                /*appController.getTabDao().queryForEq("section_id", s);*/
                if(tabs != null  && sections != null )
                    ((MainActivity) getActivity()).openSection(tabs, sections);

                else {
                    ((MainActivity) getActivity()).openSectionFromWebHomeView(sections);
                }
            }else if(url.contains("category(")){
                String s = url.split("\\(")[1];
                s = s.replaceAll("\\D+","");//s.split("\\)")[0];
                Category category = realm.where(Category.class).equalTo("id",Integer.parseInt(s)).findFirst();
                //appController.getCategoryById(Integer.parseInt(s));
                if(category != null)
                ((MainActivity) getActivity()).openCategory(category);
            }/*else if(url.contains("contact(")){
                String s = url.split("\\(")[1];
                s = s.replaceAll("\\D+","");//s.split("\\)")[0];
                Contact contact = realm.where(Contact.class).equalTo("id",Integer.parseInt(s)).findFirst();
                //appController.getCategoryById(Integer.parseInt(s));
                if(contact != null) {
                    ((MainActivity) getActivity()).extras = new Bundle();
                    ((MainActivity) getActivity()).extras.putString("link", contact.getLink());

                    WebViewFragment webFragment1 = new WebViewFragment();
                    webFragment1.setArguments(((MainActivity) getActivity()).extras);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, webFragment1).addToBackStack(null).commit();

                }
            }*/
            else if(url.contains("page(")){
                String s = url.split("\\(")[1];
                s = s.replaceAll("\\D+","");//s.split("\\)")[0];

                RealmResults<Child_pages> pages = realm.where(Child_pages.class).equalTo("id", Integer.parseInt(s)).findAll();
                //appController.getChildPageDao().queryForEq("id", Integer.parseInt(s));
                if(pages != null && pages.size() > 0)
                    ((MainActivity) getActivity()).openChildPage(pages.get(0), false);
            }else if (url.contains(".pdf")) {

                Log.e(" PageFragment <=== url ", " : " + url);

                ((MainActivity) getActivity()).extras = new Bundle();
                ((MainActivity) getActivity()).extras.putString("link", /* URL to GoogleDoc => */"http://docs.google.com/gview?embedded=true&url=" +  /* URL to PDF => */url);
                WebViewFragment webFragment = new WebViewFragment();
                webFragment.setArguments(((MainActivity) getActivity()).extras);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, webFragment).addToBackStack(null).commit();
                                /*QPDFViewerFragment qpdfViewerFragment = QPDFViewerFragment.create(url);
                                //webFragment.setArguments(((MainActivity) getActivity()).extras);
                                getActivity().getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.fragment_container, qpdfViewerFragment).addToBackStack(null).commit();*/


            }
            super.onPageFinished(view, url);
        }

        /* (non-Javadoc)
         * @see android.webkit.WebViewClient#onPageStarted(android.webkit.WebView, java.lang.String, android.graphics.Bitmap)
         */
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            progess.setVisibility(View.VISIBLE);
            Log.i("WebHomeClient", ""+url);
            super.onPageStarted(view, url, favicon);
        }

        /* (non-Javadoc)
         * @see android.webkit.WebViewClient#shouldOverrideUrlLoading(android.webkit.WebView, java.lang.String)
         */
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.i("WebHomeClient", ""+url);
            return super.shouldOverrideUrlLoading(view, url);
        }

    }


}
