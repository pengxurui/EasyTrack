package com.pengxr.sample.util

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
import com.pengxr.sample.R
import com.pengxr.sample.core.*
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Delegate Property of TrackNode.
 *
 * Created by pengxr on 26/8/2021
 */

private const val EXTRA_REFERRER_NODE = "referrer_node"

// -------------------------------------------------------------------------------------------------
// Java
// -------------------------------------------------------------------------------------------------

fun track(view: View): TrackNode {
    return TrackNode().apply {
        view.trackModel = this
    }
}

fun track(holder: RecyclerView.ViewHolder): TrackNode {
    return TrackNode().apply {
        holder.itemView.trackModel = this
    }
}

fun track(fragment: Fragment): TrackNode {
    return TrackNode().apply {
        fragment.requireView().trackModel = this
    }
}

fun track(activity: ComponentActivity): TrackNode {
    return PageTrackNode().apply {
        activity.intent.getReferrerTrackNode()?.also {
            this.referrerTrackNode = it
        }
        findRootView(activity).trackModel = this
    }
}

// -------------------------------------------------------------------------------------
// Kotlin TrackNodeProperty
// -------------------------------------------------------------------------------------

fun ComponentActivity.track(trackMode: (() -> MutableMap<String, String>?)? = null): TrackNodeProperty<ComponentActivity, PageTrackNode> =
    ActivityTrackNodeProperty factory@{ _: ComponentActivity ->
        return@factory PageTrackNode().apply {
            // 来源参数
            intent.getReferrerTrackNode()?.also {
                this.referrerTrackNode = it
            }
            // 页面参数映射
            this.referrerKeyMap = trackMode?.invoke()
        }
    }

fun <F : Fragment> F.track(trackMode: ((TrackParams) -> Unit)? = null): TrackNodeProperty<F, TrackNode> =
    FragmentTrackNodeProperty factory@{ _: Fragment ->
        return@factory object : TrackNode() {
            // Collect data.
            override fun fillTrackParams(params: TrackParams) {
                super.fillTrackParams(params)
                trackMode?.invoke(params)
            }
        }
    }

fun RecyclerView.ViewHolder.track(trackMode: ((TrackParams) -> Unit)? = null): TrackNodeProperty<RecyclerView.ViewHolder, TrackNode> =
    LazyTrackNodeProperty(noteFactory@{ _: RecyclerView.ViewHolder ->
        object : TrackNode() {
            // Collect data.
            override fun fillTrackParams(params: TrackParams) {
                super.fillTrackParams(params)
                trackMode?.invoke(params)
            }
        }
    }) viewFactory@{
        return@viewFactory itemView
    }

fun View.track(trackMode: ((TrackParams) -> Unit)? = null): TrackNodeProperty<View, TrackNode> =
    LazyTrackNodeProperty(noteFactory@{ _: View ->
        object : TrackNode() {
            // Collect data.
            override fun fillTrackParams(params: TrackParams) {
                super.fillTrackParams(params)
                trackMode?.invoke(params)
            }
        }
    }) viewFactory@{
        return@viewFactory it
    }

// -------------------------------------------------------------------------------------
// TrackNodeProperty
// -------------------------------------------------------------------------------------

private const val TAG = "TrackNodeProperty"

interface TrackNodeProperty<in R : Any, out T : TrackNode> : ReadOnlyProperty<R, T> {

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

class LazyTrackNodeProperty<in R : Any, out T : TrackNode>(
    private val nodeFactory: (R) -> T, private val viewFactory: (R) -> View
) : TrackNodeProperty<R, T> {

    private var trackNode: T? = null

    @Suppress("UNCHECKED_CAST")
    @MainThread
    override fun getValue(thisRef: R, property: KProperty<*>): T {
        // Already attached
        trackNode?.let { return it }

        return nodeFactory(thisRef).also {
            this.trackNode = it
        }
    }

    @MainThread
    override fun clear() {
        trackNode = null
    }

    override fun getViewNode(thisRef: R) = viewFactory(thisRef)
}

abstract class LifecycleTrackNodeProperty<in R : Any, out T : TrackNode>(
    private val nodeFactory: (R) -> T
) : TrackNodeProperty<R, T> {

    private var trackNode: T? = null

    protected abstract fun getLifecycleOwner(thisRef: R): LifecycleOwner

    @MainThread
    override fun getValue(thisRef: R, property: KProperty<*>): T {
        // Already attached
        trackNode?.let { return it }

        val lifecycle = getLifecycleOwner(thisRef).lifecycle
        val trackNode = nodeFactory(thisRef)
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
        private val property: LifecycleTrackNodeProperty<*, *>
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

class ActivityTrackNodeProperty<in A : ComponentActivity, out T : TrackNode>(
    nodeFactory: (A) -> T
) : LifecycleTrackNodeProperty<A, T>(nodeFactory) {

    override fun getLifecycleOwner(thisRef: A): LifecycleOwner {
        return thisRef
    }

    override fun getViewNode(thisRef: A) = findRootView(thisRef)
}

class FragmentTrackNodeProperty<in F : Fragment, out T : TrackNode>(
    nodeFactory: (F) -> T
) : LifecycleTrackNodeProperty<F, T>(nodeFactory) {

    override fun getLifecycleOwner(thisRef: F): LifecycleOwner {
        try {
            return thisRef.viewLifecycleOwner
        } catch (ignored: IllegalStateException) {
            error("Fragment doesn't have view associated with it or the view has been destroyed")
        }
    }

    override fun getViewNode(thisRef: F) = thisRef.requireView()
}

class DialogFragmentTrackNodeProperty<in F : DialogFragment, out V : TrackNode>(
    nodeFactory: (F) -> V
) : LifecycleTrackNodeProperty<F, V>(nodeFactory) {

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

fun <V : View> View.requireViewByIdCompat(@IdRes id: Int): V {
    return ViewCompat.requireViewById(this, id)
}

fun <V : View> Activity.requireViewByIdCompat(@IdRes id: Int): V {
    return ActivityCompat.requireViewById(this, id)
}

/**
 * Utility to find root view for ViewBinding in Activity
 */
fun findRootView(activity: Activity): View {
    val contentView = activity.findViewById<ViewGroup>(android.R.id.content)
    checkNotNull(contentView) { "Activity has no content view" }
    return when (contentView.childCount) {
        1 -> contentView.getChildAt(0)
        0 -> error("Content view has no children. Provide root view explicitly")
        else -> error("More than one child view found in Activity content view")
    }
}

fun DialogFragment.getRootView(viewBindingRootId: Int): View {
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
 * 制作当前页面的快照节点，传递给后续页面
 */
fun Intent.setReferrerTrackNode(node: TrackNode?) {
    if (null != node) {
        setReferrerTrackNode(fillTrackParams(node))
    }
}

fun Intent.setReferrerTrackNode(node: View?) {
    if (null != node) {
        setReferrerTrackNode(fillTrackParams(node))
    }
}

fun Intent.setReferrerTrackNode(params: TrackParams?) {
    if (null != params) {
        putExtra(EXTRA_REFERRER_NODE, params)
    }
}

/**
 * 获取上个页面的快照节点
 */
fun Intent.getReferrerTrackNode(): TrackParams? {
    return getSerializableExtra(EXTRA_REFERRER_NODE) as TrackParams?
}

/**
 * 依附在 View 上的数据节点
 */
var View.trackModel: ITrackModel?
    get() = this.getTag(R.id.tag_id_track_model) as? ITrackModel
    set(value) {
        this.setTag(R.id.tag_id_track_model, value)
    }

/**
 * 事件上报
 */
fun ComponentActivity?.trackEvent(eventName: String, params: TrackParams? = null) {
    this?.let {
        findRootView(it).doTrackEvent(eventName, params)
    }
}

fun Fragment?.trackEvent(eventName: String, params: TrackParams? = null) {
    this?.requireView()?.doTrackEvent(eventName, params)
}

fun RecyclerView.ViewHolder?.trackEvent(eventName: String, params: TrackParams? = null) {
    this?.itemView?.let {
        if (null == it.parent) {
            it.post { doTrackEvent(eventName, params) }
        } else {
            doTrackEvent(eventName, params)
        }
    }
}

fun View?.trackEvent(eventName: String, params: TrackParams? = null) {
    this?.doTrackEvent(eventName, params)
}

fun ITrackModel?.trackEvent(eventName: String, params: TrackParams? = null) {
    this?.doTrackEvent(eventName, params)
}

//fun Any?.trackEvent(eventName: String, params: TrackParams? = null) {
//    this?.doTrackEvent(eventName, params)
//}