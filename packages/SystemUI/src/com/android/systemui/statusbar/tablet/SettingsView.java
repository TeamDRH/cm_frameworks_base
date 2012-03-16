/*
 * Copyright (C) 2010 The Android Open Source Project
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

package com.android.systemui.statusbar.tablet;

import android.app.StatusBarManager;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.Slog;
import android.widget.LinearLayout;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.systemui.R;
import com.android.systemui.statusbar.policy.AirplaneModeController;
import com.android.systemui.statusbar.policy.AutoRotateController;
import com.android.systemui.statusbar.policy.BrightnessController;
import com.android.systemui.statusbar.policy.BluetoothController;
import com.android.systemui.statusbar.policy.DoNotDisturbController;
import com.android.systemui.statusbar.policy.ToggleSlider;
import com.android.systemui.statusbar.policy.VolumeController;
import com.android.systemui.statusbar.policy.WifiController;
import com.android.systemui.statusbar.preferences.DrhSettings;


public class SettingsView extends LinearLayout implements View.OnClickListener {
    static final String TAG = "SettingsView";

    AirplaneModeController mAirplane;
    AutoRotateController mRotate;
    BrightnessController mBrightness;
    DoNotDisturbController mDoNotDisturb;
    VolumeController mVolume;
    BluetoothController mBluetooth;
    WifiController mWifi;

    DrhSettings mDrhSettings;

    public SettingsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        final Context context = getContext();

        mAirplane = new AirplaneModeController(context,
                (CompoundButton)findViewById(R.id.airplane_checkbox));
        findViewById(R.id.network).setOnClickListener(this);
        findViewById(R.id.bluetooth).setOnClickListener(this);
        mBluetooth = new BluetoothController(context,
					     (CompoundButton)findViewById(R.id.bluetooth_checkbox));
        mWifi = new WifiController(context,
				(CompoundButton)findViewById(R.id.wifi_checkbox));
        mRotate = new AutoRotateController(context,
                (CompoundButton)findViewById(R.id.rotate_checkbox));
        mBrightness = new BrightnessController(context,
                (ToggleSlider)findViewById(R.id.brightness));
        mDoNotDisturb = new DoNotDisturbController(context,
                (CompoundButton)findViewById(R.id.do_not_disturb_checkbox));
        findViewById(R.id.settings).setOnClickListener(this);

        if (Settings.System.getInt(mContext.getContentResolver(), Settings.System.DRH_SYSTEMUI_SETTINGS_STANDARD_AIRPLANE, 1) == 0)
            findViewById(R.id.airplane).setVisibility(View.GONE);

        if (Settings.System.getInt(mContext.getContentResolver(), Settings.System.DRH_SYSTEMUI_SETTINGS_STANDARD_WIFI, 1) == 0)
            findViewById(R.id.network).setVisibility(View.GONE);

        if (Settings.System.getInt(mContext.getContentResolver(), Settings.System.DRH_SYSTEMUI_SETTINGS_STANDARD_ROTATION, 1) == 0)
            findViewById(R.id.rotate).setVisibility(View.GONE);

        if (Settings.System.getInt(mContext.getContentResolver(), Settings.System.DRH_SYSTEMUI_SETTINGS_STANDARD_BRIGHTNESS, 1) == 0)
            findViewById(R.id.brightness_row).setVisibility(View.GONE);

        if (Settings.System.getInt(mContext.getContentResolver(), Settings.System.DRH_SYSTEMUI_SETTINGS_STANDARD_NOTIFICATIONS, 1) == 0)
            findViewById(R.id.do_not_disturb).setVisibility(View.GONE);

        if (Settings.System.getInt(mContext.getContentResolver(), Settings.System.DRH_SYSTEMUI_SETTINGS_STANDARD_SETTINGS, 1) == 0)
            findViewById(R.id.settings).setVisibility(View.GONE);

        if (Settings.System.getInt(mContext.getContentResolver(), Settings.System.DRH_SYSTEMUI_SETTINGS_ENABLED, 0) == 1) {
            findViewById(R.id.drh_settings).setVisibility(View.VISIBLE);
            mDrhSettings = new DrhSettings((ViewGroup) findViewById(R.id.drh_settings), context);
        }
        
        if (Settings.System.getInt(mContext.getContentResolver(), Settings.System.DRH_SYSTEMUI_SETTINGS_STANDARD_VOLUME, Settings.System.DRH_SYSTEMUI_SETTINGS_STANDARD_VOLUME_DEF) == 1) {
            mVolume = new VolumeController(mContext, (ToggleSlider) findViewById(R.id.volume));
        }else {
            findViewById(R.id.volume_row).setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mAirplane.release();
        mDoNotDisturb.release();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.network:
                onClickNetwork();
                break;
            case R.id.bluetooth:
                onClickBluetooth();
                break;
            case R.id.settings:
                onClickSettings();
                break;
        }
    }

    private StatusBarManager getStatusBarManager() {
        return (StatusBarManager)getContext().getSystemService(Context.STATUS_BAR_SERVICE);
    }

    // Network
    // ----------------------------
    private void onClickNetwork() {
        getContext().startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        getStatusBarManager().collapse();
    }

// Bluetooth
    // ----------------------------
    private void onClickBluetooth() {
        getContext().startActivity(new Intent(Settings.ACTION_BLUETOOTH_SETTINGS)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        getStatusBarManager().collapse();
    }

    // Settings
    // ----------------------------
    private void onClickSettings() {
        getContext().startActivity(new Intent(Settings.ACTION_SETTINGS)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        getStatusBarManager().collapse();
    }
}

