package com.pengxr.sample.goods.vm;

import com.pengxr.sample.entity.GoodsItem;

import androidx.lifecycle.ViewModel;

/**
 * Created by pengxr on 2021/9/15.
 */
public class GoodsDetailViewModel extends ViewModel {

    private GoodsItem mGoodsItem;

    public void init(GoodsItem item) {
        this.mGoodsItem = item;
    }

    public GoodsItem getGoodsItem() {
        return mGoodsItem;
    }
}