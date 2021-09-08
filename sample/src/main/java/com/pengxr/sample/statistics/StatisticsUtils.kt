package com.pengxr.sample.base

import android.content.Context
import com.pengxr.sample.BuildConfig
import com.pengxr.sample.statistics.EventConstants.*
import com.pengxr.sample.statistics.UmengProvider
import com.pengxr.sample.core.StatisticsLib
import com.pengxr.sample.core.TrackParams
import java.util.*

/**
 * Created by pengxr on 5/9/2021
 */
// Mock sensor sdk.
const val SENSOR = "sensor"

// Mock umeng sdk.
const val UMENG = "umeng"

/**
 * @param context ApplicationContext
 */
fun init(context: Context) {
    configStatistics(context)
    registerProviders(context)
    registerSuperProperties(context)

    StatisticsLib.dispatchUserLogin(getLoginUserId())
}

private fun configStatistics(context: Context) {
    StatisticsLib.configDebug(BuildConfig.DEBUG)
    StatisticsLib.configReferrerKeyMap(
        mapOf(
            CUR_PAGE to FROM_PAGE,
            CUR_TAB to FROM_TAB
        )
    )
}

private fun registerProviders(context: Context) {
    StatisticsLib.registerProvider(UMENG, UmengProvider())
}

private fun registerSuperProperties(context: Context) {
    val params = TrackParams()
    params[LONGITUDE] = "113.9"
    params[LATITUDE] = 22.5
    params[CITY_ID] = "1"
    params[CITY_NAME] = "深圳市"
    params[DEVICE_ID] = UUID.randomUUID()
}

private fun getLoginUserId() = "user_id_${UUID.randomUUID()}"