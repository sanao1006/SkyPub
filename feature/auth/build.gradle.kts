plugins {
    id("app.skypub.convention.kotlinMultiplatform")
    id("app.skypub.convention.composeMultiplatform")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.voyager)
            implementation(projects.core.data)
            implementation(projects.core.network)
            implementation(projects.core.datastore)
            implementation(projects.feature.home)
        }
    }
}
