# KafkaManager

KafkaManager is a tool designed to simplify the usage of Kafka. Initially, it provided a simple
structure to create topics and produce messages in those topics. Now, the project has been
structured following the Clean Architecture pattern.

### Usage

To test the project, run the command

```
./gradlew clean build
```

This will generate a file
named `KafkaManager-1.0-SNAPSHOT-all.jar` in the `build/libs` directory.

To execute the project, run the command

```
java -jar KafkaManager-1.0-SNAPSHOT-all.jar
```

To run the application from a command line:

```
./gradlew run --args="-folder C:\test\models"
```

## Conclusion

KafkaManager is a simple tool that can help users interact with Kafka. Its Clean Architecture
structure allows for easy maintenance and scalability.


### References
* [Gradle - The JaCoCo Plugin](https://docs.gradle.org/8.0/userguide/jacoco_plugin.html)