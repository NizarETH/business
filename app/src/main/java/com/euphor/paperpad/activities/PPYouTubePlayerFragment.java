/**
 * 
 */
package com.euphor.paperpad.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.euphor.paperpad.R;
import com.euphor.paperpad.Beans.Section;

import com.euphor.paperpad.utils.Constants;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.realm.Realm;

/**
 * @author euphordev02
 *
 */
public class PPYouTubePlayerFragment extends Fragment {


	private String link;
    private Realm realm;

	/**
	 * 
	 */
	public PPYouTubePlayerFragment() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		realm = Realm.getInstance(getActivity());

		List<Section> sections = new ArrayList<Section>();
        sections = realm.where(Section.class).equalTo("type", "video").findAll();
        // appController.getSectionsDao().queryForEq("type", "video");
        if (sections.size()>0) {
			Section sectionVideo = sections.get(0);
			String url = sectionVideo.getRoot_url();
			
			link = "wKJ9KzGQq0w";
		    String pattern = "(?:videos\\/|v=)([\\w-]+)";

		    Pattern compiledPattern = Pattern.compile(pattern);
		    Matcher matcher = compiledPattern.matcher(url);

		    if(matcher.find()){
		        link = matcher.group();
		    }
		}
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.youtube_fragment, container, false);
		YouTubePlayerView playerView = (YouTubePlayerView)view.findViewById(R.id.youtube_view);
		
		OnInitializedListener listnerOnVid = new OnInitializedListener() {
			
			@Override
			public void onInitializationSuccess(Provider arg0, YouTubePlayer player,
					boolean wasRestored) {
				if (!wasRestored) {
					player.cueVideo(link);
				}
				
			}
			
			@Override
			public void onInitializationFailure(Provider arg0,
					YouTubeInitializationResult arg1) {
				// TODO Auto-generated method stub
				
			}
		};
		playerView.initialize(Constants.DEVELOPER_KEY, listnerOnVid );
		return view;
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
