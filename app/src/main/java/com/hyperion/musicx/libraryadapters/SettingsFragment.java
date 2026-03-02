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
import android.widget.RadioGroup;


public class SettingsFragment extends Fragment {

    private static final String PREFS_NAME = "MusicXSettings";
    private static final String KEY_STREAM_MOBILE = "stream_mobile_data";
	private static final String KEY_DOWNLOAD_MOBILE = "download_mobile_data";
	private static final int REQ_MIC = 101;
	private SwitchCompat animationsSwitch; // Move to class level so it's accessible everywhere
	
	
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
		// Define context from the view that was clicked
		final Context context = this.getContext(); 
		final SharedPreferences prefs = context.getSharedPreferences("DownloadPrefs", Context.MODE_PRIVATE);

// Retrieve last saved ID, default to R.id.rb_standard since it's your default in XML
		int savedId = prefs.getInt("selected_quality_id", R.id.rb_standard);
		
		
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
        RelativeLayout playbackqualityRow = view.findViewById(R.id.row_online_quality);
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

						final AlertDialog dialog = new AlertDialog.Builder(context).create();
						dialog.show();

						LayoutInflater inflater = LayoutInflater.from(context);
						View dialogView = inflater.inflate(R.layout.custom_dialog, null);
						dialog.setContentView(dialogView);

						if (dialog.getWindow() != null) {
							dialog.getWindow().setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT));

							android.view.WindowManager.LayoutParams lp = new android.view.WindowManager.LayoutParams();
							lp.copyFrom(dialog.getWindow().getAttributes());

							int width = (int)(context.getResources().getDisplayMetrics().widthPixels * 0.85);
							lp.width = width;
							lp.height = android.view.WindowManager.LayoutParams.WRAP_CONTENT;

							dialog.getWindow().setAttributes(lp);
						}
						
						int red = android.graphics.Color.parseColor("#FF0000");
						int grey = android.graphics.Color.parseColor("#757575");

						android.content.res.ColorStateList colorStateList = new android.content.res.ColorStateList(
							new int[][]{
								new int[]{android.R.attr.state_checked},
								new int[]{-android.R.attr.state_checked}
							},
							new int[]{ red, grey }
						);

						final SharedPreferences prefs = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);

						RadioGroup qualityGroup = (RadioGroup) dialogView.findViewById(R.id.quality_group);
						RadioButton rbSuper = (RadioButton) dialogView.findViewById(R.id.rb_super);
						RadioButton rbHigh = (RadioButton) dialogView.findViewById(R.id.rb_high);
						RadioButton rbStandard = (RadioButton) dialogView.findViewById(R.id.rb_standard);

						// Null/Default Logic: Retrieve saved ID, fallback to rb_standard if none saved
						int savedId = prefs.getInt("download_quality_id", R.id.rb_standard);
						qualityGroup.check(savedId);

						qualityGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
								@Override
								public void onCheckedChanged(RadioGroup group, int checkedId) {
									if (checkedId != -1) {
										prefs.edit().putInt("download_quality_id", checkedId).apply();

										if (checkedId == R.id.rb_super) {
											// TODO: Super quality logic
										} else if (checkedId == R.id.rb_high) {
											// TODO: High quality logic
										} else if (checkedId == R.id.rb_standard) {
											// TODO: Standard quality logic
										}
									}
								}
							});
						
						if (android.os.Build.VERSION.SDK_INT >= 21) {
							rbSuper.setButtonTintList(colorStateList);
							rbHigh.setButtonTintList(colorStateList);
							rbStandard.setButtonTintList(colorStateList);
						}

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

// 1. Initial State Check (Sets switch position on fragment load)
		if (android.support.v4.content.ContextCompat.checkSelfPermission(getActivity(), 
																		 android.Manifest.permission.RECORD_AUDIO) == android.content.pm.PackageManager.PERMISSION_GRANTED) {
			animationsSwitch.setChecked(true);
		} else {
			animationsSwitch.setChecked(false);
		}

// 2. Toggle Logic
		animationsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if (isChecked) {
						if (android.support.v4.content.ContextCompat.checkSelfPermission(getActivity(), 
																						 android.Manifest.permission.RECORD_AUDIO) != android.content.pm.PackageManager.PERMISSION_GRANTED) {

							// Always try to request the standard system dialog first
							requestPermissions(new String[]{android.Manifest.permission.RECORD_AUDIO}, REQ_MIC);

							// Revert switch until granted
							buttonView.setChecked(false);
						} else {
							Toast.makeText(getActivity(), "Animations: " + isChecked, Toast.LENGTH_SHORT).show();
						}
					}
				}
			});
		
        RelativeLayout timerRow = (RelativeLayout) view.findViewById(R.id.row_timer);
        timerRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());

                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.custom_dialog_timer, null);
                    builder.setView(dialogView);

                    final android.support.v7.app.AlertDialog dialog = builder.create();
                    dialog.show();

                    // --- FIX: TRUE FULL WIDTH ---
                    if (dialog.getWindow() != null) {
                        // 1. Remove the default system dialog background (which has margins)
                        dialog.getWindow().setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT));

                        // 2. Set width to MATCH_PARENT
                        android.view.WindowManager.LayoutParams lp = new android.view.WindowManager.LayoutParams();
                        lp.copyFrom(dialog.getWindow().getAttributes());
                        lp.width = android.view.WindowManager.LayoutParams.MATCH_PARENT;
                        lp.height = android.view.WindowManager.LayoutParams.WRAP_CONTENT;
                        dialog.getWindow().setAttributes(lp);
                    }

                    // --- PREFERENCES & DYNAMIC TINTING ---
                    final SharedPreferences prefs = getActivity().getSharedPreferences("TimerPrefs", Context.MODE_PRIVATE);
                    int savedId = prefs.getInt("selected_timer_id", R.id.none);

                    // Color State List for RadioButtons
                    int[][] states = new int[][] {
                        new int[] { android.R.attr.state_checked },
                        new int[] { -android.R.attr.state_checked }
                    };
                    int[] colors = new int[] {
                        android.graphics.Color.parseColor("#FF0000"), // Red
                        android.graphics.Color.parseColor("#bdbdbd")  // Grey
                    };
                    android.content.res.ColorStateList colorList = new android.content.res.ColorStateList(states, colors);

                    RadioGroup qualityGroup = (RadioGroup) dialogView.findViewById(R.id.quality_group_timer);

                    // Loop through children to apply tint and restore saved state
                    for (int i = 0; i < qualityGroup.getChildCount(); i++) {
                        View child = qualityGroup.getChildAt(i);
                        if (child instanceof RadioButton) {
                            RadioButton rb = (RadioButton) child;
                            android.support.v4.widget.CompoundButtonCompat.setButtonTintList(rb, colorList);
                            if (rb.getId() == savedId) {
                                rb.setChecked(true);
                            }
                        }
                    }

                    // --- LISTENERS ---
                    qualityGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup group, int checkedId) {
                                // Save choice, but DO NOT dismiss (so user can toggle the switch)
                                prefs.edit().putInt("selected_timer_id", checkedId).apply();
                            }
                        });

                    // Handle the Switch
                    android.support.v7.widget.SwitchCompat finishTrackSwitch = (android.support.v7.widget.SwitchCompat) dialogView.findViewById(R.id.switch_animations);
                    finishTrackSwitch.setChecked(prefs.getBoolean("finish_track", false));
                    finishTrackSwitch.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(android.widget.CompoundButton buttonView, boolean isChecked) {
                                prefs.edit().putBoolean("finish_track", isChecked).apply();
                            }
                        });

                    TextView btnCancel = (TextView) dialogView.findViewById(R.id.btn_cancel_timer);
                    btnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                    TextView btnOK = (TextView) dialogView.findViewById(R.id.btn_ok);
                    btnOK.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
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
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);

		if (requestCode == REQ_MIC) {
			if (grantResults.length > 0 && grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED) {
				// CASE 1: User allowed!
				Toast.makeText(getActivity(), "Permission Granted", Toast.LENGTH_SHORT).show();
				updateSwitchState(true);
			} else {
				// CASE 2: User denied ("Don't allow" clicked)

				// Check if they denied it PERMANENTLY (clicked twice or "Don't ask again")
				if (!shouldShowRequestPermissionRationale(android.Manifest.permission.RECORD_AUDIO)) {
					// This triggers the custom HUAWEI-style dialog from your image
					showSettingsDialog();
				} else {
					// Just a regular single denial
					Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_SHORT).show();
				}

				if (animationsSwitch != null) {
					animationsSwitch.setChecked(false);
				}
			}
		}
	}

	private void showSettingsDialog() {
		final android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(getActivity()).create();

		// Inflate custom view
		android.view.LayoutInflater inflater = getActivity().getLayoutInflater();
		android.view.View dialogView = inflater.inflate(R.layout.dialog_permission, null);
		dialog.setView(dialogView);

		// Make the system window transparent so our rounded corners show
		if (dialog.getWindow() != null) {
			dialog.getWindow().setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT));
		}

		// Set Up Button Logic
		dialogView.findViewById(R.id.btn_setup).setOnClickListener(new android.view.View.OnClickListener() {
				@Override
				public void onClick(android.view.View v) {
					android.content.Intent intent = new android.content.Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
					android.net.Uri uri = android.net.Uri.fromParts("package", getActivity().getPackageName(), null);
					intent.setData(uri);
					startActivity(intent);
					dialog.dismiss();
				}
			});

		// Cancel Button Logic
		dialogView.findViewById(R.id.btn_cancel).setOnClickListener(new android.view.View.OnClickListener() {
				@Override
				public void onClick(android.view.View v) {
					dialog.dismiss();
				}
			});

		dialog.show();

		// Set width to 85% of screen to match your image
		int width = (int)(getResources().getDisplayMetrics().widthPixels * 0.85);
		dialog.getWindow().setLayout(width, android.view.WindowManager.LayoutParams.WRAP_CONTENT);
	}
	
	

	private void updateSwitchState(final boolean checked) {
		if (animationsSwitch != null) {
			animationsSwitch.setOnCheckedChangeListener(null);
			animationsSwitch.setChecked(checked);
			animationsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton b, boolean isChecked) {
						// Re-attach your logic
						if (isChecked) {
							requestPermissions(new String[]{android.Manifest.permission.RECORD_AUDIO}, REQ_MIC);
							animationsSwitch.setChecked(false);
						}
					}
				});
		}
	}
	
}

