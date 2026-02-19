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

        // 3. Initialize Buttons
        TextView aboutBtn = (TextView) view.findViewById(R.id.btn_about);
        aboutBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Toast.makeText(getActivity(), "Opening About", Toast.LENGTH_SHORT).show();
				}
			});

        return view;
    }
}

