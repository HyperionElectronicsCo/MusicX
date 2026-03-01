package com.hyperion.musicx.libraryadapters.settings;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import com.hyperion.musicx.R;

public class OnlinePlaybackQuality extends Fragment {

    public static OnlinePlaybackQuality newInstance() {
        return new OnlinePlaybackQuality();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.onlineplaybackquality, container, false);

        // Mobile Data IDs
        setupRow(view, R.id.tv_m_super, R.id.rb_m_super, "SQ", "#FF0000");
       setupRow(view, R.id.tv_m_high, R.id.rb_m_high, "HQ", "#444444");
        setupRow(view, R.id.tv_m_std, R.id.rb_m_std, "128K", "#444444");

        return view;
    }

    private void setupRow(View parent, int tvId, final int rbId, String badge, String color) {
        TextView tv = (TextView) parent.findViewById(tvId);
        final RadioButton rb = (RadioButton) parent.findViewById(rbId);
        if (tv == null || rb == null) return;

        // 1. Set the Badge and Subtext colors
        String text = tv.getText().toString();
        SpannableString ss = new SpannableString(text);
        int start = text.indexOf(badge);
        if (start != -1) {
            int end = start + badge.length();
            ss.setSpan(new BackgroundColorSpan(Color.parseColor(color)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(new ForegroundColorSpan(Color.WHITE), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        int newline = text.indexOf("\n");
        if (newline != -1) {
            ss.setSpan(new ForegroundColorSpan(Color.parseColor("#808080")), newline, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        tv.setText(ss);

        // 2. Make clicking the text toggle the radio button
        tv.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					rb.setChecked(true);
				}
			});
    }
}

