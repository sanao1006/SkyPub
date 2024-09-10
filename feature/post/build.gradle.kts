plugins {
    id("app.skypub.convention.kotlinMultiplatform")
    id("app.skypub.convention.composeMultiplatform")
}
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.voyager)
            implementation(projects.core.data)
            api(projects.core.network)
            api(libs.sketch.compose)
        }
    }
}
