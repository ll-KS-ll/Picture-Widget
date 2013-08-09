package com.bro_software.picturewidget.ks;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.TextView;

public class WidgetConfigureActivity extends Activity implements
		OnItemClickListener {

	private int awID;
	private AppWidgetManager awm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.widget_configure_layout);

		
		
		Bundle extras = getIntent().getExtras();
		if (extras != null)
			awID = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
		else
			finish();
		awm = AppWidgetManager.getInstance(WidgetConfigureActivity.this);
		ListView subredditList = (ListView) findViewById(R.id.subreddit_list);
		subredditList.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
		TextView title = (TextView) view.findViewById(android.R.id.text1);
		String subreddit = title.getText().toString();
		
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(WidgetConfigureActivity.this);
		sharedPref.edit().putString("subreddit_" + awID, subreddit).commit();
		
		new SetNewImageTask(WidgetConfigureActivity.this, awID, subreddit);

		RemoteViews remoteView = new RemoteViews(WidgetConfigureActivity.this.getPackageName(), R.layout.widget_layout);
		awm.updateAppWidget(awID, remoteView);

		Intent resultValue = new Intent();
		resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, awID);
		setResult(RESULT_OK, resultValue);

		finish();

	}
}
