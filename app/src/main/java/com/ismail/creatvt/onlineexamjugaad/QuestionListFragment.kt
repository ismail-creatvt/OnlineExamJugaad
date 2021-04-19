package com.ismail.creatvt.onlineexamjugaad

import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.ismail.creatvt.onlineexamjugaad.databinding.FragmentQuestionListBinding

class QuestionListFragment : Fragment() {

    private var binding: FragmentQuestionListBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_question_list,
                container,
                false
            )
        binding?.lifecycleOwner = this
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val isCompleted = requireArguments().getBoolean(COMPLETED_KEY, false)

        val viewModel = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.AndroidViewModelFactory(
                requireContext().applicationContext as Application
            )
        ).get(QuestionsViewModel::class.java)

        binding?.adapter = QuestionsListAdapter(
            if(isCompleted) viewModel.completed else viewModel.remaining,
            isCompleted,
            this
        )

    }

}