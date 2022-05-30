package com.quantomitsolutions.newsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.quantomitsolutions.newsapp.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_register)
        mAuth = Firebase.auth
        binding.checkBox.setOnClickListener{
        }
        binding.registerBtn.setOnClickListener {
            if( binding.nameEt.text.toString().isEmpty() ||
                binding.emailEt.text.toString().isEmpty() ||
                binding.passwordEt.text.toString().isEmpty() ||
                binding.phoneNoEt.text.toString().isEmpty())
            {
                Toast.makeText(this,"Please Enter All Credentials!!", Toast.LENGTH_SHORT).show()
            } else {
                val name_txt = binding.nameEt.text.toString()
                val email_txt = binding.emailEt.text.toString()
                val password_text = binding.passwordEt.text.toString()
                val phone_no_text = binding.phoneNoEt.text.toString()
                if(!binding.checkBox.isChecked){
                    Toast.makeText(this,"Please Agree All the terms", Toast.LENGTH_SHORT).show()
                }else{
                    register(email_txt,password_text, phone_no_text, name_txt)
                }

            }
        }
        binding.signInTv.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.termTv.setOnClickListener {
            
        }
        binding.loginTvUpper.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun register(emailTxt: String, passwordText: String, phoneNoText: String, nameTxt: String) {
        mAuth.createUserWithEmailAndPassword(emailTxt,passwordText).addOnCompleteListener(this) { task->
            if (task.isSuccessful){
                Toast.makeText(this,"Account Created Successfully",Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
            }else{
                Toast.makeText(this,task.exception.toString(),Toast.LENGTH_LONG).show()
            }
        }
    }


}