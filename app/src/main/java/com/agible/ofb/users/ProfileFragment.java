package com.agible.ofb.users;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.agible.ofb.R;
import com.agible.ofb.churches.Churches;
import com.agible.ofb.data.Values;
import com.agible.ofb.events.Events;
import com.agible.ofb.listeners.EventsListener;
import com.agible.ofb.listeners.OnFinishListener;
import com.agible.ofb.utils.MActionBar;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EventsListener listener;
    private Events activity;
    boolean loggedin = false;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);

        try {
            listener = (EventsListener) activity;
            this.activity = (Events) activity;
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
        setRetainInstance(true);

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.profile_layout, null, true);
        MActionBar actionBar = new MActionBar(getActivity(), v, R.id.profile_ab);
        actionBar.setTitle("Profile");
        actionBar.setBackground(R.drawable.profile_ab_bg);
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

        final Button login = (Button)v.findViewById(R.id.profile_login);

        //set the login button to say logout if we aren't authenticated..
        if(activity.values.checkAuth()) {
            login.setText("Logout");
            actionBar.setRightDrawable(R.drawable.ic_action_edit);
            actionBar.setRightOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getActivity(), Login.class);
                    i.putExtra("mode", 1);
                    startActivityForResult(i, 200);
                }
            });
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(login.getText().equals("Logout")){

                    login.setText("Login");
                    return;
                }
                Intent i = new Intent(getActivity(), Login.class);
                startActivity(i);
                startActivityForResult(i, 2);
            }
        });



        RelativeLayout contain = (RelativeLayout)v.findViewById(R.id.profile_container);
        final TextView name = (TextView)v.findViewById(R.id.profile_name);
        final TextView phone = (TextView)v.findViewById(R.id.profile_phone);
        final TextView email = (TextView)v.findViewById(R.id.profile_email);
        final TextView skills = (TextView)v.findViewById(R.id.profile_skills);
        final TextView status = (TextView)v.findViewById(R.id.profile_status);
        final TextView bio = (TextView)v.findViewById(R.id.profile_bio);
        final TextView address = (TextView)v.findViewById(R.id.profile_address);
        final TextView church = (TextView)v.findViewById(R.id.profile_church);

        try {
            List<Object> objects = activity.values.loadObjects(Values.Users.class);
            final Values.Users user = (Values.Users) objects.get(0);


            name.setText(user.FirstName + " " + user.LastName);
            phone.setText(user.Phone);
            email.setText(user.Email);
            skills.setText(user.Skills);
            status.setText(getStatus(user.Status));
            bio.setText(user.Bio);
            Futures.addCallback(activity.values.getChurch(user.ChurchID), new FutureCallback<Values.Churches>() {
                @Override
                public void onSuccess(Values.Churches result) {
                    String church_text;
                    if(user.Status == Values.STATUS_PENDING)
                        church_text = "Pending: " + result.ChurchName;
                    else if(user.Status == Values.STATUS_BLOCKED)
                        church_text = "Blocked: " + result.ChurchName;
                    else
                        church_text = result.ChurchName;
                    church.setText(church_text);
                }

                @Override
                public void onFailure(Throwable t) {
                    /**TODO
                     * add something here.
                     */
                }
            });
            String addr = user.Address1 + ", " +
                    user.Address2 + ", " +
                    user.City + " " +
                    user.State + ", " +
                    user.PostalCode + " " +
                    user.Country;
            address.setText(addr);
        }catch (ClassCastException | ClassNotFoundException | IOException e){



        }

        church.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Curches butotn pressed");
                if(church.getText().equals(getResources().getString(R.string.default_church))){
                    Intent i = new Intent(getActivity(), Churches.class);
                    startActivity(i);
                }
            }
        });

//        LineChart lineChart = new LineChart(getActivity());
//        List<Entry> items = new ArrayList<>();
//        items.add(new Entry(50f, 0));
//        items.add(new Entry(20f, 1));
//        items.add(new Entry(10f, 2));
//        items.add(new Entry(50f, 3));
//        items.add(new Entry(70f, 4));
//        items.add(new Entry(10f, 5));
//        items.add(new Entry(5f, 6));
//        LineDataSet dataSet = new LineDataSet(items, "Events I Created");
//        dataSet.setCircleColor(Color.parseColor("#ff00ff"));
//        dataSet.setCircleSize(5f);
//        dataSet.setLineWidth(2f);
//        dataSet.setColor(Color.BLUE);
//
//        List<Entry> items1 = new ArrayList<>();
//        items1.add(new Entry(30f, 0));
//        items1.add(new Entry(10f, 1));
//        items1.add(new Entry(15f, 2));
//        items1.add(new Entry(15f, 3));
//        items1.add(new Entry(5f, 4));
//        items1.add(new Entry(7f, 5));
//        items1.add(new Entry(2f, 6));
//        LineDataSet dataSet1 = new LineDataSet(items1, "Events I Participated");
//        dataSet1.setCircleColor(Color.parseColor("#00ff00"));
//        dataSet1.setCircleSize(5f);
//        dataSet1.setLineWidth(2f);
//        dataSet1.setColor(Color.GREEN);
//
//        LineData data = new LineData(new String[] {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"}, dataSet);
//        data.addDataSet(dataSet1);
//        lineChart.setData(data);
//        lineChart.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
//        contain.addView(lineChart);


        //first check for account.


        return v;
    }

    public String getStatus(int status){
        switch(status){
            case 0:
                return "Volunteer";
            case 1:
                return "Pending Approval";
            case 2:
                return "Blocked";
            default:
                return "Unknown";
        }
    }


}
