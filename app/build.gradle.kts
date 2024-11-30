plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.fbcookie"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.fbcookie"
        minSdk = 21
        targetSdk = 35
        versionCode = 1
        versionName = "1.1"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
    create("release") {
        storeFile = file("marvin-J.jks")
        storePassword = "marvin-J" // Replace with actual password
        keyAlias = "marvin-J" // Replace with actual key alias
        keyPassword = "marvin-J" // Replace with actual key password
    }
}
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildTypes {
        getByName("release") { 
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release") 
        }
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation("com.google.firebase:firebase-analytics:22.1.2")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.code.gson:gson:2.11.0")
}
