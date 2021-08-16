package com.github.greghynds.redux.sample

import junit.framework.Assert.assertEquals
import org.junit.Test
import com.github.greghynds.redux.Action

class ActionsTest {

    @Test
    fun `creates an action to increment the count`() {
        assertEquals(createIncrementAction(), Action("INCREMENT"))
    }

    @Test
    fun `creates an action to decrement the count`() {
        assertEquals(createDecrementAction(), Action("DECREMENT"))
    }

    @Test
    fun `creates an action to reset the count`() {
        assertEquals(createResetAction(), Action("RESET"))
    }
}