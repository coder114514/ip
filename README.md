# TobTahc

TobTahc is a chatbot project built from the _Duke_ template for a greenfield Java project.

## User Guide

* [Local Documentation](docs/README.md)
* [Deployed User Guide](https://coder114514.github.io/ip)

---

## Setting up in Intellij

**Prerequisites:** JDK 17, latest version of Intellij IDEA.

1. Open Intellij. If the welcome screen is not visible, click `File` > `Close Project` to close the existing project first.
1. Import the project into Intellij as follows:
   1. Click `Open`.
   1. Select the project directory and click `OK`.
   1. If there are any further prompts, accept the defaults.
1. Configure the project to use **JDK 17** (not other versions) as explained [here](https://www.jetbrains.com/help/idea/sdk.html#set-up-jdk).
1. In the same dialog, set the **Project language level** field to `SDK default`.
1. Run the application:
   * Locate `src/main/java/tobtahc/Main.java`.
   * Right-click the file and choose `Run Main.main()`.
   * If compile errors appear, try restarting the IDE.

> [!Warning]
> Keep the `src/main/java` folder as the root for Java files. Do not rename or move files outside this path, as Gradle and other tools expect this structure.

---

## Build / Package / Run (CLI)

Run checks (tests + checkstyle):

```sh
./gradlew check
```

Build the runnable fat jar:

```sh
./gradlew shadowJar
```

Run:

```sh
java -jar build/libs/tobtahc.jar
```

Options:

```
Usage: java -jar tobtahc.jar [options]

Options:
  -g, --gui, /gui      Start the application in GUI mode (default)
  -c, --cli, /cli      Start the application in CLI mode
  -h, --help, /?       Show this help message
```
