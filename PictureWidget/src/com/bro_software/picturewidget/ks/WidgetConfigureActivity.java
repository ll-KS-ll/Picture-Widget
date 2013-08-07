package com.bro_software.picturewidget.ks;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.widget.RemoteViews;

public class WidgetConfigureActivity extends FragmentActivity implements WidgetConfigureFragment.NoticeDialogListener{

	private int awID;
	private AppWidgetManager awm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		DialogFragment dialog = new WidgetConfigureFragment();
        dialog.show(getSupportFragmentManager(), "WidgetConfigureFragment");
		
		Bundle extras = getIntent().getExtras();
		if(extras != null)
			awID = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
		else
			finish();
		awm = AppWidgetManager.getInstance(WidgetConfigureActivity.this);
	}


	@Override
	public void onDialogPositiveClick(DialogFragment dialog) {
		new SetNewImageTask(WidgetConfigureActivity.this, "wallpapers");
		
		RemoteViews remoteView = new RemoteViews(WidgetConfigureActivity.this.getPackageName(), R.layout.widget_layout);
		awm.updateAppWidget(awID, remoteView);
		
		Intent resultValue = new Intent();
		resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, awID);
		setResult(RESULT_OK, resultValue);
		
		finish();
	}

	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
		finish();
	}
}
