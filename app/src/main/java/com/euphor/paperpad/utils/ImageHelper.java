/**
 * 
 */
package com.euphor.paperpad.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author euphordev02
 *
 */
public class ImageHelper {
	
	private static final String TAG = "ImageHelper";
	
	public static Bitmap loadResizedBitmap(int reqSize, String filePath){
		Bitmap bitMapImage = null;
        // First, get the dimensions of the image
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        
        int scale = 0;
        
        
		return null;
		
	}

	public static int calculateInSampleSize2(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
    // Raw height and width of image
    final int height = options.outHeight;
    final int width = options.outWidth;
    int inSampleSize = 1;

    if (height > reqHeight || width > reqWidth) {

        // Calculate ratios of height and width to requested height and width
        final int heightRatio = Math.round((float) height / (float) reqHeight);
        final int widthRatio = Math.round((float) width / (float) reqWidth);

        // Choose the smallest ratio as inSampleSize value, this will guarantee
        // a final image with both dimensions larger than or equal to the
        // requested height and width.
        inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
    }

    return inSampleSize;
}
	
	public static Bitmap resizeBitMapImage1(String filePath, int targetWidth,
            int targetHeight) {
        Bitmap bitMapImage = null;
        // First, get the dimensions of the image
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        double sampleSize = 0;
        // Only scale if we need to
        // (16384 buffer for img processing)
        /* Boolean scaleByHeight = Math.abs(options.outHeight - targetHeight) >= Math
                .abs(options.outWidth - targetWidth);

       if (options.outHeight * options.outWidth * 2 >= 1638) {
            // Load, scaling to smallest power of 2 that'll get it <= desired
            // dimensions
            sampleSize = scaleByHeight ? options.outHeight / targetHeight
                    : options.outWidth / targetWidth;
            sampleSize = (int) Math.pow(2d,
                    Math.floor(Math.log(sampleSize) / Math.log(2d)));
        }*/
        int scale = 1;
        while(options.outWidth/scale/2>=targetWidth && options.outHeight/scale/2>=targetHeight)
            scale*=2;

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
       
        o2.inTempStorage = new byte[32*1024];
        o2.inDither=false;                     //Disable Dithering mode

        o2.inPurgeable=true;                   //Tell to gc that whether it needs free memory, the Bitmap can be cleared

        o2.inInputShareable=true;  

        // Do the actual decoding
        o2.inJustDecodeBounds = false;
//        options.inTempStorage = new byte[128];
        o2.inPreferredConfig = Bitmap.Config.ARGB_8888;
        while (true) {
            try {
            	 o2.inSampleSize = scale;
//                options.inSampleSize = (int) sampleSize;
                bitMapImage = BitmapFactory.decodeFile(filePath, o2);

                break;
            } catch (Exception ex) {
                try {
                	scale = scale * 2;
                } catch (Exception ex1) {

                }
            }
        }

        return bitMapImage;
    }
	
	public static Bitmap decodeUri(Uri selectedImage, Context ctx, int REQUIRED_WIDTH, int REQUIRED_HEIGHT) throws FileNotFoundException {

        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(ctx.getContentResolver().openInputStream(selectedImage), null, o);

        // The new size we want to scale to
        

        // Find the correct scale value. It should be the power of 2.
        
        int scale = 1;
        /* int width_tmp = o.outWidth, height_tmp = o.outHeight;
        while (true) {
            if (width_tmp / 2 < REQUIRED_WIDTH
               || height_tmp / 2 < REQUIRED_HEIGHT) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }*/
        while(o.outWidth/scale/2>=REQUIRED_WIDTH && o.outHeight/scale/2>=REQUIRED_HEIGHT)
            scale*=2;

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        o2.inTempStorage = new byte[32*1024];
        o2.inDither=false;                     //Disable Dithering mode

        o2.inPurgeable=true;                   //Tell to gc that whether it needs free memory, the Bitmap can be cleared

        o2.inInputShareable=true;              //Which kind of reference will be used to recover the Bitmap data after being clear, when it will be used in the future


        o2.inPreferredConfig = Bitmap.Config.RGB_565;
	// Decode bitmap with inSampleSize set
        o2.inJustDecodeBounds = false;
        return BitmapFactory.decodeStream(ctx.getContentResolver().openInputStream(selectedImage), null, o2);

    }
	
	public static void resizeSaveImageOnDisk(String originalPath) {

		String[] path = originalPath.split("/");
		String fileName = path[path.length - 1];
		File file = new File(Environment.getExternalStorageDirectory(),
				"/Paperpad/pano/" +path[path.length-3]+"/" + fileName);
		FileOutputStream fOut;
		if (!file.exists()) {
			originalPath.replaceAll(".jpg", "resized.jpg");
			Bitmap out = resizeBitMapImage1(originalPath, 500, 500);
//			Bitmap.createScaledBitmap(bitmap, width, height, true);
			File cacheDir = new File(Environment.getExternalStorageDirectory(),
					"/Paperpad/pano/" +path[path.length-3]+"/");
			// Check if cache folder exists, otherwise create folder.
			if (!cacheDir.exists())
				cacheDir.mkdirs();
			file = new File(Environment.getExternalStorageDirectory(),
					"/Paperpad/pano/" +path[path.length-3]+"/" + fileName);

			try {

				// file.delete();
				// file = new
				// File(dirExt,"/Paperpad/pano/"/*+path[path.length-3]+"/"*/+fileName);

				fOut = new FileOutputStream(file);
				out.compress(Bitmap.CompressFormat.PNG, 0, fOut);
				fOut.flush();
				fOut.close();

				// b.recycle();
				out.recycle();

			} catch (Exception e) { // TODO

			}
		}
	}
	
	
	public static void resize(String pathOfInputImage, int dstWidth, int dstHeight, String pathOfOutputImage){
		try
		{
		    int inWidth = 0;
		    int inHeight = 0;

		    InputStream in = new FileInputStream(pathOfInputImage);

		    // decode image size (decode metadata only, not the whole image)
		    BitmapFactory.Options options = new BitmapFactory.Options();
		    options.inJustDecodeBounds = true;
		    BitmapFactory.decodeStream(in, null, options);
		    in.close();
		    in = null;

		    // save width and height
		    inWidth = options.outWidth;
		    inHeight = options.outHeight;

		    // decode full image pre-resized
		    in = new FileInputStream(pathOfInputImage);
		    options = new BitmapFactory.Options();
		    // calc rought re-size (this is no exact resize)
		    options.inSampleSize = calculateInSampleSize(options, dstWidth, dstHeight);
//		    options.inSampleSize = Math.max(inWidth/dstWidth, inHeight/dstHeight);
		    // decode full image
		    Bitmap roughBitmap = BitmapFactory.decodeStream(in, null, options);

		    // calc exact destination size
		    Matrix m = new Matrix();
		    RectF inRect = new RectF(0, 0, roughBitmap.getWidth(), roughBitmap.getHeight());
		    RectF outRect = new RectF(0, 0, dstWidth, dstHeight);
		    m.setRectToRect(inRect, outRect, Matrix.ScaleToFit.CENTER);
		    float[] values = new float[9];
		    m.getValues(values);

		    // resize bitmap
		    Bitmap resizedBitmap = Bitmap.createScaledBitmap(roughBitmap, (int) (roughBitmap.getWidth() * values[0]), (int) (roughBitmap.getHeight() * values[4]), true);

		    // save image
		    try
		    {
		        FileOutputStream out = new FileOutputStream(pathOfOutputImage);
		        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
		    }
		    catch (Exception e)
		    {
		        Log.e("Image", e.getMessage(), e);
		    }
		}
		catch (IOException e)
		{
		    Log.e("Image", e.getMessage(), e);
		}

	}
	
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
    public static Bitmap decodeSampledBitmapFromUri(String path, int quality) {
		Bitmap bm = null;

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);

		// Calculate inSampleSize
		options.inSampleSize = quality;
		//options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		 options.inTempStorage = new byte[32*1024];
	        options.inDither=false;                     //Disable Dithering mode

	        options.inPurgeable=true;                   //Tell to gc that whether it needs free memory, the Bitmap can be cleared

	        options.inInputShareable=true;              //Which kind of reference will be used to recover the Bitmap data after being clear, when it will be used in the future


	        options.inPreferredConfig = Bitmap.Config.RGB_565;
		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		bm = BitmapFactory.decodeFile(path, options);

		return bm;
	}
    public static Bitmap decodeSampledBitmapFromUri(String path, int reqWidth, int reqHeight) {
		Bitmap bm = null;

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);

		// Calculate inSampleSize
//		options.inSampleSize = quality;
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		 options.inTempStorage = new byte[32*1024];
	        options.inDither=false;                     //Disable Dithering mode

	        options.inPurgeable=true;                   //Tell to gc that whether it needs free memory, the Bitmap can be cleared

	        options.inInputShareable=true;              //Which kind of reference will be used to recover the Bitmap data after being clear, when it will be used in the future


	        options.inPreferredConfig = Bitmap.Config.RGB_565;
		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		bm = BitmapFactory.decodeFile(path, options);

		return bm;
	}
    
    private static int calculateInSampleSize(

    		BitmapFactory.Options options, int reqWidth, int reqHeight) {
    			// Raw height and width of image
    			final int height = options.outHeight;
    			final int width = options.outWidth;
    			int inSampleSize = 1;

    			/*if (height > reqHeight || width > reqWidth) {
    				if (width > height) {
    					inSampleSize = Math.round((float) height / (float) reqHeight);
    				} else {
    					inSampleSize = Math.round((float) width / (float) reqWidth);
    				}
    			}*/
    			
    			if (height > reqHeight || width > reqWidth) {

    		        // Calculate ratios of height and width to requested height and width
    		        final int heightRatio = Math.round((float) height / (float) reqHeight);
    		        final int widthRatio = Math.round((float) width / (float) reqWidth);

    		        // Choose the smallest ratio as inSampleSize value, this will guarantee
    		        // a final image with both dimensions larger than or equal to the
    		        // requested height and width.
    		        inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
    		    }


    			return inSampleSize;
    		}
    
    
    public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
    	 
    	int width = bm.getWidth();
    	 
    	int height = bm.getHeight();
    	 
    	float scaleWidth = ((float) newWidth) / width;
    	 
    	float scaleHeight = ((float) newHeight) / height;
    	 
    	// CREATE A MATRIX FOR THE MANIPULATION
    	 
    	Matrix matrix = new Matrix();
    	 
    	// RESIZE THE BIT MAP
    	 
    	matrix.postScale(scaleWidth, scaleHeight);
    	 
    	// RECREATE THE NEW BITMAP
    	 
    	Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
    	 
    	return resizedBitmap;
    	 
    	}
    
    private Bitmap getBitmap(String path) {

//    	Uri uri = getImageUri(path);
    	Uri uri = Uri.fromFile(new File(path));
    	InputStream in = null;
    	try {
    	    final int IMAGE_MAX_SIZE = 1200000; // 1.2MP
    	    ContentResolver mContentResolver = null;
//			in = mContentResolver.openInputStream(uri);
    	    in = new FileInputStream(path);
    	    // Decode image size
    	    BitmapFactory.Options o = new BitmapFactory.Options();
    	    o.inJustDecodeBounds = true;
    	    BitmapFactory.decodeStream(in, null, o);
    	    in.close();



    	    int scale = 1;
    	    while ((o.outWidth * o.outHeight) * (1 / Math.pow(scale, 2)) > 
    	          IMAGE_MAX_SIZE) {
    	       scale++;
    	    }
    	    Log.d(TAG, "scale = " + scale + ", orig-width: " + o.outWidth + ", orig-height: " + o.outHeight);

    	    Bitmap b = null;
//    	    in = mContentResolver.openInputStream(uri);
    	    in = new FileInputStream(path);
    	    if (scale > 1) {
    	        scale--;
    	        // scale to max possible inSampleSize that still yields an image
    	        // larger than target
    	        o = new BitmapFactory.Options();
    	        o.inSampleSize = scale;
    	        b = BitmapFactory.decodeStream(in, null, o);

    	        // resize to desired dimensions
    	        int height = b.getHeight();
    	        int width = b.getWidth();
    	        Log.d(TAG, "1th scale operation dimenions - width: " + width + ", height: " + height);

    	        double y = Math.sqrt(IMAGE_MAX_SIZE
    	                / (((double) width) / height));
    	        double x = (y / height) * width;

    	        Bitmap scaledBitmap = Bitmap.createScaledBitmap(b, (int) x, 
    	           (int) y, true);
    	        b.recycle();
    	        b = scaledBitmap;

    	        System.gc();
    	    } else {
    	        b = BitmapFactory.decodeStream(in);
    	    }
    	    in.close();

    	    Log.d(TAG, "bitmap size - width: " +b.getWidth() + ", height: " + 
    	       b.getHeight());
    	    return b;
    	} catch (IOException e) {
    	    Log.e(TAG, e.getMessage(),e);
    	    return null;
    	}
    }
    
    public static Bitmap resizeWithoutInSample(){
		return null;
    	
    }
}