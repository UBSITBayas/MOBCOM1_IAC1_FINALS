package edu.mobcom.witchinhour

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import edu.mobcom.witchinhour.databinding.ActivityMainMenuBinding

class MainMenuActivity : AppCompatActivity() {

private lateinit var binding: ActivityMainMenuBinding

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val binding = ActivityMainMenuBinding.inflate(layoutInflater)
    setContentView(binding.root)

        binding.btnProfileMainMenuPage.setOnClickListener {
            val iProf = Intent(this@MainMenuActivity, ProfileActivity::class.java)
            startActivity(iProf)
        }

        binding.btnEventSchedulerMainMenu.setOnClickListener {
            Toast.makeText(this, "He he he ... T_T", Toast.LENGTH_LONG).show()
        }
    }
}