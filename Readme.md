# Proyecto Hexagonal con DDD y AWS

Este proyecto es un ejemplo de arquitectura hexagonal y DDD con Spring Boot 3.2.5 y Java 21. Utiliza:

Core: lógica de dominio, puertos de entrada/salida (File, puertos para repositorio, almacenamiento, notificaciones, Lambda, métricas).

Infrastructure: adaptadores para S3, SNS, Lambda, CloudWatch, JPA (RDS), SQS (LocalStack en dev).

API: capa de presentación REST, configuración de perfiles, arranque de Spring Boot.

# Índice

1. Arquitectura
2. Requisitos Previos
3. Estructura de Módulos
4. Configuración de AWS
5. Docker Compose (entorno dev)
6. Ejecución Local
7. Ejecución con RDS y AWS real
8. Endpoints REST
9. Pruebas con Postman 


# Arquitectura

La aplicación sigue la arquitectura hexagonal:

```bash
                                      +-------------------------+
                                      |       API (REST)        |
                                      +-----------+-------------+
                                                  |
                                       (+) Inbound Port
                                                  |
                                      +-----------v-------------+
                                      | Use Cases (Core Domain)  |
                                      +-----------+-------------+
                                     /     |       |       |    \
                                    /      |       |       |     \
                                v       v       v       v      v
                                RepoPort  S3Port  SNSPort  LambdaPort  MetricsPort
                                |       |       |        |           |
                                Infra Repo  S3   SNS      Lambda    CloudWatch
```

* Core define puertos de entrada/salida y casos de uso.
* Infrastructure implementa esos puertos usando AWS SDK y Spring Data JPA.
* API configura y expone REST controllers.

# Requisitos Previos

Java 21
* Maven 3.9+
* Docker y Docker Compose (para entorno dev)
* AWS CLI configurado con perfil dev (opcional para pruebas reales)

# Estructura de Módulos
```bash
prueba-S3 (parent POM)
├── core/
│   ├── src/main/java/... (dominio y puertos)
│   └── pom.xml (dependencies: spring-context, lombok)
├── infrastructure/
│   ├── src/main/java/... (adaptadores S3, SNS, Lambda, CloudWatch, JPA)
│   └── pom.xml (dependencies: AWS SDK v2, Spring Data JPA)
└── api/
├── src/main/java/... (controller, ApiApplication)
├── src/main/resources
│   ├── application-dev.yaml
│   └── application-prod.yaml
└── pom.xml (Spring Boot starter-web, starter-data-jpa)
```

# Configuración de AWS

Perfil dev
```bash
aws configure --profile dev
# AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY, region=us-east-1
```

# Recursos necesarios en AWS

1. RDS PostgreSQL (endpoint, DB files).
2. S3 Bucket: hexagonal-demo-bucket.
3. SNS Topic FIFO: fileNotification.fifo.
4. Lambda Function: fileUploadProcessor.
5. CloudWatch Namespace: HexagonalApp.
6. IAM Role/User con permisos mínimos:

```bash
{
"Statement":[
{"Effect":"Allow","Action":["s3:PutObject","s3:GetObject"],"Resource":"arn:aws:s3:::hexagonal-demo-bucket/*"},
{"Effect":"Allow","Action":"sns:Publish","Resource":"arn:aws:sns:us-east-1:123456789012:fileNotification.fifo"},
{"Effect":"Allow","Action":"lambda:InvokeFunction","Resource":"arn:aws:lambda:us-east-1:123456789012:function:fileUploadProcessor"},
{"Effect":"Allow","Action":"cloudwatch:PutMetricData","Resource":"*"}
]
}
```

# Docker Compose (entorno dev)
```bash
docker compose up --build -d
```
Este docker-compose.yml levanta:

* db: PostgreSQL 16 en localhost:5432.
* localstack: emula S3, SNS, Lambda, CloudWatch en localhost:4566.
* api: tu microservicio en localhost:8080.

# Ejecución Local
```bash
  # Compilar todo
./mvnw clean install

# Arrancar API con perfil dev
AWS_PROFILE=dev ./mvnw -pl api spring-boot:run -Dspring-boot.run.profiles=dev
```

# Ejecución con RDS y AWS real

Asegúrate de que application-prod.yaml apunte a tu RDS y quita endpoints de LocalStack:
```bash
spring:
datasource:
url: jdbc:postgresql://<rds-endpoint>:5432/files
username: <usuario>
password: <password>

app:
aws:
s3:
bucket: hexagonal-demo-bucket
sns:
topicArn: arn:aws:sns:us-east-1:123456789012:fileNotification.fifo
lambda:
functionName: fileUploadProcessor
cloudwatch:
namespace: HexagonalApp
```
Y lanza:

AWS_PROFILE=dev ./mvnw -pl api spring-boot:run -Dspring-boot.run.profiles=prod

# Endpoints REST

Subir fichero

* POST /api/files
* Content-Type: multipart/form-data

Form-data:
- fileName: string
- file: archivo

Respuesta: 200 OK con s3://hexagonal-demo-bucket/<fileName> o 201 Created con JSON {"location":"..."}.

# Pruebas con Postman
1. Crear petición POST a http://localhost:8080/api/files.
2. En Body: form-data con campos fileName y file.
3. Enviar → comprobar location.
4. Revisar en S3 (LocalStack o AWS):
```bash
 aws --endpoint-url=http://localhost:4566 s3api list-objects --bucket hexagonal-demo-bucket
 ```
5. Revisar SNS/Lambda/CloudWatch:

* SNS: consola LocalStack o AWS Console (mensajes publicados).
* Lambda: invocaciones en CloudWatch Logs.
* CloudWatch: métricas en namespace HexagonalApp.