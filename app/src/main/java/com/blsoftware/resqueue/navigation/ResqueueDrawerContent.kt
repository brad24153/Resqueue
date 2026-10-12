package com.blsoftware.resqueue.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun ResqueueDrawerContent(
    currentRoute: String?,
    onNavigateToDestination: (Screen) -> Unit
) {
    ModalDrawerSheet {
        Text(
            text = "Resqueue",
            modifier = Modifier.padding(16.dp)
        )
        //todo maybe try nested nav
        Screen.entries.filter{it.showInDrawer}.forEach { destination ->
            NavigationDrawerItem(
                label = {Text(stringResource(destination.titleResId))},
                icon = {Icon(destination.icon, contentDescription = null)},
                selected = currentRoute == destination.route,
                onClick = {onNavigateToDestination(destination)}
            )
        }
    }
}