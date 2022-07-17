package xyz.ummo.bite.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import xyz.ummo.bite.R
import xyz.ummo.bite.databinding.FragmentSignInBinding


class SignInFragment : Fragment() {

    private lateinit var _binding : FragmentSignInBinding
    private val binding get() = _binding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)

 navigationController()
        val view = binding.root

        //login onclickListener


       /* binding.loginBtn.setOnClickListener(View.OnClickListener {

            val intent : Intent = Intent(requireContext(),MainScreen::class.java)
            startActivity(intent)

        })*/


     return view

    }
    private fun navigationController(){
    // To register fragment

        binding.registerText.isClickable=true
        binding.registerText.setOnClickListener(View.OnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_signInFragment_to_userRegistrationFragment)
        })




        //To menu home page

        binding.loginBtn.setOnClickListener(View.OnClickListener {

            Navigation.findNavController(it).navigate(R.id.action_signInFragment_to_mainscreen)
        })





// To forgot password fragment
        binding.forgotPasswordText.isClickable=true
        binding.forgotPasswordText.setOnClickListener(View.OnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_signInFragment_to_forgotPasswordFragment2)
        })
    }
}