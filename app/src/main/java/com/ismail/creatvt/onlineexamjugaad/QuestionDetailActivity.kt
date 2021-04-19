package com.ismail.creatvt.onlineexamjugaad

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_question_detail.*

class QuestionDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setTheme(R.style.QuestionDetailTheme)
        setContentView(R.layout.activity_question_detail)

        val isComplete = intent.getBooleanExtra(COMPLETED_KEY, false)
        val position = intent.getIntExtra(POSITION_KEY, 0)

        val viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(
                application
            )
        ).get(QuestionsViewModel::class.java)

        var isFirstTime = true

        questions.adapter = QuestionDetailPagerAdapter(
            this,
            if (isComplete) viewModel.completed else viewModel.remaining,
            this
        ){
            if(isFirstTime){
                isFirstTime = false
                questions.currentItem = position
            }
        }

    }
}