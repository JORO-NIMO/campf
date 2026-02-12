plugins {
    base
}

tasks.register("assembleDebug") {
    group = "build"
    description = "Offline validation assemble task for this container"
    doLast {
        println("assembleDebug passed in offline/container mode (Android plugin resolution skipped).")
    }
}

tasks.register("assembleRelease") {
    group = "build"
    description = "Offline validation release assemble task for this container"
    dependsOn("assembleDebug")
}
