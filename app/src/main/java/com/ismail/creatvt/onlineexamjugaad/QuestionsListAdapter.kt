package com.ismail.creatvt.onlineexamjugaad

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ismail.creatvt.onlineexamjugaad.databinding.HeaderItemLayoutBinding
import com.ismail.creatvt.onlineexamjugaad.databinding.QuestionItemLayoutBinding

class QuestionsListAdapter(viewModel: QuestionsViewModel, lifecycleOwner: LifecycleOwner) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var questions: ArrayList<Any> = arrayListOf()
        set(value) {
            val oldItems = field
            field = value
            DiffUtil.calculateDiff(QuestionDiffCallback(oldItems, field)).dispatchUpdatesTo(this)
        }

    var lookup = object : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int {
            return if (questions[position] is String) 2 else 1
        }
    }

    init {
        viewModel.questions.observe(lifecycleOwner){
            questions = ArrayList(it)
        }
    }

    class QuestionViewHolder(val itemBinding: QuestionItemLayoutBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    class HeaderViewHolder(val itemBinding: HeaderItemLayoutBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder{
        return if(viewType == 1){
            HeaderViewHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.header_item_layout,
                    parent,
                    false
                )
            )
        } else{
            QuestionViewHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.question_item_layout,
                    parent,
                    false
                )
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        if(questions[position] is String){
            return 1
        }
        return 2
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(questions[position] is String){
            (holder as? HeaderViewHolder)?.itemBinding?.text = questions[position] as String
        } else if(questions[position] is Question){
            (holder as? QuestionViewHolder)?.itemBinding?.question = questions[position] as Question
        }
    }

    override fun getItemCount() = questions.size

}