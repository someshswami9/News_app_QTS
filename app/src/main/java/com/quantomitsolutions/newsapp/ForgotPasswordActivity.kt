package com.quantomitsolutions.newsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.quantomitsolutions.newsapp.databinding.ActivityForgotPasswordBinding

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForgotPasswordBinding
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_forgot_password)
        mAuth = Firebase.auth
        binding.loginBtn.setOnClickListener {
            if (binding.emailEt.text.toString().isEmpty()){
                Toast.makeText(this,"Please Enter the Valid Email Id", Toast.LENGTH_SHORT).show()
            }else {
                val email_txt = binding.emailEt.text.toString()
                mAuth.sendPasswordResetEmail(email_txt).addOnCompleteListener {
                    if (it.isSuccessful){
                        Toast.makeText(this,"Email send Successfully!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, LoginActivity::class.java))
                    }else{
                        Toast.makeText(this,it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.createAccTv.setOnClickListener{
            startActivity(Intent(this,RegisterActivity::class.java))
        }

    }

}