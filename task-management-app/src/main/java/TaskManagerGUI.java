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
import task.TaskCategory;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;

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
    private TaskCategory taskCategories;
    private TabPane categoryTabPane;
    private Map<String, ListView<Task>> categoryListViews;

    @Override
    public void start(Stage primaryStage) {
        taskQueue = new TaskQueue();
        taskItems = FXCollections.observableArrayList();
        taskHistory = new TaskHistory();
        taskCategories = new TaskCategory();
        categoryListViews = new HashMap<>();
        initializePomodoroTimer();

        TabPane mainTabPane = new TabPane();
        
        // Tasks tab (contains timer and category tabs)
        tasksTab = new Tab("Tasks");
        tasksTab.setClosable(false);
        VBox tasksBox = new VBox(10);
        tasksBox.setPadding(new Insets(10));
        tasksBox.setAlignment(Pos.TOP_CENTER);

        // Timer section
        timerLabel = new Label("25:00");
        timerLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        startPomodoroButton = new Button("Start Pomodoro");
        pauseResumeButton = new Button("Pause");
        HBox timerControls = new HBox(10, startPomodoroButton, pauseResumeButton);
        timerControls.setAlignment(Pos.CENTER);

        // Category section
        HBox categoryControls = new HBox(10);
        Button addTaskButton = new Button("Add Task");
        Button newCategoryButton = new Button("New Category");
        Button completeTaskButton = new Button("Complete Task"); // Changed from deleteCategoryButton
        categoryControls.getChildren().addAll(addTaskButton, newCategoryButton, completeTaskButton);
        categoryControls.setAlignment(Pos.CENTER);

        // Category tabs
        categoryTabPane = new TabPane();
        VBox.setVgrow(categoryTabPane, Priority.ALWAYS);

        tasksBox.getChildren().addAll(
            timerLabel,
            timerControls,
            categoryControls,
            categoryTabPane
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

        mainTabPane.getTabs().addAll(tasksTab, historyTab);

        setupEventHandlers(addTaskButton, newCategoryButton, completeTaskButton);

        Scene scene = new Scene(mainTabPane, 600, 800);
        primaryStage.setTitle("Task Manager");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void setupEventHandlers(Button addTaskButton, Button newCategoryButton, Button completeTaskButton) {
        startPomodoroButton.setOnAction(e -> pomodoroTimer.start());
        
        pauseResumeButton.setOnAction(e -> {
            if (pomodoroTimer.getCurrentState() == TimerState.WORKING) {
                pomodoroTimer.pause();
            } else if (pomodoroTimer.getCurrentState() == TimerState.PAUSED) {
                pomodoroTimer.start();
            }
        });

        newCategoryButton.setOnAction(e -> showNewCategoryDialog());
        
        // New complete task handler
        completeTaskButton.setOnAction(e -> {
            Tab selectedTab = categoryTabPane.getSelectionModel().getSelectedItem();
            if (selectedTab != null) {
                ListView<Task> listView = categoryListViews.get(selectedTab.getText());
                Task selectedTask = listView.getSelectionModel().getSelectedItem();
                
                if (selectedTask != null) {
                    selectedTask.markAsCompleted();
                    taskHistory.addCompletedTask(selectedTask);
                    
                    // Remove from category and update views
                    taskCategories.getTasksInCategory(selectedTab.getText()).remove(selectedTask);
                    updateHistoryView();
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("No Task Selected");
                    alert.setHeaderText("Please select a task to complete");
                    alert.showAndWait();
                }
            }
        });

        addTaskButton.setOnAction(e -> showAddTaskDialog());
    }

    private void showNewCategoryDialog() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("New Category");
        dialog.setHeaderText("Create a new category");
        dialog.setContentText("Category name:");

        dialog.showAndWait().ifPresent(name -> {
            if (!name.trim().isEmpty()) {
                taskCategories.addCategory(name);
                createCategoryTab(name);
            }
        });
    }

    private void createCategoryTab(String categoryName) {
        Tab categoryTab = new Tab(categoryName);
        categoryTab.setClosable(true); // Enable close button
        
        // Add close request handler
        categoryTab.setOnCloseRequest(event -> {
            event.consume(); // Prevent immediate closing
            showDeleteCategoryConfirmation(categoryName, categoryTab);
        });

        ListView<Task> categoryListView = new ListView<>();
        categoryListView.setItems(taskCategories.getTasksInCategory(categoryName));
        categoryListViews.put(categoryName, categoryListView);
        
        VBox categoryBox = new VBox(10);
        categoryBox.setPadding(new Insets(10));
        categoryBox.getChildren().add(categoryListView);
        
        categoryTab.setContent(categoryBox);
        categoryTabPane.getTabs().add(categoryTab);
    }

    private void showDeleteCategoryConfirmation(String categoryName, Tab tab) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Category");
        alert.setHeaderText("Delete category: " + categoryName);
        alert.setContentText("Are you sure? This will delete all tasks in this category.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            taskCategories.removeCategory(categoryName);
            categoryTabPane.getTabs().remove(tab);
            categoryListViews.remove(categoryName);
        }
    }

    private void showAddTaskDialog() {
        Tab selectedTab = categoryTabPane.getSelectionModel().getSelectedItem();
        if (selectedTab == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Category Selected");
            alert.setHeaderText("Please select or create a category first");
            alert.showAndWait();
            return;
        }

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
        
        // Replace category combo box with selected category
        String categoryName = selectedTab.getText();
        
        Button submitButton = new Button("Add Task");

        grid.add(new Label("Title:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Priority:"), 0, 1);
        grid.add(priorityCombo, 1, 1);
        grid.add(new Label("Due Date:"), 0, 2);
        grid.add(datePicker, 1, 2);
        grid.add(new Label("Category:"), 0, 3);
        grid.add(new Label(categoryName), 1, 3); // Display selected category
        grid.add(submitButton, 1, 4);

        // Update submit button action
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
                taskCategories.addTaskToCategory(categoryName, task);
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