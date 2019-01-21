# loyalApp
This app can connect to defined URL (see application.yml) and collect data with provided key.
Data collection goes in background, only one collection process can be launched at a time, others will stay in a queue.
You can check status of download or stop it and lose all collected data.

Tech: Spring Boot, JUnit4/Mockito

Built-in Tomcat works on 9999 port (can be changed in application.yml on `server:port`), Swagger can be found on standard /swagger-ui.html# endpoint

## Building

Make simple `mvn clean package` for build an executable jar file. You can add `-DskipTests` for skipping tests (they can take up to 10 minutes depending on speed of your internet connection).

## Endpoints

/films/check - checks status of a current collection process, returns progress in percents.

/films/genres - downloads list of a film genres and their id's

/films/ratingplain - requires genre id and calculates plain average rating of all films of this genre

/films/ratingweighted - requires genre id and calculates weighted average rating of all films of this genre

/films/stop - stops current download.

/swagger-ui.html - standard Swagger endpoint

## Testing

You can check ApplicationTest.java for integration tests that shows functionality of an app.

Tests are:
* receiving lists of genres 
* calculation of plain average for documentary films (id = 99) genre, status of calculation will be shown every 5 seconds
* calculation of weighted average for documentary films (id = 99) genre, status of calculation will be shown every 5 seconds
* stopping of calculation plain average after 10 seconds after start
* stopping of calculation weighted average after 10 seconds after start
