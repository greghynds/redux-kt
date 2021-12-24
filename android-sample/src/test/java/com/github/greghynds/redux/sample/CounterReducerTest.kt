package com.github.greghynds.redux.sample

import junit.framework.Assert.assertEquals
import org.junit.Test

class CounterReducerTest {

    @Test
    fun `increment action increments count`() {
        assertEquals(counterReducer(0, Increment()), 1)
    }

    @Test
    fun `decrement action decrements count`() {
        assertEquals(counterReducer(1, Decrement()), 0)
    }

    @Test
    fun `reset action sets count to zero`() {
        assertEquals(counterReducer(5, Reset()), 0)
    }
}