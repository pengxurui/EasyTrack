package com.pengxr.sample.core

/**
 * 抽象页面间节点
 * Created by pengxr on 2021/8/18.
 */
class PageTrackNode : TrackNode() {

    /**
     * 来源参数，用于建立跳转链路
     * 启动 App 的来源页面可以是启动页面，也可以是 Scheme / Push / DeepLink 启动参数构建的虚拟节点
     */
    var referrerTrackNode: TrackParams? = null

    /**
     * 定义参数映射
     */
    var referrerKeyMap: Map<String, String>? = null

    /**
     * 数据填充
     */
    override fun fillTrackParams(params: TrackParams) {
        // 合并当前参数
        super.fillTrackParams(params)
        // 合并来源参数
        fillReferrerKeyMap(referrerKeyMap)
        fillReferrerKeyMap(StatisticsLib.referrerKeyMap)
    }

    private fun fillReferrerKeyMap(map: Map<String, String>?) {
        if (null != map && null != referrerTrackNode) {
            for ((fromKey, toKey) in map) {
                this.setIfNull(toKey, referrerTrackNode!![fromKey])
            }
        }
    }
}