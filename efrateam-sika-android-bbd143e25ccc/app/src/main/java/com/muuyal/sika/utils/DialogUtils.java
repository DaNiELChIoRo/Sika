package com.muuyal.sika.utils;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.InputType;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.muuyal.sika.R;
import com.muuyal.sika.helpers.TypefaceHelper;
import com.muuyal.sika.interfaces.ICallbackResponse;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static com.muuyal.sika.utils.DateUtils.SDF1;

/**
 * Created by Isra on 5/22/2017.
 */

public class DialogUtils {

    public static final String TAG = DialogUtils.class.getSimpleName();
    private static MaterialDialog dialog;
    public static final SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

    /***
     * This method show normal dialog
     *
     * @param context is the context of the App
     * @param title   is the title of dialog to show
     * @param message is the message to show
     ***/
    public static void showAlert(Context context, String title, String message) {
        dismissDialog();
        dialog = new MaterialDialog.Builder(context)
                .title(title)
                .content(message)
                .positiveText(context.getString(R.string.label_ok))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })

                .titleColor(context.getResources().getColor(R.color.colorAccent))
                .backgroundColor(Color.WHITE)
                .contentColor(Color.BLACK)
                .cancelable(false)
                .typeface(TypefaceHelper.getInstance().getTypeface(TypefaceHelper.FONT_OPEN_SANS),
                        TypefaceHelper.getInstance().getTypeface(TypefaceHelper.FONT_OPEN_SANS))

                .show();
    }

    /***
     * This method show normal dialog
     *
     * @param context     is the context of the App
     * @param title       is the title of dialog to show
     * @param message     is the message to show
     * @param btnPositive is the text of btn positive
     ***/
    public static void showAlert(Context context, String title, String message, String btnPositive) {
        dismissDialog();
        dialog = new MaterialDialog.Builder(context)
                .title(title)
                .content(message)
                .positiveText(btnPositive)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })

                .titleColor(context.getResources().getColor(R.color.colorAccent))
                .backgroundColor(Color.WHITE)
                .contentColor(Color.BLACK)
                .cancelable(false)
                .typeface(TypefaceHelper.getInstance().getTypeface(TypefaceHelper.FONT_OPEN_SANS),
                        TypefaceHelper.getInstance().getTypeface(TypefaceHelper.FONT_OPEN_SANS))

                .show();
    }

    /***
     * This method show normal dialog
     *
     * @param context     is the context of the App
     * @param title       is the title of dialog to show
     * @param message     is the message to show
     * @param positiveListener is the Listener positive btn
     ***/
    public static void showAlert(Context context, String title, String message, MaterialDialog.SingleButtonCallback positiveListener) {
        dismissDialog();
        dialog = new MaterialDialog.Builder(context)
                .title(title)
                .content(message)
                .positiveText(context.getString(R.string.label_ok))
                .onPositive(positiveListener)
                .titleColor(context.getResources().getColor(R.color.colorAccent))
                .backgroundColor(Color.WHITE)
                .contentColor(Color.BLACK)
                .cancelable(false)
                .typeface(TypefaceHelper.getInstance().getTypeface(TypefaceHelper.FONT_OPEN_SANS),
                        TypefaceHelper.getInstance().getTypeface(TypefaceHelper.FONT_OPEN_SANS))

                .show();
    }

    /***
     * This method show normal dialog
     *
     * @param context          is the context of the App
     * @param title            is the title of dialog to show
     * @param message          is the message to show
     * @param positiveListener is the listener positive
     ***/
    public static void showAlertYesNo(Context context, String title, String message, MaterialDialog.SingleButtonCallback positiveListener) {
        dismissDialog();
        dialog = new MaterialDialog.Builder(context)
                .title(title)
                .content(message)
                .positiveText(context.getString(R.string.label_yes))
                .negativeText(context.getString(R.string.label_no))
                .onPositive(positiveListener)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })

                .titleColor(context.getResources().getColor(R.color.colorAccent))
                .backgroundColor(Color.WHITE)
                .contentColor(Color.BLACK)
                .cancelable(false)
                .typeface(TypefaceHelper.getInstance().getTypeface(TypefaceHelper.FONT_OPEN_SANS),
                        TypefaceHelper.getInstance().getTypeface(TypefaceHelper.FONT_OPEN_SANS))

                .show();
    }

    /***
     * This method show normal dialog
     *
     * @param context          is the context of the App
     * @param title            is the title of dialog to show
     * @param message          is the message to show
     * @param positiveListener is the listener positive
     * @param negativeListener is the listener negative
     ***/
    public static void showAlertYesNo(Context context, String title, String message,
                                      MaterialDialog.SingleButtonCallback positiveListener, MaterialDialog.SingleButtonCallback negativeListener) {
        dismissDialog();
        dialog = new MaterialDialog.Builder(context)
                .title(title)
                .content(message)
                .positiveText(context.getString(R.string.label_yes))
                .negativeText(context.getString(R.string.label_no))
                .onPositive(positiveListener)
                .onNegative(negativeListener)

                .titleColor(context.getResources().getColor(R.color.colorAccent))
                .backgroundColor(Color.WHITE)
                .contentColor(Color.BLACK)
                .cancelable(false)
                .typeface(TypefaceHelper.getInstance().getTypeface(TypefaceHelper.FONT_OPEN_SANS),
                        TypefaceHelper.getInstance().getTypeface(TypefaceHelper.FONT_OPEN_SANS))

                .show();
    }

    /***
     * This method return normal dialog
     *
     * @param context is the context of the App
     ***/
    public static Dialog showLoader(Context context) {
        return showLoader(context, null);
    }

    /***
     * This method return normal dialog
     *
     * @param mContext is the context of the App
     * @param message  is the message to show
     ***/
    public static Dialog showLoader(Context mContext, String message) {
        dismissDialog();
        dialog = new MaterialDialog.Builder(mContext)
                .progress(true, 0)
                .content(message)
                .backgroundColor(Color.WHITE)
                .contentColor(Color.BLACK)
                .cancelable(false)
                .typeface(TypefaceHelper.getInstance().getTypeface(TypefaceHelper.FONT_OPEN_SANS),
                        TypefaceHelper.getInstance().getTypeface(TypefaceHelper.FONT_OPEN_SANS))
                .build();
        return dialog;
    }

    /***
     * This method return normal dialog
     *
     * @param mContext is the context of the App
     * @param message  is the message to show
     ***/
    public static MaterialDialog getProgressLoader(Context mContext, String message, int maxProgress) {
        dismissDialog();
        dialog = new MaterialDialog.Builder(mContext)
                .content(message)
                .backgroundColor(Color.WHITE)
                .contentColor(Color.BLACK)
                .progress(false, maxProgress, true)
                .cancelable(false)
                .typeface(TypefaceHelper.getInstance().getTypeface(TypefaceHelper.FONT_OPEN_SANS),
                        TypefaceHelper.getInstance().getTypeface(TypefaceHelper.FONT_OPEN_SANS))
                .build();
        return dialog;
    }

    /***
     * This method return normal dialog
     *
     * @param mContext is the context of the App
     * @param message  is the message to show
     ***/
    public static Dialog showProgressLoader(Context mContext, String message) {
        dismissDialog();
        dialog = new MaterialDialog.Builder(mContext)
                .content(message)
                .backgroundColor(Color.WHITE)
                .contentColor(Color.BLACK)
                .progress(true, 0)
                .cancelable(false)
                .typeface(TypefaceHelper.getInstance().getTypeface(TypefaceHelper.FONT_OPEN_SANS),
                        TypefaceHelper.getInstance().getTypeface(TypefaceHelper.FONT_OPEN_SANS))
                .build();
        return dialog;
    }

    /***
     * This method dismiss the actual dialog if it exist and is showing
     ***/
    public static void dismissDialog() {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }

    /***
     * This method show the date picker dialog
     *
     * @param fm                is the actual fragment manager
     * @param mCallbackResponse is the Callback response
     ***/
    public static void showDatePickerDialog(FragmentManager fm, final ICallbackResponse<String> mCallbackResponse) {
        DialogFragment newFragment = new DatePickerFragment(mCallbackResponse);
        newFragment.show(fm, "datePicker");
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        private ICallbackResponse<String> mCallbackResponse;

        public DatePickerFragment() {
        }

        @SuppressLint("ValidFragment")
        public DatePickerFragment(ICallbackResponse<String> mCallbackResponse) {
            this.mCallbackResponse = mCallbackResponse;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            DatePickerDialog mDatePickerDialog = new DatePickerDialog(getActivity(), R.style.PickerDialogCustom, this, year, month, day);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                mDatePickerDialog.getDatePicker().setCalendarViewShown(false);
                mDatePickerDialog.getDatePicker().setSpinnersShown(true);
                mDatePickerDialog.getDatePicker().setLayoutMode(1);
            }
            // Create a new instance of DatePickerDialog and return it
            return mDatePickerDialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            if (mCallbackResponse != null) {
                try {
                    String mDate = day + "/" + (month + 1) + "/" + year;
                    mCallbackResponse.onCallback(SDF.format(SDF.parse(mDate)));
                } catch (Exception e) {
                    LoggerUtils.logError(TAG, "onDateSet", "Error format date: " + e);
                }
            }
        }
    }

    /***
     * This method show the date picker dialog
     *
     * @param fm                is the actual fragment manager
     * @param mCallbackResponse is the Callback response
     ***/
    public static void showTimePickerDialog(FragmentManager fm, final ICallbackResponse<String> mCallbackResponse) {
        DialogFragment newFragment = new TimePickerFragment(mCallbackResponse);
        newFragment.show(fm, "datePicker");
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        private ICallbackResponse<String> mCallbackResponse;

        public TimePickerFragment() {
        }

        @SuppressLint("ValidFragment")
        public TimePickerFragment(ICallbackResponse<String> mCallbackResponse) {
            this.mCallbackResponse = mCallbackResponse;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            //Use the current time as the default values for the time picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            //Create and return a new instance of TimePickerDialog
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            if (mCallbackResponse != null) {
                try {
                    String mDate = hourOfDay + ":" + minute;
                    mCallbackResponse.onCallback(SDF1.format(SDF1.parse(mDate)));
                } catch (Exception e) {
                    LoggerUtils.logError(TAG, "onDateSet", "Error format date: " + e);
                }
            }
        }
    }

    /***
     * This method show a input default dialog
     *
     * @param mContext          is the context of the app
     * @param mTitle            is the title dialog
     * @param content           is the text dialog
     * @param hint              is the hint input text
     * @param prefill           is the prefill input text
     * @param mCallbackResponse is the callback response
     ***/
    public static void showInputDialog(Context mContext, String mTitle, String content, String hint, String prefill, final ICallbackResponse<CharSequence> mCallbackResponse) {
        dismissDialog();
        dialog = new MaterialDialog.Builder(mContext)
                .title(mTitle)
                .content(content)
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input(hint, prefill, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        if (mCallbackResponse != null)
                            mCallbackResponse.onCallback(input);
                    }
                })
                .titleColor(mContext.getResources().getColor(R.color.colorAccent))
                .backgroundColor(Color.WHITE)
                .contentColor(Color.BLACK)
                .positiveText(mContext.getString(R.string.label_continue))
                .negativeText(mContext.getString(R.string.label_cancel))
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dismissDialog();
                    }
                })
                .cancelable(false)
                .typeface(TypefaceHelper.getInstance().getTypeface(TypefaceHelper.FONT_OPEN_SANS),
                        TypefaceHelper.getInstance().getTypeface(TypefaceHelper.FONT_OPEN_SANS))
                .show();
    }

    /***
     * This method show a customView dialog
     *
     * @param mContext          is the context of the app
     * @param mTitle            is the title dialog
     * @param mView             is the custom view
     * @param mCallbackResponse is the callback response
     ***/
    public static void showCustomViewDialog(Context mContext, String mTitle, View mView, final ICallbackResponse<CharSequence> mCallbackResponse) {
        dismissDialog();
        dialog = new MaterialDialog.Builder(mContext)
                .title(mTitle)
                .customView(mView, true)
                .titleColor(mContext.getResources().getColor(R.color.colorAccent))
                .backgroundColor(Color.WHITE)
                .titleColor(Color.BLACK)
                .positiveText(mContext.getString(R.string.label_continue))
                .negativeText(mContext.getString(R.string.label_cancel))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (mCallbackResponse != null)
                            mCallbackResponse.onCallback(null);
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dismissDialog();
                    }
                })
                .cancelable(false)
                .typeface(TypefaceHelper.getInstance().getTypeface(TypefaceHelper.FONT_OPEN_SANS),
                        TypefaceHelper.getInstance().getTypeface(TypefaceHelper.FONT_OPEN_SANS))
                .show();
    }

    /***
     * This method show a Option Dialog
     *
     * @param mContext   is the context App
     * @param title      is the title dialog
     * @param arrayItems are the items to show
     * @param mCallback  is the callback to return id
     ***/
    public static void showOptionDialog(Context mContext, String title, String[] arrayItems, final ICallbackResponse<Integer> mCallback) {
        dismissDialog();
        new MaterialDialog.Builder(mContext)
                .title(title)
                .items(arrayItems)
                .titleColor(mContext.getResources().getColor(R.color.colorAccent))
                .backgroundColor(Color.WHITE)
                .contentColor(Color.BLACK)
                .cancelable(false)
                .typeface(TypefaceHelper.getInstance().getTypeface(TypefaceHelper.FONT_OPEN_SANS),
                        TypefaceHelper.getInstance().getTypeface(TypefaceHelper.FONT_OPEN_SANS))
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        LoggerUtils.logDebug(TAG, "showOptionDialog", "onSelection: " + which);
                        if (mCallback != null)
                            mCallback.onCallback(which);
                    }
                })
                .show();
    }
}
