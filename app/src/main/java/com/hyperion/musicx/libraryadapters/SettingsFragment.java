package com.hyperion.musicx.libraryadapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.hyperion.musicx.R;
import com.hyperion.musicx.libraryadapters.settings.About;
import com.hyperion.musicx.libraryadapters.settings.OnlinePlaybackQuality;


public class SettingsFragment extends Fragment {

    private static final String PREFS_NAME = "MusicXSettings";
    private static final String KEY_STREAM_MOBILE = "stream_mobile_data";
	private static final String KEY_DOWNLOAD_MOBILE = "download_mobile_data";
    private SharedPreferences prefs;

    public SettingsFragment() {
        
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
		
        prefs = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        final SwitchCompat streamSwitch = (SwitchCompat) view.findViewById(R.id.switch_stream_mobile);
        final SwitchCompat downloadSwitch = (SwitchCompat) view.findViewById(R.id.switch_download_mobile);
		
		
		streamSwitch.setChecked(prefs.getBoolean(KEY_STREAM_MOBILE, false));
		downloadSwitch.setChecked(prefs.getBoolean(KEY_DOWNLOAD_MOBILE, false));
		

        final CompoundButton.OnCheckedChangeListener streamListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    new AlertDialog.Builder(getActivity())
                        .setTitle("Mobile Data Warning")
                        .setMessage("Are you sure you wish to stream over mobile data?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                prefs.edit().putBoolean(KEY_STREAM_MOBILE, true).apply();
                                Toast.makeText(getActivity(), "Stream using data: true", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                buttonView.setOnCheckedChangeListener(null);
                                buttonView.setChecked(false);
                                buttonView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
										@Override
										public void onCheckedChanged(CompoundButton bv, boolean ic) {
											
										}
									});
                                
                                buttonView.setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener)this);
								setupStreamListener(buttonView);
                                prefs.edit().putBoolean(KEY_STREAM_MOBILE, false).apply();
                            }
                            private void setupStreamListener(CompoundButton bv) {
                                
                            }
                        })
                        .setCancelable(false)
                        .show();
                } else {
                    prefs.edit().putBoolean(KEY_STREAM_MOBILE, false).apply();
                    Toast.makeText(getActivity(), "Stream using data: false", Toast.LENGTH_SHORT).show();
                }
            }
        };

        streamSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(final CompoundButton buttonView, boolean isChecked) {
					if (isChecked) {
						new AlertDialog.Builder(getActivity())
							.setTitle("Mobile Data Warning")
							.setMessage("Are you sure you wish to stream over mobile data?")
							.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									prefs.edit().putBoolean(KEY_STREAM_MOBILE, true).apply();
								}
							})
							.setNegativeButton("No", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									buttonView.setOnCheckedChangeListener(null);
									buttonView.setChecked(false);
									// Pointing back to the parent listener
									buttonView.setOnCheckedChangeListener(getStreamListener());
									prefs.edit().putBoolean(KEY_STREAM_MOBILE, false).apply();
								}
							})
							.setCancelable(false)
							.show();
					} else {
						prefs.edit().putBoolean(KEY_STREAM_MOBILE, false).apply();
					}
				}

				private CompoundButton.OnCheckedChangeListener getStreamListener() {
					return this;
				}
			});
	
        final CompoundButton.OnCheckedChangeListener downloadListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    new AlertDialog.Builder(getActivity())
                        .setTitle("Mobile Data Warning")
                        .setMessage("Are you sure you wish to download over mobile data?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                prefs.edit().putBoolean(KEY_STREAM_MOBILE, true).apply();
                                Toast.makeText(getActivity(), "Download using data: true", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                buttonView.setOnCheckedChangeListener(null);
                                buttonView.setChecked(false);
                                buttonView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
										@Override
										public void onCheckedChanged(CompoundButton bv, boolean ic) {

										}
									});

                                buttonView.setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener)this);
								setupStreamListener(buttonView);
                                prefs.edit().putBoolean(KEY_STREAM_MOBILE, false).apply();
                            }
                            private void setupStreamListener(CompoundButton bv) {

                            }
                        })
                        .setCancelable(false)
                        .show();
                } else {
                    prefs.edit().putBoolean(KEY_STREAM_MOBILE, false).apply();
                    Toast.makeText(getActivity(), "Download using data: false", Toast.LENGTH_SHORT).show();
                }
            }
        };

        downloadSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(final CompoundButton buttonView, boolean isChecked) {
					if (isChecked) {
						new AlertDialog.Builder(getActivity())
							.setTitle("Mobile Data Warning")
							.setMessage("Are you sure you wish to download over mobile data?")
							.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									prefs.edit().putBoolean(KEY_DOWNLOAD_MOBILE, true).apply();
								}
							})
							.setNegativeButton("No", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									buttonView.setOnCheckedChangeListener(null);
									buttonView.setChecked(false);
									// Pointing back to the parent listener
									buttonView.setOnCheckedChangeListener(getDownloadListener());
									prefs.edit().putBoolean(KEY_DOWNLOAD_MOBILE, false).apply();
								}
							})
							.setCancelable(false)
							.show();
					} else {
						prefs.edit().putBoolean(KEY_DOWNLOAD_MOBILE, false).apply();
					}
				}

				private CompoundButton.OnCheckedChangeListener getDownloadListener() {
					return this;
				}
			});
        RelativeLayout playbackqualityRow = (RelativeLayout) view.findViewById(R.id.row_online_quality);
        playbackqualityRow.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					OnlinePlaybackQuality playbackqualityFragment = OnlinePlaybackQuality.newInstance();
					android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
					android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
					ft.replace(R.id.frame_layout, playbackqualityFragment);
					ft.addToBackStack(null);
					ft.commit();
				}
			});
        RelativeLayout downloadqualityRow = (RelativeLayout) view.findViewById(R.id.row_download_quality);
		downloadqualityRow.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						Context context = v.getContext();

						// 1. Create and show the basic AlertDialog
						final AlertDialog dialog = new AlertDialog.Builder(context).create();
						dialog.show();

						// 2. Inflate your layout
						LayoutInflater inflater = LayoutInflater.from(context);
						View dialogView = inflater.inflate(R.layout.custom_dialog, null);
						dialog.setContentView(dialogView);

						// Replace the window width part of your current code with this:
						if (dialog.getWindow() != null) {
							dialog.getWindow().setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT));

							android.view.WindowManager.LayoutParams lp = new android.view.WindowManager.LayoutParams();
							lp.copyFrom(dialog.getWindow().getAttributes());

							// Set width to 85% of screen width for a slimmer look
							int width = (int)(context.getResources().getDisplayMetrics().widthPixels * 0.85);
							lp.width = width;
							lp.height = android.view.WindowManager.LayoutParams.WRAP_CONTENT;

							dialog.getWindow().setAttributes(lp);
						}
						

						// 4. PROGRAMMATIC RADIO BUTTON COLOR (#FF0000)
						int red = android.graphics.Color.parseColor("#FF0000");
						int grey = android.graphics.Color.parseColor("#757575");

						android.content.res.ColorStateList colorStateList = new android.content.res.ColorStateList(
							new int[][]{
								new int[]{android.R.attr.state_checked},
								new int[]{-android.R.attr.state_checked}
							},
							new int[]{ red, grey }
						);

						RadioButton rbSuper = (RadioButton) dialogView.findViewById(R.id.rb_super);
						RadioButton rbHigh = (RadioButton) dialogView.findViewById(R.id.rb_high);
						RadioButton rbStandard = (RadioButton) dialogView.findViewById(R.id.rb_standard);

						// Apply the tint (Works on SDK 21+)
						if (android.os.Build.VERSION.SDK_INT >= 21) {
							rbSuper.setButtonTintList(colorStateList);
							rbHigh.setButtonTintList(colorStateList);
							rbStandard.setButtonTintList(colorStateList);
						}

						// 5. Cancel button logic
						TextView btnCancel = (TextView) dialogView.findViewById(R.id.btn_cancel);
						btnCancel.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									dialog.dismiss();
								}
							});

					} catch (Exception e) {
						e.printStackTrace();
						android.widget.Toast.makeText(v.getContext(), "Error: " + e.getMessage(), 1).show();
					}
				}
			});
		
		
		
        SwitchCompat animationsSwitch = (SwitchCompat) view.findViewById(R.id.switch_animations);
        animationsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					Toast.makeText(getActivity(), "Animations: " + isChecked, Toast.LENGTH_SHORT).show();
				}
			});
        RelativeLayout timerRow = (RelativeLayout) view.findViewById(R.id.row_timer);
        timerRow.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Toast.makeText(getActivity(), "Timer", Toast.LENGTH_SHORT).show();
				}
			});
        SwitchCompat simplaybackSwitch = (SwitchCompat) view.findViewById(R.id.switch_simplayback);
        simplaybackSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					Toast.makeText(getActivity(), "Play over other apps: " + isChecked, Toast.LENGTH_SHORT).show();
				}
			});
        SwitchCompat smartplaySwitch = (SwitchCompat) view.findViewById(R.id.switch_smart_play);
        smartplaySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					Toast.makeText(getActivity(), "BT smart playback: " + isChecked, Toast.LENGTH_SHORT).show();
				}
			});
        SwitchCompat wiredplaySwitch = (SwitchCompat) view.findViewById(R.id.switch_wired_play);
        wiredplaySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					Toast.makeText(getActivity(), "Wired earphone control: " + isChecked, Toast.LENGTH_SHORT).show();
				}
			});
        SwitchCompat showlyricsSwitch = (SwitchCompat) view.findViewById(R.id.switch_show_lyrics);
        showlyricsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					Toast.makeText(getActivity(), "Show lyrics in car: " + isChecked, Toast.LENGTH_SHORT).show();
				}
			});
        RelativeLayout FilterRow = (RelativeLayout) view.findViewById(R.id.row_filter);
        FilterRow.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Toast.makeText(getActivity(), "Filter", Toast.LENGTH_SHORT).show();
				}
			});
        RelativeLayout notificationsRow = (RelativeLayout) view.findViewById(R.id.row_notifications);
        notificationsRow.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Toast.makeText(getActivity(), "Notifications", Toast.LENGTH_SHORT).show();
				}
			});
        SwitchCompat syncSwitch = (SwitchCompat) view.findViewById(R.id.switch_cloud_sync);
        syncSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					Toast.makeText(getActivity(), "Sync: " + isChecked, Toast.LENGTH_SHORT).show();
				}
			});
        SwitchCompat restrictionsSwitch = (SwitchCompat) view.findViewById(R.id.switch_content_restrictions);
        restrictionsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					Toast.makeText(getActivity(), "Restrictions: " + isChecked, Toast.LENGTH_SHORT).show();
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
					Toast.makeText(getActivity(), "Checking for updates...", Toast.LENGTH_SHORT).show();
				}
			});
        RelativeLayout aboutRow = (RelativeLayout) view.findViewById(R.id.row_about);
        aboutRow.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					About aboutFragment = About.newInstance();
					android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
					android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
					ft.replace(R.id.frame_layout, aboutFragment);
					ft.addToBackStack(null);
					ft.commit();
				}
			});
			
        return view;
    }
}

