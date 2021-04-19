package com.ismail.creatvt.onlineexamjugaad

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ismail.creatvt.onlineexamjugaad.databinding.ActivityMainBinding

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setTheme(R.style.Theme_OnlineExamJugaad)
        super.onCreate(savedInstanceState)
        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        setSupportActionBar(binding.toolbar)
        setTitle(R.string.app_name)

        val remaining = QuestionListFragment()
        remaining.arguments = bundleOf(COMPLETED_KEY to false)
        val completed = QuestionListFragment()
        completed.arguments = bundleOf(COMPLETED_KEY to true)

        binding.takePicture.setOnClickListener {
            startActivity(Intent(this, TakePictureActivity::class.java))
        }

        binding.viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = 2

            override fun createFragment(position: Int): Fragment {
                if(position == 0) return completed
                return remaining
            }
        }

        TabLayoutMediator(binding.tabs, binding.viewPager){
            tab, position ->
            if(position == 0){
                tab.text = getString(R.string.done)
            } else{
                tab.text = getString(R.string.remaining)
            }
        }.attach()
    }
}