package com.newcode.stream

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var dbHelper: SQLiteOpenHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        dbHelper = SQLiteOpenHelper(this)

        val emailEditText: EditText = findViewById(R.id.email)
        val passwordEditText: EditText = findViewById(R.id.password)
        val loginButton: Button = findViewById(R.id.login_button)
        val signupTextView: TextView = findViewById(R.id.signup_text)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            // Validar campos vacíos
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validar formato del correo electrónico
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Por favor, ingresa un correo electrónico válido.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Intentar iniciar sesión
            try {
                if (dbHelper.userExists(email, password)) {
                    // Inicio de sesión exitoso
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // Mostrar error
                    Toast.makeText(this, "Correo electrónico o contraseña incorrectos.", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                // Manejar excepciones inesperadas
                Toast.makeText(this, "Ocurrió un error inesperado. Intenta de nuevo.", Toast.LENGTH_LONG).show()
            }
        }

        signupTextView.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}