import com.android.build.gradle.api.ApplicationVariant
import com.android.build.gradle.api.BaseVariantOutput
import com.android.build.gradle.internal.api.BaseVariantOutputImpl
import org.apache.tools.ant.helper.DefaultExecutor
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.hilt.plugin)
    alias(libs.plugins.gms.google.services)
    alias(libs.plugins.crashlytics)
    id("kotlin-kapt")
}

val versionCode = 1
val versionName = "1.0"

android {
    namespace = "com.weather.forecastify"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.weather.forecastify"
        minSdk = 26
        targetSdk = 34
        versionCode = versionCode
        versionName = versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        register("release") {
            storePassword = "Mahmood"
            keyAlias = "key0"
            keyPassword = "Mahmood"
            storeFile = file("./forecastify_key_store.jks")
        }
    }


    buildTypes {
        getByName("release") {
            isDebuggable = false
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    kapt {
        correctErrorTypes = true
    }

    // Both of these below work

    // 1st Method
    //applicationVariants.all(ApplicationVariantAction())

    // 2nd Method
    applicationVariants.all {
        val variant = this
        variant.outputs
            .map { it as BaseVariantOutputImpl }
            .forEach { output ->
                val SEP = "_"
                val date = Date()
                val dateFormat = SimpleDateFormat("dd_MM_yy_hh_mm")
                val formattedDate = dateFormat.format(date)
                var gitBranch = getGitBranch()
                gitBranch = gitBranch.take(gitBranch.length - 1)
                var apkName = variant.name + SEP + versionCode + SEP + gitBranch + SEP + formattedDate + ".apk"
                val regex = Regex("[^A-Za-z0-9_-]")
                apkName.replace(regex, "")
                output.outputFileName = apkName
            }
    }
}

/*
class ApplicationVariantAction : Action<ApplicationVariant> {
    override fun execute(variant: ApplicationVariant) {
        val fileName = createFileName(variant)
        variant.outputs.all(VariantOutputAction(fileName))
    }

    private fun createFileName(variant: ApplicationVariant): String {
        val SEP = "_"
        val date = Date()
        val dateFormat = SimpleDateFormat("dd_MM_yy_hh_mm")
        val formattedDate = dateFormat.format(date)
        var gitBranch = getGitBranch()
        gitBranch = gitBranch.take(gitBranch.length - 1)
        val apkName = variant.name + SEP + versionCode + SEP + gitBranch + SEP + formattedDate + ".apk"

        return "Forecastify$SEP$apkName"
    }

    private fun getDateTimeFormat(): String {
        val simpleDateFormat = SimpleDateFormat("yyMdHms", Locale.US)
        return simpleDateFormat.format(Date())
    }

    class VariantOutputAction(
        private val fileName: String
    ) : Action<BaseVariantOutput> {
        override fun execute(output: BaseVariantOutput) {
            if (output is BaseVariantOutputImpl) {
                output.outputFileName = fileName
            }
        }
    }
}
*/

fun getGitBranch(): String {
    var gitBranch = "--"
    var gitCommitId = "--"

    try {
        val stdout = ByteArrayOutputStream()
        exec {
            commandLine("git", "rev-parse", "--abbrev-ref", "HEAD")
            standardOutput = stdout
        }

        gitBranch = "$stdout"
    } catch (e: Exception) {
        e.printStackTrace()
        gitBranch = "--"
        gitCommitId = "--"
    }
    return gitBranch.replace("/", "-")
}

fun getGitHash(): ByteArrayOutputStream {
    val stdout = ByteArrayOutputStream()
    exec {
        commandLine("git", "rev-parse", "--short", "HEAD")
        standardOutput = stdout
    }
    return stdout
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.aar", "*.jar"))))

    implementation(projects.coreNetwork)
    implementation(projects.coreNetwork)
    implementation(projects.connectivityAndroid)
    implementation(projects.data)
    implementation(projects.domain)

    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    implementation(libs.androidx.material3.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.ui.test.junit4)
    androidTestImplementation(platform(libs.compose.bom))
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)

    implementation(libs.kotlinx.serialization.json)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.hilt.android.gradle.plugin)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.navigation.compose)


    kapt(libs.room.compiler)
    implementation(libs.room.paging)
    implementation(libs.paging.runtime)
    implementation(libs.paging.compose)
    implementation(libs.landscapist.glide)


    implementation(libs.kotlin.serialization)

    implementation(libs.coil.compose)
    implementation(libs.coil.gif)


    implementation(libs.play.services.location)
    implementation(libs.accompanist.permissions)


    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)


    implementation(libs.ycharts)
    implementation(libs.kotlinx.collections.immutable)

    implementation(libs.lottie.compose)


    implementation(project.dependencies.platform(libs.koin.bom))
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.koin.android.compat)
    implementation(libs.koin.androidx.work.manager)
    implementation(libs.koin.androidx.navigation)
    implementation(libs.koin.compose)
    implementation(libs.koin.compose.viewmodel)
    implementation(libs.koin.compose.viewmodel.navigation)
//    implementation(libs.koin.androidx.startup)



    testImplementation(libs.koin.test)
    testImplementation(libs.koin.test.junit4)

}