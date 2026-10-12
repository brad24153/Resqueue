package com.blsoftware.resqueue.ui.components

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
fun DynamicStatusBarIcons(useDarkIcons: Boolean) {
    val view = LocalView.current

    //if block to prevent exception in previews. can remove later
    if (!view.isInEditMode) {
        DisposableEffect(useDarkIcons) {
            val window = (view.context as Activity).window
            val controller = WindowCompat.getInsetsController(window, view)

            controller.isAppearanceLightStatusBars = useDarkIcons
            onDispose {}
        }
    }
}