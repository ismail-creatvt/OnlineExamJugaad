package com.ismail.creatvt.onlineexamjugaad

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class QuestionsViewModel(application: Application) : AndroidViewModel(application) {

    private val completed = MutableLiveData(listOf<Question>())

    private val remaining = MutableLiveData(listOf<Question>())

    val questions = completed.merge(remaining){
        done, rem ->
        val merged = arrayListOf<Any>()
        if(!done.isNullOrEmpty()){
            merged.add(application.getString(R.string.done))
            merged.addAll(done)
        }

        if(!rem.isNullOrEmpty()){
            merged.add(application.getString(R.string.remaining))
            merged.addAll(rem)
        }
        return@merge merged
    }

    init {
        Firebase.firestore.collection(QUESTION_COLLECTION)
            .whereNotEqualTo(ANSWER_KEY, NO_ANSWER)
            .addSnapshotListener { snapshot, e ->
                if (snapshot != null) {
                    completed.postValue(mapDocuments(snapshot.documents))
                }
            }

        Firebase.firestore.collection(QUESTION_COLLECTION)
            .whereEqualTo(ANSWER_KEY, NO_ANSWER)
            .addSnapshotListener { snapshot, e ->
                if (snapshot != null) {
                    remaining.postValue(mapDocuments(snapshot.documents))
                }
            }
    }

    private fun mapDocuments(documents: List<DocumentSnapshot>): List<Question> {
        return documents.map { doc ->
            Question(
                doc.id,
                (doc.data?.get(IMAGE_KEY) as? String?)?:"",
                (doc.data?.get(ANSWER_KEY) as? String?)?:"?"
            )
        }
    }
}