### Buut + Android - Group project

This project is an Android application developed in 100% Kotlin. It is developed using best practices, modern architecture and the Android recommended Jetpack Compose toolkit. It is functionally a mobile version of the .NET Blazor App found at the [buut-dotnet](https://github.com/Pyrrhusn/buut-rise-dotNet#) repo.

#### Some features (non-exhaustive)
- Built with Jetpack components (ViewModel, StateFlow, Navigation) for robust and reactive user interfaces.
- Offline-first approach; Room database 
- Dependency Injection
- Retrofit is used for API communication; DTOs map network responses to domain models.
- Coroutines and Flow for asynchronous programming

#### Running the application

1. Create or modify your `local.properties` file in the project root and add the following properties:

```env
PROD_BASE_URL=https:yourproductionurl.com/ 
DEV_BASE_URL=http://10.0.2.2:5000/
```

2. Sync gradle files and run MainActivity on an android emulator.

#### Testing

In project root run the following commands:

- Run unit tests: `./gradlew test`
- Run android tests:
  - Automatically launch emulator and run tests: `./gradlew connectedAndroidTest`
  - Run tests on already connected emulator (e.g. for github actions): `./gradlew connectedCheck`

#### Build

In project root:

- Build debug apk: `./gradlew assembleDebug`
