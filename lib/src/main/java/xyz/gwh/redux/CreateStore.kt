package xyz.gwh.redux

import androidx.lifecycle.Lifecycle.State.DESTROYED
import androidx.lifecycle.Lifecycle.State.STARTED
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.Observable

/**
 * See Redux.createStore()
 */
fun <State> createStore(
    reducer: Reducer<State>,
    initialState: State,
    vararg middlewares: Middleware<State>
): Store<State> {
    return with(Redux<State>()) {
        createStore(reducer, initialState, applyMiddleware(middlewares.asList()))
    }
}


/**
 * Creates a store scoped to the Activity lifecycle.
 *
 * The store will not publish updates before the Activity has started
 * or after the Activity has been destroyed.
 */
fun <S> AppCompatActivity.createStore(
    reducer: Reducer<S>,
    initialState: S,
    vararg middlewares: Middleware<S>
): Store<S> {
    val store = xyz.gwh.redux.createStore(reducer, initialState, *middlewares)
    val updates = store.updates
        .filter {
            with(lifecycle) {
                currentState.isAtLeast(STARTED) && currentState != DESTROYED
            }
        }.share()

    return object : Store<S> {
        override val dispatch: (Action) -> Action = store.dispatch
        override val state: S get() = store.state
        override val updates: Observable<S> get() = updates
    }
}