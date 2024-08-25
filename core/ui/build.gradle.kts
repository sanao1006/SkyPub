plugins {
    id("app.skypub.convention.kotlinMultiplatform")
    id("app.skypub.convention.composeMultiplatform")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(libs.sketch.compose)
            api(projects.core.common)
            api(projects.core.data)
            api(projects.core.datastore)
            implementation(libs.voyager)
        }
    }
}
