dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("org.springframework.batch:spring-batch-test")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}