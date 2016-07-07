package com.euphor.paperpad.activities.fragments;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.euphor.paperpad.Beans.Parameters;
import com.euphor.paperpad.R;
import com.euphor.paperpad.activities.main.MainActivity;
import com.euphor.paperpad.activities.main.MyApplication;
import com.euphor.paperpad.Beans.AgendaGroup;
import com.euphor.paperpad.Beans.CategoriesMyBox;
import com.euphor.paperpad.Beans.Event;

import com.euphor.paperpad.utils.Colors;
import com.euphor.paperpad.utils.Utils;
import com.euphor.paperpad.utils.jsonUtils.AppHit;
import com.euphor.paperpad.widgets.ArrowImageView;
import com.euphor.paperpad.widgets.VerticalViewPager;


import java.io.File;
import java.io.IOException;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * @author euphordev04
 *
 */
public class CalendarHebdoPagerFragment extends Fragment{

    private CalendarPagerAdapter mAdapter = null;

    // Views
    //	private int layoutRes;
    //	private View mRootView;
    private VerticalViewPager mPager;
    private ViewPager mWeeklyPager;

    // Handlers
    private final Handler handler = new Handler();
    private Runnable runPager;
    private boolean mCreated = false;



    private List<Event> event;

    //private List<Child_pages> pages;

    //protected int id_cat = 0;
    protected LinearLayout choiceHolder;
    //public static int mActivatedPosition = 1;
    //private View view;
    private int indexPage;


    protected String backMonthName, previousMonthName, backWeekName, previousWeekName;
    //private AgendaEventAdapter adapter;

    List<String> childPagesKeys, dayKeys, childPagesWeekKeys;
    HashMap<String, List<Event>> mapMonthly;

    /**
     * <b>false</b> : for monthly calendar <br>
     * <b>true</b> : for weekly calendar
     */
    public boolean monthlyWeekly = false;


    private Colors colors;

    protected TextView backMonth, monthDate, previousMonth;

    private List<AgendaGroup> agendaGroups;

    private boolean isTablet;

    public TextView from_date, week_date, prev_from_date;

    private HashMap<String, List<Event>> mapWeekly;

    private int indexPageWeek;

    private String weekDateName;

    private String monthDateName;

    private Calendar currentCalendar;
    private Realm realm;
    private RealmResults<Event> events;

    @Override
    public void onAttach(Activity activity) {

        //appController = ((MyApplication)getActivity().getApplication()).getAppController();//new AppController(getActivity());
        realm = Realm.getInstance(getActivity());
        events = realm.where(Event.class).findAll(); //appController.getEventDao().queryForAll();realm.where(Event.class).findAll().sort("date")
         events.sort("date");
        //events.addAll(0,event);
        if (monthlyWeekly) {
            mapWeekly = getMapEventsWeekly(events);
        }else {
            mapMonthly = getMapEventsMonthly(events);
        }

        colors = ((MainActivity)activity).colors;
        Parameters paramColor =realm.where(Parameters.class).findFirst();
        if (colors==null) {
            colors = new Colors(paramColor);
        }
        isTablet = Utils.isTablet(activity);//getResources().getBoolean(R.bool.isTablet);
        ((MainActivity)activity).bodyFragment = "CalendarPagerFragment";
        //setAdapter(new CalendarPagerAdapter(getChildFragmentManager()));
        time = System.currentTimeMillis();
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.calendar_hebdo, container, false);
        view.setBackgroundColor(colors.getBackMixColor(colors.getForeground_color(), 0.10f));//getColor(colors.getForeground_color(), "20"));

        realm.beginTransaction();

        choiceHolder = (LinearLayout)view.findViewById(R.id.choicesHolder);

        agendaGroups = getAvailableAgendaGroup();

        for(int i = 0; i < agendaGroups.size(); i++) {
            agendaGroups.get(i).setSelected(true);
            fillNavigationBar(agendaGroups.get(i));
        }

        TextView monthly_btn = (TextView)view.findViewById(R.id.monthly);
        TextView weekly_btn = (TextView)view.findViewById(R.id.weekly);
        currentCalendar = new GregorianCalendar();

        monthly_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                monthlyWeekly = false;
                view.findViewById(R.id.weekly_calendar).setVisibility(View.GONE);
                view.findViewById(R.id.monthly_calendar).setVisibility(View.VISIBLE);


                //mapWeekly = getMapEventsWeekly(events);

                //currentCalendar = new GregorianCalendar();
                Date date = null;
                try {
                    date = new SimpleDateFormat("yyyy-MM-dd").parse(mapWeekly.get(childPagesWeekKeys.get(indexPageWeek)).get(0).getDate());
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                currentCalendar.setTime(date);

                //mapMonthly = getMapEventsMonthly(events);
                makeAView(view);
                //view.requestLayout();
            }
        });
        weekly_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                monthlyWeekly = true;
                view.findViewById(R.id.weekly_calendar).setVisibility(View.VISIBLE);
                view.findViewById(R.id.monthly_calendar).setVisibility(View.GONE);

                //mapMonthly = getMapEventsMonthly(events);
                //currentCalendar = new GregorianCalendar();
                Date date = null;
                try {
                    date = new SimpleDateFormat("yyyy-MM-dd").parse(mapMonthly.get(childPagesKeys.get(indexPage)).get(0).getDate());
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                currentCalendar.setTime(date);


                //mapWeekly = getMapEventsWeekly(events);
                makeAView(view);
                //view.requestLayout();
            }
        });

        mapWeekly = getMapEventsWeekly(events);
        mapMonthly = getMapEventsMonthly(events);
        makeAView(view);

        realm.commitTransaction();

        return view;
    }

    public void makeAView(View view){

        TextView monthly_btn = (TextView)view.findViewById(R.id.monthly);
        TextView weekly_btn = (TextView)view.findViewById(R.id.weekly);

        monthly_btn.setTypeface(MainActivity.FONT_BODY, Typeface.BOLD);
        weekly_btn.setTypeface(MainActivity.FONT_BODY, Typeface.BOLD);

//		RoundRectShape rect = new RoundRectShape(
//				new float[] {5, 5, 5, 5, 5 , 5, 5, 5},
//				//new float[] {10, 10, 10, 10, 10, 10, 10, 10},
//				null,
//				null);
//
//		ShapeDrawable border = new ShapeDrawable(rect);
//		border.getPaint().setColor(colors.getColor(colors.getTitle_color()));


        LinearLayout.LayoutParams txtParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);

        txtParams.setMargins(5, 12, 5, 12);
        view.findViewById(R.id.monthlyWeeklyBtn).setLayoutParams(txtParams);
        //categoryTxt.setPadding(2, 2, 2, 2);


        //		monthly_btn.setBackgroundDrawable(createUserDrawable(colors.getTitle_color(), 10, 20, 0, 0));
        //		weekly_btn.setBackgroundDrawable(createUserDrawable(colors.getTitle_color(), 0, 0, 10, 20));

        if(!monthlyWeekly){
            //mapMonthly = getMapEventsMonthly(events);

//			RoundRectShape rectM = new RoundRectShape(
//					new float[] {5, 5, 0, 0, 0, 0, 5, 5},
//					null,
//					null);
//
//			ShapeDrawable borderM = new ShapeDrawable(rectM);
//			borderM.getPaint().setColor(colors.getColor(colors.getTitle_color()));
//			monthly_btn.setBackgroundDrawable(borderM);
//
//
//
//			RoundRectShape rectW = new RoundRectShape(
//					new float[] {0, 0, 5, 5, 5, 5, 0, 0},
//					null,
//					null);
//
//
//			ShapeDrawable borderW = new ShapeDrawable(rectW);
//			borderW.getPaint().setColor(colors.getColor(colors.getBackground_color()));
//			weekly_btn.setBackgroundDrawable(borderW);
            monthly_btn.setBackgroundColor(colors.getColor(colors.getBody_color(), "AA"));
            weekly_btn.setBackgroundColor(colors.getColor(colors.getBackground_color(), "AA"));

            monthly_btn.setTextColor(colors.getColor(colors.getBackground_color()));
            weekly_btn.setTextColor(colors.getColor(colors.getBody_color()));

            view.findViewById(R.id.weekly_calendar).setVisibility(View.GONE);
            view.findViewById(R.id.monthly_calendar).setVisibility(View.VISIBLE);

            ArrowImageView backArrow = (ArrowImageView)view.findViewById(R.id.backArrow);
            backArrow.setColor(colors.getColor(colors.getTitle_color(), "AA"));
            final View finalView = view;
            view.findViewById(R.id.backMonthContainer).setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (mPager.getCurrentItem() > 0) {
                        mPager.setCurrentItem(mPager.getCurrentItem() - 1);
                    }
                }
            });

            backMonth = (TextView)view.findViewById(R.id.backMonth);
            if (!isTablet) {
                backMonth.setVisibility(View.GONE);
            }else {
                backMonth.setTypeface(MainActivity.FONT_BODY);
                backMonth.setTextColor(colors.getColor(colors.getTitle_color()));
            }
            backMonth.setTypeface(MainActivity.FONT_BODY);
            backMonth.setTextColor(colors.getColor(colors.getTitle_color()));



            monthDate = (TextView)view.findViewById(R.id.monthDate);
            monthDate.setTypeface(MainActivity.FONT_BODY);
            monthDate.setTextColor(colors.getColor(colors.getTitle_color()));

            previousMonth = (TextView)view.findViewById(R.id.previousMonth);
            if (!isTablet) {
                previousMonth.setVisibility(View.GONE);
            }else {
                previousMonth.setTypeface(MainActivity.FONT_BODY);
                previousMonth.setTextColor(colors.getColor(colors.getTitle_color()));
            }
            previousMonth.setTypeface(MainActivity.FONT_BODY);
            previousMonth.setTextColor(colors.getColor(colors.getTitle_color()));


            ArrowImageView previousArrow = (ArrowImageView)view.findViewById(R.id.previousArrow);
            previousArrow.setColor(colors.getColor(colors.getTitle_color(), "AA"));

            view.findViewById(R.id.previousMonthContainer).setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (mPager.getCurrentItem() < mapMonthly.size() - 1) {
                        mPager.setCurrentItem(mPager.getCurrentItem() + 1);
                    }
                }
            });

            mPager = (VerticalViewPager) view.findViewById(R.id.vertical_pager);
            mPager.setOffscreenPageLimit(1);
            mPager.setAdapter(new CalendarPagerAdapter(getChildFragmentManager()));
            Event event = getNextEvent(currentCalendar);
            mPager.setCurrentItem(indexPage);
            if(event != null) {
                Calendar mydate = new GregorianCalendar();
                SimpleDateFormat formatter = new SimpleDateFormat("MMMM yyyy");
                Date date = null;


                if(backMonthName!= null && !backMonthName.isEmpty())
                    backMonth.setText(backMonthName);
                if(mPager.getCurrentItem() == 0)
                    view.findViewById(R.id.backMonthContainer).setVisibility(View.GONE);

                try {
                    date = new SimpleDateFormat("yyyy-MM-dd").parse(event.getDate());
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                mydate.setTime(date);
                String mDate = Character.toUpperCase(formatter.format(mydate.getTime()).charAt(0)) + formatter.format(mydate.getTime()).substring(1);
                monthDate.setText(mDate);
                //monthDate.setText(event.getDate());

                if(previousMonthName!= null && !previousMonthName.isEmpty())
                    previousMonth.setText(previousMonthName);
                if(mPager.getCurrentItem() == mapMonthly.size() - 1)
                    view.findViewById(R.id.previousMonthContainer).setVisibility(View.GONE);

            }else{

                if(backMonthName!= null && !backMonthName.isEmpty())
                    backMonth.setText(backMonthName);
                if(mPager.getCurrentItem() == 0)
                    view.findViewById(R.id.backMonthContainer).setVisibility(View.GONE);

                //String mDate = Character.toUpperCase(formatter.format(mydate.getTime()).charAt(0)) + formatter.format(mydate.getTime()).substring(1);
                monthDate.setText(monthDateName);
                //monthDate.setText(event.getDate());

                if(previousMonthName!= null && !previousMonthName.isEmpty())
                    previousMonth.setText(previousMonthName);
                if(mPager.getCurrentItem() == mapMonthly.size() - 1)
                    view.findViewById(R.id.previousMonthContainer).setVisibility(View.GONE);
            }


            mPager.setOnPageChangeListener(new OnPageChangeListener() {

                @Override
                public void onPageSelected(int position) {

                    Calendar mydate = new GregorianCalendar();
                    SimpleDateFormat formatter = new SimpleDateFormat("MMMM");
                    Date date = null;
                    Event event = mapMonthly.get(childPagesKeys.get(position)).get(0);


                    if(event != null) {
                        indexPage = position;
                        try {
                            date = new SimpleDateFormat("yyyy-MM-dd").parse(event.getDate());
                        } catch (ParseException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                        currentCalendar.setTime(date);

                        if(position > 0) {
                            Event lastEvent = mapMonthly.get(childPagesKeys.get(position - 1)).get(0);

                            try {
                                date = new SimpleDateFormat("yyyy-MM-dd").parse(lastEvent.getDate());
                            } catch (ParseException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            mydate.setTime(date);
                            String backDate = Character.toUpperCase(formatter.format(mydate.getTime()).charAt(0)) + formatter.format(mydate.getTime()).substring(1);
                            if(((LinearLayout)finalView.findViewById(R.id.backMonthContainer)).getVisibility() == 8)
                                finalView.findViewById(R.id.backMonthContainer).setVisibility(View.VISIBLE);

                            backMonth.setText((lastEvent != null)?backDate:"");
                        }
                        else{
                            finalView.findViewById(R.id.backMonthContainer).setVisibility(View.GONE);
                        }

                        SimpleDateFormat formatter_ = new SimpleDateFormat("MMMM yyyy");

                        try {
                            date = new SimpleDateFormat("yyyy-MM-dd").parse(event.getDate());
                        } catch (ParseException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        mydate.setTime(date);
                        String mDate = Character.toUpperCase(formatter_.format(mydate.getTime()).charAt(0)) + formatter_.format(mydate.getTime()).substring(1);
                        monthDate.setText(mDate);

                        if(position < mapMonthly.size() - 1) {
                            Event nextEvent = mapMonthly.get(childPagesKeys.get(position + 1)).get(0);
                            try {
                                date = new SimpleDateFormat("yyyy-MM-dd").parse(nextEvent.getDate());
                            } catch (ParseException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            mydate.setTime(date);
                            String prevDate = Character.toUpperCase(formatter.format(mydate.getTime()).charAt(0)) + formatter.format(mydate.getTime()).substring(1);
                            if(((LinearLayout)finalView.findViewById(R.id.previousMonthContainer)).getVisibility() == 8)
                                finalView.findViewById(R.id.previousMonthContainer).setVisibility(View.VISIBLE);
                            previousMonth.setText((nextEvent != null)?prevDate:"");
                        }
                        else{
                            finalView.findViewById(R.id.previousMonthContainer).setVisibility(View.GONE);
                        }


                    }


                }

                @Override
                public void onPageScrolled(int arg0, float arg1, int arg2) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onPageScrollStateChanged(int arg0) {
                    CalendarMonthFragment frag1= (CalendarMonthFragment)mPager.getAdapter().instantiateItem(mPager, mPager.getCurrentItem());
                    frag1.updateAdapter(agendaGroups);
                }
            });
        }else{
            //mapWeekly = getMapEventsWeekly(events);



//
//
//
//			RoundRectShape rectW = new RoundRectShape(
//					new float[] {0, 0, 5, 5, 5, 5, 0, 0},
//					null,
//					null);
//
//			ShapeDrawable borderW = new ShapeDrawable(rectW);
//			borderW.getPaint().setColor(colors.getColor(colors.getTitle_color()));
//			weekly_btn.setBackgroundDrawable(borderW);


            monthly_btn.setBackgroundColor(colors.getColor(colors.getBackground_color(), "AA"));
            weekly_btn.setBackgroundColor(colors.getColor(colors.getBody_color(), "AA"));

            monthly_btn.setTextColor(colors.getColor(colors.getBody_color()));
            weekly_btn.setTextColor(colors.getColor(colors.getBackground_color()));

            view.findViewById(R.id.weekly_calendar).setVisibility(View.VISIBLE);
            view.findViewById(R.id.monthly_calendar).setVisibility(View.GONE);

            ArrowImageView backArrow = (ArrowImageView)view.findViewById(R.id.weekly_backArrow);
            backArrow.setColor(colors.getColor(colors.getTitle_color(), "AA"));

            view.findViewById(R.id.backWeekContainer).setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (mWeeklyPager.getCurrentItem() > 0) {
                        mWeeklyPager.setCurrentItem(mWeeklyPager.getCurrentItem() - 1);
                    }
                }
            });

            ArrowImageView previousArrow = (ArrowImageView)view.findViewById(R.id.weekly_previousArrow);
            previousArrow.setColor(colors.getColor(colors.getTitle_color(), "AA"));

            view.findViewById(R.id.previousWeekContainer).setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (mWeeklyPager.getCurrentItem() < mapWeekly.size() - 1) {
                        mWeeklyPager.setCurrentItem(mWeeklyPager.getCurrentItem() + 1);
                    }
                }
            });

            mWeeklyPager = (ViewPager)view.findViewById(R.id.weekly_pager);
            from_date = (TextView)view.findViewById(R.id.from_date);
            //to_date = (TextView)view.findViewById(R.id.to_date);
            week_date = (TextView)view.findViewById(R.id.week_date);
            prev_from_date = (TextView)view.findViewById(R.id.prev_from_date);
            //prev_to_date = (TextView)view.findViewById(R.id.prev_to_date);

            from_date.setTypeface(MainActivity.FONT_BODY);
            week_date.setTypeface(MainActivity.FONT_BODY);
            prev_from_date.setTypeface(MainActivity.FONT_BODY);

            from_date.setTextColor(colors.getColor(colors.getTitle_color()));
            week_date.setTextColor(colors.getColor(colors.getTitle_color()));
            prev_from_date.setTextColor(colors.getColor(colors.getTitle_color()));

            mWeeklyPager.setOffscreenPageLimit(1);
            mWeeklyPager.setAdapter(new CalendarWeekPagerAdapter(getChildFragmentManager()));
            Event event = getNextWeeklyEvent(currentCalendar);
            mWeeklyPager.setCurrentItem(indexPageWeek);

            if(event != null) {
                Calendar mydate = new GregorianCalendar();
                SimpleDateFormat formatter = new SimpleDateFormat("MMMM yyyy");
                Date date = null;


                if(backWeekName!= null && !backWeekName.isEmpty()){
                    from_date.setText(backWeekName);
                }
                if(mWeeklyPager.getCurrentItem() == 0)
                    view.findViewById(R.id.backWeekContainer).setVisibility(View.GONE);

                try {
                    date = new SimpleDateFormat("yyyy-MM-dd").parse(event.getDate());
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                mydate.setTime(date);
                //String mDate = Character.toUpperCase(formatter.format(mydate.getTime()).charAt(0)) + formatter.format(mydate.getTime()).substring(1);
                //week_date.setText(mDate);

                week_date.setText(getCurrentWeek(mydate).replace("\n", " "));
                //monthDate.setText(event.getDate());

                if(previousWeekName!= null && !previousWeekName.isEmpty()){
                    //					prev_from_date.setText(getString(R.string.start_date)+" "+previousWeekName);
                    //					prev_to_date.setText(getString(R.string.end_date)+" "+previousWeekName);
                    //prev_to_date.setVisibility(View.GONE);
                    prev_from_date.setText(previousWeekName);
                }
                if(mWeeklyPager.getCurrentItem() == mapWeekly.size() - 1)
                    view.findViewById(R.id.previousWeekContainer).setVisibility(View.GONE);

            }else{

                if(backWeekName!= null && !backWeekName.isEmpty()){
                    from_date.setText(backWeekName);
                }
                if(mWeeklyPager.getCurrentItem() == 0)
                    view.findViewById(R.id.backWeekContainer).setVisibility(View.GONE);

                //String mDate = Character.toUpperCase(formatter.format(mydate.getTime()).charAt(0)) + formatter.format(mydate.getTime()).substring(1);
                //week_date.setText(mDate);

                week_date.setText(weekDateName.replace("\n", " "));
                //monthDate.setText(event.getDate());

                if(previousWeekName!= null && !previousWeekName.isEmpty()){
                    //					prev_from_date.setText(getString(R.string.start_date)+" "+previousWeekName);
                    //					prev_to_date.setText(getString(R.string.end_date)+" "+previousWeekName);
                    //prev_to_date.setVisibility(View.GONE);
                    prev_from_date.setText(previousWeekName);
                }
                if(mWeeklyPager.getCurrentItem() == mapWeekly.size() - 1)
                    view.findViewById(R.id.previousWeekContainer).setVisibility(View.GONE);
            }

            final View finalView = view;
            mWeeklyPager.setOnPageChangeListener(new OnPageChangeListener() {

                @Override
                public void onPageSelected(int position) {

                    Calendar mydate = new GregorianCalendar();
                    SimpleDateFormat formatter = new SimpleDateFormat("MMMM");
                    Date date = null;

                    Event event = mapWeekly.get(childPagesWeekKeys.get(position)).get(0);

                    if(event != null) {

                        indexPageWeek = position;

                        try {
                            date = new SimpleDateFormat("yyyy-MM-dd").parse(event.getDate());
                        } catch (ParseException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                        currentCalendar.setTime(date);

                        if(position > 0) {
                            Event lastEvent = mapWeekly.get(childPagesWeekKeys.get(position - 1)).get(0);

                            try {
                                date = new SimpleDateFormat("yyyy-MM-dd").parse(lastEvent.getDate());
                            } catch (ParseException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            mydate.setTime(date);
                            String backDate = Character.toUpperCase(formatter.format(mydate.getTime()).charAt(0)) + formatter.format(mydate.getTime()).substring(1);
                            if(((LinearLayout)finalView.findViewById(R.id.backWeekContainer)).getVisibility() == 8)
                                finalView.findViewById(R.id.backWeekContainer).setVisibility(View.VISIBLE);

                            //							from_date.setText((lastEvent != null)?backDate:"");
                            //							to_date.setText((lastEvent != null)?backDate:"");
                            //to_date.setVisibility(View.GONE);
                            from_date.setText(getCurrentWeek(mydate));
                        }
                        else{
                            finalView.findViewById(R.id.backWeekContainer).setVisibility(View.GONE);
                        }

                        SimpleDateFormat formatter_ = new SimpleDateFormat("MMMM yyyy");

                        try {
                            date = new SimpleDateFormat("yyyy-MM-dd").parse(event.getDate());
                        } catch (ParseException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        mydate.setTime(date);
                        String mDate = Character.toUpperCase(formatter_.format(mydate.getTime()).charAt(0)) + formatter_.format(mydate.getTime()).substring(1);
                        week_date.setText(getCurrentWeek(mydate).replace("\n", " "));

                        if(position < mapWeekly.size() - 1) {
                            Event nextEvent = mapWeekly.get(childPagesWeekKeys.get(position + 1)).get(0);
                            try {
                                date = new SimpleDateFormat("yyyy-MM-dd").parse(nextEvent.getDate());
                            } catch (ParseException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            mydate.setTime(date);
                            String prevDate = Character.toUpperCase(formatter.format(mydate.getTime()).charAt(0)) + formatter.format(mydate.getTime()).substring(1);
                            if(((LinearLayout)finalView.findViewById(R.id.previousWeekContainer)).getVisibility() == 8)
                                finalView.findViewById(R.id.previousWeekContainer).setVisibility(View.VISIBLE);
                            //							prev_from_date.setText((nextEvent != null)?prevDate:"");
                            //							prev_to_date.setText((nextEvent != null)?prevDate:"");
                            //prev_to_date.setVisibility(View.GONE);
                            prev_from_date.setText(getCurrentWeek(mydate));
                        }
                        else{
                            finalView.findViewById(R.id.previousWeekContainer).setVisibility(View.GONE);
                        }
                    }

                }

                @Override
                public void onPageScrolled(int arg0, float arg1, int arg2) {}

                @Override
                public void onPageScrollStateChanged(int arg0) {
                    CalendarWeekPageFragment frag1= (CalendarWeekPageFragment)mWeeklyPager.getAdapter().instantiateItem(mWeeklyPager, mWeeklyPager.getCurrentItem());
                    frag1.updateAdapter(agendaGroups);
                }
            });



        }

//		RoundRectShape rectM = new RoundRectShape(
//		new float[] {5, 5, 0, 0, 0, 0, 5, 5},
//		null,
//		null);

        if(isTablet){

            RoundRectShape rect = new RoundRectShape(
                    new float[] {5, 5, 5, 5, 5 , 5, 5, 5},
                    //new float[] {10, 10, 10, 10, 10, 10, 10, 10},
                    null,
                    null);

            ShapeDrawable border = new ShapeDrawable(rect);
            //border.getPaint().setColor(colors.getColor(colors.getTitle_color()));
            //ShapeDrawable border = new ShapeDrawable(new RectShape());
            border.getPaint().setStyle(Style.STROKE);
            border.getPaint().setStrokeWidth(5);
            border.getPaint().setColor(colors.getColor(colors.getBody_color(), "AA"));
            view.findViewById(R.id.monthlyWeeklyBtn).setPadding(2, 2, 2, 2);
            view.findViewById(R.id.monthlyWeeklyBtn).setBackgroundDrawable(border);
        }else{
            view.findViewById(R.id.monthlyWeeklyBtn).setVisibility(View.GONE);
        }
    }

    /* (non-Javadoc)
     * @see com.paperpad.mybox.fragments.BasePagerFragment#onActivityCreated(android.os.Bundle)
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //mPager.setCurrentItem(indexPage);
        if (runPager != null) handler.post(runPager);
        mCreated = true;
    }


    private Event getNextEvent(Calendar myCalendar) {
        Event event = null;
        try {
            Calendar mydate = new GregorianCalendar();
            SimpleDateFormat formatter = new SimpleDateFormat("MMMM");

            System.out.println("Current time => " + mydate.getTime());

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
            String actualDate = df.format(mydate.getTime());

            if(myCalendar != null){
                mydate = myCalendar;
                actualDate = df.format(mydate.getTime());
            }

            String nextEventDate;


            for(int i = 0; i < events.size(); i++) {

                Date date = new SimpleDateFormat("yyyy-MM").parse(events.get(i).getDate());
                mydate.setTime(date);
                nextEventDate = df.format(mydate.getTime()) ;
                if( nextEventDate.compareTo(actualDate) >= 0) {
                    event = events.get(i);
                    int month = mydate.get(Calendar.MONTH);

                    if(month > 0)
                        mydate.set(Calendar.MONTH, month - 1);
                    else
                        mydate.set(Calendar.MONTH, 11);

                    String mBackDate = Character.toUpperCase(formatter.format(mydate.getTime()).charAt(0)) + formatter.format(mydate.getTime()).substring(1);
                    backMonthName = mBackDate;

                    if(month < 11)
                        mydate.set(Calendar.MONTH, month + 1);
                    else
                        mydate.set(Calendar.MONTH, 0);
                    String mPrevDate = Character.toUpperCase(formatter.format(mydate.getTime()).charAt(0)) + formatter.format(mydate.getTime()).substring(1);
                    previousMonthName = mPrevDate;
//					if(i > 0) {
//						Event lastEvent = events.get(i - 1);
//						date = new SimpleDateFormat("yyyy-MM").parse(lastEvent.getDate());
//						mydate.setTime(date);
//						String mDate = Character.toUpperCase(formatter.format(mydate.getTime()).charAt(0)) + formatter.format(mydate.getTime()).substring(1);
//						backMonthName = mDate;
//						//backMonthName = (lastEvent != null)?formatter.format(mydate.getTime()) : "";
//					}else {
//
//						date = new SimpleDateFormat("yyyy-MM").parse(event.getDate());
//						mydate.setTime(date);
//						String mDate = Character.toUpperCase(formatter.format(mydate.getTime()).charAt(0)) + formatter.format(mydate.getTime()).substring(1);
//						backMonthName = mDate;
//					}
//					if(i < events.size() - 1) {
//						Event nextEvent = events.get(i + 1);
//						date = new SimpleDateFormat("yyyy-MM").parse(nextEvent.getDate());
//						mydate.setTime(date);
//						String mDate = Character.toUpperCase(formatter.format(mydate.getTime()).charAt(0)) + formatter.format(mydate.getTime()).substring(1);
//						previousMonthName = mDate;
//						//previousMonthName = (nextEvent != null)?formatter.format(mydate.getTime()) : "";
//					}else{
//						date = new SimpleDateFormat("yyyy-MM").parse(event.getDate());
//						mydate.setTime(date);
//						String mDate = Character.toUpperCase(formatter.format(mydate.getTime()).charAt(0)) + formatter.format(mydate.getTime()).substring(1);
//						previousMonthName = "";// mDate;
//
//					}
                    indexPage = getPageIndexEvent(event);
                    realm.beginTransaction();
                    event.setSelected(true);
                    realm.commitTransaction();
                    return event;
                }

            }

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        backMonthName = "";
        if(childPagesKeys.size() >= 2){
            monthDateName = childPagesKeys.get(0);
            previousMonthName = childPagesKeys.get(1);
        }
        else{
            monthDateName = "";
            previousMonthName = "";
        }

        return null;
    }

    private Event getNextWeeklyEvent(Calendar myCalendar) {
        Event event = null;
        try {
            Calendar mydate = new GregorianCalendar();

            //System.out.println("Current time => " + mydate.getTime());

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String actualDate = df.format(mydate.getTime());
            //String actualWeek = getCurrentWeek(mydate);

            if(myCalendar != null){
                mydate = myCalendar;
                actualDate = df.format(mydate.getTime());
                //event = getWeeklyEvent(myCalendar);
                //return event;
            }

            String nextEventDate;


            for(int i = 0; i < events.size(); i++) {

                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(events.get(i).getDate());
                mydate.setTime(date);
                nextEventDate = df.format(mydate.getTime()) ;
                if( nextEventDate.compareTo(actualDate) >= 0) {
                    event = events.get(i);
                    if(i > 0) {
                        Event lastEvent = events.get(i - 1);
                        date = new SimpleDateFormat("yyyy-MM-dd").parse(lastEvent.getDate());
                        mydate.setTime(date);
                        backWeekName = getCurrentWeek(mydate);
                    }else {

                        date = new SimpleDateFormat("yyyy-MM-dd").parse(event.getDate());
                        mydate.setTime(date);
                        backWeekName = getCurrentWeek(mydate);
                    }
                    if(i < events.size() - 1) {
                        Event nextEvent = events.get(i + 1);
                        date = new SimpleDateFormat("yyyy-MM-dd").parse(nextEvent.getDate());
                        mydate.setTime(date);
                        //if(!actualWeek.equals(getCurrentWeek(mydate))){
                        previousWeekName = getCurrentWeek(mydate);
//							indexPageWeek = getWeekPageIndexEvent(event);
//							event.setSelected(true);
//							return event;
                        //}

                        //previousMonthName = (nextEvent != null)?formatter.format(mydate.getTime()) : "";
                    }else{
                        date = new SimpleDateFormat("yyyy-MM-dd").parse(event.getDate());
                        mydate.setTime(date);
                        //						String mDate = Character.toUpperCase(formatter.format(mydate.getTime()).charAt(0)) + formatter.format(mydate.getTime()).substring(1);
                        //						previousWeekName = mDate;
                        previousWeekName = getCurrentWeek(mydate);
//						indexPageWeek = getWeekPageIndexEvent(event);
//						event.setSelected(true);
//						return event;
                    }
                    indexPageWeek = getWeekPageIndexEvent(event);
                    event.setSelected(true);
                    return event;
                }

            }

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        backWeekName = "";
        if(childPagesWeekKeys.size() >= 2){
            weekDateName = childPagesWeekKeys.get(0);
            previousWeekName = childPagesWeekKeys.get(1);
        }
        else{
            weekDateName = "";
            previousWeekName = "";
        }

        return null;
    }

    private int getPageIndexEvent(Event event) {
        //int pageIndexEvent = 0;

        Calendar mydate = new GregorianCalendar();
        SimpleDateFormat formatter = new SimpleDateFormat("MMMM yyyy");
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM").parse(event.getDate());
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        mydate.setTime(date);
        String eventDate = formatter.format(mydate.getTime()) ;


        for(int i = 0; i < childPagesKeys.size(); i++) {

            if(childPagesKeys.get(i).compareTo(eventDate) == 0)return i;
        }

        return 0;
    }


    private int getWeekPageIndexEvent(Event event) {
        //int pageIndexEvent = 0;

        Calendar mydate = new GregorianCalendar();
        //		SimpleDateFormat formatter = new SimpleDateFormat("MMMM yyyy");

        //		Date date = new SimpleDateFormat("yyyy-MM-dd").parse(event.getDate());
        //		mydate.setTime(date);

        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(event.getDate());
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        mydate.setTime(date);
        //			String eventDate = formatter.format(mydate.getTime()) ;
        //String dateTag = ""+mydate.get(Calendar.WEEK_OF_MONTH) +(mydate.get(Calendar.MONTH) + 1) +mydate.get(Calendar.YEAR)+"";


        for(int i = 0; i < childPagesWeekKeys.size(); i++){
            if(childPagesWeekKeys.get(i).compareTo(getCurrentWeek(mydate)) == 0)return i;
        }

        return 0;
    }

    private List<AgendaGroup> getAvailableAgendaGroup(){
        List<AgendaGroup> agendaGrps = new ArrayList<AgendaGroup>();
        List<AgendaGroup> agendaGroups = realm.where(AgendaGroup.class).findAll(); //appController.getAgendaGroupDao().queryForAll();
        for(int j = 0; j < agendaGroups.size(); j++) {
            for(int i = 0; i < events.size(); i++) {

                if(events.get(i).getAgenda_group_id() == agendaGroups.get(j).getId()) {
                    agendaGrps.add(agendaGroups.get(j));
                    break;
                }
            }
        }

        return agendaGrps;
    }


    /**
     * Set the VerticalViewPager adapter from this or from a subclass.
     *
     * @author chris.jenkins
     * @param mAdapter2 This is a FragmentStatePagerAdapter due to the way it creates the TAG for the
     * Fragment.
     */
    protected void setAdapter(CalendarPagerAdapter mAdapter2)
    {
        mAdapter = mAdapter2;
        runPager = new Runnable() {

            @Override
            public void run()
            {
                mPager.setAdapter(mAdapter);
            }
        };
        if (mCreated)
        {
            handler.post(runPager);
        }
    }

    public class CalendarPagerAdapter extends FragmentStatePagerAdapter {


        public CalendarPagerAdapter(FragmentManager fm) {
            super(fm);
            //calendarFragments = new ArrayList<CalendarMonthFragment>();
        }


        @Override
        public Fragment getItem(int position) {

            CalendarMonthFragment calendarFragment = CalendarMonthFragment.create(mapMonthly.get(childPagesKeys.get(position)), agendaGroups);
            calendarFragment.setCustomTag(""+position);
            return calendarFragment;
        }

        @Override
        public int getCount() {
            return childPagesKeys.size();
        }

        //		@Override
        //		public Object instantiateItem(ViewGroup arg0, int arg1) {
        //			//mPager.setCurrentItem(indexPage);
        //			return super.instantiateItem(arg0, arg1);
        //		}



    }


    public class CalendarWeekPagerAdapter extends FragmentStatePagerAdapter {


        public CalendarWeekPagerAdapter(FragmentManager fm) {
            super(fm);
            //calendarFragments = new ArrayList<CalendarMonthFragment>();
        }


        @Override
        public Fragment getItem(int position) {

            CalendarWeekPageFragment calendarFragment = CalendarWeekPageFragment.create(mapWeekly.get(childPagesWeekKeys.get(position)), childPagesWeekKeys.get(position), agendaGroups);
            calendarFragment.setCustomTag(""+position);
            return calendarFragment;
        }

        @Override
        public int getCount() {
            return childPagesWeekKeys.size();

        }

        //		@Override
        //		public Object instantiateItem(ViewGroup arg0, int arg1) {
        //			//mPager.setCurrentItem(indexPage);
        //			return super.instantiateItem(arg0, arg1);
        //		}



    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runPager);
    }


    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onDestroy()
     */
    @Override
    public void onDestroy() {
        if(mapMonthly != null)
            mapMonthly = null;
        super.onDestroy();
    }

    /** a method to fill the upper bar where we choose the {@link CategoriesMyBox}
     * @param category
     */
    private void fillNavigationBar(AgendaGroup agendaGroup) {
        LinearLayout container = new LinearLayout(getActivity());
        ImageView img = new ImageView(getActivity());
        TextView categoryTxt = new TextView(getActivity());
        categoryTxt.setTypeface(MainActivity.FONT_BODY, Typeface.BOLD);
        container.setOrientation(LinearLayout.HORIZONTAL);
        if (monthlyWeekly)
            container.setGravity(Gravity.CENTER_VERTICAL|Gravity.RIGHT);
        else
            container.setGravity(Gravity.CENTER);
        //		LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(20, 20);
        //		imgParams.gravity = Gravity.CENTER;
        //		img.setLayoutParams(imgParams);
        LinearLayout.LayoutParams txtParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        //txtParams.gravity = Gravity.CENTER;
        if (agendaGroup.isSelected()) {

            txtParams.setMargins(5, 12, 5, 12);
            categoryTxt.setPadding(2, 2, 2, 2);
            categoryTxt.setGravity(Gravity.CENTER);
            categoryTxt.setText(agendaGroup.getTitle().toUpperCase());
            categoryTxt.setTextColor(colors.getColor(colors.getBackground_color()));
            categoryTxt.setTextSize(16);
            //			Drawable selectDrawable = getResources().getDrawable(R.drawable.back_empty);
            //			selectDrawable.setColorFilter(colors.getColor(agendaGroup.getColor()), Mode.MULTIPLY);
            //			categoryTxt.setBackgroundDrawable(selectDrawable);
            Glide.with(getActivity()).load(new File("android_asset/"+agendaGroup.getIcon())).into(img);
            container.setTag(agendaGroup);
            //img.setPadding(3, 0, 5, 0);
            container.addView(img, 25, 25);
            container.addView(categoryTxt, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            container.setBackgroundColor(colors.getColor(agendaGroup.getColor()));
            container.setPadding(5, 0, 5, 0);
            choiceHolder.addView(container, txtParams);
        }else {

            txtParams.setMargins(5, 12, 5, 12);
            categoryTxt.setPadding(2, 2, 2, 2);
            categoryTxt.setGravity(Gravity.CENTER);
            categoryTxt.setText(agendaGroup.getTitle().toUpperCase());
            categoryTxt.setTextColor(colors.getColor(agendaGroup.getColor()));
            categoryTxt.setTextSize(16);
            Bitmap bm = null;
            try {
                bm = BitmapFactory.decodeStream(getActivity().getAssets().open(agendaGroup.getIcon()));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Drawable drawable = new BitmapDrawable(bm);//(new File("android_asset/"+agendaGroup.getIcon()).getAbsolutePath());
            drawable.setColorFilter(new PorterDuffColorFilter(colors.getColor(agendaGroup.getColor()),PorterDuff.Mode.MULTIPLY));
            img.setBackgroundDrawable(drawable);
            //			Drawable selectDrawable = getResources().getDrawable(R.drawable.back_choices_final);
            //			selectDrawable.setColorFilter(colors.getColor(agendaGroup.getColor()), Mode.MULTIPLY);
            //			categoryTxt.setBackgroundColor(Color.TRANSPARENT);//Drawable(selectDrawable);
            container.setTag(agendaGroup);
            img.setPadding(3, 0, 5, 0);
            container.addView(img, 25, 25);
            container.addView(categoryTxt, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            container.setBackgroundColor(Color.TRANSPARENT);
            container.setPadding(5, 0, 5, 0);
//			RoundRectShape rect = new RoundRectShape(
//					new float[] {5, 5, 5, 5, 5 , 5, 5, 5},
//					//new float[] {10, 10, 10, 10, 10, 10, 10, 10},
//					null,
//					null);
//
//					ShapeDrawable border = new ShapeDrawable(rect);
            //border.getPaint().setColor(colors.getColor(colors.getTitle_color()));
            ShapeDrawable border = new ShapeDrawable(new RectShape());
            border.getPaint().setStyle(Style.STROKE);
            border.getPaint().setStrokeWidth(3);
            border.getPaint().setColor(colors.getColor(agendaGroup.getColor()));
            //container.setPadding(2, 2, 2, 2);
            container.setBackgroundDrawable(border);
            choiceHolder.addView(container, txtParams);


        }

        container.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                AgendaGroup agendaGroup = (AgendaGroup)v.getTag();

                //agendaGroup.setSelected(false);
                choiceHolder.removeAllViews();
                for (int i = 0; i < agendaGroups.size(); i++) {
                    if(agendaGroups.get(i).getId() == agendaGroup.getId())
                        agendaGroups.get(i).setSelected(agendaGroups.get(i).isSelected()?false:true);
                    fillNavigationBar(agendaGroups.get(i));
                }

                if(monthlyWeekly){
                    CalendarWeekPageFragment frag1= (CalendarWeekPageFragment)mWeeklyPager.getAdapter().instantiateItem(mWeeklyPager, mWeeklyPager.getCurrentItem());
                    frag1.updateAdapter(agendaGroups);

                }else{
                    CalendarMonthFragment frag1= (CalendarMonthFragment)mPager.getAdapter().instantiateItem(mPager, mPager.getCurrentItem());
                    frag1.updateAdapter(agendaGroups);
                }


            }
        });

    }

    public HashMap<String, List<Event>> getMapEventsWeekly(List<Event> events){
        HashMap<String,  List<Event>> map = new HashMap<String, List<Event>>();


        String tmp, tmp2 = null, comparTag = "", dateTag = "";
        int j,som = 0;

        Event event = null;

        j = events.size() - 1;

        if(j > 0) {
           /* quicksort(events, 0, j);*/
        }


        int year = 0;
        int month = 0;
        int day = 0;
        int week = 0;

        List<String> list = new ArrayList<String>();

        //		pages = new ArrayList<Child_pages>();
        dayKeys = new ArrayList<String>();
        childPagesWeekKeys = new ArrayList<String>();

        List<Event> listEvent = null; // = new ArrayList<Event>();
        Calendar mydate = new GregorianCalendar();
        Calendar mTmpCalendar = null;
        boolean end = false;
        boolean anotherDate = false;
        j = 0;
        while(!end) {
            listEvent = new ArrayList<Event>();

            for(int i = j ; i<events.size(); i++) {
                event = events.get(i);

                try {

                    Date date = new SimpleDateFormat("yyyy-MM-dd").parse(event.getDate());
                    mydate.setTime(date);
                    dateTag = ""+mydate.get(Calendar.WEEK_OF_MONTH) +" "+(mydate.get(Calendar.MONTH) + 1) +mydate.get(Calendar.YEAR)+"";

                    // if( comparTag.compareTo(dateTag) != 0)


                    if(!dateTag.isEmpty() && !isAlreadyChecked(list, dateTag)) {
                        if(anotherDate) break;
                        mTmpCalendar = (Calendar) mydate.clone();
                        //					if(isLooperFinished) {
                        list.add(dateTag);
                        listEvent.add(event);
                        comparTag = dateTag; //list.get(j);
                        year = mydate.get(Calendar.YEAR); //date.getYear();
                        month = mydate.get(Calendar.MONTH); //date.getMonth();
                        day = mydate.get(Calendar.DAY_OF_MONTH) + 1; //date.getDay();
                        week  = mydate.get(Calendar.WEEK_OF_MONTH);
                        dayKeys.add(""+day);
                        //					}
                        anotherDate = true;
                        //isLooperFinished = false;
                        j++;
                    }else if(comparTag.compareTo(dateTag) == 0){

                        listEvent.add(event);
                        dayKeys.add(""+day);
                        j++;
                    }



                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            anotherDate = false;
            //
            //			SharedPreferences wmbPreference = PreferenceManager
            //					.getDefaultSharedPreferences(getActivity());
            //
            //			String lang = wmbPreference.getString(Utils.LANG, "fr");
            //			if(lang.compareTo("en") == 0) {
            //				tmp = week + " " + DateFormatSymbols.getInstance(Locale.ENGLISH).getMonths()[month]+" "+year;
            //				//tmp2 = DateFormatSymbols.getInstance(Locale.ENGLISH).getMonths()[month + 1]+" "+year;
            //			}
            //			else {
            //				tmp = week + " " + DateFormatSymbols.getInstance(Locale.FRENCH).getMonths()[month]+" "+year;
            //				//tmp2 = DateFormatSymbols.getInstance(Locale.ENGLISH).getMonths()[month + 1]+" "+year;
            //			}

            tmp = getCurrentWeek(mTmpCalendar);

            if(!map.keySet().contains(tmp)) {
                map.put(tmp, listEvent);
                childPagesWeekKeys.add(tmp);
                //				for(int i = 0; i < childPagesWeekKeys.size(); i++) {
                //					if(childPagesWeekKeys.get(i).compareTo(tmp2) < 0) {
                //
                //					}
                //				}
            }


            som += listEvent.size();

            if(/*som >= events.size() pr resoudre som =14 et events=15 */ som ==events.size()-1) {
                end = true;
            }

        }

        return map;

    }

    public HashMap<String, List<Event>> getMapEventsMonthly(List<Event> events){
        HashMap<String,  List<Event>> map = new HashMap<String, List<Event>>();
//       realm.beginTransaction();

        String tmp, tmp2 = null, comparTag = "", dateTag = "";
        int j,som = 0;

        Event event = null;

        j = events.size() - 1;

        if(j > 0) {
            /*quicksort(events, 0, j);*//* à modifier*/
        }


        int year = 0;
        int month = 0;
        int day = 0;

        List<String> list = new ArrayList<String>();

        //		pages = new ArrayList<Child_pages>();
        dayKeys = new ArrayList<String>();
        childPagesKeys= new ArrayList<String>();

        List<Event> listEvent = null; // = new ArrayList<Event>();
        Calendar mydate = new GregorianCalendar();
        boolean end = false;
        boolean anotherDate = false;
        j = 0;
        while(!end) {
            listEvent = new ArrayList<Event>();

            for(int i = j ; i<events.size(); i++) {
                event = events.get(i);

                try {

                    Date date = new SimpleDateFormat("yyyy-MM-dd").parse(event.getDate());
                    mydate.setTime(date);
                    dateTag = ""+(mydate.get(Calendar.MONTH) + 1)+mydate.get(Calendar.YEAR)+"";

                    // if( comparTag.compareTo(dateTag) != 0)


                    if(!dateTag.isEmpty() && !isAlreadyChecked(list, dateTag)) {
                        if(anotherDate) break;
                        //					if(isLooperFinished) {
                        list.add(dateTag);
                        listEvent.add(event);
                        comparTag = dateTag; //list.get(j);
                        year = mydate.get(Calendar.YEAR); //date.getYear();
                        month = mydate.get(Calendar.MONTH); //date.getMonth();
                        day = mydate.get(Calendar.DAY_OF_MONTH) + 1; //date.getDay();
                        dayKeys.add(""+day);
                        //					}
                        anotherDate = true;
                        //isLooperFinished = false;
                        j++;
                    }else if(comparTag.compareTo(dateTag) == 0){

                        listEvent.add(event);
                        dayKeys.add(""+day);
                        j++;
                    }



                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            anotherDate = false;

            SharedPreferences wmbPreference = PreferenceManager
                    .getDefaultSharedPreferences(getActivity());

            String lang = wmbPreference.getString(Utils.LANG, "fr");
            if(lang.compareTo("en") == 0) {
                tmp = DateFormatSymbols.getInstance(Locale.ENGLISH).getMonths()[month]+" "+year;

                //tmp2 = DateFormatSymbols.getInstance(Locale.ENGLISH).getMonths()[month + 1]+" "+year;
            }
            else {
                tmp = DateFormatSymbols.getInstance(Locale.FRENCH).getMonths()[month]+" "+year;
                //tmp2 = DateFormatSymbols.getInstance(Locale.ENGLISH).getMonths()[month + 1]+" "+year;
            }

            if(!map.keySet().contains(tmp)) {
                map.put(tmp, listEvent);
                childPagesKeys.add(tmp);
                //				for(int i = 0; i < childPagesKeys.size(); i++) {
                //					if(childPagesKeys.get(i).compareTo(tmp2) < 0) {
                //
                //					}
                //				}
            }


            som += listEvent.size();

            if(/*som >= events.size() pr resoudre som =14 et events=15 */ som==events.size()-1) {
                end = true;
            }

        }
        realm.commitTransaction();

        return map;

    }



    public boolean isAlreadyChecked(List<String> list, String value) {
        if(list.size() == 0)return false;

        for(int i = 0; i< list.size(); i++) {
            if(list.get(i).compareTo(value) == 0)
                return true;
        }

        return false;
    }


    public void fastTri(List<Event> events, int pivotIndex, int lastIndex){

        int j = 0;

        if(pivotIndex < lastIndex)
        {
            j = partition(events, pivotIndex, lastIndex);
            fastTri(events, pivotIndex, j);
            fastTri(events, j+1 , lastIndex);
        }

    }



    public static void quicksort(List<Event> events, int debut, int fin) {
        if (debut < fin) {
            int indicePivot = partition(events, debut, fin);
            quicksort(events, debut, indicePivot-1);
            quicksort(events, indicePivot+1, fin);
        }
    }

    public static int partition (List<Event> events, int debut, int fin) {
        Event pivotEvent = events.get(debut);
        String valeurPivot = events.get(debut).getDate(); //t[d�but];
        int d = debut+1;
        int f = fin;

        while (d < f) {
            while(d < f && events.get(f).getDate().compareTo(valeurPivot) >= 0) f--;
            while(d < f && events.get(d).getDate().compareTo(valeurPivot) <= 0) d++;

            Event tmp = events.get(d);
            events.add(d, events.get(f));
            events.add(f, tmp);

                     }
        if (events.get(d).getDate().compareTo(valeurPivot) >= 0) d--;
        events.add(debut, events.get(d));
        events.add(d, pivotEvent);
        return d;
    }

    private long time;

    @Override
    public void onStop() {

        AppHit hit = new AppHit(System.currentTimeMillis()/1000, time/1000, "events_section", 0);
        ((MyApplication)getActivity().getApplication()).hits.add(hit);
        super.onStop();
    }

    public static String getLastWeek(Calendar mCalendar) {
        // Monday
        mCalendar.add(Calendar.DAY_OF_YEAR, -13);
        Date mDateMonday = mCalendar.getTime();

        // Sunday
        mCalendar.add(Calendar.DAY_OF_YEAR, 6);
        Date mDateSunday = mCalendar.getTime();

        // Date format
        String strDateFormat = "dd MMM";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);

        String MONDAY = sdf.format(mDateMonday);
        String SUNDAY = sdf.format(mDateSunday);

        // Substring
        if ((MONDAY.substring(3, 6)).equals(SUNDAY.substring(3, 6))) {
            MONDAY = MONDAY.substring(0, 2);
        }

        return MONDAY + " - " + SUNDAY;
    }

    public String getPreviousWeek(Calendar mCalendar) {
        // Monday
        mCalendar.add(Calendar.DAY_OF_YEAR, 1);
        Date mDateMonday = mCalendar.getTime();

        // Sunday
        mCalendar.add(Calendar.DAY_OF_YEAR, 6);
        Date Week_Sunday_Date = mCalendar.getTime();

        // Date format
        String strDateFormat = "dd MMM";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);

        String MONDAY = sdf.format(mDateMonday);
        String SUNDAY = sdf.format(Week_Sunday_Date);

        // Sub string
        if ((MONDAY.substring(3, 6)).equals(SUNDAY.substring(3, 6))) {
            MONDAY = MONDAY.substring(0, 2);
        }

        return getString(R.string.start_date)+" "+MONDAY + " " + getString(R.string.end_date) + " " + SUNDAY;
    }

    public String getCurrentWeek(Calendar mCalendar) {
        //        Date date = new Date();
        //        mCalendar.setTime(date);

        // 1 = Sunday, 2 = Monday, etc.
        int day_of_week = mCalendar.get(Calendar.DAY_OF_WEEK);

        int monday_offset;
        if (day_of_week == 1) {
            monday_offset = -6;
        } else
            monday_offset = (2 - day_of_week); // need to minus back
        mCalendar.add(Calendar.DAY_OF_YEAR, monday_offset);

        Date mDateMonday = mCalendar.getTime();

        // return 6 the next days of current day (object cal save current day)
        mCalendar.add(Calendar.DAY_OF_YEAR, 6);
        Date mDateSunday = mCalendar.getTime();

        //Get format date
        String strDateFormat = "dd MMMM";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);

        String MONDAY = sdf.format(mDateMonday);
        String SUNDAY = sdf.format(mDateSunday);

        // Sub String
        if ((MONDAY.substring(3, 6)).equals(SUNDAY.substring(3, 6))) {
            MONDAY = MONDAY.substring(0, 2);
        }

        return getString(R.string.start_date)+" "+MONDAY + "\n" + getString(R.string.end_date) + " " + SUNDAY;

        //        mCalendar.add(Calendar.DAY_OF_YEAR, 2);
        //        Date mDateMonday = mCalendar.getTime();
        //
        //        // Sunday
        //        mCalendar.add(Calendar.DAY_OF_YEAR, 6);
        //        Date Week_Sunday_Date = mCalendar.getTime();
        //
        //        // Date format
        //        String strDateFormat = "dd MMM";
        //        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
        //
        //        String MONDAY = sdf.format(mDateMonday);
        //        String SUNDAY = sdf.format(Week_Sunday_Date);
        //
        //        // Sub string
        //        if ((MONDAY.substring(3, 6)).equals(SUNDAY.substring(3, 6))) {
        //            MONDAY = MONDAY.substring(0, 2);
        //        }
        //
        //        return getString(R.string.start_date)+" "+MONDAY + "\n" + getString(R.string.end_date) + " " + SUNDAY;
    }

    public String getNextWeek(Calendar mCalendar) {

        //mCalendar.add(Calendar.WEEK_OF_MONTH, 1);
        // Monday
        Calendar calendar = (Calendar) mCalendar.clone();
        mCalendar.add(Calendar.DAY_OF_YEAR, 2);
        Date mDateMonday = mCalendar.getTime();

        // Sunday
        mCalendar.add(Calendar.DAY_OF_YEAR, 6);
        Date Week_Sunday_Date = mCalendar.getTime();

        // Date format
        String strDateFormat = "dd MMM";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);

        String MONDAY = sdf.format(mDateMonday);
        String SUNDAY = sdf.format(Week_Sunday_Date);

        // Sub string
        if ((MONDAY.substring(3, 6)).equals(SUNDAY.substring(3, 6))) {
            MONDAY = MONDAY.substring(0, 2);
        }

        return getString(R.string.start_date)+" "+MONDAY + "\n" + getString(R.string.end_date) + " " + SUNDAY;
    }

    public static ShapeDrawable createUserDrawable(String color, int leftBounds, int topBounds, int rightBound, int bottomBounds) {
        ShapeDrawable drawable = new ShapeDrawable();
        drawable.setBounds(leftBounds, topBounds, rightBound, bottomBounds);
        float radius = 4;
        float[] radii = new float[] {radius, radius, radius, radius, radius, radius, radius, radius};
        Shape shape = new RoundRectShape(radii, new RectF(), radii);
        drawable.setShape(shape);
        Paint paint = drawable.getPaint();
        paint.setColor(Color.parseColor("#"+color));
        return drawable;
    }

}
