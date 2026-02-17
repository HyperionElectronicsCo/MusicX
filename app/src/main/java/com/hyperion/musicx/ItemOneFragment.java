package com.hyperion.musicx;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView; // Added for color manipulation
import android.graphics.Color;    // Added for Color.WHITE
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;

public class ItemOneFragment extends Fragment {
    private ProgressBar progressBar;
    private ListView rssListView;
    private ArrayList<String> titles = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    public static ItemOneFragment newInstance() {
        return new ItemOneFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_one, container, false);

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        rssListView = (ListView) view.findViewById(R.id.rssListView);

        // Overriding getView programmatically to apply white text color
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, titles) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View row = super.getView(position, convertView, parent);
                // simple_list_item_1 is a TextView with this specific system ID
                TextView text = (TextView) row.findViewById(android.R.id.text1);
                text.setTextColor(Color.WHITE);
                return row;
            }
        };

        rssListView.setAdapter(adapter);

        // Replace with your actual RSS feed URL
        loadFeed("https://rss.marketingtools.apple.com/api/v2/gb/music/most-played/50/songs.rss");

        return view;
    }

    private void loadFeed(final String urlString) {
        new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						URL url = new URL(urlString);
						HttpURLConnection conn = (HttpURLConnection) url.openConnection();
						conn.setReadTimeout(10000);
						conn.setConnectTimeout(15000);
						conn.setRequestMethod("GET");
						conn.connect();

						InputStream stream = conn.getInputStream();
						XmlPullParser parser = Xml.newPullParser();
						parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
						parser.setInput(stream, null);

						parseXml(parser);
						stream.close();

						updateUI();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
    }

    private void parseXml(XmlPullParser parser) throws Exception {
        int eventType = parser.getEventType();
        boolean insideItem = false;

        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                if (parser.getName().equalsIgnoreCase("item")) {
                    insideItem = true;
                } else if (parser.getName().equalsIgnoreCase("title")) {
                    if (insideItem) titles.add(parser.nextText());
                }
            } else if (eventType == XmlPullParser.END_TAG && parser.getName().equalsIgnoreCase("item")) {
                insideItem = false;
            }
            eventType = parser.next();
        }
    }

    private void updateUI() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						progressBar.setVisibility(View.GONE);
						rssListView.setVisibility(View.VISIBLE);
						adapter.notifyDataSetChanged();
					}
				});
        }
    }
}

