plugins {
    id("app.skypub.convention.kotlinMultiplatform")
    id("app.skypub.convention.composeMultiplatform")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(libs.sketch.compose)
        }
    }
}
