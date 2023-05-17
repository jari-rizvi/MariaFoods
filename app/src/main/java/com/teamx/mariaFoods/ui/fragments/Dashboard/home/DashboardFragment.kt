package com.teamx.mariaFoods.ui.fragments.Dashboard.home


import android.os.Bundle
import android.view.View
import androidx.navigation.NavOptions
import androidx.navigation.navOptions
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.teamx.mariaFoods.BR
import com.teamx.mariaFoods.R
import com.teamx.mariaFoods.baseclasses.BaseFragment
import com.teamx.mariaFoods.databinding.FragmentDashboardBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardFragment : BaseFragment<FragmentDashboardBinding, Dashboard>(),OnFeatureProductListener {

    override val layoutId: Int
        get() = R.layout.fragment_dashboard
    override val viewModel: Class<Dashboard>
        get() = Dashboard::class.java
    override val bindingVariable: Int
        get() = BR.viewModel

    private lateinit var options: NavOptions
    lateinit var featureProductAdapter: FeatureProductAdapter
    lateinit var featureProductArrayList: ArrayList<Int>
    private var tabLayoutMediator: TabLayoutMediator? = null


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


    }

    private fun initializeFeatureProducts() {

        featureProductArrayList = ArrayList()
        featureProductArrayList.add(R.drawable.account)
        featureProductArrayList.add(R.drawable.account)
        featureProductArrayList.add(R.drawable.account)
        featureProductArrayList.add(R.drawable.account)
        featureProductArrayList.add(R.drawable.account)
        featureProductArrayList.add(R.drawable.account)

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
//            tab.setText("")
            mViewDataBinding.screenViewpager.setCurrentItem(tab.position, true)
        }

        tabLayoutMediator!!.attach()
    }

    override fun OnFeatureProductClickListener(position: Int) {
        TODO("Not yet implemented")
    }


}
