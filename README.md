# image-slider-app

This application was developed in Android Studio. The Android Studio Package folder can be immediately imported into Android Studio and run. The application downloads images and rotates them asynchronously from pre-set URLs.

Android’s threading model uses AsyncTasks to do background processing. They have convenient methods that allow the interaction with the UI thread before, during, and after the process is complete. These tasks are tied to the Activity's lifecycle. Android is an event driven system. The main UI thread loops indefinitely waiting for a new event or user input. When something is received, it is processed. 
