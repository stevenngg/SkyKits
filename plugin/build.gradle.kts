plugins {
    id("com.github.johnrengelman.shadow") version("8.1.1")
    java
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
    mavenCentral()
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20-R0.1-SNAPSHOT")
}

java.toolchain.languageVersion.set(JavaLanguageVersion.of(17))

tasks {
    build {
        dependsOn(shadowJar)
    }
    compileJava {
        options.encoding = "UTF-8"
        options.release.set(17)
    }
    shadowJar {
        archiveFileName.set("SkyKits.jar")
        archiveClassifier.set("")

        destinationDirectory.set(file("$rootDir/bin/"))
    }
    clean {
        delete("$rootDir/bin/")
    }
}