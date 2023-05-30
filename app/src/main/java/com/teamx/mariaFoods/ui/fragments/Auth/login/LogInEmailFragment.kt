package com.teamx.mariaFoods.ui.fragments.Auth.login

import android.os.Bundle
import android.view.View
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.navOptions
import com.teamx.mariaFoods.BR
import com.teamx.mariaFoods.R
import com.teamx.mariaFoods.baseclasses.BaseFragment
import com.teamx.mariaFoods.databinding.FragmentLoginEmailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LogInEmailFragment :
    BaseFragment<FragmentLoginEmailBinding, LoginViewModel>() {

    override val layoutId: Int
        get() = R.layout.fragment_login_email
    override val viewModel: Class<LoginViewModel>
        get() = LoginViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel


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

        mViewDataBinding.btnForgot.setOnClickListener {
            navController =
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
            navController.navigate(R.id.resetEmailFragment, null, options)
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

//    private fun initialization() {
//        userEmail = mViewDataBinding.email.text.toString().trim()
//        password = mViewDataBinding.pass.text.toString().trim()
//    }
//
//    override fun subscribeToNetworkLiveData() {
//        super.subscribeToNetworkLiveData()
//
//        initialization()
//
//        if (!userEmail!!.isEmpty() || !password!!.isEmpty()) {
//
//            val params = JsonObject()
//            try {
//                params.addProperty("phone", userEmail)
//                params.addProperty("password", password)
//            } catch (e: JSONException) {
//                e.printStackTrace()
//            }
//
//            Log.e("UserData", params.toString())
//
//            mViewModel.loginPhone(params)
//
//            if (!mViewModel.loginResponse.hasActiveObservers()) {
//                mViewModel.loginResponse.observe(requireActivity()) {
//                    when (it.status) {
//                        Resource.Status.LOADING -> {
//                            loadingDialog.show()
//                        }
//                        Resource.Status.SUCCESS -> {
//                            loadingDialog.dismiss()
//                            it.data?.let { data ->
//                                if (data.Flag == 1) {
//                                    showToast("agaaydata")
//                                } else {
//                                    showToast(data.Message)
//                                }
//
//
//                            }
//                        }
//                        Resource.Status.ERROR -> {
//                            loadingDialog.dismiss()
//                            DialogHelperClass.errorDialog(requireContext(), it.message!!)
//                        }
//                    }
//                    if (isAdded) {
//                        mViewModel.loginResponse.removeObservers(viewLifecycleOwner)
//                    }
//                }
//            }
//
//        }
//    }


}