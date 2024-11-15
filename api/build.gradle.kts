plugins {
    id("java")
    id("maven-publish")
    id("com.github.johnrengelman.shadow") version("8.1.1")
}

dependencies {
    compileOnly("org.spigotmc:spigot:${findProperty("minecraftVersion")}-R0.1-SNAPSHOT")
    compileOnly("org.geysermc.floodgate:api:${findProperty("floodgateVersion")}-SNAPSHOT")

    implementation("org.lushplugins:LushLib:${findProperty("lushlibVersion")}")
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    shadowJar {
        relocate("org.lushplugins.lushlib", "org.lushplugins.rewardsapi.libs.lushlib")

        minimize()

        archiveFileName.set("${rootProject.name}-${project.name}-${project.version}.jar")
    }
}

publishing {
    repositories {
        maven {
            name = "lushReleases"
            url = uri("https://repo.lushplugins.org/releases")
            credentials(PasswordCredentials::class)
            authentication {
                isAllowInsecureProtocol = true
                create<BasicAuthentication>("basic")
            }
        }

        maven {
            name = "lushSnapshots"
            url = uri("https://repo.lushplugins.org/snapshots")
            credentials(PasswordCredentials::class)
            authentication {
                isAllowInsecureProtocol = true
                create<BasicAuthentication>("basic")
            }
        }
    }

    publications {
        create<MavenPublication>("maven") {
            groupId = rootProject.group.toString()
            artifactId = rootProject.name
            version = rootProject.version.toString()
            from(project.components["java"])
        }
    }
}