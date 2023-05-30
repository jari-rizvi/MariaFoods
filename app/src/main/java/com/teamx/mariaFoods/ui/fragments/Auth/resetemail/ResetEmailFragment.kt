package com.teamx.mariaFoods.ui.fragments.Auth.resetemail

import android.os.Bundle
import android.view.View
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.navOptions
import com.teamx.mariaFoods.BR
import com.teamx.mariaFoods.R
import com.teamx.mariaFoods.baseclasses.BaseFragment
import com.teamx.mariaFoods.databinding.FragmentResetEmailBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ResetEmailFragment() : BaseFragment<FragmentResetEmailBinding, ResetEmailViewModel>() {

    override val layoutId: Int
        get() = R.layout.fragment_reset_email
    override val viewModel: Class<ResetEmailViewModel>
        get() = ResetEmailViewModel::class.java
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

       mViewDataBinding.btnVerify.setOnClickListener {
           navController =
               Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
           navController.navigate(R.id.changePasswordFragment, null, options)
       }


    }

}
