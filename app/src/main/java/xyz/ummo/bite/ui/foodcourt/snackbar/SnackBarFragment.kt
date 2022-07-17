package xyz.ummo.bite.ui.foodcourt.snackbar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import xyz.ummo.bite.R
import xyz.ummo.bite.databinding.FragmentSnackBarBinding

class SnackBarFragment : Fragment() {
private lateinit var _binding: FragmentSnackBarBinding
private val binding get()= _binding

    private lateinit var rootView :View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

    _binding = FragmentSnackBarBinding.inflate(inflater,container,false )
        rootView = binding.root
        return rootView


    }


}