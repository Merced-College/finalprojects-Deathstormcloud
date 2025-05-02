// Unit tests for Pomodoro timer functionality
// Verifies timer operations and state management

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PomodoroTimerTest {
    private PomodoroTimer pomodoroTimer;

    @BeforeEach
    void setUp() {
        pomodoroTimer = new PomodoroTimer();
    }

    @Test
    void testStartTimer() {
        pomodoroTimer.start();
        assertEquals(TimerState.RUNNING, pomodoroTimer.getState());
    }

    @Test
    void testStopTimer() {
        pomodoroTimer.start();
        pomodoroTimer.stop();
        assertEquals(TimerState.STOPPED, pomodoroTimer.getState());
    }

    @Test
    void testPauseTimer() {
        pomodoroTimer.start();
        pomodoroTimer.pause();
        assertEquals(TimerState.PAUSED, pomodoroTimer.getState());
    }

    @Test
    void testResumeTimer() {
        pomodoroTimer.start();
        pomodoroTimer.pause();
        pomodoroTimer.resume();
        assertEquals(TimerState.RUNNING, pomodoroTimer.getState());
    }

    @Test
    void testTimerDuration() {
        pomodoroTimer.setDuration(25); // Set duration to 25 minutes
        assertEquals(25, pomodoroTimer.getDuration());
    }
}