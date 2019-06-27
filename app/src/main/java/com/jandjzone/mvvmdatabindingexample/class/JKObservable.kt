package com.jandjzone.james.`class`

import com.jandjzone.james.`interface`.JKObservableInterface
import com.jandjzone.james.`interface`.JKObserverInterface
import kotlin.properties.Delegates

class JKObservable<T>(initialValue: T): JKObservableInterface<T> {
    override var value: T by Delegates.observable(
        initialValue = initialValue,
        onChange = {
                property, oldValue, newValue ->
            onValueChanged(oldValue, newValue)
        }
    )
    override var writer: Any = Unit
    override val observers = mutableListOf<JKObserverInterface<T>>()
}
