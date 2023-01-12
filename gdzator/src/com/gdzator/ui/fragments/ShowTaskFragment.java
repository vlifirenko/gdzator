package com.gdzator.ui.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gdzator.GdzatorApplication;
import com.gdzator.R;
import com.gdzator.content.model.Section;
import com.gdzator.content.model.Task;
import com.gdzator.ui.MainActivity;
import com.gdzator.ui.ShowTaskActivity_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_show_task)
public class ShowTaskFragment extends Fragment {
    public final static String LOG_TAG = ShowTaskFragment.class.getName();

    private Task task;
    private Section section;
    private int clazz;

    @ViewById
    WebView webView;

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
            task = args.getParcelable(TasksFragment.ARG_TASK);
            section = args.getParcelable(SectionsFragment.ARG_SECTION);
            clazz = args.getInt(ClassesFragment.ARG_CLASS);
            if (section == null)
                ((MainActivity) getActivity()).setHeaderTitle(task.number);
            else
                ((MainActivity) getActivity()).setHeaderTitle(String.format("%s, %s", section.name, task.number));
        }
    }

    @AfterViews
    void afterViews() {
        if (section == null)
            ((MainActivity) getActivity()).setHeaderTitle(task.number);
        else
            ((MainActivity) getActivity()).setHeaderTitle(String.format("%s, %s", section.name, task.number));
        purchase.setText(String.format(getString(R.string.buy_class), clazz));
        price.setText(String.format(getString(R.string.class_price), MainActivity.KEY_COST));
        webView.setBackgroundColor(Color.WHITE);
        webView.setInitialScale(1);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.loadUrl(task.url);
        webView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Intent intent = new Intent(getActivity(), ShowTaskActivity_.class);
                    Bundle b = new Bundle();
                    b.putParcelable(TasksFragment.ARG_TASK, task);
                    intent.putExtras(b);
                    startActivity(intent);
                }
                return false;
            }
        });
        //purchaseKey = getResources().getStringArray(R.array.purchase_keys)[clazz - 5];
        if (((MainActivity) getActivity()).checkPurchase())
            buy.setVisibility(View.GONE);
    }

    @Click
    void buy() {
       ((MainActivity) getActivity()).purchase(GdzatorApplication.KEY_ADS);
    }
}
