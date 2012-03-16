package com.android.systemui.statusbar.preferences;

import android.content.*;
import android.view.*;
import android.widget.*;
import android.provider.*;
import android.net.Uri;
import java.util.List;
import java.util.ArrayList;

import com.android.systemui.R;
import com.android.systemui.statusbar.preferences.*;
import android.app.*;
import android.util.*;

public class StatusbarSoftKeysController extends SettingsController {

    private ContentResolver mContentResolver;
    private static int preferenceStatus = -1;

    public StatusbarSoftKeysController(Context context, View button) {
        super(context, button);

        ((ImageView) button.findViewById(R.id.drh_settings_icon))
                .setImageResource(R.drawable.ic_sysbar_home);

        mContentResolver = context.getContentResolver();
        if (preferenceStatus == -1)
            preferenceStatus = 0;

        updateController();
    }

    protected int getPreferenceStatus() {
        return preferenceStatus;
    }

    protected void setPreferenceStatus(int status) {
        preferenceStatus = status;
        StatusBarManager statusBarManager = (StatusBarManager) mContext
                .getSystemService("statusbar");
        if (status == 1)
            statusBarManager.disable(0x400000 | 0x800000 | 0x200000 | 0x40000 | 0x20000 | 0x80000);
        else
            statusBarManager.disable(0);
    }

    protected String getSettingsIntent() {
        return null;
    }

}
