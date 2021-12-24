package com.github.greghynds.redux.sample

import com.github.greghynds.redux.Reducer
import kotlin.math.max

val counterReducer: Reducer<Int> = { state, action ->
    when (action) {
        is Increment -> state + 1
        is Decrement -> max(0, state - 1)
        is Reset -> 0
        else -> state
    }
}