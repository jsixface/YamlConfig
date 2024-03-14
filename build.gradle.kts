plugins {
    `java-library`
}

group = "com.github.jsixface"
version = "1.3.0"

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.snakeyaml)
    testImplementation(libs.junit)
}
