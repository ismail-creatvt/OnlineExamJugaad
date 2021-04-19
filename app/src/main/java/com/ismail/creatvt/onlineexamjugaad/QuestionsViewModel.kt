package com.ismail.creatvt.onlineexamjugaad

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class QuestionsViewModel(application: Application) : AndroidViewModel(application) {

    val completed = MutableLiveData(listOf<Question>())

    val remaining = MutableLiveData(listOf<Question>())

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