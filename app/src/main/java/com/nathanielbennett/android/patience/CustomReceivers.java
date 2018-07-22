package com.nathanielbennett.android.patience;

public class CustomReceivers {
    private static final String PACKAGE_NAME = PatienceWidget.class.getPackage().getName();
    public static final String ACTION_COUNTDOWN_TICK = PACKAGE_NAME + ".COUNTDOWN_TICK";
    public static final String ACTION_SECONDS_TICK = PACKAGE_NAME + ".SECONDS_TICK";
    public static final String ACTION_SETTINGS_CHANGED = PACKAGE_NAME + ".SETTINGS_CHANGED";
    public static final String ACTION_JOB_TICK = PACKAGE_NAME + ".JOB_CLOCK_TICK";
}
