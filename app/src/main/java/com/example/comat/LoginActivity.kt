package com.example.comat

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.basgeekball.awesomevalidation.ValidationStyle
import com.example.comat.databinding.ActivityLoginBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?): Unit {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        binding= DataBindingUtil.setContentView(this, R.layout.activity_login)
        if(auth.currentUser != null){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.signupButton.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }


        val validation = AwesomeValidation(ValidationStyle.BASIC)
        validation.addValidation(
            this,
            R.id.userEmail,
            android.util.Patterns.EMAIL_ADDRESS,
            R.string.invalid_email
        )
        validation.addValidation(this, R.id.userPassword, ".{6,}", R.string.invalid_password)

        binding.buttonLogin.setOnClickListener {
            if(validation.validate()){
               loginUser(this)
            }
            else {
                makeText(this, "Form invalid", Toast.LENGTH_LONG)
            }
        }
    }


    private fun loginUser(context: Context): Unit {
        val auth = FirebaseAuth.getInstance()
        val email = binding.userEmail.text.toString()
        val password = binding.userPassword.text.toString()
        ProgressBar.VISIBLE
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this,
                OnCompleteListener<AuthResult?> { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("response", "signInWithEmail:success")
                        val user: FirebaseUser? = auth.currentUser
                        ProgressBar.INVISIBLE
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("response", "signInWithEmail:failure", task.exception)
                        makeText(
                            this, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
    }
}