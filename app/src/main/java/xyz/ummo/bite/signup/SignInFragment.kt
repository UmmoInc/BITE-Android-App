package xyz.ummo.bite.signup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.Task
import timber.log.Timber
import xyz.ummo.bite.R
import xyz.ummo.bite.databinding.FragmentSignInBinding
import xyz.ummo.bite.sharedpreferences.Prefs
import xyz.ummo.bite.utils.constants.Constants


class SignInFragment : Fragment() {

    private lateinit var _binding : FragmentSignInBinding
    private val binding get() = _binding
    private lateinit var      mGoogleSignInClient : GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
// Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN
        val serverClientId = Constants.SERVER_CLIENT_ID
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestScopes(Scope(Scopes.DRIVE_APPFOLDER))
            .requestServerAuthCode(serverClientId)
            .requestEmail()
            .build()
// Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        val preferences: Prefs = Prefs(requireContext())

 setGoogleSigninText()
        setgooglesignInButtonOnClick()
        setEmailsignInOnClick()
 navigationController()
        val view = binding.root

        //login onclickListener


       /* binding.loginBtn.setOnClickListener(View.OnClickListener {

            val intent : Intent = Intent(requireContext(),MainScreen::class.java)
            startActivity(intent)

        })*/


     return view

    }

    private fun setEmailsignInOnClick() {
        binding.loginBtn.setOnClickListener({
            moveTomainscreen()
        })
    }


    private fun setGoogleSigninText() {
        val TextViewWithin:View = binding.signInButton.getChildAt(0)
        (TextViewWithin as TextView).setText(getString(R.string.login_google))

    }

    private fun GooglesignIn(){
        val signInIntent: Intent = mGoogleSignInClient.getSignInIntent()
        startActivityForResult(signInIntent, Constants.RC_SIGN_IN)


    }

    private fun GooglesignOut(){
        mGoogleSignInClient.signOut()
            .addOnCompleteListener( {
                fun onComplete(task: Task<Void?>) {
                    Toast.makeText(requireContext(), "Signed Out", Toast.LENGTH_SHORT).show()
                }
            })
    }


    private fun setgooglesignInButtonOnClick(){

        binding.signInButton.setOnClickListener(View.OnClickListener {
            //Move to GoogleSignIn Fragment

            GooglesignIn()

        })
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

    private fun moveTomainscreen() {
        val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(
            R.id.NavHostFragment
        ) as NavHostFragment
        val   navController = navHostFragment.navController
        navController.navigate(R.id.action_signInFragment_to_mainscreen)
    }

    private fun navigationController(){
    // To register fragment

        binding.registerText.isClickable=true
        binding.registerText.setOnClickListener(View.OnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_signInFragment_to_userRegistrationFragment)
        })



// To forgot password fragment
        binding.forgotPasswordText.isClickable=true
        binding.forgotPasswordText.setOnClickListener(View.OnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_signInFragment_to_forgotPasswordFragment2)
        })



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

            sendAuthCodeToAsh(authCode)
            Timber.e("authcode->$authCode")
            Log.d("authcode:", "$authCode")
            // Signed in successfully, show authenticated UI.
            updateUI(account)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.d("GoogleSignIN", "signInResult:failed code=" + e.statusCode)
            updateUI(null)
        }
    }

    private fun sendAuthCodeToAsh(authCode: String?) {
        // HTTP POST REQUEST TO SEND AUTH CODE



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
}