package com.allsouls.redux

import io.reactivex.Observable

/**
 * From the reduxjs docs:
 * A Redux store that lets you read the state, dispatch actions and subscribe to changes.
 */
interface Store<State> {
    val dispatch: (Action) -> Action
    val state: State
    val updates: Observable<State>
}