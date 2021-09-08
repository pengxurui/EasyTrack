package com.pengxr.sample.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


/**
 * Created by pengxr on 5/9/2021
 */
@Parcelize
class StoreDetail(

    var id: String,
    var store_name: String,

    ) : Parcelable {
}