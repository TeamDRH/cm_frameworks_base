package com.android.systemui.statusbar.preferences;

import android.content.Context;
import android.os.Vibrator;
import android.provider.Settings;
import android.view.View;

import com.android.systemui.R;

public abstract class MultipleStateController extends SettingsController {

    public static final int STATE_TYPE_DISABLED = 0;
    public static final int STATE_TYPE_ENABLED = 1;
    public static final int STATE_TYPE_TRANSITION = 2;

    public MultipleStateController(Context context, View controlWidget) {
        super(context, controlWidget);
    }

    abstract protected int getStateType(int state);

    abstract public int[] getStateTransitions();

    public void updateControllerDrawable(int state) {
        if (getStateType(state) == STATE_TYPE_DISABLED) {
            controlWidget.findViewById(R.id.drh_settings_status).setBackgroundResource(
                    R.drawable.quicksettings_widget_unpressed);
            controlWidget.findViewById(R.id.drh_settings_main).setBackgroundResource(
                    R.drawable.drh_settings_widget_main_unpressed);
        } else if (getStateType(state) == STATE_TYPE_ENABLED) {
            controlWidget.findViewById(R.id.drh_settings_status).setBackgroundResource(
                    R.drawable.quicksettings_widget_pressed);
            controlWidget.findViewById(R.id.drh_settings_main).setBackgroundResource(
                    R.drawable.drh_settings_widget_main_pressed);
        } else if (getStateType(state) == STATE_TYPE_TRANSITION) {
            controlWidget.findViewById(R.id.drh_settings_status).setBackgroundResource(
                    R.drawable.quicksettings_widget_transition);
        }

        controlWidget.invalidate();
    }

    public void onClick(View view) {
        mPreferenceState = getStateTransitions()[mPreferenceState];
        updateControllerDrawable(mPreferenceState);
        setPreferenceStatus(mPreferenceState);

        try {
            if (Settings.System.getInt(mContext.getContentResolver(),
                    Settings.System.HAPTIC_FEEDBACK_ENABLED) == 1) {
                Vibrator vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
                if (vibrator.hasVibrator()) vibrator.vibrate(10);
            }
        } catch (android.provider.Settings.SettingNotFoundException e) {
        }
    }
}
