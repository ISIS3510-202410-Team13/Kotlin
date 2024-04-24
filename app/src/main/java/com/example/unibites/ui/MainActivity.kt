package com.example.unibites.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.unibites.Signup.repository.SignUpViewModel
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.auth


class MainActivity : ComponentActivity() {

    val auth = Firebase.auth
    lateinit var viewModel: SignUpViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this);
        setContent { UniBitesApp() }

    }

    override fun onStart() {
        super.onStart()
    }
}
