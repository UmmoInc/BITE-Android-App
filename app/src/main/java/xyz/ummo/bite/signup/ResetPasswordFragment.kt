package xyz.ummo.bite.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation

import xyz.ummo.bite.R
import xyz.ummo.bite.databinding.FragmentResetPasswordBinding


class ResetPasswordFragment : Fragment() {

    private lateinit var _binding : FragmentResetPasswordBinding
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding= FragmentResetPasswordBinding.inflate(inflater,container,false)

        navigationController()

        val view = binding.root

        return view
    }
 private fun navigationController(){
    binding.appBarResetPassword.setNavigationOnClickListener(View.OnClickListener {

        Navigation.findNavController(it).navigate(R.id.action_resetPasswordFragment_to_forgotPasswordFragment)
         })

      }

   }