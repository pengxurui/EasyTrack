package com.pengxr.easytrack.impl

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.pengxr.easytrack.core.IPageTrackNode
import com.pengxr.easytrack.core.ITrackNode
import com.pengxr.easytrack.core.EasyTrack
import com.pengxr.easytrack.core.TrackParams
import com.pengxr.easytrack.util.getReferrerParams

/**
 * Base Activity with event track，you don't have to used it.
 *
 * Created by pengxr on 10/9/2021
 */
abstract class BaseTrackActivity : AppCompatActivity, IPageTrackNode {

    constructor ()

    constructor (@LayoutRes contentLayoutId: Int) : super(contentLayoutId)

    // The snapshot of referrer page node.
    private var referrerSnapshot: ITrackNode? = null

    protected val trackParams by lazy {
        TrackParams()
    }

    // ---------------------------------------------------------------------------------------------
    // Activity
    // ---------------------------------------------------------------------------------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Snapshot for referrer page node.
        getReferrerSnapshot()?.let { referrerParams ->
            referrerSnapshot = object : ITrackNode {
                override val parent: ITrackNode? = null

                override fun fillTrackParams(params: TrackParams) {
                    params.merge(referrerParams)
                }
            }
        }
    }

    // ---------------------------------------------------------------------------------------------
    // public
    // ---------------------------------------------------------------------------------------------

    /**
     * Get params from referrer page node.
     */
    fun getReferrerSnapshot(): TrackParams? = intent.getReferrerParams()?.let { referrerParams ->
        TrackParams().apply {
            // Fill referrer params.
            fillReferrerKeyMap(referrerKeyMap(), referrerParams, this)
            fillReferrerKeyMap(EasyTrack.referrerKeyMap, referrerParams, this)
        }
    }

    // ---------------------------------------------------------------------------------------------
    // IPageTrackNode
    // ---------------------------------------------------------------------------------------------

    override fun referrerKeyMap(): Map<String, String>? = null

    override fun referrerSnapshot(): ITrackNode? = referrerSnapshot

    @CallSuper
    override fun fillTrackParams(params: TrackParams) {
        params.merge(trackParams)
        // You can expose api here, it makes subclasses more convenient to pass parameters.
    }

    // ---------------------------------------------------------------------------------------------
    // protected
    // ---------------------------------------------------------------------------------------------

    private fun fillReferrerKeyMap(
        map: Map<String, String>?, referrerParams: TrackParams, params: TrackParams
    ) {
        if (map.isNullOrEmpty()) {
            return
        }
        for ((fromKey, toKey) in map) {
            val toValue = referrerParams[fromKey]
            if (null != toValue) {
                params.setIfNull(toKey, toValue)
            }
        }
    }
}