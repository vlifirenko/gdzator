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
import com.gdzator.adapters.BookListAdapter;
import com.gdzator.content.model.Book;
import com.gdzator.content.model.Subject;
import com.gdzator.content.rest.BooksResponse;
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

import java.util.List;

@EFragment(R.layout.fragment_books)
public class BooksFragment extends Fragment {
    public final static String LOG_TAG = BooksFragment.class.getName();

    public final static String ARG_BOOK = "book";

    @ViewById
    ListView listView;

    @Bean
    BookListAdapter adapter;

    private Subject subject;

    @RestService
    RestClient restClient;

    @ViewById
    TextView purchase;

    @ViewById
    TextView price;

    @ViewById
    LinearLayout buy;

    private int clazz;
    //private String purchaseKey;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            subject = args.getParcelable(SubjectsFragment.ARG_SUBJECT);
            clazz = args.getInt(ClassesFragment.ARG_CLASS);
        }
    }

    @AfterViews
    void afterViews() {
        ((MainActivity) getActivity()).setHeaderTitle(String.format("%s. %s %s", subject.name, clazz, getString(R.string.clazz)));
        purchase.setText(String.format(getString(R.string.buy_class), clazz));
        price.setText(String.format(getString(R.string.class_price), MainActivity.KEY_COST));
        try {
            adapter.setValues(Book.getBooks(getActivity(), clazz, subject.getId()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        listView.setAdapter(adapter);
        //purchaseKey = getResources().getStringArray(R.array.purchase_keys)[clazz - 5];
        if (((MainActivity) getActivity()).checkPurchase())
            buy.setVisibility(View.GONE);

        getBooks();
    }

    @ItemClick
    void listViewItemClicked(Book book) {
        if (!isAdded())
            return;
        if (book.hasSections) {
            SectionsFragment_ fragment = new SectionsFragment_();
            Bundle args = new Bundle();
            args.putParcelable(ARG_BOOK, book);
            args.putInt(ClassesFragment.ARG_CLASS, clazz);
            args.putParcelable(SubjectsFragment.ARG_SUBJECT, subject);
            fragment.setArguments(args);
            getFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right,
                            android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                    .replace(R.id.container, fragment, SectionsFragment_.LOG_TAG)
                    .addToBackStack(SectionsFragment_.LOG_TAG)
                    .commitAllowingStateLoss();
        } else {
            TasksFragment_ fragment = new TasksFragment_();
            Bundle args = new Bundle();
            args.putParcelable(BooksFragment.ARG_BOOK, book);
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

    @Click
    void buy() {
        ((MainActivity) getActivity()).purchase(GdzatorApplication.KEY_ADS);
    }

    @Background
    void getBooks() {
        if (adapter.getCount() == 0 && isAdded()) {
            ((MainActivity) getActivity()).showLoadingDialog(true);
        }
        try {
            BooksResponse response = restClient.getBooks(String.valueOf(subject.clazz), String.valueOf(subject.getId()));
            if (response.status != 1) {
                Log.e(LOG_TAG, response.error);
                ((MainActivity) getActivity()).showLoadingDialog(false);
                return;
            }
            adapter.setValues(response.result);
            initList(response.result);
        } catch (Exception e) {
            Log.e(LOG_TAG, e.toString());
        }
        if (isAdded())
            ((MainActivity) getActivity()).showLoadingDialog(false);
    }

    @UiThread
    void initList(List<Book> books) {
        try {
            Book.save(getActivity(), books, clazz, subject.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
    }


}
