package com.example.project_one

import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.project_one.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var googleSignInClient: GoogleSignInClient? = null
    var firebaseAuth: FirebaseAuth? = null

    private lateinit var database: FirebaseDatabase

    val networkChangeListener = NetworlChangeListener()

    lateinit var progressBar: MyProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        progressBar = MyProgressBar("Wait...", this)

        if (firebaseAuth!!.currentUser != null && firebaseAuth!!.currentUser!!.isEmailVerified) {
            Toast.makeText(this, "Login successfully", Toast.LENGTH_SHORT).show()
            val intent: Intent = Intent(this, MainHomeScreen::class.java)
            startActivity(intent)
            finish()
        }

        val googleSignInOptions = GoogleSignInOptions.Builder(
            GoogleSignInOptions.DEFAULT_SIGN_IN
        ).requestIdToken("215889474988-ijopsepdmkfovjn4pjgupsrc19bompv8.apps.googleusercontent.com")
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        binding.googleSignInBtn.setOnClickListener(View.OnClickListener {
            progressBar.show()
            googleSignIn()
        })
    }

    private fun googleSignIn() {
        val intent = googleSignInClient!!.signInIntent
        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        progressBar.dismiss()
        if (requestCode == 100) {
            val signInAccountTask: Task<GoogleSignInAccount> =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            if (signInAccountTask.isSuccessful) {
                progressBar.dismiss()
                val s = "Google sign in successful"
                displayToast(s)
                try {
                    val googleSignInAccount: GoogleSignInAccount = signInAccountTask
                        .getResult(ApiException::class.java)
                    if (googleSignInAccount != null) {
                        val authCredential = GoogleAuthProvider
                            .getCredential(
                                googleSignInAccount.idToken, null
                            )
                        firebaseAuth!!.signInWithCredential(authCredential)
                            .addOnCompleteListener(
                                this
                            ) { task ->
                                if (task.isSuccessful) {
                                    progressBar.show()
                                    val databaseRef = database.reference.child("users")
                                        .child(FirebaseAuth.getInstance().currentUser!!.uid)
                                    val name = firebaseAuth!!.currentUser!!.displayName.toString()
                                    val email = firebaseAuth!!.currentUser!!.email.toString()
                                    val users = Users(
                                        name, email, FirebaseAuth.getInstance().currentUser!!.uid
                                    )
                                    databaseRef.setValue(users).addOnCompleteListener {it1->
                                        if (it1.isSuccessful) {
                                            progressBar.dismiss()
                                            startActivity(
                                                Intent(this, MainHomeScreen::class.java)
                                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                            )
                                            finish()
                                        } else {
                                            progressBar.dismiss()
                                            displayToast("Something went wrong ,try again")
                                        }
                                    }
                                } else {
                                    progressBar.dismiss()
                                    displayToast("Something went wrong ,try again")
                                }
                            }
                    }
                } catch (e: ApiException) {
                    progressBar.dismiss()
                    e.printStackTrace()
                }
            }
        }
    }

    private fun displayToast(s: String) {
        Toast.makeText(applicationContext, s, Toast.LENGTH_SHORT).show()
    }

    override fun onStart() {
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkChangeListener, filter)
        super.onStart()
    }

    override fun onStop() {
        unregisterReceiver(networkChangeListener)
        super.onStop()
    }
}