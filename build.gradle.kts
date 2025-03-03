
import org.gradle.configurationcache.extensions.capitalized
import java.io.ByteArrayOutputStream
import java.io.OutputStream

plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    implementation(files("lib/jpf-classes.jar"))
    implementation(files("lib/jpf-annotations.jar"))
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
        vendor.set(JvmVendorSpec.ADOPTIUM)
    }
}

// Utility function for verification through JPF

val verificationGroup = "Verification"

val verifyAll by tasks.register<DefaultTask>("runVerifyAll") {
    group = verificationGroup
    description = "Run all the JPF verification"
}

/**
 * This function will return an array of string that will be used to mount the container
 * The container will be mounted with all the files and folders of the project, except the build folder
 */
val allFileButBuildAndHide = File(rootProject.rootDir.path)
    .listFiles { a -> !(a.name.startsWith(".") || a.name == "build") }
    .map { it -> "type=bind,source=${it.absolutePath},target=/home/${it.name}" }
    .flatMap { it ->  listOf("--mount", it) }
    .toTypedArray()

// Path in which the search of other jpf file starts
val searchingPath = "/src/main/jpf/"

// Output for all tasks
val noOutput = ByteArrayOutputStream()
/**
 * This will create a task for each jpf file in the src/main/jpf folder
 * Particularly, it will create two tasks called run<FileName>Verify and run<FileName>Clean
 * The first one will run the verification, the second one will clean the container
 * With cleanAll you can clean all the containers, you can do that when you want clean the environment
 * If you have a jpf in another path, then it is better to use the jpfVerify task
 */
File(rootProject.rootDir.path + searchingPath).listFiles()
    ?.filter { it.extension == "jpf" }
    ?.sortedBy { it.nameWithoutExtension }
    ?.forEach {
        fun launchVerificationTask(taskName: String, file: File) = tasks.register<JavaExec>(taskName) {
            group = verificationGroup
            description = "Verify the ${file.nameWithoutExtension} using JPF"
            javaLauncher.set(
                javaToolchains.launcherFor {
                    languageVersion.set(JavaLanguageVersion.of(11))
                }
            )
            main = "-jar"
            args = listOf(
                "-jar",
                "--add-opens", "java.base/jdk.internal.misc=ALL-UNNAMED",
                "./jpf-runner/build/RunJPF.jar", ".${searchingPath}" + file.name
            )
            //args = listOf("./jpf-runner/build/RunJPF.jar", ".${searchingPath}" + file.name)
        }
        val capitalizedName = it.nameWithoutExtension.capitalize()
        val jpfVerification by launchVerificationTask("run${capitalizedName}Verify", it)
        jpfVerification.dependsOn(tasks.getByName("compileJava"))
        verifyAll.dependsOn(jpfVerification)
    }
