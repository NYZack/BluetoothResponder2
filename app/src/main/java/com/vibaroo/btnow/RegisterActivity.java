package com.vibaroo.btnow;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;


public class RegisterActivity extends Activity {
    private static final int ACTIVATE_DIRECTLY = 0;
    private static final int ACTIVATE_BY_VOICE = 1;
    int activateHow;
    Intent voiceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        voiceIntent = new Intent(this, MainActivity.class);
        voiceIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        activateHow = Integer.parseInt(prefs.getString("pref_launch",this.getString(R.string.pref_launchType_default)));

        switch (activateHow) {
            case ACTIVATE_DIRECTLY:
                startService(new Intent(this, HUD.class));
                break;
            case ACTIVATE_BY_VOICE:
                startActivity(voiceIntent);
                break;
            default:
                startService(new Intent(this, HUD.class));
                break;
        }
    }
}
