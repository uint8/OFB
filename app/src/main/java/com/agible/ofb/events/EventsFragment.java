package com.agible.ofb.events;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.agible.ofb.listeners.OnFinishListener;
import com.agible.ofb.map.Map;
import com.agible.ofb.utils.MActionBar;
import com.agible.ofb.listeners.EventsListener;
import com.agible.ofb.listeners.OnItemClickListener;
import com.agible.ofb.R;
import com.agible.ofb.data.Values;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    private EventsListener listener;
    private EventsAdapter adapter;
    private Events activity;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EventsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EventsFragment newInstance(String param1, String param2) {
        EventsFragment fragment = new EventsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public EventsFragment() {
        // Required empty public constructor
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.events_layout, null, true);

        MActionBar actionBar = new MActionBar(getActivity(), v, R.id.events_ab);
        actionBar.setTitle("Events");
        actionBar.setBackground(R.drawable.actionbar_bg);
        actionBar.setRightDrawable(R.drawable.ic_action_new_event);
        actionBar.setLeftDrawable(R.drawable.ic_action_person);
        actionBar.setTitleColor(Color.WHITE);
        actionBar.setTitleSize(20f);

        recyclerView = (RecyclerView)v.findViewById(R.id.events_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        Futures.addCallback(activity.values.getEvents(activity.values.getChurchId()), new FutureCallback<MobileServiceList<Values.Events>>() {
            @Override
            public void onSuccess(MobileServiceList<Values.Events> result) {

                result.get(0).Status = Values.STATUS_PENDING;
                //initiate a adapter.
                adapter = new EventsAdapter(getActivity(), result, R.layout.event_item);

                //set the on click listener.
                adapter.setOnItemClickListener(new OnItemClickListener<Values.Events>() {
                    @Override
                    public void onClick(Values.Events item, int i) {
                        System.out.println(item.Name);


                        startActivity(new Intent(getActivity(), Map.class));
                        //return;

                        /** TODO REMOVE COMMENTING LATER **/
                        //try {




//                            //create an intent and pass the event item as a string into the activity.
//                            Intent intent = new Intent(getActivity(), EventDetails.class);
//                            intent.putExtra(EventDetails.EVENT_ARG, Values.serialize(item));
//                            startActivity(intent);
                        //} catch (IOException e) {
                        //  Log.e("EventsFragment", e.getMessage());
                        // }
                    }
                });
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });

        //set on click listeners for right and left actionbar buttons.
        actionBar.setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onPageChange(0);
            }
        });
        actionBar.setRightOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onPageChange(2);
            }
        });


        return v;
    }


}
