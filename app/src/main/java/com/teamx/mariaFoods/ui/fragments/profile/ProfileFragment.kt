package com.teamx.mariaFoods.ui.fragments.profile

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.navOptions
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