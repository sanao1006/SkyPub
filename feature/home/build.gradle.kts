plugins {
    id("app.skypub.convention.kotlinMultiplatform")
    id("app.skypub.convention.composeMultiplatform")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.voyager)
            implementation(projects.core.data)
            implementation(projects.feature.post)
            implementation(projects.core.ui)
            api(libs.sketch.compose)
            api(projects.core.network)
            api(projects.feature.navigation)
            api(projects.core.common)
        }
    }
}
