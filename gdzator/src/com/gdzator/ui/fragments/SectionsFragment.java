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
import com.gdzator.adapters.SectionsListAdapter;
import com.gdzator.content.model.Book;
import com.gdzator.content.model.Section;
import com.gdzator.content.model.Subject;
import com.gdzator.content.rest.SectionsResponse;
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

@EFragment(R.layout.fragment_sections)
public class SectionsFragment extends Fragment {
    public final static String LOG_TAG = SectionsFragment.class.getName();

    public final static String ARG_SECTION = "section";

    @ViewById
    ListView listView;

    @ViewById
    TextView purchase;

    @ViewById
    TextView price;

    @Bean
    SectionsListAdapter adapter;

    @ViewById
    LinearLayout buy;

    private Book book;

    @RestService
    RestClient restClient;

    private List<Section> sections = new ArrayList<>();
    private int clazz;
    //private String purchaseKey;
    private Subject subject;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            try {
                book = args.getParcelable(BooksFragment.ARG_BOOK);
            } catch (Exception e) {
                e.printStackTrace();
            }
            clazz = args.getInt(ClassesFragment.ARG_CLASS);
            subject = args.getParcelable(SubjectsFragment.ARG_SUBJECT);
            ((MainActivity) getActivity()).setHeaderTitle(String.format("%s. %s", book.name, book.getAuthors()));
        }
    }

    @AfterViews
    void afterViews() {
        if (book != null) {
            ((MainActivity) getActivity()).setHeaderTitle(String.format("%s. %s", book.name, book.getAuthors()));
        }
        purchase.setText(String.format(getString(R.string.buy_class), clazz));
        price.setText(String.format(getString(R.string.class_price), MainActivity.KEY_COST));
        try {
            sections = Section.getSections(getActivity(), clazz, subject.getId(), book.getId());
            if (sections.size() > 0)
                initList(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        listView.setAdapter(adapter);
        //purchaseKey = getResources().getStringArray(R.array.purchase_keys)[clazz - 5];
        if (((MainActivity) getActivity()).checkPurchase())
            buy.setVisibility(View.GONE);
        getSections();
    }

    @Background
    void getSections() {
        if (sections.size() == 0 && isAdded())
            ((MainActivity) getActivity()).showLoadingDialog(true);
        try {
            SectionsResponse response = restClient.getSections(String.valueOf(book.getId()));
            if (response.status != 1) {
                Log.e(LOG_TAG, response.error);
                if (isAdded())
                    ((MainActivity) getActivity()).showLoadingDialog(false);
                return;
            }
            if (response.result.size() == 0) {
                if (isAdded())
                    ((MainActivity) getActivity()).showLoadingDialog(false);
                return;
            }
            sections.clear();
            for (Section section : response.result) {
                sections.add(section);
            }
            Section.save(getActivity(), sections, clazz, subject.getId(), book.getId());
            initList(null);
        } catch (Exception e) {
            Log.e(LOG_TAG, e.toString());
        }
        if (isAdded())
            ((MainActivity) getActivity()).showLoadingDialog(false);
    }

    @ItemClick
    void listViewItemClicked(Section section) {
        initList(section);
    }

    @Click
    void buy() {
        ((MainActivity) getActivity()).purchase(GdzatorApplication.KEY_ADS);
    }

    @UiThread
    void initList(Section selectSection) {
        List<Section> showSections = new ArrayList<>();
        for (Section section : this.sections) {
            if (section.parent == null || section.parent == -1) {
                if (selectSection == null)
                    showSections.add(section);
                continue;
            }
            if (selectSection != null && section.parent.equals(selectSection.getId()))
                showSections.add(section);
        }
        if (showSections.size() > 0) {
            adapter.setValues(showSections);
            adapter.notifyDataSetChanged();
        } else {
            toTasks(selectSection);
        }
    }

    @UiThread
    void toTasks(Section selectSection) {
        if (!isAdded())
            return;
        TasksFragment_ fragment = new TasksFragment_();
        Bundle args = new Bundle();
        args.putParcelable(ARG_SECTION, selectSection);
        args.putParcelable(BooksFragment.ARG_BOOK, this.book);
        args.putInt(ClassesFragment.ARG_CLASS, clazz);
        args.putParcelable(SubjectsFragment.ARG_SUBJECT, subject);
        fragment.setArguments(args);
        getFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right,
                        android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .replace(R.id.container, fragment, TasksFragment.LOG_TAG)
                .addToBackStack(TasksFragment.LOG_TAG)
                .commitAllowingStateLoss();
    }
}
