package com.salvin.mayatest.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.salvin.mayatest.R;


/**
 * Created by salvin on 8/26/16.
 */
public class TabFragment extends Fragment {

    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    public static int int_items = 2 ;

    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;

    boolean lnBangla;
    String[] titles = {"Popular", "bangla"};
    MyAdapter myAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /**
         *Inflate tab_layout and setup Views.
         */
        View x =  inflater.inflate(R.layout.tab_layout,null);
        tabLayout = (TabLayout) x.findViewById(R.id.tabs);
        viewPager = (ViewPager) x.findViewById(R.id.viewpager);

        sharedPreferences = getActivity().getSharedPreferences("localinfo", getActivity().MODE_PRIVATE);
        lnBangla = sharedPreferences.getBoolean("lnBangla", false);


        myAdapter = new MyAdapter(getChildFragmentManager());

        updateUI(lnBangla);

        viewPager.setAdapter(myAdapter);

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });

        return x;

    }

    public void updateUI(boolean lnBangla){
        System.out.println("change title");

        if (lnBangla) {
            titles[0] = "পপুলার";
            titles[1] = "উত্তর";
        }
        else {
            titles[0] = "Popular";
            titles[1] = "Answered";
        }
        System.out.println(titles[0]);
        myAdapter.notifyDataSetChanged();
    }

    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position)
        {
            switch (position){
                case 0 : return new PopularFragment();
                case 1 : return new PopularFragment();
            }
            return null;
        }
        @Override
        public int getCount() {return int_items;
        }

        @Override
        public CharSequence getPageTitle(int position) {


            switch (position){
                case 0 :
                    return titles[position];
                case 1 :
                    return titles[position];
            }
            return null;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_language) {
            System.out.println("from fragment menu");
            lnBangla = !lnBangla;
            editor = getActivity().getSharedPreferences("localinfo", getActivity().MODE_PRIVATE).edit();
            editor.putBoolean("lnBangla", lnBangla);
            editor.commit();
            updateUI(lnBangla);;
            return true;
        }


        return false;
    }

}

