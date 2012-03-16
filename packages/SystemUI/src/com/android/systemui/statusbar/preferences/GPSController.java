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

public class GPSController extends SettingsController {

    private ContentResolver mContentResolver;

    public GPSController(Context context, View button) {
        super(context, button);
        mContentResolver = context.getContentResolver();
        ((ImageView) button.findViewById(R.id.drh_settings_icon))
                .setImageResource(R.drawable.drh_gps);
        updateController();
    }

    protected int getPreferenceStatus() {
        return (Settings.Secure.isLocationProviderEnabled(mContentResolver, "gps") ? 1 : 0);
    }

    protected void setPreferenceStatus(int status) {
        Settings.Secure.setLocationProviderEnabled(mContentResolver, "gps", (status == 1));
    }

    protected void handleUriChange(Uri uri) {
        updateController();
    }

    protected String getSettingsIntent() {
        return "android.settings.LOCATION_SOURCE_SETTINGS";
    }

    protected List<Uri> getObservedUris() {
        ArrayList<Uri> uris = new ArrayList<Uri>();
        uris.add(Settings.Secure.getUriFor(Settings.Secure.LOCATION_PROVIDERS_ALLOWED));

        return uris;
    }
}
