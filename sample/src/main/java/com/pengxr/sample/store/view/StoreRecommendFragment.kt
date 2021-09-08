package com.pengxr.sample.store.view

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.pengxr.sample.R
import com.pengxr.sample.databinding.LayoutFragmentBinding
import com.pengxr.sample.entity.GoodsItem
import com.pengxr.sample.statistics.EventConstants.CUR_TAB
import com.pengxr.sample.store.vm.StoreHomeViewModel
import com.pengxr.sample.store.widget.GoodsViewHolder
import com.pengxr.sample.store.widget.inflater
import com.pengxr.sample.utils.VMCompat
import com.pengxr.ktx.delegate.viewBinding
import com.pengxr.sample.util.track

/**
 * Created by pengxr on 5/9/2021
 */
class StoreRecommendFragment : Fragment(R.layout.layout_fragment) {

    private val viewModel by lazy {
        VMCompat.get(requireActivity(), StoreHomeViewModel::class.java)
    }

    private val binding by viewBinding(LayoutFragmentBinding::bind)

    // todo
    private val trackNode by track {
        it[CUR_TAB] = "推荐"
    }

    private val adapter = GoodsAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding) {
            rv.layoutManager = StaggeredGridLayoutManager(2, VERTICAL)
            rv.addItemDecoration(SpacingDecoration(requireContext(), 8, 8).apply {
                setOutSpacing(requireContext(), 8, 15, 8, 15)
            })
            rv.adapter = adapter
        }

        viewModel.recommendGoodsList.observe(viewLifecycleOwner) { list ->
            adapter.setData(list)
        }
        viewModel.fetchRecommendGoodsList(requireContext())
//        trackNode["test"] = "test"
    }

    private class GoodsAdapter : RecyclerView.Adapter<GoodsViewHolder>() {

        private val data = ArrayList<GoodsItem>()

        fun setData(data: List<GoodsItem>?) {
            this.data.clear()
            data?.let {
                this.data.addAll(data)
            }
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            inflater(parent.context, parent)

        override fun onBindViewHolder(holder: GoodsViewHolder, position: Int) =
            holder.bind(data[position])

        override fun getItemCount() = data.size
    }
}