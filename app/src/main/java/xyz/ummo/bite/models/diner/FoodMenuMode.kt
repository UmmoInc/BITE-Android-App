package xyz.ummo.bite.models.diner

import android.graphics.drawable.Drawable
import java.io.Serializable

data class FoodMenuModel(
    var menuItemId: String,
    var menuItemName: String,
    var menuItemDescription: String,
    var menuItemImage: Drawable,
    var menuItemPriceRange: String,
    /*var menuItemExtras: ArrayList<String>,
    var menuItemAddOns: ArrayList<String>,*/
    var menuItemLiked: Boolean
) : Serializable