package xyz.ummo.bite.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import kotlinx.coroutines.*
import xyz.ummo.bite.R
import xyz.ummo.bite.databinding.FragmentSplashScreenToMenuBinding
import xyz.ummo.bite.utils.constants.Constants.Companion.SPLASHSCREEN_DELAY_TIME
import kotlin.coroutines.coroutineContext

class SplashScreenToJoints : Fragment() {
    private lateinit var _binding: FragmentSplashScreenToMenuBinding
    private val binding get() = _binding
    private lateinit var rootView: View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSplashScreenToMenuBinding.inflate(inflater, container, false)
        rootView = binding.root




        return rootView

    }
    /// this function opens the mainscreen fragment
        private suspend fun moveToNexFragment(){

            // use sleep instead of delay to avoid error crash when user clicks back
            //while splashscreen fragment is loaded
        // so the screen loads before the system sleeps
        delay(SPLASHSCREEN_DELAY_TIME)
        //sleep(Constants.TOMENUFRAGMENT_SPLASHSCREEN_WAIT_TIME)
        delay(SPLASHSCREEN_DELAY_TIME)
        navigationController()

    }

    private fun navigationController() {
        val navHostFragment = (requireActivity()).supportFragmentManager.findFragmentById(
            R.id.NavHostFragment
        ) as NavHostFragment
        val   navController = navHostFragment.navController
        navController.navigate(R.id.action_splashScreenToMenu_to_mainscreen)

    }
}