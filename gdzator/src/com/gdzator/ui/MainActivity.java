package com.gdzator.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.gdzator.GdzatorApplication;
import com.gdzator.R;
import com.gdzator.content.provider.Contract;
import com.gdzator.content.provider.DatabaseHelper;
import com.gdzator.helpers.AdsHelper;
import com.gdzator.ui.fragments.ClassesFragment;
import com.gdzator.ui.fragments.ClassesFragment_;
import com.gdzator.ui.fragments.FeedbackFragment;
import com.gdzator.ui.fragments.FeedbackFragment_;
import com.gdzator.ui.fragments.InfoFragment;
import com.gdzator.ui.fragments.InfoFragment_;
import com.gdzator.ui.fragments.NavigationDrawerFragment;
import com.gdzator.ui.fragments.SubjectsFragment_;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.splunk.mint.Mint;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.security.MessageDigest;

import hotchemi.android.rate.AppRate;

@EActivity(R.layout.activity_main)
public class MainActivity extends FragmentActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, BillingProcessor.IBillingHandler {
    public final static String LOG_TAG = MainActivity.class.getName();

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private BillingProcessor bp;

    public static final String big_string = "qfgoiib3y934uy8rosgv45v-asa9,vascav325m90-,asc0";
    public static final String KEY_COST = "49";
    public static final String FIELD_OFF_ADS = "off_ads";

    @ViewById
    LinearLayout menu;

    @ViewById
    LinearLayout back;

    @ViewById
    TextView title;

    @ViewById
    ProgressBar progressBar;

    @ViewById
    LinearLayout container;

    @ViewById
    LinearLayout admob;

    /*private String[] tryCodes = new String[3];
    private String tryCode;
    private long tryCodeDbId;*/

    private AdView adView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (GdzatorApplication.TESTING)
            Mint.initAndStartSession(this, GdzatorApplication.BUGSENSE_KEY);
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .delayBeforeLoading(100)
                .resetViewBeforeLoading(true)
                .build();
        int availableMemory = (int) Runtime.getRuntime().maxMemory() / 1048576;
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .defaultDisplayImageOptions(options)
                .diskCacheExtraOptions(480, 320, null)
                .threadPoolSize(1)
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(50 * 1024 * 1024)
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .diskCache(new UnlimitedDiscCache(StorageUtils.getCacheDirectory(this)))
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100)
                .denyCacheImageMultipleSizesInMemory()
                .diskCache(new UnlimitedDiscCache(getCacheDir()))
                .memoryCache(new WeakMemoryCache())
                .memoryCacheSize((availableMemory / 2) * 1024 * 1024)
                .build();
        ImageLoader.getInstance().init(config);
        bp = new BillingProcessor(this, GdzatorApplication.BILLING_PUBLIC_KEY, this);

        AppRate.with(this)
                .setInstallDays(0)
                .setLaunchTimes(3)
                .setRemindInterval(5)
                .monitor();
        AppRate.showRateDialogIfMeetsConditions(this);
    }

    @AfterViews
    void afterViews() {
        if (!AdsHelper.checkOffAds(this)) {
            adView = new AdView(this);
            adView.setAdUnitId(GdzatorApplication.BANNER_UNIT_ID1);
            adView.setAdSize(AdSize.SMART_BANNER);
            admob.addView(adView);
            try {
                AdRequest adRequest = new AdRequest.Builder().build();
                adView.loadAd(adRequest);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        back.setVisibility(View.GONE);

        /*try {
            String android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            byte[] bytesOfMessage = ("1" + android_id + big_string).getBytes("UTF-8");
            tryCodes[0] = new String(MessageDigest.getInstance("MD5").digest(bytesOfMessage));
            bytesOfMessage = ("2" + android_id + big_string).getBytes("UTF-8");
            tryCodes[1] = new String(MessageDigest.getInstance("MD5").digest(bytesOfMessage));
            bytesOfMessage = ("3" + android_id + big_string).getBytes("UTF-8");
            tryCodes[2] = new String(MessageDigest.getInstance("MD5").digest(bytesOfMessage));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            SQLiteDatabase db = DatabaseHelper.getInstance(this).getReadableDatabase();
            Cursor c = db.query(Contract.TryData.TABLE_NAME, null, null, null, null, null, null);
            c.moveToFirst();
            if (c.getCount() == 0) {
                // init tries
                ContentValues values = new ContentValues();
                values.put(Contract.TryData.COLUMN_NAME_TRY_CODE, tryCodes[2]);
                this.tryCodeDbId = db.insert(Contract.TryData.TABLE_NAME, null, values);
                this.tryCode = tryCodes[2];
            } else {
                if (c.getString(c.getColumnIndex(Contract.TryData.COLUMN_NAME_TRY_CODE)).equals("empty")) {
                    this.tryCode = null;
                } else {
                    this.tryCode = c.getString(c.getColumnIndex(Contract.TryData.COLUMN_NAME_TRY_CODE));
                    this.tryCodeDbId = c.getInt(c.getColumnIndex(BaseColumns._ID));
                }
            }
            c.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right,
                        android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .replace(R.id.container, new ClassesFragment_(), ClassesFragment_.LOG_TAG)
                .commitAllowingStateLoss();
    }

    /*public boolean useTryCode() {
        if (this.tryCode == null || this.tryCode.length() == 0)
            return false;
        try {
            SQLiteDatabase db = DatabaseHelper.getInstance(this).getReadableDatabase();
            ContentValues values = new ContentValues();
            if (this.tryCode.equals(tryCodes[2])) { //if 3 tries
                values.put(Contract.TryData.COLUMN_NAME_TRY_CODE, tryCodes[1]);
                db.update(Contract.TryData.TABLE_NAME, values, BaseColumns._ID + " = ?", new String[]{String.valueOf(this.tryCodeDbId)});
                this.tryCode = tryCodes[1];
                return true;
            } else if (this.tryCode.equals(tryCodes[1])) { //if 2 tries
                values.put(Contract.TryData.COLUMN_NAME_TRY_CODE, tryCodes[0]);
                db.update(Contract.TryData.TABLE_NAME, values, BaseColumns._ID + " = ?", new String[]{String.valueOf(this.tryCodeDbId)});
                this.tryCode = tryCodes[0];
                return true;
            } else if (this.tryCode.equals(tryCodes[0])) { //if 1 tries
                values.put(Contract.TryData.COLUMN_NAME_TRY_CODE, "empty");
                db.update(Contract.TryData.TABLE_NAME, values, BaseColumns._ID + " = ?", new String[]{String.valueOf(this.tryCodeDbId)});
                this.tryCode = null;
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }*/

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        if (position == -1)
            return;

        if (position == 0) {
            /*getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new StartFragment_(), StartFragment.LOG_TAG)
                    .addToBackStack(StartFragment.LOG_TAG)
                    .commit();*/
        /*} else if (position == 1) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, new MyLibraryFragment_(), MyLibraryFragment.LOG_TAG)
                    .addToBackStack(MyLibraryFragment.LOG_TAG)
                    .commit();*/
        } else if (position > 0 && position <= 7) {
            SubjectsFragment_ fragment = new SubjectsFragment_();
            Bundle args = new Bundle();
            args.putInt(ClassesFragment.ARG_CLASS, position + 4);
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment, SubjectsFragment_.LOG_TAG)
                    .addToBackStack(SubjectsFragment_.LOG_TAG)
                    .commitAllowingStateLoss();
        /*} else if (position == 10) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new SettingsFragment_(), SettingsFragment.LOG_TAG)
                    .addToBackStack(SettingsFragment.LOG_TAG)
                    .commit();*/
        } else if (position == 8) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new InfoFragment_(), InfoFragment.LOG_TAG)
                    .addToBackStack(InfoFragment.LOG_TAG)
                    .commitAllowingStateLoss();
        } else if (position == 9) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new FeedbackFragment_(), FeedbackFragment.LOG_TAG)
                    .addToBackStack(FeedbackFragment.LOG_TAG)
                    .commitAllowingStateLoss();
        } else if (position == 10) {
            purchase(GdzatorApplication.KEY_ADS);
        }
    }

    @Click
    void menu() {
        mNavigationDrawerFragment.openDrawer();
    }

    @Click
    void back() {
        try {
            getSupportFragmentManager().popBackStack();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setHeaderTitle(String title) {
        if (this.title == null || title == null)
            return;
        this.title.setText(title.toUpperCase());
    }

    public void showBackButton() {
        menu.setVisibility(View.GONE);
        back.setVisibility(View.VISIBLE);
    }

    public void showMenuButton() {
        menu.setVisibility(View.VISIBLE);
        back.setVisibility(View.GONE);
    }

    @Override
    public void onProductPurchased(String productId, TransactionDetails transactionDetails) {
        Log.d(LOG_TAG, "onProductPurchased");
        Log.d(LOG_TAG, productId);
        //purchaseClass(productId);
        if (productId.equals(GdzatorApplication.KEY_ADS))
            offAds();
    }

    @Override
    public void onPurchaseHistoryRestored() {
        Log.d(LOG_TAG, "onPurchaseHistoryRestored");
    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {
        Log.d(LOG_TAG, "onBillingError " + errorCode);
    }

    @Override
    public void onBillingInitialized() {
        Log.d(LOG_TAG, "onBillingInitialized");
        if (bp.loadOwnedPurchasesFromGoogle()) {
            Log.d(LOG_TAG, "Items Loaded");
        }
        for (String str : bp.listOwnedProducts()) {
            //purchaseClass(str);
            if (str.equals(GdzatorApplication.KEY_ADS))
                offAds();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        boolean status = false;
        try {
            status = bp.handleActivityResult(requestCode, resultCode, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!status)
            super.onActivityResult(requestCode, resultCode, data);
    }

    public void purchase(String key) {
        bp.purchase(key);
    }

    private void purchaseClass(String key) {
        try {
            SQLiteDatabase db = DatabaseHelper.getInstance(this).getWritableDatabase();
            ContentValues values = new ContentValues();
            String android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            byte[] bytesOfMessage = (key + android_id + big_string).getBytes("UTF-8");
            values.put(Contract.PurchaseData.COLUMN_NAME_CODE,
                    new String(MessageDigest.getInstance("MD5").digest(bytesOfMessage)));
            values.put(Contract.PurchaseData.COLUMN_NAME_KEY, key);
            db.insert(Contract.PurchaseData.TABLE_NAME, null, values);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void offAds() {
        try {
            SQLiteDatabase db = DatabaseHelper.getInstance(this).getWritableDatabase();
            ContentValues values = new ContentValues();
            String android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            byte[] bytesOfMessage = (android_id + big_string).getBytes("UTF-8");
            values.put(Contract.PurchaseData.COLUMN_NAME_CODE,
                    new String(MessageDigest.getInstance("MD5").digest(bytesOfMessage)));
            values.put(Contract.PurchaseData.COLUMN_NAME_KEY, FIELD_OFF_ADS);
            db.insert(Contract.PurchaseData.TABLE_NAME, null, values);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    public boolean checkPurchase(/*String key*/) {
        return AdsHelper.checkOffAds(this);
        /*SQLiteDatabase db = null;
        try {
            db = DatabaseHelper.getInstance(this).getReadableDatabase();
            Cursor c = db.query(Contract.PurchaseData.TABLE_NAME, null,
                    Contract.PurchaseData.COLUMN_NAME_KEY + "= '" + key + "'", null, null, null, null);
            c.moveToFirst();
            if (c.getCount() == 0)
                return false;
            String android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            byte[] bytesOfMessage = (key + android_id + big_string).getBytes("UTF-8");
            String code = new String(MessageDigest.getInstance("MD5").digest(bytesOfMessage));
            String codeFromBd = c.getString(c.getColumnIndex(Contract.PurchaseData.COLUMN_NAME_CODE));
            c.close();
            return code.equals(codeFromBd);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (db != null)
                db.close();
        }*/
    }

    @UiThread
    public void showLoadingDialog(boolean isShow) {
        if (isShow) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        if (bp != null)
            bp.release();
        if (adView != null)
            adView.destroy();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (adView != null)
            adView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adView != null)
            adView.resume();
    }
}
