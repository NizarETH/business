/**
 * 
 */
package com.euphor.paperpad.activities.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.StateListDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

//import com.crashlytics.android.Crashlytics;
import com.euphor.paperpad.Beans.Parameters;
import com.euphor.paperpad.R;
import com.euphor.paperpad.activities.main.MainActivity;
import com.euphor.paperpad.Beans.Child_pages;
import com.euphor.paperpad.Beans.Illustration;


import com.euphor.paperpad.utils.Colors;
import com.euphor.paperpad.utils.ImageHelper;
import com.panoramagl.PLCubicPanorama;
import com.panoramagl.PLImage;
import com.panoramagl.enumeration.PLCubeFaceOrientation;
import com.panoramagl.utils.PLUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;

/**
 * @author euphordev02
 *
 */
public class PanoramaFragment extends Fragment {

	private static final int FIRST = 0;
	private static final int PREVIOUS = -1;
	private static final int NEXT = 1;
	private static int WIDTH_SWIPE_PICS = 600;
	private static String ALPHA = "CC";
	private String design;

	private int page_id;
	private Child_pages page;
	private Colors colors;
	private List<Child_pages> pages;
	private int indexCurrent;
	private Integer categoryId;
	private StateListDrawable stateListDrawable;
	private MainActivity mainActivity;
	private PLImage imageFace;
	private PLImage imageDroite;
	private PLImage imageRetour;
	private PLImage imageGauche;
	private PLImage imageHaut;
	private PLImage imageBas;
	private PLCubicPanorama panorama2;
	private boolean alreadyResized;
    public Realm realm;
	
	/**
	 * 
	 */
	public PanoramaFragment() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
		//new AppController(getActivity());
				realm = Realm.getInstance(getActivity());
		colors = ((MainActivity)activity).colors;
        Parameters ParamColor = realm.where(Parameters.class).findFirst();
        if (colors==null) {
            colors = new Colors(ParamColor);
        }


        mainActivity = (MainActivity)activity;
		refresher = (Refresher)activity;
		super.onAttach(activity);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setRetainInstance(true);
		super.onCreate(savedInstanceState);

		page_id = getArguments().getInt("page_id");
			((MainActivity) getActivity()).extras = new Bundle();
			((MainActivity) getActivity()).extras.putInt("page_id", page_id);
			((MainActivity) getActivity()).bodyFragment = "PanoramaFragment";
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.pano_fragment, container, false);
		RelativeLayout pano_relativeLayout = (RelativeLayout)view.findViewById(R.id.pano_relativeLayout);
		try {
			page = realm.where(Child_pages.class).equalTo("id_cp",page_id).findFirst(); //appController.getChildPageDao().queryForId(page_id);
		
		
		panorama2 = new PLCubicPanorama();
		panorama2.setRotateSensitivity(1f);
		panorama2.setPitchEnabled(true);
		panorama2.setPitchRange(-1f, 1f );
		PLCubeFaceOrientation faceFace = PLCubeFaceOrientation.PLCubeFaceOrientationFront;
		PLCubeFaceOrientation faceDroite = PLCubeFaceOrientation.PLCubeFaceOrientationRight;
		PLCubeFaceOrientation faceRetour = PLCubeFaceOrientation.PLCubeFaceOrientationBack;
		PLCubeFaceOrientation faceGauche = PLCubeFaceOrientation.PLCubeFaceOrientationLeft;
		PLCubeFaceOrientation faceHaut = PLCubeFaceOrientation.PLCubeFaceOrientationUp;
		PLCubeFaceOrientation faceBas = PLCubeFaceOrientation.PLCubeFaceOrientationDown;
		
		alreadyResized = false;
		if (page != null) {
			Illustration imagesToResize = page.getIllustration();
			List<Illustration> list = new ArrayList<Illustration>();
			list.add(imagesToResize);
			if (true) {

				//Collections.sort(list);
				if(list.size()>5){
				for (int i = 0; i < list.size(); i++) {
					Illustration illustration = list.get(i);
					String realPath = illustration.getPath();
					if (i == 0) {
						imageFace = PLImage.imageWithBitmap(PLUtils.getBitmap(	getActivity(), "file://" + realPath));
						imageFace.scale(1024, 1024);
					} else if (i == 1) {
						imageDroite = PLImage	.imageWithBitmap(PLUtils.getBitmap(	getActivity(), "file://" + realPath));
						imageDroite.scale(1024, 1024);
					} else if (i == 2) {
						imageRetour = PLImage
								.imageWithBitmap(PLUtils.getBitmap(	getActivity(), "file://" + realPath));
						imageRetour.scale(1024, 1024);
					} else if (i == 3) {
						imageGauche = PLImage
								.imageWithBitmap(PLUtils.getBitmap(	getActivity(), "file://" + realPath));
						imageGauche.scale(1024, 1024);
					} else if (i == 4) {
						imageHaut = PLImage.imageWithBitmap(PLUtils.getBitmap(	getActivity(), "file://" + realPath));
						imageHaut.scale(1024, 1024);
					} else if (i == 5) {
						imageBas = PLImage.imageWithBitmap(PLUtils.getBitmap(		getActivity(), "file://" + realPath));
						imageBas.scale(1024, 1024);
					}
				}
				}else {
					Toast.makeText(getActivity(), "Il faudra y'avoir 6 images au moins pour une vue panoramique correct", Toast.LENGTH_SHORT).show();
					view.setBackgroundColor(colors.getColor(colors.getBackground_color()));
					return view;
				}

				panorama2.setImage(mainActivity.getCurrentGL(), imageFace,	faceFace);
				panorama2.setImage(mainActivity.getCurrentGL(), imageDroite,	faceDroite);
				panorama2.setImage(mainActivity.getCurrentGL(), imageRetour, faceRetour);
				panorama2.setImage(mainActivity.getCurrentGL(), imageGauche, faceGauche);
				panorama2.setImage(mainActivity.getCurrentGL(), imageHaut,	faceHaut);
				panorama2.setImage(mainActivity.getCurrentGL(), imageBas,	faceBas);
				mainActivity.createPanorama(panorama2);
			}
			if (((ViewGroup) mainActivity.surfaceView.getParent()) != null) {
				((ViewGroup) mainActivity.surfaceView.getParent()).removeView(mainActivity.surfaceView);
			}
		}else {
			Intent intent = new Intent(getActivity(), MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}

		} catch (Exception e) {
			//Crashlytics.logException(e);
		}
		
		try {
			pano_relativeLayout.addView(mainActivity.surfaceView);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return view;
		
	}

	

	private int indexOfPage() {
		int index = -1;
		for (int i = 0; i < pages.size(); i++) {
			if (page.getId_cp()==pages.get(i).getId_cp()) {
				index = i;
			}
		}
		return index;
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
		Runtime.getRuntime().gc();
//		imageLoader.clearMemoryCache();
		super.onDestroy();
	}
	
	public class ResizerAsyncTasc extends AsyncTask<RealmList<Illustration>, Integer, String>{

		/** progress dialog to show user that the backup is processing. */
	    public ProgressDialog dialog;

	    
		/**
		 * 
		 */
		/*public ResizerAsyncTasc(Collection<ImagePage> images) {
			super();
			dialog = new ProgressDialog(getActivity());
			this.images = images;
		}*/

		@Override
		protected String doInBackground(RealmList<Illustration>... params) {
			RealmList<Illustration> imagesToResize = params[0];
			for (Iterator<Illustration> iterator = imagesToResize.iterator(); iterator.hasNext();) {

                Illustration imagePage = (Illustration) iterator.next();

				ImageHelper.resizeSaveImageOnDisk(imagePage.getPath());
				
			}
			return null;
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(String result) {
			if (this.dialog.isShowing()) {
				this.dialog.dismiss();
	        }
			/*PLCubeFaceOrientation faceFace = PLCubeFaceOrientation.PLCubeFaceOrientationFront;
			PLCubeFaceOrientation faceDroite = PLCubeFaceOrientation.PLCubeFaceOrientationRight;
			PLCubeFaceOrientation faceRetour = PLCubeFaceOrientation.PLCubeFaceOrientationBack;
			PLCubeFaceOrientation faceGauche = PLCubeFaceOrientation.PLCubeFaceOrientationLeft;
			PLCubeFaceOrientation faceHaut = PLCubeFaceOrientation.PLCubeFaceOrientationUp;
			PLCubeFaceOrientation faceBas = PLCubeFaceOrientation.PLCubeFaceOrientationDown;
			for (Iterator<ImagePage> iterator = images.iterator(); iterator.hasNext();) {
				ImagePage image = (ImagePage) iterator.next();
				Illustration illustration = image.getIllustration();
				if (illustration.getPath().contains("_1")) {
					String[] path = illustration.getPath().split("/");
					String realPath = (new File(Environment.getExternalStorageDirectory(),"/Paperpad/pano/"+path[path.length-3]+"/"+path[path.length-1])).getAbsolutePath();
					Bitmap bm = BitmapFactory.decodeFile(realPath);
					imageDroite = PLImage.imageWithBitmap(bm);
				}else if (illustration.getPath().contains("_2")) {
					String[] path = illustration.getPath().split("/");
					String realPath = (new File(Environment.getExternalStorageDirectory(),"/Paperpad/pano/"+path[path.length-3]+"/"+path[path.length-1])).getAbsolutePath();
					Bitmap bm = BitmapFactory.decodeFile(realPath);
					imageRetour = PLImage.imageWithBitmap(bm);
				}else if (illustration.getPath().contains("_3")) {
					String[] path = illustration.getPath().split("/");
					String realPath = (new File(Environment.getExternalStorageDirectory(),"/Paperpad/pano/"+path[path.length-3]+"/"+path[path.length-1])).getAbsolutePath();
					Bitmap bm = BitmapFactory.decodeFile(realPath);
					imageGauche = PLImage.imageWithBitmap(bm);
				}else if (illustration.getPath().contains("_4")) {
					String[] path = illustration.getPath().split("/");
					String realPath = (new File(Environment.getExternalStorageDirectory(),"/Paperpad/pano/"+path[path.length-3]+"/"+path[path.length-1])).getAbsolutePath();
					Bitmap bm = BitmapFactory.decodeFile(realPath);
					imageHaut = PLImage.imageWithBitmap(bm);
				}else if (illustration.getPath().contains("_5")) {
					String[] path = illustration.getPath().split("/");
					String realPath = (new File(Environment.getExternalStorageDirectory(),"/Paperpad/pano/"+path[path.length-3]+"/"+path[path.length-1])).getAbsolutePath();
					Bitmap bm = BitmapFactory.decodeFile(realPath);
					imageBas = PLImage.imageWithBitmap(bm);
				}else if (illustration.getPath().contains("_01")) {
					String[] path = illustration.getPath().split("/");
					String realPath = (new File(Environment.getExternalStorageDirectory(),"/Paperpad/pano/"+path[path.length-3]+"/"+path[path.length-1])).getAbsolutePath();
					Bitmap bm = BitmapFactory.decodeFile(realPath);
					imageFace = PLImage.imageWithBitmap(bm);
				}
			}
//			imageFace = PLImage.imageWithBitmap(PLUtils.getBitmap(getActivity(), R.raw.chambre03_01));
//			imageDroite = PLImage.imageWithBitmap(PLUtils.getBitmap(getActivity(), R.raw.chambre03_1));
//			imageRetour = PLImage.imageWithBitmap(PLUtils.getBitmap(getActivity(), R.raw.chambre03_2));
//			imageGauche =PLImage.imageWithBitmap(PLUtils.getBitmap(getActivity(), R.raw.chambre03_3));
//			imageHaut = PLImage.imageWithBitmap(PLUtils.getBitmap(getActivity(), R.raw.chambre03_4));
//			imageBas = PLImage.imageWithBitmap(PLUtils.getBitmap(getActivity(), R.raw.chambre03_5));



			panorama2.setImage(mainActivity.getCurrentGL(), imageFace , faceFace );
			panorama2.setImage(mainActivity.getCurrentGL(), imageDroite  , faceDroite );
			panorama2.setImage(mainActivity.getCurrentGL(), imageRetour , faceRetour );
			panorama2.setImage(mainActivity.getCurrentGL(), imageGauche, faceGauche );
			panorama2.setImage(mainActivity.getCurrentGL(), imageHaut , faceHaut );
			panorama2.setImage(mainActivity.getCurrentGL(), imageBas , faceBas );

			mainActivity.createPanorama(panorama2);*/
			refresher.onRefreshRequested(page_id);
			super.onPostExecute(result);
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@Override
		protected void onPreExecute() {
			this.dialog.setMessage(getResources().getString(R.string.waiting));
            this.dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            this.dialog.setIndeterminate(false);
            this.dialog.setCancelable(false);
            this.dialog.show();
			super.onPreExecute();
		}
		
	}
	
	Refresher refresher = new Refresher() {
		
		@Override
		public void onRefreshRequested(int id) {
			// TODO Auto-generated method stub
			
		}
	};
	
	public interface Refresher{
		public void onRefreshRequested(int id);
	}

}
