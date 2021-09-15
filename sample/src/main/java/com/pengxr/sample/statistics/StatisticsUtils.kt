package com.pengxr.sample.base

import android.content.Context
import com.pengxr.easytrack.core.EasyTrack
import com.pengxr.sample.BuildConfig
import com.pengxr.sample.statistics.EventConstants.*
import com.pengxr.sample.statistics.SensorProvider
import com.pengxr.sample.statistics.UmengProvider

/**
 * Created by pengxr on 5/9/2021
 */

val umengProvider by lazy {
    UmengProvider()
}

val sensorProvider by lazy {
    SensorProvider()
}

/**
 * @param context ApplicationContext
 */
fun init(context: Context) {
    configStatistics(context)
    registerProviders(context)
}

private fun configStatistics(context: Context) {
    EasyTrack.debug = BuildConfig.DEBUG
    EasyTrack.referrerKeyMap = mapOf(
        CUR_PAGE to FROM_PAGE,
        CUR_TAB to FROM_TAB
    )
}

private fun registerProviders(context: Context) {
    EasyTrack.registerProvider(umengProvider)
    EasyTrack.registerProvider(sensorProvider)
}