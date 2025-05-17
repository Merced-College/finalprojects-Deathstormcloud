import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.application.Platform;
import java.time.LocalDateTime;
import task.Task;
import task.TaskQueue;
import timer.PomodoroTimer;
import timer.TimerState;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Modality;
import javafx.geometry.Pos;
import task.TaskHistory;

public class TaskManagerGUI extends Application {
    private TaskQueue taskQueue;
    private PomodoroTimer pomodoroTimer;
    private Label timerLabel;
    private Button startPomodoroButton;
    private Button pauseResumeButton;
    private ListView<Task> taskListView;
    private ObservableList<Task> taskItems;
    private TaskHistory taskHistory;
    private ListView<Task> historyListView;
    private Tab tasksTab;
    private Tab historyTab;

    @Override
    public void start(Stage primaryStage) {
        taskQueue = new TaskQueue();
        taskItems = FXCollections.observableArrayList();
        taskHistory = new TaskHistory();
        initializePomodoroTimer();

        TabPane tabPane = new TabPane();
        
        // Tasks tab
        tasksTab = new Tab("Tasks");
        tasksTab.setClosable(false);
        VBox tasksBox = new VBox(10);
        tasksBox.setPadding(new Insets(10));
        tasksBox.setAlignment(Pos.TOP_CENTER);

        // Timer section with better spacing
        timerLabel = new Label("25:00");
        timerLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        startPomodoroButton = new Button("Start Pomodoro");
        pauseResumeButton = new Button("Pause");
        HBox timerControls = new HBox(10, startPomodoroButton, pauseResumeButton);
        timerControls.setAlignment(Pos.CENTER);

        // Task section
        Button addTaskButton = new Button("Add Task");
        taskListView = new ListView<>(taskItems);
        taskListView.setMaxHeight(Double.MAX_VALUE);
        VBox.setVgrow(taskListView, Priority.ALWAYS);

        // Add complete task button to task list
        Button completeTaskButton = new Button("Complete Selected Task");
        
        tasksBox.getChildren().addAll(
            timerLabel,
            timerControls,
            addTaskButton,
            taskListView,
            completeTaskButton
        );
        tasksTab.setContent(tasksBox);

        // History tab
        historyTab = new Tab("Completed Tasks");
        historyTab.setClosable(false);
        VBox historyBox = new VBox(10);
        historyBox.setPadding(new Insets(10));
        
        Label historyLabel = new Label("Completed Tasks History");
        historyLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        historyListView = new ListView<>();
        VBox.setVgrow(historyListView, Priority.ALWAYS);
        
        historyBox.getChildren().addAll(historyLabel, historyListView);
        historyTab.setContent(historyBox);

        // Add tabs to tab pane
        tabPane.getTabs().addAll(tasksTab, historyTab);

        setupEventHandlers(addTaskButton, completeTaskButton);

        Scene scene = new Scene(tabPane, 400, 600);
        primaryStage.setTitle("Task Manager");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void setupEventHandlers(Button addTaskButton, Button completeTaskButton) {
        startPomodoroButton.setOnAction(e -> pomodoroTimer.start());
        
        pauseResumeButton.setOnAction(e -> {
            if (pomodoroTimer.getCurrentState() == TimerState.WORKING) {
                pomodoroTimer.pause();
            } else if (pomodoroTimer.getCurrentState() == TimerState.PAUSED) {
                pomodoroTimer.start();
            }
        });

        addTaskButton.setOnAction(e -> showAddTaskDialog());

        // Set up complete task button handler
        completeTaskButton.setOnAction(e -> {
            Task selectedTask = taskListView.getSelectionModel().getSelectedItem();
            if (selectedTask != null) {
                selectedTask.markAsCompleted();
                taskItems.remove(selectedTask);
                taskHistory.addCompletedTask(selectedTask);
                updateHistoryView();
            }
        });
    }

    private void showAddTaskDialog() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Add New Task");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField titleField = new TextField();
        ComboBox<Integer> priorityCombo = new ComboBox<>(
            FXCollections.observableArrayList(1, 2, 3, 4, 5)
        );
        DatePicker datePicker = new DatePicker();
        Button submitButton = new Button("Add Task");

        grid.add(new Label("Title:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Priority:"), 0, 1);
        grid.add(priorityCombo, 1, 1);
        grid.add(new Label("Due Date:"), 0, 2);
        grid.add(datePicker, 1, 2);
        grid.add(submitButton, 1, 3);

        submitButton.setOnAction(e -> {
            if (!titleField.getText().isEmpty() && priorityCombo.getValue() != null) {
                LocalDateTime dueDate = datePicker.getValue() != null ? 
                    datePicker.getValue().atStartOfDay() : null;
                
                Task task = new Task(
                    titleField.getText(),
                    priorityCombo.getValue(),
                    dueDate,
                    false
                );
                
                taskQueue.enqueue(task);
                taskItems.add(task);
                dialog.close();
            }
        });

        Scene dialogScene = new Scene(grid);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    private void initializePomodoroTimer() {
        pomodoroTimer = new PomodoroTimer(new PomodoroTimer.TimerCallback() {
            @Override
            public void onTick(long remainingTime) {
                // Reduce UI updates to improve performance
                if (remainingTime % 1000 == 0) {
                    Platform.runLater(() -> {
                        long minutes = remainingTime / 60000;
                        long seconds = (remainingTime % 60000) / 1000;
                        timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
                    });
                }
            }

            @Override
            public void onStateChange(TimerState newState) {
                Platform.runLater(() -> updateTimerState(newState));
            }
        });
    }

    private void updateTimerState(TimerState newState) {
        switch (newState) {
            case WORKING:
                startPomodoroButton.setDisable(true);
                pauseResumeButton.setText("Pause");
                break;
            case PAUSED:
                startPomodoroButton.setDisable(true);
                pauseResumeButton.setText("Resume");
                break;
            case IDLE:
                startPomodoroButton.setDisable(false);
                pauseResumeButton.setText("Pause");
                break;
            default:
                break;
        }
    }

    // Add this method to update the history view
    private void updateHistoryView() {
        Task[] completedTasks = taskHistory.getCompletedTasks();
        ObservableList<Task> historyItems = FXCollections.observableArrayList(completedTasks);
        historyListView.setItems(historyItems);
    }

    public static void main(String[] args) {
        launch(args);
    }
}