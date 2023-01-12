package com.gdzator.adapters.views;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gdzator.R;
import com.gdzator.content.model.Subject;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

@EViewGroup(R.layout.item_subject)
public class SubjectItemView extends LinearLayout {

    @ViewById
    TextView name;

    public SubjectItemView(Context context) {
        super(context);
    }

    public void bind(Subject subject) {
        name.setText(subject.name);
    }
}
