package com.android.systemui.statusbar;

import android.database.ContentObserver;
import android.os.Handler;

public class SettingsObserver extends ContentObserver{
	Handler handler;
	int message;
	
	public SettingsObserver(Handler handler, int message) {
		super(handler);
		this.handler = handler;
		this.message = message;
	}
	
	public void onChange(boolean selfChange){
		handler.sendEmptyMessage(message);
	}
}