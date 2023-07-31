package com.teamx.mariaFoods.ui.fragments.Auth.otp

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.navOptions
import com.google.gson.JsonObject
import com.teamx.mariaFoods.BR
import com.teamx.mariaFoods.R
import com.teamx.mariaFoods.baseclasses.BaseFragment
import com.teamx.mariaFoods.data.remote.Resource
import com.teamx.mariaFoods.databinding.FragmentOtpRegisterBinding
import com.teamx.mariaFoods.utils.DialogHelperClass
import com.teamx.mariaFoods.utils.PrefHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONException


@AndroidEntryPoint
class OtpRegisterFragment() : BaseFragment<FragmentOtpRegisterBinding, OtpViewModel>() {

    override val layoutId: Int
        get() = R.layout.fragment_otp_register
    override val viewModel: Class<OtpViewModel>
        get() = OtpViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel

    private var phoneNumber: String?? = null

    private lateinit var options: NavOptions
    var Guser_id = ""


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

        Guser_id = PrefHelper.getInstance(requireContext()).getUserId!!
        mViewDataBinding.btnBack.setOnClickListener {
            popUpStack()
        }

        mViewDataBinding.btnVerify.setOnClickListener {

            verifyotp()
        }

        initialization()
    }

    private fun initialization() {
        val bundle = arguments
        if (bundle != null) {
            phoneNumber = bundle.getString("phone").toString()

        }
    }

    private fun verifyotp() {
        val code = mViewDataBinding.pinView.text.toString()
        if (phoneNumber!!.isNotEmpty()) {
            val params = JsonObject()
            try {
                params.addProperty("phone", phoneNumber.toString())
                params.addProperty("otp", code)
                params.addProperty("through", "signin")
            } catch (e: JSONException) {
                e.printStackTrace()
            }

                val paramsGuest = JsonObject()
            try {
                paramsGuest.addProperty("phone", phoneNumber.toString())
                paramsGuest.addProperty("otp", code)
                paramsGuest.addProperty("guest_id", Guser_id)
                paramsGuest.addProperty("through", "signin")
            } catch (e: JSONException) {
                e.printStackTrace()
            }


            if (Guser_id.isNullOrEmpty()) {
                mViewModel.otpVerify(params,this)
                if (!mViewModel.otpVerifyResponse.hasActiveObservers()) { mViewModel.otpVerifyResponse.observe(requireActivity()) {
                    when (it.status) {
                        Resource.Status.LOADING -> {
                            loadingDialog.show()
                        }
                        Resource.Status.SUCCESS -> {
                            loadingDialog.dismiss()
                            it.data?.let { data ->
                                if (data.Flag == 1) {
                                    lifecycleScope.launch(Dispatchers.IO) {
                                        dataStoreProvider.saveUserToken(data.AccessToken!!)

                                        dataStoreProvider.saveUserDetails(
                                            data.User!!
                                        )
                                    }
                                    navController =
                                        Navigation.findNavController(
                                            requireActivity(),
                                            R.id.nav_host_fragment
                                        )
                                    navController.navigate(R.id.dashboardFragment, null, options)
                                }
                                else{
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
                        mViewModel.otpVerifyResponse.removeObservers(viewLifecycleOwner)
                    }
                }
                }
            } else {
                mViewModel.otpVerify(paramsGuest,this)
                if (!mViewModel.otpVerifyResponse.hasActiveObservers()) { mViewModel.otpVerifyResponse.observe(requireActivity()) {
                    when (it.status) {
                        Resource.Status.LOADING -> {
                            loadingDialog.show()
                        }
                        Resource.Status.SUCCESS -> {
                            loadingDialog.dismiss()
                            it.data?.let { data ->
                                if (data.Flag == 1) {
                                    lifecycleScope.launch(Dispatchers.IO) {
                                        dataStoreProvider.saveUserToken(data.AccessToken!!)

                                        dataStoreProvider.saveUserDetails(
                                            data.User!!
                                        )
                                    }
                                    navController =
                                        Navigation.findNavController(
                                            requireActivity(),
                                            R.id.nav_host_fragment
                                        )
                                    navController.navigate(R.id.checkoutFragment, null, options)
                                }
                                else{
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
                        mViewModel.otpVerifyResponse.removeObservers(viewLifecycleOwner)
                    }
                }
                }
            }

//            mViewModel.otpVerify(params, this)
//
//            mViewModel.otpVerifyResponse.observe(requireActivity(), Observer {
//                when (it.status) {
//                    Resource.Status.LOADING -> {
//                        loadingDialog.show()
//                    }
//
//                    Resource.Status.SUCCESS -> {
//                        loadingDialog.dismiss()
//                        it.data?.let { data ->
//
//                            if (data.Flag == 1) {
//
//                                lifecycleScope.launch(Dispatchers.IO) {
//                                    dataStoreProvider.saveUserToken(data.AccessToken!!)
//
//                                    dataStoreProvider.saveUserDetails(
//                                        data.User!!
//                                    )
//                                }
//                                navController =
//                                    Navigation.findNavController(
//                                        requireActivity(),
//                                        R.id.nav_host_fragment
//                                    )
//                                navController.navigate(R.id.dashboardFragment, null, options)
//                            } else {
//                                data.Message?.let { it1 -> showToast(it1) }
//                            }
//                        }
//                    }
//
//                    Resource.Status.ERROR -> {
//                        loadingDialog.dismiss()
//                        DialogHelperClass.errorDialog(requireContext(), it.message!!)
//                    }
//                }
//            })


        }
    }

}
