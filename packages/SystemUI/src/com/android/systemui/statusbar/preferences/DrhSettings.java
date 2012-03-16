package com.android.systemui.statusbar.preferences;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;

import com.android.systemui.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.LinkedList;

public class DrhSettings {

    private static String            APP_TAG = "DRH Settings";

    private Context                  mContext;
    private ContentResolver          mContentResolver;
    private ViewGroup                mParent;
    private List<SettingsController> visibleControllers;
    private SettingsObserver         mSettingsObserver;

    public DrhSettings(ViewGroup parent, Context context) {
        if (parent == null) {
            Log.i(APP_TAG, "Parent is null, not continuing");
            return;
        }

        mContext = context;
        mContentResolver = mContext.getContentResolver();
        mParent = parent;
        mSettingsObserver = new SettingsObserver(new H());

        mContentResolver.registerContentObserver(
                Settings.System.getUriFor(Settings.System.DRH_SYSTEMUI_SETTINGS_ENABLED_CONTROLS),
                false, mSettingsObserver);
        setupControllers();
    }

    private void setupControllers() {
        mParent.removeAllViews();
        visibleControllers = new LinkedList<SettingsController>();
        String[] controls = null;

        String quickControls = Settings.System.getString(mContentResolver,
                Settings.System.DRH_SYSTEMUI_SETTINGS_ENABLED_CONTROLS);
        if (quickControls != null) {
            controls = quickControls.split("\\|");
        } else {
            controls = Settings.System.DRH_SYSTEMUI_SETTINGS_DEFAULTS;
        }

        if (controls.length > 1) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            for (String i : controls) {
                View child = inflater.inflate(R.layout.drh_quicksetting, mParent, false);
                mParent.addView(child);
                Log.w(APP_TAG, i);

                if (i.equals(Settings.System.DRH_SYSTEMUI_SETTINGS_AIRPLANE)) {
                    visibleControllers.add(new AirplaneController(mContext, child));
                } else if (i.equals(Settings.System.DRH_SYSTEMUI_SETTINGS_AUTO_ROTATE)) {
                    visibleControllers.add(new AutoRotateController(mContext, child));
                } else if (i.equals(Settings.System.DRH_SYSTEMUI_SETTINGS_BLUETOOTH)) {
                    visibleControllers.add(new BluetoothController(mContext, child));
                } else if (i.equals(Settings.System.DRH_SYSTEMUI_SETTINGS_GPS)) {
                    visibleControllers.add(new GPSController(mContext, child));
                } else if (i.equals(Settings.System.DRH_SYSTEMUI_SETTINGS_NOTIFICATIONS)) {
                    visibleControllers.add(new NotificationsController(mContext, child));
                } else if (i.equals(Settings.System.DRH_SYSTEMUI_SETTINGS_SILENT)) {
                    visibleControllers.add(new AudioController(mContext, child));
                } else if (i.equals(Settings.System.DRH_SYSTEMUI_SETTINGS_SOFTWAREKEYS)) {
                    visibleControllers.add(new StatusbarSoftKeysController(mContext, child));
                } else if (i.equals(Settings.System.DRH_SYSTEMUI_SETTINGS_TORCH)) {
                    visibleControllers.add(new TorchController(mContext, child));
                } else if (i.equals(Settings.System.DRH_SYSTEMUI_SETTINGS_WIFI)) {
                    visibleControllers.add(new WifiController(mContext, child));
                } else if (i.equals(Settings.System.DRH_SYSTEMUI_SETTINGS_MOBILEDATA)) {
                    visibleControllers.add(new MobileDataController(mContext, child));
                } else if (i.equals(Settings.System.DRH_SYSTEMUI_SETTINGS_WIFITETHER)) {
                    visibleControllers.add(new WifiTetherController(mContext, child));
                } else if (i.equals(Settings.System.DRH_SYSTEMUI_SETTINGS_USBTETHER)) {
                    visibleControllers.add(new UsbTetherController(mContext, child));
            }

            }
        }
    }

    public void attach() {
        for (SettingsController i : visibleControllers)
            i.attach();
    }

    public void detach() {
        for (SettingsController i : visibleControllers)
            i.detach();
    }

    private class H extends Handler {
        public void handleMessage(Message msg) {
            detach();
            setupControllers();
        }
    }

    private class SettingsObserver extends ContentObserver {
        Handler mHandler;

        public SettingsObserver(Handler handler) {
            super(handler);
            mHandler = handler;
        }

        public void onChange(boolean selfChange) {
            mHandler.sendMessage(new Message());
        }
    }
}
