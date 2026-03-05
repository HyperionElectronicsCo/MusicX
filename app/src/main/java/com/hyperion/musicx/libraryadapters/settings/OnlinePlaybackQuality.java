package com.hyperion.musicx.libraryadapters.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.Toolbar;
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
import java.util.ArrayList;
import java.util.List;

public class OnlinePlaybackQuality extends Fragment {
    private static final String PREFS_NAME = "MusicX_Prefs";
    private static final String KEY_MOBILE_QUALITY = "mobile_quality_id";
    private static final String KEY_WIFI_QUALITY = "wifi_quality_id";

    private List<RadioButton> mobileButtons = new ArrayList<>();
    private List<RadioButton> wifiButtons = new ArrayList<>();
    private SharedPreferences prefs;

    public static OnlinePlaybackQuality newInstance() {
        return new OnlinePlaybackQuality();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.onlineplaybackquality, container, false);
        prefs = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // --- TRANSPARENT TOOLBAR WITH WHITE HEADING & WHITE ARROW ---
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbarquality);
        if (toolbar != null) {
            toolbar.setTitle("Online playback quality");
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

        mobileButtons.clear();
        wifiButtons.clear();

        setupRow(view, R.id.tv_m_super, R.id.rb_m_super, "SQ", "#FF0000", mobileButtons, KEY_MOBILE_QUALITY);
        setupRow(view, R.id.tv_m_high, R.id.rb_m_high, "HQ", "#444444", mobileButtons, KEY_MOBILE_QUALITY);
        setupRow(view, R.id.tv_m_std, R.id.rb_m_std, "128K", "#444444", mobileButtons, KEY_MOBILE_QUALITY);
        setupRow(view, R.id.tv_m_low, R.id.rb_m_low, "64K", "#444444", mobileButtons, KEY_MOBILE_QUALITY);

        setupRow(view, R.id.tv_w_super, R.id.rb_w_super, "SQ", "#FF0000", wifiButtons, KEY_WIFI_QUALITY);
        setupRow(view, R.id.tv_w_high, R.id.rb_w_high, "HQ", "#444444", wifiButtons, KEY_WIFI_QUALITY);
        setupRow(view, R.id.tv_w_std, R.id.rb_w_std, "128K", "#444444", wifiButtons, KEY_WIFI_QUALITY);
        setupRow(view, R.id.tv_w_low, R.id.rb_w_low, "64K", "#444444", wifiButtons, KEY_WIFI_QUALITY);

        restoreSelection(mobileButtons, KEY_MOBILE_QUALITY, R.id.rb_m_std);
        restoreSelection(wifiButtons, KEY_WIFI_QUALITY, R.id.rb_w_std);

        return view;
    }

    private void setupRow(View parent, int tvId, final int rbId, String badge, String color, final List<RadioButton> group, final String prefKey) {
        TextView tv = (TextView) parent.findViewById(tvId);
        final RadioButton rb = (RadioButton) parent.findViewById(rbId);
        if (tv == null || rb == null) return;

        group.add(rb);
        rb.setButtonTintList(AppCompatResources.getColorStateList(getContext(), R.xml.switch_track_selector));

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

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (RadioButton button : group) {
                    button.setChecked(button.getId() == rbId);
                }
                prefs.edit().putInt(prefKey, rbId).apply();
            }
        };
        tv.setOnClickListener(listener);
        rb.setOnClickListener(listener);
    }

    private void restoreSelection(List<RadioButton> group, String prefKey, int defaultId) {
        int savedId = prefs.getInt(prefKey, defaultId);
        for (RadioButton rb : group) {
            rb.setChecked(rb.getId() == savedId);
        }
    }
}

