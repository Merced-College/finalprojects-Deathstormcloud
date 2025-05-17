@echo off
cd src\main\java

:: Clean previous build
rmdir /s /q "..\..\..\target\classes" 2>nul
mkdir "..\..\..\target\classes"

:: Compile
javac --module-path "D:/Downloads/javafx-sdk-17.0.9/lib" --add-modules javafx.controls,javafx.fxml -d ../../../target/classes module-info.java task/*.java timer/*.java *.java

:: Run
java --module-path "D:/Downloads/javafx-sdk-17.0.9/lib" --add-modules javafx.controls,javafx.fxml -cp ../../../target/classes TaskManagerGUI

pause