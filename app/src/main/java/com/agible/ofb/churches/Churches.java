package com.agible.ofb.churches;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.agible.ofb.R;
import com.agible.ofb.data.Values;
import com.agible.ofb.utils.MActionBar;
import com.agible.ofb.utils.Utilities;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.authentication.MobileServiceUser;

import java.net.MalformedURLException;


public class Churches extends ActionBarActivity {
    public MobileServiceClient mClient;
    public Values values;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_churches);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ChurchesFragment())
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class ChurchesFragment extends Fragment {

        public ChurchesFragment() {
        }
        Churches activity;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.churches_layout, container, false);

            //#FFD875
            final MActionBar actionBar = new MActionBar(getActivity(), v, R.id.churches_ab);
            actionBar.setTitle("Churches");
            actionBar.setBackground(R.drawable.churches_ab_bg);
            actionBar.setLeftDrawable(R.drawable.ic_action_back);
            actionBar.setRightDrawable(R.drawable.ic_action_new);
            actionBar.setLeftOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                }
            });
            actionBar.setTitleColor(Color.WHITE);
            actionBar.setTitleSize(20f);
            actionBar.setRightOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.toggle();
                }
            });

            RecyclerView rv = (RecyclerView)v.findViewById(R.id.churches_rv);
            rv.setLayoutManager(new LinearLayoutManager(getActivity()));

            return v;
        }

        @Override
        public void onAttach(Activity activity){
            super.onAttach(activity);
            try{
                this.activity = (Churches)activity;
            }catch (ClassCastException e){

            }
        }

    }
    public static class ChurchFragment extends Fragment {
        public ChurchFragment(){

        }
        Churches activity;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
            View v = inflater.inflate(R.layout.church_layout, parent, false);
            final Utilities utilities = new Utilities(getActivity());
            //#FFD875
            final MActionBar actionBar = new MActionBar(getActivity(), v, R.id.church_ab);
            actionBar.setTitle("Churches");
            actionBar.setBackground(R.drawable.churches_ab_bg);
            actionBar.setLeftDrawable(R.drawable.ic_action_back);
            actionBar.setRightDrawable(R.drawable.none);
            actionBar.setLeftOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   activity.toggle();
                }
            });
            actionBar.setTitleColor(Color.WHITE);
            actionBar.setTitleSize(20f);

            final EditText name = (EditText) v.findViewById(R.id.church_name);
            final EditText addr1 = (EditText)v.findViewById(R.id.church_address1);
            final EditText addr2 = (EditText)v.findViewById(R.id.church_address2);
            final EditText city = (EditText)v.findViewById(R.id.church_city);
            final EditText state = (EditText)v.findViewById(R.id.church_state);
            final EditText country = (EditText)v.findViewById(R.id.church_country);
            final EditText postalcode = (EditText)v.findViewById(R.id.church_postalcode);
            final EditText phone = (EditText)v.findViewById(R.id.church_phone);
            final EditText email = (EditText)v.findViewById(R.id.church_email);
            final EditText website = (EditText)v.findViewById(R.id.church_website);
            final EditText pname = (EditText)v.findViewById(R.id.pastor_name);
            final EditText pphone = (EditText)v.findViewById(R.id.pastor_phone);
            final EditText pemail = (EditText)v.findViewById(R.id.pastor_email);
            final Button done = (Button)v.findViewById(R.id.church_done);
            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //createa  church object.
                    Values.Churches church = new Values.Churches();

                    //add the values to it.
                    church.ChurchName = utilities.getStringFromView(name);
                    church.Address1 = utilities.getStringFromView(addr1);
                    church.Address2 = utilities.getStringFromView(addr2);
                    church.City = utilities.getStringFromView(city);
                    church.State = utilities.getStringFromView(state);
                    church.Country = utilities.getStringFromView(country);
                    church.PostalCode = utilities.getStringFromView(postalcode);
                    church.ChurchPhone = utilities.getStringFromView(phone);
                    church.ChurchEmail = utilities.getStringFromView(email);
                    church.ChurchWebsite = utilities.getStringFromView(website);
                    String PastorName = utilities.getStringFromView(pname);
                    church.PastorPhone = utilities.getStringFromView(pphone);
                    church.PastorEmail = utilities.getStringFromView(pemail);
                    church.Status = Values.STATUS_PENDING;
                    church.AdminID = "";

                    //check all the objects.
                    if(!utilities.checkObjects(church.ChurchName, church.Address1, church.Address2, church.City, church.State, church.Country, church.PostalCode, church.ChurchEmail, church.ChurchWebsite, church.ChurchPhone, PastorName, church.PastorPhone, church.PastorEmail))
                        return;

                    //split the pastor's name.
                    String[] PNames = PastorName.split("\\s");

                    //make sure it's got both the first and last name.
                    if(PNames.length != 2)
                        return;
                    //add the first and last name of the pastor to our church object.
                    church.PastorFName = PNames[0];
                    church.PastorLName = PNames[1];

                    //insert the church into the database!
                    Futures.addCallback(activity.values.addChurch(church), new FutureCallback<Values.Churches>() {
                        @Override
                        public void onSuccess(Values.Churches result) {
                            Toast.makeText(getActivity(), "Your church application will be reviewed shortly.", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onFailure(Throwable t) {
                            Toast.makeText(getActivity(), "Oops! Looks like we've encountered an error. Please try again later.", Toast.LENGTH_SHORT).show();
                            Log.e("ChurchFragment", t.getLocalizedMessage());
                        }
                    });
                }
            });

            return v;
        }

        @Override
        public void onAttach(Activity activity){
            super.onAttach(activity);
            try{
                this.activity = (Churches)activity;
            }catch (ClassCastException e){

            }
        }


    }
    public void toggle(){
        if(getSupportFragmentManager().getFragments().get(0) instanceof ChurchesFragment)
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new ChurchFragment())
                .commit();
        else
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new ChurchesFragment())
                .commit();
    }



}
