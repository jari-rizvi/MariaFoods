package com.teamx.mariaFoods.ui.fragments.Auth.signup

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.util.Patterns
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.navOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.JsonObject
import com.teamx.mariaFoods.BR
import com.teamx.mariaFoods.R
import com.teamx.mariaFoods.baseclasses.BaseFragment
import com.teamx.mariaFoods.data.remote.Resource
import com.teamx.mariaFoods.databinding.FragmentSignupEmailBinding
import com.teamx.mariaFoods.utils.DialogHelperClass
import com.teamx.mariaFoods.utils.snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONException


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
    private var Cpassword: String? = null
    private lateinit var fcmToken: String

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

        mViewDataBinding.btnSignup.setOnClickListener {
            validate()
        }

        mViewDataBinding.btnBack.setOnClickListener {
            popUpStack()
        }

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("TAG", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            fcmToken = task.result


            Log.d("TokeeennnFcm", "onViewCreated: $fcmToken")


        })

        mViewDataBinding.showPassword.setOnClickListener {
            mViewDataBinding.pass.transformationMethod =
                HideReturnsTransformationMethod.getInstance();
            mViewDataBinding.hidePassword.visibility = View.VISIBLE
            mViewDataBinding.showPassword.visibility = View.GONE
        }

        mViewDataBinding.hidePassword.setOnClickListener {
            mViewDataBinding.pass.transformationMethod = PasswordTransformationMethod.getInstance();
            mViewDataBinding.hidePassword.visibility = View.GONE
            mViewDataBinding.showPassword.visibility = View.VISIBLE
        }

        mViewDataBinding.btnAlreadyRegister.setOnClickListener {
            navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
            navController.navigate(R.id.logInEmailFragment, null, options)
        }

        mViewDataBinding.showPasswordConfirm.setOnClickListener {
            mViewDataBinding.cnfrmPass.transformationMethod =
                HideReturnsTransformationMethod.getInstance()
            mViewDataBinding.hidePasswordConfirm.visibility = View.VISIBLE
            mViewDataBinding.showPasswordConfirm.visibility = View.GONE

        }

        mViewDataBinding.hidePasswordConfirm.setOnClickListener {
            mViewDataBinding.cnfrmPass.transformationMethod =
                PasswordTransformationMethod.getInstance()
            mViewDataBinding.hidePasswordConfirm.visibility = View.GONE
            mViewDataBinding.showPasswordConfirm.visibility = View.VISIBLE
        }
    }


    private fun initialization() {
        userEmail = mViewDataBinding.email.text.toString().trim()
        password = mViewDataBinding.pass.text.toString().trim()
        Cpassword = mViewDataBinding.cnfrmPass.text.toString().trim()

    }

    fun validate(): Boolean {

        if (!Patterns.EMAIL_ADDRESS.matcher(mViewDataBinding.email.text.toString().trim())
                .matches()
        ) {
            mViewDataBinding.root.snackbar(getString(R.string.invalid_email))
            return false
        }
        if (mViewDataBinding.pass.text.toString().trim().isEmpty()) {
            mViewDataBinding.root.snackbar(getString(R.string.enter_Password))
            return false
        }
        if (mViewDataBinding.pass.text.toString().trim().length < 8) {
            mViewDataBinding.root.snackbar(getString(R.string.password_8_character))
            return false
        }
        if (mViewDataBinding.cnfrmPass.text.toString().trim().isEmpty()) {
            mViewDataBinding.root.snackbar(getString(R.string.enter_Password))
            return false
        }
        if (mViewDataBinding.cnfrmPass.text.toString().trim().length < 7) {
            mViewDataBinding.root.snackbar(getString(R.string.password_8_character))
            return false
        }
        if (!mViewDataBinding.pass.text.toString().trim()
                .equals(mViewDataBinding.cnfrmPass.text.toString().trim())
        ) {
            mViewDataBinding.root.snackbar(getString(R.string.password_does_not_match))
            return false
        }

        if (mViewDataBinding.cbPolicy.isChecked) {
            subscribeToNetworkLiveData()

        } else {
            mViewDataBinding.root.snackbar("Please Agree to continue")
        }

        return true

    }


    override fun subscribeToNetworkLiveData() {
        super.subscribeToNetworkLiveData()

        initialization()

        if (!userEmail!!.isEmpty() || !password!!.isEmpty() || !Cpassword!!.isEmpty()) {

            val params = JsonObject()
            try {
                params.addProperty("email", userEmail)
                params.addProperty("password", password)
                params.addProperty("through", "email")
                params.addProperty("fcm_token", fcmToken)

            } catch (e: JSONException) {
                e.printStackTrace()
            }

            Log.e("UserData", params.toString())

            mViewModel.signup(params)

            if (!mViewModel.signupResponse.hasActiveObservers()) {
                mViewModel.signupResponse.observe(requireActivity()) {
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
                                            data.User
                                        )
                                    }


                                    val bundle = Bundle()
                                    bundle.putString("email", data.User?.email)

                                    navController =
                                        Navigation.findNavController(
                                            requireActivity(),
                                            R.id.nav_host_fragment
                                        )
                                    navController.navigate(
                                        R.id.otpRegisterEmailFragment,
                                        bundle,
                                        options
                                    )
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
                    if (isAdded) {
                        mViewModel.signupResponse.removeObservers(viewLifecycleOwner)
                    }
                }


            }
        }
    }


}