package com.pengxr.easytask.core

import androidx.annotation.CallSuper
import java.io.Serializable

/**
 * 数据节点
 * Created by pengxr on 2021/8/18.
 */
class TrackModel : ITrackModel, Serializable {

    protected val params by lazy {
        TrackParams()
    }

    /**
     * 设置参数，会覆盖已有参数
     */
    operator fun set(key: String, value: Any?) {
        params[key] = value
    }

    /**
     * 获取参数
     */
    operator fun get(key: String) = params[key]

    /**
     * 设置参数，不会覆盖已有参数
     */
    fun setIfNull(key: String, value: Any?) {
        params.setIfNull(key, value)
    }

    /**
     * 获取参数，为空返回默认值
     */
    fun get(key: String, default: String?) = params.get(key, default)

    /**
     * 数据填充
     */
    @CallSuper
    override fun fillTrackParams(params: TrackParams) {
        // 合并当前参数
        params.merge(this.params)
    }
}