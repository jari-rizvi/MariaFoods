package com.teamx.mariaFoods.ui.fragments.Auth.otp

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
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

    private var phoneNumber: String? = null

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

            mViewModel.otpVerify(params, this)

            mViewModel.otpVerifyResponse.observe(requireActivity(), Observer {
                when (it.status) {
                    Resource.Status.LOADING -> {
                        loadingDialog.show()
                    }
                    Resource.Status.SUCCESS -> {
                        loadingDialog.dismiss()
                        it.data?.let { data ->

                            if (data.Flag == 1) {

                                lifecycleScope.launch(Dispatchers.IO) {
                                    dataStoreProvider.saveUserToken(data.AccessToken)
                                }
                                navController =
                                    Navigation.findNavController(
                                        requireActivity(),
                                        R.id.nav_host_fragment
                                    )
                                navController.navigate(R.id.dashboardFragment, null, options)
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
            })


        }
    }

}
