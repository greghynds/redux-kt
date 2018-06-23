package com.allsouls.redux.utils

import com.allsouls.redux.Action
import com.allsouls.redux.Store

/**
 * From the reduxjs docs:
 * Reducers specify how the application's state changes in response to actions sent to the store.
 */
typealias Reducer<State> = (State, Action) -> State

/**
 * From the reduxjs docs:
 * A store creator is a function that creates a Redux store.
 */
typealias StoreCreator<State> = (reducer: Reducer<State>, preloadedState: State) -> Store<State>

/**
 * From the reduxjs docs:
 * A store enhancer is a higher-order function that composes a store creator to return a new, enhanced store creator.
 */
typealias StoreEnhancer<State> = (next: StoreCreator<State>) -> StoreCreator<State>

/**
 * From the reduxjs docs:
 * A middleware is a higher-order function that composes a dispatch function to return a new dispatch function.
 */
typealias Middleware<State> = (Store<State>) -> ((Action) -> Action)