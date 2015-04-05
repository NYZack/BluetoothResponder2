package com.vibaroo.btnow;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class MainActivity extends Activity {
    AudioManager audioManager;
    BroadcastReceiver scoReceiver;
    int scoStartState;


/** Called when the activity is first created. */
@Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(null);
    this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
    setContentView(R.layout.activity_main);

    audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
}
    @Override
    public void onResume() {
        super.onResume();
        Intent startingIntent = getIntent();
        if (startingIntent == null || startBluetooth() != 1) finish();
  }

    @Override
    public void onPause() {
        super.onPause();
        stopBluetooth();
        if (scoReceiver != null) unregisterReceiver(scoReceiver);
    }

    private int startBluetooth() {
        scoReceiver = new SCOReceiver(audioManager);
        Intent scoIntent = registerReceiver(scoReceiver, new IntentFilter(AudioManager.ACTION_SCO_AUDIO_STATE_UPDATED));
        scoStartState = scoIntent.getIntExtra(AudioManager.EXTRA_SCO_AUDIO_STATE, -1);

        try {
            audioManager.startBluetoothSco();
            return(1);
        } catch (Exception e) {
            Log.e("LogTag", e.getMessage());
            Toast.makeText(this, "Cannot connect to Bluetooth", Toast.LENGTH_LONG).show();
            return(0);
        }

/*        try {
            audioManager.setMode(AudioManager.MODE_IN_CALL);
            audioManager.startBluetoothSco();
            audioManager.setBluetoothScoOn(true);
            return(1);
        } catch (Exception e) {
            Log.e("LogTag", e.getMessage());
            Toast.makeText(this, "Cannot connect to Bluetooth", Toast.LENGTH_LONG).show();
            return(0);
        }*/
    }

    private int stopBluetooth() {
        try {
            audioManager.setMode(AudioManager.MODE_NORMAL);
            if (scoStartState == AudioManager.SCO_AUDIO_STATE_DISCONNECTED) audioManager.stopBluetoothSco();
            audioManager.setBluetoothScoOn(false);
            return(1);
        } catch (Exception e) {
            Log.e("LogTag", e.getMessage());
            Toast.makeText(this, "Cannot connect to Bluetooth", Toast.LENGTH_LONG).show();
            return(0);
        }
    }


}