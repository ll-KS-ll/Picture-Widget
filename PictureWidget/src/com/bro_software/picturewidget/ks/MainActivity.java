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

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;

public class MainActivity extends Activity {

	private final String URL_TEMPLATE= "http://www.reddit.com/r/SUBREDDIT_NAME/hot.json";
	
	private ImageView frame;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		frame = (ImageView) findViewById(R.id.imageView1);
		new SetNewImageTask().execute("wallpapers");
	}

	public String getLastPostImageURL(String subreddit){
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
			
			return lastPost.optString("url");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		// Return null if image URL couldn't be retrieved
		return null;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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
			frame.setImageBitmap(result);
		}

	}

}
