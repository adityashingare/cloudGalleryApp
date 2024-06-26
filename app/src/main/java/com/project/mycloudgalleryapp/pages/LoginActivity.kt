package com.project.mycloudgalleryapp.pages

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.project.mycloudgalleryapp.R
import com.project.mycloudgalleryapp.sharedpreferences.SharedPreferenceHelper

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var registerButton: TextView
    private lateinit var forgetPasswordButton: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)
        registerButton = findViewById(R.id.registerTextView)
        forgetPasswordButton = findViewById(R.id.forgotPasswordTextView)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginUser(email, password)
            } else {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }

        val user: FirebaseUser? = auth.currentUser
        if (user != null) {
            SharedPreferenceHelper.setLoggedIn(this, true)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        registerButton.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        forgetPasswordButton.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    SharedPreferenceHelper.setLoggedIn(this, true)
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Authentication Failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun goToRegister(view: android.view.View) {
        startActivity(Intent(this, RegisterActivity::class.java))
    }

    fun goToForgotPassword(view: android.view.View) {
        startActivity(Intent(this, ForgotPasswordActivity::class.java))
    }
}

