package xyz.ummo.bite.ui.signup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.Task
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import xyz.ummo.bite.utils.constants.Constants



import xyz.ummo.bite.viewmodels.SignUpViewModel

class SignInFragment : Fragment() {

    private lateinit var _binding: FragmentSignInBinding
    private val binding get() = _binding
    private val accessToken = AccessToken.getCurrentAccessToken()
    private var isLoggedIn: Boolean = accessToken != null && !accessToken.isExpired
    private val signUpRepo = SignUpRepo()
    private lateinit var mSignUpViewModel: SignUpViewModel
    private lateinit var      mGoogleSignInClient : GoogleSignInClient
    private val coroutineScope = CoroutineScope(Dispatchers.Main + parentJob)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.e("User FB Access Token -> $accessToken")
        Timber.e("User is FB Logged In -> $isLoggedIn")

        FacebookSdk.fullyInitialize()

        ///Google Sign-in related:
        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN
        val serverClientId =getString(R.string.google_SERVER_CLIENT_ID)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestScopes(Scope(Scopes.DRIVE_APPFOLDER))
            .requestServerAuthCode(serverClientId)
            .requestProfile()
            .requestIdToken(serverClientId)
            .build()
// Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        setGoogleSigninText()
        setgooglesignInButtonOnClick()
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
    private fun GooglesignIn() {
        val signInIntent: Intent = mGoogleSignInClient.getSignInIntent()
        startActivityForResult(signInIntent, Constants.RC_SIGN_IN)
    }
    private fun setgooglesignInButtonOnClick() {
        binding.signInButton.setOnClickListener(View.OnClickListener {
            //Move to GoogleSignIn Fragment
            GooglesignIn()
        })
    }




    private fun setGoogleSigninText() {
        val TextViewWithin:View = binding.signInButton.getChildAt(0)
        (TextViewWithin as TextView).setText(getString(R.string.login_google))

    }
    private fun GooglesignOut(){
        mGoogleSignInClient.signOut()
            .addOnCompleteListener( {
                fun onComplete(task: Task<Void?>) {
                    Toast.makeText(requireContext(), "Signed Out", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun moveTomainscreen() {

        val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(
            R.id.NavHostFragment
        ) as NavHostFragment
        val   navController = navHostFragment.navController
        navController.navigate(R.id.action_signInFragment_to_mainscreen)
    }


    private fun updateUI(account: GoogleSignInAccount?) {
// move user to main screen -> do this later
        // for now  make a Toast
        if(account !=null) {
            moveTomainscreen()
        }
        else {
            //prompt google sign In .
            Toast.makeText(requireContext(), "begin  google signIn..", Toast.LENGTH_SHORT).show()
        }
    }




    private fun navigationController() {
        // To register fragment
        binding.registerText.isClickable = true
        binding.registerText.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_signInFragment_to_userRegistrationFragment)
        }

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
                signUpRepo.signUserInDirectly(userSignInInfo)
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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == Constants.RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(task: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount = task.getResult(ApiException::class.java)
            val authCode = account.serverAuthCode
            Timber.e("authcode->$authCode")
            Log.d("authCode:","$authCode")
            Timber.e("idToke->${account.idToken}")
            Log.d("idToken:","${account.idToken}")
            Timber.e("family-name->${account.familyName}")
            Log.d("authcode:", "$authCode")
            try    {coroutineScope.launch {  sendAuthCodeToAsh(authCode)}}
            catch (e:Exception){
                Timber.e("failedTosendGoogleAuthCode")
            }

            // Signed in successfully, show authenticated UI.
            updateUI(account)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.d("GoogleSignIN", "signInResult:failed code=" + e.statusCode)
            updateUI(null)
        }
    }

    private suspend fun sendAuthCodeToAsh(authCode: String?) {
        // HTTP POST REQUEST TO SEND AUTH CODE
        withContext(Dispatchers.IO) {
            try {
                signUpRepo.signUserUpGoogle(authCode)
            } catch (IOE: IOException) {
                IOE.printStackTrace()
                Timber.e("$IOE")
            }
        }
    }


    override fun onStart(){
        super.onStart()
        // Check for existing Google Sign In account, if the user is already signed in
// the GoogleSignInAccount will be non-null.
        // Check for existing Google Sign In account, if the user is already signed in
// the GoogleSignInAccount will be non-null.
        val account = GoogleSignIn.getLastSignedInAccount(requireContext())
        updateUI(account)
//Note: If you need to detect changes to a user's auth state that happen outside your app,
        //  such as access token or ID token revocation, or to perform cross-device sign-in,
        //you might also call ***GoogleSignInClient.silentSignIn**** when your app starts.
    }


    companion object {



            val MEDIA_TYPE_MARKDOWN = "text/x-markdown; charset=utf-8".toMediaType()
            val MEDIA_TYPE_JSON: MediaType = "application/json; charset=utf-8".toMediaType()

        private val parentJob = Job()
    }
}