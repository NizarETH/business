package com.euphor.paperpad.activities.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.euphor.paperpad.Beans.Parameters;
import com.euphor.paperpad.InterfaceRealm.CommunElements1;
import com.euphor.paperpad.R;
import com.euphor.paperpad.activities.main.MainActivity;
import com.euphor.paperpad.activities.main.MyApplication;
import com.euphor.paperpad.adapters.CommunElements;
import com.euphor.paperpad.adapters.DesignCategoryFlowAdapter;
import com.euphor.paperpad.Beans.Category;
import com.euphor.paperpad.Beans.Child_pages;
import com.euphor.paperpad.utils.Colors;
import com.euphor.paperpad.widgets.CoverFlow;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import at.technikum.mti.fancycoverflow.FancyCoverFlow;
import io.realm.Realm;
import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;


/**
 * Created by uness on 22/07/15.
 */
public class DesignCategoryFlowFragment extends Fragment {

    private Colors colors;
    private Category category;
    private Realm realm;

    public static DesignCategoryFlowFragment create(Category category) {
        DesignCategoryFlowFragment f = new DesignCategoryFlowFragment();
        f.setCategory(category);
        return f;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);


        		realm = Realm.getInstance(getActivity());
        colors = ((MainActivity)activity).colors;
        Parameters ParamColor = realm.where(Parameters.class).findFirst();
        if (colors==null) {
            colors = new Colors(ParamColor);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.design_category_flow, container, false);
        //view.setBackgroundColor(colors.getColor(colors.getTitle_color(), "AA"));
        view.setBackgroundColor(colors.getBackMixColor(colors.getBackground_color(), 0.10f));
        List<CommunElements1> elements = null;
        if(category !=null) {
            if (category.getCategories().isEmpty()) {
                elements = new ArrayList<CommunElements1>(category.getChildren_pages());
                if (category.getChildren_pages().isEmpty())
                    elements = new ArrayList<CommunElements1>(category.getChildren_categories());

            } else {
                elements = new ArrayList<CommunElements1>(category.getCategories());

            }
        }

        final CoverFlow coverFlow = (CoverFlow)view.findViewById(R.id.coverFlow);

        final TextView titleFlow = (TextView)view.findViewById(R.id.titleFlow);


        //coverFlow.setSpacing(-25);
        //int section = elements.size() / 2;
        //coverFlow.setSelection(section, true);

        //coverFlow.setSelection(0, true);

        //coverFlow.setSpacing(-90);
        //coverFlow.offsetLeftAndRight(0);
        //coverFlow.setElevation(50f);
        //coverFlow.setUnselectedAlpha(1.0f);

       /* coverFlow.setMaxRotationAngle(-40);

        coverFlow.setMaxZoom(-80);*/
        coverFlow.setMaxRotationAngle(15);
        //coverFlow.setSpacing(0);
        coverFlow.setMaxZoom(-80);
        coverFlow.setGravity(Gravity.CENTER);

        coverFlow.setAnimationDuration(500);
        final DesignCategoryFlowAdapter adapter = new DesignCategoryFlowAdapter(getActivity(), elements, titleFlow, colors.getColor(colors.getTitle_color()));
        coverFlow.setAdapter(adapter);


        coverFlow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                //Toast.makeText(getActivity(), "Item clicked! : " + id, Toast.LENGTH_LONG).show();
                CommunElements1 element = adapter.getItem(position);

                if (element != null) {
                    if (element instanceof Category) {
                        Category category = (Category) element;
                        ((MainActivity) getActivity()).openCategory(category);
                    } else if (element instanceof Child_pages) {
                        Child_pages page = (Child_pages) element;
                        ((MainActivity) getActivity()).openChildPage(page, true);
                    }
                }
            }

        });

        /*coverFlow.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                adapter.notifyDataSetChanged();
                return false;
            }
        });*/


        /*FeatureCoverFlow mCoverFlow = (FeatureCoverFlow) view.findViewById(R.id.coverflow);
        //mCoverFlow.setMaxScaleFactor(1.5f);
        //mCoverFlow.setReflectionGap(0);
        //mCoverFlow.setRotationTreshold(0.5f);
        //mCoverFlow.setScalingThreshold(0.5f);
        //mCoverFlow.setShouldRepeat(false);
        mCoverFlow.setAdapter(adapter);

        mCoverFlow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CommunElements1 element = adapter.getItem(position);

                if (element != null) {
                    if (element instanceof Category) {
                        Category category = (Category) element;
                        ((MainActivity) getActivity()).openCategory(category);
                    } else if (element instanceof Child_pages) {
                        Child_pages page = (Child_pages) element;
                        ((MainActivity) getActivity()).openChildPage(page, true);
                    }
                }
            }
        });

        mCoverFlow.setOnScrollPositionListener(new FeatureCoverFlow.OnScrollPositionListener() {
            @Override
            public void onScrolledToPosition(int position) {
                //TODO CoverFlow stopped to position
                CommunElements1 element = adapter.getItem(position);
                titleFlow.setText(""+element.getCommunTitle1());
            }

            @Override
            public void onScrolling() {
                //TODO CoverFlow began scrolling
            }
        });*/


        /*FeatureCoverFlow mCoverFlow = (FeatureCoverFlow) view.findViewById(R.id.coverflow);
        final DesignCategoryFlowAdapter adapter = new DesignCategoryFlowAdapter(getActivity(), elements, colors.getColor(colors.getBackground_color()));
        mCoverFlow.setAdapter(adapter);

        mCoverFlow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO CoverFlow item clicked
                CommunElements element = adapter.getItem(position);

                if(element != null) {
                    if(element instanceof Category){
                        Category category = (Category)element;
                        ((MainActivity)getActivity()).openCategory(category);
                    }else if(element instanceof Child_pages){
                        Child_pages page = (Child_pages)element;
                        ((MainActivity)getActivity()).openChildPage(page, true);
                    }
                }
            }
        });

        mCoverFlow.setOnScrollPositionListener(new FeatureCoverFlow.OnScrollPositionListener() {
            @Override
            public void onScrolledToPosition(int position) {
                //TODO CoverFlow stopped to position
            }

            @Override
            public void onScrolling() {
                //TODO CoverFlow began scrolling
            }
        });*/

        return view;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}