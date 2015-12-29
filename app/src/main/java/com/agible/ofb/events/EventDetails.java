package com.agible.ofb.events;

import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.agible.ofb.data.Values;
import com.agible.ofb.utils.MActionBar;
import com.agible.ofb.R;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.views.MapView;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.MapView;
//import com.google.android.gms.maps.OnMapReadyCallback;


public class EventDetails extends ActionBarActivity {
public static final String EVENT_ARG = "event";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);


        String event = getIntent().getStringExtra(EVENT_ARG);


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, DetailsFragment.newInstance(event))
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

        Values.Events event;
        boolean mapsSupported;

        MapView mapView;

        public DetailsFragment() {
        }


        //static class for creating fragment.
        public static DetailsFragment newInstance(String event) {
            Bundle b = new Bundle();
            b.putString(EVENT_ARG, event);
            DetailsFragment fragment = new DetailsFragment();
            fragment.setArguments(b);
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            //get the event from the arguments.
            try {
                event = (Values.Events) Values.deserialize(getArguments().getString(EVENT_ARG));
            } catch (IOException | ClassNotFoundException e) {
                Log.e("EventDetails", e.getMessage());
            }
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

            //inflate our views from the XML layout.
            final TextView leader_name = (TextView) v.findViewById(R.id.event_details_leader_name);
            final TextView user_name = (TextView) v.findViewById(R.id.event_details_user_name);
            final TextView user_aware = (TextView) v.findViewById(R.id.event_details_user_aware);
            final TextView event_status = (TextView) v.findViewById(R.id.event_details_status);
            final TextView event_start = (TextView) v.findViewById(R.id.event_details_start_time);
            final TextView event_end = (TextView) v.findViewById(R.id.event_details_end_time);
            final TextView event_desc = (TextView) v.findViewById(R.id.event_details_description);
            final TextView event_title = (TextView) v.findViewById(R.id.event_details_title);
            final Button event_join = (Button) v.findViewById(R.id.event_details_join);
            final Button event_lead = (Button) v.findViewById(R.id.event_details_lead);

            //set the values.
            user_name.setText("For: " + event.PersonFirstName + " " + event.PersonLastName);
            if (!event.PersonIsAware)
                user_aware.setText("User is not aware of planned event!");

            //convert the event status codes to string values.
            switch (event.Status) {
                case Values.STATUS_PENDING:
                    event_status.setText("Status: pending");
                    break;
                case Values.STATUS_ACTIVE:
                    event_status.setText("Status: active");
                    break;
                case Values.STATUS_COMPLETED:
                    event_status.setText("Status: completed");
                    break;
                default:
                    event_status.setText("Status: undetermined");
                    break;
            }

            //if the start date isn't a valid date, don't do anything with it.
            if (event.StartDate != 0)
                event_start.setText(new Date(event.StartDate).toString());

            //if the event end date isn't a valid date, don't do anything with it.
            if (event.EndDate != 0)
                event_end.setText(new Date(event.EndDate).toString());

            //set the event description.
            event_desc.setText(event.Description);

            //set the event title.
            event_title.setText(event.Name);

            //setup our onclick listeners.
            event_join.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /**TODO ADD ACTION
                     *
                     * Check if user has already joined event or is already leading event.
                     *
                     */
                }
            });

            event_lead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /**TODO ADD ACTION
                     *
                     */
                }
            });

            actionBar.setLeftOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().finish();
                }
            });

//            // Gets the MapView from the XML layout and creates it
//            MapView mapView = (MapView) v.findViewById(R.id.map_view);
//            mapView.onCreate(savedInstanceState);
//
//            // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
//            MapsInitializer.initialize(this.getActivity());
//            mapView.getMapAsync(new OnMapReadyCallback() {
//                @Override
//                public void onMapReady(GoogleMap map) {
//                    map.getUiSettings().setMyLocationButtonEnabled(false);
//                    map.setMyLocationEnabled(true);
//                    // Updates the location and zoom of the MapView
//                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(49.2827, 123.1207), 10);
//
//                    map.animateCamera(cameraUpdate);
//                }
//            });

            mapView = (MapView) v.findViewById(R.id.mapview);
            mapView.setStyleUrl(Style.MAPBOX_STREETS);
            LatLng eventLocation = new LatLng(event.Latitude, event.Longitude);
            mapView.setCenterCoordinate(eventLocation);
            MarkerOptions eventMarker = new MarkerOptions();
            eventMarker.position(eventLocation);
            eventMarker.title(event.Name);
            mapView.addMarker(eventMarker);
            mapView.setZoomLevel(11);
            mapView.onCreate(savedInstanceState);


            return v;
        }


    }
}
