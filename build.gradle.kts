plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version("8.1.1")
}

dependencies {
    // The below should be passed from the api (unsure why they are not)
    compileOnly("org.spigotmc:spigot:${findProperty("minecraftVersion")}-R0.1-SNAPSHOT")

    // Libraries
    implementation("org.lushplugins:LushLib:${findProperty("lushlibVersion")}")

    // Projects
    implementation(project(":api"))
}


allprojects {
    apply(plugin="java")
    apply(plugin="com.github.johnrengelman.shadow")

    group = "org.lushplugins"
    version = "0.3.3"

    repositories {
        mavenCentral()
        mavenLocal()
        maven("https://oss.sonatype.org/content/repositories/snapshots/")
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") // Spigot
        maven("https://repo.lushplugins.org/releases/") // ChatColorHandler
        maven("https://repo.lushplugins.org/snapshots/") // LushLib
        maven("https://repo.opencollab.dev/main/") // Floodgate
    }

    java {
        toolchain.languageVersion.set(JavaLanguageVersion.of(17))

        registerFeature("optional") {
            usingSourceSet(sourceSets["main"])
        }

        withSourcesJar()
    }

    tasks {
        withType<JavaCompile> {
            options.encoding = "UTF-8"
        }

        shadowJar {
            relocate("org.lushplugins.lushlib", "org.lushplugins.rewardsapi.libs.lushlib")

            minimize()

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
}