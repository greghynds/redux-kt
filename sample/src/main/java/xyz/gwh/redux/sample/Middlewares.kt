package xyz.gwh.redux.sample

import android.util.Log
import xyz.gwh.redux.Middleware

val logging: Middleware<Int> = { _ ->
    { action -> action.also { Log.d("Logger", "Dispatching action: $action") } }
}