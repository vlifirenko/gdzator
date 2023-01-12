package com.gdzator.adapters.views;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gdzator.R;
import com.gdzator.content.model.Section;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

@EViewGroup(R.layout.item_section)
public class SectionItemView extends LinearLayout {

    @ViewById
    TextView name;

    public SectionItemView(Context context) {
        super(context);
    }

    public void bind(Section section) {
        name.setText(section.name);
    }
}
