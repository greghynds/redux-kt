package com.github.greghynds.redux.sample

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.greghynds.redux.Store
import com.github.grehynds.redux.android.createStore

class CounterActivity : AppCompatActivity() {

    // create the store with the root reducer and an initial count
    private val store: Store<Int> by lazy { createStore(counterReducer, 0, logging) }

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample)

        // bind actions
        findViewById<Button>(R.id.increment_button).setOnClickListener { store.dispatch(Increment()) }
        findViewById<Button>(R.id.decrement_button).setOnClickListener { store.dispatch(Decrement()) }
        findViewById<Button>(R.id.reset_button).setOnClickListener { store.dispatch(Reset()) }

        val countText = findViewById<TextView>(R.id.count_text)

        // subscribe to updates from the store
        store.subscribe { state ->
            countText.text = getString(R.string.count_text_prefix, state)
        }
    }
}