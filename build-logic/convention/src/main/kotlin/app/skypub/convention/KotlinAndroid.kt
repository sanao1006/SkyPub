package app.skypub.convention

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project

internal fun Project.configureKotlinAndroid(
    extension: LibraryExtension
) = extension.apply {
    compileSdk = libs.findVersion("android.compileSdk").get().requiredVersion.toInt()

    val moduleName = path.split(":").drop(2).joinToString(".")
    namespace = if(moduleName.isNotEmpty()) "app.skypub.project.$moduleName" else "app.skypub.project"

    defaultConfig {
        minSdk = libs.findVersion("android.minSdk").get().requiredVersion.toInt()
        consumerProguardFiles("consumer-proguard-rules.pro")
        testOptions {
            targetSdk = libs.findVersion("android.targetSdk").get().requiredVersion.toInt()
        }
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}