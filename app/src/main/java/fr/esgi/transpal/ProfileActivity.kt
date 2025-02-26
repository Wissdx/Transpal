package fr.esgi.transpal

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        name_et = findViewById(R.id.name_et)
        email_et = findViewById(R.id.email_et)
        update_btn = findViewById(R.id.update_btn)

        profileViewModel.profile.observe(this, Observer { profile ->
            name_et.setText(profile.name)
            email_et.setText(profile.email)
        })

        update_btn.setOnClickListener {
            val name = name_et.text.toString()
            val email = email_et.text.toString()
            profileViewModel.updateProfile(ProfileRequest(name, email))
        }

        profileViewModel.loadProfile()
    }
}