package xyz.ummo.bite.rvItems

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

import com.squareup.picasso.Picasso

import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

import xyz.ummo.bite.R
import xyz.ummo.bite.main.mainscreen.Companion.supportFM
import xyz.ummo.bite.ui.foodcourt.menu.item.FoodItemBottomSheet
import xyz.ummo.bite.models.diner.FoodMenuModel
import xyz.ummo.bite.utils.constants.Constants.Companion.FOOD_ITEM_OBJECT

class FoodMenuItem(
    private val foodMenuModel: FoodMenuModel,
    val context: Context
) : Item<GroupieViewHolder>(){



    private lateinit var picasso: Picasso

    override fun getLayout(): Int {
        return R.layout.food_menu_card
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        /** Setting up [Picasso] **/
        picasso = Picasso.get()

        viewHolder.itemView.findViewById<TextView>(R.id.food_item_title_text_view).text = foodMenuModel.menuItemName
        viewHolder.itemView.findViewById<TextView>(R.id.food_item_description_text_view).text = foodMenuModel.menuItemDescription
        viewHolder.itemView.findViewById<ImageView>(R.id.food_item_image_view).setImageDrawable(foodMenuModel.menuItemImage)
//        picasso.load(foodMenuModel.menuItemImage).into(viewHolder.itemView.food_item_image_view)
        viewHolder.itemView.findViewById<TextView>(R.id.food_item_price_range_text_view).text = foodMenuModel.menuItemPriceRange

        /** Handling all click instances on the card **/
        viewHolder.itemView.findViewById<ImageView>(R.id.food_item_image_view).setOnClickListener { launchFoodItemOrderBottomSheet() }
        viewHolder.itemView.findViewById<TextView>(R.id.food_item_title_text_view).setOnClickListener { launchFoodItemOrderBottomSheet() }
        viewHolder.itemView.findViewById<TextView>(R.id.food_item_description_text_view).setOnClickListener { launchFoodItemOrderBottomSheet() }
        viewHolder.itemView.findViewById<TextView>(R.id.food_item_price_range_text_view).setOnClickListener { launchFoodItemOrderBottomSheet() }
        viewHolder.itemView.findViewById<Button>(R.id.add_to_cart_button).setOnClickListener { launchFoodItemOrderBottomSheet() }
    }

    private fun launchFoodItemOrderBottomSheet() {
        val foodItemBundle = Bundle()
        foodItemBundle.putSerializable(FOOD_ITEM_OBJECT, foodMenuModel)
        val foodItemOrderBottomSheet = FoodItemBottomSheet()
        foodItemOrderBottomSheet.show(supportFM, FoodItemBottomSheet.TAG)
    }



 }

