package xyz.ummo.bite.signup


import android.content.ContentValues.TAG
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.transition.TransitionManager
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import timber.log.Timber

import xyz.ummo.bite.R
import xyz.ummo.bite.databinding.FragmentOTPBinding
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
        binding.otpSubtitleUsermobile.text = arguments?.getString("phone_key")
        // Timber.e("phone->$arguments?.getString(\"phone_key\")")
        Log.e("n", "$arguments?.getString(\"phone_key\")")
        rootview = binding.root



        phoneNumber =arguments?.getString("phone_key").toString()
        auth = FirebaseAuth.getInstance()
//phoneAuth()

      CreateTextBoxAutoFocusFeature()

movetoNextFrag(motpEntered)

      //  Toast.makeText(requireActivity(), getSmScode(), Toast.LENGTH_SHORT).show()






        return rootview
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



    private fun getSmScode(): String {


        return if(count==6){
            SMS_String
        } else{


            ""
        }



    }


    private fun verifyPhoneNumberWithCode(code: String) {
        try {
            val credential = PhoneAuthProvider.getCredential(storedVerificationId!!,
               code
            )
            Timber.e("credentail->$credential")
            signInWithPhoneAuthCredential(credential)
        } catch (ex: Exception) {
            //    showSnackbarRed("EXC -> $ex", 0)
            Toast.makeText(requireActivity(), "fail to call signInwithPhoneAuth", Toast.LENGTH_SHORT).show()
        }
    }


    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {

        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) {
                if (it.isSuccessful) {
                    navigationController()
                    Toast.makeText(requireContext(), "SuccessfullLogin", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(requireContext(), "FailedLogin", Toast.LENGTH_LONG).show()
                }
            }

    }


    private fun navigationController() {
        //navigation icon to sign up fragment
        binding.appBarOtp.setNavigationOnClickListener(View.OnClickListener { appbar ->
            Navigation.findNavController(appbar)
                .navigate(R.id.action_OTPFragment_to_userRegistrationFragment)
        })
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
                                 publisher()


                            }

                        }
                    })


                }


            }
        }
    }

 }
    @Subscribe
    private fun publisher(){
        if(count==6){
            motpEntered= otpEntered(true)
        EventBus.getDefault().post(motpEntered)}
        else{

            Toast.makeText(requireContext(), "no", Toast.LENGTH_SHORT).show()
        }
    }


@Subscribe
fun movetoNextFrag(event:otpEntered) {
    if(event.smsProvided == true){
        Toast.makeText(requireActivity(), "Move", Toast.LENGTH_SHORT).show()
        Timber.d("eventTrue->${event.smsProvided}")
        navigationController()
    }
    else{
        Timber.d("eventTrue->${event.smsProvided}")
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

