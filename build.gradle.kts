buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.1.0")
        classpath("com.google.gms:google-services:4.2.0")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        flatDir {
            dirs("libs")
        }
    }
}

task("clean", Delete::class) {
    delete(rootProject.buildDir)
}