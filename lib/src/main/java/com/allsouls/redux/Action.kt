package com.allsouls.redux

data class Action(
        val type: String = "",
        val payload: Any? = null,
        val error: Boolean = false
) {
    fun ofType(vararg type: String): Boolean = type.contains(this.type)
}