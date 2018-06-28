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
        action.ofType(ADD_TODO) -> state.mutate { list -> list.add(action.payload as TodoActivity.Todo) }
        action.ofType(TOGGLE_TODO) -> {
            val match = state.find { todo -> todo.id == action.payload as Int }
            state.mutate { list ->
                if (match != null) {
                    val index = list.indexOf(match)
                    list[index] = list[index].copy(checked = !list[index].checked)
                }
            }
        }
        else -> state
    }
}