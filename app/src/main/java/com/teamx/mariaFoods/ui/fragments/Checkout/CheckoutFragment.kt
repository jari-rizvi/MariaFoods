package com.teamx.mariaFoods.ui.fragments.Checkout

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.google.gson.JsonObject
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import com.teamx.mariaFoods.BR
import com.teamx.mariaFoods.R
import com.teamx.mariaFoods.baseclasses.BaseFragment
import com.teamx.mariaFoods.constants.NetworkCallPoints
import com.teamx.mariaFoods.data.dataclasses.getAddress.Data
import com.teamx.mariaFoods.data.dataclasses.getCart.Cart
import com.teamx.mariaFoods.data.remote.Resource
import com.teamx.mariaFoods.databinding.FragmentCheckoutBinding
import com.teamx.mariaFoods.ui.activity.mainActivity.MainActivity
import com.teamx.mariaFoods.utils.DialogHelperClass
import com.teamx.mariaFoods.utils.PrefHelper
import com.teamx.mariaFoods.utils.snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONException
import timber.log.Timber
import java.io.IOException
import java.util.Locale

@AndroidEntryPoint
class CheckoutFragment : BaseFragment<FragmentCheckoutBinding, CheckoutViewModel>(),
    DialogHelperClass.Companion.DialogLoginCallBack {

    override val layoutId: Int
        get() = R.layout.fragment_checkout
    override val viewModel: Class<CheckoutViewModel>
        get() = CheckoutViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel


    private lateinit var options: NavOptions

    lateinit var cartAdapter: CartAdapter
    lateinit var cartArrayList: ArrayList<Cart>


    lateinit var addressArrayList: ArrayList<Data>

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    private lateinit var country: String
    private lateinit var city: String
    private lateinit var state: String
    private lateinit var postal: String
    private var name: String? = ""
    private var addressid: String? = ""
    private lateinit var address1: String
    private var paymentid: String? = ""
    var token: String?? = null


    lateinit var paymentSheet: PaymentSheet
    var paymentIntentClientSecret: String = ""
    lateinit var customerConfig: PaymentSheet.CustomerConfiguration
    var stripPublicKey: String =
        "pk_live_51NIUy0Cth47xs3WC4Q9ty9wNEA1QKq4G3FmPg5meLMbZR4wMVoeXwoZNV082ZOjV6ou6Sg4gin6lWRV7xC48s14800xP6kGFu7"

    fun presentPaymentSheet() {
        paymentSheet.presentWithPaymentIntent(
            paymentIntentClientSecret, PaymentSheet.Configuration(
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
//                showSnackBar("Completed")

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


    var Guser_id = ""


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewDataBinding.lifecycleOwner = viewLifecycleOwner

        //GuestUserId
        Guser_id = PrefHelper.getInstance(requireContext()).getUserId!!
        Log.d("TAG", "onViewCredsdsdsated: $Guser_id")

        options = navOptions {
            anim {
                enter = R.anim.enter_from_left
                exit = R.anim.exit_to_left
                popEnter = R.anim.nav_default_pop_enter_anim
                popExit = R.anim.nav_default_pop_exit_anim
            }
        }
        addressArrayList = ArrayList()
        mViewDataBinding.bottomSheetLayout.btnHome.setOnClickListener {
            mViewDataBinding.bottomSheetLayout.btnHome.isChecked = true
            mViewDataBinding.bottomSheetLayout.btnWork.isChecked = false
            mViewDataBinding.bottomSheetLayout.btnOther.isChecked = false
            name = mViewDataBinding.bottomSheetLayout.btnHome.text.toString()
        }

        mViewDataBinding.bottomSheetLayout.btnWork.setOnClickListener {
            mViewDataBinding.bottomSheetLayout.btnWork.isChecked = true
            mViewDataBinding.bottomSheetLayout.btnHome.isChecked = false
            mViewDataBinding.bottomSheetLayout.btnOther.isChecked = false
            name = mViewDataBinding.bottomSheetLayout.btnWork.text.toString()
        }

        mViewDataBinding.bottomSheetLayout.btnOther.setOnClickListener {
            mViewDataBinding.bottomSheetLayout.btnOther.isChecked = true
            mViewDataBinding.bottomSheetLayout.btnWork.isChecked = false
            mViewDataBinding.bottomSheetLayout.btnHome.isChecked = false
            name = mViewDataBinding.bottomSheetLayout.btnOther.text.toString()
        }

        mViewDataBinding.bottomSheetLayout11.btnHome.setOnClickListener {
            mViewDataBinding.bottomSheetLayout11.btnHome.isChecked = true
            mViewDataBinding.bottomSheetLayout11.btnWork.isChecked = false
            mViewDataBinding.bottomSheetLayout11.btnOther.isChecked = false
            name = mViewDataBinding.bottomSheetLayout11.btnHome.text.toString()
        }

        mViewDataBinding.bottomSheetLayout11.btnWork.setOnClickListener {
            mViewDataBinding.bottomSheetLayout11.btnWork.isChecked = true
            mViewDataBinding.bottomSheetLayout11.btnHome.isChecked = false
            mViewDataBinding.bottomSheetLayout11.btnOther.isChecked = false
            name = mViewDataBinding.bottomSheetLayout11.btnWork.text.toString()
        }

        mViewDataBinding.bottomSheetLayout11.btnOther.setOnClickListener {
            mViewDataBinding.bottomSheetLayout11.btnOther.isChecked = true
            mViewDataBinding.bottomSheetLayout11.btnWork.isChecked = false
            mViewDataBinding.bottomSheetLayout11.btnHome.isChecked = false
            name = mViewDataBinding.bottomSheetLayout11.btnOther.text.toString()
        }




        mViewDataBinding.textView20.setOnClickListener {

            var token: String?? = null
            CoroutineScope(Dispatchers.Main).launch {

                dataStoreProvider.token.collect {
                    Log.d("Databsae Token", "CoroutineScope ${it}")

                    Log.d("dataStoreProvider", "subscribeToNetworkLiveData: $it")

                    token = it

                    NetworkCallPoints.TOKENER = token.toString()


                    try {

                        if (isAdded) {
                            if (token.isNullOrBlank()) {
                                navController = Navigation.findNavController(
                                    requireActivity(), R.id.nav_host_fragment
                                )
                                navController.navigate(R.id.logInFragment, null, options)

                            } else {


                                if (mViewDataBinding.autoCompleteTextView.text.isNullOrEmpty()) {
                                    showToast("Enter Voucher")
                                } else {
                                    val params = JsonObject()
                                    try {
                                        params.addProperty(
                                            "code",
                                            mViewDataBinding.autoCompleteTextView.text.toString()
                                        )
                                    } catch (e: JSONException) {
                                        e.printStackTrace()
                                    }
                                    mViewModel.Applycoupon(params)
                                }
                            }


                        }
                    } catch (e: Exception) {

                    }

                }


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
                            if (data.Flag == 1) {
                                mViewDataBinding.subtotal.text = data.data.subTotal
                                mViewDataBinding.discount.text = data.data.couponDiscount
                                mViewDataBinding.vat.text = data.data.vat
                                mViewDataBinding.deliveryfee.text = data.data.delivery_charges
                                mViewDataBinding.total.text = data.data.Total
                            } else {
                                it.message?.let { it1 -> mViewDataBinding.root.snackbar(it1) }

                            }

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

        mViewDataBinding.imageView11.setOnClickListener {
            popUpStack()
        }

        mViewDataBinding.btnDefault.setOnClickListener {
            navController = Navigation.findNavController(
                requireActivity(), R.id.nav_host_fragment
            )
            navController.navigate(R.id.paymentFragment, null, options)
        }


        /*
                mViewDataBinding.bottomSheetLayout1.btnShopping.setOnClickListener {
                    bottomSheetBehavior =
                        BottomSheetBehavior.from(mViewDataBinding.bottomSheetLayout1.bottomSheetOrderPlace)


                    val state =
                        if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) BottomSheetBehavior.STATE_COLLAPSED
                        else BottomSheetBehavior.STATE_EXPANDED
                    bottomSheetBehavior.state = state
                    navController = Navigation.findNavController(
                        requireActivity(), R.id.nav_host_fragment
                    )
                    navController.navigate(R.id.dashboardFragment, null, options)
        //            popUpStack()


                }
        */

        if (Guser_id.isNotEmpty()) {
            mViewDataBinding.imageView13.visibility = View.GONE
            mViewDataBinding.textView25.visibility = View.GONE
        }



        mViewDataBinding.bottomSheetLayout.btnAdd.setOnClickListener {
            initialization()

            if (/*!country!!.isEmpty() ||*/!city!!.isEmpty() || !address1!!.isEmpty() || !postal!!.isEmpty() || !state!!.isEmpty()) {

                val params = JsonObject()
                try {
                    params.addProperty("name", name)
                    params.addProperty("country", country)
                    params.addProperty("city", city)
                    params.addProperty("address_1", address1)
                    params.addProperty("address_2", address1)
                    params.addProperty("postal", postal)
                    params.addProperty("state", state)
                    params.addProperty("is_default", 1)

                } catch (e: JSONException) {
                    e.printStackTrace()
                }


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
                                        val state =
                                            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) BottomSheetBehavior.STATE_COLLAPSED
                                            else BottomSheetBehavior.STATE_EXPANDED
                                        bottomSheetBehavior.state = state

                                        mViewModel.getAddress()

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
                        BottomSheetBehavior.STATE_EXPANDED -> {
                            MainActivity.bottomNav?.visibility = View.GONE

                        }

                        BottomSheetBehavior.STATE_COLLAPSED -> {
//                            if(addressArrayList.size > 0){
//                                mViewDataBinding.address.text = "${addressArrayList[0].address_1}"
//                                mViewDataBinding.postal.text = "Postal Code ${addressArrayList[0].postal}"
//                            }

                            MainActivity.bottomNav?.visibility = View.VISIBLE
                        }

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


//            val itemidAddress = addressArrayList[0]

            bottomSheetBehavior =
                BottomSheetBehavior.from(mViewDataBinding.bottomSheetLayout11.bottomSheetUpdateaddress)

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


            mViewDataBinding.bottomSheetLayout11.btnUpdate.setOnClickListener {

                val params = JsonObject()
                try {
                    params.addProperty("name", name)

                    params.addProperty("id", addressArrayList[0].id)

                    params.addProperty("country", addressArrayList[0].country)

                    params.addProperty(
                        "city", mViewDataBinding.bottomSheetLayout11.city.text.toString()
                    )
                    params.addProperty(
                        "address_1",
                        mViewDataBinding.bottomSheetLayout11.editAddress1.text.toString()
                    )
                    params.addProperty(
                        "address_2",
                        mViewDataBinding.bottomSheetLayout11.editAddress2.text.toString()
                    )
                    params.addProperty(
                        "postal", mViewDataBinding.bottomSheetLayout11.etPostal.text.toString()
                    )
                    params.addProperty(
                        "state", mViewDataBinding.bottomSheetLayout11.etState.text.toString()
                    )
                    params.addProperty("is_default", 1)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }


                mViewModel.updateAddress(params)

                if (!mViewModel.updateaddress.hasActiveObservers()) {
                    mViewModel.updateaddress.observe(requireActivity()) {
                        when (it.status) {
                            Resource.Status.LOADING -> {
                                loadingDialog.show()
                            }

                            Resource.Status.SUCCESS -> {
                                loadingDialog.dismiss()
                                it.data?.let { data ->
                                    val state =
                                        if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) BottomSheetBehavior.STATE_COLLAPSED
                                        else BottomSheetBehavior.STATE_EXPANDED
                                    bottomSheetBehavior.state = state

                                    mViewModel.getAddress()

                                    mViewDataBinding.root.snackbar("Updated")

                                }
                            }

                            Resource.Status.ERROR -> {
                                loadingDialog.dismiss()
                                DialogHelperClass.errorDialog(requireContext(), it.message!!)
                            }
                        }
                        if (isAdded) {
                            mViewModel.updateaddress.removeObservers(viewLifecycleOwner)
                        }
                    }
                }
            }

            mViewModel.editAddress(addressArrayList[0].id!!)


            mViewModel.editaddress.observe(requireActivity()) {
                when (it.status) {
                    Resource.Status.LOADING -> {
                        loadingDialog.show()
                    }

                    Resource.Status.SUCCESS -> {
                        loadingDialog.dismiss()
                        it.data?.let { data ->

                            data.data.name

                            mViewDataBinding.bottomSheetLayout11.editAddress1?.setText(data.data.address_1?.toString())

                            mViewDataBinding.bottomSheetLayout11.editAddress2?.setText(data.data.address_2?.toString())
                            mViewDataBinding.bottomSheetLayout11.etPostal?.setText(data.data.postal?.toString())
                            mViewDataBinding.bottomSheetLayout11.etState?.setText(data.data.state?.toString())
                            mViewDataBinding.bottomSheetLayout11.etState?.setText(data.data.country?.toString())
                            mViewDataBinding.bottomSheetLayout11.city?.setText(data.data.city?.toString())
                            mViewDataBinding.bottomSheetLayout11.country?.setText(data.data.country?.toString())


                            if (data.data.name == "Home") {
                                mViewDataBinding.bottomSheetLayout11.btnHome.isChecked = true
                            } else if (data.data.name == "Work") {
                                mViewDataBinding.bottomSheetLayout11.btnWork.isChecked = true
                            } else if (data.data.name == "Other") {
                                mViewDataBinding.bottomSheetLayout11.btnOther.isChecked = true
                            } else {

                            }


                        }
                    }

                    Resource.Status.ERROR -> {
                        loadingDialog.dismiss()
                        DialogHelperClass.errorDialog(requireContext(), it.message!!)
                    }
                }

            }

        }

        paymentSheet = PaymentSheet(this, ::onPaymentSheetResult)
        PaymentConfiguration.init(
            requireActivity().applicationContext, stripPublicKey!!
        )


        mViewDataBinding.btnPlaceOrder.setOnClickListener {


            if (token.isNullOrBlank()) {
                DialogHelperClass.LoginDialog(
                    requireContext(), this, true
                )

            } else {

                val params = JsonObject()
                try {
                    params.addProperty("payment_method", "STRIPE")
                    params.addProperty("shipping_address", addressid)
                    if (!paymentid.isNullOrEmpty()) {
                        params.addProperty("payment_method_id",  paymentid)
                    }

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
                                paymentIntentClientSecret = data.client_secreat!!

                                if (!paymentid.isNullOrEmpty()) {

                                    navController = Navigation.findNavController(
                                        requireActivity(), R.id.nav_host_fragment
                                    )
                                    navController.navigate(
                                        R.id.placeOrderFragment,
                                        null,
                                        options
                                    )
                                } else {
                                    presentPaymentSheet()
                                }

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
        }



        mViewDataBinding.bottomSheetLayout.btnLocation.setOnClickListener {
            getCurrentLocation()
        }
        mViewDataBinding.bottomSheetLayout11.btnLocation.setOnClickListener {
            getCurrentLocation()
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
                            mViewDataBinding.btnAddAdrress.visibility = View.GONE
                            Log.d("Databsae Token", "token ${token}")

                        } else {
                            mViewModel.getAddress()

                            mViewModel.addressList.observe(requireActivity()) {
                                when (it.status) {
                                    Resource.Status.LOADING -> {
                                        loadingDialog.show()
                                    }

                                    Resource.Status.SUCCESS -> {
                                        loadingDialog.dismiss()
                                        it.data?.let { data ->

                                            mViewDataBinding.containerAddress.visibility = View.GONE
                                            data.data.forEach {
                                                if (it.is_default == 1) {
                                                    mViewDataBinding.containerAddress.visibility =
                                                        View.VISIBLE
                                                    addressArrayList.add(it)
                                                    mViewDataBinding.address.text = it.address_1
                                                    mViewDataBinding.postal.text =
                                                        "Postal Code " + it.postal
                                                    addressid = it.id.toString()
                                                }
                                            }

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
                                    mViewModel.addressList.removeObservers(viewLifecycleOwner)
                                }
                            }

                            mViewModel.getDefaultStripeCard()

                            if (!mViewModel.getDefaultStripeCardsResponse.hasActiveObservers()) {
                                mViewModel.getDefaultStripeCardsResponse.observe(requireActivity()) {
                                    when (it.status) {
                                        Resource.Status.LOADING -> {
                                            loadingDialog.show()
                                        }

                                        Resource.Status.SUCCESS -> {
                                            loadingDialog.dismiss()
                                            it.data?.let { data ->
                                                try {
                                                    mViewDataBinding.paymentName.text =
                                                        "*** *** *** ${data.data.card.last4}"
                                                    if (data.data.card.brand == "visa") {
                                                        mViewDataBinding.paymentVisa.visibility =
                                                            View.VISIBLE
                                                    } else if (data.data.card.brand == "mastercard") {
                                                        mViewDataBinding.paymentaster.visibility =
                                                            View.VISIBLE

                                                    }

                                                    paymentid = data.data.id

                                                } catch (e: Exception) {

                                                }


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
                                        mViewModel.getDefaultStripeCardsResponse.removeObservers(
                                            viewLifecycleOwner
                                        )
                                    }
                                }
                            }

//                            mViewModel.getCart()

//                            mViewModel.addressList.observe(requireActivity()) {
//                                when (it.status) {
//                                    Resource.Status.LOADING -> {
//                                        loadingDialog.show()
//                                    }
//                                    Resource.Status.SUCCESS -> {
//                                        loadingDialog.dismiss()
//                                        it.data?.let { data ->
//
//                                            data.data.forEach {
//                                                if (it.is_default == 1) {
//                                                    addressArrayList.add(it)
//                                                    val address = data.data[0].address_1
//                                                    mViewDataBinding.textView4.text =
//                                                        address?.dropLast(30)
//                                                } else {
//                                                    mViewDataBinding.textView4.text = ""
//
//                                                }
//                                            }
//                                        }
//                                    }
//                                    Resource.Status.ERROR -> {
//                                        loadingDialog.dismiss()
//                                        DialogHelperClass.errorDialog(
//                                            requireContext(), it.message!!
//                                        )
//                                    }
//                                }
//                                if (isAdded) {
//                                    mViewModel.addressList.removeObservers(viewLifecycleOwner)
//                                }
//                            }

                        }
                    }

                }


            }


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


//        mViewModel.getGuestCart(Guser_id.toInt())


        if (!mViewModel.getCartList.hasActiveObservers()) {
            mViewModel.getCartList.observe(requireActivity()) {
                when (it.status) {
                    Resource.Status.LOADING -> {
                        loadingDialog.show()
                    }

                    Resource.Status.SUCCESS -> {
                        loadingDialog.dismiss()
                        it.data?.let { data ->

                            val size = data?.data?.cartCount ?: 0
                            it?.let {
                                MainActivity.bottomNav?.getOrCreateBadge(R.id.cart)?.apply {
                                    backgroundColor = Color.RED
                                    badgeTextColor = Color.WHITE
                                    maxCharacterCount = 3
                                    number = size
                                    isVisible = size != 0
                                }
                            }
                            cartArrayList.clear()

                            data.data.carts.forEach {

                                cartArrayList.add(it)
                            }

                            cartAdapter.notifyDataSetChanged()

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

                }
            }
        }

        cartRecyclerview()

    }

    fun initialization() {
        address1 = mViewDataBinding.bottomSheetLayout.editAddress1.text.toString()
        postal = mViewDataBinding.bottomSheetLayout.etPostal.text.toString()
        state = mViewDataBinding.bottomSheetLayout.etState.text.toString()
        city = mViewDataBinding.bottomSheetLayout.city.text.toString()
//        country = mViewDataBinding.bottomSheetLayout.country.selectedCountryName
        country = mViewDataBinding.bottomSheetLayout.country.text.toString()
    }

    private fun cartRecyclerview() {
        cartArrayList = ArrayList()

        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        mViewDataBinding.recyclerView.layoutManager = linearLayoutManager

        cartAdapter = CartAdapter(cartArrayList)
        mViewDataBinding.recyclerView.adapter = cartAdapter

    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private fun getCurrentLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        if (ActivityCompat.checkSelfPermission(
                requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener {
            val location = Location("provider").apply {
                latitude = it.latitude
                longitude = it.longitude
            }

            getAddressFromLocation(requireActivity(), location)
        }


    }

    private fun getAddressFromLocation(context: Context, location: Location): String? {
        val geocoder = Geocoder(context, Locale.getDefault())
        var addressText = ""

        try {
            val addresses: MutableList<Address>? =
                geocoder.getFromLocation(location.latitude, location.longitude, 1)

            if (addresses != null) {
                if (addresses.isNotEmpty()) {
                    val address: Address = addresses[0]
                    val sb = StringBuilder()

                    for (i in 0..address.maxAddressLineIndex) {
                        sb.append(address.getAddressLine(i)).append("\n")
                    }

                    addressText = sb.toString()

                    address1 = addressText


                    val city: String?? = address.locality
                    val state: String?? = address.adminArea
                    val country: String?? = address.countryName
                    val postalCode: String?? = address.postalCode
                    val knownName: String?? = address.featureName
                    val knownName2: String?? = address.subLocality
                    val phone: String?? = address.phone

                    mViewDataBinding.bottomSheetLayout.editAddress1.setText(addressText.toString())
                    mViewDataBinding.bottomSheetLayout.city.setText(city)
                    mViewDataBinding.bottomSheetLayout.etState.setText(state)
                    mViewDataBinding.bottomSheetLayout.etPostal.setText(postalCode)
                    mViewDataBinding.bottomSheetLayout.country.setText(country)

                    mViewDataBinding.bottomSheetLayout11.editAddress1.setText(addressText.toString())
                    mViewDataBinding.bottomSheetLayout11.city.setText(city)
                    mViewDataBinding.bottomSheetLayout11.etState.setText(state)
                    mViewDataBinding.bottomSheetLayout11.etPostal.setText(postalCode)
                    mViewDataBinding.bottomSheetLayout11.country.setText(country)




                    Log.d("lastLocation", "onCreate:latitude ${phone}")
                    Log.d("lastLocation", "onCreate:latitude ${knownName2}")
                    Log.d("lastLocation", "onCreate:latitude ${postalCode}")
                    Log.d("lastLocation", "onCreate:latitude ${country}")
                    Log.d("lastLocation", "onCreate:latitude ${state}")
                    Log.d("lastLocation", "onCreate:latitude ${city}")
                    Log.d("lastLocation", "chahye ${addressText}")
                    Log.d("lastLocation", "onCreate:latitude ${address}")
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return addressText
    }

    override fun CnfrmClicked() {
        navController = Navigation.findNavController(
            requireActivity(), R.id.nav_host_fragment
        )
        navController.navigate(R.id.logInFragment, null, options)
    }

    override fun CnclClicked() {
    }

}