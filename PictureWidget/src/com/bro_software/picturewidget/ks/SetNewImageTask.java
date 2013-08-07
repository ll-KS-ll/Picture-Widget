package com.bro_software.picturewidget.ks;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.RemoteViews;

public class SetNewImageTask {
	
	private Context context = null;
	
	public SetNewImageTask(Context context){
		this.context = context;
	}
	
	public SetNewImageTask(Context context, String subreddit){
		this.context = context;
		new SetNewImageAsyncTask().execute(subreddit);
	}
	
	public void setNewImage(String subreddit){
		new SetNewImageAsyncTask().execute(subreddit);
	}
	
	private class SetNewImageAsyncTask extends AsyncTask<String, Void, Bitmap> {

		@Override
		protected Bitmap doInBackground(String... params) {
			
			String imageURL = RedditHandler.getLastPostImageURL(params[0]);
			if (imageURL != null) {
				try {
					//Load image from a URL
					return BitmapFactory.decodeStream((InputStream) new URL(imageURL).getContent());
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
			//Set the image
			RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
			remoteView.setImageViewBitmap(R.id.image_holder, result);
			
			ComponentName componentName = new ComponentName(context, PictureWidget.class);
			AppWidgetManager.getInstance(context).updateAppWidget(componentName, remoteView);
		}

	}
}
