# Getting Started

### How to build and run locally

Prerequisites:

* Java 17 installed
* Docker installed
* kubectl installed
* Minikube installed

Steps:

* In case you introduced some changes and want a new runnable jar - bump app version
  in `build.gradle`
* Run `./gradlew clean build` to build the app
* Build the docker
  image `docker build --build-arg APP_VERSION=<current_app_version> --tag example/spring-boot-mongo:<current_app_version>`
  where `<current_app_version>` is the version specified in the `build.gradle` file
* Pull the image into the Minikube
  cluster `minikube image load example/spring-boot-mongo:<current_app_version>`
* Navigate to `deploy` folder `cd deploy`
* Run `kubectl apply -f mongodb-config.yaml`
* Run `kubectl apply -f mongodb-secret.yaml`
* Run `kubectl apply -f mongodb-deployment.yaml`
* Run `kubectl apply -f spring-boot-mongo-deployment.yaml`
* Verify both services are up and running `kubectl get all`
* Forward the port from the
  cluster `kubectl port-forward deployment/spring-boot-mongo-deployment 8080:8080`
* Issue `GET` request to `http://localhost:8080/api/v1/students` endpoint
* Examine response (sample below)

```json
[
  {
    "id": "637757f93654c676d6722333",
    "name": "Michael",
    "surname": "Jordan",
    "email": "mj23@jordan.com",
    "gender": "MALE",
    "address": {
      "country": "USA",
      "city": "Chicago",
      "postCode": "Main street"
    },
    "favourites": [
      "Victory"
    ],
    "totalSpentInBooks": 10,
    "createdAt": "2022-11-18T10:01:28.957"
  }
]
```

#### Adding Ingress for external access to the app in a cluster

* Run `kubectl apply -f mongo-ingress.yaml`
* Add `127.0.0.1 spring-boot-mongo.net` to your `hosts` file (in Windows C:
  \Windows\System32\drivers\etc)

Option 1.

* Open a separate (second) terminal window and
  run `minikube service spring-boot-mongo-service --url`
* You should see the output. The important thing is the port number, in this case it is 55126 (used
  as <PORT_NUMBER> below)

``` shell
  http://127.0.0.1:55126
  ! Because you are using a Docker driver on windows, the terminal needs to be open to run it.
```

* Open your browser and submit request to `spring-boot-mongo.net:<PORT_NUMBER>/api/v1/students`
* Observe the results and close the second terminal window

Option 2.

* Open a separate (second) terminal window and run `minikube tunnel`
* You should see the output

``` shell
* Tunnel successfully started

* NOTE: Please do not close this terminal as this process must stay alive for the tunnel to be accessible ...

* Starting tunnel for service spring-boot-mongo-service.
! Access to ports below 1024 may fail on Windows with OpenSSH clients older than v8.1. For more information, see: https://minikube.sigs.k8s.io/docs/handbook/accessing/#access-to-ports-1024-on-windows-requires-root-permission
* Starting tunnel for service mongo-ingress.
```

* Change service type in `spring-boot-mongo-deployment.yaml` from `NodePort` to `LoadBalancer` (see
  line 46) and comment out line 53
* Change service port in `spring-boot-mongo-deployment.yaml` from 8080 to 80 (line 51): `port: 80`
* Run `kubectl apply -f spring-boot-mongo-deployment.yaml`
* Open your browser and submit request to `spring-boot-mongo.net/api/v1/students`
* Observe the results and close the second terminal window

### Useful Minikube and Kubernetes documentation

* [https://minikube.sigs.k8s.io/docs/handbook/accessing/](https://minikube.sigs.k8s.io/docs/handbook/accessing/)
* [Use Port Forwarding to Access Applications in a Cluster](https://kubernetes.io/docs/tasks/access-application-cluster/port-forward-access-application-cluster/)

### How to import data into MongoDB running in a container

* modify `mongo-compose.yml`: set `volumes` for `mongodb` service:

```yaml
services:
  mongodb:
    volumes:
      - ./MongoDB_data:/data
```

where `./MongoDB_data` is the folder on your machine and `data` is the folder in a container

* run `docker compose -f mongo-compose.yml up -d`
*

run `mongoimport --username="rootuser" --authenticationDatabase="admin" --db=target_database_name your_file_name.json`

### Guides and Reference Documentation

The following guides illustrate how to use some features concretely:

* [Spring Boot Integration with MongoDB Tutorial](https://www.mongodb.com/compatibility/spring-boot)
* [Accessing Data with MongoDB](https://spring.io/guides/gs/accessing-data-mongodb/)
* [Spring Data MongoDB](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#data.nosql.mongodb)


* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)


* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.7.5/gradle-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.7.5/gradle-plugin/reference/html/#build-image)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.7.5/reference/htmlsingle/#web)

### Some console queries examples

```mongodb-json-query
db.help()
show dbs
show collections

db.authors.insertOne({
  "name": "Shawn Kemp"
})
db.authors.insertMany([
  {
    "name": "MJ"
  },
  {
    "name": "Pip"
  },
  {
    "name": "Kobe"
  }
])

db.authors.find().pretty()
db.authors.findOne({
  name: "MJ"
})

db.authors.updateOne({
  name: "MJ"
}, {$set: {"number": "23"}})
db.authors.updateMany({}, {$set: {"league":"NBA"
}
})

db.authors.deleteOne({
name: "Shawn Kemp"
})

db.authors.createIndex({
"name": 1
})

db.inventory.findOne({"price": {
$gte: 18000
}
})
db.inventory.find({$and: [{"price": {$gte: 19600}}, {"variations.variation": {$nin: ["Blue", "Red"]}}]})
db.inventory.find({
variations: {
$elemMatch: { variation: "Blue", quantity: {$gt: 8}
}
}
})

db.movies.find({
}, {_id: 1, director: 1, title: 1, genres: 1} ).sort({director: 1, title: 1}).skip(200).limit(12)
db.movies.find({genres: "Comedy"}, {_id: 0})
db.movies.find({genres: ["Comedy", "Drama"]}, {
_id: 0
}).skip(10).limit(12)
db.movies.find({genres: {$all: ["Comedy", "Drama"]}
}, {
_id: 0
}).skip(10).limit(12)
db.movies.findOne({
_id: ObjectId("6399aa36c85df57007400aa3")
})

db.movies.updateOne({_id: ObjectId("6399aa36c85df57007400aa3")}, {$push: {genres: "Test"}})
db.movies.updateOne({_id: ObjectId("6399aa36c85df57007400aa3")}, {$addToSet: {genres: "Test No2"
}
})
db.movies.updateOne({
_id: ObjectId("6399aa36c85df57007400aa3")}, {$addToSet: {genres: "Test No2"
}
})
db.movies.updateOne({
_id: ObjectId("6399aa36c85df57007400aa3")}, {$pop: {genres: 1}})
db.movies.updateOne({_id: ObjectId("6399aa36c85df57007400aa3")}, {$pop: {genres: 1}
})
db.movies.updateOne({
_id: ObjectId("6399aa36c85df57007400aa3")}, {$pop: {genres: -1}})

db.inventory.findOne()
db.inventory.aggregate([{$group: {_id: "$source", cars_count: {$sum: 1}}}])
db.inventory.aggregate([{$group: {_id: "$name", make_count: {$sum: 1}}}])
db.inventory.aggregate([{$group: {_id: "$name", make_count: {$sum: 1}, models: {$addToSet : "$model"}, avg_price: {$avg: "$price" }}}])
db.inventory.findOne()

db.inventory.aggregate([{$bucket: {groupBy: "$year", boundaries: [1980, 1990, 2000, 2010], default: "allOtherYears"}}])
db.inventory.aggregate([{$bucketAuto: {groupBy: "$year", buckets: 5}}])

db.inventory.aggregate([{$unwind: "$variations"}, {$match: { "variations.quantity": 19}}, {$out: { db: "sample_data", coll: "the_cars"}}])
db.the_cars.find( {
name: "Chevrolet"
} ).sort({
price: -1
})
```
