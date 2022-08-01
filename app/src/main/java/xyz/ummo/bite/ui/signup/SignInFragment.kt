package xyz.ummo.bite.ui.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.facebook.AccessToken
import com.facebook.CallbackManager.Factory.create
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.login.LoginResult
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import org.json.JSONObject
import timber.log.Timber
import xyz.ummo.bite.R
import xyz.ummo.bite.databinding.FragmentSignInBinding
import xyz.ummo.bite.repos.SignUpRepo
import java.io.IOException

class SignInFragment : Fragment() {

    private lateinit var _binding: FragmentSignInBinding
    private val binding get() = _binding
    private val accessToken = AccessToken.getCurrentAccessToken()
    private var isLoggedIn: Boolean = accessToken != null && !accessToken.isExpired
    private val signUpRepo = SignUpRepo()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + parentJob)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.e("User FB Access Token -> $accessToken")
        Timber.e("User is FB Logged In -> $isLoggedIn")

        FacebookSdk.fullyInitialize()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)

        navigationController()
        val view = binding.root

        //login onclickListener

        binding.loginBtn.setOnClickListener {
            coroutineScope.launch(Dispatchers.IO) {
                signUserIn()
            }
        }
        continueWithFacebook()

        return view

    }

    private fun navigationController() {
        // To register fragment
        binding.registerText.isClickable = true
        binding.registerText.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_signInFragment_to_userRegistrationFragment)
        }

        //To menu home page

        /*binding.loginBtn.setOnClickListener {

            Navigation.findNavController(it).navigate(R.id.action_signInFragment_to_mainscreen)
        }*/

// To forgot password fragment
        binding.forgotPasswordText.isClickable = true
        binding.forgotPasswordText.setOnClickListener(View.OnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_signInFragment_to_forgotPasswordFragment2)
        })
    }

    private suspend fun signUserIn() {
        val userSignInInfo = JSONObject()
            .put("email", binding.userEmailEditText.text)
            .put("password", binding.userPasswordEditText.text)
        Timber.e("Attempting to sign in -> $userSignInInfo")

        withContext(Dispatchers.IO) {
            try {
                signUpRepo.signUserUpDirectly(userSignInInfo)
            } catch (IOE: IOException) {
                IOE.printStackTrace()
                Timber.e("$IOE")
            }
        }
    }

    private fun continueWithFacebook() {
        val callbackManagerFactory = create()

        binding.fbLoginButton.setFragment(this)

        binding.fbLoginButton.registerCallback(
            callbackManagerFactory,
            object : FacebookCallback<LoginResult?> {

                override fun onCancel() {
                    // App code
                    Timber.e("FB log-in")
                }

                override fun onError(error: FacebookException) {
                    Timber.e("Error logging in -> $error")
                    // App code
                }

                override fun onSuccess(result: LoginResult?) {
                    val snackbar = Snackbar.make(
                        requireActivity().findViewById(android.R.id.content),
                        "Successfully logged in",
                        0
                    )
                    snackbar.show()
                    Timber.e("FB Login Access Token -> ${result!!.accessToken}")
                    Timber.e("FB Login Auth Token -> ${result.authenticationToken}")
                }
            })
        binding.fbLoginButton.setOnClickListener {
        }
    }

    companion object {
        private val parentJob = Job()
    }
}