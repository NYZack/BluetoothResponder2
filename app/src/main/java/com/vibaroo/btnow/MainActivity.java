package com.vibaroo.btnow;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    AudioManager audioManager;
    BroadcastReceiver scoReceiver;
    int scoStartState;

@Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(null);
    this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
    setContentView(R.layout.activity_main);


}
    @Override
    public void onResume() {
        super.onResume();
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        Intent startingIntent = getIntent();
        if (startingIntent == null || startBluetooth() != 1) finish();
  }

    @Override
    public void onPause() {
        super.onPause();
        audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
        stopBluetooth();
        if (scoReceiver != null) unregisterReceiver(scoReceiver);
    }

    private int startBluetooth() {
        scoReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int scoState = intent.getIntExtra(AudioManager.EXTRA_SCO_AUDIO_STATE, -1);
                switch (scoState) {
                    case -1:
                        Log.e("LogTag", "SCO State: Error");
                        break;
                    case AudioManager.SCO_AUDIO_STATE_DISCONNECTED:
                        Log.i("LogTag", "SCO State: Disconnected");
                        break;
                    case AudioManager.SCO_AUDIO_STATE_CONNECTED:
                        Log.i("LogTag", "SCO State: Connected");
                        audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
                        TextView okGoogle = (TextView) findViewById(R.id.say_ok_google_placeholder);
                        okGoogle.setText(getString(R.string.say_ok_google));
                        break;
                    case AudioManager.SCO_AUDIO_STATE_CONNECTING:
                        Log.i("LogTag", "SCO State: Connecting");
                        break;
                }
            }
        };

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
    }

    private int stopBluetooth() {
        try {
//            audioManager.setMode(startMode);
            Handler handler = new Handler();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    audioManager.stopBluetoothSco();
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
            };
            handler.postDelayed(runnable, 10000);
            return(1);
        } catch (Exception e) {
            Log.e("LogTag", e.getMessage());
            Toast.makeText(this, "Cannot connect to Bluetooth", Toast.LENGTH_LONG).show();
            return(0);
        }
    }
}