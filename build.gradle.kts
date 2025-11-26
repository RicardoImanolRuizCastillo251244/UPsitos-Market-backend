plugins {
    id("application")
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}


group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    implementation("com.mysql:mysql-connector-j:9.3.0")
    implementation("com.zaxxer:HikariCP:5.1.0")
    implementation("io.github.cdimascio:dotenv-java:3.0.0")
    implementation("io.javalin:javalin-bundle:6.6.0")
    implementation("org.slf4j:slf4j-simple:2.0.16")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.2")

    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

    implementation("com.password4j:password4j:1.7.3")
}

application {
    mainClass.set("org.example.Main")
}

tasks{
    shadowJar {
        archiveBaseName.set("UPsitosMarket")
        archiveVersion.set("")
        archiveClassifier.set("")

        manifest {
            attributes["Main-Class"] = "org.example.Main"
        }
    }
}

tasks.test {
    useJUnitPlatform()
}