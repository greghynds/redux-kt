package com.allsouls.reduxsample.counter

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.allsouls.redux.Store
import com.allsouls.redux.utils.createStore
import com.allsouls.reduxsample.R
import kotlinx.android.synthetic.main.activity_sample.*

class CounterActivity : AppCompatActivity() {

    // create the store with the root reducer and an initial count
    private val store: Store<Int> by lazy { createStore(counterReducer, 0, logging) }

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample)

        // bind actions
        increment_button.setOnClickListener { store.dispatch(createIncrementAction()) }
        decrement_button.setOnClickListener { store.dispatch(createDecrementAction()) }
        reset_button.setOnClickListener { store.dispatch(createResetAction()) }

        // subscribe to updates from the store
        store.updates.subscribe { state ->
            count_text.text = getString(R.string.count_text_prefix, state)
        }
    }
}