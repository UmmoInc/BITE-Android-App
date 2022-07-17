package xyz.ummo.bite.ui.foodcourt.menu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_food_menu.view.*
import xyz.ummo.bite.R
import xyz.ummo.bite.databinding.FragmentFoodMenuBinding
import xyz.ummo.bite.models.diner.FoodMenuModel
import xyz.ummo.bite.rvItems.FoodMenuItem


class FoodMenuFragment : Fragment() {
private lateinit var  _binding :FragmentFoodMenuBinding
private val binding get() = _binding
    private lateinit var rootView :View
    private lateinit var gAdapter: GroupAdapter<GroupieViewHolder>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        gAdapter = GroupAdapter()


    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

_binding = FragmentFoodMenuBinding.inflate(inflater,container, false)
rootView = binding.root

        //setup toolbar
setUpToolbar()

        /** Scaffolding the [recyclerView] **/
       val recyclerView = rootView.food_item_recycler_view
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = rootView.service_category_recycler_view.layoutManager
//        recyclerView.layoutManager = rootView.food_item_recycler_view.layoutManager
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        recyclerView.adapter = gAdapter

        getFoodMenu()


 return rootView


        // Inflate the layout for this fragment

    }

    private fun setUpToolbar() {
        val args = FoodMenuFragmentArgs.fromBundle(requireArguments())
        val string: String = args.jointname.toString()
        binding.foodMenuToolBar.title=string
    }


    private fun getFoodMenu() {

        binding.loadMenuProgressBar.visibility = View.GONE

        gAdapter.add(
            0, FoodMenuItem(
                FoodMenuModel(
                    "123",
                    "French Fries",
                    "The crispiest fries around Kwalu",
                    requireContext().resources.getDrawable(R.drawable.fries),
                    "E10-E50",
                    true
                ), requireContext()
            )
        )

        gAdapter.add(
            1, FoodMenuItem(
                FoodMenuModel(
                    "234",
                    "Fried Chicken",
                    "Sweet and sour chicken",
                    requireContext().resources.getDrawable(R.drawable.chicken_fried_rice),
                    "E25-E35",
                    true
                ), requireContext()
            )
        )

        gAdapter.add(
            2, FoodMenuItem(
                FoodMenuModel(
                    "345",
                    "Mexican Beef",
                    "Rich spicy Mexican been",
                    requireContext().resources.getDrawable(R.drawable.mexican_beef_stew),
                    "E25-E50",
                    true
                ), requireContext()
            )
        )

        gAdapter.add(
            3, FoodMenuItem(
                FoodMenuModel(
                    "345",
                    "Caesar's Salad",
                    "Caesar always munched on these greens",
                    requireContext().resources.getDrawable(R.drawable.greek_salad),
                    "E10-E15",
                    true
                ), requireContext()
            )
        )
    }



}