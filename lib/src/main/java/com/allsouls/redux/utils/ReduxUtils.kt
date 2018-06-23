package com.allsouls.redux.utils

import android.support.v7.app.AppCompatActivity
import com.allsouls.redux.Store
import io.reactivex.disposables.Disposable

/**
 * Subscribes to updates from the store once the Activity has launched
 * and unsubscribes when the Activity is destroyed.
 *
 * Calls the update function each time the store is updated.
 */
fun <S> AppCompatActivity.connect(store: Store<S>, update: (S) -> Unit, error: (Throwable) -> Unit = {}): Disposable =
        with(RxActivity.lifecycle(this)) {
            filter { lifecycleEvent -> lifecycleEvent == RxActivity.ON_START }
                    .takeUntil(filter { lifecycleEvent -> lifecycleEvent == RxActivity.ON_DESTROY })
                    .concatMap { store.updates }
                    .subscribe(update, error)
        }