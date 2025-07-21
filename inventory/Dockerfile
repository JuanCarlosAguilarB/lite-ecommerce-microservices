# ======================
# 1. Stage de construcción
# ======================
FROM eclipse-temurin:21-jdk AS builder

# Instalar dependencias necesarias
RUN apt-get update && apt-get install -y bash && rm -rf /var/lib/apt/lists/*

# Definir el directorio de trabajo
WORKDIR /app

# Copiar los archivos Gradle primero
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# Dar permisos al gradlew
RUN chmod +x gradlew

# Descargar dependencias sin compilar el código (mejora caching)
RUN ./gradlew dependencies --no-daemon || return 0

# Copiar el resto del código fuente
COPY src src

# Compilar y generar el JAR
RUN ./gradlew clean bootJar --no-daemon

# ======================
# 2. Stage de runtime
# ======================
FROM eclipse-temurin:21-jdk AS runtime

# Directorio de la app
WORKDIR /app

# Copiar el JAR desde el stage de builder
COPY --from=builder /app/build/libs/*.jar app.jar

# Exponer el puerto de la app
EXPOSE 8080

# Comando de inicio
ENTRYPOINT ["java", "-jar", "app.jar"]
