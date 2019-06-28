package com.jandjzone.mvvmdatabindingexample.`class`

import kotlin.properties.Delegates

interface JKObservableInterface<T> {

    var value: T

    // For recording who changed the value
    // In some cases, a UI element can both be the changer and the observer of a same observable. It may cause an infinite loop like
    //  (1) The user operates the UI and changes the value of a UI element, say controlA
    //  (2) controlA's listener is triggered by the change, and then writes the new value to the observable, say observableO
    //  (3) The onChange block of the observer is triggered by observerO's value change, and then updates all the related UI elements, including controlA
    //  (4) controlA's listener is triggered again by step(3) and an infinite loop between step(2) and step(3) started
    //  (...)
    //  (n) Finally the app crashed
    // To avoid this, in this scenario, in step(2), mark the "writer" as self (controlA) before writing the new value to the observable, and in step(3), update all the UI elements OTHER THAN the writer itself.
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

class JKObserver<T>(observable: JKObservableInterface<T>, onChange: (oldValue: T, newValue: T) -> Unit): JKObserverInterface<T> {

    private val onChange = onChange

    init {
        observable.registerListener(this)
    }

    override fun onValueChanged(oldValue: T, newValue: T) {
        onChange(oldValue, newValue)
    }

}
