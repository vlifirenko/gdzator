package com.gdzator.ui.fragments;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.EditText;

import com.gdzator.GdzatorApplication;
import com.gdzator.R;
import com.gdzator.ui.MainActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_feedback)
public class FeedbackFragment extends Fragment {
    public final static String LOG_TAG = FeedbackFragment.class.getName();

    @ViewById
    EditText text;

    @AfterViews
    void afterViews() {
        ((MainActivity) getActivity()).setHeaderTitle(getString(R.string.feedback));
    }

    @Override
    public void onResume() {
        ((MainActivity) getActivity()).showBackButton();
        super.onResume();
    }

    @Click
    void send() {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{GdzatorApplication.FEEDBACK_EMAIL});
        i.putExtra(Intent.EXTRA_SUBJECT, "Gdzator feedback");
        i.putExtra(Intent.EXTRA_TEXT, text.getText().toString());
        try {
            startActivity(Intent.createChooser(i, getString(R.string.send_mail)));
        } catch (android.content.ActivityNotFoundException ex) {
            Log.d(LOG_TAG, "There are no email clients installed.");
        }
    }

}
