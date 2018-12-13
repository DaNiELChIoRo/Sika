package com.muuyal.sika.customs;

import android.content.Context;
import android.util.AttributeSet;

import static com.muuyal.sika.helpers.TypefaceHelper.applyFont;

/**
 * Created by isra on 12/7/17.
 */

public class CustomTextView extends android.support.v7.widget.AppCompatTextView {

    public CustomTextView(Context context) {
        this(context, null);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        applyFont(this, attrs, defStyle);
    }

}
