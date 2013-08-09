package com.bro_software.picturewidget.ks;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.RemoteViews;

public class SetNewImageTask {

	private Context context = null;
	private int appWidgetID;

	public SetNewImageTask(Context context, int appWidgetID) {
		this.context = context;
		this.appWidgetID = appWidgetID;
	}

	public SetNewImageTask(Context context, int appWidgetID, String subreddit) {
		this.context = context;
		this.appWidgetID = appWidgetID;
		new SetNewImageAsyncTask().execute(subreddit);
	}

	private Point getScreenSize() {
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point screenSize = new Point();
		display.getSize(screenSize);
		return screenSize;
	}

	public void setNewImage(String subreddit) {
		new SetNewImageAsyncTask().execute(subreddit);
	}

	private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		Log.d("SetNewImageTask:calculateInSampleSize", "Screen size: " + reqWidth+ "x" + reqHeight);
		Log.d("SetNewImageTask:calculateInSampleSize", "Image size: " + width+ "x" + height);
		
		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			
			Log.d("SetNewImageTask:calculateInSampleSize", "Image samplesize: " + widthRatio+ "x" + heightRatio);
			
			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		
		Log.d("SetNewImageTask:calculateInSampleSize", "Returned image sapmlesize: " + inSampleSize);
		return inSampleSize;
	}
	
	private class SetNewImageAsyncTask extends AsyncTask<String, Void, Bitmap> {

		@Override
		protected Bitmap doInBackground(String... params) {

			String imageURL = RedditHandler.getLastPostImageURL(params[0]);
			if (imageURL != null) {
				try {
					// Load image from a URL
					
					// First decode with inJustDecodeBounds=true to check dimensions
				    final BitmapFactory.Options options = new BitmapFactory.Options();
				    options.inJustDecodeBounds = true;
				    BitmapFactory.decodeStream((InputStream) new URL(imageURL).getContent(), null, options);

				    // Calculate inSampleSize
				    Point screenSize = getScreenSize();
				    options.inSampleSize = calculateInSampleSize(options, screenSize.x, screenSize.y);

				    // Decode bitmap with inSampleSize set
				    options.inJustDecodeBounds = false;
				    //return BitmapFactory.decodeResource(res, resId, options);
					
				    //Bitmap bmp = BitmapFactory.decodeStream((InputStream) new URL(imageURL).getContent(), null, options);
				    
				    //Bitmap dstBmp = Bitmap.createBitmap(bmp, srcBmp.getWidth()/2 - srcBmp.getHeight()/2, 0, srcBmp.getHeight(), srcBmp.getHeight());
				    
					return BitmapFactory.decodeStream((InputStream) new URL(imageURL).getContent(), null, options);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			// Set the image
			RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
			remoteView.setImageViewBitmap(R.id.image_holder, result);

			getScreenSize();
			Log.d("SetNewImageAsyncTask:onPostExecute", "Image bytes: " + result.getByteCount() / 1024);

			AppWidgetManager.getInstance(context).updateAppWidget(appWidgetID,
					remoteView);
		}

	}
}
