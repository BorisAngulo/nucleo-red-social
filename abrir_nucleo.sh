#!/bin/bash
# Compilar e iniciar el proyecto del grupo con la rama-adrian
mkdir -p out
javac -encoding UTF-8 -d out $(find src/main/java -name "*.java")
java -cp out org.example.interfaz.RedSocialApp

