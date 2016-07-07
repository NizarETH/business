/**
 * 
 */
package com.euphor.paperpad.activities.fragments;


import com.euphor.paperpad.R;
import com.euphor.paperpad.activities.main.MainActivity;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;

/**
 * @author euphordev02
 *
 */
public class OrderFragment extends DialogFragment {

	/**
	 * 
	 */
	public OrderFragment() {
		// TODO Auto-generated constructor stub
	}
	
	public static OrderFragment newInstance() {
		OrderFragment f = new OrderFragment();
        return f;
    }

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setRetainInstance(true);
		
		super.onCreate(savedInstanceState);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.order_fragment, container, false);
//		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE); 
		FragmentTransaction ft = getChildFragmentManager().beginTransaction();
		Fragment prev = getChildFragmentManager().findFragmentByTag("dialog");
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);
		((MainActivity)getActivity()).bodyFragment = "FormDialogFragment";
		// Create and show the dialog.
		FormCartFragment newFragment = FormCartFragment.newInstance(1);
//		newFragment.setCancelable(false);
		//							newFragment.setStyle( DialogFragment.STYLE_NO_TITLE, R.style.CustomDialog );
//		newFragment.show(ft, "dialog");
		getChildFragmentManager().beginTransaction().replace(R.id.frame_content, newFragment, "dialog").addToBackStack("dialog").commit();
		return view;
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		Window window = getDialog().getWindow();
	    LayoutParams attributes = window.getAttributes();
	    //must setBackgroundDrawable(TRANSPARENT) in onActivityCreated()
	    window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
	    this.setCancelable(false);
	    window.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	    //window.setLayout((int)getResources().getDimension(R.dimen.window_width), (int)getResources().getDimension(R.dimen.window_height));
	    DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		//			if (bottomNav) {
		
	    attributes.width = dm.widthPixels;
	    attributes.height = dm.heightPixels;
		super.onActivityCreated(savedInstanceState);
	}
	
	
	@Override
	public void onDestroyView() {
		if (getDialog() != null && getRetainInstance())
			getDialog().setOnDismissListener(null);
		super.onDestroyView();
	}

}
