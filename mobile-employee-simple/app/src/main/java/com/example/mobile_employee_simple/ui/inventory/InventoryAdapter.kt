package com.example.mobile_employee_simple.ui.inventory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile_employee_simple.R

class InventoryAdapter(
    private val onItemClick: (InventoryItem) -> Unit
) : RecyclerView.Adapter<InventoryAdapter.ViewHolder>() {
    
    private var items = listOf<InventoryItem>()

    fun updateItems(newItems: List<InventoryItem>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_inventory, parent, false)
        return ViewHolder(view, onItemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    class ViewHolder(
        itemView: View,
        private val onItemClick: (InventoryItem) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        
        private val nameText: TextView = itemView.findViewById(R.id.item_name)
        private val skuText: TextView = itemView.findViewById(R.id.item_sku)
        private val quantityText: TextView = itemView.findViewById(R.id.item_quantity)
        private val locationText: TextView = itemView.findViewById(R.id.item_location)
        private val lastUpdatedText: TextView = itemView.findViewById(R.id.item_last_updated)

        fun bind(item: InventoryItem) {
            nameText.text = item.name
            skuText.text = "SKU: ${item.sku}"
            quantityText.text = "${item.quantity} ${item.unit}"
            locationText.text = "位置: ${item.location}"
            lastUpdatedText.text = "更新: ${item.lastUpdated}"
            
            itemView.setOnClickListener {
                onItemClick(item)
            }
        }
    }
} 