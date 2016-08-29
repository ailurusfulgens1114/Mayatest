package com.salvin.mayatest.helpers;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;


import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.models.User;

/**
 * FbConnectHelper.java
 */
public class TwitterConnectHelper {
    private Activity activity;
    private Fragment fragment;
    private OnTwitterSignInListener twitterSignInListener;
    private TwitterAuthClient twitterAuthClient;
    /**
     * Interface to listen the Facebook connect
     */
    public interface OnTwitterSignInListener {
        void onTwitterSuccess(User twitterSessionResult);

        void onTwitterError(String errorMessage);
    }

    public TwitterConnectHelper(Activity activity, OnTwitterSignInListener twitterSignInListener) {
        this.activity = activity;
        twitterAuthClient = new TwitterAuthClient();
        this.twitterSignInListener = twitterSignInListener;
    }

    public TwitterConnectHelper(Fragment fragment, OnTwitterSignInListener twitterSignInListener) {
        this.fragment = fragment;
        twitterAuthClient = new TwitterAuthClient();
        this.twitterSignInListener = twitterSignInListener;
    }

    public void connect() {
        twitterAuthClient.authorize(activity, new com.twitter.sdk.android.core.Callback<TwitterSession>() {

            @Override
            public void success(final Result<TwitterSession> twitterSessionResult) {
                Twitter.getApiClient(twitterSessionResult.data).getAccountService()
                        .verifyCredentials(true, false, new Callback<User>() {
                            @Override
                            public void success(Result<User> userResult) {

                                final User user = userResult.data;
                                final String email = null;
                                System.out.println(user.profileImageUrl);
                                System.out.println(user.screenName);
                                System.out.println(user.name);
                                System.out.println(user.description);
                                twitterSignInListener.onTwitterSuccess(user);
//                                twitterAuthClient.requestEmail(twitterSessionResult.data, new Callback<String>() {
//                                    @Override
//                                    public void success(Result<String> result) {
//                                        // Do something with the result, which provides the email address
//                                        twitterSignInListener.onTwitterSuccess(user, result.data);
//                                    }
//
//                                    @Override
//                                    public void failure(TwitterException exception) {
//                                        // Do something on failure
//                                        Log.e(TwitterConnectHelper.class.getSimpleName(), exception.toString());
//                                        twitterSignInListener.onTwitterError(exception.toString());
//                                    }
//                                });
                            }

                            @Override
                            public void failure(TwitterException e) {
                                twitterSignInListener.onTwitterError(e.toString());
                            }

                        });
            }

            @Override
            public void failure(TwitterException e) {
                twitterSignInListener.onTwitterError(e.toString());
            }
        });
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (twitterAuthClient != null)
            twitterAuthClient.onActivityResult(requestCode, resultCode, data);
    }
}
