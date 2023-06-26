package com.teamx.mariaFoods.ui.fragments.order

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.teamx.mariaFoods.BR
import com.teamx.mariaFoods.R
import com.teamx.mariaFoods.baseclasses.BaseFragment
import com.teamx.mariaFoods.constants.NetworkCallPoints
import com.teamx.mariaFoods.data.dataclasses.notificationModel.DataExtented1
import com.teamx.mariaFoods.data.dataclasses.notificationModel.Item1
import com.teamx.mariaFoods.data.dataclasses.notificationModel.Jari1
import com.teamx.mariaFoods.data.remote.Resource
import com.teamx.mariaFoods.databinding.FragmentOrderHistoryBinding
import com.teamx.mariaFoods.utils.DialogHelperClass
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject

@AndroidEntryPoint
class OrderHistoryFragment :
    BaseFragment<FragmentOrderHistoryBinding, OrderHistoryViewModel>(), OnOrderListener{

    override val layoutId: Int
        get() = R.layout.fragment_order_history
    override val viewModel: Class<OrderHistoryViewModel>
        get() = OrderHistoryViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel


    private lateinit var options: NavOptions
    lateinit var orderAdapter: OrderAdapter
    lateinit var orderArrayList: ArrayList<DataExtented1>

    lateinit var dayArrayList: ArrayList<Jari1>
    var token: String? = null


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
        mViewDataBinding.btnBack.setOnClickListener {
            popUpStack()
        }

        if (isAdded) {
            CoroutineScope(Dispatchers.Main).launch {

                dataStoreProvider.token.collect {
                    Log.d("Databsae Token", "CoroutineScope ${it}")

                    Log.d("dataStoreProvider", "subscribeToNetworkLiveData: $it")

                    token = it

                    NetworkCallPoints.TOKENER = token.toString()

                    if (isAdded) {
                        if (token.isNullOrBlank()) {
                            Log.d("Databsae Token", "token ${token}")
//                            navController = Navigation.findNavController(
//                                requireActivity(),
//                                R.id.nav_host_fragment
//                            )
//                            navController.navigate(R.id.dashboardFragment, null, options)

                        } else {

                            mViewModel.getOrder()

                            if (!mViewModel.orderList.hasActiveObservers()) {
                                mViewModel.orderList.observe(requireActivity()) {
                                    when (it.status) {
                                        Resource.Status.LOADING -> {
                                            loadingDialog.show()
                                        }
                                        Resource.Status.SUCCESS -> {
                                            loadingDialog.dismiss()
                                            it.data?.let { data ->

                                                Log.d("TAG", "onViewCreated33333: $data")

                                                orderArrayList.clear()


                                                val jsonObject = JSONObject(data.toString())

                                                try {
                                                    val data = jsonObject.getJSONObject("data")
                                                    val a: ArrayList<String> = ArrayList()
                                                    var counter = 0

                                                    val stringIterator: Iterator<String> = data.keys()
                                                    while (stringIterator.hasNext()) {
                                                        a.add(stringIterator.next())
                                                        Log.d("TAG", "onViewCreated33333: ${a.size}")
                                                        Log.d("TAG", "onViewCreated: ${a.get(0)}")
                                                    }

                                                    a.forEach {
                                                        val object1 = data.getJSONArray(it)
                                                        val jari = ArrayList<Jari1>()
                                                        for (i in 0..object1.length() - 1) {
                                                            val items = JSONObject(object1[i].toString())
                                                            jari.add(
                                                                Jari1(
                                                                    id = items.getInt("id"),
                                                                    name = items.getJSONObject("product").getString("name"),
                                                                    price = items.getJSONObject("product").getDouble("max_price").toString(),
                                                                    quantity = items.getInt("order_quantity").toString(),
                                                                    created_at = items.getString("created_at").toString(),
                                                                    delivery_status = items.getString("delivery_status").toString()
                                                                )
                                                            )
                                                        }

                                                        Log.d("TAG", "onViewCreated123123222: ${jari.size}")
                                                        orderArrayList.add(DataExtented1(Item1("$it", jari)))
                                                        counter++

                                                    }
                                                } catch (e: Exception) {
                                                    e.printStackTrace()
                                                }


                                                orderAdapter.notifyDataSetChanged()


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
                            }

                        }
                    }

                }


            }


        }



        orderRecyclerview()

    }
    private fun orderRecyclerview() {
        orderArrayList = ArrayList()

        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        mViewDataBinding.orderRecycler.layoutManager = linearLayoutManager

        orderAdapter = OrderAdapter(orderArrayList, this)
        mViewDataBinding.orderRecycler.adapter = orderAdapter

    }

    override fun oneReorderClick(position: Int) {
        navController = Navigation.findNavController(
            requireActivity(), R.id.nav_host_fragment
        )
        navController.navigate(R.id.dashboardFragment, null, options)

    }

    override fun oneCancelOrderClick(position: Int) {
        showToast("asas")


        val params = JsonObject()
        try {
            params.addProperty("order_id", position)
        } catch (e: JSONException) {
            e.printStackTrace()
        }


        mViewModel.cancelOrder(params)

            mViewModel.cancelOrder.observe(requireActivity()) {
                when (it.status) {
                    Resource.Status.LOADING -> {
                        loadingDialog.show()
                    }
                    Resource.Status.SUCCESS -> {
                        loadingDialog.dismiss()
                        it.data?.let { data ->
                            if (data.Flag == 1) {
                                mViewModel.getOrder()
                                data.Message?.let { it1 -> showToast(it1) }


                            } else {
                                data.Message?.let { it1 -> showToast(it1) }
                            }

                        }
                    }
                    Resource.Status.ERROR -> {
                        loadingDialog.dismiss()
                        DialogHelperClass.errorDialog(requireContext(), it.message!!)
                    }
                }
                if (isAdded) {
                    mViewModel.cancelOrder.removeObservers(viewLifecycleOwner)
                }
            }
        }

    }

