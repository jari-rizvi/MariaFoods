package com.teamx.mariaFoods.ui.fragments.placeorder

import android.os.Bundle
import android.view.View
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.navOptions
import com.teamx.mariaFoods.BR
import com.teamx.mariaFoods.R
import com.teamx.mariaFoods.baseclasses.BaseFragment
import com.teamx.mariaFoods.databinding.FragmentPlaceOrderBinding
import com.teamx.mariaFoods.ui.fragments.help.HelpViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PlaceOrderFragment : BaseFragment<FragmentPlaceOrderBinding, HelpViewModel>() {

    override val layoutId: Int
        get() = com.teamx.mariaFoods.R.layout.fragment_place_order
    override val viewModel: Class<HelpViewModel>
        get() = HelpViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel


    private lateinit var options: NavOptions

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewDataBinding.lifecycleOwner = viewLifecycleOwner

        options = navOptions {
            anim {
                enter = com.teamx.mariaFoods.R.anim.enter_from_left
                exit = com.teamx.mariaFoods.R.anim.exit_to_left
                popEnter = com.teamx.mariaFoods.R.anim.nav_default_pop_enter_anim
                popExit = com.teamx.mariaFoods.R.anim.nav_default_pop_exit_anim
            }
        }


        mViewDataBinding.btnShopping.setOnClickListener {
            navController = Navigation.findNavController(
                requireActivity(), R.id.nav_host_fragment
            )
            navController.navigate(R.id.dashboardFragment, null, options)

        }


    }


}


