package com.allsouls.reduxsample.todo

import com.allsouls.redux.Action

const val ADD_TODO = "ADD_TODO"
fun createAddTodoAction(text: String) = Action(ADD_TODO, text)

const val CHECK_TODO = "CHECK_TODO"
fun createCheckTodoAction(index: Int) = Action(CHECK_TODO, index)