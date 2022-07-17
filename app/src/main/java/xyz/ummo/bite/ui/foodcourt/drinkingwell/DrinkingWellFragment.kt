package xyz.ummo.bite.ui.foodcourt.drinkingwell

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import xyz.ummo.bite.R
import xyz.ummo.bite.databinding.FragmentDrinkingWellBinding

class DrinkingWellFragment : Fragment() {
    private lateinit var _binding: FragmentDrinkingWellBinding
    private val binding get()= _binding
    private lateinit var rootView:View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding= FragmentDrinkingWellBinding.inflate(inflater,container,false )
        rootView=binding.root
        return rootView

        }


}