plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
}

android {
    compileSdkVersion(Android.compileSdk)

    defaultConfig {
        applicationId = "de.trbnb.apptemplate"
        minSdkVersion(21)
        targetSdkVersion(Android.compileSdk)
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        named("release").configure {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = Versions.java
        targetCompatibility = Versions.java
    }

    kotlinOptions {
        jvmTarget = Versions.java.toString()
    }

    buildFeatures {
        dataBinding = true
    }

    packagingOptions {
        exclude("META-INF/rxkotlin.kotlin_module")
    }
}

kapt {
    correctErrorTypes = true
}

dependencies {
    implementation(fileTree("dir" to "libs", "include" to listOf("*.jar")))

    implementation(project(":mvvmbase"))
    implementation(project(":mvvmbaseConductor"))
    implementation(project(":mvvmbaseRxJava2"))
    implementation(project(":mvvmbaseRxJava3"))

    // Support library
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("com.google.android.material:material:1.3.0")
    implementation("androidx.recyclerview:recyclerview:1.2.0")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1")
    implementation("androidx.fragment:fragment-ktx:1.3.3")
    implementation("androidx.navigation:navigation-fragment-ktx:2.3.5")
    implementation("androidx.navigation:navigation-ui-ktx:2.3.5")

    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")

    // Dagger 2
    /*kapt("com.google.dagger:dagger-compiler:${Versions.dagger}")
    implementation("com.google.dagger:dagger:${Versions.dagger}")*/
    compileOnly("javax.annotation:jsr250-api:1.0")

    implementation("io.reactivex.rxjava2:rxkotlin:2.4.0")

    implementation("com.bluelinelabs:conductor:2.1.5")
    implementation("com.bluelinelabs:conductor-archlifecycle:2.1.5")
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}
