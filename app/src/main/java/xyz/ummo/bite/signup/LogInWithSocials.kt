package xyz.ummo.bite.signup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope

import com.google.android.gms.tasks.Task
import timber.log.Timber
import xyz.ummo.bite.R
import xyz.ummo.bite.databinding.FragmentLogInWithSocialsBinding
import xyz.ummo.bite.utils.constants.Constants.Companion.RC_SIGN_IN
import xyz.ummo.bite.utils.constants.Constants.Companion.SERVER_CLIENT_ID


class LogInWithSocials : Fragment() {
private lateinit var  _binding:FragmentLogInWithSocialsBinding
private val binding get() =_binding
    private lateinit var  rootView: View
private lateinit var      mGoogleSignInClient :GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
// Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN
        val serverClientId =  SERVER_CLIENT_ID
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
        // Inflate the layout for this fragment
       _binding = FragmentLogInWithSocialsBinding.inflate(inflater,container,false)
        rootView = binding.root
        setgooglesignInButton()
        setsignoutButtonOnClick()
        return rootView
    }
    private fun GooglesignOut(){
        mGoogleSignInClient.signOut()
            .addOnCompleteListener( {
                fun onComplete(task: Task<Void?>) {
                    Toast.makeText(requireContext(), "Signed Out", Toast.LENGTH_SHORT).show()
                }
            })

    }
    private fun setsignoutButtonOnClick() {
        binding.signOutButton.setOnClickListener(View.OnClickListener {
            GooglesignOut()
        })
    }

    private fun GooglesignIn(){
        val signInIntent: Intent = mGoogleSignInClient.getSignInIntent()
        startActivityForResult(signInIntent,  RC_SIGN_IN)


    }

    private fun updateUI(account: GoogleSignInAccount?) {
// move user to main screen -> do this later

        // for now  make a Toast
if(account !=null) {
    Toast.makeText(requireActivity(), "User Already Signed In To Google", Toast.LENGTH_SHORT).show()
binding.signOutButton.visibility= View.GONE

    }
else {
binding.signOutButton.visibility=View.VISIBLE

}

    }
    private fun setgooglesignInButton() {
        binding.signInButton.setOnClickListener(View.OnClickListener {
           //Move to GoogleSignIn Fragment

GooglesignIn()

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
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


    override fun onStart(){
         super.onStart()
         // Check for existing Google Sign In account, if the user is already signed in
// the GoogleSignInAccount will be non-null.
         // Check for existing Google Sign In account, if the user is already signed in
// the GoogleSignInAccount will be non-null.
         val account = GoogleSignIn.getLastSignedInAccount(requireContext())
         updateUI(account)
//Note: If you need to detect changes to a user's auth state that happen outside your app,
     //         such as access token or ID token revocation, or to perform cross-device sign-in,
     //         you might also call ***GoogleSignInClient.silentSignIn**** when your app starts.
         
     }

 }

