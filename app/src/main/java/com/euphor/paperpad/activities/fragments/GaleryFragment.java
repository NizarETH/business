/**
 * 
 */
package com.euphor.paperpad.activities.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.euphor.paperpad.R;
import com.euphor.paperpad.activities.main.MainActivity;
import com.euphor.paperpad.activities.main.MyApplication;
import com.euphor.paperpad.activities.main.ScreenSlideActivity;
import com.euphor.paperpad.Beans.Album;
import com.euphor.paperpad.Beans.Illustration;
import com.euphor.paperpad.Beans.Parameters;
import com.euphor.paperpad.Beans.Photo;

import com.euphor.paperpad.utils.Colors;
import com.euphor.paperpad.utils.ImageUtils;
import com.euphor.paperpad.utils.Utils;
import com.euphor.paperpad.utils.jsonUtils.AppHit;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.realm.Realm;

/**
 * @author euphordev02
 *
 */
public class GaleryFragment extends Fragment{


	private static final int HEIGHT_GALERY = 300;
	/**
	 * You'll need this in your class to cache the helper in the class.
	 */

	private Colors colors;
	private ArrayList<List<Illustration>> lines;
	private Album album;
	private boolean bottomNav;
	private boolean isTablet;
	private long time;
	private int id;
	int deviceWidth;
    public Realm realm;

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle arg0) {

		super.onCreate(arg0);
		setRetainInstance(true);


	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onDestroy()
	 */
	@Override
	public void onDestroy() {
		Runtime.getRuntime().gc();
		super.onDestroy();
	}



	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		ScrollView scrollView = (ScrollView) getView().findViewById(R.id.galerySV);
		String whereToScroll = getArguments().getString("whereToScroll");
		if (whereToScroll != null && !whereToScroll.isEmpty()) {
			int index = getIndexIllustration(whereToScroll);
			if (index > -1) {
				scrollView.scrollTo(0, index * HEIGHT_GALERY);
			}

		}

		super.onActivityCreated(savedInstanceState);
	}

	private int getIndexIllustration(String whereToScroll) {
		int index = -1;
		for (int i = 0; i < lines.size(); i++) {
			List<Illustration> line = lines.get(i);
			for (int j = 0; j < line.size(); j++) {
				if(whereToScroll.equalsIgnoreCase(line.get(j).getPath())){
					index = i;
					return index;
				}
			}
		}
		return index;
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
//new AppController(getActivity());
				realm = Realm.getInstance(getActivity());

		colors = ((MainActivity)activity).colors;
        com.euphor.paperpad.Beans.Parameters ParamColor = realm.where(com.euphor.paperpad.Beans.Parameters.class).findFirst();
        if (colors==null) {
            colors = new Colors(ParamColor);
        }

        int id_album = getArguments().getInt("Album_id");
		id = id_album;
		time =System.currentTimeMillis();
        album = realm.where(Album.class).equalTo("id_album",id_album).findFirst();//appController.getAlbumDao().queryForId(id_album);

        ((MainActivity)getActivity()).bodyFragment = "GaleryFragment";
		((MainActivity)getActivity()).extras = new Bundle();
		((MainActivity)getActivity()).extras.putInt("Album_id", album.getId_album());
		Parameters params = null;

        params = realm.where(Parameters.class).findFirst();// appController.getParametersDao().queryForId(1);


        if (params != null) {
            if (params.getNavigation_type()!= null) {
                if (params.getNavigation_type().contains("top_wide") || params.getNavigation_type().contains("bottom") || params.getNavigation_type().contains("top_narrow")) {
                    bottomNav = true;
                }else {
                    bottomNav = false;
                }
            }else {
                bottomNav = true;
            }
        }

        isTablet = Utils.isTablet(activity);

		super.onAttach(activity);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.galery_fragment, container, false);
		View toastView = inflater.inflate(R.layout.toast_galery, null, false);
		Toast toast = new Toast(getActivity());
		toast.setView(toastView);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.show();
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

		if (bottomNav || !isTablet) {
			deviceWidth = dm.widthPixels;
		}else {
			deviceWidth = dm.widthPixels - (int)getResources().getDimension(R.dimen.width_tab_fragment);
		}
		//		try {
		if (album!=null) {
			if (album.getTitle().isEmpty()) {
				view.findViewById(R.id.TitleHolder).setVisibility(View.GONE);

			}else {
				view.findViewById(R.id.TitleHolder).setBackgroundDrawable(colors.getForePD());//.setBackgroundColor(colors.getColor(colors.getForeground_color()));
				TextView titleContactsTV = (TextView)view.findViewById(R.id.TitleTV);
				titleContactsTV.setTypeface(MainActivity.FONT_TITLE);
				titleContactsTV.setText(album.getTitle());
				titleContactsTV.setTextColor(colors.getColor(colors.getBackground_color()));
			}
			List<Photo> photos = new ArrayList<Photo>();
			photos.addAll(album.getPhotos()) ;
			LinearLayout rootHolder = (LinearLayout)view.findViewById(R.id.container);
			rootHolder.setBackgroundColor(colors.getColor(colors.getBackground_color()));
			if (photos != null && photos.size()>0 && photos.iterator().next().getIllustration()!= null && !photos.iterator().next().getIllustration().getPath().isEmpty()) {

				//do lines of images 

				lines = new ArrayList<List<Illustration>>();
				List<Illustration> tmpList = new ArrayList<Illustration>();
				int sum = 0;
				for (Iterator<Photo> iterator = photos.iterator(); iterator.hasNext();) {
					Photo photo = (Photo) iterator.next();

					Illustration illustration = photo.getIllustration();
					if (illustration.getOriginalHeight()>0) {
						sum += ImageUtils.getHeight(
								illustration.getOriginalWidth(),
								illustration.getOriginalHeight(),
								HEIGHT_GALERY);
						if (sum < deviceWidth) {
							tmpList.add(illustration);
						} else {
							List<Illustration> list = new ArrayList<Illustration>();
							for (int j = 0; j < tmpList.size(); j++) {
								list.add(tmpList.get(j));
							}
							lines.add(list);
							tmpList.clear();
							tmpList.add(illustration);
							sum = ImageUtils.getHeight(
									illustration.getOriginalWidth(),
									illustration.getOriginalHeight(),
									HEIGHT_GALERY);
						}
					}/*else {
						GetSizeAsyncTask sizeAsyncTask = new GetSizeAsyncTask(illustration);
						sizeAsyncTask.execute(illustration);
						//							Point point = ImageUtils.getSizeOfImage(fis);

					}*/



				}
				if (tmpList.size()>0) {
					lines.add(tmpList);
				}



				if (lines.size()>0) {


					for (int j = 0; j < lines.size(); j++) {
						List<Illustration> line = lines.get(j);
						if (line.size()>0) {
							//							android.widget.LinearLayout.LayoutParams params = 
							//									new android.widget.LinearLayout.LayoutParams(
							//											android.widget.LinearLayout.LayoutParams.WRAP_CONTENT, 
							//											HEIGHT_GALERY);
							//							RelativeLayout rlContainer = new RelativeLayout(getActivity());
							//							rlContainer.setLayoutParams(params);

							LinearLayout lineHolder = new LinearLayout(getActivity());
							lineHolder.setTag(line);
							android.widget.RelativeLayout.LayoutParams paramsLL = 
									new android.widget.RelativeLayout.LayoutParams(
											android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT, 
											HEIGHT_GALERY);
							lineHolder.setLayoutParams(paramsLL);
							lineHolder.setOrientation(LinearLayout.HORIZONTAL);


							sum = 0;
							for (int k = 0; k < line.size(); k++) {
								sum += ImageUtils.getHeight(line.get(k).getOriginalWidth(), line.get(k).getOriginalHeight(), HEIGHT_GALERY);
							}
							int addThis = (int)((float)(deviceWidth-sum)/(float)(line.size()));
							if (j==(lines.size()-1)) {
								int quarterScreen = (int) ((float) deviceWidth / (float) 4);
								if (addThis > quarterScreen) {
									addThis = 0;
								}
							}
							for (int k = 0; k < line.size(); k++) {
								final Illustration illustration = line.get(k);
								int relativeWidth = ImageUtils.getHeight(line.get(k).getOriginalWidth(), line.get(k).getOriginalHeight(), HEIGHT_GALERY)+addThis;
								LinearLayout layImg = new LinearLayout(getActivity());
								android.widget.RelativeLayout.LayoutParams paramsLLImg = 
										new android.widget.RelativeLayout.LayoutParams(relativeWidth, HEIGHT_GALERY);
								layImg.setLayoutParams(paramsLLImg);
								//								layImg.setBackgroundColor(Color.WHITE);
								layImg.setPadding(5, 5, 5, 5);
								StateListDrawable backClickImg = new StateListDrawable();
								backClickImg.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(colors.getColor(colors.getForeground_color())));
								backClickImg.addState(new int[]{android.R.attr.state_focused}, new ColorDrawable(colors.getColor(colors.getForeground_color())));
								layImg.setBackgroundDrawable(backClickImg);
								layImg.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										Bundle bundle = new Bundle();
										bundle.putInt("id_illustration", illustration.getId_i());
										bundle.putInt("id_album", album.getId_album());
										Intent intent = new Intent(getActivity(), ScreenSlideActivity.class);
										intent.putExtras(bundle);
										intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

										getActivity().startActivity(intent);
									}
								});
								final ImageView image = new ImageView(getActivity());
								android.widget.LinearLayout.LayoutParams paramsImg = 
										new android.widget.LinearLayout.LayoutParams(
												android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 
												android.widget.LinearLayout.LayoutParams.MATCH_PARENT);

								image.setLayoutParams(paramsImg);
								image.setScaleType(ScaleType.CENTER_CROP);
								int width = ImageUtils.getHeight(illustration.getOriginalWidth(), illustration.getOriginalHeight(), HEIGHT_GALERY)+addThis-10;
								//								ImageSize imageSize = new ImageSize(width, HEIGHT_GALERY);
								String patImg = illustration.getPath();
								if (!patImg.isEmpty()) {
									patImg = "file:///" + illustration.getPath();
								}else {
									patImg = illustration.getLink();
								}
								Glide.with(getActivity()).load(new File(illustration.getPath())).override(width, HEIGHT_GALERY).centerCrop().into(image);
								//								imageLoader.loadImage(patImg,imageSize, imgLoadListener );//file:///mnt/sdcard/image.png //Paperpad/http:/backoffice.paperpad.fr/medias/14/optimized
								Log.i("Image size", "the height "+image.getHeight()+" the width "+image.getWidth());
								layImg.addView(image);
								lineHolder.addView(layImg);
							}
							//							rlContainer.addView(lay);
							rootHolder.addView(lineHolder);

							final ScrollView scrollView = (ScrollView)view.findViewById(R.id.galerySV);
							scrollView.setOnTouchListener(new OnTouchListener() {

								@Override
								public boolean onTouch(View v, MotionEvent event) {
									//									scrollView.post(new Runnable() {
									//										@Override
									//										public void run() {
									//										    Toast.makeText(getActivity(),"Current Y is : "+scrollView.getScrollY(),Toast.LENGTH_SHORT).show();
									//										            }
									//										        });
									return false;
								}
							});

						}
					}
				}


			}else if (photos != null && photos.size()>0 && photos.iterator().next().getIllustration()!= null && photos.iterator().next().getIllustration().getPath().isEmpty()) {
				if (isTablet) {
					int relativeWidth = (int) ((float)deviceWidth/(float)2);
					for (int i = 0; i < photos.size(); i+=2) {
						LinearLayout lineHolder = new LinearLayout(
								getActivity());
						android.widget.RelativeLayout.LayoutParams paramsLL = new android.widget.RelativeLayout.LayoutParams(
								android.widget.RelativeLayout.LayoutParams.MATCH_PARENT,
								HEIGHT_GALERY);
						lineHolder.setLayoutParams(paramsLL);
						lineHolder.setOrientation(LinearLayout.HORIZONTAL);
						
						if (i%2 == 0 && i<photos.size()) {
							Photo photo = photos.get(i);
							final Illustration illustration = photo
									.getIllustration();

							/*if (isTablet) {
							relativeWidth = (int) ((float)deviceWidth/(float)2);
							}else {
							relativeWidth = deviceWidth;
							}*/
							LinearLayout layImg = new LinearLayout(
									getActivity());
							LinearLayout.LayoutParams paramsLLImg = new LinearLayout.LayoutParams(
									0, HEIGHT_GALERY,1);
							layImg.setLayoutParams(paramsLLImg);
							//								layImg.setBackgroundColor(Color.WHITE);
							layImg.setPadding(5, 5, 5, 5);
							StateListDrawable backClickImg = new StateListDrawable();
							backClickImg.addState(
									new int[] { android.R.attr.state_pressed },
									new ColorDrawable(colors.getColor(colors
											.getForeground_color())));
							backClickImg.addState(
									new int[] { android.R.attr.state_focused },
									new ColorDrawable(colors.getColor(colors
											.getForeground_color())));
							layImg.setBackgroundDrawable(backClickImg);
							layImg.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									Bundle bundle = new Bundle();
									bundle.putInt("id_illustration",
											illustration.getId_i());
									bundle.putInt("id_album",
											album.getId_album());
									Intent intent = new Intent(getActivity(),
											ScreenSlideActivity.class);
									intent.putExtras(bundle);
									intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

									getActivity().startActivity(intent);
								}
							});
							final ImageView image = new ImageView(getActivity());
							android.widget.LinearLayout.LayoutParams paramsImg = new android.widget.LinearLayout.LayoutParams(
									android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
									android.widget.LinearLayout.LayoutParams.MATCH_PARENT);
							image.setLayoutParams(paramsImg);
							image.setScaleType(ScaleType.CENTER_CROP);
							//								ImageSize imageSize = new ImageSize(width, HEIGHT_GALERY);
							String patImg = illustration.getLink();
							/*if (!patImg.isEmpty()) {
								patImg = "file:///" + illustration.getPath();
							}else {
								patImg = illustration.getLink();
							}*/
							Glide.with(getActivity()).load(patImg)
							.into(image);
							//								imageLoader.loadImage(patImg,imageSize, imgLoadListener );//file:///mnt/sdcard/image.png //Paperpad/http:/backoffice.paperpad.fr/medias/14/optimized
							Log.i("Image size",
									"the height " + image.getHeight()
									+ " the width " + image.getWidth());
							layImg.addView(image);
							lineHolder.addView(layImg);

						}
						if (i+1<photos.size()) {

							Photo photo = photos.get(i+1);
							final Illustration illustration = photo
									.getIllustration();

							/*if (isTablet) {
							relativeWidth = (int) ((float)deviceWidth/(float)2);
							}else {
							relativeWidth = deviceWidth;
							}*/
							LinearLayout layImg = new LinearLayout(
									getActivity());
							LinearLayout.LayoutParams paramsLLImg = new LinearLayout.LayoutParams(
									0, HEIGHT_GALERY,1);
							layImg.setLayoutParams(paramsLLImg);
							//								layImg.setBackgroundColor(Color.WHITE);
							layImg.setPadding(5, 5, 5, 5);
							StateListDrawable backClickImg = new StateListDrawable();
							backClickImg.addState(
									new int[] { android.R.attr.state_pressed },
									new ColorDrawable(colors.getColor(colors
											.getForeground_color())));
							backClickImg.addState(
									new int[] { android.R.attr.state_focused },
									new ColorDrawable(colors.getColor(colors
											.getForeground_color())));
							layImg.setBackgroundDrawable(backClickImg);
							layImg.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									Bundle bundle = new Bundle();
									bundle.putInt("id_illustration",
											illustration.getId_i());
									bundle.putInt("id_album",
											album.getId_album());
									Intent intent = new Intent(getActivity(),
											ScreenSlideActivity.class);
									intent.putExtras(bundle);
									intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

									getActivity().startActivity(intent);
								}
							});
							final ImageView image = new ImageView(getActivity());
							android.widget.LinearLayout.LayoutParams paramsImg = new android.widget.LinearLayout.LayoutParams(
									android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
									android.widget.LinearLayout.LayoutParams.MATCH_PARENT);
							image.setLayoutParams(paramsImg);
							image.setScaleType(ScaleType.CENTER_CROP);
							//								ImageSize imageSize = new ImageSize(width, HEIGHT_GALERY);
							String patImg = illustration.getLink();
							/*if (!patImg.isEmpty()) {
								patImg = "file:///" + illustration.getPath();
							}else {
								patImg = illustration.getLink();
							}*/
							Glide.with(getActivity()).load(patImg)
							.into(image);
							//								imageLoader.loadImage(patImg,imageSize, imgLoadListener );//file:///mnt/sdcard/image.png //Paperpad/http:/backoffice.paperpad.fr/medias/14/optimized
							Log.i("Image size",
									"the height " + image.getHeight()
									+ " the width " + image.getWidth());
							layImg.addView(image);
							lineHolder.addView(layImg);

						}
						rootHolder.addView(lineHolder);
					}
				}else {

					int relativeWidth = deviceWidth;
					for (Iterator<Photo> iterator = photos.iterator(); iterator.hasNext();) {
						Photo photo = (Photo) iterator.next();

						final Illustration illustration = photo.getIllustration();
						LinearLayout lineHolder = new LinearLayout(getActivity());
						android.widget.RelativeLayout.LayoutParams paramsLL = 
								new android.widget.RelativeLayout.LayoutParams(
										android.widget.RelativeLayout.LayoutParams.MATCH_PARENT, 
										HEIGHT_GALERY);
						lineHolder.setLayoutParams(paramsLL);
						lineHolder.setOrientation(LinearLayout.HORIZONTAL);
						LinearLayout layImg = new LinearLayout(getActivity());
						/*if (isTablet) {
						relativeWidth = (int) ((float)deviceWidth/(float)2);
					}else {
						relativeWidth = deviceWidth;
					}*/
						android.widget.RelativeLayout.LayoutParams paramsLLImg = 
								new android.widget.RelativeLayout.LayoutParams(relativeWidth, HEIGHT_GALERY);
						layImg.setLayoutParams(paramsLLImg);
						//								layImg.setBackgroundColor(Color.WHITE);
						layImg.setPadding(5, 5, 5, 5);
						StateListDrawable backClickImg = new StateListDrawable();
						backClickImg.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(colors.getColor(colors.getForeground_color())));
						backClickImg.addState(new int[]{android.R.attr.state_focused}, new ColorDrawable(colors.getColor(colors.getForeground_color())));
						layImg.setBackgroundDrawable(backClickImg);
						layImg.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								Bundle bundle = new Bundle();
								bundle.putInt("id_illustration", illustration.getId_i());
								bundle.putInt("id_album", album.getId_album());
								Intent intent = new Intent(getActivity(), ScreenSlideActivity.class);
								intent.putExtras(bundle);
								intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

								getActivity().startActivity(intent);
							}
						});
						final ImageView image = new ImageView(getActivity());
						android.widget.LinearLayout.LayoutParams paramsImg = 
								new android.widget.LinearLayout.LayoutParams(
										android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 
										android.widget.LinearLayout.LayoutParams.MATCH_PARENT);

						image.setLayoutParams(paramsImg);
						image.setScaleType(ScaleType.CENTER_CROP);
						//								ImageSize imageSize = new ImageSize(width, HEIGHT_GALERY);
						String patImg = illustration.getLink();
						
//						if (!patImg.isEmpty()) {
//							patImg = "file:///" + illustration.getPath();
//						}else {
//							patImg = illustration.getLink();
//						}
						Glide.with(getActivity()).load(patImg).into(image);
						//								imageLoader.loadImage(patImg,imageSize, imgLoadListener );//file:///mnt/sdcard/image.png //Paperpad/http:/backoffice.paperpad.fr/medias/14/optimized
						Log.i("Image size", "the height "+image.getHeight()+" the width "+image.getWidth());
						layImg.addView(image);
						lineHolder.addView(layImg);
						rootHolder.addView(lineHolder);
					}

				}
			}
		}

		return view;
	}


	@Override
	public void onStop() {
		AppHit hit = new AppHit(System.currentTimeMillis()/1000, time/1000, "sales_gallery_section", id);
		((MyApplication)getActivity().getApplication()).hits.add(hit);
		super.onStop();
	}

	public class GetSizeAsyncTask extends AsyncTask<Illustration, String, Point>{
		Illustration illustration;
		@Override
		protected Point doInBackground(Illustration... params) {
			Illustration illustration = params[0];
			String link = illustration.getLink();
			Point point;
			if (link != null && !link.isEmpty()) {
				//				link = Utils.encodeUrl(link);
				try{
					link = link.replaceAll(" ", "%20");
					String[] tmp = link.split("/");
					String nameImage = tmp[tmp.length-1];
					String correctName = URLEncoder.encode(nameImage);
					link = link.replace(nameImage, correctName);
					// Prepare a request object
					HttpGet httpget = new HttpGet(link);
					// Execute the request
					HttpClient httpclient = new DefaultHttpClient();
					HttpResponse response;
					response = httpclient.execute(httpget);

					// Examine the response status
					System.out.println(response.getStatusLine());
					// Get hold of the response entity
					HttpEntity entity = response.getEntity();
					// Open InputStream to download the image. 
					//URLConnection connection = new URL("http://backoffice.paperpad.fr/"+listMedias.get(i).getPath()).openConnection();
					InputStream is = entity.getContent();

					//getHeight and width of image 
					point = ImageUtils.getSizeOfImage(is);
					illustration.setOriginalWidth(point.x);
					illustration.setOriginalHeight(point.y);

                    realm.copyToRealmOrUpdate(illustration);
                    //appController.getIllustrationDao().createOrUpdate(illustration);

                } catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (ClientProtocolException e1) {
					e1.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				// Set up OutputStream to write data into image file. 


				//						getHelper().getIllustrationDao().refresh(illustration);
			}
			return null;
		}

		@Override
		protected void onPostExecute(Point result) {
			int sum = 0;
			ArrayList<List<Illustration>> tmpList = new ArrayList<List<Illustration>>();
			/*if (illustration.getOriginalHeight()>0) {
				sum += ImageUtils.getHeight(
						illustration.getOriginalWidth(),
						illustration.getOriginalHeight(),
						HEIGHT_GALERY);

				if (sum < deviceWidth) {
					tmpList.add(illustration);
				} else {
					List<Illustration> list = new ArrayList<Illustration>();
					for (int j = 0; j < tmpList.size(); j++) {
						list.add(tmpList.get(j));
					}
					lines.add(list);
					tmpList.clear();
					tmpList.add(illustration);
					sum = ImageUtils.getHeight(
							illustration.getOriginalWidth(),
							illustration.getOriginalHeight(),
							HEIGHT_GALERY);
				}
			}*/
			super.onPostExecute(result);
		}

		public GetSizeAsyncTask(Illustration illustration) {
			super();
			this.illustration = illustration;
		}



	}

}
