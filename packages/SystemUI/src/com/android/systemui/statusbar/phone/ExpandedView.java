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
import android.util.Slog;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class ExpandedView extends LinearLayout {
    PhoneStatusBar mService;
    int mPrevHeight = -1;
    DrhSettings mDrhSettings;
    SettingsObserver mDrhSettingsObserver;
    
    private static final int MSG_DRH_SYSTEMUI_SETTINGS_PHONE_TOP = 1000;

    public ExpandedView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDrhSettingsObserver = new SettingsObserver(new H(), MSG_DRH_SYSTEMUI_SETTINGS_PHONE_TOP);
        getContext().getContentResolver().registerContentObserver(Settings.System.getUriFor(Settings.System.DRH_SYSTEMUI_SETTINGS_PHONE_TOP), true, mDrhSettingsObserver);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    /** We want to shrink down to 0, and ignore the background. */
    @Override
    public int getSuggestedMinimumHeight() {
        return 0;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
         super.onLayout(changed, left, top, right, bottom);
         int height = bottom - top;
         if (height != mPrevHeight) {
             if (PhoneStatusBar.DEBUG) {
                 Slog.d(PhoneStatusBar.TAG, "ExpandedView height changed old=" + mPrevHeight
                      + " new=" + height);
             }
             mPrevHeight = height;
             mService.updateExpandedViewPos(PhoneStatusBar.EXPANDED_LEAVE_ALONE);
         }
         
         if (Settings.System.getInt(getContext().getContentResolver(), Settings.System.DRH_SYSTEMUI_SETTINGS_ENABLED, 0) == 1 &&
        		 Settings.System.getInt(getContext().getContentResolver(), Settings.System.DRH_SYSTEMUI_SETTINGS_PHONE_TOP, 0) == 1 &&
        		 mDrhSettings == null){
        	 mDrhSettings = new DrhSettings(((ViewGroup) findViewById(R.id.drh_settings)), mContext);
        	 findViewById(R.id.drh_settings).setVisibility(VISIBLE);
         }
     }
    
    private void updateDrhSettings() {
        if (Settings.System.getInt(getContext().getContentResolver(), Settings.System.DRH_SYSTEMUI_SETTINGS_ENABLED, 0) == 1 &&
        		Settings.System.getInt(getContext().getContentResolver(), Settings.System.DRH_SYSTEMUI_SETTINGS_PHONE_TOP, 0) == 1){
       	 mDrhSettings = new DrhSettings(((ViewGroup) findViewById(R.id.drh_settings)), mContext);
       	 findViewById(R.id.drh_settings).setVisibility(VISIBLE);
        }else {
        	if (mDrhSettings != null) mDrhSettings.detach();
        	mDrhSettings = null;
        	findViewById(R.id.drh_settings).setVisibility(GONE);
        }
    }
    
    private class H extends Handler {
    	public void handleMessage(Message m){
    		switch (m.what){
    		case MSG_DRH_SYSTEMUI_SETTINGS_PHONE_TOP: updateDrhSettings();
    			break;
    		}
    	}
    }
}
