package com.allsouls.redux

/**
 * See Redux.createStore()
 */
fun <State> createStore(reducer: Reducer<State>, initialState: State, vararg middlewares: Middleware<State>): Store<State> =
        with(Redux<State>()) { createStore(reducer, initialState, applyMiddleware(middlewares.asList())) }

/**
 * See Redux.createStore()
 */
fun <State> createStore(reducer: Reducer<State>, initialState: State, enhancer: StoreEnhancer<State>? = null): Store<State> =
        with(Redux<State>()) { createStore(reducer, initialState, enhancer) }

