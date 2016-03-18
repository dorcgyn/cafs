# cafs
Download CAF file system.

From the [tutorial](https://help.github.com/articles/fetching-a-remote/)

```
git clone https://github.com/dorcgyn/cafs.git
``` 

## Build Project
```
mvn clean install
```

# Command Line Interface
```
cd target
```

List root directory

```
java -jar hun-1.0-SNAPSHOT-jar-with-dependencies.jar -ls "/"
// Mkdir, only support absolute path currently 
java -jar hun-1.0-SNAPSHOT-jar-with-dependencies.jar -mkdir "<absolute_path>"
```

## Start jetty server
```
mvn jetty:run
```

In browser, go to "http://localhost:8080/ls". 
