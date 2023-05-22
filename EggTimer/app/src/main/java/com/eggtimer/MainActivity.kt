package com.eggtimer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.widget.Button
import android.widget.Chronometer
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.Snackbar
import com.eggtimer.R

class MainActivity : AppCompatActivity() {

    lateinit var countdown:Chronometer
    lateinit var egg: ImageView
    lateinit var startButton: Button
    lateinit var stopButton: Button
    lateinit var resetButton: Button

    var running = false
    var offset:Long = -300000


    val OFFSET_KEY = "offset"
    val RUNNING_KEY = "running"
    val BASE_KEY = "base"

    fun setBaseTime() {
        countdown.base = SystemClock.elapsedRealtime() - offset
    }

    fun saveOffset() {
        offset = SystemClock.elapsedRealtime() - countdown.base
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val layout = findViewById<ConstraintLayout>(R.id.layout)

        countdown = findViewById(R.id.countdown)
        egg = findViewById(R.id.eggImage)
        startButton = findViewById(R.id.start)
        stopButton = findViewById(R.id.stop)
        resetButton = findViewById(R.id.reset)

        setBaseTime()

        if (savedInstanceState != null)
        {
            offset = savedInstanceState.getLong(OFFSET_KEY)
            running = savedInstanceState.getBoolean(RUNNING_KEY)
            if (running) {

                countdown.base = savedInstanceState.getLong(BASE_KEY)
                countdown.start()
            }
            else setBaseTime()
        }

        val startButton = findViewById<Button>(R.id.start)
        startButton.setOnClickListener {
            setBaseTime()
            egg.setImageResource(R.drawable.egg)
            if (!running) {
                setBaseTime()
                countdown.start()
                running = true
            }
        }

        val stopButton = findViewById<Button>(R.id.stop)
        stopButton.setOnClickListener {
            if(running){
                saveOffset()
                countdown.stop()
                running = false
            }
        }

        val resetButton = findViewById<Button>(R.id.reset)
        resetButton.setOnClickListener {
            offset = -300000
            setBaseTime()
            countdown.stop()
            running = false
            egg.setImageResource(R.drawable.egg);
        }

        countdown.setOnChronometerTickListener {
            if(SystemClock.elapsedRealtime() - countdown.base >= -1000){
                countdown.stop()
                running = false
                egg.setImageResource(R.drawable.boiled_egg);
                Snackbar.make(layout,"Egg is done.",Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (running) {
            saveOffset()
            countdown.stop()
            Toast.makeText(this,"p",Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        if (running) {
            setBaseTime()
            countdown.start()
            offset = 0
            Toast.makeText(this,"r",Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.putLong(OFFSET_KEY, offset)
        savedInstanceState.putBoolean(RUNNING_KEY, running)
        savedInstanceState.putLong(BASE_KEY, countdown.base)
        super.onSaveInstanceState(savedInstanceState)
    }


}



