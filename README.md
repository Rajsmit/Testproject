# Weather APP

Weather APP is a Android Application which shows the Current Weather Report as well as 5 days weather forcast.

# Getting Started

## Import Project to Android Studio
  Select File-> Open project -> Select build.gradle file inside Weather Directory.

### Prerequisites

To Develope/Contribute following tools is required
  1. Android studio 2.2.2
  2. Gradle 2.14.1
  3. Android sdk.
  4. Android Emulator or Android phone with Developer option enabled
  
  To Enable the developer option in Android Phone Go to Settings -> About phone -> Press 7 times Build number to enable developer Option
  
###Building Android APK.
 Command to run to build apk in debug and release mode
 
 >gradle assembleDebug
 
 >gradle assembleRelease.
 
 Build command will create apk inside Directory ..\Weather\app\build\outputs\apk


### Installing

####Using Android Studio Ide
  Select Run -> Run APP to install and run application from Android Studio after importing. 
####using command line 
  adb install app-release.apk
  

### Running the tests

To run the test from command line 
  ./gradlew connectedAndroidTest
    This will generate the Test Report @ directory Weather\app\build\reports

## Authors

* **Rajesh kumar** - *Initial work* - [Rajsmit](https://github.com/Rajsmit)

## License

This project is licensed under the Test License.

## Third party library:
1. Gson 2.8.0 ( com.google.code.gson:gson:2.8.0 )
2. Volley 2015.05.28 ( eu.the4thfloor.volley:com.android.volley:2015.05.28 )

##Future Enhancement

1. Use GPS to show current location weather info
2. show full day temperature variation.
3. city selector to change the city.
4. use recycler view/list view for forcast layout.
5. localization and fetch localized weather data.
6. show days based on timezone of selected country 
7. Cutome Weatther icons as compared to current openweathermap icon.

