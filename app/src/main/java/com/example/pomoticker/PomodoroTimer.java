package com.example.pomoticker;

import java.util.Locale;

public class PomodoroTimer {

    public static final long WORK_TIME_IN_MILLIS = 1500000; // 25 minutes

    public static final long BREAK_TIME_IN_MILLIS = 300000; // 5 minutes

    private boolean isWorkTimer = true;

    private long timeLeftInMillis = WORK_TIME_IN_MILLIS;

    public void reset() {
        timeLeftInMillis = isWorkTimer ? WORK_TIME_IN_MILLIS : BREAK_TIME_IN_MILLIS;
    }

    public void startBreak() {
        isWorkTimer = false;
        timeLeftInMillis = BREAK_TIME_IN_MILLIS;
    }

    public void switchToWork() {
        isWorkTimer = true;
        timeLeftInMillis = WORK_TIME_IN_MILLIS;
    }

    public String getFormattedTime() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
    }

    public String getSessionLabel() {
        return isWorkTimer ? "Work Session" : "Break Session";
    }

    public boolean isWorkTimer() {
        return isWorkTimer;
    }

    public long getTimeLeftInMillis() {
        return timeLeftInMillis;
    }

    public void setTimeLeftInMillis(long millis) {
        this.timeLeftInMillis = millis;
    }
}