package xyz.gwh.redux

import android.annotation.SuppressLint
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

/**
 * From the reduxjs docs:
 * Reducers specify how the application's state changes in response to actions sent to the store.
 */
typealias Reducer<State> = (State, Action) -> State

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
typealias Middleware<State> = (Store<State>) -> ((Action) -> Action)


/**
 * From the reduxjs docs:
 * A Redux store that lets you read the state, dispatch actions and subscribe to changes.
 */
interface Store<State> {
    val dispatch: (Action) -> Action
    val state: State
    val updates: Observable<State>
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
            val actions = BehaviorSubject.create<Action>()
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
                        override val dispatch: (Action) -> Action
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
                        override val dispatch: (Action) -> Action get() = dispatch
                        override val state: State get() = store.state
                        override val updates: Observable<State> get() = store.updates
                    }

                    val chain = middlewares
                        .map { middleware -> middleware(middlewareAPI) }
                        .toMutableList()
                        .apply { add(store.dispatch) }

                    dispatch = chain.reduceRight { composed, f -> f.compose(composed) }

                    object : Store<State> {
                        override val dispatch: (Action) -> Action get() = dispatch
                        override val state: State get() = store.state
                        override val updates: Observable<State> get() = store.updates
                    }
                }

                creator
            }
        }
}


/**
 * Borrowed from https://github.com/MarioAriasC/funKTionale
 */
private fun <IP, R, P1> ((IP) -> R).compose(f: (P1) -> IP): (P1) -> R = { p1: P1 -> this(f(p1)) }