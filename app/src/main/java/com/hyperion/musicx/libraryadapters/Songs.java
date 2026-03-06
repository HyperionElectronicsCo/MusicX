package com.hyperion.musicx.libraryadapters;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.hyperion.musicx.R;
import java.io.IOException;
import java.util.ArrayList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.Toolbar;
import android.graphics.drawable.Drawable;
import android.support.v7.content.res.AppCompatResources;

public class Songs extends Fragment {

    private static final int STORAGE_PERMISSION_CODE = 1;
    private ListView listView;
    private ArrayList<Song> songList = new ArrayList<>();
    private SongAdapter adapter;
    private MediaPlayer mediaPlayer;

    private View miniPlayerLayout;
    private TextView miniTitle, miniArtist;
    private ImageButton miniPlayPause;

    class Song {
        String title, path, artist;
        Song(String t, String p, String a) { this.title = t; this.path = p; this.artist = a; }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_songs, container, false);

        listView = (ListView) view.findViewById(R.id.song_list);
        ImageButton refreshBtn = (ImageButton) view.findViewById(R.id.btn_refresh);
        miniPlayerLayout = view.findViewById(R.id.mini_player_layout);
        miniTitle = (TextView) view.findViewById(R.id.mini_song_title);
        miniArtist = (TextView) view.findViewById(R.id.mini_song_artist);
        miniPlayPause = (ImageButton) view.findViewById(R.id.mini_play_pause);

        if (refreshBtn != null) refreshBtn.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbarsongs);
        if (toolbar != null) {
            toolbar.setTitle("Songs");
            toolbar.setTitleTextColor(Color.WHITE);
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
        adapter = new SongAdapter(getActivity(), songList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    playSong(songList.get(position));
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
                    }
                }
            });

        refreshBtn.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) { checkPermissionAndLoad(); }
            });

        checkPermissionAndLoad();
        return view;
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
        } catch (IOException e) {
            Toast.makeText(getActivity(), "Error playing file", Toast.LENGTH_SHORT).show();
        }
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
        songList.clear();
        queryStorage(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        queryStorage(MediaStore.Audio.Media.INTERNAL_CONTENT_URI);
        adapter.notifyDataSetChanged();
    }

    private void queryStorage(Uri uri) {
        String[] projection = { MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.ARTIST };
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                songList.add(new Song(cursor.getString(0), cursor.getString(1), cursor.getString(2)));
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
        if (mediaPlayer != null) mediaPlayer.release();
    }
}

