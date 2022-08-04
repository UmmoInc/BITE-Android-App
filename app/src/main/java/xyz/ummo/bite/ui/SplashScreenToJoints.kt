package xyz.ummo.bite.ui

import android.os.Bundle
import android.os.SystemClock.sleep
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import kotlinx.coroutines.*
import xyz.ummo.bite.R
import xyz.ummo.bite.databinding.FragmentSplashScreenToMenuBinding
import xyz.ummo.bite.main.MainActivity
import xyz.ummo.bite.utils.constants.Constants


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
        moveToNexFragment()
        return rootView

    }
    /// this function opens the mainscreen fragment
        private fun moveToNexFragment(){
        var job : Job?= null
        job = MainScope().launch {

            // use sleep instead of delay to avoid error crash when user clicks back
            //while splashscreen fragment is loaded
            sleep(Constants.TOMENUFRAGMENT_SPLASHSCREEN_WAIT_TIME)
            navigationController()
        }
    }

    private fun navigationController() {
        val navHostFragment = (requireActivity()).supportFragmentManager.findFragmentById(
            R.id.NavHostFragment
        ) as NavHostFragment
        val   navController = navHostFragment.navController
        navController.navigate(R.id.action_splashScreenToMenu_to_mainscreen)

    }
}