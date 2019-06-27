package com.jandjzone.james.`class`

import com.jandjzone.james.`interface`.JKObservableInterface
import com.jandjzone.james.`interface`.JKObserverInterface

class JKObserver<T>(observable: JKObservableInterface<T>, onChange: (oldValue: T, newValue: T) -> Unit): JKObserverInterface<T> {

    private val onChange = onChange

    init {
        observable.registerListener(this)
    }

    override fun onValueChanged(oldValue: T, newValue: T) {
        onChange(oldValue, newValue)
    }

}