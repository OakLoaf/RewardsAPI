plugins {
    id("maven-publish")
}

dependencies {
    compileOnly("org.spigotmc:spigot:${findProperty("minecraftVersion")}-R0.1-SNAPSHOT")
    compileOnly("org.geysermc.floodgate:api:${findProperty("floodgateVersion")}-SNAPSHOT")

    implementation("org.lushplugins:LushLib:${findProperty("lushlibVersion")}")
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