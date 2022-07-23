package xyz.ummo.bite

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import xyz.ummo.bite.databinding.FragmentSplashScreenToOTPBinding
import xyz.ummo.bite.signup.OTPFragmentDirections
import xyz.ummo.bite.utils.constants.Constants
import java.util.concurrent.TimeUnit


class SplashScreenToOTP : Fragment() {

     private lateinit var  _binding : FragmentSplashScreenToOTPBinding
private val binding get() =_binding
    private lateinit var rootView:View
    private lateinit var mBundle: Bundle
    private var   storedVerificationId: String? = "default"
    private var resendToken: PhoneAuthProvider.ForceResendingToken? = null
    private lateinit var phoneNumber : String
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
_binding = FragmentSplashScreenToOTPBinding.inflate(inflater, container,false)
        rootView=binding.root
        auth = FirebaseAuth.getInstance()

        phoneNumber =arguments?.getString("phone_key").toString()


    phoneAuth()

        return rootView

    }
    private fun moveToOTPFragment(){
        var job : Job?= null// background thread to delay transition SplashScreen->OTPFragment
        job = MainScope().launch {
            delay(Constants.TOMENUFROM_OTP_SPLASHSCREEN_WAIT_TIME)
            navigationController()
        }
    }
    private fun navigationController() {
        val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(
            R.id.NavHostFragment
        ) as NavHostFragment
        val   navController = navHostFragment.navController
        navController.navigate(SplashScreenToOTPDirections.actionSplashScreenToOTPToOTPFragment(storedVerificationId!!,
            resendToken.toString()
        ,phoneNumber))

    }



    private fun phoneAuth() {
        val   callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d("verifC", "onVerificationCompleted:$credential")

                signInWithPhoneAuthCredential(credential)
                moveTo_mainscreenFragment()
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.d("verifF", "onVerificationFailed", e)

                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                }

                // Show a message and update the UI
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d("codeSent", "onCodeSent:$verificationId")

                // Save verification ID and resending token so we can use them later
                storedVerificationId = verificationId
                resendToken = token
                moveToOTPFragment()

            }
        }
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(requireActivity())                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }
    private fun moveTo_mainscreenFragment() {

        val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(
            R.id.NavHostFragment
        ) as NavHostFragment
        val   navController = navHostFragment.navController
        navController.navigate(R.id.action_splashScreenToOTP_to_splashScreenToMenu2)

    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) {
                if (it.isSuccessful) {

                    Log.d("loginstatus","successful")
                    Toast.makeText(requireContext(), "SuccessfullLogin", Toast.LENGTH_SHORT)
                        .show()
                } else {
                 Log.d("loginstatus","loginfailed")
                }
            }
    }
}

