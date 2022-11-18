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
  cluster ` kubectl port-forward deployment/spring-boot-mongo-deployment 8080:8080`
* Issue `GET` request to `http://localhost:8080/api/v1/students/all` endpoint
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

### Guides and Reference Documentation

The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Accessing Data with MongoDB](https://spring.io/guides/gs/accessing-data-mongodb/)

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.7.5/gradle-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.7.5/gradle-plugin/reference/html/#build-image)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.7.5/reference/htmlsingle/#web)
* [Spring Data MongoDB](https://docs.spring.io/spring-boot/docs/2.7.5/reference/htmlsingle/#data.nosql.mongodb)
