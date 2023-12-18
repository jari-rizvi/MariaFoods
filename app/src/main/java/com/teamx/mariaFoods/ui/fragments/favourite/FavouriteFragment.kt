package com.teamx.mariaFoods.ui.fragments.favourite

import android.os.Bundle
import android.view.View
import androidx.navigation.NavOptions
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.teamx.mariaFoods.BR
import com.teamx.mariaFoods.R
import com.teamx.mariaFoods.baseclasses.BaseFragment
import com.teamx.mariaFoods.data.dataclasses.wishList.Item
import com.teamx.mariaFoods.data.remote.Resource
import com.teamx.mariaFoods.databinding.FragmentFavouriteBinding
import com.teamx.mariaFoods.utils.DialogHelperClass
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavouriteFragment : BaseFragment<FragmentFavouriteBinding, FavouriteViewModel>() {

    override val layoutId: Int
        get() = R.layout.fragment_favourite
    override val viewModel: Class<FavouriteViewModel>
        get() = FavouriteViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel


    private lateinit var options: NavOptions

    lateinit var favouriteAdapter: FavouriteAdapter
    lateinit var favouriteArrayList: ArrayList<Item>
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


        mViewDataBinding.btnBack.setOnClickListener {
            popUpStack()
        }
        try {
            mViewModel.getWishList()

        } catch (e: Exception) {

        }

        if (!mViewModel.getWishlistResponse.hasActiveObservers()) {
            mViewModel.getWishlistResponse.observe(requireActivity()) {
                when (it.status) {
                    Resource.Status.LOADING -> {
                        loadingDialog.show()
                    }

                    Resource.Status.SUCCESS -> {
                        loadingDialog.dismiss()
                        it.data?.let { data ->
                            data.data.forEach {
                                favouriteArrayList.add(it.item)
                            }

                            favouriteAdapter.notifyDataSetChanged()


                        }
                    }

                    Resource.Status.ERROR -> {
                        loadingDialog.dismiss()
                        DialogHelperClass.errorDialog(
                            requireContext(),
                            it.message!!
                        )
                    }
                }
                if (isAdded) {
                    mViewModel.getWishlistResponse.removeObservers(
                        viewLifecycleOwner
                    )
                }
            }
        }


        favRecyclerview()
    }

    private fun favRecyclerview() {
        favouriteArrayList = ArrayList()

        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        mViewDataBinding.favRecycler.layoutManager = linearLayoutManager

        favouriteAdapter = FavouriteAdapter(favouriteArrayList)
        mViewDataBinding.favRecycler.adapter = favouriteAdapter

    }


}