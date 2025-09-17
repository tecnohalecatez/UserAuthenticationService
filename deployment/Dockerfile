# Multi-stage build for better security and smaller image size
FROM eclipse-temurin:21-jdk-alpine AS builder

# Set working directory
WORKDIR /workspace/app

# Copy gradle wrapper and build files first for better layer caching
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY main.gradle .
COPY settings.gradle .
COPY gradle.properties .
COPY lombok.config .

# Copy source code
COPY domain domain
COPY infrastructure infrastructure
COPY applications applications

# Make gradlew executable
RUN chmod +x ./gradlew

# Build the application
RUN ./gradlew clean bootJar -x test --no-daemon

# Runtime stage - smaller base image
FROM eclipse-temurin:21-jre-alpine AS runtime

# Install security updates and required packages
RUN apk update && \
    apk upgrade && \
    apk add --no-cache \
    curl \
    dumb-init && \
    rm -rf /var/cache/apk/*

# Create non-root user with specific UID/GID
RUN addgroup -g 1001 -S appuser && \
    adduser -u 1001 -S appuser -G appuser

# Create app directory with proper permissions
RUN mkdir -p /app/tmp /app/logs && \
    chown -R appuser:appuser /app

# Set working directory
WORKDIR /app

# Copy JAR from builder stage
COPY --from=builder --chown=appuser:appuser /workspace/app/applications/app-service/build/libs/UserAuthenticationService.jar app.jar

# Create cache directory with proper permissions
RUN mkdir -p /cache && \
    chown -R appuser:appuser /cache

# Switch to non-root user
USER appuser

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# Set JVM options for containerized environment
ENV JAVA_OPTS="-XX:+UseContainerSupport \
    -XX:MaxRAMPercentage=75.0 \
    -XX:+UseG1GC \
    -XX:+UseStringDeduplication \
    -XX:+OptimizeStringConcat \
    -Djava.security.egd=file:/dev/./urandom \
    -Djava.awt.headless=true \
    -Dfile.encoding=UTF-8 \
    -Duser.timezone=UTC \
    -Dspring.backgroundpreinitializer.ignore=true"

# Security options
ENV JAVA_SECURITY_OPTS="-Djava.security.policy=all.policy \
    -Dcom.sun.management.jmxremote=false"

# Application port
EXPOSE 8989

# Use dumb-init to handle signals properly
ENTRYPOINT ["dumb-init", "--"]

# Start the application
CMD ["sh", "-c", "java $JAVA_OPTS $JAVA_SECURITY_OPTS -jar app.jar"]