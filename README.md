[![Open in Codespaces](https://classroom.github.com/assets/launch-codespace-2972f46106e565e64193e422d61a12cf1da4916b45550586e14ef0a7c637dd04.svg)](https://classroom.github.com/open-in-codespaces?assignment_repo_id=19399969)
# cpsc39-finalProjects


# README.md

# Task Management Terminal Application

## Overview
The Task Management Terminal Application is a command-line tool designed to help users manage their tasks efficiently using the Pomodoro technique. This application allows users to create tasks, manage them in a queue, and utilize a Pomodoro timer to enhance productivity.

## Features
- Task creation and management
- Pomodoro timer functionality
- Queue handling for tasks
- Command-line interface for user interaction

## Debug
# Navigate to source directory
cd C:\Users\rainl\OneDrive\Documents\GitHub\finalprojects-Deathstormcloud\task-management-app\src\main\java

# Clean existing class files
Get-ChildItem -Path ../../../target/classes -Include *.class -Recurse | Remove-Item -Force

# Compile
javac --module-path "D:/Downloads/javafx-sdk-17.0.9/lib" --add-modules javafx.controls,javafx.fxml -d ../../../target/classes module-info.java task/*.java timer/*.java *.java

# Run
java --module-path "D:/Downloads/javafx-sdk-17.0.9/lib" --add-modules javafx.controls,javafx.fxml -cp ../../../target/classes TaskManagerGUI

## Assignment requirements

 Data Structures Usage:

 STRINGS:
 - Main.java: Used throughout for user input and messages
 - Task.java: Used in title field (line 8)
 - ConsoleUtils.java: String manipulation for I/O operations

 QUEUES:
 - TaskQueue.java: Uses LinkedList as Queue implementation (line 8)
   private Queue<Task> taskQueue;

 LINKED LISTS:
 - TaskQueue.java: Underlying implementation of Queue (line 11)
   this.taskQueue = new LinkedList<>();

 Not Yet Implemented Data Structures That Could Be Added:
 ARRAYS: Could be added for storing completed tasks history
 HASH TABLES: Could be added for category-based task organization
 STACKS: Could be added for undo/redo functionality
 RECORDS: Could be used to create immutable task snapshots