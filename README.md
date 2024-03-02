# JPF project template for Gradle
This project contains a template for using [JPF](http://babelfish.arc.nasa.gov/trac/jpf/wiki) with [Gradle](http://www.gradle.org/).
Particularly, it contains a build script that configures JPF and its dependencies, 
and a sample class that can be used to run JPF on a simple Java program.

## Usage
The structure of the project is as follows:
- `src/main/java`: contains the Java program to be analyzed
- `src/main/jpf`: contains the JPF configuration file
- `lib`: contains the JPF libraries used by the project to analyze the Java program

The gradle build script create a task for each JPF configuration file in the `src/main/jpf` directory.
The task name is `run<ConfigName>Verify`, where `<ConfigName>` is the name of the configuration file.
For instance, `src/main/jpf/MyConfig.jpf` can be run with the command `./gradle runMyConfigVerify`.

## How it works
It uses the gradle toolchain to use the right JDK (8) and run the JPF analysis.
