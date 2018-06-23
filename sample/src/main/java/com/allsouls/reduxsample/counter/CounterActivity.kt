package com.allsouls.reduxsample.counter

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.allsouls.redux.connect
import com.allsouls.redux.createStore
import com.allsouls.reduxsample.R
import kotlinx.android.synthetic.main.activity_sample.*

class CounterActivity : AppCompatActivity() {

    val store = createStore(counterReducer, initialState = 0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample)

        increment_button.setOnClickListener { store.dispatch(createIncrementAction()) }
        decrement_button.setOnClickListener { store.dispatch(createDecrementAction()) }
        reset_button.setOnClickListener { store.dispatch(createResetAction()) }

        connect(store).subscribe { count ->
            count_text.text = getString(R.string.count_text_prefix, count)
        }
    }
}