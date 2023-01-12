package com.gdzator.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.gdzator.adapters.views.SubjectItemView;
import com.gdzator.adapters.views.SubjectItemView_;
import com.gdzator.content.model.Subject;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.List;

@EBean
public class SubjectListAdapter extends BaseAdapter {

    private List<Subject> values = new ArrayList<Subject>();

    @RootContext
    Context context;

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        SubjectItemView subjectItemView;
        if (convertView == null) {
            subjectItemView = SubjectItemView_.build(context);
        } else {
            subjectItemView = (SubjectItemView) convertView;
        }
        subjectItemView.bind(getItem(position));
        return subjectItemView;
    }

    @Override
    public int getCount() {
        return values.size();
    }

    @Override
    public Subject getItem(int position) {
        return values.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setValues(List<Subject> values) {
        this.values = values;
    }
}
