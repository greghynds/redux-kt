package com.github.greghynds.redux

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject


/**
 * From the reduxjs docs:
 * Creates a Redux store that holds the state tree.
 * The only way to change the data in the store is to call `dispatch()` on it.
 *
 * @returns A Redux store that lets you read the state, dispatch actions
 * and subscribe to changes.
 */
@Suppress("CheckResult")
fun <State> createStore(reducer: Reducer<State>, initialState: State, enhancer: StoreEnhancer<State>?): Store<State> {
    var currentState = initialState
    val actions = BehaviorSubject.create<Any>()
    val updates = BehaviorSubject.create<State>()

    actions.scan(initialState, reducer)
        .subscribe(
            { state -> updates.onNext(state) },
            { error -> updates.onError(error) }
        )

    return when {
        enhancer != null -> enhancer { r, s -> createStore(r, s, null) }(reducer, initialState)
        else -> {
            object : Store<State>() {
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
fun <State> applyMiddleware(vararg middlewares: Middleware<State>): StoreEnhancer<State> {
    return applyMiddleware(middlewares.toList())
}

/**
 * Helper function for selecting a slice of global state.
 */
fun <State, Slice> Store<State>.select(block: (State) -> Slice): Observable<Slice> {
    return updates.map { state: State -> block(state) }
}

private fun <State> applyMiddleware(middlewares: List<Middleware<State>>): StoreEnhancer<State> {
    return { next ->
        val creator: StoreCreator<State> = { reducer, initialState ->
            val store: Store<State> = next(reducer, initialState)
            var dispatch = store.dispatch
            val middlewareAPI = object : Store<State>() {
                override val dispatch: (Any) -> Any get() = dispatch
                override val state: State get() = store.state
                override val updates: Observable<State> get() = store.updates
            }

            val chain = middlewares
                .map { middleware -> middleware(middlewareAPI) }
                .toMutableList()
                .apply { add(store.dispatch) }

            dispatch = chain.reduceRight { composed, f -> f.compose(composed) }

            object : Store<State>() {
                override val dispatch: (Any) -> Any get() = dispatch
                override val state: State get() = store.state
                override val updates: Observable<State> get() = store.updates
            }
        }

        creator
    }
}

private fun <IP, R, P1> ((IP) -> R).compose(f: (P1) -> IP): (P1) -> R = { p1: P1 -> this(f(p1)) }