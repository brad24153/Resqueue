package com.blsoftware.resqueue

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import com.blsoftware.resqueue.navigation.ResqueueMainLayout

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ResqueueApp()

        }
    }
}

@Composable
fun ResqueueApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    ResqueueMainLayout(navController = navController)
}




