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
* app  http://localhost:8083/actuator
* pgAdmin http://localhost:5050/login

List of planned work:
------------------------
- catalog-service api design (first version of catalog API prepared - docs/catalog-api-conduct.yml))
- order-service
- [to be completed]



