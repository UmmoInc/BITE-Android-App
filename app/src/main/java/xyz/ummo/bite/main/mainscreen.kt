package xyz.ummo.bite.main

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import xyz.ummo.bite.R
import xyz.ummo.bite.databinding.FragmentMainscreenBinding
import xyz.ummo.bite.ui.DinersFragmentTabbed
import xyz.ummo.bite.ui.foodcourt.order_tracker.OrderTracker
import xyz.ummo.bite.ui.profile.ProfileFragment


class mainscreen : Fragment() {
private lateinit var _binding : FragmentMainscreenBinding
private val binding get() =_binding
    private lateinit var rootView :View






    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
    _binding = FragmentMainscreenBinding.inflate(inflater,container,false)
        rootView= binding.root
        supportFM = requireActivity().supportFragmentManager

 setUpbottomnav()
        navigationController()

        return rootView
    }

    private fun navigationController() {
        //To FoodMenu


    }

    private fun setUpbottomnav() {

        //set up bottom nav with nav controller




        binding.apply {


            val mOnNavigationItemSelectedListener =
                BottomNavigationView.OnNavigationItemSelectedListener { item ->


                    when (item.itemId) {

                        R.id.dinersFragmentTabbed -> {

                            val dinerFragment = DinersFragmentTabbed()
                            openFragment(dinerFragment)



                            return@OnNavigationItemSelectedListener true
                        }


                        R.id.orderTracker2 -> {
                            val orderTracker = OrderTracker()
                            openFragment(orderTracker)


                            return@OnNavigationItemSelectedListener true
                        }


                        R.id.profileFragment2 -> {
                            val profileFragment = ProfileFragment()
                            openFragment(profileFragment)



                            return@OnNavigationItemSelectedListener true
                        }
                    }
                    false
                }

            bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        }


    }
    private fun openFragment(fragment: Fragment) {

        val fragmentTransaction: FragmentTransaction = requireActivity().supportFragmentManager
            .beginTransaction()

        fragmentTransaction.replace(R.id.mainscreen_container, fragment)
        fragmentTransaction.commit()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //get the frame layout to commit

        val fragmentTransaction: FragmentTransaction = requireActivity().supportFragmentManager
            .beginTransaction()

        fragmentTransaction.replace(R.id.mainscreen_container, DinersFragmentTabbed())
        fragmentTransaction.commit()
    }
    companion object {

        // tags used to attach the fragments

        lateinit var supportFM: FragmentManager



    }

}