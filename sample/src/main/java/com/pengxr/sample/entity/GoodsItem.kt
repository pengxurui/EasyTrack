package com.pengxr.sample.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by pengxr on 6/9/2021
 */
@Parcelize
class GoodsItem(
    var id: String,
    var goods_name: String,
    var goods_content: String,
    ) : Parcelable {
}