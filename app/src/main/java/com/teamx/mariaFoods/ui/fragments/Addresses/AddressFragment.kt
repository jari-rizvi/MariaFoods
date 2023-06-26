package com.teamx.mariaFoods.ui.fragments.Addresses

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.navigation.NavOptions
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
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
import org.json.JSONObject
import java.io.IOException
import java.util.*

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
    private var countryName: String?? = null

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>


    private var name: String? = ""
    private lateinit var country: String
    private lateinit var city: String
    private var address1: String? = ""
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
            getCurrentLocation()
        }

        mViewDataBinding.bottomSheetLayout1.btnLocation.setOnClickListener {
            getCurrentLocation()

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

            if (/*!country!!.isEmpty() ||*/!city!!.isEmpty() || !address1!!.isEmpty() || !postal!!.isEmpty() || !statee!!.isEmpty()) {

                val params = JsonObject()
                try {
                    params.addProperty("name", name)
                    params.addProperty("country", country)
                    params.addProperty("city", city)
                    params.addProperty("address_1", address1)
                    params.addProperty("address_2", address1)
                    params.addProperty("postal", postal)
                    params.addProperty("state", statee)

                    if (mViewDataBinding.bottomSheetLayout.defaultAddress.isChecked) {
                        params.addProperty("is_default", 1)

                    } else {
                        params.addProperty("is_default", 0)
                    }
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

                                    val jsonObject = JSONObject(data.toString())

                                    val Flag = jsonObject.getInt("Flag")
                                    val Message = jsonObject.getString("Message")

                                    if (Flag == 1) {
                                        val state =
                                            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) BottomSheetBehavior.STATE_COLLAPSED
                                            else BottomSheetBehavior.STATE_EXPANDED
                                        bottomSheetBehavior.state = state

                                        addressArrayList.add(
                                            Data(
                                                name = name,
                                                country = country,
                                                city = city,
                                                address_1 = address1,
                                                address_2 = address1,
                                                postal = postal,
                                                state = statee,
                                                id = 0,
                                                is_default = 0,
                                                long = "",
                                                lat = "",
                                                user_id = 0
                                            )
                                        )

                                        addressAdapter.notifyDataSetChanged()
//                                        mViewModel.getAddress()

                                    } else {
                                        showToast(Message)
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


                            val jsonObject = JSONObject(data.toString())

                            val Flag = jsonObject.getInt("Flag")
                            val Data = jsonObject.getJSONObject("data")

                            addressArrayList.add(
                                Data(
                                    name = name,
                                    country = country,
                                    city = city,
                                    address_1 = address1,
                                    address_2 = address1,
                                    postal = postal,
                                    state = statee,
                                    id = 0,
                                    is_default = 0,
                                    long = "",
                                    lat = "",
                                    user_id = 0
                                )
                            )


//                            data.data.forEach {
//
//                                addressArrayList.add(it)
//                            }
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

                    mViewDataBinding.bottomSheetLayout1.editAddress1.setText(addressText.toString())
                    mViewDataBinding.bottomSheetLayout1.city.setText(city)
                    mViewDataBinding.bottomSheetLayout1.etState.setText(state)
                    mViewDataBinding.bottomSheetLayout1.etPostal.setText(postalCode)
                    mViewDataBinding.bottomSheetLayout1.country.setText(country)


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

    private fun addressRecyclerview() {
        addressArrayList = ArrayList()

        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        mViewDataBinding.addressRecycler.layoutManager = linearLayoutManager

        addressAdapter = AddressAdapter(addressArrayList, this)
        mViewDataBinding.addressRecycler.adapter = addressAdapter

    }

    fun initialization() {
        address1 = mViewDataBinding.bottomSheetLayout.editAddress1.text.toString()
        postal = mViewDataBinding.bottomSheetLayout.etPostal.text.toString()
        statee = mViewDataBinding.bottomSheetLayout.etState.text.toString()
        city = mViewDataBinding.bottomSheetLayout.city.text.toString()
//        country = mViewDataBinding.bottomSheetLayout.country.selectedCountryName
        country = mViewDataBinding.bottomSheetLayout.country.text.toString()


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
                    "address_1", mViewDataBinding.bottomSheetLayout1.editAddress1.text.toString()
                )
                params.addProperty(
                    "address_2", mViewDataBinding.bottomSheetLayout1.editAddress2.text.toString()
                )
                params.addProperty(
                    "postal", mViewDataBinding.bottomSheetLayout1.etPostal.text.toString()
                )
                params.addProperty(
                    "state", mViewDataBinding.bottomSheetLayout1.etState.text.toString()
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
                                val jsonObject = JSONObject(data.toString())

                                val Flag = jsonObject.getInt("Flag")
                                val Message = jsonObject.getString("Message")


                                val state =
                                    if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) BottomSheetBehavior.STATE_COLLAPSED
                                    else BottomSheetBehavior.STATE_EXPANDED
                                bottomSheetBehavior.state = state

                                addressAdapter.notifyDataSetChanged()
                                addressArrayList.clear()
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


        mViewModel.editAddress(itemidAddress.id!!)

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
                        mViewDataBinding.bottomSheetLayout1.city?.setText(data.data.city?.toString())
                        mViewDataBinding.bottomSheetLayout1.country?.setText(data.data.country?.toString())

                        Log.d("TAG", "cityy: ${data.data.city} ")
                        Log.d("TAG", "state: ${data.data.state} ")

                        if (data.data.name == "Home") {
                            mViewDataBinding.bottomSheetLayout1.btnHome.isChecked = true
                        } else if (data.data.name == "Work") {
                            mViewDataBinding.bottomSheetLayout1.btnWork.isChecked = true
                        } else if (data.data.name == "Other") {
                            mViewDataBinding.bottomSheetLayout1.btnOther.isChecked = true
                        } else {

                        }


                        addressAdapter.notifyDataSetChanged()


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

        mViewModel.deleteAddress(addressid.id!!)

        mViewModel.deleteaddress.observe(requireActivity()) {
            when (it.status) {
                Resource.Status.LOADING -> {
                    loadingDialog.show()
                }
                Resource.Status.SUCCESS -> {
                    loadingDialog.dismiss()
                    it.data?.let { data ->

                        val jsonObject = JSONObject(data.toString())

                        val Flag = jsonObject.getInt("Flag")
                        val Message = jsonObject.getString("Message")

                        if (Flag == 1) {
                            addressArrayList.clear()
                            mViewModel.getAddress()

                            addressAdapter.notifyDataSetChanged()
                            mViewDataBinding.root.snackbar(Message)

                        } else {
                            showToast(
                                Message
                            )

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

    override fun onCountrySelected() {
        countryName = ccp!!.selectedCountryName.toString()
    }


}