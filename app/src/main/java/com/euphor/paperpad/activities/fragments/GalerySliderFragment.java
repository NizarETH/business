package com.euphor.paperpad.activities.fragments;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.euphor.paperpad.Beans.Parameters;
import com.euphor.paperpad.R;
import com.euphor.paperpad.activities.main.MainActivity;
import com.euphor.paperpad.Beans.Album;
import com.euphor.paperpad.Beans.Illustration;
import com.euphor.paperpad.Beans.Photo;

import com.euphor.paperpad.utils.Colors;
import com.euphor.paperpad.utils.Utils;


import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.realm.Realm;

/**
 * Created by Euphor on 27/01/15.
 */
public class GalerySliderFragment extends Fragment{

    private Colors colors;

    protected int id_cat = 0;
    protected LinearLayout choiceHolder;
    public Album album;
    private LinearLayout hsvLayout;
    Illustration illust;
    List<Illustration> illustrations;
    String path = null;


    private List<Album> albums;
    private ViewPager mPager;
    private GallerySlidePagerAdapter adapter;
    private Realm realm;

    /* (non-Javadoc)
         * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
         */
    @Override
    public void onAttach(Activity activity) {
        		realm = Realm.getInstance(getActivity());

        colors = ((MainActivity)activity).colors;
        if (colors==null) {
            Parameters ParamColor = realm.where(Parameters.class).findFirst();
            colors = new Colors(ParamColor);
        }
        albums = new ArrayList<Album>();
        albums = realm.where(Album.class).findAll();



/*        if(albums.size() == 1) {
            GaleryFragment galeryFragment = new GaleryFragment();
            ((MainActivity)getActivity()).bodyFragment = "GalerySliderFragment";
            ((MainActivity)getActivity()).extras.putInt("Album_id", albums.get(0).getId_album());
            galeryFragment.setArguments(((MainActivity)getActivity()).extras);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, galeryFragment).addToBackStack(null).commit();
        }else {
            //AlbumsAdapter albumsAdapter = new AlbumsAdapter(albums , getActivity(), colors);
            //setGridAdapter(albumsAdapter);
        }*/

        super.onAttach(activity);
    }

    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.galerie_slider, container, false);
        view.setBackgroundColor(Color.BLACK/*colors.getColor(colors.getBackground_color())*/);
        hsvLayout = (LinearLayout)view.findViewById(R.id.choicesProduct);
        //productInfo = (FrameLayout)view.findViewById(R.id.productInfo);
/*        GradientDrawable drawable_bar = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[] {Color.BLACK*//*colors.getColor(colors.getBackground_color(), "80")*//*, colors.getColor(colors.getBackground_color(), "00")});
        drawable_bar.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        drawable_bar.setCornerRadius(0f);
        drawable_bar.setColorFilter(Color.BLACK*//*colors.getColor(colors.getBackground_color())*//*, android.graphics.PorterDuff.Mode.OVERLAY);*/
        view.findViewById(R.id.productList).setBackgroundColor(Color.BLACK);//.setBackgroundDrawable(drawable_bar);
        view.findViewById(R.id.productList).setAlpha(0.8f);
        //view.findViewById(R.id.choicesHolderLayout).setBackgroundColor(colors.getBackMixColor(colors.getForeground_color(), 0.10f));

        RelativeLayout.LayoutParams layoutParams;
        if(Utils.isTablet(getActivity())) {
            layoutParams = new  RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (int) Utils.dpToPixels(getActivity(), 150f));
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
            view.findViewById(R.id.productList).setLayoutParams(layoutParams);
        }else{
            layoutParams = new  RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (int) Utils.dpToPixels(getActivity(), 80f));
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
            view.findViewById(R.id.productList).setLayoutParams(layoutParams);
        }


        final ImageView nextImageList = (ImageView) view.findViewById(R.id.nextImageList);

        final HorizontalScrollView horizontalScrollView = (HorizontalScrollView)view.findViewById(R.id.SVProductsHolder);
        horizontalScrollView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                int diff = (horizontalScrollView.getChildAt(0).getRight() - horizontalScrollView.getWidth() + horizontalScrollView.getScrollX());

                if (diff == horizontalScrollView.getChildAt(0).getRight() - horizontalScrollView.getWidth()) {
                    nextImageList.setVisibility(View.GONE);
                }else if(diff ==  0){
                    if(horizontalScrollView.getWidth() < horizontalScrollView.getChildAt(0).getRight())//scrollView.getScrollX())
                        nextImageList.setVisibility(View.VISIBLE);
                }else{
                    nextImageList.setVisibility(View.VISIBLE);
                }
            }
        });

        nextImageList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //horizontalScrollView.setScrollX(horizontalScrollView.getScrollX() + 120);
                horizontalScrollView.setScrollX(horizontalScrollView.getWidth());
            }
        });
realm.beginTransaction();
        if(albums.size() > 0) {

            if(Utils.isTablet(getActivity())){
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                params.addRule(RelativeLayout.BELOW, R.id.choicesHolderLayout);
                params.bottomMargin = 130;//(int) Utils.dpToPixels(getActivity(), 130f)
                view.findViewById(R.id.pagerContainer).setLayoutParams(params);
            }else{

                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                params.addRule(RelativeLayout.BELOW, R.id.choicesHolderLayout);
                params.bottomMargin = 60;//(int) Utils.dpToPixels(getActivity(), 60f)
                view.findViewById(R.id.pagerContainer).setLayoutParams(params);
            }

            choiceHolder = (LinearLayout) view.findViewById(R.id.choicesHolder);
            album = albums.get(0);
            album.setSelected(true);

            for (Iterator<Album> iterator = albums.iterator(); iterator.hasNext(); ) {
                Album element = (Album) iterator.next();
                fillNavigationBar(element);
            }

            if (album != null) {

                mPager = (ViewPager) view.findViewById(R.id.pager);
                //productInfo.addView(mPager, android.widget.FrameLayout.LayoutParams.MATCH_PARENT, android.widget.FrameLayout.LayoutParams.MATCH_PARENT);
                mPager.setOffscreenPageLimit(1);

                fillImagesScroller(album);


                mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                    @Override
                    public void onPageSelected(int position) {

                        if (position >= 0) {
                            //int i = position;// - 1;

                            ((HorizontalScrollView) ((View) hsvLayout.getParent())).smoothScrollTo(hsvLayout.getChildAt(position).getLeft(), hsvLayout.getChildAt(position).getTop());
                            hsvLayout.getChildAt(position).setAlpha(1);
                            if ((position + 1) <= illustrations.size() - 1)
                                hsvLayout.getChildAt(position + 1).setAlpha(0.7f);
                        }


                        if (position <= illustrations.size() - 1) {
                            //int i = position;// + 1;
                            //mPager.setCurrentItem(i);


                            ((HorizontalScrollView) ((View) hsvLayout.getParent())).smoothScrollTo(hsvLayout.getChildAt(position).getLeft() , hsvLayout.getChildAt(position).getTop());
                            hsvLayout.getChildAt(position).setAlpha(1);
                            if ((position - 1) >= 0)
                                hsvLayout.getChildAt(position - 1).setAlpha(0.7f);
                        }

                    }

                    @Override
                    public void onPageScrolled(int arg0, float arg1, int arg2) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onPageScrollStateChanged(int arg0) {
                        // TODO Auto-generated method stub

                    }
                });

                final ImageView previousImage = (ImageView) view.findViewById(R.id.previousImage);
                final ImageView nextImage = (ImageView) view.findViewById(R.id.nextImage);

                previousImage.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        int currentItem = mPager.getCurrentItem();
                        if(currentItem > 0) {
                            previousImage.setVisibility(View.VISIBLE);
                            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
                            nextImage.setVisibility(View.VISIBLE);
                        }
                        else{
                            previousImage.setVisibility(View.GONE);
                        }

                    }
                });

                nextImage.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        int currentItem = mPager.getCurrentItem();
                        if(currentItem < adapter.getCount() - 1){
                            nextImage.setVisibility(View.VISIBLE);
                            previousImage.setVisibility(View.VISIBLE);
                            mPager.setCurrentItem(mPager.getCurrentItem() + 1);
                        }else{
                            nextImage.setVisibility(View.GONE);
                        }
                    }
                });


            }
        }

    realm.commitTransaction();

        return view;
    }



    /** a method to fill the upper bar
     * @param album
     */
    private void fillNavigationBar(Album album) {
        TextView albumTitleTxt = new TextView(getActivity());
        albumTitleTxt.setTextAppearance(getActivity(), android.R.style.TextAppearance_DeviceDefault_Medium);
        albumTitleTxt.setTypeface(MainActivity.FONT_REGULAR);
        if (album.isSelected() ) {
            LinearLayout.LayoutParams txtParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
            //txtParams.setMargins(10, 0, 10, 5);
            txtParams.gravity = Gravity.CENTER;
            albumTitleTxt.setGravity(Gravity.CENTER);
            albumTitleTxt.setText(album.getTitle().toUpperCase());
            albumTitleTxt.setTextColor(colors.getColor(colors.getBackground_color()));

//			Drawable dr = getResources().getDrawable(R.drawable.bottom_bubble);
//			Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
//			Drawable d = new BitmapDrawable(getResources(),Bitmap.createScaledBitmap(bitmap, categoryTxt.getText().length() * 11, 50, true));
//			d.setColorFilter(colors.getColor(colors.getTitle_color()), Mode.MULTIPLY);
//			//categoryTxt.setCompoundDrawablesWithIntrinsicBounds(d, null,null,null);
//			categoryTxt.setBackgroundDrawable(d);
            /*Drawable selectDrawable = getResources().getDrawable(R.drawable.another_back);
            selectDrawable.setColorFilter(colors.getColor(colors.getTitle_color()), PorterDuff.Mode.MULTIPLY);
            albumTitleTxt.setBackgroundDrawable(selectDrawable);*/
            albumTitleTxt.setPadding(10, 15, 10, 15);
            albumTitleTxt.setBackgroundColor(colors.getColor(colors.getTitle_color()));
            albumTitleTxt.setTag(album);
            choiceHolder.addView(albumTitleTxt);//, txtParams);

        }else {

            LinearLayout.LayoutParams txtParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
            //txtParams.setMargins(10, 0, 10, 5);
            albumTitleTxt.setPadding(10, 0, 10, 0);
            albumTitleTxt.setGravity(Gravity.CENTER);
            txtParams.gravity = Gravity.CENTER;
            albumTitleTxt.setText((album.getTitle()).toUpperCase());
            albumTitleTxt.setTextColor(colors.getColor(colors.getTitle_color()));
//			Drawable selectDrawable = getResources().getDrawable(R.drawable.back_empty);
//			selectDrawable.setColorFilter(colors.getColor(colors.getForeground_color()), Mode.MULTIPLY);
//			categoryTxt.setBackgroundDrawable(selectDrawable);
            albumTitleTxt.setTag(album);
            choiceHolder.addView(albumTitleTxt, txtParams);
        }

        albumTitleTxt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                realm.beginTransaction();

                GalerySliderFragment.this.album = (Album) v.getTag();
                for (int i = 0; i < albums.size(); i++) {
                    albums.get(i).setSelected(false);
                }
                GalerySliderFragment.this.album.setSelected(true);
                choiceHolder.removeAllViews();
                for (int i = 0; i < albums.size(); i++) {

                    fillNavigationBar(albums.get(i));
                }
                hsvLayout.removeAllViews();
                fillImagesScroller(GalerySliderFragment.this.album);
/*                if (album != null && getChildrenPages(cat).size() > 0) {
                    fillProductScroller(getChildrenPages(cat), getChildrenPages(cat).iterator().next());
                    pages = (ArrayList<Child_pages>) getChildrenPages(cat);
                }*/
  realm.commitTransaction();

            }
        });
    }

    View selectedBefore;
    //private int indexCurrent;
    /**
     * @param album
     *
     */
    public void fillImagesScroller(Album album) {
        hsvLayout.removeAllViews();

        illustrations = new ArrayList<Illustration>();

        int i = 0;
        for (Iterator<Photo> photoIterator = album.getPhotos().iterator(); photoIterator.hasNext();) {

            LinearLayout layoutProduct = new LinearLayout(getActivity());
            layoutProduct.setTag(i);
            layoutProduct.setPadding(3, 5, 3, 5);

            ImageView imgProd = new ImageView(getActivity());
            //Thumb Product

            Photo photo = photoIterator.next();
            Illustration illust = photo.getIllustration();

//				String path = !illust.getPath().isEmpty()?"file:///"+illust.getPath():illust.getLink();
//				imageLoader.displayImage(path, imgProd);
            if(illust != null) {

                illustrations.add(illust);

                imgProd.setScaleType(ImageView.ScaleType.CENTER_CROP);

                    /*float ratioWidth = ((illust.getOriginalWidth() / illust.getOriginalHeight() * 10) == 0) ? 10 : (illust.getOriginalWidth() / illust.getOriginalHeight() * 15);
                    imgProd.setLayoutParams(new LinearLayout.LayoutParams((int) (ratioWidth * 15), LinearLayout.LayoutParams.MATCH_PARENT));
*/
                if(Utils.isTablet(getActivity())) {
                    imgProd.setLayoutParams(new LinearLayout.LayoutParams((int) Utils.dpToPixels(getActivity(), 190f), (int) Utils.dpToPixels(getActivity(), 150f)));
                    layoutProduct.addView(imgProd);
                    hsvLayout.addView(layoutProduct, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));

                }else{
                    imgProd.setLayoutParams(new LinearLayout.LayoutParams((int) Utils.dpToPixels(getActivity(), 100f), (int) Utils.dpToPixels(getActivity(), 80f)));
                    layoutProduct.addView(imgProd);
                    hsvLayout.addView(layoutProduct, new ViewGroup.LayoutParams((int) Utils.dpToPixels(getActivity(), 100f), (int) Utils.dpToPixels(getActivity(), 80f)));
                }

                if (!illust.getPath().isEmpty()) {
                    Glide.with(getActivity()).load(new File(illust.getPath())).into(imgProd);
                }else {
                    Glide.with(getActivity()).load(illust.getLink()).into(imgProd);
                }
            }




            if (i == 0) {
                layoutProduct.setAlpha(1);
                selectedBefore = layoutProduct;

            }else {
                layoutProduct.setAlpha(0.7f);
            }


            layoutProduct.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
//					Toast.makeText(getActivity(), "id_page : "+((Child_pages)v.getTag()).getId_cpages(), Toast.LENGTH_SHORT).show();
                    v.setAlpha(1);
                    if (selectedBefore!= null && !(((Integer)selectedBefore.getTag()) == ((Integer)v.getTag()))) {
                        selectedBefore.setAlpha(0.7f);
                    }
                    selectedBefore = v;

                    mPager.setCurrentItem((Integer)v.getTag(), true);

                }
            });

            i++;
        }

        adapter = new GallerySlidePagerAdapter(getChildFragmentManager());
        mPager.setAdapter(adapter);


//		for (int i = 0 ; i <  hsvLayout.getChildCount(); i++){
//
//			LinearLayout layoutProduct = (LinearLayout) hsvLayout.getChildAt(i);
//			if (layoutProduct.getAlpha() == 1.0f) {
//				((HorizontalScrollView)((View)hsvLayout.getParent())).smoothScrollTo(layoutProduct.getLeft() + layoutProduct.getPaddingLeft(), layoutProduct.getTop());
//
//			}
//
//		}
    }


    private Paint getNewPaint() {
        Paint paint = new Paint();
        paint.setColor(colors.getColor(colors.getForeground_color(),"FF"));
        return paint;
    }

    /**
     * A simple pager adapter that represents 5 {@link com.euphor.paperpad.activities.main.ScreenSlidePageFragment} objects, in
     * sequence.
     */
    private class GallerySlidePagerAdapter extends android.support.v4.app.FragmentStatePagerAdapter {

        public GallerySlidePagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return GallerySlidePageFragment.create(illustrations.get(position), album.getId_album(), getActivity(), getActivity());
        }

        @Override
        public int getCount() {
            return illustrations.size();
        }
    }


}
