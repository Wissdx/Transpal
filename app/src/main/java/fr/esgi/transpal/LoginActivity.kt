package fr.esgi.transpal

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import fr.esgi.transpal.network.dto.LoginRequest
import fr.esgi.transpal.network.repositories.AuthRepository
import fr.esgi.transpal.viewmodel.AuthViewModel
import fr.esgi.transpal.viewmodel.factories.AuthViewModelFactory

class LoginActivity : ComponentActivity() {

    private val authRepository = AuthRepository();
    private val authViewModel: AuthViewModel by viewModels {
        AuthViewModelFactory(authRepository)
    }

    private lateinit var email_et : EditText;
    private lateinit var password_et : EditText;
    private lateinit var login_btn : Button;
    private lateinit var error_tv : TextView;
    private lateinit var register_here_tv : TextView;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge();
        setContentView(R.layout.activity_login);

        email_et = findViewById(R.id.email_et);
        password_et = findViewById(R.id.password_et);
        login_btn = findViewById(R.id.login_btn);
        error_tv = findViewById(R.id.error_tv);
        register_here_tv = findViewById(R.id.register_here_tv);


        login_btn.setOnClickListener {
            val email = email_et.text.toString();
            val password = password_et.text.toString();

            if (email.isNotEmpty() && password.isNotEmpty()) {
                login_btn.isEnabled = false;
                authViewModel.login(LoginRequest(email, password));
            } else {
                error_tv.visibility = TextView.VISIBLE;
                error_tv.text = "Veuillez remplir tous les champs";
            }
        }

        register_here_tv.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        authViewModel.loginResponse.observe(this, Observer { result ->
            login_btn.isEnabled = true
            if (result.token.isNotEmpty()) {
                val sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                with(sharedPreferences.edit()) {
                    putString("auth_token", result.token)
                    putInt("user_id", result.user.id)
                    putString("user_email", result.user.email)
                    putString("user_name", result.user.name)
                    putString("user_created_at", result.user.createdAt)
                    putString("user_updated_at", result.user.updatedAt)
                    apply()
                }
                Toast.makeText(this, "Connexion réussie !", Toast.LENGTH_LONG).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Connexion échouée : ${result.message}", Toast.LENGTH_LONG).show()
            }
        })

    }
}
