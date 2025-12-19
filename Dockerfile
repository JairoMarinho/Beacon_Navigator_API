# ====================
# Backend Dockerfile
# ====================

# Stage 1: Build
FROM maven:3.9-eclipse-temurin-21-alpine AS build

WORKDIR /app

# Copiar arquivos Maven
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

# Baixar dependências (layer de cache)
RUN ./mvnw dependency:go-offline -B

# Copiar código fonte
COPY src ./src

# Build do projeto (pula testes para acelerar)
RUN ./mvnw clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Criar diretório para uploads
RUN mkdir -p /app/uploads && chmod 777 /app/uploads

# Copiar JAR da stage de build
COPY --from=build /app/target/*.jar app.jar

# Variáveis de ambiente padrão
ENV SPRING_PROFILES_ACTIVE=prod \
    DB_URL=jdbc:mysql://mysql:3306/beacon_navigator \
    DB_USER=root \
    DB_PASSWORD=root \
    JWT_SECRET=CHANGE_ME_IN_PRODUCTION_min32chars_123456 \
    JWT_EXP_MINUTES=120 \
    PORT=8080

# Expor porta
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Comando de execução
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]