package com.hyperion.musicx.libraryadapters.settings;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import com.hyperion.musicx.R;
import android.support.v7.widget.Toolbar;
import android.graphics.drawable.Drawable;
import android.support.v7.content.res.AppCompatResources;
import android.graphics.Color;
import android.graphics.PorterDuff;

public class ClearCache extends Fragment {

    private RecyclerView recyclerView;
    private CacheAdapter adapter;
    private List<CacheItem> cacheItems;
    private Button btnClearAll;

    // This is the method your SettingsFragment is calling
    public static ClearCache newInstance() {
        return new ClearCache();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cache, container, false);

        // --- TRANSPARENT TOOLBAR WITH WHITE HEADING & WHITE ARROW ---
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbarcache);
        if (toolbar != null) {
            toolbar.setTitle("Clear Cache");
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
        
        recyclerView = (RecyclerView) view.findViewById(R.id.rvCacheItems);
        btnClearAll = (Button) view.findViewById(R.id.btnClearAll);

        loadCacheData();

        btnClearAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clearAllCache();
                    loadCacheData(); 
                }
            });

        return view;
    }

    private void loadCacheData() {
        cacheItems = new ArrayList<CacheItem>();
        // Using getContext() instead of getCacheDir() directly
        File cacheDir = getContext().getCacheDir();

        cacheItems.add(new CacheItem("Album covers", getFolderSize(new File(cacheDir, "album_covers"))));
        cacheItems.add(new CacheItem("Songs", getFolderSize(new File(cacheDir, "songs"))));
        cacheItems.add(new CacheItem("Web pages", getFolderSize(new File(cacheDir, "web_pages"))));
        cacheItems.add(new CacheItem("Other", getFolderSize(new File(cacheDir, "other"))));
        
        adapter = new CacheAdapter(cacheItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        updateTotalSize();
    }

    private void updateTotalSize() {
        long total = 0;
        for (CacheItem item : cacheItems) total += item.getSizeInBytes();
        btnClearAll.setText("CLEAR ALL (" + formatSize(total) + ")");
    }

    private void clearAllCache() {
        deleteDir(getContext().getCacheDir());
    }

    private boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                if (!deleteDir(new File(dir, children[i]))) return false;
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        }
        return false;
    }

    private long getFolderSize(File directory) {
        long length = 0;
        File[] files = directory.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()) length += files[i].length();
                else length += getFolderSize(files[i]);
            }
        }
        return length;
    }

    private String formatSize(long bytes) {
        if (bytes <= 0) return "0 B";
        int digitGroups = (int) (Math.log10(bytes) / Math.log10(1024));
        return new DecimalFormat("#,##0.##").format(bytes / Math.pow(1024, digitGroups)) 
            + " " + new String[]{"B", "KB", "MB", "GB"}[digitGroups];
    }
}

