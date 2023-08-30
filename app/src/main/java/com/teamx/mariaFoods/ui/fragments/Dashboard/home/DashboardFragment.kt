package com.teamx.mariaFoods.ui.fragments.Dashboard.home


import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.JsonObject
import com.teamx.mariaFoods.BR
import com.teamx.mariaFoods.R
import com.teamx.mariaFoods.baseclasses.BaseFragment
import com.teamx.mariaFoods.constants.NetworkCallPoints
import com.teamx.mariaFoods.data.dataclasses.banners.Data
import com.teamx.mariaFoods.data.dataclasses.products.OrderDay
import com.teamx.mariaFoods.data.dataclasses.products.TimeSlot
import com.teamx.mariaFoods.data.remote.Resource
import com.teamx.mariaFoods.databinding.FragmentDashboardBinding
import com.teamx.mariaFoods.ui.activity.mainActivity.MainActivity
import com.teamx.mariaFoods.utils.DialogHelperClass
import com.teamx.mariaFoods.utils.PrefHelper
import com.teamx.mariaFoods.utils.snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONException
import java.lang.Math.abs
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.random.Random

@AndroidEntryPoint
class DashboardFragment : BaseFragment<FragmentDashboardBinding, Dashboard>(), OnProductListener,
    OnCartListener, OnTimeListener {

    override val layoutId: Int
        get() = R.layout.fragment_dashboard
    override val viewModel: Class<Dashboard>
        get() = Dashboard::class.java
    override val bindingVariable: Int
        get() = BR.viewModel

    private lateinit var options: NavOptions
    lateinit var featureProductAdapter: FeatureProductAdapter
    lateinit var featureProductArrayList: ArrayList<Data>
    private var tabLayoutMediator: TabLayoutMediator? = null


    lateinit var productAdapter: ProductAdapter
    lateinit var productArrayList: ArrayList<com.teamx.mariaFoods.data.dataclasses.products.Data>

    lateinit var timeAdapter: TimeAdapter
    lateinit var timeArrayList: ArrayList<TimeSlot>
    lateinit var dTimeArrayList: ArrayList<TimeSlot>

    lateinit var dayAdapter: DateAdapter
    lateinit var dayArrayList: ArrayList<OrderDay>

    var days: Int? = 0
    var time: Int? = 0
    var token: String?? = null


    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    private lateinit var handler: Handler
    lateinit var addressArrayList: java.util.ArrayList<com.teamx.mariaFoods.data.dataclasses.getAddress.Data>


    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
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
        mViewDataBinding.imageView.setOnClickListener {
            navController = Navigation.findNavController(
                requireActivity(), R.id.nav_host_fragment
            )
            navController.navigate(R.id.notificationFragment, null, options)
        }

        mViewDataBinding.fav.setOnClickListener {
            navController = Navigation.findNavController(
                requireActivity(), R.id.nav_host_fragment
            )
            navController.navigate(R.id.notificationFragment, null, options)
        }


        lifecycleScope.launch {
            dataStoreProvider.userFlow.collect { user ->

                mViewDataBinding.textView3.text = ("Hello " + user.first_name.toString())

            }
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

                                            data.data.forEach {
                                                if (it.is_default == 1) {
                                                    addressArrayList.add(it)
                                                    val address = data.data[0].address_1
                                                    mViewDataBinding.textView4.text =
                                                        address?.dropLast(30)
                                                } else {
                                                    mViewDataBinding.textView4.text = ""

                                                }
                                            }
                                        }
                                    }

                                    Resource.Status.ERROR -> {
                                        loadingDialog.dismiss()
                                        DialogHelperClass.errorDialog(
                                            requireContext(), it.message!!
                                        )
                                    }
                                }
                                if (isAdded) {
                                    mViewModel.addressList.removeObservers(viewLifecycleOwner)
                                }
                            }

                        }
                    }

                }


            }


        }

        addressArrayList = ArrayList()
        val currentMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM"))
        mViewDataBinding.bottomSheetLayout.months.text = currentMonth

        initializeFeatureProducts()
        productRecyclerview()

        mViewModel.bannerList()

        if (!mViewModel.bannerList.hasActiveObservers()) {
            mViewModel.bannerList.observe(requireActivity()) {
                when (it.status) {
                    Resource.Status.LOADING -> {
                        loadingDialog.show()
                    }

                    Resource.Status.SUCCESS -> {
                        loadingDialog.dismiss()
                        it.data?.let { data ->

                            data.data?.forEach {
                                if (it != null) {
                                    featureProductArrayList.add(it)
                                }

                            }
                            featureProductAdapter.notifyDataSetChanged()


                        }
                    }

                    Resource.Status.ERROR -> {
                        loadingDialog.dismiss()
                        DialogHelperClass.errorDialog(requireContext(), it.message!!)
                    }
                }
                if (isAdded) {
                    mViewModel.bannerList.removeObservers(viewLifecycleOwner)
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

                            data.data?.forEach {
                                if (it != null) {
                                    productArrayList.add(it)
                                }
                            }
                            productAdapter.notifyDataSetChanged()


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
        setUpTransformer()
        timeRecyclerview()
        daysRecyclerview()

        mViewDataBinding.bottomSheetLayout.btnBook.setOnClickListener {
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


    }

    private fun initializeFeatureProducts() {

        featureProductArrayList = ArrayList()

        featureProductAdapter = FeatureProductAdapter(featureProductArrayList)
        mViewDataBinding.screenViewpager.adapter = featureProductAdapter

        TabLayoutMediator(
            mViewDataBinding.tabIndicator, mViewDataBinding.screenViewpager
        ) { tab, position ->
            tab.text = featureProductArrayList[position].toString()
        }.attach()

        tabLayoutMediator = TabLayoutMediator(
            mViewDataBinding.tabIndicator, mViewDataBinding.screenViewpager
        ) { tab: TabLayout.Tab, position: Int ->
            mViewDataBinding.screenViewpager.setCurrentItem(tab.position, true)
        }
        tabLayoutMediator!!.attach()

        mViewDataBinding.screenViewpager.offscreenPageLimit = 3
        mViewDataBinding.screenViewpager.clipToPadding = false
        mViewDataBinding.screenViewpager.clipChildren = false
        mViewDataBinding.screenViewpager.getChildAt(0).overScrollMode =
            RecyclerView.OVER_SCROLL_NEVER

        handler = Handler(Looper.myLooper()!!)

        mViewDataBinding.screenViewpager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                handler.removeCallbacks(runnable)
                handler.postDelayed(runnable, 2000)
            }
        })


    }

    private fun productRecyclerview() {
        productArrayList = ArrayList()

        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        mViewDataBinding.productRecycler.layoutManager = linearLayoutManager

        productAdapter = ProductAdapter(productArrayList, this, this)
        mViewDataBinding.productRecycler.adapter = productAdapter

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


    override fun onproductClick(position: Int) {

    }

    override fun onScheduleClick(position: Int) {
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

    override fun onAddFavClick(position: Int) {

        val id_ = productArrayList[position].id

        val params = JsonObject()
        try {
            params.addProperty("product_id", id_)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        mViewModel.addWishList(params)

        if (!mViewModel.addtowishlist.hasActiveObservers()) {
            mViewModel.addtowishlist.observe(requireActivity()) {
                when (it.status) {
                    Resource.Status.LOADING -> {
                        loadingDialog.show()
                    }

                    Resource.Status.SUCCESS -> {
                        loadingDialog.dismiss()
                        it.data?.let { data ->
                            if (data.Flag == 1) {

                                navController = Navigation.findNavController(
                                    requireActivity(), R.id.nav_host_fragment
                                )
                                navController.navigate(R.id.favouriteFragment,null,  options)

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
                    mViewModel.addtowishlist.removeObservers(viewLifecycleOwner)
                }
            }
        }

    }

    override fun onPause() {
        super.onPause()

        handler.removeCallbacks(runnable)
    }

    override fun onResume() {
        super.onResume()

        handler.postDelayed(runnable, 2000)
    }

    private val runnable = Runnable {
        mViewDataBinding.screenViewpager.currentItem =
            mViewDataBinding.screenViewpager.currentItem + 1
    }

    private fun setUpTransformer() {
        val transformer = CompositePageTransformer()
        transformer.addTransformer(MarginPageTransformer(40))
        transformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = 0.85f + r * 0.14f
        }

        mViewDataBinding.screenViewpager.setPageTransformer(transformer)
    }

    var qty = 1
    override fun onAddClickListener(position: Int) {
        val Pqty = productArrayList[position]
        qty = Pqty.qty!!
        if (qty < 1) {
            qty = 1
        }
        qty += 1
        productArrayList[position].qty = qty
        productAdapter.notifyDataSetChanged()

        Log.d("qyuuu", "onAddClickListener: $qty")


    }

    override fun onSubClickListener(position: Int) {
        val Pqty = productArrayList[position]
        qty = Pqty.qty!!
        if (qty <= 1) {
            mViewDataBinding.root.snackbar("atleast select one item")
            return
        }
        qty -= 1
        productArrayList[position].qty = qty
        productAdapter.notifyDataSetChanged()


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

    override fun onAddToCartListener(id: Int) {

        val randomId = generateRandomId()
        Log.d("TAG", "onViewCredsdsdsated: $randomId")
        PrefHelper.getInstance(requireContext()).savaUserId(randomId.toString())


        /*
                if (token.isNullOrBlank()) {
                    DialogHelperClass.LoginDialog(
                        requireContext(), this, true
                    )

                } else {*/

        val params = JsonObject()
        try {
            params.addProperty("product_variation_id", id)
            params.addProperty("quantity", qty)
            params.addProperty("order_day", days)
            params.addProperty("time_slot", time)
            params.addProperty("guest_id", randomId)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        Log.e("UserData", params.toString())

        mViewModel.addCart(params)

        if (!mViewModel.addtocart.hasActiveObservers()) {
            mViewModel.addtocart.observe(requireActivity()) {
                when (it.status) {
                    Resource.Status.LOADING -> {
                        loadingDialog.show()
                    }

                    Resource.Status.SUCCESS -> {
                        loadingDialog.dismiss()
                        it.data?.let { data ->
                            if (data.Flag == 1) {


                                val bundle = Bundle()
                                bundle.putString("days", days.toString())
                                bundle.putString("time", time.toString())

                                navController = Navigation.findNavController(
                                    requireActivity(), R.id.nav_host_fragment
                                )
                                navController.navigate(R.id.checkoutFragment, bundle, options)


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
                    mViewModel.addtocart.removeObservers(viewLifecycleOwner)
                }
            }
        }


    }

    fun generateRandomId(): Int {
        val random = Random.Default
        return random.nextInt(10000, 100000)
    }


}
