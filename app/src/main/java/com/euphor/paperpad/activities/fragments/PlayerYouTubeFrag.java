/**
 * 
 */
package com.euphor.paperpad.activities.fragments;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.euphor.paperpad.activities.main.MainActivity;
import com.euphor.paperpad.Beans.Section;

import com.euphor.paperpad.utils.Constants;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import io.realm.Realm;

/**
 * @author euphordev02
 *
 */
public class PlayerYouTubeFrag extends YouTubePlayerSupportFragment {

	/**
	 * 
	 */
	public PlayerYouTubeFrag() {
		// TODO Auto-generated constructor stub
	}
	
	private String currentVideoID = "wKJ9KzGQq0w";
    private YouTubePlayer activePlayer;

	private int section_id;
   private Realm realm;
    public static PlayerYouTubeFrag newInstance(int section_id) {

    PlayerYouTubeFrag playerYouTubeFrag = new PlayerYouTubeFrag();

        Bundle bundle = new Bundle();
        bundle.putInt("section_id", section_id);

        playerYouTubeFrag.setArguments(bundle);

        return playerYouTubeFrag;
    }

    private void init() {

        initialize(Constants.DEVELOPER_KEY, new OnInitializedListener() {


            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
            	player.setFullscreenControlFlags(0);
                /*activePlayer = player;
                activePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                if (!wasRestored) {
                    activePlayer.loadVideo(currentVideoID);
                }*/
				if (!wasRestored) {
					activePlayer = player;
					//activePlayer.setFullscreen(true);
					activePlayer.loadVideo("2zNSgSzhBfM");
					activePlayer.play();
				}
            }

			@Override
			public void onInitializationFailure(Provider arg0,
					YouTubeInitializationResult arg1) {
				Log.i("YouTube faillure", "Provider "+arg0.toString()+" YouTubeInitializationResult "+arg1.ordinal());
				// TODO Auto-generated method stub
				
			}
        });
    }

	/* (non-Javadoc)
	 * @see com.google.android.youtube.player.YouTubePlayerSupportFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle arg0) {
		setRetainInstance(true);
		super.onCreate(arg0);
	}

	/* (non-Javadoc)
	 * @see com.google.android.youtube.player.YouTubePlayerSupportFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater arg0, ViewGroup arg1, Bundle arg2) {
		init();
		return super.onCreateView(arg0, arg1, arg2);
	}

	/* (non-Javadoc)
	 * @see com.google.android.youtube.player.YouTubePlayerSupportFragment#onDestroyView()
	 */
	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
//new AppController(getActivity());
				realm = Realm.getInstance(getActivity());
		if (getArguments() != null) {
			section_id = getArguments().getInt("section_id");
            Section section = realm.where(Section.class).equalTo("id_s",section_id).findFirst();
            // appController.getSectionsDao().queryForId(section_id);
            if (section.getType().equalsIgnoreCase("video")) {
                String pattern = "(?<=watch\\?v=|v\\/|/videos/|embed\\/)[^#\\&\\?]*"; // "(?<=watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*" // "\b(?<=v.|/)[a-zA-Z0-9_-]{11,}\b"
                Pattern compiledPattern = Pattern.compile(pattern);
                Matcher matcher = compiledPattern.matcher(section.getRoot_url());

                if(matcher.find()){
                    currentVideoID = matcher.group();
                }
            }
        }
		((MainActivity) getActivity()).extras = getArguments();
		super.onAttach(activity);
	}

	/* (non-Javadoc)
	 * @see com.google.android.youtube.player.YouTubePlayerSupportFragment#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	public void onSaveInstanceState(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(arg0);
	}

    

}
