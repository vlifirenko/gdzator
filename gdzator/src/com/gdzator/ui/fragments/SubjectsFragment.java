package com.gdzator.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gdzator.GdzatorApplication;
import com.gdzator.R;
import com.gdzator.adapters.SubjectListAdapter;
import com.gdzator.content.model.Subject;
import com.gdzator.content.rest.SubjectsResponse;
import com.gdzator.rest.RestClient;
import com.gdzator.ui.MainActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;

import java.util.ArrayList;
import java.util.List;

@EFragment(R.layout.fragment_subjects)
public class SubjectsFragment extends Fragment {
    public final static String LOG_TAG = SubjectsFragment.class.getName();

    public final static String ARG_SUBJECT = "subject";

    private int clazz;

    @ViewById
    ListView listView;

    @Bean
    SubjectListAdapter adapter;

    @RestService
    RestClient restClient;

    @ViewById
    TextView purchase;

    @ViewById
    TextView price;

    @ViewById
    LinearLayout buy;

    //private String purchaseKey;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            clazz = args.getInt(ClassesFragment.ARG_CLASS, -1);
            if (clazz != -1) {
                ((MainActivity) getActivity()).setHeaderTitle(String.format("%s класс", clazz));
            }
        }
    }

    @AfterViews
    void afterViews() {
        ((MainActivity) getActivity()).setHeaderTitle(String.format("%s класс", clazz));
        ((MainActivity) getActivity()).showBackButton();
        purchase.setText(String.format(getString(R.string.buy_class), clazz));
        price.setText(String.format(getString(R.string.class_price), MainActivity.KEY_COST));
        try {
            adapter.setValues(Subject.getSubjects(getActivity(), clazz));
        } catch (Exception e) {
            e.printStackTrace();
        }
        listView.setAdapter(adapter);
        //purchaseKey = getResources().getStringArray(R.array.purchase_keys)[clazz - 5];
        if (((MainActivity) getActivity()).checkPurchase())
            buy.setVisibility(View.GONE);

        getSubjects();
    }

    @ItemClick
    void listViewItemClicked(Subject subject) {
        if (!isAdded())
            return;
        BooksFragment_ fragment = new BooksFragment_();
        Bundle args = new Bundle();
        args.putParcelable(ARG_SUBJECT, subject);
        args.putInt(ClassesFragment.ARG_CLASS, clazz);
        fragment.setArguments(args);
        getFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right,
                        android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .replace(R.id.container, fragment, BooksFragment_.LOG_TAG)
                .addToBackStack(BooksFragment_.LOG_TAG)
                .commitAllowingStateLoss();
    }

    @Click
    void buy() {
        ((MainActivity) getActivity()).purchase(GdzatorApplication.KEY_ADS);
    }

    @Background
    void getSubjects() {
        if (adapter.getCount() == 0 && isAdded()) {
            ((MainActivity) getActivity()).showLoadingDialog(true);
        }
        try {
            SubjectsResponse response = restClient.getSubjects(String.valueOf(clazz));
            if (response.status != 1) {
                Log.e(LOG_TAG, response.error);
                ((MainActivity) getActivity()).showLoadingDialog(false);
                return;
            }
            List<Subject> subjects = new ArrayList<>();
            for (Subject subject : response.result) {
                subject.clazz = this.clazz;
                subjects.add(subject);
            }
            adapter.setValues(subjects);
            initList(subjects);
        } catch (Exception e) {
            Log.e(LOG_TAG, e.toString());
        }
        if (isAdded())
            ((MainActivity) getActivity()).showLoadingDialog(false);
    }

    @UiThread
    void initList(List<Subject> subjects) {
        try {
            Subject.save(getActivity(), subjects);
        } catch (Exception e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
    }
}
