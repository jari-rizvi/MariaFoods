package com.teamx.mariaFoods.ui.fragments.cart

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.JsonObject
import com.teamx.mariaFoods.BR
import com.teamx.mariaFoods.R
import com.teamx.mariaFoods.baseclasses.BaseFragment
import com.teamx.mariaFoods.constants.NetworkCallPoints
import com.teamx.mariaFoods.data.dataclasses.getCart.Cart
import com.teamx.mariaFoods.data.dataclasses.products.OrderDay
import com.teamx.mariaFoods.data.dataclasses.products.TimeSlot
import com.teamx.mariaFoods.data.dataclasses.wishList.GetWishlist
import com.teamx.mariaFoods.data.remote.Resource
import com.teamx.mariaFoods.databinding.FragmentCartBinding
import com.teamx.mariaFoods.ui.activity.mainActivity.MainActivity
import com.teamx.mariaFoods.ui.fragments.Dashboard.home.DateAdapter
import com.teamx.mariaFoods.ui.fragments.Dashboard.home.OnCartListener
import com.teamx.mariaFoods.ui.fragments.Dashboard.home.OnTimeListener
import com.teamx.mariaFoods.ui.fragments.Dashboard.home.TimeAdapter
import com.teamx.mariaFoods.utils.DialogHelperClass
import com.teamx.mariaFoods.utils.snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONException
import java.util.Stack

@AndroidEntryPoint
class CartFragment : BaseFragment<FragmentCartBinding, CartViewModel>(), OnCartListener,
    OnTimeListener {

    override val layoutId: Int
        get() = R.layout.fragment_cart
    override val viewModel: Class<CartViewModel>
        get() = CartViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel

    var days: Int? = 0
    var time: Int? = 0

    lateinit var timeAdapter: TimeAdapter
    lateinit var timeArrayList: ArrayList<TimeSlot>
    lateinit var dTimeArrayList: ArrayList<TimeSlot>

    var favArrayList: GetWishlist? = null


    lateinit var dayAdapter: DateAdapter
    lateinit var dayArrayList: ArrayList<OrderDay>


    lateinit var cartAdapter: CarttAdapter
    lateinit var cartArrayList: ArrayList<Cart>
    var Guser_id = ""

    private lateinit var options: NavOptions
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

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

        mViewDataBinding.btnSchedule.setOnClickListener {
            openBottomSheer()
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
                            try {
                                mViewDataBinding.date.text = data.data.slot.timeline
                            } catch (e: Exception) {

                            }
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



        mViewModel.getProducts()
        if (!mViewModel.products.hasActiveObservers()) {
            mViewModel.products.observe(requireActivity()) {
                when (it.status) {
                    Resource.Status.LOADING -> {
                        loadingDialog.show()
                    }

                    Resource.Status.SUCCESS -> {
                        loadingDialog.dismiss()
                        it.data?.let { data ->


                            data.shedule?.order_days?.forEach {
                                if (it != null) {
                                    dayArrayList.add(it)
                                }
                                dayArrayList[0].isChecked = true
                            }


                            dayAdapter.notifyDataSetChanged()


                            dTimeArrayList = ArrayList()
                            data.shedule?.time_slots?.forEach {
                                if (it != null) {
                                    Log.d(
                                        "TAG", "onViewCreated: ${
                                            it.last_order_time?.substringBefore(":")!!.toInt()
                                        }"
                                    )
                                }
                                if (it != null) {
                                    timeArrayList.add(it)
                                }
                                if (it != null) {
                                    dTimeArrayList.add(it)
                                }
                                timeAdapter.notifyDataSetChanged()


                                ondaysClick(0)
                            }


                        }
                    }

                    Resource.Status.ERROR -> {
                        loadingDialog.dismiss()
                        DialogHelperClass.errorDialog(requireContext(), it.message!!)
                    }
                }
                if (isAdded) {
                    mViewModel.products.removeObservers(viewLifecycleOwner)
                }
            }
        }


        mViewDataBinding.bottomSheetLayout.btnBook.setOnClickListener {

            val params = JsonObject()
            try {
                params.addProperty("order_day", days)
                params.addProperty("time_slot", time)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            mViewModel.changeSlot(params)


            if (!mViewModel.changeSlotResponse.hasActiveObservers()) {
                mViewModel.changeSlotResponse.observe(requireActivity()) {
                    when (it.status) {
                        Resource.Status.LOADING -> {
                            loadingDialog.show()
                        }

                        Resource.Status.SUCCESS -> {
                            loadingDialog.dismiss()
                            it.data?.let { data ->
                                if (data.Flag == 1) {

                                    var token: String?? = null
                                    CoroutineScope(Dispatchers.Main).launch {

                                        dataStoreProvider.token.collect {
                                            Log.d("Databsae Token", "CoroutineScope ${it}")

                                            Log.d(
                                                "dataStoreProvider",
                                                "subscribeToNetworkLiveData: $it"
                                            )

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

                                    openBottomSheer()

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
                        mViewModel.changeSlotResponse.removeObservers(viewLifecycleOwner)
                    }
                }
            }

        }


        cartRecyclerview()
        timeRecyclerview()
        daysRecyclerview()

    }

    private fun timeRecyclerview() {
        timeArrayList = ArrayList()

        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        mViewDataBinding.bottomSheetLayout.recyclerTime.layoutManager = linearLayoutManager

        timeAdapter = TimeAdapter(timeArrayList, this)
        mViewDataBinding.bottomSheetLayout.recyclerTime.adapter = timeAdapter

    }

    private fun daysRecyclerview() {
        dayArrayList = ArrayList()

        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        mViewDataBinding.bottomSheetLayout.recyclerDates.layoutManager = linearLayoutManager

        dayAdapter = DateAdapter(dayArrayList, this)
        mViewDataBinding.bottomSheetLayout.recyclerDates.adapter = dayAdapter

    }


    private fun cartRecyclerview() {
        cartArrayList = ArrayList()

        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        mViewDataBinding.ccartRecycler.layoutManager = linearLayoutManager

        cartAdapter = CarttAdapter(cartArrayList, this)
        mViewDataBinding.ccartRecycler.adapter = cartAdapter

    }

    fun openBottomSheer() {
        bottomSheetBehavior =
            BottomSheetBehavior.from(mViewDataBinding.bottomSheetLayout.bottomSheetSlots)

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

    private val debounceDelayMillis = 1000 // Set your desired debounce delay in milliseconds
    private val handler = Handler(Looper.getMainLooper())
    private val actionStack = Stack<Int>()

    override fun onQuantityChange(position: Int, quantity: Int) {
//        val Pqty = cartArrayList[position]

        handler.removeCallbacksAndMessages(null)

        if (quantity > 0) {

            if (!actionStack.contains(position)) {
                actionStack.push(position)
            }
            cartArrayList[position].qty = quantity
            cartAdapter.notifyItemChanged(position)

            updateQtyResponse()

        }
    }

    private fun updateQtyResponse() {

        val params = JsonObject()

        handler.postDelayed({
            if (actionStack.isNotEmpty()) {
                val cartmodel = cartArrayList[actionStack.pop()]
                params.addProperty("cart_id", cartmodel.id)
                params.addProperty("qty", cartmodel.qty)
                mViewModel.increaseDecrease(params)

                mViewModel.increaseDecreaseResponse.observe(requireActivity()) {
                    when (it.status) {
                        Resource.Status.LOADING -> {
                            loadingDialog.show()
                        }

                        Resource.Status.SUCCESS -> {
                            loadingDialog.dismiss()
                            it.data?.let { data ->

                                if (actionStack.isNotEmpty()) {
                                    val cartmodel1 = cartArrayList[actionStack.pop()]
                                    params.addProperty("cart_id", cartmodel1.id)
                                    params.addProperty("qty", cartmodel1.qty)
                                    mViewModel.increaseDecrease(params)
                                }
//                                else {
//                                    cartAdapter.notifyDataSetChanged()
//                                }
                            }
                            mViewModel.increaseDecreaseResponse.removeObservers(viewLifecycleOwner)
                        }

                        Resource.Status.ERROR -> {
                            loadingDialog.dismiss()
                            mViewDataBinding.root.snackbar(it.message!!)
                        }
                    }
                }
            }

        }, debounceDelayMillis.toLong())
    }

    override fun ontimeClick(position: Int) {
        for (cat in timeArrayList) {
            cat.isChecked = false
        }


        val timeSlicl = timeArrayList[position]

        time = timeSlicl.id
        timeSlicl.isChecked = true


        timeAdapter.notifyDataSetChanged()
    }

    override fun ondaysClick(position: Int) {
        for (cat in dayArrayList) {
            cat.isChecked = false
        }

        val daysSlicl = dayArrayList[position]

        days = daysSlicl.day
        daysSlicl.isChecked = true

        val currentDateTime: java.util.Date = java.util.Date()
        val arr = timeArrayList
        timeArrayList.clear()


        if (timeArrayList.size > 1) {
            mViewDataBinding.bottomSheetLayout.notAvailable.visibility = View.VISIBLE
        }


        dTimeArrayList.forEach {
            if (dayArrayList[position].day != 1) {
                Log.d("TAG", "onViewCreated1212121daynot")
                timeArrayList.add(it)

            } else if (currentDateTime.hours < it.last_order_time?.substringBefore(":")!!.toInt()) {
                Log.d("TAG", "onViewCreated1212121day")
                timeArrayList.add(it)
            }
            timeAdapter.notifyDataSetChanged()
        }


        dayAdapter.notifyDataSetChanged()
    }

}