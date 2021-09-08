package com.pengxr.sample.core

import android.util.Log
import android.view.View
import com.pengxr.sample.util.trackModel

/**
 * Created by pengxr on 2021/8/18.
 */
internal const val TAG = "StatisticsLib"

class StatisticsLib {
    companion object {

        /**
         * Underlying statistical provider.
         */
        internal var providers: MutableMap<String, ITrackProvider> = HashMap()
            private set

        internal var referrerKeyMap: Map<String, String>? = null

        internal var debug: Boolean = true

        /**
         * Register underlying statistical provider.
         */
        fun registerProvider(name: String, provider: ITrackProvider) {
            if (null == providers[name]) {
                providers[name] = provider.apply {
                    onInit()
                }
            }
        }

        /**
         * Unregister underlying statistical provider.
         */
        fun unRegisterProvider(name: String) {
            providers[name]?.also {
                it.onClear()
                providers.remove(name)
            }
        }

        /**
         * Dispatch user login event.
         */
        fun dispatchUserLogin(userId: String) {
            for ((_, provider) in providers) {
                provider.onUserLogin(userId)
            }
        }

        /**
         * Register or unregister super properties, which will be merge into every event.
         */
        fun registerSuperProperties(params: TrackParams) {
            for ((_, provider) in providers) {
                provider.registerSuperProperties(params)
            }
        }

        fun registerSuperProperty(key: String, value: String) {
            for ((_, provider) in providers) {
                provider.registerSuperProperty(key, value)
            }
        }

        fun unRegisterSuperProperties(key: String) {
            for ((_, provider) in providers) {
                provider.unRegisterSuperProperties(key)
            }
        }

        /**
         * Get underlying statistical provider with name.
         */
        fun getProvider(name: String): ITrackProvider? = providers[name]

        /**
         * Dispatch event without recursive node tree.
         */
        fun dispatchEvent(event: String, params: TrackParams) {
            for ((_, provider) in providers) {
                provider.onEvent(event, params)
            }
        }

        /**
         * 合并事件参数
         */
        fun mergeEvent(name: String, params: TrackParams): String? =
            getProvider(name)?.onMergeEvent(params)

        /**
         * 配置页面节点参数映射
         */
        fun configReferrerKeyMap(map: Map<String, String>) {
            referrerKeyMap = map
        }

        fun configDebug(debug: Boolean) {
            this.debug = debug
        }
    }
}

// -------------------------------------------------------------------------------------------------
// public
// -------------------------------------------------------------------------------------------------

internal fun Any.doTrackEvent(eventName: String, otherParams: TrackParams? = null) {
    // 1. Check whether the underlying statistical provider are available.
    if (StatisticsLib.providers.isEmpty()) {
        Log.d(TAG, "Try track event $eventName, but the providers is Empty.")
        return
    }
    // 2. Collect data recursively.
    val params = if (this is View || this is TrackNode) {
        fillTrackParams(this, otherParams)
    } else {
        otherParams
    }
    if (null == params) {
        return
    }
    // 3. Log.
    if (StatisticsLib.debug) {
        val logStr = StringBuilder().apply {
            append(" ")
            append("\nonEvent：$eventName")
            for ((key, value) in params) {
                append("\n$key = $value")
            }
            append("\n------------------------------------------------------")
        }.toString()
        Log.d(TAG, logStr)
    }
    // 4. Do event reporting.
    for ((name, provider) in StatisticsLib.providers) {
        if (!provider.enabled) {
            Log.d(TAG, "Try track event $eventName, but the provider is disabled.")
            continue
        }
        Log.d(TAG, "Try track event $eventName with provider $name.")
        provider.onEvent(eventName, params)
    }
}

/**
 * Collect data recursively
 */
internal fun fillTrackParams(node: Any?, params: TrackParams? = null): TrackParams {
    val result = params ?: TrackParams()
    var curNode = node
    outer@ while (null != curNode) {
        curNode = when (curNode) {
            is View -> {
                if (android.R.id.content == curNode.id) {
                    break@outer
                }
                // View node
                curNode.trackModel?.fillTrackParams(result)
                curNode.parent
            }
            is TrackNode -> {
                // Track Node
                curNode.fillTrackParams(result)
                curNode.parent
            }
            else -> {
                throw IllegalStateException()
            }
        }
    }
    return result
}

///**
// * 创建 Event 实例
// */
//fun View?.newTrackEvent(eventName: String): TrackEvent {
//    this?.doTrackEvent(eventName)
//}

///**
// * 创建 Event 实例
// */
//fun ITrackModel?.newTrackEvent(eventName: String): TrackEvent {
//    this?.doTrackEvent(eventName)
//}