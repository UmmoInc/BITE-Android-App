package xyz.ummo.bite.signup.phoneauth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import xyz.ummo.bite.R
import xyz.ummo.bite.databinding.FragmentFoodItemBottomSheetBinding
import xyz.ummo.bite.databinding.FragmentPhoneAuthBottomSheetBinding


class phoneAuthBottomSheet : BottomSheetDialogFragment() {


    private lateinit var  _binding: FragmentPhoneAuthBottomSheetBinding
    private val  binding get()=_binding!!
    private lateinit var  rootView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
      _binding = FragmentPhoneAuthBottomSheetBinding.inflate(inflater,container,false)
        rootView= binding.root
         binding.phoneauthBottomsheetSkipButton.setOnClickListener(View.OnClickListener {
             moveToFoodJointSplashScreen()
             this.dismiss()
         })
        binding.phoneauthresendSmsButton.setOnClickListener(View.OnClickListener {
            restartPhoneAuthProcess()
                this.dismiss()
        })


        return rootView
    }

    private fun restartPhoneAuthProcess() {



       //
        val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(
            R.id.NavHostFragment
        ) as NavHostFragment
        val   navController = navHostFragment.navController
     //   navController.navigate(R.id.action_OTPFragment_to_splashScreenToOTP)
        navController.navigate(R.id.action_OTPFragment_to_splashScreenToOTP)


    }



    private fun moveToFoodJointSplashScreen() {
//  To menu fragment
        val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(
            R.id.NavHostFragment
        ) as NavHostFragment
        val   navController = navHostFragment.navController
        navController.navigate(R.id.action_OTPFragment_to_splashScreenToMenu)

    }


    companion object {

        const val TAG = "PhoneAuthBottomSheet"}
}