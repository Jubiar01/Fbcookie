
plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    
}

android {
    namespace = "com.example.fbcookie"
    compileSdk = 34
    
    defaultConfig {
        applicationId = "com.example.fbcookie"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.1"
        
        vectorDrawables { 
            useSupportLibrary = true
        }
    }

signingConfigs {
        release {
            storeFile file("my-release-key.jks") // Path to your keystore
            storePassword "your_keystore_password"
            keyAlias "upload_key"
            keyPassword "your_key_password"
        }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig signingConfigs.release
        }
    }

    buildFeatures {
        viewBinding = true
        
    }
    
}

dependencies {
    implementation("com.google.firebase:firebase-analytics:22.1.2")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.code.gson:gson:2.11.0")
}
