package com.github.greghynds.redux.sample

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.greghynds.redux.applyMiddleware
import com.github.greghynds.redux.createStore
import com.github.greghynds.redux.sample.databinding.CounterActivityBinding
import com.github.grehynds.redux.android.subscribe

class CounterActivity : AppCompatActivity() {

    // create the store with the root reducer and an initial count
    private val store = createStore(counterReducer, 0, applyMiddleware(logging))

    private lateinit var binding: CounterActivityBinding

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CounterActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // bind actions
        binding.incrementButton.setOnClickListener { store.dispatch(Increment()) }
        binding.decrementButton.setOnClickListener { store.dispatch(Decrement()) }
        binding.resetButton.setOnClickListener { store.dispatch(Reset()) }

        // subscribe to updates from the store
        store.subscribe(lifecycle) { state ->
            binding.countText.text = getString(R.string.count_text_prefix, state)
        }
    }
}