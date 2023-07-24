package com.teamx.mariaFoods.ui.fragments.profile

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.navOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.JsonObject
import com.hbb20.CountryCodePicker
import com.teamx.mariaFoods.BR
import com.teamx.mariaFoods.R
import com.teamx.mariaFoods.baseclasses.BaseFragment
import com.teamx.mariaFoods.data.remote.Resource
import com.teamx.mariaFoods.databinding.FragmentEditProfileBinding
import com.teamx.mariaFoods.ui.activity.mainActivity.MainActivity
import com.teamx.mariaFoods.utils.DialogHelperClass
import com.teamx.mariaFoods.utils.snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONException


@AndroidEntryPoint
class EditProfileFragment : BaseFragment<FragmentEditProfileBinding, EditProfileViewModel>(),
    CountryCodePicker.OnCountryChangeListener {

    override val layoutId: Int
        get() = R.layout.fragment_edit_profile
    override val viewModel: Class<EditProfileViewModel>
        get() = EditProfileViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel


    private lateinit var options: NavOptions
    private lateinit var phone: String
    private lateinit var lastName: String
    private lateinit var email: String
    private lateinit var FirstName: String

    var strImg = ""

    private var ccp: CountryCodePicker? = null
    private var countryCode: String? = null
    private var countryName: String? = null

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    @SuppressLint("ResourceAsColor")
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



//        val aa: String = mViewDataBinding.email.text.toString()
//        val bb: String = mViewDataBinding.phone.text.toString()
//
//        if(aa.isEmpty() || bb.isEmpty()){
//            mViewDataBinding.email.isEnabled = true
//            mViewDataBinding.phone.isEnabled = true
//        }
//        else{
//            mViewDataBinding.email.isEnabled = false
//            mViewDataBinding.phone.isEnabled = false
//        }


        lifecycleScope.launch {
            dataStoreProvider.userFlow.collect { user ->
//                mViewDataBinding.email.setText(user.email.toString())
                mViewDataBinding.fName.setText(user.first_name.toString())
                mViewDataBinding.lName.setText(user.last_name.toString())
                mViewDataBinding.email.setText(user.email.toString())
                mViewDataBinding.phone.setText(user.phone.toString())
//                mViewDataBinding.phone.setText(user.phone.toString())

                mViewDataBinding.email.isEnabled = user.email.isNullOrEmpty()

                mViewDataBinding.phone.isEnabled = user.phone.isNullOrEmpty()


            }




//            if (mViewDataBinding.email.text.isNotEmpty()) {
//                mViewDataBinding.email.isClickable = false
//                mViewDataBinding.email.isEnabled = false
//                mViewDataBinding.email.isFocusable = false
//                mViewDataBinding.email.isFocusableInTouchMode = false
//
//            }
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

//        mViewDataBinding.bottomSheetLayout1.countryCode.registerCarrierNumberEditText(
//            mViewDataBinding.bottomSheetLayout1.etPhone
//        )


        mViewDataBinding.btnEditProfile.setOnClickListener {
            editProfile()
        }

        mViewDataBinding.bottomSheetLayout.btnChangePass.setOnClickListener {

            validate()

        }

//        mViewDataBinding.bottomSheetLayout1.btnNext.setOnClickListener {
//            changePhone()
//            val state =
//                if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) BottomSheetBehavior.STATE_COLLAPSED
//                else BottomSheetBehavior.STATE_EXPANDED
//            bottomSheetBehavior.state = state
//        }
//        mViewDataBinding.bottomSheetLayout2.btnVerify.setOnClickListener {
//            changePhoneVerify()
//        }
//
//
//        mViewDataBinding.btnChangePhone.setOnClickListener {
//            bottomSheetBehavior =
//                BottomSheetBehavior.from(mViewDataBinding.bottomSheetLayout1.bottomSheetChangeMobile)
//
//            bottomSheetBehavior.addBottomSheetCallback(object :
//                BottomSheetBehavior.BottomSheetCallback() {
//                override fun onSlide(bottomSheet: View, slideOffset: Float) {
//
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
//                if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) BottomSheetBehavior.STATE_COLLAPSED
//                else BottomSheetBehavior.STATE_EXPANDED
//            bottomSheetBehavior.state = state
//        }



        mViewDataBinding.btnChangePass.setOnClickListener {
            bottomSheetBehavior =
                BottomSheetBehavior.from(mViewDataBinding.bottomSheetLayout.bottomSheetChangePass)

            bottomSheetBehavior.addBottomSheetCallback(object :
                BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(bottomSheet: View, slideOffset: Float) {

                }

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_EXPANDED -> MainActivity.bottomNav?.visibility =
                            View.GONE
                        BottomSheetBehavior.STATE_COLLAPSED -> MainActivity.bottomNav?.visibility =
                            View.VISIBLE
                        else -> "Persistent Bottom Sheet"
                    }
                }
            })

            val state =
                if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) BottomSheetBehavior.STATE_COLLAPSED
                else BottomSheetBehavior.STATE_EXPANDED
            bottomSheetBehavior.state = state
        }

        mViewDataBinding.btnBack.setOnClickListener {
            popUpStack()
        }



    }

    private fun initialization() {
        email = mViewDataBinding.email.text.toString().trim()
        lastName = mViewDataBinding.lName.text.toString().trim()
        FirstName = mViewDataBinding.fName.text.toString().trim()
        phone = mViewDataBinding.phone.text.toString().trim()
//        ccp = mViewDataBinding.bottomSheetLayout1.countryCode
//        ccp!!.setOnCountryChangeListener(this)

    }

    fun editProfile() {

        initialization()

        if (!FirstName!!.isEmpty() || !lastName!!.isEmpty()) {

            val params = JsonObject()
            try {
                params.addProperty("first_name", FirstName)
                params.addProperty("last_name", lastName)
                params.addProperty("email", email)
                params.addProperty("phone", phone)
            } catch (e: JSONException) {
                e.printStackTrace()
            }


            mViewModel.editProfiles(params)
            mViewModel.editProfileResponse.observe(requireActivity()) {
                when (it.status) {
                    Resource.Status.LOADING -> {
                        loadingDialog.show()
                    }
                    Resource.Status.SUCCESS -> {
                        loadingDialog.dismiss()

                        it.data?.let { data ->
                            lifecycleScope.launch(Dispatchers.IO) {

                                dataStoreProvider.saveUserDetails(
                                    data.User!!
                                )

                            }

                            mViewDataBinding.root.snackbar("Profile Has Been Updated")


                        }


                    }
                    Resource.Status.ERROR -> {
                        loadingDialog.dismiss()
                        DialogHelperClass.errorDialog(requireContext(), it.message!!)
                    }
                }
                if (isAdded) {
                    mViewModel.editProfileResponse.removeObservers(viewLifecycleOwner)
                }
            }

        }
    }


    fun changePass() {

        val params = JsonObject()
        try {
            params.addProperty(
                "old_password", mViewDataBinding.bottomSheetLayout.currentPass.toString()
            )
            params.addProperty(
                "new_password", mViewDataBinding.bottomSheetLayout.newPass.toString()
            )
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        mViewModel.changePassword(params)

        if (!mViewModel.changePasswordResponse.hasActiveObservers()) {
            mViewModel.changePasswordResponse.observe(requireActivity(), Observer {
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

//    fun changePhone() {
//
//        initialization()
//        val params = JsonObject()
//        try {
//            params.addProperty(
//                "phone", mViewDataBinding.bottomSheetLayout1.countryCode.fullNumberWithPlus
//            )
//
//        } catch (e: JSONException) {
//            e.printStackTrace()
//        }
//
//        mViewModel.changePhone(params)
//
//        if (!mViewModel.changePhoneResponse.hasActiveObservers()) {
//            mViewModel.changePhoneResponse.observe(requireActivity(), Observer {
//                when (it.status) {
//                    Resource.Status.LOADING -> {
//                        loadingDialog.show()
//                    }
//                    Resource.Status.SUCCESS -> {
//                        loadingDialog.dismiss()
//                        it.data?.let { data ->
//
//                            bottomSheetBehavior =
//                                BottomSheetBehavior.from(mViewDataBinding.bottomSheetLayout2.bottomSheetOtop)
//
//                            bottomSheetBehavior.addBottomSheetCallback(object :
//                                BottomSheetBehavior.BottomSheetCallback() {
//                                override fun onSlide(bottomSheet: View, slideOffset: Float) {
//
//                                }
//
//                                override fun onStateChanged(bottomSheet: View, newState: Int) {
//                                    when (newState) {
//                                        BottomSheetBehavior.STATE_EXPANDED -> MainActivity.bottomNav?.visibility =
//                                            View.GONE
//                                        BottomSheetBehavior.STATE_COLLAPSED -> MainActivity.bottomNav?.visibility =
//                                            View.VISIBLE
//                                        else -> "Persistent Bottom Sheet"
//                                    }
//                                }
//                            })
//
//                            val state =
//                                if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) BottomSheetBehavior.STATE_COLLAPSED
//                                else BottomSheetBehavior.STATE_EXPANDED
//                            bottomSheetBehavior.state = state
//
//                        }
//                    }
//                    Resource.Status.ERROR -> {
//                        loadingDialog.dismiss()
//                        DialogHelperClass.errorDialog(requireContext(), it.message!!)
//                    }
//                }
//            })
//        }
//    }
//
//    fun changePhoneVerify() {
//
//        val code = mViewDataBinding.bottomSheetLayout2.pinView.text.toString()
//
//        initialization()
//        val params = JsonObject()
//        try {
//            params.addProperty(
//                "phone", mViewDataBinding.bottomSheetLayout1.countryCode.fullNumberWithPlus
//            )
//            params.addProperty("through", "settings")
//            params.addProperty("otp", code)
//
//        } catch (e: JSONException) {
//            e.printStackTrace()
//        }
//
//        mViewModel.changePhoneVerify(params)
//
//        if (!mViewModel.changePhoneVerifyResponse.hasActiveObservers()) {
//            mViewModel.changePhoneVerifyResponse.observe(requireActivity(), Observer {
//                when (it.status) {
//                    Resource.Status.LOADING -> {
//                        loadingDialog.show()
//                    }
//                    Resource.Status.SUCCESS -> {
//                        loadingDialog.dismiss()
//                        it.data?.let { data ->
//                            val state =
//                                if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) BottomSheetBehavior.STATE_COLLAPSED
//                                else BottomSheetBehavior.STATE_EXPANDED
//                            bottomSheetBehavior.state = state
//
//                        }
//                    }
//                    Resource.Status.ERROR -> {
//                        loadingDialog.dismiss()
//                        DialogHelperClass.errorDialog(requireContext(), it.message!!)
//                    }
//                }
//            })
//        }
//    }

    private fun validate(): Boolean {
        if (mViewDataBinding.bottomSheetLayout.currentPass.text.toString().trim().isEmpty()) {
            mViewDataBinding.root.snackbar(getString(R.string.enter_Password))
            return false
        }
        if (mViewDataBinding.bottomSheetLayout.currentPass.text.toString().trim().length < 8) {
            mViewDataBinding.root.snackbar(getString(R.string.password_8_character))
            return false
        }
        if (mViewDataBinding.bottomSheetLayout.newPass.text.toString().trim().isEmpty()) {
            mViewDataBinding.root.snackbar(getString(R.string.enter_Password))
            return false
        }
        if (mViewDataBinding.bottomSheetLayout.newPass.text.toString().trim().length < 7) {
            mViewDataBinding.root.snackbar(getString(R.string.password_8_character))
            return false
        }
        if (!mViewDataBinding.bottomSheetLayout.newPass.text.toString()
                .equals(mViewDataBinding.bottomSheetLayout.cnfrmPass.text.toString())
        ) {
            mViewDataBinding.root.snackbar("Password does not match")
        }

        changePass()
        return true
    }


    override fun onCountrySelected() {
        countryCode = ccp!!.selectedCountryCode
        countryName = ccp!!.selectedCountryName

    }
}