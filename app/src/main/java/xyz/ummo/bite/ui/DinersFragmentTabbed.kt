package xyz.ummo.bite.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.appbar.MaterialToolbar
import xyz.ummo.bite.R

import xyz.ummo.bite.databinding.FragmentDinersTabbedBinding
import xyz.ummo.bite.adapters.PagesViewPagerAdapter
import xyz.ummo.bite.ui.foodcourt.FoodCourtFragment
import xyz.ummo.bite.ui.foodcourt.drinkingwell.DrinkingWellFragment
import xyz.ummo.bite.ui.foodcourt.snackbar.SnackBarFragment

class DinersFragmentTabbed : Fragment() {
    private lateinit var toolbar: MaterialToolbar
    private lateinit var _binding:FragmentDinersTabbedBinding
    private val binding get() = _binding!!
    private lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
_binding= FragmentDinersTabbedBinding.inflate(inflater,container,false)
    rootView = binding.root
        /** Setting up Diner Tabs **/
        setupDinerTabs()
 return rootView
    }


    /*
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /** Hiding the MainActivity toolbar **/
        toolbar = requireActivity().findViewById(R.id.toolbar)
        toolbar.visibility = View.GONE

    }
*/
    private fun setupDinerTabs() {
        val dineAdapter = PagesViewPagerAdapter(childFragmentManager)

        dineAdapter.addFragment(FoodCourtFragment(), "Food-Court")
        dineAdapter.addFragment(SnackBarFragment(), "Snack-Bar")
        dineAdapter.addFragment(DrinkingWellFragment(), "Drinking-Well")

        binding.dineTabsViewPager.adapter = dineAdapter
        binding.dineTabLayout.setupWithViewPager(binding.dineTabsViewPager)
        binding.dineTabLayout.getTabAt(0)!!.setIcon(R.drawable.ic_fastfood_24)
        binding.dineTabLayout.getTabAt(1)!!.setIcon(R.drawable.ic_cookie_24)
        binding.dineTabLayout.getTabAt(2)!!.setIcon(R.drawable.ic_wine_bar_24)
    }


  }


