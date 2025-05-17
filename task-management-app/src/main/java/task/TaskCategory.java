package task;

import java.util.HashMap;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TaskCategory {
    private HashMap<String, ObservableList<Task>> categories;

    public TaskCategory() {
        this.categories = new HashMap<>();
    }

    public void addCategory(String categoryName) {
        if (!categories.containsKey(categoryName)) {
            categories.put(categoryName, FXCollections.observableArrayList());
        }
    }

    public void addTaskToCategory(String category, Task task) {
        if (!categories.containsKey(category)) {
            addCategory(category);
        }
        categories.get(category).add(task);
    }

    public ObservableList<Task> getTasksInCategory(String category) {
        return categories.get(category);
    }

    public ObservableList<String> getAllCategories() {
        return FXCollections.observableArrayList(categories.keySet());
    }

    public void removeCategory(String categoryName) {
        categories.remove(categoryName);
    }
}