package com.pengxr.easytrack.core

import java.io.Serializable

/**
 * 定义数据埋点能力
 * Created by pengxr on 2021/8/18.
 */
interface ITrackModel : Serializable {

    /**
     * 数据填充
     */
    fun fillTrackParams(params: TrackParams)

//    /**
//     * 启动埋点会话
//     */
//    @AnyThread
//    fun startSession(sessionName: String): TrackSession {
//        return TrackSession().apply {
//            sessionMap[sessionName] = this
//        }
//    }
//
//    /**
//     * 移除埋点会话
//     */
//    @AnyThread
//    fun removeSession(sessionName: String) {
//        sessionMap[sessionName]?.isEnabled = false
//        sessionMap.remove(sessionName)
//    }
//
//    /**
//     * 清除埋点会话
//     */
//    @AnyThread
//    fun clear() {
//        val oldSession = sessionMap
//        sessionMap = ConcurrentHashMap<String, TrackSession>()
//
//        for ((name, session) in oldSession) {
//            session.isEnabled = false
//            oldSession.remove(name)
//        }
//    }
//
//    /**
//     * 获取埋点会话
//     */
//    @AnyThread
//    fun getSession(sessionName: String): TrackSession? = sessionMap[sessionName]
}