package com.teamx.mariaFoods.ui.fragments.profile

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.navOptions
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.squareup.picasso.Picasso
import com.teamx.mariaFoods.BR
import com.teamx.mariaFoods.R
import com.teamx.mariaFoods.baseclasses.BaseFragment
import com.teamx.mariaFoods.data.remote.Resource
import com.teamx.mariaFoods.databinding.FragmentProfileBinding
import com.teamx.mariaFoods.ui.fragments.Auth.temp.TempViewModel
import com.teamx.mariaFoods.utils.DialogHelperClass
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class ProfileFragment :
    BaseFragment<FragmentProfileBinding, TempViewModel>() {

    override val layoutId: Int
        get() = R.layout.fragment_profile
    override val viewModel: Class<TempViewModel>
        get() = TempViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel


    private lateinit var options: NavOptions
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


        lifecycleScope.launch {
            dataStoreProvider.userFlow.collect { user ->

                mViewDataBinding.btnEditProfile.text = user.first_name.toString()
                mViewDataBinding.textView42.text = user.email.toString()
                Picasso.get().load(user.avatar.toString()).into(mViewDataBinding.profilePicture)
            }
        }



        val accessToken = AccessToken.getCurrentAccessToken()
        val isLoggedIn = accessToken != null && !accessToken.isExpired

        if (isLoggedIn) {
            val request = GraphRequest.newMeRequest(
                accessToken
            ) { jsonObject, response ->
                if (response?.error != null) {
                    // Handle error
                } else {
                    val email = jsonObject?.getString("email")
                    val name = jsonObject?.getString("name")
                    val profilePicUrl = jsonObject?.getJSONObject("picture")
                        ?.getJSONObject("data")
                        ?.getString("url")

                    // Use the retrieved data
                    mViewDataBinding.btnEditProfile.text = name
                    Picasso.get().load(profilePicUrl).into(mViewDataBinding.profilePicture)
                    mViewDataBinding.textView42.text = email

                }
            }

            val parameters = Bundle()
            parameters.putString("fields", "email,name,picture.type(large)")
            request.parameters = parameters
            request.executeAsync()
        }



        val acct = GoogleSignIn.getLastSignedInAccount(requireContext())
        if (acct != null) {
            val personName = acct.displayName
            val personEmail = acct.email
            val personId = acct.id
            val personPhoto = acct.photoUrl
            val token = acct.idToken


            mViewDataBinding.btnEditProfile.text = personName
            mViewDataBinding.textView42.text = personEmail

            Picasso.get().load(personPhoto).into(mViewDataBinding.profilePicture)

            Timber.tag("TAG").d( personPhoto.toString())

        }

        mViewDataBinding.btnAddress.setOnClickListener {

            navController =
                Navigation.findNavController(
                    requireActivity(),
                    R.id.nav_host_fragment
                )
            navController.navigate(R.id.addressFragment, null, options)

//            bottomSheetBehavior =
//                BottomSheetBehavior.from(mViewDataBinding.bottomSheetLayout.bottomSheet)
//
//            bottomSheetBehavior.addBottomSheetCallback(object :
//                BottomSheetBehavior.BottomSheetCallback() {
//                override fun onSlide(bottomSheet: View, slideOffset: Float) {
//                }
//
//                override fun onStateChanged(bottomSheet: View, newState: Int) {
//                    when (newState) {
//                        BottomSheetBehavior.STATE_EXPANDED -> MainActivity.bottomNav?.visibility =
//                            View.GONE
//                        BottomSheetBehavior.STATE_COLLAPSED -> MainActivity.bottomNav?.visibility =
//                            View.VISIBLE
//                        else -> "Persistent Bottom Sheet"
//                    }
//                }
//            })
//
//            val state =
//                if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED)
//                    BottomSheetBehavior.STATE_COLLAPSED
//                else
//                    BottomSheetBehavior.STATE_EXPANDED
//            bottomSheetBehavior.state = state
        }

        mViewDataBinding.btnEditProfile.setOnClickListener {
            navController =
                Navigation.findNavController(
                    requireActivity(),
                    R.id.nav_host_fragment
                )
            navController.navigate(R.id.editProfileFragment, null, options)
        }


        mViewDataBinding.btnBack.setOnClickListener {
            popUpStack()
        }

        mViewDataBinding.btnLogput.setOnClickListener {

            mViewModel.logout()

            if (!mViewModel.logoutResponse.hasActiveObservers()) {
                mViewModel.logoutResponse.observe(requireActivity()) {
                    when (it.status) {
                        Resource.Status.LOADING -> {
                            loadingDialog.show()
                        }
                        Resource.Status.SUCCESS -> {
                            loadingDialog.dismiss()
                            it.data?.let { data ->
                                if (data.Flag == 1) {

                                    lifecycleScope.launch(Dispatchers.IO) {
                                        dataStoreProvider.removeAll()

                                    }
                                    navController =
                                        Navigation.findNavController(
                                            requireActivity(),
                                            R.id.nav_host_fragment
                                        )
                                    navController.navigate(R.id.tempFragment, null, options)
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
                        mViewModel.logoutResponse.removeObservers(viewLifecycleOwner)
                    }
                }
            }
        }


    }


}