package xyz.ummo.bite.ui.foodcourt.menu.item

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import timber.log.Timber
import xyz.ummo.bite.R

import xyz.ummo.bite.databinding.FragmentFoodItemBottomSheetBinding
import xyz.ummo.bite.ui.foodcourt.cart.CartFragment


class FoodItemBottomSheet : BottomSheetDialogFragment() {

private lateinit var  _binding:FragmentFoodItemBottomSheetBinding
private val  binding get()=_binding!!
    private lateinit var  rootView: View



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding= FragmentFoodItemBottomSheetBinding.inflate(inflater,container,false)
        rootView= binding.root
        var addToCartButton = binding.addOrderToCardButton
        addToCartButton.setOnClickListener(View.OnClickListener {
            this.dismiss()
            Timber.e("CART BUTTON TAPPED!")

          //open cart fragment
            val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(
                R.id.NavHostFragment
            ) as NavHostFragment
            val   navController = navHostFragment.navController

            navController.navigate(R.id.action_foodMenuFragment_to_cartFragment)


        })
        return rootView
    }





    companion object {

        const val TAG = "FoodItemInfoBottomSheet"

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FoodItemBottomSheet().apply {
                arguments = Bundle().apply {
                }
              }

         }

      }