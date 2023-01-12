package com.gdzator.ui.fragments;

import android.support.v4.app.Fragment;

import com.gdzator.R;
import com.gdzator.ui.MainActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;

@EFragment(R.layout.fragment_start)
public class StartFragment extends Fragment {
    public final static String LOG_TAG = StartFragment.class.getName();

    @AfterViews
    void afterViews() {
        ((MainActivity)getActivity()).setHeaderTitle(getString(R.string.app_name));
        ((MainActivity)getActivity()).showMenuButton();
    }

    @Click
    void selectClass() {
        getFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right,
                        android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .replace(R.id.container, new ClassesFragment_(), ClassesFragment_.LOG_TAG)
                .addToBackStack(ClassesFragment_.LOG_TAG)
                .commitAllowingStateLoss();
    }

    @Click
    void myLibrary() {
        getFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right,
                        android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .replace(R.id.container, new MyLibraryFragment_(), MyLibraryFragment_.LOG_TAG)
                .addToBackStack(MyLibraryFragment_.LOG_TAG)
                .commitAllowingStateLoss();
    }

    @Click
    void settings() {
        getFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right,
                        android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .replace(R.id.container, new SettingsFragment_(), SettingsFragment_.LOG_TAG)
                .addToBackStack(SettingsFragment_.LOG_TAG)
                .commitAllowingStateLoss();
    }

    @Click
    void info() {
        getFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right,
                        android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .replace(R.id.container, new InfoFragment_(), InfoFragment_.LOG_TAG)
                .addToBackStack(InfoFragment_.LOG_TAG)
                .commitAllowingStateLoss();
    }
}
