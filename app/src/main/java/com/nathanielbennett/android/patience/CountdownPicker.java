package com.nathanielbennett.android.patience;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

public class CountdownPicker extends LinearLayout {

    private static final int UNIVESRAL_SELECTOR_MIN = 0;
    private static final int SECONDS_SELECTOR_MAX = 60;
    private static final int MINUTES_SELECTOR_MAX = 60;
    private static final int HOURS_SELECTOR_MAX = 60;



    public CountdownPicker(Context context) {
        super(context);
    }

    public CountdownPicker(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CountdownPicker(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(){

        View yearsSelector = buildDigitPicker(0,0);

    }

    private View buildDigitPicker(int minDigit, int maxDigit){

        NumberPicker digitSelector = new NumberPicker(getContext());
        NumberPicker.LayoutParams digitSelector_layout =
                new NumberPicker.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        digitSelector.setLayoutParams(digitSelector_layout);
        digitSelector.setMinValue(minDigit);
        digitSelector.setMaxValue(maxDigit);

        return digitSelector;
    }

    private View buildDigitPickerLabel(int labelRes) {
        TextView digitLabel = new TextView(getContext());
        ViewGroup.LayoutParams digitLabel_layout =
                new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        digitLabel.setLayoutParams(digitLabel_layout);
        if(labelRes != 0) digitLabel.setText(labelRes);

        return digitLabel;
    }
}
