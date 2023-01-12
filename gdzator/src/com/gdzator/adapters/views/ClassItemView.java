package com.gdzator.adapters.views;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gdzator.R;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

@EViewGroup(R.layout.item_class)
public class ClassItemView extends LinearLayout {

    @ViewById
    TextView name;

    Context context;

    public ClassItemView(Context context) {
        super(context);
        this.context = context;
    }

    public void bind(String clazz) {
        name.setText(String.format("%s %s", clazz, context.getString(R.string.clazz)));
    }
}
