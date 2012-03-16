package com.android.systemui.statusbar.preferences;

import android.content.*;
import android.view.*;
import android.widget.*;
import android.provider.*;
import android.net.Uri;
import java.util.List;
import java.util.ArrayList;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Vibrator;

import com.android.server.VibratorService;
import com.android.systemui.R;
import com.android.systemui.statusbar.preferences.*;
import java.lang.Runnable;

public class AudioController extends MultipleStateController {

    AudioManager             mAudioManager;
    private ImageView        mIcon;

    private static final int STATE_VIBRATE = 0;
    private static final int STATE_SILENT  = 1;
    private static final int STATE_NORMAL  = 2;

    public AudioController(Context context, View button) {
        super(context, button);
        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        mIcon = ((ImageView) button.findViewById(R.id.drh_settings_icon));
        updateController();
    }

    protected int getPreferenceStatus() {
        int ringerMode = mAudioManager.getRingerMode();
        if (ringerMode == AudioManager.RINGER_MODE_VIBRATE)
            return STATE_VIBRATE;
        if (ringerMode == AudioManager.RINGER_MODE_SILENT)
            return STATE_SILENT;
        return STATE_NORMAL;
    }

    protected void setPreferenceStatus(final int status) {
        switch (status){
        case STATE_VIBRATE:
            mAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
            break;
        case STATE_SILENT:
            mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            break;
        case STATE_NORMAL:
            mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            break;
        }
    }

    protected void handleBroadcast(Intent intent) {
        mPreferenceState = getPreferenceStatus();
    }

    protected String getSettingsIntent() {
        return "android.settings.SOUND_SETTINGS";
    }

    protected IntentFilter getBroadcastIntents() {
        IntentFilter intents = new IntentFilter();
        intents.addAction(AudioManager.RINGER_MODE_CHANGED_ACTION);

        return intents;
    }

    @Override
    protected int getStateType(int state) {
        return MultipleStateController.STATE_TYPE_ENABLED;
    }

    @Override
    public int[] getStateTransitions() {
        Vibrator mVibratorService = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
        if (mVibratorService.hasVibrator()) {
            return new int[] { 1, 2, 0 };
        } else {
            return new int[] { 0, 2, 1 };
        }
    }

    @Override
    public void updateController() {
        mPreferenceState = getPreferenceStatus();
        updateControllerDrawable(mPreferenceState);
        updateControllerImage(mPreferenceState);
        controlWidget.invalidate();
    }

    private void updateControllerImage(int preferenceState) {
        switch (preferenceState) {
        case STATE_VIBRATE:
            mIcon.setImageResource(R.drawable.drh_audio_vibrate);
            break;
        case STATE_SILENT:
            mIcon.setImageResource(R.drawable.drh_audio_silent);
            break;
        case STATE_NORMAL:
            mIcon.setImageResource(R.drawable.drh_audio_normal);
        }
    }
}
