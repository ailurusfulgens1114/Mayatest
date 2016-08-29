package com.salvin.mayatest;

import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.salvin.mayatest.fragments.MyMayaFragment;
import com.salvin.mayatest.fragments.TabFragment;
import com.salvin.mayatest.managers.SharedPreferenceManager;
import com.salvin.mayatest.model.UserModel;

public class MainActivity extends AppCompatActivity {
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    ImageView ivHeaderPhoto;
    ImageLoader imageLoader;

    String user_name = "", user_email = "", user_profile_url = "", user_gender = "";
    boolean lnBangla = false;

    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;

    ActionBarDrawerToggle mDrawerToggle;
    android.support.v7.widget.Toolbar toolbar;
    Menu menu;
    MenuItem nav_item_feed, nav_item_mymaya, nav_item_logout ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView = (NavigationView) findViewById(R.id.navdrawer) ;
        imageLoader = ImageLoader.getInstance();


        sharedPreferences = getSharedPreferences("localinfo", MODE_PRIVATE);
        lnBangla = sharedPreferences.getBoolean("lnBangla", false);

        UserModel userModel = getUserModelFromIntent();
        if(userModel!=null){
            View headerView = mNavigationView.getHeaderView(0);

            ivHeaderPhoto = (ImageView) headerView.findViewById(R.id.nav_image);
            System.out.println(userModel.profilePic);
            imageLoader.displayImage(userModel.profilePic, ivHeaderPhoto);
           // ivHeaderPhoto.setImageURI(Uri.parse(userModel.profilePic));

            user_name =  userModel.userName;
            user_email = userModel.userEmail;
            user_profile_url = userModel.profilePic;
            user_gender = userModel.gender;

        }



        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.containerView,new TabFragment()).commit();
        /**
         * Setup click events on the Navigation View Items.
         */



        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                mDrawerLayout.closeDrawers();



                if (menuItem.getItemId() == R.id.nav_item_mymaya) {
                    Bundle bundle = new Bundle();
                    bundle.putString("userName", user_name);
                    bundle.putString("userEmail", user_email);
                    bundle.putString("userProfilePicture", user_profile_url);
                    bundle.putString("userGender", user_gender);
                    FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                    MyMayaFragment myMayaFragment = new MyMayaFragment();
                    myMayaFragment.setArguments(bundle);
                    fragmentTransaction.replace(R.id.containerView,myMayaFragment).commit();

                }

                if (menuItem.getItemId() == R.id.nav_item_feed) {
                    FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
                    xfragmentTransaction.replace(R.id.containerView,new TabFragment()).commit();
                }

                if(menuItem.getItemId() == R.id.nav_item_logout){
                    SharedPreferenceManager.getSharedInstance().clearAllPreferences();

                    startLoginActivity();
                }

                return false;
            }

        });

        /**
         * Setup Drawer Toggle of the Toolbar
         */

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout, toolbar,R.string.app_name,
                R.string.app_name);

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerToggle.syncState();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Menu menu = mNavigationView.getMenu();
        nav_item_feed = menu.findItem(R.id.nav_item_feed);
        nav_item_mymaya = menu.findItem(R.id.nav_item_mymaya);
        nav_item_logout = menu.findItem(R.id.nav_item_logout);

        updateUI(lnBangla);

    }

    public void updateUI(boolean lnBangla){

        if(lnBangla){
             toolbar.setTitle(R.string.app_name_bangla);
             nav_item_feed.setTitle(R.string.nav_item_feed_bangla);
             nav_item_mymaya.setTitle(R.string.nav_item_mymaya_bangla);
             nav_item_logout.setTitle(R.string.nav_item_logout_bangla);
        }else{
            toolbar.setTitle(R.string.app_name_english);
            nav_item_feed.setTitle(R.string.nav_item_feed);
            nav_item_mymaya.setTitle(R.string.nav_item_mymaya);
            nav_item_logout.setTitle(R.string.nav_item_logout);
        }
    }

    public void onSignOut(View view) {
        finish();
    }

    private UserModel getUserModelFromIntent()
    {
        Intent intent = getIntent();
        return intent.getParcelableExtra(UserModel.class.getSimpleName());
    }

    private void startLoginActivity()
    {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finishAffinity();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

       return  super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_language) {
            lnBangla = !lnBangla;
            editor = getSharedPreferences("localinfo", MODE_PRIVATE).edit();
            editor.putBoolean("lnBangla", lnBangla);
            editor.commit();
            updateUI(lnBangla);
            return true;
        }
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);

        }


}

