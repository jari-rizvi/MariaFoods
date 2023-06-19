package com.teamx.mariaFoods.ui.fragments.profile

import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.navOptions
import com.squareup.picasso.Picasso
import com.teamx.mariaFoods.BR
import com.teamx.mariaFoods.R
import com.teamx.mariaFoods.baseclasses.BaseFragment
import com.teamx.mariaFoods.data.dataclasses.login.User
import com.teamx.mariaFoods.data.remote.Resource
import com.teamx.mariaFoods.databinding.FragmentProfileBinding
import com.teamx.mariaFoods.utils.DialogHelperClass
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding, EditProfileViewModel>() {

    override val layoutId: Int
        get() = R.layout.fragment_profile
    override val viewModel: Class<EditProfileViewModel>
        get() = EditProfileViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel
    var isProfileImg: Boolean = false


    private lateinit var options: NavOptions
//    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>


    private fun uploadWithRetrofit(file: File) {
//        val fileDir = requireContext().applicationContext.filesDir
//        val file = File(fileDir, "picture.png")
//        Timber.tag("TAG").d( "uploadWithRetrofit: $fileDir")
//        Timber.tag("TAG").d( "uploadWithRetrofit: $file")
//        val inputStream = requireContext().contentResolver.openInputStream(uri)
//        val outputStream = FileOutputStream(file)
//        inputStream!!.copyTo(outputStream)
        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())

        val filePart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "avatar", file.name, requestBody
        )
        Timber.tag("TAG").d("uploadWithRetrofit: $filePart")

        mViewModel.updateProfile(filePart)

    }

    var strImg = ""

    var user1: User? = null
    private fun checker() {

        mViewModel.updateProfileResponse.observe(viewLifecycleOwner) {
            when (it.status) {
                Resource.Status.LOADING -> {
                    Timber.tag("TAG").d("updateProfile:1 ")
                    loadingDialog.show()
                }
                Resource.Status.SUCCESS -> {
                    loadingDialog.dismiss()
                    it.data?.let { data ->
                        Log.e("checker", "strImg ${data.avatar}")
                        if (isProfileImg) {
                            this.strImg = data.avatar

                            if (user1 != null) {
                                it.data?.let { data ->
                                    lifecycleScope.launch(Dispatchers.IO) {


                                        user1!!.avatar = data.avatar


                                        Log.e("checker", "lifecycleScope ${user1!!.avatar}")
                                        Log.e("checker", "lifecycleScope ${user1!!.phone}")
                                        Log.e("checker", "lifecycleScope ${user1!!.first_name}")
                                        Log.e("checker", "lifecycleScope ${user1!!.last_name}")

                                        dataStoreProvider.saveUserDetails(
                                            user1!!
                                        )

                                    }


                                }

                            }


                        } else {

                        }

                    }
                }
                Resource.Status.ERROR -> {
                    loadingDialog.dismiss()
                    DialogHelperClass.errorDialog(requireContext(), it.message!!)
                    Timber.tag("TAG").d("updateProfile:3 ${it.message}")
                }
            }
//            if (isAdded) {
//            mViewModel.updateImgProfileResponse.removeObservers(viewLifecycleOwner)
//}
        }
    }

    private fun fetchImageFromGallery() {
        startForResult.launch("image/*")
        checker()
    }

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                val str = "${requireActivity().filesDir}/file.jpg"
                if (isProfileImg) {

                    val imageUri = uri

// Load the bitmap from the image URI
                    val bitmap = MediaStore.Images.Media.getBitmap(
                        requireActivity().contentResolver, imageUri
                    )

// Compress the bitmap to a JPEG format with 80% quality and save it to a file
                    val outputStream = FileOutputStream(str)
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)

                    outputStream.close()
//                    mViewDataBinding.imgCar.visibility = View.GONE
                    Picasso.get().load(File(str)).into(mViewDataBinding.profilePicture)
                } else {
                    val imageUri = uri

// Load the bitmap from the image URI
                    val bitmap = MediaStore.Images.Media.getBitmap(
                        requireActivity().contentResolver, imageUri
                    )

// Compress the bitmap to a JPEG format with 80% quality and save it to a file
                    val outputStream = FileOutputStream(str)
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)
                    outputStream.close()
                    Timber.tag("TAG").d("${bitmap.byteCount}: ")
                }
                uploadWithRetrofit(File(str))

            }

        }

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


        mViewDataBinding.profilePicture.setOnClickListener {
            isProfileImg = true
            fetchImageFromGallery()
        }

        mViewDataBinding.termsCondition.setOnClickListener {
            navController = Navigation.findNavController(
                requireActivity(), R.id.nav_host_fragment
            )
            navController.navigate(R.id.termsCondtionFragment, null, options)
        }

        mViewDataBinding.btnHelp.setOnClickListener {
            navController = Navigation.findNavController(
                requireActivity(), R.id.nav_host_fragment
            )
            navController.navigate(R.id.helpFragment, null, options)
        }
        mViewDataBinding.btnPayment.setOnClickListener {
            navController = Navigation.findNavController(
                requireActivity(), R.id.nav_host_fragment
            )
            navController.navigate(R.id.paymentFragment, null, options)
        }



        lifecycleScope.launch {
            dataStoreProvider.userFlow.collect { user ->

                user1 = user

                mViewDataBinding.btnEditProfile.text = user.first_name?.toString()
                mViewDataBinding.textView42.text = user.email?.toString()
                Picasso.get().load("https://dev.dogtvfoods.com/${user.avatar}")
                    .into(mViewDataBinding.profilePicture)
            }
        }

//        val accessToken = AccessToken.getCurrentAccessToken()
//        val isLoggedIn = accessToken != null && !accessToken.isExpired
//
//        if (isLoggedIn) {
//            val request = GraphRequest.newMeRequest(
//                accessToken
//            ) { jsonObject, response ->
//                if (response?.error != null) {
//                    // Handle error
//                } else {
//                    val email = jsonObject?.getString("email")
//                    val name = jsonObject?.getString("name")
//                    val profilePicUrl = jsonObject?.getJSONObject("picture")?.getJSONObject("data")
//                        ?.getString("url")
//
//                    // Use the retrieved data
//                    mViewDataBinding.btnEditProfile.text = name

//                    Picasso.get().load(profilePicUrl).into(mViewDataBinding.profilePicture)
//                    mViewDataBinding.textView42.text = email
//
//                }
//            }
//
//            val parameters = Bundle()
//            parameters.putString("fields", "email,name,picture.type(large)")
//            request.parameters = parameters
//            request.executeAsync()
//        }
//
//
//        val acct = GoogleSignIn.getLastSignedInAccount(requireContext())
//        if (acct != null) {
//            val personName = acct.displayName
//            val personEmail = acct.email
//            val personId = acct.id
//            val personPhoto = acct.photoUrl
//            val token = acct.idToken
//
//
//            mViewDataBinding.btnEditProfile.text = personName
//            mViewDataBinding.textView42.text = personEmail
//
//            Picasso.get().load(personPhoto).into(mViewDataBinding.profilePicture)
//
//            Timber.tag("TAG").d(personPhoto.toString())
//
//        }

        mViewDataBinding.btnAddress.setOnClickListener {

            navController = Navigation.findNavController(
                requireActivity(), R.id.nav_host_fragment
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
            navController = Navigation.findNavController(
                requireActivity(), R.id.nav_host_fragment
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
                                    navController = Navigation.findNavController(
                                        requireActivity(), R.id.nav_host_fragment
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