package com.allsouls.redux.utils

import android.support.v7.app.AppCompatActivity
import com.allsouls.redux.Action
import com.allsouls.redux.Middleware
import com.allsouls.redux.Reducer
import com.allsouls.redux.Store
import com.allsouls.redux.utils.RxActivity.Companion.ON_DESTROY
import com.allsouls.redux.utils.RxActivity.Companion.ON_START
import io.reactivex.Observable

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
    val store = com.allsouls.redux.createStore(reducer, initialState, *middlewares)
    val updates = with(RxActivity.lifecycle(this)) {
        filter { lifecycleEvent -> lifecycleEvent == ON_START }
            .takeUntil(filter { lifecycleEvent -> lifecycleEvent == ON_DESTROY })
            .concatMap { store.updates }
            .share()
    }

    return object : Store<S> {
        override val dispatch: (Action) -> Action = store.dispatch
        override val state: S get() = store.state
        override val updates: Observable<S> get() = updates
    }
}