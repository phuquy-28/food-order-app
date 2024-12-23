plugins {
    id ("com.android.application")
    id ("com.google.gms.google-services")
}

android {
    compileSdkVersion(31)
    buildToolsVersion ("30.0.0")

    defaultConfig {
        applicationId = "com.example.foodorderapp"
        minSdkVersion(21)
        targetSdkVersion(31)
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("com.google.android.material:material:1.3.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.+")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")

    // Circle Imageview
    implementation("de.hdodenhof:circleimageview:3.0.0")
    // Room database
    implementation("androidx.room:room-runtime:2.2.5")
    annotationProcessor("androidx.room:room-compiler:2.2.5")
    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:31.0.3"))
    implementation("com.google.firebase:firebase-database")
    implementation("com.google.firebase:firebase-auth")
    //event bus
    implementation("org.greenrobot:eventbus:3.0.0")
    // Glide load image
    implementation("com.github.bumptech.glide:glide:3.7.0")
    // Gson
    implementation("com.google.code.gson:gson:2.9.0")
    // MaterialDialog
    implementation("com.afollestad.material-dialogs:core:0.9.6.0")
    // Indicator
    implementation("me.relex:circleindicator:2.1.6")
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(files("libs/zpdk-release-v3.1.aar"))
    implementation("com.squareup.okhttp3:okhttp:4.6.0")

}