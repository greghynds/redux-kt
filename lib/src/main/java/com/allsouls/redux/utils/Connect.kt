package com.allsouls.redux.utils

import android.support.v7.app.AppCompatActivity
import com.allsouls.redux.Selector
import com.allsouls.redux.Store
import io.reactivex.Observable

/**
 * Connects an AppCompatActivity to updates from the store and renders the state.
 */
fun <State> AppCompatActivity.connect(store: Store<State>): Observable<State> =
        connect(store, { state -> state })

/**
 * Connects an AppCompatActivity to updates from the store and renders a slice of the state.
 */
fun <State, Slice> AppCompatActivity.connect(store: Store<State>, selector: Selector<State, Slice>): Observable<Slice> =
        with(RxActivity.lifecycle(this)) {
            filter { lifecycleEvent -> lifecycleEvent == RxActivity.ON_START }
                    .takeUntil(filter { lifecycleEvent -> lifecycleEvent == RxActivity.ON_PAUSE })
                    .concatMap { store.updates }
                    .map { state -> selector(state) }
        }