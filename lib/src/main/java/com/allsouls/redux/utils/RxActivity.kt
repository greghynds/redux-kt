package com.allsouls.redux.utils

import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * Reactive wrapper for AppCompatActivity that exposes
 * a stream of Activity lifecycle events.
 *
 * Inspired by RxLifecycle - https://github.com/zhihu/RxLifecycle/
 * Lifecycle events are hijacked by attaching a fragment to the Activity.
 */
class RxActivity private constructor(val lifecycleStream: Observable<Int>) {

    companion object {
        const val ON_ATTACH = 0
        const val ON_CREATE = ON_ATTACH + 1
        const val ON_CREATE_VIEW = ON_CREATE + 1
        const val ON_START = ON_CREATE_VIEW + 1
        const val ON_RESUME = ON_START + 1
        const val ON_PAUSE = ON_RESUME + 1
        const val ON_STOP = ON_PAUSE + 1
        const val ON_DESTROY_VIEW = ON_STOP + 1
        const val ON_DESTROY = ON_DESTROY_VIEW + 1
        const val ON_DETACH = ON_DESTROY + 1

        private val FRAGMENT_TAG = "_RxActivityFragment_"

        /**
         * Returns a stream of Integers that represent Activity lifecycle events.
         */
        fun lifecycle(activity: AppCompatActivity): Observable<Int> {
            val fragmentManager = activity.supportFragmentManager
            var fragment = fragmentManager.findFragmentByTag(FRAGMENT_TAG) as RxActivityFragment?
            if (fragment == null) {
                fragment = RxActivityFragment()

                val transaction = fragmentManager.beginTransaction()
                transaction.add(fragment, FRAGMENT_TAG)
                transaction.commit()

            } else if (fragment.isDetached) {
                val transaction = fragmentManager.beginTransaction()
                transaction.attach(fragment)
                transaction.commit()
            }

            return RxActivity(fragment.lifecycle()).lifecycleStream
        }
    }

    /**
     * Fragment used to capture Activity lifecycle events.
     */
    internal class RxActivityFragment : Fragment() {

        private val lifecycleStream = PublishSubject.create<Int>()

        fun lifecycle(): Observable<Int> = lifecycleStream

        override fun onStart() {
            super.onStart()
            lifecycleStream.onNext(ON_START)
        }

        override fun onStop() {
            super.onStop()
            lifecycleStream.onNext(ON_STOP)
        }
    }
}