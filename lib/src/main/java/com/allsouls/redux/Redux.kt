package com.allsouls.redux

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

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