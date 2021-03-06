plugins {
    id("org.jetbrains.kotlin.multiplatform")
}

kotlin {
    js {
        browser {}
    }

    jvm {
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(kotlin("stdlib-common"))
            }
        }

//        commonTest {
//            dependencies {
//                implementation kotlin('test-common')
//                implementation kotlin('test-annotations-common')
//            }
//        }
    }
}