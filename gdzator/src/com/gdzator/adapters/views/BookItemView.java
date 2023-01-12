package com.gdzator.adapters.views;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gdzator.R;
import com.gdzator.content.model.Book;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

@EViewGroup(R.layout.item_book)
public class BookItemView extends LinearLayout {

    @ViewById
    ImageView img;

    @ViewById
    TextView name;

    @ViewById
    TextView author;

    public BookItemView(Context context) {
        super(context);
    }

    public void bind(Book book) {
        if (book.image != null)
            ImageLoader.getInstance().displayImage(book.image.url, img);
        name.setText(book.name);
        author.setText(book.getAuthors());
    }
}
