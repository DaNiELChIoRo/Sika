package com.muuyal.sika.customs;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

import com.muuyal.sika.helpers.TypefaceHelper;

/**
 * Created by isra on 12/7/17.
 */

public class CustomButton extends AppCompatButton {

    public CustomButton(Context context) {
        this(context, null);
    }

    public CustomButton(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public CustomButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypefaceHelper.applyFont(this, attrs, defStyleAttr);
    }
}
