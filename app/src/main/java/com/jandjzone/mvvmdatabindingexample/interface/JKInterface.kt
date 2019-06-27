package com.jandjzone.james.`interface`

interface JKObservableInterface<T> {

    var value: T
    var writer: Any
    val observers: MutableList<JKObserverInterface<T>>

    fun registerListener(observer: JKObserverInterface<T>) {
        if (observers.contains(observer)) {
            return
        }
        observers.add(observer)
    }

    fun deregisterListener(observer: JKObserverInterface<T>) {
        if (observers.contains(observer)) {
            observers.remove(observer)
        }
    }

    fun removeAllListeners() {
        observers.removeAll { true }
    }

    fun onValueChanged(oldValue: T, newValue: T) {
        observers.forEach({
            it.onValueChanged(oldValue, newValue)
        })
    }
}

interface JKObserverInterface<T> {

    // Acknowlegments
    // https://medium.com/@ahmedrizwan/kotlin-design-patterns-the-observer-fc8e5a702b4c

    fun onValueChanged(oldValue: T, newValue: T)
}
