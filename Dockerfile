FROM amazoncorretto:17-alpine

# Metadatos
LABEL maintainer="mi-playlist"
LABEL description="Aplicación Mi Playlist Musical"

# Instalar wget para healthcheck (Alpine usa apk, no apt-get)
RUN apk add --no-cache wget

# Directorio de trabajo
WORKDIR /app

# Copiar el JAR de la aplicación
COPY target/mi-playlist.jar app.jar

# Exponer puerto
EXPOSE 8080

# Variables de entorno
ENV JAVA_OPTS="-Xms256m -Xmx512m"

# Comando para ejecutar la aplicación
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

# Healthcheck
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD wget --quiet --tries=1 --spider http://localhost:8080 || exit 1