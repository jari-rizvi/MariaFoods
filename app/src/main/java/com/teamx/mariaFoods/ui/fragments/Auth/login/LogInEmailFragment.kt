package com.teamx.mariaFoods.ui.fragments.Auth.login

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
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
import com.teamx.mariaFoods.databinding.FragmentLoginEmailBinding
import com.teamx.mariaFoods.utils.DialogHelperClass
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONException

@AndroidEntryPoint
class LogInEmailFragment : BaseFragment<FragmentLoginEmailBinding, LoginViewModel>() {

    override val layoutId: Int
        get() = R.layout.fragment_login_email
    override val viewModel: Class<LoginViewModel>
        get() = LoginViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel


    private lateinit var options: NavOptions
    private lateinit var userEmail: String
    private lateinit var password: String

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

        mViewDataBinding.btnForgot.setOnClickListener {
            navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
            navController.navigate(R.id.resetEmailFragment, null, options)
        }

        mViewDataBinding.textView65.setOnClickListener {
            navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
            navController.navigate(R.id.signupFragment, null, options)
        }

        mViewDataBinding.btnBack.setOnClickListener {
            popUpStack()
        }

        mViewDataBinding.btnLogin.setOnClickListener {
            subscribeToNetworkLiveData()
        }

        mViewDataBinding.showPassword.setOnClickListener {
            mViewDataBinding.password.transformationMethod =
                HideReturnsTransformationMethod.getInstance();
            mViewDataBinding.hidePassword.visibility = View.VISIBLE
            mViewDataBinding.showPassword.visibility = View.GONE
        }

        mViewDataBinding.hidePassword.setOnClickListener {
            mViewDataBinding.password.transformationMethod =
                PasswordTransformationMethod.getInstance();
            mViewDataBinding.hidePassword.visibility = View.GONE
            mViewDataBinding.showPassword.visibility = View.VISIBLE
        }


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
        userEmail = mViewDataBinding.email.text.toString().trim()
        password = mViewDataBinding.password.text.toString().trim()
    }

    override fun subscribeToNetworkLiveData() {
        super.subscribeToNetworkLiveData()

        initialization()

        if (!userEmail!!.isEmpty() || !password!!.isEmpty()) {

            val params = JsonObject()
            try {
                params.addProperty("email", userEmail)
                params.addProperty("password", password)
                params.addProperty("through", "email&pass")
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            Log.e("UserData", params.toString())

            mViewModel.loginEmail(params)

            if (!mViewModel.loginResponse.hasActiveObservers()) {
                mViewModel.loginResponse.observe(requireActivity()) {
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


                                        val firstname = data.User.first_name
                                        val lastname = data.User.last_name
                                        val email = data.User.email
                                        val number = data.User.phone

                                        dataStoreProvider.saveUserDetails(
                                            firstname, lastname, email, number
                                        )

                                    }

                                    navController = Navigation.findNavController(
                                        requireActivity(), R.id.nav_host_fragment
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
                    if (isAdded) {
                        mViewModel.loginResponse.removeObservers(viewLifecycleOwner)
                    }
                }
            }

        }
    }


}