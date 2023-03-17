# JPF project template for Gradle
This project contains a template for using [JPF](http://babelfish.arc.nasa.gov/trac/jpf/wiki) with [Gradle](http://www.gradle.org/).
Particularly, it contains a build script that downloads and configures JPF and its dependencies, 
and a sample class that can be used to run JPF on a simple Java program.

## Usage
The structure of the project is as follows:
- `src/main/java`: contains the Java program to be analyzed
- `src/main/jpf`: contains the JPF configuration file
- `lib`: contains the JPF libraries used by the project to analyze the Java program

The gradle build script create a task for each JPF configuration file in the `src/main/jpf` directory.
The task name is `run<ConfigName>Verify`, where `<ConfigName>` is the name of the configuration file.
For instance, `src/main/jpf/MyConfig.jpf` can be run with the command `./gradle runMyConfigVerify`.

After running the analysis, it is possible to clean the container created using the command `./gradle cleanJPF`.

If you want to run the analysis using a jpf located in another folder, 
you can use the task `jpfVerify -Pfile=<path to jpf>`.

Internally, it uses the docker image shown in class: gianlucaaguzzi/pcd-jpf:latest

## How it works
Each task will use the same container in which there is the copy of the current folder, 
and then it will run the JPF analysis inside it.
Particularly, the docker container will be created and will mount the current directory.
It will compile your project with java 8 and gradle, and then it will run the JPF analysis.
The first time it will download the `gradlew` associated with the project, so it could be slow.
The docker container persists after the analysis is completed, so that the user can inspect the results 
and can re-run the analysis with different java files and jpf files.

## Troubleshooting
If you change the project structure adding new folder, you need to shut down the container using:
- `./gradlew cleanJPF`

or
- ```docker container ls --quiet | xargs -I{} docker container kill {}```
