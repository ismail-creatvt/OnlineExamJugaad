package com.ismail.creatvt.onlineexamjugaad

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.hardware.Camera
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_take_picture.*
import kotlin.random.Random

class TakePictureActivity : AppCompatActivity() {
    private var mCamera: Camera? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setTheme(R.style.QuestionDetailTheme)
        setContentView(R.layout.activity_take_picture)

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            setUpCamera()
        } else{
            ActivityCompat.requestPermissions(this, arrayOf(
                Manifest.permission.CAMERA
            ), 10)
        }

        doneButton.setOnClickListener {
            mCamera?.takePicture(null, null, mPicture)
        }

    }

    private val mPicture = Camera.PictureCallback { data, _ ->
        if(data != null){
            saveToFirebase(data)
        }
    }

    private fun saveToFirebase(data: ByteArray) {
        val fileName = "${Random.nextInt()}_${System.currentTimeMillis()}.jpg"
        val fileRef = FirebaseStorage.getInstance().reference.child(fileName)

        progressBar.isVisible = true
        view.isVisible = true
        doneButton.isEnabled = false
        fileRef.putBytes(data.getScaledByteArray()).addOnSuccessListener { snapshot ->
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        var isGranted = true

        for(result in grantResults){
            if(result == PackageManager.PERMISSION_DENIED){
                isGranted = false
                break
            }
        }
        if(isGranted){
            setUpCamera()
        } else{
            Toast.makeText(this, "Camera permission denied!!", Toast.LENGTH_SHORT).show()
            finish()
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun setUpCamera() {
        mCamera = Camera.open()

        val preview = mCamera?.let {
            // Create our Preview view
            CameraPreview(this, it)
        }
        surfaceContainer.addView(preview)
    }
}