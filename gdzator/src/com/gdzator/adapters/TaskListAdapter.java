package com.gdzator.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.gdzator.adapters.views.TaskItemView;
import com.gdzator.adapters.views.TaskItemView_;
import com.gdzator.content.model.Task;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.List;

@EBean
public class TaskListAdapter extends BaseAdapter {

    private List<Task> values = new ArrayList<Task>();

    @RootContext
    Context context;

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        TaskItemView taskItemView;
        if (convertView == null) {
            taskItemView = TaskItemView_.build(context);
        } else {
            taskItemView = (TaskItemView) convertView;
        }
        taskItemView.bind(getItem(position));
        return taskItemView;
    }

    @Override
    public int getCount() {
        return values.size();
    }

    @Override
    public Task getItem(int position) {
        return values.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setValues(List<Task> values) {
        this.values = values;
    }

    public List<Task> getValues() {
        return values;
    }
}
