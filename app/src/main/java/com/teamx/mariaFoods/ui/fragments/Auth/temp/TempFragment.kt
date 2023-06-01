package com.teamx.mariaFoods.ui.fragments.Auth.temp


import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.navOptions
import com.teamx.mariaFoods.BR
import com.teamx.mariaFoods.R
import com.teamx.mariaFoods.baseclasses.BaseFragment
import com.teamx.mariaFoods.constants.NetworkCallPoints.Companion.TOKENER
import com.teamx.mariaFoods.databinding.FragmentTempBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TempFragment : BaseFragment<FragmentTempBinding, TempViewModel>() {

    override val layoutId: Int
        get() = R.layout.fragment_temp
    override val viewModel: Class<TempViewModel>
        get() = TempViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel

    private lateinit var options: NavOptions


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        options = navOptions {
            anim {
                enter = R.anim.enter_from_left
                exit = R.anim.exit_to_left
                popEnter = R.anim.nav_default_pop_enter_anim
                popExit = R.anim.nav_default_pop_exit_anim
            }
        }

        Handler(Looper.getMainLooper()).postDelayed({
            if (isAdded) {
                var token: String? = null
                CoroutineScope(Dispatchers.Main).launch {

                    dataStoreProvider.token.collect {
                        Log.d("Databsae Token", "CoroutineScope ${it}")

                        Log.d("dataStoreProvider", "subscribeToNetworkLiveData: $it")

                        token = it

                        TOKENER = token.toString()

                        if (isAdded) {
                            if (token.isNullOrBlank()) {
                                Log.d("Databsae Token", "token ${token}")
                                navController = Navigation.findNavController(
                                    requireActivity(),
                                    R.id.nav_host_fragment
                                )
                                navController.navigate(R.id.logInFragment, null, options)

                            } else {
                                navController = Navigation.findNavController(
                                    requireActivity(),
                                    R.id.nav_host_fragment
                                )
                                navController.navigate(R.id.dashboardFragment, null, options)
                            }
                        }

                    }


                }


            }

        }, 2000)





    }

}
