dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("ch.qos.logback.contrib", "logback-json-classic", "0.1.5")
    implementation("ch.qos.logback.contrib", "logback-jackson", "0.1.5")
    testImplementation("org.springframework.batch:spring-batch-test")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}