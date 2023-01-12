package com.gdzator.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.gdzator.adapters.views.ClassItemView;
import com.gdzator.adapters.views.ClassItemView_;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.List;

@EBean
public class ClassListAdapter extends BaseAdapter {

    private List<String> values = new ArrayList<>();

    @RootContext
    Context context;

    public ClassListAdapter(Context context) {

    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ClassItemView classItemView;
        if (convertView == null) {
            classItemView = ClassItemView_.build(context);
        } else {
            classItemView = (ClassItemView) convertView;
        }
        classItemView.bind(getItem(position));
        return classItemView;
    }

    @Override
    public int getCount() {
        return values.size();
    }

    @Override
    public String getItem(int position) {
        return values.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }
}