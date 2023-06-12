package com.teamx.mariaFoods.ui.fragments.Auth.resetPass

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
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
import com.teamx.mariaFoods.databinding.FragmentChangePasswordBinding
import com.teamx.mariaFoods.ui.fragments.Auth.changePassword.ChangePasswordViewModel
import com.teamx.mariaFoods.utils.DialogHelperClass
import com.teamx.mariaFoods.utils.snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONException


@AndroidEntryPoint
class ResetPasswordFragment() :
    BaseFragment<FragmentChangePasswordBinding, ChangePasswordViewModel>() {

    override val layoutId: Int
        get() = R.layout.fragment_change_password
    override val viewModel: Class<ChangePasswordViewModel>
        get() = ChangePasswordViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel


    private lateinit var options: NavOptions
    private var email: String? = null
    private var code: String? = null
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



        mViewDataBinding.btnReset.setOnClickListener {
            validate()
        }
        initialization()

        FirebaseApp.initializeApp(requireContext())
        Firebase.initialize(requireContext())
        askNotificationPermission()

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
            params.addProperty("fcm_token", fcmToken)
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

                            lifecycleScope.launch(Dispatchers.IO) {
                                dataStoreProvider.saveUserToken(data.AccessToken)

                                dataStoreProvider.saveUserDetails(
                                    data.User
                                )

                            }
                            navController = Navigation.findNavController(
                                requireActivity(), R.id.nav_host_fragment
                            )
                            navController.navigate(R.id.dashboardFragment, null, options)
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
        if (!mViewDataBinding.newPass.text.toString()
                .equals(mViewDataBinding.CnfrmPass.text.toString())
        ) {
            mViewDataBinding.root.snackbar("Password does not match")
        }

        changePassCall()
        return true
    }
}
