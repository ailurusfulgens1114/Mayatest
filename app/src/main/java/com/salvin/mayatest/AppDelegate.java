package com.salvin.mayatest;

import android.app.Activity;
import android.app.Application;
import android.graphics.Bitmap;
import android.os.Bundle;


import com.facebook.FacebookSdk;
import com.facebook.accountkit.AccountKit;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.salvin.mayatest.managers.SharedPreferenceManager;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;


public class AppDelegate extends Application implements Application.ActivityLifecycleCallbacks {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "b9V86ifozhOGXkwqoFhQpaEhq";
    private static final String TWITTER_SECRET = "jVX8aMMHIbEG9vl52t9suUiVaE047A8gJczYyQKs1i4lFRczYc";



    @Override
    public void onCreate() {
        super.onCreate();
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.EXACTLY).build();
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder( getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions);


        config.threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .threadPoolSize(3)
                .diskCacheExtraOptions(480, 320, null)
                .diskCacheSize(20 * 1024 * 1024)
                .tasksProcessingOrder(QueueProcessingType.LIFO);


        ImageLoader.getInstance().init(config.build());

        instantiateManagers();
    }

    /**
     * Method to instantiate all the managers in this app
     */
    private void instantiateManagers() {

        FacebookSdk.sdkInitialize(this);
        Fresco.initialize(this);
        SharedPreferenceManager.getSharedInstance().initiateSharedPreferences(getApplicationContext());
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        AccountKit.initialize(this);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {
    }

    @Override
    public void onActivityResumed(Activity activity) {
    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }


}

