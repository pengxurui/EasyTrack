package com.pengxr.sample.goods.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.pengxr.easytrack.core.TrackParams;
import com.pengxr.easytrack.util.EasyTrackUtilsKt;
import com.pengxr.sample.base.BaseActivity;
import com.pengxr.sample.databinding.GoodsDetailActivityBinding;
import com.pengxr.sample.entity.GoodsItem;
import com.pengxr.sample.goods.vm.GoodsDetailViewModel;
import com.pengxr.sample.utils.ToastUtil;
import com.pengxr.sample.utils.VMCompat;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;

import static com.pengxr.sample.statistics.EventConstants.GOODS_DETAIL_NAME;
import static com.pengxr.sample.statistics.EventConstants.GOODS_ID;
import static com.pengxr.sample.statistics.EventConstants.GOODS_NAME;
import static com.pengxr.sample.statistics.EventConstants.SHARE_CLICK_STEP1;
import static com.pengxr.sample.statistics.EventConstants.STORE_ID;
import static com.pengxr.sample.statistics.EventConstants.STORE_NAME;

/**
 * <p>
 * Created by pengxr on 6/9/2021
 */

public class GoodsDetailActivity extends BaseActivity {

    private static final String EXTRA_GOODS = "extra_goods";

    private GoodsDetailViewModel mViewModel;
    private GoodsDetailActivityBinding binding;

    public static void start(Context context, GoodsItem item, TrackParams params) {
        Intent intent = new Intent(context, GoodsDetailActivity.class);
        intent.putExtra(EXTRA_GOODS, item);
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
        map.put(STORE_NAME, STORE_NAME);
        return map;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GoodsItem item = getIntent().getParcelableExtra(EXTRA_GOODS);
        mViewModel = VMCompat.get(this, GoodsDetailViewModel.class);
        mViewModel.init(item);

        binding = GoodsDetailActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
    }

    private void init() {
        initTrack();
        initView();
    }

    private void initTrack() {
        // Add track params.
        getTrackParams().set(GOODS_ID, mViewModel.getGoodsItem().getId());
        getTrackParams().set(GOODS_NAME, mViewModel.getGoodsItem().getGoods_name());
    }

    private void initView() {
        binding.titleGoodsHome.tvTitle.setText("商品详情");
        binding.titleGoodsHome.ivBack.setVisibility(View.VISIBLE);
        binding.titleGoodsHome.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.titleGoodsHome.ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtil.toast(GoodsDetailActivity.this, "分享商品");
                EasyTrackUtilsKt.trackEvent(binding.titleGoodsHome.ivShare, SHARE_CLICK_STEP1);
            }
        });
    }
}