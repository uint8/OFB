package com.agible.ofb.users;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.agible.ofb.R;
import com.agible.ofb.data.Values;
import com.agible.ofb.utils.MActionBar;
import com.agible.ofb.utils.Utilities;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;

public class AccountFragment extends Fragment {
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

        //find and cast all of our views.
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

        //two different modes. we can sepecify this mode when we launch the activity.. one mode is for updating and the other is for adding.
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
                Log.e("AccountFragment", e.getMessage());
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
                user.Status = Values.STATUS_PENDING;

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


                //call api.
                if(mode == MODE_CREATE)
                Futures.addCallback(activity.values.addUser(user), new FutureCallback<Values.Users>() {
                    @Override
                    public void onSuccess(final Values.Users result) {

                        //alert the user's creation.
                        Toast.makeText(getActivity(), "User created!", Toast.LENGTH_SHORT).show();

                        //if the verification code isn't valid, close the activity.
                        if(result.VerificationCode == null || result.VerificationCode.equals("")) {
                            getActivity().finish();
                            return;
                        }


                        //otherwise we create a popup requesting the user enter his or her verification code.
                        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());


                        final EditText edittext = new EditText(getActivity());
                        alert.setMessage("Check your spam folder if you don't receive a email.");
                        alert.setTitle("Email Verification");

                        alert.setView(edittext);

                        alert.setPositiveButton("Verify", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //What ever you want to do with the value
                                //Editable YouEditTextValue = edittext.getText();
                                //OR
                                String code = edittext.getText().toString();
                                if (code.equals(result.VerificationCode)) {
                                    Toast.makeText(getActivity(), "Congratulations! Your account is now verified. Once it's been reviewed accepted, you can use the app.", Toast.LENGTH_LONG).show();
                                    getActivity().finish();
                                } else {
                                    Toast.makeText(getActivity(), "Oops! We were unable to verify your account with the provided code. Please ensure that it was entered correctly and try again!", Toast.LENGTH_LONG).show();
                                }

                            }
                        });

                        final AlertDialog dialog = alert.show();
                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                //if we want to close the dialog we run dialog.dismiss();
                            }
                        });
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Toast.makeText(getActivity(), "Oops! Looks like we've encountered an error. Please try again later.", Toast.LENGTH_SHORT).show();
                        Log.e("AccountFragment", t.getLocalizedMessage());
                    }
                });
                else
                    Futures.addCallback(activity.values.updateUser(user), new FutureCallback<Values.Users>() {
                        @Override
                        public void onSuccess(Values.Users result) {
                            Toast.makeText(getActivity(), "Account updated successfully!", Toast.LENGTH_SHORT).show();
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
            return activity.values.addUser(user);
        else
            return activity.values.updateUser(user);
    }

}

