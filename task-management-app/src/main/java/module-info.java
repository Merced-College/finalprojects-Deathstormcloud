module task.management.app {
    requires javafx.controls;
    requires javafx.fxml;
    
    exports task;
    exports timer;
    opens task to javafx.fxml;
    opens timer to javafx.fxml;
}