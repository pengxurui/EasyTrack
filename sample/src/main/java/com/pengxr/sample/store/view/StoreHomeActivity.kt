package com.pengxr.sample.store.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentPagerAdapter
import com.pengxr.easytask.util.trackEvent
import com.pengxr.ktx.delegate.viewBinding
import com.pengxr.sample.R
import com.pengxr.sample.base.BaseActivity
import com.pengxr.sample.databinding.StoreHomeActivityBinding
import com.pengxr.sample.statistics.EventConstants.*
import com.pengxr.sample.store.vm.StoreHomeViewModel
import com.pengxr.sample.utils.ToastUtil
import com.pengxr.sample.utils.VMCompat

/**
 * Created by pengxr on 5/9/2021
 */
class StoreHomeActivity : BaseActivity() {

    private val viewModel by lazy {
        VMCompat.get(this, StoreHomeViewModel::class.java)
    }

    private val binding by viewBinding(StoreHomeActivityBinding::bind)

    override fun getCurPage() = STORE_HOME_NAME

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.store_home_activity)

        init()
    }

    private fun init() {
        initView()
        initObserve()

        fetchData()
    }

    private fun initView() {
        with(binding) {
            titleStoreHome.ivBack.visibility = View.INVISIBLE
            titleStoreHome.ivShare.setOnClickListener { ivShare ->
                viewModel.storeDetail?.let { detail ->
                    ToastUtil.toast(this@StoreHomeActivity, "分享商店：${detail.store_name}")
                    ivShare.trackEvent(SHARE_CLICK_STEP1)
                }
            }
            val titles = arrayOf("推荐","最新")
            val fragments = arrayOf(StoreRecommendFragment(),StoreNewestFragment())

            for (title in titles) {
                tabStoreHome.addTab(tabStoreHome.newTab().setText(title))
            }
            pagerStoreHome.adapter = object : FragmentPagerAdapter(supportFragmentManager) {
                override fun getCount() = fragments.size

                override fun getItem(position: Int) = fragments[position]

                override fun getPageTitle(position: Int) = titles[position]
            }
            tabStoreHome.setupWithViewPager(pagerStoreHome, false)
        }
    }

    private fun initObserve() {
        viewModel.storeDetailLiveData.observe(this) { detail ->
            // Add track params.
            mTrackParams[STORE_ID] = detail.id
            mTrackParams[STORE_NAME] = detail.store_name
            binding.titleStoreHome.tvTitle.text = detail.store_name
        }
    }

    private fun fetchData() {
        viewModel.fetchData(this)
    }
}