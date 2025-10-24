# TaskFlow API
![CI Pipeline](https://github.com/RubenFM/taskflow-api/workflows/CI%20Pipeline/badge.svg)
API REST para gestión de tareas desarrollada con Spring Boot, PostgreSQL y Docker.

## Descripción

TaskFlow es una aplicación backend que permite gestionar tareas y usuarios con sus relaciones. Incluye operaciones CRUD completas, validaciones, documentación con Swagger y está dockerizada para facilitar el despliegue.

## ️Tecnologías

- **Java 17**
- **Spring Boot 3.3.5**
- **Spring Data JPA**
- **PostgreSQL 15**
- **Docker & Docker Compose**
- **Maven**
- **Swagger/OpenAPI 3**
- **Bean Validation**

## Requisitos Previos

- Java 17+
- Docker Desktop
- Maven (opcional, incluido en el proyecto)

## Instalación y Ejecución

### **Con Docker (Recomendado)**

1. **Clonar el repositorio:**
```bash
git clone https://github.com/RubenFM/taskflow.git
cd taskflow
```

2. **Levantar los servicios:**
```bash
docker-compose up -d
```

Esto iniciará:
- PostgreSQL en `localhost:5432`
- TaskFlow API en `localhost:8080`

3. **Verificar que funciona:**
```bash
curl http://localhost:8080/actuator/health
```

### **Sin Docker (Desarrollo local)**

1. **Levantar solo PostgreSQL:**
```bash
docker-compose up postgres -d
```

2. **Ejecutar la aplicación desde IntelliJ:**
- Abrir el proyecto en IntelliJ IDEA
- Run `TaskflowApplication`

## Documentación API (Swagger)

Una vez iniciada la aplicación, accede a:
```
http://localhost:8080/swagger-ui.html
```

Aquí podrás:
- Ver todos los endpoints disponibles
- Probar la API directamente desde el navegador
- Ver ejemplos de request/response

## Endpoints Principales

### **Tasks**

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/tasks` | Obtener todas las tareas |
| GET | `/api/tasks/{id}` | Obtener tarea por ID |
| POST | `/api/tasks` | Crear nueva tarea |
| PUT | `/api/tasks/{id}` | Actualizar tarea |
| DELETE | `/api/tasks/{id}` | Eliminar tarea |

### **Users**

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/users` | Obtener todos los usuarios |
| GET | `/api/users/{id}` | Obtener usuario por ID |
| POST | `/api/users` | Crear nuevo usuario |
| PUT | `/api/users/{id}` | Actualizar usuario |
| DELETE | `/api/users/{id}` | Eliminar usuario |

## Ejemplos de Uso

### **Crear una tarea:**
```bash
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Completar proyecto",
    "description": "Finalizar el backend de TaskFlow",
    "status": "IN_PROGRESS",
    "userId": 1
  }'
```

### **Crear un usuario:**
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Juan Pérez",
    "email": "juan@example.com"
  }'
```

## Estructura del Proyecto
```
taskflow/
├── src/
│   ├── main/
│   │   ├── java/com/ausanchez/taskflow/
│   │   │   ├── controller/       # Controladores REST
│   │   │   ├── dto/              # Data Transfer Objects
│   │   │   ├── exception/        # Manejo de excepciones
│   │   │   ├── model/            # Entidades JPA
│   │   │   ├── repository/       # Repositorios
│   │   │   ├── service/          # Lógica de negocio
│   │   │   └── TaskflowApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── application-dev.properties
│   │       └── application-prod.properties
│   └── test/                     # Tests unitarios e integración
├── docker-compose.yml            # Orquestación de contenedores
├── Dockerfile                    # Imagen de la aplicación
├── pom.xml                       # Dependencias Maven
└── README.md
```

## Configuración

### **Perfiles de Spring**

El proyecto tiene dos perfiles:

**Development (H2 en memoria):**
```properties
spring.profiles.active=dev
```

**Production (PostgreSQL):**
```properties
spring.profiles.active=prod
```

### **Variables de Entorno**

Configuradas en `docker-compose.yml`:
```yaml
POSTGRES_DB: taskflowdb
POSTGRES_USER: taskflow_user
POSTGRES_PASSWORD: taskflow_pass
```

## Comandos Docker
```bash
# Iniciar servicios
docker-compose up -d

# Ver logs
docker-compose logs -f app

# Parar servicios
docker-compose down

# Rebuild de la imagen
docker build -t taskflow-api:latest .

# Ver contenedores activos
docker ps
```

## Validaciones

La API incluye validaciones automáticas:

- **Title:** No puede estar vacío, máximo 100 caracteres
- **Email:** Debe ser formato válido
- **Status:** Solo valores: PENDING, IN_PROGRESS, COMPLETED

## Testing
```bash
# Ejecutar todos los tests
mvn test

# Ejecutar tests con cobertura
mvn test jacoco:report
```

## Próximas Mejoras

- [ ] Tests unitarios con JUnit y Mockito
- [ ] Tests de integración
- [ ] GitHub Actions (CI/CD)
- [ ] Autenticación JWT
- [ ] Paginación de resultados
- [ ] Filtros y búsqueda avanzada

## Autor

**Rubén Fernández Molina**

- GitHub: [@RubenFM](https://github.com/RubenFM)
- LinkedIn: [Rubén Fernández Molina](https://www.linkedin.com/in/ruben-fernandez-molina/)
- Email: : rubenfernandezmolina13@gmail.com
