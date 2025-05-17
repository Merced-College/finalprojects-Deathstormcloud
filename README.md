[![Open in Codespaces](https://classroom.github.com/assets/launch-codespace-2972f46106e565e64193e422d61a12cf1da4916b45550586e14ef0a7c637dd04.svg)](https://classroom.github.com/open-in-codespaces?assignment_repo_id=19399969)
# cpsc39-finalProjects
# Gongyu Yan
# Task Management Application with Pomodoro Timer Report

## Application Overview
The Task Management Application is a JavaFX-based productivity tool that combines task organization with the Pomodoro technique. Users can create tasks, categorize them, track completion, and use an integrated Pomodoro timer for focused work sessions. The application provides a visual interface for managing tasks across different categories while maintaining task history.

## Key Algorithms

### 1. Task Category Management Algorithm
```java
// Hash table implementation for O(1) category access
private void addTaskToCategory(String category, Task task) {
    if (!categories.containsKey(category)) {
        categories.put(category, new ArrayList<>());
    }
    categories.get(category).add(task);
}
```
**Big O Analysis**: O(1) average case for category lookup and task addition

### 2. Task History Tracking Algorithm
```java
// Circular array implementation for task history
public void addCompletedTask(Task task) {
    if (currentIndex >= MAX_HISTORY) {
        System.arraycopy(completedTasks, 1, completedTasks, 0, MAX_HISTORY - 1);
        currentIndex = MAX_HISTORY - 1;
    }
    completedTasks[currentIndex++] = task;
}
```
**Big O Analysis**: O(n) for array shifting, where n is MAX_HISTORY

### 3. Pomodoro Timer State Management Algorithm
```java
// State machine implementation for timer control
public void handleTimerState(TimerState newState) {
    switch (currentState) {
        case WORKING:
            if (remainingTime <= 0) {
                startBreak();
            }
            break;
        case ON_BREAK:
            if (remainingTime <= 0) {
                completedPomodoros++;
                resetTimer();
            }
            break;
    }
}
```
**Big O Analysis**: O(1) for state transitions

## Data Structures Used

1. **HashMap (Categories)**
   - Purpose: Quick category lookup and task association
   - Why: O(1) access time for category operations
   - Implementation: `private HashMap<String, ObservableList<Task>> categories`

2. **Queue (Task Management)**
   - Purpose: FIFO task processing
   - Why: Natural fit for task order maintenance
   - Implementation: `private Queue<Task> taskQueue = new LinkedList<>()`

3. **Array (Task History)**
   - Purpose: Fixed-size history tracking
   - Why: Efficient memory use for limited history
   - Implementation: `private Task[] completedTasks`

## Development Process

### Opportunities Encountered
When switching from the terminal to a more user friendly GUI interface, I added the catagory system for better orginization and a date selector to save time on typing due dates this made the application more intuitive and user-friendly.

### Error Resolution
Initially encountered performance issues with timer updates causing UI lag. Resolved by:
1. Reducing update frequency
2. Using Platform.runLater() for JavaFX thread safety
3. Implementing more efficient state management

## Future Improvements
1. Add task priority sorting within categories
2. Implement data persistence using file storage
3. Add task statistics and productivity metrics
4. Include customizable timer intervals
5. Add support for recurring tasks
6. Implement task dependencies
7. Add multi-user support with cloud sync

## Technical Implementation
The application uses JavaFX for the GUI, with modular design principles separating concerns between task management, timer functionality, and user interface. The code follows object-oriented practices with clear class responsibilities and minimal coupling.

## This application was made using AI help, cases from ai generated code is the gui system along with code related to the pomodoro timer as well as StarApp.bat was AI implimented, however the rest of the code was hand typed with very minor ai autofill help to speed up typing. 
- citing helpful resources and videos I used 
1. https://www.youtube.com/watch?v=a8ZFHHwPIY0&ab_channel=jackFmyers
2. https://www.youtube.com/watch?v=NYGHL8N6Kc8&pp=0gcJCY0JAYcqIYzv
3. https://www.youtube.com/watch?v=4gnLTg8e3xs
4. https://www.youtube.com/watch?v=_Px3DcV1I0Y&pp=ygUpaG93IHRvIGltcGxlbWVudCBhIHBvbW9kb3JvIHRpbWVyIGluIGphdmE%3D
5. https://www.youtube.com/watch?v=5G2XM1nlX5Q&pp=ygUoaG93IHRvIG1ha2UgeW91ciBvd24gamF2YSBndWkgaW4gdnMgY29kZQ%3D%3D