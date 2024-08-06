package app.skypub.convention

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal fun Project.configureKotlinMultiplatform(
    extension: KotlinMultiplatformExtension
) = extension.apply {
    jvmToolchain(11)

    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

//    jvm("desktop")

//    @OptIn(ExperimentalWasmDsl::class)
//    wasmJs { browser() }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    )

    applyDefaultHierarchyTemplate()

    sourceSets.apply {
        commonMain {
            dependencies {
                implementation(libs.findLibrary("kotlinx.coroutines.core").get())
                implementation(libs.findLibrary("kotlinx.serialization").get())
                implementation(libs.findLibrary("androidx.viewmodel").get())

                api(libs.findLibrary("koin.core").get())
                api(libs.findLibrary("koin.compose").get())
                api(libs.findLibrary("koin.compose.viewmodel").get())
                api(libs.findLibrary("arrow-core").get())
                api(libs.findLibrary("arrow-fx-coroutines").get())
                implementation(libs.findLibrary("napier").get())
            }

            androidMain {
                dependencies {
                    implementation(libs.findLibrary("koin.android").get())
                    implementation(libs.findLibrary("koin.androidx.compose").get())
                    implementation(libs.findLibrary("kotlinx.coroutines.android").get())
                }
            }
        }
    }
}