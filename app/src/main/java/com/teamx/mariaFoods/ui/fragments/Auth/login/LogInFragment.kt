package com.teamx.mariaFoods.ui.fragments.Auth.login

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.NavOptions
import androidx.navigation.navOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.JsonObject
import com.teamx.mariaFoods.BR
import com.teamx.mariaFoods.R
import com.teamx.mariaFoods.baseclasses.BaseFragment
import com.teamx.mariaFoods.data.remote.Resource
import com.teamx.mariaFoods.databinding.FragmentLoginBinding
import com.teamx.mariaFoods.utils.DialogHelperClass
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONException

@AndroidEntryPoint
class LogInFragment :
    BaseFragment<FragmentLoginBinding, LoginViewModel>() {

    override val layoutId: Int
        get() = R.layout.fragment_login
    override val viewModel: Class<LoginViewModel>
        get() = LoginViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel


    private lateinit var options: NavOptions
    private var userEmail: String? = null
    private var password: String? = null


    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>


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

        mViewDataBinding.btn.setOnClickListener {
            subscribeToNetworkLiveData()
        }

        bottomSheetBehavior =
            BottomSheetBehavior.from(mViewDataBinding.bottomSheetLayout.bottomSheet)

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
               when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> "Close Persistent Bottom Sheet"
                    BottomSheetBehavior.STATE_COLLAPSED -> "Open Persistent Bottom Sheet"
                    else -> "Persistent Bottom Sheet"
                }
            }
        })


        //#4 Changing the BottomSheet State on ButtonClick
        mViewDataBinding.btn.setOnClickListener {
            val state =
                if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED)
                    BottomSheetBehavior.STATE_COLLAPSED
                else
                    BottomSheetBehavior.STATE_EXPANDED
            bottomSheetBehavior.state = state
        }

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
        password = mViewDataBinding.pass.text.toString().trim()
    }

    override fun subscribeToNetworkLiveData() {
        super.subscribeToNetworkLiveData()

        initialization()

        if (!userEmail!!.isEmpty() || !password!!.isEmpty()) {

            val params = JsonObject()
            try {
                params.addProperty("phone", userEmail)
                params.addProperty("password", password)
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            Log.e("UserData", params.toString())

            mViewModel.loginPhone(params)

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
                                    showToast("agaaydata")
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