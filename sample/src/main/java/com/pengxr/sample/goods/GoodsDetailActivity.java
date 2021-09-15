package com.pengxr.sample.goods;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.pengxr.easytask.core.TrackParams;
import com.pengxr.easytask.util.EasyTrackUtilsKt;
import com.pengxr.sample.base.BaseActivity;
import com.pengxr.sample.databinding.GoodsDetailActivityBinding;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;

import static com.pengxr.sample.statistics.EventConstants.GOODS_DETAIL_NAME;
import static com.pengxr.sample.statistics.EventConstants.STORE_ID;

/**
 * <p>
 * Created by pengxr on 6/9/2021
 */

public class GoodsDetailActivity extends BaseActivity {

    private GoodsDetailActivityBinding binding;

    public static void start(Context context, TrackParams params) {
        Intent intent = new Intent(context, GoodsDetailActivity.class);
        EasyTrackUtilsKt.setReferrerSnapshot(intent, params);
        context.startActivity(intent);
    }

    @Nullable
    @Override
    protected String getCurPage() {
        return GOODS_DETAIL_NAME;
    }

    @Nullable
    @Override
    public Map<String, String> referrerKeyMap() {
        Map<String, String> map = new HashMap<>();
        map.put(STORE_ID, STORE_ID);
        return map;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = GoodsDetailActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
    }

    private void init() {
        initView();
        initObserve();

        fetchData();
    }

    private void initView() {
        EasyTrackUtilsKt.trackEvent(this, "", null);
    }

    private void initObserve() {

    }

    private void fetchData() {

    }
}