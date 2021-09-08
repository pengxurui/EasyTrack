package com.pengxr.sample.store.vm

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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
            GoodsItem("1000", "汽车模型", "缺少电池"),
            GoodsItem("1001", "冰淇淋", "吃剩下的"),
            GoodsItem("1002", "椅子", "缺了一条腿"),
        )
    }

    fun fetchNewestGoodsList(context: Context) {
        newestGoodsList.value = listOf(
            GoodsItem("1003", "遥控器", "电视已经停产"),
            GoodsItem("1004", "潮流T恤", "破浪不堪"),
            GoodsItem("1005", "风筝", "这个样子不可能飞起来"),
        )
    }
}