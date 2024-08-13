plugins {
    id("app.skypub.convention.kotlinMultiplatform")
    id("app.skypub.convention.composeMultiplatform")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.voyager)
            api(projects.core.data)
            api(projects.core.network)
            api(projects.core.ui)
            api(projects.feature.navigation)
            api(projects.core.common)
        }
    }
}
