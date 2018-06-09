package com.allsouls.redux

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

typealias Reducer<State> = (State, Action) -> State
typealias StoreCreator<State> = (reducer: Reducer<State>, preloadedState: State) -> Store<State>
typealias StoreEnhancer<State> = (next: StoreCreator<State>) -> StoreCreator<State>
typealias Selector<State, Slice> = (State) -> Slice
typealias Middleware<State> = (Store<State>) -> ((Action) -> Action)

internal class Redux<State> {

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

    private fun <IP, R, P1> ((IP) -> R).compose(f: (P1) -> IP): (P1) -> R {
        return { p1: P1 -> this(f(p1)) }
    }
}