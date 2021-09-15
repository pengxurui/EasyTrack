package com.pengxr.easytask.impl

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.pengxr.easytask.core.IPageTrackNode
import com.pengxr.easytask.core.ITrackNode
import com.pengxr.easytask.core.EasyTrack
import com.pengxr.easytask.core.TrackParams
import com.pengxr.easytask.util.getReferrerSnapshot

/**
 * Base Activity with event track，you don't have to used it.
 *
 * Created by pengxr on 10/9/2021
 */
abstract class BaseTrackActivity : AppCompatActivity, IPageTrackNode {

    constructor ()

    constructor (@LayoutRes contentLayoutId: Int) : super(contentLayoutId)

    private var referrerTrackNode: ITrackNode? = null

    protected val mTrackParams by lazy {
        TrackParams()
    }

    // ---------------------------------------------------------------------------------------------
    // Activity
    // ---------------------------------------------------------------------------------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Init referrer track node.
        getReferrerParams()?.let { referrerParams ->
            referrerTrackNode = object : ITrackNode {
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
     * Get referrer params.
     */
    fun getReferrerParams(): TrackParams? =
        intent.getReferrerSnapshot()?.let { referrerParams ->
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

    override fun referrerTrackNode(): ITrackNode? = referrerTrackNode

    @CallSuper
    override fun fillTrackParams(params: TrackParams) {
        params.merge(mTrackParams)
        // You can expose api here, it makes subclasses more convenient to pass parameters.
    }

    // ---------------------------------------------------------------------------------------------
    // protected
    // ---------------------------------------------------------------------------------------------

    private fun fillReferrerKeyMap(
        map: Map<String, String>?,
        referrerParams: TrackParams,
        params: TrackParams
    ) {
        if (null != map) {
            for ((fromKey, toKey) in map) {
                params.setIfNull(toKey, referrerParams[fromKey])
            }
        }
    }
}