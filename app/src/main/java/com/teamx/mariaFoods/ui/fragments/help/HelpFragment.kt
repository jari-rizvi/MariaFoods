package com.teamx.mariaFoods.ui.fragments.help

import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.navigation.NavOptions
import androidx.navigation.navOptions
import com.teamx.mariaFoods.BR
import com.teamx.mariaFoods.baseclasses.BaseFragment
import com.teamx.mariaFoods.data.remote.Resource
import com.teamx.mariaFoods.databinding.FragmentHelpBinding
import com.teamx.mariaFoods.utils.DialogHelperClass
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HelpFragment : BaseFragment<FragmentHelpBinding, HelpViewModel>() {

    override val layoutId: Int
        get() = com.teamx.mariaFoods.R.layout.fragment_help
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


        mViewDataBinding.imageView11.setOnClickListener {
            popUpStack()
        }


        mViewModel.getHelp()
        mViewModel.helpResponse.observe(requireActivity()) {
            when (it.status) {
                Resource.Status.LOADING -> {
                    loadingDialog.show()
                }
                Resource.Status.SUCCESS -> {
                    loadingDialog.dismiss()

                    it.data?.let { data ->

                        val web_view: WebView = mViewDataBinding.webView
                        web_view.requestFocus()
                        web_view.settings.lightTouchEnabled = true
                        web_view.settings.javaScriptEnabled = true
                        web_view.settings.setGeolocationEnabled(true)

                        web_view.webChromeClient = WebChromeClient()
                        data.data?.content?.let { it1 ->
                            web_view.loadDataWithBaseURL(
                                "https://dev.dogtvfoods.com/api/v1",
                                it1,
                                "text/html",
                                "UTF-8",
                                null
                            )
                        }

                        web_view.isSoundEffectsEnabled = true


                    }


                }
                Resource.Status.ERROR -> {
                    loadingDialog.dismiss()
                    DialogHelperClass.errorDialog(requireContext(), it.message!!)
                }
            }
            if (isAdded) {
                mViewModel.helpResponse.removeObservers(viewLifecycleOwner)
            }
        }


    }


}


