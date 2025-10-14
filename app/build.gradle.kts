plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")               // 必须启用
    kotlin("plugin.parcelize")  // 解决 Parcelize 问题
}

android {
    namespace = "com.example.girlfriends"
    compileSdk = 34  // Android 14，对应 2024 年最新版

    defaultConfig {
        applicationId = "com.example.girlfriends"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.android.material:material:1.10.0")
    implementation("org.jsoup:jsoup:1.17.1")
    // 网络请求（可换成 ktor 或 Retrofit）
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")

    // 图片加载
    implementation("io.coil-kt:coil:2.7.0")

    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("io.coil-kt:coil:2.6.0")

    // 视频播放器
    implementation("androidx.media3:media3-ui:1.3.1")
    implementation("androidx.media3:media3-exoplayer:1.3.1")

    // 协程（异步加载）
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")
}