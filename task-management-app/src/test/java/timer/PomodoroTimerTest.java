// Unit tests for Pomodoro timer functionality
// Verifies timer operations and state management

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PomodoroTimerTest {
    private PomodoroTimer pomodoroTimer;

    @BeforeEach
    void setUp() {
        pomodoroTimer = new PomodoroTimer(new PomodoroTimer.TimerCallback() {
            @Override
            public void onTick(long remainingTime) {}
            @Override
            public void onStateChange(TimerState newState) {}
        });
    }

    @Test
    void testStartTimer() {
        pomodoroTimer.start();
        assertEquals(TimerState.WORKING, pomodoroTimer.getCurrentState());
    }

    @Test
    void testPauseTimer() {
        pomodoroTimer.start();
        pomodoroTimer.pause();
        assertEquals(TimerState.PAUSED, pomodoroTimer.getCurrentState());
    }

    @Test
    void testResumeTimer() {
        pomodoroTimer.start();
        pomodoroTimer.pause();
        pomodoroTimer.resume();
        assertEquals(TimerState.RUNNING, pomodoroTimer.getCurrentState());
    }
}