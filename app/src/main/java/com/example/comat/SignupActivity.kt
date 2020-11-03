package com.example.comat

import android.R.attr.password
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.basgeekball.awesomevalidation.ValidationStyle
import com.example.comat.databinding.ActivitySignupBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        binding= DataBindingUtil.setContentView(this, R.layout.activity_signup)
        binding.buttonLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
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

        binding.signupButton.setOnClickListener {
            if(validation.validate()){
                createUser(this)
            }
            else {
                Toast.makeText(this, "Form invalid", Toast.LENGTH_LONG)
            }
        }
    }

    private fun createUser(context: Context): Unit {
        val auth = FirebaseAuth.getInstance()
        val email = binding.userEmail.text.toString()
        val password = binding.userPassword.text.toString()
        ProgressBar.VISIBLE
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this,
                OnCompleteListener<AuthResult?> { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("response", "createUserWithEmail:success")
                        val user: FirebaseUser? = auth.currentUser
                        ProgressBar.INVISIBLE
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("response", "createUserWithEmail:failure", task.exception)
                        Toast.makeText(
                            this, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
    }
}