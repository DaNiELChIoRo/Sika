package com.muuyal.sika.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.muuyal.sika.R;
import com.muuyal.sika.customs.CustomTextView;
import com.muuyal.sika.utils.DialogUtils;

/**
 * Created by isra on 7/29/17.
 */

public class CalculatorFragment extends BaseFragment {

    public static final String TAG = CalculatorFragment.class.getSimpleName();

    private View rootView;
    private EditText etWidth;
    private EditText etHeight;
    private EditText etHigh;

    private CustomTextView tvAreaWall;
    private CustomTextView tvAreaFloor;
    private CustomTextView tvAreaTotal;

    private double areaWall = 0.0;
    private double areaFloor = 0.0;
    private double areaTotal = 0.0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = View.inflate(mContext, R.layout.fragment_calculator, null);

        etWidth = rootView.findViewById(R.id.et_width);
        etHeight = rootView.findViewById(R.id.et_height);
        etHigh = rootView.findViewById(R.id.et_hight);

        tvAreaWall = rootView.findViewById(R.id.tv_area_wall);
        tvAreaFloor = rootView.findViewById(R.id.tv_area_floor);
        tvAreaTotal = rootView.findViewById(R.id.tv_area_total);

        rootView.findViewById(R.id.btn_calculate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateFields()) {
                    calculate();
                }
            }
        });

        return rootView;
    }

    /***
     * This method validate fields to login
     ***/
    private boolean validateFields() {
        String emptyFields = "";
        if (TextUtils.isEmpty(etWidth.getText().toString()))
            emptyFields += "- " + getString(R.string.label_empty_width) + "\n";
        if (TextUtils.isEmpty(etHeight.getText().toString()))
            emptyFields += "- " + getString(R.string.label_empty_height) + "\n";
        if (TextUtils.isEmpty(etHigh.getText().toString()))
            emptyFields += "- " + getString(R.string.label_empty_high) + "\n";
        if (TextUtils.isEmpty(emptyFields)) {

            return true;
        } else {
            DialogUtils.showAlert(getActivity(),
                    getString(R.string.title_notification),
                    String.format(getString(R.string.message_verify_fields),
                            emptyFields));
        }

        return false;
    }


    /****
     *   Área de muros = ((ancho*altura)*2)+((largo*altura)*2)
     *   Área de piso = Largo * Área de muros
     *   Area total = Área de muros + Área de piso
     ****/
    private void calculate() {
        double width = Double.parseDouble(etWidth.getText().toString());
        double height = Double.parseDouble(etHeight.getText().toString());
        double high = Double.parseDouble(etHigh.getText().toString());

        areaWall = ((height * high) * 2) + ((width * high) * 2);
        areaFloor = high * areaWall;
        areaTotal = areaWall + areaFloor;;

        tvAreaWall.setText(String.valueOf(areaWall));
        tvAreaFloor.setText(String.valueOf(areaFloor));
        tvAreaTotal.setText(String.valueOf(areaTotal));
    }
}
