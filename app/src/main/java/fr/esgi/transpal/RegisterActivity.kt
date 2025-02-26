package fr.esgi.transpal

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import fr.esgi.transpal.network.dto.RegisterRequest
import fr.esgi.transpal.network.repositories.AuthRepository
import fr.esgi.transpal.viewmodel.RegisterViewModel
import fr.esgi.transpal.viewmodel.factories.RegisterViewModelFactory

class RegisterActivity : ComponentActivity() {

    private val authRepository = AuthRepository()
    private val registerViewModel: RegisterViewModel by viewModels {
        RegisterViewModelFactory(authRepository)
    }

    private lateinit var email_et: EditText
    private lateinit var name_et: EditText
    private lateinit var password_et: EditText
    private lateinit var register_btn: Button
    private lateinit var login_here_tv: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        email_et = findViewById(R.id.email_et)
        name_et = findViewById(R.id.name_et)
        password_et = findViewById(R.id.password_et)
        register_btn = findViewById(R.id.register_btn)
        login_here_tv = findViewById(R.id.login_here_tv)

        register_btn.setOnClickListener {
            val email = email_et.text.toString()
            val name = name_et.text.toString()
            val password = password_et.text.toString()

            if (email.isNotEmpty() && name.isNotEmpty() && password.isNotEmpty()) {
                register_btn.isEnabled = false
                registerViewModel.register(RegisterRequest(email, name, password))
            } else {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
            }
        }

        login_here_tv.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        registerViewModel.registerResponse.observe(this, Observer { result ->
            register_btn.isEnabled = true
            if (result.name.isNotEmpty()) {
                Toast.makeText(this, "Inscription réussie !", Toast.LENGTH_LONG).show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Inscription échouée", Toast.LENGTH_LONG).show()
            }
        })
    }
}