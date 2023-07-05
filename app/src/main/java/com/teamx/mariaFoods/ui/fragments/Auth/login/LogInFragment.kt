package com.teamx.mariaFoods.ui.fragments.Auth.login

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
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
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.messaging.FirebaseMessaging
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

@AndroidEntryPoint
class LogInFragment : BaseFragment<FragmentLoginBinding, LoginViewModel>() {

    override val layoutId: Int
        get() = R.layout.fragment_login
    override val viewModel: Class<LoginViewModel>
        get() = LoginViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel


    private lateinit var options: NavOptions

    //    var mGoogleSignInClient: GoogleSignInClient? = null/
    var callbackManager: CallbackManager? = null
    private lateinit var fcmToken: String

    var RC_SIGN_IN = 1

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

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
        auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.server_client_id)).requestEmail().build()

//        googleSignInClient = GoogleSignIn.getClient(requireActivity() , gso)
        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)



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
            signInGoogle()
        }

        mViewDataBinding.txtLoginGoogle.setOnClickListener {
            fb()
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


        mViewModel.socialLoginResponse.observe(requireActivity()) {
            when (it.status) {
                Resource.Status.LOADING -> {
                    loadingDialog.show()
                }
                Resource.Status.SUCCESS -> {
                    loadingDialog.dismiss()

                    it.data?.let { data ->
                        lifecycleScope.launch(Dispatchers.IO) {
                            dataStoreProvider.saveUserToken(data.AccessToken!!)
                            dataStoreProvider.saveUserDetails(
                                data.User
                            )
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


//        val account = GoogleSignIn.getLastSignedInAccount(requireContext())
////        updateUI(account)
//
//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(getString(R.string.server_client_id)).requestEmail().build()
//
//        mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso)


    }

    @SuppressLint("LongLogTag")
    fun getUserProfile(token: AccessToken?, userId: String??) {
        val parameters = Bundle()
        parameters.putString(
            "fields", "id, first_name, middle_name, last_name, name, picture, email"
        )
        GraphRequest(
            token,
            "/$userId/",
            parameters,
            HttpMethod.GET,
            GraphRequest.Callback { response ->
                val jsonObject = response.jsonObject

                // Facebook Access Token
                // You can see Access Token only in Debug mode.
                // You can't see it in Logcat using Log.d, Facebook did that to avoid leaking user's access token.
                if (BuildConfig.DEBUG) {
                    FacebookSdk.setIsDebugEnabled(true)
                    FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS)
                }

                // Facebook Id
                if (jsonObject?.has("id") == true) {
                    val facebookId = jsonObject.getString("id")
                    Log.i("Facebook Id: ", facebookId.toString())
                } else {
                    Log.i("Facebook Id: ", "Not exists")
                }


                // Facebook First Name
                if (jsonObject?.has("first_name") == true) {
                    val facebookFirstName = jsonObject.getString("first_name")
                    Log.i("Facebook First Name: ", facebookFirstName)
                } else {
                    Log.i("Facebook First Name: ", "Not exists")
                }


                // Facebook Middle Name
                if (jsonObject?.has("middle_name") == true) {
                    val facebookMiddleName = jsonObject.getString("middle_name")
                    Log.i("Facebook Middle Name: ", facebookMiddleName)
                } else {
                    Log.i("Facebook Middle Name: ", "Not exists")
                }


                // Facebook Last Name
                if (jsonObject?.has("last_name") == true) {
                    val facebookLastName = jsonObject.getString("last_name")
                    Log.i("Facebook Last Name: ", facebookLastName)
                } else {
                    Log.i("Facebook Last Name: ", "Not exists")
                }


                // Facebook Name
                if (jsonObject?.has("name") == true) {
                    val facebookName = jsonObject.getString("name")
                    Log.i("Facebook Name: ", facebookName)
                } else {
                    Log.i("Facebook Name: ", "Not exists")
                }


                // Facebook Profile Pic URL
                if (jsonObject?.has("picture") == true) {
                    val facebookPictureObject = jsonObject.getJSONObject("picture")
                    if (facebookPictureObject.has("data")) {
                        val facebookDataObject = facebookPictureObject.getJSONObject("data")
                        if (facebookDataObject.has("url")) {
                            val facebookProfilePicURL = facebookDataObject.getString("url")
                            Log.i("Facebook Profile Pic URL: ", facebookProfilePicURL)
                        }
                    }
                } else {
                    Log.i("Facebook Profile Pic URL: ", "Not exists")
                }

                // Facebook Email
                if (jsonObject?.has("email") == true) {
                    val facebookEmail = jsonObject.getString("email")
                    Log.i("Facebook Email: ", facebookEmail)
                } else {
                    Log.i("Facebook Email: ", "Not exists")
                }
            }).executeAsync()
    }

//    private fun signIn() {
//        val signInIntent = mGoogleSignInClient!!.signInIntent
//
//        startActivityForResult(signInIntent, RC_SIGN_IN)
//    }

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

                    getUserProfile(result?.accessToken, result?.accessToken?.userId)

                    val idTokenFb = result.accessToken.token

                    val params = JsonObject()
                    try {
                        params.addProperty("token", idTokenFb)
                        params.addProperty("provider", "facebook")
                        params.addProperty("platform", "android")
                        params.addProperty("fcm_token", fcmToken)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                    mViewModel.socialLogins(params)

                }

            })


        val accessToken = AccessToken.getCurrentAccessToken()
        val isLoggedIn = accessToken != null && !accessToken.isExpired

        LoginManager.getInstance().logInWithReadPermissions(this, listOf("public_profile", "email"))



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
                        params.addProperty("fcm_token", fcmToken)
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


        private fun signInGoogle() {
            val signInIntent = googleSignInClient.signInIntent
            launcher.launch(signInIntent)
        }

        private val launcher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {

                    val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    handleResults(task)
                }
            }

        private fun handleResults(task: Task<GoogleSignInAccount>) {
            if (task.isSuccessful) {
                val account: GoogleSignInAccount? = task.result
                if (account != null) {
                    updateUI(account)
                }
            } else {
                Toast.makeText(requireContext(), task.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }

        private fun updateUI(account: GoogleSignInAccount) {
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            auth.signInWithCredential(credential).addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d(ContentValues.TAG, "gmailtoken: ${account.idToken}")

                    val params = JsonObject()
                    try {
                        params.addProperty("token", account.idToken)
                        params.addProperty("provider", "google")
                        params.addProperty("platform", "android")
                        params.addProperty("fcm_token", fcmToken)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                    mViewModel.socialLogins(params)

                } else {
                    Toast.makeText(requireContext(), it.exception.toString(), Toast.LENGTH_SHORT).show()
                    Log.d(ContentValues.TAG, "gmailtoken: ${it.exception.toString()}")


                }
            }
        }


//    override fun onActivityResult(requestCode: Int?, resultCode: Int?, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
//        if (requestCode == RC_SIGN_IN) {
//            // The Task returned from this call is always completed, no need to attach
//            // a listener.
//            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
//            handleSignInResult(task)
//        }
//    }
//
//    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
//        try {
//            val account: GoogleSignInAccount = completedTask.getResult(ApiException::class.java)
//
//            val idToken = account.idToken
//
//            Log.d(ContentValues.TAG, "gmailtoken: $idToken")
//
//            val params = JsonObject()
//            try {
//                params.addProperty("token", idToken)
//                params.addProperty("provider", "google")
//                params.addProperty("platform", "android")
//                params.addProperty("fcm_token", fcmToken)
//            } catch (e: JSONException) {
//                e.printStackTrace()
//            }
//
//            mViewModel.socialLogins(params)
//
//            Timber.tag("TAG").d("signInResult:failed code=")
//
//
////            updateUI(account)
//        } catch (e: ApiException) {
//            e.printStackTrace()
//            // The ApiException status code indicates the detailed failure reason.
//            // Please refer to the GoogleSignInStatusCodes class reference for more information.
//            Log.w(ContentValues.TAG, "signInResult:failed code=" + e.statusCode)
////            updateUI(null)
//        }
//    }

}