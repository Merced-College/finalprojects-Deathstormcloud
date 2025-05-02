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
    }

    // Credit: Timer state management logic assisted by ClaudeAI
    public void start() {
        if (currentState == TimerState.IDLE || currentState == TimerState.PAUSED) {
            startWorkSession();
        }
    }

    // Credit: Work session implementation with callback handling by ClaudeAI
    private void startWorkSession() {
        currentState = TimerState.WORKING;
        callback.onStateChange(currentState);
        
        timer.scheduleAtFixedRate(new TimerTask() {
            private long timeRemaining = WORK_TIME;

            @Override
            public void run() {
                if (timeRemaining > 0) {
                    timeRemaining -= 1000;
                    callback.onTick(timeRemaining);
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
        callback.onStateChange(currentState);

        timer.scheduleAtFixedRate(new TimerTask() {
            private long timeRemaining = breakDuration;

            @Override
            public void run() {
                if (timeRemaining > 0) {
                    timeRemaining -= 1000;
                    callback.onTick(timeRemaining);
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