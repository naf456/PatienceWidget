package com.nathanielbennett.android.patience;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.jakewharton.threetenabp.AndroidThreeTen;
import com.nathanielbennett.android.patiencecomplexlengthoftimepicker.SegmentedDateTimeLengthPicker;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneId;


/**
 * The configuration screen for the {@link PatienceWidget PatienceWidget} AppWidget.
 */
public class PatienceWidgetConfigureActivity extends AppCompatActivity {

    int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    SegmentedDateTimeLengthPicker segmentedDateTimeLengthPicker;
    EditText countdownMessageEditText;

    public PatienceWidgetConfigureActivity() {
        super();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        AndroidThreeTen.init(this);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.activity_patience_configure);

        segmentedDateTimeLengthPicker = findViewById(R.id.timePicker);
        countdownMessageEditText = findViewById(R.id.input_countdown_message);

        findViewById(R.id.done_btn).setOnClickListener(onDoneClickListener);

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            appWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }
    }



    View.OnClickListener onDoneClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = PatienceWidgetConfigureActivity.this;

            String countdownMessage = countdownMessageEditText.getText().toString();

            LocalDateTime futureDateTime = segmentedDateTimeLengthPicker.getPickedDateTime();

            long futureEpoch = futureDateTime.atZone(ZoneId.systemDefault()).toEpochSecond();

            //Save our widget data
            PatienceDataManager.saveFutureEpoch(context, appWidgetId, futureEpoch);
            PatienceDataManager.saveWidgetMessage(context, appWidgetId, countdownMessage);

            // It is the responsibility of the configuration activity to update the app widget
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            PatienceWidget.updateAppWidget(context, appWidgetManager, appWidgetId);


            // Make sure we pass back the original appWidgetId
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };

    View.OnClickListener onPickFromCalender = new View.OnClickListener(){
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_PICK);
            intent.addCategory(Intent.CATEGORY_APP_CALENDAR);

            int requestCode = 1;

            startActivityForResult(intent, requestCode);
        }
    };
}

