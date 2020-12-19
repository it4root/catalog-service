# catalog-service

 Just one more shop implementation ))) just for study ... 

**Note:** This code will be implemented in order to be used only for my study. Sometimes it will be simplified (like skip validation of params)

### Build with Docker
before build the jar file 
then using console build catalog image and run docker-compose:
```console
$ docker build -t it4root/catalog-service .
$ docker-compose up
```
https://stackoverflow.com/questions/42564058/how-to-use-local-docker-images-with-minikube
Use a local registry:

docker run -d -p 5000:5000 --restart=always --name registry registry:2

docker tag it4root/catalog-service localhost:5000/it4root/catalog-service

docker push localhost:5000/it4root/catalog-service

docker pull localhost:5000/ubuntu

```console
docker build -t it4root/library -f Dockerfile.build .
$ kubectl apply -f deployment-catalog-service.yml
```


```console
minikube start
kubectl get po -A
minikube dashboard
```

```console
$ docker build -t it4root/catalog-service .

```
* app  http://localhost:8083/actuator
* pgAdmin http://localhost:5050/login
* swagger ui http://localhost:8083/swagger-ui.html

http://127.0.0.1:58296/v1/api/catalogs/health

TASK:
------------------------
- event storming and DDD
- swagger and open API
- docker
- docker-compose
- kubernetes
- metrics and visualization: Prometheus and Grafana
- service discovery and config server: consul vs zookeeper vs ansible?

- [to be completed]

Planned Technologies during application evolution:
------------------------
- Java 8
- Lombok

- Spring Boot
- Spring Core
- Spring Data
- Spring Security
- Spring MVC

- Hibernate
- PostgreSQL
- MongoDB
- Redis
- Liquidbase
- ElasticSearch
- Cassandra ?


- RabbitMQ

- REST
Swagger
- RPC
- Websocket

- RestAssured ?
- JUnit
- Spock
- Mockito
- Jepsen

- ELK
- Hystrix
- Kibana
- Grafana
- Prometheus
- Haproxy
- Zookeeper ?
- Ansible

- Angular ?

-------------------

TDD/BDD.
SOLID
DDD
CQRS
Event sourcing
Code quality(Sonar)

