# 🚀 API RESTful – Gestión de Empleados

Este proyecto implementa una API RESTful para la Gestión de Empleados, desarrollada con Spring Boot, protegida con JSON Web Token (JWT) y documentada con OpenAPI/Swagger UI.
Incluye manejo global de errores, validaciones, y CRUD completo.

---
## 🛠️ Tecnologías Utilizadas

| Tecnología                  | Descripción                  |
| --------------------------- | ---------------------------- |
| **Java 17+**                | Lenguaje principal           |
| **Spring Boot 3.x**         | Framework backend            |
| **Spring Web**              | Creación de API REST         |
| **Spring Security + JWT**   | Autenticación y autorización |
| **Spring Data JPA**         | Persistencia                 |
| **Hibernate**               | ORM                          |
| **H2 / MySQL / PostgreSQL** | Bases de datos soportadas    |
| **OpenAPI – Swagger UI**    | Documentación interactiva    |

## 📦 Requisitos Previos
Asegúrate de tener instalado:

* JDK 17 o superior
* Maven 3.8+
* Un IDE (IntelJ IDEA, VS Code, Eclipse)

## 🔧 Instalación y Ejecución
1️⃣ Clonar el repositorio
git clone https://github.com/Monica-Ismelia/gestion-empleados-api_AA5_EV03.git
cd demo-spring

2️⃣ Ejecutar la aplicación

mvn clean package
java -jar target/gestion-empleados-1.0-SNAPSHOT.jar

La API estará disponible en:
http://localhost:8080

#  🌐 Endpoints de la API

La API está dividida en dos grupos: Autenticación y Gestión de Empleados.

1️⃣ Autenticación (Pública)

🔵 1. Autenticación (Público)
| Método   | Endpoint             | Descripción         | Código |
| -------- | -------------------- | ------------------- | ------ |
| **POST** | `/api/auth/register` | Registrar usuario   | 201    |
| **POST** | `/api/auth/login`    | Autenticación + JWT | 200    |

## 📥 POST /api/auth/register

Crea un nuevo usuario.

Body (JSON):
{
  "nombre": "Juan Pérez",
  "correo": "juan@example.com",
  "contrasena": "123456"
}

## 🔐 POST /api/auth/login

Inicia sesión y genera un token JWT.

Body (JSON):
{
  "correo": "juan@example.com",
  "contrasena": "123456"
}

Respuesta:
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI..."
}
2️⃣ Gestión de Empleados (Protegido – Requiere JWT)

Todos los endpoints requieren enviar el token como encabezado:
Authorization: Bearer <tu_token_jwt>

🟢 2. Empleados (Protegido con JWT)
| Método     | Endpoint              | Descripción           | Código          |
| ---------- | --------------------- | --------------------- | --------------- |
| **GET**    | `/api/empleados`      | Listar todos          | 200             |
| **GET**    | `/api/empleados/{id}` | Buscar por ID         | 200 / 404       |
| **POST**   | `/api/empleados`      | Crear empleado        | 201 / 400       |
| **PUT**    | `/api/empleados/{id}` | Actualización total   | 200 / 400 / 404 |
| **PATCH**  | `/api/empleados/{id}` | Actualización parcial | 200 / 400 / 404 |
| **DELETE** | `/api/empleados/{id}` | Eliminar              | 200 / 404       |

## 📄 Ejemplos de Respuestas de Error (Manejo Global)

Gracias al GlobalExceptionHandler, cualquier error devuelve JSON uniforme:
* ❌ 404 – Recurso no encontrado
{
  "status": 404,
  "error": "NOT_FOUND",
  "message": "Empleado no encontrado",
  "timestamp": "2025-01-01T10:15:30"
}
* ❌ 403 – No autorizado
{
  "status": 403,
  "error": "No autorizado",
  "message": "No tiene permisos para acceder a este recurso",
  "timestamp": "2025-01-01T10:15:30"
}
* ❌ 400 – Datos inválidos
{
  "status": 400,
  "error": "BAD_REQUEST",
  "message": "El correo ya está registrado",
  "timestamp": "2025-01-01T10:15:30"
}

## 🧪 Documentación Interactiva (Swagger UI)

Una vez ejecutada la aplicación, visita:
👉 http://localhost:8080/swagger-ui/index.html

Desde Swagger puedes:

✔ Probar tus endpoints
✔ Autenticarse con JWT
✔ Ver modelos (schemas)
✔ Ver ejemplos de errores

### 🔑 Cómo Usar el Token JWT en Swagger

1. Ingresa al endpoint POST /api/auth/login
2. Copia el campo "token"
3. En Swagger, haz clic en Authorize
4. Ingresa
Bearer tuTokenAqui
5. Ahora puedes usar todos los endpoints protegidos.

### 📦 Estructura del Proyecto

src/main/java/com/example/demo_spring/
├── auth/          → Controladores de login/register
├── config/        → Seguridad y excepciones globales
├── controller/    → Controladores REST
├── model/         → Entidades JPA
├── repository/    → Interfaces JPA
└── service/       → Lógica de negocio


## 📝 Notas Importantes

✔ Cuenta con manejo global de errores
✔ Swagger muestra ejemplos JSON para cada código
✔ Los endpoints protegidos requieren Bearer Token
✔ Gestión completa de empleados mediante CRUD

## 👩‍💻 Autor

**Aprendiz:** Mónica Ismelia Cañas Reyes
**Programa:** Tecnólogo en Análisis y Desarrollo de Software
**Institución:** Servicio Nacional de Aprendizaje – SENA 🟩
**Centro:** Centro Nacional de Asistencia Técnica a la Industria – ASTIN
**Evidencia:** GA7-220501096-AA5-EV03
**Fecha:** Diciembre de 2025
