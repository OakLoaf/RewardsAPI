plugins {
    id("java")
    id("maven-publish")
    id("com.github.johnrengelman.shadow") version("8.1.1")
}

allprojects {
    group = "org.lushplugins"
    version = "0.1"

    repositories {
        mavenCentral()
        mavenLocal()
        maven(url = "https://oss.sonatype.org/content/repositories/snapshots/")
        maven(url = "https://hub.spigotmc.org/nexus/content/repositories/snapshots/") // Spigot
        maven(url = "https://repo.smrt-1.com/releases/") // ChatColorHandler
        maven(url = "https://repo.smrt-1.com/snapshots/") // LushLib
        maven(url = "https://repo.opencollab.dev/main/") // Floodgate
    }
}

dependencies {
    // The below should be passed from the api (unsure why they are not)
    compileOnly("org.spigotmc:spigot:${findProperty("minecraftVersion")}-R0.1-SNAPSHOT")
    compileOnly("org.lushplugins:LushLib:${findProperty("lushlibVersion")}")

    implementation(project(":api"))
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    shadowJar {
        minimize()

        val folder = System.getenv("pluginFolder_1-20")
        if (folder != null) destinationDirectory.set(file(folder))
        archiveFileName.set("${project.name}-${project.version}.jar")
    }

    processResources{
        expand(project.properties)

        inputs.property("version", rootProject.version)
        filesMatching("plugin.yml") {
            expand("version" to rootProject.version)
        }
    }
}