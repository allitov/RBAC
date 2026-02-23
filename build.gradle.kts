plugins {
    id("java")
    id("com.diffplug.spotless") version "8.2.1"
}

repositories {
    mavenCentral()
}

val commonsLangVersion = "3.20.0"
val jetbrainsAnnotationsVersion = "26.1.0"
val assertJVersion = "3.27.7"
val mockitoVersion = "5.21.0"

dependencies {
    implementation("org.apache.commons:commons-lang3:$commonsLangVersion")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core:$assertJVersion")
    testImplementation("org.mockito:mockito-junit-jupiter:$mockitoVersion")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    compileOnly("org.jetbrains:annotations:$jetbrainsAnnotationsVersion")
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
