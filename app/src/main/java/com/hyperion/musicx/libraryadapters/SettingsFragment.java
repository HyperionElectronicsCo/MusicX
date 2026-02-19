package com.hyperion.musicx.libraryadapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.hyperion.musicx.R;

public class SettingsFragment extends Fragment {

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        // 1. Initialize Switches
        SwitchCompat streamSwitch = (SwitchCompat) view.findViewById(R.id.switch_stream_mobile);
        streamSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					Toast.makeText(getActivity(), "Stream: " + isChecked, Toast.LENGTH_SHORT).show();
				}
			});

        // 2. Initialize Clickable Rows
        RelativeLayout qualityRow = (RelativeLayout) view.findViewById(R.id.row_online_quality);
        qualityRow.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Toast.makeText(getActivity(), "Opening Quality Settings", Toast.LENGTH_SHORT).show();
				}
			});
			
			
			
		RelativeLayout cacheRow = (RelativeLayout) view.findViewById(R.id.row_cache);
        cacheRow.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Toast.makeText(getActivity(), "Clear cache", Toast.LENGTH_SHORT).show();
				}
			});
		RelativeLayout extrasRow = (RelativeLayout) view.findViewById(R.id.row_extras);
        extrasRow.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Toast.makeText(getActivity(), "More settings", Toast.LENGTH_SHORT).show();
				}
			});
        RelativeLayout updatesRow = (RelativeLayout) view.findViewById(R.id.row_updates);
        updatesRow.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Toast.makeText(getActivity(), "Checking for updates", Toast.LENGTH_SHORT).show();
				}
			});
        RelativeLayout aboutRow = (RelativeLayout) view.findViewById(R.id.row_about);
        aboutRow.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Toast.makeText(getActivity(), "Opening About", Toast.LENGTH_SHORT).show();
				}
			});

        return view;
    }
}

