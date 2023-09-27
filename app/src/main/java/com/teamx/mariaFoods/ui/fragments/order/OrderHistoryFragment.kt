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
import com.teamx.mariaFoods.data.dataclasses.notificationModel.HelperOrderList
import com.teamx.mariaFoods.data.dataclasses.notificationModel.MainDateOrderList
import com.teamx.mariaFoods.data.dataclasses.notificationModel.MainOrderList
import com.teamx.mariaFoods.data.dataclasses.notificationModel.ProductOrderList
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
    BaseFragment<FragmentOrderHistoryBinding, OrderHistoryViewModel>(), OnOrderListener {

    override val layoutId: Int
        get() = R.layout.fragment_order_history
    override val viewModel: Class<OrderHistoryViewModel>
        get() = OrderHistoryViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel


    private lateinit var options: NavOptions
    lateinit var orderAdapter: OrderAdapter
    lateinit var orderArrayList: ArrayList<MainDateOrderList>

    lateinit var dayArrayList: ArrayList<HelperOrderList>
    var token: String? = null

    val helperOrderLists = ArrayList<HelperOrderList>()
    val productOrderList: ArrayList<ProductOrderList> = ArrayList()
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

                    token = it

                    NetworkCallPoints.TOKENER = token.toString()

                    if (isAdded) {
                        if (token.isNullOrBlank()) {
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


                                                orderArrayList.clear()


                                                val jsonObject = JSONObject(data.toString())

                                                try {
                                                    val data = jsonObject.getJSONObject("data")
                                                    val a: ArrayList<String> = ArrayList()

                                                    var counter = 0

                                                    val stringIterator: Iterator<String> =
                                                        data.keys()
                                                    while (stringIterator.hasNext()) {
                                                        a.add(stringIterator.next())

                                                    }

                                                    a.forEach {
                                                        val object1 = data.getJSONArray(it)

                                                        helperOrderLists.clear()
                                                        for (mission in 0 until object1.length()) {
                                                            val items =
                                                                JSONObject(object1[mission].toString())
                                                            val productOrderList1 =
                                                                items.getJSONArray("products")


                                                            for (jk in 0 until productOrderList1.length()) {
                                                                val items1 =
                                                                    JSONObject(productOrderList1[jk].toString())

                                                                productOrderList.add(
                                                                    ProductOrderList(
                                                                        name = items1.getString("name"),
                                                                        quantity = items1.getDouble(
                                                                            "qty"
                                                                        )
                                                                            .toString(),
                                                                        price = items1.getDouble("max_price")
                                                                            .toString()
                                                                    )

                                                                )
                                                            }


                                                            helperOrderLists.add(
                                                                HelperOrderList(
                                                                    total = items.getString("Total"),
                                                                    id = items.getInt("id"),
                                                                    orderId = items.getString("orderId"),
                                                                    name = """items.getJSONObject("product")
                                                                        .getString("name")""",
                                                                    price = """items.getJSONObject("product")
                                                                        .getDouble("max_price")
                                                                        .toString()""",
                                                                    quantity = """items.getInt("order_quantity")
                                                                        .toString()""",
                                                                    created_at = items.getString("created_at")
                                                                        .toString(),
                                                                    delivery_status = items.getString(
                                                                        "delivery_status"
                                                                    ).toString(),
                                                                    productorderlist = if (productOrderList1.length() != 0) {
                                                                        val ioio =
                                                                            ArrayList<ProductOrderList>()
                                                                        for (jk in 0 until productOrderList1.length()) {
                                                                            val items1 = JSONObject(
                                                                                productOrderList1[jk].toString()
                                                                            )

                                                                            ioio.add(
                                                                                ProductOrderList(
                                                                                    name = items1.getString(
                                                                                        "name"
                                                                                    ),
                                                                                    quantity = items1.getDouble(
                                                                                        "qty"
                                                                                    )
                                                                                        .toString(),
                                                                                    price = items1.getDouble(
                                                                                        "max_price"
                                                                                    )
                                                                                        .toString()
                                                                                )

                                                                            )
                                                                        }
                                                                        ioio
                                                                    } else {

                                                                        ArrayList()
                                                                    }
                                                                )
                                                            )
//                                                            productOrderList.clear()
//                                                            }


                                                            Log.d(
                                                                "TAG",
                                                                "onViewCreated123123222!!1: Step2@@${helperOrderLists.size}"
                                                            )
                                                            Log.d(
                                                                "TAG",
                                                                "onViewCreated123123222!!2: Step2@@${object1.length()}"
                                                            )
//                                                            if (jari.size == object1.length()) {

//                                                            }
                                                        }
                                                        orderArrayList.add(
                                                            MainDateOrderList(
                                                                MainOrderList(
                                                                    "$it",
                                                                    helperOrderLists
                                                                )
                                                            )
                                                        )
//                                                        Log.d(
//                                                            "@@@@@",
//                                                            "onViewCreated:${jari.get(0).productorderlist.size} "
//                                                        )
                                                        //ye kam uncommit karna he
//                                                        orderArrayList.add(
//                                                            MainDateOrderList(
//                                                                MainOrderList("$it", jari)
//                                                            )
//                                                        )

                                                        //
//                                                        Log.d(
//                                                            "TAG",
//                                                            "onViewCreated123123222@@####: ${
//                                                                orderArrayList.get(
//                                                                    counter
//                                                                ).item.jariis.get(0).productorderlist.size
//                                                            }"
//                                                        )

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
                                            DialogHelperClass.errorDialog(
                                                requireContext(),
                                                it.message!!
                                            )
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

