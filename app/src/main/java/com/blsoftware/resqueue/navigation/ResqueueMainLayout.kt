package com.blsoftware.resqueue.navigation

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlinx.coroutines.launch
import com.blsoftware.resqueue.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResqueueMainLayout(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val activeDestination = Screen.entries.find { it.route == currentRoute }
    val topBarTitle = if (activeDestination != null) {
        stringResource(activeDestination.titleResId)
    } else {
        stringResource(R.string.app_name)
    }


    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ResqueueDrawerContent(
                currentRoute = currentRoute,
                onNavigateToDestination = { destination ->
                    scope.launch { drawerState.close() }

                    if (currentRoute != destination.route) {
                        if (destination.route == Screen.HOME.route) {
                            navController.popBackStack(
                                Screen.HOME.route,
                                inclusive = false
                            )
                        } else {
                            navController.navigate(destination.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }

                        }
                    }
                }
            )
        }
    )
    {
        ResqueueNavHost(
            navController = navController,
            onOpenDrawer = {scope.launch { drawerState.open() } }
        )
    }

}