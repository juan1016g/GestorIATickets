# Etapa 1: Build de la aplicación
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app

# Copiar archivos de configuración de Gradle
COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./

# Dar permisos de ejecución al wrapper
RUN chmod +x ./gradlew

# Descargar dependencias en caché
RUN ./gradlew dependencies --no-daemon || true

# Copiar el código fuente y compilar excluyendo tests (para agilizar el deploy)
COPY src src
RUN ./gradlew bootJar --no-daemon -x test

# Etapa 2: Imagen ligera de producción
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Crear usuario sin privilegios por seguridad
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copiar el artefacto generado desde la etapa de compilación
COPY --from=builder /app/build/libs/*.jar app.jar

# Variable de entorno para optimizar memoria en planes gratuitos de Render
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# Render asigna dinámicamente la variable PORT
EXPOSE 8080

# Iniciar la aplicación pasando el perfil de producción
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Dserver.port=${PORT:-8080} -Dspring.profiles.active=prod -jar app.jar"]