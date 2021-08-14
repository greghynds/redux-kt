package xyz.gwh.redux.sample

import junit.framework.Assert.assertEquals
import org.junit.Test
import xyz.gwh.redux.Action
import xyz.gwh.redux.createDecrementAction
import xyz.gwh.redux.createIncrementAction

class CounterReducerTest {

    @Test
    fun `empty action returns initial state`() {
        assertEquals(counterReducer(0, Action.EMPTY), 0)
    }

    @Test
    fun `increment action increments count`() {
        assertEquals(counterReducer(0, createIncrementAction()), 1)
    }

    @Test
    fun `decrement action decrements count`() {
        assertEquals(counterReducer(1, createDecrementAction()), 0)
    }

    @Test
    fun `reset action sets count to zero`() {
        assertEquals(counterReducer(1, createDecrementAction()), 0)
    }
}