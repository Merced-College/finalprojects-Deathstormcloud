package timer;

import java.util.Timer;
import java.util.TimerTask;

/**
 * PomodoroTimer class implements the Pomodoro Technique timer functionality
 * Credit: Core timer implementation and state management assisted by ClaudeAI
 * Implementation of Pomodoro timer with queue handling
 * Handles work/break sessions and maintains timer states
 */
public class PomodoroTimer {
    // Standard Pomodoro technique intervals in milliseconds
    private static final long WORK_TIME = 25 * 60 * 1000;    // 25 minutes
    private static final long SHORT_BREAK = 5 * 60 * 1000;   // 5 minutes
    private static final long LONG_BREAK = 15 * 60 * 1000;   // 15 minutes

    private Timer timer;
    private TimerState currentState;
    private int completedPomodoros;
    private TimerCallback callback;
    private long remainingTime; // Add this field
    private boolean isWorkSession; // Add this to track session type

    // Interface for timer state callbacks
    public interface TimerCallback {
        void onTick(long remainingTime);
        void onStateChange(TimerState newState);
    }

    public PomodoroTimer(TimerCallback callback) {
        this.timer = new Timer();
        this.currentState = TimerState.IDLE;
        this.completedPomodoros = 0;
        this.callback = callback;
        this.remainingTime = WORK_TIME; // Initialize with work time
        this.isWorkSession = true;
    }

    // Credit: Timer state management logic assisted by ClaudeAI
    public void start() {
        if (currentState == TimerState.IDLE) {
            remainingTime = WORK_TIME;
            isWorkSession = true;
            startWorkSession();
        } else if (currentState == TimerState.PAUSED) {
            resumeSession(); // New method to handle resume
        }
    }

    private void resumeSession() {
        currentState = isWorkSession ? TimerState.WORKING : TimerState.ON_BREAK;
        callback.onStateChange(currentState);
        
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (remainingTime > 0) {
                    remainingTime -= 1000;
                    callback.onTick(remainingTime);
                } else {
                    this.cancel();
                    if (isWorkSession) {
                        handleWorkSessionComplete();
                    } else {
                        currentState = TimerState.IDLE;
                        callback.onStateChange(currentState);
                    }
                }
            }
        }, 0, 1000);
    }

    // Credit: Work session implementation with callback handling by ClaudeAI
    private void startWorkSession() {
        currentState = TimerState.WORKING;
        isWorkSession = true;
        remainingTime = WORK_TIME;
        callback.onStateChange(currentState);
        
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (remainingTime > 0) {
                    remainingTime -= 1000;
                    callback.onTick(remainingTime);
                } else {
                    this.cancel();
                    handleWorkSessionComplete();
                }
            }
        }, 0, 1000);
    }

    // Credit: Session completion logic and state transitions by ClaudeAI
    private void handleWorkSessionComplete() {
        completedPomodoros++;
        if (completedPomodoros % 4 == 0) {
            startLongBreak();
        } else {
            startShortBreak();
        }
    }

    private void startShortBreak() {
        startBreak(SHORT_BREAK);
    }

    private void startLongBreak() {
        startBreak(LONG_BREAK);
    }

    // Credit: Break timer implementation with callback integration by ClaudeAI 
    private void startBreak(long breakDuration) {
        currentState = TimerState.ON_BREAK;
        isWorkSession = false;
        remainingTime = breakDuration;
        callback.onStateChange(currentState);

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (remainingTime > 0) {
                    remainingTime -= 1000;
                    callback.onTick(remainingTime);
                } else {
                    this.cancel();
                    currentState = TimerState.IDLE;
                    callback.onStateChange(currentState);
                }
            }
        }, 0, 1000);
    }

    public void pause() {
        if (currentState == TimerState.WORKING || currentState == TimerState.ON_BREAK) {
            timer.cancel();
            timer = new Timer();
            currentState = TimerState.PAUSED;
            // remainingTime is preserved here
            callback.onStateChange(currentState);
        }
    }

    public void reset() {
        timer.cancel();
        timer = new Timer();
        currentState = TimerState.IDLE;
        completedPomodoros = 0;
        callback.onStateChange(currentState);
    }

    public TimerState getCurrentState() {
        return currentState;
    }

    public int getCompletedPomodoros() {
        return completedPomodoros;
    }
}