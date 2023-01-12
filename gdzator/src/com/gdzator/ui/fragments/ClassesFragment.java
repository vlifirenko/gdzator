package com.gdzator.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.GridView;

import com.gdzator.R;
import com.gdzator.adapters.ClassListAdapter;
import com.gdzator.ui.MainActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import java.util.Arrays;

@EFragment(R.layout.fragment_classes)
public class ClassesFragment extends Fragment {
    public final static String LOG_TAG = ClassesFragment.class.getName();

    public final static String ARG_CLASS = "class";

    @ViewById
    GridView listView;

    @Bean
    ClassListAdapter adapter;

    @AfterViews
    void afterViews() {
        ((MainActivity)getActivity()).setHeaderTitle(getString(R.string.app_name));
        ((MainActivity)getActivity()).showMenuButton();
        adapter.setValues(Arrays.asList(getResources().getStringArray(R.array.classes)));
        listView.setAdapter(adapter);
    }

    @ItemClick
    void listViewItemClicked(String clazz) {
        SubjectsFragment_ fragment = new SubjectsFragment_();
        Bundle args = new Bundle();
        args.putInt(ARG_CLASS, Integer.parseInt(clazz));
        fragment.setArguments(args);
        getFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right,
                        android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .replace(R.id.container, fragment, SubjectsFragment_.LOG_TAG)
                .addToBackStack(SubjectsFragment_.LOG_TAG)
                .commitAllowingStateLoss();
    }
}
