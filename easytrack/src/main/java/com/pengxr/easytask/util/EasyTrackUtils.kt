package com.pengxr.easytask.util

import android.app.Activity
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.annotation.IdRes
import androidx.annotation.MainThread
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.RecyclerView
import com.pengxr.easytask.R
import com.pengxr.easytask.core.*
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Delegate Property of TrackNode.
 *
 * Created by pengxr on 26/8/2021
 */

private const val EXTRA_REFERRER_SNAPSHOT = "referrer_node"

// -------------------------------------------------------------------------------------------------
// Java
// -------------------------------------------------------------------------------------------------

fun trackNode(view: View): TrackModel {
    return TrackModel().apply {
        view.trackModel = this
    }
}

fun trackNode(holder: RecyclerView.ViewHolder): TrackModel {
    return TrackModel().apply {
        holder.itemView.trackModel = this
    }
}

fun trackNode(fragment: Fragment): TrackModel {
    return TrackModel().apply {
        fragment.requireView().trackModel = this
    }
}

// -------------------------------------------------------------------------------------
// Kotlin TrackNodeProperty
// -------------------------------------------------------------------------------------

fun <F : Fragment> F.track(): TrackNodeProperty<F> = FragmentTrackNodeProperty()

fun RecyclerView.ViewHolder.track(): TrackNodeProperty<RecyclerView.ViewHolder> =
    LazyTrackNodeProperty() viewFactory@{
        return@viewFactory itemView
    }

fun View.track(): TrackNodeProperty<View> = LazyTrackNodeProperty() viewFactory@{
    return@viewFactory it
}

// -------------------------------------------------------------------------------------
// TrackNodeProperty
// -------------------------------------------------------------------------------------

private const val TAG = "TrackNodeProperty"

interface TrackNodeProperty<in R : Any> : ReadOnlyProperty<R, TrackModel> {

    /**
     * 视图节点
     */
    fun getViewNode(thisRef: R): View

    /**
     * 清除
     */
    @MainThread
    fun clear()
}

class LazyTrackNodeProperty<in R : Any>(
    private val viewFactory: (R) -> View
) : TrackNodeProperty<R> {

    private var trackNode: TrackModel? = null

    @Suppress("UNCHECKED_CAST")
    @MainThread
    override fun getValue(thisRef: R, property: KProperty<*>): TrackModel {
        // Already attached
        trackNode?.let { return it }

        return TrackModel().also {
            this.trackNode = it
        }
    }

    @MainThread
    override fun clear() {
        trackNode = null
    }

    override fun getViewNode(thisRef: R) = viewFactory(thisRef)
}

abstract class LifecycleTrackNodeProperty<in R : Any> : TrackNodeProperty<R> {

    private var trackNode: TrackModel? = null

    protected abstract fun getLifecycleOwner(thisRef: R): LifecycleOwner

    @MainThread
    override fun getValue(thisRef: R, property: KProperty<*>): TrackModel {
        // Already attached
        trackNode?.let { return it }

        val lifecycle = getLifecycleOwner(thisRef).lifecycle
        val trackNode = TrackModel()
        if (lifecycle.currentState == Lifecycle.State.DESTROYED) {
            Log.w(
                TAG,
                "Access to trackNode after Lifecycle is destroyed or hasn't created yet. " + "The instance of trackNode will be not cached."
            )
            // We can access to TrackNode after Fragment.onDestroyView(), but don't save it to prevent memory leak
        } else {
            lifecycle.addObserver(ClearOnDestroyLifecycleObserver(this))
            // attach
            this.trackNode = trackNode
            getViewNode(thisRef).trackModel = trackNode
        }
        return trackNode
    }

    @MainThread
    override fun clear() {
        trackNode = null
    }

    private class ClearOnDestroyLifecycleObserver(
        private val property: LifecycleTrackNodeProperty<*>
    ) : LifecycleObserver {

        private companion object {
            private val mainHandler = Handler(Looper.getMainLooper())
        }

        @MainThread
        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestroy(owner: LifecycleOwner) {
            mainHandler.post { property.clear() }
        }
    }
}

class FragmentTrackNodeProperty<in F : Fragment> : LifecycleTrackNodeProperty<F>() {

    override fun getLifecycleOwner(thisRef: F): LifecycleOwner {
        try {
            return thisRef.viewLifecycleOwner
        } catch (ignored: IllegalStateException) {
            error("Fragment doesn't have view associated with it or the view has been destroyed")
        }
    }

    override fun getViewNode(thisRef: F) = thisRef.requireView()
}

class DialogFragmentTrackNodeProperty<in F : DialogFragment>(
) : LifecycleTrackNodeProperty<F>() {

    override fun getLifecycleOwner(thisRef: F): LifecycleOwner {
        return if (thisRef.showsDialog) {
            thisRef
        } else {
            try {
                thisRef.viewLifecycleOwner
            } catch (ignored: IllegalStateException) {
                error(
                    "Fragment doesn't have view associated with it or the view has been destroyed"
                )
            }
        }
    }

    override fun getViewNode(thisRef: F) = thisRef.requireView()
}

// -------------------------------------------------------------------------------------
// Utils
// -------------------------------------------------------------------------------------

private fun <V : View> View.requireViewByIdCompat(@IdRes id: Int): V {
    return ViewCompat.requireViewById(this, id)
}

private fun <V : View> Activity.requireViewByIdCompat(@IdRes id: Int): V {
    return ActivityCompat.requireViewById(this, id)
}

/**
 * Utility to find root view for ViewBinding in Activity
 */
private fun findRootView(activity: Activity?): View? {
    val contentView = activity?.findViewById<ViewGroup>(android.R.id.content)
    return when (contentView?.childCount) {
        1 -> contentView.getChildAt(0)
        0 -> null
        else -> null
    }
}

private fun DialogFragment.getRootView(viewBindingRootId: Int): View {
    val dialog = checkNotNull(dialog) {
        "DialogFragment doesn't have dialog. Use viewBinding delegate after onCreateDialog"
    }
    val window = checkNotNull(dialog.window) { "Fragment's Dialog has no window" }
    return with(window.decorView) {
        if (viewBindingRootId != 0) requireViewByIdCompat(
            viewBindingRootId
        ) else this
    }
}

/**
 * Params from referrer page note.
 */
fun Intent.setReferrerSnapshot(node: ITrackModel?) {
    if (null != node) {
        setReferrerSnapshot(fillTrackParams(node))
    }
}

fun Intent.setReferrerSnapshot(node: View?) {
    if (null != node) {
        setReferrerSnapshot(fillTrackParams(node))
    }
}

fun Intent.setReferrerSnapshot(params: TrackParams?) {
    if (null != params) {
        putExtra(EXTRA_REFERRER_SNAPSHOT, params)
    }
}

fun Intent.getReferrerParams(): TrackParams? {
    return getSerializableExtra(EXTRA_REFERRER_SNAPSHOT) as TrackParams?
}

/**
 * Attach track model on the view.
 */
var View.trackModel: ITrackModel?
    get() = this.getTag(R.id.tag_id_track_model) as? ITrackModel
    set(value) {
        this.setTag(R.id.tag_id_track_model, value)
    }

/**
 * Do event track, it will collect event Params around the node tree.
 */
@JvmOverloads
fun ComponentActivity?.trackEvent(eventName: String, params: TrackParams? = null) =
    findRootView(this)?.doTrackEvent(eventName, params)

@JvmOverloads
fun Fragment?.trackEvent(eventName: String, params: TrackParams? = null) =
    this?.requireView()?.doTrackEvent(eventName, params)

@JvmOverloads
fun RecyclerView.ViewHolder?.trackEvent(eventName: String, params: TrackParams? = null) {
    this?.itemView?.let {
        if (null == it.parent) {
            it.post { it.doTrackEvent(eventName, params) }
        } else {
            it.doTrackEvent(eventName, params)
        }
    }
}

@JvmOverloads
fun View?.trackEvent(eventName: String, params: TrackParams? = null): TrackParams? =
    this?.doTrackEvent(eventName, params)