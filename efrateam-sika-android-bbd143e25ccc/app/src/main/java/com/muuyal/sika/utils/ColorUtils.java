package com.muuyal.sika.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;

import com.muuyal.sika.R;

/**
 * Created by Isra on 6/29/17.
 */

public class ColorUtils {

    private static int positionColor = 0;
    private static int nextPosition = 0;

    public static int getNextColor(Context mContext, int position) {
        positionColor++;

        if (position % 6 == 0) {
            positionColor = 0;
        }

        switch (positionColor) {
            case 0:
                return mContext.getResources().getColor(R.color.title_1);
            case 1:
                return mContext.getResources().getColor(R.color.title_2);
            case 2:
                return mContext.getResources().getColor(R.color.title_3);
            case 3:
                return mContext.getResources().getColor(R.color.title_4);
            case 4:
                return mContext.getResources().getColor(R.color.title_5);
            case 5:
                return mContext.getResources().getColor(R.color.title_6);
            default:
                return mContext.getResources().getColor(R.color.title_1);
        }
    }

    /***
     * Thie method return the custom Color state
     *
     * @param mContext is the context of the App
     ***/
    public static ColorStateList getCustomColorStateList(Context mContext, int colorSelected) {
        return new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_focused},
                        new int[]{android.R.attr.state_pressed},
                        new int[]{android.R.attr.state_selected},
                        new int[]{android.R.attr.state_checked},
                        new int[]{-android.R.attr.state_checked}
                },
                new int[]{
                        Color.WHITE,
                        Color.WHITE,
                        Color.WHITE,
                        Color.WHITE,
                        colorSelected
                }
        );
    }
}
