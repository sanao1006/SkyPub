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
            api(projects.core.network)
        }
    }
}
