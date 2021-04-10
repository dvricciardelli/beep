package com.playground.beep.models

import java.io.Serializable

data class MyResponse (
    val success: WeatherResponse?,
    val error: Any?
) : Serializable