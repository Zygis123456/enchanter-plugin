# Multi-stage build for Minecraft plugin
FROM maven:3.8.4-openjdk-17 AS builder

# Set working directory
WORKDIR /app

# Copy pom.xml first for better caching
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# Build the plugin
RUN mvn clean package

# Runtime stage
FROM alpine:latest

# Install Java runtime
RUN apk add --no-cache openjdk17-jre

# Copy the built JAR
COPY --from=builder /app/target/*.jar /plugins/

# Create output directory
VOLUME ["/output"]

# Copy JAR to output when container runs
CMD cp /plugins/*.jar /output/ && echo "Plugin JAR copied to /output/"