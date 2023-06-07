package com.teamx.mariaFoods.ui.fragments.Auth.resetemail

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.navOptions
import com.google.gson.JsonObject
import com.teamx.mariaFoods.BR
import com.teamx.mariaFoods.R
import com.teamx.mariaFoods.baseclasses.BaseFragment
import com.teamx.mariaFoods.data.remote.Resource
import com.teamx.mariaFoods.databinding.FragmentResetEmailBinding
import com.teamx.mariaFoods.utils.DialogHelperClass
import com.teamx.mariaFoods.utils.snackbar
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONException


@AndroidEntryPoint
class ResetEmailFragment() : BaseFragment<FragmentResetEmailBinding, ResetEmailViewModel>() {

    override val layoutId: Int
        get() = R.layout.fragment_reset_email
    override val viewModel: Class<ResetEmailViewModel>
        get() = ResetEmailViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel


    private lateinit var options: NavOptions
    private lateinit var userEmail: String

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
            validate()
        }
        mViewDataBinding.textView65.setOnClickListener {
            navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
            navController.navigate(R.id.signupFragment, null, options)
        }

        mViewDataBinding.btnBack.setOnClickListener {
            popUpStack()
        }


    }

    private fun initialization() {
        userEmail = mViewDataBinding.email.text.toString().trim()

    }

    fun validate(): Boolean {
        if (mViewDataBinding.email.text.toString().trim().isEmpty()) {
            mViewDataBinding.root.snackbar("Enter Email")
            return false
        } else {
            subscribeToNetworkLiveData()
            return true
        }

    }

    override fun subscribeToNetworkLiveData() {
        super.subscribeToNetworkLiveData()

        initialization()

        if (!userEmail!!.isEmpty()) {

            val params = JsonObject()
            try {
                params.addProperty("email", userEmail)
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            Log.e("UserData", params.toString())

            mViewModel.resetPass(params)

            if (!mViewModel.resetPassResponse.hasActiveObservers()) {
                mViewModel.resetPassResponse.observe(requireActivity()) {
                    when (it.status) {
                        Resource.Status.LOADING -> {
                            loadingDialog.show()
                        }
                        Resource.Status.SUCCESS -> {
                            loadingDialog.dismiss()
                            it.data?.let { data ->
                                if (data.Flag == 1) {

                                    val bundle = Bundle()
                                    bundle.putString("email", data.email)

                                    navController =
                                        Navigation.findNavController(
                                            requireActivity(),
                                            R.id.nav_host_fragment
                                        )
                                    navController.navigate(
                                        R.id.otpForgotEmailFragment,
                                        bundle,
                                        options
                                    )
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
                        mViewModel.resetPassResponse.removeObservers(viewLifecycleOwner)
                    }
                }


            }
        }
    }


}
