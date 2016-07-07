package com.euphor.paperpad.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.euphor.paperpad.Beans.Illustration;
import com.euphor.paperpad.R;
import com.euphor.paperpad.activities.main.SplashActivity;


import com.todddavies.components.progressbar.ProgressWheel;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Random;

import io.realm.Realm;

/**
 * Created by Euphor on 23/03/2015.
 */
public class Utils1 {
    static boolean[] b = new boolean[]{
            false, false, false, false,
            false, false, false, false,
            false, false, false, false};
    static LinearLayout gridImgDownloaded;
    static int[][] imageRowColumn ;
    static final int rowCount = 3;
    static final int columnCount = 4;
    static int count = 0;
    private Context context;
    private static ProgressWheel pw;
    static  int nbr=10;
    static  int j;
    static  int id=0;
   public static Realm realm;
   public  static int nbr_cle = 98; //value
    public static Long time ;


    public Utils1() { }

    public static SplashActivity activity;


    public Utils1(SplashActivity _activity) {
        this.activity = _activity;
    }


    public void setContext(Context context) {
        this.context = context.getApplicationContext();
    }


    public static  Illustration Download_images(String link, Illustration illustration) throws IOException {


        String paths= "";
        String DIRECTORY_IMAGES = activity.getPackageName() + "_images";
        if (link != null && !link.isEmpty() && link.contains("http")) {
            //Log.e(" link ::::::::::::>"+link,"<::::::::");

            String path = link;

            path.replaceAll("http://backoffice.paperpad.fr/", "");
            String[] temp = path.split("/");
            String imageName = temp[temp.length - 1];
            //Log.i("Image Name", imageName);
            path.replaceAll(imageName, "");
            String newFilePath = "";
            int templength = temp.length;
            for (int j = 0; j < templength - 1; j++) {
                newFilePath = newFilePath + temp[j] + "/";
            }

            String root = getFirstWritableDirectory().toString();
            String optimized = "thumbs1";

            File myDir = new File(root + "/" + DIRECTORY_IMAGES + "/" + optimized);
            myDir.mkdirs();

           /* Log.e("path image", myDir.getAbsolutePath()+"/"+imageName);*/

            // Setting up file to write the image to.

            File f = new File(myDir, imageName);


                if (!f.exists()) {
                HttpClient httpclient = new DefaultHttpClient();



                if (link != null && !link.isEmpty()) {

                    link = link.replaceAll(" ", "%20");

                    String[] tmp = link.split("/");
                    String nameImage = tmp[tmp.length - 1];
                    String correctName = URLEncoder.encode(nameImage);
                    link = link.replace(nameImage, correctName);
                    HttpGet httpget = new HttpGet(link);
                    //Log.e(":::::::::::>"+link,"<::::::::");
                    HttpResponse response = httpclient.execute(httpget);
                    System.out.println(response.getStatusLine());
                    HttpEntity entity = response.getEntity();
                    InputStream is = entity.getContent();
                    OutputStream os = new FileOutputStream(f);
                    ImageUtils.CopyStream(is, os);
                    final File file_illustration = f;

                    activity.runOnUiThread(new Runnable() {

                        public void run() {
                            //  activity.setContentView(R.layout.fragment_images);
                            activity.findViewById(R.id.progress_wheel).setVisibility(View.VISIBLE);
                            pw = (ProgressWheel)activity.findViewById(R.id.pw_spinner);




                            //  setContentView();
                            gridImgDownloaded = (LinearLayout) activity.findViewById(R.id.downloadedImageLayout);
                            gridImgDownloaded.setVisibility(View.VISIBLE);
                            Random r = new Random();
                            int index =0,
                                    row = 0,
                                    column = 0;

                            imageRowColumn = new int[][]{{R.id.img_column00, R.id.img_column01, R.id.img_column02, R.id.img_column03},
                                    {R.id.img_column10, R.id.img_column11, R.id.img_column12, R.id.img_column13},
                                    {R.id.img_column20, R.id.img_column21, R.id.img_column22, R.id.img_column23}};
                            index = r.nextInt(rowCount * columnCount);
                            if (b[index] == false || (count >= rowCount * columnCount)) {
                                count++;
                                b[index] = true;

                                if (index >= 0 && index < 4) {
                                    row = 0;
                                    column = 3 - index;
                                } else if (index >= 4 && index < 8) {
                                    row = 1;
                                    column = 7 - index;
                                } else if (index >= 8) {
                                    row = 2;
                                    column = 11 - index;
                                }
                                Glide.with(activity.getApplicationContext()).load(file_illustration).fitCenter().into((ImageView) gridImgDownloaded.findViewById(imageRowColumn[row][column]));

                                pw.incrementProgress((int) ((j / (float) nbr_cle) * 360));
                                j++;

                            }
                        }

                    });
                }
            }
            paths=(myDir.toString()+"/"+imageName);
            FileInputStream fis = new FileInputStream(f);
            Point point = ImageUtils.getSizeOfImage(fis);
            id++;
                illustration = new Illustration(id, link, paths, point.y, point.x);




        }


        return illustration;

    }
    public static File getFirstWritableDirectory() {
        File file1 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath());
        File file2 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath());
        File file3 = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        if (file1.exists()) {
            return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        } else if (file2.exists()) {
            return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        } else if (file3.exists()) {
            return Environment.getExternalStorageDirectory();
        } else {
            return Environment.getExternalStorageDirectory();
        }

    }
    public static boolean isTablet(Context context) {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }

}
