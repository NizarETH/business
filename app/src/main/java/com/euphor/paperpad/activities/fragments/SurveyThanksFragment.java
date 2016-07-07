package com.euphor.paperpad.activities.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.euphor.paperpad.R;

public class SurveyThanksFragment extends Fragment {

	public SurveyThanksFragment() {
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
		super.onCreate(savedInstanceState);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		TextView finishStatement = new TextView(getActivity());
		ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
		finishStatement.setLayoutParams(params);
		finishStatement.setTextSize(TypedValue.COMPLEX_UNIT_SP, 27);
		finishStatement.setText(getResources().getString(R.string.survey_thanks));
		finishStatement.setGravity(Gravity.CENTER);
		return finishStatement;
	}
	
	static SurveyThanksFragment newInstance() {
		SurveyThanksFragment frag=new SurveyThanksFragment();
		return(frag);
	}

}
