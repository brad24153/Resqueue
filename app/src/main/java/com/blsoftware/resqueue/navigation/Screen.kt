package com.blsoftware.resqueue.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.ui.graphics.vector.ImageVector
import com.blsoftware.resqueue.R


//Add screens here to update title in topAppBar and show in NavDrawer
enum class Screen(
    @StringRes val titleResId: Int,
    val route: String,
    val icon: ImageVector,
    val showInDrawer: Boolean
) {
    ANIMAL_LIST(R.string.in_custody_animal_list, "animal-list",Icons.Default.Home, true),
    ANIMAL_DETAIL(R.string.animal_detail,"animal-detail/{animalId}",Icons.Default.Home,false),
    HOME(R.string.home,"home", Icons.Default.Home,true),
    INTAKE(R.string.new_intake,"intake",Icons.Default.Add,false),
    OUTCOME(R.string.new_live_outcome,"outcome",Icons.Default.ThumbUp,false),
    SETTINGS(R.string.settings,"settings", Icons.Default.Settings,true);

    //todo add other screens/routes

    fun createRoute(animalId: String): String {
        return "animal-detail/$animalId"
    }
}