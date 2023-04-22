package com.example.trees

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

private const val TAG = "Main activity"
class MainActivity : AppCompatActivity() {
    val CURRENT_FRAGMENT_BUNDLE_KEY = "current fragment bundle key "
    var currentFragmentMap = "Map"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        currentFragmentMap = savedInstanceState?.getString(CURRENT_FRAGMENT_BUNDLE_KEY)?: "Map"
        showFragment("MAP")
        val bottomNavigationMenu = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationMenu.setOnClickListener{menuItem ->
            when (menuItem.id){
                R.id.show_map ->{
                    showFragment("MAP")
                    true
                }
                R.id.show_list ->{
                    showFragment("MAP")
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

    private fun showFragment(tag: String ){
        if (supportFragmentManager.findFragmentByTag(tag)== null){
            val transaction = supportFragmentManager.beginTransaction()
            when (tag){
                "MAP" -> transaction.replace(R.id.fragmentContainerView,tree_map_fragment.newInstance())
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(CURRENT_FRAGMENT_BUNDLE_KEY, currentFragmentMap)
    }
}



