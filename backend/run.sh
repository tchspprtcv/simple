#!/bin/bash

echo "Iniciando a aplicação Simple Backend..."

# Verifica se o Java está instalado
if ! command -v java &> /dev/null; then
    echo "Java não encontrado. Por favor, instale o Java 17 ou superior."
    exit 1
fi

# Verifica a versão do Java
java_version=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}')
echo "Versão do Java: $java_version"

# Compila o projeto se o jar não existir
if [ ! -f "target/simple-0.0.1-SNAPSHOT.jar" ]; then
    echo "Compilando o projeto..."
    ./mvnw clean package -DskipTests
fi

# Inicia a aplicação
echo "Iniciando o servidor..."
java -jar target/simple-0.0.1-SNAPSHOT.jar
