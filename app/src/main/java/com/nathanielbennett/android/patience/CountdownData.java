package com.nathanielbennett.android.patience;

public class CountdownData {

    private boolean completed;
    private long yearsLeft;
    private long monthsLeft;
    private long weeksLeft;
    private long daysLeft;
    private long hoursLeft;
    private long minutesLeft;
    private long secondsLeft;

    public CountdownData(boolean completed, long hoursLeft, long minutesLeft, long secondsLeft) {
        this.completed = completed;
        this.hoursLeft = hoursLeft;
        this.minutesLeft = minutesLeft;
        this.secondsLeft = secondsLeft;
    }

    public long getYearsLeft() { return yearsLeft; }

    public long getMonthsLeft() { return monthsLeft; }

    public long getWeeksLeft() { return weeksLeft; }

    public long getDaysLeft() { return daysLeft; }

    public long getHoursLeft() {
        return hoursLeft;
    }

    public long getMinutesLeft() {
        return minutesLeft;
    }

    public long getSecondsLeft() {
        return secondsLeft;
    }

    public boolean isCompleted() { return completed; }
}
