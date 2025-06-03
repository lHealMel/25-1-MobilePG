plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.services)
}

android {
    namespace = "com.example.mbpg"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.mbpg"
        minSdk = 33
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        manifestPlaceholders["redirectSchemeName"] = "com.example.mbpg"
        manifestPlaceholders["redirectHostName"] = "auth"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
        isCoreLibraryDesugaringEnabled = true
    }
    buildFeatures {
        viewBinding = true
    }
}


dependencies {
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.play.services.tasks)
    implementation(libs.firebase.auth)
    implementation(libs.play.services.auth)
    implementation(libs.googleid)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(files("libs/spotify-app-remote-release-0.8.0.aar"))
    implementation(libs.auth)
    implementation(libs.gson)
    coreLibraryDesugaring(libs.desugar.jdk.libs)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.credentials)
    implementation(libs.credentials.play.services.auth)
}