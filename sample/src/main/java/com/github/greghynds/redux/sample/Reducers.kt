package com.github.greghynds.redux.sample

import com.github.greghynds.redux.Reducer
import kotlin.math.max

val counterReducer: Reducer<Int> = { state, action ->
    when {
        action.isOfType(INCREMENT) -> state + 1
        action.isOfType(DECREMENT) -> max(0, state - 1)
        action.isOfType(RESET) -> 0
        else -> state
    }
}