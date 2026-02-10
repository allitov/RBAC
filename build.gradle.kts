plugins {
    id("java")
    id("com.diffplug.spotless") version "8.2.1"
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

configure<JavaPluginExtension> {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

configure<com.diffplug.gradle.spotless.SpotlessExtension> {
    java {
        target("src/**/*.java")
        removeUnusedImports()
        trimTrailingWhitespace()
        endWithNewline()
        palantirJavaFormat("2.86.0")
    }
}

tasks.test {
    useJUnitPlatform()
}

tasks.register<Copy>("copyGitHooks") {
    description = "Copies git hooks from project directory to .git/hooks"
    group = "git"

    val gitDir = File("$rootDir/.git")

    onlyIf { gitDir.exists() }

    from("$rootDir/git/hooks")
    into("${gitDir.path}/hooks")

    filePermissions {
        user { read = true; write = true; execute = true }

        group { read = true; execute = true }

        other { read = true; execute = true }
    }

    doLast {
        println("Git hooks installed successfully into ${gitDir.path}/hooks")
        file("$rootDir/git/hooks").listFiles()?.forEach {
            println("   -> Enabled hook: ${it.name}")
        }
    }
}

tasks.maybeCreate("prepareKotlinBuildScriptModel").dependsOn("copyGitHooks")
