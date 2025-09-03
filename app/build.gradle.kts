import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp")
    alias(libs.plugins.kotlin.parcelize)
}

fun getBaseUrl(propertyName: String): String {
    val p = Properties()
    p.load(project.rootProject.file("local.properties").reader())
    return p.getProperty(propertyName)
}

fun getRegistrationUrl(baseUrl: String): String {
    return "\"${baseUrl}login/registration\""
}

android {
    namespace = "com.example.android_2425_gent2"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.android_2425_gent2"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        addManifestPlaceholders(
            mapOf(
                "auth0Domain" to "@string/com_auth0_domain",
                "auth0ClientId" to "@string/com_auth0_client_id",
                "auth0Scheme" to "@string/com_auth0_scheme"
            )
        )

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            val baseUrl = getBaseUrl("PROD_BASE_URL")
            buildConfigField("String", "BASE_URL", "\"${baseUrl}\"")
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
            buildConfigField("String", "REGISTRATION_URL", "\"${baseUrl}login/registration\"")
        }
        debug {
            val baseUrl = getBaseUrl("DEV_BASE_URL")
            buildConfigField("String", "BASE_URL", "\"${baseUrl}\"")
            applicationIdSuffix = ".debug"
            isDebuggable = true
            buildConfigField("String", "REGISTRATION_URL", "\"${baseUrl}login/registration\"")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    buildToolsVersion = "34.0.0"
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.places)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.coroutines.android)
    implementation("androidx.compose.material3:material3-window-size-class")

    // Test dependencies
    testImplementation(libs.junit)
    testImplementation(libs.androidx.runner)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk)
    androidTestImplementation(libs.mockito.kotlin)
    androidTestImplementation(libs.mockito.core)
    testImplementation(libs.turbine)

    // Room
    implementation(libs.androidx.room.runtime)
    annotationProcessor(libs.androidx.room.compiler)
    annotationProcessor(libs.room.compiler)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)


    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // WorkManager
    implementation(libs.androidx.work.runtime.ktx)

    //Auth
    implementation(libs.android.auth0)

    implementation(libs.android.jwt)

}