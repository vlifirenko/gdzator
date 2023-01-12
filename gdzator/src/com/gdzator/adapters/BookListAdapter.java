package com.gdzator.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.gdzator.adapters.views.BookItemView;
import com.gdzator.adapters.views.BookItemView_;
import com.gdzator.content.model.Book;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.List;

@EBean
public class BookListAdapter extends BaseAdapter {

    private List<Book> values = new ArrayList<>();

    @RootContext
    Context context;

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        BookItemView bookItemView;
        if (convertView == null) {
            bookItemView = BookItemView_.build(context);
        } else {
            bookItemView = (BookItemView) convertView;
        }
        bookItemView.bind(getItem(position));
        return bookItemView;
    }

    @Override
    public int getCount() {
        return values.size();
    }

    @Override
    public Book getItem(int position) {
        return values.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setValues(List<Book> values) {
        this.values = values;
    }
}
