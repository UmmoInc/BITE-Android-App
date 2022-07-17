package xyz.ummo.bite.rvItems

import android.content.Context
import android.os.Bundle
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation

import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import xyz.ummo.bite.R
import xyz.ummo.bite.main.mainscreen
import xyz.ummo.bite.main.mainscreenDirections
import xyz.ummo.bite.models.diner.FoodJointModel
import xyz.ummo.bite.ui.foodcourt.menu.FoodMenuFragment


class FoodJointItem(
    private val foodJointModel: FoodJointModel,
    val context: Context
) : Item<GroupieViewHolder>() {

    private val bundle = Bundle()


    override fun getLayout(): Int {
        return R.layout.food_joint_card
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.findViewById<TextView>(R.id.food_joint_title_text_view).text = foodJointModel.foodJointName
        viewHolder.itemView.findViewById<TextView>(R.id.food_joint_description_text_view).text =
            foodJointModel.foodJointDescription


        viewHolder.itemView.findViewById<ImageView>(R.id.food_joint_image_view).setImageDrawable(context.resources.getDrawable(R.drawable.restaurant))

        viewHolder.itemView.findViewById<ImageView>(R.id.food_joint_image_view).setOnClickListener {


            //navigate to menu fragrament NB
            Navigation.findNavController(it).navigate(mainscreenDirections.actionMainscreenToSplashScreenFragment(
                foodJointModel.foodJointName
            ))


        }
        viewHolder.itemView.findViewById<RelativeLayout>(R.id.food_joint_card_bottom_layout).setOnClickListener { openMenuFragment() }
    }

    private fun openMenuFragment() {

        //Navigate to menu Fragment

        /*
        val fragmentActivity = context as FragmentActivity
        val fragmentManager = fragmentActivity.supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val foodMenuFragment = FoodMenuFragment()
        fragmentTransaction.replace(R.id.frame, foodMenuFragment)
        fragmentTransaction.commit() */
    }
}