import java.util.Properties
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}
val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localPropertiesFile.inputStream().use { localProperties.load(it) }
}
val storePassword = localProperties.getProperty("RELEASE_STORE_PASSWORD") ?: ""
val keyPassword = localProperties.getProperty("RELEASE_KEY_PASSWORD") ?: ""

android {
    namespace = "com.example.petal"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.petal"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val mapsApiKey = localProperties.getProperty("MAPS_API_KEY") ?: ""
        buildConfigField("String", "MAPS_API_KEY", "\"$mapsApiKey\"")
        manifestPlaceholders["MAPS_API_KEY"] = mapsApiKey
    }

    signingConfigs {
        create("release") {
            storeFile = file("../keystore/petal-release-key.jks")
            storePassword = storePassword
            keyAlias = "petal_key"
            keyPassword = keyPassword
        }
    }

    // ✅ THEN use it
    buildTypes {
        release {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("release")

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
        }
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.foundation)
    implementation("androidx.compose.material:material-icons-extended:1.7.0")
    implementation(libs.play.services.location)
    implementation(libs.androidx.foundation)
    implementation(libs.androidx.compose.ui.text)
    implementation(libs.androidx.compose.runtime.annotation)
    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // Navigation - SIMPLE STRING-BASED
    //implementation(libs.androidx.navigation.compose)
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // Other dependencies
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.coil.compose)

    // Gson and Retrofit
    implementation(libs.gson)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.gson)

    // Kotlin Serialization
    implementation(libs.kotlinx.serialization.json)

    //voyager
    implementation(libs.voyager.navigator)
    implementation(libs.voyager.tab.navigator)
    implementation(libs.voyager.transitions)
    implementation(libs.voyager.screenmodel)
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation("com.google.accompanist:accompanist-permissions:0.34.0")
    implementation("androidx.compose.foundation:foundation:1.7.0")
    implementation("io.coil-kt:coil-compose:2.6.0")

    //google maps
    implementation("com.google.android.libraries.places:places:3.5.0")
    implementation("com.google.maps.android:maps-compose:8.1.0")
    implementation("com.google.android.gms:play-services-maps:20.0.0")
    implementation("com.google.android.gms:play-services-location:21.3.0")
    implementation("com.google.android.libraries.places:places:3.5.0")
    implementation("org.maplibre.gl:android-sdk:12.3.1")
    implementation("com.google.android.libraries.places:places:3.3.0")
}
