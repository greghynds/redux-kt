package com.github.greghynds.redux

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

/**
 * From the reduxjs docs:
 * Reducers specify how the application's state changes in response to actions sent to the store.
 */
typealias Reducer<State> = (State, Any) -> State

/**
 * From the reduxjs docs:
 * A store creator is a function that creates a Redux store.
 */
typealias StoreCreator<State> = (reducer: Reducer<State>, preloadedState: State) -> Store<State>

/**
 * From the reduxjs docs:
 * A store enhancer is a higher-order function that composes a store creator to return a new, enhanced store creator.
 */
typealias StoreEnhancer<State> = (next: StoreCreator<State>) -> StoreCreator<State>

/**
 * From the reduxjs docs:
 * A middleware is a higher-order function that composes a dispatch function to return a new dispatch function.
 */
typealias Middleware<State> = (Store<State>) -> ((Any) -> Any)


/**
 * From the reduxjs docs:
 * A Redux store that lets you read the state, dispatch actions and subscribe to changes.
 */
interface Store<State> {
    val dispatch: (Any) -> Any
    val state: State
    val updates: Observable<State>
}

/**
 * From the reduxjs docs:
 * Actions are payloads of information that send data from your application to your store.
 *
 * Based on Flux Standard Action - https://github.com/redux-utilities/flux-standard-action
 */
data class Action(
    val type: String = "",
    val payload: Any? = null,
    val error: Boolean = false
) {
    fun isOfType(vararg type: String): Boolean = type.contains(this.type)

    companion object {
        val EMPTY = Action("NONE")
    }
}

/**
 * A wrapper to allow typing.
 */
internal class Redux<State> {

    /**
     * From the reduxjs docs:
     * Creates a Redux store that holds the state tree.
     * The only way to change the data in the store is to call `dispatch()` on it.
     *
     * @returns A Redux store that lets you read the state, dispatch actions
     * and subscribe to changes.
     */
    val createStore: (Reducer<State>, State, StoreEnhancer<State>?) -> Store<State>
        @SuppressLint("CheckResult")
        get() = { reducer, initialState, enhancer ->
            var currentState = initialState
            val actions = BehaviorSubject.create<Any>()
            val updates = BehaviorSubject.create<State>()

            actions.scan(initialState, reducer)
                .subscribe(
                    { state -> updates.onNext(state) },
                    { error -> updates.onError(error) }
                )

            when {
                enhancer != null -> enhancer({ r, s -> createStore(r, s, null) })(reducer, initialState)
                else -> {
                    object : Store<State> {
                        override val dispatch: (Any) -> Any
                            get() = { action ->
                                currentState = reducer(currentState, action)
                                actions.onNext(action)
                                action
                            }
                        override val state: State get() = currentState
                        override val updates: Observable<State> get() = updates.replay(1).refCount()
                    }
                }
            }
        }

    /**
     * From the reduxjs docs:
     * Creates a store enhancer that applies middleware to the dispatch method
     * of the Redux store. This is handy for a variety of tasks, such as expressing
     * asynchronous actions in a concise manner, or logging every action payload.
     *
     * @returns A store enhancer applying the middleware.
     */
    val applyMiddleware: (List<Middleware<State>>) -> StoreEnhancer<State>
        get() = { middlewares ->
            { next ->
                val creator: StoreCreator<State> = { reducer, initialState ->
                    val store: Store<State> = next(reducer, initialState)
                    var dispatch = store.dispatch
                    val middlewareAPI = object : Store<State> {
                        override val dispatch: (Any) -> Any get() = dispatch
                        override val state: State get() = store.state
                        override val updates: Observable<State> get() = store.updates
                    }

                    val chain = middlewares
                        .map { middleware -> middleware(middlewareAPI) }
                        .toMutableList()
                        .apply { add(store.dispatch) }

                    dispatch = chain.reduceRight { composed, f -> f.compose(composed) }

                    object : Store<State> {
                        override val dispatch: (Any) -> Any get() = dispatch
                        override val state: State get() = store.state
                        override val updates: Observable<State> get() = store.updates
                    }
                }

                creator
            }
        }
}

/**
 * Helper function for selecting a slice of global state.
 */
fun <State, Slice> Store<State>.select(block: (State) -> Slice): Observable<Slice> {
    return updates.map { state: State -> block(state) }
}

/**
 * See Redux.createStore()
 */
fun <State> createStore(
    reducer: Reducer<State>,
    initialState: State,
    vararg middlewares: Middleware<State>
): Store<State> {
    return with(Redux<State>()) {
        createStore(reducer, initialState, applyMiddleware(middlewares.asList()))
    }
}

/**
 * Creates a store scoped to the Activity lifecycle.
 *
 * The store will not emit updates after the Activity has been destroyed.
 */
fun <S> AppCompatActivity.createStore(
    reducer: Reducer<S>,
    initialState: S,
    vararg middlewares: Middleware<S>
): Store<S> {
    val store = com.github.greghynds.redux.createStore(reducer, initialState, *middlewares)
    val updates = store.updates
        .filter { _: S -> lifecycle.currentState != Lifecycle.State.DESTROYED }
        .share()

    return object : Store<S> {
        override val dispatch: (Any) -> Any = store.dispatch
        override val state: S get() = store.state
        override val updates: Observable<S> get() = updates
    }
}

/**
 * Borrowed from https://github.com/MarioAriasC/funKTionale
 */
private fun <IP, R, P1> ((IP) -> R).compose(f: (P1) -> IP): (P1) -> R = { p1: P1 -> this(f(p1)) }