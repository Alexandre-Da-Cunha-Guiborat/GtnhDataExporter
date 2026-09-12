plugins {
    id("com.gtnewhorizons.gtnhconvention")
}

tasks.withType<JavaExec>().configureEach {
    jvmArgs("-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005")
}
