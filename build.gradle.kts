
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


// Utility function for verification through JPF

val verificationGroup = "Verification"

val cleanAll by tasks.register<DefaultTask>("cleanAllJPF") {
    group = verificationGroup
    description = "Clean all the JPF containers in running"
}

val verifyAll by tasks.register<DefaultTask>("runAllJPF") {
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

/**
 * This function will mount a new container with the name passed
 * The container will be mounted with all the files and folders of the project, except the build folder
 * The container will be detached and will run the sleep infinity command to keep it alive and accept the exec command
 */
fun mountNewContainer(toMount: Array<String>, name: String, out: OutputStream) = exec {
    commandLine("docker", "run", "--name", name,
        *toMount,
        "-d", "gianlucaaguzzi/pcd-jpf:latest", "sleep", "infinity")
    isIgnoreExitValue = true
    standardOutput = out
}

// If any problem persists, type: docker container ls --quiet | xargs -I{} docker container kill {}
fun cleanOldInstances(name: String, out: OutputStream) = exec {
    commandLine("docker", "container", "rm", name, "--force")
    isIgnoreExitValue = true
    standardOutput = out
}

// Effectively run the container to verify the fileName passed. The fileName should be a jpf file
fun runJPF(name: String, fileName: String): () -> ExecResult = {
    exec { commandLine("ls") }
    exec { commandLine("docker", "exec", name, "./gradlew", "build") }
    exec { commandLine("docker", "exec", name, "java", "-jar", "/usr/lib/JPF/jpf-core/build/RunJPF.jar", fileName)}
}

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
        fun launchVerificationTask(taskName: String, file: File) = tasks.register<Task>(taskName) {
            group = verificationGroup
            description = "Verify the ${file.nameWithoutExtension} using JPF"
            doFirst {
                // Get the container in execution
                val stdout = ByteArrayOutputStream()
                // Get all the folders and file of this project and bind them with the docker image
                // Get the container in execution
                exec {
                    commandLine("docker", "container", "ps")
                    standardOutput = stdout
                }
                // If there isn't the project container, the process should clean the environment (i.e. kill the previous container and starts a new one)
                if (!stdout.toString().contains(file.nameWithoutExtension)) {
                    cleanOldInstances(file.nameWithoutExtension, stdout)
                    mountNewContainer(allFileButBuildAndHide, file.nameWithoutExtension, stdout)
                }
            }
            doLast {
               runJPF(file.nameWithoutExtension, ".${searchingPath}" + file.name)()
            }
        }
        fun clean(taskName: String, fileName: String) = tasks.register<Task>(taskName) {
            group = verificationGroup
            description = "Verify the ${fileName} using JPF"
            doLast {
                println("Cleaning the $fileName container")
                cleanOldInstances(fileName, noOutput)
            }
        }
        val capitalizedName = it.nameWithoutExtension.capitalize()
        val jpfVerification by launchVerificationTask("run${capitalizedName}Verify", it)
        val jpfClean by clean("run${capitalizedName}Clean", capitalizedName)
        cleanAll.dependsOn(jpfClean)
        verifyAll.dependsOn(jpfVerification)
    }

/**
 * This task will run the verification of the jpf file passed with the -Pfile parameter
 * The file should be a jpf file
 * If the container is not running, it will mount a new one
 * If the container is running, it will use the existing one
 * To clean the container, please type: docker container rm jpf_run_<projectName> --force
 */
tasks.register("jpfVerify") {
    group = "Verification"
    description = "Run JPF verification with ./gradlew jpfVerify /path/of/your/jpf"

    doFirst {
        if(!properties.containsKey("file")) {
            error("""you have to pass the file that you want to verify. Use: -Pfile="/your/path/file.jpf" """)
        }
        val stdout = ByteArrayOutputStream()
        // Get all the folders and file of this project and bind them with the docker image
        // NB! .gradle and build should not be included, the compile process should be done with the internal jdk
        val toMount: Array<String> = allFileButBuildAndHide
        // Get the container in execution
        exec {
            commandLine("docker", "container", "ps")
            standardOutput = stdout
        }
        // If there isn't the project container, the process should clean the environment (i.e. kill the previous container and starts a new one)
        if(!stdout.toString().contains("jpf_run_${project.name}")) {
            cleanOldInstances(project.name, stdout)
            mountNewContainer(toMount, project.name, stdout)
        }
    }

    doLast {
        runJPF(project.name, properties["file"].toString())()
    }
}