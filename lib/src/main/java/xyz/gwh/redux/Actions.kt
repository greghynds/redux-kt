package xyz.gwh.redux

const val INCREMENT = "INCREMENT"
fun createIncrementAction() = Action(INCREMENT)

const val DECREMENT = "DECREMENT"
fun createDecrementAction() = Action(DECREMENT)

const val RESET = "RESET"
fun createResetAction() = Action(RESET)
