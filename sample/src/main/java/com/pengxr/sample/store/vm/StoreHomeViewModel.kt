package com.pengxr.sample.store.vm

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pengxr.sample.R
import com.pengxr.sample.entity.GoodsItem
import com.pengxr.sample.entity.StoreDetail

/**
 * Created by pengxr on 5/9/2021
 */
class StoreHomeViewModel : ViewModel() {

    val storeDetailLiveData = MutableLiveData<StoreDetail>()

    val recommendGoodsList = MutableLiveData<List<GoodsItem>>()

    val newestGoodsList = MutableLiveData<List<GoodsItem>>()

    val storeDetail: StoreDetail?
        get() = storeDetailLiveData.value

    fun fetchData(context: Context) {
        storeDetailLiveData.value = StoreDetail("10000", "小熊商店")
    }

    fun fetchRecommendGoodsList(context: Context) {
        recommendGoodsList.value = listOf(
            GoodsItem("1000", "自行车", "没有轮子的自行车", R.drawable.icon_bike),
            GoodsItem("1001", "冰淇淋", "已融化的冰淇淋", R.drawable.icon_icecream),
            GoodsItem("1002", "吉他", "断了弦的吉他", R.drawable.icon_guita),
            GoodsItem("1003", "包子", "吃剩下的包子", R.drawable.icon_baozi),
            GoodsItem("1004", "猫", "会咬人的猫", R.drawable.icon_cat),
            GoodsItem("1005", "面条", "隔夜的面条", R.drawable.icon_noodle)
        )
    }

    fun fetchNewestGoodsList(context: Context) {
        newestGoodsList.value = listOf(
            GoodsItem("1000", "自行车", "没有轮子的自行车", R.drawable.icon_bike),
            GoodsItem("1001", "冰淇淋", "已融化的冰淇淋", R.drawable.icon_icecream),
            GoodsItem("1002", "吉他", "断了弦的吉他", R.drawable.icon_guita),
            GoodsItem("1003", "包子", "吃剩下的包子", R.drawable.icon_baozi),
            GoodsItem("1004", "猫", "会咬人的猫", R.drawable.icon_cat),
            GoodsItem("1005", "面条", "隔夜的面条", R.drawable.icon_noodle)
        )
    }
}