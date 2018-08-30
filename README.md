# OpenBack Android Sample App

This application is a simple project highlighting the integration of the OpenBack library into an Android application. It shows how to set custom trigger values using the OpenBack SDK. It also comes with a ready made OpenBack app code, which has already been setup with some simple campaigns reacting to the trigger values. The application is setup to use Firebase messaging for demonstration purposes.

> For the full Android integration guide, check out the [OpenBack Documentation](https://docs.openback.com/android/integration/).

## How the sample app was setup

The application `build.gradle` file was modified with the following:

```
repositories {
    maven { url 'https://maven.openback.com/public' }
}
   
dependencies {
    compile "com.openback:OpenBack:2.+"
}
 ```

An icon named `ic_notification_icon` was created and added to the application resources. This is the icon used for notifications on Android 5+.

A sound file named `ding` was added to the raw resources. This is the sound used when the notification is triggered.

A file named `openback.json` was added to the  **assets** folder with the following content:
 
``` json
{
    "appCode": "IYYTNHSYZA",
    "notification": {
        "icon_material": {
            "name": "ic_notification_icon",
            "type": "drawable",           
            "color": "#231F20"            
        },
        "light": {
            "color": "#FF4081",           
            "onMs": 500,                 
            "offMs": 1000                
        },
        "vibrate": {
            "pattern": [ 100, 200,  100, 200 ] 
        },
        "sound": {
            "name": "ding",               
            "type": "raw"                 
        }
    }
}
```

The [Application class](/app/src/main/java/com/openback/androidsampleapp/Application.java) was tweaked to setup OpenBack during its `onCreate()` call. Setting up OpenBack in the application class is preferred to a setup in the main activity as your application might be launched with a different activity.

Check the [Main activity](/app/src/main/java/com/openback/androidsampleapp/MainActivity.java) to see how the custom triggers are called. For example:

``` java
OpenBack.setCustomTrigger(context, OpenBack.CUSTOM_TRIGGER_1, "Bob");
``` 

Firebase was setup by following the official documentation found at https://firebase.google.com/docs/android/setup.  

### Notes on Firebase

For security reasons, the `google-services.json` file is not part of the sample app. If you want to
 test with firebase, follow the Firebase setup and add the json file in the app folder.
 Creating a new application and setting the server key is recommended. 