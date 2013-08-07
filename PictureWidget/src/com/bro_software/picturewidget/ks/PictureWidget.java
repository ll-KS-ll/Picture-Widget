package com.bro_software.picturewidget.ks;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.util.Log;

public class PictureWidget extends AppWidgetProvider{

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		Log.d("PictureWidget:onUpdate", "onUpdate called.");
		new SetNewImageTask(context).setNewImage("cityporn");
	}

	
}
