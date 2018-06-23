package com.allsouls.reduxsample.counter

import com.allsouls.redux.utils.Reducer

val counterReducer: Reducer<Int> = { state, action ->
    when {
        action.ofType(INCREMENT) -> state + 1
        action.ofType(DECREMENT) -> Math.max(0, state - 1)
        action.ofType(RESET) -> 0
        else -> state
    }
}