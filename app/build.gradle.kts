import java.util.Properties
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    //KSP (lo usa Room para generar el código de la base de datos)
    id("com.google.devtools.ksp")
}

// La clave de la NASA se lee de local.properties (ese archivo NO se sube a GitHub)
val localProperties = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) file.inputStream().use { load(it) }
}
android {
    namespace = "pe.edu.esan.diarioespacial"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "pe.edu.esan.diarioespacial"
        minSdk = 34
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        //Clave de la API de la NASA -> BuildConfig.NASA_API_KEY
        buildConfigField("String", "NASA_API_KEY", "\"${localProperties.getProperty("NASA_API_KEY", "")}\"")
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    //Navigation Compose
    implementation("androidx.navigation:navigation-compose:2.10.1")
    //ViewModel para Compose (MVVM: ViewModel + StateFlow)
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.11.0")
    //Material icons
    implementation("androidx.compose.material:material-icons-extended:1.7.8")
    // Coil Compose (imágenes de APOD)
    implementation("io.coil-kt:coil-compose:2.7.0")

    //Retrofit
    implementation("com.squareup.retrofit2:retrofit:3.0.0")
    implementation("com.squareup.retrofit2:converter-gson:3.0.0")

    //Room
    val roomVersion = "2.8.5"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}