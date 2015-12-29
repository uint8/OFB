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

                Futures.addCallback(values.getUser(user.id), new FutureCallback<Values.Users>() {
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
