package com.example.ddm

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ddm.ui.theme.DDMTheme
import com.google.android.play.core.ktx.SplitInstallStateUpdatedListener
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener

class MainActivity : ComponentActivity() {
    private val TAG: String = "xDDM"

    private var sessionId: Int = 0
    val splitSdkManager by lazy { SplitInstallManagerFactory.create(this) }
    val contract: MyContract? by lazy {
        try {
            val r = Class.forName("com.example.sampledynamicfeature.MyContractImpl").getDeclaredConstructor().newInstance() as MyContract
            return@lazy r
        } catch (e: Exception) {
            return@lazy null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DDMTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        Button(onClick = { downloadSdk() }) {
                            Text(text = "Download")
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(onClick = { isSDKInitailised() }) {
                            Text(text = "Is Downloaded")
                        }
                        Button(onClick = { init() }) {
                            Text(text = "initialise")
                        }
                        Button(onClick = { intValue() }) {
                            Text(text = "getIntValue")
                        }
                        Button(onClick = { stringValue() }) {
                            Text(text = "getStringValue")
                        }

                    }
                }
            }
        }
    }

    private fun init() {
        Log.d(TAG, "init: ")
        contract?.initialise()
    }

    private fun intValue() {
        Log.d(TAG, "intValue: ")
        contract?.getIntValue()
    }

    private fun stringValue() {
        Log.d(TAG, "stringValue: ")
        contract?.getStringValue()
    }

    fun isSDKInitailised(): Boolean {
        return splitSdkManager.installedModules.contains("sampledynamicfeature").also {
            Log.d(TAG, "isSDKInitailised: $it")
        }
    }

    private fun downloadSdk() {
        Log.d(TAG, "downloadSdk:")

        val listener = SplitInstallStateUpdatedListener { state ->
            if (state.sessionId() == sessionId) {
                Log.d(TAG, "downloadSdk: $state")
            }
        }

        splitSdkManager.registerListener(listener)
        val request = SplitInstallRequest.newBuilder()
            .addModule("sampledynamicfeature")
            .build()

        splitSdkManager.startInstall(request)
            .addOnSuccessListener {
                sessionId = it
                Log.d(TAG, "downloadSdk: Success")
            }
            .addOnCanceledListener {
                Log.d(TAG, "downloadSdk: Cancelled")
            }
            .addOnFailureListener {
                Log.d(TAG, "downloadSdk: Failed")
            }
    }


}