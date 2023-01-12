package com.gdzator.adapters.views;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gdzator.R;
import com.gdzator.content.model.Task;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

@EViewGroup(R.layout.item_task)
public class TaskItemView extends LinearLayout {

    @ViewById
    TextView name;

    public TaskItemView(Context context) {
        super(context);
    }

    public void bind(Task task) {
        name.setText(task.number);
    }
}
