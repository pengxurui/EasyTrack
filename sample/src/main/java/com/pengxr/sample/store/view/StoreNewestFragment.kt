package com.pengxr.sample.store.view

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.pengxr.easytrack.util.track
import com.pengxr.ktx.delegate.viewBinding
import com.pengxr.sample.R
import com.pengxr.sample.databinding.LayoutFragmentBinding
import com.pengxr.sample.entity.GoodsItem
import com.pengxr.sample.statistics.EventConstants.CUR_PAGE
import com.pengxr.sample.store.vm.StoreHomeViewModel
import com.pengxr.sample.store.widget.GoodsViewHolder
import com.pengxr.sample.store.widget.inflater
import com.pengxr.sample.utils.VMCompat
import com.pengxr.sample.widget.SpacingDecoration

/**
 * Created by pengxr on 11/9/2021
 */
class StoreNewestFragment : Fragment(R.layout.layout_fragment) {

    private val viewModel by lazy {
        VMCompat.get(requireActivity(), StoreHomeViewModel::class.java)
    }

    private val binding by viewBinding(LayoutFragmentBinding::bind)

    private val trackNode by track()

    private val adapter = GoodsAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        initView()
        initTrack()
    }

    private fun initView() {
        with(binding) {
            rv.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            rv.addItemDecoration(SpacingDecoration(requireContext(), 8, 8).apply {
                setOutSpacing(requireContext(), 8, 8, 8, 8)
            })
            rv.adapter = adapter
        }

        viewModel.newestGoodsList.observe(viewLifecycleOwner) { list ->
            adapter.setData(list)
        }
        viewModel.fetchNewestGoodsList(requireContext())
    }

    private fun initTrack() {
        trackNode[CUR_PAGE] = "Newest"
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