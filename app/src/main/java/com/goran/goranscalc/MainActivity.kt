package com.goran.goranscalc

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


enum class Mode
{
    NoMode, Add, Subtract, Multiply, Divide;
}

class MainActivity : AppCompatActivity() {

    var lastWasMode = false;
    var currentMode = Mode.NoMode;
    var labelText = "";
    var savedNumber = 0.0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setup();

    }

    fun setup() {
        val buttons: Array<Button> = arrayOf(
            findViewById(R.id.button_0),
            findViewById(R.id.button1),
            findViewById(R.id.button2),
            findViewById(R.id.button3),
            findViewById(R.id.button4),
            findViewById(R.id.button5),
            findViewById(R.id.button6),
            findViewById(R.id.button7),
            findViewById(R.id.button8),
            findViewById(R.id.button9)
        )

        for (i in buttons.indices) {
            buttons[i] .setOnClickListener {
                pressedNumber(i)
            }
        }
        findViewById<Button>(R.id.button_plus)
            .setOnClickListener { changeMode (Mode.Add)} ;
        findViewById<Button>(R.id.button_minus)
            .setOnClickListener { changeMode (Mode.Subtract)} ;
        findViewById<Button>(R.id.button_multiply)
            .setOnClickListener { changeMode (Mode.Multiply)} ;
        findViewById<Button>(R.id.button_divide)
            .setOnClickListener { changeMode (Mode.Divide)} ;
        findViewById<Button>(R.id.button_c)
            .setOnClickListener { pressedClear() } ;
        findViewById<Button>(R.id.button_equal)
            .setOnClickListener { pressedEquals() } ;
        findViewById<Button>(R.id.button_point).setOnClickListener {
            addPoint()
        }
    }

    fun pressedEquals(){
        if(lastWasMode)
            return;
        var tview = findViewById<TextView>(R.id.textView) ;
        val labelDouble = labelText.toDoubleOrNull() ?: return
        when (currentMode) {
            Mode.NoMode -> return
            Mode.Add -> savedNumber += labelDouble
            Mode.Subtract -> savedNumber -= labelDouble
            Mode.Multiply -> savedNumber *= labelDouble
            Mode.Divide -> savedNumber /= labelDouble
        }
        currentMode = Mode.NoMode;
        labelText = "$savedNumber";
        updateLabelText();
        lastWasMode = true;
    }

    fun pressedClear() {
        lastWasMode = false;
        currentMode = Mode.NoMode;
        labelText = "";
        savedNumber = 0.0;
        findViewById<TextView>(R.id.textView) .text = "0";
    }

    fun pressedNumber(num: Int){
        val numString = num.toString() ;
        if(lastWasMode){
            lastWasMode = false;
            labelText = "";
        }
        labelText = "$labelText$numString"
        updateLabelText();
    }

    fun updateLabelText() {
        val tview = findViewById<TextView> (R.id.textView) ;
        if(labelText.length > 16) {
            pressedClear();
            tview.text = "Nope!";
            return;
        }

        val labelDouble = labelText.toDoubleOrNull() ?: 0.0
        if (currentMode == Mode.NoMode) {
            savedNumber = labelDouble
        }

        tview.text = labelText;
    }

    fun changeMode(mode: Mode) {
        if(savedNumber == 0.0){
            return;
        }
        currentMode = mode;
        lastWasMode = true;
    }

    fun addPoint() {
        if (!labelText.contains(".")) {
            labelText += "."
            updateLabelText()
        }
    }
}
