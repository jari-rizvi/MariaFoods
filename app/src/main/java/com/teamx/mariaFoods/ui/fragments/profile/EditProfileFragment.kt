package com.teamx.mariaFoods.ui.fragments.profile

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.navOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.JsonObject
import com.teamx.mariaFoods.BR
import com.teamx.mariaFoods.R
import com.teamx.mariaFoods.baseclasses.BaseFragment
import com.teamx.mariaFoods.data.remote.Resource
import com.teamx.mariaFoods.databinding.FragmentEditProfileBinding
import com.teamx.mariaFoods.ui.activity.mainActivity.MainActivity
import com.teamx.mariaFoods.utils.DialogHelperClass
import com.teamx.mariaFoods.utils.PrefHelper
import com.teamx.mariaFoods.utils.snackbar
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONException
import timber.log.Timber

@AndroidEntryPoint
class EditProfileFragment : BaseFragment<FragmentEditProfileBinding, EditProfileViewModel>() {

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


        mViewDataBinding.btnEditProfile.setOnClickListener {
            editProfile()
        }

        mViewDataBinding.bottomSheetLayout.btnChangePass.setOnClickListener {

            validate()

        }



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

        PrefHelper.getInstance(requireContext()).saveProfile(
            mViewDataBinding.fName.text.toString(),
            mViewDataBinding.lName.text.toString(),
            mViewDataBinding.email.text.toString(),
            strImg,
            mViewDataBinding.phone.text.toString()
        )

        PrefHelper.getInstance(requireContext()).firstname?.let { Timber.tag("TAG").d(it) }
        PrefHelper.getInstance(requireContext()).lastname?.let { Timber.tag("TAG").d(it) }
        PrefHelper.getInstance(requireContext()).email?.let { Timber.tag("TAG").d(it) }
        PrefHelper.getInstance(requireContext()).avatar?.let { Timber.tag("TAG").d(it) }
        PrefHelper.getInstance(requireContext()).number?.let { Timber.tag("TAG").d(it) }
    }

    private fun initialization() {
        phone = mViewDataBinding.phone.text.toString().trim()
        email = mViewDataBinding.email.text.toString().trim()
        lastName = mViewDataBinding.lName.text.toString().trim()
        FirstName = mViewDataBinding.fName.text.toString().trim()


    }

    fun editProfile() {

        initialization()

        if (!FirstName!!.isEmpty() || !lastName!!.isEmpty()) {

            val params = JsonObject()
            try {
                params.addProperty("first_name", FirstName)
                params.addProperty("last_name", lastName)
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


}