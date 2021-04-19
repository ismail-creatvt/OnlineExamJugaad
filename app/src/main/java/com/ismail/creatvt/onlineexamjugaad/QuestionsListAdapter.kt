package com.ismail.creatvt.onlineexamjugaad

import android.content.Intent
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

class QuestionsListAdapter(
    questionsLiveData: LiveData<List<Question>>,
    val isCompleted: Boolean,
    lifecycleOwner: LifecycleOwner
) :
    RecyclerView.Adapter<QuestionsListAdapter.QuestionViewHolder>() {

    private var questions: ArrayList<Question> = arrayListOf()
        set(value) {
            val oldItems = field
            field = value
            DiffUtil.calculateDiff(QuestionDiffCallback(oldItems, field)).dispatchUpdatesTo(this)
        }

    init {
        questionsLiveData.observe(lifecycleOwner) {
            questions = ArrayList(it)
        }
    }

    class QuestionViewHolder(val itemBinding: QuestionItemLayoutBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = QuestionViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.question_item_layout,
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        holder.itemBinding.question = questions[position]
        holder.itemBinding.questionCard.setOnClickListener {
            it.context.startActivity(
                Intent(
                    it.context,
                    QuestionDetailActivity::class.java
                ).apply {
                    putExtra(COMPLETED_KEY, isCompleted)
                    putExtra(POSITION_KEY, holder.adapterPosition)
                }
            )
        }
    }

    override fun getItemCount() = questions.size

}