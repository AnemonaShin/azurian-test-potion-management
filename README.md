# Prueba Tecnica - Desarrollador Java

Esta solución corresponde al desarrollo de una aplicación Full Stack construida con Java Spring Boot y Angular para la empresa Azurian.

El objetivo de la aplicación es Gestionar usuarios en un sistema tipo "**ROL**" tomando en cuenta que exista en la vida real la creacion de prociones. En primera instancia este proyecto permite la gestion de usuarios y sus respectivos roles mediante una API Rest y una Interfaz WEB que realiza operaciones como Consultas, Creaciones, Ediciones y eliminaciones de registros de manera "soft" (Tomando en cuenta una parcial desactivacion y activacion de estos).

La application fue realizada siguiendo la arquitectura MVC y utilizando distintos principios como la separación por capas, validación de datos, persistencia relacional como las pruebas automatizadas.

## Tecnologías Utilizadas

**Backend:** Java 21, Spring Boot 3, Spring Data JPA, PostgreSQL, Maven, JUnit 5, Mockito, SLF4J, OpenAPI/Swagger.

**Frontend:** Angular LTS, TypeScript, Vitest, RxJS, BootStrap Icons, Spring Boot Proxy, localStorage, Istanbul/V8.

## Entidades

### La aplicación usa las siguientes entidades:

#### BASE

Hereda a otras entidades.

Posee los siguientes campos:
| Campo | Tipo | Descripcion |
| :-------- | :------- | :------------------------- |
| `active` | `boolean` | Identificador de activo |
| `createdAt` | `LocalDateTime` | Fecha de creacion |
| `updatedAt` | `LocalDatetime` | Fecha de actualizacion |

#### USER

Alberga la informacion de los usuarios.

Posee los siguientes campos:

| Campo      | Tipo            | Descripcion               |
| :--------- | :-------------- | :------------------------ |
| `user_id`  | `boolean`       | ID del Usuario            |
| `username` | `LocalDateTime` | Nick del usuario          |
| `password` | `LocalDatetime` | Contraseña del usuario    |
| `email`    | `LocalDatetime` | Email del usuario         |
| `role_id`  | `LocalDatetime` | Id del rol en la relacion |

#### ROLE

Alberga la informacion de los roles.

Posee los siguientes campos:
| Campo | Type | Description |
| :-------- | :------- | :------------------------- |
| `role_id` | `long` | ID del ROL |
| `role_name` | `String` | Nombre del rol |
| `role_icon` | `String` | Icono del rol |

### RELACIONES

**Un Role puede estar asociado a múltiples User.**

**Cada User pertenece a un único Role.**

## Estructura del Proyecto

### Backend

```java
potion/
├── pom.xml
├── Dockerfile
└── src/
    ├── main/
    │   ├── java/cl/management/potion/
    │   │   ├── config/         # Configuración de seguridad y aplicación
    │   │   ├── controller/     # Endpoints REST
    │   │   ├── dto/            # Objetos de transferencia (request/response)
    │   │   ├── exception/      # Manejo global de excepciones
    │   │   ├── model/          # Entidades JPA
    │   │   ├── repository/     # Acceso a datos
    │   │   ├── service/        # Lógica de negocio
    │   │   ├── util/           # Utilidades y enums
    │   │   └── PotionApplication.java
    │   │
    │   └── resources/
    │       ├── application.yml
    │       └── db/
    │           └── data.sql
    │
    └── test/
        └── java/cl/management/potion/
            ├── controller/     # Pruebas de controladores
            ├── dto/            # Pruebas de DTOs
            ├── exception/      # Pruebas de manejo de excepciones
            ├── service/        # Pruebas de servicios
            ├── util/           # Pruebas de utilidades
            └── PotionApplicationTests.java
```

### Frontend

```typescript
potion-management/
├── home/          # Página principal
├── login/         # Autenticación y registro
├── nav/           # Menú lateral y navegación
├── user-list/     # Gestión de usuarios
├── role-list/     # Gestión de roles
├── settings/      # Perfil del usuario
├── services/      # Servicios HTTP y autenticación
├── utils/         # Utilidades compartidas
└── interceptors/  # Interceptores HTTP

```

### Puertos Utilizados

| Servicio         | Puerto |
| ---------------- | ------ |
| Frontend Angular | 4200   |
| API Spring Boot  | 9001   |
| PostgreSQL       | 5433   |

## Cómo Usar

### Como recorrer

Todo se ejecuta desde el menu de hambuerguesa, ahí se podrá ver tanto la lista de usuarios existentes en la bd como la lista de los roles existentes.

como administrador se pueden hacer las ediciones y agregaciones correspondientes.

### Data para prueba

La data para los usuarios existentes cuando levanta el proyecto son:

| Usuario | Contraseña |
| ------- | ---------- |
| admin   | admin1234. |
| pivote  | piv1234.   |

De todas formas se deberia poder ingresar con usuarios que se registren.

Solo los administradores podrán actualizar data de otros usuarios, los usuarios regularmente pueden actualizar su data desde settings en el menu de hamburguesa.

### Pruebas funcionales por Postman

Dentro de la carpeta resources/templates en la Aplicacion Java se encuentra un .zip con lo necesario para la ejecucion.

### Ejecución con Docker Compose

Levantar todos los servicios:

```bash
docker compose up -d
```

Detener todos los servicios:

```bash
docker compose down
```

Ver logs de los contenedores:

```bash
docker compose logs -f
```

---

### Ejecución Manual

#### Backend

Ingresar al directorio del backend:

```bash
cd potion
```

Compilar el proyecto:

```bash
mvn clean install
```

Ejecutar la aplicación:

```bash
mvn spring-boot:run
```

---

#### Frontend

Ingresar al directorio del frontend:

```bash
cd potion-management
```

Instalar dependencias:

```bash
npm install
```

Ejecutar la aplicación:

```bash
npm start
```

o alternativamente:

```bash
ng serve
```

---

## Ejecución de Pruebas

### Backend

Ejecutar pruebas unitarias:

```bash
mvn clean test
```

Generar reporte de cobertura:

```bash
mvn clean verify
```

Luego ir a carpeta target/site/jacoco y abrir archivo **index.html** en navegador.

---

### Frontend

Ejecutar pruebas unitarias:

```bash
npm test
```

Generar reporte de cobertura:

```bash
npm run test
```

o en su defecto

```bash
ng run test --no-watch
```

Luego ir a carpeta coverage/potion-management y abrir archivo **index.html** en navegador.

## Decisiones tecnicas

- Se utilizó Spring Boot 3 y Java 21 por ser versiones LTS ampliamente utilizadas en entornos empresariales.
- Se implementó una arquitectura en capas para separar responsabilidades entre controladores, servicios y acceso a datos.
- Se utilizaron DTOs para desacoplar el modelo de persistencia de los contratos expuestos por la API.
- Se incorporaron validaciones de entrada y manejo centralizado de excepciones para mejorar la robustez del sistema.
- Se implementó Docker Compose para simplificar la ejecución del entorno completo.
- Se desarrollaron pruebas unitarias para los componentes críticos del sistema y se generó reporte de cobertura de código.
