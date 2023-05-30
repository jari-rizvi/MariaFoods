package com.teamx.mariaFoods.ui.fragments.Auth.signup

import android.os.Bundle
import android.view.View
import androidx.navigation.NavOptions
import androidx.navigation.navOptions
import com.teamx.mariaFoods.BR
import com.teamx.mariaFoods.R
import com.teamx.mariaFoods.baseclasses.BaseFragment
import com.teamx.mariaFoods.databinding.FragmentSignupEmailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupemailFragment :
    BaseFragment<FragmentSignupEmailBinding, SignupViewModel>() {

    override val layoutId: Int
        get() = R.layout.fragment_signup_email
    override val viewModel: Class<SignupViewModel>
        get() = SignupViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel


    private lateinit var options: NavOptions
    private var userEmail: String? = null
    private var password: String? = null
    private var phone: String? = null
    private var lname: String? = null
    private var fname: String? = null

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

//        mViewDataBinding.btn.setOnClickListener {
//            subscribeToNetworkLiveData()
//        }

    }

//    private fun initialization() {
//        userEmail = mViewDataBinding.email.text.toString().trim()
//        password = mViewDataBinding.pass.text.toString().trim()
//        phone = mViewDataBinding.phone.text.toString().trim()
//        lname = mViewDataBinding.lname.text.toString().trim()
//        fname = mViewDataBinding.name.text.toString().trim()
//    }
//
//    override fun subscribeToNetworkLiveData() {
//        super.subscribeToNetworkLiveData()
//
//        initialization()
//
//        if (!userEmail!!.isEmpty() || !password!!.isEmpty() || !fname!!.isEmpty() || !lname!!.isEmpty() || !phone!!.isEmpty()) {
//
//            val params = JsonObject()
//            try {
//                params.addProperty("email", userEmail)
//                params.addProperty("password", password)
//                params.addProperty("firstname", fname)
//                params.addProperty("lastname", lname)
//                params.addProperty("phone", phone)
//            } catch (e: JSONException) {
//                e.printStackTrace()
//            }
//
//            Log.e("UserData", params.toString())
//
//            mViewModel.signup(params)
//
//            if (!mViewModel.signupResponse.hasActiveObservers()) {
//                mViewModel.signupResponse.observe(requireActivity()) {
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
//                        mViewModel.signupResponse.removeObservers(viewLifecycleOwner)
//                    }
//                }
//            }
//
//        }
//    }


}