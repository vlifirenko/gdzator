package com.gdzator.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gdzator.GdzatorApplication;
import com.gdzator.R;
import com.gdzator.adapters.TaskListAdapter;
import com.gdzator.content.model.Book;
import com.gdzator.content.model.Section;
import com.gdzator.content.model.Task;
import com.gdzator.content.rest.TasksResponse;
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

@EFragment(R.layout.fragment_tasks)
public class TasksFragment extends Fragment {
    public final static String LOG_TAG = TasksFragment.class.getName();

    public final static String ARG_TASK = "task";

    @ViewById
    ListView listView;

    @ViewById
    TextView purchase;

    @ViewById
    TextView price;

    @ViewById
    EditText search;

    @ViewById
    LinearLayout buy;

    @Bean
    TaskListAdapter adapter;

    private Book book;
    private Section section;
    private int clazz;
    private List<Task> tasks;

    @RestService
    RestClient restClient;

    //private String purchaseKey;
    private boolean purchased;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            try {
                this.book = args.getParcelable(BooksFragment.ARG_BOOK);
                this.section = args.getParcelable(SectionsFragment.ARG_SECTION);
                if (section == null)
                    ((MainActivity) getActivity()).setHeaderTitle(book.name);
                else
                    ((MainActivity) getActivity()).setHeaderTitle(String.format("%s. %s", book.name, section.name));
                clazz = args.getInt(ClassesFragment.ARG_CLASS);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @AfterViews
    void afterViews() {
        if (book != null) {
            if (section == null)
                ((MainActivity) getActivity()).setHeaderTitle(book.name);
            else
                ((MainActivity) getActivity()).setHeaderTitle(String.format("%s. %s", book.name, section.name));

        }
        purchase.setText(String.format(getString(R.string.buy_class), clazz));
        price.setText(String.format(getString(R.string.class_price), MainActivity.KEY_COST));
        try {
            adapter.setValues(Task.getTasks(getActivity(), book.getId(), section));
        } catch (Exception e) {
            e.printStackTrace();
        }
        listView.setAdapter(adapter);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0 && tasks != null) {
                    adapter.setValues(tasks);
                    adapter.notifyDataSetChanged();
                    return;
                }
                if (tasks == null)
                    return;
                List<Task> searchResult = new ArrayList<>();
                for (Task task : tasks) {
                    if (task.number != null && task.number.contains(s))
                        searchResult.add(task);
                }
                adapter.setValues(searchResult);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        //purchaseKey = getResources().getStringArray(R.array.purchase_keys)[clazz - 5];
        if (((MainActivity) getActivity()).checkPurchase()) {
            buy.setVisibility(View.GONE);
        }
        purchased = true;

        getTasks();
    }

    @ItemClick
    void listViewItemClicked(Task task) {
        //if (purchased || ((MainActivity) getActivity()).useTryCode()) {
        if (purchased) {
            ShowTaskFragment_ fragment = new ShowTaskFragment_();
            Bundle args = new Bundle();
            args.putParcelable(ARG_TASK, task);
            args.putParcelable(SectionsFragment.ARG_SECTION, section);
            args.putInt(ClassesFragment.ARG_CLASS, clazz);
            fragment.setArguments(args);
            getFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right,
                            android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                    .replace(R.id.container, fragment, ShowTaskFragment_.LOG_TAG)
                    .addToBackStack(ShowTaskFragment_.LOG_TAG)
                    .commitAllowingStateLoss();
        } else {
            Toast.makeText(getActivity(), getString(R.string.need_buy, clazz), Toast.LENGTH_SHORT).show();
        }
    }

    @Click
    void buy() {
       ((MainActivity) getActivity()).purchase(GdzatorApplication.KEY_ADS);
    }

    @Background
    void getTasks() {
        if (adapter.getCount() == 0 && isAdded())
            ((MainActivity) getActivity()).showLoadingDialog(true);
        try {
            TasksResponse response;
            if (section == null)
                response = restClient.getTasks(String.valueOf(book.getId()));
            else
                response = restClient.getTasksBySection(String.valueOf(book.getId()), String.valueOf(section.getId()));
            if (response.status != 1) {
                Log.e(LOG_TAG, response.error);
                if (isAdded())
                    ((MainActivity) getActivity()).showLoadingDialog(false);
                return;
            }
            tasks = response.result;
            adapter.setValues(response.result);
            initList(response.result);
        } catch (Exception e) {
            Log.e(LOG_TAG, e.toString());
        }
        if (isAdded())
            ((MainActivity) getActivity()).showLoadingDialog(false);
    }

    @UiThread
    void initList(List<Task> list) {
        try {
            Task.save(getActivity(), list, section, book.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
    }
}
