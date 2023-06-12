package com.teamx.mariaFoods.ui.fragments.Auth.login

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
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

    override fun subscribeToNetworkLiveData() {
        super.subscribeToNetworkLiveData()

        initialization()

        if (!userEmail!!.isEmpty() || !password!!.isEmpty()) {

            val params = JsonObject()
            try {
                params.addProperty("email", userEmail)
                params.addProperty("password", password)
                params.addProperty("fcm_token", fcmToken)
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
                                            data.User
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