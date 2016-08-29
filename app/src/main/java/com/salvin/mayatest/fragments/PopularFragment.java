package com.salvin.mayatest.fragments;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pnikosis.materialishprogress.ProgressWheel;
import com.salvin.mayatest.R;
import com.salvin.mayatest.constants.Constants;
import com.salvin.mayatest.feed.Content;
import com.salvin.mayatest.feed.Feed;
import com.salvin.mayatest.feed.FeedService;


import java.util.ArrayList;


public class PopularFragment extends Fragment {

    Bitmap NO_IMAGE;
    Feed feed, refreshFeed;
    AsyncTask asyncTask;

    private SwipeRefreshLayout swipeRefreshLayout;
    ProgressWheel progressWheel;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    int curSize;

    ArrayList<Content> allfeed;


    public static PopularFragment newInstance() {
        return new PopularFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_popular, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        feed = new Feed();

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        progressWheel = (ProgressWheel) view.findViewById(R.id.progressBar1);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);


        mRecyclerView.setHasFixedSize(true);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mAdapter = new RecyclerViewAdapterPotrait(feed.getContents(), getActivity());
        mRecyclerView.setAdapter(mAdapter);


        asyncTask = new JSONParseFeed().execute();

        mRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list

                if(mAdapter.getItemCount()<13) {
                    System.out.println("Loading more data");
                    asyncTask = new JSONMoreFeed().execute();
                    mAdapter.notifyDataSetChanged();
                }


            }
        });


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                asyncTask = new JSONRefreshFeed().execute();
               // swipeRefreshLayout.setRefreshing(false);
            }
        });


    }



    private class JSONParseFeed extends AsyncTask<String, String, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressWheel.setVisibility(View.VISIBLE);
        }

        public Void doInBackground(String... args) {

            feed.setContents(new FeedService().getContents(Constants.FEED_URL));
            return null;
        }

        protected void onPostExecute(Void result) {


            if (feed.getContentsSize() == 0) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Sorry, can't access to server !")
                        .setMessage("Please, check your connection.")
                        .setPositiveButton("Close the App", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                getActivity().moveTaskToBack(true);
                                getActivity().finish();
                            }
                        })
                        .setNegativeButton("Try Again", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                asyncTask = new JSONParseFeed().execute();
                            }
                        })
                        .show();

            }


            swipeRefreshLayout.setRefreshing(false);
            progressWheel.setVisibility(View.GONE);
            mAdapter.notifyDataSetChanged();


        }

    }

    private class JSONMoreFeed extends AsyncTask<String, String, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressWheel.setVisibility(View.VISIBLE);
        }

        public Void doInBackground(String... args) {
  ;

            curSize = mAdapter.getItemCount();
            allfeed = new FeedService().getContents(Constants.LOAD_MORE_FEED_URL);




            return null;
        }

        protected void onPostExecute(Void result) {
            progressWheel.setVisibility(View.GONE);
            feed.setContents(allfeed);

            allfeed.clear();
            allfeed.addAll(feed.getContents());

            System.out.println("all Feed  "   + allfeed.get(2).getName());
            System.out.println(String.valueOf(allfeed.size()) + "  Current Adapter " + String.valueOf(curSize));
          mAdapter.notifyItemRangeInserted(curSize, allfeed.size() - 1);
            mAdapter.notifyDataSetChanged();

        }



    }

    private class JSONRefreshFeed extends AsyncTask<String, String, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        public Void doInBackground(String... args) {
            refreshFeed = new Feed();
            refreshFeed.setContents(new FeedService().getContents(Constants.REFRESH_FEED_URL));

            return null;
        }

        protected void onPostExecute(Void result) {

            mRecyclerView.removeAllViewsInLayout();
            mAdapter = new RecyclerViewAdapterPotrait(refreshFeed.getContents(), getActivity());
            mRecyclerView.setAdapter(mAdapter);
            swipeRefreshLayout.setRefreshing(false);
            mAdapter.notifyDataSetChanged();


        }

    }

}
