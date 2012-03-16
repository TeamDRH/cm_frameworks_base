/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.systemui.statusbar.phone;

import com.android.systemui.R;
import com.android.systemui.statusbar.SettingsObserver;
import com.android.systemui.statusbar.preferences.DrhSettings;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.AttributeSet;
import android.view.Display;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.view.ViewGroup;
import android.widget.LinearLayout;


public class TrackingView extends LinearLayout {
    PhoneStatusBar mService;
    boolean mTracking;
    int mStartX, mStartY;
    Handler mHandler = new Handler();
    SettingsObserver mDrhSettingsObserver;
    static DrhSettings mDrhSettings = null;
    
    private static final int MSG_DRH_SYSTEMUI_SETTINGS_PHONE_BOTTOM = 1000;

    public TrackingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDrhSettingsObserver = new SettingsObserver(new H(), MSG_DRH_SYSTEMUI_SETTINGS_PHONE_BOTTOM);
        getContext().getContentResolver().registerContentObserver(Settings.System.getUriFor(Settings.System.DRH_SYSTEMUI_SETTINGS_PHONE_BOTTOM), true, mDrhSettingsObserver);
    }
    
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (Settings.System.getInt(getContext().getContentResolver(), Settings.System.DRH_SYSTEMUI_SETTINGS_ENABLED, 0) == 1 &&
        		Settings.System.getInt(getContext().getContentResolver(), Settings.System.DRH_SYSTEMUI_SETTINGS_PHONE_BOTTOM, 0) == 1 &&
        		mDrhSettings == null){
        	mDrhSettings = new DrhSettings((ViewGroup) findViewById(R.id.drh_settings), getContext());
        	findViewById(R.id.drh_settings).setVisibility(VISIBLE);
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        boolean down = event.getAction() == KeyEvent.ACTION_DOWN;
        switch (event.getKeyCode()) {
        case KeyEvent.KEYCODE_BACK:
            if (down) {
                //mService.deactivate();
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mService.onTrackingViewAttached();
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (visibility == VISIBLE) {
            mHandler.post(new Runnable() {
                @Override public void run() {
                    mService.updateExpandedViewPos(PhoneStatusBar.EXPANDED_LEAVE_ALONE);
                }
            });
            if (mDrhSettings != null){
            	mDrhSettings.attach();
            }
        }else {
        	if (mDrhSettings != null)
        		mDrhSettings.detach();
        }
    }
    
    private void updateDrhSystemUISettings () {
    	if (Settings.System.getInt(getContext().getContentResolver(), Settings.System.DRH_SYSTEMUI_SETTINGS_ENABLED, 0) == 1 &&
    			Settings.System.getInt(getContext().getContentResolver(), Settings.System.DRH_SYSTEMUI_SETTINGS_PHONE_BOTTOM, 0) == 0){
    		if (mDrhSettings != null) mDrhSettings.detach();
    		mDrhSettings = null;
    		findViewById(R.id.drh_settings).setVisibility(GONE);
    		((ViewGroup) findViewById(R.id.drh_settings)).removeAllViews();
    	}else {
    		mDrhSettings = new DrhSettings((ViewGroup) findViewById(R.id.drh_settings), getContext());
    		findViewById(R.id.drh_settings).setVisibility(VISIBLE);
    	}
    }
    
    private class H extends Handler {
    	public void handleMessage(Message m) {
    		switch (m.what) {
    		case MSG_DRH_SYSTEMUI_SETTINGS_PHONE_BOTTOM:
    			updateDrhSystemUISettings();
    			break;
    		}
    	}
    }
}
