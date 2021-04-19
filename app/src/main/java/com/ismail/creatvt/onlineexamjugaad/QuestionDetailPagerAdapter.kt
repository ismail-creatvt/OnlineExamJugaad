package com.ismail.creatvt.onlineexamjugaad

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.viewpager2.adapter.FragmentStateAdapter

class QuestionDetailPagerAdapter(
    fragmentActivity: FragmentActivity,
    questionsLiveData: LiveData<List<Question>>,
    lifecycleOwner: LifecycleOwner,
    val onDataLoaded: ()->Unit
) :
    FragmentStateAdapter(fragmentActivity) {


    private var questions: ArrayList<Question> = arrayListOf()
        set(value) {
            val oldItems = field
            field = value
            DiffUtil.calculateDiff(QuestionDiffCallback(oldItems, field)).dispatchUpdatesTo(this)
        }

    init {
        questionsLiveData.observe(lifecycleOwner) {
            questions = ArrayList(it)
            if(!it.isNullOrEmpty()){
                onDataLoaded()
            }
        }
    }

    override fun getItemCount() = questions.size

    override fun createFragment(position: Int): Fragment {
        return QuestionDetailFragment().apply {
            arguments = bundleOf(ID_KEY to questions[position].id)
        }
    }
}