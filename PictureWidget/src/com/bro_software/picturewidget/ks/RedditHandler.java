package com.bro_software.picturewidget.ks;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

public class RedditHandler {

	private static final String URL_TEMPLATE= "http://www.reddit.com/r/SUBREDDIT_NAME/hot.json";
	
	/**
	 * 
	 * @param subreddit
	 * @return
	 */
	public static String getLastPostImageURL(String subreddit){
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
}
