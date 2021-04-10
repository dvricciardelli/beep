package com.playground.beep

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import com.playground.beep.common.GetRestEndpoint
import com.playground.beep.common.ThreadInfoLogger
import com.playground.beep.models.MyResponse
import kotlinx.android.synthetic.main.activity_exercise.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.playground.beep.usecase.*

class ExerciseActivity : AppCompatActivity() {

    private val coroutineScope = CoroutineScope(Dispatchers.Main.immediate)
    private val weatherService = WeatherUseCase()


    val benchmarkDurationSeconds = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)
        tvBeepTimer.text = "$benchmarkDurationSeconds"

        tvBeepTimer.setOnClickListener {
            logThreadInfo("timer callback")
            /*coroutineScope.launch {
                tvBeepTimer.isEnabled = false
                val beep = setExerciseProgressBar() //observer on beep
                Toast.makeText(this@ExerciseActivity, "$beep", Toast.LENGTH_SHORT).show()
                tvBeepTimer.isEnabled = true
            }*/
            logThreadInfo("Call weather service")
            coroutineScope.launch {
                //weatherService.getLocationWeatherDetails(this@ExerciseActivity, 33.6183291, -117.8724881)
                val callBack = weatherService.getLocationWeatherDetails(this@ExerciseActivity, 35.0, 139.0)
                logThreadInfo("weatherService: $callBack")
                val response = weatherService.callBackWeatherService(callBack)
                logThreadInfo("weatherCallback: $response")
                Toast.makeText(this@ExerciseActivity, "$response", Toast.LENGTH_LONG).show()
                //tvWeather.text = response!!.success!!.visibility.toString()
            }

        }

    }

    private suspend fun setExerciseProgressBar(): Long{

        updateRemainingTime(benchmarkDurationSeconds)

        return withContext(Dispatchers.Default) {
            logThreadInfo("timer started")

            val stopTimeNano = System.nanoTime() + benchmarkDurationSeconds * 1_000_000_000L

            var iterationsCount: Long = 0
            while (System.nanoTime() < stopTimeNano) {
                iterationsCount++
            }

            logThreadInfo("timer completed")


            iterationsCount
        }
    }


    private fun updateRemainingTime(remainingTimeSeconds: Int) {
        logThreadInfo("updateRemainingTime: $remainingTimeSeconds seconds")

        if (remainingTimeSeconds > 0) {
            tvBeepTimer.text = "$remainingTimeSeconds"
            Handler(Looper.getMainLooper()).postDelayed({
                updateRemainingTime(remainingTimeSeconds - 1)
            }, 1000)
        } else {
            tvBeepTimer.text = "$benchmarkDurationSeconds"
        }

    }

    fun setupWeatherUI(){

    }

    private fun logThreadInfo(message: String) {
        ThreadInfoLogger.logThreadInfo(message)
    }
}