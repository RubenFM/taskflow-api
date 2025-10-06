# 📚 TaskFlow API - Apuntes Completos

**Proyecto:** Spring Boot REST API para gestión de tareas  
**Tecnologías:** Java 17, Spring Boot 3.x, H2, Maven  
**Fecha:** Enero 2025

---

## 🏗️ Estructura del Proyecto

```
taskflow/
├── src/main/java/com/atresmedia/taskflow/
│   ├── TaskflowApplication.java          (Punto de entrada)
│   ├── model/                             (Entidades - Datos)
│   │   ├── Task.java
│   │   └── TaskStatus.java
│   ├── repository/                        (Acceso a Base de Datos)
│   │   └── TaskRepository.java
│   ├── service/                           (Lógica de Negocio)
│   │   └── TaskService.java
│   ├── controller/                        (Endpoints REST - HTTP)
│   │   └── TaskController.java
│   └── exception/                         (Manejo de Errores)
│       └── GlobalExceptionHandler.java
└── src/main/resources/
    └── application.properties             (Configuración)
```

---

## 📦 PAQUETE: model (Entidades)

### ¿Qué es?
Clases que representan **tablas de la base de datos**. Son el modelo de datos de tu aplicación.

### ¿Para qué sirve?
Definir **qué datos** tiene tu aplicación y **cómo se guardan** en la BD.

### Task.java

**Anotaciones principales:**
- `@Entity` → Marca la clase como tabla de BD
- `@Data` (Lombok) → Genera getters, setters, toString, etc.
- `@Id` → Clave primaria
- `@GeneratedValue` → Auto-incremento del ID
- `@NotBlank`, `@Size` → Validaciones de Bean Validation
- `@Enumerated(EnumType.STRING)` → Guarda el enum como texto

**Campos:**
```java
Long id                    // Clave primaria
String title               // Título (validado: 3-100 caracteres)
String description         // Descripción (max 500 caracteres)
TaskStatus status          // Estado (enum)
LocalDateTime createdAt    // Fecha de creación
```

**Resultado en BD:**
```sql
CREATE TABLE task (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP
);
```

### TaskStatus.java

**¿Qué es?**  
Un `enum` (enumeración) con valores fijos posibles para el estado de una tarea.

**Valores:**
```java
PENDING
IN_PROGRESS
COMPLETED
CANCELLED
```

**Ventaja:** Solo acepta estos valores, no puedes poner "VOLANDO" o cualquier otra cosa.

---

## 📦 PAQUETE: repository (Acceso a Datos)

### ¿Qué es?
Interfaces que gestionan las **operaciones con la base de datos** (CRUD).

### ¿Para qué sirve?
Comunicarse con la BD sin escribir SQL manualmente. Spring Data JPA lo genera automáticamente.

### TaskRepository.java

**Es una interface** que extiende `JpaRepository<Task, Long>`

**Métodos automáticos que ya tiene:**
```java
findAll()                  // SELECT * FROM task
findById(Long id)          // SELECT * WHERE id = ?
save(Task task)            // INSERT o UPDATE
deleteById(Long id)        // DELETE WHERE id = ?
count()                    // SELECT COUNT(*)
existsById(Long id)        // SELECT COUNT(*) WHERE id = ?
```

**Clave:** No escribes código de implementación. Spring lo genera en tiempo de ejecución.

**Anotaciones:**
- `@Repository` → Marca como componente de acceso a datos

---

## 📦 PAQUETE: service (Lógica de Negocio)

### ¿Qué es?
Clases que contienen la **lógica de negocio** de tu aplicación.

### ¿Para qué sirve?
Procesar datos, aplicar reglas de negocio, orquestar operaciones. Es el "cerebro" de la aplicación.

### TaskService.java

**Responsabilidades:**
- Llamar al Repository para acceder a datos
- Aplicar lógica de negocio (validaciones, transformaciones)
- Manejar errores (lanzar excepciones si no existe una tarea)

**Métodos:**
```java
getAllTasks()                     // Obtener todas
getTaskById(Long id)              // Obtener una específica
createTask(Task task)             // Crear nueva
updateTask(Long id, Task task)    // Actualizar existente
deleteTask(Long id)               // Eliminar
```

**Anotaciones:**
- `@Service` → Marca como componente de lógica de negocio

**Constructor:**
```java
public TaskService(TaskRepository taskRepository) {
    this.taskRepository = taskRepository;
}
```
Spring inyecta automáticamente el Repository.

---

## 📦 PAQUETE: controller (Endpoints REST)

### ¿Qué es?
Clases que **exponen endpoints HTTP** para que clientes (Postman, frontend) puedan usar tu API.

### ¿Para qué sirve?
Recibir peticiones HTTP (GET, POST, PUT, DELETE) y devolver respuestas JSON.

### TaskController.java

**Anotaciones de clase:**
- `@RestController` → Marca como controlador REST (devuelve JSON)
- `@RequestMapping("/api/tasks")` → Ruta base de todos los endpoints

**Endpoints:**

| Método HTTP | URL | Qué hace |
|-------------|-----|----------|
| GET | /api/tasks | Lista todas las tareas |
| GET | /api/tasks/{id} | Obtiene una tarea específica |
| POST | /api/tasks | Crea una nueva tarea |
| PUT | /api/tasks/{id} | Actualiza una tarea |
| DELETE | /api/tasks/{id} | Elimina una tarea |

**Anotaciones de métodos:**
- `@GetMapping` → Peticiones GET (leer)
- `@PostMapping` → Peticiones POST (crear)
- `@PutMapping` → Peticiones PUT (actualizar)
- `@DeleteMapping` → Peticiones DELETE (eliminar)
- `@PathVariable` → Captura valores de la URL (/tasks/5 → id=5)
- `@RequestBody` → Lee JSON del body de la petición
- `@Valid` → Activa validaciones automáticas

**Ejemplo de método:**
```java
@GetMapping("/{id}")
public Task getTaskById(@PathVariable Long id) {
    return taskService.getTaskById(id);
}
```

---

## 📦 PAQUETE: exception (Manejo de Errores)

### ¿Qué es?
Clases que capturan y procesan **errores globalmente** en toda la aplicación.

### ¿Para qué sirve?
Devolver respuestas de error **consistentes y profesionales** sin repetir código en cada Controller.

### GlobalExceptionHandler.java

**Anotación:**
- `@RestControllerAdvice` → Captura excepciones de todos los controllers

**Métodos:**

**1. Errores de validación:**
```java
@ExceptionHandler(MethodArgumentNotValidException.class)
public ResponseEntity<Map<String, String>> handleValidationErrors(...)
```

**Captura:** Cuando falla @Valid (título vacío, muy corto, etc.)

**Respuesta:**
```json
{
  "title": "Title is required",
  "size": "Title must be between 3 and 100 characters"
}
```

**2. Recurso no encontrado:**
```java
@ExceptionHandler(RuntimeException.class)
public ResponseEntity<Map<String, Object>> handleRuntimeException(...)
```

**Captura:** Cuando no se encuentra una tarea (ID no existe)

**Respuesta:**
```json
{
  "timestamp": "2025-01-11T15:30:00",
  "status": 404,
  "message": "Task not found with id: 999"
}
```

---

## 🔧 ARCHIVO: application.properties

### ¿Qué es?
Archivo de **configuración** de Spring Boot.

### Configuración actual:

```properties
# Base de datos H2 (en memoria)
spring.datasource.url=jdbc:h2:mem:taskflowdb
spring.datasource.username=sa
spring.datasource.password=

# JPA
spring.jpa.hibernate.ddl-auto=update    # Crea/actualiza tablas automáticamente
spring.jpa.show-sql=true                # Muestra SQL en consola

# H2 Console
spring.h2.console.enabled=true          # Interfaz web para ver la BD
spring.h2.console.path=/h2-console
```

---

## 🏛️ Arquitectura en Capas

```
┌─────────────────────────────────────────┐
│  CLIENTE (Postman, Frontend)            │
└──────────────┬──────────────────────────┘
               │ HTTP Request (JSON)
               ↓
┌─────────────────────────────────────────┐
│  CONTROLLER (TaskController)            │  ← Recibe peticiones HTTP
│  - Valida con @Valid                    │
│  - Devuelve JSON                        │
└──────────────┬──────────────────────────┘
               │ Llama métodos
               ↓
┌─────────────────────────────────────────┐
│  SERVICE (TaskService)                  │  ← Lógica de negocio
│  - Procesa datos                        │
│  - Aplica reglas                        │
└──────────────┬──────────────────────────┘
               │ Usa repository
               ↓
┌─────────────────────────────────────────┐
│  REPOSITORY (TaskRepository)            │  ← Acceso a BD
│  - Spring genera SQL automáticamente    │
└──────────────┬──────────────────────────┘
               │ SQL
               ↓
┌─────────────────────────────────────────┐
│  DATABASE (H2)                          │  ← Almacena datos
└─────────────────────────────────────────┘
```

---

## 🛠️ Herramientas Utilizadas

### 1. Postman 📮

**¿Qué es?**  
Cliente HTTP para probar APIs.

**¿Para qué lo usamos?**
- Enviar peticiones GET, POST, PUT, DELETE
- Ver respuestas JSON
- Probar validaciones
- Probar errores

**Ejemplo de uso:**
```
POST http://localhost:8080/api/tasks
Headers: Content-Type: application/json
Body:
{
  "title": "Nueva tarea",
  "description": "Descripción",
  "status": "PENDING"
}
```

**Colecciones creadas:**
- GET all tasks
- GET task by ID
- POST create task
- PUT update task
- DELETE task

### 2. H2 Console 🗄️

**¿Qué es?**  
Interfaz web para visualizar la base de datos H2.

**¿Para qué lo usamos?**
- Ver tablas creadas
- Ejecutar queries SQL
- Ver datos guardados
- Verificar que la estructura es correcta

**Cómo acceder:**
```
URL: http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:taskflowdb
User: sa
Password: (vacío)
```

**Queries útiles:**
```sql
SELECT * FROM task;
SELECT * FROM task WHERE status = 'COMPLETED';
DELETE FROM task WHERE id = 1;
```

### 3. IntelliJ IDEA 💻

**¿Para qué?**
- IDE principal para escribir código
- Autocompletado
- Debugging
- Terminal integrada
- Git integrado

**Atajos útiles:**
- `Shift + F10` → Ejecutar aplicación
- `Alt + F12` → Terminal
- `Ctrl + Space` → Autocompletar
- `Ctrl + Click` → Ir a definición

### 4. Maven 📦

**¿Qué es?**  
Gestor de dependencias y build tool.

**¿Para qué?**
- Descargar librerías (Spring Boot, Lombok, etc.)
- Compilar el proyecto
- Ejecutar tests

**Archivo:** pom.xml

**Comandos útiles:**
```bash
mvn clean install     # Compilar
mvn test              # Ejecutar tests
mvn spring-boot:run   # Ejecutar aplicación
```

### 5. Git & GitHub 🌿

**¿Para qué?**
- Control de versiones
- Historial de cambios
- Backup en la nube
- Mostrar en CV/LinkedIn

**Comandos usados:**
```bash
git add .
git commit -m "mensaje"
git push origin main
git status
git log --oneline
```

---

## 📊 Conceptos Clave Aprendidos

### 1. Anotaciones de Spring

| Anotación | Dónde | Para qué |
|-----------|-------|----------|
| @SpringBootApplication | Clase principal | Punto de entrada |
| @Entity | Model | Tabla de BD |
| @Repository | Repository | Acceso a datos |
| @Service | Service | Lógica de negocio |
| @RestController | Controller | Endpoints REST |
| @RestControllerAdvice | Exception | Manejo global de errores |

### 2. Anotaciones de Validación

| Anotación | Qué valida |
|-----------|------------|
| @NotBlank | No vacío, no solo espacios |
| @NotNull | No nulo |
| @Size(min, max) | Longitud del texto |
| @Email | Formato de email válido |

### 3. Anotaciones HTTP

| Anotación | Método HTTP | Para qué |
|-----------|-------------|----------|
| @GetMapping | GET | Leer datos |
| @PostMapping | POST | Crear datos |
| @PutMapping | PUT | Actualizar datos |
| @DeleteMapping | DELETE | Eliminar datos |

### 4. Inyección de Dependencias

Spring crea y gestiona objetos automáticamente:

```java
@Service
public class TaskService {
    private final TaskRepository repository;
    
    // Spring inyecta TaskRepository automáticamente
    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }
}
```

**No haces:** `new TaskRepository()`  
**Spring lo hace por ti** cuando arranca la aplicación.

### 5. JPA (Java Persistence API)

**ORM (Object-Relational Mapping):**
- Convierte objetos Java ↔ Tablas SQL
- No escribes SQL manualmente
- Hibernate es la implementación

**Ejemplo:**
```java
Task task = new Task();
task.setTitle("Nueva");
taskRepository.save(task);

// Hibernate genera: INSERT INTO task (title, ...) VALUES (?, ...)
```

### 6. ResponseEntity

Control total de respuestas HTTP:

```java
ResponseEntity.ok(data)              // 200 OK
ResponseEntity.badRequest().body()   // 400 Bad Request
ResponseEntity.status(404).body()    // 404 Not Found
```

---

## 🎯 Flujo Completo de una Petición

### Ejemplo: Crear una tarea

```
1. Cliente (Postman):
   POST /api/tasks
   { "title": "Nueva tarea", "status": "PENDING" }

2. TaskController:
   - Recibe JSON
   - @Valid valida los campos
   - Llama a taskService.createTask(task)

3. TaskService:
   - Aplica lógica de negocio (si la hubiera)
   - Llama a taskRepository.save(task)

4. TaskRepository:
   - Spring Data genera SQL
   - Ejecuta: INSERT INTO task (title, status, created_at) VALUES (?, ?, ?)

5. Base de Datos (H2):
   - Guarda el registro
   - Devuelve el objeto con ID generado

6. TaskRepository → TaskService → TaskController:
   - El objeto Task con ID vuelve por las capas

7. TaskController → Cliente:
   - Convierte Task a JSON
   - Devuelve: {"id": 1, "title": "Nueva tarea", "status": "PENDING", ...}
```

---

## ✅ Checklist de lo que funciona

- ✅ Base de datos en memoria (H2)
- ✅ Entidad Task con validaciones
- ✅ CRUD completo (Create, Read, Update, Delete)
- ✅ API REST con 5 endpoints
- ✅ Validaciones automáticas
- ✅ Manejo de errores global
- ✅ Respuestas JSON profesionales
- ✅ Probado con Postman
- ✅ Código en GitHub

---

## 🔑 Palabras Clave para CV/LinkedIn

**Backend:** Java, Spring Boot, Spring Data JPA, REST API, Microservices Architecture

**Database:** H2, SQL, JPA, Hibernate

**Tools:** Maven, Git, Postman, IntelliJ IDEA

**Concepts:** CRUD Operations, Bean Validation, Exception Handling, Dependency Injection, MVC Pattern

---

## 🚀 Próximos Pasos

**Día 4:** Crear entidad User y relacionarla con Task (ManyToOne)

**Día 5:** Docker, PostgreSQL y perfiles de configuración

**Día 6:** Testing (JUnit, Mockito) y Swagger/OpenAPI

**Día 7:** Features avanzados y optimizaciones

---

**Proyecto:** TaskFlow API  
**GitHub:** [Tu repositorio]  
**Autor:** [Tu nombre]  
**Fecha:** Enero 2025