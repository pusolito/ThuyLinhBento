plugins {
    id("org.jetbrains.kotlin.js")
}

kotlin {
    target {
        compilations.all {
            kotlinOptions {
                moduleKind = "umd"
            }
        }
        browser {
        }
    }

    val doodleVersion = "0.3.0"

    dependencies {
        implementation(project(":models"))
        implementation("org.jetbrains.kotlin:kotlin-stdlib-js")

        // day la phan import doodle setup truoc khi bat dau xu ly doodle ben trong script kotlin
        implementation("io.nacular.doodle:core:$doodleVersion")
        implementation("io.nacular.doodle:browser:$doodleVersion")

        // Optional
        // implementation "io.nacular.doodle:controls:$doodle_version"
        // implementation "io.nacular.doodle:animation:$doodle_version"
        // implementation "io.nacular.doodle:themes:$doodle_version"
    }
}