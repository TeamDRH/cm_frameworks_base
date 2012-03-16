package com.android.systemui.statusbar.preferences;

import android.content.*;
import android.view.*;
import android.widget.*;
import android.net.ConnectivityManager;
import com.android.systemui.R;

public class MobileDataController extends SettingsController {
    
    private ConnectivityManager mConnService;

    public MobileDataController(Context context, View button) {
        super(context, button);
        mConnService = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        ((ImageView) button.findViewById(R.id.drh_settings_icon))
                .setImageResource(R.drawable.drh_mobile_data);
        updateController();
    }
    
    protected int getPreferenceStatus() {
            return (mConnService.getMobileDataEnabled() ? 1 : 0);
    }
             
    protected void setPreferenceStatus(int status) {
        if (status == 1) {
          mConnService.setMobileDataEnabled(true);
        }
        else {
          mConnService.setMobileDataEnabled(false);
        }
    } 
}
