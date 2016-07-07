package com.euphor.paperpad.activities.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.euphor.paperpad.InterfaceRealm.CommunElements1;
import com.euphor.paperpad.R;
import com.euphor.paperpad.activities.main.MainActivity;
import com.euphor.paperpad.adapters.CategoriesAdapter;
import com.euphor.paperpad.adapters.MySplitAdapter;
import com.euphor.paperpad.adapters.RSSAdapter;
import com.euphor.paperpad.adapters.RoomServicesAdapter;
import com.euphor.paperpad.Beans.CategoriesMyBox;
import com.euphor.paperpad.Beans.Category;
import com.euphor.paperpad.Beans.Child_pages;
import com.euphor.paperpad.Beans.Contact;
import com.euphor.paperpad.Beans.Parameters;
import com.euphor.paperpad.Beans.Section;
import com.euphor.paperpad.Beans.Tab;
import com.euphor.paperpad.Beans.roomServices.Request;
import com.euphor.paperpad.Beans.roomServices.RoomService;

import com.euphor.paperpad.forcastio.beans.Forecast;
import com.euphor.paperpad.rss.RssServiceDashboard;
import com.euphor.paperpad.utils.Colors;
import com.euphor.paperpad.utils.CommonUtilities;
import com.euphor.paperpad.utils.Constants;
import com.euphor.paperpad.utils.Installation;
import com.euphor.paperpad.utils.Utils;
import com.euphor.paperpad.utils.dashboardParsing.Dashboard;
import com.euphor.paperpad.utils.dashboardParsing.DashboardMain;
import com.euphor.paperpad.utils.dashboardParsing.Feed;
import com.euphor.paperpad.utils.dashboardParsing.Landscape_layout;
import com.euphor.paperpad.utils.dashboardParsing.Portrait_layout;
import com.euphor.paperpad.utils.dashboardParsing.Widget;
import com.euphor.paperpad.utils.musicBox.ActionItem;
import com.euphor.paperpad.utils.musicBox.QuickAction;
import com.euphor.paperpad.utils.musicBox.QuickAction.OnActionItemClickListener;
import com.euphor.paperpad.widgets.ArrowImageView;



import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import io.realm.Realm;
import io.realm.RealmResults;
import nl.matshofman.saxrssreader.RssFeed;

//import com.forecast.io.network.responses.INetworkResponse;
//import com.forecast.io.toolbox.NetworkServiceTask;
//import com.forecast.io.v1.network.services.HourlyForecastService;
//import com.forecast.io.v2.network.services.ForecastService;
//import com.forecast.io.network.responses.NetworkResponse;

public class DashboardFragment extends Fragment implements OnBufferingUpdateListener, OnPreparedListener, OnCompletionListener {

	public static final String LOG_TAG = DashboardFragment.class.getName();
	//private static final String URL = "http://192.168.1.12:8000/dashboard.json";

	public Colors colors;
	public Dashboard dashboard;
	protected int prevOrientation;
	private boolean bottomNav;
	private View view;
	private int deviceHeight;
	private int deviceWidth;
	private ArrayList<CommunElements1> communElements;
	private CategoriesAdapter adapter;
	private ArrayList<RelativeLayout> widgetsViews;
	private MediaPlayer mediaPlayer;
	HashMap<String, Object> musicMap;
	HashMap<String, Object> calendarMap;
	HashMap<String, Object> rssMap;
	HashMap<String, Object> weatherMap;
	private Handler handler;
	int indexTrack;
	protected boolean PLAY;
	protected int seek;
	protected RoomServicesAdapter adapterRoomServ;
	protected ListView listRoomService;
	private int section_id;
	private Timer timerRSS;
	private Timer timer;
    public Realm realm;
	/**
	 * Create a new instance of MyDialogFragment
	 */
	public static DashboardFragment newInstance(Dashboard dashboard) {
		DashboardFragment f = new DashboardFragment();
		f.setDashboard(dashboard);
		return f;
	}


	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle arg0) {

		super.onCreate(arg0);


		setRetainInstance(true);
		handler  = new Handler();

	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		if (getArguments() != null) {

			section_id = getArguments().getInt("Section_id_form");
		}
		super.onActivityCreated(savedInstanceState);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
		//appController = ((MyApplication)getActivity().getApplication()).getAppController();//new AppController(getActivity());
		realm = Realm.getInstance(getActivity());
		colors = ((MainActivity)activity).colors;
        Parameters ParamColor = realm.where(Parameters.class).findFirst();
        if (colors==null) {
            colors = new Colors(ParamColor);
        }
		if (((MainActivity) getActivity()).bodyFragment == null) {
			((MainActivity) getActivity()).bodyFragment = "DashboardFragment";
		}
		//		//Lock orientation
		//		prevOrientation = getActivity().getRequestedOrientation();
		//		if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
		//			getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		//		} else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
		//			getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		//		} else {
		//			getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
		//		}
		//		DownloadParseAsyncTask asyncTask = new DownloadParseAsyncTask();
		//		asyncTask.execute(URL);
		Parameters parameters = null;

        parameters = realm.where(Parameters.class).findFirst();
        //appController.getParametersDao().queryForId(1);

        if (parameters != null) {
			if (parameters.getNavigation_type()!= null) {
				if (parameters.getNavigation_type().contains("bottom")) {
					bottomNav = true;
				}else {
					bottomNav = false;
				}
			}else {
				bottomNav = true;
			}
		}
		//get screen dimensions 
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

		if (bottomNav) {
			deviceHeight = dm.heightPixels - (int)getResources().getDimension(R.dimen.height_tab_fragment_bottom);
			deviceWidth = dm.widthPixels;
		}else {
			deviceHeight = dm.heightPixels;
			deviceWidth = dm.widthPixels - (int)getResources().getDimension(R.dimen.width_tab_fragment);
		}

		super.onAttach(activity);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dashboard_home_grid, container, false);
        //		view.setBackgroundColor(color.android_green);

        //		Drawable shadow =  getResources().getDrawable(R.drawable.shadow_back_widgets);
        Drawable drawableTop = getResources().getDrawable(R.drawable.shape_back_widgets);
        drawableTop.setColorFilter(colors.getColor(colors.getBackground_color(), "88"), Mode.MULTIPLY);
        //		LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{drawableTop, shadow});
        //		layerDrawable.setLayerInset(0, 0, 0, 0, 0);
        //		layerDrawable.setLayerInset(1, 1, 1, 1, 0);
        //end get screen dimensions
        ColorDrawable colorDrawable2 = new ColorDrawable(colors.getColor(colors.getForeground_color(), "5"));
        colorDrawable2.setColorFilter(colors.getColor(colors.getBackground_color()), Mode.OVERLAY);
        widgetsViews = new ArrayList<RelativeLayout>();
        RelativeLayout rlContainer = new RelativeLayout(getActivity());
        RelativeLayout.LayoutParams pms;
        Dashboard dashboard = realm.where(Dashboard.class).findFirst();
        if(dashboard !=null)
        {

        int innerMargin = dashboard.getInner_padding();
        int outerMargin = dashboard.getOuter_margin();
        for (int i = 0; i < dashboard.getWidgets().size(); i++) {
            final Widget widget = dashboard.getWidgets().get(i);

            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

                Landscape_layout land_layout = widget.getLandscape_layout();
                int relW = land_layout.getWidth();
                int relH = land_layout.getHeight();
                int widgetWidth = (int) ((float) relW * (((float) deviceWidth / (float) 40)));
                int widgetHeight = (int) ((float) relH * (((float) deviceHeight / (float) 30)));
                pms = new LayoutParams(widgetWidth, widgetHeight);
                pms.leftMargin = (int) (((float) land_layout.getX()) * ((float) deviceWidth / (float) 40));
                pms.topMargin = (int) (((float) land_layout.getY()) * (((float) deviceHeight / (float) 30)));

                RelativeLayout widgetContainer = new RelativeLayout(getActivity());

                widgetsViews.add(widgetContainer);
                //				widgetContainer.setBackgroundColor(Color.parseColor("#FFDEF7"));
                LinearLayout intermediare = new LinearLayout(getActivity());
                intermediare.addView(widgetContainer, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                intermediare.setLayoutParams(pms);
                int realInnerMargin = (int) ((float) innerMargin / (float) 2);
                intermediare.setPadding(realInnerMargin, realInnerMargin, realInnerMargin, realInnerMargin);
                widgetContainer.setBackgroundColor(colors.getColor(colors.getTitle_color(), "10"));//widgetContainer.setBackgroundColor(colors.getColor(colors.getBackground_color(), "ff"));
                rlContainer.addView(intermediare);
                View widgetLayout = inflater.inflate(R.layout.widget_layout, null, false);
                final RelativeLayout widgetContent = (RelativeLayout) widgetLayout.findViewById(R.id.contentWidget);
                widgetContent.setBackgroundColor(colors.getColor(colors.getBackground_color()));
                widgetContainer.addView(widgetLayout, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                final LinearLayout titleProductHolder_widget = (LinearLayout) widgetLayout.findViewById(R.id.titleProductHolder_widget);
                TextView titleTV_widget = (TextView) widgetLayout.findViewById(R.id.titleTV_widget);
                titleTV_widget.setText(widget.getTitle());
                titleTV_widget.setTypeface(MainActivity.FONT_TITLE);
                titleProductHolder_widget.setBackgroundDrawable(colorDrawable2);
                titleTV_widget.setTextColor(colors.getColor(colors.getTitle_color()));
                RelativeLayout.LayoutParams params = new LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                params.addRule(RelativeLayout.CENTER_IN_PARENT);

                drawWidgets(inflater, i, widget, widgetWidth, widgetContainer,
                        widgetContent, titleProductHolder_widget, params);

            } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                Portrait_layout port_layout = widget.getPortrait_layout();
                int relW = port_layout.getWidth();
                int relH = port_layout.getHeight();
                int widgetWidth = (int) ((float) relW * (((float) deviceWidth / (float) 30)));
                int widgetHeight = (int) ((float) relH * (((float) deviceHeight / (float) 40)));
                pms = new LayoutParams(widgetWidth, widgetHeight);
                pms.leftMargin = (int) (((float) port_layout.getX()) * ((float) deviceWidth / (float) 30));
                pms.topMargin = (int) (((float) port_layout.getY()) * (((float) deviceHeight / (float) 40)));

                RelativeLayout widgetContainer = new RelativeLayout(getActivity());
                widgetsViews.add(widgetContainer);
                //				widgetContainer.setBackgroundColor(Color.parseColor("#FFDEF7"));
                LinearLayout intermediare = new LinearLayout(getActivity());
                intermediare.addView(widgetContainer, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                intermediare.setLayoutParams(pms);
                int realInnerMargin = (int) ((float) innerMargin / (float) 2);
                intermediare.setPadding(realInnerMargin, realInnerMargin, realInnerMargin, realInnerMargin);
                widgetContainer.setBackgroundColor(colors.getColor(colors.getTitle_color(), "10"));//widgetContainer.setBackgroundColor(colors.getColor(colors.getBackground_color(), "ff"));
                rlContainer.addView(intermediare);
                View widgetLayout = inflater.inflate(R.layout.widget_layout, null, false);
                final RelativeLayout widgetContent = (RelativeLayout) widgetLayout.findViewById(R.id.contentWidget);
                widgetContent.setBackgroundColor(colors.getColor(colors.getBackground_color(), "ff"));
                widgetContainer.addView(widgetLayout, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                final LinearLayout titleProductHolder_widget = (LinearLayout) widgetLayout.findViewById(R.id.titleProductHolder_widget);
                TextView titleTV_widget = (TextView) widgetLayout.findViewById(R.id.titleTV_widget);
                titleTV_widget.setText(widget.getTitle());
                titleTV_widget.setTypeface(MainActivity.FONT_TITLE);
                titleTV_widget.setTextColor(colors.getColor(colors.getTitle_color()));
                RelativeLayout.LayoutParams params = new LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                params.addRule(RelativeLayout.CENTER_IN_PARENT);
                titleProductHolder_widget.setBackgroundDrawable(colorDrawable2);

                drawWidgets(inflater, i, widget, widgetWidth, widgetContainer,
                        widgetContent, titleProductHolder_widget, params);
            }
        }

        LinearLayout tilesContainer = (LinearLayout) view.findViewById(R.id.tilesContainer);
        ColorDrawable colorDrawable = new ColorDrawable(colors.getColor(colors.getForeground_color(), "20"));
        colorDrawable.setColorFilter(colors.getColor(colors.getBackground_color()), Mode.OVERLAY);
        tilesContainer.setBackgroundDrawable(colorDrawable);/*Color(Color.parseColor("#E0CECC")colors.getColor(colors.getBackground_color()));*/
        tilesContainer.setPadding(outerMargin, outerMargin, outerMargin, outerMargin);
        LinearLayout.LayoutParams paramsRLContainer = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        tilesContainer.addView(rlContainer, paramsRLContainer);


    }
		//		drawLayouts(deviceHeight, deviceWidth, inflater);
		return view;
	}

	/**
	 * @param inflater
	 * @param i
	 * @param widget
	 * @param widgetWidth
	 * @param widgetContainer
	 * @param widgetContent
	 * @param titleProductHolder_widget
	 * @param params
	 */
	public void drawWidgets(LayoutInflater inflater, int i,
			final Widget widget, int widgetWidth,
			RelativeLayout widgetContainer, final RelativeLayout widgetContent,
			final LinearLayout titleProductHolder_widget,
			RelativeLayout.LayoutParams params) {
		if (widget.getType().equalsIgnoreCase("weather")) {

			ProgressBar bar = new ProgressBar(getActivity());
			widgetContent.addView(bar, params );
			titleProductHolder_widget.setVisibility(View.GONE);
			View widgetMeteo = inflater.inflate(R.layout.widget_weather, null, false);
			widgetMeteo.setVisibility(View.INVISIBLE);
			ImageView todayIcon = (ImageView)widgetMeteo.findViewById(R.id.currentTemp_img);
			TextView todayDegree = (TextView)widgetMeteo.findViewById(R.id.currentTemp_txt);
			TextView todayDegreeMin = (TextView)widgetMeteo.findViewById(R.id.forcast_min_txt);
			TextView todayDegreeMax = (TextView)widgetMeteo.findViewById(R.id.forcast_max_txt);
			TextView day1Degree = (TextView)widgetMeteo.findViewById(R.id.forcast1_txt);
			TextView day2Degree = (TextView)widgetMeteo.findViewById(R.id.forcast2_txt);
			TextView day3Degree = (TextView)widgetMeteo.findViewById(R.id.forcast3_txt);
			TextView day4Degree = (TextView)widgetMeteo.findViewById(R.id.forcast4_txt);
			TextView day5Degree = (TextView)widgetMeteo.findViewById(R.id.forcast5_txt);
			ImageView day1Icon = (ImageView)widgetMeteo.findViewById(R.id.forcast1_img);
			ImageView day2Icon = (ImageView)widgetMeteo.findViewById(R.id.forcast2_img);
			ImageView day3Icon = (ImageView)widgetMeteo.findViewById(R.id.forcast3_img);
			ImageView day4Icon = (ImageView)widgetMeteo.findViewById(R.id.forcast4_img);
			ImageView day5Icon = (ImageView)widgetMeteo.findViewById(R.id.forcast5_img);
			TextView day1 = (TextView)widgetMeteo.findViewById(R.id.forcast1_txtDay);
			TextView day2 = (TextView)widgetMeteo.findViewById(R.id.forcast2_txtDay);
			TextView day3 = (TextView)widgetMeteo.findViewById(R.id.forcast3_txtDay);
			TextView day4 = (TextView)widgetMeteo.findViewById(R.id.forcast4_txtDay);
			TextView day5 = (TextView)widgetMeteo.findViewById(R.id.forcast5_txtDay);

			weatherMap = new HashMap<String, Object>();
			weatherMap.put("widgetMeteo", widgetMeteo);
			weatherMap.put("todayIcon", todayIcon);
			weatherMap.put("todayDegree", todayDegree);
			weatherMap.put("todayDegreeMin", todayDegreeMin);
			weatherMap.put("todayDegreeMax", todayDegreeMax);
			weatherMap.put("day1Degree", day1Degree);
			weatherMap.put("day2Degree", day2Degree);
			weatherMap.put("day3Degree", day3Degree);
			weatherMap.put("day4Degree", day4Degree);
			weatherMap.put("day5Degree", day5Degree);
			weatherMap.put("day1Icon", day1Icon);
			weatherMap.put("day2Icon", day2Icon);
			weatherMap.put("day3Icon", day3Icon);
			weatherMap.put("day4Icon", day4Icon);
			weatherMap.put("day5Icon", day5Icon);
			weatherMap.put("day1", day1);
			weatherMap.put("day2", day2);
			weatherMap.put("day3", day3);
			weatherMap.put("day4", day4);
			weatherMap.put("day5", day5);


			executeWeatherTimedThread(widget, bar);
			widgetContent.addView(widgetMeteo, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

		}else if (widget.getType().equalsIgnoreCase("rss")) {
            realm.beginTransaction();

			ProgressBar bar = new ProgressBar(getActivity());
			widgetContainer.addView(bar, params );
			titleProductHolder_widget.setVisibility(View.GONE);
			View widgetRss = inflater.inflate(R.layout.rss_list, null, false);
			ListView lvRss = (ListView)widgetRss.findViewById(R.id.ListRSS);
			LinearLayout feedChoicesHolder = (LinearLayout)widgetRss.findViewById(R.id.feedChoicesHolder);
//			ColorDrawable colorDrawable2 = new ColorDrawable(colors.getColor(colors.getForeground_color(), "19"));
//			colorDrawable2.setColorFilter(colors.getColor(colors.getBackground_color()), Mode.OVERLAY);
//			widgetRss.findViewById(R.id.llSVFeedChoicesHolder).setBackgroundColor(colorDrawable2.getColor());
			widgetRss.findViewById(R.id.llSVFeedChoicesHolder).setBackgroundColor(colors.getColor(colors.getTitle_color(), "30"));
			RSSAdapter adapter = new RSSAdapter(getActivity(), new RssFeed(), colors, R.layout.widget_rss_list_item);
			final RssServiceDashboard serviceDashboard = new RssServiceDashboard(this, lvRss, adapter, bar);
			serviceDashboard.execute(widget.getContent().getFeeds().get(0).getRss_path());

			rssMap = new HashMap<String, Object>();
			rssMap.put("list", lvRss);
			rssMap.put("adapter", adapter);
			rssMap.put("widget", widget);
			rssMap.put("bar", bar);
			rssMap.put("feedChoicesHolder", feedChoicesHolder);
			rssMap.put("feeds", widget.getContent().getFeeds());
			rssMap.put("timer", timerRSS);
			rssMap.put("feed", widget.getContent().getFeeds().get(0));
			widget.getContent().getFeeds().get(0).setSelected(true);
			for (int j = 0; j < widget.getContent().getFeeds().size(); j++) {
				fillNavigationBar(widget.getContent().getFeeds().get(j));
			}
			timerRSS = new Timer();
			timerRSS = executeRSSTimedThread(rssMap); 
			//			ViewTreeObserver vto = feedChoicesHolder.getViewTreeObserver();
			//			vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			//			    private int maxLines = -1;
			//			    @Override
			//			    public void onGlobalLayout() {
			//			    	
			//			    }
			//			});
			//			serviceDashboard.executeOnExecutor(exec , widget.getContent().getRss_path());
			widgetContent.addView(widgetRss, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            realm.commitTransaction();

		}else if (widget.getType().equalsIgnoreCase("link")) {
			//			ProgressBar bar = new ProgressBar(getActivity());
			//			widgetContent.addView(bar, params );
			View linkWidget = inflater.inflate(R.layout.link_widget, null, false);
			ArrowImageView arrow_widget_link = (ArrowImageView)linkWidget.findViewById(R.id.arrow_widget_link);
			Paint paint = new Paint();
			paint.setColor(colors.getColor(colors.getTitle_color()));
			arrow_widget_link.setPaint(paint );
			final TextView text = (TextView)linkWidget.findViewById(R.id.link_widget_tv);
			String txt =widget.getContent().getText();
			//					txt = txt.concat("\n");
			text.setText(txt);
			text.setTypeface(MainActivity.FONT_BODY);
			ViewTreeObserver vto = text.getViewTreeObserver();
			vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
				private int maxLines = -1;
				@Override
				public void onGlobalLayout() {
					if (maxLines < 0 && text.getHeight() > 0 && text.getLineHeight() > 0) {
						int height = text.getHeight();
						int lineHeight = text.getLineHeight();
						maxLines = height / lineHeight;
						text.setMaxLines(maxLines);
					}
				}
			});
			final Category category = realm.where(Category.class).equalTo("id",widget.getContent().getCategory_id()).findFirst();
			//appController.getCategoryById(widget.getContent().getCategory_id());
			widgetContent.addView(linkWidget, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
			widgetContent.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					((MainActivity)getActivity()).openCategory(category);
				}
			} );


		}else if (widget.getType().equalsIgnoreCase("calendar")) {
			//					ProgressBar bar = new ProgressBar(getActivity());
			//					widgetContainer.addView(bar, params );
			final LayoutInflater inf = inflater;

			calendarMap = new HashMap<String, Object>();
			titleProductHolder_widget.setVisibility(View.GONE);
			View calendar_widget = inf.inflate(R.layout.calendar_widget, null, false);
			
			
			TextView hourTxt = (TextView)calendar_widget.findViewById(R.id.textView4);
			final TextView txt_points = (TextView)calendar_widget.findViewById(R.id.txt_points);
			TextView txt_minutes = (TextView)calendar_widget.findViewById(R.id.txt_minutes);
			
			
			Calendar c = Calendar.getInstance(TimeZone.getTimeZone(widget.getContent().getTimezone_code()), Locale.FRANCE);
			int Hr24=c.get(Calendar.HOUR_OF_DAY);
			int Min=c.get(Calendar.MINUTE);
			hourTxt.setText(Hr24+"");
			hourTxt.setTypeface(MainActivity.FONT_BODY);
			txt_minutes.setText(Min+"");
			txt_minutes.setTypeface(MainActivity.FONT_BODY);
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.FRANCE);
			dateFormat.setTimeZone(TimeZone.getTimeZone(widget.getContent().getTimezone_code())); 
//			timeTv.setText(dateFormat.format(new Date()));
//			timeTv.setTypeface(MainActivity.FONT_BODY);
			TextView dayInMonth = (TextView)calendar_widget.findViewById(R.id.textView2);
			dateFormat = new SimpleDateFormat("dd", Locale.FRANCE);
			dateFormat.setTimeZone(TimeZone.getTimeZone(widget.getContent().getTimezone_code())); 
			dayInMonth.setText(dateFormat.format(new Date()));
			dayInMonth.setTypeface(MainActivity.FONT_BODY);

			TextView dayName = (TextView)calendar_widget.findViewById(R.id.textView1);
			dateFormat = new SimpleDateFormat("EEEE", Locale.FRANCE);
			dateFormat.setTimeZone(TimeZone.getTimeZone(widget.getContent().getTimezone_code())); 
			dayName.setText(dateFormat.format(new Date()));
			dayName.setTypeface(MainActivity.FONT_BODY);

			TextView month = (TextView)calendar_widget.findViewById(R.id.textView3);
			dateFormat = new SimpleDateFormat("MMMM" ,Locale.FRANCE);
			dateFormat.setTimeZone(TimeZone.getTimeZone(widget.getContent().getTimezone_code())); 
			month.setText(dateFormat.format(new Date()));
			month.setTypeface(MainActivity.FONT_BODY);
			widgetContent.addView(calendar_widget, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
			calendarMap.put("time", hourTxt);
			calendarMap.put("time_min", txt_minutes);
			calendarMap.put("point", txt_points);
			calendarMap.put("month", month);
			calendarMap.put("day", dayName);
			calendarMap.put("dayNumber", dayInMonth);
			hourTxt.setTextColor(colors.getColor(colors.getTitle_color()));
			txt_minutes.setTextColor(colors.getColor(colors.getTitle_color()));
			month.setTextColor(colors.getColor(colors.getTitle_color()));
			dayName.setTextColor(colors.getColor(colors.getTitle_color()));
			dayInMonth.setTextColor(colors.getColor(colors.getTitle_color()));
			
			
			/*Timer timer = new Timer();
			timer.scheduleAtFixedRate(new TimerTask() {
				boolean round = false;
				@Override
				public void run() {
					TextView txt_points = (TextView)calendarMap.get("point");
					if (round) {
						txt_points.setVisibility(View.VISIBLE);
						round = false;
					}else {
						txt_points.setVisibility(View.GONE);
						round = true;
					}
					
					
				}
			}, 0, 1000);*/

		
			
			ColorDrawable colorDraw = new ColorDrawable();
			colorDraw.setColorFilter(colors.getColor(colors.getForeground_color(), "FF"), Mode.LIGHTEN);
			calendar_widget.findViewById(R.id.date_widget).setBackgroundColor(colors.getColor(colors.getForeground_color(), "25"));
			
			executeTimedThread(calendarMap, widget.getContent().getTimezone_code());

		}else if (widget.getType().equalsIgnoreCase("room_service")) {

			ProgressBar bar = new ProgressBar(getActivity());
			widgetContent.addView(bar, params );
			//					DownloadRoomService roomService = new DownloadRoomService();
			View widgetRoom = inflater.inflate(R.layout.widget_room_service, null, false);
			//					String roomServiceUrl = Constants.BASE_URL+"/api/application/requests/id/"+Installation.id(getActivity())+"/lang/fr";
			listRoomService = (ListView)widgetRoom.findViewById(android.R.id.list);

//			LinearLayout view = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.divided_screen_item, null, false);//new RelativeLayout(getActivity());
//			//view.setLayoutParams(new LinearLayout.LayoutParams(250, 150));
//			view.setBackgroundColor(Color.BLUE);
//			TextView txt = (TextView) view.findViewById(R.id.TVTitleCategory); //new TextView(getActivity());
//			view.findViewById(R.id.imgCategory).setVisibility(View.GONE);
//			//txt.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
//			txt.setText(" Vous n'avez fait aucune demande.");
//			//txt.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
//			txt.setTextSize(24);
//			txt.setTextColor(colors.getColor(colors.getTitle_color()));
//			view.addView(txt, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
//			widgetContent.addView(view, params );


			listRoomService.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					com.euphor.paperpad.utils.roomServiceBox.QuickAction quickAction = new com.euphor.paperpad.utils.roomServiceBox.QuickAction(getActivity(), 
							com.euphor.paperpad.utils.roomServiceBox.QuickAction.VERTICAL, colors);
					if (adapterRoomServ != null && adapterRoomServ.getItem(position) != null) {
						Request request = adapterRoomServ.getItem(position);
						for (int j = 0; j < request.getProducts().size(); j++) {
		com.euphor.paperpad.utils.roomServiceBox.ActionItem item = new com.euphor.paperpad.utils.roomServiceBox.ActionItem("",
									j, request.getProducts().get(j));
							quickAction.addActionItem(item);
						}
					}
					quickAction.show(arg1);

				}
			});
			executeRoomTimedThread(bar);
			//					roomService.execute(roomServiceUrl/*"http://backoffice.paperpad.fr/api///application/requests/id/f8cd189c-59a2-417c-924e-b45443c4bc16//lang/fr"*/);
			widgetContent.addView(widgetRoom, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

		}else if (widget.getType().equalsIgnoreCase("category")) {
			addListCategory(inflater, widgetContent, widget);


		}else if (widget.getType().equalsIgnoreCase("playlist")) {
			//			ProgressBar bar = new ProgressBar(getActivity());
			//			widgetContent.addView(bar, params );
			titleProductHolder_widget.setVisibility(View.GONE);

			final int sizePlaylist = widget.getContent().getPlaylist().size();
			musicMap = new HashMap<String, Object>();
			musicMap.put("widget", widget);
			//			musicMap.put("bar", bar);
			View musicWidget;
			if (widgetWidth > 400) {
				musicWidget = inflater.inflate(R.layout.music_widget_horizontal, null, false);
			}else {
				musicWidget = inflater.inflate(R.layout.music_widget_vertical, null, false);
			}
			//					View musicWidget = inflater.inflate(R.layout.music_widget, null, false);
			widgetContent.addView(musicWidget, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
			final ImageView play_pause = (ImageView)musicWidget.findViewById(R.id.playImag);
			ImageView prev = (ImageView)musicWidget.findViewById(R.id.prevImag);
			ImageView next = (ImageView)musicWidget.findViewById(R.id.nextImag);
			ImageView listTracks = (ImageView)musicWidget.findViewById(R.id.imageView1);
			final ImageView cover = (ImageView)musicWidget.findViewById(R.id.coverImag);
			Glide.with(getActivity()).load(widget.getContent().getPlaylist().get(0).getCover()).into(cover);
			final TextView songTitle = (TextView)musicWidget.findViewById(R.id.songTitle);
			songTitle.setText(widget.getContent().getPlaylist().get(0).getSong());
			songTitle.setTypeface(MainActivity.FONT_BODY);
			songTitle.setTextColor(colors.getColor(colors.getTitle_color()));
			final TextView songArtist = (TextView)musicWidget.findViewById(R.id.songArtist);
			songArtist.setText(widget.getContent().getPlaylist().get(0).getArtist());
			songArtist.setTextColor(colors.getColor(colors.getTitle_color()));
			songArtist.setTypeface(MainActivity.FONT_BODY);
			play_pause.getDrawable().setColorFilter(colors.getColor(colors.getTitle_color()), Mode.MULTIPLY);
			prev.getDrawable().setColorFilter(colors.getColor(colors.getTitle_color()), Mode.MULTIPLY);
			next.getDrawable().setColorFilter(colors.getColor(colors.getTitle_color()), Mode.MULTIPLY);
			listTracks.getDrawable().setColorFilter(colors.getColor(colors.getTitle_color()), Mode.MULTIPLY);
			listTracks.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					QuickAction quickAction= new QuickAction(getActivity(), QuickAction.VERTICAL, colors);
					for (int j = 0; j < widget.getContent().getPlaylist().size(); j++) {
						ActionItem item = new ActionItem("", j, widget.getContent().getPlaylist().get(j));
						quickAction.addActionItem(item);
					}
					quickAction
					.setOnActionItemClickListener(new OnActionItemClickListener() {

						@Override
						public void onItemClick(QuickAction source, int pos, int actionId) {
							//							PLAY = true;
							indexTrack = actionId;
							Glide.with(getActivity()).load(widget.getContent().getPlaylist().get(indexTrack).getCover()).into(cover);
							songTitle.setText(widget.getContent().getPlaylist().get(indexTrack).getSong());
							songArtist.setText(widget.getContent().getPlaylist().get(indexTrack).getArtist());
							seek = 0;
							playMp3();

						}
					});
					quickAction.show(v);

				}
			});
			PLAY = false;
			play_pause.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (PLAY) {
						PLAY = false;
					}else {
						PLAY = true;
					}
					if (PLAY) {
						//								mediaPlayer.seekTo(seek);
						//						playMp3();
						if (mediaPlayer != null) {
							mediaPlayer.start();
						}
						play_pause.setImageDrawable(getResources().getDrawable(R.drawable.pause_sound));
						play_pause.getDrawable().setColorFilter(colors.getColor(colors.getTitle_color()), Mode.MULTIPLY);
					}else {
						if (mediaPlayer != null){
							mediaPlayer.pause();
							seek = mediaPlayer.getCurrentPosition();
						}

						play_pause.setImageDrawable(getResources().getDrawable(R.drawable.play_sound));
						play_pause.getDrawable().setColorFilter(colors.getColor(colors.getTitle_color()), Mode.MULTIPLY);
					}

				}
			});
			prev.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (indexTrack<= 0) {
						indexTrack  = sizePlaylist-1;
					}else {
						indexTrack--;
					}
					Glide.with(getActivity()).load(widget.getContent().getPlaylist().get(indexTrack).getCover()).into(cover);
					songTitle.setText(widget.getContent().getPlaylist().get(indexTrack).getSong());
					songArtist.setText(widget.getContent().getPlaylist().get(indexTrack).getArtist());
					seek = 0;
					playMp3();
					//					play_pause.setImageDrawable(getResources().getDrawable(R.drawable.pause_sound));
					//					play_pause.getDrawable().setColorFilter(colors.getColor(colors.getTitle_color()), Mode.MULTIPLY);
				}
			});
			next.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (indexTrack >= sizePlaylist-1) {
						indexTrack  = 0;
					}else {
						indexTrack++;
					}
					Glide.with(getActivity()).load(widget.getContent().getPlaylist().get(indexTrack).getCover()).into(cover);
					songTitle.setText(widget.getContent().getPlaylist().get(indexTrack).getSong());
					songArtist.setText(widget.getContent().getPlaylist().get(indexTrack).getArtist());
					seek = 0;
					playMp3();
					//					play_pause.setImageDrawable(getResources().getDrawable(R.drawable.pause_sound));
					//					play_pause.getDrawable().setColorFilter(colors.getColor(colors.getTitle_color()), Mode.MULTIPLY);
				}
			});
			mediaPlayer = new MediaPlayer();
			playMp3();

			mediaPlayer.pause();
		}else if (widget.getType().equalsIgnoreCase("section_link")) {
			View linkWidget = inflater.inflate(R.layout.link_widget, null, false);
			ArrowImageView arrow_widget_link = (ArrowImageView)linkWidget.findViewById(R.id.arrow_widget_link);
			Paint paint = new Paint();
			paint.setColor(colors.getColor(colors.getTitle_color()));
			arrow_widget_link.setPaint(paint );
			final TextView text = (TextView)linkWidget.findViewById(R.id.link_widget_tv);
			String txt =widget.getContent().getText();
			//					txt = txt.concat("\n");
			text.setText(txt);
			text.setTypeface(MainActivity.FONT_BODY);
			ViewTreeObserver vto = text.getViewTreeObserver();
			vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
				private int maxLines = -1;
				@Override
				public void onGlobalLayout() {
					if (maxLines < 0 && text.getHeight() > 0 && text.getLineHeight() > 0) {
						int height = text.getHeight();
						int lineHeight = text.getLineHeight();
						maxLines = height / lineHeight;
						text.setMaxLines(maxLines);
					}
				}
			});

			widgetContent.addView(linkWidget, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
			widgetContent.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					RealmResults<Section>  list = realm.where(Section.class).equalTo("id", widget.getContent().getSection_id()).findAll();
                    //appController.getSectionsDao().queryForEq("id", widget.getContent().getSection_id());
                    Section section = null;
					if (list.size()>0) {
						section = list.get(0);
						Tab tab = new Tab();
						tab.setIsHomeGrid(false);
						((MainActivity)getActivity()).openSection(tab , section);
					}

				}
			} );


		}else if (widget.getType().equalsIgnoreCase("contact_form")) {
			View linkWidget = inflater.inflate(R.layout.link_widget, null, false);
			ArrowImageView arrow_widget_link = (ArrowImageView)linkWidget.findViewById(R.id.arrow_widget_link);
			Paint paint = new Paint();
			paint.setColor(colors.getColor(colors.getTitle_color()));
			arrow_widget_link.setPaint(paint );
			final TextView text = (TextView)linkWidget.findViewById(R.id.link_widget_tv);
			String txt =widget.getContent().getText();
			//					txt = txt.concat("\n");
			text.setText(txt);
			text.setTypeface(MainActivity.FONT_BODY);
			ViewTreeObserver vto = text.getViewTreeObserver();
			vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
				private int maxLines = -1;
				@Override
				public void onGlobalLayout() {
					if (maxLines < 0 && text.getHeight() > 0 && text.getLineHeight() > 0) {
						int height = text.getHeight();
						int lineHeight = text.getLineHeight();
						maxLines = height / lineHeight;
						text.setMaxLines(maxLines);
					}
				}
			});
			widgetContent.addView(linkWidget, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
			widgetContent.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					FormContactFragment formContactFragment = new FormContactFragment(); //.newInstance();
					((MainActivity)getActivity()).bodyFragment = "FormContactFragment";
					int contact_id = widget.getContent().getContact_form_id();
                    RealmResults<Contact>  contact = realm.where(Contact.class).equalTo("id", contact_id).findAll();
                    //= appController.getContactDao().queryForEq("id", contact_id);
                    if(contact.size()>0)
                        contact_id = contact.get(0).getId_con();
                    //					Log.e(" DashboardFragment <=== contact_id "," : "+contact_id+"   widget.getContent().getContact_form_id() : "+widget.getContent().getContact_form_id());

					// In case this activity was started with special instructions from an Intent,
					// pass the Intent's extras to the fragment as arguments
					((MainActivity)getActivity()).extras = new Bundle();
					((MainActivity)getActivity()).extras.putInt("Section_id_form", section_id);
					//					((MainActivity)getActivity()).extras.putInt("Contact", widget.getContent().getContact_form_id()); /** is a problem**/
					/** Uness Modif **/
					((MainActivity) getActivity()).extras.putInt("Contact",contact_id );
					((MainActivity) getActivity()).extras.putBoolean("newDesign",true);

					formContactFragment.setArguments(((MainActivity)getActivity()).extras);
					// Add the fragment to the 'fragment_container' FrameLayout
					getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, formContactFragment).addToBackStack(null).commit();

				}
			} );
		}
	}

	private Timer executeRSSTimedThread(final HashMap<String, Object> rssMap) {
		Timer timer = new Timer();
		timer.schedule(new TimerTask()
		{
			@Override
			public void run()
			{
				handler.post(new Runnable() {

					@Override
					public void run() {
						ListView lvRss = (ListView)rssMap.get("list");
						RSSAdapter adapter = (RSSAdapter)rssMap.get("adapter");
						ProgressBar bar = (ProgressBar)rssMap.get("bar");
						Widget widget = (Widget)rssMap.get("widget");
						Feed feed = (Feed) rssMap.get("feed");
						RssServiceDashboard serviceDashboard = new RssServiceDashboard(DashboardFragment.this, lvRss , adapter, bar );
						serviceDashboard.execute(feed.getRss_path());

					}
				});

			}
		}, 0, 10*60*1000);
		return timer;

	}

	private void executeRoomTimedThread(final ProgressBar bar) {
		//Timer 
		timer = new Timer();
		timer.schedule(new TimerTask()
		{
			@Override
			public void run()
			{
				handler.post(new Runnable() {

					@Override
					public void run() {
						DownloadRoomService roomService = new DownloadRoomService(bar);
						String roomServiceUrl = Constants.BASE_URL+"/api/application/requests/id/"+Installation.id(getActivity())+"/lang/fr";
						roomService.execute(roomServiceUrl); /*"http://backoffice.paperpad.fr/api///application/requests/id/f8cd189c-59a2-417c-924e-b45443c4bc16//lang/fr"*/

					}
				});

			}
		}, 0, 10*1000);

	}


	private void executeWeatherTimedThread(final Widget widget, final ProgressBar bar) {
		Timer timer = new Timer();
		timer.schedule(new TimerTask()
		{
			@Override
			public void run()
			{
				handler.post(new Runnable() {

					@Override
					public void run() {
						String url_weather = "https://api.forecast.io/forecast/3a6dc0ee17bd5c9575b79e598fa6a7bc/"
								+widget.getContent().getWeather_location().getLatitude()+","+widget.getContent().getWeather_location().getLongitude()+"?units=si";
						DownloadMeteo downloadMeteo = new DownloadMeteo(bar);
						downloadMeteo.execute(url_weather);

					}
				});

			}
		}, 0, 20*60*1000);


	}


	private void executeTimedThread(final HashMap<String, Object> map, final String timezone) {	
		Timer timer = new Timer();
		
		
		timer.schedule(new TimerTask()
		{
			boolean round = false;
			
			@Override
			public void run()
			{
				handler.post(new Runnable() {

					@Override
					public void run() {
						TextView txt_points = (TextView)map.get("point");
						if (round) {
							txt_points.setVisibility(View.VISIBLE);
							round = false;
						}else {
							txt_points.setVisibility(View.INVISIBLE);
							round = true;
						}
						
						TextView hourTxt = (TextView)map.get("time");
						TextView txt_minutes = (TextView)map.get("time_min");
						Calendar c = Calendar.getInstance(TimeZone.getTimeZone(timezone), Locale.FRANCE);
						int Hr24=c.get(Calendar.HOUR_OF_DAY);
						int Min=c.get(Calendar.MINUTE);
						hourTxt.setText(Hr24+"");
						hourTxt.setTypeface(MainActivity.FONT_BODY);
						txt_minutes.setText(Min+"");
						txt_minutes.setTypeface(MainActivity.FONT_BODY);
						
						
//						SimpleDateFormat dateFormat = new SimpleDateFormat("KK:mm", Locale.FRANCE);
//						dateFormat.setTimeZone(TimeZone.getTimeZone(timezone)); 
//						timeTv.setText(dateFormat.format(new Date()));
						TextView dayInMonth = (TextView)map.get("dayNumber");
						SimpleDateFormat dateFormat = new SimpleDateFormat("dd", Locale.FRANCE);
						dateFormat.setTimeZone(TimeZone.getTimeZone(timezone)); 
						dayInMonth.setText(dateFormat.format(new Date()));

						TextView dayName = (TextView)map.get("day");
						dateFormat = new SimpleDateFormat("EEEE", Locale.FRANCE);
						dateFormat.setTimeZone(TimeZone.getTimeZone(timezone)); 
						dayName.setText(dateFormat.format(new Date()));

						TextView month = (TextView)map.get("month");
						dateFormat = new SimpleDateFormat("MMMM" ,Locale.FRANCE);
						dateFormat.setTimeZone(TimeZone.getTimeZone(timezone)); 
						month.setText(dateFormat.format(new Date()));

					}
				});

			}
		}, 0, 1000);

	}


	public void playMp3(){

		Widget widget = (Widget)musicMap.get("widget");
		//		ProgressBar progressbar = (ProgressBar) musicMap.get("bar");
		if (indexTrack >= widget.getContent().getPlaylist().size()) {
			indexTrack=0;
		}

		String _link = widget.getContent().getPlaylist().get(indexTrack).getMp3();

		mediaPlayer.reset();
		//        progressbar.setVisibility(View.VISIBLE);
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);


		try {
			mediaPlayer.setDataSource(_link);
			mediaPlayer.setOnBufferingUpdateListener(this);
			mediaPlayer.setOnPreparedListener(this);
			mediaPlayer.setOnCompletionListener(this);
			//mediaPlayer.prepare(); // might take long! (for buffering, etc)   //@@
			mediaPlayer.prepareAsync();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block///
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	/**
	 * @param inflater
	 * @param widgetContent
	 * @param widget
	 */
	public void addListCategory(LayoutInflater inflater,RelativeLayout widgetContent, Widget widget) {
		ListView lv = (ListView) inflater.inflate(R.layout.categories_list, null, false); 
		communElements = new ArrayList<CommunElements1>();

		int category_id = widget.getContent().getCategory_id();

		Category category = realm.where(Category.class).equalTo("id",category_id).findFirst();
		//appController.getCategoryById(category_id);
		if (category != null) {
			fillListCategories(communElements, category);
			/** Uness Modif **/
			//			final CategoriesAdapter adapter = new CategoriesAdapter((MainActivity) getActivity(), communElements, colors, R.layout.list_item_category_dashborad);
			final MySplitAdapter adapter = new MySplitAdapter((MainActivity) getActivity(), communElements, colors, R.layout.divided_screen_item, (int) getResources().getDimension(R.dimen.title_item_category)/*R.layout.list_item_category_dashborad*/);

			lv.setAdapter(adapter);
			final ListView list = lv;
			lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					CommunElements1 element = adapter.getItem(position); //.getElements().get(position); // because of the listView header we subtract 1


					String url = null; // "http://backoffice.paperpad.fr/pdf/41/CONNECTION_WIFI.pdf";


					if (element instanceof Category) {

						Category category = (Category)element;
						((MainActivity) getActivity()).openCategory(category);

					}else if (element instanceof Child_pages) {

						Child_pages page = (Child_pages)element;
						url = page.getAuto_open_url();
						if(url != null && !url.isEmpty()) {
							//Log.e(" DashboardFragment <=== url "," : "+url);
							((MainActivity) getActivity()).extras = new Bundle();
							//String url = new ArrayList<Child_pages>(elements).get(position - IS_HEADER_ADDED).getAuto_open_url();
							((MainActivity) getActivity()).extras.putString("link",url);
							//((MainActivity) getActivity()).extras.putString("link", /** URL to GoogleDoc => **/"http://docs.google.com/gview?embedded=true&url=" +  /** URL to PDF => **/ "http://backoffice.paperpad.fr/pdf/41/CONNECTION_WIFI.pdf");
							WebViewFragment webFragment = new WebViewFragment();
							webFragment.setArguments(((MainActivity) getActivity()).extras);
							getActivity().getSupportFragmentManager().beginTransaction()
							.replace(R.id.fragment_container, webFragment).addToBackStack(null).commit();
							return;
						}
						else {
							((MainActivity) getActivity()).extras = new Bundle();
							((MainActivity) getActivity()).extras.putInt("page_id", page.getId_cp());
							PagesFragment pagesFragment = new PagesFragment();
							pagesFragment.setArguments(((MainActivity) getActivity()).extras);
							getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, pagesFragment).addToBackStack(null).commit();

							//((MainActivity) getActivity()).openChildPage(page,false);
						}


					}

//					if(mActivatedPosition != position){
//						if(list.getChildAt(mActivatedPosition - list.getFirstVisiblePosition()) != null)
//						{
//							if(position >1 && position < 4) {
//								list.getChildAt(mActivatedPosition - list.getFirstVisiblePosition()).setBackgroundColor(colors.getColor(colors.getBackground_color()));
//								((TextView)list.getChildAt(mActivatedPosition - list.getFirstVisiblePosition()).findViewById(R.id.TVTitleCategory)).setTextColor(colors.getColor(colors.getTitle_color()));
//
//							}
////							if(((TextView)list.getChildAt(mActivatedPosition - list.getFirstVisiblePosition()).findViewById(R.id.TVTitleCategory)) != null) {
//							list.getChildAt(mActivatedPosition - list.getFirstVisiblePosition()).setBackgroundColor(colors.getColor(colors.getBackground_color()));
//
//							((TextView)list.getChildAt(mActivatedPosition - list.getFirstVisiblePosition()).findViewById(R.id.TVTitleCategory)).setTextColor(colors.getColor(colors.getTitle_color()));
//							Paint paint = new Paint();
//							paint.setColor(colors.getColor(colors.getTitle_color()));
//							((ArrowImageView)list.getChildAt(position - list.getFirstVisiblePosition()).findViewById(R.id.imgArrow)).setPaint(paint);
////							Log.e("  SpltFilredCategories <<!normal>> Color Changed To :  ", ""+colors.getTitle_color());
////							}
//						}
//						
//						mActivatedPosition = position;

						list.getChildAt(position - list.getFirstVisiblePosition()).setBackgroundDrawable(colors.makeGradientToColor(colors.getForeground_color()));//Color(colors.getColor(colors.getTitle_color()));
						((TextView)list.getChildAt(position - list.getFirstVisiblePosition()).findViewById(R.id.TVTitleCategory)).setTextColor(colors.getColor(colors.getBackground_color()));

//					}

				}
			});
		}
		widgetContent.addView(lv, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
	}



	public class DownloadParseAsyncTask extends AsyncTask<String, String, Boolean>{

		/** progress dialog to show user that the backup is processing. */
		public ProgressDialog dialog;
		boolean erreur = false;

		@Override
		protected Boolean doInBackground(String... params) {


			String dashString = Utils.retrieveJson(params[0]);
            Realm  r = Realm.getInstance(getActivity());
            r.beginTransaction();
           r.createOrUpdateObjectFromJson(DashboardMain.class,dashString);
            r.commitTransaction();
				 /*mapper.readValue(dashString, DashboardMain.class);*/


            return erreur;
		}


		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@Override
		protected void onPreExecute() {
			this.dialog.setMessage(getResources().getString(R.string.waiting));
			this.dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			this.dialog.setIndeterminate(false);
			this.dialog.setCancelable(false);
			this.dialog.show();
			super.onPreExecute();
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onProgressUpdate(java.lang.Object[])
		 */
		@Override
		protected void onProgressUpdate(String... values) {
			super.onProgressUpdate(values);
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(Boolean result) {
			if (this.dialog.isShowing()) {
				this.dialog.dismiss();
			}
			//			drawLayouts(deviceHeight, deviceWidth);
			Realm  r = Realm.getInstance(getActivity());
			DashboardMain dashboardMain =r.where(DashboardMain.class).findFirst();
			dashboard = dashboardMain.getDashboard();
			Log.i(LOG_TAG, dashboardMain.getDashboard().getInner_padding()+"");
			getActivity().setRequestedOrientation(prevOrientation);

			super.onPostExecute(result);
		}


		public DownloadParseAsyncTask() {
			super();
			dialog = new ProgressDialog(getActivity());
		}

	}



	/**
	 * @return the dashboard
	 */
	public Dashboard getDashboard() {
		return dashboard;
	}


	/**
	 * @param dashboard the dashboard to set
	 */
	public void setDashboard(Dashboard dashboard) {
		this.dashboard = dashboard;
	}

	public void fillListCategories(List<CommunElements1> communElements, Category category) {
		//		titleInStrip = category.getTitle();

		if (category.getChildren_categories().size()>1) {
			for (Iterator<Category> iterator = category.getChildren_categories().iterator(); iterator
					.hasNext();) {
				CommunElements1 communElement = (CommunElements1) iterator
						.next();
				communElements.add(communElement);
			}
		}else if (category.getChildren_categories().size()==1) {
			Category childCategory = category.getChildren_categories().iterator().next();
			fillListCategories(communElements, childCategory);

		}else {

			Collection<Child_pages> elements = category.getChildren_pages();

			int size = elements.size();
			if (size > 1) {
				for (Iterator<Child_pages> iterator = elements
						.iterator(); iterator.hasNext();) {
					CommunElements1 communElement = (Child_pages) iterator.next();
					if (((Child_pages)communElement).isVisible()) {
						communElements.add(communElement);
					}

				}
			} else if (size == 1) {
				Child_pages page = elements.iterator().next();
				communElements.add(page);

			}

		}


	}


	@Override
	public void onPrepared(MediaPlayer mp) {
		if(!mediaPlayer.isPlaying() && PLAY){
			mediaPlayer.start();
			if (seek >= 0) {
				mediaPlayer.seekTo(seek);
			}
			//	        Progressbar.setVisibility(View.INVISIBLE);
			//	        play.setVisibility(View.GONE);
			//	        stop.setVisibility(View.VISIBLE);
			//	        songProgressBar.setProgress(0);
			//	        songProgressBar.setMax(100);
		}else if (mediaPlayer.isPlaying() && !PLAY) {
			mediaPlayer.pause();
		}
		//	        updateProgressBar(); 

	}


	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {


	}


	@Override
	public void onCompletion(MediaPlayer mp) {
		indexTrack++;
		playMp3();

	}


	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
		}

		super.onDestroy();
	}


	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onStop()
	 */
	@Override
	public void onStop() {

		if (mediaPlayer != null) {
			//	mediaPlayer.stop();
		}
		super.onStop();
	}

	private class MyTimerTask extends TimerTask{

		@Override
		public void run() {        
			//            runOnUiThread(new Runnable() {              
			//                @Override
			//                public void run() {
			//                }
			//            });
		}       
	}



	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onDestroyView()
	 */
	@Override
	public void onDestroyView() {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
		}
		super.onDestroyView();
	}

	public Drawable getMeteoDrawable(String string, boolean hd) {
		Drawable drawable = null;
		if (hd) {
			if (string.equalsIgnoreCase("clear-day") || string.equalsIgnoreCase("clear-night")) {
				drawable = getResources().getDrawable(R.drawable.sun);
			}else if (string.equalsIgnoreCase("sleet") || string.equalsIgnoreCase("rain")) {
				drawable = getResources().getDrawable(R.drawable.rain);
			}else if (string.equalsIgnoreCase("cloudy")) {
				drawable = getResources().getDrawable(R.drawable.cloud);
			}else if (string.equalsIgnoreCase("partly-cloudy-day") || string.equalsIgnoreCase("partly-cloudy-night")) {
				drawable = getResources().getDrawable(R.drawable.sun_cloud);
			}else if (string.equalsIgnoreCase("fog")) {
				drawable = getResources().getDrawable(R.drawable.fog);
			}else if (string.equalsIgnoreCase("snow")) {
				drawable = getResources().getDrawable(R.drawable.snow);
			}else {
				drawable = getResources().getDrawable(R.drawable.sun);
			}
		}else{
			if (string.equalsIgnoreCase("clear-day") || string.equalsIgnoreCase("clear-night")) {
				drawable = getResources().getDrawable(R.drawable.ic_action_sun2x);
			}else if (string.equalsIgnoreCase("sleet") || string.equalsIgnoreCase("rain")) {
				drawable = getResources().getDrawable(R.drawable.ic_action_rain2x);
			}else if (string.equalsIgnoreCase("cloudy")) {
				drawable = getResources().getDrawable(R.drawable.ic_action_cloud2x);
			}else if (string.equalsIgnoreCase("partly-cloudy-day") || string.equalsIgnoreCase("partly-cloudy-night")) {
				drawable = getResources().getDrawable(R.drawable.ic_action_sun_cloud2x);
			}else if (string.equalsIgnoreCase("fog")) {
				drawable = getResources().getDrawable(R.drawable.ic_action_fog2x);
			}else if (string.equalsIgnoreCase("snow")) {
				drawable = getResources().getDrawable(R.drawable.ic_action_snow2x);
			}else {
				drawable = getResources().getDrawable(R.drawable.ic_action_sun2x);
			}
		}
		drawable.setColorFilter(colors.getColor(colors.getTabs_background_color()), Mode.MULTIPLY);
		return drawable;
	}



	public class DownloadMeteo extends AsyncTask<String, Integer, Forecast>{

		ProgressBar bar;

		public DownloadMeteo(ProgressBar bar) {
			this.bar = bar;
		}

		@Override
		protected Forecast doInBackground(String... params) {

			String dashString = Utils.retrieveJson(params[0]);

                Realm r = Realm.getInstance(getActivity());
            r.beginTransaction();
             r.createOrUpdateObjectFromJson(Forecast.class,dashString);
            r.commitTransaction();/*mapper.readValue(dashString, Forecast.class);*/


            return null;
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(Forecast result) {
			Realm  r = Realm.getInstance(getActivity());
			Forecast forecast =r.where(Forecast.class).findFirst();
			if(forecast!=null)
				Log.i(LOG_TAG, forecast.getTimezone()+" "+ forecast.getLatitude()+", "+forecast.getLongitude());
			NumberFormat nf = NumberFormat.getInstance(); // get instance
			nf.setMaximumFractionDigits(0); // set decimal places
			nf.setMinimumFractionDigits(0);

			ImageView todayIcon = (ImageView) weatherMap.get("todayIcon");
			TextView todayDegree = (TextView) weatherMap.get("todayDegree");
			TextView todayDegreeMin = (TextView) weatherMap.get("todayDegreeMin");
			TextView todayDegreeMax = (TextView) weatherMap.get("todayDegreeMax");
			TextView day1Degree = (TextView) weatherMap.get("day1Degree");
			TextView day2Degree = (TextView) weatherMap.get("day2Degree");
			TextView day3Degree = (TextView) weatherMap.get("day3Degree");
			TextView day4Degree = (TextView) weatherMap.get("day4Degree");
			TextView day5Degree = (TextView) weatherMap.get("day5Degree");
			ImageView day1Icon = (ImageView) weatherMap.get("day1Icon");
			ImageView day2Icon = (ImageView) weatherMap.get("day2Icon");
			ImageView day3Icon = (ImageView) weatherMap.get("day3Icon");
			ImageView day4Icon = (ImageView) weatherMap.get("day4Icon");
			ImageView day5Icon = (ImageView) weatherMap.get("day5Icon");
			TextView day1 = (TextView) weatherMap.get("day1");
			TextView day2 = (TextView) weatherMap.get("day2");
			TextView day3 = (TextView) weatherMap.get("day3");
			TextView day4 = (TextView) weatherMap.get("day4");
			TextView day5 = (TextView) weatherMap.get("day5");
			
			todayDegree.setTypeface(MainActivity.FONT_BODY);
			todayDegreeMax.setTypeface(MainActivity.FONT_BODY);
			todayDegreeMin.setTypeface(MainActivity.FONT_BODY);
			day1Degree.setTypeface(MainActivity.FONT_BODY);
			day2Degree.setTypeface(MainActivity.FONT_BODY);
			day3Degree.setTypeface(MainActivity.FONT_BODY);
			day4Degree.setTypeface(MainActivity.FONT_BODY);
			day5Degree.setTypeface(MainActivity.FONT_BODY);
			day1.setTypeface(MainActivity.FONT_BODY);
			day2.setTypeface(MainActivity.FONT_BODY);
			day3.setTypeface(MainActivity.FONT_BODY);
			day4.setTypeface(MainActivity.FONT_BODY);
			day5.setTypeface(MainActivity.FONT_BODY);
					realm = Realm.getInstance(getActivity());
            result = realm.where(Forecast.class).findFirst();
			if (result != null) {
				todayDegree.setText(nf.format(result.getCurrently().getTemperature())+"°");
				todayDegreeMin.setText(nf.format(result.getDaily().getData().get(0).getTemperatureMin())+"°");
				todayDegreeMax.setText(nf.format(result.getDaily().getData().get(0).getTemperatureMax())+"°");
				todayIcon.setImageDrawable(getMeteoDrawable(result.getCurrently().getIcon(), true));
				day1Degree.setText(nf.format(result.getDaily().getData().get(1).getTemperatureMax())+"°");
				day2Degree.setText(nf.format(result.getDaily().getData().get(2).getTemperatureMax())+"°");
				day3Degree.setText(nf.format(result.getDaily().getData().get(3).getTemperatureMax())+"°");
				day4Degree.setText(nf.format(result.getDaily().getData().get(4).getTemperatureMax())+"°");
				day5Degree.setText(nf.format(result.getDaily().getData().get(5).getTemperatureMax())+"°");
				day1Icon.setImageDrawable(getMeteoDrawable(result.getDaily().getData().get(1).getIcon(), false));
				day2Icon.setImageDrawable(getMeteoDrawable(result.getDaily().getData().get(2).getIcon(), false));
				day3Icon.setImageDrawable(getMeteoDrawable(result.getDaily().getData().get(3).getIcon(), false));
				day4Icon.setImageDrawable(getMeteoDrawable(result.getDaily().getData().get(4).getIcon(), false));
				day5Icon.setImageDrawable(getMeteoDrawable(result.getDaily().getData().get(5).getIcon(), false));

				//				SimpleDateFormat dateFormat = new SimpleDateFormat("EE", Locale.FRANCE);
				//				dateFormat.setTimeZone(TimeZone.getTimeZone(timezone)); 
				day1.setText(CommonUtilities.getDateStringCurrentTimeZone(result.getDaily().getData().get(1).getTime()));
				day2.setText(CommonUtilities.getDateStringCurrentTimeZone(result.getDaily().getData().get(2).getTime()));
				day3.setText(CommonUtilities.getDateStringCurrentTimeZone(result.getDaily().getData().get(3).getTime()));
				day4.setText(CommonUtilities.getDateStringCurrentTimeZone(result.getDaily().getData().get(4).getTime()));
				day5.setText(CommonUtilities.getDateStringCurrentTimeZone(result.getDaily().getData().get(5).getTime()));

				// colors
				todayDegree.setTextColor(colors.getColor(colors.getTitle_color()));
				todayDegreeMin.setTextColor(colors.getColor(colors.getTitle_color()));
				todayDegreeMax.setTextColor(colors.getColor(colors.getTitle_color()));
				day1Degree.setTextColor(colors.getColor(colors.getTitle_color()));
				day2Degree.setTextColor(colors.getColor(colors.getTitle_color()));
				day3Degree.setTextColor(colors.getColor(colors.getTitle_color()));
				day4Degree.setTextColor(colors.getColor(colors.getTitle_color()));
				day5Degree.setTextColor(colors.getColor(colors.getTitle_color()));
				day1.setTextColor(colors.getColor(colors.getTitle_color()));
				day2.setTextColor(colors.getColor(colors.getTitle_color()));
				day3.setTextColor(colors.getColor(colors.getTitle_color()));
				day4.setTextColor(colors.getColor(colors.getTitle_color()));
				day5.setTextColor(colors.getColor(colors.getTitle_color()));

			}
			View widgetMeteo = (View) weatherMap.get("widgetMeteo");
			widgetMeteo.setVisibility(View.VISIBLE);
			bar.setVisibility(View.GONE);

			super.onPostExecute(result);
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@Override
		protected void onPreExecute() {
			bar.setVisibility(View.VISIBLE);
			super.onPreExecute();
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onProgressUpdate(java.lang.Object[])
		 */
		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}

	}




	public class DownloadRoomService extends AsyncTask<String, Integer, RoomService>{

		private ProgressBar bar;

		@Override
		protected RoomService doInBackground(String... params) {

			String roomString = Utils.retrieveJson(params[0]);
			RoomService roomServices = null;
			if (roomString != null) {
                Realm r =  Realm.getInstance(getActivity());
                r.beginTransaction();
                r.createOrUpdateObjectFromJson(RoomService.class,roomString);
                r.commitTransaction();


                //roomServices = mapper.readValue(roomString, RoomService.class);
                //				Log.i(LOG_TAG, roomServices.getRequests().get(0).getTitle()+" "+ roomServices.getRequests().get(0).getStatus()+", "+roomServices.getRequests().get(0).getTotal_amount());
            }
			return roomServices;
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(RoomService result) {
			Log.e("ASYNC DashboardFragment", "POST EXECUTE");
			final RoomService roomService = result;
			if (roomService != null && getActivity() != null) {
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (roomService.getRequests() != null && roomService.getRequests().size()>0) {
							adapterRoomServ = new RoomServicesAdapter(roomService.getRequests(), colors, getActivity());
							listRoomService.setAdapter(adapterRoomServ);
							adapterRoomServ.notifyDataSetChanged();
						}			
//						else {
							/** Uness Modif **/
//							LinearLayout view = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.divided_screen_item, null, false);//new RelativeLayout(getActivity());
//							//view.setLayoutParams(new LinearLayout.LayoutParams(250, 150));
//							view.setBackgroundColor(Color.BLUE);
//							TextView txt = (TextView) view.findViewById(R.id.TVTitleCategory); //new TextView(getActivity());
//							view.findViewById(R.id.imgCategory).setVisibility(View.GONE);
//							//txt.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
//							txt.setText(" Vous n'avez fait aucune demande.");
//							//txt.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
//							txt.setTextSize(24);
//							txt.setTextColor(colors.getColor(colors.getTitle_color()));
//							//							view.addView(txt, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
//							view.setClickable(false);
//							listRoomService.addHeaderView(view, null, true);
//							
//							listRoomService.setMinimumHeight(150);

							/** End **/

//						}



					}
				});
			}
			bar.setVisibility(View.GONE);
			super.onPostExecute(result);
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@Override
		protected void onPreExecute() {
			bar.setVisibility(View.VISIBLE);
			super.onPreExecute();
		}

		public DownloadRoomService(ProgressBar bar) {
			super();
			this.bar = bar;
		}

	}

	/** a method to fill the upper bar where we choose the {@link CategoriesMyBox}
	 * @param feed
	 */
	private void fillNavigationBar( Feed feed) {
		final LinearLayout feedChoicesHolder = (LinearLayout) rssMap.get("feedChoicesHolder");
		TextView feedName = new TextView(getActivity());
		feedName.setTypeface(MainActivity.FONT_BODY, Typeface.BOLD);//.setTypeface(MainActivity.FONT_BODY);
		//		View verticalSeparator = new View(getActivity());
		//		LinearLayout.LayoutParams separatorParams = new LinearLayout.LayoutParams(1, LinearLayout.LayoutParams.MATCH_PARENT);
		//		separatorParams.setMargins(0, 5, 0, 5);
		//		verticalSeparator.setBackgroundColor(Color.parseColor("#88777777"));
		//		feedChoicesHolder.addView(verticalSeparator, separatorParams);
		if (feed.isSelected() ) {
			LinearLayout.LayoutParams txtParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
			txtParams.gravity = Gravity.CENTER;
			feedName.setGravity(Gravity.CENTER);
			//			txtParams.setMargins(10, 5, 10, 5);
			feedName.setText(feed.getTitle().toUpperCase());
			feedName.setTextColor(colors.getColor(colors.getBackground_color()));
			feedName.setPadding(10, 5, 10, 5);
			//			Drawable selectDrawable = getResources().getDrawable(R.drawable.back_choices_final);
			//			selectDrawable.setColorFilter(colors.getColor(colors.getTitle_color()), Mode.MULTIPLY);
			//			feedName.setBackgroundDrawable(selectDrawable);
			feedName.setBackgroundColor(colors.getColor(colors.getTitle_color()));
			feedName.setTag(feed);
			feedChoicesHolder.addView(feedName, txtParams);

		}else {

			LinearLayout.LayoutParams txtParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
			//			txtParams.setMargins(10, 5, 10, 5);
			feedName.setGravity(Gravity.CENTER);
			txtParams.gravity = Gravity.CENTER;
			feedName.setText((feed.getTitle()).toUpperCase());
			feedName.setTextColor(colors.getColor(colors.getTitle_color()));
			feedName.setTag(feed);
			feedName.setPadding(10, 5, 10, 5);

			feedChoicesHolder.addView(feedName, txtParams);
		}

		feedName.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
                realm.beginTransaction();
				Feed feed = (Feed) v.getTag();
				List<Feed> feeds = (List<Feed>) rssMap.get("feeds");
				for (int i = 0; i < feeds.size(); i++) {
					feeds.get(i).setSelected(false);
				}
				feed.setSelected(true);
				if (timerRSS != null) {
					timerRSS.cancel();
				}
				feedChoicesHolder.removeAllViews();
				for (int j = 0; j < feeds.size(); j++) {
					fillNavigationBar(feeds.get(j));
				}
				rssMap.put("feed", feed);
				timerRSS = executeRSSTimedThread(rssMap);
                realm.commitTransaction();
			}
		});
	}

	/**
	 * @param deviceHeight
	 * @param deviceWidth
	 */
	public void drawLayouts(int deviceHeight, int deviceWidth, LayoutInflater inflater) {
		widgetsViews = new ArrayList<RelativeLayout>();
		RelativeLayout rlContainer = new RelativeLayout(getActivity());
		RelativeLayout.LayoutParams pms;
		int innerMargin = dashboard.getInner_padding();
		int outerMargin = dashboard.getOuter_margin();
		for (int i = 0; i < dashboard.getWidgets().size(); i++) {

			final Widget widget = dashboard.getWidgets().get(i);
			if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {


				Landscape_layout land_layout = widget.getLandscape_layout();
				int relW = land_layout.getWidth();
				int relH = land_layout.getHeight();
				int widgetWidth = (int)((float)relW*(((float)deviceWidth/(float)40)));
				int widgetHeight = (int)((float)relH*(((float)deviceHeight/(float)30)));
				pms = new LayoutParams(widgetWidth, widgetHeight);
				pms.leftMargin = (int) (((float)land_layout.getX())*((float)deviceWidth/(float)40));
				pms.topMargin = (int) (((float)land_layout.getY())*(((float)deviceHeight/(float)30)));

				RelativeLayout widgetContainer = new RelativeLayout(getActivity());
				widgetsViews.add(widgetContainer);
				widgetContainer.setBackgroundColor(Color.parseColor("#FFDEF7"));
				LinearLayout intermediare = new LinearLayout(getActivity());
				intermediare.addView(widgetContainer, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
				intermediare.setLayoutParams(pms);
				intermediare.setPadding(innerMargin, innerMargin, innerMargin, innerMargin);
				rlContainer.addView(intermediare);
				View widgetLayout = inflater.inflate(R.layout.widget_layout, null, false);
				final RelativeLayout widgetContent = (RelativeLayout)widgetLayout.findViewById(R.id.contentWidget);
				widgetContainer.addView(widgetLayout, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
				final LinearLayout titleProductHolder_widget = (LinearLayout)widgetLayout.findViewById(R.id.titleProductHolder_widget);
				TextView titleTV_widget = (TextView)widgetLayout.findViewById(R.id.titleTV_widget);
				titleTV_widget.setText(widget.getTitle());
				titleTV_widget.setTypeface(MainActivity.FONT_BODY);

				RelativeLayout.LayoutParams params = new LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
				params.addRule(RelativeLayout.CENTER_IN_PARENT);

				drawWidgets(inflater, i, widget, widgetWidth, widgetContainer,
						widgetContent, titleProductHolder_widget, params);

			} else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

				Portrait_layout port_layout = widget.getPortrait_layout();
				int relW = port_layout.getWidth();
				int relH = port_layout.getHeight();

				pms = new LayoutParams((int)((float)relW*(((float)deviceWidth/(float)30))), (int)((float)relH*(((float)deviceHeight/(float)40))));
				pms.leftMargin = (int) (((float)port_layout.getX())*((float)deviceWidth/(float)30));
				pms.topMargin = (int) (((float)port_layout.getY())*(((float)deviceHeight/(float)40)));

				RelativeLayout imgContainerRL = new RelativeLayout(getActivity());
				imgContainerRL.setBackgroundColor(Color.parseColor(Utils.progressiveColor(i+1, dashboard.getWidgets().size())));
				LinearLayout intermediare = new LinearLayout(getActivity());
				intermediare.addView(imgContainerRL, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
				intermediare.setLayoutParams(pms);
				intermediare.setPadding(innerMargin, innerMargin, innerMargin, innerMargin);
				rlContainer.addView(intermediare);


			} else {

			}
		}
		LinearLayout tilesContainer = (LinearLayout)view.findViewById(R.id.tilesContainer);
		tilesContainer.setPadding(outerMargin, outerMargin, outerMargin, outerMargin);
		LinearLayout.LayoutParams paramsRLContainer = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		tilesContainer.addView(rlContainer, paramsRLContainer);
	}


@Override
public void onPause() {
	if(timer != null)
		timer.cancel();
	super.onPause();
}



}
