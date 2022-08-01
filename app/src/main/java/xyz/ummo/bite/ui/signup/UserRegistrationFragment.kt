package xyz.ummo.bite.ui.signup

import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Html
import android.text.method.LinkMovementMethod
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.json.JSONObject
import timber.log.Timber
import xyz.ummo.bite.R
import xyz.ummo.bite.databinding.FragmentUserRegistrationBinding
import xyz.ummo.bite.repos.SignUpRepo
import xyz.ummo.bite.utils.constants.Constants.Companion.CHARACTER_COUNT_PHONE
import xyz.ummo.bite.utils.eventBusClasses.RegistrationCallbackEvent
import java.io.IOException

class UserRegistrationFragment : Fragment() {
    private lateinit var _binding: FragmentUserRegistrationBinding
    private val binding get() = _binding
    private lateinit var mBundle: Bundle
    private lateinit var rootView: View
    private var passwordEndIconChangeHelper = true
    private val signUpRepo = SignUpRepo()
    private val coroutineScope =
        CoroutineScope(Dispatchers.Main + UserRegistrationFragment.parentJob)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        _binding = FragmentUserRegistrationBinding.inflate(inflater, container, false)
        rootView = binding.root
        mBundle = Bundle()
        termsAndConditions()
        navigationControls()
        //  setupPasswordTextinput()

        binding.submitBtn.setOnClickListener {
            checkIfAllTextBoxesAreFilled()
        }
        return rootView
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    private fun setupPasswordTextinput() {
        binding.apply {

            passwordTextinput.setEndIconOnClickListener {
                passwordEndIconChangeHelper = if (passwordEndIconChangeHelper) {
                    passwordTextinput.setEndIconDrawable(R.drawable.password_visibilty)
                    false
                } else {
                    passwordTextinput.setEndIconDrawable(R.drawable.password_visibilty_off)
                    true
                }
            }
        }
    }

    //TODO: Move this function to the ViewModel
    private suspend fun signUserUp() {
        val userSignUpInfo = JSONObject()
            .put("mobile", binding.userRegistrationPhone.text)
            .put("email", binding.registrationEmailAddress.text)
            .put("password", binding.registrationPassword.text)
            .put("firstname", binding.registrationName.text)
            .put("lastname", binding.registrationSurname.text)

        withContext(Dispatchers.IO) {
            try {
                signUpRepo.signUserUpDirectly(userSignUpInfo)
                Timber.e("USER REGISTRATION SUCCESSFUL!")
            } catch (IOE: IOException) {
                //TODO: Add a UX exception handler
                IOE.printStackTrace()
                Timber.e("USER REGISTRATION Exception -> $IOE")
            }
        }
    }

    /** The following [EventBus] subscription is for checking the states of our Registration event.
     * We receive these events from the [SignUpRepo]**/
    @Subscribe
    fun onRegistrationCallbackEvent(registrationCallbackEvent: RegistrationCallbackEvent) {
        when (registrationCallbackEvent.responseStatusCode) {
            201 -> {
                Timber.e("Server responded well -> ${registrationCallbackEvent.responseStatusMessage}")
                moveToOTPFragment()
            }
            400 -> {
                Timber.e("Server not happy -> ${registrationCallbackEvent.responseStatusMessage}")
                showSnackbarWarning(registrationCallbackEvent.responseStatusMessage.toString(), 0)

                if (registrationCallbackEvent.responseStatusMessage!!.contains("Email", true)) {
                    /** Since the [EventBus] instance cannot touch the view within the context, we
                     * then runOnUiThread **/
                    requireActivity().runOnUiThread {
                        binding.registrationEmailAddress.requestFocus()
                        binding.registrationEmailAddress.error = "Please try another email"
                    }
                }
            }
            500 -> {
                Timber.e("Server acting weird -> ${registrationCallbackEvent.responseStatusMessage}")
                showSnackbarWarning(registrationCallbackEvent.responseStatusMessage.toString(), 0)
            }
        }
    }

    private fun transitionUserToNewScreen() {
        val timer = object : CountDownTimer(3000, 1000) {
            override fun onTick(p0: Long) {
                showProgressBar()
            }

            override fun onFinish() {
                hideProgressBar()
            }
        }

        timer.start()
    }

    private fun showProgressBar() {
        binding.signingUpLoaderRelativeLayout.visibility = View.VISIBLE
        binding.userRegistrationNestedScrollView.visibility = View.GONE
    }

    private fun hideProgressBar() {
        binding.signingUpLoaderRelativeLayout.visibility = View.GONE
        binding.userRegistrationNestedScrollView.visibility = View.VISIBLE
    }

    private fun navigationControls() {

        // to Log In Fragment
        binding.logInText.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_userRegistrationFragment_to_signInFragment)
        }

        binding.appBar.setNavigationOnClickListener(View.OnClickListener {

            Navigation.findNavController(it)
                .navigate(R.id.action_userRegistrationFragment_to_signInFragment)
        })
//To OTP Fragment
        binding.submitBtn.setOnClickListener(View.OnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_userRegistrationFragment_to_splashScreenOTPFragment)
        })

    }

    private fun inputValidator(
        name: TextInputEditText, surname: TextInputEditText,
        mobile: TextInputEditText, email: TextInputEditText,
        password: TextInputEditText
    ): Boolean {
        val condition1: Boolean =
            !name.text.isNullOrEmpty() && !surname.text.isNullOrEmpty() && !mobile.text.isNullOrEmpty()
        val condition2: Boolean = !email.text.isNullOrEmpty() && !password.text.isNullOrEmpty()

        Timber.e("INPUT VALIDATOR -> 1: $condition1 && 2: $condition2")
        return !(condition1 && condition2)

    }

    private fun isPhoneValid(text: TextInputEditText?): Boolean {
        return text != null && text.length() == CHARACTER_COUNT_PHONE
    }

    private fun termsAndConditions() {
        binding.legalTerms.isClickable = true
        binding.legalTerms.movementMethod = LinkMovementMethod.getInstance()
        val legalTerms =
            "<div>By signing up, you agree to Ummo's <a href='https://sites.google.com/view/ummo-terms-and-conditions/home'>Terms of Use</a> & <a href='https://sites.google.com/view/ummo-privacy-policy/home'> Privacy Policy </a></div>"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.legalTerms.text = Html.fromHtml(legalTerms, Html.FROM_HTML_MODE_LEGACY)

            Timber.e("USING HTML FLAG")
        } else {
            binding.legalTerms.text = Html.fromHtml(legalTerms)
            Timber.e("NOT USING HTML FLAG")
        }
    }

    private fun checkIfAllTextBoxesAreFilled() {
        val editTextsNotFilled = inputValidator(
            binding.registrationName,
            binding.registrationSurname,
            binding.registrationEmailAddress,
            binding.userRegistrationPhone,
            binding.registrationPassword
        )
        if (!editTextsNotFilled) {
            //Check if Phone number is valid
            if (isPhoneValid(binding.userRegistrationPhone)) {

                checkIfEmailIsValid()
                // moveToOTPFragment()

            } else {
                binding.userRegistrationPhone.isFocusable = true
                binding.userRegistrationPhone.requestFocus()
                binding.userRegistrationPhone.error = "Please check your mobile contact"
            }
        } else {
            Toast.makeText(requireContext(), "Please fill in all fields ", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun checkIfEmailIsValid() {
        val emailField: EditText = binding.registrationEmailAddress
        val userEmail: String = binding.registrationEmailAddress.text.toString()

        when {
            Patterns.EMAIL_ADDRESS.matcher(userEmail).matches().not() -> {
                emailField.error = "Please use a valid email..."
                emailField.requestFocus()
            }
            emailField.length() == 0 -> {
                emailField.error = "Please provide an email..."
                emailField.requestFocus()
            }
            else -> {
                transitionUserToNewScreen()
                coroutineScope.launch(Dispatchers.Main) {
                    signUserUp()
                }
            }
        }
    }

    private fun showSnackbarWarning(message: String, length: Int) {
        /** Length is 0 for Snackbar.LENGTH_LONG
         *  Length is -1 for Snackbar.LENGTH_SHORT
         *  Length is -2 for Snackbar.LENGTH_INDEFINITE**/
        val snackbar =
            Snackbar.make(requireActivity().findViewById(android.R.id.content), message, length)
        snackbar.setTextColor(resources.getColor(R.color.gold))
        val textView =
            snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        textView.textSize = 14F
        snackbar.show()
    }

    private fun moveToOTPFragment() {

        // GET SUPPLIED PHONE NUMBER
        val phone = binding.userRegistrationPhone.text.toString()
        mBundle.putString("phone_key", "+268$phone")
        // navigate to OTP fragment : prepare  verify user
        Navigation.findNavController(rootView)
            .navigate(R.id.action_userRegistrationFragment_to_splashScreenOTPFragment, mBundle)
    }

    companion object {
        private val parentJob = Job()
    }
}