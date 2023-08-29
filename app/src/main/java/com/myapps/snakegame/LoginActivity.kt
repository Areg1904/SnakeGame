package com.myapps.snakegame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private var emailLogin: EditText? = null
    private var passwordLogin: EditText? = null
    private var login: Button? = null
    private var createAccount: Button? = null

    private var authentication: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        emailLogin = findViewById(R.id.emailLogin)
        passwordLogin = findViewById(R.id.passwordLogin)
        login = findViewById(R.id.login)
        createAccount = findViewById(R.id.createAccount)

        authentication = FirebaseAuth.getInstance()

        login?.setOnClickListener {
            if (emailLogin?.text?.isEmpty()!! || passwordLogin?.text?.isEmpty()!!) {
                Toast.makeText(applicationContext, "Please, fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                authentication?.signInWithEmailAndPassword(emailLogin?.text.toString(), passwordLogin?.text.toString())?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val intent = Intent(applicationContext, GameActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        createAccount?.setOnClickListener {
            val intent = Intent(applicationContext, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        if (authentication?.currentUser != null) {
            val intent = Intent(applicationContext, GameActivity::class.java)
            startActivity(intent)
        }
    }
}