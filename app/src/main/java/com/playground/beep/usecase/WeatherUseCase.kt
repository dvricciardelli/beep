package com.playground.beep.usecase

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import com.playground.beep.ExerciseActivity
import com.playground.beep.common.ThreadInfoLogger
import com.playground.beep.common.ThreadInfoLogger.logThreadInfo
import com.playground.beep.models.MyResponse
import com.playground.beep.models.WeatherResponse
import com.playground.beep.network.WeatherService
import com.playground.beep.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
//import com.squareup.okhttp.OkHttpClient
//import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.OkHttpClient.Builder

import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

import retrofit.*

class WeatherUseCase {

    suspend fun callBackWeatherService(listCall: Call<WeatherResponse>): MyResponse {



        return withContext(Dispatchers.Default) {
            logThreadInfo("RetrieveWeatherInfo")
            var mResponse = MyResponse(null, 0)


            listCall.enqueue(object : Callback<WeatherResponse> {
                @SuppressLint("SetTextI18n")
                override fun onResponse(
                        response: Response<WeatherResponse>,
                        retrofit: Retrofit
                ) {
                    logThreadInfo("determineSuccessOrFailure")
                    // Check weather the response is success or not.
                    if (response.isSuccess) {

                        val weatherList: WeatherResponse = response.body()
                        mResponse = MyResponse(weatherList, "");
                        Log.i("Response Result", "$weatherList")

                    } else {
                        // If the response is not success then we check the response code.
                        val sc = response.code()
                        //mResponse = MyResponse(null, sc)
                        //TODO: Make it reusable.  Doesn't need to parse response.
                        when (sc) {
                            400 -> {
                                Log.e("Error 400", "Bad Request")
                            }
                            404 -> {
                                Log.e("Error 404", "Not Found")
                            }
                            else -> {
                                Log.e("Error", "Generic Error")
                            }
                        }

                        mResponse = MyResponse(null, sc)

                    }
                }

                override fun onFailure(t: Throwable) {
                    mResponse = MyResponse(null, t.message.toString());
                    Log.e("Errorrrrr", t.message.toString())
                    //hideProgressDialog()
                }
            })

            mResponse
        }
    }

    suspend fun getLocationWeatherDetails(activity: ExerciseActivity, latitude: Double, longitude: Double): Call<WeatherResponse> {

        logThreadInfo("getWeatherService")

        return withContext(Dispatchers.Default) {
            logThreadInfo("BuildWeatherService")

            val listCall: Call<WeatherResponse> = RetrofitBuilder.service.getWeather(
                    latitude, longitude, Constants.METRIC_UNIT, Constants.APP_ID
            )

            listCall
        }
    }

    private fun logThreadInfo(message: String) {
        ThreadInfoLogger.logThreadInfo(message)
    }

    object RetrofitBuilder {

        val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        //can generalize to an API Service
        val service: WeatherService = retrofit
                .create(WeatherService::class.java)
    }
}