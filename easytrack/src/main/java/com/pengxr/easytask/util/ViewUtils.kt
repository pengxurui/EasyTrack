package com.pengxr.easytask.util

import android.app.Activity
import android.content.ContextWrapper
import android.view.View

/**
 * Created by pengxr on 10/9/2021
 */

/**
 * try get host activity from view.
 * views hosted on floating window like dialog and toast will sure return null.
 *
 * @return host activity; or null if not available
 */
internal fun getActivityFromView(view: View): Activity? {
    var context = view.context
    while (context is ContextWrapper) {
        if (context is Activity) {
            return context
        }
        context = context.baseContext
    }
    return null
}
