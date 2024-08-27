plugins {
    id("app.skypub.convention.kotlinMultiplatform")
    id("app.skypub.convention.composeMultiplatform")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.voyager)
            api(projects.core.common)
            api(projects.core.network)
        }
    }
}