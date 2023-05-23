package com.teamx.mariaFoods.ui.fragments.Dashboard.home


import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.navigation.NavOptions
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.teamx.mariaFoods.BR
import com.teamx.mariaFoods.R
import com.teamx.mariaFoods.baseclasses.BaseFragment
import com.teamx.mariaFoods.data.dataclasses.banners.Data
import com.teamx.mariaFoods.data.remote.Resource
import com.teamx.mariaFoods.databinding.FragmentDashboardBinding
import com.teamx.mariaFoods.utils.DialogHelperClass
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Math.abs

@AndroidEntryPoint
class DashboardFragment : BaseFragment<FragmentDashboardBinding, Dashboard>(),
    OnFeatureProductListener, OnProductListener {

    override val layoutId: Int
        get() = R.layout.fragment_dashboard
    override val viewModel: Class<Dashboard>
        get() = Dashboard::class.java
    override val bindingVariable: Int
        get() = BR.viewModel

    private lateinit var options: NavOptions
    lateinit var featureProductAdapter: FeatureProductAdapter
    lateinit var featureProductArrayList: ArrayList<Data>
    private var tabLayoutMediator: TabLayoutMediator? = null


    lateinit var productAdapter: ProductAdapter
    lateinit var productArrayList: ArrayList<com.teamx.mariaFoods.data.dataclasses.products.Data>

    private lateinit var handler: Handler

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        options = navOptions {
            anim {
                enter = R.anim.enter_from_left
                exit = R.anim.exit_to_left
                popEnter = R.anim.nav_default_pop_enter_anim
                popExit = R.anim.nav_default_pop_exit_anim
            }
        }

        initializeFeatureProducts()
        productRecyclerview()

        mViewModel.bannerList()

        mViewModel.bannerList.observe(requireActivity()) {
            when (it.status) {
                Resource.Status.LOADING -> {
                    loadingDialog.show()
                }
                Resource.Status.SUCCESS -> {
                    loadingDialog.dismiss()
                    it.data?.let { data ->

                        featureProductArrayList.addAll(data.data)
                        featureProductAdapter.notifyDataSetChanged()
//                        it.let {
//                            featureProductArrayList.clear()
//                            featureProductArrayList.addAll(it.data.get(0).path)
//                            featureProductAdapter.notifyDataSetChanged()                        }

                    }
                }
                Resource.Status.ERROR -> {
                    loadingDialog.dismiss()
                    DialogHelperClass.errorDialog(requireContext(), it.message!!)
                }
            }
            if (isAdded) {
                mViewModel.bannerList.removeObservers(viewLifecycleOwner)
            }
        }

        mViewModel.getProducts()

        mViewModel.products.observe(requireActivity()) {
            when (it.status) {
                Resource.Status.LOADING -> {
                    loadingDialog.show()
                }
                Resource.Status.SUCCESS -> {
                    loadingDialog.dismiss()
                    it.data?.let { data ->

                        productArrayList.addAll(data.data)
                        productAdapter.notifyDataSetChanged()


                    }
                }
                Resource.Status.ERROR -> {
                    loadingDialog.dismiss()
                    DialogHelperClass.errorDialog(requireContext(), it.message!!)
                }
            }
            if (isAdded) {
                mViewModel.bannerList.removeObservers(viewLifecycleOwner)
            }
        }
        setUpTransformer()

    }

    private fun initializeFeatureProducts() {

        featureProductArrayList = ArrayList()

        featureProductAdapter = FeatureProductAdapter(featureProductArrayList, this)
        mViewDataBinding.screenViewpager.adapter = featureProductAdapter

        TabLayoutMediator(
            mViewDataBinding.tabIndicator, mViewDataBinding.screenViewpager
        ) { tab, position ->
            tab.text = featureProductArrayList[position].toString()
        }.attach()

        tabLayoutMediator = TabLayoutMediator(
            mViewDataBinding.tabIndicator, mViewDataBinding.screenViewpager
        ) { tab: TabLayout.Tab, position: Int ->
            mViewDataBinding.screenViewpager.setCurrentItem(tab.position, true)
        }
        tabLayoutMediator!!.attach()

        mViewDataBinding.screenViewpager.offscreenPageLimit = 3
        mViewDataBinding.screenViewpager.clipToPadding = false
        mViewDataBinding.screenViewpager.clipChildren = false
        mViewDataBinding.screenViewpager.getChildAt(0).overScrollMode =
            RecyclerView.OVER_SCROLL_NEVER

        handler = Handler(Looper.myLooper()!!)

        mViewDataBinding.screenViewpager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                handler.removeCallbacks(runnable)
                handler.postDelayed(runnable, 2000)
            }
        })


    }

    private fun productRecyclerview() {
        productArrayList = ArrayList()

        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        mViewDataBinding.productRecycler.layoutManager = linearLayoutManager

        productAdapter = ProductAdapter(productArrayList, this)
        mViewDataBinding.productRecycler.adapter = productAdapter

    }

    override fun OnFeatureProductClickListener(position: Int) {
        TODO("Not yet implemented")
    }

    override fun onproductClick(position: Int) {
        TODO("Not yet implemented")

    }

    override fun onPause() {
        super.onPause()

        handler.removeCallbacks(runnable)
    }

    override fun onResume() {
        super.onResume()

        handler.postDelayed(runnable, 2000)
    }

    private val runnable = Runnable {
        mViewDataBinding.screenViewpager.currentItem =
            mViewDataBinding.screenViewpager.currentItem + 1
    }

    private fun setUpTransformer() {
        val transformer = CompositePageTransformer()
        transformer.addTransformer(MarginPageTransformer(40))
        transformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = 0.85f + r * 0.14f
        }

        mViewDataBinding.screenViewpager.setPageTransformer(transformer)
    }
}
