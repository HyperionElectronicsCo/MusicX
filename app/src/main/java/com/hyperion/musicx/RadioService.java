package com.hyperion.musicx;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import java.io.IOException;

public class RadioService extends Service {
    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;
    private boolean isBuffering = false; // New state tracker
    private String currentName = "Radio";

    private static final String CHANNEL_ID = "RadioPlaybackChannel";
    public static final String ACTION_STOP = "com.hyperion.musicx.STOP";
    public static final String ACTION_PLAY_PAUSE = "com.hyperion.musicx.PLAY_PAUSE";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getAction() != null) {
            String action = intent.getAction();
            if (action.equals(ACTION_STOP)) {
                stopPlayback();
                return START_NOT_STICKY;
            } else if (action.equals(ACTION_PLAY_PAUSE)) {
                togglePlayback();
                return START_NOT_STICKY;
            }
        }

        if (intent != null) {
            String url = intent.getStringExtra("url");
            String name = intent.getStringExtra("name");
            if (url != null) {
                currentName = name;
                playStream(url);
            }
        }
        return START_NOT_STICKY;
    }

    private void playStream(String url) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }

        isBuffering = true; // Start buffering
        isPlaying = false;

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();

            // Show notification immediately with "Buffering..." text
            startForeground(101, buildNotification(currentName));

            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
					@Override
					public void onPrepared(MediaPlayer mp) {
						isBuffering = false; // Done buffering
						isPlaying = true;
						mp.start();
						updateNotification();
					}
				});

            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
					@Override
					public boolean onError(MediaPlayer mp, int what, int extra) {
						isBuffering = false;
						updateNotification();
						return false;
					}
				});
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void togglePlayback() {
        if (mediaPlayer != null && !isBuffering) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                isPlaying = false;
            } else {
                mediaPlayer.start();
                isPlaying = true;
            }
            updateNotification();
        }
    }

    private void stopPlayback() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        isPlaying = false;
        isBuffering = false;
        stopForeground(true);
        stopSelf();
    }

    private void updateNotification() {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.notify(101, buildNotification(currentName));
        }
    }

    private Notification buildNotification(String name) {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Intent playPauseIntent = new Intent(this, RadioService.class);
        playPauseIntent.setAction(ACTION_PLAY_PAUSE);
        PendingIntent playPausePendingIntent = PendingIntent.getService(this, 1, playPauseIntent, 0);

        Intent stopIntent = new Intent(this, RadioService.class);
        stopIntent.setAction(ACTION_STOP);
        PendingIntent stopPendingIntent = PendingIntent.getService(this, 2, stopIntent, 0);

        // Logic for Dynamic Text and Icons
        String contentText = isBuffering ? "Buffering..." : name;
        int playPauseIcon;
        String playPauseLabel;

        if (isBuffering) {
            playPauseIcon = android.R.drawable.ic_menu_rotate; // "Loading" look
            playPauseLabel = "WAIT";
        } else {
            playPauseIcon = isPlaying ? android.R.drawable.ic_media_pause : android.R.drawable.ic_media_play;
            playPauseLabel = isPlaying ? "PAUSE" : "PLAY";
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("Radio Player")
			.setContentText(contentText)
			.setSmallIcon(android.R.drawable.ic_media_play)
			.setContentIntent(pendingIntent)
			.setPriority(NotificationCompat.PRIORITY_LOW)
			.setOngoing(true)
			.addAction(playPauseIcon, playPauseLabel, playPausePendingIntent)
			.addAction(android.R.drawable.ic_menu_close_clear_cancel, "STOP", stopPendingIntent);

        Notification notification = builder.build();

        // Reflection hack for Channel ID on Android O+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                java.lang.reflect.Field field = notification.getClass().getDeclaredField("mChannelId");
                field.setAccessible(true);
                field.set(notification, CHANNEL_ID);
            } catch (Exception e) { e.printStackTrace(); }
        }

        return notification;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID, "Radio Playback", NotificationManager.IMPORTANCE_LOW);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    @Override
    public void onDestroy() {
        stopPlayback();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) { return null; }
}

