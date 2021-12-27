package com.github.greghynds.redux

import io.reactivex.Observable

/**
 * From the reduxjs docs:
 * A Redux store that lets you read the state, dispatch actions and subscribe to changes.
 */
abstract class Store<State> {
    abstract val dispatch: (Any) -> Any
    abstract val state: State
    abstract val updates: Observable<State>

    fun subscribe(listener: (State) -> Unit): Subscription {
        val disposable = updates.subscribe { state -> listener(state) }

        return object : Subscription {
            override fun unsubscribe() {
                disposable.dispose()
            }
        }
    }

    interface Subscription {
        fun unsubscribe()

        companion object {
            val NONE = object : Subscription {
                override fun unsubscribe() { /* no op */ }
            }
        }
    }
}