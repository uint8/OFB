package com.agible.ofb.events;

import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.agible.ofb.utils.MActionBar;
import com.agible.ofb.R;

//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.MapView;
//import com.google.android.gms.maps.OnMapReadyCallback;


public class EventDetails extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new DetailsFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_event_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class DetailsFragment extends Fragment {

        public DetailsFragment() {
        }

       // GoogleMap googleMap;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.event_details_layout, container, false);

            MActionBar actionBar = new MActionBar(getActivity(), v, R.id.event_details_ab);
            actionBar.setTitle("Details");
            actionBar.setBackground(R.drawable.actionbar_bg);
            actionBar.setRightDrawable(R.drawable.none);
            actionBar.setLeftDrawable(R.drawable.ic_action_back);
            actionBar.setTitleColor(Color.WHITE);
            actionBar.setTitleSize(20f);

            actionBar.setLeftOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().finish();
                }
            });
//            MapView map = (MapView)v.findViewById(R.id.event_details_map_view);
//
//            map.onSaveInstanceState(savedInstanceState);
//            map.getMapAsync(new OnMapReadyCallback() {
//                @Override
//                public void onMapReady(GoogleMap googleMap) {
//                    DetailsFragment.this.googleMap = googleMap;
//                    googleMap.setMyLocationEnabled(true);
//                }
//            });
//
//            actionBar.setLeftOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    getActivity().finish();
//                }
//            });

            return v;
        }
    }
}
