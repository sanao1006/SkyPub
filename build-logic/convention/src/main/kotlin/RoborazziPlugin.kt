import app.skypub.convention.libs
import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

@Suppress("unused")
class RoborazziPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            with(pluginManager) {
                apply("io.github.takahirom.roborazzi")
                apply("com.google.devtools.ksp")
            }
            extensions.configure(LibraryExtension::class) {
                testOptions {
                    unitTests {
                        all { test ->
                            test.jvmArgs("-noverify")
                            test.systemProperties["robolectric.logging.enabled"] = "true"
                            test.systemProperties["robolectric.graphicsMode"] = "NATIVE"
                            test.systemProperties["robolectric.pixelCopyRenderMode"] = "hardware"
                            test.maxParallelForks = Runtime.getRuntime().availableProcessors()
                            test.testLogging {
                                events.addAll(
                                    listOf(
                                        TestLogEvent.STARTED,
                                        TestLogEvent.PASSED,
                                        TestLogEvent.SKIPPED,
                                        TestLogEvent.FAILED
                                    )
                                )
                                showCauses = true
                                showExceptions = true
                                exceptionFormat = TestExceptionFormat.FULL
                            }
                        }
                    }
                }

                sourceSets.apply {
                    dependencies {
                        add(
                            "testImplementation",
                            libs.findLibrary("androidxTestEspressoEspressoCore").get()
                        )
                        add("testImplementation", libs.findLibrary("junit").get())
                        add("testImplementation", libs.findLibrary("robolectric").get())
                        add("testImplementation", libs.findLibrary("androidxTestExtJunit").get())
                        add("testImplementation", libs.findLibrary("roborazzi").get())
                        add("testImplementation", libs.findLibrary("roborazziCompose").get())
                        add(
                            "testImplementation",
                            libs.findLibrary("composablePreviewScanner").get()
                        )
                        add(
                            "testImplementation",
                            libs.findLibrary("composablePreviewScannerJvm").get()
                        )
                        add(
                            "testImplementation",
                            libs.findLibrary("roborazziPreviewScannerSupport").get()
                        )
                    }
                }
            }

        }
    }
}