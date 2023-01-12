package com.gdzator.ui.fragments;

import android.support.v4.app.Fragment;
import android.webkit.WebView;
import android.widget.TextView;

import com.gdzator.R;
import com.gdzator.ui.MainActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_info)
public class InfoFragment extends Fragment {
    public final static String LOG_TAG = InfoFragment.class.getName();

    @AfterViews
    void afterViews() {
        ((MainActivity) getActivity()).setHeaderTitle(getString(R.string.info));
    }

    @Override
    public void onResume() {
        ((MainActivity)getActivity()).showBackButton();
        super.onResume();
    }

}
