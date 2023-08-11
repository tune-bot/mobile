plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "net.tune_bot"
    compileSdk = 33

    defaultConfig {
        applicationId = "net.tune_bot"
        minSdk = 21
        targetSdk = 33
        versionCode = 1
        versionName = "0.0.1"

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

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.8"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    sourceSets["main"].java {
        srcDir("src/main/kotlin")
    }
}

dependencies {
    implementation("androidx.compose.material:material:1.4.3")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.leanback:leanback:1.0.0")
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation("androidx.navigation:navigation-compose:2.6.0")
    implementation("com.google.code.gson:gson:2.10.1")
}