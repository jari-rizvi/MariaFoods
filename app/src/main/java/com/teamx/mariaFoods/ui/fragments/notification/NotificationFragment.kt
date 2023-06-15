package com.teamx.mariaFoods.ui.fragments.notification

import android.os.Bundle
import android.view.View
import androidx.navigation.NavOptions
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.teamx.mariaFoods.BR
import com.teamx.mariaFoods.R
import com.teamx.mariaFoods.baseclasses.BaseFragment
import com.teamx.mariaFoods.data.dataclasses.notification.Data
import com.teamx.mariaFoods.data.remote.Resource
import com.teamx.mariaFoods.databinding.FragmentNotificationBinding
import com.teamx.mariaFoods.utils.DialogHelperClass
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationFragment : BaseFragment<FragmentNotificationBinding, NotificationViewModel>() {

    override val layoutId: Int
        get() = R.layout.fragment_notification
    override val viewModel: Class<NotificationViewModel>
        get() = NotificationViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel


    private lateinit var options: NavOptions
    lateinit var notificationAdapter: NotificationAdapter
    lateinit var notificationArrayList: ArrayList<Data>
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

        mViewDataBinding.imageView11.setOnClickListener {
            popUpStack()
        }


        mViewModel.getnotification()

        if (!mViewModel.notificationList.hasActiveObservers()) {
            mViewModel.notificationList.observe(requireActivity()) {
                when (it.status) {
                    Resource.Status.LOADING -> {
                        loadingDialog.show()
                    }
                    Resource.Status.SUCCESS -> {
                        loadingDialog.dismiss()
                        it.data?.let { data ->

                            notificationArrayList.addAll(listOf(data.data))
                            notificationAdapter.notifyDataSetChanged()

                        }
                    }
                    Resource.Status.ERROR -> {
                        loadingDialog.dismiss()
                        DialogHelperClass.errorDialog(requireContext(), it.message!!)
                    }
                }
                if (isAdded) {
                    mViewModel.notificationList.removeObservers(viewLifecycleOwner)
                }
            }
        }

        notificationRecyclerview()
    }


    private fun notificationRecyclerview() {
        notificationArrayList = ArrayList()

        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        mViewDataBinding.notificationRecycler.layoutManager = linearLayoutManager

        notificationAdapter = NotificationAdapter(notificationArrayList)
        mViewDataBinding.notificationRecycler.adapter = notificationAdapter

    }

}