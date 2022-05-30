package com.quantomitsolutions.newsapp

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.CallbackManager.Factory.create
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.quantomitsolutions.newsapp.databinding.ActivityLoginBinding
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*


class LoginActivity : AppCompatActivity() {


    private lateinit var binding: ActivityLoginBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var gso : GoogleSignInOptions
    private lateinit var gsc : GoogleSignInClient
    private lateinit var callbackManager : CallbackManager



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login)
        mAuth = Firebase.auth

        gso  = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
        gsc = GoogleSignIn.getClient(this,gso)
        printHashKey(this)

        callbackManager = create()

        binding.googleLoginBtn.setOnClickListener {
            googleSinIn()
        }
        binding.facebookLoginBtn.setOnClickListener {
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
            facbookSignIn()
        }
        binding.loginBtn.setOnClickListener {
            if(binding.emailEt.text.toString().isEmpty() || binding.passwordEt.text.toString().isEmpty()){
                Toast.makeText(this,"please enter all the credentials", Toast.LENGTH_SHORT).show()
            }else{
                val email_txt = binding.emailEt.text.toString()
                val password_txt = binding.passwordEt.text.toString()
                login(email_txt,password_txt)
            }
        }
        binding.forgotPassTv.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }
        binding.registerTv.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.signUpTv.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }


        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult?>{
            override fun onCancel() {

            }

            override fun onError(error: FacebookException) {

            }

            override fun onSuccess(result: LoginResult?) {
                val i = Intent(applicationContext, MainActivity::class.java)
                startActivity(i)
            }

        })
        


    }

    private fun googleSinIn() {
        val intent = gsc.signInIntent
        startActivityForResult(intent, 1000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account: GoogleSignInAccount = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.getIdToken(), null)
        mAuth.signInWithCredential(credential)
            .addOnSuccessListener(this) { authResult ->
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            .addOnFailureListener(this) { e ->
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
            }
    }

    private fun login(emailTxt: String, passwordTxt: String) {
        mAuth.signInWithEmailAndPassword(emailTxt,passwordTxt).addOnCompleteListener (this) {
            if (it.isSuccessful){
                Toast.makeText(this,"Login Successful", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this,MainActivity::class.java))
            }else{
                Toast.makeText(this,it.exception.toString(),Toast.LENGTH_LONG).show()
            }
        }
    }



    private fun facbookSignIn() {


    }

    private fun handleFacebookAccessToken(accessToken: AccessToken) {

    }

    fun printHashKey(pContext: Context) {
        try {
            val info: PackageInfo = pContext.getPackageManager()
                .getPackageInfo(pContext.getPackageName(), PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val hashKey: String = String(Base64.encode(md.digest(), 0))
                Log.i(TAG, "printHashKey() Hash Key: $hashKey")
            }
        } catch (e: NoSuchAlgorithmException) {
            Log.e(TAG, "printHashKey()", e)
        } catch (e: Exception) {
            Log.e(TAG, "printHashKey()", e)
        }
    }

    private fun LoginManager.registerCallback(callbackManager: CallbackManager, callback: FacebookCallback<LoginResult?>) {


    }


}


