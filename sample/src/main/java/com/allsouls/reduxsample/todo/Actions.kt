package com.allsouls.reduxsample.todo

import com.allsouls.redux.Action
import java.util.concurrent.atomic.AtomicInteger

val idGenerator = AtomicInteger()

const val ADD_TODO = "ADD_TODO"
fun createAddTodoAction(text: String) = Action(ADD_TODO, TodoActivity.Todo(idGenerator.getAndIncrement(), text))

const val TOGGLE_TODO = "TOGGLE_TODO"
fun createToggleTodoAction(id: Int) = Action(TOGGLE_TODO, id)