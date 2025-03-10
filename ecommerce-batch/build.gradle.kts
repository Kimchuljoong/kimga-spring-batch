dependencies {
    implementation("org.springframework.boot:spring-boot-starter-batch")
    implementation("org.apache.commons:commons-csv:1.11.0")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("io.micrometer:micrometer-registry-prometheus-simpleclient:1.13.4")
    implementation("io.prometheus:simpleclient_pushgateway:0.16.0")

    testImplementation("org.springframework.batch:spring-batch-test")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}