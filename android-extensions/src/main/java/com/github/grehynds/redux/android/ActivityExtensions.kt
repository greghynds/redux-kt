package com.github.grehynds.redux.android

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import com.github.greghynds.redux.Middleware
import com.github.greghynds.redux.Reducer
import com.github.greghynds.redux.Store
import com.github.greghynds.redux.applyMiddleware
import io.reactivex.Observable

/**
 * Creates a store scoped to the Activity lifecycle.
 *
 * The store will not emit updates after the Activity has been destroyed.
 */
fun <S> AppCompatActivity.createStore(
    reducer: Reducer<S>,
    initialState: S,
    vararg middlewares: Middleware<S>
): Store<S> {
    val store = com.github.greghynds.redux.createStore(
        reducer,
        initialState,
        applyMiddleware(*middlewares)
    )
    val updates = store.updates
        .filter { _: S -> lifecycle.currentState != Lifecycle.State.DESTROYED }
        .share()

    return object : Store<S>() {
        override val dispatch: (Any) -> Any = store.dispatch
        override val state: S get() = store.state
        override val updates: Observable<S> get() = updates
    }
}