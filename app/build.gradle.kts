import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp) // Kotlin Symbol Processing for Room
    alias(libs.plugins.kotlin.serialization) // For JSON parsing
    alias(libs.plugins.hilt)
    alias(libs.plugins.kover)
}

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(localPropertiesFile.inputStream())
}

tasks.withType<Test> {
    useJUnitPlatform()

    jvmArgs(
        "--add-opens=java.base/jdk.internal.access=ALL-UNNAMED",
        "-XX:+EnableDynamicAgentLoading"
    )
}


android {
    namespace = "com.blsoftware.resqueue"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.blsoftware.resqueue"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val apiKey = localProperties.getProperty("SHELTERLUV_API_KEY") ?: ""
        buildConfigField("String", "SHELTERLUV_API_KEY", "\"$apiKey\"")
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }

        debug {
            enableUnitTestCoverage = true
            enableAndroidTestCoverage = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        buildConfig = true
        compose = true
    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
    }
}

kover {
    reports {
        filters {
            excludes {
                packages(
                    "com.blsoftware.resqueue.data.remote",
                    "com.blsoftware.resqueue.data.local"
                )
                classes(
                    "*_Factory*",
                    "*_Provide",
                    "*_Hilt*",
                    "*_Impl*",
                    "*.BuildConfig",
                    "com.blsoftware.resqueue.data.*Module*",
                    "com.blsoftware.resqueue.data.DataException*"
                )
                annotatedBy("*Generated*")
            }
        }

        total {
            xml {
                onCheck = false
                xmlFile = layout.buildDirectory.file("reports/kover/report.xml")
            }
            html {
                onCheck = false
                htmlDir = layout.buildDirectory.dir("reports/kover/html")
            }
        }
    }

    dependencies {
        implementation(platform(libs.androidx.compose.bom))
        implementation(libs.androidx.activity.compose)
        implementation(libs.androidx.junit.ktx)
        implementation(libs.androidx.lifecycle.viewmodel.compose)
        implementation(libs.androidx.navigation.compose)
        implementation(libs.androidx.lifecycle.runtime.compose)
        implementation(libs.androidx.compose.material.icons.extended)
        implementation(libs.androidx.compose.material3)
        implementation(libs.androidx.compose.ui)
        implementation(libs.androidx.compose.ui.graphics)
        implementation(libs.androidx.compose.ui.tooling.preview)
        implementation(libs.androidx.core.ktx)
        implementation(libs.androidx.lifecycle.runtime.ktx)
        implementation(libs.androidx.lifecycle.viewmodel.ktx)
        implementation(libs.androidx.ui)
        implementation(libs.coil.compose)
        implementation(libs.coil.network.okhttp)
        implementation(libs.core.ktx)
        implementation(libs.hilt.android)
        implementation(libs.logging.interceptor)
        testImplementation(libs.junit)
        testImplementation(libs.junit.jupiter.api)
        testImplementation(libs.junit.jupiter.params)
        testRuntimeOnly(libs.junit.jupiter.engine)
        testRuntimeOnly(libs.junit.platform.launcher)
        testRuntimeOnly(libs.junit.vintage.engine)
        androidTestImplementation(platform(libs.androidx.compose.bom))
        androidTestImplementation(libs.androidx.compose.ui.test.junit4)
        androidTestImplementation(libs.androidx.espresso.core)
        androidTestImplementation(libs.androidx.junit)
        testImplementation(libs.kotlinx.coroutines.test)
        testImplementation(libs.mockk)
        debugImplementation(libs.androidx.compose.ui.test.manifest)
        debugImplementation(libs.androidx.compose.ui.tooling)

        // Room Database
        implementation(libs.androidx.room.runtime)
        implementation(libs.androidx.room.ktx)
        ksp(libs.hilt.compiler)
        ksp(libs.room.compiler)
        implementation(libs.hilt.navigation.compose)

        // Retrofit & KotlinX Serialization
        implementation(libs.retrofit)
        implementation(libs.retrofit.converter.gson)
        implementation(libs.retrofit.kotlinx.serialization)
        implementation(libs.kotlinx.serialization.json)
    }
}

