plugins {
    `kotlin-dsl`
}

group = "app.skypub.project.buildlogic"
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
}

fun Provider<PluginDependency>.toDep() = map {
    "${it.pluginId}:${it.pluginId}.gradle.plugin:${it.version}"
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

gradlePlugin {
    plugins {
        register("kotlinMultiplatform") {
            id = "app.skypub.convention.kotlinMultiplatform"
            implementationClass = "KotlinMultiplatformConventionPlugin"
        }
        register("composeMultiplatform") {
            id = "app.skypub.convention.composeMultiplatform"
            implementationClass = "ComposeMultiPlatformConventionPlugin"
        }
        register("roborazzi") {
            id = "app.skypub.convention.roborazzi"
            implementationClass = "RoborazziPlugin"
        }
    }
}
