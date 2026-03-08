package com.hyperion.musicx.libraryadapters;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hyperion.musicx.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class Songs extends Fragment {

    private static final int STORAGE_PERMISSION_CODE = 1;
    private ListView listView;
    private ArrayList<Song> fullSongList = new ArrayList<Song>();
    private ArrayList<Song> filteredList = new ArrayList<Song>();
    private SongAdapter adapter;
    private MediaPlayer mediaPlayer;
    private int currentSongIndex = -1;

    private View miniPlayerLayout;
    private TextView miniTitle, miniArtist, tvCurrentTime, tvTotalDuration;
    private ImageButton miniPlayPause;
    private SeekBar songProgressBar;
    private Handler progressHandler = new Handler();

    class Song {
        String title, path, artist;
        Song(String t, String p, String a) { this.title = t; this.path = p; this.artist = a; }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_songs, container, false);

        listView = (ListView) view.findViewById(R.id.song_list);
        ImageButton refreshBtn = (ImageButton) view.findViewById(R.id.btn_refresh);
        final EditText searchBar = (EditText) view.findViewById(R.id.search_bar);
        miniPlayerLayout = view.findViewById(R.id.mini_player_layout);
        miniTitle = (TextView) view.findViewById(R.id.mini_song_title);
        miniArtist = (TextView) view.findViewById(R.id.mini_song_artist);
        tvCurrentTime = (TextView) view.findViewById(R.id.tv_current_time);
        tvTotalDuration = (TextView) view.findViewById(R.id.tv_total_duration);
        miniPlayPause = (ImageButton) view.findViewById(R.id.mini_play_pause);
        songProgressBar = (SeekBar) view.findViewById(R.id.song_progress);

        // --- SEEKBAR STYLING & LOGIC ---
        if (songProgressBar != null) {
            songProgressBar.getProgressDrawable().setColorFilter(Color.parseColor("#FF0000"), PorterDuff.Mode.SRC_IN);
            if (songProgressBar.getThumb() != null) {
                songProgressBar.getThumb().setColorFilter(Color.parseColor("#FF0000"), PorterDuff.Mode.SRC_IN);
            }

            songProgressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (fromUser && mediaPlayer != null) {
                            mediaPlayer.seekTo(progress);
                            tvCurrentTime.setText(formatTime(progress));
                        }
                    }
                    @Override public void onStartTrackingTouch(SeekBar seekBar) {}
                    @Override public void onStopTrackingTouch(SeekBar seekBar) {}
                });
        }

        if (refreshBtn != null) refreshBtn.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbarsongs);
        if (toolbar != null) {
            Drawable backArrow = AppCompatResources.getDrawable(getContext(), R.drawable.abc_ic_ab_back_material);
            if (backArrow != null) {
                backArrow.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                toolbar.setNavigationIcon(backArrow);
            }
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) { getActivity().onBackPressed(); }
                });
        }

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override public void onCompletion(MediaPlayer mp) { playNextSong(); }
            });

        adapter = new SongAdapter(getActivity(), filteredList);
        listView.setAdapter(adapter);

        searchBar.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) { searchBar.setCursorVisible(true); }
            });

        searchBar.addTextChangedListener(new TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override public void onTextChanged(CharSequence s, int start, int before, int count) { filter(s.toString()); }
                @Override public void afterTextChanged(Editable s) {}
            });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    currentSongIndex = position;
                    playSong(filteredList.get(position));
                }
            });

        miniPlayPause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                        miniPlayPause.setImageResource(android.R.drawable.ic_media_play);
                    } else {
                        mediaPlayer.start();
                        miniPlayPause.setImageResource(android.R.drawable.ic_media_pause);
                        updateProgressBar();
                    }
                }
            });

        refreshBtn.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) { checkPermissionAndLoad(); }
            });

        checkPermissionAndLoad();
        return view;
    }

    private void filter(String text) {
        filteredList.clear();
        if (text.isEmpty()) {
            filteredList.addAll(fullSongList);
        } else {
            for (Song song : fullSongList) {
                if (song.title.toLowerCase().contains(text.toLowerCase()) || 
                    song.artist.toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(song);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void playSong(Song song) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(song.path);
            mediaPlayer.prepare();
            mediaPlayer.start();
            miniPlayerLayout.setVisibility(View.VISIBLE);
            miniTitle.setText(song.title);
            miniArtist.setText(song.artist);
            miniPlayPause.setImageResource(android.R.drawable.ic_media_pause);

            int duration = mediaPlayer.getDuration();
            songProgressBar.setMax(duration);
            tvTotalDuration.setText(formatTime(duration));
            updateProgressBar();
        } catch (IOException e) {
            Toast.makeText(getActivity(), "Error playing file", Toast.LENGTH_SHORT).show();
        }
    }

    private void playNextSong() {
        if (currentSongIndex < filteredList.size() - 1) {
            currentSongIndex++;
            playSong(filteredList.get(currentSongIndex));
        }
    }

    private void updateProgressBar() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            int currentPos = mediaPlayer.getCurrentPosition();
            songProgressBar.setProgress(currentPos);
            tvCurrentTime.setText(formatTime(currentPos));
            progressHandler.postDelayed(new Runnable() {
                    @Override public void run() { updateProgressBar(); }
                }, 1000);
        }
    }

    private String formatTime(int millis) {
        int seconds = (millis / 1000) % 60;
        int minutes = (millis / (1000 * 60)) % 60;
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
    }

    private void checkPermissionAndLoad() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) 
            == PackageManager.PERMISSION_GRANTED) {
            loadAudioFiles();
        } else {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    private void loadAudioFiles() {
        fullSongList.clear();
        queryStorage(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        queryStorage(MediaStore.Audio.Media.INTERNAL_CONTENT_URI);
        filter(""); 
    }

    private void queryStorage(Uri uri) {
        String[] proj = { MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.ARTIST };
        Cursor cursor = getActivity().getContentResolver().query(uri, proj, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                fullSongList.add(new Song(cursor.getString(0), cursor.getString(1), cursor.getString(2)));
            }
            cursor.close();
        }
    }

    class SongAdapter extends ArrayAdapter<Song> {
        SongAdapter(Context context, ArrayList<Song> songs) { super(context, 0, songs); }
        @Override
        public View getView(int position, View v, ViewGroup p) {
            if (v == null) v = LayoutInflater.from(getContext()).inflate(R.layout.list_item_song, p, false);
            Song s = getItem(position);
            ((TextView) v.findViewById(R.id.song_title)).setText(s.title);
            ((TextView) v.findViewById(R.id.song_artist)).setText(s.artist);
            return v;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        progressHandler.removeCallbacksAndMessages(null);
    }
}

