# 🎵 Mi Playlist Musical

Aplicación web desarrollada en Java con Spring Boot para gestionar una playlist de videos musicales.

## 📋 Características

- ✅ Agregar y eliminar videos musicales
- ✅ Visualización embebida de videos (YouTube, Vimeo, Dailymotion)
- ✅ Sistema de likes
- ✅ Marcar videos como favoritos
- ✅ Persistencia de datos en archivo JSON
- ✅ Interfaz moderna y responsive con Bootstrap
- ✅ Tests unitarios con JUnit
- ✅ Pipeline de CI/CD con Jenkins

## 🛠️ Tecnologías Utilizadas

- **Backend**: Java 17, Spring Boot 3.1.5
- **Frontend**: HTML5, CSS3, JavaScript, Bootstrap 5
- **Template Engine**: Thymeleaf
- **Build Tool**: Maven
- **Testing**: JUnit 5, Mockito
- **CI/CD**: Jenkins
- **Persistencia**: JSON (Jackson)

## 📁 Estructura del Proyecto

```
mi-playlist/
├── src/
│   ├── main/
│   │   ├── java/com/playlist/
│   │   │   ├── PlaylistApplication.java
│   │   │   ├── controller/
│   │   │   │   ├── HomeController.java
│   │   │   │   └── VideoController.java
│   │   │   ├── model/
│   │   │   │   └── Video.java
│   │   │   ├── repository/
│   │   │   │   └── VideoRepository.java
│   │   │   └── service/
│   │   │       └── VideoService.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── templates/
│   │           └── index.html
│   └── test/
│       └── java/com/playlist/
│           ├── model/
│           │   └── VideoTest.java
│           └── service/
│               └── VideoServiceTest.java
├── Jenkinsfile
├── deploy.sh
├── deploy.bat
├── pom.xml
└── README.md
```

## 🚀 Instalación y Ejecución

### Prerrequisitos

- Java 17 o superior
- Maven 3.6+
- Git


## 🧪 Tests

El proyecto incluye tests unitarios para:
- Modelo `Video`
- Servicio `VideoService`

Ejecutar tests:
```bash
mvn test
```

Ver reporte de tests:
```bash
mvn surefire-report:report
```

## 📝 API Endpoints

### Videos

- `GET /api/videos` - Obtener todos los videos
- `GET /api/videos/{id}` - Obtener un video por ID
- `POST /api/videos` - Agregar nuevo video
  ```json
  {
    "nombre": "Nombre de la canción",
    "url": "https://www.youtube.com/watch?v=..."
  }
  ```
- `DELETE /api/videos/{id}` - Eliminar video
- `POST /api/videos/{id}/like` - Dar like a un video
- `POST /api/videos/{id}/favorito` - Marcar/desmarcar favorito
- `GET /api/videos/favoritos` - Obtener videos favoritos

## 🐛 Code Smells y Refactoring

### Code Smell Identificado: **Long Method**

**Ubicación**: `Video.java` - método `getEmbedUrl()`

**Problema**: El método hace demasiadas cosas (procesar URLs de YouTube, Vimeo y Dailymotion)

**Técnica de Refactoring**: **Extract Method**

**Solución**: Dividir en métodos más pequeños, uno por cada plataforma.

Ver archivo `REFACTORING.md` para más detalles.

## 👥 Autores

[Tus nombres aquí]

## 📄 Licencia

Este proyecto fue desarrollado como entregable del curso de Programación Avanzada 2025.
