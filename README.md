### Description

This is a set of tools to connect to SonarQube's instance API for testing the project status since a given time.

Implementations in Java and Python are provided

Time is spected to be in ISO format with offset. On any *nix console this can be obtained with

```bash
date -Iseconds
```

#### Java

This is a runnable jar 

```bash
 java -jar sonar-build-breaker-1.0.0-jar-with-dependencies.jar URL TOKEN PROJECT_KEY SINCE_TIME [RETRIES [SLEEP_TIME]]
```


By default RETRIES is set to 6 and SLEEP_TIME to 30000 (30 sec) for a total timeout of 3 minutes.


#### Python

This script works in a similar manner to the Java version, except that it does not have a timeout yet.

The script is written in Python 3


```bash
 ./sonar-build-breaker.py  URL TOKEN PROJECT_KEY SINCE_TIME
```

