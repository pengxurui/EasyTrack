package com.pengxr.sample.core

import androidx.annotation.UiThread

/**
 * 数据统计底层能力
 *
 * Created by pengxr on 2021/8/18.
 */
abstract class ITrackProvider {

    /**
     * 是否开启数据统计
     */
    abstract var enabled: Boolean

    /**
     * 初始化回调
     */
    @UiThread
    abstract fun onInit()

    /**
     * 销毁回调（几乎不可能回调，仅用于实现逻辑闭环）
     */
    abstract fun onClear()

    /**
     * 用户登录回调
     * 为了准确记录登录用户的行为信息，建议在以下时机各调用一次 login() 方法：
     * 1、用户在注册成功时
     * 2、用户登录成功时
     * 3、已登录用户每次启动 App 时
     */
    abstract fun onUserLogin(userId: String)

    /**
     * 注册 / 注销公共事件
     */
    abstract fun registerSuperProperties(params: TrackParams)

    abstract fun registerSuperProperty(key: String, value: String)

    abstract fun unRegisterSuperProperties(key: String)

    /**
     * 事件上报
     */
    abstract fun onEvent(eventName: String, params: TrackParams)

    /**
     * 合并事件参数
     */
    abstract fun onMergeEvent(params: TrackParams): String?
}