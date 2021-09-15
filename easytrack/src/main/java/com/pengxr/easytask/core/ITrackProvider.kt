package com.pengxr.easytask.core

import androidx.annotation.UiThread

/**
 * Underlying statistical provider.
 *
 * Created by pengxr on 2021/8/18.
 */
abstract class ITrackProvider {

    /**
     * Enable data statistics or not.
     */
    abstract var enabled: Boolean

    /**
     * The tag of this provider.
     */
    abstract var name: String

    /**
     * Init the provider.
     */
    @UiThread
    abstract fun onInit()

    /**
     * Do event track.
     */
    abstract fun onEvent(eventName: String, params: TrackParams)
}