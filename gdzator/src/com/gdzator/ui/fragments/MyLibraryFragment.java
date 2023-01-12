package com.gdzator.ui.fragments;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.GridView;

import com.gdzator.R;
import com.gdzator.adapters.ClassListAdapter;
import com.gdzator.content.provider.Contract;
import com.gdzator.content.provider.DatabaseHelper;
import com.gdzator.ui.MainActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EFragment(R.layout.fragment_classes)
public class MyLibraryFragment extends Fragment {
    public final static String LOG_TAG = MyLibraryFragment.class.getName();

    @ViewById
    GridView listView;

    @Bean
    ClassListAdapter adapter;

    private List<String> classes = new ArrayList<>();

    @AfterViews
    void afterViews() {
        ((MainActivity) getActivity()).setHeaderTitle(getString(R.string.my_library));
        try {
            SQLiteDatabase db = DatabaseHelper.getInstance(getActivity()).getReadableDatabase();
            Cursor c = db.query(Contract.PurchaseData.TABLE_NAME, null, null, null, null, null, null);
            c.moveToFirst();
            classes.clear();
            while (!c.isAfterLast()) {
                String key = c.getString(c.getColumnIndex(Contract.PurchaseData.COLUMN_NAME_KEY));
                if (((MainActivity) getActivity()).checkPurchase(/*key*/)) {
                    classes.add(key.replaceAll("com.gdzator.class", ""));
                }
                c.moveToNext();
            }

            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        adapter.setValues(classes);
        listView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        ((MainActivity) getActivity()).showBackButton();
        super.onResume();
    }

    @ItemClick
    void listViewItemClicked(String clazz) {
        SubjectsFragment_ fragment = new SubjectsFragment_();
        Bundle args = new Bundle();
        args.putInt(ClassesFragment.ARG_CLASS, Integer.parseInt(clazz));
        fragment.setArguments(args);
        getFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right,
                        android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .replace(R.id.container, fragment, SubjectsFragment_.LOG_TAG)
                .addToBackStack(SubjectsFragment_.LOG_TAG)
                .commitAllowingStateLoss();
    }
}
