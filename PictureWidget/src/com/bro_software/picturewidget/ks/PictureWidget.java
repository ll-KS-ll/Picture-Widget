package com.bro_software.picturewidget.ks;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.RemoteViews;

public class PictureWidget extends AppWidgetProvider{

	private final String URL_TEMPLATE= "http://www.reddit.com/r/SUBREDDIT_NAME/hot.json";
	private Context context = null;

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		Log.d("PictureWidget:onUpdate", "onUpdate called.");
		this.context = context;
		new SetNewImageTask().execute("cityporn");
	}

	
	/**
	 * 
	 * @param subreddit
	 * @return
	 */
	private String getLastPostImageURL(String subreddit){
		Log.d("PictureWidget:getLastPostImageURL", "Start getting an image url fron subreddit /r/" + subreddit);
		// Build URL to subreddit
		String url = URL_TEMPLATE.replace("SUBREDDIT_NAME", subreddit);

		try {
			// Get raw JSON data from subreddit
			HttpGet getSubreddit = new HttpGet(url.toString());
			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(getSubreddit);
			String JSONData = EntityUtils.toString(response.getEntity());
			
			JSONObject data = new JSONObject(JSONData).getJSONObject("data");
			JSONArray posts = data.getJSONArray("children");
			JSONObject lastPost = posts.getJSONObject(0).getJSONObject("data");
			
			Log.d("PictureWidget:getLastPostImageURL", "Found image URL: " + lastPost.optString("url"));
			return lastPost.optString("url");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		// Return null if image URL couldn't be retrieved
		Log.d("PictureWidget:getLastPostImageURL", "Couldn't find image URL.");
		return null;
	}
	
	private class SetNewImageTask extends AsyncTask<String, Void, Bitmap> {

		@Override
		protected Bitmap doInBackground(String... params) {
			
			String imageURL = getLastPostImageURL(params[0]);
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
