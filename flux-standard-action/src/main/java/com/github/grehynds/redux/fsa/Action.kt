package com.github.grehynds.redux.fsa

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

/**
 * From the reduxjs docs:
 * Actions are payloads of information that send data from your application to your store.
 *
 * Based on Flux Standard Action - https://github.com/redux-utilities/flux-standard-action
 */
data class Action(
    val type: String = "",
    val payload: Any? = null,
    val error: Boolean = false
) {
    fun isOfType(vararg type: String): Boolean = type.contains(this.type)

    companion object {
        val EMPTY = Action("NONE")
    }
}

@ExperimentalContracts
fun Any.isOfType(type: String): Boolean {
    contract {
        returns(true) implies (this@isOfType is Action)
    }

    return this is Action && isOfType(type)
}