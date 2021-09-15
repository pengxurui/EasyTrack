package com.pengxr.easytrack.core

import java.io.Serializable

/**
 * Track Params attach on the node
 * Created by pengxr on 2021/8/18.
 */
open class TrackParams : Iterable<Any?>, Serializable {

    /**
     * Internal data.
     */
    private val data = HashMap<String, String?>()

    /**
     * Set anywhere.
     */
    operator fun set(key: String, value: Any?): TrackParams {
        data[key] = value?.toString()
        return this
    }

    /**
     * Get by key.
     */
    operator fun get(key: String): String? = data[key]

    /**
     * Set if null.
     */
    fun setIfNull(key: String, value: Any?): TrackParams {
        val oldValue = data[key]
        if (null == oldValue) {
            data[key] = value?.toString()
        }
        return this
    }

    /**
     * Get or default.
     */
    fun get(key: String, default: String?): String? = data[key] ?: default

    /**
     * Merge other object into current object.
     */
    fun merge(other: TrackParams?): TrackParams {
        if (null != other) {
            for ((key, value) in other) {
                setIfNull(key, value)
            }
        }
        return this
    }

    /**
     * Create an iterator.
     */
    override fun iterator() = data.iterator()

    override fun toString(): String {
        return StringBuilder().apply {
            append("[")
            for ((key, value) in data) {
                append(" $key = $value ,")
            }
            deleteCharAt(this.length - 1)
            append("]")
        }.toString()
    }
}