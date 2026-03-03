package com.hyperion.musicx.libraryadapters.settings;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.content.Context;
import android.content.SharedPreferences;

public class TimerService extends Service {
    private Handler handler = new Handler();

    private Runnable stopMusicTask = new Runnable() {
        @Override
        public void run() {
            SharedPreferences prefs = getSharedPreferences("TimerPrefs", Context.MODE_PRIVATE);
            boolean finishTrack = prefs.getBoolean("finish_track", false);

            if (finishTrack) {
                // Set flag for the MediaPlayer's OnCompletionListener to check
                prefs.edit().putBoolean("stop_after_current", true).apply();
            } else {
                // Immediate pause via system broadcast
                Intent i = new Intent("com.android.music.musicservicecommand");
                i.putExtra("command", "pause");
                sendBroadcast(i);

                // Also send a general media button intent for broader compatibility
                sendBroadcast(new Intent("com.android.music.save_track_info"));
            }
            stopSelf();
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        long duration = intent.getLongExtra("duration", 0);

        // If duration is -1, it means the user selected "None", so cancel timer
        if (duration == -1) {
            handler.removeCallbacks(stopMusicTask);
            stopSelf();
            return START_NOT_STICKY;
        }

        handler.removeCallbacks(stopMusicTask);
        handler.postDelayed(stopMusicTask, duration);
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacks(stopMusicTask);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) { return null; }
}


