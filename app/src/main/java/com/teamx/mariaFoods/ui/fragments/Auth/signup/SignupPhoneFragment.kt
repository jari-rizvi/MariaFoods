package com.teamx.mariaFoods.ui.fragments.Auth.signup

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.navOptions
import com.google.gson.JsonObject
import com.hbb20.CountryCodePicker
import com.teamx.mariaFoods.BR
import com.teamx.mariaFoods.R
import com.teamx.mariaFoods.baseclasses.BaseFragment
import com.teamx.mariaFoods.data.remote.Resource
import com.teamx.mariaFoods.databinding.FragmentSignupPhoneBinding
import com.teamx.mariaFoods.ui.fragments.Auth.login.LoginViewModel
import com.teamx.mariaFoods.utils.DialogHelperClass
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONException
import org.json.JSONObject

@AndroidEntryPoint
class SignupPhoneFragment :
    BaseFragment<FragmentSignupPhoneBinding, LoginViewModel>(),
    CountryCodePicker.OnCountryChangeListener {

    override val layoutId: Int
        get() = R.layout.fragment_signup_phone
    override val viewModel: Class<LoginViewModel>
        get() = LoginViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel


    private lateinit var options: NavOptions
    private lateinit var phone: String
    private var ccp: CountryCodePicker? = null
    private var countryCode: String? = null
    private var countryName: String? = null


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

        mViewDataBinding.countryCode.registerCarrierNumberEditText(mViewDataBinding.etPhone)


        mViewDataBinding.btnNext.setOnClickListener {
            subscribeToNetworkLiveData()
        }
        mViewDataBinding.btnBack.setOnClickListener {
            popUpStack()
        }


//        mViewDataBinding.btn.setOnClickListener {
//            subscribeToNetworkLiveData()
//        }
//
//
//        //#4 Changing the BottomSheet State on ButtonClick
//        mViewDataBinding.btn.setOnClickListener {
//
//        }

//        bottomSheetBehavior.addBottomSheetCallback(object :
//            BottomSheetBehavior.BottomSheetCallback() {
//
//
//            override fun onStateChanged(bottomSheet: View, newState: Int) {
//
//                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
//                    mViewDataBinding.email.visibility = View.GONE
//                    mViewDataBinding.bottomSheetLayout.bottomSheet = View.VISIBLE
//
//
//                } else {
//                    mViewDataBinding.email.visibility = View.VISIBLE
//                }
//
//            }
//
//            override fun onSlide(bottomSheet: View, slideOffset: Float) {
//
//            }
//
//
//        })


    }

    private fun initialization() {
        phone = mViewDataBinding.etPhone.text.toString().trim()
        ccp = mViewDataBinding.countryCode
        ccp!!.setOnCountryChangeListener(this)



    }

    override fun subscribeToNetworkLiveData() {
        super.subscribeToNetworkLiveData()

        initialization()

        if (!phone!!.isEmpty()) {

            val params = JsonObject()
            try {
                params.addProperty("phone", mViewDataBinding.countryCode.fullNumberWithPlus)
                params.addProperty("through", "phone")
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            Log.e("UserData", params.toString())

            mViewModel.loginPhone(params)

            if (!mViewModel.loginPhoneResponse.hasActiveObservers()) {
                mViewModel.loginPhoneResponse.observe(requireActivity()) {
                    when (it.status) {
                        Resource.Status.LOADING -> {
                            loadingDialog.show()
                        }
                        Resource.Status.SUCCESS -> {
                            loadingDialog.dismiss()
                            it.data?.let { data ->
                                val jsonObject = JSONObject(data.toString())

                                val Flag = jsonObject.getInt("Flag")
                                val phone = jsonObject.getString("phone")
                                val Message = jsonObject.getString("Message")

                                if (Flag == 1) {
                                    val bundle = Bundle()
                                    bundle.putString("phone", phone)
                                    navController =
                                        Navigation.findNavController(
                                            requireActivity(),
                                            R.id.nav_host_fragment
                                        )
                                    navController.navigate(R.id.otpRegisterFragment, bundle, options)
                                }
                                else{
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
                        mViewModel.loginPhoneResponse.removeObservers(viewLifecycleOwner)
                    }
                }
            }

        }
    }

    override fun onCountrySelected() {
        countryCode = ccp!!.selectedCountryCode
        countryName = ccp!!.selectedCountryName

    }


}