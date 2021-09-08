package com.pengxr.sample.goods;

import android.os.Bundle;

import com.pengxr.sample.databinding.GoodsDetailActivityBinding;
import com.pengxr.sample.core.TrackNode;
import com.pengxr.sample.util.TrackNodeUtilsKt;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import static com.pengxr.sample.statistics.EventConstants.CUR_PAGE;
import static com.pengxr.sample.statistics.EventConstants.GOODS_DETAIL_NAME;

/**
 * <p>
 * Created by pengxr on 6/9/2021
 */
public class GoodsDetailActivity extends AppCompatActivity {

    private final TrackNode trackNode = TrackNodeUtilsKt.track(this);

    private GoodsDetailActivityBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = GoodsDetailActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
    }

    private void init() {
        initTrack();
        initView();
        initObserve();

        fetchData();
    }

    private void initTrack() {
        trackNode.set(CUR_PAGE, GOODS_DETAIL_NAME);
    }

    private void initView() {
        TrackNodeUtilsKt.trackEvent(this,null,null);
    }

    private void initObserve() {

    }

    private void fetchData() {

    }
}
