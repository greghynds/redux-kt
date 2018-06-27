package com.allsouls.reduxsample.todo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import com.allsouls.redux.createStore
import com.allsouls.redux.utils.connect
import com.allsouls.reduxsample.R
import kotlinx.android.synthetic.main.activity_todo.*


class TodoActivity : AppCompatActivity() {

    // create the store with the root reducer and an initial count
    val store = createStore(todoReducer, initialState = TodoState.INITIAL)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo)

        // bind actions
        add_button.setOnClickListener {
            store.dispatch(createAddTodoAction(add_todo_entry.editableText.toString()))
        }

        connect(store, { state ->
            todo_list.adapter = ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_list_item_1,
                    state.todos.map { todo -> todo.text }
            )
        })
    }

    data class TodoState(
            val todos: List<Todo>
    ) {
        companion object {
            val INITIAL = TodoState(
                    todos = listOf(Todo("Write the docs"))
            )
        }
    }

    data class Todo(
            val text: String,
            val checked: Boolean = false
    )
}