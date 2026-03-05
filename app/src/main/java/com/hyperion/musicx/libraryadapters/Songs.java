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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.hyperion.musicx.R;
import java.io.IOException;
import java.util.ArrayList;
import android.widget.ImageButton;
// Added for coloring
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.graphics.drawable.Drawable;
import android.support.v7.content.res.AppCompatResources;

public class Songs extends Fragment {

    private static final int STORAGE_PERMISSION_CODE = 1;
    private ListView listView;
    private ArrayList<String> songTitles = new ArrayList<>();
    private ArrayList<String> songPaths = new ArrayList<>(); 
    private ArrayAdapter<String> adapter;
    private MediaPlayer mediaPlayer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_songs, container, false);
        listView = (ListView) view.findViewById(R.id.song_list);
        ImageButton refreshBtn = (ImageButton) view.findViewById(R.id.btn_refresh);

        // --- MAKE REFRESH ICON WHITE ---
        if (refreshBtn != null) {
            refreshBtn.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        }
// --- TRANSPARENT TOOLBAR WITH WHITE HEADING & WHITE ARROW ---
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbarsongs);
        if (toolbar != null) {
            toolbar.setTitle("Songs");
            toolbar.setTitleTextColor(Color.WHITE); 
            toolbar.setBackgroundColor(Color.TRANSPARENT);

            // Get the back icon and tint it WHITE
            Drawable backArrow = AppCompatResources.getDrawable(getContext(), R.drawable.abc_ic_ab_back_material);
            if (backArrow != null) {
                backArrow.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                toolbar.setNavigationIcon(backArrow);
            }

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (getFragmentManager() != null) {
                            getFragmentManager().popBackStack();
                        } else if (getActivity() != null) {
                            getActivity().onBackPressed();
                        }
                    }
                });
                }
        mediaPlayer = new MediaPlayer();

        // --- CUSTOM ADAPTER TO MAKE TEXT WHITE ---
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, songTitles) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View row = super.getView(position, convertView, parent);
                TextView tv = (TextView) row.findViewById(android.R.id.text1);
                tv.setTextColor(Color.WHITE); // Force text color to white
                return row;
            }
        };

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    playSong(songPaths.get(position), songTitles.get(position));
                }
            });

        refreshBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkPermissionAndLoad();
                }
            });

        if (savedInstanceState != null) {
            songTitles.addAll(savedInstanceState.getStringArrayList("saved_titles"));
            songPaths.addAll(savedInstanceState.getStringArrayList("saved_paths"));
            adapter.notifyDataSetChanged();
        } else {
            checkPermissionAndLoad();
        }

        return view;
    }

    private void playSong(String path, String title) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.start();
            Toast.makeText(getActivity(), "Playing: " + title, Toast.LENGTH_SHORT).show();
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
        songTitles.clear();
        songPaths.clear();
        queryStorage(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        queryStorage(MediaStore.Audio.Media.INTERNAL_CONTENT_URI);
        adapter.notifyDataSetChanged();

        if(songTitles.isEmpty()){
            Toast.makeText(getActivity(), "No audio files found", Toast.LENGTH_SHORT).show();
        }
    }

    private void queryStorage(Uri contentUri) {
        String[] projection = { MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DATA };
        String selection = MediaStore.Audio.Media.DATA + " LIKE '%.mp3' OR " +
            MediaStore.Audio.Media.DATA + " LIKE '%.wav' OR " +
            MediaStore.Audio.Media.DATA + " LIKE '%.flac' OR " +
            MediaStore.Audio.Media.DATA + " LIKE '%.m4a'";

        Cursor cursor = getActivity().getContentResolver().query(contentUri, projection, selection, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String title = cursor.getString(0);
                String path = cursor.getString(1);
                if (title == null || title.isEmpty()) {
                    title = path.substring(path.lastIndexOf("/") + 1);
                }
                songTitles.add(title);
                songPaths.add(path);
            }
            cursor.close();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            loadAudioFiles();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("saved_titles", songTitles);
        outState.putStringArrayList("saved_paths", songPaths);
    }
}

