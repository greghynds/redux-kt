package com.github.greghynds.redux.sample

import android.util.Log
import com.github.greghynds.redux.Middleware

val logging: Middleware<Int> = { _ ->
    { action -> action.also { Log.d("Logger", "Dispatching action: $action") } }
}