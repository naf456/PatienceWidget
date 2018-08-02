package com.nathanielbennett.android.patiencecomplexlengthoftimepicker;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.jakewharton.threetenabp.AndroidThreeTen;

import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;

public class SegmentedDateTimeLengthPicker extends FrameLayout {

    private static final int MAX_DATETIME_SELECTORS = 3;

    private LinearLayout mDateTimeSelectorContainer;
    private ImageButton mAddSelectorSegmentButton;
    private ArrayList<DateTimeSelectorSegment> mDateTimeSelectorSegments = new ArrayList<>();

    public SegmentedDateTimeLengthPicker(Context context) {
        super(context);
        initialize();
    }

    public SegmentedDateTimeLengthPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public SegmentedDateTimeLengthPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    private void initialize(){
        AndroidThreeTen.init(getContext());
        inflate(getContext(), R.layout.segmented_datetime_length_picker, this);
        mDateTimeSelectorContainer = findViewById(R.id.dateTimeSelector_container);
        mAddSelectorSegmentButton = findViewById(R.id.add__dateTimeSelector);
        mAddSelectorSegmentButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                addDateTimeSelectorSegment();
                DateTimeSelectorSegment lastAddedSegment = mDateTimeSelectorSegments.get(mDateTimeSelectorSegments.size() -1);
                addDateTimeSelectorSegment_remover(lastAddedSegment);
            }
        });
        addInitialDateTimeSelectorSegment();
    }

    private void addInitialDateTimeSelectorSegment(){

        DateTimeSelectorSegment selectorSegment = new DateTimeSelectorSegment(getContext());
        int selectorSegment_width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int selectorSegment_height =LinearLayout.LayoutParams.MATCH_PARENT;
        DateTimeSelectorSegment.LayoutParams params =
                new LinearLayout.LayoutParams(selectorSegment_width, selectorSegment_height);

        params.rightMargin = Math.round(12 * getContext().getResources().getDisplayMetrics().density);

        selectorSegment.setLayoutParams(params);

        selectorSegment.setCurrentUnit(DateTimeSelectorSegment.UNIT_MINUTES);

        selectorSegment.setOnUnitChangedListener(mOnSegmentUnitChangeListener);

        mDateTimeSelectorSegments.add(selectorSegment);

        mDateTimeSelectorContainer.addView(selectorSegment);
    }

    private void addDateTimeSelectorSegment(){
        DateTimeSelectorSegment selectorSegment = new DateTimeSelectorSegment(getContext());
        int selectorSegment_width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int selectorSegment_height = LinearLayout.LayoutParams.MATCH_PARENT;
        DateTimeSelectorSegment.LayoutParams params =
                new LinearLayout.LayoutParams(selectorSegment_width, selectorSegment_height);

        //To provide visual separation from the segments
        params.leftMargin = Math.round(8 * getContext().getResources().getDisplayMetrics().density);

        selectorSegment.setLayoutParams(params);

        selectorSegment.setCurrentUnit(DateTimeSelectorSegment.UNIT_MINUTES);

        selectorSegment.setOnUnitChangedListener(mOnSegmentUnitChangeListener);

        mDateTimeSelectorSegments.add(selectorSegment);

        mDateTimeSelectorContainer.addView(selectorSegment);

        if(mDateTimeSelectorSegments.size() == MAX_DATETIME_SELECTORS){
            showAddSelectionSection(false);
        }
    }

    private void addDateTimeSelectorSegment_remover(DateTimeSelectorSegment segment){

        ViewRemoverButton viewRemoverButton = new ViewRemoverButton(getContext());
        int viewRemoverButton_width = LayoutParams.WRAP_CONTENT;
        int viewRemoverButton_height = LayoutParams.MATCH_PARENT;
        ViewRemoverButton.LayoutParams params =
                new LayoutParams(viewRemoverButton_width, viewRemoverButton_height);

        viewRemoverButton.setLayoutParams(params);

        viewRemoverButton.setViewToRemove(segment);
        viewRemoverButton.setOnViewRemovedListener(new ViewRemoverButton.OnViewRemovedListener(){
            @Override
            void onRemoved(View viewDestroyed) {
                DateTimeSelectorSegment segment = (DateTimeSelectorSegment) viewDestroyed;
                mDateTimeSelectorSegments.remove(segment);
                if(mDateTimeSelectorSegments.size() <= MAX_DATETIME_SELECTORS) showAddSelectionSection(true);
            }
        });
        viewRemoverButton.setRemoveSelf(true);

        mDateTimeSelectorContainer.addView(viewRemoverButton);
    }

    private void showAddSelectionSection(boolean show){
        if(show){
            mAddSelectorSegmentButton.setVisibility(View.VISIBLE);
        } else {
            mAddSelectorSegmentButton.setVisibility(View.GONE);
        }
    }

    public LocalDateTime getPickedDateTime() {

        LocalDateTime dateTime = LocalDateTime.now();

        for(DateTimeSelectorSegment section : mDateTimeSelectorSegments) {

            int sectionQuantity = section.getQuantity();

            if(sectionQuantity > 0) {

                switch (section.getUnit()) {
                    case DateTimeSelectorSegment.UNIT_YEARS:
                        dateTime = dateTime.plusYears(sectionQuantity);
                        break;
                    case DateTimeSelectorSegment.UNIT_MONTHS:
                        dateTime = dateTime.plusMonths(sectionQuantity);
                        break;
                    case DateTimeSelectorSegment.UNIT_WEEKS:
                        dateTime = dateTime.plusWeeks(sectionQuantity);
                        break;
                    case DateTimeSelectorSegment.UNIT_DAYS:
                        dateTime = dateTime.plusDays(sectionQuantity);
                        break;
                    case DateTimeSelectorSegment.UNIT_HOURS:
                        dateTime = dateTime.plusHours(sectionQuantity);
                        break;
                    case DateTimeSelectorSegment.UNIT_MINUTES:
                        dateTime = dateTime.plusMinutes(sectionQuantity);
                        break;
                    case DateTimeSelectorSegment.UNIT_SECONDS:
                        dateTime = dateTime.plusSeconds(sectionQuantity);
                        break;
                }
            }
        }

        return dateTime;
    }

    private DateTimeSelectorSegment.OnUnitChangedListener mOnSegmentUnitChangeListener =
            new DateTimeSelectorSegment.OnUnitChangedListener() {
        @Override
        public void onUnitChange() {
            //TODO: Fix this shit
            //clampUnitValues();
        }
    };

    private void clampUnitValues(){
        String lastUnit = null;
        for(DateTimeSelectorSegment segment: mDateTimeSelectorSegments) {
            if(lastUnit == null) {
                lastUnit = segment.getUnit();
                continue;
            }
            segment.setAvailableUnitsBelow(lastUnit);
            lastUnit = segment.getUnit();
        }

    }
}
