# Используем базовый образ с Java 21
FROM eclipse-temurin:21-jdk-jammy

# Рабочая директория
WORKDIR /app

# Копируем собранный JAR-файл
COPY build/libs/*.jar app.jar

# Порт, который будет использовать приложение
EXPOSE 8080

# Команда для запуска приложения
ENTRYPOINT ["java", "-jar", "app.jar"]