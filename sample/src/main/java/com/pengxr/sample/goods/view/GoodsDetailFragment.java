package com.pengxr.sample.goods.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pengxr.easytrack.core.ITrackModel;
import com.pengxr.easytrack.core.TrackParams;
import com.pengxr.easytrack.util.EasyTrackUtilsKt;
import com.pengxr.sample.R;
import com.pengxr.sample.databinding.GoodsDetailFragmentBinding;
import com.pengxr.sample.entity.GoodsItem;
import com.pengxr.sample.goods.vm.GoodsDetailViewModel;
import com.pengxr.sample.statistics.EventConstants;
import com.pengxr.sample.utils.VMCompat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static com.pengxr.sample.statistics.EventConstants.GOODS_DETAIL_CLICK;
import static com.pengxr.sample.statistics.EventConstants.GOODS_DETAIL_IMAGE_NAME;

/**
 * Created by pengxr on 10/9/2021
 */
public class GoodsDetailFragment extends Fragment implements ITrackModel {

    private GoodsDetailViewModel mViewModel;
    private GoodsDetailFragmentBinding binding;

    public GoodsDetailFragment() {
        super(R.layout.goods_detail_fragment);
    }

    @Override
    public void fillTrackParams(@NonNull TrackParams params) {
        params.set(EventConstants.CUR_PAGE, GOODS_DETAIL_IMAGE_NAME);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = GoodsDetailFragmentBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EasyTrackUtilsKt.setTrackModel(view, this);

        init();
    }

    private void init() {
        mViewModel = VMCompat.get(getActivity(), GoodsDetailViewModel.class);

        initView();
    }

    private void initView() {
        GoodsItem item = mViewModel.getGoodsItem();
        binding.ivImage.setImageResource(item.getGoods_icon());
        binding.tvTitle.setText(item.getGoods_name());
        binding.tvContent.setText(item.getGoods_content());

        binding.ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EasyTrackUtilsKt.trackEvent(view, GOODS_DETAIL_CLICK);
            }
        });
    }
}