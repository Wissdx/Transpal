package fr.esgi.transpal

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import fr.esgi.transpal.network.dto.ProfileRequest
import fr.esgi.transpal.network.repositories.UserRepository
import fr.esgi.transpal.viewmodel.UserViewModel
import fr.esgi.transpal.viewmodel.factories.UserViewModelFactory

class ProfileActivity : ComponentActivity() {

    private val userRepository = UserRepository()
    private val userViewModel: UserViewModel by viewModels {
        UserViewModelFactory(userRepository)
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
        val userId = sharedPreferences.getInt("user_id", 0)
        val token = sharedPreferences.getString("token", "") ?: ""
        val userNameString = sharedPreferences.getString("user_name", "UserName")
        val userMailString = sharedPreferences.getString("user_email", "UserMail")
        user_name_tv.text = userNameString
        name_et.setText(userNameString)
        email_et.setText(userMailString)

        userViewModel.user.observe(this, Observer { profile ->
            Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show()
        })

        update_btn.setOnClickListener {
            val name = name_et.text.toString()
            val email = email_et.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty()) {
                userViewModel.updateUser(token, userId, ProfileRequest(name, email))
                updateProfile(name, email)
                Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show()
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
    }

    private fun updateProfile(name: String, email: String) {
        val sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        user_name_tv.text = name
        name_et.setText(name)
        email_et.setText(email)
        val editor = sharedPreferences.edit()
        editor.putString("user_name", name)
        editor.putString("user_email", email)
        editor.apply()
    }
}