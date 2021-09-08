package com.pengxr.sample.session

import com.pengxr.sample.core.ITrackModel
import com.pengxr.sample.core.TrackParams

/**
 * 埋点事件
 * <p>
 * Created by pengxr on 21/8/2021
 */
class TrackEvent internal constructor(
    val eventName: String, var params: TrackParams?
) {

    /**
     * 声明需要使用埋点会话中的参数
     */
    fun with(clazz: Class<ITrackModel>) {

    }

    /**
     * 声明需要使用埋点会话中的参数
     */
    fun with(vararg params: Array<String>) {

    }

    /**
     * 执行上报
     */
    fun emit() {
        params = params ?: TrackParams()

    }
}