package com.vibaroo.btnow;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;

public class SCOReceiver extends BroadcastReceiver {
    AudioManager mAudioManager;
    public SCOReceiver(AudioManager audioManager) {
        mAudioManager = audioManager;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int scoState = intent.getIntExtra(AudioManager.EXTRA_SCO_AUDIO_STATE, -1);
        switch (scoState) {
            case -1:
                Log.e("LogTag", "SCO State: Error");
                break;
            case AudioManager.SCO_AUDIO_STATE_DISCONNECTED:
                break;
            case AudioManager.SCO_AUDIO_STATE_CONNECTED:
                mAudioManager.setMode(AudioManager.MODE_IN_CALL);
                mAudioManager.setBluetoothScoOn(true);
                break;
            case AudioManager.SCO_AUDIO_STATE_CONNECTING:
                break;
        }
    }
}
