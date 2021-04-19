package com.ismail.creatvt.onlineexamjugaad

import androidx.recyclerview.widget.DiffUtil

class QuestionDiffCallback(val oldItems: List<Any>, val newItems: List<Any>) :
    DiffUtil.Callback() {
    override fun getOldListSize() = oldItems.size

    override fun getNewListSize() = newItems.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val (oldItem, newItem) = getItemPair(oldItemPosition, newItemPosition)
        if (oldItem is String && newItem == String) {
            return oldItem == newItem
        } else if (oldItem is Question && newItem is Question) {
            return oldItem.id == newItem.id
        }
        return false
    }

    private fun getItemPair(oldItemPosition: Int, newItemPosition: Int): Pair<Any, Any> {
        return Pair(oldItems[oldItemPosition], newItems[newItemPosition])
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val (oldItem, newItem) = getItemPair(oldItemPosition, newItemPosition)
        if (oldItem is String && newItem == String) {
            return oldItem == newItem
        } else if (oldItem is Question && newItem is Question) {
            return oldItem.imageUrl == newItem.imageUrl && oldItem.answer == newItem.answer
        }
        return false
    }
}