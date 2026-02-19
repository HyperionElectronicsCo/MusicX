package com.hyperion.musicx;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.FragmentTransaction; 
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.widget.ListView;
import android.widget.TextView;
import android.graphics.Color;
import com.hyperion.musicx.libraryadapters.SettingsFragment;

public class ItemFiveFragment extends ListFragment {

    public static ItemFiveFragment newInstance() {
        return new ItemFiveFragment();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Array reduced to 7 items (index 0-6)
        final String[] values = new String[] { 
            "All Songs", "Downloads", "Playlists", "Favourites", 
            "Recently Played", "Messages", "Settings"
        };

        // Create an anonymous subclass of ArrayAdapter to style the text
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), 
																android.R.layout.simple_list_item_1, values) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);

                // Set text color to white
                text.setTextColor(Color.WHITE); 
                return view;
            }
        };

        setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, 
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_item_five, container, false);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        // Get the value of the clicked item for the Toast message
        String itemValue = (String) l.getItemAtPosition(position);

        switch (position) {
            case 0: // All Songs
                Toast.makeText(getActivity(), "Opening: " + itemValue, Toast.LENGTH_SHORT).show();
                break;
            case 1: // Downloads
                Toast.makeText(getActivity(), "Opening: " + itemValue, Toast.LENGTH_SHORT).show();
                break;
            case 2: // Playlists
                Toast.makeText(getActivity(), "Opening: " + itemValue, Toast.LENGTH_SHORT).show();
                break;
            case 3: // Favourites
                Toast.makeText(getActivity(), "Opening: " + itemValue, Toast.LENGTH_SHORT).show();
                break;
            case 4: // Recently Played
                Toast.makeText(getActivity(), "Opening: " + itemValue, Toast.LENGTH_SHORT).show();
                break;
            case 5: // Messages
                Toast.makeText(getActivity(), "Opening: " + itemValue, Toast.LENGTH_SHORT).show();
                break;
            case 6: // Settings
                // Initialize the new fragment
                SettingsFragment settingsFrag = new SettingsFragment();
                // Start transaction using the Support FragmentManager
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                // Replace current fragment with SettingsFragment
                transaction.replace(R.id.frame_layout, settingsFrag);
                // Add to back stack so the back button returns to this list
                transaction.addToBackStack(null);
                // Commit the change
                transaction.commit();
                break;
            default:
                break;
        }
    }
}

