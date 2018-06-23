package com.allsouls.redux

/**
 * Based on Flux Standard Action - https://github.com/redux-utilities/flux-standard-action
 */
data class Action(
        val type: String = "",
        val payload: Any? = null,
        val error: Boolean = false
) {
    fun ofType(vararg type: String): Boolean = type.contains(this.type)
}