package com.teamx.mariaFoods.ui.activity.mainActivity

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.teamx.mariaFoods.BR
import com.teamx.mariaFoods.MainApplication
import com.teamx.mariaFoods.R
import com.teamx.mariaFoods.baseclasses.BaseActivity
import com.teamx.mariaFoods.databinding.ActivityMainBinding
import com.teamx.mariaFoods.facebooklogin.FacebookResponse
import com.teamx.mariaFoods.facebooklogin.FacebookUser
import com.teamx.mariaFoods.utils.FragHelper
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
open class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(), FacebookResponse {


    override val viewModel: Class<MainViewModel>
        get() = MainViewModel::class.java

    override val layoutId: Int
        get() = R.layout.activity_main

    override val bindingVariable: Int
        get() = BR.viewModel


    private var navController: NavController? = null

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Timber.tag("321321").d("onRestoreInstanceState: ")
    }

    override fun onRestoreInstanceState(
        savedInstanceState: Bundle?, persistentState: PersistableBundle?
    ) {
        super.onRestoreInstanceState(savedInstanceState, persistentState)
        Timber.tag("321321").d("onRestoreInstanceState: ")
    }

    override fun onStateNotSaved() {
        super.onStateNotSaved()
        Log.d("321321", "onStateNotSaved: ")
    }


    lateinit var progress_bar: ProgressBar


    override fun onPause() {
        super.onPause()
        val navState = navController!!.saveState()!!
        mViewModel.bundleB.postValue(navState)
        Log.d("321321", "onPause:$navState ")
    }

    override fun onResume() {
        super.onResume()
        val navState = mViewModel.bundleB.value
        navState!!.let {
            navController!!.restoreState(it)
        }
        Log.d("321321", "onResume:$navState ")
    }

    override fun onStop() {
        super.onStop()
        Log.d("321321", "onStop: ")
    }

    private val LOCATION_PERMISSION_REQUEST_CODE = 100


    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String?>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            // Check if the permission has been granted
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with your app logic
                // ...
            } else {
                // Permission denied, handle accordingly (e.g., show an explanation or disable location-related functionality)
                // ...
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialising()

        stateHelper = FragHelper(supportFragmentManager)

        if (savedInstanceState == null) {
            idN = R.id.tempFragment
        } else {
            val helperState = savedInstanceState.getBundle(STATE_HELPER)
            stateHelper.restoreHelperState(helperState!!)
        }


        if (ContextCompat.checkSelfPermission(
                this, android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is already granted, proceed with your app logic
            // ...
        } else {
            // Request location permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            );
        }


        Log.d("321321", "onCreate: ")

//        bottomNav = findViewById(R.id.bottomnavigationbar)


        bottomNav = findViewById(R.id.bottomnavigationbar)
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)


        val navState = navController!!.saveState()!!

        mViewModel.bundleB.postValue(navState)


        setupBottomNavMenu(navController!!)

        navController!!.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {

                R.id.dashboardFragment -> {
                    bottomNav?.visibility = View.VISIBLE
                }
                R.id.checkoutFragment -> {
                    bottomNav?.visibility = View.VISIBLE
                }
                R.id.orderHistoryFragment -> {
                    bottomNav?.visibility = View.VISIBLE
                }
                R.id.profileFragment -> {
                    bottomNav?.visibility = View.VISIBLE
                }
                else -> {
                    bottomNav?.visibility = View.GONE
                }
            }
            setupBottomNavMenu(navController!!)
        }


    }

    private fun initialising() {
        progress_bar = findViewById(R.id.progress_bar)
    }

    open fun showProgressBar() {
        progress_bar.visibility = View.VISIBLE
    }


    open fun hideProgressBar() {
        progress_bar.visibility = View.GONE
    }

    override fun onBackPressed() {
        super.onBackPressed()

    }

    override fun attachBaseContext(newBase: Context?) =
        super.attachBaseContext(MainApplication.localeManager!!.setLocale(newBase!!))


    private fun setupBottomNavMenu(navController: NavController) {
        bottomNav?.setupWithNavController(navController)
        bottomNav?.setOnItemSelectedListener {
            when (it.itemId) {

                R.id.dashboard -> {
                    navController.navigate(R.id.dashboardFragment, null)
                }
                R.id.order -> {
                    navController.navigate(R.id.orderHistoryFragment, null)
                }
                R.id.profile -> {
                    navController.navigate(R.id.profileFragment, null)
                }
            }
            /* val newFragment = if (true) {
                 fragments[it.itemId] ?: TempFragment()
             } else {
                 // We are pretending we aren't keeping the Fragments in memory
                 TempFragment()
             }
             fragments[it.itemId] = newFragment
             idN = it.itemId
             if (*//*state_switch.isChecked &&*//*idN != 0) {
                Log.d("321321", "setupBottomNavMenu: $idN")
                saveCurrentState()
                stateHelper.restoreState(newFragment, it.itemId)
            }*/
            Log.d("321321", "saveCurrentState:$idN ")
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.container, newFragment)
//                .commitNowAllowingStateLoss()


            return@setOnItemSelectedListener true
        }

    }


    companion object {
        private const val STATE_SAVE_STATE = "save_state"
        private const val STATE_KEEP_FRAGS = "keep_frags"
        private const val STATE_HELPER = "helper"
        var bottomNav: BottomNavigationView? = null

    }


    private lateinit var stateHelper: FragHelper

    private val fragments = mutableMapOf<Int, Fragment>()

    var idN: Int = 0;
    override fun onSaveInstanceState(outState: Bundle) {
        saveCurrentState()

//        outState.putInt("asdf",navController!!.currentDestination!!.id)
        outState.putBundle(STATE_HELPER, stateHelper.saveHelperState())

        super.onSaveInstanceState(outState)
    }

    private fun saveCurrentState() {
        Log.d("321321", "saveCurrentState:$idN ")
        fragments[navController!!.currentDestination!!.id]?.let { oldFragment ->
            stateHelper.saveState(oldFragment, navController!!.currentDestination!!.id)
        }
    }

    override fun onFbSignInFail() {
        TODO("Not yet implemented")
    }

    override fun onFbSignInSuccess() {
        TODO("Not yet implemented")
    }

    override fun onFbProfileReceived(facebookUser: FacebookUser?) {


    }

    override fun onFBSignOut() {
        TODO("Not yet implemented")
    }

}