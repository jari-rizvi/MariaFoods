package com.teamx.mariaFoods.ui.fragments.cart

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.teamx.mariaFoods.BR
import com.teamx.mariaFoods.R
import com.teamx.mariaFoods.baseclasses.BaseFragment
import com.teamx.mariaFoods.constants.NetworkCallPoints
import com.teamx.mariaFoods.data.dataclasses.getCart.Cart
import com.teamx.mariaFoods.data.remote.Resource
import com.teamx.mariaFoods.databinding.FragmentCartBinding
import com.teamx.mariaFoods.ui.fragments.Dashboard.home.OnCartListener
import com.teamx.mariaFoods.utils.DialogHelperClass
import com.teamx.mariaFoods.utils.snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CartFragment : BaseFragment<FragmentCartBinding, CartViewModel>(), OnCartListener {

    override val layoutId: Int
        get() = R.layout.fragment_cart
    override val viewModel: Class<CartViewModel>
        get() = CartViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel

    lateinit var cartAdapter: CarttAdapter
    lateinit var cartArrayList: ArrayList<Cart>
    var Guser_id = ""

    private lateinit var options: NavOptions

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

        mViewDataBinding.btnCheckout.setOnClickListener {

            navController = Navigation.findNavController(
                requireActivity(), R.id.nav_host_fragment
            )
            navController.navigate(R.id.checkoutFragment, null, options)

        }


        var token: String?? = null
        CoroutineScope(Dispatchers.Main).launch {

            dataStoreProvider.token.collect {
                Log.d("Databsae Token", "CoroutineScope ${it}")

                Log.d("dataStoreProvider", "subscribeToNetworkLiveData: $it")

                token = it

                NetworkCallPoints.TOKENER = token.toString()

                if (isAdded) {
                    if (token.isNullOrBlank()) {
                        mViewModel.getGuestCart(Guser_id.toInt())

                    } else {

                        mViewModel.getCart()
                    }
                }

            }


        }

        /*
                mViewModel.getCart()
        *//*
        mViewModel.getGuestCart(Guser_id.toInt())*/

        if (!mViewModel.getCartList.hasActiveObservers()) {
            mViewModel.getCartList.observe(requireActivity()) {
                when (it.status) {
                    Resource.Status.LOADING -> {
                        loadingDialog.show()
                    }

                    Resource.Status.SUCCESS -> {
                        loadingDialog.dismiss()
                        it.data?.let { data ->
                            cartArrayList.clear()

                            data.data.carts.forEach {

                                cartArrayList.add(it)
                            }

                            cartAdapter.notifyDataSetChanged()

                            mViewDataBinding.subtotal.text = data.data.subTotal
                            /*    mViewDataBinding.discount.text = data.data.couponDiscount
                                mViewDataBinding.vat.text = data.data.vat
                                mViewDataBinding.deliveryfee.text = data.data.delivery_charges*/
                            mViewDataBinding.total.text = data.data.Total

                        }
                    }

                    Resource.Status.ERROR -> {
                        loadingDialog.dismiss()
                        DialogHelperClass.errorDialog(requireContext(), it.message!!)
                    }
                }
                if (isAdded) {

                }
            }
        }

        cartRecyclerview()


    }

    private fun cartRecyclerview() {
        cartArrayList = ArrayList()

        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        mViewDataBinding.ccartRecycler.layoutManager = linearLayoutManager

        cartAdapter = CarttAdapter(cartArrayList, this)
        mViewDataBinding.ccartRecycler.adapter = cartAdapter

    }

    var qty = 1
    override fun onAddClickListener(position: Int) {
        val Pqty = cartArrayList[position]
        qty = Pqty.qty!!
        if (qty < 1) {
            qty = 1
        }
        qty += 1
        cartArrayList[position].qty = qty
        cartAdapter.notifyDataSetChanged()

        Log.d("qyuuu", "onAddClickListener: $qty")


    }

    override fun onSubClickListener(position: Int) {
        val Pqty = cartArrayList[position]
        qty = Pqty.qty!!
        if (qty <= 1) {
            mViewDataBinding.root.snackbar("atleast select one item")
            return
        }
        qty -= 1
        cartArrayList[position].qty = qty
        cartAdapter.notifyDataSetChanged()


    }

    override fun onAddToCartListener(id: Int) {
    }

    override fun onRemoveToCartListener(position: Int) {

        val cartid = cartArrayList[position].id

        mViewModel.removeCart(cartid)

        if (!mViewModel.removeCart.hasActiveObservers()) {
            mViewModel.removeCart.observe(requireActivity()) {
                when (it.status) {
                    Resource.Status.LOADING -> {
                        loadingDialog.show()
                    }

                    Resource.Status.SUCCESS -> {
                        loadingDialog.dismiss()
                        it.data?.let { data ->
                            cartArrayList.clear()

                            mViewModel.getCart()

                            cartAdapter.notifyDataSetChanged()
                            data.Message?.let { it1 -> mViewDataBinding.root.snackbar(it1) }


                        }
                    }

                    Resource.Status.ERROR -> {
                        loadingDialog.dismiss()
                        DialogHelperClass.errorDialog(requireContext(), it.message!!)
                    }
                }

            }
        }

    }

}