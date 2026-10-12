package com.blsoftware.resqueue.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.blsoftware.resqueue.R
import com.blsoftware.resqueue.navigation.Screen

class HomeViewModel : ViewModel() {
    private val _cards = MutableStateFlow<List<HomeCardItem>>(emptyList())

    val cards: StateFlow<List<HomeCardItem>> = _cards.asStateFlow()

    init {
        loadHomeCards()
    }

    private fun loadHomeCards() {
        _cards.value = listOf(
            HomeCardItem("intake",R.string.new_intake, R.drawable.ic_intake,"New Intake"),
            HomeCardItem("outcome",R.string.new_live_outcome, R.drawable.ic_intake,"New Outcome"),
            HomeCardItem("intake",R.string.new_intake, R.drawable.ic_intake, "New Intake"),
            HomeCardItem(Screen.ANIMAL_LIST.route,R.string.in_custody_animal_list,R.drawable.ic_intake,"In CustodyList")
        )
    }
}