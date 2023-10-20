Summary

A simple weather app that gets your location or you search for location and displays the forecast for that day and a few days after that. Also it shows various other metrics like humidity, temperature, rain and precipitation.


*API :* 
1. [OpenWeatherMap](https://open-meteo.com/en/docs) - To get the metrics for weather. Also this is free and provides a lot of parameters for weather.
2. [Nomatim](https://nominatim.openstreetmap.org/) - To get the location co-ordinates(latitude/longitude) as per user search. This is also free to use.


Reason for choosing mentioned API :
1. Both the APIs are free to use for small projects.
2. Wide audience of apps use this api. Hence its reliable.
3. Flexible - Many more fields can be requested from the same response body by adding to it. Hence avoiding duplicate fields across response classes.

# Pre-requisite 📝

In your `local.properties` you will need to add your Open Weather API key and copy the urls in.

```properties
  Just clone the repo and run.
  Make sure to use latest Anroid studio and Android Gradle Version > 8.0.0
```

*Environment*
- Built on A.S Iguana
- Gradle Version > 8
- Kotlin version >= 1.9.0


# Technologies 🔨

**Language :** [Kotlin](https://github.com/JetBrains/kotlin)

**Libraries :**
  *UI*
- [Compose](https://developer.android.com/jetpack/compose)
- [Coil](https://coil-kt.github.io/coil/compose/https://coil-kt.github.io/coil/compose/) 
- [Landscapist](https://github.com/skydoves/landscapist) 

  *Data*
- [Ktor](https://square.github.io/retrofit/)
- [Gson](https://github.com/google/gson)
- [Preference Data Store](https://developer.android.com/topic/libraries/architecture/datastore)
- [Fused Location Provider](https://developers.google.com/location-context/fused-location-provider/)

   *Tooling/Project setup*
- [Gradle secrets plugin](https://github.com/google/secrets-gradle-plugin)
- [Hilt(DI)](https://developer.android.com/training/dependency-injection/hilt-android)


# Design/Architectural decisions 📐

The project follows common android patterns in modern android codebases. 
P.S - The modularization of this can result in better codebase structure.

**Project Structure**

The folders are split into 5 boundaries:
 - **Connection**:
   Contains the classes that are required for keeping an eye on mobile network connectivity. This in itself can be a separate module for identifying and observing device connectivity events.

 - **Core**:
   Contains the classes that are required for platform specific code. Like setting up network client, user Preferences. Basically this is the inner most layer of data.

 - **Data**:
   This package contains models, data sources, both local or remote and repositories as well. All data related actions and formatting happens in this layer as well.
   It also contains framework related dependencies to co-ordinate and create instances of data stores like a database or shared preference etc.
   The repository pattern, is used, which mediates data sources and acts as a source of truth to the consumer.

 - **UI**:

   This is the presentation layer of the app.
   This is further divided into 3 parts.
   1. common - UI that is common and can be used across app with minor changes. Eg:- Toolbar
   2. feature - each screen is considered to be a feature. Each feature has a UI, viewModel and a viewState which is observed my the UI.
   3. theme - Base theming of the application

   Each feature has a UI, viewmodel and a viewState which is observed my the UI. The UI just observes the state and reacts to the changes. It doesn't directly change the state. It request viewmodel to changes the state and react accordingly. Thus maitaining single source of truth. The state observed by UI is a stateFlow of an immutable type. Stateflow ensures that out app has the latest updated data.
   
   Some design patterns that can be seen here are the Observer pattern when consuming the flow -> state flows in the composables and provides a reactive app.

 - **Util**:
   Contains the extension functions for routine task like date formatting and SDK checks.


# Screenshots 📱

|                         Home(Celsius, Current Info, Hourly Forecast, 10 days Forecast)                          |
|:---------------------------------------------------------:|
| <img src="![Screenshot_2023-10-20-08-24-40-494_com example composedweather (1)](https://github.com/mahmood199/ComposedWeather/assets/58071934/cbc9f724-d6bb-4932-aeb3-08df3a41bc9a)
" width="200px"> |


|                         Home(Celsius, Current Info, 10 days Forecast)                          |
|:---------------------------------------------------------:|
| <img src="![Screenshot_2023-10-20-08-24-45-509_com example composedweather](https://github.com/mahmood199/ComposedWeather/assets/58071934/3ab0bf43-faeb-43b3-b500-21e785e0d1a7)
" width="300px"> |


|                         Home(Celsius, 10 days Forecast)                           |
|:---------------------------------------------------------:|
| <img src="![Screenshot_2023-10-20-08-24-49-885_com example composedweather](https://github.com/mahmood199/ComposedWeather/assets/58071934/7875f7ac-253b-4e76-a3e3-bed15e452210)
" width="300px"> |


|                         Search(With Search Result)                          |
|:---------------------------------------------------------:|
| <img src="![Screenshot_2023-10-20-08-25-24-296_com example composedweather](https://github.com/mahmood199/ComposedWeather/assets/58071934/095685ae-e527-4bd4-9787-e619bf9c10b4)" width="300px> |


|                         Recording of the App Flow                        |
|:---------------------------------------------------------:|
https://drive.google.com/file/d/11Q_j8K_mQI8fsHnHib5gTmrk3UzfIj9I/view


# Improvements 🚀
As for every other project there is always some room for improvement.
Additio of these were not possible due to time constraints.
Things that are worth adding
1. Modularization.
2. Testing.
3. Adding UseCase as an intermediate between viewModel and repository.
4. Add corresponding UI model class for each remote model class
5. Add Mappers for local and remote model classes.

# LICENSE

```
   Copyright 2023 Mahmood Ahmad

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
   
```

