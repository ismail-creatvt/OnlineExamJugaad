package com.ismail.creatvt.onlineexamjugaad

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ismail.creatvt.onlineexamjugaad.databinding.FragmentQuestionDetailBinding
import kotlinx.android.synthetic.main.fragment_question_detail.*

class QuestionDetailFragment : Fragment() {

    private var itemBinding: FragmentQuestionDetailBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        itemBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_question_detail,
            container,
            false
        )
        itemBinding?.lifecycleOwner = this
        return itemBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val questionId = requireArguments().getString(ID_KEY) ?: ""

        Firebase.firestore
            .collection(QUESTION_COLLECTION)
            .document(questionId)
            .get().addOnSuccessListener {
                val question = Question(
                    it.id,
                    (it.data?.get(IMAGE_KEY) as? String?) ?: "",
                    (it.data?.get(ANSWER_KEY) as? String?) ?: "?"
                )
                itemBinding?.question = question
            }

        update_button.setOnClickListener {
            val answer = answer_field.text.toString()
            Firebase.firestore.collection(QUESTION_COLLECTION)
                .document(questionId)
                .update(mapOf(ANSWER_KEY to answer)).addOnSuccessListener {
                    Toast.makeText(requireContext(), "Answer Updated!", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(requireContext(), "Update failed!", Toast.LENGTH_SHORT).show()
                }
        }
    }
}