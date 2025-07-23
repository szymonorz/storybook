FROM maven:3.8.6-eclipse-temurin-17 AS builder
WORKDIR /app    
COPY pom.xml .

COPY . .
#RUN mvn dependency:go-offline       
RUN ./mvnw clean package -DskipTests    

FROM eclipse-temurin:17.0.2_8-jre    
WORKDIR /app    
COPY --from=builder /app/target/*.jar app.jar    
EXPOSE 8080    
ENTRYPOINT ["java", "-jar", "app.jar"]