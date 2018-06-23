package com.allsouls.redux

import com.allsouls.redux.utils.Middleware
import com.allsouls.redux.utils.Reducer
import com.allsouls.redux.utils.StoreEnhancer
import io.reactivex.Observable

interface Store<State> {
    val dispatch: (Action) -> Action
    val state: State
    val updates: Observable<State>
}

fun <State> createStore(reducer: Reducer<State>, initialState: State, vararg middlewares: Middleware<State>): Store<State> =
        with(Redux<State>()) { createStore(reducer, initialState, applyMiddleware(middlewares.asList())) }

fun <State> createStore(reducer: Reducer<State>, initialState: State, enhancer: StoreEnhancer<State>? = null): Store<State> =
        with(Redux<State>()) { createStore(reducer, initialState, enhancer) }