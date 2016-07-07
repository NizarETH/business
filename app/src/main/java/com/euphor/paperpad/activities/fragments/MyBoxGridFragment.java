/**
 * 
 */
package com.euphor.paperpad.activities.fragments;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.euphor.paperpad.Beans.Parameters;
import com.euphor.paperpad.R;
import com.euphor.paperpad.activities.main.MainActivity;
import com.euphor.paperpad.activities.main.MyApplication;
import com.euphor.paperpad.adapters.MyBoxCategoriesAdapter;
import com.euphor.paperpad.Beans.CategoriesMyBox;
import com.euphor.paperpad.Beans.MyBox;
import com.euphor.paperpad.Beans.Section;

import com.euphor.paperpad.utils.Colors;
import com.euphor.paperpad.utils.Utils;
import com.euphor.paperpad.utils.jsonUtils.AppHit;
import com.euphor.paperpad.widgets.GridFragment;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * @author euphordev02
 *
 */
public class MyBoxGridFragment extends GridFragment {

    private Colors colors;
    private List<MyBox> boxs;
    private LinearLayout choiceHolder;
    private List<CategoriesMyBox> categories;
    private MyBoxCategoriesAdapter albumsAdapter;
    protected int id_cat = 0;
    private long time;
    private Realm realm;

    /**
     *
     */
    public MyBoxGridFragment() {
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see com.euphor.paperpad.widgets.GridFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        realm.beginTransaction();
        View view = inflater.inflate(R.layout.mybox_grid_fragment, container, false);
        view.setBackgroundColor(colors.getColor(colors.getBackground_color()));
        LinearLayout backChoices = (LinearLayout)view.findViewById(R.id.backChoices);
        //		Drawable selectDrawable = getResources().getDrawable(R.drawable.back_empty);
        //		selectDrawable.setColorFilter(colors.getColor(colors.getForeground_color()), Mode.MULTIPLY);
        //		backChoices.setBackgroundDrawable(selectDrawable);
        backChoices.setBackgroundDrawable(colors.getBackPD());
        HorizontalScrollView sVchoicesHolder = (HorizontalScrollView)view.findViewById(R.id.SVchoicesHolder);
        sVchoicesHolder.setBackgroundColor(Color.TRANSPARENT);
        int section_id = getArguments().getInt("Section_id");
        Section section = realm.where(Section.class).equalTo("id",section_id).findFirst(); //appController.getSectionsDao().queryForId(section_id);
        if (section != null) {
            if (section.getTitle().isEmpty()) {
                view.findViewById(R.id.TitleHolder).setBackgroundColor(colors.getColor(colors.getForeground_color()));
            }else {
                view.findViewById(R.id.TitleHolder).setBackgroundColor(colors.getColor(colors.getForeground_color()));
            }

        }

        categories =  realm.where(CategoriesMyBox.class).findAll(); //appController.getCategoriesMyBoxDao().queryForAll();
//			Collections.sort(categories);


        if (categories.size()>0) {

            CategoriesMyBox catMyBox = new CategoriesMyBox(0, true); // pour TOUS LES COFFRETS
            CategoriesMyBox catMyBox1 = realm.copyToRealmOrUpdate(catMyBox);

            categories.get(0).setCategoriesMyBox(catMyBox1);

            realm.where(CategoriesMyBox.class).findAll();
          /*  if (categories.size() > 0) {*/
            if (id_cat >= 0) {
                for (int i = 0; i < categories.size(); i++) {
                    if (categories.get(i).getId_categorie() == id_cat) {
                        categories.get(i).setSelected(true);

                    }else {
                        categories.get(i).setSelected(false);
                    }
                }

            }

            choiceHolder = (LinearLayout)view.findViewById(R.id.choicesHolder);

            choiceHolder.setBackgroundColor(Color.TRANSPARENT);

            categories = realm.where(CategoriesMyBox.class).findAllSorted("id_categorie");

            for (int i = 0; i < categories.size(); i++) {

                fillNavigationBar(categories.get(i));

            }
            List<CategoriesMyBox> cats = realm.where(CategoriesMyBox.class).equalTo("id_categorie", id_cat).findAll(); //appController.getCategoriesMyBoxDao().queryForEq("id_categorie", id_cat);
            if (cats.size()>0) {
                CategoriesMyBox cat = cats.get(0);
                boxs = getAllCategoryBoxs(cat);
                albumsAdapter = new MyBoxCategoriesAdapter(boxs , getActivity(), colors);
                setGridAdapter(albumsAdapter);
            }

        }
    realm.commitTransaction();
        return view;
    }

    /** a method to fill the upper bar where we choose the {@link CategoriesMyBox}
     * @param category
     */
    private void fillNavigationBar( CategoriesMyBox category) {
        TextView categoryTxt = new TextView(getActivity());
        categoryTxt.setTypeface(MainActivity.FONT_BODY);
        RealmResults<MyBox> myBox = realm.where(MyBox.class).findAll();
        //		categoryTxt.setAllCaps(true);
        if (category.isSelected()  /*|| (category.getId_categorie()==0 && allselected)*/) {
            //			if (category.getId_categorie()==0) {
            //			}
            LinearLayout.LayoutParams txtParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
            txtParams.gravity = Gravity.CENTER;
            categoryTxt.setGravity(Gravity.CENTER);
            if (category.getId_categorie() == 0/*category.getId_CatMyBox() == 1*/) {
                categoryTxt.setText(getResources().getString(R.string.all_boxs));
            }else {
                for(MyBox myBox1 : myBox)
                {
                    if(category.getId_categorie() == myBox1.getId_categorie())
                        categoryTxt.setText((category.getName_categorie()).toUpperCase());
                }
                categoryTxt.setText(category.getName_categorie().toUpperCase());
            }
            categoryTxt.setTextColor(colors.getColor(colors.getBackground_color()));
            Drawable selectDrawable = getResources().getDrawable(R.drawable.back_choices_final);
            selectDrawable.setColorFilter(colors.getColor(colors.getTitle_color()), Mode.MULTIPLY);
            categoryTxt.setBackgroundDrawable(selectDrawable);
            categoryTxt.setTag(category);
            choiceHolder.addView(categoryTxt, txtParams);

        }else {

            LinearLayout.LayoutParams txtParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
            txtParams.setMargins(10, 0, 10, 5);
            categoryTxt.setGravity(Gravity.CENTER);
            txtParams.gravity = Gravity.CENTER;
            if (category.getId_categorie() == 0/*category.getId_CatMyBox()==1*/) {
                categoryTxt.setText(getResources().getString(R.string.all_boxs));
            }else {
                for(MyBox myBox1 : myBox)
                            {
                if(category.getId_categorie() == myBox1.getId_categorie())
                categoryTxt.setText((category.getName_categorie()).toUpperCase());
                             }
            }
            categoryTxt.setTextColor(colors.getColor(colors.getTitle_color()));

            categoryTxt.setTag(category);
            choiceHolder.addView(categoryTxt, txtParams);
        }

        categoryTxt.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                realm.beginTransaction();
                CategoriesMyBox cat = (CategoriesMyBox)v.getTag();
                id_cat = cat.getId_categorie();
                for (int i = 0; i < categories.size(); i++) {
                    categories.get(i).setSelected(false);
                }
                cat.setSelected(true);
                choiceHolder.removeAllViews();
                for (int i = 0; i < categories.size(); i++) {

                    fillNavigationBar(categories.get(i));
                }
                boxs = getAllCategoryBoxs(cat);
                albumsAdapter = new MyBoxCategoriesAdapter(boxs , getActivity(), colors);
                setGridAdapter(albumsAdapter);

            realm.commitTransaction();

            }
        });
    }
    protected List<MyBox> getAllCategoryWithIdCategory(List<CategoriesMyBox> cat) {
        List<MyBox> result = new ArrayList<MyBox>();
        boxs = realm.where(MyBox.class).findAll();// appController.getMyBoxDao().queryForAll();
        if (boxs.size()>0) {

            for (int i = 0; i < cat.size(); i++) {
                if (cat.get(i).getId_categorie() ==boxs.get(i).getId_categorie() ) {
                    result.add(boxs.get(i));
                }
            }
        }
        return result;
    }

    protected List<MyBox> getAllCategoryBoxs(CategoriesMyBox cat) {
        List<MyBox> result = new ArrayList<MyBox>();
        boxs = realm.where(MyBox.class).findAll();// appController.getMyBoxDao().queryForAll();
        if (boxs.size()>0) {
            if (cat.getId_categorie() == 0) {
                return boxs;
            }
            for (int i = 0; i < boxs.size(); i++) {
                if (boxs.get(i).getId_categorie() == cat.getId_categorie()) {
                    result.add(boxs.get(i));
                }
            }
        }
        return result;
    }

    /* (non-Javadoc)
     * @see com.euphor.paperpad.widgets.GridFragment#setGridAdapter(android.widget.ListAdapter)
     */
    @Override
    public void setGridAdapter(ListAdapter adapter) {
        // TODO Auto-generated method stub
        super.setGridAdapter(adapter);
    }

    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
     */
    @Override
    public void onAttach(Activity activity) {
       // appController = ((MyApplication)getActivity().getApplication()).getAppController();//new AppController(getActivity());
        		realm = Realm.getInstance(getActivity());

        colors = ((MainActivity)activity).colors;
        Parameters ParamColor = realm.where(Parameters.class).findFirst();
        if (colors==null) {
            colors = new Colors(ParamColor);
        }


        if (((MainActivity) getActivity()).bodyFragment == null) {
            ((MainActivity) getActivity()).bodyFragment = "MyBoxGridFragment";
        }

        int Section_id = getArguments().getInt("Section_id");
        if (((MainActivity) getActivity()).extras == null)
            ((MainActivity) getActivity()).extras = new Bundle();
        ((MainActivity) getActivity()).extras.putInt("Section_id", Section_id);

        if (getArguments() != null) {
            id_cat = getArguments().getInt("selected_category");
        }


        boxs = new ArrayList<MyBox>();
        boxs =  realm.where(MyBox.class).findAll();//appController.getMyBoxDao().queryForAll();
        albumsAdapter = new MyBoxCategoriesAdapter(boxs , getActivity(), colors);
        setGridAdapter(albumsAdapter);

        Utils.changeLocale("fr",getActivity());
        time = System.currentTimeMillis();
        super.onAttach(activity);
    }

    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    /* (non-Javadoc)
     * @see com.euphor.paperpad.widgets.GridFragment#onGridItemClick(android.widget.GridView, android.view.View, int, long)
     */
    @Override
    public void onGridItemClick(GridView g, View v, int position, long id) {
        MyBoxPageFragment boxPageFragment = new MyBoxPageFragment();
        ((MainActivity)getActivity()).bodyFragment = "MyBoxPageFragment";
        MyBox box = (MyBox) g.getItemAtPosition(position);
        ((MainActivity)getActivity()).extras.putInt("box_id", box.getId_mb());
        boxPageFragment.setArguments(((MainActivity)getActivity()).extras);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, boxPageFragment).addToBackStack(null).commit();


    }

    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onDestroy()
     */
    @Override
    public void onDestroy() {
        ((MainActivity) getActivity()).extras.putInt("selected_category", id_cat);
        Runtime.getRuntime().gc();
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        ((MainActivity) getActivity()).extras.putInt("selected_category", id_cat);
        Runtime.getRuntime().gc();
        super.onDestroyView();
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }

    @Override
    public void onStop() {
        AppHit hit = new AppHit(System.currentTimeMillis()/1000, time/1000, "sales_mybox_section", id_cat);
        ((MyApplication)getActivity().getApplication()).hits.add(hit);
        super.onStop();
    }
}

