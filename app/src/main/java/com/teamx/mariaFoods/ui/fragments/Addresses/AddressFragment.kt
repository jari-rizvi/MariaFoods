package com.teamx.mariaFoods.ui.fragments.Addresses

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.constraintlayout.motion.widget.Debug.getLocation
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.NavOptions
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.JsonObject
import com.hbb20.CountryCodePicker
import com.teamx.mariaFoods.BR
import com.teamx.mariaFoods.R
import com.teamx.mariaFoods.baseclasses.BaseFragment
import com.teamx.mariaFoods.data.dataclasses.getAddress.Data
import com.teamx.mariaFoods.data.remote.Resource
import com.teamx.mariaFoods.databinding.FragmentAddressBinding
import com.teamx.mariaFoods.ui.activity.mainActivity.MainActivity
import com.teamx.mariaFoods.utils.DialogHelperClass
import com.teamx.mariaFoods.utils.snackbar
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONException

@AndroidEntryPoint
class AddressFragment : BaseFragment<FragmentAddressBinding, AddressViewModel>(), OnAddressListener,
    CountryCodePicker.OnCountryChangeListener {

    override val layoutId: Int
        get() = R.layout.fragment_address
    override val viewModel: Class<AddressViewModel>
        get() = AddressViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel


    private lateinit var options: NavOptions
    lateinit var addressAdapter: AddressAdapter
    lateinit var addressArrayList: ArrayList<Data>


    private var ccp: CountryCodePicker? = null
    private var countryName: String? = null

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>


    private lateinit var name: String
    private lateinit var country: String
    private lateinit var city: String
    private lateinit var address: String
    private lateinit var statee: String
    private lateinit var postal: String

    @SuppressLint("NotifyDataSetChanged")
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

        mViewDataBinding.bottomSheetLayout.btnLocation.setOnClickListener {
            getLocation()
        }

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

        mViewDataBinding.btnAddAddress.setOnClickListener {

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

        mViewDataBinding.bottomSheetLayout.btnAdd.setOnClickListener {
            initialization()

            if (/*!country!!.isEmpty() ||*/!city!!.isEmpty() || !address!!.isEmpty() || !postal!!.isEmpty() || !statee!!.isEmpty()) {

                val params = JsonObject()
                try {
                    params.addProperty("name", name)
                    params.addProperty("country", country)
                    params.addProperty("city", city)
                    params.addProperty("address_1", address)
                    params.addProperty("address_2", address)
                    params.addProperty("postal", postal)
                    params.addProperty("state", statee)
                    params.addProperty("is_default", 0)
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
                                        val state =
                                            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) BottomSheetBehavior.STATE_COLLAPSED
                                            else BottomSheetBehavior.STATE_EXPANDED
                                        bottomSheetBehavior.state = state

                                        /*   addressArrayList.add(
                                               Data(
                                                   name = name,
                                                   country = country,
                                                   city = city,
                                                   address_1 = address,
                                                   address_2 = address,
                                                   postal = postal,
                                                   state = statee,
                                                   id = 0,
                                                   is_default = 0,
                                                   long = "",
                                                   lat = "",
                                                   user_id = 0
                                               )
                                           )

                                           addressAdapter.notifyDataSetChanged()*/
                                        mViewModel.getAddress()

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

        mViewModel.getAddress()

        if (!mViewModel.addressList.hasActiveObservers()) {
            mViewModel.addressList.observe(requireActivity()) {
                when (it.status) {
                    Resource.Status.LOADING -> {
                        loadingDialog.show()
                    }
                    Resource.Status.SUCCESS -> {
                        loadingDialog.dismiss()
                        it.data?.let { data ->

                            if (addressArrayList.isEmpty()) {
                                mViewDataBinding.emptyTV.visibility = View.VISIBLE
                                mViewDataBinding.addressRecycler.visibility = View.GONE
                            }
                            mViewDataBinding.emptyTV.visibility = View.GONE
                            mViewDataBinding.addressRecycler.visibility = View.VISIBLE

                            addressArrayList.addAll(data.data)
                            addressAdapter.notifyDataSetChanged()

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


        mViewDataBinding.bottomSheetLayout1.btnHome.setOnClickListener {
            mViewDataBinding.bottomSheetLayout1.btnHome.isChecked = true
            mViewDataBinding.bottomSheetLayout1.btnWork.isChecked = false
            mViewDataBinding.bottomSheetLayout1.btnOther.isChecked = false
            name = mViewDataBinding.bottomSheetLayout1.btnHome.text.toString()
        }

        mViewDataBinding.bottomSheetLayout1.btnWork.setOnClickListener {
            mViewDataBinding.bottomSheetLayout1.btnWork.isChecked = true
            mViewDataBinding.bottomSheetLayout1.btnHome.isChecked = false
            mViewDataBinding.bottomSheetLayout1.btnOther.isChecked = false
            name = mViewDataBinding.bottomSheetLayout1.btnWork.text.toString()
        }

        mViewDataBinding.bottomSheetLayout1.btnOther.setOnClickListener {
            mViewDataBinding.bottomSheetLayout1.btnOther.isChecked = true
            mViewDataBinding.bottomSheetLayout1.btnWork.isChecked = false
            mViewDataBinding.bottomSheetLayout1.btnHome.isChecked = false
            name = mViewDataBinding.bottomSheetLayout1.btnOther.text.toString()
        }



        addressRecyclerview()
    }

    private fun addressRecyclerview() {
        addressArrayList = ArrayList()

        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        mViewDataBinding.addressRecycler.layoutManager = linearLayoutManager

        addressAdapter = AddressAdapter(addressArrayList, this)
        mViewDataBinding.addressRecycler.adapter = addressAdapter

    }

    fun initialization() {
        address = mViewDataBinding.bottomSheetLayout.editAddress1.text.toString()
        postal = mViewDataBinding.bottomSheetLayout.etPostal.text.toString()
        statee = mViewDataBinding.bottomSheetLayout.etState.text.toString()
        city = mViewDataBinding.bottomSheetLayout.city.text.toString()
        country = mViewDataBinding.bottomSheetLayout.country.selectedCountryName

    }


    override fun oneditClick(position: Int) {

        val itemidAddress = addressArrayList[position]

        bottomSheetBehavior =
            BottomSheetBehavior.from(mViewDataBinding.bottomSheetLayout1.bottomSheetUpdateaddress)

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


        mViewDataBinding.bottomSheetLayout1.btnUpdate.setOnClickListener {

            val params = JsonObject()
            try {
                params.addProperty("name", name)
                params.addProperty("id", itemidAddress.id)
                params.addProperty("country", itemidAddress.country)
                params.addProperty("city", mViewDataBinding.bottomSheetLayout1.city.text.toString())
                params.addProperty(
                    "address_1",
                    mViewDataBinding.bottomSheetLayout1.editAddress1.text.toString()
                )
                params.addProperty(
                    "address_2",
                    mViewDataBinding.bottomSheetLayout1.editAddress2.text.toString()
                )
                params.addProperty(
                    "postal",
                    mViewDataBinding.bottomSheetLayout1.etPostal.text.toString()
                )
                params.addProperty(
                    "state",
                    mViewDataBinding.bottomSheetLayout1.etState.text.toString()
                )
                params.addProperty("is_default", 0)
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


        mViewModel.editAddress(itemidAddress.id)

        mViewModel.editaddress.observe(requireActivity()) {
            when (it.status) {
                Resource.Status.LOADING -> {
                    loadingDialog.show()
                }
                Resource.Status.SUCCESS -> {
                    loadingDialog.dismiss()
                    it.data?.let { data ->

                        data.data.name

                        mViewDataBinding.bottomSheetLayout1.editAddress1?.setText(data.data.address_1?.toString())

                        mViewDataBinding.bottomSheetLayout1.editAddress2?.setText(data.data.address_2?.toString())
                        mViewDataBinding.bottomSheetLayout1.etPostal?.setText(data.data.postal?.toString())
                        mViewDataBinding.bottomSheetLayout1.etState?.setText(data.data.state?.toString())
                        mViewDataBinding.bottomSheetLayout1.etState?.setText(data.data.country?.toString())
                        mViewDataBinding.bottomSheetLayout1.city?.setText(data.data.city?.toString())

                        if (data.data.name == "Home") {
                            mViewDataBinding.bottomSheetLayout1.btnHome.isChecked = true
                        } else if (data.data.name == "Work") {
                            mViewDataBinding.bottomSheetLayout1.btnWork.isChecked = true
                        } else if (data.data.name == "Other") {
                            mViewDataBinding.bottomSheetLayout1.btnOther.isChecked = true
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

    override fun ondeleteClick(position: Int) {
        val addressid = addressArrayList[position]

        mViewModel.deleteAddress(addressid.id)

        mViewModel.deleteaddress.observe(requireActivity()) {
            when (it.status) {
                Resource.Status.LOADING -> {
                    loadingDialog.show()
                }
                Resource.Status.SUCCESS -> {
                    loadingDialog.dismiss()
                    it.data?.let { data ->
                        mViewModel.getAddress()

//                        addressAdapter.notifyDataSetChanged()
                        mViewDataBinding.root.snackbar(data.Message)

                    }
                }
                Resource.Status.ERROR -> {
                    loadingDialog.dismiss()
                    DialogHelperClass.errorDialog(requireContext(), it.message!!)
                }
            }

        }
    }

    override fun onCountrySelected() {
        countryName = ccp!!.selectedCountryName.toString()
    }


}