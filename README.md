# loyalApp
This app can connect to defined URL (see application.yml) and collect data with provided key.
Data collection goes in background, only one collection process can be launched at a time, others will stay in a queue.
You can check status of download or stop it and lose all collected data.

Tech: Spring Boot, JUnit4/Mockito

Built-in Tomcat works on 9999 port (check application.yml), Swagger can be found on standard /swagger-ui.html# endpoint

## Endpoints

/films/check - checks status of a current collection process, returns progress in percents.

/films/genres - downloads list of a film genres and their id's

/films/ratingplain - requires genre id and calculates plain average rating of all films of this genre

/films/ratingweighted - requires genre id and calculates weighted average rating of all films of this genre

/films/stop - stops current download.

/swagger-ui.html - standard Swagger endpoint
