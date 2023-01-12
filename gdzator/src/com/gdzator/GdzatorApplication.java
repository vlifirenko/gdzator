package com.gdzator;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.HashMap;


public class GdzatorApplication extends Application {

    public static final boolean TESTING = true;
    public static final String BUGSENSE_KEY = "63d85bce";
    public static final String BILLING_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAp4vBkl60XLwpCOwH1plmOabk4FWjKDx+ygsHYubbJNT4b9BXqf/4te07sIzqV6ps0hzPrR5bGN4GS0UqKQZkWcuuj+x3LOY83Ok06K1a+UnH0BbrxgIc8YVqhQOyEy0qRQHq8dEp8kCQe/D6jQizLxXtiBwVdFvYnggaWh7ogwDKSmOGp2me2Lvpr6us+lfUWEpcLtcI8MwjVxKIjSdNBkZuIhjabjfSBSG51Qb3+4jNygJZ0G2BE+ZXYz4JQSJvsnm1R7q3oUsao+R4JVHWPItH9sRxexdfk99e+An+FsxIov5K0Z7DXYWwlnXXRyjZ0gkeYS9XqGeIlpKKjNmuCwIDAQAB";
    public static final String FEEDBACK_EMAIL = "Egor.Ignatiev@gmail.com";
    public static final String BANNER_UNIT_ID1 = "ca-app-pub-7581550092624542/5795596336";
    public static final String BANNER_UNIT_ID2 = "ca-app-pub-7581550092624542/2842129931";
    public static final String KEY_ADS = "com.gdzator.ads";

    @Override
    public void onCreate() {
        super.onCreate();
        Tracker t = GoogleAnalytics.getInstance(this).newTracker("UA-60034297-1");
        t.setScreenName("AnalyticsSampleApp ScreenView");
        t.send(new HitBuilders.AppViewBuilder().build());
    }
}
