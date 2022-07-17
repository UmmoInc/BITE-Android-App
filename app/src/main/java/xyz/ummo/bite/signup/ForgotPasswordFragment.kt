package xyz.ummo.bite.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import xyz.ummo.bite.R


import xyz.ummo.bite.databinding.FragmentForgotPasswordBinding


class ForgotPasswordFragment : Fragment() {
    private lateinit var _binding : FragmentForgotPasswordBinding
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding= FragmentForgotPasswordBinding.inflate(inflater,container,false)

            val view= binding.root
        navigationController()


        return view



    }

private fun navigationController(){
 binding.appBarForgotPassword.setNavigationOnClickListener(View.OnClickListener {
     Navigation.findNavController(it).navigate(R.id.action_forgotPasswordFragment_to_signInFragment)
  })

    //To reset password
    binding.submitBtnForgotpassword.setOnClickListener(View.OnClickListener {
        Navigation.findNavController(it).navigate(R.id.action_forgotPasswordFragment_to_resetPasswordFragment)
    })

   }
  }