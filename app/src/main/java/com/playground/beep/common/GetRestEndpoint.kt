package com.playground.beep.common

import com.playground.beep.common.ThreadInfoLogger
import kotlin.random.Random

class GetRestEndpoint {
    fun getRest(): Int {
        ThreadInfoLogger.logThreadInfo("GetReputationEndpoint#getReputation(): called")
        Thread.sleep(3000)
        ThreadInfoLogger.logThreadInfo("GetReputationEndpoint#getReputation(): return data")
        return Random.nextInt(0, 100)
    }
}