package fr.esgi.transpal

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import fr.esgi.transpal.network.dto.ProfileRequest
import fr.esgi.transpal.network.repositories.ProfileRepository
import fr.esgi.transpal.viewmodel.ProfileViewModel
import fr.esgi.transpal.viewmodel.factories.ProfileViewModelFactory

class ProfileActivity : ComponentActivity() {

    private val profileRepository = ProfileRepository()
    private val profileViewModel: ProfileViewModel by viewModels {
        ProfileViewModelFactory(profileRepository)
    }

    private lateinit var name_et: EditText
    private lateinit var email_et: EditText
    private lateinit var update_btn: Button
    private lateinit var back_here_tv: TextView
    private lateinit var error_tv: TextView
    private lateinit var user_name_tv: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        name_et = findViewById(R.id.name_et)
        email_et = findViewById(R.id.email_et)
        update_btn = findViewById(R.id.update_btn)
        back_here_tv = findViewById(R.id.back_here_tv)
        error_tv = findViewById(R.id.error_tv)
        user_name_tv = findViewById(R.id.user_name_tv)

        val sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val userNameString = sharedPreferences.getString("user_name", "UserName")
        val userMailString = sharedPreferences.getString("user_mail", "UserMail")
        user_name_tv.text = userNameString

        profileViewModel.profile.observe(this, Observer { profile ->
            name_et.setText(userNameString)
            email_et.setText(userMailString)
        })

        update_btn.setOnClickListener {
            val name = name_et.text.toString()
            val email = email_et.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty()) {
                profileViewModel.updateProfile(ProfileRequest(name, email))
            } else {
                error_tv.visibility = TextView.VISIBLE
                error_tv.text = "Veuillez remplir tous les champs"
            }
        }

        back_here_tv.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        profileViewModel.loadProfile()
    }
}