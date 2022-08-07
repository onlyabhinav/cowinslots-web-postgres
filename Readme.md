
# Cowinslots with Web, War, Postgres

## Configure 

### Update Below properties in ```application.properties```

```
spring.datasource.platform=postgres
spring.datasource.url=jdbc:postgresql://localhost:5432/abhin
spring.datasource.username=abhin
spring.datasource.password=****
```


## Build

### Run below command to perform maven build. 

```
mvn clean install  -DskipTests
```


## Run

Either of below run
``` mvn spring-boot:run ```

OR

``` java -jar cowintslots-0.0.1-SNAPSHOT.war ```


OR

Depoy on Tomcat  (Tested on Tomcat v9.0)


