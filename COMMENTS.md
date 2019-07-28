# Ankh-Morpork Unified User Systeme Challenge work by Nikola Nikolic


This document contains explanation of proposed solution for challenge.
Main points are:

  - Overview of used technologies
  - Prerequisites for running application
  - Staring application using Docker
  - General development comments including time spent on each topic
  - Isuues and problems found during work

#  Overview of used technologies

Application is build using Java 11 language, MongoDB as persistence layer,  ChatJS and Bootstarp 4 for building HTML report.
Maven artifact for application is created using [Spring Initializr](https://start.spring.io/)

#  Prerequisites for running application

In order to run application on machine you will need:
- Maven
- Java 11
- MongoDB
- Docker

MongoDB can be installed locally on machine or can be run on Docker (preferred way)
To start MongoBD on Docker you will need to pull latest image from repo:
```sh
docker pull mongo
```
and start server
```sh
docker run -p 27017:27017 mongo
```

Once server is running you need to import static json files into collection and create dedicated indices to speed up queries
```sh
mongo
> use tamedia //creating db to store data
```
```sh
mongoimport -db tamedia --collection unseen --file unseen.jsonl
mongoimport -db tamedia --collection mended_drum --file mended_drum.jsonl
mongoimport -db tamedia --collection pseudopolis --file pseudopolis.jsonl
```
```sh
mongo
db.pseudopolis.createIndex( { email: -1 } )
db.mended_drum.createIndex( { email: -1 } )
db.unseen.createIndex( { email: -1 } )
```

Now when MongoDB is started and you have set up IDE and imported project you can start application by running console command from application root directory
```sh
./mvnw package && java -jar target/swe-user-overlap-challenge-0.0.1-SNAPSHOT.jar
```

# Staring application using Docker
Assumed that you already started MongoDB, execute command from project root to build Docker image
```sh
docker build -t swe-user-overlap-challenge .
```
Now you can start application by executing command
```sh
docker run -p 8080:8080 swe-user-overlap-challenge
```
If you are running MongoDB in dedicated Docker container (not on local machine) command for starting application is

```sh
docker run -p 8080:8080 -e ROOT_URL=http://localhost -e MONGO_URL=mongodb://localhost:27017 --network="host" swe-user-overlap-challenge
```

# General development comments including time spent on each topic

Java application is seeded using Spring Boot.
Maven artifact is created using Spring Initializr (Spring Web Starter, Spring Data MongoDB and Lombok).
Application has three layers of code: Repository, Services and Controllers
Service layer is dummy layer which only forwards calls from Controller to Repository
Repository layer has one MongoDB implementation of interface.
Web report is created using simple lib CharJS. Bootstrap is used as template.

Time spent per area:
-Setting up spring boot app and creating Repository layer ~6h (learning MongoDB agg functions and Spring Data)
-UI part ~1h
~Docker ~1h



# Issues and problems found during work
-All customers have same Zip code in provided data, so report has only one value in grid
-User with same email address has different name across collections e.g. "mclaughlinblake@gmail.com" in
mended_drum and pseudopolis collections has one name and other name in unseen collection.
In my solution I have used email address as unique key for user so this differences does't affect report.


