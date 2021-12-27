package com.github.grehynds.redux.android

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Lifecycle.Event.ON_DESTROY
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.github.greghynds.redux.Store

/**
 * Subscribe to updates from the store, scoped to a Lifecycle.
 */
fun <State> Store<State>.subscribe(lifecycle: Lifecycle, listener: (State) -> Unit) {
    val subscription = subscribe(listener)

    lifecycle.addObserver(object : LifecycleEventObserver {
        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            if (event == ON_DESTROY) {
                subscription.unsubscribe()
            }
        }
    })
}