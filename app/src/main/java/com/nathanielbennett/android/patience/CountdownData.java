package com.nathanielbennett.android.patience;

public class CountdownData {

    private String hoursLeft;
    private String minutesLeft;
    private String secondsLeft;

    public CountdownData(long hoursLeft, long minutesLeft, long secondsLeft) {
        this.hoursLeft = String.valueOf(hoursLeft);
        this.minutesLeft = String.valueOf(minutesLeft);
        this.secondsLeft = String.valueOf(secondsLeft);
    }

    public String getHoursLeft() {
        return hoursLeft;
    }

    public String getMinutesLeft() {
        return minutesLeft;
    }

    public String getSecondsLeft() {
        return secondsLeft;
    }
}
