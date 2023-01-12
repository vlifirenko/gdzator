package com.gdzator.ui.fragments;

import android.support.v4.app.Fragment;

import com.gdzator.R;
import com.gdzator.ui.MainActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;

@EFragment(R.layout.fragment_settings)
public class SettingsFragment extends Fragment {
    public final static String LOG_TAG = SettingsFragment.class.getName();

    @AfterViews
    void afterViews() {
        ((MainActivity) getActivity()).setHeaderTitle(getString(R.string.settings));
    }

    @Override
    public void onResume() {
        ((MainActivity)getActivity()).showBackButton();
        super.onResume();
    }
}
