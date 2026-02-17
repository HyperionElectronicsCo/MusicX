package com.hyperion.musicx;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.widget.ListView;
import android.annotation.Nullable;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.graphics.Color;


public class ItemFiveFragment extends ListFragment implements View.OnClickListener {


    public static ItemFiveFragment newInstance() {
        ItemFiveFragment fragment = new ItemFiveFragment();
        return fragment;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String[] values = new String[] { "All Songs", "Downloads", "Playlists", "Favourites", "Recently Played", "Messages", "Settings", "Feedback", "Check for updates", "About" };

// Create an anonymous subclass of ArrayAdapter
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), 
																android.R.layout.simple_list_item_1, values) {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// Use super.getView to get the default View for simple_list_item_1
				View view = super.getView(position, convertView, parent);

				// simple_list_item_1 is just a TextView, so we cast it
				TextView text = (TextView) view.findViewById(android.R.id.text1);

				// Set your desired colour (e.g., BLUE, RED, or a Hex code)
				text.setTextColor(Color.WHITE); 

				return view;
			}
		};

		setListAdapter(adapter);
		
		
    }
    @Override
    public View onCreateView(LayoutInflater inflater, 
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_five, container, false);
        return view;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case 0:
                Toast.makeText(getContext(), "Button One", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                Toast.makeText(getContext(), "Button Two", Toast.LENGTH_SHORT).show();
                break;
        }}}
