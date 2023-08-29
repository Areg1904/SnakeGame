package com.myapps.snakegame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    private var name: EditText? = null
    private var emailRegister: EditText? = null
    private var passwordRegister: EditText? = null
    private var register: Button? = null
    private var back: Button? = null

    private var authentication: FirebaseAuth? = null
    private val people = Firebase.firestore.collection("People")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        name = findViewById(R.id.name)
        emailRegister = findViewById(R.id.emailLogin)
        passwordRegister = findViewById(R.id.passwordLogin)
        register = findViewById(R.id.register)
        back = findViewById(R.id.back)

        authentication = FirebaseAuth.getInstance()

        register?.setOnClickListener {
            if (name?.text?.isEmpty()!! || emailRegister?.text?.isEmpty()!! || passwordRegister?.text?.isEmpty()!!) {
                Toast.makeText(applicationContext, "Please, fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                authentication?.createUserWithEmailAndPassword(emailRegister?.text.toString(), passwordRegister?.text.toString())?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        ////
                        val person = Person(name?.text.toString(), emailRegister?.text.toString(), 0)
                        people.document(authentication?.currentUser?.uid!!).set(person)
                        ////
                        val intent = Intent(applicationContext, GameActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        back?.setOnClickListener {
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}