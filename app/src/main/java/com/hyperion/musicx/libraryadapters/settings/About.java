package com.hyperion.musicx.libraryadapters.settings;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.hyperion.musicx.R;
import android.support.v7.widget.Toolbar;
import android.graphics.drawable.Drawable;
import android.support.v7.content.res.AppCompatResources;
import android.graphics.PorterDuff;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.ImageButton;
import android.content.Context;
import android.widget.TextView;

public class About extends Fragment {
    public static About newInstance() {
        About fragment = new About();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        
        // Target the container, not the image
        final View logoContainer = view.findViewById(R.id.logo_container);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            logoContainer.setClipToOutline(true);
            logoContainer.setOutlineProvider(new android.view.ViewOutlineProvider() {
                    @Override
                    public void getOutline(View view, android.graphics.Outline outline) {
                        // Using a DP-based value for consistent look across devices
                        int curve = (int) (14 * view.getContext().getResources().getDisplayMetrics().density);
                        outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), curve);
                    }
                });
        }
        
        // --- TRANSPARENT TOOLBAR WITH WHITE HEADING & WHITE ARROW ---
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbarabout);
        if (toolbar != null) {
            toolbar.setTitle("About");
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
            
            ImageButton settingsBtn = (ImageButton) view.findViewById(R.id.settingsTop);
           
            settingsBtn.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
            
            settingsBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        
                        android.view.ContextThemeWrapper themeWrapper = new android.view.ContextThemeWrapper(getContext(), 
                                                                                                             android.support.v7.appcompat.R.style.ThemeOverlay_AppCompat_Dark);
                        android.support.v7.widget.PopupMenu popup = new android.support.v7.widget.PopupMenu(themeWrapper, v);
                        popup.getMenuInflater().inflate(R.menu.about_menu, popup.getMenu());

                        popup.setOnMenuItemClickListener(new android.support.v7.widget.PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(android.view.MenuItem item) {
                                    if (item.getItemId() == R.id.action_view_uuid) {
                                        final String uuid = java.util.UUID.randomUUID().toString().replace("-", "");

                                        // 1. Create a container for Title and UUID
                                        android.widget.LinearLayout layout = new android.widget.LinearLayout(getContext());
                                        layout.setOrientation(android.widget.LinearLayout.VERTICAL);
                                        int padding = (int) (24 * getResources().getDisplayMetrics().density);
                                        layout.setPadding(padding, padding, padding, 0);

                                        // 2. Custom Title (White)
                                        TextView titleView = new TextView(getContext());
                                        titleView.setText("UUID:");
                                        titleView.setTextColor(Color.WHITE);
                                        titleView.setTextSize(18);
                                        titleView.setTypeface(null, android.graphics.Typeface.BOLD);
                                        layout.addView(titleView);

                                        // 3. Custom UUID text (White + Bold + Long Press)
                                        TextView messageView = new TextView(getContext());
                                        messageView.setText(uuid);
                                        messageView.setTextColor(Color.WHITE);
                                        messageView.setTextSize(16);
                                        messageView.setTypeface(null, android.graphics.Typeface.BOLD);
                                        messageView.setPadding(0, (int) (12 * getResources().getDisplayMetrics().density), 0, 0);

                                        // Add Long Press to Copy functionality
                                        messageView.setOnLongClickListener(new View.OnLongClickListener() {
                                                @Override
                                                public boolean onLongClick(View v) {
                                                    android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                                                    android.content.ClipData clip = android.content.ClipData.newPlainText("UUID", uuid);
                                                    clipboard.setPrimaryClip(clip);
                                                    android.widget.Toast.makeText(getContext(), "UUID copied to clipboard", android.widget.Toast.LENGTH_SHORT).show();
                                                    return true;
                                                }
                                            });
                                        layout.addView(messageView);

                                        // 4. Build Dialog using .setView() instead of setTitle/setMessage
                                        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getContext());
                                        builder.setView(layout); 
                                        builder.setPositiveButton("OK", null);

                                        final android.support.v7.app.AlertDialog dialog = builder.create();
                                        dialog.show();

                                        // 5. Apply Background and OK button color
                                        if (dialog.getWindow() != null) {
                                            dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_bg);
                                        }

                                        android.widget.Button okButton = dialog.getButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE);
                                        if (okButton != null) {
                                            okButton.setTextColor(Color.parseColor("#FF0000"));
                                        }

                                        return true;
                                    }
                                    return false;
                                }
                            });
                            
                        popup.show();
                    }
                });
                
        }
        return view;
    }}

