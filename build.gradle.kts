// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ksp) apply false// Kotlin Symbol Processing for Room
    alias(libs.plugins.kotlin.serialization) apply false// For JSON parsing
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.kover) apply false
}