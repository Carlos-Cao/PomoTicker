package com.example.pomoticker;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PomodoroTimerTest {

    private PomodoroTimer timer;

    @Before
    public void setUp() {
        timer = new PomodoroTimer();
    }

    @Test
    public void testInitialState() {
        assertTrue(timer.isWorkTimer());
        assertEquals(PomodoroTimer.WORK_TIME_IN_MILLIS, timer.getTimeLeftInMillis());
    }

    @Test
    public void testResetWork() {
        timer.reset();
        assertEquals(PomodoroTimer.WORK_TIME_IN_MILLIS, timer.getTimeLeftInMillis());
    }

    @Test
    public void testStartBreak() {
        timer.startBreak();
        assertFalse(timer.isWorkTimer());
        assertEquals(PomodoroTimer.BREAK_TIME_IN_MILLIS, timer.getTimeLeftInMillis());
    }

    @Test
    public void testSwitchToWork() {
        timer.startBreak();
        timer.switchToWork();
        assertTrue(timer.isWorkTimer());
        assertEquals(PomodoroTimer.WORK_TIME_IN_MILLIS, timer.getTimeLeftInMillis());
    }

    @Test
    public void testFormattedTime() {
        timer.setTimeLeftInMillis(60000);
        assertEquals("01:00", timer.getFormattedTime());
    }

    @Test
    public void testSessionLabel() {
        assertEquals("Work Session", timer.getSessionLabel());
        timer.startBreak();
        assertEquals("Break Session", timer.getSessionLabel());
    }
}