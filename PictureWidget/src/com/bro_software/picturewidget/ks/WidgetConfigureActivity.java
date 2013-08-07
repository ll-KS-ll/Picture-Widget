package com.bro_software.picturewidget.ks;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RemoteViews;

public class WidgetConfigureActivity extends Activity implements OnClickListener{

	private int awID;
	private AppWidgetManager awm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.widget_configure_layout);
		
		Bundle extras = getIntent().getExtras();
		if(extras != null)
			awID = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
		else
			finish();
		awm = AppWidgetManager.getInstance(WidgetConfigureActivity.this);
		LinearLayout layout = (LinearLayout) findViewById(R.id.LinearLayout1);
		layout.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		new SetNewImageTask(WidgetConfigureActivity.this, "wallpapers");
		
		RemoteViews remoteView = new RemoteViews(WidgetConfigureActivity.this.getPackageName(), R.layout.widget_layout);
		awm.updateAppWidget(awID, remoteView);
		
		Intent resultValue = new Intent();
		resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, awID);
		setResult(RESULT_OK, resultValue);
		
		finish();
	}
}
