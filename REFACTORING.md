# 🔧 Refactoring - Code Smell: Long Method

## 📍 Ubicación del Code Smell

**Archivo**: `src/main/java/com/playlist/model/Video.java`  
**Método**: `getEmbedUrl()`  
**Líneas**: 48-81

## 🐛 Descripción del Code Smell

### Tipo: **Long Method** (Método Largo)

El método `getEmbedUrl()` tiene las siguientes características problemáticas:

1. **Múltiples responsabilidades**: Maneja la conversión de URLs para 4 plataformas diferentes (YouTube estándar, YouTube corto, Vimeo, Dailymotion)
2. **Difícil de leer**: Con ~33 líneas de código, es difícil entender qué hace a primera vista
3. **Difícil de mantener**: Agregar soporte para una nueva plataforma requiere modificar un método ya complejo
4. **Viola el Principio de Responsabilidad Única (SRP)**: Un método debería hacer una sola cosa

### Código Original (CON code smell)

```java
public String getEmbedUrl() {
    String embedUrl = this.url;
    
    // Procesar URL de YouTube
    if (embedUrl.contains("youtube.com/watch?v=")) {
        String videoId = embedUrl.substring(embedUrl.indexOf("watch?v=") + 8);
        if (videoId.contains("&")) {
            videoId = videoId.substring(0, videoId.indexOf("&"));
        }
        embedUrl = "https://www.youtube.com/embed/" + videoId;
    } else if (embedUrl.contains("youtu.be/")) {
        String videoId = embedUrl.substring(embedUrl.indexOf("youtu.be/") + 9);
        if (videoId.contains("?")) {
            videoId = videoId.substring(0, videoId.indexOf("?"));
        }
        embedUrl = "https://www.youtube.com/embed/" + videoId;
    }
    
    // Procesar URL de Vimeo
    if (embedUrl.contains("vimeo.com/")) {
        String videoId = embedUrl.substring(embedUrl.lastIndexOf("/") + 1);
        if (videoId.contains("?")) {
            videoId = videoId.substring(0, videoId.indexOf("?"));
        }
        embedUrl = "https://player.vimeo.com/video/" + videoId;
    }
    
    // Procesar URL de Dailymotion
    if (embedUrl.contains("dailymotion.com/video/")) {
        String videoId = embedUrl.substring(embedUrl.indexOf("video/") + 6);
        if (videoId.contains("?")) {
            videoId = videoId.substring(0, videoId.indexOf("?"));
        }
        if (videoId.contains("_")) {
            videoId = videoId.substring(0, videoId.indexOf("_"));
        }
        embedUrl = "https://www.dailymotion.com/embed/video/" + videoId;
    }
    
    return embedUrl;
}
```

## 🛠️ Técnica de Refactoring Aplicada

### **Extract Method** (Extraer Método)

Esta técnica consiste en tomar fragmentos de código que tienen una responsabilidad clara y convertirlos en métodos separados con nombres descriptivos.

### Beneficios del Extract Method:
- ✅ Mejora la legibilidad del código
- ✅ Facilita el mantenimiento
- ✅ Permite reutilizar código
- ✅ Facilita el testing de cada plataforma por separado
- ✅ Sigue el principio DRY (Don't Repeat Yourself)

## ✨ Código Refactorizado (SIN code smell)

```java
public String getEmbedUrl() {
    String embedUrl = this.url;
    
    embedUrl = convertYouTubeUrl(embedUrl);
    embedUrl = convertVimeoUrl(embedUrl);
    embedUrl = convertDailymotionUrl(embedUrl);
    
    return embedUrl;
}

/**
 * Convierte URLs de YouTube al formato embed
 * Soporta: youtube.com/watch?v=... y youtu.be/...
 */
private String convertYouTubeUrl(String url) {
    if (url.contains("youtube.com/watch?v=")) {
        return convertYouTubeStandardUrl(url);
    } else if (url.contains("youtu.be/")) {
        return convertYouTubeShortUrl(url);
    }
    return url;
}

private String convertYouTubeStandardUrl(String url) {
    String videoId = extractSubstring(url, "watch?v=", 8);
    videoId = removeParametersAfter(videoId, "&");
    return "https://www.youtube.com/embed/" + videoId;
}

private String convertYouTubeShortUrl(String url) {
    String videoId = extractSubstring(url, "youtu.be/", 9);
    videoId = removeParametersAfter(videoId, "?");
    return "https://www.youtube.com/embed/" + videoId;
}

/**
 * Convierte URLs de Vimeo al formato embed
 */
private String convertVimeoUrl(String url) {
    if (!url.contains("vimeo.com/")) {
        return url;
    }
    
    String videoId = url.substring(url.lastIndexOf("/") + 1);
    videoId = removeParametersAfter(videoId, "?");
    return "https://player.vimeo.com/video/" + videoId;
}

/**
 * Convierte URLs de Dailymotion al formato embed
 */
private String convertDailymotionUrl(String url) {
    if (!url.contains("dailymotion.com/video/")) {
        return url;
    }
    
    String videoId = extractSubstring(url, "video/", 6);
    videoId = removeParametersAfter(videoId, "?");
    videoId = removeParametersAfter(videoId, "_");
    return "https://www.dailymotion.com/embed/video/" + videoId;
}

/**
 * Método helper para extraer substring
 */
private String extractSubstring(String url, String marker, int offset) {
    int startIndex = url.indexOf(marker);
    if (startIndex == -1) {
        return url;
    }
    return url.substring(startIndex + offset);
}

/**
 * Método helper para remover parámetros después de un delimitador
 */
private String removeParametersAfter(String str, String delimiter) {
    if (str.contains(delimiter)) {
        return str.substring(0, str.indexOf(delimiter));
    }
    return str;
}
```

## 📊 Comparación: Antes vs Después

| Aspecto | Antes (Con Code Smell) | Después (Refactorizado) |
|---------|------------------------|-------------------------|
| **Líneas de código en método principal** | 33 líneas | 7 líneas |
| **Responsabilidades** | 4 plataformas en 1 método | 1 método orquestador + métodos específicos |
| **Legibilidad** | Baja (código anidado) | Alta (nombres descriptivos) |
| **Mantenibilidad** | Difícil | Fácil |
| **Testability** | Difícil testear cada caso | Cada método es testeable |
| **Extensibilidad** | Requiere modificar método grande | Agregar nuevo método pequeño |

## 🎯 Instrucciones para Aplicar el Refactoring

### Paso 1: Identificar el problema
Abrir `Video.java` y ubicar el método `getEmbedUrl()`

### Paso 2: Ejecutar tests actuales
```bash
mvn test
```
Asegurarse de que todos los tests pasan ANTES del refactoring.

### Paso 3: Aplicar Extract Method
Reemplazar el método `getEmbedUrl()` y agregar los nuevos métodos privados.

### Paso 4: Ejecutar tests nuevamente
```bash
mvn test
```
Verificar que todos los tests siguen pasando DESPUÉS del refactoring.

### Paso 5: Commit y Push
```bash
git add src/main/java/com/playlist/model/Video.java
git commit -m "Refactor: Aplicar Extract Method a getEmbedUrl() para resolver Long Method code smell"
git push
```

### Paso 6: Ejecutar Pipeline de Jenkins
El pipeline automáticamente:
1. Detectará el nuevo commit
2. Compilará el código
3. Ejecutará los tests
4. Desplegará la nueva versión

## 🎓 Aprendizajes

1. **Code smells son indicadores, no errores**: El código funcionaba correctamente, pero era difícil de mantener
2. **Refactoring mejora la calidad sin cambiar funcionalidad**: Los tests pasaron antes y después
3. **Nombres descriptivos importan**: `convertYouTubeUrl()` es más claro que "Bloque de código que procesa YouTube"
4. **Métodos pequeños son más fáciles de entender y testear**
5. **CI/CD asegura que el refactoring no rompa nada**: Los tests automáticos dan confianza

## 📚 Referencias

- Fowler, M. (2018). *Refactoring: Improving the Design of Existing Code*
- Clean Code principles: Single Responsibility Principle (SRP)
- Code Smell: Long Method - https://refactoring.guru/smells/long-method
