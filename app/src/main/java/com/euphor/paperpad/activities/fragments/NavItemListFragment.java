package com.euphor.paperpad.activities.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.euphor.paperpad.R;
import com.euphor.paperpad.adapters.CommunElements;
import com.euphor.paperpad.utils.Colors;

import java.util.List;

/**
 * A list fragment representing a list of NavItems. This fragment also supports
 * tablet devices by allowing list items to be given an 'activated' state upon
 * selection. This helps indicate which item is currently being viewed in a
 * {@link NavItemDetailFragment}.
 * <p>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
@SuppressLint("ValidFragment")
public class NavItemListFragment extends ListFragment {
	
	public static final String ARG_ITEM_ID = "item_id";
	
	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * activated item position. Only used on tablets.
	 */
	private static final String STATE_ACTIVATED_POSITION = "activated_position";

	/**
	 * The fragment's current callback object, which is notified of list item
	 * clicks.
	 */
	private Callbacks mCallbacks = sDummyCallbacks;

	/**
	 * The current activated item position. Only used on tablets.
	 */
	private int mActivatedPosition = ListView.INVALID_POSITION;
	
	public static ListView  list;
	
	List<CommunElements> elements;

	private Activity activity;
	private int layout_item;
	private Colors colors;
	
	/**
	 * A callback interface that all activities containing this fragment must
	 * implement. This mechanism allows activities to be notified of item
	 * selections.
	 */
	public interface Callbacks {
		/**
		 * Callback for when an item has been selected.
		 */
		public void onItemSelected(String id);
	}

	/**
	 * A dummy implementation of the {@link Callbacks} interface that does
	 * nothing. Used only when this fragment is not attached to an activity.
	 */
	private static Callbacks sDummyCallbacks = new Callbacks() {
		@Override
		public void onItemSelected(String id) {
		}
	};

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public NavItemListFragment(Activity activity, List<CommunElements> elements) {
		this.activity = activity;
		this.elements = elements;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// TODO: replace with a real list adapter.
//		setListAdapter(new ArrayAdapter<DummyContent.DummyItem>(getActivity(),
//				android.R.layout.simple_list_item_activated_1,
//				android.R.id.text1, DummyContent.ITEMS));
		
		Log.i(" NavItemListFragment <==== onCreate " , "  ITEMS.Size() : "+elements.size());

		setListAdapter(new ArrayAdapter<CommunElements>(getActivity(),
				R.layout.divided_screen_list_item,
				R.id.utilInfo, elements));

				
//		setListAdapter(new MyListAdapter(getActivity(),
//				 DummyContent.ITEMS, R.layout.divided_screen_list_item));
//		
		
	}
	
//	View view;
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//		View view = inflater.inflate(R.layout.divided_screen_list_item, container, false);
//		 
//		
//		return view;
//	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		//super.onViewCreated(view, savedInstanceState);
		//this.view = view;
		// Restore the previously serialized activated item position.
		if (savedInstanceState != null
				&& savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
			setActivatedPosition(savedInstanceState
					.getInt(STATE_ACTIVATED_POSITION));	
		}	
		
		list = getListView();
	
				//Log.i(" NavItemListFragment <==== getListView().getSelectedItemPosition() : ", ""+getListView().getBackground());
	}
	
	@Override
	public void onResume() {
		Paint paint1 = new Paint();
		paint1.setColor(Color.BLACK);
		//((com.Euphor.masterdetail.widget.ArrowImageView) view.findViewById(R.id.listItemFlech)).setPaint(paint1);


		super.onResume();
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
		
		Log.i(" NavItemListFragment <==== onAttach " , ""+mCallbacks);

	}

	@Override
	public void onDetach() {
		super.onDetach();

		// Reset the active callbacks interface to the dummy implementation.
		mCallbacks = sDummyCallbacks;
		
		Log.i(" NavItemListFragment <==== onDetach " , ""+mCallbacks);

	}

	@Override
	public void onListItemClick(ListView listView, View view, int position,
			long id) {
		//view.getFocusables(position);
		//view.setSelected(true);
		//super.onListItemClick(listView, view, position, id);
		Log.i(" NavItemListFragment <==== onListItemClick " , " position : "+position+",  id : "+id);
		// Notify the active callbacks interface (the activity, if the
		// fragment is attached to one) that an item has been selected.
		if(mActivatedPosition != position){
			getListView().getChildAt((mActivatedPosition == -1)? position : mActivatedPosition).setBackgroundColor(Color.TRANSPARENT);
			mActivatedPosition = position;
		}
			getListView().getChildAt(mActivatedPosition).setBackgroundColor(Color.CYAN);

		mCallbacks.onItemSelected(""+position);
	}
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (mActivatedPosition != ListView.INVALID_POSITION) {
			// Serialize and persist the activated item position.
			outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
		}
	}

	/**
	 * Turns on activate-on-click mode. When this mode is on, list items will be
	 * given the 'activated' state when touched.
	 */
	public void setActivateOnItemClick(boolean activateOnItemClick) {
		// When setting CHOICE_MODE_SINGLE, ListView will automatically
		// give items the 'activated' state when touched.
		Log.i(" NavItemListFragment <==== setActivateOnItemClick " , ""+activateOnItemClick);

		getListView().setChoiceMode(
				activateOnItemClick ? ListView.CHOICE_MODE_SINGLE
						: ListView.CHOICE_MODE_NONE);
	}

	private void setActivatedPosition(int position) {
		
		Log.i(" NavItemListFragment <==== setActivatedPosition " , ""+position);
		
		if (position == ListView.INVALID_POSITION) {
			getListView().setItemChecked(mActivatedPosition, false);
		} else {
			getListView().setItemChecked(position, true);
		}

		mActivatedPosition = position;
	}
	
//	static class ViewHolder{
//		TextView txt;
//	}
//	
//	class MyListAdapter extends BaseAdapter{
//		
//		private Context mActivity;
//		private List<DummyItem> ITEMS = new ArrayList<DummyItem>();
//		private LayoutInflater mInflater;
//		RelativeLayout view;
//		
//		
//		
//		public MyListAdapter(Activity activity, List<DummyItem> items, int layoutId){
//			mActivity = activity;
//			ITEMS = items;
//			mInflater = activity.getLayoutInflater();
//			view = (RelativeLayout)mInflater.inflate(layoutId, null, false);
//		}
//
//		@Override
//		public int getCount() {
//			
//			return ITEMS.size();
//		}
//
//		@Override
//		public Object getItem(int position) {
//			// TODO Auto-generated method stub
//			return ITEMS.get(position);
//		}
//
//		@Override
//		public long getItemId(int id) {
//			 
//			return id;
//		}
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup viewGroup) {
//			ViewHolder holder = new ViewHolder();
//			if(convertView == null){
//				convertView = view;
//				holder.txt = (TextView)convertView.findViewById(R.id.utilInfo);
//				convertView.setTag(holder);
//			}
//			else{
//				holder = (ViewHolder) convertView.getTag();
//			}
//			holder.txt.setText(ITEMS.get(position).toString());
//			
//			return convertView;
//		}
//		
//		
//		
//	}
}
