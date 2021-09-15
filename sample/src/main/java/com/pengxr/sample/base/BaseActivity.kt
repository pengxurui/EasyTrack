package com.pengxr.sample.base

import androidx.annotation.CallSuper
import com.pengxr.easytrack.core.TrackParams
import com.pengxr.easytrack.impl.BaseTrackActivity
import com.pengxr.sample.statistics.EventConstants.CUR_PAGE

/**
 * Created by pengxr on 11/9/2021
 */
abstract class BaseActivity : BaseTrackActivity() {

    @CallSuper
    override fun fillTrackParams(params: TrackParams) {
        super.fillTrackParams(params)
        getCurPage()?.also {
            params.setIfNull(CUR_PAGE, it)
        }
    }

    // ---------------------------------------------------------------------------------------------
    // protected
    // ---------------------------------------------------------------------------------------------

    protected open fun getCurPage(): String? = null
}