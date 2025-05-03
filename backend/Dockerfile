FROM openjdk:17-jdk-slim

WORKDIR /app

# Copiar o arquivo pom.xml e baixar as dependências
COPY backend/pom.xml .
RUN mkdir -p src/main/java
RUN mkdir -p src/main/resources

# Copiar o código-fonte
COPY backend/src ./src

# Instalar Maven
RUN apt-get update && apt-get install -y maven

# Compilar a aplicação
RUN mvn package -DskipTests

# Copiar o arquivo JAR para um local conhecido
RUN mv target/*.jar app.jar

# Expor a porta que o Spring Boot usa
EXPOSE 8080

# Comando para executar a aplicação
CMD ["java", "-jar", "app.jar"]
