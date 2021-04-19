package com.ismail.creatvt.onlineexamjugaad

import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_add_question.*
import java.io.ByteArrayOutputStream


class AddQuestionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_add_question)

        val storage = FirebaseStorage.getInstance()

        setTitle(R.string.share_question)
        Log.d("GotIntent", "action: ${intent?.action}, type: ${intent?.type}")
        if(intent?.action == Intent.ACTION_SEND && intent?.type?.startsWith("image/") == true) {
            (intent.getParcelableExtra<Parcelable>(Intent.EXTRA_STREAM) as? Uri)?.let {
                val fileName = getFileName(it)
                image.setImageURI(it)

                done_button.setOnClickListener {
                    val fileRef = storage.reference.child(fileName)
                    val bitmap = (image.drawable as BitmapDrawable).bitmap
                    val width = 480
                    val height = ((bitmap.height.toFloat() / bitmap.width.toFloat()) * 1000f).toInt()
                    val finalBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true)

                    val baos = ByteArrayOutputStream()
                    finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                    val data = baos.toByteArray()

                    loader_group.isVisible = true
                    fileRef.putBytes(data).addOnSuccessListener { snapshot ->
                        snapshot.storage.downloadUrl.addOnSuccessListener { downloadUrl ->
                            Firebase.firestore.collection(QUESTION_COLLECTION)
                                .add(
                                    hashMapOf(
                                        ANSWER_KEY to NO_ANSWER,
                                        IMAGE_KEY to downloadUrl.toString()
                                    )
                                )
                            finish()
                        }.addOnFailureListener {
                            Toast.makeText(this, "Upload failed", Toast.LENGTH_SHORT).show()
                            finish()
                        }

                    }.addOnFailureListener{
                        Toast.makeText(this, "Upload failed", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
        } else{
            finish()
        }

    }

    fun getFileName(uri: Uri): String {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor: Cursor? = contentResolver.query(uri, null, null, null, null)
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            } finally {
                cursor?.close()
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result!!.lastIndexOf('/')
            if (cut != -1) {
                result = result.substring(cut + 1)
            }
        }
        return result
    }
}