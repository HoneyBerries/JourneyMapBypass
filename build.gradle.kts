plugins {
	id("net.fabricmc.fabric-loom")
	`maven-publish`
}

repositories {
	// Add repositories to retrieve artifacts from in here.
	// You should only use this when depending on other mods because
	// Loom adds the essential maven repositories to download Minecraft and libraries from automatically.
	// See https://docs.gradle.org/current/userguide/declaring_repositories.html
	// for more information about repositories.
	exclusiveContent {
		forRepository {
			maven("https://api.modrinth.com/maven") {
				name = "Modrinth"
			}
		}
		filter {
			includeGroup("maven.modrinth")
		}
	}
}

loom {
	splitEnvironmentSourceSets()

	mods {
		register("journeymapbypass") {
			sourceSet(sourceSets.main.get())
			sourceSet(sourceSets.getByName("client"))
		}
	}
}

dependencies {
	// To change the versions see the gradle.properties file
	minecraft("com.mojang:minecraft:${providers.gradleProperty("minecraft_version").get()}")
	implementation("net.fabricmc:fabric-loader:${providers.gradleProperty("loader_version").get()}")

	// Fabric API. This is technically optional, but you probably want it anyway.
	implementation("net.fabricmc.fabric-api:fabric-api:${providers.gradleProperty("fabric_api_version").get()}")

	// JourneyMap: compile-only, never bundled or redistributed. This mod only mixes into
	// JourneyMap's classes at runtime on the end user's own installation, which must already
	// have JourneyMap installed separately (see fabric.mod.json depends). This project has no
	// remapping step configured (no "mod*" configurations exist), so JourneyMap's already
	// Mojang-mapped jar is referenced with a plain compile-only dependency, scoped to the
	// "client" source set since InternalStateHandler is client-only.
	"clientCompileOnly"("maven.modrinth:journeymap:${providers.gradleProperty("journeymap_version").get()}")

	// JourneyMap's config-field classes (BooleanField, etc.) reach into its API module for
	// the Config<T> interface. That module isn't published as its own Modrinth artifact -
	// it's only bundled jar-in-jar inside the main jar - so it's extracted here at build time
	// (never committed to the repo) purely to compile against.
	"clientCompileOnly"(files(provider {
		val journeymapJar = configurations.detachedConfiguration(
			dependencies.create("maven.modrinth:journeymap:${providers.gradleProperty("journeymap_version").get()}")
		).resolve().single()

		val extractDir = layout.buildDirectory.dir("journeymapApiJar").get().asFile
		extractDir.mkdirs()
		val outFile = extractDir.resolve("journeymap-api.jar")

		zipTree(journeymapJar).matching { include("META-INF/jars/journeymap-api-*.jar") }.singleFile
			.copyTo(outFile, overwrite = true)

		outFile
	}))
}

tasks.processResources {
	val version = version
	inputs.property("version", version)

	filesMatching("fabric.mod.json") {
		expand("version" to version)
	}
}

tasks.withType<JavaCompile>().configureEach {
	options.release = 25
}

java {
	// Loom will automatically attach sourcesJar to a RemapSourcesJar task and to the "build" task
	// if it is present.
	// If you remove this line, sources will not be generated.
	withSourcesJar()

	sourceCompatibility = JavaVersion.VERSION_25
	targetCompatibility = JavaVersion.VERSION_25
}

tasks.jar {
	val projectName = project.name
	inputs.property("projectName", projectName)

	from("LICENSE") {
		rename { "${it}_$projectName" }
	}
}

// configure the maven publication
publishing {
	publications {
		register<MavenPublication>("mavenJava") {
			from(components["java"])
		}
	}

	// See https://docs.gradle.org/current/userguide/publishing_maven.html for information on how to set up publishing.
	repositories {
		// Add repositories to publish to here.
		// Notice: This block does NOT have the same function as the block in the top level.
		// The repositories here will be used for publishing your artifact, not for
		// retrieving dependencies.
	}
}
