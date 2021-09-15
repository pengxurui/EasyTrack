package com.pengxr.sample.store.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Outline
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import androidx.recyclerview.widget.RecyclerView
import com.pengxr.easytrack.util.track
import com.pengxr.easytrack.util.trackEvent
import com.pengxr.ktx.delegate.viewBinding
import com.pengxr.sample.R
import com.pengxr.sample.databinding.StoreHomeGoodsItemBinding
import com.pengxr.sample.entity.GoodsItem
import com.pengxr.sample.goods.view.GoodsDetailActivity
import com.pengxr.sample.statistics.EventConstants.*
import com.pengxr.sample.utils.DensityUtil

/**
 * Created by pengxr on 6/9/2021
 */

fun inflater(context: Context, parent: ViewGroup) = GoodsViewHolder(
    LayoutInflater.from(context).inflate(R.layout.store_home_goods_item, parent, false)
)

class GoodsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var mItem: GoodsItem? = null
    private val binding by viewBinding(StoreHomeGoodsItemBinding::bind)

    private val trackNode by track()

    init {
        itemView.setOnClickListener {
            mItem?.let { item ->
                itemView.trackEvent(GOODS_CLICK)?.also {
                    GoodsDetailActivity.start(itemView.context, item, it)
                }
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            itemView.outlineProvider = object : ViewOutlineProvider() {
                @SuppressLint("NewApi")
                override fun getOutline(view: View, outline: Outline) {
                    val d: Int = DensityUtil.dip2px(view.context, 6F)
                    outline.setRoundRect(0, 0, view.measuredWidth, view.measuredHeight, d.toFloat())
                }
            }
            itemView.clipToOutline = true
        }
    }

    fun bind(item: GoodsItem) {
        mItem = item
        with(binding) {
            tvTitle.text = item.goods_name
            tvContent.text = item.goods_content
            ivImage.setImageResource(item.goods_icon)

            trackNode[GOODS_ID] = item.id
            trackNode[GOODS_NAME] = item.goods_name

            trackEvent(GOODS_EXPOSE)
        }
    }
}