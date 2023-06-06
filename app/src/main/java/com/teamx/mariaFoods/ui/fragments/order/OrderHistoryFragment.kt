package com.teamx.mariaFoods.ui.fragments.order

import android.os.Bundle
import android.view.View
import androidx.navigation.NavOptions
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.teamx.mariaFoods.BR
import com.teamx.mariaFoods.R
import com.teamx.mariaFoods.baseclasses.BaseFragment
import com.teamx.mariaFoods.data.remote.Resource
import com.teamx.mariaFoods.databinding.FragmentOrderHistoryBinding
import com.teamx.mariaFoods.utils.DialogHelperClass
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderHistoryFragment :
    BaseFragment<FragmentOrderHistoryBinding, OrderHistoryViewModel>(),OnOrderListener {

    override val layoutId: Int
        get() = R.layout.fragment_order_history
    override val viewModel: Class<OrderHistoryViewModel>
        get() = OrderHistoryViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel


    private lateinit var options: NavOptions
    lateinit var orderAdapter: OrderListAdapter
    lateinit var orderArrayList: ArrayList<com.teamx.mariaFoods.data.dataclasses.orderHistory.Data>
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewDataBinding.lifecycleOwner = viewLifecycleOwner

        options = navOptions {
            anim {
                enter = R.anim.enter_from_left
                exit = R.anim.exit_to_left
                popEnter = R.anim.nav_default_pop_enter_anim
                popExit = R.anim.nav_default_pop_exit_anim
            }
        }

        mViewModel.getOrder()

        mViewModel.orderList.observe(requireActivity()) {
            when (it.status) {
                Resource.Status.LOADING -> {
                    loadingDialog.show()
                }
                Resource.Status.SUCCESS -> {
                    loadingDialog.dismiss()
                    it.data?.let { data ->

                        orderArrayList.addAll(data.data)
                        orderAdapter.notifyDataSetChanged()
//                        it.let {
//                            orderArrayList.clear()
//                            orderArrayList.addAll(it.data.get(0).path)
//                            orderAdapter.notifyDataSetChanged()                        }

                    }
                }
                Resource.Status.ERROR -> {
                    loadingDialog.dismiss()
                    DialogHelperClass.errorDialog(requireContext(), it.message!!)
                }
            }
            if (isAdded) {
                mViewModel.orderList.removeObservers(viewLifecycleOwner)
            }
        }
        orderRecyclerview()

    }
    private fun orderRecyclerview() {
        orderArrayList = ArrayList()

        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        mViewDataBinding.orderRecycler.layoutManager = linearLayoutManager

        orderAdapter = OrderListAdapter(orderArrayList, this)
        mViewDataBinding.orderRecycler.adapter = orderAdapter

    }


    override fun oneOrderClick(position: Int) {

    }


}