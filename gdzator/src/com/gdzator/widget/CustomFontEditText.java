package com.gdzator.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

import com.gdzator.R;

public class CustomFontEditText extends EditText {

    public CustomFontEditText(Context context) {
        super(context);
    }

    public CustomFontEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CustomFontEditText(Context context, AttributeSet attrs, int defStyle) {
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
