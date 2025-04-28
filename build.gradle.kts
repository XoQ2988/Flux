import java.net.URI

plugins {
    id("fabric-loom") version "1.10-SNAPSHOT"
    id("maven-publish")
}

base {
    version = properties["mod_version"] as String
    group = properties["maven_group"] as String
    archivesName.set(properties["archives_base_name"] as String)
}

repositories {
    mavenCentral()
    maven { url = URI("https://maven.fabricmc.net/") }
    maven { url = URI("https://api.modrinth.com/maven") }
}

dependencies {
    // Minecraft & Fabric
    minecraft("com.mojang:minecraft:${properties["minecraft_version"] as String}")
    mappings("net.fabricmc:yarn:${properties["yarn_mappings"] as String}:v2")
    modImplementation("net.fabricmc:fabric-loader:${properties["loader_version"] as String}")

    // Fabric API
    // modApi("net.fabricmc.fabric-api:fabric-api:${property("fabric_version") as String}")
}

tasks.processResources {
    val props = mapOf(
        "version"           to project.version,
        "minecraft_version" to project.property("minecraft_version"),
        "loader_version"    to project.property("loader_version")
    )
    inputs.properties(props)
    filesMatching("fabric.mod.json") { expand(props) }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.release.set(17)
}