package com.euphor.paperpad.activities.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.euphor.paperpad.InterfaceRealm.CommunElements1;
import com.euphor.paperpad.R;
import com.euphor.paperpad.activities.main.MainActivity;

import com.euphor.paperpad.utils.Colors;

import java.util.List;

/**
 * A fragment representing a single NavItem detail screen. This fragment is
 * either contained in a {@link } in two-pane mode (on
 * tablets) or a {@link } on handsets.
 */
@SuppressLint("ValidFragment")
public class NavItemDetailFragment extends Fragment implements android.view.View.OnTouchListener {
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";
	
	private int current;
	private ViewFlipper viewFlipper;
	private int downX,upX;
	private View view;
	private Activity activity;
	private int layout_item;
	private Colors colors;

	/**
	 * The dummy content this fragment is presenting.
	 */
	private List<CommunElements1> mItem;

	private static final String STATE_ACTIVATED_DETAIL = "activated_detail";

	private Callbacks mCallbacks = mDetailCallbacks;

	public static int currentDetailPosition = 0;




	public interface Callbacks {
		/**
		 * Callback for when an item has been selected.
		 */
		public void onTouchDetail(int DetailId);
	}


	private static Callbacks mDetailCallbacks = new Callbacks() {
		@Override
		public void onTouchDetail(int DetailId) {
			
//			Log.i(" mDetailCallbacks onToucheDetail  ==> detailID : ",DetailId);
		}
	};

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	
	 
	
	@SuppressLint("ValidFragment")
	public NavItemDetailFragment(MainActivity activity, List<CommunElements1> elements, Colors colors, int layout_item){
		this.activity = activity;
		this.mItem = elements;
		this.colors = colors;
		this.layout_item = layout_item;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


		if (getArguments().containsKey(ARG_ITEM_ID)) {
			// Load the dummy content specified by the fragment
			// arguments. In a real-world scenario, use a Loader
			// to load content from a content provider.
			currentDetailPosition = Integer.parseInt(getArguments().getString(
					ARG_ITEM_ID));
			
//			mItem = DummyContent.ITEM_MAP.get(getArguments().getString(
//					ARG_ITEM_ID));
			
			/** to get Item **/
		}


	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_navitem_detail,
				container, false);

		view = rootView;
		
		rootView.setOnTouchListener(this);
		
		
		Log.i(" NavItemDetailFragment <==== onCreateView " , " mItem (DummyContent.DummyItem) : "+mItem);
		TextView titlel = new TextView(getActivity());
		//		txtDetail.setTextColor(Color.BLUE);
		//		txtDetail.setTextSize(22);
		//		LinearLayout lyt = new LinearLayout(getActivity());
		//		lyt.setLayoutParams(new LinearLayout.LayoutParams(300, 300));

		titlel = (TextView) rootView.findViewById(R.id.navitem_detail);
		titlel.setTypeface(MainActivity.FONT_TITLE);
		// Show the dummy content as text in a TextView.
		if (mItem != null) {
			
			
			titlel.setText(mItem.get(0).getCommunTitle1()+" Title ");
		}
		else{
			titlel.setText(" No Detail for this element ");
		}



		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// Activities containing this fragment must implement its callbacks.
		if (!(activity instanceof Callbacks)) {
			throw new IllegalStateException(
					"Activity must implement fragment's callbacks.");
		}
		mCallbacks = (Callbacks) activity;

		Log.i(" NavItemDetailFragment <==== onAttach " , ""+mCallbacks);
	}


	@Override
	public void onDetach() {
		super.onDetach();
		// Reset the active callbacks interface to the dummy implementation.
		mCallbacks = mDetailCallbacks;
		Log.i(" NavItemDetailFragment <==== onDetach " , ""+mCallbacks);
	}


	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (currentDetailPosition != -1) {
			// Serialize and persist the activated item position.
			outState.putInt(STATE_ACTIVATED_DETAIL, currentDetailPosition);
		}
	}







	@Override
	public boolean onTouch(View v, MotionEvent event) {


		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			downX = (int) event.getX(); 
			Log.i("event.getX()", " downX " + downX);
			return true;
		} 

		else if (event.getAction() == MotionEvent.ACTION_UP) {
			upX = (int) event.getX(); 
			if (upX - downX > 100) {

				currentDetailPosition--;
				          if (currentDetailPosition < 0) {
				        	  currentDetailPosition = mItem.size() - 1;
				          }

				view.setAnimation(AnimationUtils.loadAnimation(getActivity().getApplicationContext(),android.R.anim.slide_in_left));
				view.setAnimation(AnimationUtils.loadAnimation(getActivity().getApplicationContext(),android.R.anim.slide_out_right));

				//viewFlipper.showPrevious(); 
			} 

			else if (downX - upX > -100) {

				currentDetailPosition++;
				          if (currentDetailPosition > mItem.size() - 1) {
				        	  currentDetailPosition = 0;
				          }


				view.setAnimation(AnimationUtils.loadAnimation(getActivity().getApplicationContext(),android.R.anim.slide_out_right));
				view.setAnimation(AnimationUtils.loadAnimation(getActivity().getApplicationContext(),android.R.anim.slide_in_left));

				//viewFlipper.showNext(); 
				Log.i("event.getX()", " upX " + downX+"  currentDetailPosition : "+currentDetailPosition);

			}
			mCallbacks.onTouchDetail(currentDetailPosition);

			return true;
		}

		return false;

	}
}