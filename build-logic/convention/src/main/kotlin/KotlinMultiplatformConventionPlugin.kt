import app.skypub.project.configureKotlinAndroid
import app.skypub.project.libs
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class KotlinMultiplatformConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply(libs.findPlugin("kotlinMultiplatform").get().get().pluginId)
            apply(libs.findPlugin("androidLibrary").get().get().pluginId)
//            apply(libs.findPlugin("kotlin.serialization").get().get().pluginId)
        }
        extensions.configure<LibraryExtension>(::configureKotlinAndroid)
    }
}