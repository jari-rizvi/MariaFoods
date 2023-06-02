package com.teamx.mariaFoods.ui.fragments.Auth.resetPass

import android.os.Bundle
import android.util.Log
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
import com.teamx.mariaFoods.databinding.FragmentChangePasswordBinding
import com.teamx.mariaFoods.ui.fragments.Auth.changePassword.ChangePasswordViewModel
import com.teamx.mariaFoods.utils.DialogHelperClass
import com.teamx.mariaFoods.utils.snackbar
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONException


@AndroidEntryPoint
class ResetPasswordFragment() : BaseFragment<FragmentChangePasswordBinding, ChangePasswordViewModel>() {

    override val layoutId: Int
        get() = R.layout.fragment_change_password
    override val viewModel: Class<ChangePasswordViewModel>
        get() = ChangePasswordViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel


    private lateinit var options: NavOptions
    private var email: String? = null
    private var code: String? = null

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



       mViewDataBinding.btnReset.setOnClickListener {
           validate()
       }
        initialization()

    }

    private fun initialization() {
        val bundle = arguments
        if (bundle != null) {
            email = bundle.getString("email").toString()
            code = bundle.getString("otp").toString()

            Log.d("TAG", "ResetCpde: $code")
        }
    }

    private fun changePassCall() {
        super.subscribeToNetworkLiveData()


        val params = JsonObject()
        try {
            params.addProperty("email", email)
            params.addProperty("otp", code)
            params.addProperty("password", mViewDataBinding.newPass.text.toString())
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        mViewModel.resetPasswordChange(params)

        if (!mViewModel.resetPasswordChangeResponse.hasActiveObservers()) {
            mViewModel.resetPasswordChangeResponse.observe(requireActivity(), Observer {
                when (it.status) {
                    Resource.Status.LOADING -> {
                        loadingDialog.show()
                    }
                    Resource.Status.SUCCESS -> {
                        loadingDialog.dismiss()
                        it.data?.let { data ->
                            navController = Navigation.findNavController(
                                requireActivity(), R.id.nav_host_fragment
                            )
                            navController.navigate(R.id.logInFragment, null, options)
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

    private fun validate(): Boolean {
        if (mViewDataBinding.newPass.text.toString().trim().isEmpty()) {
            mViewDataBinding.root.snackbar(getString(R.string.enter_Password))
            return false
        }
        if (mViewDataBinding.newPass.text.toString().trim().length < 8) {
            mViewDataBinding.root.snackbar(getString(R.string.password_8_character))
            return false
        }
        if (mViewDataBinding.CnfrmPass.text.toString().trim().isEmpty()) {
            mViewDataBinding.root.snackbar(getString(R.string.enter_Password))
            return false
        }
        if (mViewDataBinding.CnfrmPass.text.toString().trim().length < 7) {
            mViewDataBinding.root.snackbar(getString(R.string.password_8_character))
            return false
        }
        if(!mViewDataBinding.newPass.text.toString().equals(mViewDataBinding.CnfrmPass.text.toString())){
            mViewDataBinding.root.snackbar("Password does not match")
        }

        changePassCall()
        return true
    }
}
