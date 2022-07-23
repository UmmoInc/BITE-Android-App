package xyz.ummo.bite.signup



import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.transition.TransitionManager
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


import xyz.ummo.bite.R
import xyz.ummo.bite.databinding.FragmentOTPBinding
import xyz.ummo.bite.main.MainActivity


import xyz.ummo.bite.signup.phoneauth.phoneAuthBottomSheet

import xyz.ummo.bite.utils.eventBusClasses.otpEntered
import java.util.concurrent.TimeUnit


private var motpEntered=otpEntered(false)

class OTPFragment : Fragment() {

    private lateinit var _binding: FragmentOTPBinding
    private val binding get() = _binding!!
    private var   storedVerificationId: String? = "default"
    private var resendToken: PhoneAuthProvider.ForceResendingToken? = null
    private lateinit var phoneNumber : String
    private lateinit var rootview:View
    //Firebase variables
    var count :Int =0
    private lateinit var auth: FirebaseAuth
    private var SMS_String:String=""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentOTPBinding.inflate(inflater, container, false)
        val args = OTPFragmentArgs.fromBundle(requireArguments())
        binding.otpSubtitleUsermobile.text = args.phoneNumber
        rootview = binding.root
        phoneNumber =args.phoneNumber.trim()
        auth = FirebaseAuth.getInstance()
        setNavigationUplistener()
        setonCodeSentvariables()
        CreateTextBoxAutoFocusFeature()

        return rootview
    }

    private fun setonCodeSentvariables() {

        val args = OTPFragmentArgs.fromBundle(requireArguments())
        storedVerificationId=args.verificationID

    }

    private fun disableTextBoxes() {
        binding.smsIntaker.isEnabled=false
        binding.smsIntaker.visibility=View.GONE

    }
    private fun enableTextBoxes() {
        binding.smsIntaker.isEnabled=true
        binding.smsIntaker.visibility=View.VISIBLE

    }
    private fun setNavigationUplistener() {
        //navigation icon to sign up fragment
        binding.appBarOtp.setNavigationOnClickListener(View.OnClickListener { appbar ->
            Navigation.findNavController(appbar)
                .navigate(R.id.action_OTPFragment_to_userRegistrationFragment)
        })
    }





    private fun validator(
        box1: TextInputEditText,
        box2: TextInputEditText,
        box3: TextInputEditText,
        box4: TextInputEditText,
        box5: TextInputEditText,
        box6: TextInputEditText
    ): Boolean {

        val condition1: Boolean =
            !box1.text.isNullOrEmpty() && !box2.text.isNullOrEmpty() && !box3.text.isNullOrEmpty()
        val condition2: Boolean =
            !box4.text.isNullOrEmpty() && !box5.text.isNullOrEmpty() && !box6.text.isNullOrEmpty()

        return !(condition1 && condition2)
    }



@Subscribe
fun checkIfSmsIsAvailable(event: otpEntered){

    if (event.smsProvided == true){
signInPhoneNumberWithSMSCode(SMS_String)
    }

}
    private fun signInPhoneNumberWithSMSCode(code: String){
        try {
            val credential = PhoneAuthProvider.getCredential(storedVerificationId!!,
                code
            )
   Log.d("credentialfromsms:","$code")
           signInWithPhoneAuthCredential(credential)

        } catch (ex: Exception) {
            Log.d("exceptionnfromsms:","${ex.message}")

        }
    }


    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) {
                if (it.isSuccessful) {
                    movetoSplashScreen()
                    Toast.makeText(requireContext(), "SuccessfullLogin", Toast.LENGTH_SHORT)
                        .show()
                } else {
                   launchPhoneAuthBottomSheet()
                }
            }
    }

    private fun launchPhoneAuthBottomSheet() {
        val foodItemOrderBottomSheet = phoneAuthBottomSheet()
        foodItemOrderBottomSheet.show(MainActivity.supportFM, phoneAuthBottomSheet.TAG)
    }
    private fun movetoSplashScreen() {
//  To menu fragment
        val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(
            R.id.NavHostFragment
        ) as NavHostFragment
        val   navController = navHostFragment.navController
        navController.navigate(R.id.action_OTPFragment_to_splashScreenToMenu)
    }
    private fun CreateTextBoxAutoFocusFeature(){
        binding.apply {

            val array: Array<Any> = arrayOf(
                otpEditText1.id,
                otpEditText2.id,
                otpEditText3.id,
                otpEditText4.id,
                otpEditText5.id,
                otpEditText6.id
            )


            for (id in array) {

                when (id) {
                    array[0] -> {
                        otpEditText1.addTextChangedListener(object : TextWatcher {
                            override fun beforeTextChanged(
                                s: CharSequence?,
                                start: Int,
                                count: Int,
                                after: Int
                            ) {

                            }

                            override fun onTextChanged(
                                s: CharSequence?,
                                start: Int,
                                before: Int,
                                count: Int
                            ) {

                            }

                            override fun afterTextChanged(s: Editable?) {
                                rootview.findViewById<TextInputEditText>(array[1] as Int).isFocusable =
                                    true

                                //change focus on change but not if change = delete operation
                                if (!(s!!.toString().length == 0)) {
                                    TransitionManager.beginDelayedTransition(rootview as ViewGroup)
                                    rootview.findViewById<TextInputEditText>(array[1] as Int)
                                        .requestFocus()
                                    SMS_String+=s!!.toString()
                                    count++
                                }
                            }
                        })
                    }


                    array[1] -> {
                        otpEditText2.addTextChangedListener(object : TextWatcher {
                            override fun beforeTextChanged(
                                s: CharSequence?,
                                start: Int,
                                count: Int,
                                after: Int
                            ) {

                            }

                            override fun onTextChanged(
                                s: CharSequence?,
                                start: Int,
                                before: Int,
                                count: Int
                            ) {

                            }

                            override fun afterTextChanged(s: Editable?) {
                                rootview.findViewById<TextInputEditText>(array[2] as Int).isFocusable =
                                    true

                                //change focus on change but not if change = delete operation
                                if (!(s!!.toString().length == 0)) {
                                    TransitionManager.beginDelayedTransition(rootview as ViewGroup)
                                    rootview.findViewById<TextInputEditText>(array[2] as Int)
                                        .requestFocus()
                                    SMS_String+=s!!.toString()
                                    count ++
                                }
                            }
                        })


                    }


                    array[2] -> {
                        otpEditText3.addTextChangedListener(object : TextWatcher {
                            override fun beforeTextChanged(
                                s: CharSequence?,
                                start: Int,
                                count: Int,
                                after: Int
                            ) {

                            }

                            override fun onTextChanged(
                                s: CharSequence?,
                                start: Int,
                                before: Int,
                                count: Int
                            ) {

                            }

                            override fun afterTextChanged(s: Editable?) {
                                // first delete data in edit text

                                rootview.findViewById<TextInputEditText>(array[3] as Int).isFocusable =
                                    true
                                //change focus on change but not if change = delete operation
                                if (!(s!!.toString().length == 0)) {
                                    TransitionManager.beginDelayedTransition(rootview as ViewGroup)
                                    rootview.findViewById<TextInputEditText>(array[3] as Int)
                                        .requestFocus()
                                    SMS_String+=s!!.toString()
                                    count++


                                }

                            }
                        })


                    }


                    array[3] -> {
                        otpEditText4.addTextChangedListener(object : TextWatcher {
                            override fun beforeTextChanged(
                                s: CharSequence?,
                                start: Int,
                                count: Int,
                                after: Int
                            ) {

                            }

                            override fun onTextChanged(
                                s: CharSequence?,
                                start: Int,
                                before: Int,
                                count: Int
                            ) {

                            }

                            override fun afterTextChanged(s: Editable?) {
                                rootview.findViewById<TextInputEditText>(array[4] as Int).isFocusable =
                                    true
                                //change focus on change but not if change = delete operation
                                if (!(s!!.toString().length == 0)) {
                                    TransitionManager.beginDelayedTransition(rootview as ViewGroup)
                                    rootview.findViewById<TextInputEditText>(array[4] as Int)
                                        .requestFocus()
                                    SMS_String+=s!!.toString()
                                    count++
                                }
                            }
                        })


                    }


                    array[4] -> {
                        otpEditText5.addTextChangedListener(object : TextWatcher {
                            override fun beforeTextChanged(
                                s: CharSequence?,
                                start: Int,
                                count: Int,
                                after: Int
                            ) {

                            }

                            override fun onTextChanged(
                                s: CharSequence?,
                                start: Int,
                                before: Int,
                                count: Int
                            ) {

                            }

                            override fun afterTextChanged(s: Editable?) {
                                rootview.findViewById<TextInputEditText>(array[5] as Int).isFocusable =
                                    true
                                //change focus on change but not if change = delete operation
                                if (!(s!!.toString().length == 0)) {
                                    TransitionManager.beginDelayedTransition(rootview as ViewGroup)
                                    rootview.findViewById<TextInputEditText>(array[5] as Int)
                                        .requestFocus()
                                    SMS_String+=s!!.toString()
                                    count++
                                }
                            }
                        })


                    }


                    array[5] -> {
                        otpEditText6.addTextChangedListener(object : TextWatcher {
                            override fun beforeTextChanged(
                                s: CharSequence?,
                                start: Int,
                                count: Int,
                                after: Int
                            ) {

                            }

                            override fun onTextChanged(
                                s: CharSequence?,
                                start: Int,
                                before: Int,
                                count: Int
                            ) {

                            }

                            override fun afterTextChanged(s: Editable?) {
                                if (!(s!!.toString().length == 0)) {
                                    TransitionManager.beginDelayedTransition(rootview as ViewGroup)
                                    rootview.findViewById<TextInputEditText>(array[0] as Int)
                                        .requestFocus()
                                    count++
                                    SMS_String+=s!!.toString()
                                    otpEnteredpublisher()


                                }

                            }
                        })


                    }


                }
            }
        }
    }
    @Subscribe
    private fun otpEnteredpublisher(){
        if(count==6){
            motpEntered= otpEntered(true)
            EventBus.getDefault().post(motpEntered)}
        else{

            Toast.makeText(requireContext(), "no", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }
    @Subscribe
    override fun onDestroy() {

        motpEntered= otpEntered(false)
        EventBus.getDefault().post(motpEntered)
        super.onDestroy()
    }
}