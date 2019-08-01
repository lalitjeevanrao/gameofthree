# Game of Three

**Goal**

The Goal is to implement a game with two independent units – the players – communicating with each other using an API.

**Description**

When a player starts, it generates a random (whole) number and sends it to the second player as an approach of starting the game. The receiving player can now choose between adding one of {­1, 0, 1} to get to a number that is divisible by 3, divide it by three, and send the resulting whole number to the original sender. The same rules are applied until one player reaches the number 1 (after the division) and wins.

For each "move", a sufficient output should get generated (mandatory: the added, and the resulting number). Both players should be able to play automatically without user input. One of the players should optionally be adjustable by a user

# Requirements 
* JDK 1.8+
* The application comes with gradle wrapper so no need of install gradle

# Design
* The application is built as using [Vertx](https://vertx.io/) to utilize features of a reactive system. It can handle a lot of concurrency using a small number of threads and scale with minimal hardware. The inspiration was taken from a [slide](https://www.slideshare.net/codepitbull/ddd-and-reactive-frameworks) and a nice [blog](https://www.lightbend.com/blog/using-the-actor-model-with-domain-driven-design-ddd-in-reactive-systems-with-vaughn-vernon) which promotes using Actors in DDD
* It uses [MQTT](http://mqtt.org/) protocol to communicate over events and uses [Hive MQ](https://www.hivemq.com/public-mqtt-broker/) broker to send and recieves events. Another option could have been communicating over [vertx's eventbus](https://medium.com/@alexey.soshin/understanding-vert-x-event-bus-c31759757ce8) for event based communication. However, that would have required cluster manager like Hazelcast or Zookeeper to [distribute eventbus](https://vertx.io/docs/vertx-hazelcast/java/) accross different JVMs.
* The application tries to avoid anemic domain model using Value objects incorporating business logic in itself.
* When the game starts, it asks from the user whether to configure automatically or manually. In manual gameplay, it asks for the player name and the number to begin with. In automatic mode, the application generates the player name and a random positive integer to begin the game.


# Test
* Test can be executed running `./gradlew test` in the project root folder.
* Test report can be found at `gameofthree/build/reports/tests/test/index.html`
* Code coverage can be found at `gameofthree/build/reports/jacocoHtml/index.html`

# Running the application
* cd to gameofthree folder
* run `./gradlew clean build`
* run the application `java -jar build/libs/gameofthree.jar` to start player1 and run again on same machine or different machine to start player2


