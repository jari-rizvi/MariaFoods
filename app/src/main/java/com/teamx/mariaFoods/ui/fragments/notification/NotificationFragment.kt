package com.teamx.mariaFoods.ui.fragments.notification

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.NavOptions
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.teamx.mariaFoods.BR
import com.teamx.mariaFoods.R
import com.teamx.mariaFoods.baseclasses.BaseFragment
import com.teamx.mariaFoods.data.dataclasses.notificationModel.DataExtented
import com.teamx.mariaFoods.data.dataclasses.notificationModel.Item
import com.teamx.mariaFoods.data.dataclasses.notificationModel.Jari
import com.teamx.mariaFoods.data.remote.Resource
import com.teamx.mariaFoods.databinding.FragmentNotificationBinding
import com.teamx.mariaFoods.utils.DialogHelperClass
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject

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
    lateinit var notificationArrayList: ArrayList<DataExtented>
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

                            notificationArrayList.clear()


                            val jsonObject = JSONObject(data.toString())


                            try {
                                val data = jsonObject.getJSONObject("data")
                                val a: ArrayList<String> = ArrayList()
                                var counter = 0

                                val stringIterator: Iterator<String> = data.keys()
                                while (stringIterator.hasNext()) {
                                    a.add(stringIterator.next())
                                    Log.d("TAG", "onViewCreated: ${a.size}")
                                    Log.d("TAG", "onViewCreated: ${a.get(0)}")
                                }

                                a.forEach {
                                    val object1 = data.getJSONArray(it)
                                    val jari = ArrayList<Jari>()
                                    for (i in 0..object1.length() - 1) {
                                        val items = JSONObject(object1[i].toString())
                                        jari.add(
                                            Jari(
                                                title = items.getString("title"),
                                                body = items.getString("body"),
                                                time = items.getString("time"),
                                            )
                                        )
                                    }
                                    Log.d("TAG", "onViewCreated123123222: ${jari.size}")
                                    notificationArrayList.add(DataExtented(Item("$it", jari)))
                                    counter++

                                }
                            } catch (e: Exception) {

                            }


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