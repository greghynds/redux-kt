# ReduxKt    
A simple port of the [ReduxJS](https://redux.js.org/introduction/getting-started#basic-example) library for Android

## Overview
- Uses [RxJava](https://github.com/ReactiveX/RxJava) for pub/sub
- Works with [Flux Standard Action](https://github.com/redux-utilities/flux-standard-action) pattern
- Unsubscribes from store automatically when Activity destroyed
    
## Usage
```kotlin 
// create a reducer
val counterReducer: Reducer<Int> = { state, action ->
    when {
        action.isOfType("INCREMENT") -> state + 1
        action.isOfType("DECREMENT") -> max(0, state - 1)
        action.isOfType("RESET") -> 0
        else -> state
    }
}

// create a store
val store: Store<Int> by lazy { createStore(counterReducer, 0) }

// subscribe to state updates
store.updates.subscribe { state -> Log.d("ReduxKt", "State: $state")}

// dispatch actions
store.dispatch(Action("INCREMENT"))

```

## Sample 
You can find an example of using ReduxKt in the [sample project](https://github.com/greghynds/redux-kt/tree/main/sample/src). 

## Installing 
ReduxKt is available on [JitPack](https://jitpack.io). To include it in your project, add the following line to your `build.gradle`:

```gradle
dependencies {
    implementation 'com.github.greghynds:redux-kt:1.0.1'
}
```
  
## Building    
The project can be built by navigating to the root directory and running:    
    
```./gradlew clean build ```    

## Contributing  
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.    

## License
Apache License 2.0
