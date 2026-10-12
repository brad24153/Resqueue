package com.blsoftware.resqueue.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.blsoftware.resqueue.animaldetail.AnimalDetailScreen
import com.blsoftware.resqueue.animaldetail.AnimalDetailViewModel
import com.blsoftware.resqueue.animallist.AnimalListScreen
import com.blsoftware.resqueue.animallist.AnimalListViewModel
import com.blsoftware.resqueue.home.HomeScreen
import com.blsoftware.resqueue.home.HomeViewModel
import com.blsoftware.resqueue.intake.IntakeScreen
import com.blsoftware.resqueue.outcome.OutcomeScreen
import com.blsoftware.resqueue.settings.SettingsScreen

@Composable
fun ResqueueNavHost(
    navController: NavHostController,
    onOpenDrawer: () -> Unit,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.HOME.route,
        modifier = modifier
    ) {
        composable(Screen.HOME.route) {
            val homeViewModel: HomeViewModel = hiltViewModel()
            val cardList by homeViewModel.cards.collectAsStateWithLifecycle()

            HomeScreen(
                cardList = cardList,
                onCardClick = {clickedCard ->
                    navController.navigate(clickedCard.destinationRoute)
                },
                onOpenDrawer = onOpenDrawer
            )
        }
        composable(Screen.INTAKE.route) {
            IntakeScreen()
        }
        composable(Screen.OUTCOME.route) {
            OutcomeScreen()
        }
        composable(Screen.SETTINGS.route){
            SettingsScreen()
        }
        composable(Screen.ANIMAL_LIST.route) {
            val viewModel: AnimalListViewModel = hiltViewModel()
            AnimalListScreen(
                viewModel = viewModel,
                onAnimalClick = { animalId ->
                    // Navigate using the helper function
                    navController.navigate(Screen.ANIMAL_DETAIL.createRoute(animalId))
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        // Detail Route Registration
        composable(
            route = Screen.ANIMAL_DETAIL.route,
            arguments = listOf(
                navArgument("animalId") {
                    type = NavType.StringType
                }
            )
        ) {
            // Hilt automatically injects SavedStateHandle containing "animalId" into AnimalDetailViewModel
            val viewModel: AnimalDetailViewModel = hiltViewModel()
            AnimalDetailScreen(viewModel = viewModel)
        }

        //TODO setup settings route
    }

}