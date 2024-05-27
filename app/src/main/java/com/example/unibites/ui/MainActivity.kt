package com.example.unibites.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import android.widget.Toast
import com.example.unibites.services.InternetConnectionCallback

import com.example.unibites.services.InternetConnectionObserver
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore


class MainActivity : ComponentActivity(), InternetConnectionCallback {

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()

        super.onCreate(savedInstanceState)
        InternetConnectionObserver.instance(this).setCallback(this).register()
        auth = Firebase.auth

        // Set up uncaught exception handler
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            val crashData = hashMapOf(
                "threadName" to thread.name,
                "throwableMessage" to throwable.localizedMessage,
                "stackTrace" to throwable.stackTraceToString(),
                "timestamp" to FieldValue.serverTimestamp()
            )
            // Let Firebase Crashlytics handle the uncaught exception
            FirebaseCrashlytics.getInstance().recordException(throwable)
            // Log the crash data to Firestore
            FirebaseFirestore.getInstance().collection("crashes").add(crashData)
        }

        setContent { UniBitesApp(auth) }
    }
    override fun onDestroy() {
        super.onDestroy()
        InternetConnectionObserver.unRegister()
    }
     override fun onConnected() {
        Toast.makeText(this, "Conexión a internet resumida", Toast.LENGTH_LONG).show()
    }

    override fun onDisconnected() {
        Toast.makeText(this, "Conexión a Internet perdida", Toast.LENGTH_LONG).show()
    }


}


