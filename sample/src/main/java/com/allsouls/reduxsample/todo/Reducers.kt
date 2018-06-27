package com.allsouls.reduxsample.todo

import com.allsouls.redux.Reducer
import com.allsouls.redux.utils.mutate

val todoReducer: Reducer<TodoActivity.TodoState> = { state, action ->
    TodoActivity.TodoState(
            todos = todosReducer(state.todos, action)
    )
}

val todosReducer: Reducer<List<TodoActivity.Todo>> = { state, action ->
    when {
        action.ofType(ADD_TODO) -> state.mutate { list -> list.add(TodoActivity.Todo(action.payload as String, false)) }
        action.ofType(CHECK_TODO) -> {
            val index = action.payload as Int
            if (index >= 0 && index < state.size) state.mutate { list -> list[index] } else state
        }
        else -> state
    }
}