#!/bin/bash
echo "Compilando o projeto..."
mvn clean compile -DskipTests -Dmaven.test.skip=true -Dmaven.wagon.http.ssl.insecure=true -Dmaven.wagon.http.ssl.allowall=true -Dmaven.wagon.http.ssl.ignore.validity.dates=true
echo "Compilação concluída!"
