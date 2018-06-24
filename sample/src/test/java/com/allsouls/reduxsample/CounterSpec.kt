package com.allsouls.reduxsample

import com.allsouls.redux.Action
import com.allsouls.reduxsample.counter.DECREMENT
import com.allsouls.reduxsample.counter.INCREMENT
import com.allsouls.reduxsample.counter.RESET
import com.allsouls.reduxsample.counter.counterReducer
import junit.framework.Assert.assertEquals
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

object CounterSpec : Spek({
    val reducer = counterReducer

    describe("counter reducer", {
        it("should return the initial state", {
            assertEquals(reducer(0, Action.EMPTY), 0)
        })
        it("should handle $INCREMENT", {
            assertEquals(reducer(0, Action(INCREMENT)), 1)
        })
        it("should handle $DECREMENT", {
            assertEquals(reducer(1, Action(DECREMENT)), 0)
        })
        it("should handle $RESET", {
            assertEquals(reducer(10, Action(RESET)), 0)
        })
    })
})