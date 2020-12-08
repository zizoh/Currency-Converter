<h1 align="center">Currency Converter</h1>
<h4 align="center">
	Convert currencies and view historical foreign exchange of rates.
</h4>

## Summary
The app gets currency and historical foreign exchange rates from [Fixer](https://fixer.io/) API. It is built according to the `Model-View-Intent`(MVI) architecture. The UI was inspired by [Calculator](https://dribbble.com/shots/6647815-Calculator/attachments/6647815?mode=media) while its architecture by [Star Wars search](https://github.com/Ezike/StarWarsSearch). [Please check out](https://github.com/Ezike/StarWarsSearch/blob/master/process.md) the explanation it has on the whats and whys of MVI used in the project.

## Features
* Clean Architecture with MVI (Uni-directional data flow)
* Kotlin Coroutines with Flow
* Dagger Hilt
* Kotlin Gradle DSL

## Prerequisite
To build this project, you require:
- Android Studio 4.1 or higher
- Gradle 6.5
- Get an API key from [Fixer](https://fixer.io/) and assign it to "API_KEY" in [ApiService.kt](./libraries/remote/src/main/java/com/zizohanto/android/currencyconverter/remote/ApiService.kt)

## Note
The Fixer API's timeseries endpoint isn't supported on the free plan so as a (ridiculous) work-around, we can get data for every day within the 30 or 90 days period, concurrently.
To conserve API request volume, the historical data displayed in the charts are stubbed. You can change it by making "stubNetworkCall" in [ConverterRepositoryImpl](./libraries/data/src/main/java/com/zizohanto/android/currencyconverter/data/repository/ConverterRepositoryImpl.kt) to false.

## Libraries
- [Viewmodel](https://developer.android.com/topic/libraries/architecture/viewmodel)
- [FlowBinding](https://github.com/ReactiveCircus/FlowBinding)
- [Room](https://developer.android.com/training/data-storage/room)
- [Retrofit](https://square.github.io/retrofit/)  
- [Moshi](https://github.com/square/moshi)
- [okhttp-logging-interceptor](https://github.com/square/okhttp/blob/master/okhttp-logging-interceptor/README.md)
- [kotlinx.coroutines](https://github.com/Kotlin/kotlinx.coroutines)
- [Truth](https://truth.dev/)
- [MockWebServer](https://github.com/square/okhttp/tree/master/mockwebserver)
- [Robolectric](http://robolectric.org/)
- [Kotlin coroutines](https://github.com/Kotlin/kotlinx.coroutines)
- [Dagger Hilt](https://dagger.dev/hilt)
- [Kotlin Gradle DSL](https://guides.gradle.org/migrating-build-logic-from-groovy-to-kotlin)

## License
This project is released under the Apache License 2.0.
See [LICENSE](./LICENSE) for details.

```
Copyright 2020 Zizoh James Anto. All rights reserved.

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
