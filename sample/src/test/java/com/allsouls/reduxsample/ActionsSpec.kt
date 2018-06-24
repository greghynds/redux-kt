package com.allsouls.reduxsample

import com.allsouls.redux.Action
import com.allsouls.reduxsample.counter.*
import junit.framework.Assert.assertEquals
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

object ActionsSpec : Spek({
    describe("actions", {
        it("should create an action to increment the count", {
            val expected = Action(INCREMENT)
            assertEquals(createIncrementAction(), expected)
        })
        it("should create an action to decrement the count", {
            val expected = Action(DECREMENT)
            assertEquals(createDecrementAction(), expected)
        })
        it("should create an action to reset the count", {
            val expected = Action(RESET)
            assertEquals(createResetAction(), expected)
        })
    })
})