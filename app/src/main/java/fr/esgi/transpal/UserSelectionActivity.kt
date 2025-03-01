package fr.esgi.transpal

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import fr.esgi.transpal.network.repositories.UserRepository
import fr.esgi.transpal.viewmodel.UserViewModel
import fr.esgi.transpal.viewmodel.factories.UserViewModelFactory

class UserSelectionActivity : AppCompatActivity() {

    private val userRepository = UserRepository()

    private lateinit var userViewModel: UserViewModel
    private lateinit var userListView: ListView
    private lateinit var searchEditText: EditText
    private lateinit var backTextView: TextView
    private lateinit var backImageView: ImageView
    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_selection)

        userListView = findViewById(R.id.user_list_view)
        searchEditText = findViewById(R.id.search_edit_text)
        backTextView = findViewById(R.id.back_here_tv)
        backImageView = findViewById(R.id.back_icon)


        userViewModel = ViewModelProvider(this, UserViewModelFactory(userRepository)).get(UserViewModel::class.java)

        userViewModel.users.observe(this, Observer { users ->
            adapter = UserAdapter(this, users)
            userListView.adapter = adapter
        })

        userViewModel.error.observe(this, Observer { error ->
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        })

        val sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("auth_token", "") ?: ""

        userViewModel.getUsers(token)

        userListView.setOnItemClickListener { _, _, position, _ ->
            val selectedUser = userViewModel.users.value?.get(position)
            val intent = Intent()
            intent.putExtra("selectedUserId", selectedUser?.id)
            intent.putExtra("selectedUserName", selectedUser?.name)
            intent.putExtra("selectedUserEmail", selectedUser?.email)
            setResult(RESULT_OK, intent)
            finish()
        }

        backTextView.setOnClickListener { finish() }

        backImageView.setOnClickListener { finish() }

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.filter.filter(s)
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }
}