package com.gdzator.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.gdzator.R;

public class CustomFontTextView extends TextView {

    public CustomFontTextView(Context context) {
        super(context);
    }

    public CustomFontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CustomFontTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context ctx, AttributeSet attrs) {
        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.CustomFontTextView);
        String customFontName = a.getString(R.styleable.CustomFontTextView_typeface);
        setTypeface(customFontName);
        a.recycle();
    }

    public void setTypeface(String fontName) {
        if (fontName != null) {
            try {
                Typeface mCustomFont = Typeface.createFromAsset(getContext().getAssets(), fontName);
                setTypeface(mCustomFont);
            } catch (Exception e) {
                e.printStackTrace(System.err);
            }
        }
    }
}