# ReduxKt

A Kotlin port of the [ReduxJS](https://redux.js.org/introduction/getting-started#basic-example) library.

## Usage

```kotlin 
// create a reducer
val counterReducer: Reducer<Int> = { state, action ->
    when (action) {
        is Increment -> state + 1
        is Decrement -> max(0, state - 1)
        is Reset -> 0
        else -> state
    }
}

// create a store
val store: Store<Int> by lazy { createStore(counterReducer, 0) }

// subscribe to state updates
store.subscribe { state -> println("State: $state") }

// dispatch actions
store.dispatch(Increment())

```

## Android Sample
You can find an example of using ReduxKt for Android in the [sample project](https://github.com/greghynds/redux-kt/tree/main/android-sample/src).

## Installing
ReduxKt modules are available on [JitPack](https://jitpack.io). To include in your project, declare a dependency in your `build.gradle`:

```gradle
dependencies {

    // redux store + core classes
    implementation 'com.github.greghynds.redux-kt:store:1.2.0'
    
    // an optional format for actions
    implementation 'com.github.greghynds.redux-kt:flux-standard-action:1.2.0' 
    
    // utilities for using redux in Android projects
    implementation 'com.github.greghynds.redux-kt:android-extensions:1.2.0' 
}
```

## Building
The project can be built by navigating to the root directory and running:

```./gradlew clean build```

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

## License
Apache License 2.0
