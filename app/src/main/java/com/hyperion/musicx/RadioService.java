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
import java.io.IOException;
import android.support.v7.app.NotificationCompat.MediaStyle;

public class RadioService extends Service {
    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;
    private boolean isBuffering = false; 
    private String currentName = "Radio";

    private static final String CHANNEL_ID = "RadioPlaybackChannel";
    public static final String ACTION_STOP = "com.hyperion.musicx.STOP";
    public static final String ACTION_PLAY_PAUSE = "com.hyperion.musicx.PLAY_PAUSE";
    public static final String ACTION_NEXT = "com.hyperion.musicx.NEXT";
    public static final String ACTION_PREVIOUS = "com.hyperion.musicx.PREVIOUS";

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
            } else if (action.equals(ACTION_NEXT)) {
                sendBroadcast(new Intent("ACTION_NEXT"));
                return START_NOT_STICKY;
            } else if (action.equals(ACTION_PREVIOUS)) {
                sendBroadcast(new Intent("ACTION_PREV"));
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
        isBuffering = true;
        isPlaying = false;
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();

            // Initial notification show
            startForeground(101, buildNotification(currentName));

            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
					@Override
					public void onPrepared(MediaPlayer mp) {
						isBuffering = false;
						isPlaying = true;
						mp.start();
						// Keep in foreground while playing
						startForeground(101, buildNotification(currentName));
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
                // CRITICAL: Stop foreground to allow swipe-to-dismiss when paused
                stopForeground(false);
                updateNotification();
            } else {
                mediaPlayer.start();
                isPlaying = true;
                // Return to foreground to prevent service death
                startForeground(101, buildNotification(currentName));
            }
        }
    }

    private void stopPlayback() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
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

        // Standard Intents
        PendingIntent prevPI = PendingIntent.getService(this, 3, new Intent(this, RadioService.class).setAction(ACTION_PREVIOUS), 0);
        PendingIntent playPI = PendingIntent.getService(this, 1, new Intent(this, RadioService.class).setAction(ACTION_PLAY_PAUSE), 0);
        PendingIntent nextPI = PendingIntent.getService(this, 4, new Intent(this, RadioService.class).setAction(ACTION_NEXT), 0);
        PendingIntent stopPI = PendingIntent.getService(this, 2, new Intent(this, RadioService.class).setAction(ACTION_STOP), 0);

        // SWIPE INTENT: This fires ACTION_STOP when user clears the notification
        PendingIntent deletePI = PendingIntent.getService(this, 5, new Intent(this, RadioService.class).setAction(ACTION_STOP), 0);

        String contentText = isBuffering ? "Buffering..." : name;
        int playPauseIcon = isBuffering ? android.R.drawable.ic_menu_rotate : 
            (isPlaying ? android.R.drawable.ic_media_pause : android.R.drawable.ic_media_play);

        android.support.v4.app.NotificationCompat.Builder builder = 
            new android.support.v4.app.NotificationCompat.Builder(this);

        builder.setContentTitle("Radio Player")
            .setContentText(contentText)
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setContentIntent(pendingIntent)
            .setDeleteIntent(deletePI) // <--- Handles the swipe
            .setOngoing(isPlaying)      // True = Cannot swipe | False = Swipable
            .addAction(android.R.drawable.ic_media_previous, "PREV", prevPI)
            .addAction(playPauseIcon, isPlaying ? "PAUSE" : "PLAY", playPI)
            .addAction(android.R.drawable.ic_media_next, "NEXT", nextPI)
            .addAction(android.R.drawable.ic_menu_close_clear_cancel, "STOP", stopPI);

        // Correct path for SDK 25 / Support Library 25+
		android.support.v7.app.NotificationCompat.MediaStyle style = 
			new android.support.v7.app.NotificationCompat.MediaStyle();

		style.setShowActionsInCompactView(0, 1, 2);

		builder.setStyle(style);

        Notification notification = builder.build();

        // Android O+ Channel compatibility
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

    @Override public void onDestroy() { stopPlayback(); super.onDestroy(); }
    @Override public IBinder onBind(Intent intent) { return null; }
}

