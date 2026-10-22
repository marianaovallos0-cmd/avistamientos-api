# API REST — Registro de Avistamientos de Aves

API REST construida con Spring Boot para registrar avistamientos de aves observadas en el campo.

Tecnologías:
- Java 21
- Spring Boot 4.1.1
- Spring Data JPA
- H2 (base de datos en memoria)
- Lombok
- Maven

Cómo ejecutar:

Requisitos previos:
- Java 21 o superior
- Maven (o usar el wrapper mvnw incluido)

Pasos:
1. Clonar el repositorio:
   git clone https://github.com/marianaovallos0-cmd/avistamientos-api.git
   cd avistamientos-api

2. Ejecutar la aplicación:
   ./mvnw spring-boot:run

   En Windows:
   mvnw.cmd spring-boot:run

3. La API queda disponible en http://localhost:8080

4. Consola web de H2: http://localhost:8080/h2-console
   - JDBC URL: jdbc:h2:mem:avistamientos
   - Usuario: sa
   - Contraseña: (vacía)

Endpoints:

GET     /avistamientos          Lista todos los avistamientos        200
GET     /avistamientos/{id}     Ver un avistamiento                  200 / 404
POST    /avistamientos          Registrar un avistamiento            201 / 400
PUT     /avistamientos/{id}     Actualizar un avistamiento           200 / 404
DELETE  /avistamientos/{id}     Eliminar un avistamiento             204 / 404
GET     /avistamientos/resumen  Conteo por especie (bonus)           200

Modelo del recurso:

{
  "id": 1,
  "especie": "Colibrí",
  "lugar": "Jardín Botánico",
  "fecha": "2026-09-29",
  "observador": "María Gómez"
}

Ejemplos con curl:

Listar todos:
curl http://localhost:8080/avistamientos

Ver uno por id:
curl http://localhost:8080/avistamientos/1

Registrar uno nuevo:
curl -X POST http://localhost:8080/avistamientos -H "Content-Type: application/json" -d "{\"especie\":\"Colibrí\",\"lugar\":\"Jardín Botánico\",\"fecha\":\"2026-09-29\",\"observador\":\"María Gómez\"}"

Actualizar uno:
curl -X PUT http://localhost:8080/avistamientos/1 -H "Content-Type: application/json" -d "{\"especie\":\"Colibrí verde\",\"lugar\":\"Monserrate\",\"fecha\":\"2026-09-30\",\"observador\":\"María Gómez\"}"

Eliminar uno:
curl -X DELETE http://localhost:8080/avistamientos/1

Resumen por especie (bonus):
curl http://localhost:8080/avistamientos/resumen

Pruebas:

Correr todas las pruebas:
mvnw.cmd test

Correr una sola clase de pruebas:
mvnw.cmd test -Dtest=ValidadorDeAvistamientoTest

Generar el reporte de cobertura (JaCoCo):
mvnw.cmd verify

El reporte queda en target/site/jacoco/index.html

Las pruebas están en src/test/java:
- validacion/ValidadorDeAvistamientoTest: pruebas unitarias de la regla de negocio, con RelojFijo como stub del reloj.
- service/AvistamientoServiceTest: pruebas unitarias del servicio, con un mock del repositorio.
- AvistamientoApiIntegracionTest: pruebas de integración de los endpoints por HTTP, con la base H2.

Uso de IA:

Este proyecto fue desarrollado con asistencia de DeepSeek para explicar conceptos y resolver errores. El código fue comprendido, probado y adaptado.