package com.blsoftware.resqueue.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.blsoftware.resqueue.R
import com.blsoftware.resqueue.ui.components.DynamicStatusBarIcons

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    cardList: List<HomeCardItem>,
    modifier: Modifier = Modifier,
    onCardClick: (HomeCardItem) -> Unit,
    onOpenDrawer: () -> Unit
    ) {
    DynamicStatusBarIcons(useDarkIcons = true)


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.home)) },
                navigationIcon = {
                    IconButton(onClick = onOpenDrawer) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Open Drawer"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(innerPadding)
        ) {
            items(cardList) {item ->
                Card(
                    onClick = {onCardClick(item)},
                    modifier = Modifier.padding(8.dp)) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(8.dp).fillMaxWidth()){
                        Icon(
                            painter = painterResource(id = item.iconResId),
                            contentDescription = null,
                            modifier = Modifier.size(96.dp)
                        )
                        Text(text = stringResource(item.titleResId))
                    }

                }
            }
        }
    }

}

//@Preview(
//    showBackground = true,
//    showSystemUi = true,
//    name = "Home Screen Full View"
//)
//@Composable
//fun HomeScreenPreview() {
//    ResqueueTheme {
//        HomeScreen(
//            cardList = listOf(
//                HomeCardItem("1", R.string.new_intake, R.drawable.ic_intake, "Description 1"),
//                HomeCardItem("2", R.string.new_live_outcome, R.drawable.ic_intake,"Description 2"),
//                HomeCardItem("3", R.string.new_intake, R.drawable.ic_intake,"Description 3"),
//                HomeCardItem("4", R.string.new_intake, R.drawable.ic_intake,"Description 4")
//            )
//        )
//    }
//}