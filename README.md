# MVVMDataBindingExampleKotlin

MVVM has been prominent for years featuring separation of data and business logic from views. ([This tutorial](https://www.raywenderlich.com/636803-mvvm-and-databinding-android-design-patterns) gives a clear idea of the differences between MVC/MVP/MVVM.)

The most valuable part of MVVM is Data Binding. Unfortunately, the [official solution to Data Binding](https://developer.android.com/topic/libraries/data-binding), though powerful, is way too complicated for our daily needs and for sure will scare away many new comers including myself. Here we are demonstrating a much lighter and easier solution, without losing any power. And most importantly, this solution is 100% native.

The main idea is to define two classes with generic, JKObservable<T> and JKObserver<T>, (don't be distracted by the prefix JK which is my initials,) so that the instance of JKObservable can be observed by one or many instances of JKObserver.

Usage is very simple. In the example, we created an object ColorMixture holding integer values (ranging from 0 to 255) for RGB.

    object ColorMixture {
        var red = JKObservable<Int>(0)
        var green = JKObservable<Int>(0)
        var blue = JKObservable<Int>(0)
    }

In order to let a view observe the color value of "red" in object ColorMixture, as shown in our example's MainActivity, we created an observer

    private val redObserver = JKObserver<Int>(ColorMixture.red, onChange = { oldValue: Int, newValue: Int ->
        // update UI element(s) according to newValue
    })

That's it. As easy as pie, right?

One thing worth mentioning, if you dig into JKObservable, you may have noticed a

var writer: Any = Unit

The comments which is above the "write" variable explains the purpose,

    // For recording who changed the value
    // In some cases, a UI element can both be the changer and the observer of a same observable. It may cause an infinite loop like
    //  (1) The user operates the UI and changes the value of a UI element, say controlA
    //  (2) controlA's listener is triggered by the change, and then writes the new value to the observable, say observableO
    //  (3) The onChange block of the observer is triggered by observerO's value change, and then updates all the related UI elements, including controlA
    //  (4) controlA's listener is triggered again by step(3) and an infinite loop between step(2) and step(3) started
    //  (...)
    //  (n) Finally the app crashed
    // To avoid this, in this scenario, in step(2), mark the "writer" as self (controlA) before writing the new value to the observable, and in step(3), update all the UI elements OTHER THAN the writer itself.

As in our example, we can see

    redSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            ColorMixture.red.writer = redSeekBar
            ColorMixture.red.value = progress
        }
        // ...
    })


    private val redObserver = JKObserver<Int>(ColorMixture.red, onChange = { oldValue: Int, newValue: Int ->
        // check writer and update redSeekBar
        if (ColorMixture.red.writer != redSeekBar) {
            redSeekBar.setProgress(newValue)
        }
        // check writer and update other UI elements
    })

# Demo
![](https://github.com/zjkuang/MVVMDataBindingExampleKotlin/blob/master/MVVMDataBindingExampleKotlin.gif)
