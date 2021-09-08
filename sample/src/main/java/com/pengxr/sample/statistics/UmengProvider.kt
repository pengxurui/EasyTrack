package com.pengxr.sample.statistics

import android.util.Log
import com.pengxr.sample.core.ITrackProvider
import com.pengxr.sample.core.TrackParams

/**
 * Mock Umeng SDK.
 * <p>
 * Created by pengxr on 5/9/2021
 */
class UmengProvider : ITrackProvider() {

    companion object {
        private const val TAG = "Umeng"

        // Mock user login status.
        private var userId: String? = null

        // Mock internal datas.
        private val data = HashMap<String, String?>()
    }

    /**
     * 是否开启数据统计
     */
    override var enabled = true

    /**
     * 初始化回调
     */
    override fun onInit() {
        Log.d(TAG, "初始化")
    }

    /**
     * 销毁回调（几乎不可能回调，仅用于实现逻辑闭环）
     */
    override fun onClear() {
        Log.d(TAG, "销毁")
    }

    /**
     * 用户登录回调
     * 为了准确记录登录用户的行为信息，建议在以下时机各调用一次 login() 方法：
     * 1、用户在注册成功时
     * 2、用户登录成功时
     * 3、已登录用户每次启动 App 时
     */
    override fun onUserLogin(userId: String) {
        UmengProvider.userId = userId
    }

    /**
     * 注册 / 注销公共事件
     */
    override fun registerSuperProperties(params: TrackParams) {
        for ((key, value) in params) {
            data[key] = value
        }
    }

    override fun registerSuperProperty(key: String, value: String) {
        data[key] = value
    }

    override fun unRegisterSuperProperties(key: String) {
        data.remove(key)
    }

    /**
     * 事件上报
     */
    override fun onEvent(eventName: String, params: TrackParams) {
        Log.d(TAG, params.toString())
    }

    /**
     * 合并事件参数
     */
    override fun onMergeEvent(params: TrackParams): String {
        return params.toString()
    }
}