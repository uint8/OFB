package com.agible.ofb.events;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.agible.ofb.map.Map;
import com.agible.ofb.utils.MActionBar;
import com.agible.ofb.utils.MSpinnerAdapter;
import com.agible.ofb.R;
import com.agible.ofb.data.Values;
import com.agible.ofb.listeners.EventsListener;
import com.agible.ofb.utils.GPSTracker;
import com.agible.ofb.utils.Utilities;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.nlopez.smartlocation.OnGeocodingListener;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.OnReverseGeocodingListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.geocoding.utils.LocationAddress;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText name;
    private EditText desc;
    private EditText address1;
    private EditText address2;
    private EditText city;
    private EditText state;
    private EditText country;
    private EditText postalCode;
    private CheckBox person_aware;
    private EditText person_name;
    private long longitude;
    private long latitude;
    private Button create;
    private Button cancel;
    private EventsListener listener;
    private int pplneeded;
    private int gender;
    private boolean enter_address = false;
    private List<String> genderArray;
    private List<String> peopleArray;
    private Values values;
    private Events activity;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddEventFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EventFragment newInstance(String param1, String param2) {
        EventFragment fragment = new EventFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public EventFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);

        try {
            listener = (EventsListener) activity;
            this.activity = (Events)activity;
        }catch (Exception e){
            Log.e("EventsFragment", e.getMessage());
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.event_layout, null, false);

        //setup the actionbar.
        MActionBar actionBar = new MActionBar(getActivity(), v, R.id.addevent_ab);
        actionBar.setTitle("New Event");
        actionBar.setBackground(R.drawable.addevent_ab_bg);
        actionBar.setRightDrawable(R.drawable.none);
        actionBar.setLeftDrawable(R.drawable.ic_action_back);
        actionBar.setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onPageChange(1);
            }
        });
        actionBar.setTitleColor(Color.WHITE);
        actionBar.setTitleSize(20f);
        //find each of our subviews from our parent view.
        final Spinner genderSpinner = (Spinner)v.findViewById(R.id.addevent_gender);
        final Spinner peopleSpinner = (Spinner)v.findViewById(R.id.addevent_people);
        final RelativeLayout enter_addr_layout = (RelativeLayout)v.findViewById(R.id.addevent_addr_enter);
        Button enteraddress = (Button)v.findViewById(R.id.addevent_enteraddress);
        Button mylocation = (Button)v.findViewById(R.id.addevent_mylocation);
        final LinearLayout address_parent = (LinearLayout)v.findViewById(R.id.addevent_addr_parent);
        create = (Button)v.findViewById(R.id.addevent_create);
        cancel = (Button)v.findViewById(R.id.addevent_cancel);
        name = (EditText)v.findViewById(R.id.addevent_name);
        desc = (EditText)v.findViewById(R.id.addevent_desc);
        address1 = (EditText)v.findViewById(R.id.addevent_addr1);
        address2 = (EditText)v.findViewById(R.id.addevent_addr2);
        city = (EditText)v.findViewById(R.id.addevent_city);
        state = (EditText)v.findViewById(R.id.addevent_state);
        country = (EditText)v.findViewById(R.id.addevent_country);
        postalCode = (EditText)v.findViewById(R.id.addevent_postalcode);
        person_aware = (CheckBox)v.findViewById(R.id.addevent_user_aware);
        person_name = (EditText)v.findViewById(R.id.addevent_user_name);

        values = new Values(getActivity());

        //enter address button.
        enteraddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                enter_address = true;

                //make the address boxes visible.
                enter_addr_layout.setVisibility(View.VISIBLE);

                //make the address parent invisible.
                address_parent.setVisibility(View.GONE);

            }
        });

        //use location button.
        mylocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get our location once.
                SmartLocation.with(getActivity()).location()
                        .oneFix()
                        .start(new OnLocationUpdatedListener() {
                            @Override
                            public void onLocationUpdated(Location location) {
                                //when the location is received, get an address from it.
                                latitude = (long)location.getLatitude();
                                longitude = (long)location.getLongitude();

                                SmartLocation.with(getActivity()).geocoding()
                                        .reverse(location, new OnReverseGeocodingListener() {
                                            @Override
                                            public void onAddressResolved(Location original, List<Address> results) {
                                                //populate the address, city, state, country, and postal code with the address.
                                                address1.setText(results.get(0).getAddressLine(0));
                                                address2.setText(results.get(0).getAddressLine(1));
                                                city.setText(results.get(0).getAddressLine(2));
//                                                state.setText(results.get(0).get);
                                                country.setText(results.get(0).getCountryName());
                                                  for(Address address : results)
                                                      for(int i = 0; i < address.getMaxAddressLineIndex(); ++i)
                                                          System.out.println(address.getAddressLine(i));

                                            }
                                        });
                            }
                        });
            }
        });


        genderArray = new ArrayList<>();
        genderArray.add("Gender");
        genderArray.add("All");
        genderArray.add("Male");
        genderArray.add("Female");
        genderSpinner.setAdapter(new MSpinnerAdapter(getActivity(), R.layout.spinner_item, R.layout.spinner_drop_item, genderArray));
        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getActivity(), "Item selected", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Utilities utilities = new Utilities(getActivity());
                final Values.Events event = new Values.Events();
                String id = createId();
                event.id = id;
                event.CreatorUserID = values.getUserId();
                event.Address1 = utilities.getStringFromView(address1);
                event.Address2 = address2.getText().toString();
                event.City = utilities.getStringFromView(city);
                event.State = utilities.getStringFromView(state);
                event.Country = utilities.getStringFromView(country);
                event.PostalCode = utilities.getStringFromView(postalCode);
                event.Name = utilities.getStringFromView(name);
                event.Description = utilities.getStringFromView(desc);
                event.GenderNeeded = gender;
                event.PeopleNeeded = pplneeded;
                event.Anonymous = false;
                event.StartDate = 0;
                event.CreationDate = new Date().getTime();
                event.EndDate = 0;
                event.LeaderUserID = "";
                event.Category = Values.CATEGORY_DEFAULT;
                event.Status = Values.STATUS_PENDING;
                event.ChurchId = values.getChurchId();


                //if the address was manually entered, we try to determine the location from it.

                String addr = event.Address1 + " " + event.Address2 + " " + event.City + " " + event.State + " " + event.PostalCode + " " + event.Country;
                System.out.println(addr);

                if(enter_address)
                SmartLocation.with(getActivity()).geocoding()
                        .direct(addr, new OnGeocodingListener() {
                            @Override
                            public void onLocationResolved(String name, List<LocationAddress> results) {
                                // name is the same you introduced in the parameters of the call
                                // results could come empty if there is no match, so please add some checks around that
                                // LocationAddress is a wrapper class for Address that has a Location based on its data
                                if (results.size() > 0) {
                                    Location addressLocation = results.get(0).getLocation();
                                    event.Latitude = (long)addressLocation.getLatitude();
                                    event.Longitude = (long)addressLocation.getLongitude();
                                    createEvent(event, utilities);
                                } else {
                                     /**TODO
                                     * Make popup that alerts user to the fact that we were unable to find a location from the address and prompts the user to use the map to select the location. . .
                                     * TODO
                                     * Remove Toast later.
                                     */

                                    //if we can't find the location from the address, we force the user to insert it.
                                    Intent i = new Intent(getActivity(), Map.class);
                                    /**TODO CHANGE TO RESULT ACTIVITY
                                     *
                                     */
                                    startActivity(i);

                                    Toast.makeText(getActivity(), "We were unable to find any locations associated with the provided address. Please try a different address.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                else
                    createEvent(event, utilities);




            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventFragment.this.onDestroy();
                listener.onPageChange(1);
            }
        });



        peopleArray = new ArrayList<>();
        peopleArray.add("People");
        peopleArray.add("No limit");
        peopleArray.add("1+");
        peopleArray.add("2+");
        peopleArray.add("3+");
        peopleArray.add("5+");
        peopleArray.add("7+");
        peopleArray.add("10+");
        peopleArray.add("15+");
        peopleArray.add("20+");
        peopleArray.add("25+");
        peopleArray.add("30+");
        peopleSpinner.setAdapter(new MSpinnerAdapter(getActivity(), R.layout.spinner_item, R.layout.spinner_drop_item, peopleArray));
        peopleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //0 = not filled. 1 = no limit. 2 = 1+.
                pplneeded = parsePeople( i );
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return v;
    }

    public int parsePeople(int at){
        if(at == 0 || at == 1)
            return 0;
        String p = peopleArray.get(at).replaceAll("\\+", "");
        return Integer.valueOf(p);
    }

    public void createEvent(Values.Events event, Utilities utilities){

        /**TODO move below code into function and make it called twice depending on whether the user enters the address or the address is reverse geocoded.
         *
         */
        //split the pastor's name.
        String[] PNames = person_name.getText().toString().split("\\s");

        //make sure it's got both the first and last name.
        if(PNames.length != 2)
            return;
        //add the first and last name of the pastor to our church object.
        event.PersonFirstName = PNames[0];
        event.PersonLastName = PNames[1];

        event.PersonIsAware = person_aware.isChecked();
        event.ViewCount = 1;

        //run checks for each string.
        if(!utilities.checkObjects(event.Address1, event.City, event.State, event.Name, event.Description, event.CreatorUserID, event.id, event.Country, event.PostalCode, event.PersonFirstName, event.PersonLastName, event.ChurchId))
            return;

        Futures.addCallback(activity.values.addEvent(event), new FutureCallback<Values.Events>() {
            @Override
            public void onSuccess(Values.Events result) {
                Toast.makeText(getActivity(), "Event Added.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getActivity(), "Ooops! We were unable to create the event at this time. Please try again later. ", Toast.LENGTH_SHORT).show();
                Log.e("AddEvent", t.getLocalizedMessage());
            }
        });
    }



    public void toast(){
        Toast.makeText(getActivity(), "Please ensure that all required fields are filled out.", Toast.LENGTH_SHORT).show();
    }
    Values.Events getEvent(){

        Values.Events event = new Values.Events();
        event.Description = desc.getText().toString();
        event.Name = name.getText().toString();
        event.PeopleNeeded = Integer.valueOf(peopleArray.get(pplneeded).replace("+", ""));
        event.GenderNeeded = gender;
        event.CreatorUserID = new Values(getActivity()).getUserId();


        return event;
    }

    String createId(){

        Date d = new Date();
        String i2s = String.valueOf(d.getTime());
        return values.getUserId() + ":" + i2s;


    }
}
