package xyz.ummo.bite.ui.foodcourt

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import xyz.ummo.bite.R
import xyz.ummo.bite.databinding.FragmentFoodCourtBinding
import xyz.ummo.bite.models.diner.FoodJointModel
import xyz.ummo.bite.rvItems.FoodJointItem

class FoodCourtFragment : Fragment() {


private lateinit var _binding:FragmentFoodCourtBinding
private val binding get() = _binding!!
    private lateinit var rootView:View
    private lateinit var gAdapter: GroupAdapter<GroupieViewHolder>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        gAdapter = GroupAdapter()

        getFoodJoints()


    }




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
_binding= FragmentFoodCourtBinding.inflate(inflater,container,false)
        rootView= binding.root

        /** Scaffolding the [recyclerView] **/

     val   recyclerView = binding.foodCourtRecyclerView
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = rootView.service_category_recycler_view.layoutManager
        recyclerView.layoutManager = binding.foodCourtRecyclerView.layoutManager
        recyclerView.adapter = gAdapter
        return rootView
    }

    private fun getFoodJoints() {
        gAdapter.add(
            0, FoodJointItem(
                FoodJointModel(
                    "1234",
                    "Maphanga's Fast-Food",
                    "Come enjoy the best French Fries in Matsapha",
                    resources.getDrawable(R.drawable.restaurant),
                    8,
                    8
                ), requireContext()
            )
        )

        gAdapter.add(
            1, FoodJointItem(
                FoodJointModel(
                    "2345",
                    "Fifty's Fast-Food",
                    "Come shawty, it's your lunch time",
                    resources.getDrawable(R.drawable.restaurant),
                    8,
                    8
                ), requireContext()
            )
        )
    }
}