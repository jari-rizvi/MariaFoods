package com.teamx.mariaFoods.ui.fragments.Checkout

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.NavOptions
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.google.gson.JsonObject
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import com.teamx.mariaFoods.BR
import com.teamx.mariaFoods.R
import com.teamx.mariaFoods.baseclasses.BaseFragment
import com.teamx.mariaFoods.data.dataclasses.getCart.Cart
import com.teamx.mariaFoods.data.remote.Resource
import com.teamx.mariaFoods.databinding.FragmentCheckoutBinding
import com.teamx.mariaFoods.ui.activity.mainActivity.MainActivity
import com.teamx.mariaFoods.utils.DialogHelperClass
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONException
import timber.log.Timber

@AndroidEntryPoint
class CheckoutFragment : BaseFragment<FragmentCheckoutBinding, CheckoutViewModel>() {

    override val layoutId: Int
        get() = R.layout.fragment_checkout
    override val viewModel: Class<CheckoutViewModel>
        get() = CheckoutViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel


    private lateinit var options: NavOptions

    lateinit var cartAdapter: CartAdapter
    lateinit var cartArrayList: ArrayList<Cart>

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    private lateinit var country: String
    private lateinit var city: String
    private lateinit var address: String
    private lateinit var state: String
    private lateinit var postal: String

    lateinit var paymentSheet: PaymentSheet
    var paymentIntentClientSecret: String = ""
    lateinit var customerConfig: PaymentSheet.CustomerConfiguration
    var stripPublicKey: String =
        "pk_test_51HWpLoH5a96j3Kt2rdV31pdGaiLEmPeIgoNBBSbCy79FaDdhOEuXnIiNWi6pT4mzmVxmBuZ5x60WpDUg7pfeln7i00v22JRQsM"

    fun presentPaymentSheet() {
        paymentSheet.presentWithPaymentIntent(
            paymentIntentClientSecret,
            PaymentSheet.Configuration(
                merchantDisplayName = "Maria Foods",
//                customer = customerConfig,
                // Set `allowsDelayedPaymentMethods` to true if your business
                // can handle payment methods that complete payment after a delay, like SEPA Debit and Sofort.
                allowsDelayedPaymentMethods = true
            )
        )
    }

    private fun onPaymentSheetResult(paymentSheetResult: PaymentSheetResult) {
        when (paymentSheetResult) {
            is PaymentSheetResult.Canceled -> {
                Timber.tag("Cancel").d("helllo there")
                print("Canceled")
                showSnackBar("Cancel")
            }
            is PaymentSheetResult.Failed -> {
                print("Error: ${paymentSheetResult.error}")
                Log.d("ErrroAaaTAG", "onPaymentSheetResult: ${paymentSheetResult.error}")
                Timber.tag("Error").d("helllo there")
                showSnackBar("Error")
            }
            is PaymentSheetResult.Completed -> {
                // Display for example, an order confirmation screen
                print("Completed")
                Timber.tag("Completed").d("helllo there")
                /* verifyPaymentSheet(paymentSheetResult.error)*/
                showSnackBar("Completed")

                bottomSheetBehavior =
                    BottomSheetBehavior.from(mViewDataBinding.bottomSheetLayout1.bottomSheetOrderPlace)

                bottomSheetBehavior.addBottomSheetCallback(object :
                    BottomSheetBehavior.BottomSheetCallback() {
                    override fun onSlide(bottomSheet: View, slideOffset: Float) {

                    }

                    override fun onStateChanged(bottomSheet: View, newState: Int) {
                        when (newState) {
                            BottomSheetBehavior.STATE_EXPANDED -> MainActivity.bottomNav?.visibility =
                                View.GONE
                            BottomSheetBehavior.STATE_COLLAPSED -> MainActivity.bottomNav?.visibility =
                                View.VISIBLE
                            else -> "Persistent Bottom Sheet"
                        }
                    }
                })

                val state =
                    if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) BottomSheetBehavior.STATE_COLLAPSED
                    else BottomSheetBehavior.STATE_EXPANDED
                bottomSheetBehavior.state = state

            }
        }
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(
            mViewDataBinding.root, message, Snackbar.LENGTH_SHORT
        ).show()
    }


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

        mViewDataBinding.textView20.setOnClickListener {
            if (mViewDataBinding.autoCompleteTextView.text.isNullOrEmpty()) {
                showToast("Enter Voucher")
            } else {

                val params = JsonObject()
                try {
                    params.addProperty(
                        "code", mViewDataBinding.autoCompleteTextView.text.toString()
                    )
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                mViewModel.Applycoupon(params)
            }

        }

        if (!mViewModel.coupon.hasActiveObservers()) {
            mViewModel.coupon.observe(requireActivity()) {
                when (it.status) {
                    Resource.Status.LOADING -> {
                        loadingDialog.show()
                    }
                    Resource.Status.SUCCESS -> {
                        loadingDialog.dismiss()
                        it.data?.let { data ->

                            mViewDataBinding.subtotal.text = data.data.subTotal
                            mViewDataBinding.discount.text = data.data.couponDiscount
                            mViewDataBinding.vat.text = data.data.vat
                            mViewDataBinding.deliveryfee.text = data.data.delivery_charges
                            mViewDataBinding.total.text = data.data.Total

                        }
                    }
                    Resource.Status.ERROR -> {
                        loadingDialog.dismiss()
                        DialogHelperClass.errorDialog(requireContext(), it.message!!)
                    }
                }

                if (isAdded) {
                    mViewModel.coupon.removeObservers(viewLifecycleOwner)
                }
            }
        }



        mViewDataBinding.bottomSheetLayout1.btnShopping.setOnClickListener {
            bottomSheetBehavior =
                BottomSheetBehavior.from(mViewDataBinding.bottomSheetLayout1.bottomSheetOrderPlace)


            val state =
                if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) BottomSheetBehavior.STATE_COLLAPSED
                else BottomSheetBehavior.STATE_EXPANDED
            bottomSheetBehavior.state = state

            popUpStack()


        }

        mViewDataBinding.bottomSheetLayout.btnAdd.setOnClickListener {
            initialization()

            if (/*!country!!.isEmpty() ||*/!city!!.isEmpty() || !address!!.isEmpty() || !postal!!.isEmpty() || !state!!.isEmpty()) {

                val params = JsonObject()
                try {
                    params.addProperty("country", country)
                    params.addProperty("city", city)
                    params.addProperty("address_1", address)
                    params.addProperty("address_2", address)
                    params.addProperty("postal", postal)
                    params.addProperty("state", state)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

                Log.e("UserData", params.toString())

                mViewModel.addAddress(params)

                if (!mViewModel.addaddress.hasActiveObservers()) {
                    mViewModel.addaddress.observe(requireActivity()) {
                        when (it.status) {
                            Resource.Status.LOADING -> {
                                loadingDialog.show()
                            }
                            Resource.Status.SUCCESS -> {
                                loadingDialog.dismiss()
                                it.data?.let { data ->
                                    if (data.Flag == 1) {

                                    } else {
                                        showToast(data.Message)
                                    }


                                }
                            }
                            Resource.Status.ERROR -> {
                                loadingDialog.dismiss()
                                DialogHelperClass.errorDialog(requireContext(), it.message!!)
                            }
                        }
                        if (isAdded) {
                            mViewModel.addaddress.removeObservers(viewLifecycleOwner)
                        }
                    }


                }
            }
        }

        mViewDataBinding.btnAddAdrress.setOnClickListener {
            bottomSheetBehavior =
                BottomSheetBehavior.from(mViewDataBinding.bottomSheetLayout.bottomSheetAddress)

            bottomSheetBehavior.addBottomSheetCallback(object :
                BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(bottomSheet: View, slideOffset: Float) {

                }

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_EXPANDED -> MainActivity.bottomNav?.visibility =
                            View.GONE
                        BottomSheetBehavior.STATE_COLLAPSED -> MainActivity.bottomNav?.visibility =
                            View.VISIBLE
                        else -> "Persistent Bottom Sheet"
                    }
                }
            })

            val state =
                if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) BottomSheetBehavior.STATE_COLLAPSED
                else BottomSheetBehavior.STATE_EXPANDED
            bottomSheetBehavior.state = state
        }


        mViewDataBinding.textView25.setOnClickListener {
            bottomSheetBehavior =
                BottomSheetBehavior.from(mViewDataBinding.bottomSheetLayout.bottomSheetAddress)

            bottomSheetBehavior.addBottomSheetCallback(object :
                BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(bottomSheet: View, slideOffset: Float) {

                }

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_EXPANDED -> MainActivity.bottomNav?.visibility =
                            View.GONE
                        BottomSheetBehavior.STATE_COLLAPSED -> MainActivity.bottomNav?.visibility =
                            View.VISIBLE
                        else -> "Persistent Bottom Sheet"
                    }
                }
            })

            val state =
                if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) BottomSheetBehavior.STATE_COLLAPSED
                else BottomSheetBehavior.STATE_EXPANDED
            bottomSheetBehavior.state = state
        }

        paymentSheet = PaymentSheet(this, ::onPaymentSheetResult)
        PaymentConfiguration.init(
            requireActivity().applicationContext, stripPublicKey
        )
        mViewDataBinding.btnPlaceOrder.setOnClickListener {

            val params = JsonObject()
            try {
                params.addProperty("payment_method", "STRIPE")
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            mViewModel.checkout(params)
            mViewModel.checkout.observe(requireActivity()) {
                when (it.status) {
                    Resource.Status.LOADING -> {
                        loadingDialog.show()
                    }
                    Resource.Status.SUCCESS -> {
                        loadingDialog.dismiss()
                        it.data?.let { data ->
                            paymentIntentClientSecret = data.client_secreat


                            presentPaymentSheet()

                        }
                    }
                    Resource.Status.ERROR -> {
                        loadingDialog.dismiss()
                        DialogHelperClass.errorDialog(requireContext(), it.message!!)
                    }
                }
                if (isAdded) {
                    mViewModel.addressList.removeObservers(viewLifecycleOwner)
                }
            }


        }


        mViewModel.getAddress()

        mViewModel.addressList.observe(requireActivity()) {
            when (it.status) {
                Resource.Status.LOADING -> {
                    loadingDialog.show()
                }
                Resource.Status.SUCCESS -> {
                    loadingDialog.dismiss()
                    it.data?.let { data ->


                        mViewDataBinding.containerAddress.visibility = View.VISIBLE
                        mViewDataBinding.address.text = data.data[0].address_1
                        mViewDataBinding.postal.text = "Postal Code " + data.data[0].postal

                    }
                }
                Resource.Status.ERROR -> {
                    loadingDialog.dismiss()
                    DialogHelperClass.errorDialog(requireContext(), it.message!!)
                }
            }
            if (isAdded) {
                mViewModel.addressList.removeObservers(viewLifecycleOwner)
            }
        }


        mViewModel.getCart()

        mViewModel.getCartList.observe(requireActivity()) {
            when (it.status) {
                Resource.Status.LOADING -> {
                    loadingDialog.show()
                }
                Resource.Status.SUCCESS -> {
                    loadingDialog.dismiss()
                    it.data?.let { data ->

                        cartArrayList.addAll(data.data.carts)
                        cartAdapter.notifyDataSetChanged()

                        mViewDataBinding.subtotal.text = data.data.subTotal + "AED"
                        mViewDataBinding.discount.text = "0.00" + "AED"
                        mViewDataBinding.vat.text = "25.00" + "AED"
                        mViewDataBinding.deliveryfee.text = "100.00" + "AED"
                        mViewDataBinding.total.text = "300.00" + "AED"

                    }
                }
                Resource.Status.ERROR -> {
                    loadingDialog.dismiss()
                    DialogHelperClass.errorDialog(requireContext(), it.message!!)
                }
            }
            if (isAdded) {
                mViewModel.getCartList.removeObservers(viewLifecycleOwner)
            }
        }
        cartRecyclerview()

    }

    fun initialization() {
        address = ""
        postal = mViewDataBinding.bottomSheetLayout.etPostal.text.toString()
        state = mViewDataBinding.bottomSheetLayout.etState.text.toString()
        city = mViewDataBinding.bottomSheetLayout.city.text.toString()
        country = mViewDataBinding.bottomSheetLayout.country.selectedCountryName

    }

    private fun cartRecyclerview() {
        cartArrayList = ArrayList()

        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        mViewDataBinding.recyclerView.layoutManager = linearLayoutManager

        cartAdapter = CartAdapter(cartArrayList)
        mViewDataBinding.recyclerView.adapter = cartAdapter

    }

}