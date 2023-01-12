package com.gdzator.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.gdzator.adapters.views.SectionItemView;
import com.gdzator.adapters.views.SectionItemView_;
import com.gdzator.content.model.Section;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.List;

@EBean
public class SectionsListAdapter extends BaseAdapter {

    private List<Section> values = new ArrayList<Section>();

    @RootContext
    Context context;

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        SectionItemView sectionItemView;
        if (convertView == null) {
            sectionItemView = SectionItemView_.build(context);
        } else {
            sectionItemView = (SectionItemView) convertView;
        }
        sectionItemView.bind(getItem(position));
        return sectionItemView;
    }

    @Override
    public int getCount() {
        return values.size();
    }

    @Override
    public Section getItem(int position) {
        return values.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setValues(List<Section> values) {
        this.values = values;
    }
}
