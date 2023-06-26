package com.teamx.mariaFoods.ui.fragments.Auth.otp

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.navOptions
import com.google.gson.JsonObject
import com.teamx.mariaFoods.BR
import com.teamx.mariaFoods.R
import com.teamx.mariaFoods.baseclasses.BaseFragment
import com.teamx.mariaFoods.data.remote.Resource
import com.teamx.mariaFoods.databinding.FragmentOtpForgotEmailBinding
import com.teamx.mariaFoods.utils.DialogHelperClass
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONException


@AndroidEntryPoint
class OtpForgotEmailFragment() : BaseFragment<FragmentOtpForgotEmailBinding, OtpViewModel>() {

    override val layoutId: Int
        get() = R.layout.fragment_otp_forgot_email
    override val viewModel: Class<OtpViewModel>
        get() = OtpViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel

    private var email: String?? = null

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

        mViewDataBinding.btnVerify.setOnClickListener {

            verifyotp()
        }

        mViewDataBinding.textView65.setOnClickListener {
            navController =
                Navigation.findNavController(
                    requireActivity(),
                    R.id.nav_host_fragment
                )
            navController.navigate(R.id.logInFragment, null, options)
        }


        mViewDataBinding.btnBack.setOnClickListener {
            popUpStack()
        }

        initialization()
    }

    private fun initialization() {
        val bundle = arguments
        if (bundle != null) {
            email = bundle.getString("email").toString()

        }
    }

    private fun verifyotp() {
        val code = mViewDataBinding.pinView.text.toString()
        if (email!!.isNotEmpty()) {
            val params = JsonObject()
            try {
                params.addProperty("email", email.toString())
                params.addProperty("otp", code)
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            mViewModel.otpVerifyForgotEmail(params, this)

            mViewModel.otpVerifyForgotEmailResponse.observe(requireActivity(), Observer {
                when (it.status) {
                    Resource.Status.LOADING -> {
                        loadingDialog.show()
                    }
                    Resource.Status.SUCCESS -> {
                        loadingDialog.dismiss()
                        it.data?.let { data ->

                            if (data.Flag == 1) {

                                val bundle = Bundle()
                                bundle.putString("email", email)
                                bundle.putString("otp", code)

                                navController =
                                    Navigation.findNavController(
                                        requireActivity(),
                                        R.id.nav_host_fragment
                                    )
                                navController.navigate(R.id.resetPasswordFragment, bundle, options)
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
            })


        }
    }

}
