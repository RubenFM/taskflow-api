# TaskFlow API

![CI Pipeline](https://github.com/RubenFM/taskflow-api/workflows/CI%20Pipeline/badge.svg)
![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.5-brightgreen)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue)
![Docker](https://img.shields.io/badge/Docker-Ready-blue)

API REST para gestión de tareas desarrollada con Spring Boot, PostgreSQL, Docker y pruebas automatizadas.

---

##  Descripción

TaskFlow es una aplicación backend completa que permite gestionar tareas y usuarios con sus relaciones. Desarrollada siguiendo buenas prácticas en la industria, incluye arquitectura por capas, testing, documentación interactiva y CI/CD automatizado.

---

## Características

-  **CRUD completo** de Tareas y Usuarios
-  **Relaciones JPA** (ManyToOne entre Task y User)
-  **Validaciones** automáticas con Bean Validation
-  **Manejo de errores** global y personalizado
-  **Documentación interactiva** con Swagger/OpenAPI
-  **Testing completo**: 28 tests (unitarios + integración)
-  **CI/CD** con GitHub Actions
-  **Dockerizado** con Docker Compose
-  **Multi-entorno**: profiles dev/test/prod

---

## ️Tecnologías

### Backend
- **Java 17**
- **Spring Boot 3.3.5**
- **Spring Data JPA**
- **Spring Validation**
- **Lombok**

### Base de Datos
- **PostgreSQL 15** (producción)
- **H2** (desarrollo y tests)

### Testing
- **JUnit 5** - Framework de testing
- **Mockito** - Mocks para tests unitarios
- **@DataJpaTest** - Tests de integración

### DevOps
- **Docker & Docker Compose**
- **GitHub Actions** (CI/CD)
- **Maven** (gestión de dependencias)

### Documentación
- **Swagger/OpenAPI 3**
- **SpringDoc**

---

## Requisitos Previos

- Java 17+
- Docker Desktop
- Maven (opcional)

---

## Instalación y Ejecución

### **Opción 1: Con Docker (Recomendado)**
```bash
# 1. Clonar el repositorio
git clone https://github.com/RubenFM/taskflow-api.git
cd taskflow-api

# 2. Levantar los servicios
docker-compose up -d

# 3. Verificar que funciona
curl http://localhost:8080/actuator/health
```

**La aplicación estará disponible en:** `http://localhost:8080`

---

### **Opción 2: Desarrollo local (PostgreSQL en Docker)**
```bash
# 1. Levantar solo PostgreSQL
docker-compose up postgres -d

# 2. Ejecutar la aplicación desde IntelliJ
# Run > TaskflowApplication

# O desde terminal
./mvnw spring-boot:run
```

---

### **Opción 3: Modo desarrollo (H2 en memoria)**
```bash
# Cambiar perfil a 'dev' en application.properties
spring.profiles.active=dev

# Ejecutar aplicación
./mvnw spring-boot:run
```

**H2 Console:** `http://localhost:8080/h2-console`

---

## Documentación API (Swagger)

Una vez iniciada la aplicación:
```
http://localhost:8080/swagger-ui.html
```

Desde Swagger UI podrás:
- Ver todos los endpoints disponibles
- Probar la API directamente desde el navegador
- Ver esquemas y ejemplos de request/response
- Explorar modelos de datos

---

## Endpoints Principales

### **Tasks**

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/tasks` | Obtener todas las tareas |
| GET | `/api/tasks/{id}` | Obtener tarea por ID |
| GET | `/api/tasks/user/{userId}` | Tareas de un usuario |
| GET | `/api/tasks/search?keyword=...` | Buscar tareas |
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

---

## Ejemplos de Uso

### **Crear un usuario:**
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Juan Pérez",
    "email": "juan@example.com",
    "role": "USER"
  }'
```

### **Crear una tarea:**
```bash
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Completar proyecto",
    "description": "Finalizar el backend de TaskFlow",
    "status": "IN_PROGRESS"
  }'
```

### **Asignar tarea a usuario:**
```bash
curl -X PUT http://localhost:8080/api/tasks/1 \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Completar proyecto",
    "description": "Finalizar el backend de TaskFlow",
    "status": "IN_PROGRESS",
    "assignedTo": {
      "id": 1
    }
  }'
```
---

## Testing

El proyecto incluye **28 tests automatizados** con cobertura completa:

### **Tests Unitarios (16 tests)**
- `TaskServiceTest`: 8 tests con Mockito
- `UserServiceTest`: 8 tests con Mockito

### **Tests de Integración (12 tests)**
- `TaskRepositoryTest`: 6 tests con @DataJpaTest
- `UserRepositoryTest`: 6 tests con @DataJpaTest

### **Ejecutar tests:**
```bash
# Todos los tests
./mvnw test

# Test específico
./mvnw test -Dtest=TaskServiceTest

# Con reporte de cobertura
./mvnw test jacoco:report
```

### **Tipos de tests:**

✅ **Tests unitarios:** Verifican lógica de negocio aislada con mocks  
✅ **Tests de integración:** Verifican persistencia con H2 real  
✅ **Casos de éxito:** Operaciones correctas  
✅ **Casos de error:** Excepciones y validaciones

---

## Configuración

### **Perfiles de Spring**

El proyecto incluye 3 perfiles:

| Perfil | Base de Datos | Uso |
|--------|---------------|-----|
| **dev** | H2 (memoria) | Desarrollo local |
| **test** | H2 (memoria) | Tests automatizados |
| **prod** | PostgreSQL | Producción con Docker |

**Cambiar perfil:**
```properties
# application.properties
spring.profiles.active=prod
```

---

### **Variables de Entorno (Docker)**

Configuradas en `docker-compose.yml`:
```yaml
POSTGRES_DB: taskflowdb
POSTGRES_USER: taskflow_user
POSTGRES_PASSWORD: taskflow_pass
SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/taskflowdb
```

---

## Comandos Docker
```bash
# Iniciar servicios
docker-compose up -d

# Ver logs en tiempo real
docker-compose logs -f app

# Parar servicios
docker-compose down

# Parar y eliminar volúmenes (datos)
docker-compose down -v

# Rebuild de la imagen
docker build -t taskflow-api:latest .

# Ver contenedores activos
docker ps

# Acceder al contenedor
docker exec -it taskflow-app bash
```

---

## Validaciones

La API incluye validaciones automáticas con mensajes descriptivos:

### **Task:**
- `title`: Requerido, 3-100 caracteres
- `description`: Máximo 500 caracteres
- `status`: PENDING, IN_PROGRESS, COMPLETED, CANCELLED

### **User:**
- `name`: Requerido, 3-50 caracteres
- `email`: Requerido, formato válido, único
- `role`: USER, ADMIN, MANAGER

### **Ejemplo de error de validación:**
```json
{
  "title": "Title cannot be blank",
  "email": "Email format is invalid"
}
```

---

## Manejo de Errores

Respuestas de error estandarizadas:

### **404 Not Found:**
```json
{
  "timestamp": "2025-10-24T18:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Task not found with id: 999"
}
```

### **400 Bad Request:**
```json
{
  "title": "Title must be between 3 and 100 characters",
  "status": "Status is required"
}
```

---

## CI/CD Pipeline

El proyecto incluye GitHub Actions que se ejecuta automáticamente en cada push:

### **Pipeline stages:**
1. ✅ Checkout del código
2. ✅ Setup JDK 17
3. ✅ Build con Maven
4. ✅ Ejecución de tests
5. ✅ Build de imagen Docker

**Ver pipeline:** [Actions](https://github.com/RubenFM/taskflow-api/actions)

---

## Próximas Mejoras

- [ ] Spring Security + JWT Authentication
- [ ] Tests end-to-end
- [ ] Paginación y ordenamiento
- [ ] Filtros avanzados de búsqueda
- [ ] WebSockets para notificaciones
- [ ] Auditoría (createdBy, updatedAt)
- [ ] Caché con Redis
- [ ] Métricas con Prometheus/Grafana
- [ ] Refactor a microservicios
- [ ] Deploy en Cloud (GCP/AWS)

---

## Autor

**Rubén Fernández Molina**

- GitHub: [@RubenFM](https://github.com/RubenFM)
- LinkedIn: [Rubén Fernández Molina](https://www.linkedin.com/in/ruben-fernandez-molina/)
- Email: rubenfernandezmolina13@gmail.com