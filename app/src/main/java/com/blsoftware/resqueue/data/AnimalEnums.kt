package com.blsoftware.resqueue.data

import androidx.annotation.StringRes
import com.blsoftware.resqueue.R

enum class AlteredStatus(@StringRes val labelResId: Int) {
    YES(R.string.altered),
    NO(R.string.not_altered),
    UNKNOWN(R.string.alter_unknown);

    companion object {
        fun fromString(value: String?): AlteredStatus {
            if (value == null) return UNKNOWN
            return when (value.trim().lowercase()) {
                "yes" -> YES
                "no" -> NO
                else -> UNKNOWN
            }
        }
    }
}

enum class AgeCategory(val label: String) {
    PUPPY("Puppy"),
    ADULT("Adult"),
    SENIOR("Senior"),
    UNKNOWN("Unknown");

    companion object {
        fun fromMonths(ageInMonths: Int?): AgeCategory {
            if (ageInMonths == null || ageInMonths < 0) return UNKNOWN
            return when {
                ageInMonths < 12 -> PUPPY  // 0–11 months
                ageInMonths < 84 -> ADULT         // 1–6 years
                else -> SENIOR                    // 7+ years
            }
        }
    }
}