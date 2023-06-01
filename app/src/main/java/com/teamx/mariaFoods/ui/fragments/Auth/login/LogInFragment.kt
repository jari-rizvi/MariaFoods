package com.teamx.mariaFoods.ui.fragments.Auth.login

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.navOptions
import com.facebook.*
import com.facebook.CallbackManager.Factory.create
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.gson.JsonObject
import com.teamx.mariaFoods.BR
import com.teamx.mariaFoods.R
import com.teamx.mariaFoods.baseclasses.BaseFragment
import com.teamx.mariaFoods.data.remote.Resource
import com.teamx.mariaFoods.databinding.FragmentLoginBinding
import com.teamx.mariaFoods.utils.DialogHelperClass
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONException
import timber.log.Timber
import java.util.*

@AndroidEntryPoint
class LogInFragment : BaseFragment<FragmentLoginBinding, LoginViewModel>() {

    override val layoutId: Int
        get() = R.layout.fragment_login
    override val viewModel: Class<LoginViewModel>
        get() = LoginViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel


    private lateinit var options: NavOptions

    var mGoogleSignInClient: GoogleSignInClient? = null
    var callbackManager: CallbackManager? = null


    var RC_SIGN_IN = 1

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

        mViewDataBinding.btnPhone.setOnClickListener {
            navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
            navController.navigate(R.id.signupPhoneFragment, null, options)
        }

        mViewDataBinding.btnEmail.setOnClickListener {
            navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
            navController.navigate(R.id.signupFragment, null, options)
        }

        mViewDataBinding.btnBack.setOnClickListener {
            popUpStack()
        }

        mViewDataBinding.txtLoginFacebook.setOnClickListener {
            signIn()
        }

        mViewDataBinding.txtLoginGoogle.setOnClickListener {
            fb()

        }



        mViewModel.socialLoginResponse.observe(requireActivity()) {
            when (it.status) {
                Resource.Status.LOADING -> {
                    loadingDialog.show()
                }
                Resource.Status.SUCCESS -> {
                    loadingDialog.dismiss()

                    it.data?.let { data ->
                        lifecycleScope.launch(Dispatchers.IO) {
                            dataStoreProvider.saveUserToken(data.AccessToken)

                        }

                        navController =
                            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                        navController.navigate(R.id.dashboardFragment, null, options)

                    }


                }
                Resource.Status.ERROR -> {
                    loadingDialog.dismiss()
                    DialogHelperClass.errorDialog(requireContext(), it.message!!)
                }
            }
            if (isAdded) {
                mViewModel.socialLoginResponse.removeObservers(viewLifecycleOwner)
            }
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

        val account = GoogleSignIn.getLastSignedInAccount(requireContext())
//        updateUI(account)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.server_client_id)).requestEmail().build()

        mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso)


    }


    private fun signIn() {
        val signInIntent = mGoogleSignInClient!!.signInIntent

        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun fb() {
        callbackManager = create();

        val EMAIL = "email"

        var loginButton = mViewDataBinding.txtLoginGoogle


        listOf(EMAIL)

        LoginManager.getInstance()
            .registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onCancel() {
                }

                override fun onError(error: FacebookException) {
                }

                override fun onSuccess(result: LoginResult) {
                    Timber.tag(ContentValues.TAG).d("1stfbToken: ${result.accessToken}")


                    val idTokenFb = result.accessToken.token

                    val params = JsonObject()
                    try {
                        params.addProperty("token", idTokenFb)
                        params.addProperty("provider", "facebook")
                        params.addProperty("platform", "android")
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                    mViewModel.socialLogins(params)

                }

            })


        val accessToken = AccessToken.getCurrentAccessToken()
        val isLoggedIn = accessToken != null && !accessToken.isExpired

        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"))



        LoginManager.getInstance()
            .retrieveLoginStatus(requireContext(), object : LoginStatusCallback {
                override fun onCompleted(accessToken: AccessToken) {
                    // User was previously logged in, can log them in directly here.
                    // If this callback is called, a popup notification appears that says
                    // "Logged in as <User Name>"

                    Timber.tag(ContentValues.TAG).d("fbToken: ${accessToken.token}")


                    val idTokenFb = accessToken.token.toString()

                    val params = JsonObject()
                    try {
                        params.addProperty("token", idTokenFb)
                        params.addProperty("platform", "android")
                        params.addProperty("provider", "facebook")
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                    mViewModel.socialLogins(params)

                }

                override fun onFailure() {
                    // No access token could be retrieved for the user
                }

                override fun onError(exception: Exception) {
                    // An error occurred
                }
            })


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount = completedTask.getResult(ApiException::class.java)

            val idToken = account.idToken

            Log.d(ContentValues.TAG, "gmailtoken: $idToken")

            val params = JsonObject()
            try {
                params.addProperty("token", idToken)
                params.addProperty("provider", "google")
                params.addProperty("platform", "android")
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            mViewModel.socialLogins(params)

            Timber.tag("TAG").d("signInResult:failed code=")

            // Signed in successfully, show authenticated UI.
//            navController = Navigation.findNavController(
//                requireActivity(), R.id.nav_host_fragment
//            )
//            navController.navigate(R.id.userProfileFragment, null, options)

//            updateUI(account)
        } catch (e: ApiException) {
            e.printStackTrace()
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(ContentValues.TAG, "signInResult:failed code=" + e.statusCode)
//            updateUI(null)
        }
    }

}