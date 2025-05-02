# README.md

# Task Management Terminal Application

## Overview
The Task Management Terminal Application is a command-line tool designed to help users manage their tasks efficiently using the Pomodoro technique. This application allows users to create tasks, manage them in a queue, and utilize a Pomodoro timer to enhance productivity.

## Features
- Task creation and management
- Pomodoro timer functionality
- Queue handling for tasks
- Command-line interface for user interaction

## Project Structure
```
task-management-app
├── src
│   ├── main
│   │   └── java
│   │       ├── Main.java
│   │       ├── timer
│   │       │   ├── PomodoroTimer.java
│   │       │   └── TimerState.java
│   │       ├── task
│   │       │   ├── Task.java
│   │       │   └── TaskQueue.java
│   │       └── utils
│   │           └── ConsoleUtils.java
│   └── test
│       └── java
│           ├── timer
│           │   └── PomodoroTimerTest.java
│           └── task
│               └── TaskQueueTest.java
├── pom.xml
└── README.md
```

## Setup Instructions
1. Clone the repository:
   ```
   git clone <repository-url>
   ```
2. Navigate to the project directory:
   ```
   cd task-management-app
   ```
3. Build the project using Maven:
   ```
   mvn clean install
   ```

## Usage
To run the application, execute the following command:
```
java -cp target/task-management-app-1.0-SNAPSHOT.jar Main
```

Follow the on-screen instructions to manage your tasks and utilize the Pomodoro timer.