package com.nathanielbennett.android.patiencecomplexlengthoftimepicker;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import java.util.Arrays;
import java.util.List;

public class DateTimeSelectorSegment extends LinearLayout {

    protected static final int MIN_DATETIME_QUANTITY = 1;
    protected static final int MAX_DATETIME_QUANTITY = 99;
    public static final String UNIT_YEARS = "Years";
    public static final String UNIT_MONTHS = "Months";
    public static final String UNIT_WEEKS = "Weeks";
    public static final String UNIT_DAYS = "Days";
    public static final String UNIT_HOURS = "Hours";
    public static final String UNIT_MINUTES = "Minutes";
    public static final String UNIT_SECONDS = "Seconds";

    private final String[] mAllAvailableUnits = {UNIT_YEARS, UNIT_MONTHS, UNIT_WEEKS, UNIT_DAYS, UNIT_HOURS,
            UNIT_MINUTES, UNIT_SECONDS};

    private String[] mCurrentAvailableUnits = mAllAvailableUnits;

    private NumberPicker mQuantityPicker;
    private NumberPicker mUnitPicker;

    private OnUnitChangedListener mOnUnitChangedListener;

    public DateTimeSelectorSegment(Context context) {
        super(context);
        initialize();
    }

    public DateTimeSelectorSegment(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public DateTimeSelectorSegment(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    private void initialize(){
        setOrientation(LinearLayout.HORIZONTAL);

        inflate(getContext(), R.layout.datetime_selector_segment, this);

        mQuantityPicker = findViewById(R.id.quantityPicker);
        mQuantityPicker.setMinValue(MIN_DATETIME_QUANTITY);
        mQuantityPicker.setMaxValue(MAX_DATETIME_QUANTITY);
        mQuantityPicker.setWrapSelectorWheel(false);

        mUnitPicker = findViewById(R.id.unitPicker);
        mUnitPicker.setMinValue(0);
        mUnitPicker.setMaxValue(mCurrentAvailableUnits.length - 1);
        mUnitPicker.setDisplayedValues(mCurrentAvailableUnits);
        mUnitPicker.setWrapSelectorWheel(false);
        mUnitPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                if(mOnUnitChangedListener != null) {
                    mOnUnitChangedListener.onUnitChange();
                }
            }
        });
    }

    public void setCurrentUnit(String UNIT){
        List unitList = Arrays.asList(mCurrentAvailableUnits);
        if(unitList.contains(UNIT)){
            mUnitPicker.setValue(unitList.indexOf(UNIT));
        }
    }

    public int getQuantity() {
        return mQuantityPicker.getValue();
    }

    public String getUnit(){
        int index = mUnitPicker.getValue();
        return mCurrentAvailableUnits[index];
    }

    public void setAvailableUnitsBelow(String unit) {
        int unitIndex = Arrays.binarySearch(mAllAvailableUnits, unit);
        String[] availableUnits = Arrays.copyOfRange(mAllAvailableUnits, unitIndex + 1, mAllAvailableUnits.length - 1);
        setAvailableUnits(availableUnits);
    }

    public void setAvailableUnits(String[] units){
        mCurrentAvailableUnits = units;
        mUnitPicker.setMaxValue(mCurrentAvailableUnits.length - 1);
        mUnitPicker.setDisplayedValues(mCurrentAvailableUnits);
    }

    public void setOnUnitChangedListener(OnUnitChangedListener onUnitChangedListener) {
        this.mOnUnitChangedListener = onUnitChangedListener;
    }

    public static abstract class OnUnitChangedListener implements NumberPicker.OnValueChangeListener {

        public abstract void onUnitChange();

        @Override
        public void onValueChange(NumberPicker numberPicker, int i, int i1) {
            onUnitChange();
        }
    }


}
