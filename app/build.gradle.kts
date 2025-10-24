plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "ru.mirea.pasportnikovaeo.bookshell"
    compileSdk = 35

    defaultConfig {
        applicationId = "ru.mirea.pasportnikovaeo.bookshell"
        minSdk = 26
        targetSdk = 35
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
    buildFeatures {
        viewBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(project(":domain"))
    implementation(project(":data"))


        // Основные Android зависимости
        implementation("androidx.appcompat:appcompat:1.6.1")
        implementation("com.google.android.material:material:1.11.0")
        implementation("androidx.constraintlayout:constraintlayout:2.1.4")
        implementation("androidx.activity:activity:1.8.2")

        // Room Database
        implementation("androidx.room:room-runtime:2.6.1")
        annotationProcessor("androidx.room:room-compiler:2.6.1")

        // ViewModel и LiveData
        implementation("androidx.lifecycle:lifecycle-viewmodel:2.7.0")
        implementation("androidx.lifecycle:lifecycle-livedata:2.7.0")
        implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
        implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")

        // UI компоненты
        implementation("androidx.recyclerview:recyclerview:1.3.2")
        implementation("androidx.cardview:cardview:1.0.0")
        implementation("androidx.core:core-ktx:1.12.0")

        // Твои модули
        implementation(project(":domain"))
        implementation(project(":data"))

        // Остальные
        implementation("androidx.compose.material3:material3:1.1.2")
        implementation("com.github.bumptech.glide:glide:4.16.0")

        // Firebase
        implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
        implementation("com.google.firebase:firebase-auth")
        implementation("com.google.firebase:firebase-firestore")

        // Тестирование
        testImplementation("junit:junit:4.13.2")
        androidTestImplementation("androidx.test.ext:junit:1.1.5")
        androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

}