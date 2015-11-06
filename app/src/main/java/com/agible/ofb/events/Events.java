package com.agible.ofb.events;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.agible.ofb.listeners.EventsListener;
import com.agible.ofb.listeners.OnFinishListener;
import com.agible.ofb.users.ProfileFragment;
import com.agible.ofb.R;
import com.agible.ofb.data.Values;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.microsoft.windowsazure.mobileservices.*;
import com.microsoft.windowsazure.mobileservices.authentication.MobileServiceUser;
import com.microsoft.windowsazure.mobileservices.table.query.ExecutableQuery;
import com.microsoft.windowsazure.mobileservices.table.query.Query;

import com.agible.ofb.data.Values.*;

import java.net.MalformedURLException;
import java.util.Locale;


public class Events extends AppCompatActivity implements EventsListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;
    public Values values;
    public Users user;

    //Project Number: 205416429007


    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_events);



        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(1);

        try {

            MobileServiceClient mClient = new MobileServiceClient(
                    Values.MOBILE_SERVICE_URL,
                    Values.APPLICATION_KEY,
                    this

            );
            values = new Values(getApplicationContext(), mClient);
            MobileServiceUser user = new MobileServiceUser(values.getUserId());
            user.setAuthenticationToken(values.getAuthToken());
            values.login(user.getUserId(), user.getAuthenticationToken());
            if(user.getUserId() != null && user.getUserId().length() > 5)
                mClient.setCurrentUser(user);



        } catch (MalformedURLException e){
            Log.e("Events", e.getMessage());
        }


    }



    @Override
    public void onPageChange(int i) {

        if(mViewPager == null)
            return;

        mViewPager.setCurrentItem(i);
    }



    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return newInstance((byte)position);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return "Profile";
                case 1:
                    return "Events";
                case 2:
                    return "Add Event";
            }
            return null;
        }

        public Fragment newInstance(byte position){
            switch(position){
                case 0:
                    return new ProfileFragment();
                case 1:
                    return new EventsFragment();
                case 2:
                    return new EventFragment();
                default:
                    return new EventsFragment();
            }
        }
    }

    public class Item {
        public String Id;
        public String Text;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent i){
        super.onActivityResult(requestCode, resultCode, i);

        switch(resultCode){
            case 200:
                MobileServiceUser user = new MobileServiceUser(values.getUserId());
                user.setAuthenticationToken(values.getAuthToken());
                values.mClient.setCurrentUser(user);
                mViewPager.getAdapter().notifyDataSetChanged();
                Log.i("Events", "data set changed.");

                break;
            case 401:
                Log.e("Events", "Login error");
                break;
            case 202:
                Log.i("Events", "Update Successful!");
                mViewPager.getAdapter().notifyDataSetChanged();

                if(values.mClient.getCurrentUser() != null)
                values.login(values.mClient.getCurrentUser().getUserId(), values.mClient.getCurrentUser().getAuthenticationToken());

                break;
            default:

        }

    }






}
