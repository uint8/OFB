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

import com.agible.ofb.utils.MActionBar;
import com.agible.ofb.listeners.EventsListener;
import com.agible.ofb.listeners.OnItemClickListener;
import com.agible.ofb.R;
import com.agible.ofb.data.Values;

import java.util.ArrayList;
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
        List<EventItem>dummydata = new ArrayList<>();
        List<Values.Events> data = new ArrayList<>();
        Values.Events event1 = new Values.Events();
        event1.Anonymous = false;
        event1.Category = 1;
        event1.Status = 1;
        event1.id = "mEventId";
        event1.Name = "Clean Rob's shed.";
        data.add(event1);
        Values.Events event2 = new Values.Events();
        event2.Anonymous = false;
        event2.Category = 1;
        event2.Status = 1;
        event2.id = "mEventId";
        event2.Name = "Move Kathy's bee hives.";
        data.add(event2);
        Values.Events event3 = new Values.Events();
        event3.Anonymous = false;
        event3.Category = 1;
        event3.Status = 2;
        event3.id = "mEventId";
        event3.Name = "Repair Mike's roof.";
        data.add(event3);
        Values.Events event4 = new Values.Events();
        event4.Anonymous = false;
        event4.Category = 1;
        event4.Status = 3;
        event4.id = "mEventId";
        event4.Name = "Paint Trevor's house.";
        data.add(event4);
        Values.Events event5 = new Values.Events();
        event5.Anonymous = false;
        event5.Category = 1;
        event5.Status = 2;
        event5.id = "mEventId";
        event5.Name = "Handout Glow tracks.";
        data.add(event5);
        Values.Events event6 = new Values.Events();
        event6.Anonymous = false;
        event6.Category = 1;
        event6.Status = 2;
        event6.id = "mEventId";
        event6.Name = "Move Amy into her new home.";
        data.add(event6);


        adapter = new EventsAdapter(getActivity(), data, R.layout.event_item);
        adapter.setOnItemClickListener(new OnItemClickListener<Values.Events>() {
            @Override
            public void onClick(Values.Events item, int i) {
                System.out.println(item.Name);
                Intent intent = new Intent(getActivity(), EventDetails.class);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
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
