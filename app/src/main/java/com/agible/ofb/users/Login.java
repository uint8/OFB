package com.agible.ofb.users;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.agible.ofb.utils.MActionBar;
import com.agible.ofb.R;
import com.agible.ofb.utils.Utilities;
import com.agible.ofb.data.Values;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.authentication.MobileServiceAuthenticationProvider;
import com.microsoft.windowsazure.mobileservices.authentication.MobileServiceUser;

import java.net.MalformedURLException;
import java.util.List;
import java.util.Objects;

public class Login extends ActionBarActivity {

    public MobileServiceClient mClient;
    public Values values;
    public final int MODE_UPDATE = 1;
    public final int MODE_CREATE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Intent i = getIntent();
        int mode = i.getIntExtra("mode", 0);

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

        if (savedInstanceState == null) {
            Fragment fragment;
            Bundle bundle = new Bundle();
            bundle.putInt("mode", mode);

            if(mode == 1)
                fragment = AccountFragment.newInstance(bundle);
            else
                fragment = new LoginFragment();


            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();

        }



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
    public static class LoginFragment extends Fragment {

        Login activity;

        public LoginFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.login_layout, container, false);

            MActionBar actionBar = new MActionBar(getActivity(), v, R.id.login_ab);
            actionBar.setTitle("Login");
            actionBar.setBackground(R.drawable.profile_ab_bg);
            actionBar.setRightDrawable(R.drawable.none);
            actionBar.setLeftDrawable(R.drawable.ic_action_back);
            actionBar.setLeftOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                }
            });
            actionBar.setTitleColor(Color.WHITE);
            actionBar.setTitleSize(20f);

            RelativeLayout facebook = (RelativeLayout) v.findViewById(R.id.login_facebook);
            RelativeLayout google = (RelativeLayout) v.findViewById(R.id.login_google);
            RelativeLayout twitter = (RelativeLayout) v.findViewById(R.id.login_twitter);
            RelativeLayout microsoft = (RelativeLayout) v.findViewById(R.id.login_microsoft);

            //setup on click listeners.
            facebook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.auth(MobileServiceAuthenticationProvider.Facebook);
                }
            });
            google.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.auth(MobileServiceAuthenticationProvider.Google);
                }
            });
            twitter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.auth(MobileServiceAuthenticationProvider.Twitter);
                }
            });
            microsoft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.auth(MobileServiceAuthenticationProvider.MicrosoftAccount);
                }
            });

            return v;
        }

        @Override
        public void onAttach(Activity activity){
            super.onAttach(activity);

            this.activity = (Login) activity;

        }
    }

    public static class AccountFragment extends Fragment {
        public final int MODE_UPDATE = 1;
        public final int MODE_CREATE = 2;
        private Login activity;

        public int mode;
        public AccountFragment(){

        }
        public static AccountFragment newInstance(Bundle data){
            AccountFragment fragment = new AccountFragment();
            fragment.setArguments(data);
            return fragment;
        }

        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
             View v = inflater.inflate(R.layout.account_layout, container, false);
            final Utilities utilities = new Utilities(getActivity());
            MActionBar actionBar = new MActionBar(getActivity(), v, R.id.account_ab);
            actionBar.setTitle("Account Setup");
            actionBar.setBackground(R.drawable.profile_ab_bg);
            actionBar.setRightDrawable(R.drawable.none);
            actionBar.setLeftDrawable(R.drawable.ic_action_back);
            actionBar.setLeftOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                }
            });
            actionBar.setTitleColor(Color.WHITE);
            actionBar.setTitleSize(20f);
            final Bundle args = getArguments();
            mode = args.getInt("mode");


            final EditText firstName = (EditText)v.findViewById(R.id.account_fname);
            final EditText lastName = (EditText)v.findViewById(R.id.account_lname);
            final EditText email = (EditText)v.findViewById(R.id.account_email);
            final EditText address1 = (EditText)v.findViewById(R.id.account_address1);
            final EditText address2 = (EditText)v.findViewById(R.id.account_address2);
            final EditText city = (EditText)v.findViewById(R.id.account_city);
            final EditText state =(EditText)v.findViewById(R.id.account_state);
            final EditText country = (EditText)v.findViewById(R.id.account_country);
            final EditText postalCode = (EditText)v.findViewById(R.id.account_postal);
            final EditText gender = (EditText)v.findViewById(R.id.account_gender);
            final EditText bio = (EditText)v.findViewById(R.id.account_bio);
            final EditText skills = (EditText)v.findViewById(R.id.account_skills);
            final EditText birthDate = (EditText)v.findViewById(R.id.account_birthdate);
            final EditText phone = (EditText)v.findViewById(R.id.account_phone);
            Button done = (Button)v.findViewById(R.id.account_done);
            //final Values values = new Values(getActivity());

            if(mode == MODE_UPDATE){
                try {
                    List<Object> objects = activity.values.loadObjects(Values.Users.class);
                    Values.Users user = (Values.Users)objects.get(0);
                    firstName.setText(user.FirstName);
                    lastName.setText(user.LastName);
                    phone.setText(user.Phone);
                    email.setText(user.Email);
                    skills.setText(user.Skills);
                    bio.setText(user.Bio);
                    String gen = "Female";
                    if (user.Gender)
                        gen = "Male";
                    gender.setText(gen);

                    address1.setText(user.Address1);
                    address2.setText(user.Address2);
                    city.setText(user.City);
                    state.setText(user.State);
                    country.setText(user.Country);
                    postalCode.setText(user.PostalCode);
                    //birthDate.setText("");

                }catch (Exception e){

                }
            }

            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Values.Users user = new Values.Users();

                    //populate each user item.
                    user.FirstName = utilities.getStringFromView(firstName);
                    user.LastName = utilities.getStringFromView(lastName);
                    user.Email = utilities.getStringFromView(email);
                    user.Address1 = utilities.getStringFromView(address1);
                    user.Address2 = utilities.getStringFromView(address2);
                    user.City = utilities.getStringFromView(city);
                    user.State = utilities.getStringFromView(state);
                    user.Country = utilities.getStringFromView(country);
                    user.PostalCode = utilities.getStringFromView(postalCode);
                    String Gender = utilities.getStringFromView(gender);
                    user.Phone = utilities.getStringFromView(phone);
                    String BirthDate = utilities.getStringFromView(birthDate);
                    user.Skills = utilities.getStringFromView(skills);
                    user.Bio = utilities.getStringFromView(bio);
                    user.ChurchID = "";
                    user.PictureName = "";
                    user.Status = 0;

                    //make sure all objects are not null.
                    if(!utilities.checkObjects(user.FirstName, user.LastName, user.Email, user.Address1, user.Address2, user.City, user.State, user.Country, user.PostalCode, Gender, user.Phone, BirthDate, user.Skills, user.Bio, user.ChurchID, user.PictureName, user.Status))
                        return;

                    //populate our user object.
                    user.BirthDate = new Utilities().getBirthDate(BirthDate);
                    user.id = activity.values.getUserId();

                    if(Gender.toUpperCase().charAt(0) == 'M')
                        user.Gender = Values.MALE;
                    else
                        user.Gender = Values.FEMALE;

                    //check to make sure that the id and id length is valid.
                    if(user.id == null || user.id.length() < 5)
                        return;

                    //run our insert statement.
                    Futures.addCallback(activity.values.addUsers(user), new FutureCallback<Values.Users>() {
                        @Override
                        public void onSuccess(Values.Users result) {


                            //alert the user's creation.
                            Toast.makeText(getActivity(), "User created!", Toast.LENGTH_SHORT).show();
                            getActivity().finish();

                        }

                        @Override
                        public void onFailure(Throwable t) {
                            Toast.makeText(getActivity(), "Oops! Looks like we've encountered an error. Please try again later.", Toast.LENGTH_SHORT).show();
                            Log.e("AccountFragment", t.getLocalizedMessage());
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
                this.activity = (Login)activity;
            }catch (ClassCastException e){
                Log.e("Login", e.getMessage());
            }

        }

        public ListenableFuture<Values.Users> getUserCallback(Values.Users user){
            if(mode != MODE_UPDATE)
                return activity.values.addUsers(user);
            else
                return activity.values.updateUsers(user);
        }

    }

    public int auth(MobileServiceAuthenticationProvider authProvider){

        ListenableFuture<MobileServiceUser> mLogin = mClient.login(authProvider);

        Futures.addCallback(mLogin, new FutureCallback<MobileServiceUser>() {
            @Override
            public void onSuccess(MobileServiceUser result) {

                System.out.println(result.getAuthenticationToken() + " User ID = " + result.getUserId());


                values.setUserId(result.getUserId());
                values.setAuthToken(result.getAuthenticationToken());

                final Values.Users user = new Values.Users();
                user.id = result.getUserId();
                System.out.println(result.getUserId());
                System.out.println(result.getAuthenticationToken());

                values.login(result.getUserId(), result.getAuthenticationToken());

                Futures.addCallback(values.getUsers(user.id), new FutureCallback<Values.Users>() {
                    @Override
                    public void onSuccess(Values.Users result) {
                        if (result.FirstName != null && result.FirstName.length() > 0)
                            finish();
                        else
                            replaceFragment(MODE_CREATE);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        replaceFragment(MODE_CREATE);
                        //finish();
                    }
                });




            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getApplicationContext(), "Sorry, please try again later!", Toast.LENGTH_SHORT).show();
                values.setUserId("undefined");
                values.setAuthToken("undefined");
            }
        });

        return 0;
    }

    public void replaceFragment(int mode){
        AccountFragment fragment = new AccountFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("mode", mode);
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }
}
