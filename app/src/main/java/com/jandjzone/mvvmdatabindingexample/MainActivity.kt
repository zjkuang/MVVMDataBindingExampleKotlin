package com.jandjzone.mvvmdatabindingexample

import android.content.Context
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.SeekBar
import com.jandjzone.mvvmdatabindingexample.`class`.JKObserver
import com.jandjzone.mvvmdatabindingexample.viewmodel.ColorMixture
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val redObserver = JKObserver<Int>(ColorMixture.red, onChange = { oldValue: Int, newValue: Int ->
        if (ColorMixture.red.writer != redSeekBar) {
            redSeekBar.setProgress(newValue)
        }
        if (ColorMixture.red.writer != redNumberEditText) {
            redNumberEditText.setText(newValue.toString())
        }
        mixColor()
    })

    private val greenObserver = JKObserver<Int>(ColorMixture.green, onChange = { oldValue: Int, newValue: Int ->
        if (ColorMixture.green.writer != greenSeekBar) {
            greenSeekBar.setProgress(newValue)
        }
        if (ColorMixture.green.writer != greenNumberEditText) {
            greenNumberEditText.setText(newValue.toString())
        }
        mixColor()
    })

    private val blueObserver = JKObserver<Int>(ColorMixture.blue, onChange = { oldValue: Int, newValue: Int ->
        if (ColorMixture.blue.writer != blueSeekBar) {
            blueSeekBar.setProgress(newValue)
        }
        if (ColorMixture.blue.writer != blueNumberEditText) {
            blueNumberEditText.setText(newValue.toString())
        }
        mixColor()
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setListeners()

        ColorMixture.red.value = 0
        ColorMixture.green.value = 0
        ColorMixture.blue.value = 0
    }

    private fun setListeners() {
        redSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

                // Seek
                ColorMixture.red.writer = redSeekBar

                ColorMixture.red.value = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                //
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                //
            }

        })

        greenSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                ColorMixture.green.writer = greenSeekBar
                ColorMixture.green.value = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                //
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                //
            }

        })

        blueSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                ColorMixture.blue.writer = blueSeekBar
                ColorMixture.blue.value = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                //
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                //
            }

        })

        redNumberEditText.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                if ((event?.action == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    dismissKeyboard(redNumberEditText)
                    val value = (redNumberEditText.text.toString()?.toInt() ?: 0) % 255
                    redNumberEditText.setText("%d".format(value))
                    ColorMixture.red.writer = redNumberEditText
                    ColorMixture.red.value = value
                    return true
                }
                return false
            }

        })

        greenNumberEditText.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                if ((event?.action == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    dismissKeyboard(greenNumberEditText)
                    val value = (greenNumberEditText.text.toString()?.toInt() ?: 0) % 255
                    greenNumberEditText.setText("%d".format(value))
                    ColorMixture.green.writer = greenNumberEditText
                    ColorMixture.green.value = value
                    return true
                }
                return false
            }

        })

        blueNumberEditText.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                if ((event?.action == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    dismissKeyboard(blueNumberEditText)
                    val value = (blueNumberEditText.text.toString()?.toInt() ?: 0) % 255
                    blueNumberEditText.setText("%d".format(value))
                    ColorMixture.blue.writer = blueNumberEditText
                    ColorMixture.blue.value = value
                    return true
                }
                return false
            }

        })
    }

    private fun mixColor() {
        val colorCodeString = "%02X%02X%02X".format(ColorMixture.red.value, ColorMixture.green.value, ColorMixture.blue.value)
        val complementaryColorCodeString = "%02X%02X%02X".format(255 - ColorMixture.red.value, 255 - ColorMixture.green.value, 255 - ColorMixture.blue.value)
        val colorCode = colorCodeString.toInt(16)
        val complementaryColorCode = 0xFFFFFF - colorCode
        colorCodeTextView.setText("#${colorCodeString}")
        colorCodeTextView.setTextColor(Color.parseColor("#${colorCodeString}"))
        colorCodeTextView.setBackgroundColor(Color.parseColor("#${complementaryColorCodeString}"))
        mixedColorView.setBackgroundColor(Color.parseColor("#${colorCodeString}"))
    }

    private fun dismissKeyboard(editText: EditText) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(editText.windowToken, 0)

    }

}
