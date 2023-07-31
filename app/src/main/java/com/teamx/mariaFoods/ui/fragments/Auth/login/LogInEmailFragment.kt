package com.teamx.mariaFoods.ui.fragments.Auth.login

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.util.Patterns
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.navOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.JsonObject
import com.teamx.mariaFoods.BR
import com.teamx.mariaFoods.R
import com.teamx.mariaFoods.baseclasses.BaseFragment
import com.teamx.mariaFoods.data.dataclasses.login.User
import com.teamx.mariaFoods.data.remote.Resource
import com.teamx.mariaFoods.databinding.FragmentLoginEmailBinding
import com.teamx.mariaFoods.utils.DialogHelperClass
import com.teamx.mariaFoods.utils.PrefHelper
import com.teamx.mariaFoods.utils.snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject

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


        FirebaseApp.initializeApp(requireContext())
        Firebase.initialize(requireContext())
        askNotificationPermission()

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
            validate()
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


        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("TAG", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            fcmToken = task.result


            Log.d("TokeeennnFcm", "onViewCreated: $fcmToken")


        })


    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
            Firebase.initialize(requireContext())
            FirebaseApp.initializeApp(requireContext())
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("123123", "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new FCM registration token

                fcmToken = task.result

                // Log and toast
//                val msg = getString(R.string.about_us, token)
//                Log.d("TAG", msg)
            })

        } else {
//             Inform user that that your app will not show notifications.
        }
    }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(), Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {


                FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w("123123", "Fetching FCM registration token failed", task.exception)
                        return@OnCompleteListener
                    }

                    // Get new FCM registration token
                    fcmToken = task.result


                    // Log and toast
//                val msg = getString(R.string.about_us, token)
//                Log.d("TAG", msg)
                })
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {

            } else {
                // Directly ask for t
                //
                //
                // he permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun initialization() {
        userEmail = mViewDataBinding.email.text.toString().trim()
        password = mViewDataBinding.password.text.toString().trim()
    }


    var Guser_id = ""

    override fun subscribeToNetworkLiveData() {
        super.subscribeToNetworkLiveData()

        initialization()
        Guser_id = PrefHelper.getInstance(requireContext()).getUserId!!
        Log.d("TAG", "onViewCredsdsdsated: $Guser_id")

        if (!userEmail!!.isEmpty() || !password!!.isEmpty()) {

            val paramsGuest = JsonObject()
            try {
                paramsGuest.addProperty("email", userEmail)
                paramsGuest.addProperty("password", password)
                paramsGuest.addProperty("guest_id", Guser_id)
                paramsGuest.addProperty("fcm_token", fcmToken)
                paramsGuest.addProperty("through", "email&pass")
            } catch (e: JSONException) {
                e.printStackTrace()
            }


            val params = JsonObject()
            try {
                params.addProperty("email", userEmail)
                params.addProperty("password", password)
                params.addProperty("fcm_token", fcmToken)
                params.addProperty("through", "email&pass")
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            if (Guser_id.isNullOrEmpty()) {
                mViewModel.loginEmail(params)
                if (!mViewModel.loginResponse.hasActiveObservers()) {
                    mViewModel.loginResponse.observe(requireActivity()) {
                        when (it.status) {
                            Resource.Status.LOADING -> {
                                loadingDialog.show()
                            }

                            Resource.Status.SUCCESS -> {
                                Log.d("UserData", it.data.toString())
                                loadingDialog.dismiss()
                                it.data?.let { data ->

                                    val jsonObject = JSONObject(data.toString())

                                    val Flag = jsonObject.getInt("Flag")
                                    val AccessToken = jsonObject.getString("AccessToken")
                                    val Message = jsonObject.getString("Message")
                                    val User = jsonObject.getJSONObject("User")
                                    if (Flag == 1) {
                                        lifecycleScope.launch(Dispatchers.IO) {
                                            dataStoreProvider.saveUserToken(AccessToken)


                                            val user = User(
                                                id = User.getInt("id"),
                                                first_name = User.getString("first_name"),
                                                last_name = User.getString("last_name"),
                                                email = User.getString("email"),
                                                phone = User.getString("phone"),
                                                email_or_otp_verified = User.getInt("email_or_otp_verified"),
                                                fcm = User.getInt("fcm"),
                                                provider_id = User.getString("provider_id"),
                                                avatar = User.getString("avatar"),
                                                name = User.getString("name"),
                                                with_email_and_pass = User.getBoolean("with_email_and_pass")
                                            )

                                            val firstname = user.first_name
                                            val lastname = user.last_name
                                            val email = user.email
                                            val number = user.phone
                                            dataStoreProvider.saveUserDetails(
                                                user
                                            )

                                        }

                                        navController = Navigation.findNavController(
                                            requireActivity(), R.id.nav_host_fragment
                                        )
                                        navController.navigate(R.id.dashboardFragment, null, options)


                                    } else {
                                        showToast(
                                            Message
                                        )

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
            } else {
                mViewModel.loginEmail(paramsGuest)
                if (!mViewModel.loginResponse.hasActiveObservers()) {
                    mViewModel.loginResponse.observe(requireActivity()) {
                        when (it.status) {
                            Resource.Status.LOADING -> {
                                loadingDialog.show()
                            }

                            Resource.Status.SUCCESS -> {
                                Log.d("UserData", it.data.toString())
                                loadingDialog.dismiss()
                                it.data?.let { data ->

                                    val jsonObject = JSONObject(data.toString())

                                    val Flag = jsonObject.getInt("Flag")

                                    val AccessToken = jsonObject.getString("AccessToken")
                                    val Message = jsonObject.getString("Message")
                                    val User = jsonObject.getJSONObject("User")
                                    if (Flag == 1) {
                                        lifecycleScope.launch(Dispatchers.IO) {
                                            dataStoreProvider.saveUserToken(AccessToken)


                                            val user = User(
                                                id = User.getInt("id"),
                                                first_name = User.getString("first_name"),
                                                last_name = User.getString("last_name"),
                                                email = User.getString("email"),
                                                phone = User.getString("phone"),
                                                email_or_otp_verified = User.getInt("email_or_otp_verified"),
                                                fcm = User.getInt("fcm"),
                                                provider_id = User.getString("provider_id"),
                                                avatar = User.getString("avatar"),
                                                name = User.getString("name"),
                                                with_email_and_pass = User.getBoolean("with_email_and_pass")
                                            )

                                            val firstname = user.first_name
                                            val lastname = user.last_name
                                            val email = user.email
                                            val number = user.phone
                                            dataStoreProvider.saveUserDetails(
                                                user
                                            )

                                        }

                                        navController = Navigation.findNavController(
                                            requireActivity(), R.id.nav_host_fragment
                                        )
                                        navController.navigate(R.id.checkoutFragment, null, options)


                                    } else {
                                        showToast(
                                            Message
                                        )

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

    private fun validate(): Boolean {
        if (!Patterns.EMAIL_ADDRESS.matcher(mViewDataBinding.email.text.toString().trim())
                .matches()
        ) {
            mViewDataBinding.root.snackbar(getString(R.string.invalid_email))
            return false
        }
        if (mViewDataBinding.password.text.toString().trim().isEmpty()) {
            mViewDataBinding.root.snackbar(getString(R.string.enter_Password))
            return false
        }
        if (mViewDataBinding.password.text.toString().trim().length < 8) {
            mViewDataBinding.root.snackbar(getString(R.string.password_8_character))
            return false
        }

        subscribeToNetworkLiveData()
        return true
    }


}