import app.skypub.convention.configureKotlinAndroid
import app.skypub.convention.configureKotlinMultiplatform
import app.skypub.convention.libs
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KotlinMultiplatformConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply(libs.findPlugin("kotlinMultiplatform").get().get().pluginId)
            apply(libs.findPlugin("androidLibrary").get().get().pluginId)
//            apply(libs.findPlugin("kotlin.serialization").get().get().pluginId)
        }
        extensions.configure<LibraryExtension>(::configureKotlinAndroid)
        extensions.configure<KotlinMultiplatformExtension> {
            configureKotlinMultiplatform(this)
        }
    }
}