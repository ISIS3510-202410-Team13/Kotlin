package com.example.unibites.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.crashlytics.internal.model.CrashlyticsReport
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore


class MainActivity : ComponentActivity() {


    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
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
        }

        setContent { UniBitesApp(auth) }
    }


}

@Composable
fun CrashButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.padding(16.dp)
    ) {
        Text("Trigger Crash")
    }
}


