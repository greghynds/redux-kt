package com.allsouls.redux.utils

import com.allsouls.redux.Action
import com.allsouls.redux.Store

typealias Reducer<State> = (State, Action) -> State
typealias StoreCreator<State> = (reducer: Reducer<State>, preloadedState: State) -> Store<State>
typealias StoreEnhancer<State> = (next: StoreCreator<State>) -> StoreCreator<State>
typealias Middleware<State> = (Store<State>) -> ((Action) -> Action)