package com.teamx.mariaFoods.ui.fragments.payment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.NavOptions
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.teamx.mariaFoods.BR
import com.teamx.mariaFoods.R
import com.teamx.mariaFoods.baseclasses.BaseFragment
import com.teamx.mariaFoods.data.remote.Resource
import com.teamx.mariaFoods.databinding.FragmentPaymentBinding
import com.teamx.mariaFoods.utils.DialogHelperClass
import com.teamx.mariaFoods.utils.snackbar
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONException

@AndroidEntryPoint
class PaymentFragment : BaseFragment<FragmentPaymentBinding, PaymentViewModel>(),
    DialogHelperClass.Companion.DialogCallBack,
    OnStripeCardListener {

    override val layoutId: Int
        get() = R.layout.fragment_payment
    override val viewModel: Class<PaymentViewModel>
        get() = PaymentViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel


    private lateinit var options: NavOptions
    lateinit var stripecardsAdapter: PaymentAdapter
    lateinit var stripecardsArrayList: ArrayList<com.teamx.mariaFoods.data.dataclasses.getStripecards.Data>

    var itemid = ""

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

        mViewDataBinding.imageView11.setOnClickListener {
            popUpStack()
        }

        mViewModel.getStripeCardss()

        if (!mViewModel.getStripeCardsResponse.hasActiveObservers()) {
            mViewModel.getStripeCardsResponse.observe(requireActivity()) {
                when (it.status) {
                    Resource.Status.LOADING -> {
                        loadingDialog.show()
                    }
                    Resource.Status.SUCCESS -> {
                        loadingDialog.dismiss()
                        it.data?.let { data ->

                            stripecardsArrayList.addAll(data.data)
                            stripecardsAdapter.notifyDataSetChanged()

                        }
                    }
                    Resource.Status.ERROR -> {
                        loadingDialog.dismiss()
                        DialogHelperClass.errorDialog(requireContext(), it.message!!)
                    }
                }
                if (isAdded) {
                    mViewModel.getStripeCardsResponse.removeObservers(viewLifecycleOwner)
                }
            }
        }



        StripeRecyclerview()
    }

    private fun StripeRecyclerview() {
        stripecardsArrayList = ArrayList()

        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        mViewDataBinding.stripecardsRecycler.layoutManager = linearLayoutManager

        stripecardsAdapter = PaymentAdapter(stripecardsArrayList, this)
        mViewDataBinding.stripecardsRecycler.adapter = stripecardsAdapter

    }

    override fun onDeleteClickListener(position: Int) {
        val cardid = stripecardsArrayList[position]
        mViewModel.deleteStripeCard(cardid.id)

        mViewModel.deletestripecard.observe(requireActivity()) {
            when (it.status) {
                Resource.Status.LOADING -> {
                    loadingDialog.show()
                }
                Resource.Status.SUCCESS -> {
                    loadingDialog.dismiss()
                    it.data?.let { data ->
                        stripecardsArrayList.clear()
                        mViewModel.getStripeCardss()

                        stripecardsAdapter.notifyDataSetChanged()
                        mViewDataBinding.root.snackbar(data.Message)

                    }
                }
                Resource.Status.ERROR -> {
                    loadingDialog.dismiss()
                    DialogHelperClass.errorDialog(requireContext(), it.message!!)
                }
            }

        }
    }


    override fun onItemClickListener(position: Int, id: String) {
        itemid = id
        DialogHelperClass.defaultCardDialog(
            requireContext(), this, true
        )


    }

    override fun onCnfrmClicked() {


        val params = JsonObject()
        try {
            params.addProperty("payment_method_id", itemid)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        Log.e("UserData", params.toString())

        mViewModel.setDefaultCard(params)

        if (!mViewModel.setDefaultCard.hasActiveObservers()) {
            mViewModel.setDefaultCard.observe(requireActivity()) {
                when (it.status) {
                    Resource.Status.LOADING -> {
                        loadingDialog.show()
                    }
                    Resource.Status.SUCCESS -> {
                        loadingDialog.dismiss()
                        it.data?.let { data ->
                            if (data.Flag == 1) {
                                mViewDataBinding.root.snackbar(data.Message)

//                                navController = Navigation.findNavController(
//                                    requireActivity(), R.id.nav_host_fragment
//                                )
//                                navController.navigate(R.id.checkoutFragment, null, options)


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
                    mViewModel.setDefaultCard.removeObservers(viewLifecycleOwner)
                }
            }
        }

    }

    override fun onCnclClicked() {

    }


}