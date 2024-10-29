# Etapa de construção
FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app

# Copia o arquivo de configuração do Maven e as dependências para o cache
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia o código-fonte para o container e compila o projeto
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa de execução
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copia o JAR gerado na etapa de construção para a etapa final
COPY --from=build /app/target/*.jar app.jar

# Expor a porta padrão do Spring Boot
EXPOSE 8081

# Comando para rodar o JAR
ENTRYPOINT ["java", "-jar", "app.jar"]
