package com.agible.ofb.churches;

import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.agible.ofb.R;
import com.agible.ofb.data.Values;
import com.agible.ofb.utils.MActionBar;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.authentication.MobileServiceUser;

import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.List;

public class ChurchDetails extends ActionBarActivity {
    public static final String CHURCH_ARG = "church";

    public MobileServiceClient mClient;
    public Values values;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_churches);
        if (savedInstanceState == null) {
            Serializable s = getIntent().getSerializableExtra(CHURCH_ARG);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, ChurchDetailsFragment.newInstance(s))
                    .commit();
        }


        try {
            mClient = new MobileServiceClient(
                    Values.MOBILE_SERVICE_URL,
                    Values.APPLICATION_KEY,
                    this
            );

            values = new Values(getApplicationContext(), mClient);
            MobileServiceUser user = new MobileServiceUser(values.getUserId());
            user.setAuthenticationToken(values.getAuthToken());
            if(user.getUserId() != null && user.getUserId().length() > 5)
                mClient.setCurrentUser(user);

        } catch (MalformedURLException e){
            Log.e("Events", e.getMessage());
        }


        //setup the adapter.

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_churches, menu);
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
    public static class ChurchDetailsFragment extends Fragment {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


        // TODO: Rename and change types of parameters
        private Values.Churches church;
        private ChurchDetails activity;
        private OnFragmentInteractionListener mListener;


        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param church Parameter 1.
         * @return A new instance of fragment ChurchDetails.
         */
        // TODO: Rename and change types and number of parameters
        public static ChurchDetailsFragment newInstance(Serializable church) {
            ChurchDetailsFragment fragment = new ChurchDetailsFragment();
            Bundle args = new Bundle();
            args.putSerializable(CHURCH_ARG, church);
            fragment.setArguments(args);
            return fragment;
        }

        public ChurchDetailsFragment() {
            // Required empty public constructor
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (getArguments() != null) {
                try {
                    church = (Values.Churches) Values.deserialize(getArguments().getString(CHURCH_ARG));
                } catch (ClassNotFoundException | IOException e){
                    Log.e("ChurchDetails", e.getMessage());
                }
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            View v = inflater.inflate(R.layout.church_details_layout, container, false);

            //#FFD875
            final MActionBar actionBar = new MActionBar(getActivity(), v, R.id.church_details_ab);
            actionBar.setTitle("Church Details");
            actionBar.setBackground(R.drawable.churches_ab_bg);
            actionBar.setLeftDrawable(R.drawable.ic_action_back);
            actionBar.setRightDrawable(R.drawable.none);
            actionBar.setLeftOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                }
            });
            actionBar.setTitleColor(Color.WHITE);
            actionBar.setTitleSize(20f);

            final TextView church_name = (TextView) v.findViewById(R.id.church_details_name);
            final TextView church_address = (TextView) v.findViewById(R.id.church_details_address);
            final TextView church_email = (TextView) v.findViewById(R.id.church_details_email);
            final TextView church_phone = (TextView) v.findViewById(R.id.church_details_phone);
            final TextView pastor_name = (TextView) v.findViewById(R.id.church_details_pastor_name);
            final TextView pastor_email = (TextView) v.findViewById(R.id.church_details_pastor_email);
            final TextView pastor_phone = (TextView) v.findViewById(R.id.church_details_pastor_phone);
            final Button join = (Button) v.findViewById(R.id.church_details_join);

            church_name.setText(church.ChurchName);
            String address = church.Address1 + "\n" +
                    church.Address2 + "\n" +
                    church.City + " " + church.State + "\n" +
                    church.PostalCode + "\n" +
                    church.Country;
            church_address.setText(address);
            church_email.setText(church.ChurchEmail);
            church_phone.setText(church.ChurchPhone);

            pastor_email.setText(church.PastorEmail);
            pastor_name.setText("Pastor " + church.PastorFName + " " + church.PastorLName);
            pastor_phone.setText(church.PastorPhone);

            join.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        //make web request to join church.
                        List<Object> objects = activity.values.loadObjects(Values.Users.class);
                        Values.Users user = (Values.Users) objects.get(0);
                        user.ChurchID = church.id;
                        user.Status = Values.STATUS_PENDING;
                        Futures.addCallback(activity.values.updateUser(user), new FutureCallback<Values.Users>() {
                            @Override
                            public void onSuccess(Values.Users result) {
                                Toast.makeText(getActivity(), "We're reviewing your request. We'll let you know whether it's been accepted shortly.", Toast.LENGTH_SHORT).show();
                                join.setText("Processing request");
                                join.setOnClickListener(null);
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                Toast.makeText(getActivity(), "Ooops! Looks like we've encountered an error. Please try again later!", Toast.LENGTH_SHORT).show();
                            }
                        });




                    }catch (IOException | ClassNotFoundException e){
                        Log.e("ChurchDetails",e.getMessage());
                    }
                }
            });

            return v;
        }

        // TODO: Rename method, update argument and hook method into UI event
        public void onButtonPressed(Uri uri) {
            if (mListener != null) {
                mListener.onFragmentInteraction(uri);
            }
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            try {
                //mListener = (OnFragmentInteractionListener) activity;
                this.activity = (ChurchDetails) activity;
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString()
                        + " must implement OnFragmentInteractionListener");
            }
        }

        @Override
        public void onDetach() {
            super.onDetach();
            mListener = null;
        }

        /**
         * This interface must be implemented by activities that contain this
         * fragment to allow an interaction in this fragment to be communicated
         * to the activity and potentially other fragments contained in that
         * activity.
         * <p/>
         * See the Android Training lesson <a href=
         * "http://developer.android.com/training/basics/fragments/communicating.html"
         * >Communicating with Other Fragments</a> for more information.
         */
        public interface OnFragmentInteractionListener {
            // TODO: Update argument type and name
            public void onFragmentInteraction(Uri uri);
        }

    }

}