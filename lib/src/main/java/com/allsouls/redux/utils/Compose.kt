package com.allsouls.redux.utils


/**
 * Borrowed from https://github.com/MarioAriasC/funKTionale
 */
fun <IP, R, P1> ((IP) -> R).compose(f: (P1) -> IP): (P1) -> R {
    return { p1: P1 -> this(f(p1)) }
}