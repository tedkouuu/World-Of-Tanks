FROM openjdk:17-jdk-alpine

WORKDIR /app

# Копиране на скрипта за изчакване
COPY wait-for.sh ./wait-for.sh

# Задаване на права за изпълнение
RUN chmod +x ./wait-for.sh

# Копиране на JAR файла
COPY target/World_Of_Tanks-0.0.1-SNAPSHOT.jar app.jar

# Стартиране със забавяне
ENTRYPOINT ["./wait-for.sh", "mysql", "3306", "--", "java", "-jar", "app.jar"]