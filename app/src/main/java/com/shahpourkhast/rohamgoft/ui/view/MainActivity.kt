package com.shahpourkhast.rohamgoft.ui.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.shahpourkhast.rohamgoft.R
import com.shahpourkhast.rohamgoft.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bottomNav()


    }

    private fun bottomNav() {

        binding.bottomNav.setItemSelected(R.id.home, true)

        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.fragmentContainerView, PostsFragment())
        transaction.commit()

        binding.bottomNav.setOnItemSelectedListener { id ->

            when (id) {

                R.id.home -> {
                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fragmentContainerView, PostsFragment())
                    transaction.commit()
                }

                R.id.addPost -> {
                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fragmentContainerView, AddPostFragment())
                    transaction.commit()
                }

            }

        }
    }

}