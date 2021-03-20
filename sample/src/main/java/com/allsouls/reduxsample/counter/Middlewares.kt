package com.allsouls.reduxsample.counter

import android.util.Log
import com.allsouls.redux.Middleware

val logging: Middleware<Int> = { _ ->
    { action ->
        action.also { Log.d("Logger", "Dispatching action: $action") }
    }
}