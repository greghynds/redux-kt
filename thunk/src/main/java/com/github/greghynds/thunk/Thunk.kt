package com.github.greghynds.thunk

import com.github.greghynds.redux.Middleware
import com.github.greghynds.redux.Store

fun interface Thunk<State> {
    fun create(store: Store<State>): Any
}

@Suppress("UNCHECKED_CAST")
fun <State> createThunkMiddleware(): Middleware<State> = { store ->
    { action ->
        when (action) {
            is Thunk<*> -> (action as? Thunk<State>)?.create(store) ?: action
            else -> action
        }
    }
}