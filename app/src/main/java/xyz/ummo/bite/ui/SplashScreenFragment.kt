package xyz.ummo.bite.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import xyz.ummo.bite.R
import xyz.ummo.bite.databinding.FragmentSplashScreenBinding
import xyz.ummo.bite.main.MainActivity
import xyz.ummo.bite.utils.constants.Constants.Companion.TOMENUFRAGMENT_SPLASHSCREEN_WAIT_TIME

class SplashScreenFragment : Fragment() {

private lateinit var  _binding :FragmentSplashScreenBinding
private val  binding get() = _binding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        // Inflate the layout for this fragment
   _binding = FragmentSplashScreenBinding.inflate(inflater,container,false)
moveToNexFragment()
        return binding.root



    }

    private fun navigationController(){

// get Restuarent name

        val args = SplashScreenFragmentArgs.fromBundle(requireArguments())
        val JointName : String= args.nameofjoint
  Log.e("splashArgument",JointName)

        val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(
            R.id.NavHostFragment
        ) as NavHostFragment
        val   navController = navHostFragment.navController
        navController.navigate(
        SplashScreenFragmentDirections.actionSplashScreenFragmentToFoodMenuFragment(JointName)
        )

        //(binding.root as MainActivity).findNavController(R.id.NavHostFragment).navigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToFoodMenuFragment(JointName))
    }
    private fun moveToNexFragment(){
        var job : Job?= null
        job = MainScope().launch {
            delay(TOMENUFRAGMENT_SPLASHSCREEN_WAIT_TIME)
            navigationController()
        }
    }

  }