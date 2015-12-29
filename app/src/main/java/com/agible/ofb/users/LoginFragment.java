package com.agible.ofb.users;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.agible.ofb.R;
import com.agible.ofb.utils.MActionBar;
import com.microsoft.windowsazure.mobileservices.authentication.MobileServiceAuthenticationProvider;

/**
 * A placeholder fragment containing a simple view.
 */
public class LoginFragment extends Fragment {

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