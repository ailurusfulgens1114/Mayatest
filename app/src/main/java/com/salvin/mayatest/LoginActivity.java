package com.salvin.mayatest;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;

import com.salvin.mayatest.helpers.InternetConnectionDetector;
import com.salvin.mayatest.managers.SharedPreferenceManager;
import com.salvin.mayatest.model.UserModel;



public class LoginActivity extends FragmentActivity {

    Button mobileLogin, socialLogin;
    private InternetConnectionDetector internetconnectiondetector;

    public UserModel userModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mobileLogin = (Button) findViewById(R.id.mobileLogin);
        socialLogin = (Button) findViewById(R.id.socialLogin);

        internetconnectiondetector = new InternetConnectionDetector(getApplicationContext());

        if(!internetconnectiondetector.isConnectedToInternet()){
            new AlertDialog.Builder(this)
                    .setTitle("Sorry, No Internet Connection")
                    .setMessage("Please, check your connection.")
                    .setPositiveButton("Close the App", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                           finish();
                        }
                    })
                    .show();
        }

        userModel = SharedPreferenceManager.getSharedInstance().getUserModelFromPreferences();
        if (userModel != null) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(UserModel.class.getSimpleName(), userModel);
            startActivity(intent);
            finishAffinity();
        } else {

            if (findViewById(R.id.content_frame) != null) {

                if (savedInstanceState != null) {
                    return;
                }

                mobileLogin.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        FBAccountKitFragment fbAccountKitFragment = new FBAccountKitFragment();
                        fbAccountKitFragment.setArguments(getIntent().getExtras());
                        getSupportFragmentManager().beginTransaction().add(R.id.content_frame, fbAccountKitFragment).commit();
                    }
                });

                socialLogin.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        LoginFragment loginFragment = new LoginFragment();
                        loginFragment.setArguments(getIntent().getExtras());
                        getSupportFragmentManager().beginTransaction().add(R.id.content_frame, loginFragment).commit();
                    }
                });


            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            LoginFragment loginFragment = (LoginFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);
            if (loginFragment != null) {
                loginFragment.onActivityResult(requestCode, resultCode, data);
            }

        }




}
