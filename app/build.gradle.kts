plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.adventsignin"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.adventsignin"
        minSdk = 28
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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    // Add this packagingOptions block to fix META-INF file conflicts
    packagingOptions {
        exclude( "META-INF/DEPENDENCIES");
        exclude( "META-INF/LICENSE");
        exclude( "META-INF/LICENSE.txt");
        exclude( "META-INF/NOTICE");
        exclude( "META-INF/NOTICE.txt");
    }
}

dependencies {
    implementation("com.google.api-client:google-api-client-android:1.35.0")
    implementation("com.google.apis:google-api-services-sheets:v4-rev20230227-2.0.0")

    implementation("com.google.http-client:google-http-client-android:1.43.3")
    implementation("com.google.http-client:google-http-client-gson:1.43.3")
    implementation("com.google.android.gms:play-services-auth:20.7.0")

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
