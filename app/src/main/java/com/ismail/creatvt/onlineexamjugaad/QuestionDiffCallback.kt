package com.ismail.creatvt.onlineexamjugaad

import androidx.recyclerview.widget.DiffUtil

class QuestionDiffCallback(val oldItems: List<Question>, val newItems: List<Question>) :
    DiffUtil.Callback() {
    override fun getOldListSize() = oldItems.size

    override fun getNewListSize() = newItems.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val (oldItem, newItem) = getItemPair(oldItemPosition, newItemPosition)
        return oldItem.id == newItem.id
    }

    private fun getItemPair(oldItemPosition: Int, newItemPosition: Int): Pair<Question, Question> {
        return Pair(oldItems[oldItemPosition], newItems[newItemPosition])
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val (oldItem, newItem) = getItemPair(oldItemPosition, newItemPosition)
        return oldItem.imageUrl == newItem.imageUrl && oldItem.answer == newItem.answer
    }
}