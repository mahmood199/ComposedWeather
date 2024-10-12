import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.cocoapods)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.jetbrains.compose)
}

kotlin {
    jvmToolchain(17)
    androidTarget {
        compilations.all {
            kotlin {
                jvm("17")
            }
        }
    }

    iosX64 ()
    iosArm64 ()
    iosSimulatorArm64 ()

    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        version = "1.0"
        ios.deploymentTarget = "16.0"
        framework {
            baseName = "shared"
            isStatic = true
        }
    }
    
    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
//            implementation(compose.ui)
//            implementation(compose.preview)

            //put your multiplatform dependencies here
            implementation(libs.kotlinx.coroutines.core)
//            implementation(libs.landscapist.glide)
            implementation(libs.landscapist.coil3)
        }

        androidMain.dependencies {
            implementation(libs.lifecycle.runtime.ktx)
            implementation(libs.activity.compose)
            implementation(libs.androidx.appcompat)
        }

        commonTest.dependencies {

        // Add this library later for testing purpose
        // implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "com.weather.forecastify.shared"
    compileSdk = 34
    defaultConfig {
        minSdk = 26
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        jvm("17")
    }
    buildFeatures {
        compose = true
    }
}
dependencies {
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)
}


kotlin.targets.withType<KotlinNativeTarget>().configureEach {
    binaries.framework {
        baseName = "ForecastifyIOS"
        isStatic = true
    }
}