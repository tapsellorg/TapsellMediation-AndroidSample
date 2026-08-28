package ir.tapsell.sample.nativelist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ir.tapsell.sample.databinding.ItemNativeListBinding

internal class FoodsAdapter() : ListAdapter<FoodItem, RecyclerView.ViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        FoodItemViewHolder(
            ItemNativeListBinding.inflate(
                LayoutInflater.from(viewGroup.context), viewGroup, false
            )
        )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as FoodItemViewHolder).bind(getItem(position))
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FoodItem>() {
            override fun areItemsTheSame(oldItem: FoodItem, newItem: FoodItem): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: FoodItem, newItem: FoodItem): Boolean =
                oldItem == newItem
        }
    }

    class FoodItemViewHolder(
        private val binding: ItemNativeListBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(food: FoodItem) {
            with(binding) {
                foodItemImage.setImageResource(
                    itemView.resources.getIdentifier(
                        food.imageName, "drawable", itemView.context.packageName
                    )
                )
                foodItemName.text = "${food.id} ${food.name}"
                foodItemPrice.text = food.price
                foodItemCategory.text = food.category
                foodItemDescription.text = food.description
            }
        }
    }
}