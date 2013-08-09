package com.bro_software.picturewidget.ks;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class PictureWidget extends AppWidgetProvider{

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		Log.d("PictureWidget:onUpdate", "onUpdate called.");
		for(int n = 0; n < appWidgetIds.length; n++){
			SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
			String subreddit = sharedPref.getString("subreddit_" + appWidgetIds[n], "wallpapers");
			new SetNewImageTask(context, appWidgetIds[n]).setNewImage(subreddit);
		}
	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		super.onDeleted(context, appWidgetIds);
		
		for(int n = 0; n < appWidgetIds.length; n++){
			SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
			sharedPref.edit().remove("subreddit_" + appWidgetIds[n]).commit();
		}
	}

}
