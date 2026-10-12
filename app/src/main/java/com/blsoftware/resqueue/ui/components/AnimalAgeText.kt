package com.blsoftware.resqueue.ui.components

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.text.TextStyle
import com.blsoftware.resqueue.R

@Composable
fun AnimalAgeText(
    ageInMonths: Int,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    color: Color = LocalContentColor.current
) {
    val ageText = if (ageInMonths < 12) {
        pluralStringResource(
            id = R.plurals.age_months,
            count = ageInMonths,
            ageInMonths
        )
    } else {
        val years = ageInMonths / 12
        pluralStringResource(
            id = R.plurals.age_years,
            count = years,
            years
        )
    }

    Text(
        text = ageText,
        style = style,
        color = color,
        modifier = modifier
    )
}