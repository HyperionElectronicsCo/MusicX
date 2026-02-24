package com.hyperion.musicx;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class ItemThreeFragment extends Fragment {

    private ArrayList<RadioStation> stations = new ArrayList<>();
    private int currentStationIndex = -1; // Track the currently playing station

    // Receiver to handle Next/Previous clicks from the Notification
    private final BroadcastReceiver stationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("ACTION_NEXT".equals(action)) {
                playNext();
            } else if ("ACTION_PREV".equals(action)) {
                playPrevious();
            }
        }
    };

    class RadioStation {
        String name, genre, url;
        boolean isHeader;

        RadioStation(String name, String genre, String url) {
            this.name = name;
            this.genre = genre;
            this.url = url;
            this.isHeader = false;
        }

        RadioStation(String headerName) {
            this.name = headerName;
            this.isHeader = true;
        }
    }

    public static ItemThreeFragment newInstance() {
        return new ItemThreeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_three, container, false);
        ListView listView = (ListView) view.findViewById(R.id.radioListView);

        loadStations();

        ArrayAdapter<RadioStation> adapter = new ArrayAdapter<RadioStation>(getActivity(), 
																			android.R.layout.simple_list_item_2, android.R.id.text1, stations) {

            @Override
            public View getView(int pos, View convert, ViewGroup parent) {
                View v = super.getView(pos, convert, parent);
                RadioStation item = stations.get(pos);

                TextView t1 = (TextView) v.findViewById(android.R.id.text1);
                TextView t2 = (TextView) v.findViewById(android.R.id.text2);

                if (item.isHeader) {
                    v.setBackgroundColor(Color.parseColor("#333333"));
                    t1.setText(item.name);
                    t1.setTextColor(Color.YELLOW);
                    t1.setPadding(30, 30, 10, 10);
                    t2.setVisibility(View.GONE);
                } else {
                    v.setBackgroundColor(Color.TRANSPARENT);
                    t1.setText(item.name);
                    t1.setTextColor(Color.WHITE);
                    t1.setPadding(0, 0, 10, 0);
                    t2.setVisibility(View.VISIBLE);
                    t2.setText(item.genre);
                    t2.setTextColor(Color.LTGRAY);
                }
                return v;
            }

            @Override
            public boolean isEnabled(int position) {
                return !stations.get(position).isHeader;
            }
        };

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(android.widget.AdapterView<?> parent, View view, int position, long id) {
					RadioStation selected = stations.get(position);
					if (!selected.isHeader) {
						currentStationIndex = position; // Save index
						playStream(selected.url, selected.name);
					}
				}
			});

        return view;
    }

    private void playNext() {
        if (stations.isEmpty()) return;
        int nextIndex = currentStationIndex + 1;

        // Loop and skip headers
        if (nextIndex >= stations.size()) nextIndex = 0;
        while (nextIndex < stations.size() && stations.get(nextIndex).isHeader) {
            nextIndex++;
            if (nextIndex >= stations.size()) nextIndex = 0;
        }

        currentStationIndex = nextIndex;
        playStream(stations.get(nextIndex).url, stations.get(nextIndex).name);
    }

    private void playPrevious() {
        if (stations.isEmpty()) return;
        int prevIndex = currentStationIndex - 1;

        // Loop and skip headers
        if (prevIndex < 0) prevIndex = stations.size() - 1;
        while (prevIndex >= 0 && stations.get(prevIndex).isHeader) {
            prevIndex--;
            if (prevIndex < 0) prevIndex = stations.size() - 1;
        }

        currentStationIndex = prevIndex;
        playStream(stations.get(prevIndex).url, stations.get(prevIndex).name);
    }

    private void loadStations() {
        stations.clear();
        // Use the single-string constructor to add headers
        stations.add(new RadioStation("--- 24/7 SELECTION ---"));
        // Updated with 24-7 stations
		stations.add(new RadioStation("24-7 The Power of Love", "24-7 / The Power of Love", "http://antares.dribbcast.com:4468/stream/1/"));
		stations.add(new RadioStation("24-7 Serenity", "24-7 / Serenity", "http://antares.dribbcast.com:4352/stream/1/"));
		stations.add(new RadioStation("24-7 60s", "24-7 / 60s", "http://antares.dribbcast.com:4361/stream/1/"));
		stations.add(new RadioStation("24-7 70s", "24-7 / 70s", "http://antares.dribbcast.com:4418/stream/1/"));
		stations.add(new RadioStation("24-7 80's", "24-7 / 80s", "http://antares.dribbcast.com:4424/stream/1/"));
		stations.add(new RadioStation("24-7 Reggae", "24-7 / Reggae", "http://antares.dribbcast.com:4346/stream/1/"));
		stations.add(new RadioStation("24-7 Soul", "24-7 / Soul", "http://antares.dribbcast.com:4456/stream/1/"));
		stations.add(new RadioStation("24-7 Northern Soul", "24-7 / Northern Soul", "http://antares.dribbcast.com:4398/stream/1/"));
		stations.add(new RadioStation("24-7 Dance Rock", "24-7 / Dance Rock", "http://antares.dribbcast.com:4450/stream/1/"));
        stations.add(new RadioStation("24-7 Punk", "24-7 / Punk", "http://antares.dribbcast.com:4462/stream/1/"));
		stations.add(new RadioStation("24-7 Disco", "24-7 / Disco", "http://antares.dribbcast.com:4336/stream/1/"));
		stations.add(new RadioStation("24-7 Party", "24-7 / Party", "http://antares.dribbcast.com:4412/stream/1/"));

		stations.add(new RadioStation("--- BBC RADIO ---"));
		// Updated with full working .m3u8")); URLs from BBC sources
		stations.add(new RadioStation("BBC - Radio 1", "Pop / Top 40", "http://as-hls-ww-live.akamaized.net/pool_01505109/live/ww/bbc_radio_one/bbc_radio_one.isml/bbc_radio_one-audio%3d96000.norewind.m3u8"));
		stations.add(new RadioStation("BBC - Radio 1Xtra", "Hip-Hop / RnB", "http://as-hls-ww-live.akamaized.net/pool_92079267/live/ww/bbc_1xtra/bbc_1xtra.isml/bbc_1xtra-audio%3d96000.norewind.m3u8"));
		stations.add(new RadioStation("BBC - Radio 1Dance", "Dance / Top 40", "http://as-hls-ww-live.akamaized.net/pool_62063831/live/ww/bbc_radio_one_dance/bbc_radio_one_dance.isml/bbc_radio_one_dance-audio%3d96000.norewind.m3u8"));
		stations.add(new RadioStation("BBC - Radio 1 Anthems", "Oldskool / classics", "http://as-hls-uk-live.akamaized.net/pool_11351741/live/uk/bbc_radio_one_anthems/bbc_radio_one_anthems.isml/bbc_radio_one_anthems-audio%3d96000.norewind.m3u8"));
		stations.add(new RadioStation("BBC - Radio 2", "Adult Contemporary", "http://as-hls-ww-live.akamaized.net/pool_74208725/live/ww/bbc_radio_two/bbc_radio_two.isml/bbc_radio_two-audio%3d96000.norewind.m3u8"));
		stations.add(new RadioStation("BBC - Radio 3", "Classical / Arts", "http://as-hls-ww-live.akamaized.net/pool_23461179/live/ww/bbc_radio_three/bbc_radio_three.isml/bbc_radio_three-audio%3d96000.norewind.m3u8"));
		stations.add(new RadioStation("BBC - Radio 3 Unwind", "Classical / Arts", "http://as-hls-uk-live.akamaized.net/pool_30624046/live/uk/bbc_radio_three_unwind/bbc_radio_three_unwind.isml/bbc_radio_three_unwind-audio%3d320000.norewind.m3u8"));
		stations.add(new RadioStation("BBC - Radio 4", "Talk / News", "http://as-hls-ww-live.akamaized.net/pool_55057080/live/ww/bbc_radio_fourfm/bbc_radio_fourfm.isml/bbc_radio_fourfm-audio%3d96000.norewind.m3u8"));
		stations.add(new RadioStation("BBC - Radio 4 Extra", "Comedy / Drama", "http://as-hls-ww-live.akamaized.net/pool_26173715/live/ww/bbc_radio_four_extra/bbc_radio_four_extra.isml/bbc_radio_four_extra-audio%3d96000.norewind.m3u8"));
		stations.add(new RadioStation("BBC - Radio 5 Live ", "Live Sports", "http://as-hls-ww-live.akamaized.net/pool_89021708/live/ww/bbc_radio_five_live/bbc_radio_five_live.isml/bbc_radio_five_live-audio%3d96000.norewind.m3u8"));
		stations.add(new RadioStation("BBC - Radio 5 Live extra", "Live Sports / Extra", "http://as-hls-uk-live.akamaized.net/pool_47700285/live/uk/bbc_radio_five_live_sports_extra/bbc_radio_five_live_sports_extra.isml/bbc_radio_five_live_sports_extra-audio%3d96000.norewind.m3u8"));
		stations.add(new RadioStation("BBC - Radio 6 Music ", "Alternative / Indie", "http://as-hls-ww-live.akamaized.net/pool_81827798/live/ww/bbc_6music/bbc_6music.isml/bbc_6music-audio%3d96000.norewind.m3u8"));
		stations.add(new RadioStation("BBC - Radio Asian Network", "Asian / Underground", "http://as-hls-ww-live.akamaized.net/pool_22108647/live/ww/bbc_asian_network/bbc_asian_network.isml/bbc_asian_network-audio%3d96000.norewind.m3u8"));
		stations.add(new RadioStation("BBC - BBC World Service", "Global / News", "http://as-hls-ww-live.akamaized.net/pool_87948813/live/ww/bbc_world_service/bbc_world_service.isml/bbc_world_service-audio%3d96000.norewind.m3u8"));

		stations.add(new RadioStation("--- LONDON COMMERCIAL ---"));
		// updated with capitalFM
		stations.add(new RadioStation("Capital FM", "CapitalFM / LDN", "http://media-ice.musicradio.com/CapitalMP3"));
		
    }

    private void playStream(String url, String name) {
        if (getActivity() == null) return;
        Toast.makeText(getActivity(), "Buffering: " + name, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getActivity(), RadioService.class);
        intent.putExtra("url", url);
        intent.putExtra("name", name);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getActivity().startForegroundService(intent);
        } else {
            getActivity().startService(intent);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Register the receiver to listen for Next/Prev button clicks
        IntentFilter filter = new IntentFilter();
        filter.addAction("ACTION_NEXT");
        filter.addAction("ACTION_PREV");
        if (getActivity() != null) {
            getActivity().registerReceiver(stationReceiver, filter);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        // Unregister to prevent memory leaks
        if (getActivity() != null) {
            getActivity().unregisterReceiver(stationReceiver);
        }
    }
}
