package com.pxl.parkingApp.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.pxl.parkingApp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class LoginActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var button: Button
    private var currentUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        mAuth = FirebaseAuth.getInstance()

        if(mAuth.currentUser != null){
            startActivity(Intent(this, HomeActivity::class.java))
        }

        button = findViewById<View>(R.id.button_login) as Button

        button.setOnClickListener{
            login()
        }
    }

    override fun onStart() {
        super.onStart()

        currentUser = mAuth?.currentUser
    }

    private fun login(){
        val emailTxt = findViewById<View>(R.id.et_email) as EditText
        val passwordTxt = findViewById<View>(R.id.et_password) as EditText
        val email = emailTxt.text.toString()
        val password = passwordTxt.text.toString()

        if (!email.isEmpty() && !password.isEmpty()) {
            this.mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener ( this, OnCompleteListener<AuthResult> { task ->
                if (task.isSuccessful) {
                    startActivity(Intent(this, HomeActivity::class.java))
                    Toast.makeText(this, "Successfully Logged in", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Error Logging in", Toast.LENGTH_SHORT).show()
                }
            })

        }else {
            Toast.makeText(this, "Please fill up the Credentials :|", Toast.LENGTH_SHORT).show()
        }
    }
}
